package com.tokopedia.shop.settings.etalase.view.listener

import com.tokopedia.abstraction.base.view.listener.CustomerView

interface ShopSettingsEtalaseAddEditView: CustomerView {
    fun onSuccesAddEdit(string: String?)
    fun onErrorAddEdit(throwable: Throwable?)
    fun onSuccessGetEtalaseList()
    fun onErrorGetEtalaseList(throwable: Throwable?)
    fun showLoading()
    fun hideLoading()
}