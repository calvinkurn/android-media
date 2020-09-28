package com.tokopedia.play.broadcaster.view.adapter.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.play.broadcaster.ui.model.CarouselCoverUiModel
import com.tokopedia.play.broadcaster.ui.viewholder.PlayCoverCameraViewHolder

/**
 * Created by jegul on 24/06/20
 */
class PlayCoverCameraAdapterDelegate(
        private val listener: PlayCoverCameraViewHolder.Listener
) : TypedAdapterDelegate<CarouselCoverUiModel.Camera, CarouselCoverUiModel, PlayCoverCameraViewHolder>(PlayCoverCameraViewHolder.LAYOUT) {

    override fun onBindViewHolder(item: CarouselCoverUiModel.Camera, holder: PlayCoverCameraViewHolder) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): PlayCoverCameraViewHolder {
        return PlayCoverCameraViewHolder(basicView, listener)
    }
}