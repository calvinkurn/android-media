package com.tokopedia.topads.edit.view.adapter.edit_neg_keyword.viewholder

import android.view.View
import android.widget.LinearLayout
import androidx.annotation.LayoutRes
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.topads.edit.R
import com.tokopedia.topads.edit.view.adapter.edit_neg_keyword.viewmodel.EditNegKeywordEmptyViewModel
import com.tokopedia.topads.edit.view.customview.TipsRowItem
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

/**
 * Created by Pika on 12/4/20.
 */

class EditNegKeywordEmptyViewHolder(val view: View, var actionAdd: (() -> Unit?)) : EditNegKeywordViewHolder<EditNegKeywordEmptyViewModel>(view) {

    var imageView = view.findViewById<ImageUnify>(com.tokopedia.topads.common.R.id.image_empty)
    var titleText = view.findViewById<Typography>(com.tokopedia.topads.common.R.id.text_title)
    var descText = view.findViewById<Typography>(com.tokopedia.topads.common.R.id.text_desc)
    var addBtn = view.findViewById<UnifyButton>(com.tokopedia.topads.common.R.id.addBtn)
    var tipsLayout = view.findViewById<LinearLayout>(com.tokopedia.topads.common.R.id.tipsLayout)

    companion object {
        @LayoutRes
        var LAYOUT = com.tokopedia.topads.common.R.layout.topads_dash_insight_empty_layout
    }

    override fun bind(item: EditNegKeywordEmptyViewModel) {
        imageView.setImageDrawable(view.context.getResDrawable(com.tokopedia.topads.common.R.drawable.ic_empty_keyword))
        titleText.text = view.context.getString(R.string.topads_edit_no_neg_keyword_title)
        descText.text = view.context.getString(R.string.topads_edit_no_neg_keyword_desc_new)
        tipsLayout.visibility = View.VISIBLE

        var view1 = TipsRowItem(view.context)
        var view2 = TipsRowItem(view.context)
        var view3 = TipsRowItem(view.context)
        var view4 = TipsRowItem(view.context)
        var view5 = TipsRowItem(view.context)

        tipsLayout.addView(view1)
        tipsLayout.addView(view2)
        tipsLayout.addView(view3)
        tipsLayout.addView(view4)
        tipsLayout.addView(view5)

        view1.setData(view.context.getString(R.string.top_ads_edit_negatif_tip_1))
        view2.setData(view.context.getString(R.string.top_ads_edit_negatif_tip_2))
        view3.setData(view.context.getString(R.string.top_ads_edit_negatif_tip_3))
        view4.setData(view.context.getString(R.string.top_ads_edit_negatif_tip_4))
        view5.setData(view.context.getString(R.string.top_ads_edit_negatif_tip_5))

        addBtn.visibility = View.VISIBLE
        addBtn.text = view.context.getString(com.tokopedia.topads.common.R.string.add_keyword_negatif)
        addBtn.setOnClickListener {
            actionAdd.invoke()
        }
    }

}