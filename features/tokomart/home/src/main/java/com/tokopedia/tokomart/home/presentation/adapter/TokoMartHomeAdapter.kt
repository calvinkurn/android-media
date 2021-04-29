package com.tokopedia.tokomart.home.presentation.adapter

import com.tokopedia.tokomart.home.base.adapter.BaseHomeListAdapter
import com.tokopedia.tokomart.home.base.uimodel.BaseHomeUiModel
import com.tokopedia.tokomart.home.presentation.adapter.differ.TokoMartHomeListDiffer

class TokoMartHomeAdapter(
    typeFactory: TokoMartHomeAdapterTypeFactory,
    differ: TokoMartHomeListDiffer
): BaseHomeListAdapter<BaseHomeUiModel, TokoMartHomeAdapterTypeFactory>(typeFactory, differ)