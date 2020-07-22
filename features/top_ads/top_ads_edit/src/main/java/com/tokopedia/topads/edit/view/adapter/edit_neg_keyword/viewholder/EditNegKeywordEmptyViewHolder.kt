package com.tokopedia.topads.edit.view.adapter.edit_neg_keyword.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.topads.edit.R
import com.tokopedia.topads.edit.view.adapter.edit_neg_keyword.viewmodel.EditNegKeywordEmptyViewModel
import kotlinx.android.synthetic.main.topads_edit_no_negative_keywords_layout.view.*

/**
 * Created by Pika on 12/4/20.
 */

class EditNegKeywordEmptyViewHolder(val view: View, var actionAdd: (() -> Unit?)) : EditNegKeywordViewHolder<EditNegKeywordEmptyViewModel>(view) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.topads_edit_no_negative_keywords_layout
    }

    override fun bind(item: EditNegKeywordEmptyViewModel) {
        view.add.setOnClickListener {
            actionAdd.invoke()
        }
    }

}