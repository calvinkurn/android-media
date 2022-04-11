package com.tokopedia.product.detail.bsinfo.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.product.detail.bsinfo.view.ProductEducationalBottomSheet
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Scope


const val TYPE_ARG_NAMED = "TYPE_EDUCATIONAL"

@ProductEducationalScope
@Component(modules = [
    ViewModelModule::class,
    ProductEducationalModule::class],
        dependencies = [BaseAppComponent::class])
interface ProductEducationalComponent {
    fun inject(productEducationalBottomSheet: ProductEducationalBottomSheet)
}

@Module
class ProductEducationalModule(val type: String) {
    @ProductEducationalScope
    @Provides
    fun provideGraphQlRepository() = GraphqlInteractor.getInstance().graphqlRepository

    @ProductEducationalScope
    @Provides
    @Named(TYPE_ARG_NAMED)
    fun provideTypeString() = type
}

@MustBeDocumented
@Scope
@Retention
annotation class ProductEducationalScope