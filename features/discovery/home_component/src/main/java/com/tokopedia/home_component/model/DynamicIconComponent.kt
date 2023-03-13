package com.tokopedia.home_component.model

import com.tokopedia.kotlin.model.ImpressHolder

/**
 * Created by Lukas on 1/8/21.
 */
data class DynamicIconComponent(
    val dynamicIcon: List<DynamicIcon> = listOf()
) {
    data class DynamicIcon(
        val id: String = "-1",
        val applink: String = "",
        val imageUrl: String = "",
        val name: String = "",
        val url: String = "",
        val businessUnitIdentifier: String = "",
        val galaxyAttribution: String = "",
        val persona: String = "",
        val brandId: String = "",
        val categoryPersona: String = "",
        val campaignCode: String = "",
        val withBackground: Boolean = false,
        val isSkipCache: Boolean = false
    ) : ImpressHolder()
}
