package com.tokopedia.groupchat.chatroom.view.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.common.utils.snackbar.SnackbarManager;
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.groupchat.GroupChatModuleRouter;
import com.tokopedia.groupchat.R;
import com.tokopedia.groupchat.channel.view.ProgressBarWithTimer;
import com.tokopedia.groupchat.chatroom.di.DaggerChatroomComponent;
import com.tokopedia.groupchat.chatroom.view.activity.GroupChatActivity;
import com.tokopedia.groupchat.chatroom.view.listener.ChannelVoteContract;
import com.tokopedia.groupchat.chatroom.view.listener.GroupChatContract;
import com.tokopedia.groupchat.chatroom.view.presenter.ChannelVotePresenter;
import com.tokopedia.groupchat.common.analytics.GroupChatAnalytics;
import com.tokopedia.groupchat.common.design.CloseableBottomSheetDialog;
import com.tokopedia.groupchat.common.design.GridVoteItemDecoration;
import com.tokopedia.groupchat.common.design.SpaceItemDecoration;
import com.tokopedia.groupchat.common.di.component.DaggerGroupChatComponent;
import com.tokopedia.groupchat.common.di.component.GroupChatComponent;
import com.tokopedia.groupchat.vote.view.adapter.VoteAdapter;
import com.tokopedia.groupchat.vote.view.adapter.typefactory.VoteTypeFactory;
import com.tokopedia.groupchat.vote.view.adapter.typefactory.VoteTypeFactoryImpl;
import com.tokopedia.groupchat.vote.view.model.VoteInfoViewModel;
import com.tokopedia.groupchat.vote.view.model.VoteViewModel;
import com.tokopedia.vote.di.VoteModule;
import com.tokopedia.vote.domain.model.VoteStatisticDomainModel;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import javax.inject.Inject;

import static com.tokopedia.groupchat.chatroom.view.activity.GroupChatActivity.VOTE;

/**
 * @author by StevenFredian on 20/03/18.
 */

public class ChannelVoteFragment extends BaseDaggerFragment implements ChannelVoteContract.View
        , ProgressBarWithTimer.Listener, ChannelVoteContract.View.VoteOptionListener {

    private RecyclerView voteRecyclerView;

    @Inject
    ChannelVotePresenter presenter;

    @Inject
    GroupChatAnalytics analytics;

    private static final int REQUEST_LOGIN = 111;

    private View rootView;
    private View loading;
    private View voteBar;
    private View voteBody;
    private TextView voteTitle;
    private TextView voteInfoLink;
    private ImageView iconVote;
    private View votedView;
    private TextView voteStatus;
    private CloseableBottomSheetDialog channelInfoDialog;

    private VoteInfoViewModel voteInfoViewModel;
    private VoteAdapter voteAdapter;
    private ProgressBarWithTimer progressBarWithTimer;
    private UserSessionInterface userSession;
    private Snackbar snackBar;
    private boolean canVote = true;

    @Override
    protected String getScreenName() {
        return null;
    }

    public static Fragment createInstance(Bundle bundle) {
        Fragment fragment = new ChannelVoteFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initInjector() {
        GroupChatComponent streamComponent = DaggerGroupChatComponent.builder()
                .voteModule(new VoteModule())
                .baseAppComponent(((BaseMainApplication) getActivity().getApplication()).getBaseAppComponent())
                .build();

        DaggerChatroomComponent.builder()
                .groupChatComponent(streamComponent)
                .build().inject(this);

        presenter.attachView(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userSession = new UserSession(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_channel_vote, container, false);

        channelInfoDialog = CloseableBottomSheetDialog.createInstance(getActivity());
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

        loading = rootView.findViewById(R.id.loading);
        voteRecyclerView = rootView.findViewById(R.id.vote_list);
        voteBar = rootView.findViewById(R.id.vote_header);
        voteBody = rootView.findViewById(R.id.vote_body);
        voteTitle = rootView.findViewById(R.id.vote_title);
        voteInfoLink = rootView.findViewById(R.id.vote_info_link);
        iconVote = rootView.findViewById(R.id.icon_vote);
        voteStatus = rootView.findViewById(R.id.vote_status);
        votedView = rootView.findViewById(R.id.layout_voted);
        progressBarWithTimer = rootView.findViewById(R.id.timer);
        progressBarWithTimer.setListener(this);

        prepareView();
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressBarWithTimer.setListener(this);
        voteRecyclerView.setNestedScrollingEnabled(false);
        KeyboardHandler.DropKeyboard(getContext(), getView());
        Parcelable temp = getArguments().getParcelable(VOTE);
        showVoteLayout((VoteInfoViewModel) temp);
        if (getActivity() instanceof GroupChatContract.View) {
            ((GroupChatContract.View) getActivity()).showInfoDialog();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (voteInfoViewModel != null
                && voteInfoViewModel.getStartTime() != 0
                && voteInfoViewModel.getEndTime() != 0
                && voteInfoViewModel.getStartTime() < voteInfoViewModel.getEndTime()
                && voteInfoViewModel.getEndTime()
                > System.currentTimeMillis() / 1000L
                ) {
            progressBarWithTimer.restart();
        }

        checkDateTime();
    }

    private void checkDateTime() {
        if (MethodChecker.isTimezoneNotAutomatic(getActivity()) && snackBar == null) {
            snackBar = SnackbarManager.make(getActivity(), getString(R.string.check_timezone),
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.action_check, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (getActivity() != null && isAdded()) {
                                startActivity(new Intent(android.provider.Settings.ACTION_DATE_SETTINGS));
                            }
                        }
                    });
        }

        if (MethodChecker.isTimezoneNotAutomatic(getActivity())) {
            snackBar.show();
        }
    }

    @Override
    public void onPause() {
        progressBarWithTimer.cancel();
        if (snackBar != null) {
            snackBar.dismiss();
        }
        super.onPause();
    }

    @Override
    public void onDestroy() {
        presenter.detachView();
        progressBarWithTimer.cancel();
        super.onDestroy();
    }

    private void prepareView() {
        VoteTypeFactory voteTypeFactory = new VoteTypeFactoryImpl(this);
        voteAdapter = VoteAdapter.createInstance(voteTypeFactory);
    }

    @Override
    public void showVoteLayout(final VoteInfoViewModel model) {
        this.voteInfoViewModel = model;

        if (rootView == null || model == null) {
            return;
        }

        setVoteAdapter();

        loading.setVisibility(View.GONE);

        voteBody.setVisibility(View.VISIBLE);
        voteBar.setVisibility(View.VISIBLE);

        if (voteInfoViewModel.isVoted()) {
            setVoted(true);
        } else {
            setVoted(false);
        }

        voteInfoLink.setText(voteInfoViewModel.getVoteInfoStringResId());
        voteInfoLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(voteInfoViewModel.getVoteInfoUrl())) {
                    ((GroupChatModuleRouter) getActivity().getApplicationContext()).openRedirectUrl
                            (getActivity(), voteInfoViewModel.getVoteInfoUrl());
                    if(getActivity() instanceof GroupChatActivity) {
                        analytics.eventActionClickVoteInfo
                                (String.format("%s - %s"
                                        , ((GroupChatActivity) getActivity()).getChannelInfoViewModel().getChannelId()
                                        , voteInfoViewModel.getVoteInfoUrl()));
                    }
                }

            }
        });

        if (this.voteInfoViewModel.getStatusId() == VoteInfoViewModel.STATUS_FINISH
                || this.voteInfoViewModel.getStatusId() == VoteInfoViewModel.STATUS_FORCE_FINISH) {
            progressBarWithTimer.setVisibility(View.GONE);
            progressBarWithTimer.cancel();
            setVoteHasEnded();
        } else {
            progressBarWithTimer.setVisibility(View.VISIBLE);
            progressBarWithTimer.cancel();
            progressBarWithTimer.setTimer(voteInfoViewModel.getStartTime(), voteInfoViewModel.getEndTime());
        }

    }

    public void setVoteHasEnded() {
        if (getActivity() != null) {
            progressBarWithTimer.setVisibility(View.GONE);
            voteStatus.setText(R.string.vote_has_ended);
            voteStatus.setTextColor(MethodChecker.getColor(getActivity(), R.color.black_54));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                ImageHandler.loadImageWithIdWithoutPlaceholder(iconVote, R.drawable.ic_timer_inactive);
            } else {
                iconVote.setImageResource(R.drawable.ic_timer_inactive);
            }
            voteAdapter.updateStatistic();
            if (voteInfoViewModel != null) {
                voteInfoViewModel.setStatusId(VoteInfoViewModel.STATUS_FINISH);

                if (getActivity() instanceof GroupChatContract.View) {
                    ((GroupChatContract.View) getActivity()).updateVoteViewModel(
                            voteInfoViewModel, "");
                }
            }
        }
    }

    public void setVoteStarted() {
        if (getActivity() != null) {
            progressBarWithTimer.setVisibility(View.VISIBLE);
            voteStatus.setText(R.string.time_remaining);
            voteStatus.setTextColor(MethodChecker.getColor(getActivity(), R.color.medium_green));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                ImageHandler.loadImageWithIdWithoutPlaceholder(iconVote, R.drawable.ic_timer);
            } else {
                iconVote.setImageResource(R.drawable.ic_timer);
            }

        }
    }

    /**
     * @param voted commented per request from UI team.
     */
    public void setVoted(boolean voted) {
//        if(voted) {
//            votedView.setVisibility(View.VISIBLE);
//        }else {
//            votedView.setVisibility(View.GONE);
//        }
    }

    @Override
    public void onVoteOptionClicked(VoteViewModel element) {
        if (canVote && (voteInfoViewModel.getStatusId() == VoteInfoViewModel.STATUS_ACTIVE
                || voteInfoViewModel.getStatusId() == VoteInfoViewModel.STATUS_FORCE_ACTIVE)) {
            canVote = false;
            boolean voted = voteInfoViewModel.isVoted();
            loading.setVisibility(View.VISIBLE);
            getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            presenter.sendVote(userSession, voteInfoViewModel.getPollId(), voted, element
                        , ((GroupChatActivity) getActivity()).getChannelInfoViewModel().getGroupChatToken());

            if (getActivity() != null
                    && getActivity() instanceof GroupChatContract.View
                    && ((GroupChatContract.View) getActivity()).getChannelInfoViewModel() != null
                    && !voted) {
                analytics.eventClickVote(
                        element.getType(),
                        ((GroupChatContract.View) getActivity()).
                                getChannelInfoViewModel().getTitle());
            }
        }
    }

    @Override
    public void showHasVoted() {
        canVote = true;
        View view = getLayoutInflater().inflate(R.layout.has_voted_bottom_sheet_dialog, null);
        TextView title = view.findViewById(R.id.title);
        title.setText(R.string.has_voted);
        channelInfoDialog.setContentView(view);
        channelInfoDialog.show();
        loading.setVisibility(View.GONE);
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    @Override
    public void showSuccessVoted() {
        View view = getLayoutInflater().inflate(R.layout.has_voted_bottom_sheet_dialog, null);
        channelInfoDialog.setContentView(view);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                channelInfoDialog.show();
            }
        }, 500);
        loading.setVisibility(View.GONE);
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    @Override
    public void onSuccessVote(VoteViewModel element, VoteStatisticDomainModel voteStatisticViewModel) {
        canVote = true;
        if (voteInfoViewModel != null) {
            loading.setVisibility(View.GONE);
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            voteAdapter.change(voteInfoViewModel, element, voteStatisticViewModel);
            voteInfoViewModel.setVoted(true);
            voteInfoViewModel.setParticipant(
                    String.valueOf(Integer.parseInt(voteStatisticViewModel.getTotalParticipants())));
            setVoted(true);

            if (getActivity() instanceof GroupChatContract.View) {
                ((GroupChatContract.View) getActivity()).updateVoteViewModel(
                        voteInfoViewModel, "");
            }
        }
    }


    @Override
    public void onErrorVote(String errorMessage) {
        canVote = true;
        loading.setVisibility(View.GONE);
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
    }

    @Override
    public void redirectToLogin() {
        canVote = true;
        startActivityForResult(((GroupChatModuleRouter) getActivity().getApplicationContext())
                .getLoginIntent(getActivity()), REQUEST_LOGIN);
    }

    @Override
    public void onStartTick() {
        setVoteStarted();
    }

    @Override
    public void onFinishTick() {
        setVoteHasEnded();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_LOGIN) {
            userSession = new UserSession(getActivity());
            if (getActivity() instanceof GroupChatActivity) {
                ((GroupChatActivity) getActivity()).onSuccessLogin();
                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }
        }
    }

    private void setVoteAdapter() {
        LinearLayoutManager voteLayoutManager;
        RecyclerView.ItemDecoration itemDecoration = null;

        if (voteRecyclerView != null && voteRecyclerView.getAdapter() != null) {
            int loop = voteRecyclerView.getItemDecorationCount();
            while (voteRecyclerView.getItemDecorationCount() > 0) {
                voteRecyclerView.removeItemDecorationAt(loop-1);
                loop--;
            }
        }

        if (voteInfoViewModel.getVoteOptionType().equals(VoteViewModel.IMAGE_TYPE)) {
            voteLayoutManager = new GridLayoutManager(getActivity(), 2);
            itemDecoration = new GridVoteItemDecoration((int) getActivity().getResources().getDimension(R.dimen.space_mini), (int) getActivity().getResources().getDimension(R.dimen.dp_16), 2, voteInfoViewModel.getListOption().size());
        } else {
            voteLayoutManager = new LinearLayoutManager(getActivity());
            itemDecoration = new SpaceItemDecoration((int) getActivity().getResources().getDimension(R.dimen.space_between), false);
        }
        voteRecyclerView.addItemDecoration(itemDecoration);
        voteRecyclerView.setLayoutManager(voteLayoutManager);
        voteRecyclerView.setAdapter(voteAdapter);
        voteTitle.setText(voteInfoViewModel.getQuestion());

        voteAdapter.addList(voteInfoViewModel.getListOption());
    }

    public void refreshVote(VoteInfoViewModel voteInfoViewModel) {
        showVoteLayout(voteInfoViewModel);
    }
}
