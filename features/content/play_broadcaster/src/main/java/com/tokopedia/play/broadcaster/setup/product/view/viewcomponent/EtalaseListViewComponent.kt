package com.tokopedia.play.broadcaster.setup.product.view.viewcomponent

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.play.broadcaster.setup.product.view.adapter.EtalaseListAdapter
import com.tokopedia.play.broadcaster.setup.product.view.model.EtalaseListModel
import com.tokopedia.play.broadcaster.setup.product.view.viewholder.EtalaseListViewHolder
import com.tokopedia.play.broadcaster.ui.model.campaign.CampaignUiModel
import com.tokopedia.play.broadcaster.ui.model.etalase.EtalaseUiModel
import com.tokopedia.play.broadcaster.util.eventbus.EventBus
import com.tokopedia.play_common.viewcomponent.ViewComponent

/**
 * Created by kenny.hadisaputra on 27/01/22
 */
internal class EtalaseListViewComponent(
    view: RecyclerView,
    eventBus: EventBus<in Event>,
) : ViewComponent(view) {

    private val adapter = EtalaseListAdapter(object : EtalaseListViewHolder.Body.Listener {
        override fun onCampaignClicked(campaign: CampaignUiModel) {
            eventBus.emit(Event.OnCampaignSelected(campaign))
        }

        override fun onEtalaseClicked(etalase: EtalaseUiModel) {
            eventBus.emit(Event.OnEtalaseSelected(etalase))
        }
    })

    init {
        view.adapter = adapter
        view.layoutManager = LinearLayoutManager(view.context, RecyclerView.VERTICAL, false)
    }

    fun setEtalaseList(etalaseList: List<EtalaseListModel>) {
        adapter.setItemsAndAnimateChanges(etalaseList)
    }

    sealed class Event {
        data class OnCampaignSelected(val campaign: CampaignUiModel) : Event()
        data class OnEtalaseSelected(val etalase: EtalaseUiModel) : Event()
    }
}