package com.tokopedia.topads.dashboard.view.adapter.keyword.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.view.adapter.keyword.viewmodel.KeywordEmptyModel
import kotlinx.android.synthetic.main.topads_dash_group_empty_state.view.*

/**
 * Created by Pika on 7/6/20.
 */
class KeywordEmptyViewHolder(val view: View, private val addKeywords: (() -> Unit)) : KeywordViewHolder<KeywordEmptyModel>(view) {

    companion object {
        @LayoutRes
        var LAYOUT = R.layout.topads_dash_group_empty_state
    }

    override fun bind(item: KeywordEmptyModel, selectMode: Boolean, fromSearch: Boolean) {
        item.let {
            view.image_empty.setImageDrawable(view.context.getResDrawable(com.tokopedia.topads.common.R.drawable.ic_empty_keyword))

            if (!fromSearch) {
                view.btn_submit.visibility = View.VISIBLE
                view.text_title.text = view.context.getString(R.string.topads_dash_empty_keyword_title)
                view.text_desc.text = view.context.getString(R.string.topads_dash_empty_keyword_desc)
                view.btn_submit.text = view.context.getString(com.tokopedia.topads.common.R.string.add_keyword)

            } else {
                view.text_title.text = view.context.getString(R.string.topads_no_search_keyword_title)
                view.text_desc.text = view.context.getString(R.string.topads_empty_on_search_desc)
                view.btn_submit.visibility = View.GONE
            }
            view.btn_submit.setOnClickListener {
                addKeywords.invoke()
            }
        }
    }

}