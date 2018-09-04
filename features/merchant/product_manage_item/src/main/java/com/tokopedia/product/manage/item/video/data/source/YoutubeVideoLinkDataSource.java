package com.tokopedia.product.manage.item.video.data.source;

import com.tokopedia.product.manage.item.main.base.data.mapper.YoutubeVidToDomainMapper;
import com.tokopedia.product.manage.item.video.data.source.cloud.YoutubeVideoLinkCloud;
import com.tokopedia.product.manage.item.video.domain.model.YoutubeVideoModel;

import rx.Observable;

/**
 * Created by normansyahputa on 4/12/17.
 */

public class YoutubeVideoLinkDataSource {

    private final YoutubeVideoLinkCloud youtubeVideoLinkCloud;
    private final YoutubeVidToDomainMapper youtubeVidToDomainMapper;

    public YoutubeVideoLinkDataSource(
            YoutubeVideoLinkCloud youtubeVideoLinkCloud,
            YoutubeVidToDomainMapper youtubeVidToDomainMapper
    ) {
        this.youtubeVideoLinkCloud = youtubeVideoLinkCloud;
        this.youtubeVidToDomainMapper = youtubeVidToDomainMapper;
    }

    public Observable<YoutubeVideoModel> fetchDataFromNetwork(String videoId, String keyId) {
        return youtubeVideoLinkCloud.fetchDataFromNetwork(videoId, keyId)
                .map(youtubeVidToDomainMapper);
    }
}
