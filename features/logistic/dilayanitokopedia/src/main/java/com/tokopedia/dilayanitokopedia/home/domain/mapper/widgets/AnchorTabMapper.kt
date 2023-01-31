package com.tokopedia.dilayanitokopedia.home.domain.mapper.widgets

import androidx.annotation.VisibleForTesting
import com.tokopedia.dilayanitokopedia.home.domain.model.GetHomeAnchorTabResponse
import com.tokopedia.dilayanitokopedia.home.presentation.uimodel.AnchorTabUiModel
import java.net.URLDecoder

/**
 * Created by irpan on 07/11/22.
 */
object AnchorTabMapper {

    @VisibleForTesting
    const val KEY_ANCHOR_IDENTIFIER = "anchor_indentifier"
    @VisibleForTesting
    const val KEYWOARD_CHANNEL_GROUP_ID = "channelgroupid_"

    /**
     * Mapping layout list to list menu Anchor tab
     */
    fun GetHomeAnchorTabResponse.GetHomeIconV2.mapMenuList(): List<AnchorTabUiModel> {
        var listMenu = mutableListOf<AnchorTabUiModel>()

        this.icons.forEachIndexed anchorTabForEach@{ index, homeIcon ->

            val listFeParam = splitKeyValueAnchorTabParam(homeIcon.feParam)
            val valueAnchorIdentifier = listFeParam?.getValue(KEY_ANCHOR_IDENTIFIER).toString()
            val channelGroupId = valueAnchorIdentifier.replace(KEYWOARD_CHANNEL_GROUP_ID, "")

            val anchorTab = AnchorTabUiModel(
                homeIcon.id.toString(),
                homeIcon.name,
                homeIcon.imageUrl,
                channelGroupId
            )

            listMenu.add(anchorTab)
            listMenu = listMenu.toMutableList()
        }
        return listMenu.toList()
    }

    private fun splitKeyValueAnchorTabParam(url: String): Map<String, String>? {
        val queryPairs: MutableMap<String, String> = LinkedHashMap()

        val pairs = url.split("&").toTypedArray()
        for (pair in pairs) {
            val idx = pair.indexOf("=")
            queryPairs[URLDecoder.decode(pair.substring(0, idx), "UTF-8")] = URLDecoder.decode(pair.substring(idx + 1), "UTF-8")
        }

        return queryPairs
    }
}
