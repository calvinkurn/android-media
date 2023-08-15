package com.tokopedia.topads.dashboard.recommendation.views.adapter.recommendation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.CONST_1
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.CONST_2
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.CONST_3
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.CONST_4
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.CONST_5
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.HEADLINE_KEY
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.PRODUCT_KEY
import com.tokopedia.topads.dashboard.recommendation.common.Utils
import com.tokopedia.topads.dashboard.recommendation.data.model.local.AdGroupUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.EmptyStateUiListModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.InsightListUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.LoadingUiModel
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifycomponents.PageControl
import com.tokopedia.unifycomponents.ProgressBarUnify
import com.tokopedia.unifyprinciples.Typography
import timber.log.Timber

class InsightListAdapter(private val onInsightItemClick: (list: ArrayList<AdGroupUiModel>, item: AdGroupUiModel) -> Unit) :
    ListAdapter<InsightListUiModel, RecyclerView.ViewHolder>(InsightListDiffUtilCallBack()) {

    inner class InsightListItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        private val groupCardTitle: Typography = view.findViewById(R.id.groupCardTitle)
        private val groupCardSubTitle: Typography = view.findViewById(R.id.groupCardSubTitle)
        private val groupCardCountWarning: IconUnify = view.findViewById(R.id.groupCardCountWarning)
        private val groupCardInsightCount: Typography =
            view.findViewById(R.id.groupCardInsightCount)
        private val groupCardProgressBar: ProgressBarUnify =
            view.findViewById(R.id.groupCardProgressBar)

        fun bind(
            item: AdGroupUiModel,
            onInsightItemClick: (list: ArrayList<AdGroupUiModel>, item: AdGroupUiModel) -> Unit
        ) {
            groupCardTitle.text = item.adGroupName
            groupCardSubTitle.showWithCondition(item.showGroupType)
            groupCardSubTitle.text = when (item.adGroupType) {
                PRODUCT_KEY -> view.context.getString(R.string.iklan_produk_subtitle)
                HEADLINE_KEY -> view.context.getString(R.string.iklan_toko_subtitle)
                else -> ""
            }

            groupCardInsightCount.show()
            groupCardInsightCount.text = HtmlCompat.fromHtml(
                String.format(
                    view.context.getString(R.string.topads_group_card_insight_count_format),
                    item.count
                ),
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )
            groupCardCountWarning.hide()
            setProgressBar(item.count, item.adGroupType)
            view.setOnClickListener {
                val list = ArrayList<AdGroupUiModel>()
                this@InsightListAdapter.currentList.map {
                    (it as? AdGroupUiModel)?.let { adGroupUiModel ->
                        list.add(adGroupUiModel)
                    }
                }
                onInsightItemClick(list, item)
            }
        }

        private fun setProgressBar(count: Int, adGroupType: String) {
            when (count) {
                CONST_5 -> {
                    groupCardProgressBar.setValue(Utils().toProgressPercent(CONST_5), false)
                    if (adGroupType == PRODUCT_KEY) groupCardCountWarning.show() else groupCardCountWarning.hide()
                }
                CONST_4 -> {
                    groupCardProgressBar.progressBarColor = intArrayOf(
                        ContextCompat.getColor(
                            view.context,
                            com.tokopedia.unifycomponents.R.color.Unify_YN500
                        ),
                        ContextCompat.getColor(
                            view.context,
                            com.tokopedia.unifycomponents.R.color.Unify_YN500
                        )

                    )
                    groupCardProgressBar.setValue(Utils().toProgressPercent(CONST_4), false)
                }
                CONST_3 -> {
                    groupCardProgressBar.progressBarColor = intArrayOf(
                        ContextCompat.getColor(
                            view.context,
                            com.tokopedia.unifycomponents.R.color.Unify_YN300
                        ),
                        ContextCompat.getColor(
                            view.context,
                            com.tokopedia.unifycomponents.R.color.Unify_YN300
                        )
                    )
                    groupCardProgressBar.setValue(Utils().toProgressPercent(CONST_3), false)
                }
                CONST_2 -> {
                    groupCardProgressBar.progressBarColor = intArrayOf(
                        ContextCompat.getColor(
                            view.context,
                            com.tokopedia.unifycomponents.R.color.Unify_GN200
                        ),
                        ContextCompat.getColor(
                            view.context,
                            com.tokopedia.unifycomponents.R.color.Unify_GN200
                        )
                    )
                    groupCardProgressBar.setValue(Utils().toProgressPercent(CONST_2), false)
                }
                CONST_1 -> {
                    if (adGroupType == HEADLINE_KEY) {
                        groupCardCountWarning.show()
                        groupCardProgressBar.setValue(Int.ZERO, false)
                    } else {
                        groupCardProgressBar.progressBarColor = intArrayOf(
                            ContextCompat.getColor(
                                view.context,
                                com.tokopedia.unifycomponents.R.color.Unify_GN200
                            ),
                            ContextCompat.getColor(
                                view.context,
                                com.tokopedia.unifycomponents.R.color.Unify_GN200
                            )
                        )
                        groupCardProgressBar.setValue(Utils().toProgressPercent(CONST_1), false)
                        groupCardCountWarning.hide()
                    }
                }
            }
        }
    }

    inner class LoadingItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val loader: LoaderUnify = view.findViewById(R.id.bottomLoader)
        fun bind(item: LoadingUiModel) {
            loader.showWithCondition(item.isLoading)
        }
    }

    inner class EmptyStateListViewHolder(view: View) :
        RecyclerView.ViewHolder(view) {

        private var emptyStateRecyclerView: RecyclerView = view.findViewById(R.id.emptyStateView)
        private var pageControlEmptyState: PageControl =
            view.findViewById(R.id.pageControlEmptyState)

        private val pagerAdapter by lazy {
            EmptyStatePagerAdapter()
        }

        private val layoutManager by lazy {
            return@lazy object : LinearLayoutManager(view.context, HORIZONTAL, false) {
                override fun canScrollVertically(): Boolean = false
            }
        }

        fun bind(item: EmptyStateUiListModel) {
            pagerAdapter.emptyStatePages = item.statesList
            emptyStateRecyclerView.layoutManager = layoutManager
            emptyStateRecyclerView.adapter = pagerAdapter
            if (item.statesList.size > Int.ONE) {
                pageControlEmptyState.setIndicator(item.statesList.size)
                pageControlEmptyState.show()
            } else {
                pageControlEmptyState.hide()
            }

            emptyStateRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val currentPosition = layoutManager.findFirstCompletelyVisibleItemPosition()
                    if (currentPosition != RecyclerView.NO_POSITION && item.statesList.size > 1) {
                        pageControlEmptyState.setCurrentIndicator(currentPosition)
                    }
                }
            })
            try {
                PagerSnapHelper().attachToRecyclerView(emptyStateRecyclerView)
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.topads_group_card_item_layout -> {
                val view =
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.topads_group_card_item_layout, parent, false)
                InsightListItemViewHolder(view)
            }
            R.layout.insight_bottom_loading_layout -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.insight_bottom_loading_layout, parent, false)
                LoadingItemViewHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.topads_insight_centre_empty_state_layout, parent, false)
                EmptyStateListViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is AdGroupUiModel -> {
                (holder as? InsightListItemViewHolder)?.bind(
                    item,
                    onInsightItemClick
                )
            }
            is LoadingUiModel -> {
                (holder as? LoadingItemViewHolder)?.bind(item)
            }
            is EmptyStateUiListModel -> {
                (holder as? EmptyStateListViewHolder)?.bind(item)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is AdGroupUiModel -> R.layout.topads_group_card_item_layout
            is LoadingUiModel -> R.layout.insight_bottom_loading_layout
            is EmptyStateUiListModel -> R.layout.topads_insight_centre_empty_state_layout
            else -> throw IllegalArgumentException("Invalid item type")
        }
    }
}

class InsightListDiffUtilCallBack : DiffUtil.ItemCallback<InsightListUiModel>() {
    override fun areItemsTheSame(
        oldItem: InsightListUiModel,
        newItem: InsightListUiModel
    ): Boolean {
        return oldItem.id() == newItem.id()
    }

    override fun areContentsTheSame(
        oldItem: InsightListUiModel,
        newItem: InsightListUiModel
    ): Boolean {
        return oldItem.equalsWith(newItem)
    }
}
