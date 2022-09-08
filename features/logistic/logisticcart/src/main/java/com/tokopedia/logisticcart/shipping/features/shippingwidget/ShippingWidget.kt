package com.tokopedia.logisticcart.shipping.features.shippingwidget

import android.animation.Animator
import android.content.Context
import android.graphics.Typeface.BOLD
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.animation.CycleInterpolator
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.logisticcart.R
import com.tokopedia.logisticcart.databinding.ItemShipmentShippingExperienceBinding
import com.tokopedia.logisticcart.shipping.model.*
import com.tokopedia.purchase_platform.common.utils.Utils.removeDecimalSuffix
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.utils.contentdescription.TextAndContentDescriptionUtil
import com.tokopedia.utils.currency.CurrencyFormatUtil.convertPriceValueToIdrFormat

class ShippingWidget: ConstraintLayout {

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
        binding = ItemShipmentShippingExperienceBinding.inflate(LayoutInflater.from(context), this, true)
    }

    interface ShippingWidgetListener {

        fun onChangeDurationClickListener(shipmentCartItemModel: ShipmentCartItemModel, currentAddress: RecipientAddressModel)

        fun onChangeCourierClickListener(shipmentCartItemModel: ShipmentCartItemModel, currentAddress: RecipientAddressModel)

        fun onOnTimeDeliveryClicked(url: String)

        fun onClickSetPinpoint()

        fun onClickLayoutFailedShipping(shipmentCartItemModel: ShipmentCartItemModel, recipientAddressModel: RecipientAddressModel)
    }

    fun setupListener(shippingWidgetListener: ShippingWidgetListener) {
        mListener = shippingWidgetListener
    }

    fun hideShippingStateLoading() {
        binding?.llShippingExperienceStateLoading?.root?.visibility = GONE
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
            layoutStateNoSelectedShipping.visibility = GONE
            layoutStateHasSelectedSingleShipping.visibility = GONE
            layoutStateHasSelectedFreeShipping.visibility = GONE
            layoutStateHasSelectedNormalShipping.visibility = GONE
            layoutStateFailedShipping.visibility = GONE
            labelErrorShippingTitle.text = shipmentCartItemModel.courierSelectionErrorTitle
            labelErrorShippingDescription.text = shipmentCartItemModel.courierSelectionErrorDescription
            layoutStateHasErrorShipping.visibility = VISIBLE
            llShippingExperienceStateLoading.root.visibility = GONE
            containerShippingExperience.visibility = VISIBLE
            containerShippingExperience.setBackgroundResource(R.drawable.checkout_module_bg_rounded_grey)
        }
    }

    fun showContainerShippingExperience() {
        binding?.apply {
            layoutStateNoSelectedShipping.visibility = GONE
            llShippingExperienceStateLoading.root.visibility = GONE
            containerShippingExperience.visibility = VISIBLE
            containerShippingExperience.setBackgroundResource(R.drawable.checkout_module_bg_rounded_grey)
        }
    }

    fun showLayoutSingleShippingCourier() {
        binding?.apply {
            layoutStateHasSelectedNormalShipping.visibility = GONE
            layoutStateFailedShipping.visibility = GONE
            layoutStateHasErrorShipping.visibility = GONE
            layoutStateHasSelectedFreeShipping.visibility = GONE
            layoutStateHasSelectedSingleShipping.visibility = VISIBLE
            layoutStateHasSelectedSingleShipping.setOnClickListener { }
        }
    }

    fun getLabelSelectedSingleShippingTitle() : TextView? {
        return binding?.labelSelectedSingleShippingTitle
    }

    fun showLabelSingleShippingEta(text: String) {
        binding?.apply {
            labelSingleShippingEta.visibility = VISIBLE
            labelSingleShippingEta.text = text
        }
    }

    fun showLabelSingleShippingMessage(text: CharSequence) {
        binding?.apply {
            labelSingleShippingMessage.visibility = VISIBLE
            labelSingleShippingMessage.text = text
        }
    }

    fun hideLabelSingleShippingMessage() {
        binding?.labelSingleShippingMessage?.visibility = GONE
    }

    fun showLayoutFreeShippingCourier(shipmentCartItemModel: ShipmentCartItemModel, currentAddress: RecipientAddressModel) {
        binding?.apply {
            layoutStateHasSelectedNormalShipping.visibility = GONE
            layoutStateFailedShipping.visibility = GONE
            layoutStateHasErrorShipping.visibility = GONE
            layoutStateHasSelectedSingleShipping.visibility = GONE
            layoutStateHasSelectedFreeShipping.visibility = VISIBLE
            layoutStateHasSelectedFreeShipping.setOnClickListener {
                mListener?.onChangeDurationClickListener(shipmentCartItemModel, currentAddress)
            }
        }
    }

    fun hideShipperName(isHideShipperName: Boolean) {
        binding?.labelFreeShippingCourierName?.visibility = if (isHideShipperName) GONE else VISIBLE
    }

    fun renderFreeShippingTitle(selectedCourierItemData: CourierItemData) {
        binding?.apply {
            // Change duration to promo title after promo is applied
            val htmlLinkHelper = HtmlLinkHelper(
                labelSelectedFreeShipping.context,
                selectedCourierItemData.freeShippingChosenCourierTitle
            )
            labelSelectedFreeShipping.text = htmlLinkHelper.spannedString
        }
    }

    fun showLabelFreeShippingEtaText(text: String) {
        binding?.apply {
            labelFreeShippingEta.visibility = VISIBLE
            labelFreeShippingEta.text = text
        }
    }

    fun hideLabelFreeShippingEtaText() {
        binding?.labelFreeShippingEta?.visibility = GONE
    }

    fun showNormalShippingCourier(
        shipmentCartItemModel: ShipmentCartItemModel,
        currentAddress: RecipientAddressModel,
        selectedCourierItemData: CourierItemData
    ) {
        binding?.apply {
            layoutStateHasSelectedFreeShipping.visibility = GONE
            layoutStateFailedShipping.visibility = GONE
            layoutStateHasErrorShipping.visibility = GONE
            layoutStateHasSelectedSingleShipping.visibility = GONE
            layoutStateHasSelectedNormalShipping.visibility = VISIBLE
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

    fun setLabelSelectedShippingCourier(courierName: String?, labelPriceOrDuartion: String?) {
        binding?.apply {
            TextAndContentDescriptionUtil.setTextAndContentDescription(
                labelSelectedShippingCourier,
                courierName ?: "",
                context.getString(R.string.content_desc_label_selected_shipping_courier)
            )
            labelSelectedShippingPriceOrDuration.text = labelPriceOrDuartion
        }
    }

    fun showImageMerchantVoucher(urlImage: String) {
        binding?.apply {
            imgMvc.visibility = VISIBLE
            ImageHandler.LoadImage(imgMvc, urlImage)
        }
    }

    fun hideImageMerchantVoucher() {
        binding?.imgMvc?.visibility = GONE
    }

    fun showLabelDescCourier(labelText: String?, urlLink: String?) {
        binding?.apply {
            labelDescriptionCourier.text = labelText
            labelDescriptionCourierTnc.setOnClickListener { _ ->
                mListener?.onOnTimeDeliveryClicked(urlLink ?: "")
            }
            labelDescriptionCourier.visibility = VISIBLE
            labelDescriptionCourierTnc.visibility = VISIBLE
        }
    }

    fun hideLabelDescCourier() {
        binding?.apply {
            labelDescriptionCourier.visibility = GONE
            labelDescriptionCourierTnc.visibility = GONE
        }
    }

    fun showLayoutNoSelectedShipping(
        shipmentCartItemModel: ShipmentCartItemModel,
        currentAddress: RecipientAddressModel
    ) {
        binding?.apply {
            tradeInView.layoutTradeInShippingInfo.visibility = GONE
            layoutStateNoSelectedShipping.visibility = VISIBLE
            layoutStateNoSelectedShipping.setOnClickListener {
                mListener?.onChangeDurationClickListener(shipmentCartItemModel, currentAddress)
            }
            containerShippingExperience.visibility = VISIBLE
        }
    }

    fun showLayoutTradeIn() {
        binding?.apply {
            tradeInView.layoutTradeInShippingInfo.visibility = VISIBLE
            layoutStateNoSelectedShipping.visibility = GONE
            tradeInView.tvTradeInShippingPriceTitle.visibility = VISIBLE
            tradeInView.tvTradeInShippingPriceDetail.visibility = VISIBLE
            containerShippingExperience.visibility = VISIBLE
        }
    }

    fun showLayoutStateFailedShipping(
        shipmentCartItemModel: ShipmentCartItemModel,
        recipientAddressModel: RecipientAddressModel
    ) {
        binding?.apply {
            tradeInView.layoutTradeInShippingInfo.visibility = GONE
            layoutStateNoSelectedShipping.visibility = GONE
            layoutStateFailedShipping.visibility = VISIBLE
            containerShippingExperience.visibility = VISIBLE

            layoutStateFailedShipping.setOnClickListener { _ ->
                mListener?.onClickLayoutFailedShipping(shipmentCartItemModel, recipientAddressModel)
            }
        }
    }

    fun renderNormalShippingWithoutChooseCourierCard(
        shipmentCartItemModel: ShipmentCartItemModel,
        currentAddress: RecipientAddressModel,
        selectedCourierItemData: CourierItemData
    ) {
        binding?.apply {
            layoutStateHasSelectedNormalShipping.visibility = GONE
            layoutStateFailedShipping.visibility = GONE
            layoutStateHasErrorShipping.visibility = GONE
            layoutStateHasSelectedSingleShipping.visibility = GONE
            layoutStateHasSelectedFreeShipping.visibility = VISIBLE
            layoutStateHasSelectedFreeShipping.setOnClickListener {
                mListener?.onChangeDurationClickListener(shipmentCartItemModel, currentAddress)
            }
            labelFreeShippingCourierName.visibility = GONE
            if (selectedCourierItemData.estimatedTimeDelivery != null) {
                val titleText =
                    selectedCourierItemData.estimatedTimeDelivery + " (" + removeDecimalSuffix(
                        convertPriceValueToIdrFormat(
                            selectedCourierItemData.shipperPrice, false
                        )
                    ) + ")"
                val htmlLinkHelper = HtmlLinkHelper(labelSelectedFreeShipping.context, titleText)
                labelSelectedFreeShipping.text = htmlLinkHelper.spannedString
                labelSelectedFreeShipping.setWeight(BOLD)
            }
            if (selectedCourierItemData.durationCardDescription.isNotEmpty()) {
                labelFreeShippingEta.visibility = VISIBLE
                labelFreeShippingEta.text = selectedCourierItemData.durationCardDescription
            } else {
                labelFreeShippingEta.visibility = GONE
            }
        }
    }

    fun prepareLoadCourierState() {
        binding?.apply {
            layoutStateHasSelectedFreeShipping.visibility = GONE
            layoutStateHasSelectedNormalShipping.visibility = GONE
            layoutStateHasSelectedSingleShipping.visibility = GONE
            layoutStateFailedShipping.visibility = GONE
            layoutStateHasErrorShipping.visibility = GONE
        }
    }

    fun renderLoadingCourierState() {
        binding?.apply {
            llShippingExperienceStateLoading.root.visibility = VISIBLE
            containerShippingExperience.visibility = GONE
            tradeInView.tvTradeInShippingPriceTitle.visibility = GONE
            tradeInView.tvTradeInShippingPriceDetail.visibility = GONE
        }
    }

    fun onLoadCourierStateData() {
        binding?.apply {
            llShippingExperienceStateLoading.root.visibility = VISIBLE
            containerShippingExperience.visibility = GONE
        }
    }

    fun hideTradeInShippingInfo() {
        binding?.tradeInView?.layoutTradeInShippingInfo?.visibility = GONE
    }

    fun hideTradeInTitleAndDetail() {
        binding?.apply {
            tradeInView.tvTradeInShippingPriceTitle.visibility = GONE
            tradeInView.tvTradeInShippingPriceDetail.visibility = GONE
        }
    }

    fun renderErrorPinpointCourier() {
        binding?.apply {
            layoutStateNoSelectedShipping.visibility = GONE
            llShippingExperienceStateLoading.root.visibility = GONE
            containerShippingExperience.visibility = VISIBLE
            containerShippingExperience.setBackgroundResource(R.drawable.checkout_module_bg_rounded_grey)
            layoutStateHasSelectedNormalShipping.visibility = GONE
            layoutStateFailedShipping.visibility = GONE
            layoutStateHasErrorShipping.visibility = GONE
            layoutStateHasSelectedFreeShipping.visibility = GONE

            labelSelectedSingleShippingTitle.setText(R.string.checkout_label_set_pinpoint_title)
            labelSingleShippingEta.visibility = GONE
            context?.apply {
                val pinpointErrorMessage = getString(R.string.checkout_label_set_pinpoint_description) + " "
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
                labelSingleShippingMessage.visibility = VISIBLE
            }

            layoutStateHasSelectedSingleShipping.setOnClickListener { _ ->
                mListener?.onClickSetPinpoint()
            }
            layoutStateHasSelectedSingleShipping.visibility = VISIBLE
        }
    }

    companion object {
        private const val VIBRATION_ANIMATION_DURATION: Long = 1250
        private const val VIBRATION_ANIMATION_TRANSLATION_X = -10
        private const val VIBRATION_ANIMATION_CYCLE = 4f
    }
}