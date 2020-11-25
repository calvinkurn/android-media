package com.tokopedia.brandlist.brandlist_search.presentation.activity

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.brandlist.brandlist_category.data.model.Category
import com.tokopedia.brandlist.brandlist_category.presentation.fragment.BrandlistContainerFragment
import com.tokopedia.brandlist.brandlist_category.presentation.fragment.BrandlistContainerFragment.Companion.CATEGORY_INTENT
import com.tokopedia.brandlist.brandlist_page.presentation.fragment.BrandlistPageFragment
import com.tokopedia.brandlist.brandlist_page.presentation.fragment.BrandlistPageFragment.Companion.KEY_CATEGORY
import com.tokopedia.brandlist.brandlist_search.presentation.fragment.BrandlistSearchFragment

class BrandlistSearchActivity: BaseSimpleActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setupStatusbar()
    }

    override fun getNewFragment(): Fragment? {
        var categoryData: Category? = Category()
        val bundle = intent.extras

        if (bundle != null) {
            categoryData = bundle.getParcelable(CATEGORY_INTENT) ?: Category()
        }

        return BrandlistSearchFragment.createInstance(categoryData)
    }

    private fun setupStatusbar() {
        val window: Window = getWindow()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            getWindow().statusBarColor = resources.getColor(com.tokopedia.unifyprinciples.R.color.Unify_N0)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }

}