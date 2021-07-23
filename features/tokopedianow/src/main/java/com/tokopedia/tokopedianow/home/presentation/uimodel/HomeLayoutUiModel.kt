package com.tokopedia.tokopedianow.home.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.home.presentation.adapter.HomeTypeFactory

abstract class HomeLayoutUiModel(val visitableId: String): Visitable<HomeTypeFactory>