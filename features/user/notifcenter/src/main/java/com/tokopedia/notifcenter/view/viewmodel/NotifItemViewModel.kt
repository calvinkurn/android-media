package com.tokopedia.notifcenter.view.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.notifcenter.domain.pojo.NotifCenterPojo

data class NotifItemViewModel(
        val title: String = "",
        val image: String = "",
        val time: String = "",
        val timeSummary: String = "",
        val section: String = "",
        val redirectLink: String = "",
        val status: Int = 0,
        val userId: Int = 0,
        val shopId: Int = 0)
    : Visitable<NotifCenterPojo> {
    override fun type(typeFactory: NotifCenterPojo?): Int {
        return 0
    }
}