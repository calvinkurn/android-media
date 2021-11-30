package com.tokopedia.tokopedianow.repurchase.presentation.adapter

import com.tokopedia.tokopedianow.common.base.adapter.BaseTokopediaNowListAdapter
import com.tokopedia.tokopedianow.repurchase.presentation.adapter.differ.RepurchaseProductGridDiffer
import com.tokopedia.tokopedianow.repurchase.presentation.uimodel.RepurchaseProductUiModel

class RepurchaseProductGridAdapter(
    typeFactory: RepurchaseProductGridAdapterTypeFactory
) : BaseTokopediaNowListAdapter<RepurchaseProductUiModel, RepurchaseProductGridAdapterTypeFactory>(
    typeFactory, RepurchaseProductGridDiffer()
)
