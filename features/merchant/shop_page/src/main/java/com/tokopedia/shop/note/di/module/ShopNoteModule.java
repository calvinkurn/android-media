package com.tokopedia.shop.note.di.module;

import android.content.Context;

import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.shop.R;
import com.tokopedia.shop.common.di.ShopPageContext;
import com.tokopedia.shop.note.di.scope.ShopNoteScope;
import com.tokopedia.shop.note.view.model.ShopNoteViewModel;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

import static com.tokopedia.shop.product.data.GQLQueryConstant.SHOP_NOTES;

@ShopNoteScope
@Module
public class ShopNoteModule {

    @ShopNoteScope
    @Provides
    public ShopNoteViewModel provideShopNoteViewModel(){
        return new ShopNoteViewModel();
    }

    @ShopNoteScope
    @Provides
    public UserSessionInterface provideUserSessionInterface(@ShopPageContext Context context) {
        return new UserSession(context);
    }

    @ShopNoteScope
    @Provides
    @Named(SHOP_NOTES)
    public String provideShopNotesQuery(@ShopPageContext Context context) {
        return GraphqlHelper.loadRawString(context.getResources(), R.raw.gql_get_shop_notes_by_id);
    }
}

