package com.tokopedia.topchat.chatroom.view.presenter;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.design.component.ToasterNormal;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.topchat.R;
import com.tokopedia.topchat.chatroom.domain.pojo.chatRoomSettings.ChatSettingsResponse;
import com.tokopedia.topchat.chatroom.view.listener.ChatSettingsInterface;
import com.tokopedia.topchat.common.InboxChatConstant;
import com.tokopedia.topchat.common.analytics.ChatSettingsAnalytics;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;

public class ChatSettingsPresenter extends BaseDaggerPresenter<ChatSettingsInterface.View> implements ChatSettingsInterface.Presenter {


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
                GraphqlRequest(GraphqlHelper.loadRawString(getView().getAppContext().getResources(),
                R.raw.chatsettings), ChatSettingsResponse.class, variables);

        graphqlUseCase.clearRequest();
        graphqlUseCase.addRequest(graphqlRequest);
        graphqlUseCase.execute(graphqlResponseSubscriber);

    }

    @Override
    public void onPersonalChatSettingChange(boolean state, boolean initialState) {
        if (!isViewAttached()) {
            return;
        }
        if (chatSettingsResponse != null && chatSettingsResponse.getChatBlockResponse().getChatBlockStatus().isBlocked() == state) {
            if (initialState) {
                getView().showProgressBar();
            }
            getChatSettingResponse(getView().getMessageId(), "1", state, new Subscriber<GraphqlResponse>() {
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
                    ToasterNormal.show(getView().getActivity(), getView().getActivity().getResources().getString(R.string.error_chat_message));
                    getView().updateChatSettingResponse(chatSettingsResponse);
                    CommonUtils.dumper("error occured" + e);
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
                            getView().showPersonalToast(state);
                        }
                    }
                }
            });

            if (state) {
                chatSettingsAnalytics.sendTrackingEvent(ChatSettingsAnalytics.CHAT_SETTINGS_CATEGORY, ChatSettingsAnalytics.CHAT_UNBLOCK_ACTION, ChatSettingsAnalytics.CHAT_PERSONAL_LABEL);
            } else {
                chatSettingsAnalytics.sendTrackingEvent(ChatSettingsAnalytics.CHAT_SETTINGS_CATEGORY, ChatSettingsAnalytics.CHAT_BLOCK_ACTION, ChatSettingsAnalytics.CHAT_PERSONAL_LABEL);
            }
        }
    }

    @Override
    public void onPromotionalChatSettingChange(boolean state, boolean initialState) {
        if (!isViewAttached()) {
            return;
        }
        if (chatSettingsResponse != null && chatSettingsResponse.getChatBlockResponse().getChatBlockStatus().isPromoBlocked() == state) {
            if (initialState) {
                getView().showProgressBar();
            }
            getChatSettingResponse(getView().getMessageId(), "2", state, new Subscriber<GraphqlResponse>() {
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
                    ToasterNormal.show(getView().getActivity(), getView().getActivity().getResources().getString(R.string.error_chat_message));
                    CommonUtils.dumper("error occured" + e);
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
                            getView().showPromotionToast(state);
                        }
                    }
                }
            });

            if (state) {
                chatSettingsAnalytics.sendTrackingEvent(ChatSettingsAnalytics.CHAT_SETTINGS_CATEGORY, ChatSettingsAnalytics.CHAT_UNBLOCK_ACTION, ChatSettingsAnalytics.CHAT_PROMOTION_LABEL);
            } else {
                chatSettingsAnalytics.sendTrackingEvent(ChatSettingsAnalytics.CHAT_SETTINGS_CATEGORY, ChatSettingsAnalytics.CHAT_BLOCK_ACTION, ChatSettingsAnalytics.CHAT_PROMOTION_LABEL);
            }
        }
    }
}
