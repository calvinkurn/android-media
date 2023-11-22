package com.tokopedia.search.result.product.inspirationwidget

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.search.di.scope.SearchScope
import timber.log.Timber
import javax.inject.Inject

@SearchScope
class InspirationWidgetPresenterDelegate @Inject constructor() {

    private var inspirationWidgetVisitable = mutableListOf<InspirationWidgetVisitable>()

    fun setInspirationWidgetDataViewList(
        inspirationWidgetDataViewList: List<InspirationWidgetVisitable>,
    ) {
        this.inspirationWidgetVisitable = inspirationWidgetDataViewList.toMutableList()
    }

    fun processInspirationWidgetPosition(
        totalProductItem: Int,
        action: (Int, InspirationWidgetVisitable) -> Unit,
    ) {
        if (inspirationWidgetVisitable.isEmpty()) return

        val inspirationWidgetVisitableIterator = inspirationWidgetVisitable.iterator()
        while (inspirationWidgetVisitableIterator.hasNext()) {
            val data = inspirationWidgetVisitableIterator.next()

            if (data.data.position < 0) {
                inspirationWidgetVisitableIterator.remove()
                continue
            }

            val widgetPosition = data.data.position
            if (widgetPosition <= totalProductItem) {
                try {
                    action(widgetPosition, data)

                    inspirationWidgetVisitableIterator.remove()
                } catch (exception: Throwable) {
                    Timber.w(exception)
                }
            }
        }
    }
}
