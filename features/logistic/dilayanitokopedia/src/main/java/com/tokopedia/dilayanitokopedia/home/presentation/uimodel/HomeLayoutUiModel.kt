package com.tokopedia.dilayanitokopedia.home.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.dilayanitokopedia.home.presentation.adapter.DtHomeAdapterTypeFactory
import com.tokopedia.kotlin.model.ImpressHolder

abstract class HomeLayoutUiModel(
    val visitableId: String
) : Visitable<DtHomeAdapterTypeFactory>, ImpressHolder()
