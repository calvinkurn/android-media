package com.tokopedia.tokomart.searchcategory.presentation.view

import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.tokomart.R

abstract class BaseSearchCategoryActivity: BaseSimpleActivity() {

    @LayoutRes
    override fun getLayoutRes() = R.layout.activity_tokomart_search_category

    @IdRes
    override fun getParentViewResourceID() = R.id.parent_view

    protected fun getBaseAppComponent(): BaseAppComponent =
            (applicationContext as BaseMainApplication).baseAppComponent
}