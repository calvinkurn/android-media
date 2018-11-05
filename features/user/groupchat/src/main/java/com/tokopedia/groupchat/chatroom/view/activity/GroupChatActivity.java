package com.tokopedia.groupchat.chatroom.view.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.facebook.CallbackManager;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.sendbird.android.OpenChannel;
import com.sendbird.android.SendBird;
import com.sendbird.android.User;
import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.abstraction.constant.TkpdState;
import com.tokopedia.design.card.ToolTipUtils;
import com.tokopedia.groupchat.GroupChatModuleRouter;
import com.tokopedia.groupchat.R;
import com.tokopedia.groupchat.channel.view.activity.ChannelActivity;
import com.tokopedia.groupchat.channel.view.model.ChannelViewModel;
import com.tokopedia.groupchat.chatroom.data.ChatroomUrl;
import com.tokopedia.groupchat.chatroom.di.DaggerChatroomComponent;
import com.tokopedia.groupchat.chatroom.domain.ConnectionManager;
import com.tokopedia.groupchat.chatroom.domain.pojo.ExitMessage;
import com.tokopedia.groupchat.chatroom.domain.usecase.ChannelHandlerUseCase;
import com.tokopedia.groupchat.chatroom.domain.usecase.LoginGroupChatUseCase;
import com.tokopedia.groupchat.chatroom.view.adapter.tab.GroupChatTabAdapter;
import com.tokopedia.groupchat.chatroom.view.fragment.ChannelInfoFragment;
import com.tokopedia.groupchat.chatroom.view.fragment.ChannelVoteFragment;
import com.tokopedia.groupchat.chatroom.view.fragment.GroupChatFragment;
import com.tokopedia.groupchat.chatroom.view.fragment.GroupChatVideoFragment;
import com.tokopedia.groupchat.chatroom.view.listener.ChannelInfoFragmentListener;
import com.tokopedia.groupchat.chatroom.view.listener.ChannelVoteContract;
import com.tokopedia.groupchat.chatroom.view.listener.GroupChatContract;
import com.tokopedia.groupchat.chatroom.view.preference.NotificationPreference;
import com.tokopedia.groupchat.chatroom.view.presenter.GroupChatPresenter;
import com.tokopedia.groupchat.chatroom.view.viewmodel.ChannelInfoViewModel;
import com.tokopedia.groupchat.chatroom.view.viewmodel.GroupChatViewModel;
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.AdsViewModel;
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.GroupChatPointsViewModel;
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.GroupChatQuickReplyItemViewModel;
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.GroupChatQuickReplyViewModel;
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.PinnedMessageViewModel;
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.SprintSaleAnnouncementViewModel;
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.SprintSaleViewModel;
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.UserActionViewModel;
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.VibrateViewModel;
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.VideoViewModel;
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.VoteAnnouncementViewModel;
import com.tokopedia.groupchat.chatroom.view.viewmodel.tab.TabViewModel;
import com.tokopedia.groupchat.common.analytics.EEPromotion;
import com.tokopedia.groupchat.common.analytics.GroupChatAnalytics;
import com.tokopedia.groupchat.common.applink.ApplinkConstant;
import com.tokopedia.groupchat.common.design.CloseableBottomSheetDialog;
import com.tokopedia.groupchat.common.di.component.DaggerGroupChatComponent;
import com.tokopedia.groupchat.common.di.component.GroupChatComponent;
import com.tokopedia.groupchat.common.util.TextFormatter;
import com.tokopedia.groupchat.common.util.TransparentStatusBarHelper;
import com.tokopedia.groupchat.vote.view.model.VoteInfoViewModel;
import com.tokopedia.groupchat.vote.view.model.VoteViewModel;
import com.tokopedia.youtubeutils.common.YoutubePlayerConstant;

import java.util.ArrayList;
import java.util.List;
import java.util.UnknownFormatConversionException;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

/**
 * @author by nisie on 2/6/18.
 */

public class GroupChatActivity extends BaseSimpleActivity
        implements GroupChatTabAdapter.TabListener, GroupChatContract.View,
        LoginGroupChatUseCase.LoginGroupChatListener, ChannelHandlerUseCase.ChannelHandlerListener
        , ToolTipUtils.ToolTipListener {

    private static final String TOKOPEDIA_APPLINK = "tokopedia://";
    Dialog exitDialog;
    private static final float ELEVATION = 10;
    private static final int YOUTUBE_DELAY = 1500;
    private long onPlayTime, onPauseTime, onEndTime, onLeaveTime, onTrackingTime;
    private Handler youtubeRunnable;

    @DeepLink(ApplinkConstant.GROUPCHAT_ROOM)
    public static TaskStackBuilder getCallingTaskStack(Context context, Bundle extras) {
        UserSession userSession = ((AbstractionRouter) context.getApplicationContext()).getSession();

        String id = extras.getString(ApplinkConstant.PARAM_CHANNEL_ID);
        Intent homeIntent = ((GroupChatModuleRouter) context.getApplicationContext()).getHomeIntent(context);
        Intent detailsIntent = GroupChatActivity.getCallingIntent(context, id);
        Intent parentIntent = ((GroupChatModuleRouter) context.getApplicationContext())
                .getInboxChannelsIntent(context);

        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
        taskStackBuilder.addNextIntent(homeIntent);
        taskStackBuilder.addNextIntent(detailsIntent);
        return taskStackBuilder;
    }

    @DeepLink(ApplinkConstant.GROUPCHAT_ROOM_VIA_LIST)
    public static TaskStackBuilder getCallingTaskStackViaList(Context context, Bundle extras) {
        String id = extras.getString(ApplinkConstant.PARAM_CHANNEL_ID);
        Intent homeIntent = ((GroupChatModuleRouter) context.getApplicationContext()).getHomeIntent(context);
        Intent detailsIntent = GroupChatActivity.getCallingIntent(context, id);
        Intent parentIntent = ((GroupChatModuleRouter) context.getApplicationContext())
                .getInboxChannelsIntent(context);

        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
        taskStackBuilder.addNextIntent(homeIntent);
        taskStackBuilder.addNextIntent(parentIntent);
        taskStackBuilder.addNextIntent(detailsIntent);
        return taskStackBuilder;
    }

    @DeepLink(ApplinkConstant.GROUPCHAT_VOTE_VIA_LIST)
    public static TaskStackBuilder getCallingTaskStackVoteViaList(Context context, Bundle extras) {
        String id = extras.getString(ApplinkConstant.PARAM_CHANNEL_ID);
        Intent homeIntent = ((GroupChatModuleRouter) context.getApplicationContext()).getHomeIntent(context);
        Intent detailsIntent = GroupChatActivity.getCallingIntent(context, id);
        detailsIntent.putExtra(INITIAL_FRAGMENT, CHANNEL_VOTE_FRAGMENT);
        Intent parentIntent = ((GroupChatModuleRouter) context.getApplicationContext())
                .getInboxChannelsIntent(context);

        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
        taskStackBuilder.addNextIntent(homeIntent);
        taskStackBuilder.addNextIntent(parentIntent);
        taskStackBuilder.addNextIntent(detailsIntent);
        return taskStackBuilder;
    }

    private static final long VIBRATE_LENGTH = TimeUnit.SECONDS.toMillis(1);
    private static final long KICK_TRESHOLD_TIME = TimeUnit.MINUTES.toMillis(15);
    private static final long TOOLTIP_DELAY = 1500L;

    private static final int KEYBOARD_TRESHOLD = 100;
    private static final int CHATROOM_FRAGMENT = 0;
    private static final int CHANNEL_VOTE_FRAGMENT = 1;
    private static final int CHANNEL_INFO_FRAGMENT = 2;

    public static final String EXTRA_CHANNEL_UUID = "CHANNEL_UUID";
    public static final String EXTRA_CHANNEL_INFO = "CHANNEL_INFO";
    public static final String EXTRA_SHOW_BOTTOM_DIALOG = "SHOW_BOTTOM";
    public static final String ARGS_VIEW_MODEL = "GC_VIEW_MODEL";
    public static final String INITIAL_FRAGMENT = "init_fragment";
    public static final String VOTE = "vote";
    public static final String TOTAL_VIEW = "total_view";
    public static final String EXTRA_POSITION = "position";

    public static final String TAG_CHANNEL = "{channel_url}";

    private Runnable runnable;
    private Handler tooltipHandler;
    private boolean canShowDialog = true;
    private boolean isFirstTime;
    private boolean shouldRefreshAfterLogin = false;
    private boolean youtubeIsFullScreen = false;
    private BroadcastReceiver notifReceiver;

    public View rootView, loading, main;
    private Toolbar toolbar;
    private ImageView channelBanner;
    private RecyclerView tabs;
    private GroupChatTabAdapter tabAdapter;
    private CloseableBottomSheetDialog channelInfoDialog;
    private View sponsorLayout;
    private ImageView sponsorImage;
    private GroupChatVideoFragment videoFragment;
    private YouTubePlayer youTubePlayer;

    private int initialFragment;
    private GroupChatViewModel viewModel;

    private CallbackManager callbackManager;
    private OpenChannel mChannel;

    @Inject
    GroupChatPresenter presenter;

    @Inject
    GroupChatAnalytics analytics;

    @Inject
    NotificationPreference notificationPreference;

    SharedPreferences sharedPreferences;

    private UserSession userSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            initialFragment = savedInstanceState.getInt(INITIAL_FRAGMENT, CHATROOM_FRAGMENT);
        } else if (getIntent().getExtras() != null) {
            initialFragment = getIntent().getExtras().getInt(INITIAL_FRAGMENT, CHATROOM_FRAGMENT);
        } else {
            initialFragment = CHATROOM_FRAGMENT;
        }

        isFirstTime = true;

        if (savedInstanceState != null) {
            viewModel = savedInstanceState.getParcelable(ARGS_VIEW_MODEL);
        } else if (getIntent().getExtras() != null) {
            String path = getIntent().getExtras().getString("channel_id", "");
            if(TextUtils.isEmpty(path)) {
                path = getIntent().getExtras().getString(GroupChatActivity.EXTRA_CHANNEL_UUID, "");
            }
            viewModel = new GroupChatViewModel(path, getIntent().getExtras().getInt(GroupChatActivity
                    .EXTRA_POSITION, -1));
        } else {
            Intent intent = new Intent();
            intent.putExtra(ChannelActivity.RESULT_MESSAGE, getString(R.string.default_request_error_unknown));
            if (viewModel != null) {
                intent.putExtra(TOTAL_VIEW, viewModel.getTotalView());
                intent.putExtra(EXTRA_POSITION, viewModel.getChannelPosition());
            }
            setResult(ChannelActivity.RESULT_ERROR_ENTER_CHANNEL, intent);
            finish();
        }

        callbackManager = CallbackManager.Factory.create();
        userSession = ((AbstractionRouter) getApplication()).getSession();

        initView();
        initInjector();
        initData();
        initPreference();
    }

    public void initVideoFragment(ChannelInfoViewModel channelInfoViewModel) {
        findViewById(R.id.video_container_layout).setVisibility(View.GONE);
        if(channelInfoViewModel == null){
            return;
        }
        setSponsorData();

        if (!TextUtils.isEmpty(channelInfoViewModel.getVideoId())) {
            videoFragment = (GroupChatVideoFragment) getSupportFragmentManager().findFragmentById(R.id.video_container);
            if (videoFragment == null)
                return;
            findViewById(R.id.video_container_layout).setVisibility(View.VISIBLE);
            sponsorLayout.setVisibility(View.GONE);

             if (youTubePlayer != null) {
                youTubePlayer.cueVideo(channelInfoViewModel.getVideoId());
                youtubeRunnable.postDelayed(new Runnable() {
                     @Override
                     public void run() {
                         youTubePlayer.play();
                     }
                 }, YOUTUBE_DELAY);
                return;
            }

            videoFragment.initialize(YoutubePlayerConstant.GOOGLE_API_KEY, new YouTubePlayer.OnInitializedListener() {
                @Override
                public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
                    if (!wasRestored) {
                        try {
                            youTubePlayer = player;

                            //set the player style default
                            youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
                            youTubePlayer.setShowFullscreenButton(false);
                            //cue the 1st video by default
                            youTubePlayer.cueVideo(channelInfoViewModel.getVideoId());
                            youtubeRunnable.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if(youTubePlayer != null){
                                        youTubePlayer.play();
                                    }
                                }
                            }, YOUTUBE_DELAY);

                            youTubePlayer.setPlaybackEventListener(new YouTubePlayer.PlaybackEventListener() {
                                String TAG = "youtube";

                                @Override
                                public void onPlaying() {
                                    Log.i(TAG, "onPlaying: ");
                                    if (onPlayTime == 0) {
                                        onPlayTime = System.currentTimeMillis() / 1000L;
                                        analytics.eventClickAutoPlayVideo(getChannelInfoViewModel().getChannelId());
                                    }
                                }

                                @Override
                                public void onPaused() {
                                    Log.i(TAG, "onPaused: ");
                                    onPauseTime = System.currentTimeMillis() / 1000L;
                                }

                                @Override
                                public void onStopped() {
                                    Log.i(TAG, "onStopped: ");
                                }

                                @Override
                                public void onBuffering(boolean b) {
                                    Log.i(TAG, "onBuffering: ");
                                }

                                @Override
                                public void onSeekTo(int i) {
                                    Log.i(TAG, "onSeekTo: ");
                                }
                            });

                            youTubePlayer.setPlayerStateChangeListener(new YouTubePlayer.PlayerStateChangeListener() {
                                String TAG = "youtube";

                                @Override
                                public void onLoading() {
                                    Log.i(TAG, "onLoading: ");
                                }

                                @Override
                                public void onLoaded(String s) {
                                    Log.i(TAG, "onLoaded: ");
                                }

                                @Override
                                public void onAdStarted() {
                                    Log.i(TAG, "onAdStarted: ");
                                }

                                @Override
                                public void onVideoStarted() {
                                    Log.i(TAG, "onVideoStarted: ");
                                }

                                @Override
                                public void onVideoEnded() {
                                    Log.i(TAG, "onVideoEnded: ");
                                    onEndTime = System.currentTimeMillis() / 1000L;
                                }

                                @Override
                                public void onError(YouTubePlayer.ErrorReason errorReason) {
                                    Log.i(TAG, errorReason.getDeclaringClass() + " onError: " + errorReason.name());
                                }
                            });
                        } catch (Exception e) {
                            onInitializationFailure(provider, YouTubeInitializationResult.SERVICE_MISSING);
                        }
                    }
                }

                @Override
                public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                    Log.e(GroupChatActivity.class.getSimpleName(), "Youtube Player View initialization failed");
                }
            });
        }

    }

    private void initVideoSize() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;

        RelativeLayout videoContainer = findViewById(R.id.video_container_layout);
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) videoContainer
                .getLayoutParams();
        params.height = width / 16 * 9;
        videoContainer.setLayoutParams(params);

        videoContainer.setVisibility(View.VISIBLE);


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(ARGS_VIEW_MODEL, viewModel);
        outState.putInt(INITIAL_FRAGMENT, initialFragment);
    }

    private void initInjector() {
        GroupChatComponent streamComponent = DaggerGroupChatComponent.builder()
                .baseAppComponent(((BaseMainApplication) getApplication()).getBaseAppComponent())
                .build();

        DaggerChatroomComponent.builder()
                .groupChatComponent(streamComponent)
                .build().inject(this);


        presenter.attachView(this);
    }

    private void initView() {

        Bundle bundle = new Bundle();
        if (getIntent().getExtras() != null) {
            bundle.putAll(getIntent().getExtras());
        }

        setupToolbar();

        loading = findViewById(R.id.loading);
        main = findViewById(R.id.main_content);

        channelInfoDialog = CloseableBottomSheetDialog.createInstance(this);
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

        sponsorLayout = findViewById(R.id.sponsor_layout);
        sponsorImage = findViewById(R.id.sponsor_image);

        runnable = new Runnable() {
            @Override
            public void run() {
                showTooltip();
            }
        };
//
//        youtubeRunnable =
        youtubeRunnable = new Handler();

    }

    private void initData() {
        presenter.getChannelInfo(viewModel.getChannelUuid());
        showLoading();
    }

    private void initPreference() {
        if (userSession != null
                && !TextUtils.isEmpty(userSession.getUserId())
                && getApplication() instanceof GroupChatModuleRouter) {

            sharedPreferences =
                    PreferenceManager.getDefaultSharedPreferences(getContext());

            String NOTIFICATION_GROUP_CHAT =
                    ((GroupChatModuleRouter) getApplication()).getNotificationPreferenceConstant();

            boolean isNotificationOn =
                    sharedPreferences.getBoolean(NOTIFICATION_GROUP_CHAT, false);

            if (!isNotificationOn
                    && notificationPreference.isFirstTimeUser(userSession.getUserId())) {
                sharedPreferences.edit().putBoolean(NOTIFICATION_GROUP_CHAT, true).apply();
            }

            notificationPreference.setFirstTime(userSession.getUserId());
        }
    }

    public void showLoading() {
        loading.setVisibility(View.VISIBLE);
        main.setVisibility(View.GONE);
        setChannelNotFoundView(View.GONE);
    }

    public void hideLoading() {
        loading.setVisibility(View.GONE);
        main.setVisibility(View.VISIBLE);
    }

    @Override
    protected void setupStatusBar() {

    }

    private void setupToolbar() {
        if (isLollipopOrNewer()) {
            TransparentStatusBarHelper.assistActivity(this);
        }
        removePaddingStatusBar();

        toolbar = findViewById(R.id.toolbar);
        channelBanner = findViewById(R.id.channel_banner);

        if (isLollipopOrNewer()) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
            toolbar.setPadding(0, getStatusBarHeight(), 0, 0);
        }
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

    }

    private boolean isLollipopOrNewer() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.group_chat_room_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else if (item.getItemId() == R.id.action_share) {
            shareChannel();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void shareChannel() {
        analytics.eventClickShare();

        String link = ChatroomUrl.GROUP_CHAT_URL.replace(TAG_CHANNEL, viewModel.getChannelUrl());

        String description = String.format("%s %s", String.format(getString(R.string.lets_join_channel),
                viewModel.getChannelName()), "");

        String userId = "0";
        if(userSession.isLoggedIn()){
            userId = userSession.getUserId();
        }

        ((GroupChatModuleRouter) getApplication()).shareGroupChat(this,
                viewModel.getChannelInfoViewModel().getChannelId(), viewModel.getChannelName(), description,
                viewModel.getChannelInfoViewModel().getBannerUrl(), viewModel.getChannelUrl(), userId, "sharing");
    }

    private void setupViewPager() {
        tabs = findViewById(R.id.tab);
        tabs.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        tabAdapter = GroupChatTabAdapter.createInstance(this, createListFragment());
        tabs.setAdapter(tabAdapter);
    }

    private List<TabViewModel> createListFragment() {
        List<TabViewModel> list = new ArrayList<>();
        list.add(new TabViewModel(getString(R.string.title_group_chat)));
        if (checkPollValid()) {
            list.add(new TabViewModel(getString(R.string.title_vote)));
        }
        list.add(new TabViewModel(getString(R.string.title_info)));
        return list;
    }

    private void showFragment(int fragmentPosition) {
        try {
            tabAdapter.setActiveFragment(fragmentPosition);
            if (fragmentPosition == initialFragment && !isFirstTime) {
                return;
            }
            isFirstTime = false;
            this.initialFragment = fragmentPosition;
            switch (fragmentPosition) {
                case CHATROOM_FRAGMENT:
                    showChatroomFragment(mChannel);
                    break;
                case CHANNEL_VOTE_FRAGMENT:
                    if (checkPollValid()) {
                        showChannelVoteFragment();
                    } else {
                        showChannelInfoFragment();
                    }
                    break;
                case CHANNEL_INFO_FRAGMENT:
                    showChannelInfoFragment();
                    break;
                default:
                    break;
            }

        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }

    }

    private void showChatroomFragment(OpenChannel mChannel) {

        if (mChannel != null) {

            Bundle bundle = new Bundle();
            if (getIntent().getExtras() != null) {
                bundle.putAll(getIntent().getExtras());
            }

            Fragment fragment = getSupportFragmentManager().findFragmentByTag
                    (GroupChatFragment.class.getSimpleName());
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            if (fragment == null) {
                fragment = GroupChatFragment.createInstance(bundle);
                ((GroupChatFragment) fragment).setChannel(mChannel);
            } else {
                ((GroupChatFragment) fragment).setChannel(mChannel);
                ((GroupChatFragment) fragment).refreshChat();
            }
            fragmentTransaction.replace(R.id.container, fragment, fragment.getClass().getSimpleName());
            fragmentTransaction.commitAllowingStateLoss();

        }
    }

    private void showChannelInfoFragment() {
        Fragment fragment = populateChannelInfoFragment();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment, fragment.getClass().getSimpleName());
        fragmentTransaction.commitAllowingStateLoss();
    }

    private void showChannelVoteFragment() {
        Bundle bundle = new Bundle();
        if (getIntent().getExtras() != null) {
            bundle.putAll(getIntent().getExtras());
        }
        bundle.putParcelable(VOTE, viewModel.getChannelInfoViewModel().getVoteInfoViewModel());

        Fragment fragment = getSupportFragmentManager().findFragmentByTag
                (ChannelVoteFragment.class.getSimpleName());
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (fragment == null) {
            fragment = ChannelVoteFragment.createInstance(bundle);
        }
        ((ChannelVoteContract.View) fragment).showVoteLayout(
                viewModel.getChannelInfoViewModel().getVoteInfoViewModel());
        fragmentTransaction.replace(R.id.container, fragment, fragment.getClass().getSimpleName());
        fragmentTransaction.commitAllowingStateLoss();
    }

    private void removePaddingStatusBar() {

        rootView = findViewById(R.id.root_view);
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int heightDiff = rootView.getRootView().getHeight() - rootView.getHeight();

                if (heightDiff > KEYBOARD_TRESHOLD) {
                    removePaddingIfKeyboardIsShowing();
                } else {
                    addPaddingIfKeyboardIsClosed();
                }
            }
        });
    }

    private void addPaddingIfKeyboardIsClosed() {
        if (isLollipopOrNewer() && getSoftButtonsBarSizePort(GroupChatActivity.this) > 0) {
            FrameLayout container = rootView.findViewById(R.id.container);
            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) container
                    .getLayoutParams();
            params.setMargins(0, 0, 0, getSoftButtonsBarSizePort(GroupChatActivity.this));
            container.setLayoutParams(params);
        }
    }

    private void removePaddingIfKeyboardIsShowing() {
        if (isLollipopOrNewer() && getSoftButtonsBarSizePort(GroupChatActivity.this) > 0) {
            FrameLayout container = rootView.findViewById(R.id.container);
            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) container.getLayoutParams();
            params.setMargins(0, 0, 0, 0);
            container.setLayoutParams(params);
        }
    }

    public static int getSoftButtonsBarSizePort(Activity activity) {
        // getRealMetrics is only available with API 17 and +
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            DisplayMetrics metrics = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
            int usableHeight = metrics.heightPixels;
            activity.getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
            int realHeight = metrics.heightPixels;
            if (realHeight > usableHeight)
                return realHeight - usableHeight;
            else
                return 0;
        }
        return 0;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_group_chat;
    }

    @Override
    public void onBackPressed() {
        if(youtubeIsFullScreen && youTubePlayer != null){
            youTubePlayer.setFullscreen(false);
            return;
        }

        if (currentlyLoadingFragment() || hasErrorEmptyState()) {
            finish();
            super.onBackPressed();
        } else {
            if (!currentFragmentIsChat()) {
                showFragment(CHATROOM_FRAGMENT);
            } else {
                showDialogConfirmToExit();
            }
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    private boolean hasErrorEmptyState() {
        return rootView.findViewById(R.id.main_retry) != null
                && rootView.findViewById(R.id.main_retry).getVisibility() == View.VISIBLE;
    }

    private void showDialogConfirmToExit() {
        if (getExitMessage() == null) {
            if (onPlayTime != 0) {
                analytics.eventWatchVideoDuration(getChannelInfoViewModel().getChannelId(), getDurationWatchVideo());
            }
            finish();
            GroupChatActivity.super.onBackPressed();
            return;
        }
        exitDialog.show();
    }

    private String getDurationWatchVideo() {
        if (onEndTime != 0) {
            return String.valueOf(onEndTime - onPlayTime);
        } else if (onPauseTime != 0) {
            return String.valueOf(onPauseTime - onPlayTime);
        } else {
            onLeaveTime = System.currentTimeMillis() / 1000L;
            return String.valueOf(onLeaveTime - onPlayTime);
        }
    }

    private android.app.AlertDialog.Builder createAlertDialog() {
        AlertDialog.Builder myAlertDialog = new android.app.AlertDialog.Builder(this);
        myAlertDialog.setTitle(getExitMessage().getTitle());
        myAlertDialog.setMessage(getExitMessage().getBody());
        final Context context = this;
        myAlertDialog.setPositiveButton(getString(R.string.exit_group_chat_yes), new
                DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (isTaskRoot()) {
                            startActivity(((GroupChatModuleRouter) getApplicationContext()).getInboxChannelsIntent(context));
                        }
                        if (onPlayTime != 0) {
                            analytics.eventWatchVideoDuration(getChannelInfoViewModel().getChannelId(), getDurationWatchVideo());
                        }
                        finish();
                        GroupChatActivity.super.onBackPressed();
                    }
                });
        myAlertDialog.setNegativeButton(getString(R.string.exit_group_chat_no), new
                DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

        return myAlertDialog;
    }


    @Override
    protected void setupLayout(Bundle savedInstanceState) {
        setContentView(getLayoutRes());
    }

    @Override
    protected Fragment getNewFragment() {
        return null;
    }

    @Override
    public void onTabClicked(int position) {
        showFragment(position);
    }

    /**
     * @param context          activity context
     * @param channelViewModel only to be used from channel list.
     * @param position         channel position from list
     * @return Intent
     */
    public static Intent getCallingIntent(Context context, ChannelViewModel channelViewModel, int position) {
        Intent intent = new Intent(context, GroupChatActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_CHANNEL_INFO, channelViewModel);
        bundle.putString(EXTRA_CHANNEL_UUID, channelViewModel.getChannelUrl());
        bundle.putBoolean(EXTRA_SHOW_BOTTOM_DIALOG, false);
        bundle.putInt(EXTRA_POSITION, position);
        intent.putExtras(bundle);
        return intent;
    }

    /**
     * @param context   activity context
     * @param channelId can also be substitued by channelUrl
     * @return Intent
     */
    public static Intent getCallingIntent(Context context, String channelId) {
        Intent intent = new Intent(context, GroupChatActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_CHANNEL_UUID, channelId);
        bundle.putBoolean(EXTRA_SHOW_BOTTOM_DIALOG, true);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void onErrorGetChannelInfo(String errorMessage) {
        NetworkErrorHelper.showEmptyState(this, rootView, errorMessage, new NetworkErrorHelper
                .RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                initData();
            }
        });
        setVisibilityHeader(View.GONE);
    }

    void setVisibilityHeader(int visible) {
        toolbar.setVisibility(visible);
        channelBanner.setVisibility(visible);
    }

    @Override
    public void onSuccessGetChannelInfo(ChannelInfoViewModel channelInfoViewModel) {
        try {
            setChannelInfoView(channelInfoViewModel);
            setChannelConnectionHandler();

            if (!TextUtils.isEmpty(channelInfoViewModel.getAdsImageUrl())) {
                trackAdsEE(channelInfoViewModel);
            }
            if (getApplication() instanceof AbstractionRouter) {
                userSession = ((AbstractionRouter) getApplication()).getSession();
            }

            presenter.enterChannel(userSession.getUserId(), viewModel.getChannelInfoViewModel().getChannelUrl(),
                    userSession.getName(), userSession.getProfilePicture(), this, channelInfoViewModel.getSendBirdToken());

            Intent intent = new Intent();
            intent.putExtra(TOTAL_VIEW, channelInfoViewModel.getTotalView());
            intent.putExtra(EXTRA_POSITION, viewModel.getChannelPosition());
            setResult(Activity.RESULT_OK, intent);
            if (exitDialog == null) {
                exitDialog = createAlertDialog().create();
                exitDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            }

            initVideoFragment(channelInfoViewModel);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void trackAdsEE(ChannelInfoViewModel channelInfoViewModel) {

        ArrayList<EEPromotion> list = new ArrayList<>();
        list.add(new EEPromotion(channelInfoViewModel.getAdsId(),
                EEPromotion.NAME_GROUPCHAT,
                GroupChatAnalytics.DEFAULT_EE_POSITION,
                channelInfoViewModel.getAdsName(),
                channelInfoViewModel.getAdsImageUrl(),
                getAttributionTracking(GroupChatAnalytics.ATTRIBUTE_BANNER)
        ));

        eventViewComponentEnhancedEcommerce(GroupChatAnalytics.COMPONENT_BANNER,
                channelInfoViewModel.getAdsName(),
                GroupChatAnalytics.ATTRIBUTE_BANNER,
                list);
    }

    @Override
    public void updateVoteViewModel(VoteInfoViewModel voteInfoViewModel, String voteType) {
        if (viewModel != null
                && viewModel.getChannelInfoViewModel() != null
                && viewModel.getChannelInfoViewModel().getVoteInfoViewModel() != null) {
            if (voteInfoViewModel.getStatusId() == VoteInfoViewModel.STATUS_FINISH
                    || voteInfoViewModel.getStatusId() == VoteInfoViewModel.STATUS_FORCE_FINISH
                    || voteType.equals(VoteAnnouncementViewModel.POLLING_UPDATE)) {
                boolean isVoted = viewModel.getChannelInfoViewModel().getVoteInfoViewModel()
                        .isVoted();
                List<Visitable> tempListOption = new ArrayList<>();
                tempListOption.addAll(viewModel.getChannelInfoViewModel().getVoteInfoViewModel()
                        .getListOption());
                for (int i = 0; i < voteInfoViewModel.getListOption().size(); i++) {
                    if (voteInfoViewModel.getListOption().get(i) instanceof VoteViewModel) {
                        ((VoteViewModel) voteInfoViewModel.getListOption().get(i)).setSelected(
                                ((VoteViewModel) (tempListOption.get(i))).getSelected());
                    }
                }
                voteInfoViewModel.setVoted(isVoted);
                viewModel.getChannelInfoViewModel().setVoteInfoViewModel(voteInfoViewModel);

            } else {
                viewModel.getChannelInfoViewModel().setVoteInfoViewModel(voteInfoViewModel);
            }
        } else if (viewModel != null && viewModel.getChannelInfoViewModel() != null) {
            viewModel.getChannelInfoViewModel().setVoteInfoViewModel(voteInfoViewModel);
        }

        setGreenIndicator(voteInfoViewModel);
    }

    @Override
    public void setChannelHandler() {
        if (viewModel != null && !TextUtils.isEmpty(viewModel.getChannelUrl()))
            presenter.setHandler(viewModel.getChannelUrl(), getChannelHandlerId(), this);

    }

    @Override
    public void showInfoDialog() {
        if (canShowDialog) {
            channelInfoDialog.setContentView(
                    createBottomSheetView(
                            checkPollValid(),
                            viewModel.getChannelInfoViewModel()));

            if (getIntent() != null
                    && getIntent().getExtras() != null
                    && getIntent().getExtras().getBoolean(GroupChatActivity.EXTRA_SHOW_BOTTOM_DIALOG, false)) {
                channelInfoDialog.show();
                canShowDialog = false;
            }
        }
    }

    private void setChannelInfoView(ChannelInfoViewModel channelInfoViewModel) {
        this.viewModel.setChannelInfo(channelInfoViewModel);

        setToolbarData(channelInfoViewModel.getTitle(),
                channelInfoViewModel.getBannerUrl(),
                channelInfoViewModel.getTotalView(),
                channelInfoViewModel.getBlurredBannerUrl());
        setSponsorData();
    }

    private void setTooltip(VoteInfoViewModel voteInfoViewModel) {
        if (!currentFragmentIsVote()
                && checkPollValid()
                && (voteInfoViewModel.getStatusId() == VoteInfoViewModel.STATUS_FORCE_ACTIVE
                || voteInfoViewModel.getStatusId() == VoteInfoViewModel.STATUS_ACTIVE)) {
            tooltipHandler = new Handler();
            tooltipHandler.postDelayed(runnable, TOOLTIP_DELAY);
        }
    }

    private void showTooltip() {
        if (tabs != null
                && tabAdapter != null
                && hasVoteTab()) {
            View view = ToolTipUtils.setToolTip(this, R.layout.tooltip_vote, this);
            TextView temp = view.findViewById(R.id.text);
            MethodChecker.setBackground(temp, MethodChecker.getDrawable(this, R.drawable.ic_combined_shape));
            View anchorView = tabs.getChildAt(CHANNEL_VOTE_FRAGMENT);
            if (anchorView != null) {
                ToolTipUtils.showToolTip(view, anchorView);
            }
        }
    }

    private boolean checkPollValid() {
        if (viewModel != null
                && viewModel.getChannelInfoViewModel() != null
                && viewModel.getChannelInfoViewModel().getVoteInfoViewModel() != null) {
            VoteInfoViewModel voteInfoViewModel = viewModel.getChannelInfoViewModel()
                    .getVoteInfoViewModel();
            return voteInfoViewModel.getStartTime() != 0
                    && voteInfoViewModel.getEndTime() != 0
                    && voteInfoViewModel.getStartTime() < voteInfoViewModel.getEndTime();
        } else {
            return false;
        }
    }

    private View createBottomSheetView(boolean hasValidPoll,
                                       ChannelInfoViewModel channelInfoViewModel) {
        View view = getLayoutInflater().inflate(R.layout.channel_info_bottom_sheet_dialog, null);

        TextView actionButton = view.findViewById(R.id.action_button);
        ImageView image = view.findViewById(R.id.product_image);
        ImageView profile = view.findViewById(R.id.prof_pict);
        TextView title = view.findViewById(R.id.title);
        TextView subtitle = view.findViewById(R.id.subtitle);
        TextView name = view.findViewById(R.id.name);
        TextView participant = view.findViewById(R.id.participant);

        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                channelInfoDialog.dismiss();
                analytics.eventClickJoin();
            }
        });

        participant.setText(TextFormatter.format(String.valueOf(channelInfoViewModel.getTotalView())));
        name.setText(channelInfoViewModel.getAdminName());
        title.setText(channelInfoViewModel.getTitle());
        subtitle.setText(channelInfoViewModel.getDescription());

        ImageHandler.loadImage2(image, channelInfoViewModel.getImage(), R.drawable.loading_page);
        ImageHandler.loadImageCircle2(profile.getContext(),
                profile,
                channelInfoViewModel.getAdminPicture(),
                R.drawable.loading_page);

        return view;
    }

    private void setToolbarData(String title, String bannerUrl, String totalParticipant, String blurredBannerUrl) {
        toolbar.setTitle(title);

        if (TextUtils.isEmpty(blurredBannerUrl)) {
            ImageHandler.loadImageBlur(this, channelBanner, bannerUrl);
        } else {
            ImageHandler.LoadImage(channelBanner, blurredBannerUrl);
        }

        setToolbarParticipantCount(TextFormatter.format(totalParticipant));
        setVisibilityHeader(View.VISIBLE);

    }

    private void setToolbarParticipantCount(String totalParticipant) {
        String textParticipant = String.format("%s %s", totalParticipant, getString(R.string.view));
        toolbar.setSubtitle(textParticipant);
    }

    private void setSponsorData() {
        if (!TextUtils.isEmpty(viewModel.getChannelInfoViewModel().getAdsId())) {
            sponsorLayout.setVisibility(View.VISIBLE);
            ImageHandler.loadImage2(sponsorImage,
                    viewModel.getChannelInfoViewModel().getAdsImageUrl(),
                    R.drawable.loading_page);
            sponsorImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ArrayList<EEPromotion> list = new ArrayList<>();
                    list.add(new EEPromotion(viewModel.getChannelInfoViewModel().getAdsId(),
                            EEPromotion.NAME_GROUPCHAT,
                            GroupChatAnalytics.DEFAULT_EE_POSITION,
                            viewModel.getChannelInfoViewModel().getAdsName(),
                            viewModel.getChannelInfoViewModel().getAdsImageUrl(),
                            getAttributionTracking(GroupChatAnalytics
                                    .ATTRIBUTE_BANNER)
                    ));

                    eventClickComponentEnhancedEcommerce(
                            GroupChatAnalytics.COMPONENT_BANNER,
                            viewModel.getChannelInfoViewModel().getAdsName(),
                            GroupChatAnalytics.ATTRIBUTE_BANNER,
                            list);

                    openSponsor(generateAttributeApplink(
                            viewModel.getChannelInfoViewModel().getAdsLink(),
                            GroupChatAnalytics.ATTRIBUTE_BANNER,
                            viewModel.getChannelUrl(),
                            viewModel.getChannelName()));
                }
            });
        } else {
            sponsorLayout.setVisibility(View.GONE);
        }

        if (TextUtils.isEmpty(viewModel.getChannelInfoViewModel().getAdsImageUrl())) {
            sponsorLayout.setVisibility(View.GONE);
        }
    }

    private void openSponsor(String adsLink) {
        ((GroupChatModuleRouter) getApplicationContext()).openRedirectUrl(this, adsLink);
    }

    @Override
    protected void onResume() {
        super.onResume();

        kickIfIdleForTooLong();

        if(youTubePlayer == null && getChannelInfoViewModel() != null) {
            initVideoFragment(getChannelInfoViewModel());
        }

        if (viewModel != null && viewModel.getChannelInfoViewModel() != null
                && !isFirstTime) {
            setChannelConnectionHandler();
        }

        if (notifReceiver == null) {
            notifReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if (intent.getExtras() != null) {
                        onGetNotif(intent.getExtras());
                    }
                }
            };
        }

        try {
            LocalBroadcastManager.getInstance(this).registerReceiver(notifReceiver, new IntentFilter
                    (TkpdState.LOYALTY_GROUP_CHAT));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getScreenName() {
        if (getIntent() != null && getIntent().getExtras() != null) {
            String roomName = getIntent().getExtras().getString(EXTRA_CHANNEL_UUID, "");
            return GroupChatAnalytics.SCREEN_CHAT_ROOM + roomName;
        } else {
            return GroupChatAnalytics.SCREEN_CHAT_ROOM;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        analytics.sendScreen(this, getScreenName());
    }

    @Override
    public void onSuccessRefreshChannelInfo(ChannelInfoViewModel channelInfoViewModel) {
        setChannelInfoView(channelInfoViewModel);

        presenter.logoutChannel(mChannel);
        presenter.enterChannelAfterRefresh(userSession.getUserId(),
                viewModel.getChannelInfoViewModel().getChannelUrl(),
                userSession.getName(), userSession.getProfilePicture(),
                this, channelInfoViewModel.getSendBirdToken());


    }

    private void refreshTab() {
        tabAdapter.replace(createListFragment());
    }

    @Override
    public String getAttributionTracking(String attributeName) {
        return GroupChatAnalytics.generateTrackerAttribution(attributeName, viewModel.getChannelUrl
                (), viewModel.getChannelName());
    }

    @Override
    public void removeGroupChatPoints() {
        viewModel.getChannelInfoViewModel().setGroupChatPointsViewModel(null);
    }

    @Override
    public void onSuccessLogin() {
        isFirstTime = true;
        presenter.getChannelInfo(viewModel.getChannelUuid(), true);
        showLoading();
    }

    private void showPushNotif(GroupChatPointsViewModel model) {
        ((GroupChatFragment) getSupportFragmentManager().findFragmentByTag
                (GroupChatFragment.class.getSimpleName())).onPushNotifReceived(model);
    }

    private void refreshChat() {
        ((GroupChatFragment) getSupportFragmentManager().findFragmentByTag
                (GroupChatFragment.class.getSimpleName())).refreshChat();

    }

    private void refreshVote(VoteInfoViewModel voteInfoViewModel) {
        ((ChannelVoteFragment) getSupportFragmentManager().findFragmentByTag
                (ChannelVoteFragment.class.getSimpleName())).refreshVote(voteInfoViewModel);
    }

    private Fragment populateChannelInfoFragment() {
        Bundle bundle = new Bundle();
        if (getIntent().getExtras() != null) {
            bundle.putAll(getIntent().getExtras());
        }

        Fragment fragment = getSupportFragmentManager().findFragmentByTag(
                ChannelInfoFragment.class.getSimpleName());

        if (fragment == null) {
            fragment = ChannelInfoFragment.createInstance(bundle);
        }

        ((ChannelInfoFragmentListener.View) fragment).renderData(
                viewModel.getChannelInfoViewModel());

        return fragment;
    }

    @Override
    protected void onPause() {
        if (viewModel != null && viewModel.getChannelInfoViewModel() != null
                && !TextUtils.isEmpty(viewModel.getChannelInfoViewModel().getTitle()))
            analytics.eventUserExit(viewModel.getChannelInfoViewModel().getTitle());
        if (tooltipHandler != null && runnable != null) {
            tooltipHandler.removeCallbacks(runnable);
        }
        super.onPause();
        if (viewModel != null) {
            viewModel.setTimeStampBeforePause(System.currentTimeMillis());
        }
        ConnectionManager.removeConnectionManagementHandler(getConnectionHandlerId());
        SendBird.removeChannelHandler(getChannelHandlerId());

        if (notifReceiver != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(notifReceiver);
        }
    }

    private String getChannelHandlerId() {
        if (viewModel != null && viewModel.getChannelUuid() != null) {
            return viewModel.getChannelUuid() + ConnectionManager.CHANNEL_HANDLER_ID;
        } else {
            return ConnectionManager.CHANNEL_HANDLER_ID;
        }
    }

    private String getConnectionHandlerId() {
        if (viewModel != null && viewModel.getChannelUuid() != null) {
            return viewModel.getChannelUuid() + ConnectionManager.CONNECTION_HANDLER_ID;
        } else {
            return ConnectionManager.CONNECTION_HANDLER_ID;
        }
    }

    @Override
    protected void onDestroy() {
        if(youTubePlayer!=null){
            youTubePlayer.release();
        }

        youtubeRunnable.removeCallbacksAndMessages(null);

        super.onDestroy();
        if (tooltipHandler != null && runnable != null) {
            tooltipHandler.removeCallbacks(runnable);
        }
        presenter.detachView();
        logoutChannel(mChannel);
    }

    @Override
    public void logoutChannel(OpenChannel openChannel) {
        this.mChannel = openChannel;
        presenter.logoutChannel(mChannel);
    }

    private void kickIfIdleForTooLong() {
        if (viewModel != null) {
            if (viewModel.getTimeStampBeforePause() > 0
                    && System.currentTimeMillis() - viewModel.getTimeStampBeforePause() > KICK_TRESHOLD_TIME) {
                onUserIdleTooLong();
            }
        }
    }

    private void onUserIdleTooLong() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.you_have_been_kicked);
        if (viewModel != null
                && viewModel.getChannelInfoViewModel() != null
                && !TextUtils.isEmpty(viewModel.getChannelInfoViewModel().getBannedMessage())) {
            builder.setMessage(viewModel.getChannelInfoViewModel().getKickedMessage());
        } else {
            builder.setMessage(R.string.you_have_been_idle_for_too_long);
        }
        builder.setPositiveButton(R.string.title_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                Intent intent = new Intent();
                if (viewModel != null) {
                    intent.putExtra(TOTAL_VIEW, viewModel.getTotalView());
                    intent.putExtra(EXTRA_POSITION, viewModel.getChannelPosition());
                }
                setResult(ChannelActivity.RESULT_ERROR_ENTER_CHANNEL, intent);
                finish();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    public void onSuccessEnterChannel(OpenChannel openChannel) {
        try {
            hideLoading();
            mChannel = openChannel;

            Fragment fragment = getSupportFragmentManager().findFragmentByTag
                    (GroupChatFragment.class.getSimpleName());
            if (fragment != null) {
                ((GroupChatFragment) fragment).setChannel(mChannel);
                ((GroupChatFragment) fragment).refreshChat();
            }

            setupViewPager();
            showFragment(initialFragment);

            if (viewModel.getChannelInfoViewModel().getVoteInfoViewModel() != null) {
                setGreenIndicator(viewModel.getChannelInfoViewModel().getVoteInfoViewModel());
                setTooltip(viewModel.getChannelInfoViewModel().getVoteInfoViewModel());
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            onErrorEnterChannel(getString(R.string.default_request_error_unknown));
        } catch (NullPointerException e) {
            e.printStackTrace();
            onErrorEnterChannel(getString(R.string.default_request_error_unknown));
        }
    }

    @Override
    public void onSuccessEnterRefreshChannel(OpenChannel openChannel) {

        hideLoading();
        mChannel = openChannel;
        Fragment fragment = getSupportFragmentManager().findFragmentByTag
                (GroupChatFragment.class.getSimpleName());
        if (fragment != null) {
            ((GroupChatFragment) fragment).setChannel(mChannel);
        }

        refreshTab();
        setGreenIndicator(viewModel.getChannelInfoViewModel().getVoteInfoViewModel());
        setTooltip(viewModel.getChannelInfoViewModel().getVoteInfoViewModel());
        tabAdapter.setActiveFragment(initialFragment);

        if (currentFragmentIsChat()) {
            refreshChat();
        } else if (currentFragmentIsVote() && checkPollValid()) {
            refreshVote(viewModel.getChannelInfoViewModel().getVoteInfoViewModel());
        } else if (currentFragmentIsVote()) {
            viewModel.getChannelInfoViewModel().setVoteInfoViewModel(null);
            showFragment(CHATROOM_FRAGMENT);
        } else if (currentFragmentIsInfo()) {
            populateChannelInfoFragment();
        }
    }

    @Override
    public void initVideoFragment() {
        initVideoFragment(getChannelInfoViewModel());
    }

    private void setChannelConnectionHandler() {
        ConnectionManager.addConnectionManagementHandler(userSession.getUserId(), getConnectionHandlerId(), new
                ConnectionManager.ConnectionManagementHandler() {
                    @Override
                    public void onConnected(boolean reconnect) {
                        if (viewModel != null && viewModel.getChannelInfoViewModel() != null
                                && !isFirstTime) {
                            presenter.refreshChannelInfo(viewModel.getChannelUuid());
                        } else if (reconnect && viewModel != null) {
                            presenter.getChannelInfo(viewModel.getChannelUuid());
                        }
                    }
                });
    }

    @Override
    public void onErrorEnterChannel(String errorMessage) {
        hideLoading();
        NetworkErrorHelper.showEmptyState(this, rootView, errorMessage, new NetworkErrorHelper
                .RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                presenter.enterChannel(userSession.getUserId(), viewModel.getChannelUuid(),
                        userSession.getName(), userSession.getProfilePicture(),
                        GroupChatActivity.this, viewModel.getChannelInfoViewModel().getSendBirdToken());
            }
        });
    }

    @Override
    public void onUserBanned(String errorMessage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.default_banned_title);
        if (viewModel != null
                && viewModel.getChannelInfoViewModel() != null
                && !TextUtils.isEmpty(viewModel.getChannelInfoViewModel().getBannedMessage())) {
            builder.setMessage(viewModel.getChannelInfoViewModel().getBannedMessage());
        } else {
            builder.setMessage(errorMessage);
        }
        builder.setPositiveButton(R.string.title_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                Intent intent = new Intent();
                if (viewModel != null) {
                    intent.putExtra(TOTAL_VIEW, viewModel.getTotalView());
                    intent.putExtra(EXTRA_POSITION, viewModel.getChannelPosition());
                }
                setResult(ChannelActivity.RESULT_ERROR_ENTER_CHANNEL, intent);
                finish();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    public void onChannelNotFound(String errorMessage) {
        hideLoading();
        setVisibilityHeader(View.VISIBLE);
        setToolbarPlain();
        setChannelNotFoundView(View.VISIBLE);
        if (findViewById(R.id.tab) != null) {
            findViewById(R.id.tab).setVisibility(View.GONE);
        }
        if (findViewById(R.id.sponsor_layout) != null) {
            findViewById(R.id.sponsor_layout).setVisibility(View.GONE);
        }
        if (findViewById(R.id.shadow_layer) != null) {
            findViewById(R.id.shadow_layer).setVisibility(View.GONE);
        }
        if (findViewById(R.id.video_container_layout) != null) {
            findViewById(R.id.video_container_layout).setVisibility(View.GONE);
        }
    }

    private void setChannelNotFoundView(int visibility) {
        if (findViewById(R.id.card_retry) != null) {
            findViewById(R.id.card_retry).setVisibility(visibility);
            findViewById(R.id.card_retry).findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = ((GroupChatModuleRouter) getApplicationContext())
                            .getHomeIntent(v.getContext());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
            });
        }
    }

    private void setToolbarPlain() {
        toolbar.removeAllViews();
        setToolbarWhite();
        toolbar.setSubtitleTextColor(getResources().getColor(R.color.white));
        toolbar.setTitle(getResources().getString(R.string.label_group_chat));
        toolbar.setTitleMarginTop((int) getResources().getDimension(R.dimen.dp_16));
    }

    private void setToolbarWhite() {
        toolbar.setContentInsetStartWithNavigation(0);
        toolbar.setTitleTextColor(getResources().getColor(R.color.black_70));
        toolbar.setSubtitleTextColor(getResources().getColor(R.color.black_70));
        toolbar.getMenu().findItem(R.id.action_share).setVisible(false);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setSubtitle(null);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_webview_back_button);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setElevation(ELEVATION);
            toolbar.setBackgroundResource(R.color.white);
        } else {
            toolbar.setBackgroundResource(R.drawable.bg_white_toolbar_drop_shadow);
        }
    }

    private boolean currentlyLoadingFragment() {
        return getSupportFragmentManager().findFragmentById(R.id.container) == null;
    }

    private boolean currentFragmentIsChat() {
        return getSupportFragmentManager().findFragmentById(R.id.container) != null &&
                getSupportFragmentManager().findFragmentById(R.id.container) instanceof GroupChatFragment;
    }

    private boolean currentFragmentIsVote() {
        return getSupportFragmentManager().findFragmentById(R.id.container) != null &&
                getSupportFragmentManager().findFragmentById(R.id.container) instanceof ChannelVoteFragment;
    }

    private boolean currentFragmentIsInfo() {
        return getSupportFragmentManager().findFragmentById(R.id.container) != null &&
                getSupportFragmentManager().findFragmentById(R.id.container) instanceof ChannelInfoFragment;
    }

    @Override
    public void onMessageReceived(Visitable map) {
        if (map instanceof VoteAnnouncementViewModel) {
            VoteAnnouncementViewModel voteAnnouncementViewModel = ((VoteAnnouncementViewModel) map);
            handleVoteAnnouncement(voteAnnouncementViewModel, voteAnnouncementViewModel.getVoteType());
        } else if (map instanceof SprintSaleAnnouncementViewModel) {
            updateSprintSaleData((SprintSaleAnnouncementViewModel) map);
        } else if (map instanceof VibrateViewModel) {
            vibratePhone();
        } else if (map instanceof AdsViewModel) {
            updateAds((AdsViewModel) map);
        } else if (map instanceof GroupChatQuickReplyViewModel) {
            updateQuickReply(((GroupChatQuickReplyViewModel) map).getList());
        } else if (map instanceof PinnedMessageViewModel) {
            updatePinnedMessage((PinnedMessageViewModel) map);
        } else if (map instanceof VideoViewModel) {
            updateVideo((VideoViewModel) map);
        }

        if (currentFragmentIsChat()) {
            ((GroupChatFragment) getSupportFragmentManager().findFragmentByTag
                    (GroupChatFragment.class.getSimpleName())).onMessageReceived(map);
        }
    }

    private void updateVideo(VideoViewModel map) {
        viewModel.getChannelInfoViewModel().setVideoId(map.getVideoId());
        initVideoFragment(getChannelInfoViewModel());
    }

    private void updatePinnedMessage(PinnedMessageViewModel map) {
        viewModel.getChannelInfoViewModel().setPinnedMessageViewModel(map);
    }

    private void updateQuickReply(List<GroupChatQuickReplyItemViewModel> list) {
        viewModel.getChannelInfoViewModel().setQuickRepliesViewModel(list);
    }

    private void updateAds(AdsViewModel adsViewModel) {
        viewModel.getChannelInfoViewModel().setAdsImageUrl(adsViewModel.getAdsUrl());
        viewModel.getChannelInfoViewModel().setAdsId(adsViewModel.getAdsId());
        viewModel.getChannelInfoViewModel().setAdsLink(adsViewModel.getAdsLink());
        setSponsorData();
    }

    @Override
    public void vibratePhone() {
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator != null) {
            if (Build.VERSION.SDK_INT >= 26) {
                vibrator.vibrate(VibrationEffect.createOneShot(VIBRATE_LENGTH, VibrationEffect
                        .DEFAULT_AMPLITUDE));
            } else {
                vibrator.vibrate(VIBRATE_LENGTH);
            }
        }
    }

    @Override
    public void onMessageDeleted(long msgId) {
        if (currentFragmentIsChat()) {
            ((GroupChatFragment) getSupportFragmentManager().findFragmentByTag
                    (GroupChatFragment.class.getSimpleName())).onMessageDeleted(msgId);
        }
    }

    @Override
    public void onMessageUpdated(Visitable map) {
        if (currentFragmentIsChat()) {
            ((GroupChatFragment) getSupportFragmentManager().findFragmentByTag
                    (GroupChatFragment.class.getSimpleName())).onMessageUpdated(map);
        }
    }

    @Override
    public void onUserEntered(UserActionViewModel userActionViewModel, String participantCount) {
        if (currentFragmentIsChat()) {
            ((GroupChatFragment) getSupportFragmentManager().findFragmentByTag
                    (GroupChatFragment.class.getSimpleName())).onUserEntered(userActionViewModel
            );
        }

        try {
            if (!userActionViewModel.getUserId().equalsIgnoreCase(userSession.getUserId())) {
                viewModel.setTotalView(String.valueOf(Integer.parseInt(viewModel.getTotalView()) + 1));
            }
            setToolbarParticipantCount(TextFormatter.format(viewModel.getTotalView()));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUserExited(UserActionViewModel userActionViewModel, String participantCount) {
        if (currentFragmentIsChat()) {
            ((GroupChatFragment) getSupportFragmentManager().findFragmentByTag
                    (GroupChatFragment.class.getSimpleName())).onUserExited(userActionViewModel
            );
        }
    }

    @Override
    public void onUserBanned(User user) {
        hideLoading();
        if (user != null
                && !TextUtils.isEmpty(user.getUserId())
                && userSession.getUserId().equals(user.getUserId())) {
            onUserBanned(getString(R.string.user_is_banned));
        }
    }

    @Override
    public void onChannelDeleted() {
        onChannelNotFound(getString(R.string.channel_has_been_deleted));

    }

    @Override
    public void onChannelFrozen() {
//        onChannelNotFound(getString(R.string.channel_deactivated));
        if(viewModel.getChannelInfoViewModel() == null) {
            onChannelDeleted();
            return;
        }
        AlertDialog.Builder myAlertDialog = new android.app.AlertDialog.Builder(this);
        myAlertDialog.setTitle(getString(R.string.channel_not_found));
        myAlertDialog.setMessage(getString(R.string.channel_deactivated));
        final Context context = this;
        myAlertDialog.setPositiveButton(getString(R.string.exit_group_chat_ok), new
                DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (isTaskRoot()) {
                            startActivity(((GroupChatModuleRouter) getApplicationContext()).getInboxChannelsIntent(context));
                        }
                        finish();
                        GroupChatActivity.super.onBackPressed();
                    }
                });
        myAlertDialog.setCancelable(false);
        myAlertDialog.show();
    }

    @Override
    public void setView(View view) {

    }

    @Override
    public void setListener() {

    }

    @Override
    public void handleVoteAnnouncement(VoteAnnouncementViewModel messageItem, String voteType) {
        VoteInfoViewModel voteInfoViewModel = messageItem.getVoteInfoViewModel();
        updateVoteViewModel(messageItem.getVoteInfoViewModel(), voteType);

        if ((voteInfoViewModel.getStatusId() == VoteInfoViewModel.STATUS_ACTIVE
                || voteInfoViewModel.getStatusId() == VoteInfoViewModel.STATUS_FORCE_ACTIVE)
                && !hasVoteTab()) {
            tabAdapter.add(CHANNEL_VOTE_FRAGMENT, new TabViewModel(getString(R.string
                    .title_vote)));
            tabAdapter.notifyItemInserted(CHANNEL_VOTE_FRAGMENT);
        } else if (voteInfoViewModel.getStatusId() == VoteInfoViewModel.STATUS_CANCELED
                && hasVoteTab()) {
            viewModel.getChannelInfoViewModel().setVoteInfoViewModel(null);
            tabAdapter.remove(CHANNEL_VOTE_FRAGMENT);
            tabAdapter.notifyItemRemoved(CHANNEL_VOTE_FRAGMENT);
            showFragment(CHATROOM_FRAGMENT);
        }

        setTooltip(voteInfoViewModel);

        if (currentFragmentIsVote()
                && voteInfoViewModel.getStatusId() != VoteInfoViewModel.STATUS_CANCELED) {
            ((ChannelVoteFragment) getSupportFragmentManager().findFragmentByTag
                    (ChannelVoteFragment.class.getSimpleName())).showVoteLayout(viewModel
                    .getChannelInfoViewModel().getVoteInfoViewModel());
        }


    }

    private void setGreenIndicator(VoteInfoViewModel voteInfoViewModel) {
        if (tabAdapter != null && voteInfoViewModel != null) {
            if ((voteInfoViewModel.getStatusId() == VoteInfoViewModel.STATUS_ACTIVE
                    || voteInfoViewModel.getStatusId() == VoteInfoViewModel.STATUS_FORCE_ACTIVE)
                    && !voteInfoViewModel.isVoted()
                    && hasVoteTab()) {
                tabAdapter.change(CHANNEL_VOTE_FRAGMENT, true);
            } else {
                tabAdapter.change(CHANNEL_VOTE_FRAGMENT, false);
            }
        }
    }

    public void moveToVoteFragment() {
        if (hasVoteTab()) {
            showFragment(CHANNEL_VOTE_FRAGMENT);
        }
    }

    private boolean hasVoteTab() {
        return tabAdapter.getItemCount() > 2
                && tabAdapter.getItemAt(CHANNEL_VOTE_FRAGMENT).getTitle().equals(getString(R.string
                .title_vote));
    }

    @Override
    public SprintSaleViewModel getSprintSaleViewModel() {
        if (viewModel != null && viewModel.getChannelInfoViewModel() != null && viewModel
                .getChannelInfoViewModel().getSprintSaleViewModel() != null) {
            return viewModel.getChannelInfoViewModel().getSprintSaleViewModel();
        } else {
            return null;
        }
    }

    public ExitMessage getExitMessage() {
        if (viewModel != null && viewModel.getChannelInfoViewModel() != null && viewModel
                .getChannelInfoViewModel().getExitMessage() != null) {
            return viewModel.getChannelInfoViewModel().getExitMessage();
        } else {
            return null;
        }
    }

    @Override
    public PinnedMessageViewModel getPinnedMessage() {
        if (viewModel != null && viewModel.getChannelInfoViewModel() != null && viewModel
                .getChannelInfoViewModel().getPinnedMessageViewModel() != null) {
            return viewModel.getChannelInfoViewModel().getPinnedMessageViewModel();
        } else {
            return null;
        }
    }

    @Override
    public ChannelInfoViewModel getChannelInfoViewModel() {
        if (viewModel != null && viewModel.getChannelInfoViewModel() != null) {
            return viewModel.getChannelInfoViewModel();
        } else {
            return null;
        }
    }

    @Override
    public void eventClickComponentEnhancedEcommerce(String componentType, String campaignName,
                                                     String attributeName, List<EEPromotion>
                                                             listEE) {
        if (viewModel != null && viewModel.getChannelInfoViewModel() != null) {
            analytics.eventClickComponentEnhancedEcommerce(componentType, campaignName,
                    attributeName, viewModel.getChannelUrl(), viewModel.getChannelName(), listEE);
        }
    }

    @Override
    public void eventViewComponentEnhancedEcommerce(String componentType, String campaignName,
                                                    String attributeName, List<EEPromotion>
                                                            listEE) {
        if (viewModel != null && viewModel.getChannelInfoViewModel() != null) {
            analytics.eventViewComponentEnhancedEcommerce(componentType, campaignName,
                    attributeName, viewModel.getChannelUrl(), viewModel.getChannelName(), listEE);
        }
    }

    @Override
    public void updateSprintSaleData(SprintSaleAnnouncementViewModel messageItem) {
        if (this.viewModel != null
                && this.viewModel.getChannelInfoViewModel() != null
                && this.viewModel.getChannelInfoViewModel().getSprintSaleViewModel() != null) {
            this.viewModel.getChannelInfoViewModel().getSprintSaleViewModel()
                    .setCampaignId(messageItem.getCampaignId());
            this.viewModel.getChannelInfoViewModel().getSprintSaleViewModel()
                    .setListProduct(messageItem.getListProducts());
            this.viewModel.getChannelInfoViewModel().getSprintSaleViewModel()
                    .setCampaignName(messageItem.getCampaignName());
            this.viewModel.getChannelInfoViewModel().getSprintSaleViewModel()
                    .setStartDate(messageItem.getStartDate());
            this.viewModel.getChannelInfoViewModel().getSprintSaleViewModel()
                    .setEndDate(messageItem.getEndDate());
            this.viewModel.getChannelInfoViewModel().getSprintSaleViewModel()
                    .setRedirectUrl(messageItem.getRedirectUrl());
            this.viewModel.getChannelInfoViewModel().getSprintSaleViewModel()
                    .setSprintSaleType(messageItem.getSprintSaleType());
        } else if (this.viewModel != null
                && this.viewModel.getChannelInfoViewModel() != null) {
            this.viewModel.getChannelInfoViewModel().setSprintSaleViewModel(new SprintSaleViewModel(
                    messageItem.getCampaignId(),
                    messageItem.getListProducts(),
                    messageItem.getCampaignName(),
                    messageItem.getStartDate(),
                    messageItem.getEndDate(),
                    messageItem.getRedirectUrl(),
                    messageItem.getSprintSaleType()));
        }
    }

    @Override
    public String generateAttributeApplink(String applink,
                                           String attributeBanner) {
        try {
            if (viewModel != null && isAppLink(applink)) {
                return generateAttributeApplink(applink, attributeBanner,
                        viewModel.getChannelUrl(),
                        viewModel.getChannelName());
            } else {
                return applink;
            }
        } catch (UnknownFormatConversionException e) {
            e.printStackTrace();
            return applink;
        }
    }

    private boolean isAppLink(String applink) {
        return applink.trim().toLowerCase().startsWith(TOKOPEDIA_APPLINK);
    }

    private String generateAttributeApplink(String applink,
                                            String attributeBanner,
                                            String channelUrl,
                                            String channelName) {
        if (applink.contains("?")) {
            return String.format(applink + "&%s", generateTrackerAttribution(attributeBanner,
                    channelUrl, channelName));
        } else {
            return String.format(applink + "?%s", generateTrackerAttribution(attributeBanner,
                    channelUrl, channelName));
        }
    }

    private String generateTrackerAttribution(String attributeBanner,
                                              String channelUrl,
                                              String channelName) {
        return "tracker_attribution=" + GroupChatAnalytics.generateTrackerAttribution
                (attributeBanner, channelUrl, channelName);
    }

    private void onGetNotif(Bundle data) {
        GroupChatPointsViewModel model = new GroupChatPointsViewModel(
                data.getString("desc", ""),
                data.getString("applinks", ""),
                data.getString("tkp_code", "")
        );

        if (currentFragmentIsChat()) {
            showPushNotif(model);
        } else {
            viewModel.getChannelInfoViewModel().setGroupChatPointsViewModel(model);
        }
    }
}
