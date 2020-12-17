package com.tokopedia.settingnotif.usersetting.view.dataview

import com.tokopedia.settingnotif.R
import com.tokopedia.settingnotif.usersetting.data.pojo.NotificationActivation
import com.tokopedia.settingnotif.usersetting.state.Email
import com.tokopedia.settingnotif.usersetting.state.NotificationItemState
import com.tokopedia.settingnotif.usersetting.state.Phone
import com.tokopedia.settingnotif.usersetting.state.PushNotif
import com.tokopedia.settingnotif.usersetting.state.NotificationItemState.Troubleshooter as Troubleshooter

object NotificationActivationDataView {

    fun activationPushNotif() = NotificationActivation(
            title = R.string.settingnotif_title_activation,
            description = R.string.settingnotif_desc_activation,
            action = R.string.settingnotif_activation,
            type = PushNotif
    )

    fun activationEmail() = NotificationActivation(
            title = R.string.settingnotif_title_email,
            description = R.string.settingnotif_desc_email,
            action = R.string.settingnotif_activation_email,
            type = Email
    )

    fun activationPhoneNumber() = NotificationActivation(
            title = R.string.settingnotif_title_sms,
            description = R.string.settingnotif_desc_sms,
            action = R.string.settingnotif_added_phone,
            type = Phone
    )

    fun activationTroubleshooter() = NotificationActivation(
            title = R.string.settingnotif_title_troubleshooter,
            description = R.string.settingnotif_desc_troubleshooter,
            type = Troubleshooter
    )

}