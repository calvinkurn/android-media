package com.tokopedia.minicart.bmgm.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.minicart.bmgm.presentation.adapter.factory.BmgmMiniCartAdapterFactoryImpl
import com.tokopedia.purchase_platform.common.feature.bmgm.uimodel.BmgmMiniCartVisitable

/**
 * Created by @ilhamsuaib on 31/07/23.
 */

class BmgmMiniCartAdapter : BaseListAdapter<BmgmMiniCartVisitable, AdapterTypeFactory>(
    BmgmMiniCartAdapterFactoryImpl()
)