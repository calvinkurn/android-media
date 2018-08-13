package com.tokopedia.challenges.view.customview;


import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.challenges.R;

public class CustomVideoPlayer extends FrameLayout implements CustomMediaController.ICurrentPos {


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
                    return;
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
                        videoView.start();
                        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mediaPlayer) {
                                thumbNail.setVisibility(VISIBLE);
                                mediaPlayer.reset();
                            }
                        });
                        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mediaPlayer) {
                                mediaPlayer.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                                    @Override
                                    public void onVideoSizeChanged(MediaPlayer mediaPlayer, int i, int i1) {
                                        mediaController.setAnchorView(videoView);
                                        videoView.setMediaController(mediaController);
                                    }
                                });
                            }
                        });
                }
            });

        }
    }
}
