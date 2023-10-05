package com.tokopedia.minicart.bmgm.presentation.adapter.factory

import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.minicart.bmgm.presentation.model.MiniCartDetailUiModel

/**
 * Created by @ilhamsuaib on 31/07/23.
 */

interface BmgmMiniCartDetailAdapterFactory : AdapterTypeFactory {

    fun type(model: MiniCartDetailUiModel.Section): Int
    fun type(model: MiniCartDetailUiModel.Product): Int
}