package com.tokopedia.brandlist.brandlist_category.presentation.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.brandlist.BrandlistInstance
import com.tokopedia.brandlist.brandlist_category.di.BrandlistCategoryComponent
import com.tokopedia.brandlist.brandlist_category.di.BrandlistCategoryModule
import com.tokopedia.brandlist.brandlist_category.di.DaggerBrandlistCategoryComponent
import com.tokopedia.brandlist.brandlist_category.presentation.activity.BrandlistActivity.Companion.CATEGORY_EXTRA_APPLINK
import com.tokopedia.brandlist.brandlist_category.presentation.viewmodel.BrandlistCategoryViewModel
import com.tokopedia.searchbar.MainToolbar
import javax.inject.Inject


class BrandlistContainerFragment : BaseDaggerFragment(),
        HasComponent<BrandlistCategoryComponent> {

    companion object {
//        const val CATEGORY_EXTRA_APPLINK = "category"

        @JvmStatic
//        fun createInstance(category: String): Fragment {
//            return BrandlistContainerFragment().apply {
//                arguments = Bundle().apply {
//                    putString(CATEGORY_EXTRA_APPLINK, category)
//                }
//            }
//        }
        fun createInstance() = BrandlistContainerFragment()
//        fun createInstance(shopAddressViewModel: ShopLocationViewModel?, isAddNew: Boolean) =
//                ShopSettingAddressAddEditFragment().also { it.arguments = Bundle().apply {
//                    putParcelable(PARAM_EXTRA_SHOP_ADDRESS, shopAddressViewModel)
//                    putBoolean(PARAM_EXTRA_IS_ADD_NEW, isAddNew)
//                }}
    }

    @Inject
    lateinit var viewModel: BrandlistCategoryViewModel

    private var statusBar: View? = null
    private var mainToolbar: MainToolbar? = null
    private var categorySlug = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        context?.let {  }
        categorySlug = arguments?.getString(CATEGORY_EXTRA_APPLINK) ?: ""
        println(categorySlug)
        super.onCreate(savedInstanceState)
    }

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