package com.tokopedia.autocompletecomponent.universal.presentation.mapper

import com.tokopedia.autocompletecomponent.universal.di.UniversalSearchScope
import com.tokopedia.autocompletecomponent.universal.domain.model.UniversalSearchModel
import com.tokopedia.autocompletecomponent.universal.presentation.model.UniversalDataView
import com.tokopedia.discovery.common.Mapper
import dagger.Module
import dagger.Provides

@Module
internal class UniversalSearchModelMapperModule(
    private val dimension90: String = "",
    private val keyword: String = "",
) {

    @UniversalSearchScope
    @Provides
    fun provideUniversalSearchModelMapper(): Mapper<UniversalSearchModel, UniversalDataView> {
        return UniversalSearchModelMapper(dimension90, keyword)
    }
}