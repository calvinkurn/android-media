package com.tokopedia.recommendation_widget_common.viewutil

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.tokopedia.recommendation_widget_common.presenter.RecomWidgetViewModel
import com.tokopedia.recommendation_widget_common.viewutil.RecomPageConstant.PAGENAME_K2K_PDP
import com.tokopedia.recommendation_widget_common.viewutil.RecomPageConstant.PAGENAME_PDP_3
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

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

fun String.isRecomPageNameEligibleForChips(): Boolean {
    return getRecomPageNameEligibleForChips().contains(this)
}

fun getRecomPageNameEligibleForChips(): Set<String> {
    return setOf(
        PAGENAME_PDP_3,
        PAGENAME_K2K_PDP
    )
}

internal fun LifecycleOwner.launchRepeatOnStarted(action: suspend CoroutineScope.() -> Unit): Job {
    return lifecycleScope.launch {
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            this.action()
        }
    }
}
