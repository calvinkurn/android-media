package com.tokopedia.topads.edit.view.adapter.keyword.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.topads.edit.R
import com.tokopedia.topads.edit.utils.Constants
import com.tokopedia.topads.edit.view.adapter.keyword.viewmodel.KeywordItemViewModel
import kotlinx.android.synthetic.main.topads_edit_select_layout_keyword_list_item.view.*
import kotlinx.android.synthetic.main.topads_edit_select_layout_keyword_list_item.view.checkBox
import java.lang.Exception

class KeywordItemViewHolder(val view: View, private var actionSelected: ((pos: Int) -> Unit)?) : KeywordViewHolder<KeywordItemViewModel>(view) {

    companion object {
        @LayoutRes
        var LAYOUT = R.layout.topads_edit_select_layout_keyword_list_item
    }

    init {
        view.setOnClickListener {
            it.checkBox.isChecked = !it.checkBox.isChecked
            actionSelected?.invoke(adapterPosition)
        }
    }

    override fun bind(item: KeywordItemViewModel) {
        item.data.let {
            view.keyword_name.text = it.keyword
            view.checkBox.setOnCheckedChangeListener(null)
            view.checkBox.isChecked = item.isChecked
            try {
                view.keyword_count.text = Constants.convertToCurrencyString(it.totalSearch.toLong())
            } catch (e: Exception) {
                view.keyword_count.text = it.totalSearch
            }
            view.checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
                item.isChecked = isChecked
                actionSelected?.invoke(adapterPosition)
            }
        }
    }

}