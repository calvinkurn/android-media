package com.tokopedia.topads.dashboard.recommendation.views.adapter.viewholders

import android.content.res.ColorStateList
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.common.R as topadscommonR
import com.tokopedia.unifyprinciples.R as unifyprinciplesR
import com.tokopedia.topads.dashboard.recommendation.data.model.local.GroupItemUiModel

class GroupListItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
    private val itemCard: com.tokopedia.unifycomponents.CardUnify =
        view.findViewById(R.id.groupListItemCard)
    private val title: com.tokopedia.unifyprinciples.Typography =
        view.findViewById(R.id.groupName)
    private val productCount: com.tokopedia.unifyprinciples.Typography =
        view.findViewById(R.id.productCount)
    private val keywordCount: com.tokopedia.unifyprinciples.Typography =
        view.findViewById(R.id.keywordCount)
    private val selectGroupCta: com.tokopedia.unifycomponents.selectioncontrol.RadioButtonUnify =
        view.findViewById(R.id.selectGroupCta)

    fun bind(item: GroupItemUiModel, onItemCheckedChangeListener: (String) -> Unit) {
        selectGroupCta.setOnCheckedChangeListener(null)
        bindValues(item)
        setSelected(item)
        setOnCheckedChangeListener(item, onItemCheckedChangeListener)
    }

    private fun bindValues(item: GroupItemUiModel) {
        title.text = item.groupName
        productCount.text = HtmlCompat.fromHtml(
            String.format(
                view.context.getString(topadscommonR.string.topads_common_product_count_with_bold_value),
                item.productCount
            ),
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
        keywordCount.text = HtmlCompat.fromHtml(
            String.format(
                view.context.getString(topadscommonR.string.topads_common_keyword_count_with_bold_value),
                item.keywordCount
            ),
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
    }

    private fun setSelected(item: GroupItemUiModel) {
        if (item.isSelected) {
            itemCard.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(view.context, unifyprinciplesR.color.Unify_GN500))
        } else
            itemCard.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(view.context, unifyprinciplesR.color.Unify_NN300))

        selectGroupCta.isChecked = item.isSelected
    }

    private fun setOnCheckedChangeListener(
        item: GroupItemUiModel,
        onItemCheckedChangeListener: (String) -> Unit
    ) {
        selectGroupCta.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked)
                onItemCheckedChangeListener.invoke(item.groupId)
        }
    }
}
