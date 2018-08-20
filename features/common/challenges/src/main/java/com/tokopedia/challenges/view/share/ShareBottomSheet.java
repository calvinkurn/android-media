package com.tokopedia.challenges.view.share;

import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.tokopedia.challenges.ChallengesModuleRouter;
import com.tokopedia.challenges.R;
import com.tokopedia.challenges.di.ChallengesComponent;
import com.tokopedia.challenges.view.activity.BaseActivity;
import com.tokopedia.challenges.view.presenter.ShareBottomSheetContract;
import com.tokopedia.challenges.view.presenter.ShareBottomSheetPresenter;
import com.tokopedia.core.util.ClipboardHandler;
import com.tokopedia.design.component.BottomSheets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

public class ShareBottomSheet extends BottomSheets implements BottomSheetShareAdapter.OnItemClickListener, ShareBottomSheetContract.View {
    private static final String PACKAGENAME_WHATSAPP = "com.whatsapp.ContactPicker";
    private static final String PACKAGENAME_FACEBOOK = "com.facebook.composer.shareintent.ImplicitShareIntentHandlerDefaultAlias";
    private static final String PACKAGENAME_LINE = "jp.naver.line.android.activity.selectchat.SelectChatActivityLaunchActivity";
    //private static final String PACKAGENAME_TWITTER = "com.twitter.composer.ComposerShareActivity";
    private static final String PACKAGENAME_GPLUS = "com.google.android.apps.plus.GatewayActivityAlias";
    private static final String PACKAGENAME_INSTAGRAM = "com.instagram.direct.share.handler.DirectShareHandlerActivity";
    private String[] ClassNameApplications = new String[]{PACKAGENAME_WHATSAPP, PACKAGENAME_INSTAGRAM,
            PACKAGENAME_FACEBOOK, PACKAGENAME_LINE, PACKAGENAME_GPLUS};

    private static final String KEY_WHATSAPP = "whatsapp";
    private static final String KEY_LINE = "line";
    // private static final String KEY_TWITTER = "twitter";
    private static final String KEY_FACEBOOK = "facebook";
    private static final String KEY_GOOGLE = "google";
    public static final String KEY_OTHER = "lainnya";
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

    @Override
    protected void configView(View parentView) {
        url = getArguments().getString("url");
        title = getArguments().getString("title");
        og_url = getArguments().getString("og_url");
        og_title = getArguments().getString("og_title");
        og_image = getArguments().getString("og_image");
        submissionId = getArguments().getString("submission_id");
        deepLink = getArguments().getString("deep_link");
        isChallenge = getArguments().getBoolean("is_challenge");

        super.configView(parentView);
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.bottomsheet_share_layout;
    }

    @Override
    public void initView(View view) {
        challengesComponent = ((BaseActivity) getActivity()).getComponent();
        challengesComponent.inject(this);

        presenter.attachView(this);

        Intent intent = getIntent("");
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
            List<ResolveInfo> showApplications = validate(resolvedActivities);

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

    private List<ResolveInfo> validate(List<ResolveInfo> resolvedActivities) {
        List<ResolveInfo> showApplications = new ArrayList<>();
        for (ResolveInfo resolveInfo : resolvedActivities) {
            if (Arrays.asList(ClassNameApplications)
                    .contains(resolveInfo.activityInfo.name)) {
                showApplications.add(resolveInfo);
            }
        }
        return showApplications;
    }

    @Override
    public void onItemClick(String packageName) {
        if(url == null){
            url = deepLink;
        } else if (isChallenge || url.startsWith("https://tokopedia.link") || url.startsWith("http://tokopedia.link")) {
            if (packageName.equalsIgnoreCase(KEY_COPY)) {
                ClipboardHandler.CopyToClipboard(getActivity(), url);
            } else {
                ((ChallengesModuleRouter) ((getActivity()).getApplication())).shareBranchUrlForChallenge(getActivity(), packageName, url, title);
            }
        } else {
            ((ChallengesModuleRouter) ((getActivity()).getApplication())).generateBranchUrlForChallenge(getActivity(), url, title, og_url, og_title, og_image, deepLink, new ChallengesModuleRouter.BranchLinkGenerateListener() {
                @Override
                public void onGenerateLink(String shareContents, String shareUri) {
                    presenter.postMapBranchUrl(submissionId, shareUri, packageName, shareContents);
                }
            });
        }

    }
}
