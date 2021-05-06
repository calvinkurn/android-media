package com.tokopedia.topads.headline.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.topads.common.data.response.groupitem.DataItem
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.model.CountDataItem
import com.tokopedia.topads.headline.view.adapter.viewmodel.HeadLineAdItemsEmptyModel
import kotlinx.android.synthetic.main.topads_dash_group_empty_state.view.*

/**
 * Created by Pika on 16/10/20.
 */

class HeadLineAdItemsEmptyViewHolder(val view: View) : HeadLineAdItemsViewHolder<HeadLineAdItemsEmptyModel>(view) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.topads_dash_group_empty_state
    }

    override fun bind(item: HeadLineAdItemsEmptyModel, selectedMode: Boolean, fromSearch: Boolean, statsData: MutableList<DataItem>, countList: MutableList<CountDataItem>, selectedText: String) {

        if (fromSearch || selectedText.isEmpty()) {
            view.text_title.text = view.context.getString(R.string.topads_headlin_Search_empty_title)
            view.text_desc.text = view.context.getString(R.string.topads_empty_on_search_desc)
            view.btn_submit.visibility = View.GONE
            view.text_desc.visibility = View.VISIBLE
            view.image_empty.setImageDrawable(view.context.getResDrawable(com.tokopedia.topads.common.R.drawable.ill_no_product))
        } else {
            view.image_empty.setImageDrawable(view.context.getResDrawable(R.drawable.topads_empty_headline))
            view.text_title.text = String.format(view.context.getString(R.string.topads_dash_headline_empty_title), selectedText)
            view.text_desc.visibility = View.GONE
            view.btn_submit.visibility = View.GONE
        }
    }
}