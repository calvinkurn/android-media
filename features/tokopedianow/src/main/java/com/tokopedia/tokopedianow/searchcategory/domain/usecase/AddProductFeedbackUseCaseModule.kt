package com.tokopedia.tokopedianow.searchcategory.domain.usecase

import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.tokopedianow.searchcategory.domain.model.AddProductFeedbackModel
import com.tokopedia.usecase.coroutines.UseCase
import dagger.Module
import dagger.Provides

@Module
class AddProductFeedbackUseCaseModule {

    @Provides
    fun provideAddFeedbackUseCase(): UseCase<AddProductFeedbackModel> {
        val graphqlUseCase = GraphqlUseCase<AddProductFeedbackModel>(
            GraphqlInteractor.getInstance().graphqlRepository
        )
        return AddProductFeedbackUseCase(graphqlUseCase)
    }
}
