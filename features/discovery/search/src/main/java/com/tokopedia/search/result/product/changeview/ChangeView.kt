package com.tokopedia.search.result.product.changeview

import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.search.di.scope.SearchScope
import com.tokopedia.search.result.product.ScreenNameProvider
import com.tokopedia.search.result.product.ViewUpdater
import com.tokopedia.search.result.product.changeview.ViewType.LIST
import com.tokopedia.search.result.product.changeview.ViewType.BIG_GRID
import com.tokopedia.search.result.product.changeview.ViewType.SMALL_GRID
import com.tokopedia.search.utils.SmallGridSpanCount
import javax.inject.Inject

@SearchScope
class ChangeView @Inject constructor(
    private val staggeredGridLayoutManager: StaggeredGridLayoutManager,
    private val smallGridSpanCount: SmallGridSpanCount,
    private val viewUpdater: ViewUpdater,
    screenNameProvider: ScreenNameProvider
) : ChangeViewListener,
    ScreenNameProvider by screenNameProvider {

    override var viewType: ViewType = SMALL_GRID
        private set

    override fun onChangeViewClicked() {
        change()

        ChangeViewTracking.eventSearchResultChangeView(viewType.label, getScreenName())
    }

    fun change(value: Int? = null) {
        viewType = getNewViewType(value)
        staggeredGridLayoutManager.spanCount = getSpanCount()
        viewUpdater.requestRelayout()
    }

    private fun getNewViewType(value: Int?): ViewType =
        if (value == null) viewType.change()
        else ViewType.of(value)

    private fun getSpanCount(): Int =
        when (viewType) {
            LIST, BIG_GRID -> 1
            SMALL_GRID -> smallGridSpanCount()
        }
}
