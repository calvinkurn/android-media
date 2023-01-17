package com.tokopedia.homenav.mainnav.domain.usecases

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.homenav.mainnav.data.pojo.wishlist.*
import com.tokopedia.homenav.mainnav.domain.model.NavWishlistModel
import com.tokopedia.homenav.mainnav.domain.usecases.query.GetWishlistQuery
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.asLowerCase
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

/**
 * Created by Frenzel on 21/04/22
 */
class GetWishlistNavUseCase @Inject constructor (
        private val graphqlUseCase: GraphqlUseCase<WishlistData>
): UseCase<Pair<List<NavWishlistModel>, Boolean>>(){

    init {
        graphqlUseCase.setGraphqlQuery(GetWishlistQuery())
        graphqlUseCase.setRequestParams(generateParam(WishlistParam(sortFilters = constructFilter())))
        graphqlUseCase.setTypeClass(WishlistData::class.java)
        graphqlUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
    }

    override suspend fun executeOnBackground(): Pair<List<NavWishlistModel>, Boolean> {
        val responseData = Success(graphqlUseCase.executeOnBackground().wishlist?:Wishlist())
        val wishlistList = mutableListOf<NavWishlistModel>()
        responseData.data.wishlistItems?.map {
            wishlistList.add(NavWishlistModel(
                productId = it.productId.orEmpty(),
                productName = it.name.orEmpty(),
                imageUrl = it.imageUrl.orEmpty(),
                price = it.price.orEmpty(),
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
        return Pair(wishlistList, responseData.data.hasNext?:false)
    }

    private fun List<Category>.generateCategoryBreadcrumb() : String {
        return this.joinToString(separator = " / ") { it.name.orEmpty() }
    }

    private fun List<LabelGroup>.checkHasCashback() : Boolean {
        return this.any { it.title.asLowerCase() == LABEL_CASHBACK }
    }

    private fun generateParam(param: WishlistParam): Map<String, Any?> {
        return mapOf(PARAM to param)
    }

    private fun constructFilter() : ArrayList<WishlistParam.WishlistSortFilterParam> {
        return arrayListOf(WishlistParam.WishlistSortFilterParam(PARAM_FILTER_NAME_VALUE, arrayListOf(PARAM_FILTER_SELECTED_VALUE)))
    }

    companion object{
        private const val PARAM = "params"
        private const val PARAM_FILTER_NAME_VALUE = "stock"
        private const val PARAM_FILTER_SELECTED_VALUE = "2"
        private const val LABEL_CASHBACK = "cashback"
    }
}
