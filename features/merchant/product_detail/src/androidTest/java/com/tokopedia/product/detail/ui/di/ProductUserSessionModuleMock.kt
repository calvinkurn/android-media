package com.tokopedia.product.detail.ui.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.product.detail.di.ProductDetailScope
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

/**
 * Created by Yehezkiel on 08/04/21
 */

@Module
class ProductUserSessionModuleMock {

    @Provides
    @ProductDetailScope
    fun provideUserMockSessionInterface(@ApplicationContext context: Context): UserSessionInterface = UserSessionMock(context)

}