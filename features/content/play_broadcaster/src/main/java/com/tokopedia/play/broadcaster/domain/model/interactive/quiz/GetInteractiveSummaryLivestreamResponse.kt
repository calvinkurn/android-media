package com.tokopedia.play.broadcaster.domain.model.interactive.quiz

import com.google.gson.annotations.SerializedName

data class GetInteractiveSummaryLivestreamResponse(
    @SerializedName("playInteractiveGetSummaryLivestream")
    val playInteractiveGetSummaryLivestream : PlayInteractiveGetSummaryLivestream
) {
    data class PlayInteractiveGetSummaryLivestream(
        @SerializedName("participantCount")
        val participantCount: Int,
    )
}
