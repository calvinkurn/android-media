package com.tokopedia.tokopedianow.similarproduct.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.common.base.adapter.BaseTokopediaNowListAdapter

class SimilarProductAdapter(
    typeFactory: SimilarProductAdapterTypeFactory
) : BaseTokopediaNowListAdapter<Visitable<*>, SimilarProductAdapterTypeFactory>(
    typeFactory,
    SimilarProductDiffer()
)
