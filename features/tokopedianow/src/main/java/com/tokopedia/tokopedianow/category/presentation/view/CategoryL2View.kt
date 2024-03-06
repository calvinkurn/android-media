package com.tokopedia.tokopedianow.category.presentation.view

import androidx.fragment.app.Fragment
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.tokopedianow.category.domain.model.CategorySharingModel

interface CategoryL2View {
    fun updateMiniCartWidget(data: MiniCartSimplifiedData? = null)
    fun updateToolbarNotificationCounter()
    fun showFragment(fragment: Fragment)
    fun setShareModel(model: CategorySharingModel)
}
