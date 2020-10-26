package com.tokopedia.notifcenter.presentation.adapter.typefactory.notification

import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.notifcenter.data.uimodel.BigDividerUiModel
import com.tokopedia.notifcenter.data.uimodel.NotificationUiModel
import com.tokopedia.notifcenter.data.uimodel.SectionTitleUiModel

interface NotificationTypeFactory : AdapterTypeFactory {
    fun type(sectionTitleUiModel: SectionTitleUiModel): Int
    fun type(bigDividerUiModel: BigDividerUiModel): Int
    fun type(notificationUiModel: NotificationUiModel): Int
}