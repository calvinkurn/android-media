package com.tokopedia.navigation.presentation.module;

import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.core.util.PagingHandler;
import com.tokopedia.home.beranda.di.module.HomeModule;
import com.tokopedia.home.beranda.presentation.presenter.HomePresenter;
import com.tokopedia.shop.common.domain.interactor.GetShopInfoByDomainUseCase;

import dagger.Module;

import static org.mockito.Mockito.mock;

@Module
public class TestHomeModule extends HomeModule {
    private HomePresenter homePresenter;

    @Override
    protected HomePresenter realHomePresenter(PagingHandler pagingHandler, UserSession userSession, GetShopInfoByDomainUseCase getShopInfoByDomainUseCase) {
        if (homePresenter == null)
            return homePresenter = mock(HomePresenter.class);

        return homePresenter;
    }
}
