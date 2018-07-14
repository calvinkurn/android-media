package com.tokopedia.shop.info.di.module;

import com.tokopedia.shop.info.di.scope.ShopInfoScope;
import com.tokopedia.shop.note.data.repository.ShopNoteRepositoryImpl;
import com.tokopedia.shop.note.data.source.ShopNoteDataSource;
import com.tokopedia.shop.note.domain.repository.ShopNoteRepository;
import com.tokopedia.shop.note.view.model.ShopNoteViewModel;

import dagger.Module;
import dagger.Provides;

@ShopInfoScope
@Module
public class ShopInfoModule {
    @ShopInfoScope
    @Provides
    public ShopNoteRepository provideShopNoteRepository(ShopNoteDataSource shopNoteDataSource){
        return new ShopNoteRepositoryImpl(shopNoteDataSource);
    }

    @ShopInfoScope
    @Provides
    public ShopNoteViewModel provideShopNoteViewModel(){
        return new ShopNoteViewModel();
    }
}

