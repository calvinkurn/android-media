package com.tokopedia.topads.edit.view.adapter.edit_keyword.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.topads.common.R
import com.tokopedia.topads.edit.view.adapter.edit_keyword.viewmodel.EditKeywordEmptyViewModel
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

/**
 * Created by Pika on 9/4/20.
 */

class EditKeywordEmptyViewHolder(val view: View, var actionAdd: (() -> Unit)?) : EditKeywordViewHolder<EditKeywordEmptyViewModel>(view) {

    var imageView = view.findViewById<ImageUnify>(R.id.image_empty)
    var titleText = view.findViewById<Typography>(R.id.text_title)
    var descText = view.findViewById<Typography>(R.id.text_desc)
    var addBtn = view.findViewById<UnifyButton>(R.id.addBtn)

    companion object {
        @LayoutRes
        var LAYOUT = R.layout.topads_dash_insight_empty_layout
    }

    override fun bind(item: EditKeywordEmptyViewModel, added: MutableList<Boolean>, minBid: String) {
        imageView.setImageDrawable(view.context.getResDrawable(R.drawable.ic_empty_keyword))
        titleText.text = view.context.getString(R.string.topads_empty_insight_title)
        descText.text = view.context.getString(R.string.topads_empty_insight_desc)
        addBtn.visibility = View.VISIBLE
        addBtn.text = view.context.getString(R.string.add_keyword_positif)
        addBtn.setOnClickListener {
            actionAdd?.invoke()
        }
    }

}