package com.tokopedia.shop.note.di.module

import android.content.Context
import com.tokopedia.shop.note.di.scope.ShopNoteScope
import dagger.Provides
import com.tokopedia.shop.common.di.ShopPageContext
import com.tokopedia.shop.note.view.model.ShopNoteUiModel
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module

@Module
class ShopNoteModule {
    @ShopNoteScope
    @Provides
    fun provideShopNoteViewModel(): ShopNoteUiModel {
        return ShopNoteUiModel()
    }

    @ShopNoteScope
    @Provides
    fun provideUserSessionInterface(@ShopPageContext context: Context): UserSessionInterface {
        return UserSession(context)
    }
}