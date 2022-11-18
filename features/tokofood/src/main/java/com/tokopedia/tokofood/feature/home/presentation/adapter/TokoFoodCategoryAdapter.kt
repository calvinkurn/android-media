package com.tokopedia.tokofood.feature.home.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokofood.common.presentation.adapter.BaseTokoFoodListAdapter

class TokoFoodCategoryAdapter(
    typeFactory: TokoFoodCategoryAdapterTypeFactory,
    differ: TokoFoodListDiffer
): BaseTokoFoodListAdapter<Visitable<*>, TokoFoodCategoryAdapterTypeFactory>(typeFactory, differ)