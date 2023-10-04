package com.tokopedia.stories.widget.tracking

import com.tokopedia.stories.widget.domain.StoriesWidgetState

/**
 * Created by kenny.hadisaputra on 03/10/23
 */
interface StoriesWidgetTracker {

    interface Builder {
        fun onImpressedEntryPoint(state: StoriesWidgetState): Data

        fun onClickedEntryPoint(state: StoriesWidgetState): Data
    }

    interface Sender {
        fun sendTracker(data: Data)

        fun reset()
    }

    data class Data(
        val shopId: String,
        val eventAction: String,
        val isImpression: Boolean,
        val map: Map<String, Any>,
    ) {
        companion object {
            val Empty = Data(
                shopId = "",
                eventAction = "",
                isImpression = false,
                map = emptyMap(),
            )
        }
    }
}
