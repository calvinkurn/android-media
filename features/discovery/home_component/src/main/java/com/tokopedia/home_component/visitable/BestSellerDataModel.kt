package com.tokopedia.home_component.visitable

import android.os.Bundle
import com.tokopedia.home_component.HomeComponentTypeFactory
import com.tokopedia.home_component.model.ChannelModel

data class BestSellerDataModel(
    val chipProductList: List<BestSellerChipProductDataModel> = listOf(),
    val channelModel: ChannelModel,
    val title: String = "",
    val currentPageInGroup: Int = 0,
): HomeComponentVisitable {

    private val activatedChip = chipProductList.find { it.isActivated }

    val id = channelModel.id
    val pageName = channelModel.pageName
    val widgetParam = channelModel.widgetParam
    val dividerType = channelModel.channelConfig.dividerType
    val dividerSize = channelModel.channelConfig.dividerSize
    val seeMoreApplink = activatedChip?.seeMoreApplink ?: ""
    val willShow = chipProductList.size > MIN_CHIPS_TO_SHOW
    val activeChipPosition = chipProductList.indexOf(activatedChip) + 1

    fun findChip(title: String) = chipProductList.find { it.title == title }

    override fun visitableId(): String {
        return channelModel.id
    }

    override fun equalsWith(b: Any?): Boolean {
        return b == this
    }

    override fun getChangePayloadFrom(b: Any?): Bundle {
        return Bundle()
    }

    override fun type(typeFactory: HomeComponentTypeFactory?): Int {
        return typeFactory?.type(this) ?: 0
    }

    companion object {
        private const val MIN_CHIPS_TO_SHOW = 1
    }
}
