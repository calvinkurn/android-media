package com.tokopedia.thankyou_native.di.module

import android.content.Context
import android.content.SharedPreferences
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.thankyou_native.di.qualifier.CoroutineBackgroundDispatcher
import com.tokopedia.thankyou_native.di.qualifier.CoroutineMainDispatcher
import com.tokopedia.thankyou_native.presentation.adapter.DetailedInvoiceAdapter
import com.tokopedia.thankyou_native.presentation.adapter.PurchaseDetailAdapter
import com.tokopedia.thankyou_native.presentation.adapter.factory.InvoiceTypeFactory
import com.tokopedia.thankyou_native.presentation.adapter.factory.PurchaseDetailTypeFactory
import com.tokopedia.topads.sdk.domain.usecase.TopAdsImageViewUseCase
import com.tokopedia.topads.sdk.data.repository.TopAdsRepository
import com.tokopedia.topads.sdk.utils.TopAdsIrisSession
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers


@Module
class ThankYouPageModule {

    @Provides
    fun provideGraphqlUseCase(): GraphqlUseCase = GraphqlUseCase()


    @Provides
    @CoroutineMainDispatcher
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @Provides
    @CoroutineBackgroundDispatcher
    fun provideBackgroundDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }


    @Provides
    fun provideGraphqlRepositoryModule(): GraphqlRepository {
        return GraphqlInteractor.getInstance().graphqlRepository
    }


    @Provides
    fun provideInvoiceTypeFactory(): InvoiceTypeFactory {
        return InvoiceTypeFactory()
    }


    @Provides
    fun provideDetailInvoiceAdapter(invoiceTypeFactory: InvoiceTypeFactory): DetailedInvoiceAdapter {
        return DetailedInvoiceAdapter(arrayListOf(), invoiceTypeFactory)
    }

    @Provides
    fun providePurchaseDetailTypeFactory(): PurchaseDetailTypeFactory {
        return PurchaseDetailTypeFactory()
    }

    @Provides
    fun providePurchaseDetailAdapter(purchaseDetailTypeFactory: PurchaseDetailTypeFactory): PurchaseDetailAdapter {
        return PurchaseDetailAdapter(arrayListOf(), purchaseDetailTypeFactory)
    }

    @Provides
    fun provideTopAdsImageViewUseCase(
        userSession: UserSessionInterface,
        topAdsIrisSession: TopAdsIrisSession
    ): TopAdsImageViewUseCase =
        TopAdsImageViewUseCase(
            userSession.userId,
            TopAdsRepository(),
            topAdsIrisSession.getSessionId()
        )

    @Provides
    fun provideSharedPref(
        @ApplicationContext context: Context
    ): SharedPreferences {
        return context.getSharedPreferences(THANKYOU_PAGE_CACHE, Context.MODE_PRIVATE)
    }

    companion object {
        private const val THANKYOU_PAGE_CACHE = "thankyou_page_cache"
    }
}
