package com.tokopedia.developer_options.presentation.model

import com.tokopedia.developer_options.presentation.adapter.typefactory.DeveloperOptionTypeFactory

class ApplinkLogOnNotificationUiModel(text: String) : OptionItemUiModel(text) {
    override fun type(typeFactory: DeveloperOptionTypeFactory): Int {
        return typeFactory.type(this)
    }
}