package com.tokopedia.minicart.bmgm.presentation.adapter.factory

import com.tokopedia.minicart.bmgm.presentation.model.BmgmProductPlaceholderUiModel
import com.tokopedia.purchase_platform.common.feature.bmgm.adapter.BmgmMiniCartAdapterFactory

/**
 * Created by @ilhamsuaib on 08/08/23.
 */

interface BmgmMiniCartAdapterFactoryInterface : BmgmMiniCartAdapterFactory {
    fun type(model: BmgmProductPlaceholderUiModel): Int
}