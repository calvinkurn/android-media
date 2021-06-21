package com.tokopedia.tokomart.searchcategory.presentation.view

import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.abstraction.common.di.component.BaseAppComponent

abstract class BaseSearchCategoryActivity: BaseActivity() {

    protected fun getBaseAppComponent(): BaseAppComponent =
            (applicationContext as BaseMainApplication).baseAppComponent
}