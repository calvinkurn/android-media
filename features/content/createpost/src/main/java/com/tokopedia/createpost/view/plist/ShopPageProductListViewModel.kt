package com.tokopedia.createpost.view.plist

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class ShopPageProductListViewModel @Inject constructor() : BaseViewModel(Dispatchers.Main) {

    val productList = MutableLiveData<Resources<GetShopProduct>>()
    val pageData = MutableLiveData<Resources<GetShopProduct>>()
    val sortLiveData = MutableLiveData<Resources<GetShopPagePListSortModel>>()
    val newSortValeLiveData = MutableLiveData<ShopPagePListSortItem>()
    private val gql = MultiRequestGraphqlUseCase()
    private val gqlSort = MultiRequestGraphqlUseCase()
    private var shopId: Int = -1

    fun getPageData(shopId: Int) {
        this.shopId = 463441
        productList.value = Loading()
        getList(1)
    }

    fun setNewSortValue(value: ShopPagePListSortItem) {
        newSortValeLiveData.value = value
    }

    fun getList(pageNumber: Int) {
        launchCatchError(block = {
            val variablesMain = java.util.HashMap<String, Any>()
            variablesMain["shopId"] = shopId
            variablesMain["page"] = pageNumber
            variablesMain["perPage"] = 10
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

    fun getSortData() {
        launchCatchError(block = {
            val variablesMain = java.util.HashMap<String, Any>()
            variablesMain["shopId"] = shopId
            variablesMain["query"] = ""
            variablesMain["status"] = 1
            variablesMain["device"] = "android"
            variablesMain["source"] = "shop_product"
            val request = GraphqlRequest(
                getQSort(),
                GetShopPagePListSortModel::class.java,
                variablesMain,
                false
            )
            gqlSort.clearRequest()
            gqlSort.addRequest(request)
            val response =
                gqlSort.executeOnBackground()
                    .getData<GetShopPagePListSortModel>(GetShopPagePListSortModel::class.java)
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
        return "query GetShopProduct(\$shopId: String!, \$pageNumber: Int!, \$perPage: Int!) {\n" +
                "  GetShopProduct(shopID: \$shopId, filter: {page: \$pageNumber, perPage: \$perPage}) {\n" +
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