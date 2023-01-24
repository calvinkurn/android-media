package com.tokopedia.logisticcart.shipping.features.shippingwidget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.logisticcart.R
import com.tokopedia.logisticcart.databinding.ItemShipmentShippingOccBinding
import com.tokopedia.logisticcart.shipping.model.LogisticPromoUiModel
import com.tokopedia.purchase_platform.common.utils.removeDecimalSuffix
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.currency.CurrencyFormatUtil

class ShippingOccWidget : ConstraintLayout {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private var binding: ItemShipmentShippingOccBinding? = null

    init {
        binding = ItemShipmentShippingOccBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun renderDisabledShipping(
        courierSelectionErrorTitle: String,
        courierSelectionErrorDescription: String
    ) {
        binding?.apply {
            tvShippingDuration.gone()
            tvShippingDurationEta.gone()
            btnChangeDuration.gone()
            tvShippingCourierEta.gone()
            tvShippingCourierNotes.gone()
            btnChangeCourier.gone()
            tvShippingErrorMessage.gone()
            btnReloadShipping.gone()
            iconReloadShipping.gone()
            tickerShippingPromo.gone()
            loaderShipping.gone()
            tvShippingCourier.text = courierSelectionErrorTitle
            tvShippingPrice.text = courierSelectionErrorDescription
            tvShippingCourier.visible()
            tvShippingPrice.visible()
            setMultiViewsOnClickListener(tvShippingCourier, tvShippingPrice) {
                /* no-op */
            }
        }
    }

    fun renderLoadingShipping() {
        binding?.apply {
            setInvisible(tvShippingDuration)
            setInvisible(tvShippingDurationEta)
            setInvisible(btnChangeDuration)
            setInvisible(tvShippingCourier)
            setInvisible(tvShippingPrice)
            setInvisible(tvShippingCourierEta)
            setInvisible(tvShippingCourierNotes)
            setInvisible(btnChangeCourier)
            setInvisible(tvShippingErrorMessage)
            setInvisible(btnReloadShipping)
            setInvisible(iconReloadShipping)
            setInvisible(tickerShippingPromo)
            loaderShipping.visible()
        }
    }

    private fun setInvisible(view: View) {
        if (view.isVisible) {
            view.invisible()
        }
    }

    fun hideLoaderShipping() {
        binding?.loaderShipping?.gone()
    }

    fun renderShippingDuration(
        serviceName: String?,
        shipperName: String,
        onChangeDurationListener: () -> Unit
    ) {
        binding?.apply {
            tvShippingDuration.text =
                root.context.getString(R.string.lbl_shipping_with_name, serviceName)
            tvShippingDuration.visible()
            tvShippingDurationEta.gone()
            tvShippingCourierNotes.gone()
            setMultiViewsOnClickListener(tvShippingDuration, btnChangeDuration) {
                onChangeDurationListener.invoke()
            }
            btnChangeDuration.visible()
            tvShippingCourier.text = shipperName
            tvShippingCourier.visible()
            btnChangeCourier.visible()
            tvShippingErrorMessage.gone()
            btnReloadShipping.gone()
            iconReloadShipping.gone()
        }
    }

    fun renderBboTicker(
        logisticPromo: LogisticPromoUiModel?,
        logisticPromoTickerMessage: String?,
        onTickerClickListener: () -> Unit
    ) {
        binding?.apply {
            if (logisticPromoTickerMessage?.isNotEmpty() == true && logisticPromo != null) {
                val formattedLogisticPromoTickerMessage = HtmlLinkHelper(
                    tickerShippingPromoTitle.context,
                    logisticPromoTickerMessage
                ).spannedString
                if (logisticPromo.etaData.errorCode == ERROR_CODE_ZERO && !logisticPromo.isBebasOngkirExtra) {
                    if (logisticPromo.etaData.textEta.isEmpty()) {
                        tickerShippingPromoSubtitle.setText(R.string.estimasi_tidak_tersedia)
                    } else {
                        tickerShippingPromoSubtitle.text = logisticPromo.etaData.textEta
                    }
                    tickerShippingPromoTitle.text = formattedLogisticPromoTickerMessage
                    tickerShippingPromoTitle.visible()
                    tickerShippingPromoSubtitle.visible()
                    tickerShippingPromoDescription.gone()
                } else {
                    tickerShippingPromoDescription.text = formattedLogisticPromoTickerMessage
                    tickerShippingPromoDescription.visible()
                    tickerShippingPromoTitle.gone()
                    tickerShippingPromoSubtitle.gone()
                }
                tickerShippingPromo.visible()
                tickerAction.setOnClickListener {
                    onTickerClickListener.invoke()
                }
            } else {
                tickerShippingPromo.gone()
            }
        }
    }

    fun renderBboShipping(
        logisticPromoUiModel: LogisticPromoUiModel,
        onChangeDurationListener: () -> Unit
    ) {
        binding?.apply {
            val formattedFreeShippingChosenCourierTitle = HtmlLinkHelper(
                tvShippingCourier.context,
                logisticPromoUiModel.freeShippingChosenCourierTitle
            ).spannedString
            tvShippingCourier.text = formattedFreeShippingChosenCourierTitle
            tvShippingDuration.gone()
            btnChangeDuration.gone()
            tvShippingCourierNotes.gone()
            tvShippingPrice.gone()
            if (logisticPromoUiModel.etaData.errorCode == ERROR_CODE_ZERO && !logisticPromoUiModel.isBebasOngkirExtra) {
                if (logisticPromoUiModel.etaData.textEta.isEmpty()) {
                    tvShippingCourierEta.setText(R.string.estimasi_tidak_tersedia)
                } else {
                    tvShippingCourierEta.text = logisticPromoUiModel.etaData.textEta
                }
                tvShippingCourierEta.visible()
            } else {
                tvShippingCourierEta.gone()
            }
            setMultiViewsOnClickListener(
                tvShippingCourier,
                tvShippingPrice,
                tvShippingCourierEta,
                btnChangeCourier
            ) {
                onChangeDurationListener.invoke()
            }
        }
    }

    fun renderNormalShippingWithoutChooseCourierCard(
        serviceName: String?,
        shippingPrice: Int,
        serviceEta: String?,
        description: String?,
        onChangeDurationListener: () -> Unit
    ) {
        binding?.apply {
            tvShippingCourier.text = root.context.getString(
                R.string.lbl_shipping_with_name_and_price,
                "$serviceName",
                CurrencyFormatUtil.convertPriceValueToIdrFormat(
                    shippingPrice,
                    false
                ).removeDecimalSuffix()
            )
            tvShippingCourier.setWeight(Typography.BOLD)
            tvShippingDuration.gone()
            btnChangeDuration.gone()
            tvShippingCourierNotes.gone()
            tvShippingPrice.gone()
            if (serviceEta != null) {
                tvShippingCourierEta.text = serviceEta
                tvShippingCourierEta.visible()
            } else {
                tvShippingCourierEta.gone()
            }
            if (!description.isNullOrEmpty()) {
                tvShippingCourierNotes.text = description
                tvShippingCourierNotes.weightType = Typography.DISPLAY_3
                tvShippingCourierNotes.visible()
            } else {
                tvShippingCourierNotes.gone()
            }
            setMultiViewsOnClickListener(
                tvShippingCourier,
                tvShippingCourierEta,
                btnChangeCourier
            ) {
                onChangeDurationListener.invoke()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    fun renderShippingCourierWithEta(
        shipperName: String?,
        shippingPrice: Int,
        eta: String,
        onChangeCourierListener: () -> Unit
    ) {
        binding?.apply {
            tvShippingCourier.text = "$shipperName (${
            CurrencyFormatUtil.convertPriceValueToIdrFormat(
                shippingPrice,
                false
            ).removeDecimalSuffix()
            })"
            if (eta.isNotEmpty()) {
                tvShippingCourierEta.text = eta
            } else {
                tvShippingCourierEta.setText(R.string.estimasi_tidak_tersedia)
            }
            tvShippingCourierEta.visible()
            tvShippingCourierNotes.gone()
            tvShippingPrice.gone()
            setMultiViewsOnClickListener(
                tvShippingCourier,
                tvShippingPrice,
                tvShippingCourierEta,
                btnChangeCourier
            ) {
                onChangeCourierListener.invoke()
            }
        }
    }

    fun renderShippingCourierWithoutEta(
        shippingPrice: Int,
        onChangeCourierListener: () -> Unit
    ) {
        binding?.apply {
            tvShippingPrice.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(
                shippingPrice,
                false
            ).removeDecimalSuffix()
            tvShippingPrice.visible()
            tvShippingCourierEta.gone()
            tvShippingCourierNotes.gone()
            setMultiViewsOnClickListener(
                tvShippingCourier,
                tvShippingPrice,
                tvShippingCourierEta,
                btnChangeCourier
            ) {
                onChangeCourierListener.invoke()
            }
        }
    }

    fun renderShippingWithErrorMessage(
        serviceName: String?,
        serviceEta: String?,
        errorMessage: String,
        onShippingErrorMessageClickListener: () -> Unit
    ) {
        binding?.apply {
            tvShippingDuration.text =
                root.context.getString(R.string.lbl_shipping_with_name, serviceName)
            tvShippingDuration.visible()
            if (serviceEta != null) {
                if (serviceEta.isNotEmpty()) {
                    tvShippingDurationEta.text = serviceEta
                } else {
                    tvShippingDurationEta.setText(R.string.estimasi_tidak_tersedia)
                }
                tvShippingDurationEta.visible()
            } else {
                tvShippingDurationEta.gone()
            }
            setMultiViewsOnClickListener(tvShippingDuration, btnChangeDuration) {
                /* no-op */
            }
            btnChangeDuration.gone()

            val button = root.context.getString(R.string.lbl_change_template)
            val span = SpannableString("$errorMessage $button")
            span.setSpan(
                StyleSpan(Typeface.BOLD),
                errorMessage.length + 1,
                span.length,
                SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            root.context?.let {
                span.setSpan(
                    ForegroundColorSpan(
                        ContextCompat.getColor(
                            it,
                            com.tokopedia.unifyprinciples.R.color.Unify_G500
                        )
                    ),
                    errorMessage.length + 1,
                    span.length,
                    SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
            tvShippingErrorMessage.text = span
            tvShippingErrorMessage.visible()
            tvShippingErrorMessage.setOnClickListener {
                onShippingErrorMessageClickListener.invoke()
            }
            tvShippingCourier.gone()
            btnChangeCourier.gone()
            btnReloadShipping.gone()
            iconReloadShipping.gone()
            tvShippingPrice.gone()
            tvShippingCourierEta.gone()
            tvShippingCourierNotes.gone()
            tickerShippingPromo.gone()
        }
    }

    fun renderErrorShipping(
        serviceErrorMessage: String?,
        onReloadShipping: () -> Unit
    ) {
        binding?.apply {
            tvShippingDuration.text = root.context.getString(R.string.lbl_shipping)
            tvShippingDuration.visible()
            tvShippingDurationEta.gone()
            btnChangeDuration.gone()
            setMultiViewsOnClickListener(tvShippingDuration, btnChangeDuration) {
                /* no-op */
            }
            tvShippingCourier.gone()
            btnChangeCourier.gone()
            tvShippingErrorMessage.text = serviceErrorMessage
            tvShippingErrorMessage.visible()
            tvShippingErrorMessage.setOnClickListener {
                /* no-op */
            }
            setMultiViewsOnClickListener(iconReloadShipping, btnReloadShipping) {
                onReloadShipping.invoke()
            }
            btnReloadShipping.visible()
            iconReloadShipping.visible()
            tvShippingPrice.gone()
            tvShippingCourierNotes.gone()
            tvShippingCourierEta.gone()
            tickerShippingPromo.gone()
        }
    }

    fun renderShippingPinpointError(
        onChoosePinpoint: () -> Unit
    ) {
        binding?.apply {
            tvShippingDuration.gone()
            tvShippingDurationEta.gone()
            btnChangeDuration.gone()
            tvShippingCourierEta.gone()
            tvShippingCourierNotes.gone()
            btnChangeCourier.gone()
            tvShippingErrorMessage.gone()
            btnReloadShipping.gone()
            iconReloadShipping.gone()
            tickerShippingPromo.gone()
            loaderShipping.gone()
            tvShippingCourier.text = root.context.getString(R.string.occ_pinpoint_error_title)
            val errorDescription = root.context.getString(R.string.occ_pinpoint_error_description)
            val buttonText = root.context.getString(R.string.occ_pinpoint_error_action)
            val span = SpannableString("$errorDescription $buttonText")
            span.setSpan(
                StyleSpan(Typeface.BOLD),
                errorDescription.length + 1,
                span.length,
                SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            root.context?.let {
                span.setSpan(
                    ForegroundColorSpan(
                        ContextCompat.getColor(
                            it,
                            com.tokopedia.unifyprinciples.R.color.Unify_G500
                        )
                    ),
                    errorDescription.length + 1,
                    span.length,
                    SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
            tvShippingPrice.text = span
            tvShippingCourier.visible()
            tvShippingPrice.visible()
            setMultiViewsOnClickListener(tvShippingCourier, tvShippingPrice) {
                onChoosePinpoint.invoke()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    fun renderSingleShipping(
        shipperName: String,
        shippingPrice: Int?,
        shippingEta: String?,
        logisticPromoViewModel: LogisticPromoUiModel?,
        isShowFreeShippingCourier: Boolean
    ) {
        binding?.apply {
            tvShippingDuration.gone()
            tvShippingDurationEta.gone()
            btnChangeDuration.gone()
            tvShippingPrice.gone()
            btnChangeCourier.gone()
            tvShippingErrorMessage.gone()
            btnReloadShipping.gone()
            iconReloadShipping.gone()
            tickerShippingPromo.gone()
            loaderShipping.gone()
            if (isShowFreeShippingCourier) {
                val formattedFreeShippingChosenCourierTitle = HtmlLinkHelper(
                    tvShippingCourier.context,
                    logisticPromoViewModel?.freeShippingChosenCourierTitle ?: ""
                ).spannedString
                tvShippingCourier.text = formattedFreeShippingChosenCourierTitle
            } else {
                tvShippingCourier.text = "$shipperName (${
                CurrencyFormatUtil.convertPriceValueToIdrFormat(shippingPrice ?: 0, false)
                    .removeDecimalSuffix()
                })"
            }
            if (shippingEta.isNullOrBlank()) {
                tvShippingCourierEta.setText(R.string.estimasi_tidak_tersedia)
            } else {
                tvShippingCourierEta.text = shippingEta
            }
            if (logisticPromoViewModel?.description?.isNotBlank() == true) {
                tvShippingCourierNotes.text =
                    MethodChecker.fromHtml(logisticPromoViewModel.description)
                tvShippingCourierNotes.visible()
            } else {
                tvShippingCourierNotes.gone()
            }
            tvShippingCourier.visible()
            tvShippingCourierEta.visible()
            setMultiViewsOnClickListener(
                tvShippingCourier,
                tvShippingCourierEta,
                tvShippingCourierNotes
            ) {
                /* no-op */
            }
        }
    }

    private fun setMultiViewsOnClickListener(vararg views: View?, onClickListener: () -> Unit) {
        for (view in views) {
            view?.setOnClickListener {
                onClickListener.invoke()
            }
        }
    }

    companion object {
        private const val ERROR_CODE_ZERO = 0
    }
}
