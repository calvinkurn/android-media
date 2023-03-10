package com.tokopedia.checkout.view.viewholder

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Paint
import android.graphics.Typeface
import android.text.Editable
import android.text.Spannable
import android.text.SpannableString
import android.text.TextUtils
import android.text.TextWatcher
import android.text.style.StyleSpan
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexboxLayout
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.checkout.R
import com.tokopedia.checkout.analytics.CheckoutScheduleDeliveryAnalytics.sendViewScheduledDeliveryWidgetOnTokopediaNowEvent
import com.tokopedia.checkout.analytics.CheckoutScheduleDeliveryAnalytics.sendViewUnavailableScheduledDeliveryEvent
import com.tokopedia.checkout.domain.mapper.ShipmentMapper
import com.tokopedia.checkout.utils.WeightFormatterUtil.getFormattedWeight
import com.tokopedia.checkout.view.ShipmentAdapterActionListener
import com.tokopedia.checkout.view.adapter.ShipmentInnerProductListAdapter
import com.tokopedia.checkout.view.converter.RatesDataConverter
import com.tokopedia.checkout.view.helper.ShipmentScheduleDeliveryHolderData
import com.tokopedia.checkout.view.viewholder.ShipmentCartItemViewHolder.ShipmentItemListener
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.setTextAndContentDescription
import com.tokopedia.logisticCommon.data.constant.CourierConstant
import com.tokopedia.logisticCommon.data.constant.InsuranceConstant
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.logisticcart.shipping.features.shippingwidget.ShippingWidget
import com.tokopedia.logisticcart.shipping.features.shippingwidget.ShippingWidget.ShippingWidgetListener
import com.tokopedia.logisticcart.shipping.model.CartItemModel
import com.tokopedia.logisticcart.shipping.model.CourierItemData
import com.tokopedia.logisticcart.shipping.model.ScheduleDeliveryUiModel
import com.tokopedia.logisticcart.shipping.model.ShipmentCartItemModel
import com.tokopedia.logisticcart.shipping.model.ShipmentDetailData
import com.tokopedia.purchase_platform.common.feature.bottomsheet.GeneralBottomSheet
import com.tokopedia.purchase_platform.common.feature.bottomsheet.InsuranceBottomSheet
import com.tokopedia.purchase_platform.common.feature.gifting.data.model.AddOnWordingModel
import com.tokopedia.purchase_platform.common.feature.gifting.view.ButtonGiftingAddOnView
import com.tokopedia.purchase_platform.common.prefs.PlusCoachmarkPrefs
import com.tokopedia.purchase_platform.common.utils.Utils.getHtmlFormat
import com.tokopedia.purchase_platform.common.utils.Utils.removeDecimalSuffix
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.CardUnify
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.TextFieldUnify
import com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.Ticker.Companion.SHAPE_LOOSE
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.currency.CurrencyFormatUtil.convertPriceValueToIdrFormat
import rx.Emitter
import rx.Observable
import rx.Subscriber
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Action1
import rx.schedulers.Schedulers
import rx.subjects.PublishSubject
import rx.subscriptions.CompositeSubscription
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern

/**
 * @author Irfan Khoirul on 23/04/18.
 */
@Deprecated("Use disassembled view holders!")
class ShipmentItemViewHolder(
    itemView: View,
    actionListener: ShipmentAdapterActionListener?,
    private var scheduleDeliveryCompositeSubscription: CompositeSubscription?
) : RecyclerView.ViewHolder(itemView),
    ShipmentItemListener,
    ShippingWidgetListener {

    private var mActionListener: ShipmentAdapterActionListener? = actionListener
    private var layoutError: LinearLayout
    private var tickerError: Ticker
    private var layoutWarning: LinearLayout
    private var tvShopName: Typography
    private var labelEpharmacy: Label
    private var layoutWarningAndError: LinearLayout
    private var tickerWarningCloseable: Ticker

    // Custom Error Ticker
    private var customTickerError: CardUnify
    private var customTickerDescription: Typography
    private var customTickerAction: Typography
    private var containerOrder: LinearLayout
    private var productBundlingInfo: ConstraintLayout
    private var imageBundle: ImageUnify
    private var textBundleTitle: Typography
    private var textBundlePrice: Typography
    private var textBundleSlashPrice: Typography
    private var llFrameItemProductContainer: LinearLayout
    private var rlProductInfo: ConstraintLayout
    private var vBundlingProductSeparator: View
    private var ivProductImage: ImageView
    private var tvProductName: TextView
    private var tvProductPrice: TextView
    private var tvProductOriginalPrice: TextView
    private var tvItemCountAndWeight: TextView
    private var tvOptionalNoteToSeller: TextView
    private var rlPurchaseProtection: RelativeLayout
    private var tvPPPLinkText: TextView
    private var tvPPPPrice: TextView
    private var cbPPP: CheckboxUnify
    private var rvCartItem: RecyclerView
    private var tvExpandOtherProduct: Typography
    private var ivExpandOtherProduct: IconUnify
    private var rlExpandOtherProduct: RelativeLayout
    private var ivDetailOptionChevron: IconUnify
    private var tvSubTotalPrice: Typography
    private var rlCartSubTotal: RelativeLayout
    private var tvTotalItem: Typography
    private var tvTotalItemPrice: Typography
    private var tvShippingFee: Typography
    private var tvShippingFeePrice: Typography
    private var tvInsuranceFee: Typography
    private var tvInsuranceFeePrice: Typography
    private var tvProtectionLabel: Typography
    private var tvProtectionFee: Typography
    private var rlShipmentCost: RelativeLayout
    private var llInsurance: LinearLayout
    private var cbInsurance: CheckboxUnify
    private var imgInsuranceInfo: ImageView
    private var llDropshipper: LinearLayout
    private var cbDropshipper: CheckboxUnify
    private var tvDropshipper: Typography
    private var imgDropshipperInfo: ImageView
    private var llDropshipperInfo: LinearLayout
    private var textInputLayoutShipperName: TextFieldUnify
    private var textInputLayoutShipperPhone: TextFieldUnify
    private var vSeparatorMultipleProductSameStore: View
    private var vSeparatorBelowProduct: View
    private var tvAdditionalFee: Typography
    private var tvAdditionalFeePrice: Typography
    private var tvLabelInsurance: TextView
    private var imgShopBadge: ImageView
    private var llShippingOptionsContainer: LinearLayout
    private var tickerProductError: Ticker
    private var productTicker: Ticker
    private var textVariant: Typography
    private var layoutProductInfo: FlexboxLayout
    private var tvTradeInLabel: TextView
    private var shipmentDataList: List<Any>? = null
    private var phoneNumberRegexPattern: Pattern
    private var compositeSubscription: CompositeSubscription
    private var saveStateDebounceListener: SaveStateDebounceListener? = null
    private var tvFulfillName: TextView
    private var imgFulfillmentBadge: ImageUnify
    private var separatorFreeShipping: Typography
    private var imgFreeShipping: ImageView
    private var textOrderNumber: Typography
    private var separatorPreOrder: Typography
    private var labelPreOrder: Label
    private var separatorIncidentShopLevel: Typography
    private var labelIncidentShopLevel: Label

    // order prioritas
    private var checkBoxPriority: CheckboxUnify
    private var tvPrioritasTicker: Typography
    private var llPrioritasTicker: LinearLayout
    private var llPrioritas: RelativeLayout
    private var tvPrioritasFee: Typography
    private var tvPrioritasFeePrice: Typography
    private var imgPriorityTnc: ImageView
    private var tvPrioritasInfo: Typography
    private var isPriorityChecked = false

    // Shipping Experience
    private var shippingWidget: ShippingWidget
    private var ratesDataConverter: RatesDataConverter? = null
    private var mIconTooltip: IconUnify
    private var mPricePerProduct: Typography

    // Gifting AddOn
    private var llGiftingAddOnOrderLevel: LinearLayout
    private var llGiftingAddOnProductLevel: LinearLayout
    private var buttonGiftingAddonOrderLevel: ButtonGiftingAddOnView
    private var buttonGiftingAddonProductLevel: ButtonGiftingAddOnView
    private var tvAddOnCostLabel: Typography
    private var tvAddOnPrice: Typography
    private var scheduleDeliveryDebouncedListener: ScheduleDeliveryDebouncedListener? = null
    private var scheduleDeliveryDonePublisher: PublishSubject<Boolean>? = null
    private var scheduleDeliverySubscription: Subscription? = null
    private var plusCoachmarkPrefs: PlusCoachmarkPrefs

    override fun notifyOnPurchaseProtectionChecked(checked: Boolean, position: Int) {
        if (adapterPosition != RecyclerView.NO_POSITION) {
            if (shipmentDataList?.get(adapterPosition) is ShipmentCartItemModel) {
                val data = shipmentDataList!![adapterPosition] as ShipmentCartItemModel
                data.cartItemModels[position].isProtectionOptIn = checked
                if (checked && cbDropshipper.isChecked && data.selectedShipmentDetailData?.useDropshipper == true) {
                    data.selectedShipmentDetailData?.useDropshipper = false
                    cbDropshipper.isChecked = false
                    mActionListener?.onPurchaseProtectionLogicError()
                }
            }
            mActionListener?.onNeedUpdateRequestData()
            mActionListener?.onPurchaseProtectionChangeListener(adapterPosition)
        }
    }

    override fun navigateToWebView(cartItemModel: CartItemModel) {
        mActionListener?.navigateToProtectionMore(cartItemModel)
    }

    override fun openAddOnProductLevelBottomSheet(
        cartItem: CartItemModel,
        addOnWordingModel: AddOnWordingModel
    ) {
        mActionListener?.openAddOnProductLevelBottomSheet(cartItem, addOnWordingModel)
    }

    override fun addOnProductLevelImpression(productId: String) {
        mActionListener?.addOnProductLevelImpression(productId)
    }

    fun bindViewHolder(
        shipmentCartItemModel: ShipmentCartItemModel,
        shipmentDataList: List<Any>?,
        recipientAddressModel: RecipientAddressModel?,
        ratesDataConverter: RatesDataConverter
    ) {
        if (this.shipmentDataList == null) {
            this.shipmentDataList = shipmentDataList
        }
        this.ratesDataConverter = ratesDataConverter
        renderShop(shipmentCartItemModel)
        renderFulfillment(shipmentCartItemModel)
        renderShipping(shipmentCartItemModel, recipientAddressModel, ratesDataConverter)
        renderPrioritas(shipmentCartItemModel)
        renderInsurance(shipmentCartItemModel)
        renderDropshipper(recipientAddressModel != null && recipientAddressModel.isCornerAddress)
        renderCostDetail(shipmentCartItemModel)
        renderCartItem(shipmentCartItemModel)
        renderErrorAndWarning(shipmentCartItemModel)
        renderCustomError(shipmentCartItemModel)
        renderShippingVibrationAnimation(shipmentCartItemModel)
        renderAddOnOrderLevel(shipmentCartItemModel, shipmentCartItemModel.addOnWordingModel)
    }

    fun unsubscribeDebouncer() {
        compositeSubscription.unsubscribe()
    }

    private fun renderShippingVibrationAnimation(shipmentCartItemModel: ShipmentCartItemModel) {
        shippingWidget.renderShippingVibrationAnimation(shipmentCartItemModel)
    }

    private fun renderFulfillment(model: ShipmentCartItemModel) {
        if (!TextUtils.isEmpty(model.shopLocation)) {
            if (model.isFulfillment && !TextUtils.isEmpty(model.fulfillmentBadgeUrl)) {
                ImageHandler.loadImageWithoutPlaceholderAndError(
                    imgFulfillmentBadge,
                    model.fulfillmentBadgeUrl
                )
                imgFulfillmentBadge.visibility = View.VISIBLE
            } else {
                imgFulfillmentBadge.visibility = View.GONE
            }
            tvFulfillName.visibility = View.VISIBLE
            tvFulfillName.text = model.shopLocation
        } else {
            imgFulfillmentBadge.visibility = View.GONE
            tvFulfillName.visibility = View.GONE
        }
    }

    private fun renderErrorAndWarning(shipmentCartItemModel: ShipmentCartItemModel) {
        if (shipmentCartItemModel.isError) {
            layoutWarningAndError.visibility = View.VISIBLE
        } else {
            layoutWarningAndError.visibility = View.GONE
        }
        renderError(shipmentCartItemModel)
        renderWarningCloseable(shipmentCartItemModel)
    }

    private fun renderCustomError(shipmentCartItemModel: ShipmentCartItemModel) {
        if ((
            !shipmentCartItemModel.isError && shipmentCartItemModel.isHasUnblockingError &&
                !TextUtils.isEmpty(shipmentCartItemModel.unblockingErrorMessage)
            ) && shipmentCartItemModel.firstProductErrorIndex > -1
        ) {
            val errorMessage = shipmentCartItemModel.unblockingErrorMessage
            layoutWarningAndError.visibility = View.VISIBLE
            tickerError.setHtmlDescription(errorMessage + " " + itemView.context.getString(R.string.checkout_ticker_lihat_cta_suffix))
            tickerError.setDescriptionClickEvent(object : TickerCallback {
                override fun onDescriptionViewClick(charSequence: CharSequence) {
                    mActionListener?.onClickLihatOnTickerOrderError(
                        shipmentCartItemModel.shopId.toString(),
                        errorMessage
                    )
                    if (!shipmentCartItemModel.isStateAllItemViewExpanded) {
                        shipmentCartItemModel.isTriggerScrollToErrorProduct = true
                        showAllProductListener(shipmentCartItemModel).onClick(tickerError)
                        return
                    }
                    scrollToErrorProduct(shipmentCartItemModel)
                }

                override fun onDismiss() {
                    // no op
                }
            })
            tickerError.tickerType = Ticker.TYPE_ERROR
            tickerError.tickerShape = SHAPE_LOOSE
            tickerError.closeButtonVisibility = View.GONE
            tickerError.visibility = View.VISIBLE
            layoutError.visibility = View.VISIBLE
            if (shipmentCartItemModel.isTriggerScrollToErrorProduct) {
                scrollToErrorProduct(shipmentCartItemModel)
            }
        }
    }

    private fun scrollToErrorProduct(shipmentCartItemModel: ShipmentCartItemModel) {
        rvCartItem.post {
            val firstProductErrorIndex = shipmentCartItemModel.firstProductErrorIndex - 1
            val child = rvCartItem.getChildAt(firstProductErrorIndex)
            var dy = 0f
            if (child != null) {
                dy = child.y
                var parent: View? = null
                while (rvCartItem !== parent) {
                    parent = child.parent as View
                    dy += parent.y
                }
            }
            shipmentCartItemModel.isTriggerScrollToErrorProduct = false
            mActionListener?.scrollToPositionWithOffset(adapterPosition, dy * -1)
        }
    }

    private fun renderCartItem(shipmentCartItemModel: ShipmentCartItemModel) {
        val cartItemModelList = ArrayList(shipmentCartItemModel.cartItemModels)
        if (cartItemModelList.size > 0) {
            renderFirstCartItem(
                cartItemModelList.removeAt(FIRST_ELEMENT),
                shipmentCartItemModel.addOnWordingModel
            )
        }
        if (shipmentCartItemModel.cartItemModels.size > 1) {
            rlExpandOtherProduct.visibility = View.VISIBLE
            renderOtherCartItems(shipmentCartItemModel, cartItemModelList)
            vSeparatorBelowProduct.visibility = View.VISIBLE
        } else {
            rlExpandOtherProduct.visibility = View.GONE
            rvCartItem.visibility = View.GONE
            vSeparatorMultipleProductSameStore.visibility = View.GONE
            vSeparatorBelowProduct.visibility = View.GONE
        }
    }

    private fun renderShop(shipmentCartItemModel: ShipmentCartItemModel) {
        if (shipmentCartItemModel.orderNumber > 0) {
            val orderlabel = String.format(
                itemView.context.getString(R.string.label_order_counter),
                shipmentCartItemModel.orderNumber
            )
            textOrderNumber.text = orderlabel
            textOrderNumber.visibility = View.VISIBLE
        } else {
            textOrderNumber.visibility = View.GONE
        }
        if (!TextUtils.isEmpty(shipmentCartItemModel.preOrderInfo)) {
            labelPreOrder.text = shipmentCartItemModel.preOrderInfo
            labelPreOrder.visibility = View.VISIBLE
            separatorPreOrder.visibility = View.VISIBLE
        } else {
            labelPreOrder.visibility = View.GONE
            separatorPreOrder.visibility = View.GONE
        }
        if (!TextUtils.isEmpty(shipmentCartItemModel.freeShippingBadgeUrl)) {
            ImageHandler.loadImageWithoutPlaceholderAndError(
                imgFreeShipping,
                shipmentCartItemModel.freeShippingBadgeUrl
            )
            if (shipmentCartItemModel.isFreeShippingPlus) {
                imgFreeShipping.contentDescription =
                    itemView.context.getString(com.tokopedia.purchase_platform.common.R.string.pp_cd_image_badge_plus)
            } else {
                imgFreeShipping.contentDescription =
                    itemView.context.getString(com.tokopedia.purchase_platform.common.R.string.pp_cd_image_badge_bo)
            }
            imgFreeShipping.visibility = View.VISIBLE
            separatorFreeShipping.visibility = View.VISIBLE
            if (!shipmentCartItemModel.hasSeenFreeShippingBadge && shipmentCartItemModel.isFreeShippingPlus) {
                shipmentCartItemModel.hasSeenFreeShippingBadge = true
                mActionListener?.onViewFreeShippingPlusBadge()
            }
        } else {
            imgFreeShipping.visibility = View.GONE
            separatorFreeShipping.visibility = View.GONE
        }
        if (!TextUtils.isEmpty(shipmentCartItemModel.shopAlertMessage)) {
            labelIncidentShopLevel.text = shipmentCartItemModel.shopAlertMessage
            labelIncidentShopLevel.visibility = View.VISIBLE
            separatorIncidentShopLevel.visibility = View.VISIBLE
        } else {
            labelIncidentShopLevel.visibility = View.GONE
            separatorIncidentShopLevel.visibility = View.GONE
        }
        var hasTradeInItem = false
        for (cartItemModel in shipmentCartItemModel.cartItemModels) {
            if (cartItemModel.isValidTradeIn) {
                hasTradeInItem = true
                break
            }
        }
        if (hasTradeInItem) {
            tvTradeInLabel.visibility = View.VISIBLE
        } else {
            tvTradeInLabel.visibility = View.GONE
        }
        if (shipmentCartItemModel.shopTypeInfoData.shopBadge.isNotEmpty()) {
            ImageHandler.loadImageWithoutPlaceholder(
                imgShopBadge,
                shipmentCartItemModel.shopTypeInfoData.shopBadge
            )
            imgShopBadge.visibility = View.VISIBLE
            imgShopBadge.contentDescription = itemView.context.getString(
                com.tokopedia.purchase_platform.common.R.string.pp_cd_image_shop_badge_with_shop_type,
                shipmentCartItemModel.shopTypeInfoData.title.lowercase(
                    Locale.getDefault()
                )
            )
        } else {
            imgShopBadge.visibility = View.GONE
        }
        val shopName = shipmentCartItemModel.shopName
        tvShopName.text = shopName
        if (TextUtils.isEmpty(shipmentCartItemModel.enablerLabel)) {
            labelEpharmacy.visibility = View.GONE
        } else {
            labelEpharmacy.setLabel(shipmentCartItemModel.enablerLabel)
            labelEpharmacy.visibility = View.VISIBLE
        }
    }

    @SuppressLint("StringFormatInvalid")
    private fun renderFirstCartItem(
        cartItemModel: CartItemModel,
        addOnWordingModel: AddOnWordingModel?
    ) {
        if (cartItemModel.isError) {
            showShipmentWarning(cartItemModel)
        } else {
            hideShipmentWarning()
        }
        ImageHandler.LoadImage(ivProductImage, cartItemModel.imageUrl)
        tvProductName.text = cartItemModel.name
        tvItemCountAndWeight.text = String.format(
            tvItemCountAndWeight.context
                .getString(R.string.iotem_count_and_weight_format),
            cartItemModel.quantity.toString(),
            getFormattedWeight(cartItemModel.weight, cartItemModel.quantity)
        )
        if (!TextUtils.isEmpty(cartItemModel.variant)) {
            textVariant.text = cartItemModel.variant
            textVariant.visibility = View.VISIBLE
        } else {
            textVariant.visibility = View.GONE
        }
        renderProductPrice(cartItemModel)
        renderNotesForSeller(cartItemModel)
        renderPurchaseProtection(cartItemModel)
        renderProductTicker(cartItemModel)
        renderProductProperties(cartItemModel)
        renderBundlingInfo(cartItemModel)
        renderAddOnProductLevel(cartItemModel, addOnWordingModel)
    }

    private fun renderBundlingInfo(cartItemModel: CartItemModel) {
        val ivProductImageLayoutParams =
            ivProductImage.layoutParams as ViewGroup.MarginLayoutParams
        val tvOptionalNoteToSellerLayoutParams =
            tvOptionalNoteToSeller.layoutParams as ViewGroup.MarginLayoutParams
        val productContainerLayoutParams =
            llFrameItemProductContainer.layoutParams as ViewGroup.MarginLayoutParams
        val productInfoLayoutParams = rlProductInfo.layoutParams as ViewGroup.MarginLayoutParams
        val bottomMargin =
            itemView.resources.getDimensionPixelSize(com.tokopedia.abstraction.R.dimen.dp_8)
        if (cartItemModel.isBundlingItem) {
            if (!TextUtils.isEmpty(cartItemModel.bundleIconUrl)) {
                ImageHandler.loadImage2(
                    imageBundle,
                    cartItemModel.bundleIconUrl,
                    com.tokopedia.kotlin.extensions.R.drawable.ic_loading_placeholder
                )
            }
            ivProductImageLayoutParams.leftMargin =
                itemView.resources.getDimensionPixelSize(com.tokopedia.abstraction.R.dimen.dp_14)
            tvOptionalNoteToSellerLayoutParams.leftMargin =
                itemView.resources.getDimensionPixelSize(com.tokopedia.abstraction.R.dimen.dp_14)
            vBundlingProductSeparator.visibility = View.VISIBLE
            if (cartItemModel.bundlingItemPosition == ShipmentMapper.BUNDLING_ITEM_HEADER) {
                productBundlingInfo.visibility = View.VISIBLE
                vSeparatorMultipleProductSameStore.visibility = View.VISIBLE
            } else {
                productBundlingInfo.visibility = View.GONE
                vSeparatorMultipleProductSameStore.visibility = View.GONE
            }
            textBundleTitle.text = cartItemModel.bundleTitle
            textBundlePrice.text = removeDecimalSuffix(
                convertPriceValueToIdrFormat(
                    cartItemModel.bundlePrice,
                    false
                )
            )
            textBundleSlashPrice.text =
                removeDecimalSuffix(
                    convertPriceValueToIdrFormat(
                        cartItemModel.bundleOriginalPrice,
                        false
                    )
                )
            textBundleSlashPrice.paintFlags =
                textBundleSlashPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            productContainerLayoutParams.bottomMargin = 0
            productInfoLayoutParams.bottomMargin = 0
        } else {
            ivProductImageLayoutParams.leftMargin = 0
            tvOptionalNoteToSellerLayoutParams.leftMargin = 0
            vBundlingProductSeparator.visibility = View.GONE
            productBundlingInfo.visibility = View.GONE
            vSeparatorMultipleProductSameStore.visibility = View.VISIBLE
            productContainerLayoutParams.bottomMargin = bottomMargin
            productInfoLayoutParams.bottomMargin = bottomMargin
        }
    }

    private fun renderAddOnProductLevel(
        cartItemModel: CartItemModel,
        addOnWordingModel: AddOnWordingModel?
    ) {
        val (status, _, addOnsButtonModel) = cartItemModel.addOnProductLevelModel
        if (status == 0) {
            llGiftingAddOnProductLevel.visibility = View.GONE
        } else {
            if (status == 1) {
                buttonGiftingAddonProductLevel.state = ButtonGiftingAddOnView.State.ACTIVE
            } else if (status == 2) {
                buttonGiftingAddonProductLevel.state = ButtonGiftingAddOnView.State.INACTIVE
            }
            llGiftingAddOnProductLevel.visibility = View.VISIBLE
            buttonGiftingAddonProductLevel.title = addOnsButtonModel.title
            buttonGiftingAddonProductLevel.desc = addOnsButtonModel.description
            buttonGiftingAddonProductLevel.urlLeftIcon = addOnsButtonModel.leftIconUrl
            buttonGiftingAddonProductLevel.urlRightIcon = addOnsButtonModel.rightIconUrl
            buttonGiftingAddonProductLevel.setOnClickListener { view: View? ->
                mActionListener?.openAddOnProductLevelBottomSheet(
                    cartItemModel,
                    addOnWordingModel
                )
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun renderProductProperties(cartItemModel: CartItemModel) {
        val productInformationList = cartItemModel.productInformation
        layoutProductInfo.removeAllViews()
        if (productInformationList.isNotEmpty()) {
            for (i in productInformationList.indices) {
                val productInfo = Typography(itemView.context)
                productInfo.setTextColor(
                    ContextCompat.getColor(
                        itemView.context,
                        com.tokopedia.unifyprinciples.R.color.Unify_N700_68
                    )
                )
                productInfo.setType(Typography.SMALL)
                if (layoutProductInfo.childCount > 0) {
                    productInfo.text = ", " + productInformationList[i]
                } else {
                    productInfo.text = productInformationList[i]
                }
                layoutProductInfo.addView(productInfo)
            }
            layoutProductInfo.visibility = View.VISIBLE
        } else {
            layoutProductInfo.visibility = View.GONE
        }
        renderEthicalDrugsProperty(cartItemModel)
    }

    private fun renderEthicalDrugsProperty(cartItemModel: CartItemModel) {
        if (cartItemModel.ethicalDrugDataModel.needPrescription) {
            val ethicalDrugView: View = createProductInfoTextWithIcon(cartItemModel)
            if (layoutProductInfo.childCount > 0) {
                ethicalDrugView.setPadding(
                    itemView.resources.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.unify_space_4),
                    0,
                    0,
                    0
                )
            }
            layoutProductInfo.addView(ethicalDrugView)
            layoutProductInfo.visibility = View.VISIBLE
        }
    }

    private fun createProductInfoTextWithIcon(cartItemModel: CartItemModel): LinearLayout {
        val propertyLayoutWithIcon = LinearLayout(itemView.context)
        propertyLayoutWithIcon.orientation = LinearLayout.HORIZONTAL
        val propertiesBinding = LayoutInflater.from(itemView.context)
            .inflate(com.tokopedia.purchase_platform.common.R.layout.item_product_info_add_on, null)
        val propertyIcon =
            propertiesBinding.findViewById<ImageUnify>(com.tokopedia.purchase_platform.common.R.id.pp_iv_product_info_add_on)
        if (propertyIcon != null && !TextUtils.isEmpty(cartItemModel.ethicalDrugDataModel.iconUrl)) {
            ImageHandler.loadImageWithoutPlaceholderAndError(
                propertyIcon,
                cartItemModel.ethicalDrugDataModel.iconUrl
            )
        }
        val propertyText =
            propertiesBinding.findViewById<Typography>(com.tokopedia.purchase_platform.common.R.id.pp_label_product_info_add_on)
        if (propertyText != null && !TextUtils.isEmpty(cartItemModel.ethicalDrugDataModel.text)) {
            propertyText.text = cartItemModel.ethicalDrugDataModel.text
        }
        propertyLayoutWithIcon.addView(propertiesBinding)
        return propertyLayoutWithIcon
    }

    private fun renderProductPrice(cartItemModel: CartItemModel) {
        tvProductPrice.text = removeDecimalSuffix(
            convertPriceValueToIdrFormat(
                cartItemModel.price.toLong(),
                false
            )
        )
        val dp4 =
            tvProductPrice.resources.getDimensionPixelOffset(com.tokopedia.abstraction.R.dimen.dp_4)
        if (cartItemModel.originalPrice > 0) {
            tvProductPrice.setPadding(0, dp4, 0, 0)
            tvProductOriginalPrice.setPadding(0, dp4, 0, 0)
            tvProductOriginalPrice.text =
                removeDecimalSuffix(
                    convertPriceValueToIdrFormat(
                        cartItemModel.originalPrice,
                        false
                    )
                )
            tvProductOriginalPrice.paintFlags =
                tvProductOriginalPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            tvProductOriginalPrice.visibility = View.VISIBLE
        } else {
            tvProductPrice.setPadding(0, dp4, 0, 0)
            tvProductOriginalPrice.visibility = View.GONE
        }
    }

    private fun renderNotesForSeller(cartItemModel: CartItemModel) {
        if (!TextUtils.isEmpty(cartItemModel.noteToSeller)) {
            tvOptionalNoteToSeller.text =
                getHtmlFormat(cartItemModel.noteToSeller)
            tvOptionalNoteToSeller.visibility = View.VISIBLE
        } else {
            tvOptionalNoteToSeller.visibility = View.GONE
        }
    }

    private fun renderPurchaseProtection(cartItemModel: CartItemModel) {
        rlPurchaseProtection.visibility =
            if (cartItemModel.isProtectionAvailable && !cartItemModel.isError) View.VISIBLE else View.GONE
        if (cartItemModel.isProtectionAvailable && !cartItemModel.isError) {
            mIconTooltip.setOnClickListener {
                mActionListener?.navigateToProtectionMore(
                    cartItemModel
                )
            }
            tvPPPLinkText.text = cartItemModel.protectionTitle
            tvPPPPrice.text = cartItemModel.protectionSubTitle
            mPricePerProduct.text = removeDecimalSuffix(
                convertPriceValueToIdrFormat(
                    cartItemModel.protectionPricePerProduct.toLong(),
                    false
                )
            )
            if (cartItemModel.isProtectionCheckboxDisabled) {
                cbPPP.isEnabled = false
                cbPPP.isChecked = true
                cbPPP.skipAnimation()
            } else {
                cbPPP.isEnabled = true
                cbPPP.isChecked = cartItemModel.isProtectionOptIn
                cbPPP.skipAnimation()
                cbPPP.setOnCheckedChangeListener { _: CompoundButton?, checked: Boolean ->
                    notifyOnPurchaseProtectionChecked(
                        checked,
                        0
                    )
                }
            }
        }
    }

    private fun renderProductTicker(cartItemModel: CartItemModel) {
        if (cartItemModel.isShowTicker && !TextUtils.isEmpty(cartItemModel.tickerMessage)) {
            productTicker.visibility = View.VISIBLE
            productTicker.setTextDescription(cartItemModel.tickerMessage)
        } else {
            productTicker.visibility = View.GONE
        }
    }

    @SuppressLint("StringFormatInvalid")
    private fun renderOtherCartItems(
        shipmentItem: ShipmentCartItemModel,
        cartItemModels: List<CartItemModel>
    ) {
        rlExpandOtherProduct.setOnClickListener(showAllProductListener(shipmentItem))
        initInnerRecyclerView(cartItemModels, shipmentItem.addOnWordingModel)
        if (shipmentItem.isStateAllItemViewExpanded) {
            rvCartItem.visibility = View.VISIBLE
            vSeparatorMultipleProductSameStore.visibility = View.GONE
            tvExpandOtherProduct.setText(R.string.label_hide_other_item_new)
            ivExpandOtherProduct.setImage(IconUnify.CHEVRON_UP, null, null, null, null)
        } else {
            rvCartItem.visibility = View.GONE
            vSeparatorMultipleProductSameStore.visibility = View.GONE
            tvExpandOtherProduct.text = String.format(
                tvExpandOtherProduct.context.getString(R.string.label_show_other_item_count),
                cartItemModels.size
            )
            ivExpandOtherProduct.setImage(IconUnify.CHEVRON_DOWN, null, null, null, null)
        }
    }

    private fun renderShipping(
        shipmentCartItemModel: ShipmentCartItemModel,
        currentAddress: RecipientAddressModel?,
        ratesDataConverter: RatesDataConverter
    ) {
        shippingWidget.hideTradeInShippingInfo()
        if (shipmentCartItemModel.isError) {
            renderErrorCourierState(shipmentCartItemModel)
            return
        }
        var selectedCourierItemData: CourierItemData? = null
        val isTradeInDropOff = mActionListener?.isTradeInByDropOff ?: false
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
        llShippingOptionsContainer.visibility = View.VISIBLE
        shippingWidget.renderErrorCourierState(shipmentCartItemModel)
    }

    private fun renderSelectedCourier(
        shipmentCartItemModel: ShipmentCartItemModel,
        currentAddress: RecipientAddressModel?,
        selectedCourierItemData: CourierItemData
    ) {
        llShippingOptionsContainer.visibility = View.VISIBLE
        shippingWidget.showContainerShippingExperience()
        if (shipmentCartItemModel.isShowScheduleDelivery) {
            sendScheduleDeliveryAnalytics(shipmentCartItemModel, selectedCourierItemData)
            // Show Schedule delivery widget
            shippingWidget.renderScheduleDeliveryWidget(
                shipmentCartItemModel,
                selectedCourierItemData
            )
        } else if (shipmentCartItemModel.isDisableChangeCourier) {
            // Is single shipping only
            shippingWidget.renderSingleShippingCourier(
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
            shippingWidget.renderNormalShippingWithoutChooseCourierCard(
                shipmentCartItemModel,
                currentAddress!!,
                selectedCourierItemData
            )
        } else {
            // Is normal shipping
            shippingWidget.renderNormalShippingCourier(
                shipmentCartItemModel,
                currentAddress!!,
                selectedCourierItemData
            )
        }
        showMultiplePlusOrderCoachmark(
            shipmentCartItemModel,
            shippingWidget.containerShippingExperience
        )
    }

    override fun onChangeDurationClickListener(
        shipmentCartItemModel: ShipmentCartItemModel,
        currentAddress: RecipientAddressModel
    ) {
        if (adapterPosition != RecyclerView.NO_POSITION) {
            mActionListener?.onChangeShippingDuration(
                shipmentCartItemModel,
                currentAddress,
                adapterPosition
            )
        }
    }

    override fun onChangeCourierClickListener(
        shipmentCartItemModel: ShipmentCartItemModel,
        currentAddress: RecipientAddressModel
    ) {
        if (adapterPosition != RecyclerView.NO_POSITION) {
            mActionListener?.onChangeShippingCourier(
                currentAddress,
                shipmentCartItemModel,
                adapterPosition,
                null
            )
        }
    }

    override fun onOnTimeDeliveryClicked(url: String) {
        mActionListener?.onOntimeDeliveryClicked(url)
    }

    override fun onClickSetPinpoint() {
        mActionListener?.onClickSetPinpoint(adapterPosition)
    }

    override fun onClickLayoutFailedShipping(
        shipmentCartItemModel: ShipmentCartItemModel,
        recipientAddressModel: RecipientAddressModel
    ) {
        val tmpShipmentDetailData = ratesDataConverter!!.getShipmentDetailData(
            shipmentCartItemModel,
            recipientAddressModel
        )
        val position = adapterPosition
        if (position != RecyclerView.NO_POSITION) {
            loadCourierStateData(
                shipmentCartItemModel,
                SHIPPING_SAVE_STATE_TYPE_SHIPPING_EXPERIENCE,
                tmpShipmentDetailData,
                position
            )
            mActionListener?.onClickRefreshErrorLoadCourier()
            initScheduleDeliveryPublisher()
        }
    }

    override fun onViewErrorInCourierSection(logPromoDesc: String) {
        mActionListener?.onViewErrorInCourierSection(logPromoDesc)
    }

    override fun onChangeScheduleDelivery(scheduleDeliveryUiModel: ScheduleDeliveryUiModel) {
        val position = adapterPosition
        if (position != RecyclerView.NO_POSITION) {
            scheduleDeliveryDebouncedListener?.onScheduleDeliveryChanged(
                ShipmentScheduleDeliveryHolderData(
                    scheduleDeliveryUiModel,
                    position
                )
            )
        }
    }

    private fun renderFreeShippingCourier(
        shipmentCartItemModel: ShipmentCartItemModel,
        currentAddress: RecipientAddressModel,
        selectedCourierItemData: CourierItemData
    ) {
        shippingWidget.showLayoutFreeShippingCourier(shipmentCartItemModel, currentAddress)
        if (shipmentCartItemModel.isError) {
            mActionListener?.onCancelVoucherLogisticClicked(
                shipmentCartItemModel.voucherLogisticItemUiModel!!.code,
                adapterPosition,
                shipmentCartItemModel
            )
        }
        shippingWidget.renderFreeShippingCourier(selectedCourierItemData)
    }

    private fun prepareLoadCourierState(
        shipmentCartItemModel: ShipmentCartItemModel,
        currentAddress: RecipientAddressModel?,
        ratesDataConverter: RatesDataConverter,
        isTradeInDropOff: Boolean
    ) {
        shippingWidget.prepareLoadCourierState()
        llShippingOptionsContainer.visibility = View.GONE
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
            shippingWidget.showLayoutNoSelectedShipping(shipmentCartItemModel, currentAddress!!)
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
            shippingWidget.showLayoutTradeIn()
        }
    }

    private fun renderFailShipmentState(
        shipmentCartItemModel: ShipmentCartItemModel,
        recipientAddressModel: RecipientAddressModel?,
        ratesDataConverter: RatesDataConverter
    ) {
        shippingWidget.showLayoutStateFailedShipping(
            shipmentCartItemModel,
            recipientAddressModel!!
        )
    }

    private fun renderAddOnOrderLevel(
        shipmentCartItemModel: ShipmentCartItemModel,
        addOnWordingModel: AddOnWordingModel?
    ) {
        val addOnsDataModel = shipmentCartItemModel.addOnsOrderLevelModel
        if (addOnsDataModel != null) {
            val addOnsButton = addOnsDataModel.addOnsButtonModel
            val statusAddOn = addOnsDataModel.status
            if (statusAddOn == 0) {
                llGiftingAddOnOrderLevel.visibility = View.GONE
            } else {
                if (statusAddOn == 1) {
                    buttonGiftingAddonOrderLevel.state = ButtonGiftingAddOnView.State.ACTIVE
                } else if (statusAddOn == 2) {
                    buttonGiftingAddonOrderLevel.state = ButtonGiftingAddOnView.State.INACTIVE
                }
                llGiftingAddOnOrderLevel.visibility = View.VISIBLE
                buttonGiftingAddonOrderLevel.title = addOnsButton.title
                buttonGiftingAddonOrderLevel.desc = addOnsButton.description
                buttonGiftingAddonOrderLevel.urlLeftIcon = addOnsButton.leftIconUrl
                buttonGiftingAddonOrderLevel.urlRightIcon = addOnsButton.rightIconUrl
                buttonGiftingAddonOrderLevel.setOnClickListener(
                    openAddOnOrderLevelBottomSheet(
                        shipmentCartItemModel,
                        addOnWordingModel
                    )
                )
                mActionListener?.addOnOrderLevelImpression(shipmentCartItemModel.cartItemModels)
            }
        }
    }

    private fun openAddOnOrderLevelBottomSheet(
        shipmentCartItemModel: ShipmentCartItemModel,
        addOnWordingModel: AddOnWordingModel?
    ): View.OnClickListener {
        return View.OnClickListener { view: View? ->
            mActionListener?.openAddOnOrderLevelBottomSheet(
                shipmentCartItemModel,
                addOnWordingModel
            )
        }
    }

    private fun loadCourierState(
        shipmentCartItemModel: ShipmentCartItemModel,
        recipientAddressModel: RecipientAddressModel?,
        ratesDataConverter: RatesDataConverter,
        saveStateType: Int
    ) {
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
                        val position = adapterPosition
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

    private fun renderNoSelectedCourier(
        shipmentCartItemModel: ShipmentCartItemModel,
        recipientAddressModel: RecipientAddressModel?,
        ratesDataConverter: RatesDataConverter,
        saveStateType: Int
    ) {
        when (saveStateType) {
            SHIPPING_SAVE_STATE_TYPE_TRADE_IN_DROP_OFF -> {
                val hasSelectTradeInLocation = mActionListener?.hasSelectTradeInLocation() ?: false
                renderNoSelectedCourierTradeInDropOff(
                    shipmentCartItemModel,
                    recipientAddressModel,
                    ratesDataConverter,
                    hasSelectTradeInLocation
                )
            }
            SHIPPING_SAVE_STATE_TYPE_SHIPPING_EXPERIENCE -> renderNoSelectedCourierNormalShipping(
                shipmentCartItemModel,
                recipientAddressModel,
                ratesDataConverter
            )
        }
    }

    private fun renderErrorPinpointCourier() {
        llShippingOptionsContainer.visibility = View.VISIBLE
        shippingWidget.renderErrorPinpointCourier()
    }

    private fun loadCourierStateData(
        shipmentCartItemModel: ShipmentCartItemModel,
        saveStateType: Int,
        tmpShipmentDetailData: ShipmentDetailData,
        position: Int
    ) {
        mActionListener?.onLoadShippingState(
            shipmentCartItemModel.shippingId,
            shipmentCartItemModel.spId,
            position,
            tmpShipmentDetailData,
            shipmentCartItemModel,
            shipmentCartItemModel.shopShipmentList,
            saveStateType == SHIPPING_SAVE_STATE_TYPE_TRADE_IN_DROP_OFF
        )
        shipmentCartItemModel.isStateLoadingCourierState = true
        shippingWidget.onLoadCourierStateData()
        when (saveStateType) {
            SHIPPING_SAVE_STATE_TYPE_TRADE_IN_DROP_OFF -> {
                shipmentCartItemModel.isStateHasLoadCourierTradeInDropOffState = true
                shippingWidget.hideTradeInTitleAndDetail()
            }
            SHIPPING_SAVE_STATE_TYPE_SHIPPING_EXPERIENCE ->
                shipmentCartItemModel.isStateHasLoadCourierState =
                    true
        }
    }

    private fun renderLoadingCourierState() {
        shippingWidget.renderLoadingCourierState()
    }

    private fun renderCostDetail(shipmentCartItemModel: ShipmentCartItemModel) {
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
        var totalItemLabel =
            tvTotalItem.context.getString(R.string.label_item_count_without_format)
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
        val voucherLogisticItemUiModel = shipmentCartItemModel.voucherLogisticItemUiModel
        val selectedShipmentDetailData = shipmentCartItemModel.selectedShipmentDetailData
        val selectedCourier = selectedShipmentDetailData?.selectedCourier
        if (
            (
                selectedCourier != null ||
                    selectedShipmentDetailData?.selectedCourierTradeInDropOff != null
                ) &&
            !shipmentCartItemModel.isError
        ) {
            var courierItemData: CourierItemData? = null
            if (mActionListener?.isTradeInByDropOff == true && selectedShipmentDetailData.selectedCourierTradeInDropOff != null) {
                courierItemData =
                    selectedShipmentDetailData.selectedCourierTradeInDropOff
            } else if (mActionListener?.isTradeInByDropOff == false && selectedCourier != null) {
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
                removeDecimalSuffix(
                    convertPriceValueToIdrFormat(subTotalPrice, false)
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
                    removeDecimalSuffix(
                        convertPriceValueToIdrFormat(0.0, false)
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
        tvPrioritasFeePrice.text =
            getPriceFormat(tvPrioritasFee, tvPrioritasFeePrice, priorityPrice.toLong())
        tvProtectionLabel.text = totalPPPItemLabel
        tvProtectionFee.text =
            getPriceFormat(tvProtectionLabel, tvProtectionFee, totalPurchaseProtectionPrice)
        tvAdditionalFeePrice.text =
            getPriceFormat(tvAdditionalFee, tvAdditionalFeePrice, additionalPrice.toLong())
        if (hasAddOnSelected) {
            tvAddOnCostLabel.visibility = View.VISIBLE
            tvAddOnPrice.visibility = View.VISIBLE
            tvAddOnPrice.text =
                removeDecimalSuffix(convertPriceValueToIdrFormat(totalAddOnPrice, false))
        } else {
            tvAddOnCostLabel.visibility = View.GONE
            tvAddOnPrice.visibility = View.GONE
        }
        rlCartSubTotal.setOnClickListener(getCostDetailOptionListener(shipmentCartItemModel))
    }

    private fun renderDropshipper(isCorner: Boolean) {
        if (shipmentDataList != null && adapterPosition != RecyclerView.NO_POSITION) {
            val shipmentCartItemModel = shipmentDataList!![adapterPosition] as ShipmentCartItemModel
            val isTradeInDropOff = mActionListener?.isTradeInByDropOff ?: false
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
                    mActionListener?.hideSoftKeyboard()
                    if (checked && isHavingPurchaseProtectionChecked) {
                        compoundButton.isChecked = false
                        mActionListener?.onPurchaseProtectionLogicError()
                        return@setOnCheckedChangeListener
                    }
                    if (adapterPosition != RecyclerView.NO_POSITION &&
                        shipmentDataList!![adapterPosition] is ShipmentCartItemModel
                    ) {
                        val data = shipmentDataList!![adapterPosition] as ShipmentCartItemModel
                        data.selectedShipmentDetailData!!.useDropshipper = checked
                        if (checked) {
                            textInputLayoutShipperName.textFieldInput.setText(data.dropshiperName)
                            textInputLayoutShipperPhone.textFieldInput.setText(data.dropshiperPhone)
                            data.selectedShipmentDetailData!!.dropshipperName = data.dropshiperName
                            data.selectedShipmentDetailData!!.dropshipperPhone =
                                data.dropshiperPhone
                            llDropshipperInfo.visibility = View.VISIBLE
                            mActionListener?.onDropshipCheckedForTrackingAnalytics()
                        } else {
                            textInputLayoutShipperName.textFieldInput.setText("")
                            textInputLayoutShipperPhone.textFieldInput.setText("")
                            data.selectedShipmentDetailData!!.dropshipperName = ""
                            data.selectedShipmentDetailData!!.dropshipperPhone = ""
                            data.dropshiperName = ""
                            data.dropshiperPhone = ""
                            llDropshipperInfo.visibility = View.GONE
                            data.isStateDropshipperHasError = false
                        }
                        mActionListener?.onNeedUpdateViewItem(adapterPosition)
                        mActionListener?.onNeedUpdateRequestData()
                        saveStateDebounceListener?.onNeedToSaveState(data)
                    }
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
                    tvDropshipper.setTextColor(
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
                    tvDropshipper.setTextColor(
                        ContextCompat.getColor(
                            itemView.context,
                            com.tokopedia.unifyprinciples.R.color.Unify_N700_68
                        )
                    )
                    llDropshipper.setOnClickListener(dropshipperClickListener)
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
                            if (adapterPosition != RecyclerView.NO_POSITION &&
                                shipmentDataList!![adapterPosition] is ShipmentCartItemModel
                            ) {
                                val data = shipmentDataList!![adapterPosition] as ShipmentCartItemModel
                                if (data.selectedShipmentDetailData != null) {
                                    if (!TextUtils.isEmpty(charSequence)) {
                                        data.selectedShipmentDetailData!!.dropshipperName =
                                            charSequence.toString()
                                        validateDropshipperName(data, charSequence, true)
                                        saveStateDebounceListener?.onNeedToSaveState(data)
                                    }
                                }
                            }
                        }

                        override fun afterTextChanged(editable: Editable) {}
                    })
                if (!TextUtils.isEmpty(selectedShipmentDetailData.dropshipperName) ||
                    !TextUtils.isEmpty(shipmentCartItemModel.dropshiperName)
                ) {
                    textInputLayoutShipperName.textFieldInput.setText(selectedShipmentDetailData!!.dropshipperName)
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
                textInputLayoutShipperName.textFieldInput.setSelection(textInputLayoutShipperName.textFieldInput.length())
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
                            if (adapterPosition != RecyclerView.NO_POSITION &&
                                shipmentDataList!![adapterPosition] is ShipmentCartItemModel
                            ) {
                                val data = shipmentDataList!![adapterPosition] as ShipmentCartItemModel
                                if (data.selectedShipmentDetailData != null) {
                                    if (!TextUtils.isEmpty(charSequence)) {
                                        data.selectedShipmentDetailData!!.dropshipperPhone =
                                            charSequence.toString()
                                        validateDropshipperPhone(data, charSequence, true)
                                        saveStateDebounceListener?.onNeedToSaveState(data)
                                    }
                                }
                            }
                        }

                        override fun afterTextChanged(editable: Editable) {}
                    })
                if (!TextUtils.isEmpty(selectedShipmentDetailData.dropshipperPhone) ||
                    !TextUtils.isEmpty(shipmentCartItemModel.dropshiperPhone)
                ) {
                    textInputLayoutShipperPhone.textFieldInput.setText(selectedShipmentDetailData!!.dropshipperPhone)
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

    private fun checkDropshipperState(shipmentCartItemModel: ShipmentCartItemModel) {
        cbDropshipper.isChecked = !TextUtils.isEmpty(shipmentCartItemModel.dropshiperName) ||
            !TextUtils.isEmpty(shipmentCartItemModel.dropshiperPhone)
    }

    private fun validateDropshipperPhone(
        shipmentCartItemModel: ShipmentCartItemModel,
        charSequence: CharSequence,
        fromTextWatcher: Boolean
    ) {
        val matcher = phoneNumberRegexPattern.matcher(charSequence)
        if (charSequence.isEmpty() && fromTextWatcher) {
            textInputLayoutShipperPhone.setError(true)
            textInputLayoutShipperPhone.setMessage(
                textInputLayoutShipperName.context.getString(
                    R.string.message_error_dropshipper_phone_empty
                )
            )
            shipmentCartItemModel.selectedShipmentDetailData!!.isDropshipperPhoneValid = false
            mActionListener?.onDataDisableToCheckout(null)
        } else if (textInputLayoutShipperPhone.textFieldInput.text.isNotEmpty() && !matcher.matches()) {
            textInputLayoutShipperPhone.setError(true)
            textInputLayoutShipperPhone.setMessage(
                textInputLayoutShipperName.context.getString(
                    R.string.message_error_dropshipper_phone_invalid
                )
            )
            shipmentCartItemModel.selectedShipmentDetailData!!.isDropshipperPhoneValid = false
            mActionListener?.onDataDisableToCheckout(null)
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
            mActionListener?.onDataDisableToCheckout(null)
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
            mActionListener?.onDataDisableToCheckout(null)
        } else {
            textInputLayoutShipperPhone.setError(false)
            textInputLayoutShipperPhone.setMessage("")
            shipmentCartItemModel.selectedShipmentDetailData!!.isDropshipperPhoneValid = true
            mActionListener?.onDataEnableToCheckout()
        }
    }

    private fun validateDropshipperName(
        shipmentCartItemModel: ShipmentCartItemModel,
        charSequence: CharSequence,
        fromTextWatcher: Boolean
    ) {
        if (charSequence.isEmpty() && fromTextWatcher) {
            textInputLayoutShipperName.setError(true)
            textInputLayoutShipperName.setMessage(textInputLayoutShipperName.context.getString(R.string.message_error_dropshipper_name_empty))
            shipmentCartItemModel.selectedShipmentDetailData!!.isDropshipperNameValid = false
            mActionListener?.onDataDisableToCheckout(null)
        } else if (textInputLayoutShipperName.textFieldInput.text.isNotEmpty() &&
            textInputLayoutShipperName.textFieldInput.text.length < DROPSHIPPER_MIN_NAME_LENGTH
        ) {
            textInputLayoutShipperName.setError(true)
            textInputLayoutShipperName.setMessage(textInputLayoutShipperName.context.getString(R.string.message_error_dropshipper_name_min_length))
            shipmentCartItemModel.selectedShipmentDetailData!!.isDropshipperNameValid = false
            mActionListener?.onDataDisableToCheckout(null)
        } else if (textInputLayoutShipperName.textFieldInput.text.isNotEmpty() &&
            textInputLayoutShipperName.textFieldInput.text.length > DROPSHIPPER_MAX_NAME_LENGTH
        ) {
            textInputLayoutShipperName.setError(true)
            textInputLayoutShipperName.setMessage(textInputLayoutShipperName.context.getString(R.string.message_error_dropshipper_name_max_length))
            shipmentCartItemModel.selectedShipmentDetailData!!.isDropshipperNameValid = false
            mActionListener?.onDataDisableToCheckout(null)
        } else {
            textInputLayoutShipperName.setError(false)
            textInputLayoutShipperName.setMessage("")
            shipmentCartItemModel.selectedShipmentDetailData!!.isDropshipperNameValid = true
            mActionListener?.onDataEnableToCheckout()
        }
    }

    private fun renderPrioritas(shipmentCartItemModel: ShipmentCartItemModel) {
        val cartItemModelList = ArrayList(shipmentCartItemModel.cartItemModels)
        val selectedShipmentDetailData = shipmentCartItemModel.selectedShipmentDetailData
        var renderOrderPriority = false
        val isTradeInDropOff = mActionListener?.isTradeInByDropOff ?: false
        if (selectedShipmentDetailData != null) {
            renderOrderPriority = if (isTradeInDropOff) {
                selectedShipmentDetailData.selectedCourierTradeInDropOff != null
            } else {
                selectedShipmentDetailData.selectedCourier != null
            }
        }
        if (adapterPosition != RecyclerView.NO_POSITION && renderOrderPriority) {
            if (!cartItemModelList.removeAt(FIRST_ELEMENT).isPreOrder) {
                checkBoxPriority.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        isPriorityChecked = isChecked
                        selectedShipmentDetailData!!.isOrderPriority =
                            isChecked
                        mActionListener?.onPriorityChecked(adapterPosition)
                        mActionListener?.onNeedUpdateRequestData()
                    }
                }
            }
            val spanText = SpannableString(
                tvPrioritasTicker.resources.getString(R.string.label_hardcoded_courier_ticker)
            )
            spanText.setSpan(StyleSpan(Typeface.BOLD), 43, 52, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            val courierItemData: CourierItemData? = if (isTradeInDropOff) {
                selectedShipmentDetailData!!.selectedCourierTradeInDropOff
            } else {
                selectedShipmentDetailData!!.selectedCourier
            }
            val isCourierSelected = courierItemData != null
            if (isCourierSelected && !shipmentCartItemModel.isError) {
                if (isCourierInstantOrSameday(courierItemData!!.shipperId)) {
                    if (!shipmentCartItemModel.isOrderPrioritasDisable && courierItemData.now!! && !shipmentCartItemModel.isProductIsPreorder) {
                        tvPrioritasInfo.text = courierItemData.priorityCheckboxMessage
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
        imgPriorityTnc.setOnClickListener { mActionListener?.onPriorityTncClicker() }
    }

    private fun hideAllTicker() {
        llPrioritas.visibility = View.GONE
        llPrioritasTicker.visibility = View.GONE
    }

    private fun renderInsurance(shipmentCartItemModel: ShipmentCartItemModel) {
        var renderInsurance = false
        val isTradeInDropOff = mActionListener?.isTradeInByDropOff ?: false
        val selectedShipmentDetailData = shipmentCartItemModel.selectedShipmentDetailData
        if (selectedShipmentDetailData != null) {
            renderInsurance = if (isTradeInDropOff) {
                selectedShipmentDetailData.selectedCourierTradeInDropOff != null
            } else {
                selectedShipmentDetailData.selectedCourier != null
            }
        }
        if (adapterPosition != RecyclerView.NO_POSITION && renderInsurance) {
            cbInsurance.setOnCheckedChangeListener { _: CompoundButton?, checked: Boolean ->
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    selectedShipmentDetailData?.useInsurance = checked
                    if (checked) {
                        mActionListener?.onInsuranceCheckedForTrackingAnalytics()
                    }
                    mActionListener?.onInsuranceChecked(adapterPosition)
                    mActionListener?.onNeedUpdateRequestData()
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
                    mActionListener?.onInsuranceChecked(adapterPosition)
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
                llInsurance.setOnClickListener(insuranceClickListener)
                if (useInsurance == null) {
                    if (selectedShipper.insuranceUsedDefault == InsuranceConstant.INSURANCE_USED_DEFAULT_YES) {
                        cbInsurance.isChecked = true
                        selectedShipmentDetailData.useInsurance = true
                        mActionListener?.onInsuranceChecked(adapterPosition)
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
                        mActionListener?.onInsuranceInfoTooltipClickedTrackingAnalytics()
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

    private fun renderError(shipmentCartItemModel: ShipmentCartItemModel) {
        if (shipmentCartItemModel.isError) {
            val errorTitle = shipmentCartItemModel.errorTitle
            val errorDescription = shipmentCartItemModel.errorDescription
            if (!errorTitle.isNullOrEmpty()) {
                if (!errorDescription.isNullOrEmpty()) {
                    tickerError.tickerTitle = errorTitle
                    tickerError.setTextDescription(errorDescription)
                    tickerError.setDescriptionClickEvent(object : TickerCallback {
                        override fun onDescriptionViewClick(charSequence: CharSequence) {
                            // no op
                        }

                        override fun onDismiss() {
                            // no op
                        }
                    })
                } else {
                    if (shipmentCartItemModel.isCustomEpharmacyError) {
                        tickerError.setHtmlDescription(
                            "$errorTitle " + itemView.context.getString(
                                R.string.checkout_ticker_lihat_cta_suffix
                            )
                        )
                        tickerError.setDescriptionClickEvent(object : TickerCallback {
                            override fun onDescriptionViewClick(charSequence: CharSequence) {
                                mActionListener?.onClickLihatOnTickerOrderError(
                                    shipmentCartItemModel.shopId.toString(),
                                    errorTitle
                                )
                                if (!shipmentCartItemModel.isStateAllItemViewExpanded) {
                                    shipmentCartItemModel.isTriggerScrollToErrorProduct = true
                                    showAllProductListener(shipmentCartItemModel).onClick(
                                        tickerError
                                    )
                                    return
                                }
                                scrollToErrorProduct(shipmentCartItemModel)
                            }

                            override fun onDismiss() {
                                // no op
                            }
                        })
                        if (shipmentCartItemModel.isTriggerScrollToErrorProduct) {
                            scrollToErrorProduct(shipmentCartItemModel)
                        }
                    } else {
                        tickerError.setTextDescription(errorTitle)
                        tickerError.setDescriptionClickEvent(object : TickerCallback {
                            override fun onDescriptionViewClick(charSequence: CharSequence) {
                                // no op
                            }

                            override fun onDismiss() {
                                // no op
                            }
                        })
                    }
                }
                tickerError.tickerType = Ticker.TYPE_ERROR
                tickerError.tickerShape = SHAPE_LOOSE
                tickerError.closeButtonVisibility = View.GONE
                tickerError.visibility = View.VISIBLE
                layoutError.visibility = View.VISIBLE
            } else {
                tickerError.visibility = View.GONE
                layoutError.visibility = View.GONE
                layoutWarningAndError.visibility = View.GONE
            }
            containerOrder.alpha = 0.5f
            cbPPP.isEnabled = false
            cbInsurance.isEnabled = false
            llInsurance.isClickable = false
            cbDropshipper.isEnabled = false
            llDropshipper.isClickable = false
            mIconTooltip.isClickable = false
            rlCartSubTotal.isClickable = false
            textInputLayoutShipperName.textFieldInput.isClickable = false
            textInputLayoutShipperName.textFieldInput.isFocusable = false
            textInputLayoutShipperName.textFieldInput.isFocusableInTouchMode = false
            textInputLayoutShipperPhone.textFieldInput.isClickable = false
            textInputLayoutShipperPhone.textFieldInput.isFocusable = false
            textInputLayoutShipperPhone.textFieldInput.isFocusableInTouchMode = false
        } else {
            layoutError.visibility = View.GONE
            tickerError.visibility = View.GONE
            containerOrder.alpha = 1.0f
            llInsurance.isClickable = true
            mIconTooltip.isClickable = true
            llDropshipper.isClickable = true
            rlCartSubTotal.isClickable = true
            textInputLayoutShipperName.textFieldInput.isClickable = true
            textInputLayoutShipperName.textFieldInput.isFocusable = true
            textInputLayoutShipperName.textFieldInput.isFocusableInTouchMode = true
            textInputLayoutShipperPhone.textFieldInput.isClickable = true
            textInputLayoutShipperPhone.textFieldInput.isFocusable = true
            textInputLayoutShipperPhone.textFieldInput.isFocusableInTouchMode = true
        }
    }

    private fun renderWarningCloseable(shipmentCartItemModel: ShipmentCartItemModel) {
        if (!shipmentCartItemModel.isError && !TextUtils.isEmpty(shipmentCartItemModel.shopTicker)) {
            tickerWarningCloseable.tickerTitle = shipmentCartItemModel.shopTickerTitle
            tickerWarningCloseable.setHtmlDescription(shipmentCartItemModel.shopTicker)
            tickerWarningCloseable.visibility = View.VISIBLE
            tickerWarningCloseable.setDescriptionClickEvent(object : TickerCallback {
                override fun onDescriptionViewClick(charSequence: CharSequence) {
                    // no op //
                }

                override fun onDismiss() {
                    shipmentCartItemModel.shopTicker = ""
                    tickerWarningCloseable.visibility = View.GONE
                }
            })
        } else {
            tickerWarningCloseable.visibility = View.GONE
        }
    }

    private fun initSaveStateDebouncer() {
        compositeSubscription.add(
            Observable.create<ShipmentCartItemModel> { subscriber ->
                saveStateDebounceListener = object : SaveStateDebounceListener {
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
                        mActionListener?.onNeedToSaveState(shipmentCartItemModel)
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
                mActionListener?.onChangeScheduleDelivery(
                    scheduleDeliveryUiModel,
                    position,
                    scheduleDeliveryDonePublisher!!
                )
                scheduleDeliveryDonePublisher
            }
            .subscribe()
        scheduleDeliveryCompositeSubscription?.add(scheduleDeliverySubscription)
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
        mActionListener?.currentFragmentManager?.let {
            generalBottomSheet.show(context, it)
        }
    }

    private fun showInsuranceBottomSheet(context: Context, title: String, message: String) {
        val insuranceBottomSheet = InsuranceBottomSheet()
        insuranceBottomSheet.setDesc(message)
        mActionListener?.currentFragmentManager?.let {
            insuranceBottomSheet.show(title, context, it)
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
            removeDecimalSuffix(
                convertPriceValueToIdrFormat(
                    price,
                    false
                )
            )
        }
    }

    private fun getCostDetailOptionListener(shipmentCartItemModel: ShipmentCartItemModel): View.OnClickListener {
        return View.OnClickListener {
            if (adapterPosition != RecyclerView.NO_POSITION) {
                shipmentCartItemModel.isStateDetailSubtotalViewExpanded =
                    !shipmentCartItemModel.isStateDetailSubtotalViewExpanded
                mActionListener?.onNeedUpdateViewItem(adapterPosition)
                mActionListener?.onSubTotalItemClicked(adapterPosition)
            }
        }
    }

    private val insuranceClickListener: View.OnClickListener
        private get() = View.OnClickListener {
            if (adapterPosition != RecyclerView.NO_POSITION) {
                cbInsurance.isChecked = !cbInsurance.isChecked
                mActionListener?.onInsuranceChecked(adapterPosition)
            }
        }
    private val dropshipperClickListener: View.OnClickListener
        private get() = View.OnClickListener {
            cbDropshipper.isChecked = !cbDropshipper.isChecked
        }

    private fun initInnerRecyclerView(
        cartItemList: List<CartItemModel>,
        addOnWordingModel: AddOnWordingModel?
    ) {
        rvCartItem.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(
            rvCartItem.context
        )
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        rvCartItem.layoutManager = layoutManager
        val shipmentInnerProductListAdapter =
            ShipmentInnerProductListAdapter(cartItemList.toMutableList(), addOnWordingModel!!, this)
        rvCartItem.adapter = shipmentInnerProductListAdapter
    }

    private fun showAllProductListener(shipmentCartItemModel: ShipmentCartItemModel): View.OnClickListener {
        return View.OnClickListener {
            if (adapterPosition != RecyclerView.NO_POSITION) {
                shipmentCartItemModel.isStateAllItemViewExpanded =
                    !shipmentCartItemModel.isStateAllItemViewExpanded
                mActionListener?.onNeedUpdateViewItem(adapterPosition)
            }
        }
    }

    private fun showShipmentWarning(cartItemModel: CartItemModel) {
        if (!TextUtils.isEmpty(cartItemModel.errorMessage)) {
            if (!TextUtils.isEmpty(cartItemModel.errorMessageDescription)) {
                tickerProductError.tickerTitle = cartItemModel.errorMessage
                tickerProductError.setTextDescription(cartItemModel.errorMessageDescription)
            } else {
                tickerProductError.setTextDescription(cartItemModel.errorMessage)
            }
            if (cartItemModel.isBundlingItem) {
                if (cartItemModel.bundlingItemPosition == ShipmentMapper.BUNDLING_ITEM_HEADER) {
                    tickerProductError.visibility = View.VISIBLE
                } else {
                    tickerProductError.visibility = View.GONE
                }
            } else {
                tickerProductError.visibility = View.VISIBLE
            }
        } else {
            tickerProductError.visibility = View.GONE
        }
        if (!cartItemModel.isShopError) {
            disableItemView()
        }
    }

    private fun hideShipmentWarning() {
        tickerProductError.visibility = View.GONE
        enableItemView()
    }

    private fun disableItemView() {
        productBundlingInfo.alpha = 0.5f
        llFrameItemProductContainer.alpha = 0.5f
    }

    private fun enableItemView() {
        productBundlingInfo.alpha = 1.0f
        llFrameItemProductContainer.alpha = 1.0f
    }

    private val isHavingPurchaseProtectionChecked: Boolean
        private get() {
            if (adapterPosition != RecyclerView.NO_POSITION) {
                val data = shipmentDataList!![adapterPosition] as ShipmentCartItemModel
                for (cartItemModel in data.cartItemModels) {
                    if (cartItemModel.isProtectionOptIn) return true
                }
            }
            return false
        }

    private fun isCourierInstantOrSameday(shipperId: Int): Boolean {
        val ids = CourierConstant.INSTANT_SAMEDAY_COURIER
        for (id in ids) {
            if (shipperId == id) return true
        }
        return false
    }

    private fun sendScheduleDeliveryAnalytics(
        shipmentCartItemModel: ShipmentCartItemModel,
        selectedCourierItemData: CourierItemData
    ) {
        if (!shipmentCartItemModel.hasSentScheduleDeliveryAnalytics) {
            sendViewScheduledDeliveryWidgetOnTokopediaNowEvent()
            if (selectedCourierItemData.scheduleDeliveryUiModel?.available == false) {
                sendViewUnavailableScheduledDeliveryEvent()
            }
            shipmentCartItemModel.hasSentScheduleDeliveryAnalytics = true
        }
    }

    override fun getHostFragmentManager(): FragmentManager {
        return mActionListener!!.currentFragmentManager
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

    private interface SaveStateDebounceListener {
        fun onNeedToSaveState(shipmentCartItemModel: ShipmentCartItemModel?)
    }

    private interface ScheduleDeliveryDebouncedListener {
        fun onScheduleDeliveryChanged(shipmentScheduleDeliveryHolderData: ShipmentScheduleDeliveryHolderData?)
    }

    companion object {
        @JvmField
        val ITEM_VIEW_SHIPMENT_ITEM = R.layout.item_shipment_checkout
        private const val FIRST_ELEMENT = 0
        private const val DROPSHIPPER_MIN_NAME_LENGTH = 3
        private const val DROPSHIPPER_MAX_NAME_LENGTH = 100
        private const val DROPSHIPPER_MIN_PHONE_LENGTH = 6
        private const val DROPSHIPPER_MAX_PHONE_LENGTH = 20
        private const val PHONE_NUMBER_REGEX_PATTERN = "[0-9]+"
        private const val SHIPPING_SAVE_STATE_TYPE_TRADE_IN_DROP_OFF = 1
        private const val SHIPPING_SAVE_STATE_TYPE_SHIPPING_EXPERIENCE = 2
    }

    init {
        phoneNumberRegexPattern = Pattern.compile(PHONE_NUMBER_REGEX_PATTERN)
        plusCoachmarkPrefs = PlusCoachmarkPrefs(itemView.context)
        layoutWarning =
            itemView.findViewById(com.tokopedia.purchase_platform.common.R.id.layout_warning)
        containerOrder = itemView.findViewById(R.id.container_order)
        layoutError =
            itemView.findViewById(com.tokopedia.purchase_platform.common.R.id.layout_error)
        tickerError =
            itemView.findViewById(com.tokopedia.purchase_platform.common.R.id.ticker_error)
        layoutWarning =
            itemView.findViewById(com.tokopedia.purchase_platform.common.R.id.layout_warning)
        tickerWarningCloseable = itemView.findViewById(R.id.ticker_warning_closable)
        layoutWarning.setVisibility(View.GONE)
        layoutWarningAndError = itemView.findViewById(R.id.layout_warning_and_error)
        tvShopName = itemView.findViewById(R.id.tv_shop_name)
        labelEpharmacy = itemView.findViewById(R.id.label_epharmacy)
        customTickerError = itemView.findViewById(R.id.checkout_custom_ticker_error)
        customTickerDescription = itemView.findViewById(R.id.checkout_custom_ticker_description)
        customTickerAction = itemView.findViewById(R.id.checkout_custom_ticker_action)
        productBundlingInfo = itemView.findViewById(R.id.product_bundling_info)
        imageBundle = itemView.findViewById(R.id.image_bundle)
        textBundleTitle = itemView.findViewById(R.id.text_bundle_title)
        textBundlePrice = itemView.findViewById(R.id.text_bundle_price)
        textBundleSlashPrice = itemView.findViewById(R.id.text_bundle_slash_price)
        llFrameItemProductContainer = itemView.findViewById(R.id.ll_frame_item_product_container)
        rlProductInfo = itemView.findViewById(R.id.rl_product_info)
        vBundlingProductSeparator = itemView.findViewById(R.id.v_bundling_product_separator)
        ivProductImage = itemView.findViewById(R.id.iv_product_image)
        tvProductName = itemView.findViewById(R.id.tv_product_name)
        tvProductPrice = itemView.findViewById(R.id.tv_product_price)
        tvProductOriginalPrice = itemView.findViewById(R.id.tv_product_original_price)
        rlPurchaseProtection = itemView.findViewById(R.id.rlayout_purchase_protection)
        tvPPPLinkText = itemView.findViewById(R.id.text_link_text)
        tvPPPPrice = itemView.findViewById(R.id.text_protection_desc)
        cbPPP = itemView.findViewById(R.id.checkbox_ppp)
        mIconTooltip = itemView.findViewById(R.id.icon_tooltip)
        mPricePerProduct = itemView.findViewById(R.id.text_item_per_product)
        tvItemCountAndWeight = itemView.findViewById(R.id.tv_item_count_and_weight)
        tvOptionalNoteToSeller = itemView.findViewById(R.id.tv_optional_note_to_seller)
        tvProtectionLabel = itemView.findViewById(R.id.tv_purchase_protection_label)
        tvProtectionFee = itemView.findViewById(R.id.tv_purchase_protection_fee)
        rvCartItem = itemView.findViewById(R.id.rv_cart_item)
        tvExpandOtherProduct = itemView.findViewById(R.id.tv_expand_other_product)
        ivExpandOtherProduct = itemView.findViewById(R.id.iv_expand_other_product)
        rlExpandOtherProduct = itemView.findViewById(R.id.rl_expand_other_product)
        ivDetailOptionChevron = itemView.findViewById(R.id.iv_detail_option_chevron)
        tvSubTotalPrice = itemView.findViewById(R.id.tv_sub_total_price)
        rlCartSubTotal = itemView.findViewById(R.id.rl_cart_sub_total)
        tvTotalItem = itemView.findViewById(R.id.tv_total_item)
        tvTotalItemPrice = itemView.findViewById(R.id.tv_total_item_price)
        tvShippingFee = itemView.findViewById(R.id.tv_shipping_fee)
        tvShippingFeePrice = itemView.findViewById(R.id.tv_shipping_fee_price)
        tvInsuranceFee = itemView.findViewById(R.id.tv_insurance_fee)
        tvInsuranceFeePrice = itemView.findViewById(R.id.tv_insurance_fee_price)
        rlShipmentCost = itemView.findViewById(R.id.rl_shipment_cost)
        llInsurance = itemView.findViewById(R.id.ll_insurance)
        cbInsurance = itemView.findViewById(R.id.cb_insurance)
        imgInsuranceInfo = itemView.findViewById(R.id.img_insurance_info)
        llDropshipper = itemView.findViewById(R.id.ll_dropshipper)
        cbDropshipper = itemView.findViewById(R.id.cb_dropshipper)
        tvDropshipper = itemView.findViewById(R.id.label_dropshipper)
        imgDropshipperInfo = itemView.findViewById(R.id.img_dropshipper_info)
        llDropshipperInfo = itemView.findViewById(R.id.ll_dropshipper_info)
        textInputLayoutShipperName = itemView.findViewById(R.id.text_input_layout_shipper_name)
        textInputLayoutShipperPhone = itemView.findViewById(R.id.text_input_layout_shipper_phone)
        vSeparatorMultipleProductSameStore =
            itemView.findViewById(R.id.v_separator_multiple_product_same_store)
        vSeparatorBelowProduct = itemView.findViewById(R.id.v_separator_below_product)
        tvAdditionalFee = itemView.findViewById(R.id.tv_additional_fee)
        tvAdditionalFeePrice = itemView.findViewById(R.id.tv_additional_fee_price)
        tvLabelInsurance = itemView.findViewById(R.id.tv_label_insurance)
        imgShopBadge = itemView.findViewById(R.id.img_shop_badge)
        llShippingOptionsContainer = itemView.findViewById(R.id.ll_shipping_options_container)
        tickerProductError = itemView.findViewById(R.id.checkout_ticker_product_error)
        imgFreeShipping = itemView.findViewById(R.id.img_free_shipping)
        separatorFreeShipping = itemView.findViewById(R.id.separator_free_shipping)
        productTicker = itemView.findViewById(R.id.product_ticker)
        textOrderNumber = itemView.findViewById(R.id.text_order_number)
        labelPreOrder = itemView.findViewById(R.id.label_pre_order)
        separatorPreOrder = itemView.findViewById(R.id.separator_pre_order)
        labelIncidentShopLevel = itemView.findViewById(R.id.label_incident_shop_level)
        separatorIncidentShopLevel = itemView.findViewById(R.id.separator_incident_shop_level)
        textVariant = itemView.findViewById(R.id.text_variant)
        layoutProductInfo = itemView.findViewById(R.id.layout_product_info)
        // priority
        llPrioritas = itemView.findViewById(R.id.ll_prioritas)
        checkBoxPriority = itemView.findViewById(R.id.cb_prioritas)
        llPrioritasTicker = itemView.findViewById(R.id.ll_prioritas_ticker)
        tvPrioritasTicker = itemView.findViewById(R.id.tv_prioritas_ticker)
        tvPrioritasFee = itemView.findViewById(R.id.tv_priority_fee)
        tvPrioritasFeePrice = itemView.findViewById(R.id.tv_priority_fee_price)
        imgPriorityTnc = itemView.findViewById(R.id.img_prioritas_info)
        tvPrioritasInfo = itemView.findViewById(R.id.tv_order_prioritas_info)
        tvFulfillName = itemView.findViewById(R.id.tv_fulfill_district)
        imgFulfillmentBadge = itemView.findViewById(R.id.iu_image_fulfill)
        tvTradeInLabel = itemView.findViewById(R.id.tv_trade_in_label)
        // Shipping Experience
        shippingWidget = itemView.findViewById(R.id.shipping_widget)
        shippingWidget.setupListener(this)
        // AddOn Experience
        llGiftingAddOnOrderLevel = itemView.findViewById(R.id.ll_gifting_addon_order_level)
        llGiftingAddOnProductLevel = itemView.findViewById(R.id.ll_gifting_addon_product_level)
        buttonGiftingAddonOrderLevel = itemView.findViewById(R.id.button_gifting_addon_order_level)
        buttonGiftingAddonProductLevel =
            itemView.findViewById(R.id.button_gifting_addon_product_level)
        tvAddOnCostLabel = itemView.findViewById(R.id.tv_add_on_fee)
        tvAddOnPrice = itemView.findViewById(R.id.tv_add_on_price)
        compositeSubscription = CompositeSubscription()
        initSaveStateDebouncer()
        initScheduleDeliveryPublisher()
    }
}
