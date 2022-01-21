package com.tokopedia.tokopedianow.home.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.tokopedianow.R
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
        private const val QUEST_DETAIL_STAGING_URL = "https://staging.tokopedia.com/now/quest-channel"
        private const val QUEST_DETAIL_PRODUCTION_URL = "https://www.tokopedia.com/now/quest-channel"

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
        setTitle(getString(R.string.tokopedianow_quest_sequence_widget_title))

        val currentQuestFinished = element.questList.filter { it.currentProgress == it.totalProgress }.size
        val totalQuestTarget = element.questList.size
        if (currentQuestFinished != totalQuestTarget) {
            if (TokopediaUrl.getInstance().TYPE == Env.STAGING) {
                setSeeAll(getString(R.string.tokopedianow_quest_sequence_widget_see_all), QUEST_DETAIL_STAGING_URL)
            } else {
                setSeeAll(getString(R.string.tokopedianow_quest_sequence_widget_see_all), QUEST_DETAIL_PRODUCTION_URL)
            }
        } else {
            setSeeAll(getString(R.string.tokopedianow_quest_sequence_widget_see_all))
        }

        when(element.state) {
            HomeLayoutItemState.LOADED -> questLoaded(
                id = element.id,
                currentQuestFinished = currentQuestFinished,
                totalQuestTarget = totalQuestTarget,
                questList = element.questList
            )
            else -> questNotLoaded()
        }
    }

    private fun questLoaded(id: String, currentQuestFinished: Int, totalQuestTarget: Int, questList: List<HomeQuestWidgetUiModel>) {
        addWidgets(id, currentQuestFinished, totalQuestTarget, questList)
    }

    private fun questNotLoaded() {
        addErrorWidgets()
    }

    private fun setAdapter() {
        binding?.rvQuest?.apply {
            adapter = this@HomeQuestSequenceWidgetViewHolder.adapter
            layoutManager = LinearLayoutManager(itemView.context,  LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun addWidgets(id: String, currentQuestFinished: Int, totalQuestTarget: Int, questList: List<HomeQuestWidgetUiModel>) {
        val widgets = mutableListOf<Visitable<*>>()
        if (currentQuestFinished != totalQuestTarget) {
            widgets.add(
                HomeQuestTitleUiModel(
                    currentQuestFinished = currentQuestFinished,
                    totalQuestTarget = totalQuestTarget
                )
            )
            widgets.addAll(questList.filter { it.status !=  STATUS_CLAIMED})
        } else {
            widgets.add(
                HomeQuestAllClaimedWidgetUiModel(
                    id = id,
                    currentQuestFinished = currentQuestFinished,
                    totalQuestTarget = totalQuestTarget
                )
            )
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
                    RouteManager.route(context, ApplinkConstInternalGlobal.WEBVIEW, linkUrl)
                }
            } else {
                visibility = View.INVISIBLE
            }
        }
    }

    interface HomeQuestSequenceWidgetListener {
        fun onClickRefreshQuestWidget()
        fun onCloseQuestAllClaimedBtnClicked(id: String)
    }
}