//package com.tokopedia.topchat.chatroom.domain;
//
//
//import com.tokopedia.topchat.chatlist.data.repository.MessageRepository;
//import com.tokopedia.topchat.chatroom.view.viewmodel.SendMessageViewModel;
//import com.tokopedia.usecase.RequestParams;
//import com.tokopedia.usecase.UseCase;
//
//import javax.inject.Inject;
//
//import rx.Observable;
//
///**
// * @author by nisie on 10/25/17.
// */
//
//public class SendMessageUseCase extends UseCase<SendMessageViewModel> {
//
//    private static final String PARAM_MESSAGE = "message";
//    private static final String PARAM_TO_SHOP_ID = "to_shop_id";
//    private static final String PARAM_TO_USER_ID = "to_user_id";
//    private static final String PARAM_SOURCE = "source";
//
//    private MessageRepository messageRepository;
//
//    @Inject
//    public SendMessageUseCase(MessageRepository messageRepository) {
//        super();
//        this.messageRepository = messageRepository;
//    }
//
//    @Override
//    public Observable<SendMessageViewModel> createObservable(RequestParams requestParams) {
//        return messageRepository.sendMessage(requestParams.getParameters());
//    }
//
//
//    public static RequestParams getParam(String message, String toShopId, String
//            toUserId, String source) {
//        RequestParams param = RequestParams.create();
//        if (message != null) {
//            param.putString(PARAM_MESSAGE, message);
//        }
//        if (toShopId != null)
//            param.putString(PARAM_TO_SHOP_ID, toShopId);
//        if (toUserId != null)
//            param.putString(PARAM_TO_USER_ID, toUserId);
//        if (source != null)
//            param.putString(PARAM_SOURCE, source);
//        return param;
//    }
//}
