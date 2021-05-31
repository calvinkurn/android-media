package com.tokopedia.notifcenter.data.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.notifcenter.data.entity.notification.NotifcenterDetail
import com.tokopedia.notifcenter.data.entity.notification.NotifcenterDetailResponse
import com.tokopedia.notifcenter.data.entity.notification.NotificationDetailResponseModel
import com.tokopedia.notifcenter.data.uimodel.BigDividerUiModel
import com.tokopedia.notifcenter.data.uimodel.LoadMoreUiModel
import com.tokopedia.notifcenter.data.uimodel.NotificationUiModel
import com.tokopedia.notifcenter.data.uimodel.SectionTitleUiModel
import com.tokopedia.notifcenter.presentation.adapter.typefactory.notification.NotificationTypeFactory
import javax.inject.Inject

class NotifcenterDetailMapper @Inject constructor() {

    fun mapFirstPage(
            response: NotifcenterDetailResponse,
            needSectionTitle: Boolean = true,
            needLoadMoreButton: Boolean = true
    ): NotificationDetailResponseModel {
        val items = arrayListOf<Visitable<NotificationTypeFactory>>()
        val newSections = mapNewSection(response, needSectionTitle, needLoadMoreButton).items
        val earlierSections = mapEarlierSection(
                response, needSectionTitle, needLoadMoreButton, newSections.isNotEmpty()
        ).items
        items.addAll(newSections)
        items.addAll(earlierSections)
        return NotificationDetailResponseModel(
                items = items,
                hasNext = response.notifcenterDetail.paging.hasNext
        )
    }

    fun mapNewSection(
            response: NotifcenterDetailResponse,
            needSectionTitle: Boolean = true,
            needLoadMoreButton: Boolean = true
    ): NotificationDetailResponseModel {
        val items = arrayListOf<Visitable<NotificationTypeFactory>>()
        val notifcenterDetail = response.notifcenterDetail
        if (notifcenterDetail.newList.isNotEmpty()) {
            updateNotificationOptions(notifcenterDetail.newList, notifcenterDetail)
            if (needSectionTitle) {
                items.add(SectionTitleUiModel("Terbaru"))
            }
            sortProducts(notifcenterDetail.newList)
            items.addAll(notifcenterDetail.newList)
            if (notifcenterDetail.newPaging.hasNext) {
                if (needLoadMoreButton) {
                    items.add(LoadMoreUiModel(LoadMoreUiModel.LoadMoreType.NEW))
                }
                notifcenterDetail.newPaging.lastNotifId = notifcenterDetail.newList.last().notifId
            }
        }
        return NotificationDetailResponseModel(
                items = items,
                hasNext = response.notifcenterDetail.newPaging.hasNext
        )
    }

    fun mapEarlierSection(
            response: NotifcenterDetailResponse,
            needSectionTitle: Boolean = true,
            needLoadMoreButton: Boolean = true,
            needDivider: Boolean = false
    ): NotificationDetailResponseModel {
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
            sortProducts(notifcenterDetail.list)
            items.addAll(notifcenterDetail.list)
            if (notifcenterDetail.paging.hasNext) {
                if (needLoadMoreButton) {
                    items.add(LoadMoreUiModel(LoadMoreUiModel.LoadMoreType.EARLIER))
                }
                notifcenterDetail.paging.lastNotifId = notifcenterDetail.list.last().notifId
            }
        }
        return NotificationDetailResponseModel(
                items = items,
                hasNext = response.notifcenterDetail.paging.hasNext
        )
    }

    private fun sortProducts(notifications: List<NotificationUiModel>) {
        notifications.forEach { notification ->
            notification.productData = notification.productData.sortedBy { it.hasEmptyStock() }
        }
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