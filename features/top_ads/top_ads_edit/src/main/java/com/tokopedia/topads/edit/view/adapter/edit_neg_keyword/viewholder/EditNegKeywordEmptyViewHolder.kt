package com.tokopedia.topads.edit.view.adapter.edit_neg_keyword.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.topads.edit.R
import com.tokopedia.topads.edit.view.adapter.edit_neg_keyword.viewmodel.EditNegKeywordEmptyViewModel
import com.tokopedia.topads.edit.view.customview.TipsRowItem
import kotlinx.android.synthetic.main.topads_edit_no_keywords_layout.view.*
import kotlinx.android.synthetic.main.topads_edit_no_negative_keywords_layout.view.*

/**
 * Created by Pika on 12/4/20.
 */

class EditNegKeywordEmptyViewHolder(val view: View, var actionAdd: (() -> Unit?)) : EditNegKeywordViewHolder<EditNegKeywordEmptyViewModel>(view) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.topads_edit_no_negative_keywords_layout
    }

    override fun bind(item: EditNegKeywordEmptyViewModel) {
        view.image_empty.setImageDrawable(view.context.getResDrawable(com.tokopedia.topads.common.R.drawable.ic_empty_keyword))
        view.text_title.text = view.context.getString(R.string.topads_edit_no_neg_keyword_title)
        view.text_desc.text = view.context.getString(R.string.topads_edit_no_neg_keyword_desc_new)

        var view1 = TipsRowItem(view.context)
        var view2 = TipsRowItem(view.context)
        var view3 = TipsRowItem(view.context)
        var view4 = TipsRowItem(view.context)
        var view5 = TipsRowItem(view.context)

        view.tipsLayout.addView(view1)
        view.tipsLayout.addView(view2)
        view.tipsLayout.addView(view3)
        view.tipsLayout.addView(view4)
        view.tipsLayout.addView(view5)

        view1.setData(view.context.getString(R.string.top_ads_edit_negatif_tip_1))
        view2.setData(view.context.getString(R.string.top_ads_edit_negatif_tip_2))
        view3.setData(view.context.getString(R.string.top_ads_edit_negatif_tip_3))
        view4.setData(view.context.getString(R.string.top_ads_edit_negatif_tip_4))
        view5.setData(view.context.getString(R.string.top_ads_edit_negatif_tip_5))

        view.addBtn.visibility = View.VISIBLE
        view.addBtn.text = view.context.getString(com.tokopedia.topads.common.R.string.add_keyword_negatif)
        view.addBtn.setOnClickListener {
            actionAdd.invoke()
        }
    }

}