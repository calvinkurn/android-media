package com.tokopedia.content.product.picker.seller.view.viewcomponent

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.content.common.util.eventbus.EventBus
import com.tokopedia.content.product.picker.R
import com.tokopedia.content.product.picker.seller.view.adapter.EtalaseListAdapter
import com.tokopedia.content.product.picker.seller.view.itemdecoration.EtalaseListItemDecoration
import com.tokopedia.content.product.picker.seller.view.viewholder.EtalaseListViewHolder
import com.tokopedia.content.product.picker.seller.model.campaign.CampaignUiModel
import com.tokopedia.content.product.picker.seller.model.etalase.EtalaseUiModel
import com.tokopedia.content.product.picker.seller.model.etalase.SelectedEtalaseModel
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
        view.addItemDecoration(EtalaseListItemDecoration(view.context))
    }

    @OptIn(ExperimentalStdlibApi::class)
    fun setCampaignAndEtalaseList(
        campaignList: List<CampaignUiModel>,
        etalaseList: List<EtalaseUiModel>,
        selected: SelectedEtalaseModel,
    ) {
        val combinedEtalaseList = buildList {
            if (campaignList.isNotEmpty()) {
                add(EtalaseListAdapter.Model.Header(getString(R.string.campaign)))
                addAll(campaignList.map {
                    EtalaseListAdapter.Model.Campaign(
                        campaignUiModel = it,
                        isSelected = selected is SelectedEtalaseModel.Campaign &&
                                selected.campaign.id == it.id
                    )
                })
            }

            if (etalaseList.isNotEmpty()) {
                add(EtalaseListAdapter.Model.Header(getString(R.string.etalase)))
                addAll(
                    etalaseList.map {
                        EtalaseListAdapter.Model.Etalase(
                            etalaseUiModel = it,
                            isSelected = selected is SelectedEtalaseModel.Etalase &&
                                    selected.etalase.id == it.id
                        )
                    }
                )
            }
        }

        adapter.setItemsAndAnimateChanges(combinedEtalaseList)
    }

    sealed class Event {
        data class OnCampaignSelected(val campaign: CampaignUiModel) : Event()
        data class OnEtalaseSelected(val etalase: EtalaseUiModel) : Event()
    }
}
