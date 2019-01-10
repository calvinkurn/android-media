//package com.tokopedia.topchat.chatroom.view.subscriber;
//
//import com.tokopedia.topchat.R;
//import com.tokopedia.topchat.chatlist.viewmodel.DeleteChatListViewModel;
//import com.tokopedia.topchat.chatlist.viewmodel.DeleteChatViewModel;
//import com.tokopedia.topchat.chatroom.view.listener.ChatRoomContract;
//
//import rx.Subscriber;
//
///**
// * Created by Hendri on 15/08/18.
// */
//public class ChatRoomDeleteMessageSubsciber extends Subscriber<DeleteChatListViewModel> {
//    private final ChatRoomContract.View view;
//
//    public ChatRoomDeleteMessageSubsciber(ChatRoomContract
//            .View view) {
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
//        view.showError(e.getMessage());
//    }
//
//    @Override
//    public void onNext(DeleteChatListViewModel deleteChatListViewModel) {
//        if(deleteChatListViewModel != null &&
//                deleteChatListViewModel.getList() != null &&
//                deleteChatListViewModel.getList().size() == 1) {
//            DeleteChatViewModel deleteChatViewModel = deleteChatListViewModel.getList().get(0);
//            if(deleteChatViewModel.getIsSuccess() != 0){
//                view.successDeleteChat();
//            } else {
//                this.onError(new Throwable(deleteChatViewModel.getDetailResponse()));
//            }
//        } else {
//            this.onError(new Throwable(view.getContext().getString(R.string.delete_chat_default_error_message)));
//        }
//    }
//}
