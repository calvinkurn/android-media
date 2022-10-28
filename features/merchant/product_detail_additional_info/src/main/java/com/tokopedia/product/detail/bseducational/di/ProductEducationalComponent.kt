package com.tokopedia.product.detail.bseducational.di

import android.content.Context
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.product.detail.bseducational.view.ProductEducationalBottomSheet
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
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
    fun provideUserSessionInterface(@ApplicationContext
                                    context: Context
    ): UserSessionInterface = UserSession(context)

    @ProductEducationalScope
    @Provides
    fun provideTrackingQueue(@ApplicationContext context: Context) = TrackingQueue(context)

    @ProductEducationalScope
    @Provides
    @Named(TYPE_ARG_NAMED)
    fun provideTypeString() = type
}

@MustBeDocumented
@Scope
@Retention
annotation class ProductEducationalScope