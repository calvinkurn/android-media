package com.tokopedia.challenges.view.customview;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Display;
import android.view.Gravity;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.challenges.R;
import com.tokopedia.challenges.view.activity.FullScreenVideoActivity;

import static android.content.Context.WINDOW_SERVICE;

public class CustomVideoPlayer extends FrameLayout implements CustomMediaController.ICurrentPos {


    private ImageButton fullScreen;
    Context context;
    VideoView videoView;
    ImageView thumbNail;
    MediaController mediaController;

    String videoUrl;
    private boolean isFullScreen;

    public CustomVideoPlayer(@NonNull Context context) {
        super(context);
        init();
    }

    public CustomVideoPlayer(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomVideoPlayer(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        View view = inflate(getContext(), getLayout(), this);
        videoView = view.findViewById(R.id.videoView);
        thumbNail = view.findViewById(R.id.video_thumbnail);
    }

    public void setVideoThumbNail(String thumbNailUrl, String videoUrl, boolean isFullScreen) {
        ImageHandler.loadImageThumbs(getContext(), thumbNail, thumbNailUrl);
        this.videoUrl = videoUrl;
        this.isFullScreen = isFullScreen;
        startPlay(0);
    }

    protected int getLayout() {
        return R.layout.custom_video_view;
    }

    @Override
    public int getPosition() {
        return videoView.getCurrentPosition();
    }


    public void startPlay(int pos) {
        if (!TextUtils.isEmpty(videoUrl)) {
            if (pos != -1 && pos != 0) {
                if (videoView != null) {
                    videoView.seekTo(pos);
                    videoView.start();
                }
            }
            thumbNail.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    thumbNail.setVisibility(GONE);
                    Uri video = Uri.parse(videoUrl);
                    if (mediaController == null) {
                        mediaController = new CustomMediaController(getContext(), videoUrl, pos, isFullScreen, CustomVideoPlayer.this);
                        mediaController.setAnchorView(videoView);
                        videoView.setMediaController(mediaController);
                    }
                    videoView.setVideoURI(video);
                    videoView.requestFocus();
                    videoView.seekTo(pos);
                    videoView.setOnCompletionListener(mediaPlayer -> {
                        videoView.setVideoURI(video);
                        videoView.start();
                    });
                    videoView.setOnPreparedListener(mediaPlayer -> mediaPlayer.setOnVideoSizeChangedListener((mp, width, height) -> {
                        mediaController.setAnchorView(videoView);
                        videoView.setMediaController(mediaController);
                    }));
                }
            });
        }
    }
}
