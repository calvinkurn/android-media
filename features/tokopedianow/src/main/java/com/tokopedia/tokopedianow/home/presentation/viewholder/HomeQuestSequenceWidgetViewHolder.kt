package com.tokopedia.tokopedianow.home.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.constant.ConstantUrl.QUEST_DETAIL_PRODUCTION_URL
import com.tokopedia.tokopedianow.common.constant.ConstantUrl.QUEST_DETAIL_STAGING_URL
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowQuestSequenceWidgetBinding
import com.tokopedia.tokopedianow.databinding.PartialTokopedianowViewStubDcTitleBinding
import com.tokopedia.tokopedianow.home.constant.HomeLayoutItemState
import com.tokopedia.tokopedianow.home.presentation.adapter.HomeAdapter
import com.tokopedia.tokopedianow.home.presentation.adapter.HomeAdapterTypeFactory
import com.tokopedia.tokopedianow.home.presentation.adapter.differ.HomeListDiffer
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeQuestAllClaimedWidgetUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeQuestSequenceWidgetUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeQuestTitleUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeQuestWidgetUiModel
import com.tokopedia.tokopedianow.home.presentation.viewholder.HomeQuestWidgetViewHolder.Companion.STATUS_CLAIMED
import com.tokopedia.url.Env
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.utils.view.binding.viewBinding

class HomeQuestSequenceWidgetViewHolder(
    itemView: View,
    private val listener: HomeQuestSequenceWidgetListener? = null
): AbstractViewHolder<HomeQuestSequenceWidgetUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_quest_sequence_widget
    }

    private var binding: ItemTokopedianowQuestSequenceWidgetBinding? by viewBinding()
    private var stubBinding: PartialTokopedianowViewStubDcTitleBinding? by viewBinding()

    private val adapter by lazy {
        HomeAdapter(
            typeFactory = HomeAdapterTypeFactory(homeQuestSequenceWidgetListener = listener),
            differ = HomeListDiffer(),
        )
    }

    init {
        binding?.vsTitle?.apply {
            setOnInflateListener { _, inflated ->
                stubBinding = PartialTokopedianowViewStubDcTitleBinding.bind(inflated)
            }
            inflate()
        }
    }

    override fun bind(element: HomeQuestSequenceWidgetUiModel) {
        setupUi(element)
    }

    private fun setupUi(element: HomeQuestSequenceWidgetUiModel) {
        setAdapter()

        when(element.state) {
            HomeLayoutItemState.LOADED -> questLoaded(element)
            HomeLayoutItemState.ERROR -> questError(element)
            else -> { /* do nothing */ }
        }
    }

    private fun showHeader(element: HomeQuestSequenceWidgetUiModel) {
        setTitle(getString(R.string.tokopedianow_quest_sequence_widget_title))

        if (isQuestFinished(element)) {
            setSeeAll(getString(R.string.tokopedianow_quest_sequence_widget_see_all))
        } else {
            val url = if (TokopediaUrl.getInstance().TYPE == Env.STAGING) {
                QUEST_DETAIL_STAGING_URL
            } else {
                QUEST_DETAIL_PRODUCTION_URL
            }
            setSeeAll(getString(R.string.tokopedianow_quest_sequence_widget_see_all), url)
        }
    }

    private fun setImpressionListener(element: HomeQuestSequenceWidgetUiModel) {
        itemView.addOnImpressionListener(element) {
            if (isQuestFinished(element)) {
                listener?.onFinishedQuestImpression()
            } else {
                listener?.onQuestWidgetImpression()
            }
        }
    }

    private fun questLoaded(element: HomeQuestSequenceWidgetUiModel) {
        showHeader(element)
        addWidgets(element)
        setImpressionListener(element)
    }

    private fun questError(element: HomeQuestSequenceWidgetUiModel) {
        showHeader(element)
        addErrorWidgets()
    }

    private fun setAdapter() {
        binding?.rvQuest?.apply {
            adapter = this@HomeQuestSequenceWidgetViewHolder.adapter
            layoutManager = LinearLayoutManager(itemView.context,  LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun addWidgets(element: HomeQuestSequenceWidgetUiModel) {
        val widgets = mutableListOf<Visitable<*>>()
        val currentQuestFinishedSize = element.questList.filter {
            it.currentProgress == it.totalProgress
        }.size
        val totalQuestTargetSize = element.questList.size

        if (isQuestFinished(element)) {
            widgets.add(
                HomeQuestAllClaimedWidgetUiModel(
                    id = element.id,
                    currentQuestFinished = currentQuestFinishedSize,
                    totalQuestTarget = totalQuestTargetSize
                )
            )
        } else {
            widgets.add(
                HomeQuestTitleUiModel(
                    currentQuestFinished = currentQuestFinishedSize,
                    totalQuestTarget = totalQuestTargetSize
                )
            )
            widgets.addAll(element.questList.filter { it.status !=  STATUS_CLAIMED})
        }
        adapter.submitList(widgets)
    }

    private fun addErrorWidgets() {
        val widgets = mutableListOf(
            HomeQuestTitleUiModel(isErrorState = true),
            HomeQuestWidgetUiModel(isErrorState = true)
        )
        adapter.submitList(widgets)
    }

    private fun setTitle(title: String) {
        stubBinding?.channelTitle?.apply {
            if (title.isNotBlank()) {
                show()
                text = title
            } else {
                hide()
            }
        }
    }

    private fun setSeeAll(seeAll: String, linkUrl: String = "") {
        binding?.tvSeeAll?.apply {
            if (seeAll.isNotBlank() && linkUrl.isNotBlank()) {
                show()
                text = seeAll
                setOnClickListener {
                    listener?.onClickSeeDetails()
                    RouteManager.route(context, linkUrl)
                }
            } else {
                gone()
            }
        }
    }

    private fun isQuestFinished(element: HomeQuestSequenceWidgetUiModel): Boolean {
        val currentQuestFinished = element.questList.filter { it.currentProgress == it.totalProgress }.size
        val totalQuestTarget = element.questList.size
        return currentQuestFinished == totalQuestTarget
    }

    interface HomeQuestSequenceWidgetListener {
        fun onClickRefreshQuestWidget()
        fun onCloseQuestAllClaimedBtnClicked(id: String)
        fun onQuestWidgetImpression()
        fun onFinishedQuestImpression()
        fun onClickSeeDetails()
        fun onClickQuestWidgetCard()
        fun onClickQuestWidgetTitle()
        fun onClickCheckReward()
    }
}