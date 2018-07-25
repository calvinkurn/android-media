package com.project.youtubeutils.common;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;

/**
 * @author by yfsx on 29/06/18.
 */
public class YoutubeInitializer {

    private static boolean isThumbnailReadyForInitialized = true;
    private static boolean isVideoReadyForInitialized = true;

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
        if (isVideoReadyForInitialized) {
            isVideoReadyForInitialized = false;
            return new YouTubePlayer.OnInitializedListener() {
                @Override
                public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                    YouTubePlayer youTubePlayer,
                                                    boolean b) {
                    isVideoReadyForInitialized = true;
                    youTubePlayer.setFullscreen(true);
                    youTubePlayer.setShowFullscreenButton(false);
                    listener.onSuccessInitializePlayer(youTubePlayer, videoUrl);
                }

                @Override
                public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                    YouTubeInitializationResult initializationResult) {
                    isVideoReadyForInitialized = true;
                    listener.onFailedInitializePlayer(initializationResult.toString());
                }
            };
        } else return null;
    }

    public static YouTubeThumbnailView.OnInitializedListener
    videoThumbnailInitializer(final String youtubeVideoId, OnVideoThumbnailInitialListener listener) {
        if (isThumbnailReadyForInitialized) {
            isThumbnailReadyForInitialized = false;
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
                            isThumbnailReadyForInitialized = true;
                            listener.onErrorInitializeThumbnail(errorReason.toString());
                        }
                    });
                }

                @Override
                public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView,
                                                    YouTubeInitializationResult result) {
                    isThumbnailReadyForInitialized = true;
                    listener.onErrorInitializeThumbnail(result.toString());
                }
            };
        } else return null;
    }
}
