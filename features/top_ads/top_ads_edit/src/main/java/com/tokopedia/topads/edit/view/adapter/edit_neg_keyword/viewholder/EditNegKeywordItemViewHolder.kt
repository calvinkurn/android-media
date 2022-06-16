package com.tokopedia.topads.edit.view.adapter.edit_neg_keyword.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.FragmentActivity
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.topads.edit.R
import com.tokopedia.topads.edit.view.adapter.edit_neg_keyword.viewmodel.EditNegKeywordItemViewModel
import com.tokopedia.topads.edit.view.sheet.EditKeywordSortSheet
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

/**
 * Created by Pika on 12/4/20.
 */

class EditNegKeywordItemViewHolder(
    val view: View,
    private var actionDelete: ((pos: Int) -> Unit)?,
    private var actionStatusChange: ((pos: Int) -> Unit),
) : EditNegKeywordViewHolder<EditNegKeywordItemViewModel>(view) {

    private var keywordName: Typography = view.findViewById(R.id.keyword_name)
    private var sort: Typography = view.findViewById(R.id.sort)
    private var dropDownArrow: ImageUnify = view.findViewById(R.id.drop_down_arrow)
    private var delete: ImageUnify = view.findViewById(R.id.delete)

    companion object {
        @LayoutRes
        var LAYOUT = R.layout.topads_edit_negative_keyword_edit_item_layout
        private const val TITLE_1 = "Luas"
        private const val TITLE_2 = "Spesifik"
        private const val BROAD = 12
        private const val SPECIFIC = 22
    }

    private lateinit var sortKeywordList: EditKeywordSortSheet

    override fun bind(item: EditNegKeywordItemViewModel) {
        item.data.let {
            keywordName.text = it.tag
            delete.setImageDrawable(getIconUnifyDrawable(view.context, IconUnify.DELETE))
            delete.setOnClickListener {
                actionDelete?.invoke(adapterPosition)
            }
            if (it.type == BROAD)
                sort.text = TITLE_1
            else
                sort.text = TITLE_2

            dropDownArrow.setImageDrawable(view.context.getResDrawable(com.tokopedia.iconunify.R.drawable.iconunify_edit))

            dropDownArrow.setOnClickListener {
                sortKeywordList = EditKeywordSortSheet.newInstance()
                sortKeywordList.setChecked(sort.text.toString())
                sortKeywordList.show((view.context as FragmentActivity).supportFragmentManager, "")
                sortKeywordList.onItemClick = { type ->
                    val prev = sort.text
                    if (prev != type)
                        actionStatusChange.invoke(adapterPosition)
                    if (sortKeywordList.getSelectedSortId() == TITLE_1) {
                        item.data.type = BROAD
                        sort.text = TITLE_1
                    } else {
                        item.data.type = SPECIFIC
                        sort.text = TITLE_2
                    }
                }

            }

        }

    }

}