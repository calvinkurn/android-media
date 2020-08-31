package com.tokopedia.topads.edit.view.adapter.edit_neg_keyword

import android.view.View
import com.tokopedia.topads.edit.view.adapter.edit_neg_keyword.viewholder.EditNegKeywordEmptyViewHolder
import com.tokopedia.topads.edit.view.adapter.edit_neg_keyword.viewholder.EditNegKeywordItemViewHolder
import com.tokopedia.topads.edit.view.adapter.edit_neg_keyword.viewholder.EditNegKeywordViewHolder
import com.tokopedia.topads.edit.view.adapter.edit_neg_keyword.viewmodel.EditNegKeywordEmptyViewModel
import com.tokopedia.topads.edit.view.adapter.edit_neg_keyword.viewmodel.EditNegKeywordItemViewModel

/**
 * Created by Pika on 12/4/20.
 */

class EditNegKeywordListAdapterTypeFactoryImpl(var actionDelete: ((pos: Int) -> Unit)?,
                                               var actionAdd: (() -> Unit?),
                                               var actionStatusChange: ((pos: Int) -> Unit)) : EditNegKeywordListAdapterTypeFactory {

    override fun type(model: EditNegKeywordEmptyViewModel): Int = EditNegKeywordEmptyViewHolder.LAYOUT

    override fun type(model: EditNegKeywordItemViewModel): Int = EditNegKeywordItemViewHolder.LAYOUT

    override fun holder(type: Int, view: View): EditNegKeywordViewHolder<*> {
        return when (type) {
            EditNegKeywordItemViewHolder.LAYOUT -> EditNegKeywordItemViewHolder(view, actionDelete, actionStatusChange)
            EditNegKeywordEmptyViewHolder.LAYOUT -> EditNegKeywordEmptyViewHolder(view, actionAdd)
            else -> throw RuntimeException("Illegal view type")
        }
    }

}