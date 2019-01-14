package com.tokopedia.kol.feature.post.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.youtubeutils.activity.YoutubePlayerActivity;

/**
 * @author by yfsx on 29/06/18.
 */
public class KolPostYouTubeActivity extends YoutubePlayerActivity {
    public static final String ARGS_YOUTUBE_URL = "youtube_url";

    @DeepLink(ApplinkConst.KOL_YOUTUBE)
    public static Intent getInstance(Context context, Bundle bundle) {
        return getInstance(context, bundle.getString(ARGS_YOUTUBE_URL));
    }

    public static Intent getInstance(Context context, String youtubeUrl) {
        Intent intent;
        intent = new Intent(context, KolPostYouTubeActivity.class);
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
