package com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel

import com.tokopedia.kotlin.model.ImpressHolder

/**
 * Created by dhaba
 */
data class MissionWidgetDataModel(
        val id: Long = 0L,
        val title: String = "",
        val subTitle: String = "",
        val appLink: String = "",
        val url: String = "",
        val imageURL: String = "",
        val impressHolder: ImpressHolder = ImpressHolder()
)