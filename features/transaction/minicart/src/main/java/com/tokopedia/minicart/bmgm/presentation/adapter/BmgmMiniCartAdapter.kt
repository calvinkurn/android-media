package com.tokopedia.minicart.bmgm.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.minicart.bmgm.presentation.adapter.factory.BmgmMiniCartAdapterFactoryImpl
import com.tokopedia.minicart.bmgm.presentation.model.BmgmMiniCartVisitable

/**
 * Created by @ilhamsuaib on 31/07/23.
 */

class BmgmMiniCartAdapter(listener: Listener) : BaseListAdapter<BmgmMiniCartVisitable, AdapterTypeFactory>(
    BmgmMiniCartAdapterFactoryImpl(listener)
) {
    interface Listener {
        fun setOnItemClickedListener()
    }
}