package com.tokopedia.challenges.view.share;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.challenges.R;
import com.tokopedia.challenges.di.ChallengesComponent;
import com.tokopedia.challenges.view.activity.ChallengesBaseActivity;
import com.tokopedia.challenges.view.analytics.ChallengesGaAnalyticsTracker;
import com.tokopedia.challenges.view.analytics.ChallengesMoengageAnalyticsTracker;
import com.tokopedia.challenges.view.model.Result;
import com.tokopedia.challenges.view.model.challengesubmission.SubmissionResult;
import com.tokopedia.challenges.view.presenter.ShareBottomSheetContract;
import com.tokopedia.challenges.view.presenter.ShareBottomSheetPresenter;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Ashwani Tyagi on 13/09/18.
 */

public class ShareBottomSheet extends BottomSheetDialogFragment implements BottomSheetShareAdapter.OnItemClickListener, ShareBottomSheetContract.View {

    public static final String KEY_COPY = "salinlink";
    private static final String TYPE = "text/plain";
    public static final String KEY_OTHER = "lainnya";
    private static final String PARAM_CHALLENGE_ITEM = "PARAM_CHALLENGE_ITEM";
    private static final String PARAM_SUBMISSION_ITEM = "PARAM_SUBMISSION_ITEM";
    private static final String PARAM_IS_CHALLENGE = "param_is_challenge";
    private static final String PARAM_IS_SHOW_HEADING = "param_is_show_heading";
    private RecyclerView mRecyclerView;
    @Inject
    public ShareBottomSheetPresenter presenter;
    private ChallengesComponent challengesComponent;
    private ProgressDialog progress;
    private View closeButton;
    private SubmissionResult submissionItem;
    private Result challengeItem;
    private boolean isChallenge;
    private boolean showHeading;
    private final static String SCREEN_NAME = "challenges";
    @Inject
    public ChallengesGaAnalyticsTracker analytics;

    private static ShareBottomSheet newInstance(Object item, boolean showHeading) {
        ShareBottomSheet fragment = new ShareBottomSheet();
        Bundle bundle = new Bundle();
        if (item instanceof Result) {
            bundle.putParcelable(PARAM_CHALLENGE_ITEM, (Result) item);
            bundle.putBoolean(PARAM_IS_CHALLENGE, true);
            bundle.putBoolean(PARAM_IS_SHOW_HEADING, showHeading);

        } else if (item instanceof SubmissionResult) {
            bundle.putParcelable(PARAM_SUBMISSION_ITEM, (SubmissionResult) item);
            bundle.putBoolean(PARAM_IS_CHALLENGE, false);
            bundle.putBoolean(PARAM_IS_SHOW_HEADING, showHeading);
        }
        fragment.setArguments(bundle);
        return fragment;
    }


    public static void show(FragmentManager fragmentManager, Object item, boolean showHeading) {
        newInstance(item, showHeading).show(fragmentManager, "");
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottomsheet_share_layout, container, false);

        isChallenge = getArguments().getBoolean(PARAM_IS_CHALLENGE, false);
        if (isChallenge) {
            challengeItem = getArguments().getParcelable(PARAM_CHALLENGE_ITEM);
        } else {
            submissionItem = getArguments().getParcelable(PARAM_SUBMISSION_ITEM);
        }
        showHeading = getArguments().getBoolean(PARAM_IS_SHOW_HEADING, false);

        initView(view);
        View headingView = view.findViewById(R.id.tv_heading);
        closeButton = view.findViewById(R.id.item_close);
        closeButton.setOnClickListener(v -> dismiss());
        if (showHeading) {
            headingView.setVisibility(View.VISIBLE);
        } else {
            headingView.setVisibility(View.GONE);
        }
        View titleView = view.findViewById(R.id.layout_title);
        titleView.setOnClickListener(view1 -> dismiss());
        analytics.sendScreenEvent(getActivity(), SCREEN_NAME);
        return view;
    }

    public void initView(View view) {
        challengesComponent = ((ChallengesBaseActivity) getActivity()).getComponent();
        challengesComponent.inject(this);
        presenter.attachView(this);
        mRecyclerView = view.findViewById(R.id.recyclerview);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        init();
    }

    private void init() {
        Intent intent = getIntent("");

        List<ResolveInfo> resolvedActivities = getActivity().getPackageManager()
                .queryIntentActivities(intent, 0);
        //if (!resolvedActivities.isEmpty()) {
        List<ResolveInfo> showApplications = presenter.appInstalledOrNot();

        BottomSheetShareAdapter adapter = new BottomSheetShareAdapter(showApplications, getActivity()
                .getPackageManager());
        mRecyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(this);
        // }
    }

    private Intent getIntent(String contains) {
        final Intent mIntent = new Intent(Intent.ACTION_SEND);
        mIntent.setType(TYPE);
        String title = "";
        mIntent.putExtra(Intent.EXTRA_TITLE, title);
        mIntent.putExtra(Intent.EXTRA_SUBJECT, title);
        mIntent.putExtra(Intent.EXTRA_TEXT, contains);
        return mIntent;
    }


    @Override
    public void onItemClick(String packageName, String name) {
        if (isChallenge) {
            presenter.createAndShareChallenge(packageName, name);
        } else {
            presenter.createAndShareSubmission(packageName, name);
            if (submissionItem != null && submissionItem.getCollection() != null) {
                ChallengesMoengageAnalyticsTracker.challengePostShared(getActivity(), submissionItem.getCollection().getTitle(),
                        submissionItem.getCollection().getId(), submissionItem.getId(), presenter.getParticipatedStatus(submissionItem), name);
            }
        }
    }

    @Override
    public void showProgress(String message) {
        if (progress == null) {
            progress = new ProgressDialog(getContext());
        }
        if (!progress.isShowing()) {
            progress.setMessage(message);
            progress.setIndeterminate(true);
            progress.setCanceledOnTouchOutside(false);
            progress.show();
        }
    }

    @Override
    public void hideProgress() {
        if (progress != null && progress.isShowing()) {
            progress.dismiss();
            progress = null;
        }
    }

    @Override
    public Result getChallengeItem() {
        return challengeItem;
    }

    @Override
    public SubmissionResult getSubmissionItem() {
        return submissionItem;
    }

}
