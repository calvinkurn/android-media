package com.tokopedia.buy_more_get_more.minicart.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.buy_more_get_more.minicart.presentation.adapter.factory.BmgmMiniCartDetailAdapterFactoryImpl
import com.tokopedia.buy_more_get_more.minicart.presentation.model.MiniCartDetailUiModel

/**
 * Created by @ilhamsuaib on 10/08/23.
 */

class BmgmMiniCartDetailAdapter : BaseListAdapter<MiniCartDetailUiModel, AdapterTypeFactory>(
    BmgmMiniCartDetailAdapterFactoryImpl()
)