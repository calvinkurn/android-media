package com.tokopedia.minicart.bmgm.presentation.adapter.factory

import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.minicart.bmgm.presentation.model.BmgmMiniCartVisitable

/**
 * Created by @ilhamsuaib on 08/08/23.
 */

interface BmgmMiniCartAdapterFactory : AdapterTypeFactory {

    fun type(model: BmgmMiniCartVisitable.TierUiModel): Int
    fun type(model: BmgmMiniCartVisitable.ProductUiModel): Int
    fun type(model: BmgmMiniCartVisitable.PlaceholderUiModel): Int
}