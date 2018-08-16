package com.tokopedia.shop.settings.notes.view.listener

import com.tokopedia.abstraction.base.view.listener.CustomerView

interface ShopSettingsNotesAddEditView: CustomerView {
    fun onSuccesAddEdit(string: String?)
    fun onErrorAddEdit(throwable: Throwable?)
}