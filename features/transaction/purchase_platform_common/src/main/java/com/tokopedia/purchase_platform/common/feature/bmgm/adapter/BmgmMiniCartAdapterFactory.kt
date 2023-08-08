package com.tokopedia.purchase_platform.common.feature.bmgm.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.purchase_platform.common.feature.bmgm.uimodel.BmgmCommonDataUiModel

/**
 * Created by @ilhamsuaib on 08/08/23.
 */

interface BmgmMiniCartAdapterFactory : AdapterTypeFactory {

    fun type(model: BmgmCommonDataUiModel.BundledProductUiModel): Int
    fun type(model: BmgmCommonDataUiModel.SingleProductUiModel): Int
    fun type(model: Visitable<*>): Int
}