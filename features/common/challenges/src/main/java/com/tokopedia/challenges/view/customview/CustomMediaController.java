package com.tokopedia.challenges.view.customview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.MediaController;

import com.tokopedia.challenges.R;
import com.tokopedia.challenges.view.activity.FullScreenVideoActivity;
import com.tokopedia.challenges.view.fragments.ChallegeneSubmissionFragment;

public class CustomMediaController extends MediaController {

    private ImageButton fullScreen;
    private boolean isFullScreen;
    private int pos;
    String videoUrl;
    ICurrentPos iCurrentPos;

    public CustomMediaController(Context context, String url, int pos, boolean isFullScreen, ICurrentPos iCurrentPos) {
        super(context);
        this.videoUrl = url;
        this.pos = pos;
        this.isFullScreen = isFullScreen;
        this.iCurrentPos = iCurrentPos;
    }

    @Override
    public void setAnchorView(View view) {
        super.setAnchorView(view);
        fullScreen = new ImageButton(super.getContext());
        FrameLayout.LayoutParams params =
                new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT);

        params.gravity = Gravity.RIGHT;
        params.rightMargin = 80;
        params.topMargin = 40;
        addView(fullScreen, params);
        if (isFullScreen) {
            fullScreen.setImageResource(R.drawable.ic_close_default);
        } else {
            fullScreen.setImageResource(R.drawable.fullscreen_icon);
        }
        fullScreen.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFullScreen) {
                    ChallegeneSubmissionFragment.VIDEO_POS = iCurrentPos.getPosition();
                    ((Activity) getContext()).finish();
                } else {
                    Intent intent = new Intent(getContext(), FullScreenVideoActivity.class);
                    intent.putExtra("fullScreenInd", "");
                    intent.putExtra("seekPos", iCurrentPos.getPosition());
                    intent.putExtra("videoUrl", videoUrl);
                    ((Activity) getContext()).startActivityForResult(intent, 100);
                }

            }
        });
    }
    public interface ICurrentPos {
        int getPosition();
    }
}

