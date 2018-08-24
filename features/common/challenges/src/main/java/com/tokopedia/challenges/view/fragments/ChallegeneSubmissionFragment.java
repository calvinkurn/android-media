package com.tokopedia.challenges.view.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.network.exception.MessageErrorException;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.challenges.ChallengesAnalytics;
import com.tokopedia.challenges.ChallengesModuleRouter;
import com.tokopedia.challenges.R;
import com.tokopedia.challenges.data.source.ChallengesUrl;
import com.tokopedia.challenges.di.ChallengesComponent;
import com.tokopedia.challenges.view.activity.ChallengeDetailActivity;
import com.tokopedia.challenges.view.adapter.AwardAdapter;
import com.tokopedia.challenges.view.adapter.SubmissionItemAdapter;
import com.tokopedia.challenges.view.contractor.ChallengeSubmissonContractor;
import com.tokopedia.challenges.view.customview.CountDownView;
import com.tokopedia.challenges.view.customview.CustomVideoPlayer;
import com.tokopedia.challenges.view.model.Result;
import com.tokopedia.challenges.view.model.TermsNCondition;
import com.tokopedia.challenges.view.model.challengesubmission.SubmissionResponse;
import com.tokopedia.challenges.view.model.challengesubmission.SubmissionResult;
import com.tokopedia.challenges.view.presenter.ChallengeSubmissionPresenter;
import com.tokopedia.challenges.view.share.ShareBottomSheet;
import com.tokopedia.challenges.view.utils.ChallengesFragmentCallbacks;
import com.tokopedia.challenges.view.utils.MarkdownProcessor;
import com.tokopedia.challenges.view.utils.Utils;
import com.tokopedia.common.network.util.NetworkClient;
import com.tokopedia.design.base.BaseToaster;
import com.tokopedia.design.component.ToasterError;
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
    TextView shortDescription;
    View submitButton;
    RecyclerView submissionRecyclerView, awardRecylerView, winnerRecyclerView;
    List<SubmissionResult> submissionResults, winnerResults;
    CustomVideoPlayer videoPlayer;
    ConstraintLayout timerView;
    RelativeLayout rlExpiry;
    ConstraintLayout clAbout;
    ConstraintLayout clVideoPlayer;
    ConstraintLayout clAwards;
    ConstraintLayout clSubmissions;
    ConstraintLayout clWinners;
    ConstraintLayout clHowBuzzPoints;
    ConstraintLayout clTnc;
    ProgressBar timerProgressBar;
    CountDownView countDownView;
    TextView tvHashTag;
    TextView tvTnCText;
    WebView longDescription;
    private NestedScrollView nestedScrollView;
    public static int VIDEO_POS = -1;

    FrameLayout progressBar;
    FrameLayout flHeader;
    SubmissionItemAdapter submissionItemAdapter, winnerItemAdapter;
    CoordinatorLayout mainContent;
    ConstraintLayout baseMainContent;
    private Result challengeResult;
    private AwardAdapter awardAdapter;
    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private AppBarLayout appBarLayout;
    private ChallengesFragmentCallbacks fragmentCallbacks;
    private String tncText;
    private String buzzPointText;
    private TextView tvHowBuzzPointsText;
    private String challengeId;
    private Boolean isPastChallenge;
    private FloatingActionButton btnShare;
    private boolean isWinnerList = false;
    private View bottomMarginView;
    @Inject
    public ChallengesAnalytics analytics;

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
        this.isPastChallenge = getArguments().getBoolean(Utils.QUERY_PARAM_IS_PAST_CHALLENGE);

        isWinnerList = getArguments().getBoolean("isPastChallenge", false);
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
        shortDescription = view.findViewById(R.id.short_description);
        seeMoreButtonDesc = view.findViewById(R.id.seemorebutton_description);
        submissionRecyclerView = view.findViewById(R.id.rv_submissions);
        winnerRecyclerView = view.findViewById(R.id.rv_winners);
        submitButton = view.findViewById(R.id.ll_continue);
        awardRecylerView = view.findViewById(R.id.rv_awards);
        rlExpiry = view.findViewById(R.id.cl_expiry);
        clAbout = view.findViewById(R.id.cl_about);
        clVideoPlayer = view.findViewById(R.id.cl_video_player);
        clAwards = view.findViewById(R.id.cl_awards);
        clSubmissions = view.findViewById(R.id.cl_submissions);
        clWinners = view.findViewById(R.id.cl_winners);
        clHowBuzzPoints = view.findViewById(R.id.cl_how_buzzpoints);
        clTnc = view.findViewById(R.id.cl_tnc);
        toolbar = view.findViewById(R.id.toolbar);
        ((BaseSimpleActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(ContextCompat.getDrawable(getActivity(), R.drawable.ic_action_back));
        collapsingToolbarLayout = view.findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
        collapsingToolbarLayout.setTitle(" ");
        appBarLayout = view.findViewById(R.id.app_bar);
        seeMoreButtonDesc.setOnClickListener(this);
        view.findViewById(R.id.tv_see_all).setOnClickListener(this);
        videoPlayer = view.findViewById(R.id.video_player);
        tvTnCText = view.findViewById(R.id.tv_tnc_text);
        tvHowBuzzPointsText = view.findViewById(R.id.tv_how_buzz_points_text);
        seeMoreButtonBuzzPoints = view.findViewById(R.id.seemorebutton_buzzpoints);
        seeMoreButtonTnc = view.findViewById(R.id.seemorebutton_tnc);
        progressBar = view.findViewById(R.id.progress_bar_layout);
        flHeader = view.findViewById(R.id.fl_header);
        mainContent = view.findViewById(R.id.main_content);
        btnShare = view.findViewById(R.id.fab_share);
        baseMainContent = view.findViewById(R.id.base_main_content);
        seeMoreButtonBuzzPoints.setOnClickListener(this);
        seeMoreButtonTnc.setOnClickListener(this);
        longDescription = view.findViewById(R.id.markdownView);
        nestedScrollView = view.findViewById(R.id.nested_scroll_view);
        bottomMarginView = view.findViewById(R.id.bottom_margin_view);
        mPresenter.attachView(this);
        if (isPastChallenge) {
            btnShare.setVisibility(View.GONE);
            submitButton.setVisibility(View.GONE);
            bottomMarginView.setVisibility(View.GONE);
        } else {
            btnShare.setOnClickListener(this);
            submitButton.setOnClickListener(this);
            btnShare.setVisibility(View.VISIBLE);
            submitButton.setVisibility(View.VISIBLE);
        }
        if (!TextUtils.isEmpty(challengeId) || challengeResult == null) {
            mPresenter.initialize(true, challengeResult);
        } else {
            challengeId = challengeResult.getId();
            mPresenter.initialize(false, challengeResult);
        }

        mPresenter.loadCountdownView(challengeResult, isWinnerList);

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
            submissionItemAdapter = new SubmissionItemAdapter(submissionResponse.getSubmissionResults(), this, LinearLayoutManager.HORIZONTAL, isWinnerList);
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
        if (!TextUtils.isEmpty(challengeResult.getEndDate())) {
            challengeDueDate.setText(String.format(getResources().getString(R.string.text_due_date),
                    Utils.convertUTCToString(challengeResult.getEndDate())));
        }
        if (!TextUtils.isEmpty(challengeResult.getHashTag()))
            tvHashTag.setText(challengeResult.getHashTag());
        else {
            tvHashTag.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(challengeResult.getDescription())) {
            shortDescription.setText(challengeResult.getDescription());
            clAbout.setVisibility(View.VISIBLE);
        }

        if (challengeResult.getSharing().getAssets() != null && !TextUtils.isEmpty(challengeResult.getSharing().getAssets().getVideo())) {
            videoPlayer.setVideoThumbNail(challengeResult.getSharing().getAssets().getImage(), challengeResult.getSharing().getAssets().getVideo(), false, null);
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
        if (!isPastChallenge) {
            submitButton.setVisibility(View.VISIBLE);

        }
        baseMainContent.setVisibility(View.VISIBLE);
        showBuzzPointsText();
    }

    @Override
    public void setCountDownView(String participatedText) {
        if (TextUtils.isEmpty(participatedText)) {
            try {
                countDownView.setStartDuration(Utils.convertUTCToMillis(challengeResult.getEndDate()));
                countDownView.start(timerProgressBar);
            } catch (MessageErrorException e) {
                e.printStackTrace();
            }
            timerView.setVisibility(View.VISIBLE);
            tvParticipated.setVisibility(View.GONE);
        } else {
            Utils.setTextViewBackground(getContext(), tvParticipated, participatedText);
        }
    }

    public void setTextStatus(int textColor, Drawable bgColor, TextView statusText) {
        statusText.setTextColor(textColor);
        statusText.setBackground(bgColor);
    }

    @Override
    public void renderWinnerItems(SubmissionResponse submissionResponse) {

        winnerResults = submissionResponse.getSubmissionResults();
        if (winnerResults != null && winnerResults.size() > 0) {
            clWinners.setVisibility(View.VISIBLE);
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
            winnerRecyclerView.setLayoutManager(mLayoutManager);
            winnerItemAdapter = new SubmissionItemAdapter(submissionResponse.getSubmissionResults(), this, LinearLayoutManager.HORIZONTAL, isWinnerList);
            winnerItemAdapter.isWinnerLayout(true);
            winnerRecyclerView.setAdapter(winnerItemAdapter);
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

    public void showBuzzPointsText() {
        buzzPointText = ((ChallengesModuleRouter) getActivity().getApplication()).getStringRemoteConfig("app_text_how_to_generate_buzz_point");
        if (!TextUtils.isEmpty(buzzPointText)) {
            clHowBuzzPoints.setVisibility(View.VISIBLE);
            tvHowBuzzPointsText.setText(Html.fromHtml(buzzPointText));
        }
    }

    @Override
    public void renderTnC(TermsNCondition termsNCondition) {
        tncText = termsNCondition.getTerms();
        if (!TextUtils.isEmpty(tncText)) {
            clTnc.setVisibility(View.VISIBLE);
            tvTnCText.setText(tncText);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.seemorebutton_description) {
            if (shortDescription.getVisibility() == View.VISIBLE) {
                seeMoreButtonDesc.setText(R.string.see_less);
                MarkdownProcessor m = new MarkdownProcessor();
                String html = m.markdown(challengeResult.getDescription());
                longDescription.loadDataWithBaseURL("fake://", html, "text/html", "UTF-8", null);
                shortDescription.setVisibility(View.GONE);
                longDescription.setVisibility(View.VISIBLE);
            } else {
                seeMoreButtonDesc.setText(R.string.see_more);
                shortDescription.setVisibility(View.VISIBLE);
                longDescription.setVisibility(View.GONE);
                nestedScrollView.scrollTo(0, 0);
            }
        } else if (v.getId() == R.id.tv_see_all) {
            analytics.sendEventChallenges(ChallengesAnalytics.EVENT_CLICK_CHALLENGES,
                    ChallengesAnalytics.EVENT_CATEGORY_OTHER_SUBMISSION_SEE_ALL,
                    ChallengesAnalytics.EVENT_ACTION_CLICK, "");
            fragmentCallbacks.replaceFragment(submissionResults, challengeResult.getId());
        } else if (v.getId() == R.id.ll_continue) {
            mPresenter.onSubmitButtonClick();
        } else if (v.getId() == R.id.seemorebutton_buzzpoints) {
            fragmentCallbacks.replaceFragment(buzzPointText, getString(R.string.generate_buzz_points));
        } else if (v.getId() == R.id.seemorebutton_tnc) {
            analytics.sendEventChallenges(ChallengesAnalytics.EVENT_CLICK_CHALLENGES,
                    ChallengesAnalytics.EVENT_CATEGORY_ACTIVE_CHALLENGES,
                    ChallengesAnalytics.EVENT_ACTION_CLICK, ChallengesAnalytics.EVENT_TNC);
            fragmentCallbacks.replaceFragment(tncText, getString(R.string.terms_conditions));
        } else if (v.getId() == R.id.fab_share) {
            ShareBottomSheet.show(getActivity().getSupportFragmentManager(), Utils.getApplinkPathForBranch(ChallengesUrl.AppLink.CHALLENGES_DETAILS, challengeResult.getId()), challengeResult.getTitle(), challengeResult.getSharing().getMetaTags().getOgUrl(), challengeResult.getSharing().getMetaTags().getOgTitle(), challengeResult.getSharing().getMetaTags().getOgImage(), challengeResult.getId(), Utils.getApplinkPathForBranch(ChallengesUrl.AppLink.CHALLENGES_DETAILS, challengeResult.getId()), true);
        }

    }

    @Override
    public void onNavigateToActivityRequest(Intent intent, int requestCode, int position) {
        navigateToActivityRequest(intent, requestCode);
    }

    @Override
    public void finish() {
        getActivity().finish();
    }

    @Override
    public void setSnackBarErrorMessage(String message, View.OnClickListener listener) {
        View rootView = getActivity().getWindow().getDecorView().findViewById(android.R.id.content);

        ToasterError
                .make(rootView,
                        message,
                        BaseToaster.LENGTH_LONG)
                .setAction(getResources().getString(R.string.title_ok),
                        listener)
                .show();
    }

    @Override
    public void setSnackBarErrorMessage(String message) {
        setSnackBarErrorMessage(message, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    @Override
    public Result getChallengeResult() {
        return challengeResult;
    }

    @Override
    public void setChallengeResult(Result challengeResult) {
        this.challengeResult = challengeResult;
    }

    @Override
    public String getChallengeId() {
        return challengeId;
    }

    @Override
    public void onDestroyView() {
        mPresenter.onDestroy();
        super.onDestroyView();
    }
}
