package com.tokopedia.checkout.view.viewholder

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.text.Editable
import android.text.Spannable
import android.text.SpannableString
import android.text.TextUtils
import android.text.TextWatcher
import android.text.style.StyleSpan
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
import com.tokopedia.checkout.view.helper.ShipmentScheduleDeliveryHolderData
import com.tokopedia.checkout.view.uimodel.ShipmentGroupFooterModel
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.setTextAndContentDescription
import com.tokopedia.logisticCommon.data.constant.CourierConstant
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
import com.tokopedia.purchase_platform.common.utils.Utils
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.currency.CurrencyFormatUtil
import rx.Emitter
import rx.Observable
import rx.Subscriber
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Action1
import rx.schedulers.Schedulers
import rx.subjects.PublishSubject
import rx.subscriptions.CompositeSubscription
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern

class ShipmentGroupFooterViewHolder(
    itemView: View,
    private val ratesDataConverter: RatesDataConverter,
    private val listener: Listener? = null,
    private val actionListener: ShipmentAdapterActionListener? = null,
    private val scheduleDeliveryCompositeSubscription: CompositeSubscription? = null
) : RecyclerView.ViewHolder(itemView), ShippingWidget.ShippingWidgetListener {

    companion object {

        @JvmField
        val LAYOUT = R.layout.item_shipment_group_footer

        private const val VIEW_ALPHA_ENABLED = 1.0f
        private const val VIEW_ALPHA_DISABLED = 0.5f
        private const val FIRST_ELEMENT = 0
        private const val DROPSHIPPER_MIN_NAME_LENGTH = 3
        private const val DROPSHIPPER_MAX_NAME_LENGTH = 100
        private const val DROPSHIPPER_MIN_PHONE_LENGTH = 6
        private const val DROPSHIPPER_MAX_PHONE_LENGTH = 20
        private const val PHONE_NUMBER_REGEX_PATTERN = "[0-9]+"
        private const val SHIPPING_SAVE_STATE_TYPE_TRADE_IN_DROP_OFF = 1
        private const val SHIPPING_SAVE_STATE_TYPE_SHIPPING_EXPERIENCE = 2
    }

    private val binding: ItemShipmentGroupFooterBinding =
        ItemShipmentGroupFooterBinding.bind(itemView)
    private val plusCoachmarkPrefs: PlusCoachmarkPrefs by lazy {
        PlusCoachmarkPrefs(itemView.context)
    }
    private val phoneNumberRegexPattern: Pattern = Pattern.compile(PHONE_NUMBER_REGEX_PATTERN)
    private var isPriorityChecked: Boolean = false
    private val compositeSubscription: CompositeSubscription = CompositeSubscription()
    private var saveStateDebounceListener: SaveStateDebounceListener? = null
    private var scheduleDeliverySubscription: Subscription? = null
    private var scheduleDeliveryDonePublisher: PublishSubject<Boolean>? = null
    private var scheduleDeliveryDebouncedListener: ScheduleDeliveryDebouncedListener? = null

    init {
        initSaveStateDebouncer()
        initScheduleDeliveryPublisher()
    }

    fun bind(
        shipmentGroupFooter: ShipmentGroupFooterModel,
        recipientAddress: RecipientAddressModel?,
    ) {
        renderShipping(shipmentGroupFooter, recipientAddress, ratesDataConverter)
        renderPrioritas(shipmentGroupFooter)
        renderInsurance(shipmentGroupFooter)
        val isCornerAddress = recipientAddress != null && recipientAddress.isCornerAddress
        renderDropshipper(shipmentGroupFooter, isCornerAddress)
        renderCostDetail(shipmentGroupFooter)
        renderError(shipmentGroupFooter)
        renderShippingVibrationAnimation(shipmentGroupFooter)
        renderAddOnOrderLevel(shipmentGroupFooter)
    }

    private fun renderShipping(
        shipmentGroupFooter: ShipmentGroupFooterModel,
        currentAddress: RecipientAddressModel?,
        ratesDataConverter: RatesDataConverter
    ) {
        binding.shippingWidget.setupListener(this@ShipmentGroupFooterViewHolder)
        binding.shippingWidget.hideTradeInShippingInfo()
        if (shipmentGroupFooter.shipmentCartItem.isError) {
            renderErrorCourierState(shipmentGroupFooter.shipmentCartItem)
            return
        }
        var selectedCourierItemData: CourierItemData? = null
        val isTradeInDropOff = actionListener?.isTradeInByDropOff ?: false
        if (shipmentGroupFooter.shipmentCartItem.selectedShipmentDetailData != null) {
            if (isTradeInDropOff && shipmentGroupFooter.shipmentCartItem.selectedShipmentDetailData?.selectedCourierTradeInDropOff != null) {
                selectedCourierItemData =
                    shipmentGroupFooter.shipmentCartItem.selectedShipmentDetailData?.selectedCourierTradeInDropOff
            } else if (!isTradeInDropOff && shipmentGroupFooter.shipmentCartItem.selectedShipmentDetailData?.selectedCourier != null) {
                selectedCourierItemData =
                    shipmentGroupFooter.shipmentCartItem.selectedShipmentDetailData?.selectedCourier
            }
        }
        if (selectedCourierItemData != null) {
            if (shipmentGroupFooter.shipmentCartItem.isStateLoadingCourierState) {
                // Has select shipping, but still loading
                renderLoadingCourierState()
                return
            }
            // Has select shipping
            renderSelectedCourier(shipmentGroupFooter.shipmentCartItem, currentAddress, selectedCourierItemData)
        } else {
            // Has not select shipping
            prepareLoadCourierState(
                shipmentGroupFooter.shipmentCartItem,
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
        currentAddress: RecipientAddressModel?,
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
                currentAddress!!,
                selectedCourierItemData
            )
        } else if (shipmentCartItemModel.isHideChangeCourierCard) {
            // normal shipping but not show `pilih kurir` card
            binding.shippingWidget.renderNormalShippingWithoutChooseCourierCard(
                shipmentCartItemModel,
                currentAddress!!,
                selectedCourierItemData
            )
        } else {
            // Is normal shipping
            binding.shippingWidget.renderNormalShippingCourier(
                shipmentCartItemModel,
                currentAddress!!,
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
        currentAddress: RecipientAddressModel?,
        ratesDataConverter: RatesDataConverter,
        isTradeInDropOff: Boolean
    ) {
        binding.shippingWidget.prepareLoadCourierState()
        binding.containerShippingOptions.root.visibility = View.GONE
        if (isTradeInDropOff) {
            renderNoSelectedCourier(
                shipmentCartItemModel,
                currentAddress,
                ratesDataConverter,
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
                ratesDataConverter,
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

    private fun renderPrioritas(shipmentGroupFooter: ShipmentGroupFooterModel) {
        with(binding.containerShippingOptions) {
            val cartItemModelList = ArrayList(shipmentGroupFooter.shipmentCartItem.cartItemModels)
            val selectedShipmentDetailData = shipmentGroupFooter.shipmentCartItem.selectedShipmentDetailData
            var renderOrderPriority = false
            val isTradeInDropOff = actionListener?.isTradeInByDropOff ?: false
            if (selectedShipmentDetailData != null) {
                renderOrderPriority = if (isTradeInDropOff) {
                    selectedShipmentDetailData.selectedCourierTradeInDropOff != null
                } else {
                    selectedShipmentDetailData.selectedCourier != null
                }
            }
            if (bindingAdapterPosition != RecyclerView.NO_POSITION && renderOrderPriority) {
                if (!cartItemModelList.removeAt(FIRST_ELEMENT).isPreOrder) {
                    cbPrioritas.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
                        if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                            isPriorityChecked = isChecked
                            selectedShipmentDetailData!!.isOrderPriority =
                                isChecked
                            actionListener?.onPriorityChecked(bindingAdapterPosition)
                            actionListener?.onNeedUpdateRequestData()
                        }
                    }
                }
                val spanText = SpannableString(
                    tvPrioritasTicker.resources.getString(R.string.label_hardcoded_courier_ticker)
                )
                spanText.setSpan(
                    StyleSpan(Typeface.BOLD),
                    43,
                    52,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                val courierItemData: CourierItemData? = if (isTradeInDropOff) {
                    selectedShipmentDetailData!!.selectedCourierTradeInDropOff
                } else {
                    selectedShipmentDetailData!!.selectedCourier
                }
                val isCourierSelected = courierItemData != null
                if (isCourierSelected && !shipmentGroupFooter.shipmentCartItem.isError) {
                    if (isCourierInstantOrSameday(courierItemData!!.shipperId)) {
                        if (!shipmentGroupFooter.shipmentCartItem.isOrderPrioritasDisable && courierItemData.now!! && !shipmentGroupFooter.shipmentCartItem.isProductIsPreorder) {
                            tvOrderPrioritasInfo.text = courierItemData.priorityCheckboxMessage
                            llPrioritas.visibility = View.VISIBLE
                            llPrioritasTicker.visibility = View.VISIBLE
                        } else {
                            llPrioritas.visibility = View.GONE
                            llPrioritasTicker.visibility = View.GONE
                        }
                    } else {
                        hideAllTicker()
                    }
                } else {
                    hideAllTicker()
                }
                if (courierItemData != null && isPriorityChecked) {
                    tvPrioritasTicker.text = courierItemData.priorityWarningboxMessage
                } else {
                    tvPrioritasTicker.text = spanText
                }
            } else {
                hideAllTicker()
            }
            imgPrioritasInfo.setOnClickListener { actionListener?.onPriorityTncClicker() }
        }
    }

    private fun hideAllTicker() {
        binding.containerShippingOptions.llPrioritas.visibility = View.GONE
        binding.containerShippingOptions.llPrioritasTicker.visibility = View.GONE
    }

    private fun isCourierInstantOrSameday(shipperId: Int): Boolean {
        val ids = CourierConstant.INSTANT_SAMEDAY_COURIER
        for (id in ids) {
            if (shipperId == id) return true
        }
        return false
    }

    private fun renderInsurance(shipmentGroupFooter: ShipmentGroupFooterModel) {
        with(binding.containerShippingOptions) {
            var renderInsurance = false
            val isTradeInDropOff = actionListener?.isTradeInByDropOff ?: false
            val selectedShipmentDetailData = shipmentGroupFooter.shipmentCartItem.selectedShipmentDetailData
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
                        actionListener?.onNeedUpdateRequestData()
                        saveStateDebounceListener?.onNeedToSaveState(shipmentGroupFooter.shipmentCartItem)
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
                            cbInsurance.isChecked = shipmentGroupFooter.shipmentCartItem.isInsurance
                            selectedShipmentDetailData.useInsurance =
                                shipmentGroupFooter.shipmentCartItem.isInsurance
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

    private fun renderDropshipper(shipmentGroupFooter: ShipmentGroupFooterModel, isCorner: Boolean) {
        with(binding.containerShippingOptions) {
            val isTradeInDropOff = actionListener?.isTradeInByDropOff ?: false
            var courierItemData: CourierItemData? = null
            val selectedShipmentDetailData = shipmentGroupFooter.shipmentCartItem.selectedShipmentDetailData
            if (selectedShipmentDetailData != null) {
                courierItemData = if (isTradeInDropOff) {
                    selectedShipmentDetailData.selectedCourierTradeInDropOff
                } else {
                    selectedShipmentDetailData.selectedCourier
                }
            }
            if (selectedShipmentDetailData != null && courierItemData != null) {
                if (shipmentGroupFooter.shipmentCartItem.isDropshipperDisable || !courierItemData.isAllowDropshiper || isCorner) {
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
                    if (checked && isHavingPurchaseProtectionChecked(shipmentGroupFooter.shipmentCartItem)) {
                        compoundButton.isChecked = false
                        actionListener?.onPurchaseProtectionLogicError()
                        return@setOnCheckedChangeListener
                    }
                    shipmentGroupFooter.shipmentCartItem.selectedShipmentDetailData!!.useDropshipper = checked
                    if (checked) {
                        textInputLayoutShipperName.textFieldInput.setText(shipmentGroupFooter.shipmentCartItem.dropshiperName)
                        textInputLayoutShipperPhone.textFieldInput.setText(shipmentGroupFooter.shipmentCartItem.dropshiperPhone)
                        shipmentGroupFooter.shipmentCartItem.selectedShipmentDetailData!!.dropshipperName =
                            shipmentGroupFooter.shipmentCartItem.dropshiperName
                        shipmentGroupFooter.shipmentCartItem.selectedShipmentDetailData!!.dropshipperPhone =
                            shipmentGroupFooter.shipmentCartItem.dropshiperPhone
                        llDropshipperInfo.visibility = View.VISIBLE
                        actionListener?.onDropshipCheckedForTrackingAnalytics()
                    } else {
                        textInputLayoutShipperName.textFieldInput.setText("")
                        textInputLayoutShipperPhone.textFieldInput.setText("")
                        shipmentGroupFooter.shipmentCartItem.selectedShipmentDetailData!!.dropshipperName = ""
                        shipmentGroupFooter.shipmentCartItem.selectedShipmentDetailData!!.dropshipperPhone = ""
                        shipmentGroupFooter.shipmentCartItem.dropshiperName = ""
                        shipmentGroupFooter.shipmentCartItem.dropshiperPhone = ""
                        llDropshipperInfo.visibility = View.GONE
                        shipmentGroupFooter.shipmentCartItem.isStateDropshipperHasError = false
                    }
                    actionListener?.onNeedUpdateViewItem(bindingAdapterPosition)
                    actionListener?.onNeedUpdateRequestData()
                    saveStateDebounceListener?.onNeedToSaveState(shipmentGroupFooter.shipmentCartItem)
                }
                val useDropshipper =
                    selectedShipmentDetailData.useDropshipper
                if (useDropshipper != null) {
                    if (useDropshipper) {
                        cbDropshipper.isChecked = true
                    } else {
                        checkDropshipperState(shipmentGroupFooter.shipmentCartItem)
                    }
                } else {
                    checkDropshipperState(shipmentGroupFooter.shipmentCartItem)
                }
                if (shipmentGroupFooter.shipmentCartItem.voucherLogisticItemUiModel != null) {
                    cbDropshipper.isEnabled = false
                    cbDropshipper.isChecked = false
                    llDropshipper.setOnClickListener(null)
                    labelDropshipper.setTextColor(
                        ContextCompat.getColor(
                            itemView.context,
                            com.tokopedia.unifyprinciples.R.color.Unify_N700_20
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
                            com.tokopedia.unifyprinciples.R.color.Unify_N700_68
                        )
                    )
                    llDropshipper.setOnClickListener {
                        binding.containerShippingOptions.cbDropshipper.isChecked =
                            !binding.containerShippingOptions.cbDropshipper.isChecked
                    }
                    imgDropshipperInfo.setOnClickListener { view: View? ->
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
                        if (shipmentGroupFooter.shipmentCartItem.selectedShipmentDetailData != null) {
                            if (!TextUtils.isEmpty(charSequence)) {
                                shipmentGroupFooter.shipmentCartItem.selectedShipmentDetailData!!.dropshipperName =
                                    charSequence.toString()
                                validateDropshipperName(shipmentGroupFooter.shipmentCartItem, charSequence, true)
                                saveStateDebounceListener?.onNeedToSaveState(shipmentGroupFooter.shipmentCartItem)
                            }
                        }
                    }

                    override fun afterTextChanged(editable: Editable) {}
                })
                if (!TextUtils.isEmpty(selectedShipmentDetailData.dropshipperName) ||
                    !TextUtils.isEmpty(shipmentGroupFooter.shipmentCartItem.dropshiperName)
                ) {
                    textInputLayoutShipperName.textFieldInput.setText(selectedShipmentDetailData.dropshipperName)
                } else {
                    textInputLayoutShipperName.textFieldInput.setText("")
                }
                if (shipmentGroupFooter.shipmentCartItem.isStateDropshipperHasError) {
                    validateDropshipperName(
                        shipmentGroupFooter.shipmentCartItem,
                        textInputLayoutShipperName.textFieldInput.text,
                        true
                    )
                } else {
                    validateDropshipperName(
                        shipmentGroupFooter.shipmentCartItem,
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
                        if (shipmentGroupFooter.shipmentCartItem.selectedShipmentDetailData != null) {
                            if (!TextUtils.isEmpty(charSequence)) {
                                shipmentGroupFooter.shipmentCartItem.selectedShipmentDetailData!!.dropshipperPhone =
                                    charSequence.toString()
                                validateDropshipperPhone(shipmentGroupFooter.shipmentCartItem, charSequence, true)
                                saveStateDebounceListener?.onNeedToSaveState(shipmentGroupFooter.shipmentCartItem)
                            }
                        }
                    }

                    override fun afterTextChanged(editable: Editable) {}
                })
                if (!TextUtils.isEmpty(selectedShipmentDetailData.dropshipperPhone) ||
                    !TextUtils.isEmpty(shipmentGroupFooter.shipmentCartItem.dropshiperPhone)
                ) {
                    textInputLayoutShipperPhone.textFieldInput.setText(
                        selectedShipmentDetailData.dropshipperPhone
                    )
                } else {
                    textInputLayoutShipperPhone.textFieldInput.setText("")
                }
                if (shipmentGroupFooter.shipmentCartItem.isStateDropshipperHasError) {
                    validateDropshipperPhone(
                        shipmentGroupFooter.shipmentCartItem,
                        textInputLayoutShipperPhone.textFieldInput.text,
                        true
                    )
                } else {
                    validateDropshipperPhone(
                        shipmentGroupFooter.shipmentCartItem,
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

    private fun isHavingPurchaseProtectionChecked(shipmentCartItem: ShipmentCartItemModel): Boolean {
        return shipmentCartItem.cartItemModels.any { it.isProtectionOptIn }
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

    private fun renderCostDetail(shipmentGroupFooter: ShipmentGroupFooterModel) {
        with(binding.containerSubtotal) {
            rlCartSubTotal.visibility = View.VISIBLE
            rlShipmentCost.visibility =
                if (shipmentGroupFooter.shipmentCartItem.isStateDetailSubtotalViewExpanded) View.VISIBLE else View.GONE
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
            if (shipmentGroupFooter.shipmentCartItem.isStateDetailSubtotalViewExpanded) {
                rlShipmentCost.visibility = View.VISIBLE
                ivDetailOptionChevron.setImage(IconUnify.CHEVRON_UP, null, null, null, null)
            } else {
                rlShipmentCost.visibility = View.GONE
                ivDetailOptionChevron.setImage(IconUnify.CHEVRON_DOWN, null, null, null, null)
            }
            val shippingFeeLabel = tvShippingFee.context.getString(R.string.label_delivery_price)
            var totalItemLabel =
                tvTotalItem.context.getString(R.string.label_item_count_without_format)
            for (cartItemModel in shipmentGroupFooter.shipmentCartItem.cartItemModels) {
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
            val addOnsOrderLevelModel = shipmentGroupFooter.shipmentCartItem.addOnsOrderLevelModel
            if (addOnsOrderLevelModel != null) {
                if (addOnsOrderLevelModel.status == 1) {
                    if (addOnsOrderLevelModel.addOnsDataItemModelList.isNotEmpty()) {
                        for (addOnsData in addOnsOrderLevelModel.addOnsDataItemModelList) {
                            totalAddOnPrice += addOnsData.addOnPrice.toInt()
                            hasAddOnSelected = true
                        }
                    }
                }
            }
            totalItemLabel = String.format(
                tvTotalItem.context.getString(R.string.label_item_count_with_format),
                totalItem
            )
            @SuppressLint("DefaultLocale")
            val totalPPPItemLabel =
                String.format("Proteksi Produk (%d Barang)", totalPurchaseProtectionItem)
            val voucherLogisticItemUiModel =
                shipmentGroupFooter.shipmentCartItem.voucherLogisticItemUiModel
            val selectedShipmentDetailData =
                shipmentGroupFooter.shipmentCartItem.selectedShipmentDetailData
            val selectedCourier = selectedShipmentDetailData?.selectedCourier
            if (
                (
                    selectedCourier != null ||
                        selectedShipmentDetailData?.selectedCourierTradeInDropOff != null
                    ) &&
                !shipmentGroupFooter.shipmentCartItem.isError
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
                    Utils.removeDecimalSuffix(
                        CurrencyFormatUtil.convertPriceValueToIdrFormat(subTotalPrice, false)
                    )
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
                        Utils.removeDecimalSuffix(
                            CurrencyFormatUtil.convertPriceValueToIdrFormat(0.0, false)
                        ),
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
                    Utils.removeDecimalSuffix(
                        CurrencyFormatUtil.convertPriceValueToIdrFormat(
                            totalAddOnPrice,
                            false
                        )
                    )
            } else {
                tvAddOnFee.visibility = View.GONE
                tvAddOnPrice.visibility = View.GONE
            }
            rlCartSubTotal.setOnClickListener {
                shipmentGroupFooter.shipmentCartItem.isStateDetailSubtotalViewExpanded =
                    !shipmentGroupFooter.shipmentCartItem.isStateDetailSubtotalViewExpanded
                if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                    listener?.onClickExpandSubtotal(bindingAdapterPosition, shipmentGroupFooter)
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
            Utils.removeDecimalSuffix(
                CurrencyFormatUtil.convertPriceValueToIdrFormat(
                    price,
                    false
                )
            )
        }
    }

    private fun renderError(shipmentGroupFooter: ShipmentGroupFooterModel) {
        if (shipmentGroupFooter.shipmentCartItem.isError) {
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

    private fun renderShippingVibrationAnimation(shipmentGroupFooter: ShipmentGroupFooterModel) {
        binding.shippingWidget.renderShippingVibrationAnimation(shipmentGroupFooter.shipmentCartItem)
    }

    private fun renderAddOnOrderLevel(shipmentGroupFooter: ShipmentGroupFooterModel) {
        with(binding.containerGiftingAddonOrderLevel) {
            val addOnsDataModel = shipmentGroupFooter.shipmentCartItem.addOnsOrderLevelModel
            if (addOnsDataModel != null) {
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
                            shipmentGroupFooter.shipmentCartItem,
                            shipmentGroupFooter.shipmentCartItem.addOnWordingModel
                        )
                    }
                    actionListener?.addOnOrderLevelImpression(shipmentGroupFooter.shipmentCartItem.cartItemModels)
                }
            }
        }
    }

    private fun loadCourierState(
        shipmentCartItemModel: ShipmentCartItemModel,
        recipientAddressModel: RecipientAddressModel?,
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
                            recipientAddressModel!!
                        )
                        val hasLoadCourierState: Boolean
                        hasLoadCourierState =
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
                                ratesDataConverter,
                                saveStateType
                            )
                        }
                    }
                } else {
                    renderNoSelectedCourier(
                        shipmentCartItemModel,
                        recipientAddressModel,
                        ratesDataConverter,
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
        recipientAddressModel: RecipientAddressModel?
    ): Boolean {
        return (
            recipientAddressModel!!.isTradeIn && recipientAddressModel.selectedTabIndex != 0 && shipmentCartItemModel.shippingId != 0 && shipmentCartItemModel.spId != 0 && !TextUtils.isEmpty(
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
        recipientAddressModel: RecipientAddressModel?,
        ratesDataConverter: RatesDataConverter,
        saveStateType: Int
    ) {
        when (saveStateType) {
            SHIPPING_SAVE_STATE_TYPE_TRADE_IN_DROP_OFF -> {
                val hasSelectTradeInLocation = actionListener?.hasSelectTradeInLocation() ?: false
                renderNoSelectedCourierTradeInDropOff(
                    shipmentCartItemModel,
                    recipientAddressModel,
                    ratesDataConverter,
                    hasSelectTradeInLocation
                )
            }

            SHIPPING_SAVE_STATE_TYPE_SHIPPING_EXPERIENCE -> {
                renderNoSelectedCourierNormalShipping(
                    shipmentCartItemModel,
                    recipientAddressModel,
                    ratesDataConverter
                )
            }
        }
    }

    private fun renderNoSelectedCourierTradeInDropOff(
        shipmentCartItemModel: ShipmentCartItemModel,
        currentAddress: RecipientAddressModel?,
        ratesDataConverter: RatesDataConverter,
        hasSelectTradeInLocation: Boolean
    ) {
        if (hasSelectTradeInLocation) {
            renderNoSelectedCourierNormalShipping(
                shipmentCartItemModel,
                currentAddress,
                ratesDataConverter
            )
        } else {
            binding.shippingWidget.showLayoutTradeIn()
        }
    }

    private fun renderNoSelectedCourierNormalShipping(
        shipmentCartItemModel: ShipmentCartItemModel,
        currentAddress: RecipientAddressModel?,
        ratesDataConverter: RatesDataConverter
    ) {
        if (shipmentCartItemModel.isDisableChangeCourier) {
            if (shipmentCartItemModel.hasGeolocation) {
                renderFailShipmentState(shipmentCartItemModel, currentAddress, ratesDataConverter)
            }
        } else {
            binding.shippingWidget.showLayoutNoSelectedShipping(
                shipmentCartItemModel,
                currentAddress!!
            )
        }
    }

    private fun renderFailShipmentState(
        shipmentCartItemModel: ShipmentCartItemModel,
        recipientAddressModel: RecipientAddressModel?,
        ratesDataConverter: RatesDataConverter
    ) {
        binding.shippingWidget.showLayoutStateFailedShipping(
            shipmentCartItemModel,
            recipientAddressModel!!
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

    private fun initScheduleDeliveryPublisher() {
        if (scheduleDeliverySubscription?.isUnsubscribed == false) {
            scheduleDeliverySubscription?.unsubscribe()
        }
        if (scheduleDeliveryDonePublisher?.hasCompleted() == false) {
            scheduleDeliveryDonePublisher?.onCompleted()
        }
        scheduleDeliverySubscription = Observable.create(
            Action1 { emitter: Emitter<ShipmentScheduleDeliveryHolderData> ->
                scheduleDeliveryDebouncedListener =
                    object : ScheduleDeliveryDebouncedListener {
                        override fun onScheduleDeliveryChanged(shipmentScheduleDeliveryHolderData: ShipmentScheduleDeliveryHolderData?) {
                            emitter.onNext(shipmentScheduleDeliveryHolderData)
                        }
                    }
            } as Action1<Emitter<ShipmentScheduleDeliveryHolderData>>,
            Emitter.BackpressureMode.LATEST
        )
            .observeOn(AndroidSchedulers.mainThread(), false, 1)
            .subscribeOn(AndroidSchedulers.mainThread())
            .concatMap { (scheduleDeliveryUiModel, position): ShipmentScheduleDeliveryHolderData ->
                scheduleDeliveryDonePublisher = PublishSubject.create()
                actionListener?.onChangeScheduleDelivery(
                    scheduleDeliveryUiModel,
                    position,
                    scheduleDeliveryDonePublisher!!
                )
                scheduleDeliveryDonePublisher
            }
            .subscribe()
        scheduleDeliveryCompositeSubscription?.add(scheduleDeliverySubscription)
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
            initScheduleDeliveryPublisher()
        }
    }

    override fun onViewErrorInCourierSection(logPromoDesc: String) {
        actionListener?.onViewErrorInCourierSection(logPromoDesc)
    }

    override fun onChangeScheduleDelivery(scheduleDeliveryUiModel: ScheduleDeliveryUiModel) {
        if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
            scheduleDeliveryDebouncedListener?.onScheduleDeliveryChanged(
                ShipmentScheduleDeliveryHolderData(scheduleDeliveryUiModel, bindingAdapterPosition)
            )
        }
    }

    override fun getHostFragmentManager(): FragmentManager {
        return actionListener?.currentFragmentManager!!
    }

    private interface SaveStateDebounceListener {

        fun onNeedToSaveState(shipmentCartItemModel: ShipmentCartItemModel?)
    }

    private interface ScheduleDeliveryDebouncedListener {

        fun onScheduleDeliveryChanged(shipmentScheduleDeliveryHolderData: ShipmentScheduleDeliveryHolderData?)
    }

    interface Listener {

        fun onClickExpandSubtotal(position: Int, shipmentGroupFooter: ShipmentGroupFooterModel)
    }
}
