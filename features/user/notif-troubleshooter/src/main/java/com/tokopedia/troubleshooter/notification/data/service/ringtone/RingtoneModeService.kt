package com.tokopedia.troubleshooter.notification.data.service.ringtone

import com.tokopedia.troubleshooter.notification.ui.state.RingtoneState

interface RingtoneModeService {
    fun isRing(): RingtoneState
}