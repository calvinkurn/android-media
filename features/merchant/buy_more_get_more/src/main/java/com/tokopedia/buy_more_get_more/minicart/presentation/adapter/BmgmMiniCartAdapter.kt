package com.tokopedia.buy_more_get_more.minicart.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.buy_more_get_more.minicart.presentation.adapter.factory.BmgmMiniCartAdapterFactoryImpl
import com.tokopedia.buy_more_get_more.minicart.presentation.model.BmgmMiniCartVisitable

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