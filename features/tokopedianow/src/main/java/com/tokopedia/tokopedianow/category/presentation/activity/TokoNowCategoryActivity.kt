package com.tokopedia.tokopedianow.category.presentation.activity

import android.net.Uri
import androidx.fragment.app.Fragment
import com.tokopedia.discovery.common.utils.URLParser
import com.tokopedia.tokopedianow.category.presentation.constant.CategoryParamConstant.PARAM_CATEGORY_L1
import com.tokopedia.tokopedianow.category.presentation.fragment.TokoNowCategoryFragment
import com.tokopedia.tokopedianow.common.base.activity.BaseTokoNowActivity

class TokoNowCategoryActivity: BaseTokoNowActivity() {

    override fun getFragment(): Fragment {
        val uri = intent.data
        val categoryL1 = uri.getCategoryL1()
        val queryParamMap = URLParser(uri.toString()).paramKeyValueMapDecoded
        queryParamMap.removeCategoryEntries()

        return TokoNowCategoryFragment.newInstance(categoryL1, queryParamMap)
    }

    private fun Uri?.getCategoryL1(): String = this?.getQueryParameter(PARAM_CATEGORY_L1).orEmpty()

    private fun MutableMap<String, String>.removeCategoryEntries(): Map<String, String> {
        remove(PARAM_CATEGORY_L1)
        return this
    }
}
