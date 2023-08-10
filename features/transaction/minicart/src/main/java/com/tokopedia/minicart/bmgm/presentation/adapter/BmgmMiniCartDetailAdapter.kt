package com.tokopedia.minicart.bmgm.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.minicart.bmgm.presentation.adapter.factory.BmgmMiniCartDetailAdapterFactoryImpl
import com.tokopedia.purchase_platform.common.feature.bmgm.uimodel.BmgmMiniCartVisitable

/**
 * Created by @ilhamsuaib on 10/08/23.
 */

class BmgmMiniCartDetailAdapter : BaseListAdapter<BmgmMiniCartVisitable, AdapterTypeFactory>(
    BmgmMiniCartDetailAdapterFactoryImpl()
)