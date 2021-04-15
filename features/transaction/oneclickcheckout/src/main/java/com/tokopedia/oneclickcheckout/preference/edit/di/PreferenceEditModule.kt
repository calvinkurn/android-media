package com.tokopedia.oneclickcheckout.preference.edit.di

import android.app.Activity
import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.logisticCommon.domain.mapper.AddressCornerMapper
import com.tokopedia.logisticCommon.domain.usecase.GetAddressCornerUseCase
import com.tokopedia.logisticcart.domain.executor.MainScheduler
import com.tokopedia.logisticcart.domain.executor.SchedulerProvider
import com.tokopedia.logisticcart.shipping.features.shippingduration.view.ShippingDurationConverter
import com.tokopedia.logisticcart.shipping.usecase.GetRatesUseCase
import com.tokopedia.oneclickcheckout.common.PAYMENT_LISTING_URL
import com.tokopedia.oneclickcheckout.common.domain.mapper.PreferenceModelMapper
import com.tokopedia.oneclickcheckout.preference.analytics.PreferenceListAnalytics
import com.tokopedia.oneclickcheckout.preference.edit.data.payment.OvoTopUpUrlGqlResponse
import com.tokopedia.oneclickcheckout.preference.edit.data.payment.PaymentListingParamGqlResponse
import com.tokopedia.oneclickcheckout.preference.edit.domain.create.CreatePreferenceUseCase
import com.tokopedia.oneclickcheckout.preference.edit.domain.create.CreatePreferenceUseCaseImpl
import com.tokopedia.oneclickcheckout.preference.edit.domain.create.model.CreatePreferenceGqlResponse
import com.tokopedia.oneclickcheckout.preference.edit.domain.delete.DeletePreferenceUseCase
import com.tokopedia.oneclickcheckout.preference.edit.domain.delete.DeletePreferenceUseCaseImpl
import com.tokopedia.oneclickcheckout.preference.edit.domain.delete.model.DeletePreferenceGqlResponse
import com.tokopedia.oneclickcheckout.preference.edit.domain.get.model.GetPreferenceByIdGqlResponse
import com.tokopedia.oneclickcheckout.preference.edit.domain.payment.GetOvoTopUpUrlUseCase
import com.tokopedia.oneclickcheckout.preference.edit.domain.payment.GetPaymentListingParamUseCase
import com.tokopedia.oneclickcheckout.preference.edit.domain.payment.GetPaymentListingParamUseCaseImpl
import com.tokopedia.oneclickcheckout.preference.edit.domain.shipping.GetShippingDurationUseCase
import com.tokopedia.oneclickcheckout.preference.edit.domain.shipping.mapper.ShippingDurationModelMapper
import com.tokopedia.oneclickcheckout.preference.edit.domain.update.UpdatePreferenceUseCase
import com.tokopedia.oneclickcheckout.preference.edit.domain.update.model.UpdatePreferenceGqlResponse
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Named

@Module
open class PreferenceEditModule(private val activity: Activity) {

    @PreferenceEditScope
    @Provides
    fun provideContext(): Context = activity

    @PreferenceEditScope
    @Provides
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @PreferenceEditScope
    @Provides
    internal fun providesGraphqlRepository(): GraphqlRepository {
        return GraphqlInteractor.getInstance().graphqlRepository
    }

    @PreferenceEditScope
    @Provides
    fun provideScheduler(): SchedulerProvider {
        return MainScheduler()
    }

    @PreferenceEditScope
    @Provides
    fun provideGetShippingDurationUseCase(graphqlRepository: GraphqlRepository, mapper: ShippingDurationModelMapper): GetShippingDurationUseCase {
        return GetShippingDurationUseCase(GraphqlUseCase(graphqlRepository), mapper)
    }

    @PreferenceEditScope
    @Provides
    fun providesGetPreferenceByIdGraphqlUseCase(graphqlRepository: GraphqlRepository): GraphqlUseCase<GetPreferenceByIdGqlResponse> {
        return GraphqlUseCase(graphqlRepository)
    }

    @PreferenceEditScope
    @Provides
    fun providesPreferenceModelMapper(): PreferenceModelMapper = PreferenceModelMapper

    @PreferenceEditScope
    @Provides
    fun providesCreatePreferenceGraphqlUseCase(graphqlRepository: GraphqlRepository): GraphqlUseCase<CreatePreferenceGqlResponse> {
        return GraphqlUseCase(graphqlRepository)
    }

    @PreferenceEditScope
    @Provides
    fun providesCreatePreferenceUseCase(graphqlUseCase: GraphqlUseCase<CreatePreferenceGqlResponse>): CreatePreferenceUseCase {
        return CreatePreferenceUseCaseImpl(graphqlUseCase)
    }

    @PreferenceEditScope
    @Provides
    fun providesDeletePreferenceGraphqlUseCase(graphqlRepository: GraphqlRepository): GraphqlUseCase<DeletePreferenceGqlResponse> {
        return GraphqlUseCase(graphqlRepository)
    }

    @PreferenceEditScope
    @Provides
    fun providesDeletePreferenceUseCase(graphqlUseCase: GraphqlUseCase<DeletePreferenceGqlResponse>): DeletePreferenceUseCase {
        return DeletePreferenceUseCaseImpl(graphqlUseCase)
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
                               schedulerProvider: SchedulerProvider): GetRatesUseCase {
        return GetRatesUseCase(context, converter, schedulerProvider)
    }

    @PreferenceEditScope
    @Provides
    fun provideGetAddressCornerUseCase(context: Context, graphqlUseCase: com.tokopedia.graphql.domain.GraphqlUseCase, mapper: AddressCornerMapper): GetAddressCornerUseCase {
        return GetAddressCornerUseCase(context, graphqlUseCase, mapper)
    }

    @PreferenceEditScope
    @Provides
    fun provideGetPaymentListingGraphqlUseCase(graphqlRepository: GraphqlRepository): GraphqlUseCase<PaymentListingParamGqlResponse> {
        return GraphqlUseCase(graphqlRepository)
    }

    @PreferenceEditScope
    @Provides
    fun provideGetPaymentListingParamUseCase(graphqlUseCase: GraphqlUseCase<PaymentListingParamGqlResponse>): GetPaymentListingParamUseCase {
        return GetPaymentListingParamUseCaseImpl(graphqlUseCase)
    }

    @PreferenceEditScope
    @Provides
    @Named(PAYMENT_LISTING_URL)
    open fun providePaymentListingUrl(): String {
        return "${TokopediaUrl.getInstance().PAY}/v2/payment/register/listing"
    }

    @PreferenceEditScope
    @Provides
    fun provideGetOvoTopUpUrlUseCase(graphqlUseCase: GraphqlUseCase<OvoTopUpUrlGqlResponse>): GetOvoTopUpUrlUseCase {
        return GetOvoTopUpUrlUseCase(graphqlUseCase)
    }
}