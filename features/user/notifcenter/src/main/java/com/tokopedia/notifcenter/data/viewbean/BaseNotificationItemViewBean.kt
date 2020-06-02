package com.tokopedia.notifcenter.data.viewbean

import com.tokopedia.notifcenter.data.entity.DataNotification
import com.tokopedia.notifcenter.data.entity.NotificationOptions
import com.tokopedia.notifcenter.data.entity.ProductData
import com.tokopedia.notifcenter.data.entity.UserInfo

open class BaseNotificationItemViewBean(
        var indexId: Int = 0,
        open var notificationId: String = "",
        open var isRead: Boolean = false,
        open var iconUrl: String? = "",
        open var contentUrl: String = "",
        open var time: String = "",
        open var label: Int = 1,
        open var title: String = "",
        open var sectionTitle: String = "",
        open var body: String = "",
        open var bodyHtml: String = "",
        open var templateKey: String = "",
        open var appLink: String = "",
        open var hasShop: Boolean = false,
        open var typeLink: Int = 0,
        open var totalProduct: Int = 0,
        open var btnText: String = "",
        open var dataNotification: DataNotification = DataNotification(),
        open var products: List<ProductData> = emptyList(),
        open var isLongerContent: Boolean = false,
        open var isShowBottomSheet: Boolean = false,
        open var typeBottomSheet: Int = 0,
        open var options: NotificationOptions = NotificationOptions(),
        open var userInfo: UserInfo = UserInfo()
)