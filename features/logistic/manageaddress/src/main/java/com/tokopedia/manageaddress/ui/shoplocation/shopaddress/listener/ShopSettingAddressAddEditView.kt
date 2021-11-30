package com.tokopedia.manageaddress.ui.shoplocation.shopaddress.listener

import com.tokopedia.abstraction.base.view.listener.CustomerView

interface ShopSettingAddressAddEditView: CustomerView {
    fun onSuccesAddEdit(string: String?)
    fun onErrorAddEdit(throwable: Throwable?)
}