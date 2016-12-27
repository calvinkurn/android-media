package com.tokopedia.core.product.listener;

/**
 * Created by kris on 12/5/16. Tokopedia
 */

public interface ProductYoutubeActivityView {
    void onPlayYoutubeVideo(String videoID, int videoIndex);

    void onInitiazlizeVideo(int videoIndex);
}
