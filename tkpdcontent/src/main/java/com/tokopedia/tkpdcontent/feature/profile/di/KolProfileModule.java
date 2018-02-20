package com.tokopedia.tkpdcontent.feature.profile.di;

import com.tokopedia.tkpdcontent.feature.profile.view.listener.KolPostListener;
import com.tokopedia.tkpdcontent.feature.profile.view.presenter.KolPostPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author by milhamj on 12/02/18.
 */

@KolProfileScope
@Module
class KolProfileModule {
    @KolProfileScope
    @Provides
    KolPostListener.Presenter providesPresenter() {
        return new KolPostPresenter();
    }
}
