package com.tokopedia.centralized_promo.view.fragment.partialview

import android.view.View
import android.view.ViewTreeObserver
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.centralized_promo.view.adapter.CentralizedPromoAdapter
import com.tokopedia.centralized_promo.view.fragment.CoachMarkListener
import com.tokopedia.centralized_promo.view.model.BaseUiModel

abstract class PartialView<T : BaseUiModel, F : BaseAdapterTypeFactory, V : Visitable<F>>(typeFactory: F, protected var coachMarkListener: CoachMarkListener) {
    var isReadyToShowCoachMark: Boolean = false
    abstract var shouldShowCoachMark: Boolean
    protected val adapter by lazy {
        CentralizedPromoAdapter<V, F>(typeFactory)
    }

    abstract fun renderData(data: T)
    abstract fun renderError(cause: Throwable)
    abstract fun onRefresh()

    inline fun <T: View> T.afterMeasured(crossinline f: T.() -> Unit) {
        viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                if (measuredWidth > 0 && measuredHeight > 0) {
                    viewTreeObserver.removeOnGlobalLayoutListener(this)
                    f()
                }
            }
        })
    }
}