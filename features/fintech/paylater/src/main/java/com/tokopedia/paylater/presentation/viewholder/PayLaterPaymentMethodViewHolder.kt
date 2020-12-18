package com.tokopedia.paylater.presentation.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.paylater.PayLaterHelper
import com.tokopedia.paylater.R
import com.tokopedia.paylater.domain.model.PayLaterApplicationDetail
import com.tokopedia.paylater.domain.model.PayLaterItemProductData
import kotlinx.android.synthetic.main.fragment_paylater_cards_info.*
import kotlinx.android.synthetic.main.paylater_payment_method_item.view.*

class PayLaterPaymentMethodViewHolder(val view: View, val clickListener: (PayLaterItemProductData, PayLaterApplicationDetail?) -> Unit) : RecyclerView.ViewHolder(view) {

    fun bindData(payLaterItemProductData: PayLaterItemProductData, payLaterApplicationDataForPartner: PayLaterApplicationDetail?) {
        view.apply {
            ivPayLaterArrow.setOnClickListener { clickListener(payLaterItemProductData, payLaterApplicationDataForPartner) }
            ivPayLaterPartner.maxHeight = context.dpToPx(72).toInt()
            ivPayLaterPartner.maxWidth = context.dpToPx(80).toInt()
            ImageHandler.loadImage(context,
                    ivPayLaterPartner,
                    payLaterItemProductData.partnerImgLightUrl,
                    R.drawable.ic_loading_image)
            tvTitlePaylaterPartner.text = payLaterItemProductData.partnerName
            tvDescription.text = "${context.getString(R.string.payLater_verification_until_subtitle)} ${payLaterApplicationDataForPartner?.payLaterExpirationDate}"
            payLaterApplicationDataForPartner?.let {
                PayLaterHelper.setLabelData(context,  payLaterOfferLabel, it)
            }
        }
    }

    companion object {
        private val LAYOUT_ID = R.layout.paylater_payment_method_item

        fun getViewHolder(inflater: LayoutInflater, parent: ViewGroup, clickListener: (PayLaterItemProductData, PayLaterApplicationDetail?) -> Unit) = PayLaterPaymentMethodViewHolder(
                inflater.inflate(LAYOUT_ID, parent, false), clickListener
        )
    }
}