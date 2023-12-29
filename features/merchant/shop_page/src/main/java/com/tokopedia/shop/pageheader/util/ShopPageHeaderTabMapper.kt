package com.tokopedia.shop.pageheader.util

import com.tokopedia.shop.pageheader.data.model.ShopPageHeaderTabModel
import com.tokopedia.universal_sharing.view.model.ChipProperties
import com.tokopedia.universal_sharing.view.model.LinkProperties
import java.util.*

fun ShopPageHeaderTabModel.map(isSelected: Boolean, linkProperties: LinkProperties, id:Int) = ChipProperties(
    id = id,
    title = this.chipsWording,
    isSelected = isSelected,
    properties = linkProperties,
    shareText = this.shareWording
)
