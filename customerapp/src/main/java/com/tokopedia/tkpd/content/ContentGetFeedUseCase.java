package com.tokopedia.tkpd.content;

import com.tokopedia.feedplus.domain.usecase.FollowKolPostUseCase;
import com.tokopedia.tkpd.content.di.ContentConsumerComponent;

import javax.inject.Inject;

/**
 * @author by milhamj on 28/02/18.
 */

public class ContentGetFeedUseCase {
    private static ContentGetFeedUseCase instance;
    private ContentConsumerComponent contentConsumerComponent;

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

    public FollowKolPostUseCase getFollowKolPostUseCase() {
        return followKolPostUseCase;
    }
}
