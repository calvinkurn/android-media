package com.tokopedia.topchat.chatroom.view.presenter;

import android.content.Intent;
import android.text.TextUtils;

import com.tkpd.library.utils.network.MessageErrorException;
import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.router.TkpdInboxRouter;
import com.tokopedia.core.util.PagingHandler;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.shop.common.domain.interactor.GetShopInfoUseCase;
import com.tokopedia.shop.common.domain.interactor.ToggleFavouriteShopUseCase;
import com.tokopedia.topchat.R;
import com.tokopedia.attachproduct.resultmodel.ResultProduct;
import com.tokopedia.topchat.chatlist.domain.usecase.DeleteMessageListUseCase;
import com.tokopedia.topchat.chatlist.viewmodel.InboxChatViewModel;
import com.tokopedia.topchat.chatroom.data.mapper.WebSocketMapper;
import com.tokopedia.topchat.chatroom.domain.AttachImageUseCase;
import com.tokopedia.topchat.chatroom.domain.GetChatShopInfoUseCase;
import com.tokopedia.topchat.chatroom.domain.GetExistingChatUseCase;
import com.tokopedia.topchat.chatroom.domain.GetReplyListUseCase;
import com.tokopedia.topchat.chatroom.domain.GetUserStatusUseCase;
import com.tokopedia.topchat.chatroom.domain.ReplyMessageUseCase;
import com.tokopedia.topchat.chatroom.domain.SendMessageUseCase;
import com.tokopedia.topchat.chatroom.domain.SendReasonRatingUseCase;
import com.tokopedia.topchat.chatroom.domain.SetChatRatingUseCase;
import com.tokopedia.topchat.chatroom.domain.WebSocketUseCase;
import com.tokopedia.topchat.chatroom.domain.pojo.chatroomsettings.ChatSettingsResponse;
import com.tokopedia.topchat.chatroom.domain.pojo.existingchat.ExistingChatPojo;
import com.tokopedia.topchat.chatroom.domain.pojo.invoicesent.InvoiceLinkPojo;
import com.tokopedia.topchat.chatroom.domain.pojo.rating.SendReasonRatingPojo;
import com.tokopedia.topchat.chatroom.domain.pojo.rating.SetChatRatingPojo;
import com.tokopedia.topchat.chatroom.domain.pojo.replyaction.ReplyActionData;
import com.tokopedia.topchat.chatroom.view.activity.ChatRoomActivity;
import com.tokopedia.topchat.chatroom.view.fragment.ChatRoomFragment;
import com.tokopedia.topchat.chatroom.view.listener.ChatRoomContract;
import com.tokopedia.topchat.chatroom.view.subscriber.ChatRoomDeleteMessageSubsciber;
import com.tokopedia.topchat.chatroom.view.subscriber.ChatRoomGetShopInfoSubscriber;
import com.tokopedia.topchat.chatroom.view.subscriber.GetExistingChatSubscriber;
import com.tokopedia.topchat.chatroom.view.subscriber.GetReplySubscriber;
import com.tokopedia.topchat.chatroom.view.subscriber.GetUserStatusSubscriber;
import com.tokopedia.topchat.chatroom.view.viewmodel.ChatRoomViewModel;
import com.tokopedia.topchat.chatroom.view.viewmodel.SendMessageViewModel;
import com.tokopedia.topchat.chatroom.view.viewmodel.SendableViewModel;
import com.tokopedia.topchat.chatroom.view.viewmodel.imageupload.ImageUploadViewModel;
import com.tokopedia.topchat.chatroom.view.viewmodel.quickreply.QuickReplyListViewModel;
import com.tokopedia.topchat.chatroom.view.viewmodel.quickreply.QuickReplyViewModel;
import com.tokopedia.topchat.chatroom.view.viewmodel.rating.ChatRatingViewModel;
import com.tokopedia.topchat.chattemplate.domain.usecase.GetTemplateUseCase;
import com.tokopedia.topchat.chattemplate.view.viewmodel.GetTemplateViewModel;
import com.tokopedia.topchat.chattemplate.view.viewmodel.TemplateChatModel;
import com.tokopedia.topchat.common.InboxChatConstant;
import com.tokopedia.topchat.common.util.ImageUploadHandlerChat;
import com.tokopedia.topchat.uploadimage.domain.model.UploadImageDomain;
import com.tokopedia.user.session.UserSession;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;

import static com.tokopedia.topchat.common.InboxMessageConstant.PARAM_MESSAGE_ID;
import static com.tokopedia.topchat.common.InboxMessageConstant.PARAM_SENDER_ID;

/**
 * Created by stevenfredian on 9/26/17.
 */

public class ChatRoomPresenter extends BaseDaggerPresenter<ChatRoomContract.View>
        implements ChatRoomContract.Presenter {

    private static final String ROLE_SHOP = "shop";
    private static final String BLOCK_TYPE_PROMOTION = "2";

    private final GetReplyListUseCase getReplyListUseCase;
    private final ReplyMessageUseCase replyMessageUseCase;
    private final AttachImageUseCase attachImageUseCase;
    private final GetTemplateUseCase getTemplateUseCase;
    private final GetExistingChatUseCase getExistingChatUseCase;
    private final SetChatRatingUseCase setChatRatingUseCase;
    private final SendReasonRatingUseCase sendReasonRatingUseCase;
    private final GetUserStatusUseCase getUserStatusUseCase;
    private final DeleteMessageListUseCase deleteMessageListUseCase;
    private final GetChatShopInfoUseCase getChatShopInfoUseCase;
    private final ToggleFavouriteShopUseCase toggleFavouriteShopUseCase;
    private final UserSession userSession;
    private SessionHandler sessionHandler;
    public PagingHandler pagingHandler;
    boolean isRequesting;
    private String magicString;
    private ChatWebSocketListenerImpl listener;
    private boolean flagTyping;
    private int attempt;
    private boolean isFirstTime;
    private ImageUploadHandlerChat imageUploadHandler;
    private String cameraFileLoc;
    private int shopIdFromAPI = 0;
    final static String USER_TAG = "Pengguna";
    final static String ADMIN_TAG = "Administrator";
    final static String OFFICIAL_TAG = "Official";
    public final static String SELLER = "shop";
    public final static String USER_ROLE = "user";
    private SendMessageUseCase sendMessageUseCase;
    private WebSocketUseCase webSocketUseCase;
    private WebSocketMapper webSocketMapper;
    GraphqlUseCase graphqlUseCase;

    @Inject
    ChatRoomPresenter(GetReplyListUseCase getReplyListUseCase,
                      ReplyMessageUseCase replyMessageUseCase,
                      GetTemplateUseCase getTemplateUseCase,
                      SendMessageUseCase sendMessageUseCase,
                      AttachImageUseCase attachImageUseCase,
                      SetChatRatingUseCase setChatRatingUseCase,
                      SendReasonRatingUseCase sendReasonRatingUseCase,
                      SessionHandler sessionHandler,
                      WebSocketMapper webSocketMapper,
                      GetExistingChatUseCase getExistingChatUseCase,
                      UserSession userSession,
                      GetUserStatusUseCase getUserStatusUseCase,
                      DeleteMessageListUseCase deleteMessageListUseCase,
                      GetChatShopInfoUseCase getChatShopInfoUseCase,
                      ToggleFavouriteShopUseCase toggleFavouriteShopUseCase,
                      GraphqlUseCase graphqlUseCase) {
        this.getReplyListUseCase = getReplyListUseCase;
        this.replyMessageUseCase = replyMessageUseCase;
        this.getTemplateUseCase = getTemplateUseCase;
        this.sendMessageUseCase = sendMessageUseCase;
        this.attachImageUseCase = attachImageUseCase;
        this.setChatRatingUseCase = setChatRatingUseCase;
        this.sendReasonRatingUseCase = sendReasonRatingUseCase;
        this.sessionHandler = sessionHandler;
        this.userSession = userSession;
        this.webSocketMapper = webSocketMapper;
        this.getExistingChatUseCase = getExistingChatUseCase;
        this.getUserStatusUseCase = getUserStatusUseCase;
        this.deleteMessageListUseCase = deleteMessageListUseCase;
        this.getChatShopInfoUseCase = getChatShopInfoUseCase;
        this.toggleFavouriteShopUseCase = toggleFavouriteShopUseCase;
        this.graphqlUseCase = graphqlUseCase;
    }

    @Override
    public void attachView(ChatRoomContract.View view) {
        super.attachView(view);
        attempt = 0;
        isRequesting = false;
        this.pagingHandler = new PagingHandler();

        magicString = TkpdBaseURL.CHAT_WEBSOCKET_DOMAIN +
                TkpdBaseURL.Chat.CHAT_WEBSOCKET +
                "?os_type=1" +
                "&device_id=" + GCMHandler.getRegistrationId(getView().getContext()) +
                "&user_id=" + SessionHandler.getLoginID(getView().getContext());
        listener = new ChatWebSocketListenerImpl(getView().getInterface(), webSocketMapper);
        isFirstTime = true;

        imageUploadHandler = ImageUploadHandlerChat.createInstance(getView().getFragment());


        if (getView().needCreateWebSocket()) {
            webSocketUseCase = new WebSocketUseCase(magicString, getView().getUserSession(), listener);
        } else {
            getView().setHeader();
            getView().hideMainLoading();
            getView().displayReplyField(true);
            getView().hideNotifier();
        }
        if (!getView().isChatBot()) {
            getTemplate();
        }
    }

    public void initialChatSettings() {
        if (!isViewAttached()) {
            return;
        }

        Map<String, Object> variables = new HashMap<>();
        variables.put("msgId", Integer.parseInt(getView().getArguments().getString(PARAM_MESSAGE_ID)));
        GraphqlRequest graphqlRequest = new
                GraphqlRequest(getView().getQueryString(R.raw.checkchatsettings), ChatSettingsResponse.class, variables);

        graphqlUseCase.clearRequest();
        graphqlUseCase.addRequest(graphqlRequest);

        graphqlUseCase.execute(new Subscriber<GraphqlResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable throwable) {
                throwable.printStackTrace();
                getView().shouldShowChatSettingsMenu(false);
                NetworkErrorHelper.showEmptyState(getView().getContext(),
                        getView().getRootView(), () -> initialChatSettings());
            }

            @Override
            public void onNext(GraphqlResponse graphqlResponse) {
                if (graphqlResponse != null) {
                    ChatSettingsResponse data = graphqlResponse.getData(ChatSettingsResponse.class);
                    getView().setInboxMessageVisibility(data, data.getChatBlockResponse().getChatBlockStatus().isBlocked());
                    getView().shouldShowChatSettingsMenu(true);
                }

            }
        });

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

    public void onPersonalChatSettingChange(String messageId, boolean state) {

            getChatSettingResponse(messageId, BLOCK_TYPE_PROMOTION, state, new Subscriber<GraphqlResponse>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                }

                @Override
                public void onNext(GraphqlResponse response) {

                    if (response != null) {

                        getView().enableChatSettings();
                    }
                }
            });

    }

    @Override
    public void detachView() {
        super.detachView();
        if (webSocketUseCase != null)
            webSocketUseCase.unsubscribe();
        getReplyListUseCase.unsubscribe();
        getTemplateUseCase.unsubscribe();
        replyMessageUseCase.unsubscribe();
        sendMessageUseCase.unsubscribe();
        attachImageUseCase.unsubscribe();
        setChatRatingUseCase.unsubscribe();
        getExistingChatUseCase.unsubscribe();
        sendReasonRatingUseCase.unsubscribe();
        getUserStatusUseCase.unsubscribe();
        deleteMessageListUseCase.unsubscribe();
        getChatShopInfoUseCase.unsubscribe();
        toggleFavouriteShopUseCase.unsubscribe();
        graphqlUseCase.unsubscribe();
    }

    @Override
    public void onGoToDetail(String id, String role, String source) {
        if (role != null && id != null && !role.equals(ADMIN_TAG.toLowerCase()) && !role.equals
                (OFFICIAL_TAG.toLowerCase())) {
            if (role.equals(SELLER.toLowerCase())
                    && !TextUtils.isEmpty(source)
                    && source.equals(TkpdInboxRouter.SHOP)) {
                getView().finishActivity();
            } else if (role.equals(SELLER.toLowerCase())) {
                Intent intent = ((TkpdInboxRouter) getView().getActivity().getApplicationContext
                        ()).getShopPageIntent(getView().getActivity(), String.valueOf(id));
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                getView().startActivityForResult(intent, ChatRoomFragment.CHAT_GO_TO_SHOP_DETAILS_REQUEST);
            } else {
                if (getView().getActivity().getApplicationContext() instanceof TkpdInboxRouter) {
                    getView().startActivity(
                            ((TkpdInboxRouter) getView().getActivity().getApplicationContext())
                                    .getTopProfileIntent(getView().getContext(), id)
                    );
                }
            }

        }
    }

    private void uploadWithApi(final String path, final ImageUploadViewModel model) {
        String messageId = (getView().getArguments().getString(PARAM_MESSAGE_ID));
        RequestParams params = ReplyMessageUseCase.generateParamAttachImage(messageId, path);

        replyMessageUseCase.execute(params, new Subscriber<ReplyActionData>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable throwable) {
                getView().setUploadingMode(false);
                getView().onErrorUploadImages(
                        ErrorHandler.getErrorMessage(throwable, getView().getActivity()), model);
            }

            @Override
            public void onNext(ReplyActionData data) {
                getView().setUploadingMode(false);
                getView().onSuccessSendAttach(data, model);
            }
        });

    }


    @Override
    public void sendMessage(int networkType) {
        if (isValidReply()) {
            final String reply = (getView().getReplyMessage());
            sendMessage(networkType, reply);
        }
    }

    public void sendMessage(int networkType, final String reply) {
        if (isValidReply(reply)) {
            String startTime = SendableViewModel.generateStartTime();
            getView().addDummyMessage(reply, startTime);
            getView().setViewEnabled(false);
            String messageId = (getView().getArguments().getString(PARAM_MESSAGE_ID));

            if (networkType == InboxChatConstant.MODE_WEBSOCKET) {
                sendReply(messageId, reply, startTime);
            } else if (networkType == InboxChatConstant.MODE_API) {
                RequestParams params = ReplyMessageUseCase.generateParam(messageId, reply);
                replyMessageUseCase.execute(params, new Subscriber<ReplyActionData>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().onErrorSendReply();
                    }

                    @Override
                    public void onNext(ReplyActionData data) {
                        getView().onSuccessSendReply(data, reply);
                    }
                });
            }
        }
    }

    @Override
    public void initMessage(String message, String source, String toShopId, String toUserId) {
        if (isValidReply()) {
            getView().addDummyMessage(message, SendableViewModel.generateStartTime());
            getView().disableAction();
            sendMessageUseCase.execute(SendMessageUseCase.getParam(
                    message,
                    toShopId,
                    toUserId,
                    source
            ), new Subscriber<SendMessageViewModel>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable throwable) {
                    getView().onErrorInitMessage(ErrorHandler.getErrorMessage(throwable));
                }

                @Override
                public void onNext(SendMessageViewModel sendMessageViewModel) {
                    if (sendMessageViewModel.isSuccess())
                        getView().onSuccessInitMessage();
                    else
                        getView().onErrorInitMessage("");

                }
            });
        }
    }

    @Override
    public void openCamera() {
        cameraFileLoc = imageUploadHandler.actionCamera2();
    }

    @Override
    public void startUpload(final List<ImageUploadViewModel> list, final int network) {
        getView().setUploadingMode(true);
        String userId = SessionHandler.getTempLoginSession(getView().getActivity());
        String deviceId = GCMHandler.getRegistrationId(getView().getActivity());
        final String messageId = (getView().getArguments().getString(PARAM_MESSAGE_ID));
        attachImageUseCase.execute(AttachImageUseCase.getParam(list, messageId, userId, deviceId),
                new Subscriber<UploadImageDomain>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        getView().setUploadingMode(false);
                        String error = ErrorHandler.getErrorMessage(throwable, getView()
                                .getActivity());
                        if (throwable instanceof MessageErrorException) {
                            error = throwable.getLocalizedMessage();
                        }
                        getView().onErrorUploadImages(error, list.get(0));
                    }

                    @Override
                    public void onNext(UploadImageDomain uploadImageDomain) {
                        if (network == InboxChatConstant.MODE_WEBSOCKET) {
                            sendImage(messageId, uploadImageDomain.getPicSrc(), list.get(0)
                                    .getStartTime());
                        } else if (network == InboxChatConstant.MODE_API) {
                            uploadWithApi(uploadImageDomain.getPicSrc(), list.get(0));
                        }
                    }
                });
    }

    public void setChatRating(final ChatRatingViewModel element, int userId, final int rating) {
        setChatRatingUseCase.execute(
                SetChatRatingUseCase.
                        getParams(Integer.parseInt(element.getMessageId()),
                                userId, element.getReplyTimeNano(), rating),
                new Subscriber<SetChatRatingPojo>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        getView().onErrorSetRating(com.tokopedia.abstraction.common.utils.network
                                .ErrorHandler.getErrorMessage(getView().getContext(), e));
                    }

                    @Override
                    public void onNext(SetChatRatingPojo setChatRatingPojo) {
                        element.setRatingStatus(rating);
                        getView().onSuccessSetRating(element);

                        if (setChatRatingPojo.getReasons() != null
                                && !setChatRatingPojo.getReasons().isEmpty()) {
                            getView().showReasonRating(element.getMessageId(),
                                    element.getReplyTimeNano(),
                                    setChatRatingPojo.getReasons());
                        }
                    }
                }
        );
    }

    @Override
    public void sendReasonRating(String messageId, long replyTimeNano, String reason) {
        sendReasonRatingUseCase.execute(SendReasonRatingUseCase.getParam(
                Integer.parseInt(messageId),
                Integer.parseInt(userSession.getUserId()),
                reason,
                replyTimeNano),
                new Subscriber<SendReasonRatingPojo>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(SendReasonRatingPojo sendReasonRatingPojo) {

                    }
                }
        );
    }

    @Override
    public String getFileLocFromCamera() {
        return cameraFileLoc;
    }

    public void onLoadMore() {
        if (!isRequesting) {
            pagingHandler.nextPage();
            getReply();
        }
    }

    public void getReply() {
        getReply(InboxChatViewModel.GET_CHAT_MODE);
    }

    @Override
    public void getReply(int mode) {
        RequestParams requestParam;
        if (TextUtils.isEmpty(getView().getArguments().getString(PARAM_MESSAGE_ID))) {
            return;
        }
        if (mode == InboxChatViewModel.GET_CHAT_MODE) {
            requestParam = GetReplyListUseCase.generateParam(
                    getView().getArguments().getString(PARAM_MESSAGE_ID),
                    pagingHandler.getPage());
        } else {
            requestParam = GetReplyListUseCase.generateParamSearch(
                    getView().getArguments().getString(PARAM_MESSAGE_ID));
        }

        isRequesting = true;
        getReplyListUseCase.execute(requestParam, new GetReplySubscriber(getView(), this));
    }

    public void setResult(ChatRoomViewModel replyData) {
        shopIdFromAPI = replyData.getShopId();
        getView().setCanLoadMore(false);
        getView().setHeaderModel(replyData.getNameHeader(), replyData.getImageHeader());
        getView().setHeader();
        if (pagingHandler.getPage() == 1 && replyData.getChatList().size() > 0) {
            getView().getAdapter().setList(replyData.getChatList());
            getView().scrollToBottom();
            getView().hideMainLoading();
            if (replyData.getChatList().get(0)
                    instanceof QuickReplyListViewModel) {
                QuickReplyListViewModel model =
                        (QuickReplyListViewModel) replyData.getChatList().get(0);
                getView().showQuickReplyView(model);
            }
        } else if (replyData.getChatList().size() > 0) {
            getView().getAdapter().addList(replyData.getChatList());
        } else if (replyData.getChatList().size() == 0) {
            getView().hideMainLoading();
        }
        getView().displayReplyField(replyData.getTextAreaReply() == 1);
        getView().setCanLoadMore(replyData.isHasNext());

        if (!replyData.isHasNext() && !getView().isChatBot()) {
            getView().addTimeMachine();
        }
    }

    public void finishRequest() {
        isRequesting = false;
    }

    private boolean isValidReply() {
        return isValidReply(getView().getReplyMessage().trim());
    }

    private boolean isValidReply(String message) {
        if (message.trim().length() == 0) {
            getView().showSnackbarError(getView().getString(R.string.error_empty_report));
            return false;
        }
        return true;
    }

    public void onRefresh() {
        if (!isRequesting) {
            pagingHandler.resetPage();
            getReply();
        } else {
//            getView().getRefreshHandler().finishRefresh();
        }
    }

    public void sendQuickReply(String messageId, QuickReplyViewModel quickReplyViewModel, String
            startTime) {
        webSocketUseCase.execute(webSocketUseCase.getParamSendQuickReply(messageId,
                quickReplyViewModel, startTime));
    }

    public void sendInvoiceAttachment(String messageId, InvoiceLinkPojo invoice, String startTime) {
        webSocketUseCase.execute(webSocketUseCase.getParamSendInvoiceAttachment(messageId,
                invoice, startTime));
    }

    public void sendProductAttachment(String messageId, ResultProduct product, String startTime) {
        webSocketUseCase.execute(webSocketUseCase.getParamSendProductAttachment(messageId,
                product, startTime));
    }

    public void sendReply(String messageId, String reply, String startTime) {
        webSocketUseCase.execute(webSocketUseCase.getParamSendReply(messageId, reply, startTime));
        flagTyping = false;
    }

    public void sendImage(String messageId, String path, String startTime) {
        getView().setUploadingMode(false);
        webSocketUseCase.execute(webSocketUseCase.getParamSendImage(messageId, path, startTime));
        flagTyping = false;

    }

    public void readMessage(String messageId) {
        webSocketUseCase.execute(webSocketUseCase.getReadMessage(messageId));
    }

    public void setIsTyping(String messageId) {
        if (!flagTyping && messageId != null) {
            webSocketUseCase.execute(webSocketUseCase.getParamStartTyping(messageId));
            flagTyping = true;
        }
    }

    public void stopTyping(String messageId) {
        if (messageId != null) {
            webSocketUseCase.execute(webSocketUseCase.getParamStopTyping(messageId));
            flagTyping = false;
        }
    }

    @Override
    public void getAttachProductDialog(String shopId, String shopName, String senderRole) {
        String id = "0";
        String shopNameLocal = "";
        if (senderRole.equals(ROLE_SHOP) && !TextUtils.isEmpty(shopId)) {
            id = String.valueOf(shopId);
            shopNameLocal = shopName;
        } else if (TextUtils.isEmpty(shopId) && this.shopIdFromAPI != 0) {
            id = String.valueOf(this.shopIdFromAPI);
            shopNameLocal = shopName;
        } else if (!TextUtils.isEmpty(sessionHandler.getShopID())
                && !sessionHandler.getShopID().equals("0")) {
            id = sessionHandler.getShopID();
            shopNameLocal = sessionHandler.getShopName();
        }

        getView().startAttachProductActivity(id, shopNameLocal, senderRole.equals(ROLE_SHOP));
    }

    @Override
    public void onOpenWebSocket() {
        if (isFirstTime) {
            isFirstTime = false;
            String messageId = (getView().getArguments().getString(PARAM_MESSAGE_ID));
            readMessage(messageId);
        }
    }

    @Override
    public void closeWebSocket() {
        if (webSocketUseCase != null) {
            webSocketUseCase.closeConnection();
        }
    }

    public void getTemplate() {
        getTemplateUseCase.execute(GetTemplateUseCase.generateParam(),
                new Subscriber<GetTemplateViewModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().setTemplate(null);
                    }

                    @Override
                    public void onNext(GetTemplateViewModel getTemplateViewModel) {
                        if (getTemplateViewModel.isEnabled()) {
                            List<Visitable> temp = getTemplateViewModel.getListTemplate();
                            if (temp == null) temp = new ArrayList<>();
                            if (getView().isAllowedTemplate())
                                temp.add(new TemplateChatModel(false));
                            getView().setTemplate(temp);
                        } else {
                            List<Visitable> temp = new ArrayList<>();
                            if (getView().isAllowedTemplate())
                                temp.add(new TemplateChatModel(false));
                            getView().setTemplate(temp);
                        }
                    }
                });
    }

    public void createWebSocketIfNull() {
        if (webSocketUseCase == null) {
            webSocketUseCase = new WebSocketUseCase(magicString, getView().getUserSession(), listener);
        }
    }

    public void recreateWebSocket() {
        webSocketUseCase.recreateWebSocket();
    }

    @Override
    public void getExistingChat() {
        RequestParams requestParam;
        if (!TextUtils.isEmpty(getView().getArguments().getString(PARAM_MESSAGE_ID))) {
            ExistingChatPojo pojo = new ExistingChatPojo();
            pojo.setMsgId(getView().getArguments().getString(PARAM_MESSAGE_ID));
            new GetExistingChatSubscriber(getView(), this).onNext(pojo);
            return;
        }
        boolean isUserToShop = getView().getArguments().getString(ChatRoomActivity
                .PARAM_SENDER_TAG).equals(ChatRoomActivity.ROLE_SELLER);
        String destinationId = "";
        if (isUserToShop) {
            destinationId = getView().getArguments().getString(PARAM_SENDER_ID);
        } else {
            destinationId = getView().getArguments().getString(ChatRoomActivity.PARAM_USER_ID);
        }
        String source = getView().getArguments().getString(ChatRoomActivity.PARAM_SOURCE);

        requestParam = GetExistingChatUseCase.generateParam(isUserToShop, destinationId, source);
        getExistingChatUseCase.execute(requestParam, new GetExistingChatSubscriber(getView(), this));
    }

    @Override
    public void getUserStatus(String userId, String role) {
        if (role != null) {
            if (role.equalsIgnoreCase(SELLER)) {
                getUserStatusUseCase.setUser(false);
                getUserStatusUseCase.execute(GetUserStatusUseCase.getRequestParam(userId),
                        new GetUserStatusSubscriber(this, getView()));
            } else if (role.equalsIgnoreCase(USER_ROLE)) {
                getUserStatusUseCase.setUser(true);
                getUserStatusUseCase.execute(GetUserStatusUseCase.getRequestParam(userId),
                        new GetUserStatusSubscriber(this, getView()));
            }
        }
    }

    @Override
    public void deleteChat(String messageId) {
        if (!TextUtils.isEmpty(messageId) && TextUtils.isDigitsOnly(messageId)) {
            deleteMessageListUseCase.execute(DeleteMessageListUseCase.generateParam(messageId), new
                    ChatRoomDeleteMessageSubsciber(getView()));
        }
    }

    @Override
    public void getFollowStatus(String shopId) {
        if (getChatShopInfoUseCase != null && !TextUtils.isEmpty(shopId)) {
            getChatShopInfoUseCase.execute(GetShopInfoUseCase.createRequestParam(shopId),
                    new ChatRoomGetShopInfoSubscriber(this, getView()));
        }
    }

    @Override
    public void doFollowUnfollowToggle(String shopId) {
        getView().hideMainLoading();
        toggleFavouriteShopUseCase.execute(ToggleFavouriteShopUseCase.createRequestParam(shopId),
                new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (aBoolean) getView().toggleFollowSuccess();
                    }
                });
    }
}
