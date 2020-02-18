package com.tokopedia.home.beranda.data.repository

import com.tokopedia.home.beranda.data.model.KeywordSearchData
import com.tokopedia.home.beranda.data.model.PlayLiveDynamicChannelEntity
import com.tokopedia.home.beranda.data.model.TokopointsDrawerHomeData
import com.tokopedia.home.beranda.domain.gql.ProductrevDismissSuggestion
import com.tokopedia.home.beranda.domain.model.HomeData
import com.tokopedia.home.beranda.domain.model.review.SuggestedProductReview
import com.tokopedia.home.beranda.helper.Result
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import rx.Observable

interface HomeRepository {
    fun getHomeData(): Flow<HomeData?>
    fun updateHomeData(): Flow<Result<Any>>
    fun getTokopoints(): Flow<TokopointsDrawerHomeData>
    fun getKeywordSearch(): Flow<KeywordSearchData>
    fun getSuggestedReview(): Flow<SuggestedProductReview>
    fun dismissSuggestedReview(): Flow<ProductrevDismissSuggestion>
    fun sendGeolocationInfo(): Observable<Response<String>>
    fun getPlayChannel(): Flow<PlayLiveDynamicChannelEntity>
}

