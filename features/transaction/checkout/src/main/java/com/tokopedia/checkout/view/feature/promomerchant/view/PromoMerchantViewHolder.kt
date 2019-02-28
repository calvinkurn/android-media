package com.tokopedia.checkout.view.feature.promomerchant.view

import android.app.Activity
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.tokopedia.checkout.R
import com.tokopedia.showcase.*
import java.util.ArrayList

/**
 * Created by fwidjaja on 28/02/19.
 */
class PromoMerchantViewHolder(itemView: View, private val adapter: PromoMerchantAdapter,
                                 private val cartPosition: Int, // set true if has courier promo, whether own courier or other duration's courier
                                 private val hasCourierPromo: Boolean) : RecyclerView.ViewHolder(itemView) {

    private val tvError: TextView
    private val tvDuration: TextView
    private val tvPrice: TextView
    private val tvCod: TextView
    private val tvTextDesc: TextView
    private val imgCheck: ImageView
    private val tvDurationHeaderInfo: TextView
    private val rlContent: RelativeLayout
    private val tvPromoPotency: TextView

    init {

        tvError = itemView.findViewById(R.id.tv_error)
        tvDuration = itemView.findViewById(R.id.tv_duration)
        tvPrice = itemView.findViewById(R.id.tv_price)
        tvTextDesc = itemView.findViewById(R.id.tv_text_desc)
        imgCheck = itemView.findViewById(R.id.img_check)
        tvDurationHeaderInfo = itemView.findViewById(R.id.tv_duration_header_info)
        rlContent = itemView.findViewById(R.id.rl_content)
        tvPromoPotency = itemView.findViewById(R.id.tv_promo_potency)
        tvCod = itemView.findViewById(R.id.tv_cod_availability)
    }

    fun bindData(shippingDurationViewModel: ShippingDurationViewModel,
                 promoMerchantAdapterListener: PromoMerchantAdapterListener) {

        if (shippingDurationAdapterListener.isToogleYearEndPromotionOn && shippingDurationViewModel.serviceData.isPromo == 1) {
            tvPromoPotency.visibility = View.VISIBLE
        } else {
            tvPromoPotency.visibility = View.GONE
        }

        if (!TextUtils.isEmpty(shippingDurationViewModel.errorMessage)) {
            tvDuration.setTextColor(ContextCompat.getColor(tvDuration.context, R.color.font_disabled))
            tvPrice.visibility = View.GONE
            tvTextDesc.visibility = View.GONE
            tvError.text = shippingDurationViewModel.errorMessage
            tvError.visibility = View.VISIBLE
        } else {
            tvDuration.setTextColor(ContextCompat.getColor(tvDuration.context, R.color.black_70))
            tvError.visibility = View.GONE
            tvPrice.text = shippingDurationViewModel.serviceData.texts.textRangePrice
            tvPrice.visibility = View.VISIBLE

            if (!shippingDurationViewModel.serviceData.texts.textServiceDesc.isEmpty()) {
                tvTextDesc.text = shippingDurationViewModel.serviceData.texts.textServiceDesc
                tvTextDesc.visibility = View.VISIBLE
            } else {
                tvTextDesc.visibility = View.GONE
            }
        }

        tvDuration.text = shippingDurationViewModel.serviceData.serviceName
        imgCheck.visibility = if (shippingDurationViewModel.isSelected) View.VISIBLE else View.GONE
        tvCod.text = shippingDurationViewModel.codText
        tvCod.visibility = if (shippingDurationViewModel.isCodAvailable) View.VISIBLE else View.GONE
        itemView.setOnClickListener {
            if (TextUtils.isEmpty(shippingDurationViewModel.errorMessage)) {
                shippingDurationViewModel.isSelected = !shippingDurationViewModel.isSelected
                shippingDurationAdapterListener.onShippingDurationChoosen(
                        shippingDurationViewModel.shippingCourierViewModelList, cartPosition,
                        shippingDurationViewModel.serviceData, hasCourierPromo)
            }
        }

        if (adapterPosition == adapter.itemCount - 1) {
            shippingDurationAdapterListener.onAllShippingDurationItemShown()
        }

        if (adapterPosition == 0) {
            tvDurationHeaderInfo.visibility = View.VISIBLE
            if (shippingDurationViewModel.isShowShowCase) {
                setShowCase(shippingDurationAdapterListener)
            }
        } else {
            tvDurationHeaderInfo.visibility = View.GONE
        }

    }

    private fun setShowCase(shippingDurationAdapterListener: PromoMerchantAdapterListener) {
        val label = itemView.context.getString(R.string.label_title_showcase_shipping_duration)
        val text = itemView.context.getString(R.string.label_body_showcase_shipping_duration)
        val showCase = ShowCaseObject(rlContent, label, text, ShowCaseContentPosition.UNDEFINED)

        val showCaseObjectList = ArrayList<ShowCaseObject>()

        showCaseObjectList.add(showCase)

        val showCaseDialog = createShowCaseDialog()
        showCaseDialog.setShowCaseStepListener { previousStep, nextStep, showCaseObject -> false }

        if (!ShowCasePreference.hasShown(itemView.context, PromoMerchantViewHolder::class.java.name))
            showCaseDialog.show(
                    itemView.context as Activity,
                    PromoMerchantViewHolder::class.java.name,
                    showCaseObjectList
            )
    }

    private fun createShowCaseDialog(): ShowCaseDialog {
        return ShowCaseBuilder()
                .customView(R.layout.show_case_checkout)
                .titleTextColorRes(R.color.white)
                .spacingRes(R.dimen.dp_12)
                .arrowWidth(R.dimen.dp_16)
                .textColorRes(R.color.grey_400)
                .shadowColorRes(R.color.shadow)
                .backgroundContentColorRes(R.color.black)
                .circleIndicatorBackgroundDrawableRes(R.drawable.selector_circle_green)
                .textSizeRes(R.dimen.sp_12)
                .finishStringRes(R.string.label_shipping_show_case_finish)
                .useCircleIndicator(true)
                .clickable(true)
                .useArrow(true)
                .build()
    }

    companion object {

        val ITEM_VIEW_PROMO_MERCHANT = R.layout.item_promo_merchant
    }

}