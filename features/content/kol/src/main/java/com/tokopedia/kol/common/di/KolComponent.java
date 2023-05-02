package com.tokopedia.kol.common.di;

import android.content.Context;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.feedcomponent.di.FeedComponentModule;
import com.tokopedia.kol.common.data.source.api.KolApi;
import com.tokopedia.kol.feature.video.view.fragment.VideoDetailFragment;
import com.tokopedia.user.session.UserSessionInterface;

import dagger.Component;

/**
 * @author by milhamj on 06/02/18.
 */

@KolScope
@Component(modules = {KolModule.class, FeedComponentModule.class}, dependencies = BaseAppComponent.class)
public interface KolComponent {
    KolApi kolApi();

    @ApplicationContext
    Context getContext();

    UserSessionInterface userSessionInterface();

    void inject(VideoDetailFragment videoDetailFragment);
}
