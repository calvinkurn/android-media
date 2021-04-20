package com.tokopedia.play.broadcaster.ui.model


/**
 * Created by mzennis on 24/09/20.
 */
data class EventUiModel(
        val freeze: Boolean,
        val banned: Boolean,
        val title: String,
        val message: String,
        val buttonTitle: String
)