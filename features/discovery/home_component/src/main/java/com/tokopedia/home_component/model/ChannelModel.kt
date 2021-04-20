package com.tokopedia.home_component.model

import com.tokopedia.kotlin.model.ImpressHolder

data class ChannelModel(
        val id: String,
        val groupId: String,
        val type: String = "",
        val style: ChannelStyle = ChannelStyle.ChannelHome,
        var verticalPosition: Int = 0,
        val contextualInfo: Int = 0,
        val widgetParam: String = "",
        val pageName: String = "",
        val channelHeader: ChannelHeader = ChannelHeader(),
        val channelBanner: ChannelBanner = ChannelBanner(),
        val channelConfig: ChannelConfig = ChannelConfig(),
        val trackingAttributionModel: TrackingAttributionModel = TrackingAttributionModel(),
        val channelGrids: List<ChannelGrid>  = listOf(),
        val name : String = "",
        val layout: String = ""
): ImpressHolder(){
    companion object{
        const val CHANNEL_HOME = "home"
        const val CHANNEL_OS = "os"
    }
}