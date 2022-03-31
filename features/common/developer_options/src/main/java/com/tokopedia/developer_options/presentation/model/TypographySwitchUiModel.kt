package com.tokopedia.developer_options.presentation.model

import com.tokopedia.developer_options.presentation.adapter.typefactory.DeveloperOptionTypeFactory

class TypographySwitchUiModel(keyword: List<String>): OptionItemUiModel(keyword) {
    override fun type(typeFactory: DeveloperOptionTypeFactory): Int =
        typeFactory.type(this)
}