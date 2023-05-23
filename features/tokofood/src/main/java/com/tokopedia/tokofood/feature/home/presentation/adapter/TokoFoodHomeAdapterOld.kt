package com.tokopedia.tokofood.feature.home.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokofood.common.presentation.adapter.BaseTokoFoodListAdapter

class TokoFoodHomeAdapterOld(
    typeFactory: TokoFoodHomeAdapterTypeFactoryOld,
    differ: TokoFoodListDiffer
): BaseTokoFoodListAdapter<Visitable<*>, TokoFoodHomeAdapterTypeFactoryOld>(typeFactory, differ)
