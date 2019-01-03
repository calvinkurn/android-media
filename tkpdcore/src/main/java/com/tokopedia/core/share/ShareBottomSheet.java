package com.tokopedia.core.share;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tokopedia.core.model.share.ShareData;
import com.tokopedia.core2.R;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.HotlistPageTracking;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.base.utils.StringUtils;
import com.tokopedia.core.share.adapter.ShareAdapter;
import com.tokopedia.core.util.ClipboardHandler;
import com.tokopedia.core.util.DataMapper;
import com.tokopedia.core.util.ShareSocmedHandler;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.design.component.BottomSheets;
import com.tokopedia.linker.LinkerManager;
import com.tokopedia.linker.LinkerUtils;
import com.tokopedia.linker.interfaces.ShareCallback;
import com.tokopedia.linker.model.LinkerData;
import com.tokopedia.linker.model.LinkerError;
import com.tokopedia.linker.model.LinkerShareResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by meta on 18/05/18.
 */
public class ShareBottomSheet extends BottomSheets implements ShareAdapter.OnItemClickListener {

    public static final String TITLE_EN = "Share";

    public static final String KEY_ADDING = ".isAddingProduct";

    private static final String PACKAGENAME_WHATSAPP = "com.whatsapp.ContactPicker";
    private static final String PACKAGENAME_FACEBOOK = "com.facebook.composer.shareintent.ImplicitShareIntentHandlerDefaultAlias";
    private static final String PACKAGENAME_LINE = "jp.naver.line.android.activity.selectchat.SelectChatActivityLaunchActivity";
    private static final String PACKAGENAME_TWITTER = "com.twitter.composer.ComposerShareActivity";
    private static final String PACKAGENAME_GPLUS = "com.google.android.apps.plus.GatewayActivityAlias";

    private String[] ClassNameApplications = new String[] {PACKAGENAME_WHATSAPP,
            PACKAGENAME_FACEBOOK, PACKAGENAME_LINE, PACKAGENAME_TWITTER, PACKAGENAME_GPLUS};

    private static final String KEY_WHATSAPP = "whatsapp";
    private static final String KEY_LINE = "line";
    private static final String KEY_TWITTER = "twitter";
    private static final String KEY_FACEBOOK = "facebook";
    private static final String KEY_GOOGLE = "google";
    public static final String KEY_OTHER = "lainnya";
    public static final String KEY_COPY = "salinlink";

    private static final String TYPE = "text/plain";

    private LinkerData data;
    private boolean isAdding;

    public static ShareBottomSheet newInstance(LinkerData data, boolean isAddingProduct) {
        ShareBottomSheet fragment = new ShareBottomSheet();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ShareBottomSheet.class.getName(), data);
        bundle.putBoolean(ShareBottomSheet.class.getName()+KEY_ADDING, isAddingProduct);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static void show(FragmentManager fragmentManager, LinkerData data,
                            boolean isAddingProduct) {
        newInstance(data, isAddingProduct).show(fragmentManager, TITLE_EN);
    }

    public static void show(FragmentManager fragmentManager, LinkerData data) {
        newInstance(data, false).show(fragmentManager, TITLE_EN);
    }

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
        return getString(R.string.title_share);
    }

    @Override
    protected void configView(View parentView) {
        data = getArguments().getParcelable(ShareBottomSheet.class.getName());
        isAdding = getArguments().getBoolean(ShareBottomSheet.class.getName()+KEY_ADDING, false);
        super.configView(parentView);
    }

    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private LinearLayout mLayoutError;
    private TextView mTextViewError;

    @Override
    public void initView(View view) {

        mRecyclerView = view.findViewById(R.id.recyclerview);
        mProgressBar = view.findViewById(R.id.progressbar);
        mLayoutError = view.findViewById(R.id.layout_error);
        mTextViewError = view.findViewById(R.id.message_error);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        broadcastAddProduct();
    }

    private void init() {
        Intent intent = getIntent("");

        List<ResolveInfo> resolvedActivities = getActivity().getPackageManager()
                .queryIntentActivities(intent, 0);
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
            if (Arrays.asList(ClassNameApplications)
                    .contains(resolveInfo.activityInfo.name)) {
                showApplications.add(resolveInfo);
            }
        }
        return showApplications;
    }

    @Override
    public void onItemClick(String packageName) {
        if (packageName.equalsIgnoreCase(KEY_OTHER)) {
            actionMore(packageName);
        } else if (packageName.equalsIgnoreCase(KEY_COPY)) {
            actionCopy();
        } else {
            actionShare(packageName);
        }
    }

    private void actionCopy() {
        data.setSource(AppEventTracking.Action.COPY);
        LinkerManager.getInstance().executeShareRequest(
                LinkerUtils.createShareRequest(0,
                        DataMapper.getLinkerShareData(data),
                        new ShareCallback() {
                            @Override
                            public void urlCreated(LinkerShareResult linkerShareData) {
                                ClipboardHandler.CopyToClipboard(getActivity(), linkerShareData.getShareUri());
                            }

                            @Override
                            public void onError(LinkerError linkerError) {

                            }
                        })
        );

        Toast.makeText(getActivity(), getString(R.string.msg_copy), Toast.LENGTH_SHORT).show();
        sendAnalyticsToGtm(data.getType(), AppEventTracking.Action.COPY);
    }

    private void actionShare(String packageName) {
        String media = constantMedia(packageName);
        data.setSource(media);

        ShareSocmedHandler.ShareSpecific(data, getActivity(), packageName,
                TYPE, null, null);

        sendTracker(packageName);
    }

    private void actionMore(final String packageName) {
        LinkerManager.getInstance().executeShareRequest(
                LinkerUtils.createShareRequest(
                        0, DataMapper.getLinkerShareData(data),
                        new ShareCallback() {
                            @Override
                            public void urlCreated(LinkerShareResult linkerShareData) {
                                Intent intent = getIntent(linkerShareData.getShareContents());
                                startActivity(Intent.createChooser(intent, getString(R.string.other)));
                                sendTracker(packageName);
                            }

                            @Override
                            public void onError(LinkerError linkerError) {

                            }
                        }
                )
        );
    }

    private Intent getIntent(String contains) {
        final Intent mIntent = new Intent(Intent.ACTION_SEND);
        mIntent.setType(TYPE);

        String title = "";
        if (data != null) {
            title = data.getName();
        }

        mIntent.putExtra(Intent.EXTRA_TITLE, title);
        mIntent.putExtra(Intent.EXTRA_SUBJECT, title);
        mIntent.putExtra(Intent.EXTRA_TEXT, contains);
        return mIntent;
    }

    private BroadcastReceiver addProductReceiver;

    private void stateProgress(boolean progress) {
        mLayoutError.setVisibility(View.GONE);
        if (progress) {
            mProgressBar.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        } else {
            mProgressBar.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            init();
        }
    }

    private void broadcastAddProduct() {
        stateProgress(isAdding);
        addProductReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle bundle = intent.getExtras();
                if (bundle != null) {
                    int status = bundle.getInt(TkpdState.ProductService.STATUS_FLAG, TkpdState.ProductService.STATUS_ERROR);
                    switch (status) {
                        case TkpdState.ProductService.STATUS_DONE:
                            setData(bundle);
                            stateProgress(false);
                            break;
                        case TkpdState.ProductService.STATUS_ERROR:
                        default:
                            stateProgress(false);
                            onError(bundle);
                    }
                }
            }
        };
    }

    public void onError(Bundle resultData) {
        String messageError = resultData.getString(TkpdState.ProductService.MESSAGE_ERROR_FLAG);
        mProgressBar.setVisibility(View.GONE);
        mLayoutError.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
        mTextViewError.setText(messageError + "\n" + getString(R.string.error_failed_add_product));
    }

    public void setData(Bundle data) {
        this.data = new LinkerData();
        this.data.setType(LinkerData.PRODUCT_TYPE);
        String productName = data.getString(TkpdState.ProductService.PRODUCT_NAME);
        if (StringUtils.isNotBlank(productName)) {
            this.data.setName(productName);
        }
        String imageUri = data.getString(TkpdState.ProductService.IMAGE_URI);
        if (StringUtils.isNotBlank(imageUri)) {
            this.data.setImgUri(imageUri);
        }
        String productDescription = data.getString(TkpdState.ProductService.PRODUCT_DESCRIPTION);
        if (StringUtils.isNotBlank(productDescription)) {
            this.data.setDescription(productDescription);
        }
        String productUri = data.getString(TkpdState.ProductService.PRODUCT_URI);
        if (StringUtils.isNotBlank(productUri)) {
            this.data.setUri(productUri);
        }
        String productId = data.getString(TkpdState.ProductService.PRODUCT_ID);
        if (StringUtils.isNotBlank(productId)) {
            this.data.setId(productId);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(addProductReceiver,
                new IntentFilter(TkpdState.ProductService.BROADCAST_ADD_PRODUCT));
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(addProductReceiver);
    }

    /**
     * Tracking
     * @param packageName
     * @return String media tracking
     */
    private String constantMedia(String packageName) {
        if (packageName.contains(KEY_WHATSAPP)) {
            return AppEventTracking.SOCIAL_MEDIA.WHATSHAPP;
        } else if (packageName.contains(KEY_LINE)) {
            return AppEventTracking.SOCIAL_MEDIA.LINE;
        } else if (packageName.contains(KEY_TWITTER)) {
            return AppEventTracking.SOCIAL_MEDIA.TWITTER;
        } else if (packageName.contains(KEY_FACEBOOK)) {
            return AppEventTracking.SOCIAL_MEDIA.FACEBOOK;
        } else if (packageName.contains(KEY_GOOGLE)) {
            return AppEventTracking.SOCIAL_MEDIA.GOOGLE_PLUS;
        } else if (packageName.contains(KEY_OTHER)) {
            return AppEventTracking.SOCIAL_MEDIA.OTHER;
        }
        return "";
    }

    private void sendTracker(String packageName) {
        String media = constantMedia(packageName);
        if (!media.isEmpty()) {
            if (data.getType().equals(LinkerData.CATEGORY_TYPE)) {
                shareCategory(data, media);
            } else {
                sendAnalyticsToGtm(data.getType(), media);
            }
        }
    }

    private void shareCategory(LinkerData data, String media) {
        String[] shareParam = data.getSplittedDescription(",");
        if (shareParam.length == 2) {
            UnifyTracking.eventShareCategory(getContext(), shareParam[0], shareParam[1] + "-" + media);
        }
    }

    private void sendAnalyticsToGtm(String type, String channel) {
        switch (type) {
            case LinkerData.REFERRAL_TYPE:
                UnifyTracking.eventReferralAndShare(getContext(), AppEventTracking.Action.SELECT_CHANNEL, channel);
                TrackingUtils.sendMoEngageReferralShareEvent(getContext(), channel);
                break;
            case LinkerData.APP_SHARE_TYPE:
                UnifyTracking.eventAppShareWhenReferralOff(getContext(), AppEventTracking.Action.SELECT_CHANNEL,
                        channel);
                break;
            case LinkerData.HOTLIST_TYPE:
                HotlistPageTracking.eventShareHotlist(getContext(), channel);
                break;
            default:
                UnifyTracking.eventShare(getContext(), channel);
                break;
        }
    }
}
