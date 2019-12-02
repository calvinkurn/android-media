package com.tokopedia.home_wishlist.domain

import com.tokopedia.home_wishlist.base.UseCase
import com.tokopedia.home_wishlist.data.repository.WishlistRepository
import com.tokopedia.home_wishlist.model.entity.WishlistEntityData
import javax.inject.Inject


class GetWishlistDataUseCase @Inject constructor(
        private val wishlistRepository: WishlistRepository) : UseCase<GetWishlistParameter, WishlistEntityData>() {
    override suspend fun getData(inputParameter: GetWishlistParameter): WishlistEntityData {
        return wishlistRepository.getData(inputParameter.keyword, inputParameter.page)
    }
}

data class GetWishlistParameter(
        val keyword: String = "",
        val page: Int = 1
)