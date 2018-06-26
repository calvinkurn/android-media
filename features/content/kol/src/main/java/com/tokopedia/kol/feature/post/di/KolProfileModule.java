package com.tokopedia.kol.feature.post.di;

import com.tokopedia.kol.feature.post.domain.interactor.GetKolPostUseCase;
import com.tokopedia.kol.feature.post.view.adapter.typefactory.KolPostTypeFactoryImpl;
import com.tokopedia.kol.feature.post.view.adapter.typefactory.KolPostTypeFactory;
import com.tokopedia.kol.feature.post.view.listener.KolPostListener;
import com.tokopedia.kol.feature.post.view.presenter.KolPostPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author by milhamj on 12/02/18.
 */

@Module
public class KolProfileModule {
    private final KolPostListener.View.ViewHolder viewListener;

    public KolProfileModule(KolPostListener.View.ViewHolder viewListener) {
        this.viewListener = viewListener;
    }

    @KolProfileScope
    @Provides
    KolPostListener.Presenter providesPresenter(GetKolPostUseCase getKolPostUseCase) {
        return new KolPostPresenter(getKolPostUseCase);
    }

    @KolProfileScope
    @Provides
    KolPostTypeFactory provideKolTypeFactory() {
        return new KolPostTypeFactoryImpl(viewListener);
    }
}
