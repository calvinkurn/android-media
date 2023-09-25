package com.tokopedia.inbox.universalinbox.view.adapter.typefactory

import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxMenuSeparatorUiModel
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxMenuUiModel
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxRecommendationTitleUiModel
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxRecommendationUiModel
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxRecommendationWidgetUiModel
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxTopAdsBannerUiModel
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxTopadsHeadlineUiModel
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxWidgetMetaUiModel

interface UniversalInboxTypeFactory : AdapterTypeFactory {
    fun type(uiModel: UniversalInboxMenuSeparatorUiModel): Int
    fun type(uiModel: UniversalInboxMenuUiModel): Int
    fun type(uiModel: UniversalInboxRecommendationTitleUiModel): Int
    fun type(uiModel: UniversalInboxTopAdsBannerUiModel): Int
    fun type(uiModel: UniversalInboxTopadsHeadlineUiModel): Int
    fun type(uiModel: UniversalInboxWidgetMetaUiModel): Int
    fun type(uiModel: UniversalInboxRecommendationUiModel): Int
    fun type(uiModel: UniversalInboxRecommendationWidgetUiModel): Int
}
