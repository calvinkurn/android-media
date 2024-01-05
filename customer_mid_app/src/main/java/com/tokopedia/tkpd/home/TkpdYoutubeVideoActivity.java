package com.tokopedia.tkpd.home;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.viewpager.widget.ViewPager;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tkpd.library.utils.SnackbarManager;
import com.tokopedia.abstraction.base.view.widget.TouchViewPager;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.core.analytics.ScreenTracking;
import com.tokopedia.customer_mid_app.R;
import com.tokopedia.design.viewpagerindicator.CirclePageIndicator;
import com.tokopedia.keys.Keys;
import com.tokopedia.tkpd.home.adapter.SlidingImageBannerAdapter;
import com.tokopedia.tkpd.home.analytics.HomeGATracking;
import com.tokopedia.tkpd.home.model.VideoPushBannerModel;

import java.lang.reflect.Type;
import java.util.List;

public class TkpdYoutubeVideoActivity extends YouTubeBaseActivity implements
        YouTubePlayer.OnInitializedListener {

    private static final int RECOVERY_DIALOG_REQUEST = 1;
    private static final String SCREEN_NAME = "video_push";
    private YouTubePlayerView youTubeView;
    private ImageView imgClose;
    private TextView tvHeadTitle, tvTitle, tvDesc, btnCta;
    private SlidingImageBannerAdapter adapter;
    private TouchViewPager viewPager;
    private CirclePageIndicator circlePageIndicator;
    private String videoUrlKey = "video_url";
    private String videoCtaKey = "video_cta";
    private String videoTitleKey = "video_title";
    private String videoDescHeadKey = "video_desc_head";
    private String videoDescKey = "video_desc";
    private String videoLandKey = "video_land";
    private String videoUrl;
    private String videoLand;
    private List<VideoPushBannerModel> bannerModeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.tokopedia.customer_mid_app.R.layout.activity_tkpd_youtube_video);
        youTubeView = findViewById(com.tokopedia.customer_mid_app.R.id.youtube_view);
        imgClose = findViewById(com.tokopedia.customer_mid_app.R.id.img_cross);
        tvHeadTitle = findViewById(com.tokopedia.customer_mid_app.R.id.tv_head_title);
        tvTitle = findViewById(com.tokopedia.customer_mid_app.R.id.tv_title);
        tvDesc = findViewById(com.tokopedia.customer_mid_app.R.id.tv_desc);
        btnCta = findViewById(com.tokopedia.customer_mid_app.R.id.btn_cta);
        viewPager = findViewById(com.tokopedia.customer_mid_app.R.id.v_push_bannerpager);
        circlePageIndicator = findViewById(com.tokopedia.customer_mid_app.R.id.v_pager_indicator);

        imgClose.setOnClickListener(v -> finish());

        btnCta.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(videoLand)) {
                RouteManager.route(TkpdYoutubeVideoActivity.this, videoLand);
                HomeGATracking.eventClickCTAButton(v.getContext());
            }
        });

        extractValues(getIntent().getData());
        HomeGATracking.eventYoutubeVideoImpression(this);
        ScreenTracking.sendScreen(this, SCREEN_NAME);
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
                player.loadVideo(videoUrl);
            } else {
                SnackbarManager.make(TkpdYoutubeVideoActivity.this, getString(com.tokopedia.customer_mid_app.R.string.video_not_play_error), Snackbar.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_DIALOG_REQUEST) {
            // Retry initialization if user performed a recovery action
            getYouTubePlayerProvider().initialize(Keys.getAUTH_GOOGLE_YOUTUBE_API_KEY(), this);
        }
    }

    private YouTubePlayer.Provider getYouTubePlayerProvider() {
        return (YouTubePlayerView) findViewById(R.id.youtube_view);
    }

    private void extractValues(Uri uri) {
        if (uri != null) {
            videoUrl = uri.getQueryParameter(videoUrlKey);
            String title = uri.getQueryParameter(videoDescHeadKey);
            if (!TextUtils.isEmpty(title)) {
                tvHeadTitle.setText(title);
            }
            tvTitle.setText(uri.getQueryParameter(videoTitleKey));
            tvDesc.setText(uri.getQueryParameter(videoDescKey));
            btnCta.setText(uri.getQueryParameter(videoCtaKey));
            videoLand = uri.getQueryParameter(videoLandKey);
            String jsonArray = uri.getQueryParameter("banner");;
            if (!TextUtils.isEmpty(jsonArray)) {
                setBannerAdapter(jsonArray);
            }


        }
        youTubeView.initialize(Keys.getAUTH_GOOGLE_YOUTUBE_API_KEY(), this);
    }

    private void setBannerAdapter(String jsonArray) {

        Type listType = new TypeToken<List<VideoPushBannerModel>>() {
        }.getType();
        bannerModeList = new Gson().fromJson(jsonArray, listType);
        if (bannerModeList != null && bannerModeList.size() > 0) {
            viewPager.setVisibility(View.VISIBLE);
            if (bannerModeList.size() > 1) {
                circlePageIndicator.setVisibility(View.VISIBLE);
            }
            adapter = new SlidingImageBannerAdapter(TkpdYoutubeVideoActivity.this, bannerModeList, tvHeadTitle.getText().toString());
            viewPager.setAdapter(adapter);
            circlePageIndicator.setViewPager(viewPager);
            HomeGATracking.eventClickVideoBannerImpression(tvHeadTitle.getText().toString() + "_" + bannerModeList.get(0).getBannerName() + "_" + 1);

            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    HomeGATracking.eventClickVideoBannerImpression(tvHeadTitle.getText().toString() + "_" + bannerModeList.get(position).getBannerName() + "_" + (position + 1));
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }

    }
}
