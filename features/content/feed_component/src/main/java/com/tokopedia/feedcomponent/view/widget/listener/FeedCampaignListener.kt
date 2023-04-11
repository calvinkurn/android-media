package com.tokopedia.feedcomponent.view.widget.listener

interface FeedCampaignListener {
    fun onTimerFinishUpcoming()
    fun onTimerFinishOngoing()
    fun onReminderBtnClick(isReminderSet: Boolean, positionInFeed: Int)
}