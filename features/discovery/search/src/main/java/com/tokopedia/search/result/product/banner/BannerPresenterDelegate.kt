package com.tokopedia.search.result.product.banner

import com.tokopedia.abstraction.base.view.adapter.Visitable
import timber.log.Timber
import javax.inject.Inject

class BannerPresenterDelegate @Inject constructor() {

    var bannerDataView: BannerDataView? = null

    fun isShowBanner() = bannerDataView?.imageUrl?.isNotEmpty() == true

    fun processBannerAtBottom(
        isLastPage: Boolean,
        list: MutableList<Visitable<*>>
    ) {
        if (!isLastPage) return

        bannerDataView?.let {
            list.add(it)
            bannerDataView = null
        }
    }

    fun processBannerAtTop(
        list: MutableList<Visitable<*>>,
        productList: List<Visitable<*>>,
    ) {
        bannerDataView?.let {
            list.add(list.indexOf(productList[0]), it)
            bannerDataView = null
        }
    }

    fun processBanner(
        isLastPage: Boolean,
        list: MutableList<Visitable<*>>,
        productList: List<Visitable<*>>,
    ) {
        try {
            if (!isShowBanner()) return
            val bannerDataView = bannerDataView ?: return

            when (bannerDataView.position) {
                LAST_POSITION -> processBannerAtBottom(isLastPage, list)
                FIRST_POSITION -> processBannerAtTop(list, productList)
                else -> processBannerAtPosition(list, productList)
            }
        } catch (throwable: Throwable) {
            Timber.w(throwable)
        }
    }

    private fun processBannerAtPosition(
        list: MutableList<Visitable<*>>,
        productList: List<Visitable<*>>,
    ) {
        val bannerDataView = bannerDataView ?: return
        if (productList.size < bannerDataView.position) return

        val productItemVisitableIndex = bannerDataView.position - 1
        val productItemVisitable = productList[productItemVisitableIndex]
        val bannerVisitableIndex = list.indexOf(productItemVisitable) + 1

        list.add(bannerVisitableIndex, bannerDataView)
        this.bannerDataView = null
    }

    companion object {
        const val LAST_POSITION = -1
        const val FIRST_POSITION = 0
    }
}