package com.tokopedia.feedcomponent.view.widget.listener

interface FeedCampaignListener {
    fun onTimerFinishUpcoming()
    fun onTimerFinishOngoing()
    fun setInitialStateOfReminderBtn(isReminderSet: Boolean, positionInFeed: Int)
    fun onReminderBtnClick(isReminderSet: Boolean, positionInFeed: Int)
}