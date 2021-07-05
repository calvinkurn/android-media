package com.tokopedia.rechargeocr.di

import android.content.Context
import com.google.gson.Gson
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.imageuploader.di.ImageUploaderModule
import com.tokopedia.imageuploader.di.qualifier.ImageUploaderQualifier
import com.tokopedia.imageuploader.domain.GenerateHostRepository
import com.tokopedia.imageuploader.domain.UploadImageRepository
import com.tokopedia.imageuploader.domain.UploadImageUseCase
import com.tokopedia.imageuploader.utils.ImageUploaderUtils
import com.tokopedia.utils.permission.PermissionCheckerHelper
import com.tokopedia.rechargeocr.analytics.RechargeCameraAnalytics
import com.tokopedia.rechargeocr.data.RechargeUploadImageResponse
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module(includes = [ImageUploaderModule::class])
class RechargeCameraModule {

    @RechargeCameraScope
    @Provides
    fun providePermissionCheckerHelper(): PermissionCheckerHelper {
        return PermissionCheckerHelper()
    }

    @RechargeCameraScope
    @Provides
    fun provideUploadImageUseCase(@ImageUploaderQualifier uploadImageRepository: UploadImageRepository,
                                  @ImageUploaderQualifier generateHostRepository: GenerateHostRepository,
                                  @ImageUploaderQualifier gson: Gson,
                                  @ImageUploaderQualifier userSession: UserSessionInterface,
                                  @ImageUploaderQualifier imageUploaderUtils: ImageUploaderUtils)
            : UploadImageUseCase<RechargeUploadImageResponse> {
        return UploadImageUseCase(uploadImageRepository, generateHostRepository, gson, userSession,
                RechargeUploadImageResponse::class.java, imageUploaderUtils)
    }

    @RechargeCameraScope
    @Provides
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @RechargeCameraScope
    @Provides
    fun provideGraphqlRepository(): GraphqlRepository {
        return GraphqlInteractor.getInstance().graphqlRepository
    }

    @RechargeCameraScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface = UserSession(context)

    @RechargeCameraScope
    @Provides
    fun provideRechargeCameraAnalytics(): RechargeCameraAnalytics = RechargeCameraAnalytics()
}