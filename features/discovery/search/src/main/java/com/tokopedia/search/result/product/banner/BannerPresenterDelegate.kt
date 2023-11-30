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

    fun processBannerAtBottom(action: (Int,Visitable<*>) -> Unit) {
        if (!pagination.isLastPage()) return

        bannerDataView?.let { banner ->
            action(banner.position, banner)
            bannerDataView = null
        }
    }

    fun processBannerAtTop(action: (Int, Visitable<*>) -> Unit) {
        bannerDataView?.let { banner ->
            action(banner.position, banner)
            bannerDataView = null
        }
    }

    fun processBanner(
        totalProductItem: Int,
        action: (Int, Visitable<*>) -> Unit,
    ) {
        try {
            if (!isShowBanner()) return
            val bannerDataView = bannerDataView ?: return

            when (bannerDataView.position) {
                LAST_POSITION -> processBannerAtBottom(action)
                FIRST_POSITION -> processBannerAtTop(action)
                else -> processBannerAtPosition(totalProductItem, action)
            }
        } catch (throwable: Throwable) {
            Timber.w(throwable)
        }
    }

    private fun processBannerAtPosition(totalProductItem: Int, action: (Int, Visitable<*>) -> Unit) {
        val bannerDataView = bannerDataView ?: return
        if (totalProductItem < bannerDataView.position) return

        action(bannerDataView.position, bannerDataView)
        this.bannerDataView = null
    }

    companion object {
        const val LAST_POSITION = -1
        const val FIRST_POSITION = 0
    }
}
