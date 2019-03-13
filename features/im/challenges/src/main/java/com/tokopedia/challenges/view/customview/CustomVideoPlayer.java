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
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.challenges.R;
import com.tokopedia.challenges.view.fragments.ChallengeDetailsFragment;
import com.tokopedia.challenges.view.utils.Utils;

import java.io.File;
import java.util.HashMap;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class CustomVideoPlayer extends RelativeLayout implements CustomMediaController.ICurrentPos {

    private VideoView videoView;
    private ImageView thumbNail;
    private ImageView playIcon;
    private RelativeLayout videoViewLayout;
    private CustomMediaController mediaController;
    private CustomVideoPlayerListener customVideoPlayerListener;
    private int videoWidth, videoHeight;
    private String videoOrientation = "portrait";
    private int pos = 0;

    private String videoUrl;
    private boolean isFullScreen;
    private boolean videoRotation = false;
    private boolean isLocalFile = false;

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


    public void setVideoThumbNail(String thumbNailUrl, String videoUrl, boolean isFullScreen, CustomVideoPlayerListener customVideoPlayerListener, boolean localFile) {
        ImageHandler.loadImage(getContext(), thumbNail, thumbNailUrl, R.color.grey_1100, R.color.grey_1100);
        this.videoUrl = videoUrl;
        this.isFullScreen = isFullScreen;
        this.isLocalFile = localFile;
        this.customVideoPlayerListener = customVideoPlayerListener;
        if (TextUtils.isEmpty(videoUrl) || Utils.isImage(videoUrl)) {
            playIcon.setVisibility(GONE);
        }
        startPlay(0, ChallengeDetailsFragment.isVideoPlaying);
    }


    protected int getLayout() {
        return R.layout.custom_video_view;
    }

    @Override
    public int getPosition() {
        if (videoView != null)
            return videoView.getCurrentPosition();
        return 0;
    }

    @Override
    public boolean isVideoPlaying() {
        if (videoView != null)
            return videoView.isPlaying();
        else return false;
    }


    public void startPlay(int pos, boolean isVideoPlaying) {
        this.pos = pos;
        if (!TextUtils.isEmpty(videoUrl)) {
            if (pos != -1 && pos != 0) {
                if (videoView != null) {
                    videoView.seekTo(pos);
                    if (isVideoPlaying)
                        videoView.start();
                    return;
                }
            }
            if (!TextUtils.isEmpty(videoUrl) && !Utils.isImage(videoUrl)) {
                thumbNail.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        thumbNail.setVisibility(GONE);
                        playIcon.setVisibility(GONE);
                        if (isLocalFile) {
                            playLocalVideo();
                        } else {
                            setAspectRatio();
                        }
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


    private void setMediaOptions() {
        Uri video = Uri.parse(videoUrl);
        videoView.setVideoURI(video);
        videoView.requestFocus();
        videoView.seekTo(pos);
        videoView.start();

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                if (mediaController == null) {
                    mediaController = new CustomMediaController(getContext(), videoUrl, pos, isFullScreen, CustomVideoPlayer.this, videoOrientation);
                }
                mediaPlayer.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                    @Override
                    public void onVideoSizeChanged(MediaPlayer mediaPlayer, int i, int i1) {
                        mediaController.setAnchorView(videoView);
                        videoView.setMediaController(mediaController);
                    }
                });
            }
        });

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                pos = 0;
                RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) videoView.getLayoutParams();
                int heightinDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200, getResources().getDisplayMetrics());
                params1.height = heightinDp;
                thumbNail.setVisibility(VISIBLE);
                playIcon.setVisibility(VISIBLE);
                videoView.seekTo(0);
                mediaPlayer.reset();
            }
        });


        if (customVideoPlayerListener != null)
            customVideoPlayerListener.OnVideoStart();

    }


    private void setAspectRatio() {
        Observable.just(1).map(new Func1<Integer, String>() {
            @Override
            public String call(Integer integer) {
                return getVideoAspectRatio();
            }
        }).subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(String orientation) {
                        RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) videoView.getLayoutParams();

                        if (videoHeight > videoWidth && videoHeight > 360) {
                            videoWidth = 360 * videoWidth / videoHeight;
                            videoHeight = 360;
                        }
                        int heightinDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, videoHeight, getResources().getDisplayMetrics());
                        int widthinDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, videoWidth, getResources().getDisplayMetrics());
                        videoOrientation = orientation;
                        if (orientation.equalsIgnoreCase("portrait"))
                            params1.height = heightinDp;
                        else
                            params1.width = widthinDp;
                        setMediaOptions();

                    }
                });

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
        if (videoWidth > videoHeight) {
            return "landscape";
        } else {
            return "portrait";
        }
    }

    private void playLocalVideo() {

        Uri video = Uri.parse(videoUrl);
        videoView.setVideoURI(video);
        videoView.requestFocus();
        videoView.seekTo(pos);
        videoView.start();


        MediaPlayer mediaPlayer = new MediaPlayer();
        Uri uri = Uri.fromFile(new File(videoUrl));
        try {
            mediaPlayer.setDataSource(getContext().getApplicationContext(), uri);
        } catch (IllegalArgumentException e) {
        } catch (SecurityException e) {
        } catch (IllegalStateException e) {
        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            mediaPlayer.prepare();
        } catch (IllegalStateException e) {
        } catch (Exception e) {
            e.printStackTrace();
        }
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                videoHeight = mediaPlayer.getVideoHeight();
                videoWidth = mediaPlayer.getVideoWidth();
                RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) videoView.getLayoutParams();
                if (videoHeight > videoWidth && videoHeight > 360) {
                    videoWidth = 360 * videoWidth / videoHeight;
                    videoHeight = 360;
                }

                int heightinPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, videoHeight, getResources().getDisplayMetrics());
                int widthinPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, videoWidth, getResources().getDisplayMetrics());

                params1.height = heightinPx;
                params1.width = widthinPx;
                if (videoWidth > videoHeight) {
                    videoOrientation = "landscape";
                } else {
                    videoOrientation = "portrait";
                }

                videoView.invalidate();

                if (mediaController == null) {
                    mediaController = new CustomMediaController(getContext(), videoUrl, pos, isFullScreen, CustomVideoPlayer.this, videoOrientation);
                }
                mediaPlayer.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                    @Override
                    public void onVideoSizeChanged(MediaPlayer mediaPlayer, int i, int i1) {
                        mediaController.setAnchorView(videoView);
                        videoView.setMediaController(mediaController);
                    }
                });
            }
        });

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                pos = 0;
                RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) videoView.getLayoutParams();
                int heightinDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200, getResources().getDisplayMetrics());
                params1.height = heightinDp;
                videoView.invalidate();
                thumbNail.setVisibility(VISIBLE);
                playIcon.setVisibility(VISIBLE);
                videoView.seekTo(0);
                mediaPlayer.reset();
            }
        });
        if (customVideoPlayerListener != null)
            customVideoPlayerListener.OnVideoStart();
    }

    public void pause() {
        if (videoView != null && videoView.isPlaying()) {
            videoView.pause();
        }
    }

}