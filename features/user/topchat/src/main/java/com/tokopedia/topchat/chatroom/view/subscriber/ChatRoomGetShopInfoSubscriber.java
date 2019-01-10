//package com.tokopedia.topchat.chatroom.view.subscriber;
//
//import com.tokopedia.topchat.chatroom.view.listener.ChatRoomContract;
//import com.tokopedia.topchat.chatroom.view.viewmodel.ChatShopInfoViewModel;
//
//import rx.Subscriber;
//
///**
// * Created by Hendri on 15/08/18.
// */
//public class ChatRoomGetShopInfoSubscriber extends Subscriber<ChatShopInfoViewModel>{
//    ChatRoomContract.Presenter presenter;
//    ChatRoomContract.View view;
//
//    public ChatRoomGetShopInfoSubscriber(ChatRoomContract.Presenter presenter, ChatRoomContract
//            .View view) {
//        this.presenter = presenter;
//        this.view = view;
//    }
//
//    @Override
//    public void onCompleted() {
//
//    }
//
//    @Override
//    public void onError(Throwable e) {
//        e.printStackTrace();
//    }
//
//    @Override
//    public void onNext(ChatShopInfoViewModel chatShopInfoViewModel) {
//        view.setChatShopInfoData(chatShopInfoViewModel);
//        view.setMenuVisible(true);
//    }
//}
