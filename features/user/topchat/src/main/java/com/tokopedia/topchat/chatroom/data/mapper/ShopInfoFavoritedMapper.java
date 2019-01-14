//package com.tokopedia.topchat.chatroom.data.mapper;
//
//import com.tokopedia.shop.common.data.source.cloud.model.ShopInfo;
//import com.tokopedia.topchat.chatroom.view.viewmodel.ChatShopInfoViewModel;
//
//import rx.functions.Func1;
//
///**
// * Created by Hendri on 15/08/18.
// */
//public class ShopInfoFavoritedMapper implements Func1<ShopInfo, ChatShopInfoViewModel> {
//    @Override
//    public ChatShopInfoViewModel call(ShopInfo shopInfo) {
//        String favoriteStatus = shopInfo.getInfo().getShopAlreadyFavorited();
//        ChatShopInfoViewModel chatShopInfoViewModel = new ChatShopInfoViewModel();
//        chatShopInfoViewModel.setFavorited(!favoriteStatus.equals("0"));
//        chatShopInfoViewModel.setIsShop(!shopInfo.getInfo().getShopId().equals("0"));
//        return chatShopInfoViewModel;
//    }
//}
