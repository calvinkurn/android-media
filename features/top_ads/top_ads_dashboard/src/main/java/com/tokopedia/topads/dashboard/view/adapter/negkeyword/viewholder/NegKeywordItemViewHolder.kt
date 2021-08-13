package com.tokopedia.topads.dashboard.view.adapter.negkeyword.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.view.adapter.negkeyword.viewmodel.NegKeywordItemModel
import com.tokopedia.unifycomponents.Label
import kotlinx.android.synthetic.main.topads_dash_item_neg_keyword_card.view.*


/**
 * Created by Pika on 7/6/20.
 */

class NegKeywordItemViewHolder(val view: View,
                               var onSelectMode: ((select: Boolean) -> Unit)) : NegKeywordViewHolder<NegKeywordItemModel>(view) {

    companion object {
        @LayoutRes
        var LAYOUT = R.layout.topads_dash_item_neg_keyword_card
    }

    override fun bind(item: NegKeywordItemModel, selectMode: Boolean, fromSearch: Boolean, fromHeadline: Boolean) {
        item.let {
            if (selectMode) {
                view.check_box.visibility = View.VISIBLE
            } else {
                view.card_view?.setBackgroundColor(ContextCompat.getColor(view.context, R.color.topads_dash_white))
                view.check_box.visibility = View.GONE
            }
            view.key_title.text = it.result.keywordTag
            view.label.text = it.result.keywordTypeDesc
            view.label.setLabelType(Label.GENERAL_LIGHT_GREEN)
            view.check_box.isChecked = item.isChecked
            if (!view.check_box.isChecked) {
                view.card_view.setBackgroundColor(ContextCompat.getColor(view.context, R.color.topads_dash_white))
            } else {
                view.card_view.setBackgroundColor(ContextCompat.getColor(view.context, R.color.topads_select_color))
            }
            view.item_card.setOnClickListener {
                if (selectMode) {
                    view.check_box.isChecked = !view.check_box.isChecked
                    item.isChecked = view.check_box.isChecked
                    if (view.check_box.isChecked)
                        view.card_view?.setBackgroundColor(ContextCompat.getColor(view.context, R.color.topads_select_color))
                    else
                        view.card_view?.setBackgroundColor(ContextCompat.getColor(view.context, R.color.topads_dash_white))
                }
            }
            view.item_card.setOnLongClickListener {
                item.isChecked = true
                view.check_box.isChecked = true
                view.card_view.setBackgroundColor(ContextCompat.getColor(view.context, R.color.topads_select_color))
                onSelectMode.invoke(true)
                true
            }
        }
    }
}