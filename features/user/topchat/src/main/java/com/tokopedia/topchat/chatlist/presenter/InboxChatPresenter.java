package com.tokopedia.topchat.chatlist.presenter;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.util.Pair;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.broadcast.message.common.data.model.TopChatBlastSellerMetaData;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.broadcast.message.common.domain.interactor.GetChatBlastSellerMetaDataUseCase;
import com.tokopedia.abstraction.common.utils.paging.PagingHandler;
import com.tokopedia.network.constant.TkpdBaseURL;
import com.tokopedia.topchat.R;
import com.tokopedia.topchat.chatlist.data.TopChatUrl;
import com.tokopedia.topchat.chatlist.domain.usecase.DeleteMessageListUseCase;
import com.tokopedia.topchat.chatlist.domain.usecase.GetMessageListUseCase;
import com.tokopedia.topchat.chatlist.domain.usecase.SearchMessageUseCase;
import com.tokopedia.topchat.chatlist.listener.InboxChatContract;
import com.tokopedia.topchat.chatlist.subscriber.DeleteMessageSubscriber;
import com.tokopedia.topchat.chatlist.subscriber.GetMessageSubscriber;
import com.tokopedia.topchat.chatlist.subscriber.SearchMessageSubscriber;
import com.tokopedia.topchat.chatlist.viewmodel.ChatListViewModel;
import com.tokopedia.topchat.chatlist.viewmodel.InboxChatViewModel;
import com.tokopedia.topchat.chatlist.data.mapper.WebSocketMapper;
import com.tokopedia.topchat.common.InboxMessageConstant;
import com.tokopedia.topchat.common.TopChatRouter;
import com.tokopedia.topchat.common.analytics.TopChatAnalytics;
import com.tokopedia.topchat.chatroom.view.activity.TopChatRoomActivity;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import rx.Subscriber;

import static com.tokopedia.topchat.chatlist.domain.usecase.SearchMessageUseCase.PARAM_BY_REPLY;

/**
 * Created by stevenfredian on 9/14/17.
 */

public class InboxChatPresenter extends BaseDaggerPresenter<InboxChatContract.View>
        implements InboxChatContract.Presenter, InboxMessageConstant {

    private final UserSessionInterface userSession;
    private final WebSocketMapper webSocketMapper;
    private final TopChatAnalytics analytics;
    private GetMessageListUseCase getMessageListUseCase;
    private SearchMessageUseCase searchMessageUseCase;
    private DeleteMessageListUseCase deleteMessageListUseCase;
    private PagingHandler pagingHandler;
    private GetChatBlastSellerMetaDataUseCase getChatBlastSellerMetaDataUseCase;
    private boolean isRequesting;
    private InboxChatViewModel viewModel;
    private int contactSize;
    private int chatSize;
    private List<Visitable> listFetchCache;
    private OkHttpClient client;
    private ChatWebSocketListenerImpl listener;
    private WebSocket ws;
    private int attempt;
    private boolean inActionMode;
    private CountDownTimer countDownTimer;

    @Inject
    InboxChatPresenter(GetMessageListUseCase getMessageListUseCase,
                       SearchMessageUseCase searchMessageUseCase,
                       DeleteMessageListUseCase deleteMessageListUseCase,
                       GetChatBlastSellerMetaDataUseCase getChatBlastSellerMetaDataUseCase,
                       UserSessionInterface userSession,
                       WebSocketMapper webSocketMapper,
                       TopChatAnalytics analytics) {
        this.getMessageListUseCase = getMessageListUseCase;
        this.searchMessageUseCase = searchMessageUseCase;
        this.deleteMessageListUseCase = deleteMessageListUseCase;
        this.userSession = userSession;
        this.webSocketMapper = webSocketMapper;
        this.getChatBlastSellerMetaDataUseCase = getChatBlastSellerMetaDataUseCase;
        this.analytics = analytics;
    }

    @Override
    public void attachView(InboxChatContract.View view) {
        super.attachView(view);
        initialize();
    }

    private void initialize() {
        this.pagingHandler = new PagingHandler();
        this.listFetchCache = new ArrayList<>();
        isRequesting = false;
        inActionMode = false;
        contactSize = 0;
        chatSize = 0;
        attempt = 0;

        client = new OkHttpClient();
        listener = new ChatWebSocketListenerImpl(getView().getInterface(), webSocketMapper, true);

        countDownTimer = new CountDownTimer(5000, 1000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                createWebSocket();
            }
        };

        createWebSocket();
    }

    public void getMessage() {
        if (viewModel != null) viewModel.setKeyword("");
        if (!getView().getAdapter().containLoading()) {
            showLoading();
        }
        getView().disableActions();
        getView().removeError();
        isRequesting = true;
        getMessageListUseCase.execute(GetMessageListUseCase.generateParam(pagingHandler.getPage()), new GetMessageSubscriber(getView(), this));
    }

    private void showLoading() {
        if (pagingHandler.getPage() == 1 && getView().getAdapter().getList().size() != 0) {
            getView().getRefreshHandler().setRefreshing(true);
            getView().getRefreshHandler().setIsRefreshing(true);
        } else if (pagingHandler.getPage() == 1 && getView().getAdapter().getList().size() == 0 && !getView().getRefreshHandler().isRefreshing()) {
            getView().getAdapter().showLoadingFull(true);
        } else if (!getView().getRefreshHandler().isRefreshing()) {
            getView().getAdapter().showLoading();
        }
    }

    private InboxChatViewModel modifyViewModel(InboxChatViewModel result) {
        String temp = "";
        if (viewModel != null) {
            temp = viewModel.getKeyword();
        }
        viewModel = result;
        viewModel.setKeyword(temp);
        return viewModel;
    }


    public void setResultFetch(InboxChatViewModel result) {
        viewModel = modifyViewModel(result);

        if (pagingHandler.getPage() == 1) {
            contactSize = 0;
            chatSize = 0;
            getView().getAdapter().setList(result.getListReplies());
            chatSize += result.getChatSize();
        } else {
            getView().getAdapter().addList(result.getListReplies());
            chatSize += result.getChatSize();
        }

        getView().getAdapter().showEmptyFull(false);
        getView().getAdapter().showEmptySearch(false);

        if (getView().getAdapter().getList().size() == 0) {
            if (result.getMode() == InboxChatViewModel.SEARCH_CHAT_MODE) {
                getView().getAdapter().showEmptySearch(true);
            } else if (result.getMode() == InboxChatViewModel.GET_CHAT_MODE) {
                getView().getAdapter().showEmptyFull(true);
            }
        }
    }

    public void setCache(List<Visitable> list) {
        this.listFetchCache = new ArrayList<>();
        this.listFetchCache.addAll(list);
    }

    public List<Visitable> getListCache() {
        return listFetchCache;
    }

    public void resetSearch() {
        if (viewModel != null) {
            viewModel.setMode(InboxChatViewModel.GET_CHAT_MODE);
            viewModel.setKeyword("");
            getView().getAdapter().setList(listFetchCache);
            chatSize = listFetchCache.size();
            contactSize = 0;
        }
    }

    public void setResultSearch(InboxChatViewModel result) {
        viewModel = modifyViewModel(result);

        if (pagingHandler.getPage() == 1) {
            contactSize = 0;
            chatSize = 0;
            getView().getAdapter().setList(result.getListReplies());
            chatSize += result.getChatSize();
            getView().getAdapter().addList(contactSize, result.getListContact());
            contactSize += result.getContactSize();
        } else {
            getView().getAdapter().addList(result.getListReplies());
            chatSize += result.getChatSize();
            getView().getAdapter().addList(contactSize, result.getListContact());
            contactSize += result.getContactSize();
        }


        getView().getAdapter().showEmptyFull(false);
        getView().getAdapter().showEmptySearch(false);

        if (getView().getAdapter().getList().size() == 0) {
            if (result.getMode() == InboxChatViewModel.SEARCH_CHAT_MODE) {
                getView().getAdapter().showEmptySearch(true);
            } else if (result.getMode() == InboxChatViewModel.GET_CHAT_MODE) {
                getView().getAdapter().showEmptyFull(true);
            }
        }

        getView().setMenuEnabled(false);
    }

    public void getBlastMetaData(){
        getChatBlastSellerMetaDataUseCase.execute(new Subscriber<TopChatBlastSellerMetaData>() {
            @Override
            public void onCompleted() {
                //no-op
            }

            @Override
            public void onError(Throwable throwable) {
                //no-op
            }

            @Override
            public void onNext(TopChatBlastSellerMetaData topChatBlastSellerMetaData) {
                getView().handleBroadcastChatMetaData(topChatBlastSellerMetaData);
            }
        });
    }

    @Override
    public void detachView() {
        super.detachView();
        getMessageListUseCase.unsubscribe();
        searchMessageUseCase.unsubscribe();
        deleteMessageListUseCase.unsubscribe();
        getChatBlastSellerMetaDataUseCase.unsubscribe();
        if (countDownTimer != null) countDownTimer.cancel();
    }


    public void onSelected(int position) {
        getView().getAdapter().addChecked(position);
        getView().setOptionsMenuFromSelect();
    }

    public void onDeselect(int position) {
        getView().getAdapter().removeChecked(position);
        getView().setOptionsMenuFromSelect();
    }

    public int getSelected() {
        return getView().getAdapter().getListMove().size();
    }

    public void goToDetailMessage(Context context, int position, ChatListViewModel listMessage) {

        if (viewModel == null)
            return;

        ws.close(1000, "");
        getView().dropKeyboard();

        analytics.eventOpenTopChat();

        Intent intent = TopChatRoomActivity.Companion.getCallingIntent(getView().getActivity()
                , String.valueOf(listMessage.getId())
                , listMessage.getName(),
                listMessage.getLabel().toString(),
                listMessage.getSenderId(),
                listMessage.getRole(),
                viewModel.getMode(),
                viewModel.getKeyword(),
                listMessage.getImage());
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        getView().startActivityForResult(intent, InboxMessageConstant.OPEN_DETAIL_MESSAGE);
        getView().overridePendingTransition(0, 0);
    }

    public void goToProfile(int userId) {
        if (getView().getActivity().getApplicationContext() instanceof TopChatRouter) {
            getView().startActivity(
                    ((TopChatRouter) getView().getActivity().getApplicationContext())
                            .getTopProfileIntent(getView().getActivity(), String.valueOf(userId))
            );
        }
    }

    public void goToShop(int shopId) {
        Intent intent = ((TopChatRouter) getView().getActivity().getApplicationContext()).getShopPageIntent
                (getView().getActivity(), String.valueOf(shopId));
        getView().startActivity(intent);
    }

    public void refreshData() {
        getView().finishContextMode();
        if (!isRequesting) {
            getView().getAdapter().removeLoading();
            pagingHandler.resetPage();
            getView().getRefreshHandler().setRefreshing(true);
            getView().getRefreshHandler().setIsRefreshing(true);
            if (GlobalConfig.isSellerApp())
                getBlastMetaData();
            getMessage();
        } else {
            getView().getRefreshHandler().finishRefresh();
        }
    }

    public void setUserVisibleHint(boolean isVisibleToUser, boolean isMustRefresh) {
//        initAnalytics();
        if (isDataEmpty() && isVisibleToUser && !getView().hasRetry()) {
            getMessage();
        } else if (isMustRefresh) {
            refreshData();
        }
    }

    private boolean isDataEmpty() {
        return getView().getAdapter().getList().size() == 0;
    }


    public boolean hasActionListener() {
        return true;
    }

    public void moveViewToTop() {
        if (getView() != null) {
            getView().moveViewToTop();
        }
    }

    public int getMenuID() {
        switch (getView().getArguments().getString(InboxMessageConstant.PARAM_NAV, "")) {
            case InboxMessageConstant.MESSAGE_ARCHIVE:
                return R.menu.inbox_message_archive;
            case InboxMessageConstant.MESSAGE_TRASH:
                return R.menu.inbox_message_trash;
            default:
                return R.menu.inbox_chat_delete;
        }
    }

    public void initSearch(String keyword) {
        pagingHandler.resetPage();
        searchAll(keyword);
    }

    private void searchAll(String keyword) {
        if (!isRequesting) {
            if (viewModel != null) viewModel.setKeyword(keyword);
            searchMessageUseCase.execute(SearchMessageUseCase.generateParam(keyword), new SearchMessageSubscriber(getView(), this));
            isRequesting = true;
        }
    }

    private void searchReplies(String keyword) {
        search(keyword, PARAM_BY_REPLY);
    }

    public void search(String keyword, String by) {
        if (!isRequesting) {
            if (viewModel != null) viewModel.setKeyword(keyword);
            searchMessageUseCase.execute(SearchMessageUseCase.generateParam(keyword, pagingHandler.getPage(), by), new SearchMessageSubscriber(getView(), this));
            isRequesting = true;
        }
    }

    public boolean isRequesting() {
        return isRequesting;
    }

    public void setRequesting(boolean requesting) {
        isRequesting = requesting;
    }

    public void prepareNextPage(boolean hasNext) {
        if (hasNext) {
            getView().getAdapter().showLoading();
        }
    }

    public void onLoadMore() {
        if (!isRequesting) {
            pagingHandler.nextPage();
            if (viewModel.getMode() == InboxChatViewModel.GET_CHAT_MODE) {
                getMessage();
            } else if (viewModel.getMode() == InboxChatViewModel.SEARCH_CHAT_MODE) {
                searchReplies(getView().getKeyword());
            }
        } else {
            getView().finishLoading();
        }
    }

    public String getKeyword() {
        return getView().getKeyword();
    }

    public void deleteMessage(List<Pair> listMove) {
        deleteMessageListUseCase.execute(DeleteMessageListUseCase.generateParam(listMove)
                , new DeleteMessageSubscriber(listMove, getView()));
    }


    @Override
    public void createWebSocket() {
        try {

            String websocketUrl = TopChatUrl.Companion.getPathWebsocket(userSession.getDeviceId(),
                    userSession.getUserId());
            Request request = new Request.Builder().url(websocketUrl)
                    .header("Origin", TkpdBaseURL.WEB_DOMAIN)
                    .header("Accounts-Authorization",
                            "Bearer " + userSession.getAccessToken())
                    .header("x-app-version",String.valueOf(GlobalConfig.VERSION_CODE))
                    .header("x-device", "android-" + GlobalConfig.VERSION_NAME)
                    .header("x-tkpd-app-version","android-" + GlobalConfig.VERSION_NAME)
                    .header("x-tkpd-app-name", GlobalConfig.getPackageApplicationName())
                    .build();
            ws = client.newWebSocket(request, listener);
            attempt++;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void recreateWebSocket() {
        countDownTimer.start();
    }

    @Override
    public void resetAttempt() {
        attempt = 0;
    }

    @Override
    public void closeWebsocket() {
        client.dispatcher().executorService().shutdown();
        ws.close(1000, "Goodbye !");
    }

    public boolean isInActionMode() {
        return inActionMode;
    }

    public void setInActionMode(boolean inActionMode) {
        this.inActionMode = inActionMode;
    }

    public void setError(String errorMessage) {
        if (pagingHandler.getPage() == 1) {
            getView().showErrorFull(errorMessage);
        } else {
            getView().showError(errorMessage);
        }
    }
}
