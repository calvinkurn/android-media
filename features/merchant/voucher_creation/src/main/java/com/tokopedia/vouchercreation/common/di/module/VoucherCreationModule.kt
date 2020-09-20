package com.tokopedia.vouchercreation.common.di.module

import android.content.Context
import com.google.gson.Gson
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.imageuploader.di.ImageUploaderModule
import com.tokopedia.imageuploader.di.qualifier.ImageUploaderQualifier
import com.tokopedia.imageuploader.domain.GenerateHostRepository
import com.tokopedia.imageuploader.domain.UploadImageRepository
import com.tokopedia.imageuploader.domain.UploadImageUseCase
import com.tokopedia.imageuploader.utils.ImageUploaderUtils
import com.tokopedia.permissionchecker.PermissionCheckerHelper
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.vouchercreation.common.coroutines.CoroutineDispatchers
import com.tokopedia.vouchercreation.common.coroutines.CoroutineDispatchersProvider
import com.tokopedia.vouchercreation.common.di.scope.VoucherCreationScope
import com.tokopedia.vouchercreation.create.domain.model.upload.ImageUploadResponse
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module(includes = [ImageUploaderModule::class])
class VoucherCreationModule {

    @VoucherCreationScope
    @Provides
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @VoucherCreationScope
    @Provides
    fun provideGraphqlRepository() = GraphqlInteractor.getInstance().graphqlRepository

    @VoucherCreationScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface = UserSession(context)

    @Provides
    fun provideUploadImageUseCase(
            @ImageUploaderQualifier uploadImageRepository: UploadImageRepository,
            @ImageUploaderQualifier generateHostRepository: GenerateHostRepository,
            @ImageUploaderQualifier gson: Gson,
            @ImageUploaderQualifier userSession: UserSessionInterface,
            @ImageUploaderQualifier imageUploaderUtils: ImageUploaderUtils): UploadImageUseCase<ImageUploadResponse.ImageUploadData> {
        return UploadImageUseCase(uploadImageRepository, generateHostRepository, gson, userSession, ImageUploadResponse.ImageUploadData::class.java, imageUploaderUtils)
    }

    @VoucherCreationScope
    @Provides
    fun provideCoroutineDispatchers(): CoroutineDispatchers = CoroutineDispatchersProvider

    @VoucherCreationScope
    @Provides
    fun providePermissionCheckerHelper(): PermissionCheckerHelper = PermissionCheckerHelper()
}