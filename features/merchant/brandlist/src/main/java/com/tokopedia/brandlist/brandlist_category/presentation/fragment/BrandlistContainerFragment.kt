package com.tokopedia.brandlist.brandlist_category.presentation.fragment

import android.os.Bundle
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment

class BrandlistContainerFragment : BaseDaggerFragment() {

    companion object {
        fun createInstance() = BrandlistContainerFragment()
//        fun createInstance(shopAddressViewModel: ShopLocationViewModel?, isAddNew: Boolean) =
//                ShopSettingAddressAddEditFragment().also { it.arguments = Bundle().apply {
//                    putParcelable(PARAM_EXTRA_SHOP_ADDRESS, shopAddressViewModel)
//                    putBoolean(PARAM_EXTRA_IS_ADD_NEW, isAddNew)
//                }}
    }

    override fun getScreenName(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun initInjector() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}