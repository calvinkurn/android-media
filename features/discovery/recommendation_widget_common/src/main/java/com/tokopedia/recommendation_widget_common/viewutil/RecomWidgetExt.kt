package com.tokopedia.recommendation_widget_common.viewutil

import android.view.View
import androidx.fragment.app.Fragment
import com.tokopedia.recommendation_widget_common.presenter.RecomWidgetViewModel

/**
 * Created by yfsx on 13/10/21.
 */
fun Fragment.initRecomWidgetViewModel() =
    RecomWidgetViewModelDelegate<RecomWidgetViewModel> { this.context?.getActivityFromContext() }

fun View.initRecomWidgetViewModel() =
    RecomWidgetViewModelDelegate<RecomWidgetViewModel> { this.context.getActivityFromContext() }

fun RecomWidgetViewModel.updateRecomWidgetQtyItemWithMiniCart(shopId: String) {
    this.getMiniCart(
        shopId = shopId,
        pageName = ""
    )
}
