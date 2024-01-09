package com.tokopedia.tokopedianow.annotation.presentation.activity

import androidx.fragment.app.Fragment
import com.tokopedia.tokopedianow.annotation.presentation.fragment.TokoNowAllAnnotationFragment
import com.tokopedia.tokopedianow.common.base.activity.BaseTokoNowActivity

class TokoNowAllAnnotationActivity: BaseTokoNowActivity() {
    companion object {
        const val KEY_CATEGORY_ID = "category_id"
        const val KEY_ANNOTATION_TYPE = "annotation_type"
    }

    override fun getFragment(): Fragment {
        var categoryId: String? = null
        var annotationType: String? = null

        intent.data?.apply {
            categoryId = getQueryParameter(KEY_CATEGORY_ID)
            annotationType = getQueryParameter(KEY_ANNOTATION_TYPE)
        }

        return TokoNowAllAnnotationFragment.newInstance(
            categoryId = categoryId,
            annotationType = annotationType
        )
    }
}
