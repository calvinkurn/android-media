//package com.tokopedia.topchat.chatlist.data.repository;
//
//import com.tokopedia.topchat.chatroom.data.mapper.SendMessageMapper;
//import com.tokopedia.topchat.chatroom.view.viewmodel.SendMessageViewModel;
//import com.tokopedia.topchat.common.chat.api.ChatApi;
//
//import java.util.HashMap;
//
//import javax.inject.Inject;
//
//import rx.Observable;
//
///**
// * @author by nisie on 10/25/17.
// */
//
//public class SendMessageSource {
//    private ChatApi chatApi;
//    private SendMessageMapper sendMessageMapper;
//
//    @Inject
//    public SendMessageSource(ChatApi chatApi, SendMessageMapper sendMessageMapper) {
//        this.chatApi = chatApi;
//        this.sendMessageMapper = sendMessageMapper;
//    }
//
////    public Observable<SendMessageViewModel> sendMessage(HashMap<String, Object> requestParams) {
////        return chatApi
////                .sendMessage(requestParams)
////                .map(sendMessageMapper);
////    }
//}
