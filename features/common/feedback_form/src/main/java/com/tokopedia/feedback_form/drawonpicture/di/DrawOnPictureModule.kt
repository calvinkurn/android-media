package com.tokopedia.feedback_form.drawonpicture.di

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchersProvider
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import dagger.Module
import dagger.Provides

/**
 * @author by furqan on 01/10/2020
 */
@Module
class DrawOnPictureModule {
    @Provides
    fun provideCoroutineDispatchers(): CoroutineDispatchers = CoroutineDispatchersProvider
}