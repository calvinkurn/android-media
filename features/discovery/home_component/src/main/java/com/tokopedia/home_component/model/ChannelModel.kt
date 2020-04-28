package com.tokopedia.home_component.model

data class ChannelModel(
        val id: String,
        val groupId: String,
        val headerName: String,
        val type: String = "",
        var verticalPosition: Int = 0,
        val channelHeader: ChannelHeader = ChannelHeader(),
        val channelBanner: ChannelBanner = ChannelBanner(),
        val channelConfig: ChannelConfig = ChannelConfig(),
        val trackingAttributionModel: TrackingAttributionModel = TrackingAttributionModel(),
        val channelGrids: List<ChannelGrid>  = listOf()
)