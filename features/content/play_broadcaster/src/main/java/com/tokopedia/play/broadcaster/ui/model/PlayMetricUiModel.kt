package com.tokopedia.play.broadcaster.ui.model

/**
 * Created by jegul on 10/06/20
 */
data class PlayMetricUiModel(
        val iconUrl: String,
        val spannedSentence: CharSequence,
        val type: String,
        val interval: Long
)