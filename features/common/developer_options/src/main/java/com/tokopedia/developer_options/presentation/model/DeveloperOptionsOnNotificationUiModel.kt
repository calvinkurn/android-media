package com.tokopedia.developer_options.presentation.model

import com.tokopedia.developer_options.presentation.adapter.typefactory.DeveloperOptionTypeFactory

/**
 * Created By : Jonathan Darwin on September 29, 2022
 */
class DeveloperOptionsOnNotificationUiModel(keyword: List<String>) : OptionItemUiModel(keyword) {
    override fun type(typeFactory: DeveloperOptionTypeFactory): Int {
        return typeFactory.type(this)
    }
}
