package com.tokopedia.tkpdcontent.feature.profile.di;

import com.tokopedia.tkpdcontent.feature.profile.domain.interactor.GetProfileKolDataUseCase;
import com.tokopedia.tkpdcontent.feature.profile.view.adapter.typefactory.KolPostTypeFactoryImpl;
import com.tokopedia.tkpdcontent.feature.profile.view.adapter.typefactory.KolPostTypeFactory;
import com.tokopedia.tkpdcontent.feature.profile.view.listener.KolPostListener;
import com.tokopedia.tkpdcontent.feature.profile.view.presenter.KolPostPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author by milhamj on 12/02/18.
 */

@Module
public class KolProfileModule {
    private final KolPostListener.View viewListener;

    public KolProfileModule(KolPostListener.View viewListener) {
        this.viewListener = viewListener;
    }

    @KolProfileScope
    @Provides
    KolPostListener.Presenter providesPresenter(GetProfileKolDataUseCase getProfileKolDataUseCase) {
        return new KolPostPresenter(getProfileKolDataUseCase);
    }

    @KolProfileScope
    @Provides
    KolPostTypeFactory provideKolTypeFactory() {
        return new KolPostTypeFactoryImpl(viewListener);
    }
}
