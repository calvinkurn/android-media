package com.tokopedia.logisticcart.shipping.features.shippingduration.view

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.TextUtils
import android.text.style.StyleSpan
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.logisticcart.databinding.ItemDurationBinding
import com.tokopedia.logisticcart.shipping.model.ShippingDurationUiModel
import com.tokopedia.purchase_platform.common.utils.removeDecimalSuffix
import com.tokopedia.utils.contentdescription.TextAndContentDescriptionUtil.setTextAndContentDescription
import com.tokopedia.utils.currency.CurrencyFormatUtil
import com.tokopedia.logisticcart.R as logisticcartR
import com.tokopedia.unifyprinciples.R as RUnify

/**
 * Created by Irfan Khoirul on 06/08/18.
 * tvDurationOrPrice means it will get duration in existing, and price when ETA is applied
 * tvPriceOrDuration means it will get price in existing, and duration when ETA is applied
 */
class ShippingDurationViewHolder(
    private val binding: ItemDurationBinding,
    private val cartPosition: Int
) : RecyclerView.ViewHolder(binding.root) {

    private var showCaseCoachmark: CoachMark2? = null

    fun bindData(
        shippingDurationUiModel: ShippingDurationUiModel,
        shippingDurationAdapterListener: ShippingDurationAdapterListener?,
        isDisableOrderPrioritas: Boolean
    ) {
        binding.run {
            if (shippingDurationUiModel.isShowShippingInformation && shippingDurationUiModel.etaErrorCode == 1) {
                tvShippingInformation.visibility = View.VISIBLE
            } else {
                tvShippingInformation.visibility = View.GONE
            }

            if (!TextUtils.isEmpty(shippingDurationUiModel.errorMessage)) {
                tvDurationOrPrice.setTextColor(
                    ContextCompat.getColor(
                        tvDurationOrPrice.context,
                        RUnify.color.Unify_N700_44
                    )
                )
                tvPriceOrDuration.visibility = View.GONE
                tvTextDesc.visibility = View.GONE
                tvOrderPrioritas.visibility = View.GONE
                tvError.text = shippingDurationUiModel.errorMessage
                tvError.visibility = View.VISIBLE
            } else {
                tvDurationOrPrice.setTextColor(
                    ContextCompat.getColor(
                        tvDurationOrPrice.context,
                        RUnify.color.Unify_N700_96
                    )
                )
                tvError.visibility = View.GONE
                tvPriceOrDuration.visibility = View.VISIBLE
                if (shippingDurationUiModel.serviceData.texts.textServiceDesc.isNotEmpty()) {
                    tvTextDesc.text = shippingDurationUiModel.serviceData.texts.textServiceDesc
                    tvTextDesc.visibility = View.VISIBLE
                } else {
                    tvTextDesc.visibility = View.GONE
                }
                if (!isDisableOrderPrioritas && shippingDurationUiModel.serviceData.orderPriority.now) {
                    val orderPrioritasTxt = itemView.context.getString(logisticcartR.string.order_prioritas)
                    val orderPrioritasLabel = SpannableString(orderPrioritasTxt)
                    orderPrioritasLabel.setSpan(
                        StyleSpan(Typeface.BOLD),
                        16,
                        orderPrioritasTxt.length,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    tvOrderPrioritas.text =
                        MethodChecker.fromHtml(shippingDurationUiModel.serviceData.orderPriority.staticMessage.durationMessage)
                    tvOrderPrioritas.visibility = View.VISIBLE
                } else {
                    tvOrderPrioritas.visibility = View.GONE
                }
            }

            /*MVC*/
            if (shippingDurationUiModel.merchantVoucherModel.isMvc == 1) {
                flContainer.visibility = View.VISIBLE
                flContainer.foreground =
                    ContextCompat.getDrawable(flContainer.context, logisticcartR.drawable.fg_enabled_item)
                ImageHandler.LoadImage(imgMvc, shippingDurationUiModel.merchantVoucherModel.mvcLogo)
                tvMvcText.text = shippingDurationUiModel.merchantVoucherModel.mvcTitle
                tvMvcError.visibility = View.GONE
            } else if (shippingDurationUiModel.merchantVoucherModel.isMvc == -1) {
                flContainer.visibility = View.VISIBLE
                flContainer.foreground =
                    ContextCompat.getDrawable(flContainer.context, logisticcartR.drawable.fg_disabled_item)
                ImageHandler.LoadImage(imgMvc, shippingDurationUiModel.merchantVoucherModel.mvcLogo)
                tvMvcText.text = shippingDurationUiModel.merchantVoucherModel.mvcTitle
                tvMvcError.visibility = View.VISIBLE
                tvMvcError.text = shippingDurationUiModel.merchantVoucherModel.mvcErrorMessage
            } else {
                flContainer.visibility = View.GONE
                tvMvcError.visibility = View.GONE
            }

            /*ETA*/
            if (shippingDurationUiModel.serviceData.texts.errorCode == 0) {
                val shipperNameEta: String =
                    if (shippingDurationUiModel.serviceData.rangePrice.minPrice == shippingDurationUiModel.serviceData.rangePrice.maxPrice) {
                        shippingDurationUiModel.serviceData.serviceName + " " + "(" +
                            CurrencyFormatUtil.convertPriceValueToIdrFormat(
                                shippingDurationUiModel.serviceData.rangePrice.minPrice,
                                false
                            ).removeDecimalSuffix() + ")"
                    } else {
                        val rangePrice = CurrencyFormatUtil.convertPriceValueToIdrFormat(
                            shippingDurationUiModel.serviceData.rangePrice.minPrice,
                            false
                        ).removeDecimalSuffix() + " - " +
                            CurrencyFormatUtil.convertPriceValueToIdrFormat(
                                shippingDurationUiModel.serviceData.rangePrice.maxPrice,
                                false
                            ).removeDecimalSuffix()
                        shippingDurationUiModel.serviceData.serviceName + " " + "(" + rangePrice + ")"
                    }
                if (shippingDurationUiModel.serviceData.texts.textEtaSummarize.isNotEmpty()) {
                    tvPriceOrDuration.text =
                        shippingDurationUiModel.serviceData.texts.textEtaSummarize
                } else {
                    tvPriceOrDuration.setText(logisticcartR.string.estimasi_tidak_tersedia)
                }
                setTextAndContentDescription(
                    tvDurationOrPrice,
                    shipperNameEta,
                    tvDurationOrPrice.context.getString(logisticcartR.string.content_desc_tv_duration)
                )
                lblCodAvailableEta.text = shippingDurationUiModel.codText
                lblCodAvailableEta.visibility =
                    if (shippingDurationUiModel.isCodAvailable) View.VISIBLE else View.GONE
            } else {
                setTextAndContentDescription(
                    tvDurationOrPrice,
                    shippingDurationUiModel.serviceData.serviceName,
                    tvDurationOrPrice.context.getString(logisticcartR.string.content_desc_tv_duration)
                )
                tvPriceOrDuration.text = shippingDurationUiModel.serviceData.texts.textRangePrice
                lblCodAvailableEta.text = shippingDurationUiModel.codText
                lblCodAvailableEta.visibility =
                    if (shippingDurationUiModel.isCodAvailable) View.VISIBLE else View.GONE
            }

            /*Dynamic Price*/
            if (shippingDurationUiModel.dynamicPriceModel.textLabel.isEmpty()) {
                lblDynamicPricing.visibility = View.GONE
            } else {
                lblDynamicPricing.visibility = View.VISIBLE
                lblDynamicPricing.text = shippingDurationUiModel.dynamicPriceModel.textLabel
            }
            imgCheck.visibility =
                if (shippingDurationUiModel.isSelected) View.VISIBLE else View.GONE
            if (shippingDurationUiModel.isShowShowCase) setShowCase()
            itemView.setOnClickListener {
                if (shippingDurationUiModel.errorMessage.isNullOrEmpty()) {
                    shippingDurationUiModel.isSelected = !shippingDurationUiModel.isSelected
                    shippingDurationAdapterListener?.onShippingDurationChoosen(
                        shippingDurationUiModel.shippingCourierViewModelList,
                        cartPosition,
                        shippingDurationUiModel.serviceData
                    )
                }
            }
        }
    }

    private fun setShowCase() {
        val label = itemView.context.getString(logisticcartR.string.label_title_showcase_shipping_duration)
        val text = itemView.context.getString(logisticcartR.string.label_body_showcase_shipping_duration)
        val coachMarkItem = CoachMark2Item(
            binding.layoutShippingDuration,
            label,
            text,
            CoachMark2.POSITION_BOTTOM
        )
        val list = ArrayList<CoachMark2Item>().apply {
            add(coachMarkItem)
        }
        showCaseCoachmark = CoachMark2(binding.root.context)
        showCaseCoachmark?.showCoachMark(list, null)
    }

    companion object {
        val ITEM_VIEW_SHIPMENT_DURATION = 5
    }
}
