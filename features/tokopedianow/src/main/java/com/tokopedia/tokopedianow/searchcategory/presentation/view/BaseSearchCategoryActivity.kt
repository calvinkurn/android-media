package com.tokopedia.tokopedianow.searchcategory.presentation.view

import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.tokopedianow.common.base.activity.BaseTokoNowActivity

abstract class BaseSearchCategoryActivity: BaseTokoNowActivity() {

    protected fun getBaseAppComponent(): BaseAppComponent =
            (applicationContext as BaseMainApplication).baseAppComponent
}