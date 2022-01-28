package com.tokopedia.play.broadcaster.setup.product.view.adapter

import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.play.broadcaster.setup.product.view.model.EtalaseListModel

/**
 * Created by kenny.hadisaputra on 27/01/22
 */
internal class EtalaseListAdapter : BaseDiffUtilAdapter<EtalaseListModel>() {

    init {
        delegatesManager
            .addDelegate(EtalaseListAdapterDelegate.Header())
            .addDelegate(EtalaseListAdapterDelegate.Campaign())
            .addDelegate(EtalaseListAdapterDelegate.Etalase())
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