package com.tokopedia.minicart.bmgm.presentation.model

import com.tokopedia.minicart.bmgm.presentation.adapter.factory.BmgmMiniCartAdapterFactory

/**
 * Created by @ilhamsuaib on 03/08/23.
 */

data class BmgmBundledProductUiModel(
    val products: List<BmgmSingleProductUiModel> = emptyList()
) : BaseMiniCartUiModel {

    override fun type(typeFactory: BmgmMiniCartAdapterFactory): Int {
        return typeFactory.type(this)
    }
}