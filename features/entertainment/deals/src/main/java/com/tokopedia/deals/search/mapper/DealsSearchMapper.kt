package com.tokopedia.deals.search.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.deals.common.model.response.EventSearch
import com.tokopedia.deals.common.model.response.EventProductDetail
import com.tokopedia.deals.search.model.response.*
import com.tokopedia.deals.search.model.visitor.*
import com.tokopedia.deals.search.ui.typefactory.DealsSearchTypeFactory

object DealsSearchMapper {

    private const val LAST_SEEN_TITLE = "TERAKHIR DILIHAT"
    private const val CURATED_COLLECTION_TITLE = "KOLEKSI MENARIK"
    private const val VOUCHER_LOCATION_TITLE = "VOUCHER POPULER DI"
    private const val BRAND_TITLE = "PILIHAN BRAND"
    private const val VOUCHER_TITLE = "PILIHAN VOUCHER"

    private const val NOT_FOUND_TITLE = "Yaah, voucher yang kamu cari nggak ketemu"
    private const val NOT_FOUND_DESC = "Coba cari lagi pakai kata kunci lain, yuk!"

    private const val ITEMS_COUNT_PER_PAGE = 20
    const val MAX_NUM_BRAND = 4
    private const val MAX_NUM_LAST_SEEN = 3
    const val MAX_NUM_CURATED = 8
    var initialProductList = listOf<EventProductDetail>()

    var voucherList = arrayListOf<VoucherModel>()

    fun displayInitialDataSearch(
            dataList: InitialLoadData,
            location: String
    ): ArrayList<Visitable<DealsSearchTypeFactory>> {
        val list: ArrayList<Visitable<DealsSearchTypeFactory>> = arrayListOf()

        if(dataList.travelCollectiveRecentSearches.items.isNotEmpty()) {
            val lastSeenTitle = SectionTitleModel(LAST_SEEN_TITLE)
            list.add(lastSeenTitle)

            val recent = RecentModel()
            recent.items = dataList.travelCollectiveRecentSearches.items
            if(recent.items.size > MAX_NUM_LAST_SEEN) {
                recent.items = recent.items.subList(0, MAX_NUM_LAST_SEEN)
            }
            list.add(recent)
        }

        if(dataList.eventChildCategory.categories.isNotEmpty()) {
            val collectionTitle = SectionTitleModel(CURATED_COLLECTION_TITLE)
            list.add(collectionTitle)

            val curated = CuratedModel()
            curated.categories = dataList.eventChildCategory.categories
            list.add(curated)
        }

        val voucherTitle = SectionTitleModel("$VOUCHER_LOCATION_TITLE ${location.toUpperCase()}")
        list.add(voucherTitle)

        voucherList.clear()
        val productList = dataList.eventSearch.products
        for (i in productList.indices) {
            list.add(createVoucher(productList[i], i, 0))
        }

        return list
    }

    fun displayMoreData (
            dataList: EventSearch,
            page: Int
    ): List<Visitable<DealsSearchTypeFactory>> {
        val list: ArrayList<Visitable<DealsSearchTypeFactory>> = arrayListOf()

        voucherList.clear()
        val productList = dataList.products
        for (i in productList.indices) {
            list.add(createVoucher(productList[i], i, page))
        }

        return list
    }

    fun displayDataSearchResult(
            dataList: EventSearch,
            location: String,
            keyword: String
    ): List<Visitable<DealsSearchTypeFactory>> {
        val list: ArrayList<Visitable<DealsSearchTypeFactory>> = arrayListOf()

        if(dataList.products.isEmpty() && dataList.brands.isEmpty()) {
            val notFoundModel = NotFoundModel()
            notFoundModel.title = NOT_FOUND_TITLE
            notFoundModel.desc = NOT_FOUND_DESC
            list.add(notFoundModel)

            val voucherTitle = SectionTitleModel("$VOUCHER_LOCATION_TITLE ${location.toUpperCase()}")
            list.add(voucherTitle)

            voucherList.clear()
            val productList = initialProductList
            for (i in productList.indices) {
                list.add(createVoucher(productList[i], i, 0))
            }
        } else {
            if(dataList.brands.isNotEmpty()) {
                val brandTitle = SectionTitleModel(BRAND_TITLE)
                list.add(brandTitle)

                val merchantList = MerchantModelModel()
                merchantList.merchantList = dataList.brands
                list.add(merchantList)

                if(merchantList.merchantList.size > MAX_NUM_BRAND) {
                    val moreBrand = MoreBrandModel()
                    moreBrand.searchQuery = keyword
                    moreBrand.size = merchantList.merchantList.size
                    moreBrand.brandList = dataList.brands
                    list.add(moreBrand)
                }
            }

            if(dataList.products.isNotEmpty()) {
                val voucherTitle = SectionTitleModel(VOUCHER_TITLE)
                list.add(voucherTitle)

                voucherList.clear()
                val productList = dataList.products
                for (i in productList.indices) {
                    list.add(createVoucher(productList[i], i, 0))
                }
            }
        }

        return list
    }

    private fun createVoucher(product: EventProductDetail, position: Int, page: Int): VoucherModel {
        val voucher = VoucherModel()
        voucher.voucherId = product.id
        voucher.voucherName = product.displayName
        voucher.merchantName = product.brand.title
        voucher.voucherThumbnail = product.thumbnailApp
        voucher.realPrice = product.salesPrice
        voucher.mrp = product.mrp
        voucher.discountText = product.savingPercentage
        voucher.appUrl = product.appUrl
        voucher.position = position + (ITEMS_COUNT_PER_PAGE * page)
        voucherList.add(voucher)
        return voucher
    }

}