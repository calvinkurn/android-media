package com.tokopedia.home.account.presentation.viewholder

import android.view.View
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home.account.R
import com.tokopedia.home.account.presentation.listener.AccountItemListener
import com.tokopedia.home.account.presentation.viewmodel.LabelledMenuListUiModel

class LabelledMenuListViewHolder(itemView: View, private val listener: AccountItemListener) : AbstractViewHolder<LabelledMenuListUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_menu_with_label

        private const val CLICK_TIME_INTERVAL: Long = 1000
    }

    private var lastClickTime = System.currentTimeMillis()

    private val layout: View = itemView.findViewById(R.id.container)
    private val tvTitle: TextView = itemView.findViewById(R.id.text_view_title)
    private val tvDescription: TextView = itemView.findViewById(R.id.text_view_description)
    private val label: TextView = itemView.findViewById(R.id.label_menu)
    private val separator: View = itemView.findViewById(R.id.separator)

    override fun bind(element: LabelledMenuListUiModel) {
        layout.setOnClickListener { v ->
            val now = System.currentTimeMillis()
            if (now - lastClickTime >= CLICK_TIME_INTERVAL) {
                listener.onMenuListClicked(element)
                lastClickTime = now
            }
        }

        tvTitle.text = element.menu
        label.text = element.label
        tvDescription.text = element.menuDescription

        if (element.isUseSeparator) {
            separator.visibility = View.VISIBLE
        } else {
            separator.visibility = View.GONE
        }
    }
}