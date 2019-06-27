package com.tokopedia.notifications.model

import org.json.JSONObject
import kotlin.collections.ArrayList

/**
 * Created by Ashwani Tyagi on 18/10/18.
 */
class BaseNotificationModel {

    var notificationId: Int = 0
    var campaignId: Long = 0
    var priorityPreOreo: Int = 2
    var title: String? = null
    var detailMessage: String? = null
    var message: String? = null
    var icon: String? = null
    var soundFileName: String? = null

    var tribeKey: String? = null

    var media: Media? = null

    var appLink: String? = null
    var actionButton: List<ActionButton> = ArrayList()
    var customValues: JSONObject? = null
    var type: String? = null

    var channelName: String? = null

    var persistentButtonList: List<PersistentButton>? = null

    var videoPushModel: JSONObject? = null

    var carouselList: List<Carousel> = ArrayList()

    var gridList: List<Grid> = ArrayList()

    var productInfo : ProductInfo? = null

    var subText: String? = null

    var visualCollapsedImageUrl: String? = null
    var visualExpandedImageUrl: String? = null

    var carouselIndex: Int = 0
    var isVibration: Boolean = true
    var isSound: Boolean = true
    var isUpdateExisting: Boolean = false
}
