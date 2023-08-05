package com.tokopedia.minicart.bmgm.presentation.adapter.factory

import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.minicart.bmgm.presentation.model.BmgmBundledProductUiModel
import com.tokopedia.minicart.bmgm.presentation.model.BmgmSingleProductUiModel
import com.tokopedia.minicart.bmgm.presentation.model.BmgmProductPlaceholderUiModel

/**
 * Created by @ilhamsuaib on 31/07/23.
 */

interface BmgmMiniCartAdapterFactory : AdapterTypeFactory {

    fun type(model: BmgmSingleProductUiModel): Int
    fun type(model: BmgmBundledProductUiModel): Int
    fun type(model: BmgmProductPlaceholderUiModel): Int
}