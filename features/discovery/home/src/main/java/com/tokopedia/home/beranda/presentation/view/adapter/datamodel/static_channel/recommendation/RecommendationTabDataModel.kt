package com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.collapsing.tab.layout.CollapsingTabLayout
import com.tokopedia.home_component.widget.tab.MegaTabItem

class RecommendationTabDataModel(
    val id: String,
    val name: String,
    val imageUrl: String,
    val position: Int,
    val sourceType: String = "",
    val isJumperTab: Boolean
) {

    fun convertFeedTabModelToDataObject(): Any {
        return DataLayer.mapOf(
            DATA_ID, id,
            DATA_NAME, String.format(DATA_VALUE_NAME, name),
            DATA_POSITION, position.toString(),
            DATA_CREATIVE, name,
            DATA_CREATIVE_URL, imageUrl
        )
    }

    fun toCarouselTabItem(): CollapsingTabLayout.TabItemData {
        return CollapsingTabLayout.TabItemData(name, imageUrl)
    }

    fun toMegaTabItem(): MegaTabItem {
        return MegaTabItem(name, imageUrl)
    }

    companion object {
        const val DATA_ID = "id"
        const val DATA_NAME = "name"
        const val DATA_CREATIVE = "creative"
        const val DATA_POSITION = "position"
        const val DATA_CREATIVE_URL = "creative_url"

        const val DATA_VALUE_NAME = "/ - homepage recommendation tab - %s"
    }
}
