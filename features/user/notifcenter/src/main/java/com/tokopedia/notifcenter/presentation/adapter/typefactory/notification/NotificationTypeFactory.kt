package com.tokopedia.notifcenter.presentation.adapter.typefactory.notification

import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.notifcenter.data.uimodel.BigDividerUiModel
import com.tokopedia.notifcenter.data.uimodel.NotificationUiModel
import com.tokopedia.notifcenter.data.uimodel.SectionTitleUiModel

interface NotificationTypeFactory : AdapterTypeFactory {
    fun type(sectionTitleUiModel: SectionTitleUiModel): Int
    fun type(bigDividerUiModel: BigDividerUiModel): Int
    fun type(notificationUiModel: NotificationUiModel): Int

    /**
     * to support 1 uiModel has several type of view
     * @return must be layout [LayoutRes]
     */
    @LayoutRes
    fun getItemViewType(visitables: List<Visitable<*>>, position: Int, default: Int): Int
}