package com.tokopedia.autocompletecomponent.universal.presentation.viewmodel

import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.autocompletecomponent.universal.UniversalConstant
import com.tokopedia.autocompletecomponent.universal.UniversalConstant.UNIVERSAL_SEARCH_VIEW_MODEL_FACTORY
import com.tokopedia.autocompletecomponent.universal.di.UniversalSearchScope
import com.tokopedia.autocompletecomponent.universal.domain.getuniversalsearch.UniversalSearchUseCaseModule
import com.tokopedia.autocompletecomponent.universal.domain.model.UniversalSearchModel
import com.tokopedia.autocompletecomponent.universal.presentation.mapper.UniversalSearchModelMapperModule
import com.tokopedia.autocompletecomponent.util.ChooseAddressWrapper
import com.tokopedia.discovery.common.Mapper
import com.tokopedia.usecase.coroutines.UseCase
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module(includes = [
    UniversalSearchModelMapperModule::class,
    UniversalSearchUseCaseModule::class
])
internal class UniversalSearchViewModelFactoryModule(
    private val searchParameter: Map<String, Any> = mapOf()
) {

    @UniversalSearchScope
    @Provides
    @Named(UNIVERSAL_SEARCH_VIEW_MODEL_FACTORY)
    fun provideUniversalSearchViewModelFactory(
        baseDispatcher: CoroutineDispatchers,
        @Named(UniversalConstant.UNIVERSAL_SEARCH_USE_CASE)
        universalSearchUseCase: UseCase<UniversalSearchModel>,
        universalSearchModelMapper: Mapper<UniversalSearchModel, List<Visitable<*>>>,
        chooseAddressWrapper: ChooseAddressWrapper,
    ): ViewModelProvider.Factory {
        return UniversalSearchViewModelFactory(
            baseDispatcher,
            universalSearchUseCase,
            universalSearchModelMapper,
            searchParameter,
            chooseAddressWrapper
        )
    }
}