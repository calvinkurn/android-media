package com.tokopedia.topads.edit.view.adapter.edit_keyword.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.content.res.AppCompatResources
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

    override fun bind(item: EditKeywordEmptyViewModel, added: MutableList<Boolean>, minBid: String) {
        view.add_image.setImageDrawable(AppCompatResources.getDrawable(view.context, com.tokopedia.topads.common.R.drawable.topads_plus_add_keyword))
        view.add.setOnClickListener {
            actionAdd?.invoke()
        }
    }

}