package com.tokopedia.search.result.presentation.presenter.shop;

import com.tokopedia.discovery.common.Mapper;
import com.tokopedia.discovery.common.data.DynamicFilterModel;
import com.tokopedia.search.result.domain.model.SearchShopModel;
import com.tokopedia.search.result.domain.usecase.TestErrorUseCase;
import com.tokopedia.search.result.domain.usecase.TestUseCase;
import com.tokopedia.search.result.presentation.ShopListSectionContract;
import com.tokopedia.search.result.presentation.model.ShopViewModel;
import com.tokopedia.usecase.UseCase;
import com.tokopedia.user.session.UserSessionInterface;

import org.junit.Test;

import java.util.HashMap;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ShopListPresenterTest {

    private static abstract class MockSearchShopUseCase extends UseCase<SearchShopModel> { }
    private static abstract class MockGetDynamicFilterUseCase extends UseCase<DynamicFilterModel> { }
    private static abstract class MockShopViewModelMapper implements Mapper<SearchShopModel, ShopViewModel> { }

    private ShopListSectionContract.View shopListView = mock(ShopListSectionContract.View.class);
    private UseCase<DynamicFilterModel> dynamicFilterModelUseCase = mock(MockGetDynamicFilterUseCase.class);
    private UserSessionInterface userSession = mock(UserSessionInterface.class);
    private SearchShopModel searchShopModel = new SearchShopModel();
    private ShopViewModel shopViewModel = new ShopViewModel();
    private Mapper<SearchShopModel, ShopViewModel> testShopViewModelMapper = mock(MockShopViewModelMapper.class);

    private ShopListPresenter shopListPresenter = new ShopListPresenter();

    private void shopListPresenterInjectDependencies(UseCase<SearchShopModel> searchShopUseCase) {
        shopListPresenter.attachView(shopListView);
        shopListPresenter.searchShopUseCase = searchShopUseCase;
        shopListPresenter.getDynamicFilterUseCase = dynamicFilterModelUseCase;
        shopListPresenter.shopViewModelMapper = testShopViewModelMapper;
        shopListPresenter.userSession = userSession;
    }

    @Test
    public void loadShopSuccessWithData_RenderViewOnSuccess() {
        UseCase<SearchShopModel> testSuccessSearchShopUseCase = new TestUseCase<>(searchShopModel);
        shopListPresenterInjectDependencies(testSuccessSearchShopUseCase);
        when(testShopViewModelMapper.convert(searchShopModel)).thenReturn(shopViewModel);

        shopListPresenter.loadShop(new HashMap<>());

        verify(dynamicFilterModelUseCase).unsubscribe();
        verify(shopListView).onSearchShopSuccess(shopViewModel.getShopItemList(), false);
        verify(dynamicFilterModelUseCase).execute(any(), any());
    }

    @Test
    public void loadShopSuccessWithNull_RenderViewOnFailed() {
        UseCase<SearchShopModel> testNullSearchShopUseCase = new TestUseCase<>(null);
        shopListPresenterInjectDependencies(testNullSearchShopUseCase);

        shopListPresenter.loadShop(new HashMap<>());

        verify(dynamicFilterModelUseCase).unsubscribe();
        verify(shopListView).onSearchShopFailed();
        verify(dynamicFilterModelUseCase, never()).execute(any(), any());
    }

    @Test
    public void loadShopError_RenderViewOnFailed() {
        Exception error = new Exception("Mock exception, should be handled in Subscriber onError");
        UseCase<SearchShopModel> testErrorSearchShopUseCase = new TestErrorUseCase<>(error);
        shopListPresenterInjectDependencies(testErrorSearchShopUseCase);

        shopListPresenter.loadShop(new HashMap<>());

        verify(dynamicFilterModelUseCase).unsubscribe();
        verify(shopListView).onSearchShopFailed();
        verify(dynamicFilterModelUseCase, never()).execute(any(), any());
    }

    @Test
    public void loadShopErrorWithNulls_RenderViewOnFailed() {
        UseCase<SearchShopModel> testNullErrorSearchShopUseCase = new TestErrorUseCase<>(null);
        shopListPresenterInjectDependencies(testNullErrorSearchShopUseCase);

        shopListPresenter.loadShop(new HashMap<>());

        verify(dynamicFilterModelUseCase).unsubscribe();
        verify(shopListView).onSearchShopFailed();
        verify(dynamicFilterModelUseCase, never()).execute(any(), any());
    }

    @Test
    public void detachView_NotInjected_ShouldNotError() {
        shopListPresenter.detachView();

        assert shopListPresenter.getView() == null;
    }

    @Test
    public void detachView_AfterInjectUseCase_ShouldUnsubscribeAllUseCases() {
        UseCase<SearchShopModel> searchShopUseCase = mock(MockSearchShopUseCase.class);
        shopListPresenterInjectDependencies(searchShopUseCase);

        shopListPresenter.detachView();

        assert shopListPresenter.getView() == null;
        verify(searchShopUseCase).unsubscribe();
        verify(dynamicFilterModelUseCase).unsubscribe();
    }
}