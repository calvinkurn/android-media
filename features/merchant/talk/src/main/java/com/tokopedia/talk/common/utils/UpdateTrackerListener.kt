package com.tokopedia.talk.common.utils

interface UpdateTrackerListener {
    fun updateSmartReplyTracker(finalSmartReplySwitchState: Boolean)
    fun updateTemplateListTracker(finalTemplateListSwitchState: Boolean)
}