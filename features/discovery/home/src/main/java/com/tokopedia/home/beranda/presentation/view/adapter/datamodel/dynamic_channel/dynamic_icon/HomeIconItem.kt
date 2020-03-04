package com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.dynamic_icon

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.kotlin.model.ImpressHolder

/**
 * @author by errysuprayogi on 11/28/17.
 */
data class HomeIconItem(
        var id: String? = "",
        var title: String = "",
        var icon: String = "",
        var applink: String = "",
        var url: String = "",
        var buIdentifier: String = "",
        var galaxyAttribution: String = "",
        var affinityLabel: String = "",
        var shopId: String = "",
        var categoryPersona: String = "") : ImpressHolder() {
}
