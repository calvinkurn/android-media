package com.tokopedia.core.share;

import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.tokopedia.core.R;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.product.model.share.ShareData;
import com.tokopedia.core.share.adapter.ShareAdapter;
import com.tokopedia.core.util.BranchSdkUtils;
import com.tokopedia.core.util.ClipboardHandler;
import com.tokopedia.core.util.ShareSocmedHandler;
import com.tokopedia.design.component.BottomSheets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by meta on 18/05/18.
 */
public class ShareBottomSheet extends BottomSheets implements ShareAdapter.OnItemClickListener {

    public static ShareBottomSheet newInstance(ShareData data) {
        ShareBottomSheet fragment = new ShareBottomSheet();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ShareBottomSheet.class.getName(), data);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static void show(FragmentManager fragmentManager, ShareData data) {
        newInstance(data).show(fragmentManager, "Share");
    }

    private String[] PackageNameApplications = new String[] {"com.whatsapp", "com.facebook.katana",
            "jp.naver.line.android", "com.twitter.android", "com.google.android.apps.plus"};

    private ShareData data;

    @Override
    public int getLayoutResourceId() {
        return R.layout.bottomsheet_share;
    }

    @Override
    protected BottomSheetsState state() {
        return BottomSheetsState.FULL;
    }

    @Override
    protected String title() {
//        if (data != null)
//            return getTitle(data.getType());
        return "Bagikan";
    }

    @Override
    protected void configView(View parentView) {
        data = getArguments().getParcelable(ShareBottomSheet.class.getName()); // getting data from parcelable
        super.configView(parentView);
    }

    @Override
    public void initView(View view) {

        RecyclerView mRecyclerView = view.findViewById(R.id.recyclerview);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        List<ResolveInfo> resolvedActivities = getActivity().getPackageManager()
                .queryIntentActivities(getIntent(""), 0);
        if (!resolvedActivities.isEmpty()) {
            List<ResolveInfo> showApplications = validate(resolvedActivities);

            ShareAdapter adapter = new ShareAdapter(showApplications, getActivity()
                    .getPackageManager());
            mRecyclerView.setAdapter(adapter);

            adapter.setOnItemClickListener(this);
        }
    }

    private List<ResolveInfo> validate(List<ResolveInfo> resolvedActivities) {

//        List<String> packageNames = new ArrayList<>();
//        packageNames.add("com.whatsapp");
//        packageNames.add("com.facebook.katana");
//        packageNames.add("jp.naver.line.android");
//        packageNames.add("com.twitter.android");
//        packageNames.add("com.google.android.apps.plus");
//        for (String packageName : packageNames) {
//            if (resolvedActivities.contains(packageName)) {
//
//            }
//        }

        List<ResolveInfo> showApplications = new ArrayList<>();
//        for (int i = 0; i < PackageNameApplications.length; i++) {
//            if (resolvedActivities.equals(PackageNameApplications[i])) {
//                showApplications.add(resolveInfo);
//            }
//        }

//        for (ResolveInfo resolveInfo : resolvedActivities) {

//            if (Arrays.asList(PackageNameApplications)
//                    .equals(resolveInfo.activityInfo.packageName)) {
//                showApplications.add(resolveInfo);
//            }
//        }
        return showApplications;
    }

    @Override
    public void onItemClick(String packageName) {
        if (packageName.equalsIgnoreCase("lainnya")) {
            actionMore(packageName);
        } else if (packageName.equalsIgnoreCase("salinlink")) {
            actionCopy();
        } else {
            actionShare(packageName);
        }
    }

    private void actionCopy() {
        data.setSource("Copy");
        BranchSdkUtils.generateBranchLink(data, getActivity(), new BranchSdkUtils.GenerateShareContents() {
            @Override
            public void onCreateShareContents(String shareContents, String shareUri, String branchUrl) {
                ClipboardHandler.CopyToClipboard(getActivity(), shareUri);
            }
        });

        Toast.makeText(getActivity(), "Teks berhasil disalin.", Toast.LENGTH_SHORT).show();
        sendAnalyticsToGTM(data.getType(),"Copy");
    }

    private void actionShare(String packageName) {
        String media = constantMedia(packageName);
        data.setSource(media);

        ShareSocmedHandler.ShareSpecific(data, getActivity(), packageName,
                "text/plain", null, null);

        sendTracker(packageName);
    }

    private void actionMore(final String packageName) {
        BranchSdkUtils.generateBranchLink(data, getActivity(), new BranchSdkUtils.GenerateShareContents() {
            @Override
            public void onCreateShareContents(String shareContents, String shareUri, String branchUrl) {
                Intent intent = getIntent(shareContents);
                startActivity(Intent.createChooser(intent, "Lainnya"));

                sendTracker(packageName);
            }
        });
    }

    private Intent getIntent(String contains) {
        final Intent mIntent = new Intent(Intent.ACTION_SEND);
        mIntent.setType("text/plain");

        String title = "";
        if (data != null) {
            title = data.getName();
        }

        mIntent.putExtra(Intent.EXTRA_TITLE, title);
        mIntent.putExtra(Intent.EXTRA_SUBJECT, title);
        mIntent.putExtra(Intent.EXTRA_TEXT, contains);
        return mIntent;
    }

    /**
     * Tracking
     * @param packageName
     * @return String media tracking
     */
    private String constantMedia(String packageName) {
        if (packageName.contains("whatsapp")) {
            return AppEventTracking.SOCIAL_MEDIA.WHATSHAPP;
        } else if (packageName.contains("line")) {
            return AppEventTracking.SOCIAL_MEDIA.LINE;
        } else if (packageName.contains("twitter")) {
            return AppEventTracking.SOCIAL_MEDIA.TWITTER;
        } else if (packageName.contains("facebook")) {
            return AppEventTracking.SOCIAL_MEDIA.FACEBOOK;
        } else if (packageName.contains("google")) {
            return AppEventTracking.SOCIAL_MEDIA.GOOGLE_PLUS;
        } else if (packageName.contains("lainnya")) {
            return AppEventTracking.SOCIAL_MEDIA.OTHER;
        }
        return "";
    }

    private void sendTracker(String packageName) {
        String media = constantMedia(packageName);
        if (!media.isEmpty()) {
            if (data.getType().equals(ShareData.CATEGORY_TYPE)) {
                shareCategory(data, media);
            } else {
                sendAnalyticsToGTM(data.getType(), media);
            }
        }
    }

    private void shareCategory(ShareData data, String media) {
        String[] shareParam = data.getSplittedDescription(",");
        if (shareParam.length == 2) {
            UnifyTracking.eventShareCategory(shareParam[0], shareParam[1] + "-" + media);
        }
    }

    private void sendAnalyticsToGTM(String type, String channel) {
        switch (type) {
            case ShareData.REFERRAL_TYPE:
                UnifyTracking.eventReferralAndShare(AppEventTracking.Action.SELECT_CHANNEL, channel);
                TrackingUtils.sendMoEngageReferralShareEvent(channel);
                break;
            case ShareData.APP_SHARE_TYPE:
                UnifyTracking.eventAppShareWhenReferralOff(AppEventTracking.Action.SELECT_CHANNEL, channel);
                break;
            default:
                UnifyTracking.eventShare(channel);
                break;
        }
    }
}
