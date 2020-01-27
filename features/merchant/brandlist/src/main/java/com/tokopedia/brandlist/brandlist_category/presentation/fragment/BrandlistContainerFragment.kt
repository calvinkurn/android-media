package com.tokopedia.brandlist.brandlist_category.presentation.fragment

import android.os.Bundle
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.brandlist.BrandlistInstance
import com.tokopedia.brandlist.brandlist_category.di.BrandlistCategoryComponent
import com.tokopedia.brandlist.brandlist_category.di.BrandlistCategoryModule
import com.tokopedia.brandlist.brandlist_category.di.DaggerBrandlistCategoryComponent



class BrandlistContainerFragment : BaseDaggerFragment(),
        HasComponent<BrandlistCategoryComponent> {

    companion object {
        fun createInstance() = BrandlistContainerFragment()
//        fun createInstance(shopAddressViewModel: ShopLocationViewModel?, isAddNew: Boolean) =
//                ShopSettingAddressAddEditFragment().also { it.arguments = Bundle().apply {
//                    putParcelable(PARAM_EXTRA_SHOP_ADDRESS, shopAddressViewModel)
//                    putBoolean(PARAM_EXTRA_IS_ADD_NEW, isAddNew)
//                }}
    }

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        component?.inject(this)
    }

    override fun getComponent(): BrandlistCategoryComponent? {
        return activity?.run {
            DaggerBrandlistCategoryComponent
                    .builder()
                    .brandlistCategoryModule(BrandlistCategoryModule())
                    .brandlistComponent(BrandlistInstance.getComponent(application))
                    .build()
        }
    }
}