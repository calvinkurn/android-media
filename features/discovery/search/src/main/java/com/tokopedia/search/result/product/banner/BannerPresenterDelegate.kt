package com.tokopedia.search.result.product.banner

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.search.di.scope.SearchScope
import com.tokopedia.search.result.product.pagination.Pagination
import timber.log.Timber
import javax.inject.Inject

@SearchScope
class BannerPresenterDelegate @Inject constructor(
    private val pagination: Pagination,
) {

    private var bannerDataView: BannerDataView? = null

    private val bannerPosition : Int?
        get() = bannerDataView?.position

    val isLastPositionBanner: Boolean
        get() = LAST_POSITION == bannerPosition

    val isFirstPositionBanner: Boolean
        get() = FIRST_POSITION == bannerPosition

    fun isShowBanner() = bannerDataView?.imageUrl?.isNotEmpty() == true

    fun setBannerData(data: BannerDataView?) {
        this.bannerDataView = data
    }

    fun processBannerAtBottom(
        list: List<Visitable<*>>,
        action: (Int,Visitable<*>) -> Unit,
    ) {
        if (!pagination.isLastPage()) return

        bannerDataView?.let {
            action(list.size, it)
            bannerDataView = null
        }
    }

    fun processBannerAtTop(
        list: List<Visitable<*>>,
        productList: List<Visitable<*>>,
        action: (Int, Visitable<*>) -> Unit,
    ) {
        bannerDataView?.let { banner ->
            val index = list.indexOf(productList.getOrNull(0))
            action(index, banner)
            bannerDataView = null
        }
    }

    fun processBanner(
        list: List<Visitable<*>>,
        productList: List<Visitable<*>>,
        action: (Int, Visitable<*>) -> Unit,
    ) {
        try {
            if (!isShowBanner()) return
            val bannerDataView = bannerDataView ?: return

            when (bannerDataView.position) {
                LAST_POSITION -> processBannerAtBottom(list, action)
                FIRST_POSITION -> processBannerAtTop(list, productList, action)
                else -> processBannerAtPosition(list, productList, action)
            }
        } catch (throwable: Throwable) {
            Timber.w(throwable)
        }
    }

    private fun processBannerAtPosition(
        list: List<Visitable<*>>,
        productList: List<Visitable<*>>,
        action: (Int, Visitable<*>) -> Unit,
    ) {
        val bannerDataView = bannerDataView ?: return
        if (productList.size < bannerDataView.position) return

        val productItemVisitableIndex = bannerDataView.position - 1
        val productItemVisitable = productList[productItemVisitableIndex]
        val bannerVisitableIndex = list.indexOf(productItemVisitable) + 1

        action(bannerVisitableIndex, bannerDataView)
        this.bannerDataView = null
    }

    companion object {
        const val LAST_POSITION = -1
        const val FIRST_POSITION = 0
    }
}
