package com.tokopedia.play.broadcaster.domain.model

import com.google.gson.annotations.SerializedName


/**
 * Created by mzennis on 25/06/20.
 */
data class Config(
        @SerializedName("streamAllowed")
        val streamAllowed: Boolean = false,
        @SerializedName("active_live_channel")
        val activeLiveChannel: Int = 0,
        @SerializedName("draft_channel")
        val draftChannel: Int = 0,
        @SerializedName("paused_channel")
        val pausedChannel: Int = 0,
        @SerializedName("active_live_channel_remaining_sec")
        val activeChannelRemainingDuration: Long = 0,
        @SerializedName("paused_channel_remaining_sec")
        val pausedChannelRemainingDuration: Long = 0,
        @SerializedName("max_duration_sec")
        val maxDuration: Long = 0,
        @SerializedName("max_duration_desc")
        val maxDurationDesc: String = "",
        @SerializedName("max_tagged_product")
        val maxTaggedProduct: Int = 0,
        @SerializedName("min_tagged_product")
        val minTaggedProduct: Int = 0,
        @SerializedName("max_tagged_product_desc")
        val maxTaggedProductDesc: String = "",
        @SerializedName("max_pause_duration_sec")
        val maxPauseDuration: Long = 0,
        @SerializedName("countdown_sec")
        val countdownSec: Long = 0,
        @SerializedName("max_title_length")
        val maxTitleLength: Int = 0,
        @SerializedName("complete_draft")
        val completeDraft: Boolean = false,
        @SerializedName("scheduled_time")
        val scheduledTime: ScheduledTime = ScheduledTime()
) {
        data class ScheduledTime(
                @SerializedName("minimum")
                val minimum: String = "",
                @SerializedName("maximum")
                val maximum: String = "",
                @SerializedName("default")
                val default: String = ""
        )
}