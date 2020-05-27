package com.tokopedia.topads.edit.view.adapter.edit_neg_keyword.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.topads.edit.R
import com.tokopedia.topads.edit.view.adapter.edit_neg_keyword.viewmodel.EditNegKeywordItemViewModel
import com.tokopedia.topads.edit.view.sheet.EditKeywordSortSheet
import kotlinx.android.synthetic.main.topads_edit_negative_keyword_edit_item_layout.view.*

/**
 * Created by Pika on 12/4/20.
 */

class EditNegKeywordItemViewHolder(val view: View, private var actionDelete: ((pos: Int) -> Unit)?) : EditNegKeywordViewHolder<EditNegKeywordItemViewModel>(view) {

    companion object {
        @LayoutRes
        var LAYOUT = R.layout.topads_edit_negative_keyword_edit_item_layout
        val TITLE_1 = "Pencarian luas"
        val TITLE_2 = "Pencarian Spesifik"
        val BROAD = 12
        val SPECIFIC = 22
    }

    private lateinit var sortKeywordList: EditKeywordSortSheet

    override fun bind(item: EditNegKeywordItemViewModel) {
        item.data.let {
            view.keyword_name.text = it.tag
            view.delete.setOnClickListener {
                actionDelete?.invoke(adapterPosition)
            }
            if (it.type == BROAD)
                view.sort.text = TITLE_1
            else
                view.sort.text = TITLE_2

            view.sort.setOnClickListener {
                sortKeywordList = EditKeywordSortSheet.newInstance(view.context)
                sortKeywordList.show()
                sortKeywordList.onItemClick = {
                    view.sort.text = sortKeywordList.getSelectedSortId()
                    if (sortKeywordList.getSelectedSortId() == TITLE_1) {
                        item.data.type = BROAD
                    } else {
                        item.data.type = SPECIFIC
                    }
                }

            }

        }

    }

}