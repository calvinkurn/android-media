package com.tokopedia.topads.headline.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.topads.common.data.response.groupitem.DataItem
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.common.data.model.CountDataItem
import com.tokopedia.topads.headline.view.adapter.viewmodel.HeadLineAdItemsEmptyModel
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

/**
 * Created by Pika on 16/10/20.
 */

class HeadLineAdItemsEmptyViewHolder(val view: View) : HeadLineAdItemsViewHolder<HeadLineAdItemsEmptyModel>(view) {

    private val imageEmpty : ImageUnify = view.findViewById(R.id.image_empty)
    private val textTitle : Typography = view.findViewById(R.id.text_title)
    private val textDesc : Typography = view.findViewById(R.id.text_desc)
    private val btnSubmit : UnifyButton = view.findViewById(R.id.btn_submit)

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.topads_dash_group_empty_state
    }

    override fun bind(item: HeadLineAdItemsEmptyModel, selectedMode: Boolean, fromSearch: Boolean, statsData: MutableList<DataItem>, countList: MutableList<CountDataItem>, selectedText: String) {

        if (fromSearch || selectedText.isEmpty()) {
            textTitle.text = view.context.getString(R.string.topads_headlin_Search_empty_title)
            textDesc.text = view.context.getString(R.string.topads_empty_on_search_desc)
            btnSubmit.visibility = View.GONE
            textDesc.visibility = View.VISIBLE
            imageEmpty.setImageDrawable(view.context.getResDrawable(com.tokopedia.topads.common.R.drawable.no_products))
        } else {
            imageEmpty.setImageDrawable(view.context.getResDrawable(R.drawable.topads_empty_headline))
            textTitle.text = String.format(view.context.getString(R.string.topads_dash_headline_empty_title), selectedText)
            textDesc.visibility = View.GONE
            btnSubmit.visibility = View.GONE
        }
    }
}
