package com.tokopedia.topads.edit.view.adapter.edit_keyword.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.tokopedia.topads.edit.R
import com.tokopedia.topads.edit.view.adapter.edit_keyword.viewmodel.EditKeywordItemViewModel
import kotlinx.android.synthetic.main.topads_edit_keyword_edit_item_layout.view.*

/**
 * Created by Pika on 9/4/20.
 */

class EditKeywordItemViewHolder(val view: View,
                                private val actionClick: ((pos: Int) -> Unit)?) : EditKeywordViewHolder<EditKeywordItemViewModel>(view) {

    companion object {
        @LayoutRes
        var LAYOUT = R.layout.topads_edit_keyword_edit_item_layout

        private const val SPECIFIC_TYPE = "Spesifik"
        private const val BROAD_TYPE = "Luas"
        private const val EXACT_POSITIVE = 21
    }

    override fun bind(item: EditKeywordItemViewModel, added: MutableList<Boolean>) {

        item.data.let {
            view.keywordName.text = it.tag
            if (it.type == EXACT_POSITIVE) {
                view.keywordType.text = SPECIFIC_TYPE
            } else {
                view.keywordType.text = BROAD_TYPE
            }
            view.keywordBudget.text = "Rp " + it.priceBid.toString()
            if (added.isNotEmpty() && added.size > adapterPosition) {
                if (added[adapterPosition]) {
                    view.setBackgroundColor(ContextCompat.getColor(view.context, R.color.topads_item_selected))
                    view.dotImg.visibility = View.VISIBLE
                }
                else {
                    view.dotImg.visibility = View.INVISIBLE
                    view.setBackgroundColor(ContextCompat.getColor(view.context, R.color.white))
                }
            }
            view.setOnClickListener {
                actionClick?.invoke(adapterPosition)
            }
        }
    }
}