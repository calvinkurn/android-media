package com.tokopedia.notifcenter.data.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.notifcenter.data.entity.notification.NotifcenterDetailResponse
import com.tokopedia.notifcenter.data.uimodel.BigDividerUiModel
import com.tokopedia.notifcenter.data.uimodel.SectionTitleUiModel
import com.tokopedia.notifcenter.presentation.adapter.typefactory.notification.NotificationTypeFactory
import javax.inject.Inject

class NotifcenterDetailMapper @Inject constructor() {

    fun map(response: NotifcenterDetailResponse): List<Visitable<NotificationTypeFactory>> {
        val items = arrayListOf<Visitable<NotificationTypeFactory>>()

        if (response.notifcenterDetail.newList.isNotEmpty()) {
            items.add(SectionTitleUiModel("Terbaru"))
            items.addAll(response.notifcenterDetail.newList)
        }

        if (response.notifcenterDetail.list.isNotEmpty()) {
            if (items.isNotEmpty()) {
                items.add(BigDividerUiModel())
            }
            items.add(SectionTitleUiModel("Sebelumnya"))
            items.addAll(response.notifcenterDetail.list)
        }

        return items
    }

}