package com.tokopedia.challenges.view.share;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
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
import com.tokopedia.challenges.view.activity.BaseActivity;
import com.tokopedia.challenges.view.model.Challenge;
import com.tokopedia.challenges.view.model.Result;
import com.tokopedia.challenges.view.model.challengesubmission.SubmissionResult;
import com.tokopedia.challenges.view.presenter.ShareBottomSheetContract;
import com.tokopedia.challenges.view.presenter.ShareBottomSheetPresenter;
import com.tokopedia.challenges.view.utils.Utils;

import java.util.List;

import javax.inject.Inject;

public class ShareBottomSheet extends BottomSheetDialogFragment implements BottomSheetShareAdapter.OnItemClickListener, ShareBottomSheetContract.View {

    public static final String KEY_COPY = "salinlink";
    private static final String TYPE = "text/plain";
    public static final String KEY_OTHER = "lainnya";
    private RecyclerView mRecyclerView;
    @Inject
    public ShareBottomSheetPresenter presenter;
    private ChallengesComponent challengesComponent;
    private ProgressDialog progress;
    private View closeButton;
    private SubmissionResult submissionItem;
    private Result challengeItem;
    private boolean isChallenge;

    private static ShareBottomSheet newInstance(Object item) {
        ShareBottomSheet fragment = new ShareBottomSheet();
        Bundle bundle = new Bundle();
        if (item instanceof Result) {
            bundle.putParcelable("challengeItem", (Result) item);
            bundle.putBoolean("is_challenge", true);

        } else if (item instanceof SubmissionResult) {
            bundle.putParcelable("submissionItem", (SubmissionResult) item);
            bundle.putBoolean("is_challenge", false);

        }
        fragment.setArguments(bundle);
        return fragment;
    }


    public static void show(FragmentManager fragmentManager, Object item) {
        newInstance(item).show(fragmentManager, "");
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottomsheet_share_layout, container, false);

        isChallenge = getArguments().getBoolean("is_challenge", false);
        if (isChallenge) {
            challengeItem = getArguments().getParcelable("challengeItem");
        } else {
            submissionItem = getArguments().getParcelable("submissionItem");
        }

        initView(view);
        closeButton = view.findViewById(R.id.item_close);
        closeButton.setOnClickListener(v -> dismiss());

        return view;
    }


    public void initView(View view) {
        challengesComponent = ((BaseActivity) getActivity()).getComponent();
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
        if (!resolvedActivities.isEmpty()) {
            List<ResolveInfo> showApplications = presenter.validate(resolvedActivities);

            BottomSheetShareAdapter adapter = new BottomSheetShareAdapter(showApplications, getActivity()
                    .getPackageManager());
            mRecyclerView.setAdapter(adapter);

            adapter.setOnItemClickListener(this);
        }
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
    public void onItemClick(String packageName) {
        if (isChallenge) {
            presenter.createAndShareChallenge(packageName);
        } else {
            presenter.createAndShareSubmission(packageName);
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
