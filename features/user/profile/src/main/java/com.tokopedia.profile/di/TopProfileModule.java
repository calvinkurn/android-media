package com.tokopedia.profile.di;

import com.tokopedia.kol.feature.post.domain.interactor.FollowKolPostGqlUseCase;
import com.tokopedia.profile.usecase.GetTopProfileDataUseCase;
import com.tokopedia.profile.view.listener.TopProfileActivityListener;
import com.tokopedia.profile.view.presenter.TopProfilePresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author by alvinatin on 28/02/18.
 */

@Module
public class TopProfileModule {

    @TopProfileScope
    @Provides
    TopProfileActivityListener.Presenter
    providesPresenter(GetTopProfileDataUseCase getTopProfileDataUseCase, FollowKolPostGqlUseCase followKolPostGqlUseCase){
        return new TopProfilePresenter(getTopProfileDataUseCase, followKolPostGqlUseCase);
    }

}
