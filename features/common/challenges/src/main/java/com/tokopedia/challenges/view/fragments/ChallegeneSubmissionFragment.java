package com.tokopedia.challenges.view.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.network.exception.MessageErrorException;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.challenges.ChallengesModuleRouter;
import com.tokopedia.challenges.R;
import com.tokopedia.challenges.data.source.ChallengesUrl;
import com.tokopedia.challenges.di.ChallengesComponent;
import com.tokopedia.challenges.view.activity.ChallengesSubmitActivity;
import com.tokopedia.challenges.view.activity.ChallengeDetailActivity;
import com.tokopedia.challenges.view.adapter.AwardAdapter;
import com.tokopedia.challenges.view.adapter.SubmissionItemAdapter;
import com.tokopedia.challenges.view.contractor.ChallengeSubmissonContractor;
import com.tokopedia.challenges.view.customview.CountDownView;
import com.tokopedia.challenges.view.customview.CustomVideoPlayer;
import com.tokopedia.challenges.view.customview.ExpandableTextView;
import com.tokopedia.challenges.view.model.Result;
import com.tokopedia.challenges.view.model.TermsNCondition;
import com.tokopedia.challenges.view.model.challengesubmission.SubmissionResponse;
import com.tokopedia.challenges.view.model.challengesubmission.SubmissionResult;
import com.tokopedia.challenges.view.presenter.ChallengeSubmissionPresenter;
import com.tokopedia.challenges.view.utils.ChallengesFragmentCallbacks;
import com.tokopedia.challenges.view.utils.Utils;
import com.tokopedia.common.network.util.NetworkClient;
import com.tokopedia.usecase.RequestParams;

import java.util.List;

import javax.inject.Inject;

public class ChallegeneSubmissionFragment extends BaseDaggerFragment implements ChallengeSubmissonContractor.View, View.OnClickListener, SubmissionItemAdapter.INavigateToActivityRequest {

    @Inject
    ChallengeSubmissionPresenter mPresenter;
    ImageView challengeImage;
    TextView challengeTitle;
    TextView challengeDueDate;
    TextView tvParticipated;
    TextView seeMoreButtonDesc;
    TextView seeMoreButtonBuzzPoints;
    TextView seeMoreButtonTnc;
    ExpandableTextView description;
    View submitButton;
    RecyclerView submissionRecyclerView, awardRecylerView;
    List<SubmissionResult> submissionResults;
    CustomVideoPlayer videoPlayer;
    ConstraintLayout timerView;
    RelativeLayout rlExpiry;
    ConstraintLayout clAbout;
    ConstraintLayout clVideoPlayer;
    ConstraintLayout clAwards;
    ConstraintLayout clSubmissions;
    ConstraintLayout clHowBuzzPoints;
    ConstraintLayout clTnc;
    ProgressBar timerProgressBar;
    CountDownView countDownView;
    TextView tvHashTag;
    TextView tvTnCText;
    public static int VIDEO_POS = -1;

    FrameLayout progressBar;
    FrameLayout flHeader;
    SubmissionItemAdapter submissionItemAdapter;
    CoordinatorLayout mainContent;
    ConstraintLayout baseMainContent;
    private Result challengeResult;
    private AwardAdapter awardAdapter;
    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private AppBarLayout appBarLayout;
    private ChallengesFragmentCallbacks fragmentCallbacks;
    private String tncText;
    private TextView tvHowBuzzPointsText;
    private String challengeId;
    private FloatingActionButton btnShare;

    public static Fragment createInstance(Bundle extras) {
        Fragment fragment = new ChallegeneSubmissionFragment();
        fragment.setArguments(extras);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!TextUtils.isEmpty(getArguments().getString(Utils.QUERY_PARAM_CHALLENGE_ID))) {
            this.challengeId = getArguments().getString(Utils.QUERY_PARAM_CHALLENGE_ID);
        } else {
            this.challengeResult = getArguments().getParcelable("challengesResult");
        }
        setHasOptionsMenu(true);
    }


    @Override
    public void onStart() {
        super.onStart();
        if (VIDEO_POS != -1) {
            if (videoPlayer != null)
                videoPlayer.startPlay(VIDEO_POS);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (videoPlayer != null)
            VIDEO_POS = videoPlayer.getPosition();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.challenges_detail_fragment, container, false);
        challengeImage = view.findViewById(R.id.image_challenge);
        challengeTitle = view.findViewById(R.id.tv_title);
        tvHashTag = view.findViewById(R.id.tv_hashtag);
        challengeDueDate = view.findViewById(R.id.tv_expiry_date);
        tvParticipated = view.findViewById(R.id.tv_participated);
        timerView = view.findViewById(R.id.cl_timer);
        timerProgressBar = view.findViewById(R.id.progressBar);
        countDownView = view.findViewById(R.id.count_down);
        description = view.findViewById(R.id.tv_expandable_description);
        seeMoreButtonDesc = view.findViewById(R.id.seemorebutton_description);
        submissionRecyclerView = view.findViewById(R.id.rv_submissions);
        submitButton = view.findViewById(R.id.ll_continue);
        awardRecylerView = view.findViewById(R.id.rv_awards);
        rlExpiry = view.findViewById(R.id.cl_expiry);
        clAbout = view.findViewById(R.id.cl_about);
        clVideoPlayer = view.findViewById(R.id.cl_video_player);
        clAwards = view.findViewById(R.id.cl_awards);
        clSubmissions = view.findViewById(R.id.cl_submissions);
        clHowBuzzPoints = view.findViewById(R.id.cl_how_buzzpoints);
        clTnc = view.findViewById(R.id.cl_tnc);
        toolbar = view.findViewById(R.id.toolbar);
        ((BaseSimpleActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(ContextCompat.getDrawable(getActivity(), R.drawable.ic_action_back));
        collapsingToolbarLayout = view.findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
        collapsingToolbarLayout.setTitle(" ");
        appBarLayout = view.findViewById(R.id.app_bar);
        description.setInterpolator(new OvershootInterpolator());
        seeMoreButtonDesc.setOnClickListener(this);
        view.findViewById(R.id.tv_see_all).setOnClickListener(this);
        submitButton.setOnClickListener(this);
        videoPlayer = view.findViewById(R.id.video_player);
        tvTnCText = view.findViewById(R.id.tv_tnc_text);
        tvHowBuzzPointsText = view.findViewById(R.id.tv_how_buzz_points_text);
        seeMoreButtonBuzzPoints = view.findViewById(R.id.seemorebutton_buzzpoints);
        seeMoreButtonTnc = view.findViewById(R.id.seemorebutton_tnc);
        progressBar = view.findViewById(R.id.progress_bar_layout);
        flHeader = view.findViewById(R.id.fl_header);
        mainContent=view.findViewById(R.id.main_content);
        btnShare = view.findViewById(R.id.fab_share);
        btnShare.setOnClickListener(this);
        baseMainContent=view.findViewById(R.id.base_main_content);
        seeMoreButtonBuzzPoints.setOnClickListener(this);
        seeMoreButtonTnc.setOnClickListener(this);
        mPresenter.attachView(this);
        if (!TextUtils.isEmpty(challengeId)) {
            mPresenter.initialize(true, challengeResult);
        } else {
            mPresenter.initialize(false, challengeResult);
        }
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        fragmentCallbacks = (ChallengeDetailActivity) activity;
    }

    @Override
    protected void initInjector() {
        NetworkClient.init(getActivity());
        getComponent(ChallengesComponent.class).inject(this);
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        appBarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {

            verticalOffset = Math.abs(verticalOffset);
            int difference = appBarLayout.getTotalScrollRange() - toolbar.getHeight();
            if (verticalOffset >= difference) {
                if (challengeTitle.getText() != null) {
                    collapsingToolbarLayout.setTitle(challengeTitle.getText());
                }
                setDrawableColorFilter(toolbar.getNavigationIcon(), ContextCompat.getColor(getActivity(), R.color.tkpd_dark_gray_toolbar));
            } else {
                collapsingToolbarLayout.setTitle(" ");
                setDrawableColorFilter(toolbar.getNavigationIcon(), ContextCompat.getColor(getActivity(), R.color.white));
            }
        });
    }

    public void setDrawableColorFilter(Drawable drawable, int color) {
        if (drawable != null) {
            drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        }
    }

    @Override
    public void renderSubmissionItems(SubmissionResponse submissionResponse) {

        submissionResults = submissionResponse.getSubmissionResults();
        if (submissionResults != null && submissionResults.size() > 0) {
            clSubmissions.setVisibility(View.VISIBLE);
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
            submissionRecyclerView.setLayoutManager(mLayoutManager);
            submissionItemAdapter = new SubmissionItemAdapter(submissionResponse.getSubmissionResults(), this, LinearLayoutManager.HORIZONTAL);
            submissionRecyclerView.setAdapter(submissionItemAdapter);
        }
    }



    @Override
    public void renderChallengeDetail(Result challengeResult) {
        ImageHandler.loadImage(getActivity(), challengeImage, challengeResult.getThumbnailUrl(), R.color.grey_1100, R.color.grey_1100);
        if (!TextUtils.isEmpty(challengeResult.getTitle())) {
            challengeTitle.setText(challengeResult.getTitle());
        } else {
            challengeTitle.setVisibility(View.GONE);
        }
        challengeDueDate.setText(String.format(getResources().getString(R.string.text_due_date),
                Utils.convertUTCToString(challengeResult.getEndDate())));
        if (!TextUtils.isEmpty(challengeResult.getHashTag()))
            tvHashTag.setText(challengeResult.getHashTag());
        else {
            tvHashTag.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(challengeResult.getDescription())) {
            description.setText(challengeResult.getDescription());
        } else {
            description.setVisibility(View.GONE);
        }
        if (challengeResult.getSharing().getAssets() != null && !TextUtils.isEmpty(challengeResult.getSharing().getAssets().getVideo())) {
            videoPlayer.setVideoThumbNail(challengeResult.getSharing().getAssets().getImage(), challengeResult.getSharing().getAssets().getVideo(), false);
        } else {
            clVideoPlayer.setVisibility(View.GONE);
        }
        if (challengeResult.getPrizes() != null && challengeResult.getPrizes().size() > 0) {
            LinearLayoutManager mLayoutManager1 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
            awardRecylerView.setLayoutManager(mLayoutManager1);
            awardAdapter = new AwardAdapter(challengeResult.getPrizes());
            awardRecylerView.setAdapter(awardAdapter);
        } else {
            clAwards.setVisibility(View.GONE);
        }
        submitButton.setVisibility(View.VISIBLE);
        baseMainContent.setVisibility(View.VISIBLE);
    }

    @Override
    public void setCountDownView(String participatedText) {
        if (participatedText.equals("")) {
            try {
                countDownView.setStartDuration(Utils.convertUTCToMillis(challengeResult.getEndDate()));
                countDownView.start(timerProgressBar);
            } catch (MessageErrorException e) {
                e.printStackTrace();
            }
            timerView.setVisibility(View.VISIBLE);
            tvParticipated.setVisibility(View.GONE);
        } else {
            tvParticipated.setText(participatedText);
        }
    }

    @Override
    public RequestParams getSubmissionsParams() {
        RequestParams requestParams = RequestParams.create();
        if (!TextUtils.isEmpty(challengeId))
            requestParams.putString(Utils.QUERY_PARAM_CHALLENGE_ID, challengeId);
        else
            requestParams.putString(Utils.QUERY_PARAM_CHALLENGE_ID, challengeResult.getId());
        requestParams.putInt(Utils.QUERY_PARAM_KEY_START, 0);
        requestParams.putInt(Utils.QUERY_PARAM_KEY_SIZE, 10);
        requestParams.putString(Utils.QUERY_PARAM_KEY_SORT, Utils.QUERY_PARAM_KEY_SORT_RECENT);
        return requestParams;
    }

    @Override
    public RequestParams getChallengeDetailsParams() {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(Utils.QUERY_PARAM_CHALLENGE_ID, challengeId);
        return requestParams;
    }

    @Override
    public View getRootView() {
        return mainContent;
    }

    @Override
    public void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void hideCollapsingHeader() {
        flHeader.setVisibility(View.GONE);
    }

    @Override
    public void showCollapsingHeader() {
        flHeader.setVisibility(View.VISIBLE);
    }

    @Override
    public LinearLayoutManager getLayoutManager() {
        return null;
    }

    @Override
    public void navigateToActivityRequest(Intent intent, int requestCode) {
        startActivityForResult(intent, requestCode);
    }

    @Override
    public void navigateToActivity(Intent intent) {
        startActivity(intent);
    }

    @Override
    public void renderTnC(TermsNCondition termsNCondition) {
        tncText = termsNCondition.getTerms();
        if (!TextUtils.isEmpty(tncText)) {
            clTnc.setVisibility(View.VISIBLE);
//            tvTnCText.setText(termsNCondition.getTerms());
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (tvTnCText.getLineCount() >= 10) {
                    seeMoreButtonTnc.setVisibility(View.VISIBLE);
                } else {
                    seeMoreButtonTnc.setVisibility(View.GONE);
                }
            }
        }, 100);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.seemorebutton_description) {
            if (description.isExpanded()) {
                seeMoreButtonDesc.setText(R.string.expand);
            } else {
                seeMoreButtonDesc.setText(R.string.collapse);
            }
            description.toggle();
        } else if (v.getId() == R.id.tv_see_all) {
            fragmentCallbacks.replaceFragment(submissionResults, challengeResult.getId());
        } else if (v.getId() == R.id.ll_continue) {
            startActivity(ChallengesSubmitActivity.getStartingIntent(getContext(), challengeResult));
        } else if (v.getId() == R.id.seemorebutton_buzzpoints) {
            fragmentCallbacks.replaceFragment(tncText, "How Do you Generate Buzz Points?");
        } else if (v.getId() == R.id.seemorebutton_tnc) {
            fragmentCallbacks.replaceFragment(tncText, "Terms & Conditions");
        }else if(v.getId() == R.id.fab_share){
            ((ChallengesModuleRouter) (getActivity().getApplication())).shareChallenge(getActivity(),ChallengesUrl.AppLink.CHALLENGES_DETAILS,challengeResult.getTitle(),challengeResult.getThumbnailUrl(),
                    challengeResult.getSharing().getMetaTags().getOgUrl(), challengeResult.getSharing().getMetaTags().getOgTitle(),
                    challengeResult.getSharing().getMetaTags().getOgImage());
        }

    }

    @Override
    public void onNavigateToActivityRequest(Intent intent, int requestCode, int position) {
        navigateToActivityRequest(intent, requestCode);
    }

    @Override
    public void onDestroyView() {
        mPresenter.onDestroy();
        super.onDestroyView();
    }
}
