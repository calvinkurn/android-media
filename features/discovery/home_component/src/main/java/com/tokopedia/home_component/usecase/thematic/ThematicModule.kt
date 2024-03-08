package com.tokopedia.home_component.usecase.thematic

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.usecase.coroutines.UseCase
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class ThematicModule {
    @Provides
    @Named(ThematicUsecaseUtil.THEMATIC_DI_NAME)
    fun provideThematicUseCase(): UseCase<ThematicModel> =
        ThematicUseCase(GraphqlUseCase())
}
