package com.tokopedia.groupchat.chatroom.view.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.sendbird.android.OpenChannel;
import com.sendbird.android.PreviousMessageListQuery;
import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.groupchat.GroupChatModuleRouter;
import com.tokopedia.groupchat.R;
import com.tokopedia.groupchat.chatroom.di.DaggerChatroomComponent;
import com.tokopedia.groupchat.chatroom.domain.mapper.GroupChatMessagesMapper;
import com.tokopedia.groupchat.chatroom.view.activity.GroupChatActivity;
import com.tokopedia.groupchat.chatroom.view.adapter.chatroom.GroupChatAdapter;
import com.tokopedia.groupchat.chatroom.view.adapter.chatroom.typefactory.GroupChatTypeFactory;
import com.tokopedia.groupchat.chatroom.view.adapter.chatroom.typefactory.GroupChatTypeFactoryImpl;
import com.tokopedia.groupchat.chatroom.view.listener.ChatroomContract;
import com.tokopedia.groupchat.chatroom.view.listener.GroupChatContract;
import com.tokopedia.groupchat.chatroom.view.presenter.ChatroomPresenter;
import com.tokopedia.groupchat.chatroom.view.viewmodel.ChannelInfoViewModel;
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.ChatViewModel;
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.GroupChatPointsViewModel;
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.PendingChatViewModel;
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.SprintSaleAnnouncementViewModel;
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.SprintSaleProductViewModel;
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.SprintSaleViewModel;
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.UserActionViewModel;
import com.tokopedia.groupchat.common.analytics.EEPromotion;
import com.tokopedia.groupchat.common.analytics.GroupChatAnalytics;
import com.tokopedia.groupchat.common.data.GroupChatUrl;
import com.tokopedia.groupchat.common.design.CloseableBottomSheetDialog;
import com.tokopedia.groupchat.common.design.SpaceItemDecoration;
import com.tokopedia.groupchat.common.di.component.DaggerGroupChatComponent;
import com.tokopedia.groupchat.common.di.component.GroupChatComponent;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

/**
 * @author by nisie on 2/6/18.
 */

public class GroupChatFragment extends BaseDaggerFragment implements ChatroomContract.View,
        ChatroomContract.View.ImageAnnouncementViewHolderListener,
        ChatroomContract.View.VoteAnnouncementViewHolderListener,
        ChatroomContract.View.SprintSaleViewHolderListener,
        ChatroomContract.View.GroupChatPointsViewHolderListener {

    private static final long DELAY_TIME_SPRINT_SALE = TimeUnit.SECONDS.toMillis(3);
    private static final int REQUEST_LOGIN = 111;
    private static final String NO_USER_ID = "anonymous";

    @Inject
    ChatroomPresenter presenter;

    @Inject
    GroupChatAnalytics analytics;

    @Inject
    GroupChatMessagesMapper groupChatMessagesMapper;

    private RecyclerView chatRecyclerView;
    private EditText replyEditText;
    private View sendButton;
    private View divider;
    private View main, loading;
    private GroupChatAdapter adapteradapter;
    private LinearLayoutManager layoutManager;
    private View chatNotificationView;
    private View login;
    private View sprintSaleIconLayout;
    private TextView sprintSaleText;

    private OpenChannel mChannel;
    private PreviousMessageListQuery mPrevMessageListQuery;
    private UserSession userSession;

    private Handler sprintSaleHandler;
    private Runnable sprintSaleRunnable;

    int newMessageCounter;

    private TextWatcher replyTextWatcher;

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {
        GroupChatComponent streamComponent = DaggerGroupChatComponent.builder().baseAppComponent(
                ((BaseMainApplication) getActivity().getApplication()).getBaseAppComponent()).build();

        DaggerChatroomComponent.builder()
                .groupChatComponent(streamComponent)
                .build().inject(this);

        presenter.attachView(this);
    }

    public static Fragment createInstance(Bundle bundle) {
        Fragment fragment = new GroupChatFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userSession = ((AbstractionRouter) getActivity().getApplication()).getSession();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group_chat_room_new, container, false);
        chatRecyclerView = view.findViewById(R.id.chat_list);
        replyEditText = view.findViewById(R.id.reply_edit_text);
        sendButton = view.findViewById(R.id.button_send);
        divider = view.findViewById(R.id.view);
        loading = view.findViewById(R.id.loading);
        main = view.findViewById(R.id.main_content);
        chatNotificationView = view.findViewById(R.id.layout_new_chat);
        chatNotificationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollToBottom();
            }
        });
        CloseableBottomSheetDialog channelInfoDialog = CloseableBottomSheetDialog.createInstance(getActivity());
        channelInfoDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                BottomSheetDialog d = (BottomSheetDialog) dialog;

                FrameLayout bottomSheet = d.findViewById(android.support.design.R.id.design_bottom_sheet);

                if (bottomSheet != null) {
                    BottomSheetBehavior.from(bottomSheet)
                            .setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }
        });
        login = view.findViewById(R.id.login);
        sprintSaleIconLayout = view.findViewById(R.id.sprintsale_icon_layout);
        sprintSaleText = view.findViewById(R.id.sprintsale_text);
        prepareView();
        return view;
    }

    private void prepareView() {
        GroupChatTypeFactory groupChatTypeFactory = new GroupChatTypeFactoryImpl(this);
        adapter = GroupChatAdapter.createInstance(groupChatTypeFactory);
        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        chatRecyclerView.setLayoutManager(layoutManager);
        chatRecyclerView.setAdapter(adapter);
        SpaceItemDecoration itemDecoration = new SpaceItemDecoration((int) getActivity()
                .getResources().getDimension(R.dimen.space_chat));
        chatRecyclerView.addItemDecoration(itemDecoration);

        chatRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (layoutManager.findLastVisibleItemPosition() == adapter.getItemCount() - 1
                        && !adapter.isLoading()) {
                    presenter.loadPreviousMessages(mChannel, mPrevMessageListQuery);
                }

                if (layoutManager.findFirstVisibleItemPosition() == 0) {
                    resetNewMessageCounter();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (layoutManager.findFirstVisibleItemPosition() == 0) {
                    resetNewMessageCounter();
                }
            }
        });

        setReplyTextHint();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(((GroupChatModuleRouter) getActivity().getApplicationContext())
                        .getLoginIntent(getActivity()), REQUEST_LOGIN);
            }
        });

        replyTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    setSendButtonEnabled(true);
                } else {
                    setSendButtonEnabled(false);
                }
            }
        };
        setForLoginUser(userSession != null && userSession.isLoggedIn());

        sprintSaleIconLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SprintSaleViewModel sprintSaleViewModel = ((GroupChatContract.View) getActivity())
                        .getSprintSaleViewModel();

                onSprintSaleIconClicked(sprintSaleViewModel);
            }
        });
    }

    private void setSendButtonEnabled(boolean isEnabled) {
        if (isEnabled) {
            sendButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!TextUtils.isEmpty(replyEditText.getText().toString().trim())) {
                        PendingChatViewModel pendingChatViewModel = new PendingChatViewModel
                                (presenter.checkText(replyEditText.getText().toString()),
                                        userSession.getUserId(),
                                        userSession.getName(),
                                        userSession.getProfilePicture(),
                                        false);
//                        adapter.addDummyReply(pendingChatViewModel);
                        presenter.sendReply(pendingChatViewModel, mChannel);
                        setSendButtonEnabled(false);
                    }
                }
            });
        } else {
            sendButton.setOnClickListener(null);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
    }

    private void initData() {
        setSendButtonEnabled(false);
        presenter.initMessageFirstTime(mChannel);
        showLoading();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void refreshChat() {
        presenter.refreshDataAfterReconnect(mChannel);
    }

    private void setReplyTextHint() {
        String hintText = getString(R.string.chat_as) + " " + userSession.getName() + "...";
        replyEditText.setHint(hintText);
    }

    @Override
    public void setSprintSaleIcon(SprintSaleViewModel sprintSaleViewModel) {
        if (sprintSaleViewModel != null
                && isValidSprintSale(sprintSaleViewModel)
                && sprintSaleViewModel.getSprintSaleType() != null
                && !sprintSaleViewModel.getSprintSaleType().equals(SprintSaleViewModel.TYPE_FINISHED)) {
            trackViewSprintSaleComponent(sprintSaleViewModel);
            sprintSaleIconLayout.setVisibility(View.VISIBLE);
            setupSprintSaleIcon(sprintSaleViewModel);
        } else {
            sprintSaleIconLayout.setVisibility(View.GONE);
        }
    }

    private void setupSprintSaleIcon(SprintSaleViewModel sprintSaleViewModel) {
        if (sprintSaleViewModel.getSprintSaleType().equals(SprintSaleViewModel.TYPE_UPCOMING)) {
            MethodChecker.setBackground(sprintSaleText, MethodChecker.getDrawable(getActivity(),
                    R.drawable.bg_rounded_pink_label));
            sprintSaleText.setTextColor(MethodChecker.getColor(getActivity(), R.color.red_500));
            sprintSaleText.setText(String.format("%s - %s", sprintSaleViewModel
                    .getFormattedStartDate(), sprintSaleViewModel.getFormattedEndDate()));
            sprintSaleText.setTextColor(MethodChecker.getColor(getActivity(), R.color.red_500));
        } else if (sprintSaleViewModel.getSprintSaleType().equals(SprintSaleViewModel.TYPE_ACTIVE)) {
            MethodChecker.setBackground(sprintSaleText, MethodChecker.getDrawable(getActivity(),
                    R.drawable.bg_rounded_red_label));
            sprintSaleText.setTextColor(MethodChecker.getColor(getActivity(), R.color.white));
            sprintSaleText.setText(getString(R.string.ongoing));
        }
    }

    private long getCurrentTime() {
        return new Date().getTime() / 1000L;
    }

    @Override
    public void autoAddSprintSaleAnnouncement(@Nullable final SprintSaleViewModel
                                                      sprintSaleViewModel,
                                              @Nullable final ChannelInfoViewModel channelInfoViewModel) {
        if (sprintSaleViewModel != null
                && isValidSprintSale(sprintSaleViewModel)
                && sprintSaleViewModel.getSprintSaleType() != null
                && !sprintSaleViewModel.getSprintSaleType().equals(SprintSaleViewModel.TYPE_UPCOMING)
                && !sprintSaleViewModel.getSprintSaleType().equals(SprintSaleViewModel.TYPE_FINISHED)
                && channelInfoViewModel != null) {

            trackViewSprintSaleComponent(sprintSaleViewModel);

            sprintSaleHandler = new Handler();
            sprintSaleRunnable = new Runnable() {
                @Override
                public void run() {
                    SprintSaleAnnouncementViewModel sprintSaleAnnouncementViewModel = new SprintSaleAnnouncementViewModel(
                            sprintSaleViewModel.getCampaignId(),
                            new Date().getTime(),
                            new Date().getTime(),
                            "0",
                            "0",
                            channelInfoViewModel.getAdminName(),
                            channelInfoViewModel.getAdminPicture(),
                            false,
                            true,
                            sprintSaleViewModel.getRedirectUrl(),
                            sprintSaleViewModel.getListProduct(),
                            sprintSaleViewModel.getCampaignName(),
                            sprintSaleViewModel.getStartDate(),
                            sprintSaleViewModel.getEndDate(),
                            sprintSaleViewModel.getSprintSaleType()
                    );

                    if (.getItemAt(adapter.getItemCount() - 1) != null
                            && !(adapter.getItemAt(adapter.getItemCount() - 1) instanceof
                            SprintSaleAnnouncementViewModel)) {
                        addIncomingMessage(sprintSaleAnnouncementViewModel);
                        if (getActivity() != null) {
                            ((GroupChatContract.View) getActivity()).vibratePhone();
                        }
                    }
                }
            };
            sprintSaleHandler.postDelayed(sprintSaleRunnable, DELAY_TIME_SPRINT_SALE);

        }
    }

    private void trackViewSprintSaleComponent(SprintSaleViewModel sprintSaleViewModel) {
        ArrayList<EEPromotion> list = new ArrayList<>();
        for (SprintSaleProductViewModel productViewModel : sprintSaleViewModel
                .getListProduct()) {
            list.add(new EEPromotion(productViewModel.getProductId(),
                    EEPromotion.NAME_GROUPCHAT,
                    GroupChatAnalytics.DEFAULT_EE_POSITION,
                    productViewModel.getProductName(),
                    productViewModel.getProductImage(),
                    ((GroupChatContract.View) getActivity()).getAttributionTracking(GroupChatAnalytics
                            .ATTRIBUTE_FLASH_SALE)
            ));
        }

        ((GroupChatContract.View) getActivity()).eventViewComponentEnhancedEcommerce(GroupChatAnalytics
                .COMPONENT_FLASH_SALE, sprintSaleViewModel.getCampaignName(), GroupChatAnalytics
                .ATTRIBUTE_FLASH_SALE, list);
    }

    public void autoAddGroupChatPoints(@Nullable final GroupChatPointsViewModel
                                               groupChatPointsViewModel) {
        if (groupChatPointsViewModel != null) {
            if (adapter.getItemAt(adapter.getItemCount() - 1) != null
                    && !(adapter.getItemAt(adapter.getItemCount() - 1) instanceof GroupChatPointsViewModel)) {
                addIncomingMessage(groupChatPointsViewModel);
                ((GroupChatContract.View) getActivity()).removeGroupChatPoints();
                ((GroupChatContract.View) getActivity()).vibratePhone();
            }
        }
    }

    private boolean isValidSprintSale(SprintSaleViewModel sprintSaleViewModel) {
        return sprintSaleViewModel != null
                && sprintSaleViewModel.getStartDate() != 0
                && sprintSaleViewModel.getEndDate() != 0
                && sprintSaleViewModel.getEndDate() > getCurrentTime();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (sprintSaleHandler != null && sprintSaleRunnable != null) {
            sprintSaleHandler.removeCallbacks(sprintSaleRunnable);
        }
    }

    @Override
    public void onDestroy() {
        presenter.detachView();
        super.onDestroy();
    }

    @Override
    public void onSuccessGetPreviousMessage(List<Visitable> listChat) {

        trackImpression(listChat);

        try {
            adapter.addListPrevious(listChat);
            adapter.setCanLoadMore(mPrevMessageListQuery.hasMore());
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void trackImpression(List<Visitable> listChat) {
        SprintSaleViewModel sprintSaleViewModel = ((GroupChatContract.View) getActivity())
                .getSprintSaleViewModel();
        for (Visitable visitable : listChat) {
            if (visitable instanceof SprintSaleAnnouncementViewModel && sprintSaleViewModel != null) {
                trackViewSprintSaleComponent(sprintSaleViewModel);
                break;
            }
        }
    }

    @Override
    public void onSuccessGetMessageFirstTime(List<Visitable> listChat, PreviousMessageListQuery previousMessageListQuery) {
        try {
            this.mPrevMessageListQuery = previousMessageListQuery;
            adapter.setList(listChat);
            if (!listChat.isEmpty()) {
                adapter.setCursor(listChat.get(0));
            }
            adapter.setCanLoadMore(mPrevMessageListQuery.hasMore());
            setSendButtonEnabled(true);
            replyEditText.addTextChangedListener(replyTextWatcher);
            scrollToBottom();

            hideLoading();

            if (getActivity() instanceof GroupChatContract.View) {
                ((GroupChatContract.View) getActivity()).setChannelHandler();
                ((GroupChatContract.View) getActivity()).showInfoDialog();
                autoAddSprintSaleAnnouncement(
                        ((GroupChatContract.View) getActivity()).getSprintSaleViewModel(),
                        ((GroupChatContract.View) getActivity()).getChannelInfoViewModel());

                setSprintSaleIcon(((GroupChatContract.View) getActivity()).getSprintSaleViewModel());

            }

            if (getActivity() != null
                    && ((GroupChatContract.View) getActivity()).getChannelInfoViewModel() != null
                    && ((GroupChatContract.View) getActivity()).getChannelInfoViewModel()
                    .getGroupChatPointsViewModel() != null) {
                autoAddGroupChatPoints(((GroupChatContract.View) getActivity()).getChannelInfoViewModel().getGroupChatPointsViewModel());
            }

        } catch (NullPointerException e) {
            e.printStackTrace();
        }

    }

    public void scrollToBottom() {
        resetNewMessageCounter();
        layoutManager.scrollToPosition(0);
    }

    private void resetNewMessageCounter() {
        newMessageCounter = 0;
        chatNotificationView.setVisibility(View.GONE);

    }

    @Override
    public void onErrorSendMessage(PendingChatViewModel pendingChatViewModel, String errorMessage) {
        NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
//        adapter.setRetry(pendingChatViewModel);
    }

    @Override
    public void onSuccessSendMessage(PendingChatViewModel pendingChatViewModel, ChatViewModel viewModel) {
//        adapter.removeDummy(pendingChatViewModel);
        adapter.addReply(viewModel);
        adapter.notifyDataSetChanged();
        replyEditText.setText("");
        scrollToBottom();
        setSendButtonEnabled(true);
    }

    @Override
    public void onErrorGetMessage(String errorMessage) {
        NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
    }

    @Override
    public void onErrorGetMessageFirstTime(String errorMessage) {
        NetworkErrorHelper.showEmptyState(getActivity(), getView(), errorMessage, new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                initData();
            }
        });
    }

    @Override
    public void showLoadingPreviousList() {
        adapter.showLoadingPrevious();
    }

    @Override
    public void dismissLoadingPreviousList() {
        adapter.dismissLoadingPrevious();
    }

    @Override
    public void showReconnectingMessage() {
//        NetworkErrorHelper.showSnackbar(getActivity(), "Reconnecting...");
        showLoading();
    }

    @Override
    public void dismissReconnectingMessage() {
//        NetworkErrorHelper.showSnackbar(getActivity(), "Connected!");
        hideLoading();
    }

    @Override
    public void onSuccessRefreshReconnect(List<Visitable> listChat, PreviousMessageListQuery previousMessageListQuery) {
        adapter.replaceData(listChat);
        this.mPrevMessageListQuery = previousMessageListQuery;
        adapter.setCanLoadMore(mPrevMessageListQuery.hasMore());
        scrollToBottom();

        if (getActivity() instanceof GroupChatActivity) {
            ((GroupChatActivity) getActivity()).setChannelHandler();
            autoAddSprintSaleAnnouncement(
                    ((GroupChatContract.View) getActivity()).getSprintSaleViewModel(),
                    ((GroupChatContract.View) getActivity()).getChannelInfoViewModel());

            setSprintSaleIcon(((GroupChatContract.View) getActivity()).getSprintSaleViewModel());
        }

        setSendButtonEnabled(true);

    }

    public void onMessageReceived(Visitable messageItem) {
        if (messageItem instanceof SprintSaleAnnouncementViewModel) {
            setSprintSaleIcon(((GroupChatContract.View) getActivity()).getSprintSaleViewModel());
        }

        if (!groupChatMessagesMapper.shouldHideMessage(messageItem)) {
            addIncomingMessage(messageItem);
        }

    }

    private void addIncomingMessage(Visitable messageItem) {
        adapter.addIncomingMessage(messageItem);
        adapter.notifyItemInserted(0);
        scrollToBottomWhenPossible();
    }

    private void scrollToBottomWhenPossible() {
        if (layoutManager.findFirstVisibleItemPosition() == 0) {
            scrollToBottom();
        } else {
            newMessageCounter += 1;
            showNewMessageReceived(newMessageCounter);
        }
    }

    private void showNewMessageReceived(int newMessageCounter) {
        if (login.getVisibility() != View.VISIBLE) {
            chatNotificationView.setVisibility(View.VISIBLE);
        }
    }

    public void onMessageDeleted(long msgId) {
        adapter.deleteMessage(msgId);
    }

    public void onMessageUpdated(Visitable visitable) {
        adapter.updateMessage(visitable);
    }

    public void onUserEntered(UserActionViewModel userActionViewModel) {
        if (canShowUserEnter(userActionViewModel)) {
//            adapter.addAction(userActionViewModel);
//            adapter.notifyItemInserted(0);
            scrollToBottomWhenPossible();
        }
    }

    private boolean canShowUserEnter(UserActionViewModel userActionViewModel) {
        try {
            return !TextUtils.isEmpty(userActionViewModel.getUserId())
                    && !userActionViewModel.getUserId().toLowerCase().equals(NO_USER_ID)
                    && !TextUtils.isEmpty(userActionViewModel.getUserName())
                    && (isListEmpty()
                    || lastItemIsNotUserAction()
                    || lastItemIsNotSameUser(userActionViewModel));
        } catch (NullPointerException e) {
            e.printStackTrace();
            return false;
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean lastItemIsNotSameUser(UserActionViewModel userActionViewModel) {
        int lastItemPosition = 0;
        return adapter.getItemCount() > 0
                && adapter.getItemAt(lastItemPosition) != null
                && adapter.getItemAt(lastItemPosition) instanceof UserActionViewModel
                && ((UserActionViewModel) adapter.getItemAt(lastItemPosition)).getUserId() != null
                && !(((UserActionViewModel) adapter.getItemAt(lastItemPosition)).getUserId()
                .equals(userActionViewModel.getUserId()));
    }

    private boolean lastItemIsNotUserAction() {
        int lastItemPosition = 0;
        return adapter.getItemCount() > 0
                && adapter.getItemAt(lastItemPosition) != null
                && !(adapter.getItemAt(lastItemPosition) instanceof UserActionViewModel);
    }

    private boolean isListEmpty() {
        return adapter.getItemCount() == 0;
    }

    public void onUserExited(UserActionViewModel userActionViewModel) {
    }

    public void showLoading() {
        loading.setVisibility(View.VISIBLE);
        main.setVisibility(View.GONE);
    }

    public void hideLoading() {
        loading.setVisibility(View.GONE);
        main.setVisibility(View.VISIBLE);
    }

    @Override
    public void onImageAnnouncementClicked(String url) {
        analytics.eventClickThumbnail(url);
        if (!TextUtils.isEmpty(url)) {
            ((GroupChatModuleRouter) getActivity().getApplication()).openRedirectUrl(getActivity(), url);
        }
    }

    @Override
    public void onVoteComponentClicked(String type, String name) {
        if (getActivity() instanceof GroupChatActivity) {
            ((GroupChatActivity) getActivity()).moveToVoteFragment();
        }
        analytics.eventClickVoteComponent(GroupChatAnalytics.COMPONENT_VOTE, name);
    }

    public void setChannel(OpenChannel mChannel) {
        this.mChannel = mChannel;
    }

    private void setForLoginUser(boolean isLoggedIn) {
        if (isLoggedIn) {
            setReplyTextHint();
            divider.setVisibility(View.VISIBLE);
            replyEditText.setVisibility(View.VISIBLE);
            sendButton.setVisibility(View.VISIBLE);
            login.setVisibility(View.GONE);
        } else {
            divider.setVisibility(View.GONE);
            replyEditText.setVisibility(View.GONE);
            sendButton.setVisibility(View.GONE);
            login.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onSprintSaleProductClicked(SprintSaleProductViewModel productViewModel, int
            position) {

        ArrayList<EEPromotion> list = new ArrayList<>();
        list.add(new EEPromotion(productViewModel.getProductId(),
                EEPromotion.NAME_GROUPCHAT,
                position,
                productViewModel.getProductName(),
                productViewModel.getProductImage(),
                ((GroupChatContract.View) getActivity()).getAttributionTracking(GroupChatAnalytics
                        .ATTRIBUTE_FLASH_SALE)
        ));

        ((GroupChatContract.View) getActivity()).eventClickComponentEnhancedEcommerce(GroupChatAnalytics
                .COMPONENT_FLASH_SALE, productViewModel.getProductName(), GroupChatAnalytics
                .ATTRIBUTE_FLASH_SALE, list);

        if (((GroupChatContract.View) getActivity()).getSprintSaleViewModel() != null
                && ((GroupChatContract.View) getActivity()).getSprintSaleViewModel().getCampaignId() != null
                && productViewModel.getSprintSaleCampaignId().equals(((GroupChatContract.View) getActivity())
                .getSprintSaleViewModel().getCampaignId())
                && !((GroupChatContract.View) getActivity()).getSprintSaleViewModel().getSprintSaleType().equals
                (SprintSaleAnnouncementViewModel.SPRINT_SALE_FINISH)) {


            ((GroupChatModuleRouter) getActivity().getApplicationContext()).openRedirectUrl(getActivity()
                    , ((GroupChatContract.View) getActivity()).generateAttributeApplink
                            (productViewModel.getProductUrl(), GroupChatAnalytics.ATTRIBUTE_FLASH_SALE));
        }
    }

    @Override
    public void onSprintSaleComponentClicked(SprintSaleAnnouncementViewModel sprintSaleAnnouncementViewModel) {
        if (TextUtils.isEmpty(sprintSaleAnnouncementViewModel.getRedirectUrl())) {
            return;
        }

        ArrayList<EEPromotion> list = new ArrayList<>();
        for (SprintSaleProductViewModel productViewModel : sprintSaleAnnouncementViewModel
                .getListProducts()) {
            list.add(new EEPromotion(productViewModel.getProductId(),
                    EEPromotion.NAME_GROUPCHAT,
                    GroupChatAnalytics.DEFAULT_EE_POSITION,
                    productViewModel.getProductName(),
                    productViewModel.getProductImage(),
                    ((GroupChatContract.View) getActivity()).getAttributionTracking(GroupChatAnalytics
                            .ATTRIBUTE_FLASH_SALE)
            ));
        }

        ((GroupChatContract.View) getActivity()).eventClickComponentEnhancedEcommerce(GroupChatAnalytics
                .COMPONENT_FLASH_SALE, sprintSaleAnnouncementViewModel.getCampaignName(), GroupChatAnalytics
                .ATTRIBUTE_FLASH_SALE, list);

        if (((GroupChatContract.View) getActivity()).getSprintSaleViewModel() != null
                && ((GroupChatContract.View) getActivity()).getSprintSaleViewModel().getCampaignId() != null
                && sprintSaleAnnouncementViewModel.getCampaignId().equals(((GroupChatContract.View) getActivity())
                .getSprintSaleViewModel().getCampaignId())
                && !((GroupChatContract.View) getActivity()).getSprintSaleViewModel().getSprintSaleType().equals
                (SprintSaleAnnouncementViewModel.SPRINT_SALE_FINISH)) {

            ((GroupChatModuleRouter) getActivity().getApplicationContext()).openRedirectUrl(getActivity()
                    , ((GroupChatContract.View) getActivity()).generateAttributeApplink
                            (sprintSaleAnnouncementViewModel.getRedirectUrl(),
                                    GroupChatAnalytics.ATTRIBUTE_FLASH_SALE));
        }

    }

    @Override
    public void onSprintSaleIconClicked(SprintSaleViewModel sprintSaleViewModel) {
        if (TextUtils.isEmpty(sprintSaleViewModel.getRedirectUrl())) {
            return;
        }

        ArrayList<EEPromotion> list = new ArrayList<>();
        for (SprintSaleProductViewModel productViewModel : sprintSaleViewModel
                .getListProduct()) {
            list.add(new EEPromotion(productViewModel.getProductId(),
                    EEPromotion.NAME_GROUPCHAT,
                    GroupChatAnalytics.DEFAULT_EE_POSITION,
                    productViewModel.getProductName(),
                    productViewModel.getProductImage(),
                    ((GroupChatContract.View) getActivity()).getAttributionTracking(GroupChatAnalytics
                            .ATTRIBUTE_FLASH_SALE)
            ));
        }

        ((GroupChatContract.View) getActivity()).eventClickComponentEnhancedEcommerce(GroupChatAnalytics
                .COMPONENT_FLASH_SALE, sprintSaleViewModel.getCampaignName(), GroupChatAnalytics
                .ATTRIBUTE_FLASH_SALE, list);

        ((GroupChatModuleRouter) getActivity().getApplicationContext()).openRedirectUrl(getActivity()
                , ((GroupChatContract.View) getActivity()).generateAttributeApplink
                        (sprintSaleViewModel.getRedirectUrl(),
                                GroupChatAnalytics.ATTRIBUTE_FLASH_SALE));

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_LOGIN) {
            refreshChat();
            ((GroupChatContract.View) getActivity()).onSuccessLogin();
            userSession = ((AbstractionRouter) getActivity().getApplication()).getSession();
            setForLoginUser(userSession != null && userSession.isLoggedIn());
        }
    }

    public void onPushNotifReceived(GroupChatPointsViewModel model) {
        autoAddGroupChatPoints(model);
    }

    @Override
    public void onPointsClicked(String url) {
        ((GroupChatModuleRouter) getActivity().getApplicationContext()).openRedirectUrl(getActivity(), url);

        if (getActivity() != null
                && ((GroupChatActivity) getActivity()).getChannelInfoViewModel() != null
                && ((GroupChatActivity) getActivity()).getChannelInfoViewModel().getTitle() != null) {
            String channelName = ((GroupChatActivity) getActivity())
                    .getChannelInfoViewModel().getTitle();
            analytics.eventClickLoyaltyWidget(channelName);
        }
    }

    private void scrollToLastVisible() {
        if (layoutManager.findFirstCompletelyVisibleItemPosition() == 0) {
            chatRecyclerView.scrollToPosition(0);
        }
    }
}
