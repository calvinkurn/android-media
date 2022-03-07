package com.tokopedia.product.detail.tracking

import com.tokopedia.play.widget.ui.model.PlayWidgetMediumChannelUiModel
import com.tokopedia.product.detail.common.data.model.pdplayout.DynamicProductInfoP1
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel

data class ContentWidgetTracker(
    val userId: String,
    private val productInfo: DynamicProductInfoP1,
    private val componentTrackDataModel: ComponentTrackDataModel,
    private val playItem: PlayWidgetMediumChannelUiModel? = null,
    val isRemindMe: Boolean = false
) {
    private val productBasic = productInfo.basic
    private val basicCategory = productBasic.category

    val componentName = componentTrackDataModel.componentName
    val componentType = componentTrackDataModel.componentType
    val componentPosition = componentTrackDataModel.adapterPosition.toString()
    val layoutName = productInfo.layoutName
    val categoryName = basicCategory.name
    val categoryId = basicCategory.id
    val productId = productBasic.productID
    val shopId = productBasic.shopID
    val shopType = productInfo.shopTypeString

    val channelShopId by lazy { playItem?.partner?.id ?: "" }
    val channelId by lazy { playItem?.channelId ?: "" }
    val channelTitle by lazy { playItem?.title ?: "" }
}