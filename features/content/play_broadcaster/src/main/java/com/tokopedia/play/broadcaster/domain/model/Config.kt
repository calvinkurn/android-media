package com.tokopedia.play.broadcaster.domain.model

import com.google.gson.annotations.SerializedName

/**
 * Created by mzennis on 25/06/20.
 */
data class Config(
        @SerializedName("streamAllowed")
        val streamAllowed: Boolean = false,
        @SerializedName("shortVideoAllowed")
        val shortVideoAllowed: Boolean = false,
        @SerializedName("hasContent")
        val hasContent: Boolean = false,
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
        val scheduledTime: ScheduledTime = ScheduledTime(),
        @SerializedName("tnc")
        val tnc: List<GetBroadcasterAuthorConfigResponse.TermsAndCondition> = emptyList(),
        @SerializedName("beautificationConfig")
        val beautificationConfig: BeautificationConfig = BeautificationConfig(),
        @SerializedName("show_save_button")
        val showSaveButton: Boolean = false,
) {
        data class ScheduledTime(
                @SerializedName("minimum")
                val minimum: String = "",
                @SerializedName("maximum")
                val maximum: String = "",
                @SerializedName("default")
                val default: String = ""
        )

        data class BeautificationConfig(
            @SerializedName("license")
            val license: String = "",
            @SerializedName("model")
            val model: String = "",
            @SerializedName("custom_face")
            val customFace: CustomFace = CustomFace(),
            @SerializedName("presets")
            val presets: List<Preset> = emptyList(),
        ) {

            data class CustomFace(
                @SerializedName("asset_android")
                val assetAndroid: String = "",
                @SerializedName("menu")
                val menu: List<Menu> = emptyList(),
            ) {

                data class Menu(
                    @SerializedName("id")
                    val id: String = "",
                    @SerializedName("name")
                    val name: String = "",
                    @SerializedName("active")
                    val active: Boolean = false,
                    @SerializedName("min_value")
                    val minValue: Double = 0.0,
                    @SerializedName("max_value")
                    val maxValue: Double = 0.0,
                    @SerializedName("default_value")
                    val defaultValue: Double = 0.0,
                    @SerializedName("value")
                    val value: Double = 0.0,
                )
            }

            data class Preset(
                @SerializedName("id")
                val id: String = "",
                @SerializedName("name")
                val name: String = "",
                @SerializedName("active")
                val active: Boolean = false,
                @SerializedName("min_value")
                val minValue: Double = 0.0,
                @SerializedName("max_value")
                val maxValue: Double = 0.0,
                @SerializedName("default_value")
                val defaultValue: Double = 0.0,
                @SerializedName("value")
                val value: Double = 0.0,
                @SerializedName("url_icon")
                val urlIcon: String = "",
                @SerializedName("asset_link")
                val assetLink: String = "",
            )
        }
}
