package com.tokopedia.topads.dashboard.view.adapter.negkeyword.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.view.adapter.negkeyword.viewmodel.NegKeywordEmptyModel
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

/**
 * Created by Pika on 7/6/20.
 */
class NegKeywordEmptyViewHolder(val view: View, private val addKeywords: (() -> Unit)) :
    NegKeywordViewHolder<NegKeywordEmptyModel>(view) {

    private val imageEmpty: ImageUnify = view.findViewById(R.id.image_empty)
    private val textTitle: Typography = view.findViewById(R.id.text_title)
    private val textDesc: Typography = view.findViewById(R.id.text_desc)
    private val btnSubmit: UnifyButton = view.findViewById(R.id.btn_submit)

    companion object {
        @LayoutRes
        var LAYOUT = R.layout.topads_dash_group_empty_state
    }

    override fun bind(
        item: NegKeywordEmptyModel, selectMode: Boolean, fromSearch: Boolean, fromHeadline: Boolean,
    ) {
        item.let {
            imageEmpty.setImageDrawable(view.context.getResDrawable(com.tokopedia.topads.common.R.drawable.ic_empty_keyword))

            if (!fromSearch) {
                btnSubmit.visibility = View.VISIBLE
                textTitle.text =
                    view.context.getString(R.string.topads_dash_empty_neg_keyword_title)
                textDesc.text = view.context.getString(R.string.topads_dash_empty_neg_keyword_desc)
                btnSubmit.text = view.context.getString(R.string.topads_dash_add_negative_keyword)
                btnSubmit.isEnabled = !fromHeadline
            } else {
                textTitle.text = view.context.getString(R.string.topads_empty_on_neg_keywords_title)
                textDesc.text = view.context.getString(R.string.topads_empty_on_search_desc)
                btnSubmit.visibility = View.GONE
            }

            btnSubmit.setOnClickListener {
                addKeywords.invoke()
            }
        }
    }
}