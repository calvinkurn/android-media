package com.tokopedia.navigation.data.mapper

import com.tokopedia.navigation.domain.pojo.NotificationCenterDetail
import com.tokopedia.navigation.presentation.view.viewmodel.NotificationUpdateItemViewModel
import com.tokopedia.navigation.presentation.view.viewmodel.NotificationUpdateViewModel
import javax.inject.Inject

/**
 * @author : Steven 11/04/19
 */
class GetNotificationUpdateMapper @Inject constructor(){
    open fun map(pojo: NotificationCenterDetail) : NotificationUpdateViewModel {
        var item = pojo.pojo
        var list = arrayListOf<NotificationUpdateItemViewModel>()
        for (notificationUpdateItem in item.list) {
            var datum = NotificationUpdateItemViewModel(
                    notificationId = notificationUpdateItem.notifId,
                    iconUrl = notificationUpdateItem.dataNotification.infoThumbnailUrl,
                    contentUrl = notificationUpdateItem.dataNotification.infoThumbnailUrl,
                    time = notificationUpdateItem.createTime,
                    title = notificationUpdateItem.title,
                    body = notificationUpdateItem.shortDescription,
                    sectionTitle = notificationUpdateItem.sectionKey,
                    templateKey = notificationUpdateItem.templateKey,
                    isRead = convertReadStatus(notificationUpdateItem.readStatus),
                    appLink = notificationUpdateItem.dataNotification.appLink

            )
            list.add(datum)
        }
        list.addAll(list)
        list.addAll(list)
        return NotificationUpdateViewModel(item.paging, list, item.userInfo)
    }

    private fun convertReadStatus(readStatus: Long): Boolean {
        if(readStatus.equals(1)) return false
        else return true
    }
}