package com.tokopedia.topads.view.adapter.keyword.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.topads.Utils
import com.tokopedia.topads.create.R
import com.tokopedia.topads.view.adapter.keyword.viewmodel.KeywordItemViewModel
import kotlinx.android.synthetic.main.topads_create_layout_keyword_list_item.view.*
import kotlinx.android.synthetic.main.topads_create_layout_keyword_list_item.view.checkBox

/**
 * Author errysuprayogi on 11,November,2019
 */
class KeywordItemViewHolder(val view: View, var actionSelected: (() -> Unit)?): KeywordViewHolder<KeywordItemViewModel>(view) {

    companion object {
        @LayoutRes
        var LAYOUT = R.layout.topads_create_layout_keyword_list_item
    }

    init {
        view?.setOnClickListener {
            it.checkBox.isChecked = !it.checkBox.isChecked
            actionSelected?.invoke()
        }
    }

    override fun bind(item: KeywordItemViewModel) {
        item.data?.let {
            view.keyword_name.setText(it.keyword)
            view.keyword_count.setText(Utils.format(it.totalSearch.toLong()))
            view.checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
                item.isChecked = isChecked
            }
        }
    }

}