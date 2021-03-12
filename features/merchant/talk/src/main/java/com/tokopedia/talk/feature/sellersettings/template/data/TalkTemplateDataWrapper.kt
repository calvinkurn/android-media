package com.tokopedia.talk.feature.sellersettings.template.data

data class TalkTemplateDataWrapper(
        val isSeller: Boolean = false,
        val isEditMode: Boolean = false,
        val template: String? = null,
        val index: Int? = null,
        val allowDelete: Boolean = false
)