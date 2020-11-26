package com.tokopedia.topads.edit.view.adapter.edit_neg_keyword

import android.view.View
import com.tokopedia.topads.edit.view.adapter.edit_keyword.viewholder.EditKeywordViewHolder
import com.tokopedia.topads.edit.view.adapter.edit_keyword.viewmodel.EditKeywordEmptyViewModel
import com.tokopedia.topads.edit.view.adapter.edit_keyword.viewmodel.EditKeywordItemViewModel
import com.tokopedia.topads.edit.view.adapter.edit_neg_keyword.viewholder.EditNegKeywordViewHolder
import com.tokopedia.topads.edit.view.adapter.edit_neg_keyword.viewmodel.EditNegKeywordEmptyViewModel
import com.tokopedia.topads.edit.view.adapter.edit_neg_keyword.viewmodel.EditNegKeywordItemViewModel

/**
 * Created by Pika on 12/4/20.
 */

interface EditNegKeywordListAdapterTypeFactory {

    fun type(model: EditNegKeywordEmptyViewModel): Int

    fun type(model: EditNegKeywordItemViewModel): Int

    fun holder(type: Int, view: View): EditNegKeywordViewHolder<*>

}