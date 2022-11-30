package com.tokopedia.play.view.uimodel.mapper

import com.tokopedia.content.common.model.Content
import com.tokopedia.content.common.model.WidgetSlot
import com.tokopedia.play.di.PlayScope
import com.tokopedia.play.view.uimodel.ChipWidgetUiModel
import com.tokopedia.play.view.uimodel.WidgetUiModel
import javax.inject.Inject

/**
 * @author by astidhiyaa on 30/11/22
 */
@PlayScope
class PlayExploreWidgetMapper @Inject constructor() {
    @Deprecated("Change to SubSlotUiModel")
    val WidgetSlot.isSubSlotAvailable : Boolean
        get() {
            return this.playGetContentSlot.data.any {
                it.type == SUB_SLOT_TYPE
            }
        }

    @OptIn(ExperimentalStdlibApi::class)
    fun map(widgetSlot: WidgetSlot): List<WidgetUiModel> {
        return buildList {
            widgetSlot.playGetContentSlot.data.map {
                when(it.type){
                    TAB_MENU_TYPE -> add(mapChips(it))
                    SUB_SLOT_TYPE -> add(WidgetUiModel.SubSlotUiModel)
                    else -> {}
                }
            }
        }
    }

    private fun mapChips(content: Content) : WidgetUiModel.TabMenuUiModel {
        val newList = content.items.map {
            ChipWidgetUiModel(
                group = it.group,
                sourceType = it.sourceType,
                sourceId = it.sourceId,
                text = it.label,
            )
        }
        return WidgetUiModel.TabMenuUiModel(items = newList)
    }

    companion object {
        private val TAB_MENU_TYPE = "tabMenu"
        private val SUB_SLOT_TYPE = "subSlot"
        private val CHANNEL_BLOCK_TYPE = "channelBlock"
    }
}
