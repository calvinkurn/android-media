package com.tokopedia.core.share;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.tokopedia.core.R;
import com.tokopedia.core.product.model.share.ShareData;
import com.tokopedia.core.share.adapter.ShareAdapter;
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
        if (data != null)
            return getTitle(data.getType());
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
        List<ResolveInfo> showApplications = new ArrayList<>();
        for (ResolveInfo resolveInfo : resolvedActivities) {
            if (Arrays.asList(PackageNameApplications)
                    .contains(resolveInfo.activityInfo.packageName)) {
                showApplications.add(resolveInfo);
            }
        }
        return showApplications;
    }

    @Override
    public void onItemClick(String packageName) {
        if (packageName.equalsIgnoreCase("lainnya")) {
            startActivity(Intent.createChooser(getIntent(""), "Share using"));
        } else if (packageName.equalsIgnoreCase("salinlink")) {
            Toast.makeText(getActivity(), "will be available soon", Toast.LENGTH_SHORT).show();
        } else {
            ShareSocmedHandler.ShareSpecific(data, getActivity(),packageName,
                    "text/plain", null, null);
        }

        sendTracker();
    }


    private Intent getIntent(String packageName) {
        final Intent mIntent = new Intent(Intent.ACTION_SEND);
        mIntent.setType("text/plain");

        if (!packageName.isEmpty())
            mIntent.setPackage(packageName);

        String title = "";
        String desc = "";

        if (data != null) {

        }

        mIntent.putExtra(Intent.EXTRA_TITLE, title);
        mIntent.putExtra(Intent.EXTRA_TEXT, desc);
        return mIntent;
    }

    private void sendTracker() {

    }

    private String getTitle(String type) {
        switch (type) {
            case ShareData.CATALOG_TYPE:
                return getString(R.string.product_share_catalog);
            case ShareData.SHOP_TYPE:
                return getString(R.string.product_share_shop);
            case ShareData.HOTLIST_TYPE:
                return getString(R.string.product_share_hotlist);
            case ShareData.DISCOVERY_TYPE:
                return getString(R.string.product_share_search);
            case ShareData.PRODUCT_TYPE:
                return getString(R.string.product_share_product);
            case ShareData.RIDE_TYPE:
                return getString(R.string.product_share_ride_trip);
            case ShareData.APP_SHARE_TYPE:
                return getString(R.string.product_share_app);
            case ShareData.REFERRAL_TYPE:
                return getString(R.string.product_share_app);
            case ShareData.PROMO_TYPE:
                return getString(R.string.promo_share_detail);
        }
        return "";
    }
}
