package com.tokopedia.kol.feature.post.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.project.youtubeutils.activity.YoutubePlayerActivity;

/**
 * @author by yfsx on 29/06/18.
 */
public class KolPostYouTubeActivity extends YoutubePlayerActivity {

    public static Intent getInstance(Context context, String youtubeUrl) {
        Intent intent = new Intent(context, KolPostYouTubeActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_YOUTUBE_VIDEO_URL, youtubeUrl);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected String getVideoUrl() {
        return getIntent().getExtras().getString(EXTRA_YOUTUBE_VIDEO_URL);
    }
}
