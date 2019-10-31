package com.tokopedia.topads.dashboard.view.adapter.viewholder

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.TextView

import com.tokopedia.topads.dashboard.R
import kotlinx.android.synthetic.main.item_tab_layout.view.*

/**
 * Created by hadi.putra on 15/05/18.
 */

class TabLayoutViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val mainTextView: TextView = itemView.title
    private val subTextView: TextView = itemView.subtitle
    private val baseView: View = itemView.base_layout

    fun bind(title: String, subtitle: String) {
        mainTextView.text = title
        subTextView.text = subtitle
    }

    fun toggleActivate(isActive: Boolean) {
        val white = ContextCompat.getColor(itemView.context, com.tokopedia.topads.auto.R.color.white)
        val black = ContextCompat.getColor(itemView.context, com.tokopedia.design.R.color.font_voucher)
        val blackDisabled = ContextCompat.getColor(itemView.context, com.tokopedia.design.R.color.font_black_disabled_38)
        if (isActive) {
            baseView.background = ContextCompat.getDrawable(itemView.context, R.drawable.gradient_background)
            mainTextView.setTextColor(white)
            subTextView.setTextColor(white)
        } else {
            baseView.setBackgroundColor(white)
            mainTextView.setTextColor(black)
            subTextView.setTextColor(blackDisabled)
        }
    }
}
