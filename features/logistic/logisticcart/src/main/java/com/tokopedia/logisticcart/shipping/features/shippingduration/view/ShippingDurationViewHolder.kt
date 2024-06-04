package com.tokopedia.logisticcart.shipping.features.shippingduration.view

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.ErrorProductData.Companion.ERROR_PINPOINT_NEEDED
import com.tokopedia.logisticcart.databinding.ItemDurationBinding
import com.tokopedia.logisticcart.shipping.model.ShippingDurationUiModel
import com.tokopedia.logisticcart.utils.ShippingBottomSheetUtils
import com.tokopedia.media.loader.loadImageFitCenter
import com.tokopedia.purchase_platform.common.utils.removeDecimalSuffix
import com.tokopedia.utils.contentdescription.TextAndContentDescriptionUtil.setTextAndContentDescription
import com.tokopedia.utils.currency.CurrencyFormatUtil
import com.tokopedia.logisticcart.R as logisticcartR
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

/**
 * Created by Irfan Khoirul on 06/08/18.
 * tvDurationOrPrice means it will get duration in existing, and price when ETA is applied
 * tvPriceOrDuration means it will get price in existing, and duration when ETA is applied
 */
class ShippingDurationViewHolder(
    private val binding: ItemDurationBinding,
    private val cartPosition: Int
) : RecyclerView.ViewHolder(binding.root) {

    fun bindData(
        shippingDurationUiModel: ShippingDurationUiModel,
        shippingDurationAdapterListener: ShippingDurationAdapterListener?,
        isDisableOrderPrioritas: Boolean
    ) {
        binding.run {
            renderShippingInfo(shippingDurationUiModel)
            renderErrorMessage(shippingDurationUiModel)
            renderTextColor(shippingDurationUiModel)
            renderServiceSubtitle(shippingDurationUiModel)
            renderOrderPriority(shippingDurationUiModel, isDisableOrderPrioritas)
            renderMvc(shippingDurationUiModel)
            renderServiceName(shippingDurationUiModel)
            renderCodLabel(shippingDurationUiModel)
            renderEta(shippingDurationUiModel)
            renderDynamicPrice(shippingDurationUiModel)
            renderCheck(shippingDurationUiModel)
            setClickListener(shippingDurationUiModel, shippingDurationAdapterListener)
        }
    }

    private fun ItemDurationBinding.renderServiceName(shippingDurationUiModel: ShippingDurationUiModel) {
        shippingDurationUiModel.serviceData.run {
            if (texts.errorCode == 0) {
                val title: String =
                    if (rangePrice.minPrice == rangePrice.maxPrice) {
                        "$serviceName (${
                        CurrencyFormatUtil.convertPriceValueToIdrFormat(
                            rangePrice.minPrice,
                            false
                        ).removeDecimalSuffix()
                        })"
                    } else {
                        val rangePrice = CurrencyFormatUtil.convertPriceValueToIdrFormat(
                            rangePrice.minPrice,
                            false
                        ).removeDecimalSuffix() + " - " +
                            CurrencyFormatUtil.convertPriceValueToIdrFormat(
                                rangePrice.maxPrice,
                                false
                            ).removeDecimalSuffix()
                        "$serviceName ($rangePrice)"
                    }
                if (title.isNotEmpty()) {
                    tvDurationOrPrice.visible()
                    setTextAndContentDescription(
                        tvDurationOrPrice,
                        title,
                        tvDurationOrPrice.context.getString(logisticcartR.string.content_desc_tv_duration)
                    )
                } else {
                    tvDurationOrPrice.gone()
                }
            } else {
                if (serviceName.isNotEmpty()) {
                    setTextAndContentDescription(
                        tvDurationOrPrice,
                        serviceName,
                        tvDurationOrPrice.context.getString(logisticcartR.string.content_desc_tv_duration)
                    )
                    tvDurationOrPrice.visible()
                } else {
                    tvDurationOrPrice.gone()
                }
            }
        }
    }

    private fun ItemDurationBinding.renderShippingInfo(shippingDurationUiModel: ShippingDurationUiModel) {
        shippingDurationUiModel.run {
            if (isShowShippingInformation && etaErrorCode == 1) {
                tvShippingInformation.visible()
            } else {
                tvShippingInformation.gone()
            }
        }
    }

    private fun ItemDurationBinding.renderErrorMessage(shippingDurationUiModel: ShippingDurationUiModel) {
        shippingDurationUiModel.run {
            if (errorMessage?.isNotEmpty() == true) {
                val error = serviceData.error
                tvError.text = ShippingBottomSheetUtils.constructErrorUi(
                    binding.root.context,
                    error.errorMessage,
                    error.errorId
                )
                tvError.visible()
            } else {
                tvError.gone()
            }
        }
    }

    private fun ItemDurationBinding.renderTextColor(
        shippingDurationUiModel: ShippingDurationUiModel
    ) {
        shippingDurationUiModel.run {
            if (disabled) {
                tvDurationOrPrice.setTextColor(
                    ContextCompat.getColor(
                        tvDurationOrPrice.context,
                        unifyprinciplesR.color.Unify_NN950_44
                    )
                )
            } else {
                tvDurationOrPrice.setTextColor(
                    ContextCompat.getColor(
                        tvDurationOrPrice.context,
                        unifyprinciplesR.color.Unify_NN950_96
                    )
                )
            }
        }
    }

    private fun ItemDurationBinding.renderCodLabel(shippingDurationUiModel: ShippingDurationUiModel) {
        shippingDurationUiModel.run {
            if (isCodAvailable) {
                lblCodAvailableEta.text = codText
                lblCodAvailableEta.visible()
            } else {
                lblCodAvailableEta.gone()
            }
        }
    }

    private fun ItemDurationBinding.renderServiceSubtitle(shippingDurationUiModel: ShippingDurationUiModel) {
        shippingDurationUiModel.run {
            if (!shippingDurationUiModel.disabled && shippingDurationUiModel.serviceData.texts.textServiceDesc.isNotEmpty()) {
                tvTextDesc.text = shippingDurationUiModel.serviceData.texts.textServiceDesc
                tvTextDesc.visible()
            } else {
                tvTextDesc.gone()
            }
        }
    }

    private fun ItemDurationBinding.renderOrderPriority(
        shippingDurationUiModel: ShippingDurationUiModel,
        isDisableOrderPrioritas: Boolean
    ) {
        shippingDurationUiModel.run {
            if (!disabled && !isDisableOrderPrioritas && serviceData.orderPriority.now) {
                val orderPrioritasTxt =
                    root.context.getString(logisticcartR.string.order_prioritas)
                val orderPrioritasLabel = SpannableString(orderPrioritasTxt)
                orderPrioritasLabel.setSpan(
                    StyleSpan(Typeface.BOLD),
                    16,
                    orderPrioritasTxt.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                tvOrderPrioritas.text =
                    MethodChecker.fromHtml(shippingDurationUiModel.serviceData.orderPriority.staticMessage.durationMessage)
                tvOrderPrioritas.visible()
            } else {
                tvOrderPrioritas.gone()
            }
        }
    }

    private fun ItemDurationBinding.renderMvc(shippingDurationUiModel: ShippingDurationUiModel) {
        shippingDurationUiModel.merchantVoucherModel.run {
            when (isMvc) {
                1 -> {
                    flContainer.visible()
                    flContainer.foreground =
                        ContextCompat.getDrawable(
                            flContainer.context,
                            logisticcartR.drawable.fg_enabled_item
                        )
                    imgMvc.loadImageFitCenter(mvcLogo)
                    tvMvcText.text = mvcTitle
                    tvMvcError.gone()
                }

                -1 -> {
                    flContainer.visible()
                    flContainer.foreground =
                        ContextCompat.getDrawable(
                            flContainer.context,
                            logisticcartR.drawable.fg_disabled_item
                        )
                    imgMvc.loadImageFitCenter(mvcLogo)
                    tvMvcText.text = mvcTitle
                    tvMvcError.visible()
                    tvMvcError.text = mvcErrorMessage
                }

                else -> {
                    flContainer.gone()
                    tvMvcError.gone()
                }
            }
        }
    }

    private fun ItemDurationBinding.renderCheck(shippingDurationUiModel: ShippingDurationUiModel) {
        imgCheck.visibility =
            if (shippingDurationUiModel.isSelected) View.VISIBLE else View.GONE
    }

    private fun ItemDurationBinding.setClickListener(
        shippingDurationUiModel: ShippingDurationUiModel,
        listener: ShippingDurationAdapterListener?
    ) {
        root.setOnClickListener {
            if (!shippingDurationUiModel.disabled) {
                shippingDurationUiModel.isSelected = !shippingDurationUiModel.isSelected
                listener?.onShippingDurationChoosen(
                    shippingDurationUiModel.shippingCourierViewModelList,
                    cartPosition,
                    shippingDurationUiModel.serviceData,
                    shippingDurationUiModel
                )
            }
        }
    }

    private fun ItemDurationBinding.renderEta(shippingDurationUiModel: ShippingDurationUiModel) {
        shippingDurationUiModel.run {
            if (!disabled) {
                if (serviceData.texts.textEtaSummarize.isNotEmpty() && serviceData.texts.errorCode == 0) {
                    tvPriceOrDuration.text = serviceData.texts.textEtaSummarize
                } else {
                    tvPriceOrDuration.setText(logisticcartR.string.estimasi_tidak_tersedia)
                }
                tvPriceOrDuration.visible()
            } else {
                tvPriceOrDuration.gone()
            }
        }
    }

    private fun ItemDurationBinding.renderDynamicPrice(shippingDurationUiModel: ShippingDurationUiModel) {
        shippingDurationUiModel.dynamicPriceModel.run {
            if (textLabel.isEmpty()) {
                lblDynamicPricing.gone()
            } else {
                lblDynamicPricing.visible()
                lblDynamicPricing.text = textLabel
            }
        }
    }

    private val ShippingDurationUiModel.disabled: Boolean
        get() {
            return serviceData.error.errorId.toIntOrZero() != 0 && serviceData.error.errorId != ERROR_PINPOINT_NEEDED
        }

    companion object {
        val ITEM_VIEW_SHIPMENT_DURATION = 5
    }
}
