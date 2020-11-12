package com.tokopedia.report.di

import android.content.Context
import com.google.gson.Gson
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.imageuploader.di.ImageUploaderModule
import com.tokopedia.imageuploader.di.qualifier.ImageUploaderQualifier
import com.tokopedia.imageuploader.domain.GenerateHostRepository
import com.tokopedia.imageuploader.domain.UploadImageRepository
import com.tokopedia.imageuploader.domain.UploadImageUseCase
import com.tokopedia.imageuploader.utils.ImageUploaderUtils
import com.tokopedia.report.R
import com.tokopedia.report.data.model.ImageAttachment
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import com.tokopedia.coroutines.dispatcher.CoroutineDispatchers
import com.tokopedia.coroutines.dispatcher.CoroutineDispatchersProvider
import javax.inject.Named

@MerchantReportScope
@Module(includes = [ImageUploaderModule::class, ViewModelModule::class])
class MerchantReportModule {

    @MerchantReportScope
    @Provides
    fun provideMainDispatcher(): CoroutineDispatchers = CoroutineDispatchersProvider

    @Provides
    fun provideGraphQlRepository() = GraphqlInteractor.getInstance().graphqlRepository

    @Provides
    fun provideGraphQlUseCaseRx() = GraphqlUseCase()

    @Provides
    fun provideUserSessionInterface(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @MerchantReportScope
    @Provides
    @Named("product_report_reason")
    fun getProductReportReasonQuery(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.gql_get_product_report_reason)

    @MerchantReportScope
    @Provides
    @Named("product_report_submit")
    fun getProductReportSubmitMutation(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.gql_mutation_submit_report)

    @Provides
    fun provideUploadImageUseCase(
            @ImageUploaderQualifier uploadImageRepository: UploadImageRepository,
            @ImageUploaderQualifier generateHostRepository: GenerateHostRepository,
            @ImageUploaderQualifier gson: Gson,
            @ImageUploaderQualifier userSession: UserSessionInterface,
            @ImageUploaderQualifier imageUploaderUtils: ImageUploaderUtils): UploadImageUseCase<ImageAttachment.Data> {
        return UploadImageUseCase(uploadImageRepository, generateHostRepository, gson, userSession, ImageAttachment.Data::class.java, imageUploaderUtils)
    }
}