package com.tokopedia.tkpd.home;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.tkpd.library.utils.SnackbarManager;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.home.analytics.HomeGATracking;

public class TkpdYoutubeVideoActivity extends YouTubeBaseActivity implements
        YouTubePlayer.OnInitializedListener {

    private static final int RECOVERY_DIALOG_REQUEST = 1;
    private YouTubePlayer mPlayer;
    private YouTubePlayerView youTubeView;
    private ImageView imgClose;
    private TextView tvHeadTitle, tvTitle, tvDesc, btnCta;

    private String videoUrlKey = "video_url";
    private String videoCtaKey = "video_cta";
    private String videoTitleKey = "video_title";
    private String videoDescHeadKey = "video_desc_head";
    private String videoDescKey = "video_desc";
    private String videoLandKey = "video_land";

    private String videoUrl;
    private String videoLand;

    @DeepLink(ApplinkConst.PLAY_NOTIFICATION_VIDEO)
    public static Intent getNotifVodeoApplinkCallingIntent(Context context, Bundle bundle) {
        return TkpdYoutubeVideoActivity.createApplinkCallingIntent(
                context,
                bundle
        );
    }

    public static Intent createApplinkCallingIntent(Context context, Bundle extras) {
        Intent intent = new Intent(context, TkpdYoutubeVideoActivity.class);
        intent.putExtras(extras);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tkpd_youtube_video);
        youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);
        imgClose = findViewById(R.id.img_cross);
        tvHeadTitle = findViewById(R.id.tv_head_title);
        tvTitle = findViewById(R.id.tv_title);
        tvDesc = findViewById(R.id.tv_desc);
        btnCta = findViewById(R.id.btn_cta);

        imgClose.setOnClickListener(v -> finish());

        btnCta.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(videoLand)) {
                RouteManager.route(TkpdYoutubeVideoActivity.this, videoLand);
                HomeGATracking.eventClickCTAButton(v.getContext());
            }
            finish();
        });

        extractValues(getIntent().getExtras());
        HomeGATracking.eventYoutubeVideoImpression(this);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                        YouTubeInitializationResult errorReason) {
        if (errorReason.isUserRecoverableError()) {
            errorReason.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show();
        } else {
            String errorMessage = errorReason.toString();
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                        YouTubePlayer player, boolean wasRestored) {
        if (!wasRestored) {
            if (!TextUtils.isEmpty(videoUrl)) {
                mPlayer = player;
                player.loadVideo(videoUrl);
            }else{
                SnackbarManager.make(TkpdYoutubeVideoActivity.this,getString(R.string.video_not_play_error),Snackbar.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_DIALOG_REQUEST) {
            // Retry initialization if user performed a recovery action
            getYouTubePlayerProvider().initialize(getString(com.tokopedia.tkpdpdp.R.string.GOOGLE_API_KEY), this);
        }
    }

    private YouTubePlayer.Provider getYouTubePlayerProvider() {
        return (YouTubePlayerView) findViewById(R.id.youtube_view);
    }

    private void extractValues(Bundle bundle) {
        if (bundle != null) {
            videoUrl = bundle.getString(videoUrlKey);
            String title = bundle.getString(videoDescHeadKey, "");
            if (!TextUtils.isEmpty(title)) {
                tvHeadTitle.setText(title);
            }
            tvTitle.setText(bundle.getString(videoTitleKey, ""));
            tvDesc.setText(bundle.getString(videoDescKey, ""));
            btnCta.setText(bundle.getString(videoCtaKey, ""));
            videoLand = bundle.getString(videoLandKey, "");
        }
        youTubeView.initialize(getString(com.tokopedia.tkpdpdp.R.string.GOOGLE_API_KEY), this);
    }
}
