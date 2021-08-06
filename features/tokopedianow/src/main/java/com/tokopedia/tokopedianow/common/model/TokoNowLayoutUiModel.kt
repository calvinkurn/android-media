package com.tokopedia.tokopedianow.common.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowTypeFactory

abstract class TokoNowLayoutUiModel(val visitableId: String): Visitable<TokoNowTypeFactory>