package com.tokopedia.challenges.view.customview;


import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.challenges.R;

import java.util.HashMap;

public class CustomVideoPlayer extends RelativeLayout implements CustomMediaController.ICurrentPos {

    private VideoView videoView;
    private ImageView thumbNail;
    private ImageView playIcon;
    private RelativeLayout videoViewLayout;
    private CustomMediaController mediaController;
    private CustomVideoPlayerListener customVideoPlayerListener;
    private int videoWidth, videoHeight;

    private String videoUrl;
    private boolean isFullScreen;
    private boolean videoRotation = false;

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
        videoViewLayout = view.findViewById(R.id.video_view_layout);
    }

    public void setVideoThumbNail(String thumbNailUrl, String videoUrl, boolean isFullScreen, CustomVideoPlayerListener customVideoPlayerListener) {
        ImageHandler.loadImage(getContext(), thumbNail, thumbNailUrl, R.color.grey_1100, R.color.grey_1100);
        this.videoUrl = videoUrl;
        this.isFullScreen = isFullScreen;
        this.customVideoPlayerListener = customVideoPlayerListener;
        if (TextUtils.isEmpty(videoUrl)) {
            playIcon.setVisibility(GONE);
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
                        String videoOrientation = getVideoAspectRatio();
                        Uri video = Uri.parse(videoUrl);
                        if (mediaController == null) {
                            mediaController = new CustomMediaController(getContext(), videoUrl, pos, isFullScreen, CustomVideoPlayer.this, videoOrientation);
                        }

                        videoView.setVideoURI(video);
                        videoView.requestFocus();
                        videoView.seekTo(pos);
                        videoView.start();
                        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mediaPlayer) {
                                RelativeLayout.LayoutParams params1= (RelativeLayout.LayoutParams) videoView.getLayoutParams();

                                int heightinDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200, getResources().getDisplayMetrics());

                                params1.height = heightinDp;
                                thumbNail.setVisibility(VISIBLE);
                                playIcon.setVisibility(VISIBLE);
                                videoView.seekTo(0);
                                mediaPlayer.reset();
                            }
                        });
                        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mediaPlayer) {
                                Log.d("Naveen", "on prepare has been called");
                                //mediaPlayer.setVideoScalingMode(MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
                                mediaController.setAnchorView(videoView);
                                videoView.setMediaController(mediaController);
                                //mediaPlayer.start();
                            }
                        });
                        if (customVideoPlayerListener != null)
                            customVideoPlayerListener.OnVideoStart();

                    }
                });
            }
        }
    }


    public void hideMediaController() {
        if (mediaController != null && mediaController.isShowing()) {
            mediaController.hide();
        }
    }

    public interface CustomVideoPlayerListener {
        void OnVideoStart();
    }


    private String getVideoAspectRatio() {
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

        RelativeLayout.LayoutParams params1= (RelativeLayout.LayoutParams) videoView.getLayoutParams();

        int heightinDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, videoHeight, getResources().getDisplayMetrics());
        int widthinDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, videoWidth, getResources().getDisplayMetrics());

        params1.height = heightinDp;
        params1.width = widthinDp;

            if (widthinDp > heightinDp) {
                return "landscape";
            } else {
                return "portrait";
            }
    }
}
