package com.tokopedia.homenav.mainnav.domain.usecases

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.homenav.mainnav.data.pojo.wishlist.*
import com.tokopedia.homenav.mainnav.domain.model.NavWishlistModel
import com.tokopedia.homenav.mainnav.domain.usecases.query.GetWishlistQuery
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

/**
 * Created by Frenzel on 21/04/22
 */
class GetWishlistNavUseCase @Inject constructor(
    private val graphqlUseCase: GraphqlUseCase<GetWishlistCollection>
) : UseCase<Triple<List<NavWishlistModel>, Boolean, Boolean>>() {

    companion object {
        private const val MAX_WISHLIST_COLLECTIONS = 4
    }

    init {
        graphqlUseCase.setGraphqlQuery(GetWishlistQuery())
        graphqlUseCase.setTypeClass(GetWishlistCollection::class.java)
        graphqlUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
    }

    override suspend fun executeOnBackground(): Triple<List<NavWishlistModel>, Boolean, Boolean> {
        val responseData = Success(graphqlUseCase.executeOnBackground().wishlist?.data ?: WishlistData())
        val wishlistList = mutableListOf<NavWishlistModel>()
        responseData.data.wishlistCollections?.map {
            wishlistList.add(
                NavWishlistModel(
                    id = it.id.orEmpty(),
                    name = it.name.orEmpty(),
                    totalItem = it.totalItem.orZero(),
                    itemText = it.itemText.orEmpty(),
                    images = it.images.orEmpty()
                )
            )
        }
        val showViewAll = (responseData.data.totalCollection ?: Int.ZERO) > MAX_WISHLIST_COLLECTIONS
        return Triple(wishlistList, showViewAll, responseData.data.isEmptyState.orFalse())
    }
}
