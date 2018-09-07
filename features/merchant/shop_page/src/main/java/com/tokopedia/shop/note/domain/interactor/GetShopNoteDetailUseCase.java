package com.tokopedia.shop.note.domain.interactor;

import com.tokopedia.shop.note.data.source.cloud.model.ShopNoteDetail;
import com.tokopedia.shop.note.domain.repository.ShopNoteRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by normansyahputa on 2/8/18.
 */

public class GetShopNoteDetailUseCase extends UseCase<ShopNoteDetail> {

    private static final String SHOP_NOTE_ID = "SHOP_NOTE_ID";

    private ShopNoteRepository shopNoteRepository;

    @Inject
    public GetShopNoteDetailUseCase(ShopNoteRepository shopNoteRepository) {
        super();
        this.shopNoteRepository = shopNoteRepository;
    }

    @Override
    public Observable<ShopNoteDetail> createObservable(RequestParams requestParams) {
        return shopNoteRepository.getShopNoteDetail(requestParams.getString(SHOP_NOTE_ID, ""));
    }

    public static RequestParams createRequestParam(String shopNoteId) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(SHOP_NOTE_ID, shopNoteId);
        return requestParams;
    }
}
