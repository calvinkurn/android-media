package com.tokopedia.brandlist.brandlist_category.presentation.fragment

import android.os.Bundle
import android.view.View
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.brandlist.BrandlistInstance
import com.tokopedia.brandlist.brandlist_category.di.BrandlistCategoryComponent
import com.tokopedia.brandlist.brandlist_category.di.BrandlistCategoryModule
import com.tokopedia.brandlist.brandlist_category.di.DaggerBrandlistCategoryComponent
import com.tokopedia.brandlist.brandlist_category.presentation.viewmodel.BrandlistCategoryViewModel
import javax.inject.Inject


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

    @Inject
    lateinit var viewModel: BrandlistCategoryViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getBrandlistCategories()
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