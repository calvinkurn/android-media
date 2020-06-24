package com.tokopedia.oneclickcheckout.preference.edit.di

import android.app.Activity
import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.Interactor
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.logisticcart.domain.executor.MainScheduler
import com.tokopedia.logisticcart.domain.executor.SchedulerProvider
import com.tokopedia.logisticcart.shipping.features.shippingduration.view.ShippingDurationConverter
import com.tokopedia.logisticcart.shipping.usecase.GetRatesUseCase
import com.tokopedia.oneclickcheckout.common.domain.GetShippingDurationUseCase
import com.tokopedia.oneclickcheckout.preference.analytics.PreferenceListAnalytics
import com.tokopedia.oneclickcheckout.preference.edit.domain.create.CreatePreferenceUseCase
import com.tokopedia.oneclickcheckout.preference.edit.domain.create.model.CreatePreferenceGqlResponse
import com.tokopedia.oneclickcheckout.preference.edit.domain.delete.DeletePreferenceUseCase
import com.tokopedia.oneclickcheckout.preference.edit.domain.delete.model.DeletePreferenceGqlResponse
import com.tokopedia.oneclickcheckout.preference.edit.domain.get.model.GetPreferenceByIdGqlResponse
import com.tokopedia.oneclickcheckout.preference.edit.domain.update.UpdatePreferenceUseCase
import com.tokopedia.oneclickcheckout.preference.edit.domain.update.model.UpdatePreferenceGqlResponse
import com.tokopedia.purchase_platform.common.feature.addresslist.AddressCornerMapper
import com.tokopedia.purchase_platform.common.feature.addresslist.GetAddressCornerUseCase
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@PreferenceEditScope
@Module
class PreferenceEditModule(private val activity: Activity) {

    @PreferenceEditScope
    @Provides
    fun provideContext(): Context = activity

    @PreferenceEditScope
    @Provides
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @PreferenceEditScope
    @Provides
    internal fun providesGraphqlRepository(): GraphqlRepository {
        return Interactor.getInstance().graphqlRepository
    }

    @PreferenceEditScope
    @Provides
    fun provideScheduler(): SchedulerProvider {
        return MainScheduler()
    }

    @PreferenceEditScope
    @Provides
    fun provideGetShippingDurationUseCase(graphqlRepository: GraphqlRepository): GetShippingDurationUseCase {
        return GetShippingDurationUseCase(GraphqlUseCase(graphqlRepository))
    }

    @PreferenceEditScope
    @Provides
    fun providesGetPreferenceByIdGraphqlUseCase(graphqlRepository: GraphqlRepository): GraphqlUseCase<GetPreferenceByIdGqlResponse> {
        return GraphqlUseCase(graphqlRepository)
    }

    @PreferenceEditScope
    @Provides
    fun providesCreatePreferenceGraphqlUseCase(graphqlRepository: GraphqlRepository): GraphqlUseCase<CreatePreferenceGqlResponse> {
        return GraphqlUseCase(graphqlRepository)
    }

    @PreferenceEditScope
    @Provides
    fun providesCreatePreferenceUseCase(graphqlUseCase: GraphqlUseCase<CreatePreferenceGqlResponse>): CreatePreferenceUseCase {
        return CreatePreferenceUseCase(graphqlUseCase)
    }

    @PreferenceEditScope
    @Provides
    fun providesDeletePreferenceGraphqlUseCase(graphqlRepository: GraphqlRepository): GraphqlUseCase<DeletePreferenceGqlResponse> {
        return GraphqlUseCase(graphqlRepository)
    }

    @PreferenceEditScope
    @Provides
    fun providesDeletePreferenceUseCase(graphqlUseCase: GraphqlUseCase<DeletePreferenceGqlResponse>): DeletePreferenceUseCase {
        return DeletePreferenceUseCase(graphqlUseCase)
    }

    @PreferenceEditScope
    @Provides
    fun providesUpdatePreferenceGraphqlUseCase(graphqlRepository: GraphqlRepository): GraphqlUseCase<UpdatePreferenceGqlResponse> {
        return GraphqlUseCase(graphqlRepository)
    }

    @PreferenceEditScope
    @Provides
    fun providesUpdatePreferenceUseCase(graphqlUseCase: GraphqlUseCase<UpdatePreferenceGqlResponse>): UpdatePreferenceUseCase {
        return UpdatePreferenceUseCase(graphqlUseCase)
    }

    @PreferenceEditScope
    @Provides
    fun provideRatesGraphqlUseCase(): com.tokopedia.graphql.domain.GraphqlUseCase {
        return com.tokopedia.graphql.domain.GraphqlUseCase()
    }

    @PreferenceEditScope
    @Provides
    fun providePreferenceListAnalytics(): PreferenceListAnalytics {
        return PreferenceListAnalytics()
    }

    @PreferenceEditScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @PreferenceEditScope
    @Provides
    fun provideGetRatesUseCase(context: Context, converter: ShippingDurationConverter,
                               graphqlUseCase: com.tokopedia.graphql.domain.GraphqlUseCase, schedulerProvider: SchedulerProvider): GetRatesUseCase {
        return GetRatesUseCase(context, converter, graphqlUseCase, schedulerProvider)
    }

    @PreferenceEditScope
    @Provides
    fun provideGetAddressCornerUseCase(context: Context, graphqlUseCase: com.tokopedia.graphql.domain.GraphqlUseCase, mapper: AddressCornerMapper): GetAddressCornerUseCase {
        return GetAddressCornerUseCase(context, graphqlUseCase, mapper)
    }
}