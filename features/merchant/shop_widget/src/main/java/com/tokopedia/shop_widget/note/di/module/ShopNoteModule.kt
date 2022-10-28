package com.tokopedia.shop_widget.note.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import dagger.Provides
import com.tokopedia.shop_widget.note.di.scope.ShopNoteScope
import com.tokopedia.shop_widget.note.view.model.ShopNoteUiModel
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module

@Module(includes = [ShopNoteDetailViewModelModule::class])
class ShopNoteModule {
    @ShopNoteScope
    @Provides
    fun provideShopNoteViewModel(): ShopNoteUiModel {
        return ShopNoteUiModel()
    }

    @ShopNoteScope
    @Provides
    fun provideUserSessionInterface(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }
}