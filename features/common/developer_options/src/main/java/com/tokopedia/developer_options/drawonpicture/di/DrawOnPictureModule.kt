package com.tokopedia.developer_options.drawonpicture.di

import com.tokopedia.developer_options.drawonpicture.DispatcherProvider
import com.tokopedia.developer_options.drawonpicture.ProdDispatcherProvider
import dagger.Module
import dagger.Provides

/**
 * @author by furqan on 01/10/2020
 */
@DrawOnPictureScope
@Module
class DrawOnPictureModule {
    @Provides
    fun provideDispatcherProvider(): DispatcherProvider = ProdDispatcherProvider()
}