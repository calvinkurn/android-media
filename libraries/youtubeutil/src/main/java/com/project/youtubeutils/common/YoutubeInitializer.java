package com.project.youtubeutils.common;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;

/**
 * @author by yfsx on 29/06/18.
 */
public class YoutubeInitializer {

    public interface OnSingleVideoInitialListener {
        void onSuccessInitializePlayer(YouTubePlayer player, String videoUrl);
        void onFailedInitializePlayer(String error);
    }

    public interface OnVideoThumbnailInitialListener {
        void onSuccessInitializeThumbnail(YouTubeThumbnailLoader loader, YouTubeThumbnailView youTubeThumbnailView);
        void onErrorInitializeThumbnail(String error);
    }

    public static YouTubePlayer.OnInitializedListener
    singleVideoInitializer(String videoUrl, OnSingleVideoInitialListener listener) {
        return new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                YouTubePlayer youTubePlayer,
                                                boolean b) {
                youTubePlayer.setFullscreen(true);
                youTubePlayer.setShowFullscreenButton(false);
                listener.onSuccessInitializePlayer(youTubePlayer, videoUrl);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                YouTubeInitializationResult initializationResult) {
                listener.onFailedInitializePlayer(initializationResult.toString());
            }
        };
    }

    public static YouTubeThumbnailView.OnInitializedListener
    videoThumbnailInitializer(final String youtubeVideoId, OnVideoThumbnailInitialListener listener) {
        return new YouTubeThumbnailView.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView,
                                                final YouTubeThumbnailLoader loader) {
                loader.setVideo(youtubeVideoId);
                loader.setOnThumbnailLoadedListener(new YouTubeThumbnailLoader
                        .OnThumbnailLoadedListener() {
                    @Override
                    public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView,
                                                  String s) {
                        loader.release();
                        listener.onSuccessInitializeThumbnail(loader, youTubeThumbnailView);
                    }

                    @Override
                    public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView,
                                                 YouTubeThumbnailLoader.ErrorReason errorReason) {
                        listener.onErrorInitializeThumbnail(errorReason.toString());
                    }
                });
            }

            @Override
            public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView,
                                                YouTubeInitializationResult result) {
                listener.onErrorInitializeThumbnail(result.toString());
            }
        };
    }
}
