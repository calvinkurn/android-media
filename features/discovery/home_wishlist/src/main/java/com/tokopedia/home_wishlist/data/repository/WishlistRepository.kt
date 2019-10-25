package com.tokopedia.home_wishlist.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.google.gson.Gson
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.home_wishlist.data.ExampleResponse
import com.tokopedia.home_wishlist.model.entity.WishlistItem
import com.tokopedia.home_wishlist.model.entity.WishlistResponse
import com.tokopedia.home_wishlist.util.Response
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

class WishlistRepository @Inject constructor(
        private val graphqlRepository: GraphqlRepository,
        @Named("wishlistQuery") private val query: String
) {
    companion object{
        private const val PARAM_COUNT = "count"
        private const val PARAM_PAGE = "page"
        private const val PARAM_QUERY = "query"
        private const val DEFAULT_COUNT = 8
    }

    fun load(filter: String, page: Int): LiveData<Response<List<WishlistItem>>> = MediatorLiveData<Response<List<WishlistItem>>>().apply{
        val gson = Gson()
        val data = gson.fromJson(ExampleResponse.getJson(), WishlistResponse::class.java)
        value = Response.success(data.data.wishlist.items)
    }
}