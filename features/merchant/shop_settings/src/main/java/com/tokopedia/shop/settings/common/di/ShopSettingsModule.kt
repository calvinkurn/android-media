package com.tokopedia.shop.settings.common.di

import com.google.gson.Gson
import com.tokopedia.abstraction.common.data.model.session.UserSession
import com.tokopedia.imageuploader.di.ImageUploaderModule
import com.tokopedia.imageuploader.di.qualifier.ImageUploaderQualifier
import com.tokopedia.imageuploader.domain.GenerateHostRepository
import com.tokopedia.imageuploader.domain.UploadImageRepository
import com.tokopedia.imageuploader.domain.UploadImageUseCase
import com.tokopedia.imageuploader.utils.ImageUploaderUtils
import com.tokopedia.shop.settings.basicinfo.data.UploadShopEditImageModel

import dagger.Module
import dagger.Provides

/**
 * @author by furqan on 21/03/18.
 */

@Module(includes = arrayOf(ImageUploaderModule::class))
class ShopSettingsModule {

    @Provides
    fun provideUploadImageUseCase(
            @ImageUploaderQualifier uploadImageRepository: UploadImageRepository,
            @ImageUploaderQualifier generateHostRepository: GenerateHostRepository,
            @ImageUploaderQualifier gson: Gson,
            @ImageUploaderQualifier userSession: UserSession,
            @ImageUploaderQualifier imageUploaderUtils: ImageUploaderUtils): UploadImageUseCase<UploadShopEditImageModel> {
        return UploadImageUseCase(uploadImageRepository, generateHostRepository, gson, userSession, UploadShopEditImageModel::class.java, imageUploaderUtils)
    }
}
