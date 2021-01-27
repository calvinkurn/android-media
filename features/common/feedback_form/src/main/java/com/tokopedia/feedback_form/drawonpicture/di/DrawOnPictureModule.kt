package com.tokopedia.feedback_form.drawonpicture.di

import com.tokopedia.feedback_form.drawonpicture.ProdDispatcherProvider
import com.tokopedia.feedback_form.drawonpicture.DispatcherProvider
import dagger.Module
import dagger.Provides

/**
 * @author by furqan on 01/10/2020
 */
@Module
class DrawOnPictureModule {
    @Provides
    fun provideDispatcherProvider(): DispatcherProvider = ProdDispatcherProvider()
}