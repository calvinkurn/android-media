package com.tokopedia.autocompletecomponent.suggestion.domain.getshopadssuggestion

import com.tokopedia.autocompletecomponent.suggestion.di.SuggestionScope
import com.tokopedia.autocompletecomponent.suggestion.domain.model.ShopAdsModel
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.usecase.coroutines.UseCase
import dagger.Module
import dagger.Provides

@Module
class GetShopAdsSuggestionUseCaseModule {

    @SuggestionScope
    @Provides
    fun provideGetShopAdsSuggestionUseCase(
        graphqlUseCase: GraphqlUseCase<ShopAdsModel>
    ): UseCase<ShopAdsModel> =
        GetShopAdsSuggestionUseCase(graphqlUseCase)
}