//package com.tokopedia.topchat.chatroom.domain;
//
//import com.tokopedia.shop.common.domain.interactor.GetShopInfoUseCase;
//import com.tokopedia.topchat.chatroom.data.mapper.ShopInfoFavoritedMapper;
//import com.tokopedia.topchat.chatroom.view.viewmodel.ChatShopInfoViewModel;
//import com.tokopedia.usecase.RequestParams;
//import com.tokopedia.usecase.UseCase;
//
//import rx.Observable;
//
///**
// * Created by Hendri on 15/08/18.
// */
//public class GetChatShopInfoUseCase extends UseCase<ChatShopInfoViewModel> {
//    private final GetShopInfoUseCase getShopInfoUseCase;
//
//    public GetChatShopInfoUseCase(GetShopInfoUseCase getShopInfoUseCase) {
//        this.getShopInfoUseCase = getShopInfoUseCase;
//    }
//
//    @Override
//    public Observable<ChatShopInfoViewModel> createObservable(RequestParams requestParams) {
//        return getShopInfoUseCase.createObservable(requestParams).map(new ShopInfoFavoritedMapper());
//    }
//}
