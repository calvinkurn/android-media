package com.tokopedia.topchat.chatroom.view.presenter;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.topchat.R;
import com.tokopedia.topchat.chatroom.domain.pojo.chatRoomSettings.ChatSettingsResponse;
import com.tokopedia.topchat.chatroom.view.listener.ChatSettingsInterface;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;

public class ChatSettingsPresenter extends BaseDaggerPresenter<ChatSettingsInterface.View> implements ChatSettingsInterface.Presenter {

    private static final String MESSAGE_ID = "messageID";
    private static final String MSG_ID = "msgId";
    private static final String BLOCK_TYPE = "blockType";
    private static final String BLOKCED = "isBlocked";

    GraphqlUseCase graphqlUseCase;


    @Inject
    public ChatSettingsPresenter(GraphqlUseCase graphqlUseCase) {
        this.graphqlUseCase = graphqlUseCase;
    }


    @Override
    public void initialChatSettings() {
//        if (!isViewAttached()) {
//            return;
//        }
//
//        Map<String, Object> variables = new HashMap<>();
//        variables.put(MSG_ID, Integer.parseInt(getView().getMessageId()));
//        GraphqlRequest graphqlRequest = new
//                GraphqlRequest(GraphqlHelper.loadRawString(getView().getAppContext().getResources(),
//                R.raw.checkchatsettings), ChatSettingsResponse.class, variables);
//
//        graphqlUseCase.clearRequest();
//        graphqlUseCase.addRequest(graphqlRequest);
//
//        graphqlUseCase.execute(new Subscriber<GraphqlResponse>() {
//            @Override
//            public void onCompleted() {
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//
//            }
//
//            @Override
//            public void onNext(GraphqlResponse graphqlResponse) {
//
//                if (graphqlResponse != null) {
//                    ChatSettingsResponse data = graphqlResponse.getData(ChatSettingsResponse.class);
//
//                    getView().setChatSettingPersonalResponse(data.getChatBlockResponse());
//                    getView().setChatSettingPromotionResponse(data.getChatBlockResponse());
//                }
//
//            }
//        });
//
    }

    public void getChatSettingResponse(String messageId, String blockType, boolean state, Subscriber<GraphqlResponse> graphqlResponseSubscriber) {

        if (!isViewAttached()) {
            return;
        }

        Map<String, Object> variables = new HashMap<>();
        variables.put(MESSAGE_ID, messageId);
        variables.put(BLOCK_TYPE, blockType);
        variables.put(BLOKCED, state);

        GraphqlRequest graphqlRequest = new
                GraphqlRequest(GraphqlHelper.loadRawString(getView().getAppContext().getResources(),
                R.raw.chatsettings), ChatSettingsResponse.class, variables);

        graphqlUseCase.clearRequest();
        graphqlUseCase.addRequest(graphqlRequest);
        graphqlUseCase.execute(graphqlResponseSubscriber);

    }

    @Override
    public void onPersonalChatSettingChange(boolean state) {
        getChatSettingResponse(getView().getMessageId(), "1", state, new Subscriber<GraphqlResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                CommonUtils.dumper("error occured" + e);
            }

            @Override
            public void onNext(GraphqlResponse response) {

                if (response != null) {
                    ChatSettingsResponse data = response.getData(ChatSettingsResponse.class);

                    getView().setChatSettingPersonalResponse(data);
                    getView().setChatSettingPromotionResponse(data);
                }
            }
        });
    }

    @Override
    public void onPromotionalChatSettingChange(boolean state) {
        getChatSettingResponse(getView().getMessageId(), "2", state, new Subscriber<GraphqlResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                CommonUtils.dumper("error occured" + e);
            }

            @Override
            public void onNext(GraphqlResponse response) {

                if (response != null) {
                    ChatSettingsResponse data = response.getData(ChatSettingsResponse.class);

                    getView().setChatSettingPromotionResponse(data);
                }
            }
        });
    }
}
