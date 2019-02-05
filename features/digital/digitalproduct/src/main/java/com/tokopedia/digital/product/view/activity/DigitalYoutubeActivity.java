package com.tokopedia.digital.product.view.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import com.tokopedia.youtubeutils.activity.YoutubePlayerActivity;

public class DigitalYoutubeActivity extends YoutubePlayerActivity {

    public static final String EXTRA_YOUTUBE_VIDEO_URL = "EXTRA_YOUTUBE_VIDEO_URL";
    private static final String YOUTUBE_DEFAULT_PREFIX = "https://www.youtube.com/watch?v=";

    private String videoUrl;

    public static Intent createInstance(Context context, String videoId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(YOUTUBE_DEFAULT_PREFIX + videoId));
            return intent;
        } else {
            Intent intent = new Intent(context, DigitalYoutubeActivity.class);
            intent.putExtra(EXTRA_YOUTUBE_VIDEO_URL, videoId);
            return intent;
        }
    }

    @Override
    protected String getVideoUrl() {
        videoUrl = getIntent().getStringExtra(EXTRA_YOUTUBE_VIDEO_URL);
        return videoUrl;
    }

}