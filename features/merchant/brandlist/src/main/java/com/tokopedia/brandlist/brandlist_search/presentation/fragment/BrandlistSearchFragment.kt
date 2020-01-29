package com.tokopedia.brandlist.brandlist_search.presentation.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.brandlist.BrandlistInstance
import com.tokopedia.brandlist.brandlist_search.di.BrandlistSearchComponent
import com.tokopedia.brandlist.brandlist_search.di.BrandlistSearchModule
import com.tokopedia.brandlist.brandlist_search.di.DaggerBrandlistSearchComponent

class BrandlistSearchFragment: BaseDaggerFragment(),
        HasComponent<BrandlistSearchComponent> {

    companion object {
        @JvmStatic
        fun createInstance(): Fragment {
            return BrandlistSearchFragment().apply {
                arguments = Bundle().apply {

                }
            }
        }
    }

    override fun getComponent(): BrandlistSearchComponent? {
        return activity?.run {
            DaggerBrandlistSearchComponent
                    .builder()
                    .brandlistSearchModule(BrandlistSearchModule())
                    .brandlistComponent(BrandlistInstance.getComponent(application))
                    .build()
        }
    }

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        component?.inject(this)
    }

}