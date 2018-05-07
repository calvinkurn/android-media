package com.tokopedia.tkpd.content.di;

import com.tokopedia.tkpd.content.ContentGetFeedUseCase;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.di.FeedPlusComponent;

import dagger.Component;

/**
 * @author by milhamj on 28/02/18.
 */

@ContentConsumerScope
@Component(dependencies = FeedPlusComponent.class)
public interface ContentConsumerComponent {
    void inject(ContentGetFeedUseCase contentGetFeedUseCase);
}
