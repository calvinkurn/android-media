package com.tokopedia.challenges.view.share;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.challenges.R;
import com.tokopedia.challenges.view.model.Result;
import com.tokopedia.challenges.view.model.challengesubmission.SubmissionResult;
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
        getBottomSheetBehavior().setPeekHeight((int)(screenHeight / 1.5));
    }


    @Override
    public void initView(View view) {
        TextView desc = view.findViewById(R.id.tv_desc);
        TextView btnCopy = view.findViewById(R.id.btn_copy);
        btnCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createInstagramIntent();
            }
        });
        //desc.setText(mLobDetails.getDescription());


    }

    @Override
    protected String title() {
        return "";
    }

    public void setData(Object item) {
        if (item instanceof Result) {
            this.challengeItem = (Result) item;
        } else if (item instanceof SubmissionResult) {
            this.submissionResult = (SubmissionResult) item;
        }
    }

    public void setData(String title, String contains, File media, boolean isVideo){
        this.title =title;
        this.contains = contains;
        this.media = media;
        this.isVideo = isVideo;
    }

    private void createInstagramIntent() {
        String type = "image/*";
        //String filename = "/myVideo.mp4";
        //mediaPath = Environment.getExternalStorageDirectory() + filename;

        // Create the new Intent using the 'Send' action.
        if (isVideo) {
            type = "video/*";
        }
        Intent share = new Intent(Intent.ACTION_SEND);

        // Set the MIME type
        share.setType(type);

        // Create the URI from the media
        // File media = new File(mediaPath);
        Uri uri = Uri.fromFile(media);

        // Add the URI to the Intent.
        share.putExtra(Intent.EXTRA_STREAM, uri);
        share.putExtra(Intent.EXTRA_TITLE, title);
        share.putExtra(Intent.EXTRA_SUBJECT, title);
        share.putExtra(Intent.EXTRA_TEXT, contains);
        share.setPackage(PACKAGENAME_INSTAGRAM);
        getActivity().startActivity(Intent.createChooser(share, "Share"));
    }
}
