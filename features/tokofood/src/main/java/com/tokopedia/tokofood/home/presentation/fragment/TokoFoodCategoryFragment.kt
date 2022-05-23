package com.tokopedia.tokofood.home.presentation.fragment

import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseMultiFragActivity
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.fragment.IBaseMultiFragment
import com.tokopedia.tokofood.databinding.FragmentTokofoodCategoryBinding
import com.tokopedia.tokofood.home.di.DaggerTokoFoodHomeComponent
import com.tokopedia.utils.lifecycle.autoClearedNullable

class TokoFoodCategoryFragment: BaseDaggerFragment(),
    IBaseMultiFragment {

    private var binding by autoClearedNullable<FragmentTokofoodCategoryBinding>()

    override fun getFragmentTitle(): String? {
        return null
    }

    override fun getFragmentToolbar(): Toolbar? {
        return null
    }

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        activity?.let {
            DaggerTokoFoodHomeComponent
                .builder()
                .baseAppComponent((it.applicationContext as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)
        }
    }

    override fun navigateToNewFragment(fragment: Fragment) {
        (activity as? BaseMultiFragActivity)?.navigateToNewFragment(fragment)
    }


}