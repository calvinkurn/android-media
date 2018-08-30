package com.tokopedia.challenges.view.customview;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.challenges.R;
import com.tokopedia.challenges.view.activity.FullScreenVideoActivity;

import java.util.HashMap;

public class CustomVideoPlayer extends RelativeLayout implements CustomMediaController.ICurrentPos {

    private CustomVideoView videoView;
    private ImageView thumbNail;
    private ImageView playIcon;
    private CustomMediaController mediaController;
    private CustomVideoPlayerListener customVideoPlayerListener;
    private int videoWidth, videoHeight;

    private String videoUrl;
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
        playIcon = view.findViewById(R.id.play_icon);
    }

    public void setVideoThumbNail(String thumbNailUrl, String videoUrl, boolean isFullScreen, CustomVideoPlayerListener customVideoPlayerListener) {
        ImageHandler.loadImage(getContext(), thumbNail, thumbNailUrl, R.color.grey_1100, R.color.grey_1100);
        this.videoUrl = videoUrl;
        this.isFullScreen = isFullScreen;
        this.customVideoPlayerListener = customVideoPlayerListener;
        if (TextUtils.isEmpty(videoUrl)) {
            playIcon.setVisibility(GONE);
        }
        if (!TextUtils.isEmpty(videoUrl)) {
            getVideoAspectRatio();
            videoView.setVideoDimensions(videoWidth, videoHeight);
        }
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
            if (!TextUtils.isEmpty(videoUrl)) {
                thumbNail.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        thumbNail.setVisibility(GONE);
                        playIcon.setVisibility(GONE);
                        Uri video = Uri.parse(videoUrl);
                        if (mediaController == null) {
                            mediaController = new CustomMediaController(getContext(), videoUrl, pos, isFullScreen, CustomVideoPlayer.this);
                        }
                        videoView.setVideoURI(video);
                        videoView.requestFocus();
                        videoView.seekTo(pos);
                        videoView.start();
                        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mediaPlayer) {
                                thumbNail.setVisibility(VISIBLE);
                                playIcon.setVisibility(VISIBLE);
                                videoView.seekTo(0);
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
                        if (customVideoPlayerListener != null)
                            customVideoPlayerListener.OnVideoStart();

                    }
                });
            }
        }
    }


    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Intent intent = new Intent(getContext(), FullScreenVideoActivity.class);
            intent.putExtra("fullScreenInd", "");
            intent.putExtra("seekPos", getPosition());
            intent.putExtra("videoUrl", videoUrl);
            ((Activity) getContext()).startActivityForResult(intent, 100);
        }
    }

    public void hideMediaController() {
        if(mediaController!=null && mediaController.isShowing()){
            mediaController.hide();
        }
    }

    public interface CustomVideoPlayerListener {
        void OnVideoStart();
    }


    private void getVideoAspectRatio() {
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        if (Build.VERSION.SDK_INT >= 14) {
            mediaMetadataRetriever.setDataSource(videoUrl, new HashMap<String, String>());
        } else {
            mediaMetadataRetriever.setDataSource(videoUrl);
        }
        String height = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT);
        String width = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH);
        videoWidth = Integer.parseInt(width);
        videoHeight = Integer.parseInt(height);
    }
}
