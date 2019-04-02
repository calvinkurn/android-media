package com.tokopedia.shop.settings.etalase.view.listener

import com.tokopedia.abstraction.base.view.listener.CustomerView

interface ShopSettingsEtalaseAddEditView: CustomerView {
    fun onSuccesAddEdit(string: String?)
    fun onErrorAddEdit(throwable: Throwable?)
}