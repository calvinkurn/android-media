package com.tokopedia.topads.view.adapter.keyword.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.topads.create.R
import com.tokopedia.topads.view.adapter.keyword.viewmodel.KeywordGroupViewModel
import kotlinx.android.synthetic.main.topads_create_layout_keyword_group_item.view.*

/**
 * Author errysuprayogi on 11,November,2019
 */
class KeywordGroupViewHolder(val view: View): KeywordViewHolder<KeywordGroupViewModel>(view) {

    companion object {
        @LayoutRes
        var LAYOUT = R.layout.topads_create_layout_keyword_group_item
    }

    override fun bind(item: KeywordGroupViewModel) {
        item.let {
            view.group_title.text = it.title
        }
    }

}