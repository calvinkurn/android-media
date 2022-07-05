package com.tokopedia.tokofood.feature.home.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable

class TokoFoodCategoryAdapter(
    typeFactory: TokoFoodCategoryAdapterTypeFactory,
    differ: TokoFoodListDiffer
): BaseTokoFoodListAdapter<Visitable<*>, TokoFoodCategoryAdapterTypeFactory>(typeFactory, differ)