//package com.tokopedia.topchat.chatroom.view.subscriber;
//
//import android.text.TextUtils;
//
//import com.tokopedia.core.network.retrofit.response.ErrorHandler;
//import com.tokopedia.topchat.chatlist.viewmodel.InboxChatViewModel;
//import com.tokopedia.topchat.chatroom.domain.pojo.existingchat.ExistingChatPojo;
//import com.tokopedia.topchat.chatroom.view.listener.ChatRoomContract;
//import com.tokopedia.topchat.chatroom.view.presenter.ChatRoomPresenter;
//
//import rx.Subscriber;
//
//import static com.tokopedia.topchat.common.InboxMessageConstant.PARAM_MODE;
//
///**
// * Created by Hendri on 11/06/18.
// */
//public class GetExistingChatSubscriber extends Subscriber<ExistingChatPojo> {
//
//    private final ChatRoomPresenter presenter;
//    private ChatRoomContract.View view;
//
//    public GetExistingChatSubscriber(ChatRoomContract.View view, ChatRoomPresenter presenter) {
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
//        view.setViewEnabled(true);
//        view.showError(ErrorHandler.getErrorMessage(e));
//        view.hideMainLoading();
//        presenter.finishRequest();
//    }
//
//    @Override
//    public void onNext(ExistingChatPojo existingChatPojo) {
//        if(!TextUtils.isEmpty(existingChatPojo.getMsgId())) {
//            view.setMessageId(existingChatPojo.getMsgId());
//            view.enableWebSocket();
//            presenter.createWebSocketIfNull();
//            presenter.getReply(view.getArguments().getInt(PARAM_MODE, InboxChatViewModel
//                    .GET_CHAT_MODE));
//        } else {
//            view.setViewEnabled(true);
//            view.hideMainLoading();
//        }
//    }
//}
