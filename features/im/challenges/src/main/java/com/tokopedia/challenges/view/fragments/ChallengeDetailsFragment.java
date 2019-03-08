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
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.CollapsingToolbarLayout;
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
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.network.exception.MessageErrorException;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.challenges.ChallengesModuleRouter;
import com.tokopedia.challenges.R;
import com.tokopedia.challenges.di.ChallengesComponent;
import com.tokopedia.challenges.view.activity.ChallengeDetailsActivity;
import com.tokopedia.challenges.view.adapter.ChallengeMainDetailsAdapter;
import com.tokopedia.challenges.view.adapter.SubmissionItemAdapter;
import com.tokopedia.challenges.view.adapter.viewHolder.SubmissionViewHolder;
import com.tokopedia.challenges.view.analytics.ChallengesGaAnalyticsTracker;
import com.tokopedia.challenges.view.contractor.ChallengeDetailsContract;
import com.tokopedia.challenges.view.customview.CountDownView;
import com.tokopedia.challenges.view.customview.FloatingTextButton;
import com.tokopedia.challenges.view.model.Result;
import com.tokopedia.challenges.view.model.TermsNCondition;
import com.tokopedia.challenges.view.model.challengesubmission.SubmissionResponse;
import com.tokopedia.challenges.view.model.challengesubmission.SubmissionResult;
import com.tokopedia.challenges.view.presenter.ChallengeDetailsPresenter;
import com.tokopedia.challenges.view.share.ShareBottomSheet;
import com.tokopedia.challenges.view.utils.MarkdownProcessor;
import com.tokopedia.challenges.view.utils.Utils;
import com.tokopedia.common.network.util.NetworkClient;
import com.tokopedia.design.base.BaseToaster;
import com.tokopedia.design.bottomsheet.CloseableBottomSheetDialog;
import com.tokopedia.design.component.ToasterError;
import com.tokopedia.usecase.RequestParams;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author lalit.singh
 */
public class ChallengeDetailsFragment extends BaseDaggerFragment implements ChallengeDetailsContract.View,
        View.OnClickListener, SubmissionItemAdapter.INavigateToActivityRequest,
        SubmissionViewHolder.SubmissionViewHolderListener, ChallengeMainDetailsAdapter.OnChallengeDetailClickListener {


    @Inject
    ChallengeDetailsPresenter mPresenter;

    @Inject
    ChallengesGaAnalyticsTracker analytics;

    private String challengeId;
    private Boolean isPastChallenge;
    SortBy sortBy = SortBy.Recent;

    private Result challengeResult;


    public static int VIDEO_POS = -1;
    public static boolean isVideoPlaying = false;

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


    private String tncText;


    ChallengeMainDetailsAdapter mainDetailsAdapter;
    private TextView submitPhoto;

    Handler handler = new Handler();
    private View bottomButtons;
    private CloseableBottomSheetDialog tncCloseableDialog;
    private String tncHtml, challengeDescriptionHtml;
    private String buzzPointText;
    private CloseableBottomSheetDialog challengeDesciptionDialog;
    private AppBarLayout appBarLayout;
    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private FloatingTextButton sortFloatingTextButton;
    private boolean isSubmissionLoaded;
    private View progressView;


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
        setHasOptionsMenu(true);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.challenges_details_fragment, container, false);
        mPresenter.attachView(this);
        appBarLayout = view.findViewById(R.id.app_bar);

        toolbar = view.findViewById(R.id.toolbar);
        ((BaseSimpleActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(ContextCompat.getDrawable(getActivity(), R.drawable.ic_action_back));

        collapsingToolbarLayout = view.findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
        collapsingToolbarLayout.setTitle(" ");

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
        submitPhoto = view.findViewById(R.id.submit_photo);
        sortFloatingTextButton = view.findViewById(R.id.sort_action_button);
        progressView = view.findViewById(R.id.progress_bar_layout);

        bottomButtons = view.findViewById(R.id.ll_continue);
        view.findViewById(R.id.iv_sort).setOnClickListener(this);
        view.findViewById(R.id.iv_share).setOnClickListener(this);
        if (isPastChallenge) {
            bottomButtons.setVisibility(View.GONE);
            sortFloatingTextButton.setVisibility(View.INVISIBLE);
            sortFloatingTextButton.setOnClickListener(this);
        } else {
            submitPhoto.setOnClickListener(this);
        }

        if (!TextUtils.isEmpty(challengeId) || challengeResult == null) {
            mPresenter.initialize(true, challengeResult);
        } else {
            challengeId = challengeResult.getId();
            mPresenter.initialize(false, challengeResult);
            analytics.sendEventChallenges(ChallengesGaAnalyticsTracker.EVENT_VIEW_CHALLENGES,
                    ChallengesGaAnalyticsTracker.EVENT_CATEGORY_CHALLENGES_DETAIL_PAGE_CHALLENEGE,
                    ChallengesGaAnalyticsTracker.EVENT_ACTION_PAGE_VIEW,
                    challengeResult.getTitle());
        }
        return view;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }


    @Override
    protected void initInjector() {
        NetworkClient.init(getActivity());
        getComponent(ChallengesComponent.class).inject(this);
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
                setDrawableColorFilter(toolbar.getNavigationIcon(), ContextCompat.getColor(getActivity(),
                        R.color.tkpd_dark_gray_toolbar));
            } else {
                collapsingToolbarLayout.setTitle(" ");
                setDrawableColorFilter(toolbar.getNavigationIcon(), ContextCompat.getColor(getActivity(),
                        R.color.white));
            }
        });
    }

    public void setDrawableColorFilter(Drawable drawable, int color) {
        if (drawable != null) {
            drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        }
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
    }

    @Override
    protected String getScreenName() {
        return ChallengeDetailsFragment.class.getName();
    }

    @Override
    public void showProgressBar() {
        progressView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        progressView.setVisibility(View.GONE);
    }

    @Override
    public void renderChallengeDetail(Result challengeResult) {
        if (this.challengeResult == null && challengeResult != null) {
            analytics.sendEventChallenges(ChallengesGaAnalyticsTracker.EVENT_VIEW_CHALLENGES,
                    ChallengesGaAnalyticsTracker.EVENT_CATEGORY_CHALLENGES_DETAIL_PAGE_CHALLENEGE,
                    ChallengesGaAnalyticsTracker.EVENT_ACTION_PAGE_VIEW,
                    challengeResult.getTitle());
        }

        this.challengeResult = challengeResult;
        ImageHandler.loadImage(getActivity(), challengeImage, Utils.getImageUrl(challengeResult.getThumbnailUrl()),
                R.color.grey_1100, R.color.grey_1100);
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
        if (challengeDescriptionHtml == null)
            createChallengeDescriptionText(challengeResult.getDescription());
        addChallengeDetailToAdapter();
    }

    @Override
    public void renderTnC(TermsNCondition termsNCondition) {
        tncText = termsNCondition.getTerms();
        if (tncHtml == null)
            createTermsConditions(tncText);
        if (null != mainDetailsAdapter) {
            mainDetailsAdapter.setTnc(termsNCondition.getTerms());
        }

        tncCloseableDialog = CloseableBottomSheetDialog.createInstance(getActivity());
        tncCloseableDialog.setOnShowListener(dialog -> {
            BottomSheetDialog d = (BottomSheetDialog) dialog;
            FrameLayout bottomSheet = d.findViewById(android.support.design.R.id.design_bottom_sheet);
            if (bottomSheet != null) {
                BottomSheetBehavior.from(bottomSheet)
                        .setState(BottomSheetBehavior.STATE_EXPANDED);
            }
            BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
            behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                @Override
                public void onStateChanged(@NonNull View bottomSheet, int newState) {
                    if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    }
                }

                @Override
                public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                }
            });
            handler.postDelayed(() -> {
                WebView webView = d.findViewById(R.id.webview);
                webView.loadDataWithBaseURL("fake://", tncHtml, "text/html", "UTF-8", null);
            }, 50);
        });
        View infoDialogView = getLayoutInflater().inflate(R.layout.bottomsheet_webview_layout, null);
        tncCloseableDialog.setContentView(infoDialogView, getString(R.string.ch_terms_conditions));
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
            mainDetailsAdapter = new ChallengeMainDetailsAdapter(getActivity(), challengeResult, mPresenter,
                    isPastChallenge, this, this, this);
            mainRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            mainRecyclerView.setAdapter(mainDetailsAdapter);
            mainRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (newState != RecyclerView.SCROLL_STATE_IDLE) {
                        mainDetailsAdapter.onViewScrolled();
                    }
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        if (!isPastChallenge)
                            showBottomButton(true);
                        else
                            showFloatingSortButton();
                    } else {
                        if (!isPastChallenge)
                            showBottomButton(false);
                        else
                            hideFloatingSortButton();
                    }
                }
            });

        }
    }

    boolean isBottomVisible = true;

    private void showBottomButton(boolean show) {

        if (!show && isBottomVisible) {
            isBottomVisible = false;
            Animation slide = null;

            slide = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                    0.0f, Animation.RELATIVE_TO_SELF, 1.0F);

            slide.setDuration(400);
            slide.setFillAfter(true);
            slide.setFillEnabled(true);
            bottomButtons.startAnimation(slide);

            slide.setAnimationListener(new Animation.AnimationListener() {

                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    bottomButtons.clearAnimation();
                    bottomButtons.setVisibility(View.INVISIBLE);

                }

            });
        } else if (show && !isBottomVisible) {
            isBottomVisible = true;
            Animation slide = null;
            slide = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                    1.0f, Animation.RELATIVE_TO_SELF, 0.0f);

            slide.setDuration(400);
            slide.setFillAfter(true);
            slide.setFillEnabled(true);
            bottomButtons.startAnimation(slide);

            slide.setAnimationListener(new Animation.AnimationListener() {

                @Override
                public void onAnimationStart(Animation animation) {
                    bottomButtons.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    bottomButtons.clearAnimation();
                }
            });
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
            if (isPastChallenge) {
                isSubmissionLoaded = true;
                sortFloatingTextButton.setVisibility(View.VISIBLE);
            }
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
        }, 200L);

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
        }, 200L);

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
        mPresenter.setSubmissionLike(result);
    }

    @Override
    public void onShareClick(SubmissionResult result) {
        ShareBottomSheet.show((getActivity()).getSupportFragmentManager(), result, false);
    }

    @Override
    public void onNavigateToActivityRequest(Intent intent, int requestCode, int position) {
        navigateToActivityRequest(intent, requestCode);
    }

    @Override
    public void openVideoView(SubmissionResult submissionResult) {
        ((ChallengeDetailsActivity) getActivity()).openVideoPlayer(submissionResult);
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

    public void sortByRecent() {
        handler.postDelayed(() -> {
            mainRecyclerView.stopScroll();
            mPresenter.setPageStart(0);
            mPresenter.setSortType(Utils.QUERY_PARAM_KEY_SORT_RECENT);
            mainDetailsAdapter.clearList();
            mPresenter.unsubscribe();
            mPresenter.loadMoreItems();
        }, 100L);

        if (isPastChallenge) {
            analytics.sendEventChallenges(ChallengesGaAnalyticsTracker.EVENT_CLICK_SORT_BY,
                    ChallengesGaAnalyticsTracker.EVENT_CATEGORY_CHALLENGES_PAST_CHALLENGES,
                    ChallengesGaAnalyticsTracker.EVENT_ACTION_CLICK,
                    ChallengesGaAnalyticsTracker.EVENT_SORT_RECENT);
        } else {
            analytics.sendEventChallenges(ChallengesGaAnalyticsTracker.EVENT_CLICK_SORT_BY,
                    ChallengesGaAnalyticsTracker.EVENT_CATEGORY_CHALLENGES_ACTIVE_CHALLENEGES,
                    ChallengesGaAnalyticsTracker.EVENT_ACTION_CLICK,
                    ChallengesGaAnalyticsTracker.EVENT_SORT_RECENT);
        }

    }

    public void sortByBuzzPoints() {
        handler.postDelayed(() -> {
            mainRecyclerView.stopScroll();
            mPresenter.setPageStart(0);
            mPresenter.setSortType(Utils.QUERY_PARAM_KEY_SORT_POINTS);
            mainDetailsAdapter.clearList();
            mPresenter.unsubscribe();
            mPresenter.loadMoreItems();
        }, 100L);

        if (isPastChallenge) {
            analytics.sendEventChallenges(ChallengesGaAnalyticsTracker.EVENT_CLICK_SORT_BY,
                    ChallengesGaAnalyticsTracker.EVENT_CATEGORY_CHALLENGES_PAST_CHALLENGES,
                    ChallengesGaAnalyticsTracker.EVENT_ACTION_CLICK,
                    ChallengesGaAnalyticsTracker.EVENT_SORT_BUZZ_POINT);
        } else {
            analytics.sendEventChallenges(ChallengesGaAnalyticsTracker.EVENT_CLICK_SORT_BY,
                    ChallengesGaAnalyticsTracker.EVENT_CATEGORY_CHALLENGES_ACTIVE_CHALLENEGES,
                    ChallengesGaAnalyticsTracker.EVENT_ACTION_CLICK,
                    ChallengesGaAnalyticsTracker.EVENT_SORT_BUZZ_POINT);
        }

    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == R.id.tv_buzz_points) {
            sortByBuzzPoints();
        } else if (viewId == R.id.tv_most_recent) {
            sortByRecent();
        } else if (viewId == R.id.submit_photo) {
            mPresenter.onSubmitButtonClick();
        } else if (viewId == R.id.iv_share) {
            if (null != challengeResult) {
                ShareBottomSheet.show((getActivity()).getSupportFragmentManager(), challengeResult, false);
                analytics.sendEventChallenges(ChallengesGaAnalyticsTracker.EVENT_CLICK_SORT_BY,
                        ChallengesGaAnalyticsTracker.EVENT_CATEGORY_CHALLENGES_ACTIVE_CHALLENEGES,
                        ChallengesGaAnalyticsTracker.EVENT_ACTION_SHARE,
                        challengeResult.getTitle());
            }
        } else if (viewId == R.id.iv_sort || viewId == R.id.layout_button_container) {
            showSortingDialog();
        }
    }

    private void showSortingDialog() {
        CloseableBottomSheetDialog sortingDialog = CloseableBottomSheetDialog.createInstance(getActivity());
        sortingDialog.setOnShowListener(dialog -> {
            BottomSheetDialog d = (BottomSheetDialog) dialog;

            FrameLayout bottomSheet = d.findViewById(android.support.design.R.id.design_bottom_sheet);

            if (bottomSheet != null) {
                BottomSheetBehavior.from(bottomSheet)
                        .setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });

        View sortingView = getLayoutInflater().inflate(R.layout.bottomsheet_sort_layout, null);
        sortingDialog.setContentView(sortingView, "Sort By");
        sortingView.setOnClickListener(null);
        sortingDialog.show();

        sortingView.findViewById(R.id.tv_recent).setOnClickListener(view -> {
            if (sortBy != SortBy.Recent) {
                sortBy = SortBy.Recent;
                sortByRecent();
            }
            sortingDialog.dismiss();
        });
        sortingView.findViewById(R.id.tv_topScore).setOnClickListener(view -> {
            if (sortBy != SortBy.BuzzPoint) {
                sortBy = SortBy.BuzzPoint;
                sortByBuzzPoints();
            }
            sortingDialog.dismiss();
        });
    }

    @Override
    public void onTncClick() {
        if (tncHtml == null)
            return;
        if (isPastChallenge) {
            analytics.sendEventChallenges(ChallengesGaAnalyticsTracker.EVENT_CLICK_CHALLENGES,
                    ChallengesGaAnalyticsTracker.EVENT_CATEGORY_CHALLENGES_PAST_CHALLENGES,
                    ChallengesGaAnalyticsTracker.EVENT_ACTION_CLICK,
                    ChallengesGaAnalyticsTracker.EVENT_TNC);
        } else {
            analytics.sendEventChallenges(ChallengesGaAnalyticsTracker.EVENT_CLICK_CHALLENGES,
                    ChallengesGaAnalyticsTracker.EVENT_CATEGORY_CHALLENGES_ACTIVE_CHALLENEGES,
                    ChallengesGaAnalyticsTracker.EVENT_ACTION_CLICK,
                    ChallengesGaAnalyticsTracker.EVENT_TNC);
        }
        tncCloseableDialog.show();
    }

    public void onShowBuzzPointsText() {
        buzzPointText = ((ChallengesModuleRouter) getActivity().getApplication())
                .getStringRemoteConfig(Utils.GENERATE_BUZZ_POINT_FIREBASE_KEY);
        if (!TextUtils.isEmpty(buzzPointText)) {
            CloseableBottomSheetDialog sortingDialog = CloseableBottomSheetDialog.createInstance(getActivity());
            sortingDialog.setOnShowListener(dialog -> {
                BottomSheetDialog d = (BottomSheetDialog) dialog;
                FrameLayout bottomSheet = d.findViewById(android.support.design.R.id.design_bottom_sheet);
                if (bottomSheet != null) {
                    BottomSheetBehavior.from(bottomSheet)
                            .setState(BottomSheetBehavior.STATE_EXPANDED);
                }
                buzzPointText = ((ChallengesModuleRouter) getActivity().getApplication())
                        .getStringRemoteConfig(Utils.GENERATE_BUZZ_POINT_FIREBASE_KEY);
                if (!TextUtils.isEmpty(buzzPointText)) {
                    Utils.generateBulletText(d.findViewById(R.id.buzzTextContainer), buzzPointText);
                }
            });
            View sortingView = getLayoutInflater().inflate(R.layout.bottomsheet_buzz_point_layout, null);
            sortingDialog.setContentView(sortingView, "Buzz Point");
            sortingView.setOnClickListener(null);
            sortingDialog.show();

            if (isPastChallenge) {
                analytics.sendEventChallenges(ChallengesGaAnalyticsTracker.EVENT_CLICK_CHALLENGES,
                        ChallengesGaAnalyticsTracker.EVENT_CATEGORY_CHALLENGES_PAST_CHALLENGES,
                        ChallengesGaAnalyticsTracker.EVENT_ACTION_CLICK,
                        ChallengesGaAnalyticsTracker.EVENT_BUZZ_POINT);
            } else {
                analytics.sendEventChallenges(ChallengesGaAnalyticsTracker.EVENT_CLICK_CHALLENGES,
                        ChallengesGaAnalyticsTracker.EVENT_CATEGORY_CHALLENGES_ACTIVE_CHALLENEGES,
                        ChallengesGaAnalyticsTracker.EVENT_ACTION_CLICK,
                        ChallengesGaAnalyticsTracker.EVENT_BUZZ_POINT);
            }

        }
    }

    @Override
    public void onShowChallengeDescription() {
        if (challengeDescriptionHtml == null)
            return;
        if (challengeDesciptionDialog == null) {
            challengeDesciptionDialog = CloseableBottomSheetDialog.createInstance(getActivity());
            challengeDesciptionDialog.setOnShowListener(dialog -> {
                BottomSheetDialog d = (BottomSheetDialog) dialog;
                FrameLayout bottomSheet = d.findViewById(android.support.design.R.id.design_bottom_sheet);
                if (bottomSheet != null) {
                    BottomSheetBehavior.from(bottomSheet)
                            .setState(BottomSheetBehavior.STATE_EXPANDED);
                }
                BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
                behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                    @Override
                    public void onStateChanged(@NonNull View bottomSheet, int newState) {
                        if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        }
                    }

                    @Override
                    public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                    }
                });
                handler.postDelayed(() -> {
                    WebView webView = d.findViewById(R.id.webview);
                    webView.loadDataWithBaseURL("fake://", challengeDescriptionHtml, "text/html",
                            "UTF-8", null);
                }, 50);
            });
            View infoDialogView = getLayoutInflater().inflate(R.layout.bottomsheet_webview_layout, null);
            challengeDesciptionDialog.setContentView(infoDialogView, getString(R.string.ch_description));
        }
        challengeDesciptionDialog.show();
    }

    public enum SortBy {
        BuzzPoint, Recent
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    public void createChallengeDescriptionText(String text) {
        Observable.fromCallable(() -> new MarkdownProcessor().markdown(text))
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(String s) {
                        challengeDescriptionHtml = s;
                    }
                });
    }

    public void createTermsConditions(String text) {
        Observable.fromCallable(() -> new MarkdownProcessor().markdown(text))
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(String s) {
                        tncHtml = s;
                    }
                });
    }

    private void showFloatingSortButton() {
        if (isSubmissionLoaded)
            sortFloatingTextButton.show();
    }

    private void hideFloatingSortButton() {
        if (sortFloatingTextButton.getVisibility() == View.VISIBLE)
            sortFloatingTextButton.hide();
    }
}