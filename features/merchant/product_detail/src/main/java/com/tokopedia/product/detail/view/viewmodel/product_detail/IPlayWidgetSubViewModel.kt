package com.tokopedia.product.detail.view.viewmodel.product_detail

import androidx.lifecycle.LiveData
import com.tokopedia.play.widget.ui.PlayWidgetState
import com.tokopedia.play.widget.ui.model.PlayWidgetReminderType
import com.tokopedia.usecase.coroutines.Result

/**
 * Created by yovi.putra on 24/03/23"
 * Project name: android-tokopedia-core
 **/

interface IPlayWidgetSubViewModel {

    val playWidgetModel: LiveData<Result<PlayWidgetState>>

    val playWidgetReminderSwitch: LiveData<Result<PlayWidgetReminderType>>

    fun getPlayWidgetData()

    fun updatePlayWidgetToggleReminder(
        playWidgetState: PlayWidgetState,
        channelId: String,
        reminderType: PlayWidgetReminderType
    )
}
