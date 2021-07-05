package com.tokopedia.tokopedianow.common.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowItemTypeFactory

abstract class TokoNowLayoutItemUiModel(val visitableId: String): Visitable<TokoNowItemTypeFactory>