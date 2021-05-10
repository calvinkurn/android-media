package com.tkpd.atc_variant.di

import android.content.Context
import com.tkpd.atc_variant.views.bottomsheet.AtcVariantBottomSheet
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Component
import dagger.Module
import dagger.Provides

/**
 * Created by Yehezkiel on 07/05/21
 */

@AtcVariantScope
@Component(modules = [
    AtcVariantModule::class,
    ViewModelModule::class],
        dependencies = [BaseAppComponent::class])
interface AtcVariantComponent {
    fun inject(bottomSheet: AtcVariantBottomSheet)
}

@Module
class AtcVariantModule {

    @Provides
    @AtcVariantScope
    fun provideUserSessionInterface(@ApplicationContext context: Context): UserSessionInterface = UserSession(context)

    @AtcVariantScope
    @Provides
    fun provideGraphQlRepository() = GraphqlInteractor.getInstance().graphqlRepository

}