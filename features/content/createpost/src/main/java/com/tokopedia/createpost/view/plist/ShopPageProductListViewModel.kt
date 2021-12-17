package com.tokopedia.createpost.view.plist

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.createpost.common.view.plist.*
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
    private var sortPosition: Int? = -1
    private var soruce: String? = "shop_product"

    companion object {

        const val PARAM_SHOP_ID = "shopId"
        const val PARAM_QUERY = "query"
        const val PARAM_STATUS = "status"
        const val PARAM_DEVICE = "device"
        const val PARAM_SOURCE = "source"
        const val PARAM_DEVICE_VALUE_ANDROID = "android"
        const val PARAM_SOURCE_VALUE = "shop_product"
        const val PARAM_PAGE_NUMBER = "pageNumber"
        const val PARAM_PER_PAGE = "perPage"
        const val PARAM_SORT = "sort"
    }

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
            variablesMain[PARAM_SHOP_ID] = shopId!!
            variablesMain[PARAM_PAGE_NUMBER] = pageNumber
            variablesMain[PARAM_PER_PAGE] = getPerPageSize()

            if (!sort.isZero()) {
                variablesMain[PARAM_SORT] = sort!!.toInt()
            }

            val request = GraphqlRequest(
                getShopProductQuery(),
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
    fun getPerPageSize() :Int{
        return 60
    }

    fun getSortData() {
        launchCatchError(block = {
            val variablesMain = java.util.HashMap<String, Any>()
            variablesMain[PARAM_SHOP_ID] = shopId.orEmpty()
            variablesMain[PARAM_QUERY] = ""
            variablesMain[PARAM_STATUS] = 1
            variablesMain[PARAM_DEVICE] = PARAM_DEVICE_VALUE_ANDROID
            variablesMain[PARAM_SOURCE] = PARAM_SOURCE_VALUE
            val request = GraphqlRequest(
                getSortProductQuery(),
                ShopPlIstSortingListBase::class.java,
                variablesMain,
                false
            )
            gqlSort.clearRequest()
            gqlSort.addRequest(request)
            val response =
                gqlSort.executeOnBackground()
                    .getData<ShopPlIstSortingListBase>(ShopPlIstSortingListBase::class.java)
            sortLiveData.value = Success(response)
        }) {
            sortLiveData.value = ErrorMessage(it.toString())
        }
    }

    private fun getSortProductQuery() =
         """query shopSortingOptions(${'$'}shopId: String!, ${'$'}status: Int!, ${'$'}query: String!,${'$'}device: String!, ${'$'}source: String!) {        
                    shopSortingOptions(input: {shopID: ${'$'}shopId, status: ${'$'}status, query: ${'$'}query, device: ${'$'}device, source:${'$'}source}) {       
                      result {       
                        name       
                        value       
                      }       
                      error {       
                        message       
                      }       
                    }       
                  }"""


    private fun getShopProductQuery() =
        """query GetShopProduct(${'$'}sort: Int,${'$'}shopId: String!, ${'$'}pageNumber: Int!, ${'$'}perPage: Int!) {
            GetShopProduct(shopID: ${'$'}shopId, filter: {sort:${'$'}sort, page: ${'$'}pageNumber, perPage: ${'$'}perPage}) {
            status
            errors
            totalData
            links {
                prev
                next
            }
            data {
                product_id
                name
                price {
                    text_idr
                }
                primary_image {
                    resize300
                }
                flags {
                    isPreorder
                    isWholesale
                    isWishlist
                    isSold
                }
                campaign {
                    hide_gimmick
                    is_active
                    is_upcoming
                    discounted_percentage
                    original_price_fmt
                    discounted_price_fmt
                    start_date
                    end_date
                    stock_sold_percentage
                }
                label {
                    color_hex
                    content
                }
                label_groups {
                    position
                    title
                    type
                    url
                }
                badge {
                    title
                    image_url
                }
                stats {
                    reviewCount
                    rating
                    viewCount
                }
                cashback {
                    cashback
                    cashback_amount
                }
                freeOngkir {
                    isActive
                    imgURL
                }
            }
        }
        } """

}