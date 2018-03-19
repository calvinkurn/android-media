package com.tokopedia.tkpd.content;

import com.tokopedia.tkpd.content.di.ContentConsumerComponent;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.usecase.FollowKolPostUseCase;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.usecase.LikeKolPostUseCase;

import javax.inject.Inject;

/**
 * @author by milhamj on 28/02/18.
 */

public class ContentGetFeedUseCase {
    private static ContentGetFeedUseCase instance;
    private ContentConsumerComponent contentConsumerComponent;

    @Inject
    LikeKolPostUseCase likeKolPostUseCase;
    @Inject
    FollowKolPostUseCase followKolPostUseCase;

    private ContentGetFeedUseCase(ContentConsumerComponent contentConsumerComponent) {
        this.contentConsumerComponent = contentConsumerComponent;
    }

    public static ContentGetFeedUseCase newInstance(ContentConsumerComponent
                                                            contentConsumerComponent) {
        if (instance == null) {
            instance = new ContentGetFeedUseCase(contentConsumerComponent);
        }
        return instance;
    }

    public ContentGetFeedUseCase inject() {
        if (contentConsumerComponent != null) {
            contentConsumerComponent.inject(this);
        }
        return this;
    }

    public LikeKolPostUseCase getLikeKolPostUseCase() {
        return likeKolPostUseCase;
    }

    public FollowKolPostUseCase getFollowKolPostUseCase() {
        return followKolPostUseCase;
    }
}
