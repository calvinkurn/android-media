package com.tokopedia.imagepicker.editor.di.module

import com.tokopedia.imagepicker.editor.data.NetworkServices
import com.tokopedia.imagepicker.editor.di.scope.ImageEditorQualifier
import com.tokopedia.imagepicker.editor.di.scope.ImageEditorScope
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit

@Module(includes = [NetworkModule::class])
class ImageEditorModule {

    @Provides
    @ImageEditorScope
    fun provideImageUploaderServices(
        @ImageEditorQualifier retrofit: Retrofit.Builder,
        @ImageEditorQualifier okHttpClient: OkHttpClient.Builder
    ): NetworkServices {
        val services = retrofit.client(okHttpClient.build()).build()
        return services.create(NetworkServices::class.java)
    }

}