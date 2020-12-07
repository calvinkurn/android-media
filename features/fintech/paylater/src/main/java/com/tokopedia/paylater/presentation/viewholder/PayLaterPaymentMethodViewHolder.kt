package com.tokopedia.paylater.presentation.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.paylater.R
import com.tokopedia.paylater.presentation.adapter.PayLaterOfferDescriptionAdapter
import kotlinx.android.synthetic.main.paylater_payment_method_item.view.*

class PayLaterPaymentMethodViewHolder(val view: View, val clickListener: () -> Unit) : RecyclerView.ViewHolder(view) {

    fun bindData() {
        view.ivPayLaterArrow.setOnClickListener { clickListener() }
        ImageHandler.loadImage(view.context,
                view.ivPayLaterPartner,
                "https://ecs7.tokopedia.net/assets-fintech-frontend/pdp/kredivo/kredivo.png",
                R.drawable.ic_loading_image)
    }

    companion object {
        private val LAYOUT_ID = R.layout.paylater_payment_method_item

        fun getViewHolder(inflater: LayoutInflater, parent: ViewGroup, clickListener: () -> Unit) = PayLaterPaymentMethodViewHolder(
                inflater.inflate(LAYOUT_ID, parent, false), clickListener
        )
    }
}