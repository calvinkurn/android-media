package com.tokopedia.product.manage.item.video.domain;

import com.tokopedia.product.manage.item.video.domain.model.YoutubeVideoModel;

import rx.Observable;

/**
 * @author normansyahputa on 4/11/17.
 */
public interface YoutubeVideoRepository {
    Observable<YoutubeVideoModel> fetchYoutubeVideoInfo(String videoId, String keyId);
}
