package com.tokopedia.notifcenter.view.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.notifcenter.view.adapter.typefactory.NotifCenterTypeFactory

data class NotifItemViewModel(
        val notifId: String = "",
        val title: String = "",
        val image: String = "",
        val time: String = "",
        val timeSummary: String = "",
        val section: String = "",
        val redirectLink: String = "",
        val readStatus: Int = 0,
        val userId: Int = 0,
        val shopId: Int = 0,
        var showTimeSummary: Boolean = true,
        var templateKey: String = "")
    : Visitable<NotifCenterTypeFactory> {

    companion object {
        val READ_STATUS_FALSE = 1
        val READ_STATUS_TRUE = 2
    }

    override fun type(typeFactory: NotifCenterTypeFactory?) : Int {
        return typeFactory?.type(this) ?: 0
    }
}