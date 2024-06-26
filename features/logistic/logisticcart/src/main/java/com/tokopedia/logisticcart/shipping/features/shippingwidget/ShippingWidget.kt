package com.tokopedia.logisticcart.shipping.features.shippingwidget

import android.animation.Animator
import android.annotation.SuppressLint
import android.content.Context
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.animation.CycleInterpolator
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.logisticCommon.util.StringFormatterHelper.appendHtmlBoldText
import com.tokopedia.logisticcart.R
import com.tokopedia.logisticcart.databinding.ItemShipmentShippingExperienceBinding
import com.tokopedia.logisticcart.scheduledelivery.view.ShippingScheduleWidget
import com.tokopedia.logisticcart.shipping.model.CourierItemData
import com.tokopedia.logisticcart.shipping.model.OntimeDelivery
import com.tokopedia.logisticcart.shipping.model.ScheduleDeliveryUiModel
import com.tokopedia.logisticcart.shipping.model.ShipmentCartItemModel
import com.tokopedia.media.loader.loadImage
import com.tokopedia.purchase_platform.common.utils.removeDecimalSuffix
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifyprinciples.Typography
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

    val layoutStateNoSelectedShipping: ConstraintLayout?
        get() = binding?.layoutStateNoSelectedShipping

    val containerShippingExperience: FrameLayout?
        get() = binding?.containerShippingExperience

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

        fun onChangeScheduleDelivery(scheduleDeliveryUiModel: ScheduleDeliveryUiModel)

        fun getHostFragmentManager(): FragmentManager
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
                containerShippingExperience.setBackgroundResource(com.tokopedia.purchase_platform.common.R.drawable.bg_pp_rounded_red)
            } else {
                containerShippingExperience.setBackgroundResource(com.tokopedia.purchase_platform.common.R.drawable.bg_pp_rounded_grey)
            }
            if (shipmentCartItemModel.isTriggerShippingVibrationAnimation) {
                containerShippingExperience.animate()
                    .translationX(VIBRATION_ANIMATION_TRANSLATION_X.toFloat())
                    .setDuration(VIBRATION_ANIMATION_DURATION)
                    .setInterpolator(CycleInterpolator(VIBRATION_ANIMATION_CYCLE))
                    .setListener(object : Animator.AnimatorListener {
                        override fun onAnimationStart(animator: Animator) {
                            // no op
                        }

                        override fun onAnimationEnd(animator: Animator) {
                            shipmentCartItemModel.isTriggerShippingVibrationAnimation = false
                        }

                        override fun onAnimationCancel(animator: Animator) {
                            // no op
                        }

                        override fun onAnimationRepeat(animator: Animator) {
                            // no op
                        }
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
            layoutStateHasSelectedWhitelabelShipping.gone()
            layoutStateHasSelectedNormalShipping.gone()
            layoutStateFailedShipping.gone()
            shippingNowWidget.gone()
            if (shipmentCartItemModel.courierSelectionErrorTitle.isEmpty()) {
                labelErrorShippingTitle.text =
                    context.getString(R.string.checkout_error_shipping_title)
            } else {
                labelErrorShippingTitle.text = shipmentCartItemModel.courierSelectionErrorTitle
            }
            if (shipmentCartItemModel.courierSelectionErrorDescription.isEmpty()) {
                labelErrorShippingDescription.text =
                    context.getString(R.string.checkout_error_shipping_description)
            } else {
                labelErrorShippingDescription.text =
                    shipmentCartItemModel.courierSelectionErrorDescription
            }
            layoutStateHasErrorShipping.visible()
            llShippingExperienceStateLoading.root.gone()
            containerShippingExperience.visible()
            containerShippingExperience.setBackgroundResource(com.tokopedia.purchase_platform.common.R.drawable.bg_pp_rounded_grey)
        }
    }

    fun showContainerShippingExperience() {
        binding?.apply {
            layoutStateNoSelectedShipping.gone()
            llShippingExperienceStateLoading.root.gone()
            containerShippingExperience.visible()
            containerShippingExperience.setBackgroundResource(com.tokopedia.purchase_platform.common.R.drawable.bg_pp_rounded_grey)
        }
    }

    private fun showLayoutSingleShippingCourier() {
        binding?.apply {
            layoutStateHasSelectedNormalShipping.gone()
            layoutStateFailedShipping.gone()
            layoutStateHasErrorShipping.gone()
            layoutStateHasSelectedFreeShipping.gone()
            layoutStateHasSelectedWhitelabelShipping.gone()
            shippingNowWidget.gone()
            layoutStateHasSelectedSingleShipping.visible()
            layoutStateHasSelectedSingleShipping.setOnClickListener { }
        }
    }

    private fun showLabelSingleShippingEta(selectedCourierItemData: CourierItemData) {
        binding?.apply {
            labelSingleShippingEta.visibility = VISIBLE
            labelSingleShippingEta.text = getSingleShippingLabelEta(selectedCourierItemData)
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
            shippingNowWidget.gone()
            layoutStateHasSelectedWhitelabelShipping.gone()
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
            layoutStateHasSelectedWhitelabelShipping.gone()
            layoutStateFailedShipping.gone()
            layoutStateHasErrorShipping.gone()
            layoutStateHasSelectedSingleShipping.gone()
            shippingNowWidget.gone()
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
            convertPriceValueToIdrFormat(
                selectedCourierItemData.shipperPrice,
                false
            ).removeDecimalSuffix()
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
                    labelPriceOrDuration =
                    convertPriceValueToIdrFormat(
                        selectedCourierItemData.shipperPrice,
                        false
                    ).removeDecimalSuffix()
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
        binding?.labelSelectedSingleShippingTitle?.text =
            getSingleShippingTitle(shipmentCartItemModel, selectedCourierItemData)
        showLabelSingleShippingEta(selectedCourierItemData)
        doCheckLabelSingleShippingPromo(
            shipmentCartItemModel,
            selectedCourierItemData
        ) { labelSingleShippingPromo ->
            if (labelSingleShippingPromo?.isNotBlank() == true) {
                showLabelSingleShippingMessage(labelSingleShippingPromo)
            } else {
                hideLabelSingleShippingMessage()
            }
        }
    }

    private fun doCheckLabelSingleShippingPromo(
        shipmentCartItemModel: ShipmentCartItemModel,
        selectedCourierItemData: CourierItemData,
        labelSingleShippingPromo: (CharSequence?) -> Unit
    ) {
        if (selectedCourierItemData.logPromoDesc != null && selectedCourierItemData.logPromoDesc?.isNotEmpty() == true) {
            labelSingleShippingPromo.invoke(
                MethodChecker.fromHtml(
                    selectedCourierItemData.logPromoDesc
                )
            )
            if (shipmentCartItemModel.voucherLogisticItemUiModel == null && !shipmentCartItemModel.isHasShownCourierError) {
                mListener?.onViewErrorInCourierSection(selectedCourierItemData.logPromoDesc.orEmpty())
                shipmentCartItemModel.isHasShownCourierError = true
            }
        } else {
            labelSingleShippingPromo.invoke(null)
        }
    }

    private fun getSingleShippingTitle(
        shipmentCartItemModel: ShipmentCartItemModel,
        selectedCourierItemData: CourierItemData
    ): CharSequence? {
        return if (shipmentCartItemModel.voucherLogisticItemUiModel != null) {
            // Change duration to promo title after promo is applied
            selectedCourierItemData.freeShippingChosenCourierTitle.convertToSpannedString()
        } else {
            return getTitleFromNameAndPrice(
                courierName = selectedCourierItemData.name,
                shipperPrice = selectedCourierItemData.shipperPrice
            )
        }
    }

    private fun String.convertToSpannedString(): CharSequence? {
        return HtmlLinkHelper(
            context,
            this
        ).spannedString
    }

    private fun getTitleFromNameAndPrice(
        courierName: String?,
        shipperPrice: Int
    ): String {
        val price =
            convertPriceValueToIdrFormat(
                shipperPrice,
                false
            ).removeDecimalSuffix()
        return "$courierName ($price)"
    }

    private fun getSingleShippingTitleForScheduleWidget(
        selectedCourierItemData: CourierItemData
    ): CharSequence? {
        return if (selectedCourierItemData.freeShippingChosenCourierTitle.isNotEmpty()) {
            // Change duration to promo title after promo is applied
            selectedCourierItemData.freeShippingChosenCourierTitle.convertToSpannedString()
        } else {
            val title = getTitleFromNameAndPrice(
                courierName = selectedCourierItemData.name,
                shipperPrice = selectedCourierItemData.shipperPrice
            )

            StringBuilder().apply {
                appendHtmlBoldText(title)
            }.toString().convertToSpannedString()
        }
    }

    private fun getSingleShippingLabelEta(
        selectedCourierItemData: CourierItemData
    ): String? {
        return if (selectedCourierItemData.etaErrorCode == 0 && selectedCourierItemData.etaText?.isNotEmpty() == true) {
            selectedCourierItemData.etaText
        } else {
            context.getString(R.string.estimasi_tidak_tersedia)
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
            shippingNowWidget.gone()
            layoutStateHasSelectedFreeShipping.gone()
            layoutStateHasSelectedWhitelabelShipping.visible()
            layoutStateHasSelectedWhitelabelShipping.setOnClickListener {
                mListener?.onChangeDurationClickListener(shipmentCartItemModel, currentAddress)
            }
            if (selectedCourierItemData.estimatedTimeDelivery != null) {
                val titleText = "${selectedCourierItemData.estimatedTimeDelivery} (${
                convertPriceValueToIdrFormat(
                    selectedCourierItemData.shipperPrice,
                    false
                ).removeDecimalSuffix()
                })"
                val htmlLinkHelper = HtmlLinkHelper(labelSelectedFreeShipping.context, titleText)
                labelSelectedWhitelabelShipping.text = htmlLinkHelper.spannedString
                labelSelectedWhitelabelShipping.setWeight(Typography.BOLD)
            }

            if (selectedCourierItemData.durationCardDescription.isNotEmpty()) {
                labelWhitelabelShippingEta.visible()
                labelWhitelabelShippingEta.text = selectedCourierItemData.durationCardDescription
            } else {
                labelWhitelabelShippingEta.gone()
            }

            val ontimeDelivery: OntimeDelivery? = selectedCourierItemData.ontimeDelivery
            if (ontimeDelivery?.available == true) {
                var whitelabelDescription = ""
                if (ontimeDelivery.textUrl.isNotEmpty() && ontimeDelivery.urlDetail.isNotEmpty()) {
                    labelWhitelabelDescription.setOnClickListener {
                        mListener?.onOnTimeDeliveryClicked(
                            ontimeDelivery.urlDetail
                        )
                    }
                    whitelabelDescription = if (ontimeDelivery.textLabel.isNotEmpty()) {
                        context.getString(
                            R.string.checkout_whitelabel_desc_otdg_url,
                            ontimeDelivery.textLabel,
                            ontimeDelivery.urlDetail,
                            ontimeDelivery.textUrl
                        )
                    } else {
                        context.getString(
                            R.string.checkout_whitelabel_otdg_url,
                            ontimeDelivery.urlDetail,
                            ontimeDelivery.textUrl
                        )
                    }
                } else if (ontimeDelivery.textLabel.isNotEmpty()) {
                    whitelabelDescription = ontimeDelivery.textLabel
                }
                labelWhitelabelDescription.text = HtmlLinkHelper(
                    context,
                    whitelabelDescription
                ).spannedString
                labelWhitelabelDescription.visible()
            } else {
                labelWhitelabelDescription.gone()
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
            layoutStateHasSelectedWhitelabelShipping.gone()
            layoutStateFailedShipping.gone()
            layoutStateHasErrorShipping.gone()
            shippingNowWidget.gone()
        }
    }

    fun renderLoadingCourierState() {
        binding?.apply {
            llShippingExperienceStateLoading.loaderUnify1.type = LoaderUnify.TYPE_RECT
            llShippingExperienceStateLoading.loaderUnify2.type = LoaderUnify.TYPE_RECT
            llShippingExperienceStateLoading.root.visible()
            containerShippingExperience.gone()
            tradeInView.tvTradeInShippingPriceTitle.gone()
            tradeInView.tvTradeInShippingPriceDetail.gone()
        }
    }

    fun onLoadCourierStateData() {
        binding?.apply {
            llShippingExperienceStateLoading.loaderUnify1.type = LoaderUnify.TYPE_RECT
            llShippingExperienceStateLoading.loaderUnify2.type = LoaderUnify.TYPE_RECT
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
            containerShippingExperience.setBackgroundResource(com.tokopedia.purchase_platform.common.R.drawable.bg_pp_rounded_grey)
            layoutStateHasSelectedNormalShipping.gone()
            layoutStateFailedShipping.gone()
            layoutStateHasErrorShipping.gone()
            layoutStateHasSelectedFreeShipping.gone()
            layoutStateHasSelectedWhitelabelShipping.gone()
            shippingNowWidget.gone()

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
                            com.tokopedia.unifyprinciples.R.color.Unify_GN500_96
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

    fun renderScheduleDeliveryWidget(
        shipmentCartItemModel: ShipmentCartItemModel,
        selectedCourierItemData: CourierItemData
    ) {
        showScheduleDeliveryWidget()

        var labelNow2H: CharSequence? = null

        doCheckLabelSingleShippingPromo(
            shipmentCartItemModel,
            selectedCourierItemData
        ) { labelSingleShippingPromo ->
            labelNow2H = labelSingleShippingPromo
        }

        binding?.shippingNowWidget?.bind(
            titleNow2H = getSingleShippingTitleForScheduleWidget(
                selectedCourierItemData = selectedCourierItemData
            ),
            descriptionNow2H = getSingleShippingLabelEta(selectedCourierItemData),
            labelNow2H = labelNow2H,
            scheduleDeliveryUiModel = selectedCourierItemData.scheduleDeliveryUiModel?.copy(),
            listener = object : ShippingScheduleWidget.ShippingScheduleWidgetListener {
                override fun onChangeScheduleDelivery(scheduleDeliveryUiModel: ScheduleDeliveryUiModel) {
                    mListener?.onChangeScheduleDelivery(scheduleDeliveryUiModel)
                }

                override fun getFragmentManager(): FragmentManager? {
                    return mListener?.getHostFragmentManager()
                }
            }
        )
    }

    private fun showScheduleDeliveryWidget() {
        binding?.apply {
            layoutStateHasSelectedNormalShipping.gone()
            layoutStateFailedShipping.gone()
            layoutStateHasErrorShipping.gone()
            layoutStateHasSelectedFreeShipping.gone()
            layoutStateHasSelectedWhitelabelShipping.gone()
            layoutStateHasSelectedSingleShipping.gone()
            shippingNowWidget.visible()
        }
    }

    companion object {
        private const val VIBRATION_ANIMATION_DURATION: Long = 1250
        private const val VIBRATION_ANIMATION_TRANSLATION_X = -10
        private const val VIBRATION_ANIMATION_CYCLE = 4f
    }
}
