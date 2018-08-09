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
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.network.exception.MessageErrorException;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.challenges.R;
import com.tokopedia.challenges.di.ChallengesComponent;
import com.tokopedia.challenges.di.DaggerChallengesComponent;
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
    ChallengeSubmissionPresenter challengeSubmissionPresenter;
    ImageView challengeImage;
    TextView challengeTitle;
    TextView challengeDueDate;
    TextView tv_participated;
    TextView seeMoreButton;
    ImageView seeMoreArrow;
    ExpandableTextView description;
    LinearLayout expandDescription;
    RecyclerView submissionRecyclerView, awardRecylerView;
    List<SubmissionResult> submissionResults;
    CustomVideoPlayer videoPlayer;
    ConstraintLayout timerView;
    ProgressBar timerProgressBar;
    CountDownView countDownView;
    TextView hashTag;
    TextView tv_TnC;
//    TextView expand_TnC;

    SubmissionItemAdapter submissionItemAdapter;

    Result challengeResult;
    private AwardAdapter awardAdapter;
    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private AppBarLayout appBarLayout;
    private ChallengesFragmentCallbacks fragmentCallbacks;
    private String tncText;

    public static Fragment createInstance(Bundle extras) {
        Fragment fragment = new ChallegeneSubmissionFragment();
        fragment.setArguments(extras);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.challengeResult = getArguments().getParcelable("challengesResult");
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.challenges_detail_fragment, container, false);
        challengeImage =  view.findViewById(R.id.image_challenge);
        challengeTitle =  view.findViewById(R.id.tv_title);
        hashTag =  view.findViewById(R.id.tv_hashtag);
        challengeDueDate =  view.findViewById(R.id.tv_expiry_date);
        tv_participated =  view.findViewById(R.id.tv_participated);
        timerView = view.findViewById(R.id.cl_timer);
        timerProgressBar = view.findViewById(R.id.progressBar);
        countDownView = view.findViewById(R.id.count_down);
        description =  view.findViewById(R.id.tv_expandable_description);
        expandDescription = view.findViewById(R.id.expand_view_description);
        seeMoreButton =  view.findViewById(R.id.seemorebutton_description);
        seeMoreArrow =  view.findViewById(R.id.down_arrow_description);
        submissionRecyclerView = view.findViewById(R.id.rv_submissions);
        awardRecylerView = view.findViewById(R.id.rv_awards);

        toolbar = view.findViewById(R.id.toolbar);
        ((BaseSimpleActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(ContextCompat.getDrawable(getActivity(), R.drawable.ic_action_back));
        collapsingToolbarLayout = view.findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
        collapsingToolbarLayout.setTitle(" ");
        appBarLayout = view.findViewById(R.id.app_bar);
        description.setInterpolator(new OvershootInterpolator());
        expandDescription.setOnClickListener(this);
        view.findViewById(R.id.tv_see_all).setOnClickListener(this);

        videoPlayer = view.findViewById(R.id.video_player);

        tv_TnC = view.findViewById(R.id.tv_tnc);
//        expand_TnC = view.findViewById(R.id.seemorebutton_tnc);
        challengeSubmissionPresenter.attachView(this);
        challengeSubmissionPresenter.initialize();

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
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        submissionRecyclerView.setLayoutManager(mLayoutManager);
        submissionItemAdapter = new SubmissionItemAdapter(submissionResponse.getSubmissionResults(), this, LinearLayoutManager.HORIZONTAL);
        submissionRecyclerView.setAdapter(submissionItemAdapter);
    }

    @Override
    public void renderChallengeDetail() {
        ImageHandler.loadImage(getActivity(), challengeImage, challengeResult.getThumbnailUrl(), R.color.grey_1100, R.color.grey_1100);
        challengeTitle.setText(challengeResult.getTitle());
        challengeDueDate.setText(String.format(getResources().getString(R.string.text_due_date),
                Utils.convertUTCToString(challengeResult.getEndDate())));
        if (!TextUtils.isEmpty(challengeResult.getHashTag()))
        hashTag.setText(challengeResult.getHashTag());
        description.setText(challengeResult.getDescription());
        setCountDownView();
//        videoPlayer.setVideoThumbNail(submissionResponse.getSubmissionResults());
        LinearLayoutManager mLayoutManager1 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        awardRecylerView.setLayoutManager(mLayoutManager1);
        awardAdapter = new AwardAdapter(challengeResult.getPrizes());
        awardRecylerView.setAdapter(awardAdapter);


    }

    private void setCountDownView() {
        if (challengeResult.getMe() != null && challengeResult.getMe().getSubmissionCounts() != null) {
            if (challengeResult.getMe().getSubmissionCounts().getApproved() > 0) {
                tv_participated.setText("Approved");
            } else if (challengeResult.getMe().getSubmissionCounts().getDeclined() > 0) {
                tv_participated.setText("Declined");
            } else if (challengeResult.getMe().getSubmissionCounts().getWaiting() > 0) {
                tv_participated.setText("Waiting");
            } else if (System.currentTimeMillis() > Utils.convertUTCToMillis(challengeResult.getEndDate())) {
                tv_participated.setText("Completed");
            } else {
                try {
                    countDownView.setStartDuration(Utils.convertUTCToMillis(challengeResult.getEndDate()));
                    countDownView.start(timerProgressBar);
                } catch (MessageErrorException e) {
                    e.printStackTrace();
                }
                timerView.setVisibility(View.VISIBLE);
                tv_participated.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public RequestParams getParams() {
        RequestParams requestParams=RequestParams.create();
        requestParams.putString(Utils.QUERY_PARAM_CHALLENGE_ID, challengeResult.getId());
        requestParams.putInt(Utils.QUERY_PARAM_KEY_START, 0);
        requestParams.putInt(Utils.QUERY_PARAM_KEY_SIZE, 10);
        requestParams.putString(Utils.QUERY_PARAM_KEY_SORT, Utils.QUERY_PARAM_KEY_SORT_RECENT);
        return requestParams;
    }

    @Override
    public View getRootView() {
        return null;
    }

    @Override
    public void showProgressBar() {

    }

    @Override
    public void hideProgressBar() {

    }

    @Override
    public LinearLayoutManager getLayoutManager() {
        return null;
    }

    @Override
    public void renderTnC(TermsNCondition termsNCondition) {
        tncText = termsNCondition.getTerms();
        tv_TnC.setText("Terms & Condition");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (tv_TnC.getLineCount() >= 10) {
//                    expand_TnC.setVisibility(View.VISIBLE);
                } else {
//                    expand_TnC.setVisibility(View.GONE);
                }
            }
        }, 100);
//        expand_TnC.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.expand_view_description) {
            if (description.isExpanded()) {
                seeMoreButton.setText(R.string.expand);

            } else {
                seeMoreButton.setText(R.string.collapse);

            }
            description.toggle();
        } else if (v.getId() == R.id.tv_see_all) {
            fragmentCallbacks.replaceFragment(submissionResults);
        }
//        else if (v.getId() == R.id.seemorebutton_tnc) {
//            fragmentCallbacks.replaceFragment(tncText, "Terms & Conditions");
//        }

    }

    @Override
    public void onNavigateToActivityRequest(Intent intent, int requestCode, int position) {

    }
}
