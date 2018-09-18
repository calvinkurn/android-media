package com.tokopedia.browse.homepage.presentation.presenter;

import com.tokopedia.browse.RxAndroidTestPlugins;
import com.tokopedia.browse.RxJavaTestPlugins;
import com.tokopedia.browse.homepage.domain.usecase.DigitalBrowseMarketplaceUseCase;
import com.tokopedia.browse.homepage.presentation.contract.DigitalBrowseMarketplaceContract;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * @author by furqan on 04/09/18.
 */

@RunWith(MockitoJUnitRunner.class)
public class DigitalBrowseMarketplacePresenterTest {

    @Mock
    DigitalBrowseMarketplaceUseCase marketplaceUseCase;
    @Mock
    DigitalBrowseMarketplaceContract.View view;

    DigitalBrowseMarketplacePresenter presenter;
    DigitalBrowseMarketplacePresenter presenterSpy;

    @Before
    public void setUp() throws Exception {

        RxJavaTestPlugins.setImmediateScheduler();
        RxAndroidTestPlugins.setImmediateScheduler();

        presenter = new DigitalBrowseMarketplacePresenter(marketplaceUseCase, );
        presenter.attachView(view);

        presenterSpy = Mockito.spy(presenter);
    }

    @After
    public void tearDown() throws Exception {
        RxJavaTestPlugins.resetJavaTestPlugins();
        RxAndroidTestPlugins.resetAndroidTestPlugins();
    }

    @Test
    public void OnSuccessGetMarketplaceData_ShouldRenderData() throws Exception {
        //Given

        //When
        presenter.onSuccessGetMarketplace(Mockito.anyObject());

        //Then
        Mockito.verify(presenter.getView()).renderData(Mockito.anyObject());
    }
}