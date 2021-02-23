package com.tokopedia.topads.dashboard.view.adapter.negkeyword.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.view.adapter.negkeyword.viewmodel.NegKeywordEmptyModel
import kotlinx.android.synthetic.main.topads_dash_group_empty_state.view.*

/**
 * Created by Pika on 7/6/20.
 */
class NegKeywordEmptyViewHolder(val view: View, private val addKeywords: (() -> Unit)) : NegKeywordViewHolder<NegKeywordEmptyModel>(view) {

    companion object {
        @LayoutRes
        var LAYOUT = R.layout.topads_dash_group_empty_state
    }

    override fun bind(item: NegKeywordEmptyModel, selectMode: Boolean, fromSearch: Boolean, fromHeadline: Boolean) {
        item.let {
            view.image_empty.setImageDrawable(view.context.getResDrawable(R.drawable.topads_empty_keyword))

            if (!fromSearch) {
                view.btn_submit.visibility = View.VISIBLE
                view.text_title.text = view.context.getString(R.string.topads_dash_empty_neg_keyword_title)
                view.text_desc.text = view.context.getString(R.string.topads_dash_empty_neg_keyword_desc)
                view.btn_submit.text = view.context.getString(R.string.topads_dash_add_negative_keyword)
                view.btn_submit.isEnabled = !fromHeadline
            } else {
                view.text_title.text = view.context.getString(R.string.topads_empty_on_neg_keywords_title)
                view.text_desc.text = view.context.getString(R.string.topads_empty_on_search_desc)
                view.btn_submit.visibility = View.GONE
            }

            view.btn_submit.setOnClickListener {
                addKeywords.invoke()
            }
        }
    }
}