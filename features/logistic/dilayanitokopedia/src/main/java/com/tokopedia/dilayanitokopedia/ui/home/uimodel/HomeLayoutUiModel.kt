package com.tokopedia.dilayanitokopedia.ui.home.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.dilayanitokopedia.ui.home.adapter.DtHomeAdapterTypeFactory
import com.tokopedia.kotlin.model.ImpressHolder

abstract class HomeLayoutUiModel(
    val visitableId: String
) : Visitable<DtHomeAdapterTypeFactory>, ImpressHolder()
