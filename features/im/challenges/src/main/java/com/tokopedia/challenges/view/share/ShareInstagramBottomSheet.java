package com.tokopedia.challenges.view.share;

import android.app.Activity;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.FileProvider;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.challenges.BuildConfig;
import com.tokopedia.challenges.R;
import com.tokopedia.challenges.view.model.Result;
import com.tokopedia.challenges.view.model.challengesubmission.SubmissionResult;
import com.tokopedia.challenges.view.utils.Utils;
import com.tokopedia.design.component.BottomSheets;

import java.io.File;

public class ShareInstagramBottomSheet extends BottomSheets {

    private static final String PACKAGENAME_INSTAGRAM = "com.instagram.android";

    private Result challengeItem;
    private SubmissionResult submissionResult;
    private String title;
    private String contains;
    private File media;
    boolean isVideo;
    private String hastag;

    @Override
    public int getLayoutResourceId() {
        return R.layout.share_instagram_layout;
    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int screenHeight = displaymetrics.heightPixels;
        getBottomSheetBehavior().setPeekHeight((int) (screenHeight / 1.5));
    }


    @Override
    public void initView(View view) {
        ImageView imgShare = view.findViewById(R.id.img_share);
        if (challengeItem != null) {
            ImageHandler.loadImageWithoutPlaceholder(imgShare, Utils.getImageUrlForSubmission(challengeItem.getThumbnailUrl()), R.color.grey_1100);
            hastag = challengeItem.getHashTag();
        } else if (submissionResult != null) {
            ImageHandler.loadImageWithoutPlaceholder(imgShare, Utils.getImageUrlForSubmission(submissionResult.getThumbnailUrl()), R.color.grey_1100);
            hastag = submissionResult.getCollection().getHashTag();

        }

        TextView btnCancel = view.findViewById(R.id.btn_cancel);
        TextView btnCopy = view.findViewById(R.id.btn_copy);
        btnCopy.setText("Salin " + hastag);
        btnCopy.setOnClickListener(v -> {
            copyToClipboard(hastag);
            createInstagramIntent();
        });
        btnCancel.setOnClickListener(v -> dismiss());

    }

    @Override
    protected String title() {
        return "";
    }

    private void setData(Object item) {
        if (item instanceof Result) {
            this.challengeItem = (Result) item;
        } else if (item instanceof SubmissionResult) {
            this.submissionResult = (SubmissionResult) item;
        }
    }

    public void setData(Object item, String title, String contains, File media, boolean isVideo) {
        this.title = title;
        this.contains = contains;
        this.media = media;
        this.isVideo = isVideo;
        setData(item);
    }

    private void createInstagramIntent() {
        try {
            String type = "image/*";
            //mediaPath = Environment.getExternalStorageDirectory() + filename;

            if (isVideo) {
                type = "video/*";
            }
            Intent share = new Intent(Intent.ACTION_SEND);

            share.setType(type);

            Uri uri = FileProvider.getUriForFile(getActivity(),
                    getActivity().getApplicationContext().getPackageName() + ".provider",
                    media);

            share.putExtra(Intent.EXTRA_STREAM, uri);
            share.putExtra(Intent.EXTRA_TITLE, title);
            share.putExtra(Intent.EXTRA_SUBJECT, title);
            share.putExtra(Intent.EXTRA_TEXT, contains);
            share.setPackage(PACKAGENAME_INSTAGRAM);
            getActivity().startActivity(Intent.createChooser(share, "Share"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void copyToClipboard(String contents) {
        ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Activity.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Tokopedia", contents);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(getActivity(), R.string.ch_copy_to_clipboard_bhahasa, Toast.LENGTH_LONG).show();
    }

}
