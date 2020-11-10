package com.tokopedia.notifcenter.data.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.notifcenter.data.entity.notification.NotifcenterDetail
import com.tokopedia.notifcenter.data.entity.notification.NotifcenterDetailResponse
import com.tokopedia.notifcenter.data.uimodel.BigDividerUiModel
import com.tokopedia.notifcenter.data.uimodel.LoadMoreUiModel
import com.tokopedia.notifcenter.data.uimodel.NotificationUiModel
import com.tokopedia.notifcenter.data.uimodel.SectionTitleUiModel
import com.tokopedia.notifcenter.presentation.adapter.typefactory.notification.NotificationTypeFactory
import javax.inject.Inject

class NotifcenterDetailMapper @Inject constructor() {

    fun mapFirstPage(
            response: NotifcenterDetailResponse,
            needSectionTitle: Boolean = true
    ): List<Visitable<NotificationTypeFactory>> {
        val items = arrayListOf<Visitable<NotificationTypeFactory>>()
        items.addAll(mapNewSection(response, needSectionTitle))
        items.addAll(mapEarlierSection(response, needSectionTitle, items.isNotEmpty()))
        return items
    }

    fun mapNewSection(
            response: NotifcenterDetailResponse,
            needSectionTitle: Boolean = true
    ): List<Visitable<NotificationTypeFactory>> {
        val items = arrayListOf<Visitable<NotificationTypeFactory>>()
        val notifcenterDetail = response.notifcenterDetail
        if (notifcenterDetail.newList.isNotEmpty()) {
            updateNotificationOptions(notifcenterDetail.newList, notifcenterDetail)
            if (needSectionTitle) {
                items.add(SectionTitleUiModel("Terbaru"))
            }
            items.addAll(notifcenterDetail.newList)
            if (notifcenterDetail.newPaging.hasNext) {
                items.add(LoadMoreUiModel(LoadMoreUiModel.LoadMoreType.NEW))
                notifcenterDetail.newPaging.lastNotifId = notifcenterDetail.newList.last().notifId
            }
        }
        return items
    }

    fun mapEarlierSection(
            response: NotifcenterDetailResponse,
            needSectionTitle: Boolean = true,
            needDivider: Boolean = false
    ): List<Visitable<NotificationTypeFactory>> {
        val items = arrayListOf<Visitable<NotificationTypeFactory>>()
        val notifcenterDetail = response.notifcenterDetail
        if (notifcenterDetail.list.isNotEmpty()) {
            updateNotificationOptions(notifcenterDetail.list, notifcenterDetail)
            if (needDivider && needSectionTitle) {
                items.add(BigDividerUiModel())
            }
            if (needSectionTitle) {
                items.add(SectionTitleUiModel("Sebelumnya"))
            }
            items.addAll(notifcenterDetail.list)
            if (notifcenterDetail.paging.hasNext) {
                items.add(LoadMoreUiModel(LoadMoreUiModel.LoadMoreType.EARLIER))
                notifcenterDetail.paging.lastNotifId = notifcenterDetail.list.last().notifId
            }
        }
        return items
    }


    private fun updateNotificationOptions(
            notifications: List<NotificationUiModel>,
            notifcenterDetail: NotifcenterDetail
    ) {
        notifications.forEach {
            it.options = notifcenterDetail.options
        }
    }

}