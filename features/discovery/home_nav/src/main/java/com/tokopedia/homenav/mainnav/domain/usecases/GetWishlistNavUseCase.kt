package com.tokopedia.homenav.mainnav.domain.usecases

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.homenav.mainnav.data.pojo.wishlist.Category
import com.tokopedia.homenav.mainnav.data.pojo.wishlist.LabelGroup
import com.tokopedia.homenav.mainnav.data.pojo.wishlist.Wishlist
import com.tokopedia.homenav.mainnav.data.pojo.wishlist.WishlistData
import com.tokopedia.homenav.mainnav.domain.model.NavWishlistModel
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.asLowerCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

/**
 * Created by Frenzel on 21/04/22
 */
class GetWishlistNavUseCase @Inject constructor (
        private val graphqlUseCase: GraphqlUseCase<WishlistData>
): UseCase<List<NavWishlistModel>>(){

    init {
        val query = """
            query GetWishlist(${'$'}page:Int, ${'$'}limit:Int){
              wishlist_v2(params:{page:${'$'}page, limit:${'$'}limit}){
                items {
                  id
                  name
                  url
                  image_url
                  price
                  price_fmt
                  available
                  label_status
                  preorder
                  rating
                  sold_count
                  min_order
                  shop {
                    id
                    name
                    url
                    location
                    fulfillment{
                      is_fulfillment
                      text
                    }
                    is_tokonow
                  }
                  badges{
                    title
                    image_url
                  }
                  labels
                  wholesale_price{
                    minimum
                    maximum
                    price
                  }
                  default_child_id
                  original_price
                  original_price_fmt
                  discount_percentage
                  discount_percentage_fmt
                  label_stock
                  bebas_ongkir {
                    type
                    title
                    image_url
                  }
                  label_group{
                    position
                    title
                    type
                    url
                  }
                  buttons{
                    primary_button{
                      text
                      action
                      url
                    }
                    additional_buttons{
                      text
                      action
                      url
                    }
                  }
                  wishlist_id
                  variant_name
                  category{
                    category_id
                    category_name
                  }
                }
                error_message
              }
            }
        """.trimIndent()
        graphqlUseCase.setGraphqlQuery(query)
        graphqlUseCase.setRequestParams(setParams().parameters)
        graphqlUseCase.setTypeClass(WishlistData::class.java)
        graphqlUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
    }

    override suspend fun executeOnBackground(): List<NavWishlistModel> {
        val responseData = Success(graphqlUseCase.executeOnBackground().wishlist?:Wishlist())
        val wishlistList = mutableListOf<NavWishlistModel>()
        responseData.data.wishlistItems?.map {
            wishlistList.add(NavWishlistModel(
                productId = it.productId.orEmpty(),
                productName = it.name.orEmpty(),
                imageUrl = it.imageUrl.orEmpty(),
                priceFmt = it.priceFmt.orEmpty(),
                originalPriceFmt = it.originalPriceFmt.orEmpty(),
                discountPercentageFmt = it.discountPercentageFmt.orEmpty(),
                cashback = it.labelGroup?.checkHasCashback().orFalse(),
                category = it.category.orEmpty(),
                categoryBreadcrumb = it.category?.generateCategoryBreadcrumb().orEmpty(),
                wishlistId = it.wishlistId.orEmpty(),
                variant = it.variant.orEmpty()
            ))
        }
        return wishlistList
    }

    private fun List<Category>.generateCategoryBreadcrumb() : String {
        return this.joinToString(separator = " / ") { it.name.orEmpty() }
    }

    private fun List<LabelGroup>.checkHasCashback() : Boolean {
        return this.any { it.title.asLowerCase() == LABEL_CASHBACK }
    }

    private fun setParams(
        page: Int = PARAM_PAGE_VALUE,
        limit: Int = PARAM_LIMIT_VALUE
    ) = RequestParams.create().apply {
        parameters.clear()

        putInt(PARAM_PAGE, page)
        putInt(PARAM_LIMIT, limit)
    }

    companion object{
        private const val PARAM_PAGE = "page"
        private const val PARAM_LIMIT = "limit"
        private const val PARAM_PAGE_VALUE = 1
        private const val PARAM_LIMIT_VALUE = 5
        private const val LABEL_CASHBACK = "cashback"
    }
}