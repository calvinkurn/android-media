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
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.challenges.ChallengesModuleRouter;
import com.tokopedia.challenges.R;
import com.tokopedia.challenges.di.ChallengesComponent;
import com.tokopedia.challenges.view.activity.BaseActivity;
import com.tokopedia.challenges.view.presenter.ShareBottomSheetContract;
import com.tokopedia.challenges.view.presenter.ShareBottomSheetPresenter;
import com.tokopedia.core.util.ClipboardHandler;
import com.tokopedia.design.bottomsheet.BottomSheetView;
import com.tokopedia.design.component.BottomSheets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

public class ShareBottomSheet extends BottomSheetDialogFragment implements BottomSheetShareAdapter.OnItemClickListener, ShareBottomSheetContract.View {

    public static final String KEY_COPY = "salinlink";

    private static final String TYPE = "text/plain";
    private String url;
    private String title;
    private String og_url;
    private String og_title;
    private String og_image;
    private String submissionId;
    private String deepLink;
    private boolean isChallenge;
    private RecyclerView mRecyclerView;
    @Inject
    public ShareBottomSheetPresenter presenter;
    private ChallengesComponent challengesComponent;
    private ProgressDialog progress;
    private View closeButton;

    public static ShareBottomSheet newInstance(String url, String title, String og_url, String og_title, String og_image, String submissionId, String deepLink, boolean isChallenge) {
        ShareBottomSheet fragment = new ShareBottomSheet();
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        bundle.putString("title", title);
        bundle.putString("og_url", og_url);
        bundle.putString("og_title", og_title);
        bundle.putString("og_image", og_image);
        bundle.putString("submission_id", submissionId);
        bundle.putString("deep_link", deepLink);
        bundle.putBoolean("is_challenge", isChallenge);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static void show(FragmentManager fragmentManager, String url, String title, String og_url, String og_title, String og_image, String submissionId, String deepLink, boolean isChallenge) {
        newInstance(url, title, og_url, og_title, og_image, submissionId, deepLink, isChallenge).show(fragmentManager, "");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottomsheet_share_layout, container, false);

        url = getArguments().getString("url");
        title = getArguments().getString("title");
        og_url = getArguments().getString("og_url");
        og_title = getArguments().getString("og_title");
        og_image = getArguments().getString("og_image");
        submissionId = getArguments().getString("submission_id");
        deepLink = getArguments().getString("deep_link");
        isChallenge = getArguments().getBoolean("is_challenge");
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
        if (url == null) {
            url = deepLink;
        }
        presenter.createAndShareUrl(packageName, url, submissionId, deepLink, isChallenge, title, og_url, og_title, og_image);
    }

    @Override
    public void setNewUrl(String shareUri) {
        this.url = shareUri;
    }


    @Override
    public void showProgress(String message) {
        if (progress == null) {
            progress = new ProgressDialog(getContext());
        }
        if (!progress.isShowing()) {
            progress.setMessage(message);
            progress.setIndeterminate(true);
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
}
