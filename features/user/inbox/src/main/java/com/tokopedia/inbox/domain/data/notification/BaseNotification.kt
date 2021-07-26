package com.tokopedia.inbox.domain.data.notification

import com.google.gson.annotations.SerializedName
import com.tokopedia.inbox.common.InboxFragmentType

abstract class BaseNotification {

    @SerializedName("chat_int")
    var chatInt: Int = 0
        set(value) {
            field = value
            if (field < 0) {
                field = 0
            }
            updateTotal()
        }

    @SerializedName("notifcenter_int")
    var notifcenterInt: Int = 0
        set(value) {
            field = value
            updateTotal()
        }

    @SerializedName("talk_int")
    var talkInt: Int = 0
        set(value) {
            field = value
            if (field < 0) {
                field = 0
            }
            updateTotal()
        }

    @SerializedName("review_int")
    var reviewInt: Int = 0
        set(value) {
            field = value
            if (field < 0) {
                field = 0
            }
            updateTotal()
        }

    @SerializedName("total_int")
    var totalInt: Int = 0

    @Transient
    @InboxFragmentType
    private var page: Int = InboxFragmentType.NONE
    @Transient
    private var isShowBottomNav: Boolean = true

    fun setCounterAdjuster(page: Int, isShowBottomNav: Boolean) {
        this.page = page
        this.isShowBottomNav = isShowBottomNav
        updateTotal()
    }

    private fun updateTotal() {
        totalInt = 0
        if (eligibleToOpen(InboxFragmentType.CHAT)) {
            totalInt += chatInt
        }
        if (eligibleToOpen(InboxFragmentType.NOTIFICATION)) {
            totalInt += notifcenterInt
        }
        if (eligibleToOpen(InboxFragmentType.DISCUSSION)) {
            totalInt += talkInt
        }
        if (eligibleToOpen(InboxFragmentType.REVIEW)) {
            totalInt += reviewInt
        }
    }

    private fun eligibleToOpen(
        @InboxFragmentType
        pageToOpen: Int
    ): Boolean {
        return isShowBottomNav || page == pageToOpen
    }
}