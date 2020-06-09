package com.tokopedia.play.broadcaster.view.adapter.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.play.broadcaster.ui.model.EtalaseLoadingUiModel
import com.tokopedia.play.broadcaster.ui.model.EtalaseUiModel
import com.tokopedia.play.broadcaster.ui.viewholder.PlayListLoadingViewHolder

/**
 * Created by jegul on 05/06/20
 */
class EtalaseLoadingAdapterDelegate : TypedAdapterDelegate<EtalaseLoadingUiModel, EtalaseUiModel, PlayListLoadingViewHolder>(PlayListLoadingViewHolder.LAYOUT) {

    override fun onBindViewHolder(item: EtalaseLoadingUiModel, holder: PlayListLoadingViewHolder) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): PlayListLoadingViewHolder {
        return PlayListLoadingViewHolder(basicView)
    }
}