package com.tokopedia.notifcenter.data.mapper

import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.notifcenter.data.entity.NotificationCenterDetail
import com.tokopedia.notifcenter.data.entity.NotificationCenterSingleDetail
import com.tokopedia.notifcenter.data.viewbean.NotificationItemViewBean
import com.tokopedia.notifcenter.data.model.NotificationViewData
import javax.inject.Inject

/**
 * @author : Steven 11/04/19
 */
open class GetNotificationUpdateMapper @Inject constructor(){

    open fun map(pojo: NotificationCenterDetail) : NotificationViewData {
        val item = pojo.pojo
        val list = arrayListOf<NotificationItemViewBean>()
        for (notificationUpdateItem in item.list) {
            val datum = NotificationItemViewBean(
                    notificationId = notificationUpdateItem.notifId,
                    iconUrl = notificationUpdateItem.sectionIcon,
                    contentUrl = notificationUpdateItem.dataNotification.infoThumbnailUrl,
                    time = notificationUpdateItem.createTime,
                    title = notificationUpdateItem.title,
                    body = MethodChecker.fromHtml(notificationUpdateItem.shortDescription).toString(),
                    bodyHtml = notificationUpdateItem.shortDescriptionHtml,
                    sectionTitle = notificationUpdateItem.sectionKey,
                    templateKey = notificationUpdateItem.templateKey,
                    isRead = convertReadStatus(notificationUpdateItem.readStatus),
                    appLink = notificationUpdateItem.dataNotification.appLink,
                    label = notificationUpdateItem.typeOfUser,
                    hasShop = item.userInfo.hasShop(),
                    typeLink = notificationUpdateItem.typeLink,
                    totalProduct = notificationUpdateItem.totalProducts,
                    btnText = notificationUpdateItem.btnText,
                    products = notificationUpdateItem.productData,
                    dataNotification = notificationUpdateItem.dataNotification,
                    isLongerContent = notificationUpdateItem.isLongerContent,
                    isShowBottomSheet = notificationUpdateItem.isShowBottomSheet,
                    typeBottomSheet = notificationUpdateItem.typeBottomSheet,
                    options = item.options,
                    userInfo = item.userInfo
            )
            list.add(datum)
        }
        return NotificationViewData(item.paging, list, item.userInfo)
    }

    open fun map(pojo: NotificationCenterSingleDetail) : NotificationViewData {
        val item = pojo.pojo
        val list = arrayListOf<NotificationItemViewBean>()
        for (notificationUpdateItem in item.list) {
            val datum = NotificationItemViewBean(
                    notificationId = notificationUpdateItem.notifId,
                    iconUrl = notificationUpdateItem.sectionIcon,
                    contentUrl = notificationUpdateItem.dataNotification.infoThumbnailUrl,
                    time = notificationUpdateItem.createTime,
                    title = notificationUpdateItem.title,
                    body = MethodChecker.fromHtml(notificationUpdateItem.shortDescription).toString(),
                    bodyHtml = notificationUpdateItem.shortDescriptionHtml,
                    sectionTitle = notificationUpdateItem.sectionKey,
                    templateKey = notificationUpdateItem.templateKey,
                    isRead = convertReadStatus(notificationUpdateItem.readStatus),
                    appLink = notificationUpdateItem.dataNotification.appLink,
                    label = notificationUpdateItem.typeOfUser,
                    hasShop = item.userInfo.hasShop(),
                    typeLink = notificationUpdateItem.typeLink,
                    totalProduct = notificationUpdateItem.totalProducts,
                    btnText = notificationUpdateItem.btnText,
                    products = notificationUpdateItem.productData,
                    dataNotification = notificationUpdateItem.dataNotification,
                    isLongerContent = notificationUpdateItem.isLongerContent,
                    isShowBottomSheet = notificationUpdateItem.isShowBottomSheet,
                    typeBottomSheet = notificationUpdateItem.typeBottomSheet,
                    options = item.options,
                    userInfo = item.userInfo
            )
            list.add(datum)
        }
        return NotificationViewData(item.paging, list, item.userInfo)
    }

    private fun convertReadStatus(readStatus: Long): Boolean {
        return readStatus != 1L
    }
}