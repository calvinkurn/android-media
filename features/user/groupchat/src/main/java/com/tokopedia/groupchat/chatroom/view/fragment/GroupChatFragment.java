package com.tokopedia.groupchat.chatroom.view.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.design.text.BackEditText;
import com.tokopedia.groupchat.GroupChatModuleRouter;
import com.tokopedia.groupchat.R;
import com.tokopedia.groupchat.chatroom.di.DaggerChatroomComponent;
import com.tokopedia.groupchat.chatroom.view.activity.GroupChatActivity;
import com.tokopedia.groupchat.chatroom.view.adapter.chatroom.GroupChatAdapter;
import com.tokopedia.groupchat.chatroom.view.adapter.chatroom.QuickReplyAdapter;
import com.tokopedia.groupchat.chatroom.view.adapter.chatroom.typefactory.GroupChatTypeFactory;
import com.tokopedia.groupchat.chatroom.view.adapter.chatroom.typefactory.GroupChatTypeFactoryImpl;
import com.tokopedia.groupchat.chatroom.view.adapter.chatroom.typefactory.QuickReplyTypeFactory;
import com.tokopedia.groupchat.chatroom.view.adapter.chatroom.typefactory.QuickReplyTypeFactoryImpl;
import com.tokopedia.groupchat.chatroom.view.listener.ChatroomContract;
import com.tokopedia.groupchat.chatroom.view.listener.GroupChatContract;
import com.tokopedia.groupchat.chatroom.view.presenter.ChatroomPresenter;
import com.tokopedia.groupchat.chatroom.view.viewmodel.ChannelInfoViewModel;
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.ChatViewModel;
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.GroupChatPointsViewModel;
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.GroupChatQuickReplyItemViewModel;
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.GroupChatQuickReplyViewModel;
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.ImageAnnouncementViewModel;
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.PendingChatViewModel;
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.PinnedMessageViewModel;
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.SprintSaleAnnouncementViewModel;
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.SprintSaleProductViewModel;
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.SprintSaleViewModel;
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.UserActionViewModel;
import com.tokopedia.groupchat.chatroom.websocket.WebSocketException;
import com.tokopedia.groupchat.common.analytics.EEPromotion;
import com.tokopedia.groupchat.common.analytics.GroupChatAnalytics;
import com.tokopedia.groupchat.common.design.ChannelCloseableBottomSheetDialog;
import com.tokopedia.groupchat.common.design.QuickReplyItemDecoration;
import com.tokopedia.groupchat.common.design.SpaceItemDecoration;
import com.tokopedia.groupchat.common.di.component.DaggerGroupChatComponent;
import com.tokopedia.groupchat.common.di.component.GroupChatComponent;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import static com.tokopedia.groupchat.chatroom.view.activity.GroupChatActivity.PAUSE_RESUME_TRESHOLD_TIME;

/**
 * @author by nisie on 2/6/18.
 */

public class GroupChatFragment extends BaseDaggerFragment implements ChatroomContract.View,
        ChatroomContract.ChatItem.ImageAnnouncementViewHolderListener,
        ChatroomContract.ChatItem.VoteAnnouncementViewHolderListener,
        ChatroomContract.ChatItem.SprintSaleViewHolderListener,
        ChatroomContract.ChatItem.GroupChatPointsViewHolderListener,
        ChatroomContract.QuickReply{

    private static final long DELAY_TIME_SPRINT_SALE = TimeUnit.SECONDS.toMillis(3);
    private static final int REQUEST_LOGIN = 111;
    private static final String NO_USER_ID = "anonymous";
    private static final int KEYBOARD_TRESHOLD = 100;

    private long timeStampAfterPause = 0;
    private long timeStampAfterResume = 0;

    @Inject
    ChatroomPresenter presenter;

    @Inject
    GroupChatAnalytics analytics;

    private RecyclerView chatRecyclerView;
    private RecyclerView quickReplyRecyclerView;
    private BackEditText replyEditText;
    private View sendButton;
    private View divider;
    private View main, loading;
    private GroupChatAdapter adapter;
    private QuickReplyAdapter quickReplyAdapter;
    private LinearLayoutManager layoutManager;
    private View chatNotificationView;
    private View login;
    private View sprintSaleIconLayout;
    private TextView sprintSaleText;

    private UserSessionInterface userSession;

    private Handler sprintSaleHandler;
    private Runnable sprintSaleRunnable;
    private ChannelCloseableBottomSheetDialog pinnedMessageDialog;

    int newMessageCounter;

    private TextWatcher replyTextWatcher;

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {
        GroupChatComponent streamComponent = DaggerGroupChatComponent.builder().baseAppComponent(
                ((BaseMainApplication) getActivity().getApplication()).getBaseAppComponent())
                .build();

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
        userSession = new UserSession(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group_chat_room_new, container, false);
        chatRecyclerView = view.findViewById(R.id.chat_list);
        quickReplyRecyclerView = view.findViewById(R.id.quick_reply);
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
        login = view.findViewById(R.id.login);
        sprintSaleIconLayout = view.findViewById(R.id.sprintsale_icon_layout);
        sprintSaleText = view.findViewById(R.id.sprintsale_text);
        prepareView();
        return view;
    }

    private void prepareView() {
        GroupChatTypeFactory groupChatTypeFactory = new GroupChatTypeFactoryImpl(this, this, this, this);
        adapter = GroupChatAdapter.createInstance(groupChatTypeFactory, ((GroupChatActivity) getActivity()).getList());
        QuickReplyTypeFactory quickReplyTypeFactory = new QuickReplyTypeFactoryImpl(this);
        quickReplyAdapter = new QuickReplyAdapter(quickReplyTypeFactory);
        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        chatRecyclerView.setLayoutManager(layoutManager);
        quickReplyRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        chatRecyclerView.setAdapter(adapter);
        quickReplyRecyclerView.setAdapter(quickReplyAdapter);
        SpaceItemDecoration itemDecoration = new SpaceItemDecoration((int) getActivity()
                .getResources().getDimension(R.dimen.space_chat));
        QuickReplyItemDecoration quickReplyItemDecoration = new QuickReplyItemDecoration((int) getActivity()
                .getResources().getDimension(R.dimen.dp_16));
        chatRecyclerView.addItemDecoration(itemDecoration);
        quickReplyRecyclerView.addItemDecoration(quickReplyItemDecoration);
        chatRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

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

                if (getActivity() != null
                        && getActivity() instanceof GroupChatContract.View
                        && ((GroupChatContract.View) getActivity()).getChannelInfoViewModel() != null) {
                    analytics.eventClickLogin(
                            ((GroupChatContract.View) getActivity()).
                                    getChannelInfoViewModel().getChannelId());
                }

                goToLogin();
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

        setPinnedMessage(((GroupChatContract.View) getActivity()).getPinnedMessage());

        replyEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPinnedMessage(null);
                setQuickReply(null);
                setSprintSaleIcon(null);
            }
        });

        replyEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                setPinnedMessage(null);
                setQuickReply(null);
                setSprintSaleIcon(null);
            }
        });

        replyEditText.setKeyImeChangeListener(new BackEditText.KeyImeChange() {
            @Override
            public void onKeyIme(int keyCode, KeyEvent event) {
                if (KeyEvent.KEYCODE_BACK == event.getKeyCode()) {
                    onKeyboardDismiss();
                }
            }
        });
    }

    private void goToLogin() {
        startActivityForResult(((GroupChatModuleRouter) getActivity().getApplicationContext())
                .getLoginIntent(getActivity()), REQUEST_LOGIN);
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
                        ((GroupChatActivity) getActivity()).sendViaWebSocket(pendingChatViewModel);
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
        initView();
    }

    public void initView() {
        try {
            replyEditText.addTextChangedListener(replyTextWatcher);
            scrollToBottom();

            hideLoading();

            if (getActivity() instanceof GroupChatContract.View) {
                ((GroupChatContract.View) getActivity()).showInfoDialog();
                autoAddSprintSaleAnnouncement(
                        ((GroupChatContract.View) getActivity()).getSprintSaleViewModel(),
                        ((GroupChatContract.View) getActivity()).getChannelInfoViewModel());

                setSprintSaleIcon(((GroupChatContract.View) getActivity()).getSprintSaleViewModel());
                setPinnedMessage(((GroupChatContract.View) getActivity()).getPinnedMessage());
                setQuickReply(((GroupChatContract.View) getActivity()).getChannelInfoViewModel().getQuickRepliesViewModel());
            }

            if (getActivity() != null
                    && ((GroupChatContract.View) getActivity()).getChannelInfoViewModel() != null
                    && ((GroupChatContract.View) getActivity()).getChannelInfoViewModel()
                    .getGroupChatPointsViewModel() != null) {
                autoAddGroupChatPoints(((GroupChatContract.View) getActivity()).getChannelInfoViewModel().getGroupChatPointsViewModel());
            }

            if (getActivity() != null
                    && ((GroupChatContract.View) getActivity()).getChannelInfoViewModel() != null
                    && ((GroupChatContract.View) getActivity()).getChannelInfoViewModel()
                    .getSettingGroupChat() != null) {
                replyEditText.setFilters(new InputFilter[]{
                        new InputFilter.LengthFilter(((GroupChatContract.View) getActivity()).getChannelInfoViewModel()
                                .getSettingGroupChat().getMaxChar())});
            }

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public void refreshChat() {
        initView();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (canResume()) {
            timeStampAfterResume = System.currentTimeMillis();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
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
                && !sprintSaleViewModel.getSprintSaleType().equalsIgnoreCase(SprintSaleViewModel.TYPE_FINISHED)) {
            trackViewSprintSaleComponent(sprintSaleViewModel);
            ChannelInfoViewModel channelInfoViewModel = ((GroupChatContract.View) getActivity())
                    .getChannelInfoViewModel();
            if (channelInfoViewModel != null) {
                analytics.eventViewFlashSale(
                        String.format("%s - %s", channelInfoViewModel.getChannelId(), sprintSaleViewModel.getCampaignName()));
            }
            sprintSaleIconLayout.setVisibility(View.VISIBLE);
            setupSprintSaleIcon(sprintSaleViewModel);
        } else {
            sprintSaleIconLayout.setVisibility(View.GONE);
        }
    }

    private void setPinnedMessage(final PinnedMessageViewModel pinnedMessage) {
        if (getView() != null
                && getActivity() != null
                && getActivity() instanceof GroupChatContract.View
                && ((GroupChatContract.View) getActivity()).getChannelInfoViewModel() != null) {
            ChannelInfoViewModel channelInfoViewModel = ((GroupChatContract.View) getActivity())
                    .getChannelInfoViewModel();
            View pinnedMessageView = getView().findViewById(R.id.pinned_message);
            if (pinnedMessage != null
                    && !TextUtils.isEmpty(pinnedMessage.getTitle())
                    && !TextUtils.isEmpty(pinnedMessage.getMessage())) {
                pinnedMessageView.setVisibility(View.VISIBLE);
                ((TextView) pinnedMessageView.findViewById(R.id.message)).setText(pinnedMessage.getTitle());
                pinnedMessageView.setOnClickListener(view -> {
                    if (channelInfoViewModel != null) {
                        analytics.eventClickAdminPinnedMessage(
                                String.format("%s - %s", channelInfoViewModel.getChannelId(), pinnedMessage.getTitle()));
                    }

                    showPinnedMessageBottomSheet(pinnedMessage);
                });
            } else {
                pinnedMessageView.setVisibility(View.GONE);
            }
        }
    }

    private void setQuickReply(final List<GroupChatQuickReplyItemViewModel> list) {
        if (getView() != null) {
            if (list != null && !list.isEmpty() && userSession.isLoggedIn()) {
                quickReplyRecyclerView.setVisibility(View.VISIBLE);
                quickReplyAdapter.setList(list);
                ChannelInfoViewModel channelInfoViewModel = ((GroupChatContract.View) getActivity())
                        .getChannelInfoViewModel();
                if (channelInfoViewModel != null) {
                    channelInfoViewModel.setQuickRepliesViewModel(list);
                }
            } else {
                quickReplyRecyclerView.setVisibility(View.GONE);
            }
        }
    }

    private void showPinnedMessageBottomSheet(PinnedMessageViewModel pinnedMessage) {
        pinnedMessageDialog = ChannelCloseableBottomSheetDialog.createInstance(getActivity(), () -> {
            ((GroupChatContract.View) getActivity()).showOverlayDialogOnScreen();
        }, new ChannelCloseableBottomSheetDialog.BackHardwareClickedListener() {
            @Override
            public void onBackHardwareClicked() {

            }
        });

        View view = createPinnedMessageView(pinnedMessage);
        pinnedMessageDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                BottomSheetDialog d = (BottomSheetDialog) dialog;

                FrameLayout bottomSheet = d.findViewById(android.support.design.R.id.design_bottom_sheet);

                if (bottomSheet != null) {
                    BottomSheetBehavior.from(bottomSheet)
                            .setState(BottomSheetBehavior.STATE_EXPANDED);
                    view.findViewById(R.id.thumbnail).setVisibility(View.VISIBLE);
                }
            }
        });
        pinnedMessageDialog.setContentView(view, "Pinned Chat");
        view.setOnClickListener(null);
        pinnedMessageDialog.show();
    }

    public boolean isPinnedMessageShowing() {
        if (pinnedMessageDialog != null) {
            return pinnedMessageDialog.isShowing();
        }
        return false;
    }

    private View createPinnedMessageView(final PinnedMessageViewModel pinnedMessage) {
        ChannelInfoViewModel channelInfoViewModel = ((GroupChatContract.View) getActivity()).getChannelInfoViewModel();
        View view = getLayoutInflater().inflate(R.layout.layout_pinned_message_expanded, null);
        ImageHandler.loadImageCircle2(getActivity(), (ImageView) view.findViewById(R.id.pinned_message_avatar)
                , channelInfoViewModel.getAdminPicture(), R.drawable.ic_loading_toped_new);
        ((TextView) view.findViewById(R.id.chat_header).findViewById(R.id.nickname))
                .setText(channelInfoViewModel.getAdminName());
        ((TextView) view.findViewById(R.id.message)).setText(pinnedMessage.getMessage());
        ImageHandler.loadImage(getActivity(), view.findViewById(R.id.thumbnail)
                , pinnedMessage.getThumbnail(), R.drawable.loading_page);
        if (!TextUtils.isEmpty(pinnedMessage.getImageUrl())) {
            view.findViewById(R.id.thumbnail).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((GroupChatModuleRouter) getActivity().getApplicationContext()).openRedirectUrl
                            (getActivity(), pinnedMessage.getImageUrl());
                }
            });
        }
        view.findViewById(R.id.thumbnail).setVisibility(View.GONE);
        return view;
    }

    private void setupSprintSaleIcon(SprintSaleViewModel sprintSaleViewModel) {
        if (sprintSaleViewModel.getSprintSaleType().equalsIgnoreCase(SprintSaleViewModel.TYPE_UPCOMING)) {
            MethodChecker.setBackground(sprintSaleText, MethodChecker.getDrawable(getActivity(),
                    R.drawable.bg_rounded_pink_label));
            sprintSaleText.setTextColor(MethodChecker.getColor(getActivity(), R.color.red_500));
            sprintSaleText.setText(String.format("%s - %s", sprintSaleViewModel
                    .getFormattedStartDate(), sprintSaleViewModel.getFormattedEndDate()));
            sprintSaleText.setTextColor(MethodChecker.getColor(getActivity(), R.color.red_500));
        } else if (sprintSaleViewModel.getSprintSaleType().equalsIgnoreCase(SprintSaleViewModel.TYPE_ACTIVE)) {
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
                && !sprintSaleViewModel.getSprintSaleType().equalsIgnoreCase(SprintSaleViewModel.TYPE_UPCOMING)
                && !sprintSaleViewModel.getSprintSaleType().equalsIgnoreCase(SprintSaleViewModel.TYPE_FINISHED)
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

                    if (adapter.getList().size() == 0 ||
                            (adapter.getItemAt(0) != null
                                    && !(adapter.getItemAt(0) instanceof SprintSaleAnnouncementViewModel))) {
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

    @Override
    public void addQuickReply(String message) {

        if (getActivity() != null
                && getActivity() instanceof GroupChatContract.View
                && ((GroupChatContract.View) getActivity()).getChannelInfoViewModel() != null) {
            analytics.eventClickQuickReply(
                    String.format("%s - %s", ((GroupChatContract.View) getActivity()).
                            getChannelInfoViewModel().getChannelId(), message));
        }

        String text = replyEditText.getText().toString();
        int index = replyEditText.getSelectionStart();
        replyEditText.setText(Html.fromHtml(String.format("%s %s %s", text.substring(0, index), message, text
                .substring(index))));
        PendingChatViewModel pendingChatViewModel = new PendingChatViewModel
                (presenter.checkText(replyEditText.getText().toString()),
                        userSession.getUserId(),
                        userSession.getName(),
                        userSession.getProfilePicture(),
                        false);

        ((GroupChatActivity) getActivity()).sendViaWebSocket(pendingChatViewModel);
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
            if (adapter.getList().size() == 0 || (adapter.getItemAt(0) != null
                    && !(adapter.getItemAt(0) instanceof GroupChatPointsViewModel))) {
                addIncomingMessage(groupChatPointsViewModel);
                ((GroupChatContract.View) getActivity()).removeGroupChatPoints();
                ((GroupChatContract.View) getActivity()).vibratePhone();
            }
        }
    }

    private boolean isValidSprintSale(@Nullable SprintSaleViewModel sprintSaleViewModel) {
        return sprintSaleViewModel != null
                && sprintSaleViewModel.getStartDate() != 0
                && sprintSaleViewModel.getEndDate() != 0
                && sprintSaleViewModel.getEndDate() > getCurrentTime();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (canPause()) {
            if (sprintSaleHandler != null && sprintSaleRunnable != null) {
                sprintSaleHandler.removeCallbacks(sprintSaleRunnable);
            }

            timeStampAfterPause = System.currentTimeMillis();

        }
    }

    private boolean canResume() {
        return timeStampAfterResume == 0 || (timeStampAfterResume > 0 && System.currentTimeMillis()
                - timeStampAfterResume > PAUSE_RESUME_TRESHOLD_TIME);
    }

    private boolean canPause() {
        return timeStampAfterPause == 0 || (timeStampAfterPause > 0 && System.currentTimeMillis()
                - timeStampAfterPause > PAUSE_RESUME_TRESHOLD_TIME
                && canResume());
    }

    @Override
    public void onDestroy() {
        presenter.detachView();
        super.onDestroy();
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

    public void scrollToBottom() {
        resetNewMessageCounter();
        new Handler().postDelayed(() -> chatRecyclerView.scrollToPosition(0), 200);
    }

    private void resetNewMessageCounter() {
        newMessageCounter = 0;
        chatNotificationView.setVisibility(View.GONE);
    }

    @Override
    public void onErrorSendMessage(PendingChatViewModel pendingChatViewModel, String errorMessage) {
        NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
    }

    @Override
    public void onSuccessSendMessage(PendingChatViewModel pendingChatViewModel) {
        ChatViewModel viewModel = new ChatViewModel(
                pendingChatViewModel.getMessage(),
                System.currentTimeMillis(),
                System.currentTimeMillis(),
                "",
                userSession.getUserId(),
                userSession.getName(),
                userSession.getProfilePicture(),
                false,
                false);
        adapter.addReply(viewModel);
        adapter.notifyDataSetChanged();
        ChannelInfoViewModel channelInfoViewModel = ((GroupChatContract.View) getActivity())
                .getChannelInfoViewModel();
        if (channelInfoViewModel != null) {
            channelInfoViewModel.setQuickRepliesViewModel(null);
        }
        setQuickReply(null);
        scrollToBottom();
    }


    public void afterSendMessage(PendingChatViewModel pendingChatViewModel, Exception errorSendIndicator) {
        if (errorSendIndicator == null) {
            onSuccessSendMessage(pendingChatViewModel);
        } else if (errorSendIndicator != null && !(errorSendIndicator instanceof WebSocketException)) {
            onErrorSendMessage(pendingChatViewModel, errorSendIndicator.getMessage());
        }

        if (errorSendIndicator != null && errorSendIndicator instanceof WebSocketException && getActivity() != null && getActivity() instanceof GroupChatActivity) {
            ((GroupChatActivity) getActivity()).setSnackBarRetry();
        }
        KeyboardHandler.DropKeyboard(getActivity(), getView());
        clearMessageEditText();
        onKeyboardDismiss();
        setQuickReply(null);
        setSendButtonEnabled(true);
    }

    public void onMessageReceived(Visitable messageItem, boolean hideMessage) {
        if (messageItem instanceof SprintSaleAnnouncementViewModel) {
            setSprintSaleIcon(((GroupChatContract.View) getActivity()).getSprintSaleViewModel());
        }

        if (messageItem instanceof PinnedMessageViewModel) {
            setPinnedMessage((PinnedMessageViewModel) messageItem);
        }

        if (messageItem instanceof GroupChatQuickReplyViewModel) {
            setQuickReply(((GroupChatQuickReplyViewModel) messageItem).getList());
        }

        if (messageItem instanceof ImageAnnouncementViewModel) {
            analytics.eventViewImageAnnouncement(
                    String.format("%s - %s"
                            , ((GroupChatContract.View) getActivity()).getChannelInfoViewModel().getChannelId()
                            , ((ImageAnnouncementViewModel) messageItem).getRedirectUrl()));
        }

        if (!hideMessage) {
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
    public void onImageAnnouncementClicked(ImageAnnouncementViewModel image) {
        analytics.eventClickThumbnail(String.format("%s - %s", ((GroupChatContract.View) getActivity()).
                getChannelInfoViewModel().getChannelId(), image.getRedirectUrl()));
        if (!TextUtils.isEmpty(image.getRedirectUrl())) {
            ((GroupChatModuleRouter) getActivity().getApplication()).openRedirectUrl(getActivity(), image.getRedirectUrl());
        }
    }

    @Override
    public void onVoteComponentClicked(String type, String name, String voteUrl) {
        if (getActivity() instanceof GroupChatActivity) {
            ((GroupChatActivity) getActivity()).transitionToTabVote();
        }
        analytics.eventClickVoteComponent(GroupChatAnalytics.COMPONENT_VOTE, name);
    }

    private void setForLoginUser(boolean isLoggedIn) {
        if (isLoggedIn) {
            setReplyTextHint();
            divider.setVisibility(View.VISIBLE);
            replyEditText.setVisibility(View.VISIBLE);
            sendButton.setVisibility(View.VISIBLE);
            login.setVisibility(View.GONE);
            setQuickReply(((GroupChatContract.View) getActivity()).getChannelInfoViewModel().getQuickRepliesViewModel());
        } else {
            divider.setVisibility(View.GONE);
            replyEditText.setVisibility(View.GONE);
            sendButton.setVisibility(View.GONE);
            quickReplyRecyclerView.setVisibility(View.GONE);
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
                && !((GroupChatContract.View) getActivity()).getSprintSaleViewModel().getSprintSaleType().equalsIgnoreCase
                (SprintSaleAnnouncementViewModel.SPRINT_SALE_FINISH)) {


            ((GroupChatModuleRouter) getActivity().getApplicationContext()).openRedirectUrl(getActivity()
                    , ((GroupChatContract.View) getActivity()).generateAttributeApplink
                            (productViewModel.getProductUrl(), GroupChatAnalytics.ATTRIBUTE_FLASH_SALE));

            ChannelInfoViewModel channelInfoViewModel = ((GroupChatContract.View) getActivity())
                    .getChannelInfoViewModel();
            if (channelInfoViewModel != null) {
                analytics.eventClickFlashSale(
                        String.format("%s - %s", channelInfoViewModel.getChannelId(),
                                ((GroupChatContract.View) getActivity()).getSprintSaleViewModel().getCampaignName()));
            }
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
                && !((GroupChatContract.View) getActivity()).getSprintSaleViewModel().getSprintSaleType().equalsIgnoreCase
                (SprintSaleAnnouncementViewModel.SPRINT_SALE_FINISH)) {

            ((GroupChatModuleRouter) getActivity().getApplicationContext()).openRedirectUrl(getActivity()
                    , ((GroupChatContract.View) getActivity()).generateAttributeApplink
                            (sprintSaleAnnouncementViewModel.getRedirectUrl(),
                                    GroupChatAnalytics.ATTRIBUTE_FLASH_SALE));

            ChannelInfoViewModel channelInfoViewModel = ((GroupChatContract.View) getActivity())
                    .getChannelInfoViewModel();
            if (channelInfoViewModel != null) {
                analytics.eventClickFlashSale(
                        String.format("%s - %s", channelInfoViewModel.getChannelId(),
                                ((GroupChatContract.View) getActivity()).getSprintSaleViewModel().getCampaignName()));
            }
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

        ChannelInfoViewModel channelInfoViewModel = ((GroupChatContract.View) getActivity())
                .getChannelInfoViewModel();
        if (channelInfoViewModel != null) {
            analytics.eventClickFlashSale(
                    String.format("%s - %s", channelInfoViewModel.getChannelId(),
                            ((GroupChatContract.View) getActivity()).getSprintSaleViewModel().getCampaignName()));
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_LOGIN) {
            ((GroupChatContract.View) getActivity()).onSuccessLogin();
            userSession = new UserSession(getActivity());
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
            analytics.eventClickLoyaltyWidget(((GroupChatActivity) getActivity()).getChannelInfoViewModel().getChannelId());
        }
    }

    public void onKeyboardDismiss() {
        if (getActivity() != null
                && ((GroupChatActivity) getActivity()).getChannelInfoViewModel() != null) {
            setPinnedMessage(((GroupChatContract.View) getActivity()).getChannelInfoViewModel().getPinnedMessageViewModel());
            setQuickReply(((GroupChatContract.View) getActivity()).getChannelInfoViewModel().getQuickRepliesViewModel());
            setSprintSaleIcon(((GroupChatContract.View) getActivity()).getSprintSaleViewModel());
        }
    }

    public void clearMessageEditText() {
        replyEditText.setText("");
    }

    public List<Visitable> getList() {
        return adapter.getList();
    }

}
