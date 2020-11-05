package com.tokopedia.shop_settings.presenter.shopsettingsetalase

import com.tokopedia.shop.settings.etalase.view.listener.ShopSettingsEtalaseAddEditView

class ShopSettingsEtalaseAddEditImplViewTest : ShopSettingsEtalaseAddEditView {
    override fun onSuccesAddEdit(string: String?) {
        print("Hello")
    }

    override fun onErrorAddEdit(throwable: Throwable?) {
        print("Hello")
    }

    override fun onSuccessGetEtalaseList() {
        print("Hello")
    }

    override fun onErrorGetEtalaseList(throwable: Throwable?) {
        print("Hello")
    }

    override fun showLoading() {
        print("Hello")
    }

    override fun hideLoading() {
        print("Hello")
    }
}