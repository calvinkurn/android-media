package com.tokopedia.tokofood.feature.home.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.tokofood.feature.home.presentation.adapter.TokoFoodHomeTypeFactory

abstract class TokoFoodHomeLayoutUiModel(
    val visitableId: String
): Visitable<TokoFoodHomeTypeFactory>, ImpressHolder()