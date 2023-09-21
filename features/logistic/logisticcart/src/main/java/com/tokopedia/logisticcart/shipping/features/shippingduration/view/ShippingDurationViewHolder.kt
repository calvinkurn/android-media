package com.tokopedia.logisticcart.shipping.features.shippingduration.view

import android.app.Activity
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.TextUtils
import android.text.style.StyleSpan
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.logisticcart.R
import com.tokopedia.logisticcart.shipping.model.ShippingDurationUiModel
import com.tokopedia.purchase_platform.common.utils.removeDecimalSuffix
import com.tokopedia.showcase.ShowCaseBuilder
import com.tokopedia.showcase.ShowCaseContentPosition
import com.tokopedia.showcase.ShowCaseDialog
import com.tokopedia.showcase.ShowCaseObject
import com.tokopedia.showcase.ShowCasePreference
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.contentdescription.TextAndContentDescriptionUtil.setTextAndContentDescription
import com.tokopedia.utils.currency.CurrencyFormatUtil
import com.tokopedia.unifyprinciples.R as RUnify

/**
 * Created by Irfan Khoirul on 06/08/18.
 * tvDurationOrPrice means it will get duration in existing, and price when ETA is applied
 * tvPriceOrDuration means it will get price in existing, and duration when ETA is applied
 */
class ShippingDurationViewHolder(itemView: View, private val cartPosition: Int) : RecyclerView.ViewHolder(itemView) {

    private val tvError: TextView = itemView.findViewById(R.id.tv_error)
    private val tvDurationOrPrice: TextView = itemView.findViewById(R.id.tv_duration_or_price)
    private val tvPriceOrDuration: TextView = itemView.findViewById(R.id.tv_price_or_duration)
    private val tvTextDesc: TextView = itemView.findViewById(R.id.tv_text_desc)
    private val imgCheck: IconUnify = itemView.findViewById(R.id.img_check)
    private val rlContent: RelativeLayout = itemView.findViewById(R.id.rl_content)
    private val tvOrderPrioritas: TextView = itemView.findViewById(R.id.tv_order_prioritas)
    private val tvShippingInformation: Typography = itemView.findViewById(R.id.tv_shipping_information)
    private val labelCodAvailable: Label = itemView.findViewById(R.id.lbl_cod_available)
    private val labelCodAvailableEta: Label = itemView.findViewById(R.id.lbl_cod_available_eta)
    private val tvMvcError: Typography = itemView.findViewById(R.id.tv_mvc_error)
    private val imgMvc: ImageView = itemView.findViewById(R.id.img_mvc)
    private val tvMvc: Typography = itemView.findViewById(R.id.tv_mvc_text)
    private val layoutMvc: ConstraintLayout = itemView.findViewById(R.id.layout_mvc)
    private val flDisableContainer: FrameLayout = itemView.findViewById(R.id.fl_container)
    private val labelDynamicPricing: Label = itemView.findViewById(R.id.lbl_dynamic_pricing)

    fun bindData(
        shippingDurationUiModel: ShippingDurationUiModel,
        shippingDurationAdapterListener: ShippingDurationAdapterListener?,
        isDisableOrderPrioritas: Boolean
    ) {
        if (shippingDurationUiModel.isShowShippingInformation && shippingDurationUiModel.etaErrorCode == 1) {
            tvShippingInformation.visibility = View.VISIBLE
        } else {
            tvShippingInformation.visibility = View.GONE
        }

        if (!TextUtils.isEmpty(shippingDurationUiModel.errorMessage)) {
            tvDurationOrPrice.setTextColor(ContextCompat.getColor(tvDurationOrPrice.context, RUnify.color.Unify_N700_44))
            tvPriceOrDuration.visibility = View.GONE
            tvTextDesc.visibility = View.GONE
            tvOrderPrioritas.visibility = View.GONE
            tvError.text = shippingDurationUiModel.errorMessage
            tvError.visibility = View.VISIBLE
        } else {
            tvDurationOrPrice.setTextColor(ContextCompat.getColor(tvDurationOrPrice.context, RUnify.color.Unify_N700_96))
            tvError.visibility = View.GONE
            tvPriceOrDuration.visibility = View.VISIBLE
            if (shippingDurationUiModel.serviceData.texts.textServiceDesc.isNotEmpty()) {
                tvTextDesc.text = shippingDurationUiModel.serviceData.texts.textServiceDesc
                tvTextDesc.visibility = View.VISIBLE
            } else {
                tvTextDesc.visibility = View.GONE
            }
            if (!isDisableOrderPrioritas && shippingDurationUiModel.serviceData.orderPriority.now) {
                val orderPrioritasTxt = itemView.context.getString(R.string.order_prioritas)
                val orderPrioritasLabel = SpannableString(orderPrioritasTxt)
                orderPrioritasLabel.setSpan(StyleSpan(Typeface.BOLD), 16, orderPrioritasTxt.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                tvOrderPrioritas.text = MethodChecker.fromHtml(shippingDurationUiModel.serviceData.orderPriority.staticMessage.getDurationMessage())
                tvOrderPrioritas.visibility = View.VISIBLE
            } else {
                tvOrderPrioritas.visibility = View.GONE
            }
        }

        /*MVC*/
        if (shippingDurationUiModel.merchantVoucherModel != null && shippingDurationUiModel.merchantVoucherModel!!.isMvc == 1) {
            layoutMvc.visibility = View.VISIBLE
            flDisableContainer.foreground = ContextCompat.getDrawable(flDisableContainer.context, R.drawable.fg_enabled_item)
            ImageHandler.LoadImage(imgMvc, shippingDurationUiModel.merchantVoucherModel!!.mvcLogo)
            tvMvc.text = shippingDurationUiModel.merchantVoucherModel!!.mvcTitle
            tvMvcError.visibility = View.GONE
        } else if (shippingDurationUiModel.merchantVoucherModel != null && shippingDurationUiModel.merchantVoucherModel!!.isMvc == -1) {
            layoutMvc.visibility = View.VISIBLE
            flDisableContainer.foreground = ContextCompat.getDrawable(flDisableContainer.context, R.drawable.fg_disabled_item)
            ImageHandler.LoadImage(imgMvc, shippingDurationUiModel.merchantVoucherModel!!.mvcLogo)
            tvMvc.text = shippingDurationUiModel.merchantVoucherModel!!.mvcTitle
            tvMvcError.visibility = View.VISIBLE
            tvMvcError.text = shippingDurationUiModel.merchantVoucherModel!!.mvcErrorMessage
        } else {
            layoutMvc.visibility = View.GONE
            tvMvcError.visibility = View.GONE
        }

        /*ETA*/
        if (shippingDurationUiModel.serviceData.texts.errorCode == 0) {
            val shipperNameEta: String = if (shippingDurationUiModel.serviceData.rangePrice.minPrice == shippingDurationUiModel.serviceData.rangePrice.maxPrice) {
                shippingDurationUiModel.serviceData.serviceName + " " + "(" +
                    CurrencyFormatUtil.convertPriceValueToIdrFormat(shippingDurationUiModel.serviceData.rangePrice.minPrice, false).removeDecimalSuffix() + ")"
            } else {
                val rangePrice = CurrencyFormatUtil.convertPriceValueToIdrFormat(shippingDurationUiModel.serviceData.rangePrice.minPrice, false).removeDecimalSuffix() + " - " +
                    CurrencyFormatUtil.convertPriceValueToIdrFormat(shippingDurationUiModel.serviceData.rangePrice.maxPrice, false).removeDecimalSuffix()
                shippingDurationUiModel.serviceData.serviceName + " " + "(" + rangePrice + ")"
            }
            if (shippingDurationUiModel.serviceData.texts.textEtaSummarize.isNotEmpty()) {
                tvPriceOrDuration.text = shippingDurationUiModel.serviceData.texts.textEtaSummarize
            } else {
                tvPriceOrDuration.setText(R.string.estimasi_tidak_tersedia)
            }
            setTextAndContentDescription(tvDurationOrPrice, shipperNameEta, tvDurationOrPrice.context.getString(R.string.content_desc_tv_duration))
            labelCodAvailable.visibility = View.GONE
            labelCodAvailableEta.text = shippingDurationUiModel.codText
            labelCodAvailableEta.visibility = if (shippingDurationUiModel.isCodAvailable) View.VISIBLE else View.GONE
        } else {
            setTextAndContentDescription(tvDurationOrPrice, shippingDurationUiModel.serviceData.serviceName, tvDurationOrPrice.context.getString(R.string.content_desc_tv_duration))
            tvPriceOrDuration.text = shippingDurationUiModel.serviceData.texts.textRangePrice
            labelCodAvailableEta.visibility = View.GONE
            labelCodAvailable.text = shippingDurationUiModel.codText
            labelCodAvailable.visibility = if (shippingDurationUiModel.isCodAvailable) View.VISIBLE else View.GONE
        }

        /*Dynamic Price*/
        if (shippingDurationUiModel.dynamicPriceModel == null || shippingDurationUiModel.dynamicPriceModel!!.textLabel.isEmpty()) {
            labelDynamicPricing.visibility = View.GONE
        } else {
            labelDynamicPricing.visibility = View.VISIBLE
            labelDynamicPricing.text = shippingDurationUiModel.dynamicPriceModel!!.textLabel
        }
        imgCheck.visibility = if (shippingDurationUiModel.isSelected) View.VISIBLE else View.GONE
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

    private fun setShowCase() {
        val label = itemView.context.getString(R.string.label_title_showcase_shipping_duration)
        val text = itemView.context.getString(R.string.label_body_showcase_shipping_duration)
        val showCase = ShowCaseObject(rlContent, label, text, ShowCaseContentPosition.UNDEFINED)
        val showCaseObjectList = ArrayList<ShowCaseObject>()
        showCaseObjectList.add(showCase)
        val showCaseDialog = createShowCaseDialog()
        showCaseDialog.setShowCaseStepListener { _, _, _ -> false }
        if (!ShowCasePreference.hasShown(itemView.context, ShippingDurationViewHolder::class.java.name)) {
            showCaseDialog.show(
                itemView.context as Activity,
                ShippingDurationViewHolder::class.java.name,
                showCaseObjectList
            )
        }
    }

    private fun createShowCaseDialog(): ShowCaseDialog {
        return ShowCaseBuilder()
            .customView(R.layout.show_case_checkout)
            .titleTextColorRes(com.tokopedia.unifyprinciples.R.color.Unify_NN0)
            .spacingRes(com.tokopedia.design.R.dimen.dp_12)
            .arrowWidth(com.tokopedia.design.R.dimen.dp_16)
            .textColorRes(com.tokopedia.unifyprinciples.R.color.Unify_NN400)
            .shadowColorRes(com.tokopedia.unifyprinciples.R.color.Unify_NN950_68)
            .backgroundContentColorRes(com.tokopedia.unifyprinciples.R.color.Unify_NN950)
            .circleIndicatorBackgroundDrawableRes(com.tokopedia.showcase.R.drawable.selector_circle_green)
            .textSizeRes(com.tokopedia.design.R.dimen.sp_12)
            .finishStringRes(R.string.label_shipping_show_case_finish)
            .useCircleIndicator(true)
            .clickable(true)
            .useArrow(true)
            .build()
    }

    companion object {
        val ITEM_VIEW_SHIPMENT_DURATION = R.layout.item_duration
    }
}
