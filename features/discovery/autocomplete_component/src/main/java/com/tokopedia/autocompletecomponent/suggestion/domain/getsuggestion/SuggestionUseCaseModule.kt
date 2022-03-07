package com.tokopedia.autocompletecomponent.suggestion.domain.getsuggestion

import com.tokopedia.autocompletecomponent.suggestion.GET_SUGGESTION_USE_CASE
import com.tokopedia.autocompletecomponent.suggestion.di.SuggestionScope
import com.tokopedia.autocompletecomponent.suggestion.domain.getshopadssuggestion.GetShopAdsSuggestionUseCaseModule
import com.tokopedia.autocompletecomponent.suggestion.domain.model.ShopAdsModel
import com.tokopedia.autocompletecomponent.suggestion.domain.model.SuggestionUniverse
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.usecase.coroutines.UseCase
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module(
    includes = [GetShopAdsSuggestionUseCaseModule::class]
)
class SuggestionUseCaseModule {

    @SuggestionScope
    @Provides
    @Named(GET_SUGGESTION_USE_CASE)
    fun provideSuggestionUseCase(
        getShopAdsSuggestionUseCase: UseCase<ShopAdsModel>
    ): UseCase<SuggestionUniverse> =
        SuggestionUseCase(GraphqlUseCase(), getShopAdsSuggestionUseCase)
}