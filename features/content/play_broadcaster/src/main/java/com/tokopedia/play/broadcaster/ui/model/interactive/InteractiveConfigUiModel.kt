package com.tokopedia.play.broadcaster.ui.model.interactive

/**
 * Created by jegul on 07/07/21
 */
data class InteractiveConfigUiModel(
        val isActive: Boolean,
        val nameGuidelineHeader: String,
        val nameGuidelineDetail: String,
        val timeGuidelineHeader: String,
        val timeGuidelineDetail: String,
        val durationInMs: Long,
        val availableStartTimeInMs: List<Long>,
)