package com.tokopedia.pdpsimulation.paylater.presentation.registration

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.pdpsimulation.R
import com.tokopedia.pdpsimulation.paylater.domain.model.PayLaterApplicationDetail
import com.tokopedia.pdpsimulation.paylater.domain.model.PayLaterItemProductData
import com.tokopedia.utils.view.DarkModeUtil.isDarkMode
import kotlinx.android.synthetic.main.base_payment_register_item.view.*

class PayLaterPaymentMethodViewHolder(val view: View, val clickListener: (PayLaterItemProductData, PayLaterApplicationDetail?) -> Unit) : RecyclerView.ViewHolder(view) {

    fun bindData(payLaterItemProductData: PayLaterItemProductData, payLaterApplicationDataForPartner: PayLaterApplicationDetail?) {
        val imageUrl: String?
        view.apply {
            setOnClickListener { clickListener(payLaterItemProductData, payLaterApplicationDataForPartner) }

            ivPartnerLogo.layoutParams.height = context.dpToPx(18).toInt()
            ivPartnerLogo.layoutParams.width = context.dpToPx(48).toInt()

            if (context.isDarkMode())
                imageUrl = payLaterItemProductData.partnerImgDarkUrl
            else imageUrl = payLaterItemProductData.partnerImgLightUrl
            if (!imageUrl.isNullOrEmpty())
                ivPartnerLogo.loadImage(imageUrl)

            tvTitlePaymentPartner.text = payLaterItemProductData.partnerName ?: ""
            setSubTitleContent(payLaterApplicationDataForPartner, this)
        }
    }

    private fun setSubTitleContent(payLaterApplicationDataForPartner: PayLaterApplicationDetail?, view: View) {
        view.apply {
            payLaterApplicationDataForPartner?.let {
                setLabel(it, this)
                if (it.payLaterStatusContent?.verificationContentSubHeader.isNullOrEmpty()) {
                    tvDescription.text = context.getString(R.string.pay_later_default_subtitle)
                } else {
                    tvDescription.text = it.payLaterStatusContent?.verificationContentSubHeader?.parseAsHtml()
                            ?: context.getString(R.string.pay_later_default_subtitle)
                }
            }
        }
    }

    private fun setLabel(applicationDetail: PayLaterApplicationDetail, view: View) {
        applicationDetail.payLaterApplicationStatusLabelStringId.also { resId ->
            if (resId != 0) {
                view.paymentOfferLabel.visible()
                view.paymentOfferLabel.text = view.context.getString(resId)
                view.paymentOfferLabel.setLabelType(applicationDetail.payLaterApplicationStatusLabelType)
            } else {
                view.paymentOfferLabel.gone()
            }
        }
    }

    companion object {
        private val LAYOUT_ID = R.layout.base_payment_register_item

        fun getViewHolder(inflater: LayoutInflater, parent: ViewGroup, clickListener: (PayLaterItemProductData, PayLaterApplicationDetail?) -> Unit) = PayLaterPaymentMethodViewHolder(
                inflater.inflate(LAYOUT_ID, parent, false), clickListener
        )
    }
}