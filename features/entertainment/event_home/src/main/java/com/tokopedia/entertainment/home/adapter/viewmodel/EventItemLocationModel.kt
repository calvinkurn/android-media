package com.tokopedia.entertainment.home.adapter.viewmodel

import com.tokopedia.kotlin.model.ImpressHolder

data class EventItemLocationModel(
        var id: String,
        var imageUrl: String,
        var title: String,
        var tagline: String,
        var locationType: String) : ImpressHolder()