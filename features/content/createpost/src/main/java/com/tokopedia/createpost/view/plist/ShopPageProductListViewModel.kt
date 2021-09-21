package com.tokopedia.createpost.view.plist

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.isZero
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class ShopPageProductListViewModel @Inject constructor() : BaseViewModel(Dispatchers.Main) {

    val productList = MutableLiveData<Resources<GetShopProduct>>()
    val pageData = MutableLiveData<Resources<GetShopProduct>>()
    val sortLiveData = MutableLiveData<Resources<ShopPlIstSortingListBase>>()
    val newSortValeLiveData = MutableLiveData<ShopPagePListSortItem>()
    val newProductValLiveData = MutableLiveData<ShopPageProduct>()
    val showBs = MutableLiveData<Boolean>()
    private val gql = MultiRequestGraphqlUseCase()
    private val gqlSort = MultiRequestGraphqlUseCase()
    private var shopId: String? = "-1"
    private var sort: Int? = 0
    private var sortPosition: Int? = 0
    private var soruce: String? = "shop_product"

    fun getPageData(shopId: String?, source: String?, sort: String = "") {
        this.shopId = shopId
        this.soruce = source
        productList.value = Loading()
        getList(1)
    }

    fun setNewSortValue(value: ShopPagePListSortItem, position: Int) {
        newSortValeLiveData.value = value
        sort = value.value
        showBs.value = true
        sortPosition = position
    }


    fun setNewProductValue(value: ShopPageProduct) {
        newProductValLiveData.value = value
    }

    fun getList(pageNumber: Int) {
        launchCatchError(block = {
            val variablesMain = java.util.HashMap<String, Any>()
            variablesMain["shopId"] = shopId!!
            variablesMain["pageNumber"] = pageNumber
            variablesMain["perPage"] = 10

            if (!sort.isZero()) {
                variablesMain["sort"] = sort!!.toInt()
            }

            val request = GraphqlRequest(
                getQ(),
                GetShopProduct::class.java,
                variablesMain,
                false
            )
            gql.clearRequest()
            gql.addRequest(request)
            val response =
                gql.executeOnBackground().getData<GetShopProduct>(GetShopProduct::class.java)
            productList.value = Success(response)
        }) {
            productList.value = ErrorMessage(it.toString())
        }
    }
    fun getSortPosition() :Int?{
        return sortPosition
    }

    fun getSortData() {
        launchCatchError(block = {
            val variablesMain = java.util.HashMap<String, Any>()
            variablesMain["shopId"] = shopId!!
            variablesMain["query"] = ""
            variablesMain["status"] = 1
            variablesMain["device"] = "android"
            variablesMain["source"] = "shop_product"
            val request = GraphqlRequest(
                getQSort(),
                ShopPlIstSortingListBase::class.java,
                variablesMain,
                false
            )
            gqlSort.clearRequest()
            gqlSort.addRequest(request)
            //gqlSort.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.CACHE_FIRST).setExpiryTime(5000).build())
            val response =
                gqlSort.executeOnBackground()
                    .getData<ShopPlIstSortingListBase>(ShopPlIstSortingListBase::class.java)
            sortLiveData.value = Success(response)
        }) {
            sortLiveData.value = ErrorMessage(it.toString())
        }
    }

    fun getQSort(): String {
        return "query shopSortingOptions(\$shopId: String!, \$status: Int!, \$query: String!, \$device: String!, \$source: String!) {\n" +
                "\n" +
                "  shopSortingOptions(input: {shopID: \$shopId, status: \$status, query: \$query, device: \$device, source: \$source}) {\n" +
                "    result {\n" +
                "      name\n" +
                "      value\n" +
                "    }\n" +
                "    error {\n" +
                "      message\n" +
                "    }\n" +
                "  }\n" +
                "}"
    }

    fun getQ(): String {
        return "query GetShopProduct(\$sort: Int,\$shopId: String!, \$pageNumber: Int!, \$perPage: Int!) {\n" +
                "  GetShopProduct(shopID: \$shopId, filter: {sort:\$sort, page: \$pageNumber, perPage: \$perPage}) {\n" +
                "    status\n" +
                "    errors\n" +
                "    totalData\n" +
                "    links {\n" +
                "      prev\n" +
                "      next\n" +
                "    }\n" +
                "    data {\n" +
                "      product_id\n" +
                "      name\n" +
                "      price {\n" +
                "        text_idr\n" +
                "      }\n" +
                "      primary_image {\n" +
                "        resize300\n" +
                "      }\n" +
                "      flags {\n" +
                "        isPreorder\n" +
                "        isWholesale\n" +
                "        isWishlist\n" +
                "        isSold\n" +
                "      }\n" +
                "      campaign {\n" +
                "        hide_gimmick\n" +
                "        is_active\n" +
                "        is_upcoming\n" +
                "        discounted_percentage\n" +
                "        original_price_fmt\n" +
                "        discounted_price_fmt\n" +
                "        start_date\n" +
                "        end_date\n" +
                "        stock_sold_percentage\n" +
                "      }\n" +
                "      label {\n" +
                "        color_hex\n" +
                "        content\n" +
                "      }\n" +
                "      label_groups {\n" +
                "        position\n" +
                "        title\n" +
                "        type\n" +
                "        url\n" +
                "      }\n" +
                "      badge {\n" +
                "        title\n" +
                "        image_url\n" +
                "      }\n" +
                "      stats {\n" +
                "        reviewCount\n" +
                "        rating\n" +
                "        viewCount\n" +
                "      }\n" +
                "      cashback {\n" +
                "        cashback\n" +
                "        cashback_amount\n" +
                "      }\n" +
                "      freeOngkir {\n" +
                "        isActive\n" +
                "        imgURL\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}\n"
    }
}