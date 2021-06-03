package com.tkpd.atc_variant.di

import android.content.Context
import com.tkpd.atc_variant.views.bottomsheet.AtcVariantBottomSheet
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.atc_common.AtcConstant
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Named

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

    @Provides
    @Named("atcMutation")
    fun provideAddToCartMutation(@ApplicationContext context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, com.tokopedia.atc_common.R.raw.mutation_add_to_cart)
    }

    @Provides
    @AtcVariantScope
    @Named("atcOcsMutation")
    fun provideAddToCartOcsMutation(@ApplicationContext context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, com.tokopedia.atc_common.R.raw.mutation_add_to_cart_one_click_shipment)
    }

    @Provides
    @AtcVariantScope
    @Named(AtcConstant.MUTATION_ATC_OCC)
    fun provideAtcOccMutation(@ApplicationContext context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, com.tokopedia.atc_common.R.raw.mutation_add_to_cart_one_click_checkout)
    }

    @AtcVariantScope
    @Provides
    fun provideAddWishListUseCase(@ApplicationContext context: Context): AddWishListUseCase =
            AddWishListUseCase(context)
}