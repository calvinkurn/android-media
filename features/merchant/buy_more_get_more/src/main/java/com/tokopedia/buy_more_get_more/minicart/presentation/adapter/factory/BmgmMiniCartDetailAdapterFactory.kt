package com.tokopedia.buy_more_get_more.minicart.presentation.adapter.factory

import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.buy_more_get_more.minicart.presentation.model.MiniCartDetailUiModel

/**
 * Created by @ilhamsuaib on 31/07/23.
 */

interface BmgmMiniCartDetailAdapterFactory : AdapterTypeFactory {

    fun type(model: MiniCartDetailUiModel.Section): Int
    fun type(model: MiniCartDetailUiModel.Product): Int
}