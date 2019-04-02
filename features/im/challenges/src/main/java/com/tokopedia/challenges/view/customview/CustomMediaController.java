package com.tokopedia.challenges.view.customview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.MediaController;

import com.tokopedia.challenges.R;
import com.tokopedia.challenges.view.activity.FullScreenLandscapeActivity;
import com.tokopedia.challenges.view.activity.FullScreenPortraitVideoActivity;
import com.tokopedia.challenges.view.fragments.ChallengeDetailsFragment;
//import com.tokopedia.challenges.view.fragments.ChallegeneSubmissionFragment;

public class CustomMediaController extends MediaController {

    private ImageButton fullScreen;
    private boolean isFullScreen;
    private int pos;
    String videoUrl;
    ICurrentPos iCurrentPos;
    private String videoOrientation = "portrait";

    public CustomMediaController(Context context, String url, int pos, boolean isFullScreen, ICurrentPos iCurrentPos, String videoOrientation) {
        super(context);
        this.videoUrl = url;
        this.pos = pos;
        this.isFullScreen = isFullScreen;
        this.iCurrentPos = iCurrentPos;
        this.videoOrientation = videoOrientation;
    }

    @Override
    public void setAnchorView(View view) {
        super.setAnchorView(view);
        fullScreen = new ImageButton(super.getContext());
        FrameLayout.LayoutParams params =
                new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT);

        params.gravity = Gravity.RIGHT;
        params.topMargin = 40;
        if (isFullScreen) {
            params.rightMargin = 80;
            fullScreen.setImageResource(R.drawable.ic_close_default);
        } else {
            params.rightMargin = 40;
            fullScreen.setImageResource(R.drawable.fullscreen_icon);
        }
        addView(fullScreen, params);
        if (videoOrientation.equalsIgnoreCase("portrait")) {
            fullScreen.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isFullScreen) {
                        ChallengeDetailsFragment.VIDEO_POS = iCurrentPos.getPosition();
                        ChallengeDetailsFragment.isVideoPlaying = iCurrentPos.isVideoPlaying();
                        ((Activity) getContext()).finish();
                    } else {
                        Intent intent = new Intent(getContext(), FullScreenPortraitVideoActivity.class);
                        intent.putExtra(FullScreenLandscapeActivity.SEEK_POS_PARAM, iCurrentPos.getPosition());
                        intent.putExtra(FullScreenLandscapeActivity.ISPLAYING_PARAM, iCurrentPos.isVideoPlaying());
                        intent.putExtra(FullScreenLandscapeActivity.VIDEO_URL_PARAM, videoUrl);
                        ((Activity) getContext()).startActivityForResult(intent, 100);
                    }

                }
            });
        } else {
            fullScreen.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isFullScreen) {
                        ChallengeDetailsFragment.VIDEO_POS = iCurrentPos.getPosition();
                        ChallengeDetailsFragment.isVideoPlaying = iCurrentPos.isVideoPlaying();
                        ((Activity) getContext()).finish();
                    } else {
                        Intent intent = new Intent(getContext(), FullScreenLandscapeActivity.class);
                        intent.putExtra(FullScreenLandscapeActivity.SEEK_POS_PARAM, iCurrentPos.getPosition());
                        intent.putExtra(FullScreenLandscapeActivity.ISPLAYING_PARAM, iCurrentPos.isVideoPlaying());
                        intent.putExtra(FullScreenLandscapeActivity.VIDEO_URL_PARAM, videoUrl);
                        ((Activity) getContext()).startActivityForResult(intent, 100);
                    }

                }
            });
        }
    }

    public interface ICurrentPos {
        int getPosition();
        boolean isVideoPlaying();
    }
}

