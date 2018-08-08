package com.tokopedia.challenges.view.customview;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.MediaController;
import android.widget.VideoView;

import com.tokopedia.challenges.R;

public class CustomVideoPlayer extends FrameLayout {


    Context context;
    VideoView videoView;
    MediaController mediaController;


    public CustomVideoPlayer(@NonNull Context context) {
        super(context);
        this.context = context;
        init();
    }

    private void init() {
        View view = inflate(getContext(), getLayout(), this);
        videoView = view.findViewById(R.id.videoView);

        if (mediaController == null) {
            mediaController = new MediaController(context);
            mediaController.setAnchorView(videoView);
            videoView.setMediaController(mediaController);
        }
    }

    public void setVideoThumbNail(String thumbNailUrl) {
        Bitmap thumb = ThumbnailUtils.createVideoThumbnail(thumbNailUrl,
                MediaStore.Images.Thumbnails.MINI_KIND);
        BitmapDrawable bitmapDrawable = new BitmapDrawable(thumb);
        videoView.setBackgroundDrawable(bitmapDrawable);
    }

    public void startVideoPlay(String videoUrl) {
        Uri video = Uri.parse(videoUrl);
        videoView.setVideoURI(video);

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                    @Override
                    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                        mediaController.setAnchorView(videoView);
                    }
                });
            }
        });

    }

    protected int getLayout() {
        return R.layout.custom_video_view;
    }
}
