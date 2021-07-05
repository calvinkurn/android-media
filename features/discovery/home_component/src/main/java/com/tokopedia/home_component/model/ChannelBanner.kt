package com.tokopedia.home_component.model

data class ChannelBanner(
        val id: String = "",
        val title: String = "",
        val description: String = "",
        val backColor: String = "",
        val url: String = "",
        val applink: String = "",
        val textColor: String = "",
        val imageUrl: String = "",
        val attribution: String = "",
        val cta: ChannelCtaData = ChannelCtaData(),
        val gradientColor: ArrayList<String> = arrayListOf("")
)