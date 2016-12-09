package com.tokopedia.core.product.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.tokopedia.core.R;
import com.tokopedia.core.product.listener.ProductYoutubeActivityView;


/**
 * Created by kris on 11/8/16. Tokopedia
 */

public class YoutubeActivityThumbnail extends LinearLayout{

    private YouTubeThumbnailView youTubeThumbnailView;
    private ProgressBar loadingBar;
    private ProductYoutubeActivityView parentView;

    public YoutubeActivityThumbnail(Context context) {
        super(context);
        initView(context);
    }

    public YoutubeActivityThumbnail(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public YoutubeActivityThumbnail(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.place_holder_youtube_vertical_view, this, true);

        youTubeThumbnailView = (YouTubeThumbnailView) findViewById(R.id.youtube_thumbnail_view);
        loadingBar = (ProgressBar) findViewById(R.id.loading_bar);
    }

    public void setListener(ProductYoutubeActivityView parenView) {
        this.parentView = parenView;
    }

    public void setVideo(final String videoId, final int videoIndex) {
        youTubeThumbnailView.initialize(getContext().getApplicationContext().getString(R.string.GOOGLE_API_KEY),
                thumbnailListener(videoId, videoIndex));
    }

    private YouTubeThumbnailView.OnInitializedListener thumbnailListener(final String videoId,
                                                                         final int videoIndex){
        return new YouTubeThumbnailView.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView,
                                                YouTubeThumbnailLoader loader) {
                loader.setVideo(videoId);
                loader.setOnThumbnailLoadedListener(thumbnailLoadedListener(loader,
                        videoIndex));
                youTubeThumbnailView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        parentView.onPlayYoutubeVideo(videoId, videoIndex);
                    }
                });
            }

            @Override
            public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView,
                                                YouTubeInitializationResult initializationResult) {
                loadingBar.setVisibility(GONE);
            }
        };
    }

    private YouTubeThumbnailLoader
            .OnThumbnailLoadedListener thumbnailLoadedListener(final YouTubeThumbnailLoader loader,
                                                               final int videoIndex) {
        return new YouTubeThumbnailLoader.OnThumbnailLoadedListener() {
            @Override
            public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {
                loader.release();
                parentView.onInitiazlizeVideo(videoIndex);
                loadingBar.setVisibility(GONE);
            }

            @Override
            public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView,
                                         YouTubeThumbnailLoader.ErrorReason errorReason) {

            }
        };
    }
}