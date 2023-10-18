package com.tokopedia.topads.dashboard.view.adapter.negkeyword.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.R.color
import com.tokopedia.unifyprinciples.R.color as colorUnify
import com.tokopedia.topads.dashboard.view.adapter.negkeyword.viewmodel.NegKeywordItemModel
import com.tokopedia.unifycomponents.CardUnify
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify
import com.tokopedia.unifyprinciples.Typography

/**
 * Created by Pika on 7/6/20.
 */

class NegKeywordItemViewHolder(
    val view: View,
    var onSelectMode: ((select: Boolean) -> Unit),
) : NegKeywordViewHolder<NegKeywordItemModel>(view) {

    private val cardView: CardUnify = view.findViewById(R.id.card_view)
    private val itemCard: ConstraintLayout = view.findViewById(R.id.item_card)
    private val keyTitle: Typography = view.findViewById(R.id.key_title)
    private val checkBox: CheckboxUnify = view.findViewById(R.id.check_box)
    private val label: Label = view.findViewById(R.id.label)

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.topads_dash_item_neg_keyword_card
    }

    override fun bind(
        item: NegKeywordItemModel, selectMode: Boolean, fromSearch: Boolean, fromHeadline: Boolean,
    ) {
        item.let {
            if (selectMode) {
                checkBox.visibility = View.VISIBLE
            } else {
                cardView.setBackgroundColor(ContextCompat.getColor(
                    view.context, colorUnify.Unify_NN0))
                checkBox.visibility = View.GONE
            }
            keyTitle.text = it.result.keywordTag
            label.text = it.result.keywordTypeDesc
            label.setLabelType(Label.GENERAL_LIGHT_GREEN)
            checkBox.isChecked = item.isChecked
            if (!checkBox.isChecked) {
                cardView.setBackgroundColor(ContextCompat.getColor(view.context,
                    colorUnify.Unify_NN0))
            } else {
                cardView.setBackgroundColor(ContextCompat.getColor(view.context,
                    colorUnify.Unify_B400_20))
            }
            itemCard.setOnClickListener {
                if (selectMode) {
                    checkBox.isChecked = !checkBox.isChecked
                    item.isChecked = checkBox.isChecked
                    if (checkBox.isChecked)
                        cardView.setBackgroundColor(ContextCompat.getColor(view.context,
                            colorUnify.Unify_B400_20))
                    else
                        cardView.setBackgroundColor(ContextCompat.getColor(view.context,
                            colorUnify.Unify_NN0))
                }
            }
            itemCard.setOnLongClickListener {
                item.isChecked = true
                checkBox.isChecked = true
                cardView.setBackgroundColor(ContextCompat.getColor(
                    view.context, colorUnify.Unify_B400_20))
                onSelectMode.invoke(true)
                true
            }
        }
    }
}
