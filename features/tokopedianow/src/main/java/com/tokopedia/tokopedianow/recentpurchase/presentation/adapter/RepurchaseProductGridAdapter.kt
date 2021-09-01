package com.tokopedia.tokopedianow.recentpurchase.presentation.adapter

import com.tokopedia.tokopedianow.common.base.adapter.BaseTokopediaNowListAdapter
import com.tokopedia.tokopedianow.recentpurchase.presentation.adapter.differ.RepurchaseProductGridDiffer
import com.tokopedia.tokopedianow.recentpurchase.presentation.uimodel.RepurchaseProductUiModel

class RepurchaseProductGridAdapter(
    typeFactory: RepurchaseProductGridAdapterTypeFactory
) : BaseTokopediaNowListAdapter<RepurchaseProductUiModel, RepurchaseProductGridAdapterTypeFactory>(
    typeFactory, RepurchaseProductGridDiffer()
)
