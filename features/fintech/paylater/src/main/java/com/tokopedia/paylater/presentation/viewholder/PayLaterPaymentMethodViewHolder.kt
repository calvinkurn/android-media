package com.tokopedia.paylater.presentation.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.paylater.R
import com.tokopedia.paylater.domain.model.PayLaterApplicationDetail
import com.tokopedia.paylater.domain.model.PayLaterItemProductData
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
                ImageHandler.loadImage(context,
                        ivPartnerLogo,
                        imageUrl,
                        R.drawable.ic_loading_image)

            tvTitlePaymentPartner.text = payLaterItemProductData.partnerName ?: ""
            tvDescription.text = payLaterApplicationDataForPartner?.payLaterStatusContent?.verificationContentSubHeader
                    ?: context.getString(R.string.pay_later_default_subtitle)
            payLaterApplicationDataForPartner?.let {
                it.payLaterApplicationStatusLabelStringId.also { resId ->
                    if (resId != 0) {
                        paymentOfferLabel.visible()
                        paymentOfferLabel.text = context.getString(resId)
                        paymentOfferLabel.setLabelType(it.payLaterApplicationStatusLabelType)
                    } else {
                        paymentOfferLabel.gone()
                    }
                }
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