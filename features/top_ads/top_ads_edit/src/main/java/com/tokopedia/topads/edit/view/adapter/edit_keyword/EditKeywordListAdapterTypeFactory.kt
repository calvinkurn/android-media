package com.tokopedia.topads.edit.view.adapter.edit_keyword

import android.view.View
import com.tokopedia.topads.edit.view.adapter.edit_keyword.viewholder.EditKeywordViewHolder
import com.tokopedia.topads.edit.view.adapter.edit_keyword.viewmodel.EditKeywordEmptyViewModel
import com.tokopedia.topads.edit.view.adapter.edit_keyword.viewmodel.EditKeywordItemViewModel

/**
 * Created by Pika on 9/4/20.
 */

interface EditKeywordListAdapterTypeFactory {

    fun type(model: EditKeywordEmptyViewModel): Int

    fun type(editKeywordItemViewHolder: EditKeywordItemViewModel): Int

    fun holder(type: Int, view: View): EditKeywordViewHolder<*>

}