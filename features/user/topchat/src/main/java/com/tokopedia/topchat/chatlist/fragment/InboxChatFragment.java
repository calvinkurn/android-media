package com.tokopedia.topchat.chatlist.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.KeyboardHandler;
import com.tokopedia.broadcast.message.common.data.model.TopChatBlastSellerMetaData;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.DrawerPresenterActivity;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.core.customView.TextDrawable;
import com.tokopedia.core.customwidget.SwipeToRefresh;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.SnackbarRetry;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.util.RefreshHandler;
import com.tokopedia.design.text.SearchInputView;
import com.tokopedia.broadcast.message.common.BroadcastMessageRouter;
import com.tokopedia.graphql.data.GraphqlClient;
import com.tokopedia.showcase.ShowCaseBuilder;
import com.tokopedia.showcase.ShowCaseDialog;
import com.tokopedia.showcase.ShowCaseObject;
import com.tokopedia.showcase.ShowCasePreference;
import com.tokopedia.topchat.R;
import com.tokopedia.topchat.chatlist.activity.InboxChatActivity;
import com.tokopedia.topchat.chatlist.adapter.InboxChatAdapter;
import com.tokopedia.topchat.chatlist.adapter.InboxChatTypeFactory;
import com.tokopedia.topchat.chatlist.adapter.InboxChatTypeFactoryImpl;
import com.tokopedia.topchat.chatlist.listener.InboxChatContract;
import com.tokopedia.topchat.chatlist.presenter.InboxChatPresenter;
import com.tokopedia.topchat.chatlist.viewmodel.DeleteChatViewModel;
import com.tokopedia.topchat.chatlist.viewmodel.InboxChatViewModel;
import com.tokopedia.topchat.chatroom.data.ChatWebSocketConstant;
import com.tokopedia.topchat.chatroom.domain.pojo.reply.WebSocketResponse;
import com.tokopedia.topchat.chatroom.view.activity.ChatRoomActivity;
import com.tokopedia.topchat.chatroom.view.activity.TimeMachineActivity;
import com.tokopedia.topchat.chatroom.view.fragment.ChatRoomFragment;
import com.tokopedia.topchat.chatroom.view.presenter.WebSocketInterface;
import com.tokopedia.topchat.chatroom.view.viewmodel.BaseChatViewModel;
import com.tokopedia.topchat.chatroom.view.viewmodel.ReplyParcelableModel;
import com.tokopedia.topchat.common.InboxChatConstant;
import com.tokopedia.topchat.common.InboxMessageConstant;
import com.tokopedia.topchat.common.analytics.TopChatAnalytics;
import com.tokopedia.topchat.common.di.DaggerInboxChatComponent;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by stevenfredian on 9/14/17.
 */

public class InboxChatFragment extends BaseDaggerFragment
        implements InboxChatContract.View, InboxMessageConstant, InboxChatConstant
        , SearchInputView.Listener, SearchInputView.ResetListener
        , WebSocketInterface {

    public boolean isMustRefresh = false;
    RecyclerView mainList;

    //    FloatingActionButton fab;
    SwipeToRefresh swipeToRefresh;
    View searchLoading;
    @Inject
    InboxChatPresenter presenter;
    InboxChatAdapter adapter;
    RefreshHandler refreshHandler;
    boolean isRetryShowing = false;
    LinearLayoutManager layoutManager;
    TkpdProgressDialog progressDialog;
    SnackbarRetry snackbarRetry;
    Snackbar snackbarUndo;
    SearchInputView searchInputView;
    boolean isMultiActionEnabled = false;
    ActionMode.Callback callbackContext;
    ActionMode contextMenu;
    private InboxChatTypeFactory typeFactory;
    private View notifier;
    private TextView sendBroadcast;
    private ShowCaseDialog showCaseDialog;

    public static InboxChatFragment createInstance(String navigation) {
        InboxChatFragment fragment = new InboxChatFragment();
        Bundle bundle = new Bundle();
        bundle.putString(InboxMessageConstant.PARAM_NAV, navigation);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        GraphqlClient.init(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.fragment_inbox_message, container, false);

        initView(parentView);

        return parentView;
    }

    private void initView(View parentView) {
        searchLoading = parentView.findViewById(R.id.loading_search);
        mainList = (RecyclerView) parentView.findViewById(R.id.chat_list);
        mainList.requestFocus();
        swipeToRefresh = (SwipeToRefresh) parentView.findViewById(R.id.swipe_refresh_layout);
        refreshHandler = new RefreshHandler(getActivity(), parentView, new RefreshHandler.OnRefreshHandlerListener() {
            @Override
            public void onRefresh(View view) {
                finishContextMode();
                presenter.refreshData();
            }
        });
        searchInputView = (SearchInputView) parentView.findViewById(R.id.simpleSearchView);
        searchInputView.setListener(this);
        searchInputView.setResetListener(this);
        searchInputView.setSearchHint(getActivity().getString(R.string.search_chat_user));
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mainList.setHasFixedSize(true);
        presenter.attachView(this);
        progressDialog = new TkpdProgressDialog(getActivity(), TkpdProgressDialog.NORMAL_PROGRESS);
        callbackContext = initCallbackActionMode();
        notifier = parentView.findViewById(R.id.notifier);
        sendBroadcast = parentView.findViewById(R.id.tv_bm_action);
        if (GlobalConfig.isSellerApp()){
            sendBroadcast.setOnClickListener(v -> {
                if (getActivity().getApplication() instanceof BroadcastMessageRouter && getContext() != null){
                    startActivity(((BroadcastMessageRouter) getActivity().getApplication()).getBroadcastMessageListIntent(getContext()));
                }
            });
        } else {
            sendBroadcast.setVisibility(View.GONE);
        }
        parentView.findViewById(R.id.tv_organize_action).setOnClickListener(v -> setOptionsMenu());

        typeFactory = new InboxChatTypeFactoryImpl(this, presenter);
    }


    private ActionMode.Callback initCallbackActionMode() {
        return new ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                disableActions();
                getActivity().getMenuInflater().inflate(presenter.getMenuID(), menu);
                isMultiActionEnabled = true;
                presenter.setInActionMode(true);
                if (getActivity() instanceof InboxChatActivity) {
                    ((InboxChatActivity) getActivity()).hideIndicators();
                }
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                mode.setTitle(String.valueOf(presenter.getSelected()));
                return true;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

                if (item.getItemId() == R.id.action_delete && presenter.getSelected() > 0) {
                    List<Pair> temp = new ArrayList<>();
                    temp.addAll(adapter.getListMove());
                    createDeleteDialog(presenter.getSelected(), temp).show();
                    mode.finish();
                    return true;
                }
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                adapter.clearSelection();
                isMultiActionEnabled = false;
                presenter.setInActionMode(false);
                enableActions();
                if (getActivity() instanceof InboxChatActivity) {
                    ((InboxChatActivity) getActivity()).showIndicators();
                }
            }
        };
    }


    private AlertDialog createDeleteDialog(int selected, final List<Pair> listMove) {

        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.delete_chat)
                .setMessage(R.string.forever_deleted_chat)
                .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        presenter.deleteMessage(listMove);
                        dialog.dismiss();
                    }

                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();

    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new InboxChatAdapter(typeFactory, presenter);

        searchInputView.getSearchTextView().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                searchInputView.getSearchTextView().setCursorVisible(true);
                return false;
            }
        });

        searchInputView.getSearchTextView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchInputView.getSearchTextView().setCursorVisible(true);
            }
        });

        mainList.setAdapter(adapter);
        mainList.setLayoutManager(layoutManager);
        mainList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int index = layoutManager.findLastCompletelyVisibleItemPosition();
                if (adapter.checkLoadMore(index)) {
                    presenter.onLoadMore();
                }
            }
        });

        searchLoading.setVisibility(View.VISIBLE);
        presenter.getMessage();
        if (GlobalConfig.isSellerApp())
            presenter.getBlastMetaData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
    }

    @Override
    protected void initInjector() {
        AppComponent appComponent = getComponent(AppComponent.class);

        DaggerInboxChatComponent daggerInboxChatComponent =
                (DaggerInboxChatComponent) DaggerInboxChatComponent.builder()
                        .appComponent(appComponent).build();
        daggerInboxChatComponent.inject(this);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        try {
//            presenter.setUserVisibleHint(isVisibleToUser, isMustRefresh);
//            if (snackbarRetry != null) {
//                snackbarRetry.resumeRetrySnackbar();
//                if (!isVisibleToUser)
//                    snackbarRetry.pauseRetrySnackbar();
//            }
//            if (snackbarUndo != null) {
//                snackbarUndo.dismiss();
//            }
//            finishContextMode();

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void setOptionsMenuFromSelect() {
        if (!isMultiActionEnabled) {
            contextMenu = ((AppCompatActivity) getActivity()).startSupportActionMode(callbackContext);
        }
        if (contextMenu != null) {
            contextMenu.invalidate();
            if (presenter.getSelected() == 0) {
                finishContextMode();
            }
            contextMenu.setTitle(String.format("%s %s", String.valueOf(presenter.getSelected()), getActivity().getString(R.string.title_inbox_chat)));
        }
    }

    public void setOptionsMenu() {
        if (!isMultiActionEnabled) {
            contextMenu = ((AppCompatActivity) getActivity()).startSupportActionMode(callbackContext);
        }
        if (contextMenu != null) {
            contextMenu.invalidate();
            contextMenu.setTitle(R.string.delete_choose);
        }
    }

    @Override
    public void addTimeMachine() {
        adapter.showTimeMachine();
    }

    @Override
    public void onGoToTimeMachine(String url) {
        dropKeyboard();
        startActivity(TimeMachineActivity.getCallingIntent(getActivity(), url));
    }

    @Override
    public void removeList(List<Pair> originList, List<DeleteChatViewModel> list) {
//        adapter.removeList(originList, list);
        presenter.refreshData();
    }

    @Override
    public void setResultSearch(final InboxChatViewModel inboxChatViewModel) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                presenter.setResultSearch(inboxChatViewModel);
            }
        });
    }

    @Override
    public void setResultFetch(final InboxChatViewModel inboxChatViewModel) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                presenter.setResultFetch(inboxChatViewModel);
            }
        });
    }

    @Override
    public WebSocketInterface getInterface() {
        return this;
    }

    @Override
    public String getNav() {
        return getArguments().getString(InboxMessageConstant.PARAM_NAV, "");
    }

    @Override
    public void enableActions() {
        if (getActivity() instanceof DrawerPresenterActivity)
            ((DrawerPresenterActivity) getActivity()).setDrawerEnabled(true);

//        if (adapter.getSelected() == 0) {
//            fab.show();
//            ((InboxChatActivity)getActivity()).showTabLayout(true);
//        }
//        if (presenter.hasActionListener()) {
//            adapter.setEnabled(true);
//        }
    }

    @Override
    public void disableActions() {
        if (getActivity() instanceof DrawerPresenterActivity)
            ((DrawerPresenterActivity) getActivity()).setDrawerEnabled(false);
//        if (fab.isShown())
//            fab.hide();
//        adapter.setEnabled(false);
    }

    @Override
    public void finishContextMode() {
        if (contextMenu != null) {
            contextMenu.finish();
        }
    }

    @Override
    public boolean hasRetry() {
        return isRetryShowing;
    }

    @Override
    public void overridePendingTransition(int i, int i1) {

    }

    @Override
    public String getFilter() {
        return null;
    }

    @Override
    public String getKeyword() {
        return searchInputView.getSearchText();
    }

    @Override
    public void showEmptyState(String localizedMessage) {

    }

    @Override
    public void showError(String message) {
        NetworkErrorHelper.showSnackbar(getActivity(), message);
    }


    @Override
    public void showErrorWarningDelete(int maxMessageDelete) {
        NetworkErrorHelper.showSnackbar(getActivity(), getActivity().getString(R.string.delete_message_warn) + " " + maxMessageDelete);
    }

    @Override
    public void showErrorFull(String errorMessage) {
        NetworkErrorHelper.showEmptyState(getActivity(), getView(), errorMessage, new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                presenter.getMessage();
            }
        });
    }

    @Override
    public void dropKeyboard() {
        searchInputView.getSearchTextView().setCursorVisible(false);
        KeyboardHandler.DropKeyboard(getActivity(), getView());
    }

    @Override
    public void setMenuEnabled(boolean b) {
        setHasOptionsMenu(b);
    }

    @Override
    public void onErrorDeleteMessage(String errorMessage) {
        NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
    }

    @Override
    public void moveViewToTop() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (layoutManager.findFirstVisibleItemPosition() < 2) {
                    layoutManager.scrollToPosition(0);
                }
            }
        });
    }

    @Override
    public RefreshHandler getRefreshHandler() {
        return refreshHandler;
    }

    @Override
    public InboxChatAdapter getAdapter() {
        return adapter;
    }

    @Override
    public void finishLoading() {
        refreshHandler.finishRefresh();
        progressDialog.dismiss();
        adapter.removeLoading();
        searchLoading.setVisibility(View.GONE);
    }

    @Override
    public void setMustRefresh(boolean isMustRefresh) {
        this.isMustRefresh = isMustRefresh;
    }

    @Override
    public void removeError() {
        adapter.showEmptyFull(false);
        isRetryShowing = false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == InboxMessageConstant.OPEN_DETAIL_MESSAGE && resultCode == Activity.RESULT_OK) {
            if (data.getExtras().getBoolean(InboxMessageConstant.MUST_REFRESH))
                presenter.refreshData();
            else if (data.getExtras() != null &&
                    data.getExtras().getParcelable(PARCEL) != null) {
                Bundle bundle = data.getExtras();
                ReplyParcelableModel model = bundle.getParcelable(PARCEL);
                adapter.moveToTop(model.getMessageId(), model.getMsg(), null, false);
                adapter.updateListCache(model.getMessageId(), model.getMsg(), false,
                        presenter.getListCache());
            }
        } else if (requestCode == InboxMessageConstant.OPEN_DETAIL_MESSAGE &&
                resultCode == ChatRoomFragment.CHAT_DELETED_RESULT_CODE &&
                data != null && data.hasExtra(ChatRoomActivity.PARAM_MESSAGE_ID)) {
            presenter.refreshData();
        }

        presenter.createWebSocket();
    }


    @Override
    public void finishSearch() {
        searchLoading.setVisibility(View.GONE);
    }

    @Override
    public void onSearchSubmitted(String text) {
        refreshHandler.setPullEnabled(false);
        if (text.length() > 0) {
            presenter.initSearch(text);
            searchLoading.setVisibility(View.VISIBLE);
            UnifyTracking.eventTopChatSearch(getActivity(),TopChatAnalytics.Category.INBOX_CHAT,
                    TopChatAnalytics.Action.INBOX_CHAT_SEARCH,
                    TopChatAnalytics.Name.INBOX_CHAT);
            if (getActivity() instanceof InboxChatActivity) {
                ((InboxChatActivity) getActivity()).hideIndicators();
            }
        } else {
            onSearchReset();
        }
        dropKeyboard();
    }

    @Override
    public void onSearchTextChanged(String text) {
        if (text.length() == 0) {
            onSearchReset();
        }
    }

    @Override
    public void onSearchReset() {
        refreshHandler.setPullEnabled(true);
        presenter.resetSearch();
        setHasOptionsMenu(true);
        if (getActivity() instanceof InboxChatActivity) {
            ((InboxChatActivity) getActivity()).showIndicators();
        }
    }

    @Override
    public void onIncomingEvent(final WebSocketResponse response) {
        switch (response.getCode()) {
            case ChatWebSocketConstant.EVENT_TOPCHAT_TYPING:
                adapter.showTyping(response.getData().getMsgId());
                break;
            case ChatWebSocketConstant.EVENT_TOPCHAT_END_TYPING:
                adapter.removeTyping(response.getData().getMsgId());
                break;
            case ChatWebSocketConstant.EVENT_TOPCHAT_REPLY_MESSAGE:
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        adapter.moveToTop(String.valueOf(response.getData().getMsgId()),
                                response.getData().getMessage().getCensoredReply(), response,
                                true);
                        reloadNotifDrawer();
                    });
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onErrorWebSocket() {
        if (getActivity() != null) {
            notifyConnectionWebSocket();
            presenter.recreateWebSocket();
        }
    }

    @Override
    public void onOpenWebSocket() {
        if (getActivity() != null) {
            getActivity().runOnUiThread(() -> {
                TextView title = notifier.findViewById(R.id.title);
                title.setText(R.string.connected_websocket);
                View action = notifier.findViewById(R.id.action);
                action.setVisibility(View.GONE);
            });

            notifier.postDelayed(() -> notifier.setVisibility(View.GONE), 1500);
            presenter.resetAttempt();
        }
    }

    @Override
    public void onReceiveMessage(BaseChatViewModel message) {
        //IGNORE
    }


    @Override
    public void notifyConnectionWebSocket() {
        if (getActivity() != null && presenter != null) {
            getActivity().runOnUiThread(() -> {
                notifier.setVisibility(View.VISIBLE);
                TextView title = notifier.findViewById(R.id.title);

                View action = notifier.findViewById(R.id.action);

                title.setText(R.string.error_no_connection_retrying);
                action.setVisibility(View.VISIBLE);
            });
        }
    }

    @Override
    public void onDestroy() {
        presenter.closeWebsocket();
        presenter.detachView();
        super.onDestroy();
    }

    @Override
    public void saveResult() {
        presenter.setCache(getAdapter().getList());
    }

    @Override
    public void reloadNotifDrawer() {
        if (getActivity() instanceof InboxChatActivity) {
            ((InboxChatActivity) getActivity()).updateNotifDrawerData();
        }
    }

    @Override
    public void handleBroadcastChatMetaData(TopChatBlastSellerMetaData topChatBlastSellerMetaData) {
        boolean isValidToCreateBroadcast = topChatBlastSellerMetaData.getStatus() == 1;
        sendBroadcast.setVisibility(isValidToCreateBroadcast? View.VISIBLE : View.GONE);

        if (isValidToCreateBroadcast)
            checkNeedToShowCasing();
    }

    private void checkNeedToShowCasing() {
        final String showcaseTag = getClass().getName()+".BroadcastMessage";
        if (ShowCasePreference.hasShown(getActivity(), showcaseTag) || showCaseDialog != null){
            return;
        }

        showCaseDialog = generateShowcaseDialog();
        final ArrayList<ShowCaseObject> showCaseList = new ArrayList<>();
        showCaseList.add(new ShowCaseObject(sendBroadcast, getString(R.string.bm_title),
                getString(R.string.bm_showcase_desc)));
        showCaseDialog.show(getActivity(), showcaseTag, showCaseList);
    }

    private ShowCaseDialog generateShowcaseDialog() {
        return new ShowCaseBuilder()
                .backgroundContentColorRes(R.color.black)
                .shadowColorRes(R.color.shadow)
                .textColorRes(R.color.grey_400)
                .textSizeRes(R.dimen.sp_12)
                .titleTextSizeRes(R.dimen.sp_16)
                .finishStringRes(R.string.title_understand)
                .clickable(true)
                .useArrow(true)
                .build();
    }
}
