package com.tokopedia.talk.feature.inbox.analytics

import com.tokopedia.talk.common.analytics.TalkTrackingConstants

object TalkInboxTrackingConstants {
    const val STATUS_READ = "read"
    const val STATUS_UNREAD = "unread"
    const val SCREEN_NAME = "/inbox - talk"
    const val EVENT_ACTION_CLICK_TALK_MESSAGE = "${TalkTrackingConstants.EVENT_ACTION_CLICK} talk message"
    const val EVENT_LABEL_CLICK_TALK = "talk id:%s;product id:%s; filter active:%s; message status:%s; shop id:%s; count unread messages:%s;"
    const val FILTER_STATUS_ACTIVE = "active"
    const val FILTER_STATUS_INACTIVE = "inactive"
    const val EVENT_CATEGORY_INBOX = "inbox talk - %s"
    const val EVENT_LABEL_CLICK_FILTER = "filter value:%s; filter status:%s; shop id:%s; count unread messages:%s;"
    const val EVENT_ACTION_CLICK_FILTER = "${TalkTrackingConstants.EVENT_ACTION_CLICK} quick filter"
    const val EVENT_ACTION_LAZY_LOAD = "${TalkTrackingConstants.EVENT_ACTION_CLICK} lazy load on inbox talk"
    const val EVENT_LABEL_LAZY_LOAD = "count page load:%s; count unread messages:%s; count read messages:%s; shop id:%s;"
    const val FILTER_READ = "sudah dibaca"
    const val FILTER_UNREAD = "belum dibaca"
    const val FILTER_PROBLEM = "bermasalah"
    const val FILTER_UNRESPONDED = "belum direspon"
    const val FILTER_AUTOREPLIED = "dibalas otomatis"
    const val EVENT_ACTION_CLICK_TAB = "${TalkTrackingConstants.EVENT_ACTION_CLICK} %s tab"
    const val EVENT_LABEL_CLICK_TAB = "shop id:%s;count unread messages:%s;"
    const val TAB_SELLER = "seller"
    const val TAB_BUYER = "user"
    const val EVENT_PROMO_VIEW = "promoView"
    const val EVENT_ACTION_IMPRESS_ITEM = "view - talk on inbox talk"
    const val CREATIVE_MESSAGE_STATUS = "message status:%s"
    const val EE_NAME = "inbox-talk/%s"
    const val EVENT_ACTION_CLICK_SETTINGS = "${TalkTrackingConstants.EVENT_ACTION_CLICK} diskusi settings"
    const val EVENT_LABEL_CLICK_SELLER_FILTER = "filter value:%s; filter status:%s; shop id:%s; count unread messages:%s;"
}