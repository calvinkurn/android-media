package com.tokopedia.minicart.common.widget.viewmodel.dataprovider

import com.tokopedia.minicart.cartlist.MiniCartListUiModelMapper
import com.tokopedia.minicart.cartlist.uimodel.MiniCartListUiModel
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData

object UserInteractionDataProvider {

    private val uiModelMapper = MiniCartListUiModelMapper()

    fun provideMiniCartListUiModel(): MiniCartListUiModel {
        val miniCartData = GetMiniCartListDataProvider.provideGetMiniCartListSuccessAllAvailable()
        return uiModelMapper.mapUiModel(miniCartData)
    }

    fun provideMiniCartSimplifiedData(): MiniCartSimplifiedData {
        val miniCartSimplifieddata = GetMiniCartListSimplifiedDataProvider.provideGetMiniCartSimplifiedSuccessAllAvailable()
        return miniCartSimplifieddata
    }
}