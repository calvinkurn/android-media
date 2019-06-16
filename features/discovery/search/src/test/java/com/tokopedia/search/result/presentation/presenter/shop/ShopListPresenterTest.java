package com.tokopedia.search.result.presentation.presenter.shop;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.discovery.common.data.DynamicFilterModel;
import com.tokopedia.search.result.presentation.ShopListSectionContract;
import com.tokopedia.search.result.presentation.presenter.subscriber.RequestDynamicFilterSubscriber;
import com.tokopedia.search.result.presentation.view.listener.RequestDynamicFilterListener;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ShopListPresenterTest {

    private static abstract class MockDynamicFilterModelUseCase extends UseCase<DynamicFilterModel> { }

    private ShopListSectionContract.View shopListView = mock(ShopListSectionContract.View.class);

    private RequestDynamicFilterListener requestDynamicFilterListener = mock(RequestDynamicFilterListener.class);
    private UseCase<DynamicFilterModel> dynamicFilterModelUseCase = mock(MockDynamicFilterModelUseCase.class);
    private UserSessionInterface userSession = mock(UserSessionInterface.class);

    private ShopListPresenter shopListPresenter;

    @Before
    public void setUp() {
        shopListPresenter = new ShopListPresenter();
    }

    @Test(expected = BaseDaggerPresenter.CustomerViewNotAttachedException.class)
    public void requestDynamicFilter_ViewNotAttached_ShouldThrowException() {
        shopListPresenter.requestDynamicFilter(new HashMap<>());
    }

    @Test(expected = RuntimeException.class)
    public void requestDynamicFilter_NoListener_ShouldThrowException() {
        shopListPresenter.attachView(shopListView);
        shopListPresenter.getDynamicFilterUseCase = dynamicFilterModelUseCase;

        shopListPresenter.requestDynamicFilter(new HashMap<>());
    }

    @Test(expected = RuntimeException.class)
    public void requestDynamicFilter_NotInjectedUseCase_ShouldThrowException() {
        shopListPresenter.attachView(shopListView);
        shopListPresenter.setRequestDynamicFilterListener(requestDynamicFilterListener);

        shopListPresenter.requestDynamicFilter(new HashMap<>());
    }

    @Test(expected = RuntimeException.class)
    public void requestDynamicFilter_NotInjectedUserSession_ShouldThrowException() {
        shopListPresenter.attachView(shopListView);
        shopListPresenter.setRequestDynamicFilterListener(requestDynamicFilterListener);
        shopListPresenter.getDynamicFilterUseCase = dynamicFilterModelUseCase;

        shopListPresenter.requestDynamicFilter(new HashMap<>());
    }

    @Test
    public void requestDynamicFilter_GivenAnySearchParameter_ShouldExecuteUseCase() {
        shopListPresenter.attachView(shopListView);
        shopListPresenter.setRequestDynamicFilterListener(requestDynamicFilterListener);
        shopListPresenter.getDynamicFilterUseCase = dynamicFilterModelUseCase;
        shopListPresenter.userSession = userSession;

        shopListPresenter.requestDynamicFilter(new HashMap<>());

        verify(dynamicFilterModelUseCase).execute(any(RequestParams.class), any(RequestDynamicFilterSubscriber.class));
    }
}