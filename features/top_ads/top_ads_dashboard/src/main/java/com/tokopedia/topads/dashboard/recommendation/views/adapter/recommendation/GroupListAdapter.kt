package com.tokopedia.topads.dashboard.recommendation.views.adapter.recommendation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.recommendation.data.model.local.GroupItemUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.GroupListUiModel

class GroupListAdapter(private val onItemCheckedChangeListener: (String) -> Unit) :
    ListAdapter<GroupListUiModel, RecyclerView.ViewHolder>(GroupListDiffUtilCallBack()) {

    inner class GroupListItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
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
                    view.context.getString(com.tokopedia.topads.common.R.string.topads_common_product_count_with_bold_value),
                    item.productCount
                ),
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )
            keywordCount.text = HtmlCompat.fromHtml(
                String.format(
                    view.context.getString(com.tokopedia.topads.common.R.string.topads_common_keyword_count_with_bold_value),
                    item.keywordCount
                ),
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )
        }

        private fun setSelected(item: GroupItemUiModel){
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.topads_insight_centre_group_list_item_layout -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.topads_insight_centre_group_list_item_layout, parent, false)
                GroupListItemViewHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.topads_insight_centre_group_list_item_layout, parent, false)
                GroupListItemViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is GroupItemUiModel -> {
                (holder as? GroupListItemViewHolder)?.bind(item, onItemCheckedChangeListener)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is GroupItemUiModel -> R.layout.topads_insight_centre_group_list_item_layout
            else -> throw IllegalArgumentException("Invalid item type")
        }
    }
}

class GroupListDiffUtilCallBack : DiffUtil.ItemCallback<GroupListUiModel>() {
    override fun areItemsTheSame(
        oldItem: GroupListUiModel,
        newItem: GroupListUiModel
    ): Boolean {
        return oldItem.id() == newItem.id()
    }

    override fun areContentsTheSame(
        oldItem: GroupListUiModel,
        newItem: GroupListUiModel
    ): Boolean {
        return oldItem.equalsWith(newItem)
    }

}
