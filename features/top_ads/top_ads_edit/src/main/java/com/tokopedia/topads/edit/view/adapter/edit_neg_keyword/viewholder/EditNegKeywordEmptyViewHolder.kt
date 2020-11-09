package com.tokopedia.topads.edit.view.adapter.edit_neg_keyword.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.content.res.AppCompatResources
import com.tokopedia.topads.edit.R
import com.tokopedia.topads.edit.view.adapter.edit_neg_keyword.viewmodel.EditNegKeywordEmptyViewModel
import kotlinx.android.synthetic.main.topads_edit_no_keywords_layout.view.*
import kotlinx.android.synthetic.main.topads_edit_no_negative_keywords_layout.view.*
import kotlinx.android.synthetic.main.topads_edit_no_negative_keywords_layout.view.add
import kotlinx.android.synthetic.main.topads_edit_no_negative_keywords_layout.view.add_image

/**
 * Created by Pika on 12/4/20.
 */

class EditNegKeywordEmptyViewHolder(val view: View, var actionAdd: (() -> Unit?)) : EditNegKeywordViewHolder<EditNegKeywordEmptyViewModel>(view) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.topads_edit_no_negative_keywords_layout
    }

    override fun bind(item: EditNegKeywordEmptyViewModel) {
        view.add_image.setImageDrawable(AppCompatResources.getDrawable(view.context, com.tokopedia.topads.common.R.drawable.topads_plus_add_keyword))
        view.add.setOnClickListener {
            actionAdd.invoke()
        }
    }

}