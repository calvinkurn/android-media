package com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel

import com.tokopedia.kotlin.model.ImpressHolder

/**
 * @author by yoasfs on 2020-02-18
 */
data class PopularKeywordDataModel(
        val recommendationType: String,
        val applink: String = "",
        val imageUrl: String = "",
        val title: String = "",
        val productCount : String = "",
        val impressHolder: ImpressHolder = ImpressHolder()
)