package com.tokopedia.topads.edit.view.adapter.edit_neg_keyword.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.FragmentActivity
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.topads.edit.R
import com.tokopedia.topads.edit.view.adapter.edit_neg_keyword.viewmodel.EditNegKeywordItemViewModel
import com.tokopedia.topads.edit.view.sheet.EditKeywordSortSheet
import kotlinx.android.synthetic.main.topads_edit_negative_keyword_edit_item_layout.view.*

/**
 * Created by Pika on 12/4/20.
 */

class EditNegKeywordItemViewHolder(val view: View,
                                   private var actionDelete: ((pos: Int) -> Unit)?,
                                   private var actionStatusChange: ((pos: Int) -> Unit)) : EditNegKeywordViewHolder<EditNegKeywordItemViewModel>(view) {

    companion object {
        @LayoutRes
        var LAYOUT = R.layout.topads_edit_negative_keyword_edit_item_layout
        val TITLE_1 = "Luas"
        val TITLE_2 = "Spesifik"
        val BROAD = 11
        val SPECIFIC = 22
    }


    override fun bind(item: EditNegKeywordItemViewModel) {
        item.data.let {
            view.keyword_name.text = it.tag
            view.delete.setImageDrawable(getIconUnifyDrawable(view.context, IconUnify.DELETE))
            view.delete.setOnClickListener {
                actionDelete?.invoke(adapterPosition)
            }
            if (it.type == BROAD)
                view.sort.text = TITLE_1
            else
                view.sort.text = TITLE_2

            view.drop_down_arrow.setImageDrawable(view.context.getResDrawable(com.tokopedia.iconunify.R.drawable.iconunify_edit))

            view.drop_down_arrow.setOnClickListener {
                actionStatusChange.invoke(adapterPosition)
            }
        }
    }

}