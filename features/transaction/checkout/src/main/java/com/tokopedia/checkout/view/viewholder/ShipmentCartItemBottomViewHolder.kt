package com.tokopedia.checkout.view.viewholder

import android.content.Context
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.TypedValue
import android.view.View
import android.widget.CompoundButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.checkout.R
import com.tokopedia.checkout.analytics.CheckoutScheduleDeliveryAnalytics
import com.tokopedia.checkout.databinding.ItemShipmentGroupFooterBinding
import com.tokopedia.checkout.domain.mapper.ShipmentMapper
import com.tokopedia.checkout.view.ShipmentAdapterActionListener
import com.tokopedia.checkout.view.converter.RatesDataConverter
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.setTextAndContentDescription
import com.tokopedia.logisticCommon.data.constant.InsuranceConstant
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.logisticcart.shipping.features.shippingwidget.ShippingWidget
import com.tokopedia.logisticcart.shipping.model.CourierItemData
import com.tokopedia.logisticcart.shipping.model.ScheduleDeliveryUiModel
import com.tokopedia.logisticcart.shipping.model.ShipmentCartItemModel
import com.tokopedia.logisticcart.shipping.model.ShipmentDetailData
import com.tokopedia.purchase_platform.common.feature.bottomsheet.GeneralBottomSheet
import com.tokopedia.purchase_platform.common.feature.bottomsheet.InsuranceBottomSheet
import com.tokopedia.purchase_platform.common.feature.gifting.view.ButtonGiftingAddOnView
import com.tokopedia.purchase_platform.common.prefs.PlusCoachmarkPrefs
import com.tokopedia.purchase_platform.common.utils.removeDecimalSuffix
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.currency.CurrencyFormatUtil
import rx.Observable
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subjects.PublishSubject
import rx.subscriptions.CompositeSubscription
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern

class ShipmentCartItemBottomViewHolder(
    itemView: View,
    private val ratesDataConverter: RatesDataConverter,
    private val listener: Listener? = null,
    private val actionListener: ShipmentAdapterActionListener? = null
) : RecyclerView.ViewHolder(itemView), ShippingWidget.ShippingWidgetListener {

    companion object {

        @JvmField
        val LAYOUT = R.layout.item_shipment_group_footer

        private const val VIEW_ALPHA_ENABLED = 1.0f
        private const val VIEW_ALPHA_DISABLED = 0.5f
        private const val DROPSHIPPER_MIN_NAME_LENGTH = 3
        private const val DROPSHIPPER_MAX_NAME_LENGTH = 100
        private const val DROPSHIPPER_MIN_PHONE_LENGTH = 6
        private const val DROPSHIPPER_MAX_PHONE_LENGTH = 20
        private const val PHONE_NUMBER_REGEX_PATTERN = "\\d+"
        private const val SHIPPING_SAVE_STATE_TYPE_TRADE_IN_DROP_OFF = 1
        private const val SHIPPING_SAVE_STATE_TYPE_SHIPPING_EXPERIENCE = 2
    }

    private val binding: ItemShipmentGroupFooterBinding =
        ItemShipmentGroupFooterBinding.bind(itemView)
    private val plusCoachmarkPrefs: PlusCoachmarkPrefs by lazy {
        PlusCoachmarkPrefs(itemView.context)
    }
    private val phoneNumberRegexPattern: Pattern = Pattern.compile(PHONE_NUMBER_REGEX_PATTERN)
    private val compositeSubscription: CompositeSubscription = CompositeSubscription()
    private var saveStateDebounceListener: SaveStateDebounceListener? = null

    private var scheduleDeliveryDonePublisher: PublishSubject<Boolean>? = null

    init {
        initSaveStateDebouncer()
    }

    fun bind(
        shipmentCartItemModel: ShipmentCartItemModel,
        recipientAddress: RecipientAddressModel?
    ) {
        if (recipientAddress != null) {
            renderShipping(shipmentCartItemModel, recipientAddress, ratesDataConverter)
        }
        renderInsurance(shipmentCartItemModel)
        val isCornerAddress = recipientAddress != null && recipientAddress.isCornerAddress
        renderDropshipper(shipmentCartItemModel, isCornerAddress)
        renderCostDetail(shipmentCartItemModel)
        renderError(shipmentCartItemModel)
        renderShippingVibrationAnimation(shipmentCartItemModel)
        renderAddOnOrderLevel(shipmentCartItemModel)
    }

    private fun renderShipping(
        shipmentCartItemModel: ShipmentCartItemModel,
        currentAddress: RecipientAddressModel,
        ratesDataConverter: RatesDataConverter
    ) {
        binding.shippingWidget.setupListener(this@ShipmentCartItemBottomViewHolder)
        binding.shippingWidget.hideTradeInShippingInfo()
        if (shipmentCartItemModel.isError) {
            renderErrorCourierState(shipmentCartItemModel)
            return
        }
        var selectedCourierItemData: CourierItemData? = null
        val isTradeInDropOff = actionListener?.isTradeInByDropOff ?: false
        if (shipmentCartItemModel.selectedShipmentDetailData != null) {
            if (isTradeInDropOff && shipmentCartItemModel.selectedShipmentDetailData?.selectedCourierTradeInDropOff != null) {
                selectedCourierItemData =
                    shipmentCartItemModel.selectedShipmentDetailData?.selectedCourierTradeInDropOff
            } else if (!isTradeInDropOff && shipmentCartItemModel.selectedShipmentDetailData?.selectedCourier != null) {
                selectedCourierItemData =
                    shipmentCartItemModel.selectedShipmentDetailData?.selectedCourier
            }
        }
        if (selectedCourierItemData != null) {
            if (shipmentCartItemModel.isStateLoadingCourierState) {
                // Has select shipping, but still loading
                renderLoadingCourierState()
                return
            }
            // Has select shipping
            renderSelectedCourier(shipmentCartItemModel, currentAddress, selectedCourierItemData)
        } else {
            // Has not select shipping
            prepareLoadCourierState(
                shipmentCartItemModel,
                currentAddress,
                ratesDataConverter,
                isTradeInDropOff
            )
        }
    }

    private fun renderErrorCourierState(shipmentCartItemModel: ShipmentCartItemModel) {
        binding.containerShippingOptions.root.visibility = View.VISIBLE
        binding.shippingWidget.renderErrorCourierState(shipmentCartItemModel)
    }

    private fun renderSelectedCourier(
        shipmentCartItemModel: ShipmentCartItemModel,
        currentAddress: RecipientAddressModel,
        selectedCourierItemData: CourierItemData
    ) {
        binding.containerShippingOptions.root.visibility = View.VISIBLE
        binding.shippingWidget.showContainerShippingExperience()
        if (shipmentCartItemModel.isShowScheduleDelivery) {
            sendScheduleDeliveryAnalytics(shipmentCartItemModel, selectedCourierItemData)
            // Show Schedule delivery widget
            binding.shippingWidget.renderScheduleDeliveryWidget(
                shipmentCartItemModel,
                selectedCourierItemData
            )
        } else if (shipmentCartItemModel.isDisableChangeCourier) {
            // Is single shipping only
            binding.shippingWidget.renderSingleShippingCourier(
                shipmentCartItemModel,
                selectedCourierItemData
            )
        } else if (shipmentCartItemModel.voucherLogisticItemUiModel != null) {
            // Is free ongkir shipping
            renderFreeShippingCourier(
                shipmentCartItemModel,
                currentAddress,
                selectedCourierItemData
            )
        } else if (shipmentCartItemModel.isHideChangeCourierCard) {
            // normal shipping but not show `pilih kurir` card
            binding.shippingWidget.renderNormalShippingWithoutChooseCourierCard(
                shipmentCartItemModel,
                currentAddress,
                selectedCourierItemData
            )
        } else {
            // Is normal shipping
            binding.shippingWidget.renderNormalShippingCourier(
                shipmentCartItemModel,
                currentAddress,
                selectedCourierItemData
            )
        }
        showMultiplePlusOrderCoachmark(
            shipmentCartItemModel,
            binding.shippingWidget.containerShippingExperience
        )
    }

    private fun renderFreeShippingCourier(
        shipmentCartItemModel: ShipmentCartItemModel,
        currentAddress: RecipientAddressModel,
        selectedCourierItemData: CourierItemData
    ) {
        binding.shippingWidget.showLayoutFreeShippingCourier(shipmentCartItemModel, currentAddress)
        if (shipmentCartItemModel.isError) {
            actionListener?.onCancelVoucherLogisticClicked(
                shipmentCartItemModel.voucherLogisticItemUiModel!!.code,
                shipmentCartItemModel.voucherLogisticItemUiModel!!.uniqueId,
                bindingAdapterPosition,
                shipmentCartItemModel
            )
        }
        binding.shippingWidget.renderFreeShippingCourier(selectedCourierItemData)
    }

    private fun sendScheduleDeliveryAnalytics(
        shipmentCartItemModel: ShipmentCartItemModel,
        selectedCourierItemData: CourierItemData
    ) {
        if (!shipmentCartItemModel.hasSentScheduleDeliveryAnalytics) {
            CheckoutScheduleDeliveryAnalytics.sendViewScheduledDeliveryWidgetOnTokopediaNowEvent()
            if (selectedCourierItemData.scheduleDeliveryUiModel?.available == false) {
                CheckoutScheduleDeliveryAnalytics.sendViewUnavailableScheduledDeliveryEvent()
            }
            shipmentCartItemModel.hasSentScheduleDeliveryAnalytics = true
        }
    }

    private fun prepareLoadCourierState(
        shipmentCartItemModel: ShipmentCartItemModel,
        currentAddress: RecipientAddressModel,
        ratesDataConverter: RatesDataConverter,
        isTradeInDropOff: Boolean
    ) {
        binding.shippingWidget.prepareLoadCourierState()
        binding.containerShippingOptions.root.visibility = View.GONE
        if (isTradeInDropOff) {
            renderNoSelectedCourier(
                shipmentCartItemModel,
                currentAddress,
                SHIPPING_SAVE_STATE_TYPE_TRADE_IN_DROP_OFF
            )
            loadCourierState(
                shipmentCartItemModel,
                currentAddress,
                ratesDataConverter,
                SHIPPING_SAVE_STATE_TYPE_TRADE_IN_DROP_OFF
            )
        } else {
            renderNoSelectedCourier(
                shipmentCartItemModel,
                currentAddress,
                SHIPPING_SAVE_STATE_TYPE_SHIPPING_EXPERIENCE
            )
            loadCourierState(
                shipmentCartItemModel,
                currentAddress,
                ratesDataConverter,
                SHIPPING_SAVE_STATE_TYPE_SHIPPING_EXPERIENCE
            )
        }
    }

    private fun renderInsurance(shipmentCartItemModel: ShipmentCartItemModel) {
        with(binding.containerShippingOptions) {
            var renderInsurance = false
            val isTradeInDropOff = actionListener?.isTradeInByDropOff ?: false
            val selectedShipmentDetailData = shipmentCartItemModel.selectedShipmentDetailData
            if (selectedShipmentDetailData != null) {
                renderInsurance = if (isTradeInDropOff) {
                    selectedShipmentDetailData.selectedCourierTradeInDropOff != null
                } else {
                    selectedShipmentDetailData.selectedCourier != null
                }
            }
            if (bindingAdapterPosition != RecyclerView.NO_POSITION && renderInsurance) {
                cbInsurance.setOnCheckedChangeListener { _: CompoundButton?, checked: Boolean ->
                    if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                        selectedShipmentDetailData?.useInsurance = checked
                        if (checked) {
                            actionListener?.onInsuranceCheckedForTrackingAnalytics()
                        }
                        actionListener?.onInsuranceChecked(bindingAdapterPosition)
                        saveStateDebounceListener?.onNeedToSaveState(shipmentCartItemModel)
                    }
                }
                val useInsurance = selectedShipmentDetailData?.useInsurance
                if (useInsurance != null) {
                    cbInsurance.isChecked = useInsurance
                }
                val courierItemData: CourierItemData? = if (isTradeInDropOff) {
                    selectedShipmentDetailData!!.selectedCourierTradeInDropOff
                } else {
                    selectedShipmentDetailData!!.selectedCourier
                }
                val selectedShipper = courierItemData!!.selectedShipper
                if (selectedShipper.insuranceType == InsuranceConstant.INSURANCE_TYPE_MUST) {
                    llInsurance.visibility = View.VISIBLE
                    llInsurance.background = null
                    llInsurance.setOnClickListener(null)
                    tvLabelInsurance.setText(com.tokopedia.purchase_platform.common.R.string.label_must_insurance)
                    cbInsurance.isEnabled = false
                    cbInsurance.isChecked = true
                    if (useInsurance == null) {
                        selectedShipmentDetailData.useInsurance = true
                        actionListener?.onInsuranceChecked(bindingAdapterPosition)
                    }
                } else if (selectedShipper.insuranceType == InsuranceConstant.INSURANCE_TYPE_NO || selectedShipper.insuranceType == InsuranceConstant.INSURANCE_TYPE_NONE) {
                    cbInsurance.isEnabled = true
                    cbInsurance.isChecked = false
                    llInsurance.visibility = View.GONE
                    llInsurance.background = null
                    llInsurance.setOnClickListener(null)
                    selectedShipmentDetailData.useInsurance = false
                } else if (selectedShipper.insuranceType == InsuranceConstant.INSURANCE_TYPE_OPTIONAL) {
                    tvLabelInsurance.setText(com.tokopedia.purchase_platform.common.R.string.label_shipment_insurance)
                    llInsurance.visibility = View.VISIBLE
                    cbInsurance.isEnabled = true
                    val outValue = TypedValue()
                    llInsurance.context.theme.resolveAttribute(
                        android.R.attr.selectableItemBackground,
                        outValue,
                        true
                    )
                    llInsurance.setBackgroundResource(outValue.resourceId)
                    llInsurance.setOnClickListener {
                        if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                            binding.containerShippingOptions.cbInsurance.isChecked =
                                !binding.containerShippingOptions.cbInsurance.isChecked
                            actionListener?.onInsuranceChecked(bindingAdapterPosition)
                        }
                    }
                    if (useInsurance == null) {
                        if (selectedShipper.insuranceUsedDefault == InsuranceConstant.INSURANCE_USED_DEFAULT_YES) {
                            cbInsurance.isChecked = true
                            selectedShipmentDetailData.useInsurance = true
                            actionListener?.onInsuranceChecked(bindingAdapterPosition)
                        } else if (selectedShipper.insuranceUsedDefault == InsuranceConstant.INSURANCE_USED_DEFAULT_NO) {
                            cbInsurance.isChecked = shipmentCartItemModel.isInsurance
                            selectedShipmentDetailData.useInsurance =
                                shipmentCartItemModel.isInsurance
                        }
                    }
                }
                if (!TextUtils.isEmpty(selectedShipper.insuranceUsedInfo)) {
                    if (TextUtils.isEmpty(selectedShipper.insuranceUsedInfo)) {
                        imgInsuranceInfo.visibility = View.GONE
                    } else {
                        imgInsuranceInfo.visibility = View.VISIBLE
                        imgInsuranceInfo.setOnClickListener {
                            actionListener?.onInsuranceInfoTooltipClickedTrackingAnalytics()
                            showInsuranceBottomSheet(
                                imgInsuranceInfo.context,
                                imgInsuranceInfo.context.getString(com.tokopedia.purchase_platform.common.R.string.title_bottomsheet_insurance),
                                selectedShipper.insuranceUsedInfo!!
                            )
                        }
                    }
                }
            }
        }
    }

    private fun showInsuranceBottomSheet(context: Context, title: String, message: String) {
        val insuranceBottomSheet = InsuranceBottomSheet()
        insuranceBottomSheet.setDesc(message)
        actionListener?.currentFragmentManager?.let {
            insuranceBottomSheet.show(title, context, it)
        }
    }

    private fun renderDropshipper(shipmentCartItemModel: ShipmentCartItemModel, isCorner: Boolean) {
        with(binding.containerShippingOptions) {
            val isTradeInDropOff = actionListener?.isTradeInByDropOff ?: false
            var courierItemData: CourierItemData? = null
            val selectedShipmentDetailData = shipmentCartItemModel.selectedShipmentDetailData
            if (selectedShipmentDetailData != null) {
                courierItemData = if (isTradeInDropOff) {
                    selectedShipmentDetailData.selectedCourierTradeInDropOff
                } else {
                    selectedShipmentDetailData.selectedCourier
                }
            }
            if (selectedShipmentDetailData != null && courierItemData != null) {
                if (shipmentCartItemModel.isDropshipperDisable || !courierItemData.isAllowDropshiper || isCorner) {
                    llDropshipper.visibility = View.GONE
                    llDropshipperInfo.visibility = View.GONE
                    selectedShipmentDetailData.dropshipperName = null
                    selectedShipmentDetailData.dropshipperPhone = null
                    textInputLayoutShipperName.textFieldInput.setText("")
                    textInputLayoutShipperPhone.textFieldInput.setText("")
                } else {
                    llDropshipper.visibility = View.VISIBLE
                }
                cbDropshipper.setOnCheckedChangeListener { compoundButton: CompoundButton, checked: Boolean ->
                    actionListener?.hideSoftKeyboard()
                    if (checked && isHavingPurchaseProtectionChecked(shipmentCartItemModel)) {
                        compoundButton.isChecked = false
                        actionListener?.onPurchaseProtectionLogicError()
                        return@setOnCheckedChangeListener
                    }
                    shipmentCartItemModel.selectedShipmentDetailData!!.useDropshipper = checked
                    if (checked) {
                        textInputLayoutShipperName.textFieldInput.setText(shipmentCartItemModel.dropshiperName)
                        textInputLayoutShipperPhone.textFieldInput.setText(shipmentCartItemModel.dropshiperPhone)
                        shipmentCartItemModel.selectedShipmentDetailData!!.dropshipperName =
                            shipmentCartItemModel.dropshiperName
                        shipmentCartItemModel.selectedShipmentDetailData!!.dropshipperPhone =
                            shipmentCartItemModel.dropshiperPhone
                        llDropshipperInfo.visibility = View.VISIBLE
                        actionListener?.onDropshipCheckedForTrackingAnalytics()
                    } else {
                        textInputLayoutShipperName.textFieldInput.setText("")
                        textInputLayoutShipperPhone.textFieldInput.setText("")
                        shipmentCartItemModel.selectedShipmentDetailData!!.dropshipperName = ""
                        shipmentCartItemModel.selectedShipmentDetailData!!.dropshipperPhone = ""
                        shipmentCartItemModel.dropshiperName = ""
                        shipmentCartItemModel.dropshiperPhone = ""
                        llDropshipperInfo.visibility = View.GONE
                        shipmentCartItemModel.isStateDropshipperHasError = false
                    }
                    actionListener?.onNeedUpdateViewItem(bindingAdapterPosition)
                    actionListener?.onNeedUpdateRequestData()
                    saveStateDebounceListener?.onNeedToSaveState(shipmentCartItemModel)
                }
                val useDropshipper =
                    selectedShipmentDetailData.useDropshipper
                if (useDropshipper != null) {
                    if (useDropshipper) {
                        cbDropshipper.isChecked = true
                    } else {
                        checkDropshipperState(shipmentCartItemModel)
                    }
                } else {
                    checkDropshipperState(shipmentCartItemModel)
                }
                if (shipmentCartItemModel.voucherLogisticItemUiModel != null) {
                    cbDropshipper.isEnabled = false
                    cbDropshipper.isChecked = false
                    llDropshipper.setOnClickListener(null)
                    labelDropshipper.setTextColor(
                        ContextCompat.getColor(
                            itemView.context,
                            com.tokopedia.unifyprinciples.R.color.Unify_NN950_20
                        )
                    )
                    imgDropshipperInfo.setOnClickListener {
                        showBottomSheet(
                            imgDropshipperInfo.context,
                            imgDropshipperInfo.context.getString(R.string.title_dropshipper_army),
                            imgDropshipperInfo.context.getString(R.string.desc_dropshipper_army),
                            R.drawable.checkout_module_ic_dropshipper
                        )
                    }
                } else {
                    cbDropshipper.isEnabled = true
                    labelDropshipper.setTextColor(
                        ContextCompat.getColor(
                            itemView.context,
                            com.tokopedia.unifyprinciples.R.color.Unify_NN950_68
                        )
                    )
                    llDropshipper.setOnClickListener {
                        binding.containerShippingOptions.cbDropshipper.isChecked =
                            !binding.containerShippingOptions.cbDropshipper.isChecked
                    }
                    imgDropshipperInfo.setOnClickListener {
                        showBottomSheet(
                            imgDropshipperInfo.context,
                            imgDropshipperInfo.context.getString(R.string.label_dropshipper_new),
                            imgDropshipperInfo.context.getString(R.string.label_dropshipper_info),
                            R.drawable.checkout_module_ic_dropshipper
                        )
                    }
                }
                textInputLayoutShipperName.textFieldInput.addTextChangedListener(object :
                        TextWatcher {
                        override fun beforeTextChanged(
                            charSequence: CharSequence,
                            i: Int,
                            i1: Int,
                            i2: Int
                        ) {
                        }

                        override fun onTextChanged(
                            charSequence: CharSequence,
                            i: Int,
                            i1: Int,
                            i2: Int
                        ) {
                            if (shipmentCartItemModel.selectedShipmentDetailData != null) {
                                if (!TextUtils.isEmpty(charSequence)) {
                                    shipmentCartItemModel.selectedShipmentDetailData!!.dropshipperName =
                                        charSequence.toString()
                                    validateDropshipperName(shipmentCartItemModel, charSequence, true)
                                    saveStateDebounceListener?.onNeedToSaveState(shipmentCartItemModel)
                                }
                            }
                        }

                        override fun afterTextChanged(editable: Editable) {}
                    })
                if (!TextUtils.isEmpty(selectedShipmentDetailData.dropshipperName) ||
                    !TextUtils.isEmpty(shipmentCartItemModel.dropshiperName)
                ) {
                    textInputLayoutShipperName.textFieldInput.setText(selectedShipmentDetailData.dropshipperName)
                } else {
                    textInputLayoutShipperName.textFieldInput.setText("")
                }
                if (shipmentCartItemModel.isStateDropshipperHasError) {
                    validateDropshipperName(
                        shipmentCartItemModel,
                        textInputLayoutShipperName.textFieldInput.text,
                        true
                    )
                } else {
                    validateDropshipperName(
                        shipmentCartItemModel,
                        textInputLayoutShipperName.textFieldInput.text,
                        false
                    )
                }
                textInputLayoutShipperName.textFieldInput.setSelection(
                    textInputLayoutShipperName.textFieldInput.length()
                )
                textInputLayoutShipperPhone.textFieldInput.addTextChangedListener(object :
                        TextWatcher {
                        override fun beforeTextChanged(
                            charSequence: CharSequence,
                            i: Int,
                            i1: Int,
                            i2: Int
                        ) {
                        }

                        override fun onTextChanged(
                            charSequence: CharSequence,
                            i: Int,
                            i1: Int,
                            i2: Int
                        ) {
                            if (shipmentCartItemModel.selectedShipmentDetailData != null) {
                                if (!TextUtils.isEmpty(charSequence)) {
                                    shipmentCartItemModel.selectedShipmentDetailData!!.dropshipperPhone =
                                        charSequence.toString()
                                    validateDropshipperPhone(shipmentCartItemModel, charSequence, true)
                                    saveStateDebounceListener?.onNeedToSaveState(shipmentCartItemModel)
                                }
                            }
                        }

                        override fun afterTextChanged(editable: Editable) {}
                    })
                if (!TextUtils.isEmpty(selectedShipmentDetailData.dropshipperPhone) ||
                    !TextUtils.isEmpty(shipmentCartItemModel.dropshiperPhone)
                ) {
                    textInputLayoutShipperPhone.textFieldInput.setText(
                        selectedShipmentDetailData.dropshipperPhone
                    )
                } else {
                    textInputLayoutShipperPhone.textFieldInput.setText("")
                }
                if (shipmentCartItemModel.isStateDropshipperHasError) {
                    validateDropshipperPhone(
                        shipmentCartItemModel,
                        textInputLayoutShipperPhone.textFieldInput.text,
                        true
                    )
                } else {
                    validateDropshipperPhone(
                        shipmentCartItemModel,
                        textInputLayoutShipperPhone.textFieldInput.text,
                        false
                    )
                }
                textInputLayoutShipperPhone.textFieldInput.setSelection(
                    textInputLayoutShipperPhone.textFieldInput.length()
                )
            }
        }
    }

    private fun isHavingPurchaseProtectionChecked(shipmentCartItemModel: ShipmentCartItemModel): Boolean {
        return shipmentCartItemModel.cartItemModels.any { it.isProtectionOptIn }
    }

    private fun showBottomSheet(context: Context, title: String, message: String, image: Int) {
        val generalBottomSheet = GeneralBottomSheet()
        generalBottomSheet.setTitle(title)
        generalBottomSheet.setDesc(message)
        generalBottomSheet.setButtonText(context.getString(com.tokopedia.purchase_platform.common.R.string.label_button_bottomsheet_close))
        generalBottomSheet.setIcon(image)
        generalBottomSheet.setButtonOnClickListener { bottomSheetUnify: BottomSheetUnify ->
            bottomSheetUnify.dismiss()
        }
        actionListener?.currentFragmentManager?.let {
            generalBottomSheet.show(context, it)
        }
    }

    private fun checkDropshipperState(shipmentCartItemModel: ShipmentCartItemModel) {
        binding.containerShippingOptions.cbDropshipper.isChecked =
            !TextUtils.isEmpty(shipmentCartItemModel.dropshiperName) ||
            !TextUtils.isEmpty(shipmentCartItemModel.dropshiperPhone)
    }

    private fun validateDropshipperPhone(
        shipmentCartItemModel: ShipmentCartItemModel,
        charSequence: CharSequence,
        fromTextWatcher: Boolean
    ) {
        with(binding.containerShippingOptions) {
            val matcher = phoneNumberRegexPattern.matcher(charSequence)
            if (charSequence.isEmpty() && fromTextWatcher) {
                textInputLayoutShipperPhone.setError(true)
                textInputLayoutShipperPhone.setMessage(
                    textInputLayoutShipperName.context.getString(
                        R.string.message_error_dropshipper_phone_empty
                    )
                )
                shipmentCartItemModel.selectedShipmentDetailData!!.isDropshipperPhoneValid = false
                actionListener?.onDataDisableToCheckout(null)
            } else if (textInputLayoutShipperPhone.textFieldInput.text.isNotEmpty() && !matcher.matches()) {
                textInputLayoutShipperPhone.setError(true)
                textInputLayoutShipperPhone.setMessage(
                    textInputLayoutShipperName.context.getString(
                        R.string.message_error_dropshipper_phone_invalid
                    )
                )
                shipmentCartItemModel.selectedShipmentDetailData!!.isDropshipperPhoneValid = false
                actionListener?.onDataDisableToCheckout(null)
            } else if (textInputLayoutShipperPhone.textFieldInput.text.isNotEmpty() &&
                textInputLayoutShipperPhone.textFieldInput.text.length < DROPSHIPPER_MIN_PHONE_LENGTH
            ) {
                textInputLayoutShipperPhone.setError(true)
                textInputLayoutShipperPhone.setMessage(
                    textInputLayoutShipperName.context.getString(
                        R.string.message_error_dropshipper_phone_min_length
                    )
                )
                shipmentCartItemModel.selectedShipmentDetailData!!.isDropshipperPhoneValid = false
                actionListener?.onDataDisableToCheckout(null)
            } else if (textInputLayoutShipperPhone.textFieldInput.text.isNotEmpty() &&
                textInputLayoutShipperPhone.textFieldInput.text.length > DROPSHIPPER_MAX_PHONE_LENGTH
            ) {
                textInputLayoutShipperPhone.setError(true)
                textInputLayoutShipperPhone.setMessage(
                    textInputLayoutShipperName.context.getString(
                        R.string.message_error_dropshipper_phone_max_length
                    )
                )
                shipmentCartItemModel.selectedShipmentDetailData!!.isDropshipperPhoneValid = false
                actionListener?.onDataDisableToCheckout(null)
            } else {
                textInputLayoutShipperPhone.setError(false)
                textInputLayoutShipperPhone.setMessage("")
                shipmentCartItemModel.selectedShipmentDetailData!!.isDropshipperPhoneValid = true
                actionListener?.onDataEnableToCheckout()
            }
        }
    }

    private fun validateDropshipperName(
        shipmentCartItemModel: ShipmentCartItemModel,
        charSequence: CharSequence,
        fromTextWatcher: Boolean
    ) {
        with(binding.containerShippingOptions) {
            if (charSequence.isEmpty() && fromTextWatcher) {
                textInputLayoutShipperName.setError(true)
                textInputLayoutShipperName.setMessage(textInputLayoutShipperName.context.getString(R.string.message_error_dropshipper_name_empty))
                shipmentCartItemModel.selectedShipmentDetailData!!.isDropshipperNameValid = false
                actionListener?.onDataDisableToCheckout(null)
            } else if (textInputLayoutShipperName.textFieldInput.text.isNotEmpty() &&
                textInputLayoutShipperName.textFieldInput.text.length < DROPSHIPPER_MIN_NAME_LENGTH
            ) {
                textInputLayoutShipperName.setError(true)
                textInputLayoutShipperName.setMessage(textInputLayoutShipperName.context.getString(R.string.message_error_dropshipper_name_min_length))
                shipmentCartItemModel.selectedShipmentDetailData!!.isDropshipperNameValid = false
                actionListener?.onDataDisableToCheckout(null)
            } else if (textInputLayoutShipperName.textFieldInput.text.isNotEmpty() &&
                textInputLayoutShipperName.textFieldInput.text.length > DROPSHIPPER_MAX_NAME_LENGTH
            ) {
                textInputLayoutShipperName.setError(true)
                textInputLayoutShipperName.setMessage(textInputLayoutShipperName.context.getString(R.string.message_error_dropshipper_name_max_length))
                shipmentCartItemModel.selectedShipmentDetailData!!.isDropshipperNameValid = false
                actionListener?.onDataDisableToCheckout(null)
            } else {
                textInputLayoutShipperName.setError(false)
                textInputLayoutShipperName.setMessage("")
                shipmentCartItemModel.selectedShipmentDetailData!!.isDropshipperNameValid = true
                actionListener?.onDataEnableToCheckout()
            }
        }
    }

    private fun renderCostDetail(shipmentCartItemModel: ShipmentCartItemModel) {
        with(binding.containerSubtotal) {
            rlCartSubTotal.visibility = View.VISIBLE
            rlShipmentCost.visibility =
                if (shipmentCartItemModel.isStateDetailSubtotalViewExpanded) View.VISIBLE else View.GONE
            var totalItem = 0
            var totalWeight = 0.0
            var shippingPrice = 0
            var insurancePrice = 0
            var priorityPrice = 0
            var totalPurchaseProtectionPrice: Long = 0
            var totalPurchaseProtectionItem = 0
            var additionalPrice = 0
            var subTotalPrice: Long = 0
            var totalItemPrice: Long = 0
            var totalAddOnPrice = 0
            var hasAddOnSelected = false
            if (shipmentCartItemModel.isStateDetailSubtotalViewExpanded) {
                rlShipmentCost.visibility = View.VISIBLE
                ivDetailOptionChevron.setImage(IconUnify.CHEVRON_UP, null, null, null, null)
            } else {
                rlShipmentCost.visibility = View.GONE
                ivDetailOptionChevron.setImage(IconUnify.CHEVRON_DOWN, null, null, null, null)
            }
            val shippingFeeLabel = tvShippingFee.context.getString(R.string.label_delivery_price)
            for (cartItemModel in shipmentCartItemModel.cartItemModels) {
                if (!cartItemModel.isError) {
                    if (cartItemModel.isBundlingItem) {
                        if (cartItemModel.bundlingItemPosition == ShipmentMapper.BUNDLING_ITEM_HEADER) {
                            totalItemPrice += (cartItemModel.bundlePrice * cartItemModel.bundleQuantity).toLong()
                        }
                    } else {
                        totalItemPrice += (cartItemModel.quantity * cartItemModel.price).toLong()
                    }
                    totalItem += cartItemModel.quantity
                    totalWeight += cartItemModel.weight
                    if (cartItemModel.isProtectionOptIn) {
                        totalPurchaseProtectionItem += cartItemModel.quantity
                        totalPurchaseProtectionPrice += cartItemModel.protectionPrice.toLong()
                    }
                    if (cartItemModel.addOnProductLevelModel.status == 1) {
                        if (cartItemModel.addOnProductLevelModel.addOnsDataItemModelList.isNotEmpty()) {
                            for (addOnsData in cartItemModel.addOnProductLevelModel.addOnsDataItemModelList) {
                                totalAddOnPrice += addOnsData.addOnPrice.toInt()
                                hasAddOnSelected = true
                            }
                        }
                    }
                }
            }
            val addOnsOrderLevelModel = shipmentCartItemModel.addOnsOrderLevelModel
            if (addOnsOrderLevelModel.status == 1) {
                if (addOnsOrderLevelModel.addOnsDataItemModelList.isNotEmpty()) {
                    for (addOnsData in addOnsOrderLevelModel.addOnsDataItemModelList) {
                        totalAddOnPrice += addOnsData.addOnPrice.toInt()
                        hasAddOnSelected = true
                    }
                }
            }
            val totalItemLabel = String.format(
                tvTotalItem.context.getString(R.string.label_item_count_with_format),
                totalItem
            )
            val totalPPPItemLabel = itemView.context.getString(
                R.string.label_protection_product_with_format,
                totalPurchaseProtectionItem
            )
            val voucherLogisticItemUiModel =
                shipmentCartItemModel.voucherLogisticItemUiModel
            val selectedShipmentDetailData =
                shipmentCartItemModel.selectedShipmentDetailData
            val selectedCourier = selectedShipmentDetailData?.selectedCourier
            if (
                (
                    selectedCourier != null ||
                        selectedShipmentDetailData?.selectedCourierTradeInDropOff != null
                    ) &&
                !shipmentCartItemModel.isError
            ) {
                var courierItemData: CourierItemData? = null
                if (actionListener?.isTradeInByDropOff == true && selectedShipmentDetailData.selectedCourierTradeInDropOff != null) {
                    courierItemData =
                        selectedShipmentDetailData.selectedCourierTradeInDropOff
                } else if (actionListener?.isTradeInByDropOff == false && selectedCourier != null) {
                    courierItemData = selectedCourier
                }
                if (courierItemData != null) {
                    shippingPrice = courierItemData.selectedShipper.shipperPrice
                    val useInsurance = selectedShipmentDetailData.useInsurance
                    if (useInsurance != null && useInsurance) {
                        insurancePrice = courierItemData.selectedShipper.insurancePrice
                    }
                    val isOrderPriority =
                        selectedShipmentDetailData.isOrderPriority
                    if (isOrderPriority != null && isOrderPriority) {
                        priorityPrice = courierItemData.priorityPrice
                    }
                    additionalPrice = courierItemData.additionalPrice
                    subTotalPrice += totalItemPrice + insurancePrice + totalPurchaseProtectionPrice + additionalPrice + priorityPrice
                    subTotalPrice += if (voucherLogisticItemUiModel != null) {
                        val discountedRate = courierItemData.selectedShipper.discountedRate
                        discountedRate.toLong()
                    } else {
                        shippingPrice.toLong()
                    }
                } else {
                    subTotalPrice = totalItemPrice
                }
            } else {
                subTotalPrice = totalItemPrice
            }
            subTotalPrice += totalAddOnPrice.toLong()
            tvSubTotalPrice.setTextAndContentDescription(
                if (subTotalPrice == 0L) {
                    "-"
                } else {
                    CurrencyFormatUtil.convertPriceValueToIdrFormat(subTotalPrice, false).removeDecimalSuffix()
                },
                R.string.content_desc_tv_sub_total_price
            )
            tvTotalItemPrice.setTextAndContentDescription(
                if (totalItemPrice == 0L) {
                    "-"
                } else {
                    getPriceFormat(
                        tvTotalItem,
                        tvTotalItemPrice,
                        totalItemPrice
                    )
                },
                R.string.content_desc_tv_total_item_price_subtotal
            )
            tvTotalItem.text = totalItemLabel
            tvShippingFee.text = shippingFeeLabel
            tvShippingFeePrice.setTextAndContentDescription(
                getPriceFormat(
                    tvShippingFee,
                    tvShippingFeePrice,
                    shippingPrice.toLong()
                ),
                R.string.content_desc_tv_shipping_fee_price_subtotal
            )
            if (selectedCourier != null && voucherLogisticItemUiModel != null) {
                if (selectedCourier.selectedShipper.discountedRate == 0) {
                    tvShippingFeePrice.setTextAndContentDescription(
                        CurrencyFormatUtil.convertPriceValueToIdrFormat(0.0, false)
                            .removeDecimalSuffix(),
                        R.string.content_desc_tv_shipping_fee_price_subtotal
                    )
                } else {
                    tvShippingFeePrice.setTextAndContentDescription(
                        getPriceFormat(
                            tvShippingFee,
                            tvShippingFeePrice,
                            selectedCourier.selectedShipper.discountedRate.toLong()
                        ),
                        R.string.content_desc_tv_shipping_fee_price_subtotal
                    )
                }
            }
            tvInsuranceFeePrice.setTextAndContentDescription(
                getPriceFormat(
                    tvInsuranceFee,
                    tvInsuranceFeePrice,
                    insurancePrice.toLong()
                ),
                R.string.content_desc_tv_insurance_fee_price_subtotal
            )
            tvPriorityFeePrice.text =
                getPriceFormat(tvPriorityFee, tvPriorityFeePrice, priorityPrice.toLong())
            tvPurchaseProtectionLabel.text = totalPPPItemLabel
            tvPurchaseProtectionFee.text =
                getPriceFormat(
                    tvPurchaseProtectionLabel,
                    tvPurchaseProtectionFee,
                    totalPurchaseProtectionPrice
                )
            tvAdditionalFeePrice.text =
                getPriceFormat(tvAdditionalFee, tvAdditionalFeePrice, additionalPrice.toLong())
            if (hasAddOnSelected) {
                tvAddOnFee.visibility = View.VISIBLE
                tvAddOnPrice.visibility = View.VISIBLE
                tvAddOnPrice.text =
                    CurrencyFormatUtil.convertPriceValueToIdrFormat(
                        totalAddOnPrice,
                        false
                    )
                        .removeDecimalSuffix()
            } else {
                tvAddOnFee.visibility = View.GONE
                tvAddOnPrice.visibility = View.GONE
            }
            rlCartSubTotal.setOnClickListener {
                shipmentCartItemModel.isStateDetailSubtotalViewExpanded =
                    !shipmentCartItemModel.isStateDetailSubtotalViewExpanded
                if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                    listener?.onClickExpandSubtotal(bindingAdapterPosition, shipmentCartItemModel)
                }
            }
        }
    }

    private fun getPriceFormat(
        textViewLabel: TextView,
        textViewPrice: TextView,
        price: Long
    ): String {
        return if (price == 0L) {
            textViewLabel.visibility = View.GONE
            textViewPrice.visibility = View.GONE
            "-"
        } else {
            textViewLabel.visibility = View.VISIBLE
            textViewPrice.visibility = View.VISIBLE
            CurrencyFormatUtil.convertPriceValueToIdrFormat(
                price,
                false
            )
                .removeDecimalSuffix()
        }
    }

    private fun renderError(shipmentCartItemModel: ShipmentCartItemModel) {
        if (shipmentCartItemModel.isError) {
            binding.llFooterContainer.alpha = VIEW_ALPHA_DISABLED
            with(binding.containerShippingOptions) {
                cbInsurance.isEnabled = false
                llInsurance.isClickable = false
                cbDropshipper.isEnabled = false
                llDropshipper.isClickable = false
                textInputLayoutShipperName.textFieldInput.isClickable = false
                textInputLayoutShipperName.textFieldInput.isFocusable = false
                textInputLayoutShipperName.textFieldInput.isFocusableInTouchMode = false
                textInputLayoutShipperPhone.textFieldInput.isClickable = false
                textInputLayoutShipperPhone.textFieldInput.isFocusable = false
                textInputLayoutShipperPhone.textFieldInput.isFocusableInTouchMode = false
            }
            binding.containerSubtotal.rlCartSubTotal.isClickable = false
        } else {
            binding.llFooterContainer.alpha = VIEW_ALPHA_ENABLED
            with(binding.containerShippingOptions) {
                llInsurance.isClickable = true
                llDropshipper.isClickable = true
                textInputLayoutShipperName.textFieldInput.isClickable = true
                textInputLayoutShipperName.textFieldInput.isFocusable = true
                textInputLayoutShipperName.textFieldInput.isFocusableInTouchMode = true
                textInputLayoutShipperPhone.textFieldInput.isClickable = true
                textInputLayoutShipperPhone.textFieldInput.isFocusable = true
                textInputLayoutShipperPhone.textFieldInput.isFocusableInTouchMode = true
            }
            binding.containerSubtotal.rlCartSubTotal.isClickable = true
        }
    }

    private fun renderShippingVibrationAnimation(shipmentCartItemModel: ShipmentCartItemModel) {
        binding.shippingWidget.renderShippingVibrationAnimation(shipmentCartItemModel)
    }

    private fun renderAddOnOrderLevel(shipmentCartItemModel: ShipmentCartItemModel) {
        with(binding.containerGiftingAddonOrderLevel) {
            val addOnsDataModel = shipmentCartItemModel.addOnsOrderLevelModel
            val addOnsButton = addOnsDataModel.addOnsButtonModel
            val statusAddOn = addOnsDataModel.status
            if (statusAddOn == 0) {
                root.visibility = View.GONE
            } else {
                if (statusAddOn == 1) {
                    buttonGiftingAddonOrderLevel.state = ButtonGiftingAddOnView.State.ACTIVE
                } else if (statusAddOn == 2) {
                    buttonGiftingAddonOrderLevel.state = ButtonGiftingAddOnView.State.INACTIVE
                }
                root.visibility = View.VISIBLE
                buttonGiftingAddonOrderLevel.title = addOnsButton.title
                buttonGiftingAddonOrderLevel.desc = addOnsButton.description
                buttonGiftingAddonOrderLevel.urlLeftIcon = addOnsButton.leftIconUrl
                buttonGiftingAddonOrderLevel.urlRightIcon = addOnsButton.rightIconUrl
                buttonGiftingAddonOrderLevel.setOnClickListener {
                    actionListener?.openAddOnOrderLevelBottomSheet(
                        shipmentCartItemModel,
                        shipmentCartItemModel.addOnWordingModel
                    )
                }
                actionListener?.addOnOrderLevelImpression(shipmentCartItemModel.cartItemModels)
            }
        }
    }

    private fun loadCourierState(
        shipmentCartItemModel: ShipmentCartItemModel,
        recipientAddressModel: RecipientAddressModel,
        ratesDataConverter: RatesDataConverter,
        saveStateType: Int
    ) {
        with(binding) {
            val shipmentDetailData = shipmentCartItemModel.selectedShipmentDetailData
            if (shipmentCartItemModel.isStateLoadingCourierState) {
                renderLoadingCourierState()
            } else {
                var hasLoadCourier = false
                shippingWidget.hideShippingStateLoading()
                when (saveStateType) {
                    SHIPPING_SAVE_STATE_TYPE_TRADE_IN_DROP_OFF ->
                        hasLoadCourier =
                            shipmentDetailData?.selectedCourierTradeInDropOff != null

                    SHIPPING_SAVE_STATE_TYPE_SHIPPING_EXPERIENCE ->
                        hasLoadCourier =
                            shipmentDetailData?.selectedCourier != null
                }
                if (shipmentCartItemModel.isCustomPinpointError) {
                    renderErrorPinpointCourier()
                } else if (shouldAutoLoadCourier(shipmentCartItemModel, recipientAddressModel)) {
                    if (!hasLoadCourier) {
                        val tmpShipmentDetailData = ratesDataConverter.getShipmentDetailData(
                            shipmentCartItemModel,
                            recipientAddressModel
                        )
                        val hasLoadCourierState =
                            if (saveStateType == SHIPPING_SAVE_STATE_TYPE_TRADE_IN_DROP_OFF) {
                                shipmentCartItemModel.isStateHasLoadCourierTradeInDropOffState
                            } else {
                                shipmentCartItemModel.isStateHasLoadCourierState
                            }
                        if (!hasLoadCourierState) {
                            val position = bindingAdapterPosition
                            if (position != RecyclerView.NO_POSITION) {
                                loadCourierStateData(
                                    shipmentCartItemModel,
                                    saveStateType,
                                    tmpShipmentDetailData,
                                    position
                                )
                            }
                        } else {
                            renderNoSelectedCourier(
                                shipmentCartItemModel,
                                recipientAddressModel,
                                saveStateType
                            )
                        }
                    }
                } else {
                    renderNoSelectedCourier(
                        shipmentCartItemModel,
                        recipientAddressModel,
                        saveStateType
                    )
                    showMultiplePlusOrderCoachmark(
                        shipmentCartItemModel,
                        shippingWidget.layoutStateNoSelectedShipping
                    )
                }
            }
        }
    }

    private fun renderLoadingCourierState() {
        binding.shippingWidget.renderLoadingCourierState()
    }

    private fun renderErrorPinpointCourier() {
        binding.containerShippingOptions.root.visibility = View.VISIBLE
        binding.shippingWidget.renderErrorPinpointCourier()
    }

    private fun shouldAutoLoadCourier(
        shipmentCartItemModel: ShipmentCartItemModel,
        recipientAddressModel: RecipientAddressModel
    ): Boolean {
        return (
            recipientAddressModel.isTradeIn && recipientAddressModel.selectedTabIndex != 0 && shipmentCartItemModel.shippingId != 0 && shipmentCartItemModel.spId != 0 && !TextUtils.isEmpty(
                recipientAddressModel.dropOffAddressName
            ) || recipientAddressModel.isTradeIn && recipientAddressModel.selectedTabIndex == 0 && shipmentCartItemModel.shippingId != 0 && shipmentCartItemModel.spId != 0 && !TextUtils.isEmpty(
                recipientAddressModel.provinceName
            ) || !recipientAddressModel.isTradeIn && shipmentCartItemModel.shippingId != 0 && shipmentCartItemModel.spId != 0 && !TextUtils.isEmpty(
                recipientAddressModel.provinceName
            ) || !recipientAddressModel.isTradeIn && shipmentCartItemModel.boCode.isNotEmpty() && !TextUtils.isEmpty(
                recipientAddressModel.provinceName
            ) || // normal address auto apply BO
                shipmentCartItemModel.isAutoCourierSelection
            ) // tokopedia now
    }

    private fun loadCourierStateData(
        shipmentCartItemModel: ShipmentCartItemModel,
        saveStateType: Int,
        tmpShipmentDetailData: ShipmentDetailData,
        position: Int
    ) {
        actionListener?.onLoadShippingState(
            shipmentCartItemModel.shippingId,
            shipmentCartItemModel.spId,
            position,
            tmpShipmentDetailData,
            shipmentCartItemModel,
            shipmentCartItemModel.shopShipmentList,
            saveStateType == SHIPPING_SAVE_STATE_TYPE_TRADE_IN_DROP_OFF
        )
        shipmentCartItemModel.isStateLoadingCourierState = true
        binding.shippingWidget.onLoadCourierStateData()
        when (saveStateType) {
            SHIPPING_SAVE_STATE_TYPE_TRADE_IN_DROP_OFF -> {
                shipmentCartItemModel.isStateHasLoadCourierTradeInDropOffState = true
                binding.shippingWidget.hideTradeInTitleAndDetail()
            }

            SHIPPING_SAVE_STATE_TYPE_SHIPPING_EXPERIENCE ->
                shipmentCartItemModel.isStateHasLoadCourierState =
                    true
        }
    }

    private fun renderNoSelectedCourier(
        shipmentCartItemModel: ShipmentCartItemModel,
        recipientAddressModel: RecipientAddressModel,
        saveStateType: Int
    ) {
        when (saveStateType) {
            SHIPPING_SAVE_STATE_TYPE_TRADE_IN_DROP_OFF -> {
                val hasSelectTradeInLocation = actionListener?.hasSelectTradeInLocation() ?: false
                renderNoSelectedCourierTradeInDropOff(
                    shipmentCartItemModel,
                    recipientAddressModel,
                    hasSelectTradeInLocation
                )
            }

            SHIPPING_SAVE_STATE_TYPE_SHIPPING_EXPERIENCE -> {
                renderNoSelectedCourierNormalShipping(
                    shipmentCartItemModel,
                    recipientAddressModel
                )
            }
        }
    }

    private fun renderNoSelectedCourierTradeInDropOff(
        shipmentCartItemModel: ShipmentCartItemModel,
        currentAddress: RecipientAddressModel,
        hasSelectTradeInLocation: Boolean
    ) {
        if (hasSelectTradeInLocation) {
            renderNoSelectedCourierNormalShipping(
                shipmentCartItemModel,
                currentAddress
            )
        } else {
            binding.shippingWidget.showLayoutTradeIn()
        }
    }

    private fun renderNoSelectedCourierNormalShipping(
        shipmentCartItemModel: ShipmentCartItemModel,
        currentAddress: RecipientAddressModel
    ) {
        if (shipmentCartItemModel.isDisableChangeCourier) {
            if (shipmentCartItemModel.hasGeolocation) {
                renderFailShipmentState(shipmentCartItemModel, currentAddress)
            }
        } else {
            binding.shippingWidget.showLayoutNoSelectedShipping(
                shipmentCartItemModel,
                currentAddress
            )
        }
    }

    private fun renderFailShipmentState(
        shipmentCartItemModel: ShipmentCartItemModel,
        recipientAddressModel: RecipientAddressModel
    ) {
        binding.shippingWidget.showLayoutStateFailedShipping(
            shipmentCartItemModel,
            recipientAddressModel
        )
    }

    private fun showMultiplePlusOrderCoachmark(
        shipmentCartItemModel: ShipmentCartItemModel,
        anchorView: View?
    ) {
        if (shipmentCartItemModel.coachmarkPlus.isShown && !plusCoachmarkPrefs.getPlusCoachMarkHasShown() && anchorView != null) {
            val coachMarkItem = ArrayList<CoachMark2Item>()
            val coachMark = CoachMark2(itemView.context)
            coachMarkItem.add(
                CoachMark2Item(
                    anchorView,
                    shipmentCartItemModel.coachmarkPlus.title,
                    shipmentCartItemModel.coachmarkPlus.content,
                    CoachMark2.POSITION_BOTTOM
                )
            )
            coachMark.showCoachMark(coachMarkItem, null, 0)
            plusCoachmarkPrefs.setPlusCoachmarkHasShown(true)
        }
    }

    private fun initSaveStateDebouncer() {
        compositeSubscription.add(
            Observable.create<ShipmentCartItemModel> { subscriber ->
                saveStateDebounceListener = object :
                    SaveStateDebounceListener {
                    override fun onNeedToSaveState(shipmentCartItemModel: ShipmentCartItemModel?) {
                        subscriber.onNext(shipmentCartItemModel)
                    }
                }
            }.debounce(500, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Subscriber<ShipmentCartItemModel>() {
                    override fun onCompleted() {}
                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                    }

                    override fun onNext(shipmentCartItemModel: ShipmentCartItemModel) {
                        actionListener?.onNeedToSaveState(shipmentCartItemModel)
                    }
                })
        )
    }

    fun unsubscribeDebouncer() {
        compositeSubscription.unsubscribe()
    }

    override fun onChangeDurationClickListener(
        shipmentCartItemModel: ShipmentCartItemModel,
        currentAddress: RecipientAddressModel
    ) {
        if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
            actionListener?.onChangeShippingDuration(
                shipmentCartItemModel,
                currentAddress,
                bindingAdapterPosition
            )
        }
    }

    override fun onChangeCourierClickListener(
        shipmentCartItemModel: ShipmentCartItemModel,
        currentAddress: RecipientAddressModel
    ) {
        if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
            actionListener?.onChangeShippingCourier(
                currentAddress,
                shipmentCartItemModel,
                bindingAdapterPosition,
                null
            )
        }
    }

    override fun onOnTimeDeliveryClicked(url: String) {
        actionListener?.onOntimeDeliveryClicked(url)
    }

    override fun onClickSetPinpoint() {
        actionListener?.onClickSetPinpoint(bindingAdapterPosition)
    }

    override fun onClickLayoutFailedShipping(
        shipmentCartItemModel: ShipmentCartItemModel,
        recipientAddressModel: RecipientAddressModel
    ) {
        val tmpShipmentDetailData = ratesDataConverter.getShipmentDetailData(
            shipmentCartItemModel,
            recipientAddressModel
        )
        if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
            loadCourierStateData(
                shipmentCartItemModel,
                SHIPPING_SAVE_STATE_TYPE_SHIPPING_EXPERIENCE,
                tmpShipmentDetailData,
                bindingAdapterPosition
            )
            actionListener?.onClickRefreshErrorLoadCourier()
        }
    }

    override fun onViewErrorInCourierSection(logPromoDesc: String) {
        actionListener?.onViewErrorInCourierSection(logPromoDesc)
    }

    override fun onChangeScheduleDelivery(scheduleDeliveryUiModel: ScheduleDeliveryUiModel) {
        if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
            scheduleDeliveryDonePublisher = PublishSubject.create()
            actionListener?.onChangeScheduleDelivery(
                scheduleDeliveryUiModel,
                bindingAdapterPosition,
                scheduleDeliveryDonePublisher!!
            )
        }
    }

    override fun getHostFragmentManager(): FragmentManager {
        return actionListener?.currentFragmentManager!!
    }

    private interface SaveStateDebounceListener {

        fun onNeedToSaveState(shipmentCartItemModel: ShipmentCartItemModel?)
    }

    interface Listener {

        fun onClickExpandSubtotal(position: Int, shipmentCartItemModel: ShipmentCartItemModel)
    }
}
