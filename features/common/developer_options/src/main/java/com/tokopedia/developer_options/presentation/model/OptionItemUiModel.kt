package com.tokopedia.developer_options.presentation.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.developer_options.presentation.adapter.typefactory.DeveloperOptionTypeFactory

abstract class OptionItemUiModel(val text: String): Visitable<DeveloperOptionTypeFactory>