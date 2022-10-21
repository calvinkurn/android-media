package com.tokopedia.topads.dashboard.view.adapter.viewholder

import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.topads.dashboard.R
import com.tokopedia.unifyprinciples.Typography

/**
 * Created by hadi.putra on 15/05/18.
 */

class TabLayoutViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val mainTextView: Typography = itemView.findViewById(R.id.title)
    private val subTextView: Typography = itemView.findViewById(R.id.subtitle)
    private val baseView: LinearLayout = itemView.findViewById(R.id.base_layout)

    fun bind(title: String, subtitle: String) {
        mainTextView.text = title
        subTextView.text = subtitle
    }

    fun toggleActivate(isActive: Boolean) {
        val white = ContextCompat.getColor(itemView.context,
            com.tokopedia.topads.common.R.color.Unify_Background)
        val black = ContextCompat.getColor(itemView.context,
            com.tokopedia.unifyprinciples.R.color.Unify_NN950)
        val blackDisabled = ContextCompat.getColor(itemView.context,
            com.tokopedia.unifyprinciples.R.color.Unify_NN900)
        if (isActive) {
            baseView.background =
                ContextCompat.getDrawable(itemView.context, R.drawable.gradient_background)
            mainTextView.setTextColor(white)
            subTextView.setTextColor(white)
        } else {
            baseView.setBackgroundColor(white)
            mainTextView.setTextColor(black)
            subTextView.setTextColor(blackDisabled)
        }
    }
}
