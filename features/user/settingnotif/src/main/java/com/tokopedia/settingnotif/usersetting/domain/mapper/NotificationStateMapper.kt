package com.tokopedia.settingnotif.usersetting.domain.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.settingnotif.usersetting.domain.pojo.SettingSections
import com.tokopedia.settingnotif.usersetting.view.adapter.factory.SettingFieldTypeFactory
import com.tokopedia.settingnotif.usersetting.view.dataview.NotificationStateDataView

object NotificationStateMapper {

    fun mapToViewState(
            outputData: List<Visitable<SettingFieldTypeFactory>>
    ): ArrayList<NotificationStateDataView> {
        val dataView = arrayListOf<NotificationStateDataView>()
        outputData.forEach {
            if (it is SettingSections) {
                it.listSettings.map { parent ->
                    NotificationStateDataView().apply {
                        this.name = parent?.name?: ""
                        this.icon = parent?.icon?: ""
                        this.key = parent?.key?: ""
                        this.status = parent?.status?: false
                    }
                }.map { data ->
                    dataView.add(data)
                }.toList()
            }
        }
        return dataView
    }

}