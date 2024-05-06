package com.tokopedia.tokopedianow.annotation.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.annotation.presentation.adapter.differ.AllAnnotationDiffer
import com.tokopedia.tokopedianow.common.base.adapter.BaseTokopediaNowListAdapter

class AllAnnotationAdapter(
    typeFactory: AllAnnotationAdapterTypeFactory
): BaseTokopediaNowListAdapter<Visitable<*>, AllAnnotationAdapterTypeFactory>(typeFactory, AllAnnotationDiffer())
