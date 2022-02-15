package com.tokopedia.play.broadcaster.setup.product.view.adapter

import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.play.broadcaster.setup.product.view.model.SelectedEtalaseModel
import com.tokopedia.play.broadcaster.setup.product.view.viewholder.EtalaseListViewHolder
import com.tokopedia.play.broadcaster.ui.model.campaign.CampaignUiModel
import com.tokopedia.play.broadcaster.ui.model.etalase.EtalaseUiModel

/**
 * Created by kenny.hadisaputra on 27/01/22
 */
internal class EtalaseListAdapter(
    etalaseBodyListener: EtalaseListViewHolder.Body.Listener,
) : BaseDiffUtilAdapter<EtalaseListAdapter.Model>() {

    init {
        delegatesManager
            .addDelegate(EtalaseListAdapterDelegate.Header())
            .addDelegate(EtalaseListAdapterDelegate.Campaign(etalaseBodyListener))
            .addDelegate(EtalaseListAdapterDelegate.Etalase(etalaseBodyListener))
    }

    override fun areItemsTheSame(
        oldItem: Model,
        newItem: Model
    ): Boolean {
        return if (oldItem is Model.Campaign && newItem is Model.Campaign) {
            oldItem.campaignUiModel.id == newItem.campaignUiModel.id
        } else if (oldItem is Model.Etalase && newItem is Model.Etalase) {
            oldItem.etalaseUiModel.id == newItem.etalaseUiModel.id
        } else oldItem == newItem
    }

    override fun areContentsTheSame(
        oldItem: Model,
        newItem: Model
    ): Boolean {
        return oldItem == newItem
    }

    sealed class Model {
        data class Header(val text: String) : Model()
        data class Campaign(val campaignUiModel: CampaignUiModel) : Model()
        data class Etalase(val etalaseUiModel: EtalaseUiModel) : Model()
    }
}