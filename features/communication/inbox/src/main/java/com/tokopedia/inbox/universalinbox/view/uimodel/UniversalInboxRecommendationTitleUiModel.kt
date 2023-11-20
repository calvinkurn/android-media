package com.tokopedia.inbox.universalinbox.view.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.inbox.universalinbox.view.adapter.typefactory.UniversalInboxTypeFactory

data class UniversalInboxRecommendationTitleUiModel(
    val text: String
) : Visitable<UniversalInboxTypeFactory> {

    override fun type(typeFactory: UniversalInboxTypeFactory): Int {
        return typeFactory.type(this)
    }

    companion object {
        fun areItemsTheSame(
            oldItem: UniversalInboxRecommendationTitleUiModel,
            newItem: UniversalInboxRecommendationTitleUiModel
        ): Boolean {
            return oldItem.text == newItem.text
        }
    }
}
