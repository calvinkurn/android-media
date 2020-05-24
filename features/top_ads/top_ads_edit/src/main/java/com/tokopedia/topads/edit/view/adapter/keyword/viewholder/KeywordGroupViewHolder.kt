package com.tokopedia.topads.edit.view.adapter.keyword.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.topads.edit.R
import com.tokopedia.topads.edit.view.adapter.keyword.viewmodel.KeywordGroupViewModel
import kotlinx.android.synthetic.main.topads_edit_select_layout_keyword_group_item.view.*

class KeywordGroupViewHolder(val view: View) : KeywordViewHolder<KeywordGroupViewModel>(view) {

    companion object {
        @LayoutRes
        var LAYOUT = R.layout.topads_edit_select_layout_keyword_group_item
    }

    override fun bind(item: KeywordGroupViewModel) {
        item.let {
            view.group_title.text = it.title
        }
    }

}