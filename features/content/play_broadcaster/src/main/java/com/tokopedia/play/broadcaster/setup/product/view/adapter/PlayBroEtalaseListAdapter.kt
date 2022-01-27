package com.tokopedia.play.broadcaster.setup.product.view.adapter

import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.play.broadcaster.setup.product.view.model.EtalaseListModel

/**
 * Created by kenny.hadisaputra on 27/01/22
 */
internal class PlayBroEtalaseListAdapter : BaseDiffUtilAdapter<EtalaseListModel>() {

    init {
        delegatesManager
            .addDelegate(PlayBroEtalaseListAdapterDelegate.Header())
            .addDelegate(PlayBroEtalaseListAdapterDelegate.Campaign())
            .addDelegate(PlayBroEtalaseListAdapterDelegate.Etalase())
    }

    override fun areItemsTheSame(
        oldItem: EtalaseListModel,
        newItem: EtalaseListModel
    ): Boolean {
        return if (oldItem is EtalaseListModel.Campaign && newItem is EtalaseListModel.Campaign) {
            oldItem.campaignUiModel.id == newItem.campaignUiModel.id
        } else if (oldItem is EtalaseListModel.Etalase && newItem is EtalaseListModel.Etalase) {
            oldItem.etalaseUiModel.id == newItem.etalaseUiModel.id
        } else oldItem == newItem
    }

    override fun areContentsTheSame(
        oldItem: EtalaseListModel,
        newItem: EtalaseListModel
    ): Boolean {
        return oldItem == newItem
    }
}