package com.tokopedia.payment.setting.list.view.listener

import com.tokopedia.payment.setting.list.model.SettingBannerModel

interface SettingListActionListener {
    fun onClickAddCard()
    fun onViewBanner(element: SettingBannerModel)
    fun onPaymentListImpressed()
}
