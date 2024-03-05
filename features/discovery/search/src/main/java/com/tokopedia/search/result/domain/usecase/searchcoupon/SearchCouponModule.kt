package com.tokopedia.search.result.domain.usecase.searchcoupon

import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.search.result.domain.model.SearchCouponModel
import com.tokopedia.search.result.domain.model.SearchRedeemCouponModel
import com.tokopedia.usecase.coroutines.UseCase
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
internal class SearchCouponModule {
    @Provides
    @Named(SearchConstant.SearchCoupon.SEARCH_COUPON_USE_CASE)
    fun provideGetCouponGqlUseCase(): UseCase<SearchCouponModel> = SearchGetCouponCoroutineUseCase(GraphqlUseCase())

    @Provides
    @Named(SearchConstant.SearchCoupon.SEARCH_COUPON_REDEEM_USE_CASE)
    fun provideRedeemCouponGqlUseCase(): UseCase<SearchRedeemCouponModel> = SearchRedeemCouponCoroutineUseCase(GraphqlUseCase())
}
