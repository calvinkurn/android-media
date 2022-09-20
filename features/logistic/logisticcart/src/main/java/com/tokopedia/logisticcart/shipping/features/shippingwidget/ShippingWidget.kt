package com.tokopedia.logisticcart.shipping.features.shippingwidget

import android.animation.Animator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface.BOLD
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.animation.CycleInterpolator
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.logisticcart.R
import com.tokopedia.logisticcart.databinding.ItemShipmentShippingExperienceBinding
import com.tokopedia.logisticcart.shipping.model.CourierItemData
import com.tokopedia.logisticcart.shipping.model.ShipmentCartItemModel
import com.tokopedia.media.loader.loadImage
import com.tokopedia.purchase_platform.common.utils.Utils.removeDecimalSuffix
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.utils.contentdescription.TextAndContentDescriptionUtil
import com.tokopedia.utils.currency.CurrencyFormatUtil.convertPriceValueToIdrFormat

class ShippingWidget : ConstraintLayout {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private var binding: ItemShipmentShippingExperienceBinding? = null
    private var mListener: ShippingWidgetListener? = null

    init {
        binding =
            ItemShipmentShippingExperienceBinding.inflate(LayoutInflater.from(context), this, true)
    }

    interface ShippingWidgetListener {

        fun onChangeDurationClickListener(
            shipmentCartItemModel: ShipmentCartItemModel,
            currentAddress: RecipientAddressModel
        )

        fun onChangeCourierClickListener(
            shipmentCartItemModel: ShipmentCartItemModel,
            currentAddress: RecipientAddressModel
        )

        fun onOnTimeDeliveryClicked(url: String)

        fun onClickSetPinpoint()

        fun onClickLayoutFailedShipping(
            shipmentCartItemModel: ShipmentCartItemModel,
            recipientAddressModel: RecipientAddressModel
        )

        fun onViewErrorInCourierSection(logPromoDesc: String)
    }

    fun setupListener(shippingWidgetListener: ShippingWidgetListener) {
        mListener = shippingWidgetListener
    }

    fun hideShippingStateLoading() {
        binding?.llShippingExperienceStateLoading?.root?.gone()
    }

    fun renderShippingVibrationAnimation(
        shipmentCartItemModel: ShipmentCartItemModel
    ) {
        binding?.apply {
            if (shipmentCartItemModel.isShippingBorderRed) {
                containerShippingExperience.setBackgroundResource(R.drawable.checkout_module_bg_rounded_red)
            } else {
                containerShippingExperience.setBackgroundResource(R.drawable.checkout_module_bg_rounded_grey)
            }
            if (shipmentCartItemModel.isTriggerShippingVibrationAnimation) {
                containerShippingExperience.animate()
                    .translationX(VIBRATION_ANIMATION_TRANSLATION_X.toFloat())
                    .setDuration(VIBRATION_ANIMATION_DURATION)
                    .setInterpolator(CycleInterpolator(VIBRATION_ANIMATION_CYCLE))
                    .setListener(object : Animator.AnimatorListener {
                        override fun onAnimationStart(animator: Animator) {}
                        override fun onAnimationEnd(animator: Animator) {
                            shipmentCartItemModel.isTriggerShippingVibrationAnimation = false
                        }

                        override fun onAnimationCancel(animator: Animator) {}
                        override fun onAnimationRepeat(animator: Animator) {}
                    })
                    .start()
            }
        }
    }

    fun renderErrorCourierState(shipmentCartItemModel: ShipmentCartItemModel) {
        binding?.apply {
            layoutStateNoSelectedShipping.gone()
            layoutStateHasSelectedSingleShipping.gone()
            layoutStateHasSelectedFreeShipping.gone()
            layoutStateHasSelectedNormalShipping.gone()
            layoutStateFailedShipping.gone()
            labelErrorShippingTitle.text = shipmentCartItemModel.courierSelectionErrorTitle
            labelErrorShippingDescription.text =
                shipmentCartItemModel.courierSelectionErrorDescription
            layoutStateHasErrorShipping.visible()
            llShippingExperienceStateLoading.root.gone()
            containerShippingExperience.visible()
            containerShippingExperience.setBackgroundResource(R.drawable.checkout_module_bg_rounded_grey)
        }
    }

    fun showContainerShippingExperience() {
        binding?.apply {
            layoutStateNoSelectedShipping.gone()
            llShippingExperienceStateLoading.root.gone()
            containerShippingExperience.visible()
            containerShippingExperience.setBackgroundResource(R.drawable.checkout_module_bg_rounded_grey)
        }
    }

    private fun showLayoutSingleShippingCourier() {
        binding?.apply {
            layoutStateHasSelectedNormalShipping.gone()
            layoutStateFailedShipping.gone()
            layoutStateHasErrorShipping.gone()
            layoutStateHasSelectedFreeShipping.gone()
            layoutStateHasSelectedSingleShipping.visible()
            layoutStateHasSelectedSingleShipping.setOnClickListener { }
        }
    }

    private fun showLabelSingleShippingEta(selectedCourierItemData: CourierItemData) {
        binding?.apply {
            labelSingleShippingEta.visibility = VISIBLE
            labelSingleShippingEta.text =
                if (selectedCourierItemData.etaErrorCode == 0 && selectedCourierItemData.etaText?.isNotEmpty() == true) {
                    selectedCourierItemData.etaText
                } else {
                    context.getString(R.string.estimasi_tidak_tersedia)
                }
        }
    }

    private fun showLabelSingleShippingMessage(text: CharSequence) {
        binding?.apply {
            labelSingleShippingMessage.visible()
            labelSingleShippingMessage.text = text
        }
    }

    private fun hideLabelSingleShippingMessage() {
        binding?.labelSingleShippingMessage?.gone()
    }

    fun showLayoutFreeShippingCourier(
        shipmentCartItemModel: ShipmentCartItemModel,
        currentAddress: RecipientAddressModel
    ) {
        binding?.apply {
            layoutStateHasSelectedNormalShipping.gone()
            layoutStateFailedShipping.gone()
            layoutStateHasErrorShipping.gone()
            layoutStateHasSelectedSingleShipping.gone()
            layoutStateHasSelectedFreeShipping.visible()
            layoutStateHasSelectedFreeShipping.setOnClickListener {
                mListener?.onChangeDurationClickListener(shipmentCartItemModel, currentAddress)
            }
        }
    }

    fun renderFreeShippingCourier(selectedCourierItemData: CourierItemData) {
        hideShipperName(selectedCourierItemData.isHideShipperName)
        renderFreeShippingTitle(selectedCourierItemData)
        renderFreeShippingEta(selectedCourierItemData)
    }

    private fun hideShipperName(isHideShipperName: Boolean) {
        binding?.labelFreeShippingCourierName?.visibility = if (isHideShipperName) GONE else VISIBLE
    }

    private fun renderFreeShippingTitle(selectedCourierItemData: CourierItemData) {
        binding?.apply {
            // Change duration to promo title after promo is applied
            val htmlLinkHelper = HtmlLinkHelper(
                labelSelectedFreeShipping.context,
                selectedCourierItemData.freeShippingChosenCourierTitle
            )
            labelSelectedFreeShipping.text = htmlLinkHelper.spannedString
        }
    }

    private fun renderFreeShippingEta(selectedCourierItemData: CourierItemData) {
        if (selectedCourierItemData.etaErrorCode == 0) {
            val labelFreeShippingEtaText =
                if (selectedCourierItemData.etaText != null && selectedCourierItemData.etaText?.isNotEmpty() == true) {
                    selectedCourierItemData.etaText
                } else {
                    context.getString(R.string.estimasi_tidak_tersedia)
                }
            showLabelFreeShippingEtaText(labelFreeShippingEtaText ?: "")
        } else {
            hideLabelFreeShippingEtaText()
        }
    }

    private fun showLabelFreeShippingEtaText(text: String) {
        binding?.apply {
            labelFreeShippingEta.visible()
            labelFreeShippingEta.text = text
        }
    }

    private fun hideLabelFreeShippingEtaText() {
        binding?.labelFreeShippingEta?.gone()
    }

    private fun showNormalShippingCourier(
        shipmentCartItemModel: ShipmentCartItemModel,
        currentAddress: RecipientAddressModel,
        selectedCourierItemData: CourierItemData
    ) {
        binding?.apply {
            layoutStateHasSelectedFreeShipping.gone()
            layoutStateFailedShipping.gone()
            layoutStateHasErrorShipping.gone()
            layoutStateHasSelectedSingleShipping.gone()
            layoutStateHasSelectedNormalShipping.visible()
            TextAndContentDescriptionUtil.setTextAndContentDescription(
                labelSelectedShippingDuration,
                selectedCourierItemData.estimatedTimeDelivery ?: "",
                context.getString(R.string.content_desc_label_selected_shipping_duration)
            )

            labelSelectedShippingDuration.setOnClickListener {
                mListener?.onChangeDurationClickListener(shipmentCartItemModel, currentAddress)
            }

            iconChevronChooseDuration.setOnClickListener {
                mListener?.onChangeDurationClickListener(shipmentCartItemModel, currentAddress)
            }

            labelSelectedShippingPriceOrDuration.setOnClickListener {
                mListener?.onChangeCourierClickListener(shipmentCartItemModel, currentAddress)
            }

            labelSelectedShippingCourier.setOnClickListener {
                mListener?.onChangeCourierClickListener(shipmentCartItemModel, currentAddress)
            }

            iconChevronChooseCourier.setOnClickListener {
                mListener?.onChangeCourierClickListener(shipmentCartItemModel, currentAddress)
            }

            labelDescriptionCourier.setOnClickListener {
                mListener?.onChangeCourierClickListener(shipmentCartItemModel, currentAddress)
            }

            vFillCourier.setOnClickListener {
                mListener?.onChangeCourierClickListener(shipmentCartItemModel, currentAddress)
            }
        }
    }

    fun setLabelSelectedShippingCourier(selectedCourierItemData: CourierItemData) {
        binding?.apply {
            val courierName = "${selectedCourierItemData.name} (${
                removeDecimalSuffix(
                    convertPriceValueToIdrFormat(
                        selectedCourierItemData.shipperPrice,
                        false
                    )
                )
            })"

            if (selectedCourierItemData.etaErrorCode == 0 && selectedCourierItemData.etaText?.isNotEmpty() == true) {
                renderLabelAndCourierName(
                    courierName = courierName,
                    labelPriceOrDuration = selectedCourierItemData.etaText ?: ""
                )
            } else if (selectedCourierItemData.etaErrorCode == 0 && selectedCourierItemData.etaText.isNullOrEmpty()) {
                renderLabelAndCourierName(
                    courierName = courierName,
                    labelPriceOrDuration = context.getString(R.string.estimasi_tidak_tersedia)
                )
            } else {
                renderLabelAndCourierName(
                    courierName = selectedCourierItemData.name ?: "",
                    labelPriceOrDuration = removeDecimalSuffix(
                        convertPriceValueToIdrFormat(
                            selectedCourierItemData.shipperPrice, false
                        )
                    )
                )
            }
        }
    }

    private fun renderLabelAndCourierName(courierName: String, labelPriceOrDuration: String) {
        binding?.apply {
            TextAndContentDescriptionUtil.setTextAndContentDescription(
                labelSelectedShippingCourier,
                courierName,
                context.getString(R.string.content_desc_label_selected_shipping_courier)
            )
            labelSelectedShippingPriceOrDuration.text = labelPriceOrDuration
        }
    }

    private fun showImageMerchantVoucher(urlImage: String) {
        binding?.apply {
            imgMvc.visible()
            imgMvc.loadImage(urlImage)
        }
    }

    private fun hideImageMerchantVoucher() {
        binding?.imgMvc?.gone()
    }

    private fun showLabelDescCourier(labelText: String?, urlLink: String?) {
        binding?.apply {
            labelDescriptionCourier.text = labelText
            labelDescriptionCourierTnc.setOnClickListener {
                mListener?.onOnTimeDeliveryClicked(urlLink ?: "")
            }
            labelDescriptionCourier.visible()
            labelDescriptionCourierTnc.visible()
        }
    }

    private fun hideLabelDescCourier() {
        binding?.apply {
            labelDescriptionCourier.gone()
            labelDescriptionCourierTnc.gone()
        }
    }

    fun showLayoutNoSelectedShipping(
        shipmentCartItemModel: ShipmentCartItemModel,
        currentAddress: RecipientAddressModel
    ) {
        binding?.apply {
            tradeInView.layoutTradeInShippingInfo.gone()
            layoutStateNoSelectedShipping.visible()
            layoutStateNoSelectedShipping.setOnClickListener {
                mListener?.onChangeDurationClickListener(shipmentCartItemModel, currentAddress)
            }
            containerShippingExperience.visible()
        }
    }

    fun showLayoutTradeIn() {
        binding?.apply {
            tradeInView.layoutTradeInShippingInfo.visible()
            layoutStateNoSelectedShipping.gone()
            tradeInView.tvTradeInShippingPriceTitle.visible()
            tradeInView.tvTradeInShippingPriceDetail.visible()
            containerShippingExperience.visible()
        }
    }

    fun showLayoutStateFailedShipping(
        shipmentCartItemModel: ShipmentCartItemModel,
        recipientAddressModel: RecipientAddressModel
    ) {
        binding?.apply {
            tradeInView.layoutTradeInShippingInfo.gone()
            layoutStateNoSelectedShipping.gone()
            layoutStateFailedShipping.visible()
            containerShippingExperience.visible()

            layoutStateFailedShipping.setOnClickListener {
                mListener?.onClickLayoutFailedShipping(shipmentCartItemModel, recipientAddressModel)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    fun renderSingleShippingCourier(
        shipmentCartItemModel: ShipmentCartItemModel,
        selectedCourierItemData: CourierItemData
    ) {
        showLayoutSingleShippingCourier()

        binding?.labelSelectedSingleShippingTitle?.apply {
            text = if (shipmentCartItemModel.voucherLogisticItemUiModel != null) {
                // Change duration to promo title after promo is applied
                val htmlLinkHelper = HtmlLinkHelper(
                    context, selectedCourierItemData.freeShippingChosenCourierTitle
                )
                htmlLinkHelper.spannedString
            } else {
                val price = removeDecimalSuffix(
                    convertPriceValueToIdrFormat(
                        selectedCourierItemData.shipperPrice,
                        false
                    )
                )
                selectedCourierItemData.name + " (" + price + ")"
            }
        }

        showLabelSingleShippingEta(selectedCourierItemData)

        if (selectedCourierItemData.logPromoDesc != null && selectedCourierItemData.logPromoDesc?.isNotEmpty() == true) {
            showLabelSingleShippingMessage(
                MethodChecker.fromHtml(
                    selectedCourierItemData.logPromoDesc
                )
            )
            if (shipmentCartItemModel.voucherLogisticItemUiModel == null && !shipmentCartItemModel.isHasShownCourierError) {
                mListener?.onViewErrorInCourierSection(selectedCourierItemData.logPromoDesc.orEmpty())
                shipmentCartItemModel.isHasShownCourierError = true
            }
        } else {
            hideLabelSingleShippingMessage()
        }
    }

    fun renderNormalShippingWithoutChooseCourierCard(
        shipmentCartItemModel: ShipmentCartItemModel,
        currentAddress: RecipientAddressModel,
        selectedCourierItemData: CourierItemData
    ) {
        binding?.apply {
            layoutStateHasSelectedNormalShipping.gone()
            layoutStateFailedShipping.gone()
            layoutStateHasErrorShipping.gone()
            layoutStateHasSelectedSingleShipping.gone()
            layoutStateHasSelectedFreeShipping.visible()
            layoutStateHasSelectedFreeShipping.setOnClickListener {
                mListener?.onChangeDurationClickListener(shipmentCartItemModel, currentAddress)
            }
            labelFreeShippingCourierName.gone()
            if (selectedCourierItemData.estimatedTimeDelivery != null) {
                val titleText = "${selectedCourierItemData.estimatedTimeDelivery} (${
                    removeDecimalSuffix(
                        convertPriceValueToIdrFormat(
                            selectedCourierItemData.shipperPrice,
                            false
                        )
                    )
                })"
                val htmlLinkHelper = HtmlLinkHelper(labelSelectedFreeShipping.context, titleText)
                labelSelectedFreeShipping.text = htmlLinkHelper.spannedString
                labelSelectedFreeShipping.setWeight(BOLD)
            }
            if (selectedCourierItemData.durationCardDescription.isNotEmpty()) {
                labelFreeShippingEta.visible()
                labelFreeShippingEta.text = selectedCourierItemData.durationCardDescription
            } else {
                labelFreeShippingEta.gone()
            }
        }
    }

    fun renderNormalShippingCourier(
        shipmentCartItemModel: ShipmentCartItemModel,
        currentAddress: RecipientAddressModel,
        selectedCourierItemData: CourierItemData
    ) {
        showNormalShippingCourier(shipmentCartItemModel, currentAddress, selectedCourierItemData)
        setLabelSelectedShippingCourier(selectedCourierItemData)

        val ontimeDelivery = selectedCourierItemData.ontimeDelivery
        val codProductData = selectedCourierItemData.codProductData
        val merchantVoucherProductModel = selectedCourierItemData.merchantVoucherProductModel

        if (merchantVoucherProductModel != null && merchantVoucherProductModel.isMvc == 1) {
            showImageMerchantVoucher(merchantVoucherProductModel.mvcLogo)
        } else {
            hideImageMerchantVoucher()
        }
        if (ontimeDelivery != null && ontimeDelivery.available) {
            showLabelDescCourier(ontimeDelivery.textLabel, ontimeDelivery.urlDetail)
        } else if (codProductData != null && codProductData.isCodAvailable == 1) {
            showLabelDescCourier(codProductData.codText, codProductData.tncLink)
        } else {
            hideLabelDescCourier()
        }
    }

    fun prepareLoadCourierState() {
        binding?.apply {
            layoutStateHasSelectedFreeShipping.gone()
            layoutStateHasSelectedNormalShipping.gone()
            layoutStateHasSelectedSingleShipping.gone()
            layoutStateFailedShipping.gone()
            layoutStateHasErrorShipping.gone()
        }
    }

    fun renderLoadingCourierState() {
        binding?.apply {
            llShippingExperienceStateLoading.root.visible()
            containerShippingExperience.gone()
            tradeInView.tvTradeInShippingPriceTitle.gone()
            tradeInView.tvTradeInShippingPriceDetail.gone()
        }
    }

    fun onLoadCourierStateData() {
        binding?.apply {
            llShippingExperienceStateLoading.root.visible()
            containerShippingExperience.gone()
        }
    }

    fun hideTradeInShippingInfo() {
        binding?.tradeInView?.layoutTradeInShippingInfo?.gone()
    }

    fun hideTradeInTitleAndDetail() {
        binding?.apply {
            tradeInView.tvTradeInShippingPriceTitle.gone()
            tradeInView.tvTradeInShippingPriceDetail.gone()
        }
    }

    fun renderErrorPinpointCourier() {
        binding?.apply {
            layoutStateNoSelectedShipping.gone()
            llShippingExperienceStateLoading.root.gone()
            containerShippingExperience.visible()
            containerShippingExperience.setBackgroundResource(R.drawable.checkout_module_bg_rounded_grey)
            layoutStateHasSelectedNormalShipping.gone()
            layoutStateFailedShipping.gone()
            layoutStateHasErrorShipping.gone()
            layoutStateHasSelectedFreeShipping.gone()

            labelSelectedSingleShippingTitle.setText(R.string.checkout_label_set_pinpoint_title)
            labelSingleShippingEta.gone()
            context?.apply {
                val pinpointErrorMessage =
                    getString(R.string.checkout_label_set_pinpoint_description) + " "
                val pinpointErrorAction = getString(R.string.checkout_label_set_pinpoint_action)
                val spannableString = SpannableString(pinpointErrorMessage + pinpointErrorAction)
                spannableString.setSpan(
                    ForegroundColorSpan(
                        ContextCompat.getColor(
                            this,
                            com.tokopedia.unifyprinciples.R.color.Unify_G500_96
                        )
                    ),
                    pinpointErrorMessage.length,
                    spannableString.length,
                    SpannableString.SPAN_INCLUSIVE_EXCLUSIVE
                )
                labelSingleShippingMessage.text = spannableString
                labelSingleShippingMessage.visible()
            }

            layoutStateHasSelectedSingleShipping.setOnClickListener {
                mListener?.onClickSetPinpoint()
            }
            layoutStateHasSelectedSingleShipping.visible()
        }
    }

    companion object {
        private const val VIBRATION_ANIMATION_DURATION: Long = 1250
        private const val VIBRATION_ANIMATION_TRANSLATION_X = -10
        private const val VIBRATION_ANIMATION_CYCLE = 4f
    }
}
