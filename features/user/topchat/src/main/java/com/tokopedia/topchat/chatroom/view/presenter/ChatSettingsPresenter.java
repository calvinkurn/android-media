package com.tokopedia.topchat.chatroom.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.topchat.R;
import com.tokopedia.topchat.chatroom.domain.pojo.chatroomsettings.ChatSettingsResponse;
import com.tokopedia.topchat.chatroom.view.listener.ChatSettingsInterface;
import com.tokopedia.topchat.common.InboxChatConstant;
import com.tokopedia.topchat.common.analytics.ChatSettingsAnalytics;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;

public class ChatSettingsPresenter extends BaseDaggerPresenter<ChatSettingsInterface.View> implements ChatSettingsInterface.Presenter {

    private static final String BLOCK_TYPE_PERSONAL = "1";
    private static final String BLOCK_TYPE_PROMOTION = "2";

    GraphqlUseCase graphqlUseCase;
    private ChatSettingsResponse chatSettingsResponse;
    private ChatSettingsAnalytics chatSettingsAnalytics;


    @Inject
    public ChatSettingsPresenter(GraphqlUseCase graphqlUseCase, ChatSettingsAnalytics chatSettingsAnalytics) {
        this.graphqlUseCase = graphqlUseCase;
        this.chatSettingsAnalytics = chatSettingsAnalytics;
    }


    @Override
    public void initialChatSettings(ChatSettingsResponse chatSettingsResponse) {
        this.chatSettingsResponse = chatSettingsResponse;
    }

    private void getChatSettingResponse(String messageId, String blockType, boolean state, Subscriber<GraphqlResponse> graphqlResponseSubscriber) {

        if (!isViewAttached()) {
            return;
        }

        Map<String, Object> variables = new HashMap<>();
        variables.put(InboxChatConstant.MSG_ID, messageId);
        variables.put(InboxChatConstant.BLOCK_TYPE, blockType);
        variables.put(InboxChatConstant.BLOKCED, !state);

        GraphqlRequest graphqlRequest = new
                GraphqlRequest(getView().getQueryString(R.raw.chatsettings), ChatSettingsResponse.class, variables);

        graphqlUseCase.clearRequest();
        graphqlUseCase.addRequest(graphqlRequest);
        graphqlUseCase.execute(graphqlResponseSubscriber);

    }

    @Override
    public void onPersonalChatSettingChange(String messageId, boolean state, boolean initialState, String shopId) {
        if (!isViewAttached()) {
            return;
        }
        if (chatSettingsResponse != null && chatSettingsResponse.getChatBlockResponse().getChatBlockStatus().isBlocked() == state) {
            if (initialState) {
                getView().showProgressBar();
            }
            getChatSettingResponse(messageId, BLOCK_TYPE_PERSONAL, state, new Subscriber<GraphqlResponse>() {
                @Override
                public void onCompleted() {
                    if (!isViewAttached()) {
                        return;
                    }
                }

                @Override
                public void onError(Throwable e) {
                    if (!isViewAttached()) {
                        return;
                    }
                    getView().hideProgressBar();
                    getView().updateChatSettingResponse(chatSettingsResponse);
                    getView().showErrorMessage();
                }

                @Override
                public void onNext(GraphqlResponse response) {
                    if (!isViewAttached()) {
                        return;
                    }

                    if (response != null) {
                        ChatSettingsResponse data = response.getData(ChatSettingsResponse.class);
                        chatSettingsResponse = data;
                        getView().updateChatSettingResponse(chatSettingsResponse);
                        getView().setChatSettingPersonalResponse(data);
                        getView().setPromotionViewOpacity(chatSettingsResponse.getChatBlockResponse().getChatBlockStatus().isBlocked());
                        if (initialState) {
                            getView().showToastMessage();
                            getView().hideProgressBar();
                            getView().setPersonalToast(state);
                        }
                    }
                }
            });

            if (state) {
                chatSettingsAnalytics.sendTrackingUnblockPersonal(shopId);
            } else {
                chatSettingsAnalytics.sendTrackingBlockPersonal(shopId);
            }
        }
    }

    @Override
    public void onPromotionalChatSettingChange(String messageId, boolean state, boolean initialState, String shopId) {
        if (!isViewAttached()) {
            return;
        }
        if (chatSettingsResponse != null && chatSettingsResponse.getChatBlockResponse().getChatBlockStatus().isPromoBlocked() == state) {
            if (initialState) {
                getView().showProgressBar();
            }
            getChatSettingResponse(messageId, BLOCK_TYPE_PROMOTION, state, new Subscriber<GraphqlResponse>() {
                @Override
                public void onCompleted() {
                    if (!isViewAttached()) {
                        return;
                    }
                }

                @Override
                public void onError(Throwable e) {
                    if (!isViewAttached()) {
                        return;
                    }
                    getView().hideProgressBar();
                    getView().updateChatSettingResponse(chatSettingsResponse);
                    getView().showErrorMessage();
                }

                @Override
                public void onNext(GraphqlResponse response) {
                    if (!isViewAttached()) {
                        return;
                    }
                    if (response != null) {
                        ChatSettingsResponse data = response.getData(ChatSettingsResponse.class);
                        chatSettingsResponse = data;
                        getView().updateChatSettingResponse(chatSettingsResponse);
                        getView().setChatSettingPromotionResponse(data);
                        if (initialState) {
                            getView().hideProgressBar();
                            getView().showToastMessage();
                            getView().setPromotionToast(state);
                        }
                    }
                }
            });

            if (state) {
                chatSettingsAnalytics.sendTrackingUnblockPromotion(shopId);
            } else {
                chatSettingsAnalytics.sendTrackingBlockPromotion(shopId);
            }
        }
    }
}
