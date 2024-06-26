package com.tokopedia.tokopedianow.category.presentation.activity

import android.net.Uri
import androidx.fragment.app.Fragment
import com.tokopedia.discovery.common.utils.URLParser
import com.tokopedia.tokopedianow.common.base.activity.BaseTokoNowActivity
import com.tokopedia.tokopedianow.category.presentation.constant.CategoryParamConstant.PARAM_CATEGORY_L1
import com.tokopedia.tokopedianow.category.presentation.constant.CategoryParamConstant.PARAM_CATEGORY_L2
import com.tokopedia.tokopedianow.category.presentation.fragment.TokoNowCategoryL2Fragment

class TokoNowCategoryL2Activity: BaseTokoNowActivity() {

    override fun getFragment(): Fragment {
        val uri = intent.data
        val uriString = uri.toString()
        val categoryL1 = uri.getCategoryL1()
        val categoryL2 = uri.getCategoryL2()
        val queryParamMap = URLParser(uri.toString()).paramKeyValueMapDecoded
        queryParamMap.removeCategoryEntries()

        return TokoNowCategoryL2Fragment.newInstance(categoryL1, categoryL2, uriString, queryParamMap)
    }

    private fun Uri?.getCategoryL1(): String = this?.getQueryParameter(PARAM_CATEGORY_L1).orEmpty()
    private fun Uri?.getCategoryL2(): String = this?.getQueryParameter(PARAM_CATEGORY_L2).orEmpty()

    private fun MutableMap<String, String>.removeCategoryEntries(): Map<String, String> {
        remove(PARAM_CATEGORY_L1)
        remove(PARAM_CATEGORY_L2)
        return this
    }
}
