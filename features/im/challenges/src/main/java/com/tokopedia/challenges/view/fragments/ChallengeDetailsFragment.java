package com.tokopedia.challenges.view.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.network.exception.MessageErrorException;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.challenges.R;
import com.tokopedia.challenges.di.ChallengesComponent;
import com.tokopedia.challenges.view.adapter.ChallengeMainDetailsAdapter;
import com.tokopedia.challenges.view.adapter.SubmissionItemAdapter;
import com.tokopedia.challenges.view.adapter.util.StickHeaderItemDecoration;
import com.tokopedia.challenges.view.adapter.viewHolder.SubmissionViewHolder;
import com.tokopedia.challenges.view.analytics.ChallengesGaAnalyticsTracker;
import com.tokopedia.challenges.view.contractor.ChallengeDetailsContract;
import com.tokopedia.challenges.view.customview.CountDownView;
import com.tokopedia.challenges.view.model.Result;
import com.tokopedia.challenges.view.model.challengesubmission.SubmissionResponse;
import com.tokopedia.challenges.view.model.challengesubmission.SubmissionResult;
import com.tokopedia.challenges.view.presenter.ChallengeDetailsPresenter;
import com.tokopedia.challenges.view.share.ShareBottomSheet;
import com.tokopedia.challenges.view.utils.Utils;
import com.tokopedia.common.network.util.NetworkClient;
import com.tokopedia.design.base.BaseToaster;
import com.tokopedia.design.component.ToasterError;
import com.tokopedia.usecase.RequestParams;

import java.util.List;

import javax.inject.Inject;

/**
 * @author lalit.singh
 */
public class ChallengeDetailsFragment extends BaseDaggerFragment implements ChallengeDetailsContract.View,
        StickHeaderItemDecoration.OnShowMainHeader, TabLayout.OnTabSelectedListener, View.OnClickListener,
        SubmissionItemAdapter.INavigateToActivityRequest, SubmissionViewHolder.SubmissionViewHolderListener {


    @Inject
    ChallengeDetailsPresenter mPresenter;

    private String challengeId;
    private Boolean isPastChallenge;

    private Result challengeResult;
    private boolean isWinnerList;

    /*Views*/
    View mainContentView;
    private ImageView challengeImage;
    private TextView challengeTitle;
    private TextView tvHashTag;
    private TextView tvParticipated;
    private TextView challengeDueDate;
    private ProgressBar timerProgressBar;
    private CountDownView countDownView;
    private ConstraintLayout timerView;
    private RecyclerView mainRecyclerView;
    private LinearLayout headerContainer;
    private AppBarLayout appBarLayout;
    private TextView mostRecent;
    private TextView buzzPoints;

    TabLayout tabLayout;


    ChallengeMainDetailsAdapter mainDetailsAdapter;
    private TextView submitPhoto;

    Handler handler = new Handler();
    private boolean isFromAdapter;
    private FloatingActionButton btnShare;
    private View bottomMarginView;
    private View submitButton;

    public static Fragment createInstance(Bundle extras) {
        Fragment fragment = new ChallengeDetailsFragment();
        fragment.setArguments(extras);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!TextUtils.isEmpty(getArguments().getString(Utils.QUERY_PARAM_CHALLENGE_ID))) {
            this.challengeId = getArguments().getString(Utils.QUERY_PARAM_CHALLENGE_ID);
        } else {
            this.challengeResult = getArguments().getParcelable(Utils.QUERY_PARAM_SUBMISSION_RESULT);
        }
        this.isPastChallenge = getArguments().getBoolean(Utils.QUERY_PARAM_IS_PAST_CHALLENGE);
        isWinnerList = getArguments().getBoolean(Utils.QUERY_PARAM_IS_PAST_CHALLENGE, false);
        setHasOptionsMenu(true);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.challenges_details_fragment, container, false);
        mPresenter.attachView(this);
        mainContentView = view.findViewById(R.id.main_content);
        challengeImage = view.findViewById(R.id.image_challenge);
        challengeTitle = view.findViewById(R.id.tv_title);
        tvHashTag = view.findViewById(R.id.tv_hashtag);
        challengeDueDate = view.findViewById(R.id.tv_expiry_date);
        tvParticipated = view.findViewById(R.id.tv_participated);
        timerView = view.findViewById(R.id.cl_timer);
        timerProgressBar = view.findViewById(R.id.progressBar);
        countDownView = view.findViewById(R.id.count_down);
        mainRecyclerView = view.findViewById(R.id.recyclerView_challengeDetailsMain);
        headerContainer = view.findViewById(R.id.headerContainer);
        submitPhoto = view.findViewById(R.id.submit_photo);

        tabLayout = view.findViewById(R.id.tab_challenges);
        tabLayout.addOnTabSelectedListener(this);
        appBarLayout = view.findViewById(R.id.app_bar);
        mostRecent = view.findViewById(R.id.tv_most_recent);
        buzzPoints = view.findViewById(R.id.tv_buzz_points);
        btnShare = view.findViewById(R.id.fab_share);
        bottomMarginView = view.findViewById(R.id.bottom_margin_view);
        submitButton = view.findViewById(R.id.ll_continue);
        mostRecent.setOnClickListener(this);
        buzzPoints.setOnClickListener(this);

        if (isPastChallenge) {
            btnShare.setVisibility(View.GONE);
            submitButton.setVisibility(View.GONE);
            bottomMarginView.setVisibility(View.GONE);
        } else {
            btnShare.setOnClickListener(this);
            submitButton.setOnClickListener(this);
            btnShare.setVisibility(View.VISIBLE);
            submitButton.setVisibility(View.VISIBLE);
            bottomMarginView.setVisibility(View.VISIBLE);
        }

        if (!TextUtils.isEmpty(challengeId) || challengeResult == null) {
            mPresenter.initialize(true, challengeResult);
        } else {
            challengeId = challengeResult.getId();
            mPresenter.initialize(false, challengeResult);
        }

        appBarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            if (Math.abs(verticalOffset) - appBarLayout.getTotalScrollRange() > -(appBarLayout.getTotalScrollRange() / 2)) {
                btnShare.hide();
            } else {
                btnShare.show();
            }
        });

        return view;
    }

    @Override
    protected void initInjector() {
        NetworkClient.init(getActivity());
        getComponent(ChallengesComponent.class).inject(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mainDetailsAdapter != null) {
            mainDetailsAdapter.onFragmentResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mainDetailsAdapter != null) {
            mainDetailsAdapter.onFragmentPause();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        //if (ChallengesCacheHandler.CHALLENGES_DETAILS_CACHE) {
        //}
    }

    @Override
    protected String getScreenName() {
        return ChallengeDetailsFragment.class.getName();
    }

    @Override
    public void showProgressBar() {

    }

    @Override
    public void hideProgressBar() {

    }

    @Override
    public void renderChallengeDetail(Result challengeResult) {
        ImageHandler.loadImage(getActivity(), challengeImage, Utils.getImageUrl(challengeResult.getThumbnailUrl()), R.color.grey_1100, R.color.grey_1100);
        if (!TextUtils.isEmpty(challengeResult.getTitle())) {
            challengeTitle.setText(challengeResult.getTitle());
        } else {
            challengeTitle.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(challengeResult.getEndDate())) {
            if (isPastChallenge) {
                challengeDueDate.setText(String.format(getResources().getString(R.string.ch_text_due_date),
                        Utils.convertUTCToString(challengeResult.getEndDate())));
            } else {
                challengeDueDate.setText(String.format(getResources().getString(R.string.ch_text_ended_date),
                        Utils.convertUTCToString(challengeResult.getEndDate())));
            }

        }
        if (!TextUtils.isEmpty(challengeResult.getHashTag()))
            tvHashTag.setText(challengeResult.getHashTag());
        else {
            tvHashTag.setVisibility(View.GONE);
        }

        addChallengeDetailToAdapter();
    }

    @Override
    public void renderCountDownView(String participatedText) {
        if (TextUtils.isEmpty(participatedText)) {
            submitPhoto.setText(R.string.ch_submit_video_photo_text);
            try {
                countDownView.setStartDuration(Utils.convertUTCToMillis(challengeResult.getEndDate()));
                countDownView.start(timerProgressBar);
            } catch (MessageErrorException e) {
                e.printStackTrace();
                countDownView.stop();
            }
            timerView.setVisibility(View.VISIBLE);
            tvParticipated.setVisibility(View.GONE);
        } else {
            Utils.setTextViewBackground(getContext(), tvParticipated, participatedText);
            submitPhoto.setText(R.string.ch_view_post_text);
        }
    }

    @Override
    public void renderWinnerItems(SubmissionResponse submissionResponse) {
        if (null != mainDetailsAdapter) {
            mainDetailsAdapter.setWinnerList(submissionResponse.getSubmissionResults(), isPastChallenge);
        }
    }

    private void addChallengeDetailToAdapter() {
        if (null == mainDetailsAdapter) {
            mainDetailsAdapter = new ChallengeMainDetailsAdapter(getActivity(), challengeResult, mPresenter, isPastChallenge, this, this);
            mainRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            mainRecyclerView.setAdapter(mainDetailsAdapter);
            mainRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    mainDetailsAdapter.onViewScrolled();
                }
            });

            mainRecyclerView.addItemDecoration(new StickHeaderItemDecoration(mainDetailsAdapter, this));
        }
    }

    @Override
    public RequestParams getChallengeDetailsParams() {
        RequestParams requestParams = RequestParams.create();
        if (TextUtils.isEmpty(challengeId) && challengeResult != null)
            challengeId = challengeResult.getId();
        requestParams.putString(Utils.QUERY_PARAM_CHALLENGE_ID, challengeId);
        return requestParams;
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
    public View getRootView() {
        return mainContentView;
    }

    @Override
    public void setIsPastChallenge(boolean value) {
        isPastChallenge = value;
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
    public void renderSubmissions(List<SubmissionResult> submissionResults) {
        if (mainDetailsAdapter != null) {
            mainDetailsAdapter.setSubmissionList(submissionResults);
        }
    }

    @Override
    public void showSubmissionListLoader() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mainDetailsAdapter != null) {
                    mainDetailsAdapter.showLoader();
                }
            }
        }, 210L);

    }

    @Override
    public void hideSubmissionListLoader() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mainDetailsAdapter != null) {
                    mainDetailsAdapter.hideLoader();
                }
            }
        }, 210L);

    }

    @Override
    public Result getChallengeResult() {
        return challengeResult;
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
        setSnackBarErrorMessage(message, v -> {
        });
    }

    @Override
    public void onLike(SubmissionResult result) {
        Toast.makeText(getActivity(), "Liked", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNavigateToActivityRequest(Intent intent, int requestCode, int position) {
        navigateToActivityRequest(intent, requestCode);
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
    public void onDestroyView() {
        mPresenter.onDestroy();
        if (countDownView != null) {
            countDownView.stop();
        }
        super.onDestroyView();
    }

    @Override
    public void onShowHeader(boolean show) {
        isFromAdapter = true;
        if (show) {
            tabLayout.getTabAt(1).select();
            headerContainer.setVisibility(View.VISIBLE);
        } else {
            tabLayout.getTabAt(0).select();
            headerContainer.setVisibility(View.INVISIBLE);
        }
        isFromAdapter = false;
    }

    private void scrollToList() {
        appBarLayout.setExpanded(false);
        View topChild = mainRecyclerView.getChildAt(0);
        mainRecyclerView.smoothScrollBy(0, topChild.getHeight() - mainRecyclerView.computeVerticalScrollOffset());
    }

    private void scrollToTop() {
        mainRecyclerView.scrollToPosition(0);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        if (!isFromAdapter)
            if (tab.getPosition() == 0) {
                scrollToTop();
            } else {
                scrollToList();
            }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    public void sortByRecent() {
        handler.postDelayed(() -> {
            mainRecyclerView.stopScroll();
            mostRecent.setBackgroundResource(R.drawable.bg_ch_bubble_selected);
            buzzPoints.setBackgroundResource(R.drawable.bg_ch_bubble_default);
            mPresenter.setPageStart(0);
            mPresenter.setSortType(Utils.QUERY_PARAM_KEY_SORT_RECENT);
            mainDetailsAdapter.clearList();
            mainDetailsAdapter.setSortingType(Utils.QUERY_PARAM_KEY_SORT_RECENT);
            mPresenter.unsubscribe();
            mPresenter.loadMoreItems();
        }, 100L);

    }

    public void sortByBuzzPoints() {
        handler.postDelayed(() -> {
            mainRecyclerView.stopScroll();
            buzzPoints.setBackgroundResource(R.drawable.bg_ch_bubble_selected);
            mostRecent.setBackgroundResource(R.drawable.bg_ch_bubble_default);
            mPresenter.setPageStart(0);
            mPresenter.setSortType(Utils.QUERY_PARAM_KEY_SORT_POINTS);
            mainDetailsAdapter.clearList();
            mainDetailsAdapter.setSortingType(Utils.QUERY_PARAM_KEY_SORT_POINTS);
            mPresenter.unsubscribe();
            mPresenter.loadMoreItems();
        }, 100L);

    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == R.id.tv_buzz_points) {
            sortByBuzzPoints();
        } else if (viewId == R.id.tv_most_recent) {
            sortByRecent();
        } else if (viewId == R.id.ll_continue) {
            mPresenter.onSubmitButtonClick();
        } else if (viewId == R.id.tv_tnc) {
            /*fragmentCallbacks.replaceFragment(tncText, getString(R.string.ch_terms_conditions));
            analytics.sendEventChallenges(ChallengesGaAnalyticsTracker.EVENT_CLICK_CHALLENGES,
                    ChallengesGaAnalyticsTracker.EVENT_CATEGORY_ACTIVE_CHALLENGES,
                    ChallengesGaAnalyticsTracker.EVENT_ACTION_CLICK, ChallengesGaAnalyticsTracker.EVENT_TNC);*/
        } else if (viewId == R.id.fab_share) {
            ShareBottomSheet.show((getActivity()).getSupportFragmentManager(), challengeResult, false);
        }

        /*else if (v.getId() == R.id.tv_see_all) {
            analytics.sendEventChallenges(ChallengesGaAnalyticsTracker.EVENT_CLICK_CHALLENGES,
                    ChallengesGaAnalyticsTracker.EVENT_CATEGORY_OTHER_SUBMISSION_SEE_ALL,
                    ChallengesGaAnalyticsTracker.EVENT_ACTION_CLICK, "");
            Intent intent = AllSubmissionsActivity.newInstance(getActivity());
            intent.putExtra(Utils.QUERY_PARAM_IS_PAST_CHALLENGE, isPastChallenge);
            intent.putExtra(Utils.QUERY_PARAM_CHALLENGE_ID, challengeId);
            startActivity(intent);
        } else if (v.getId() == R.id.ll_continue) {
            mPresenter.onSubmitButtonClick();
        } else if (v.getId() == R.id.seemorebutton_buzzpoints) {
            fragmentCallbacks.replaceFragment(buzzPointText, getString(R.string.ch_generate_buzz_points));
        } else if (v.getId() == R.id.tv_tnc) {
            fragmentCallbacks.replaceFragment(tncText, getString(R.string.ch_terms_conditions));
            analytics.sendEventChallenges(ChallengesGaAnalyticsTracker.EVENT_CLICK_CHALLENGES,
                    ChallengesGaAnalyticsTracker.EVENT_CATEGORY_ACTIVE_CHALLENGES,
                    ChallengesGaAnalyticsTracker.EVENT_ACTION_CLICK, ChallengesGaAnalyticsTracker.EVENT_TNC);
        } else if (v.getId() == R.id.fab_share) {
            ShareBottomSheet.show((getActivity()).getSupportFragmentManager(), challengeResult, false);
        }*/
    }
}
