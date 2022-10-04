package com.tokopedia.home_component.visitable

import com.tokopedia.home_component.listener.DeclutteredProductCardListener
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.productcard.v2.BlankSpaceConfig

/**
 * Created by frenzel
 */
class DeclutteredProductCardDataModel (
        val productModel: ProductCardModel,
        val blankSpaceConfig: BlankSpaceConfig,
        val grid: ChannelGrid,
        val applink: String = "",
        val componentName: String = "",
        val listener: DeclutteredProductCardListener
): ImpressHolder()
