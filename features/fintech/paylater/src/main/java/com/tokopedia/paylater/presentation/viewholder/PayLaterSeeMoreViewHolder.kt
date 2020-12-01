package com.tokopedia.paylater.presentation.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.paylater.R

class PayLaterSeeMoreViewHolder(view: View): RecyclerView.ViewHolder(view) {

    fun bind() {

    }

    companion object {
        private val LAYOUT_ID = R.layout.paylater_payment_option_ghost_button_item

        fun getViewHolder(inflater: LayoutInflater, parent: ViewGroup) = PayLaterSeeMoreViewHolder(
                inflater.inflate(PayLaterSeeMoreViewHolder.LAYOUT_ID, parent, false)
        )
    }
}