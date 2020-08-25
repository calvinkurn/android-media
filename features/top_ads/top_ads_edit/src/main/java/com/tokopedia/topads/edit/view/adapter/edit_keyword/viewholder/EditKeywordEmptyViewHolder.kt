package com.tokopedia.topads.edit.view.adapter.edit_keyword.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.topads.edit.R
import com.tokopedia.topads.edit.view.adapter.edit_keyword.viewmodel.EditKeywordEmptyViewModel
import kotlinx.android.synthetic.main.topads_edit_no_keywords_layout.view.*

/**
 * Created by Pika on 9/4/20.
 */

class EditKeywordEmptyViewHolder(val view: View, var actionAdd: (() -> Unit)?) : EditKeywordViewHolder<EditKeywordEmptyViewModel>(view) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.topads_edit_no_keywords_layout
    }

    override fun bind(item: EditKeywordEmptyViewModel) {
        view.add.setOnClickListener {
            actionAdd?.invoke()
        }
    }

}