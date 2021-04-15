package com.tokopedia.shop.note.di.module

import android.content.Context
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.shop.R
import com.tokopedia.shop.note.di.scope.ShopNoteScope
import dagger.Provides
import com.tokopedia.shop.common.di.ShopPageContext
import com.tokopedia.shop.note.view.model.ShopNoteUiModel
import com.tokopedia.shop.product.data.GQLQueryConstant.SHOP_NOTES
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import javax.inject.Named

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

    @ShopNoteScope
    @Provides
    @Named(SHOP_NOTES)
    fun provideShopNotesQuery(@ShopPageContext context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, R.raw.gql_get_shop_notes_by_id)
    }
}