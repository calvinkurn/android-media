package com.tokopedia.topads.edit.view.adapter.edit_keyword

import android.view.View
import com.tokopedia.topads.edit.view.adapter.edit_keyword.viewholder.EditKeywordEmptyViewHolder
import com.tokopedia.topads.edit.view.adapter.edit_keyword.viewholder.EditKeywordItemViewHolder
import com.tokopedia.topads.edit.view.adapter.edit_keyword.viewholder.EditKeywordViewHolder
import com.tokopedia.topads.edit.view.adapter.edit_keyword.viewmodel.EditKeywordEmptyViewModel
import com.tokopedia.topads.edit.view.adapter.edit_keyword.viewmodel.EditKeywordItemViewModel

/**
 * Created by Pika on 9/4/20.
 */

class EditKeywordListAdapterTypeFactoryImpl(var actionDelete: ((pos: Int) -> Unit)?, var actionAdd: (() -> Unit)?, var actionClick: () -> MutableMap<String, Int>, var actionEnable: (() -> Unit)?,var actionEdit: ((pos: Int) -> Unit)?) : EditKeywordListAdapterTypeFactory {

    override fun type(model: EditKeywordEmptyViewModel): Int = EditKeywordEmptyViewHolder.LAYOUT

    override fun type(editKeywordItemViewHolder: EditKeywordItemViewModel): Int = EditKeywordItemViewHolder.LAYOUT

    override fun holder(type: Int, view: View): EditKeywordViewHolder<*> {
        return when (type) {
            EditKeywordItemViewHolder.LAYOUT -> EditKeywordItemViewHolder(view, actionDelete, actionClick, actionEnable,actionEdit)
            EditKeywordEmptyViewHolder.LAYOUT -> EditKeywordEmptyViewHolder(view, actionAdd)
            else -> throw RuntimeException("Illegal view type")
        }
    }

}