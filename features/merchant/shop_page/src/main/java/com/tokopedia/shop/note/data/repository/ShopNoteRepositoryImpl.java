package com.tokopedia.shop.note.data.repository;

import com.tokopedia.shop.note.data.source.ShopNoteDataSource;
import com.tokopedia.shop.note.data.source.cloud.model.ShopNote;
import com.tokopedia.shop.note.data.source.cloud.model.ShopNoteDetail;
import com.tokopedia.shop.note.domain.repository.ShopNoteRepository;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author hendry on 4/20/17.
 */

public class ShopNoteRepositoryImpl implements ShopNoteRepository {
    private final ShopNoteDataSource shopNoteDataSource;

    @Inject
    public ShopNoteRepositoryImpl(ShopNoteDataSource shopNoteDataSource) {
        this.shopNoteDataSource = shopNoteDataSource;
    }

    @Override
    public Observable<List<ShopNote>> getShopNoteList(String shopId) {
        return shopNoteDataSource.getShopNoteList(shopId);
    }

    @Override
    public Observable<ShopNoteDetail> getShopNoteDetail(String shopNoteId) {
        return shopNoteDataSource.getShopNoteDetail(shopNoteId);
    }

}
