package com.tokopedia.broadcast.message.common.di.module

import com.google.gson.Gson
import com.tokopedia.abstraction.common.data.model.session.UserSession
import com.tokopedia.broadcast.message.common.di.scope.BroadcastMessagePreviewScope
import com.tokopedia.broadcast.message.data.model.ImageAttachment
import com.tokopedia.imageuploader.di.ImageUploaderModule
import com.tokopedia.imageuploader.di.qualifier.ImageUploaderQualifier
import com.tokopedia.imageuploader.domain.GenerateHostRepository
import com.tokopedia.imageuploader.domain.UploadImageRepository
import com.tokopedia.imageuploader.domain.UploadImageUseCase
import com.tokopedia.imageuploader.utils.ImageUploaderUtils
import dagger.Module
import dagger.Provides

@BroadcastMessagePreviewScope
@Module(includes = arrayOf(ImageUploaderModule::class))
class BroadcastMessagePreviewModule{

    @Provides
    fun provideUploadImageUseCase(
            @ImageUploaderQualifier uploadImageRepository: UploadImageRepository,
            @ImageUploaderQualifier generateHostRepository: GenerateHostRepository,
            @ImageUploaderQualifier gson: Gson,
            @ImageUploaderQualifier userSession: UserSession,
            @ImageUploaderQualifier imageUploaderUtils: ImageUploaderUtils): UploadImageUseCase<ImageAttachment.Data> {
        return UploadImageUseCase(uploadImageRepository, generateHostRepository, gson, userSession, ImageAttachment.Data::class.java, imageUploaderUtils)
    }
}