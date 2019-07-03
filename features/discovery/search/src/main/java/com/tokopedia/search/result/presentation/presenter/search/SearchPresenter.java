package com.tokopedia.search.result.presentation.presenter.search;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.discovery.newdiscovery.constant.SearchApiConst;
import com.tokopedia.discovery.newdiscovery.domain.model.InitiateSearchModel;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.helper.ListHelper;
import com.tokopedia.search.result.presentation.SearchContract;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;

class SearchPresenter extends BaseDaggerPresenter<SearchContract.View> implements SearchContract.Presenter {
    private static final int DISCOVERY_URL_SEARCH = 1;
    private static final int DISCOVERY_APPLINK = 2;

    @Inject
    UseCase<InitiateSearchModel> initiateSearchModelUseCase;

    @Override
    public void initInjector(SearchContract.View view) {
        SearchPresenterComponent component = DaggerSearchPresenterComponent.builder()
                .baseAppComponent(view.getBaseAppComponent())
                .build();

        component.inject(this);
    }

    @Override
    public void onPause() {
        unsubscribeUseCases();
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void detachView() {
        super.detachView();
        unsubscribeUseCases();
    }

    private void unsubscribeUseCases() {
        if(initiateSearchModelUseCase != null) initiateSearchModelUseCase.unsubscribe();
    }

    @Override
    public void initiateSearch(Map<String, Object> searchParameter) {
        initiateSearchCheckForNulls();
        if(searchParameter == null) return;

        RequestParams requestParams = createInitiateSearchRequestParams(searchParameter);

        initiateSearchModelUseCase.execute(requestParams, getInitiateSearchSubscriber());
    }

    private void initiateSearchCheckForNulls() {
        checkViewAttached();
        if(initiateSearchModelUseCase == null) throw new RuntimeException("UseCase<InitiateSearchModel> is not injected.");
    }

    private RequestParams createInitiateSearchRequestParams(Map<String, Object> searchParameter) {
        RequestParams requestParams = RequestParams.create();

        requestParams.putString(SearchApiConst.SOURCE, SearchApiConst.DEFAULT_VALUE_SOURCE_SEARCH);
        requestParams.putString(SearchApiConst.DEVICE, SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_DEVICE);
        requestParams.putBoolean(SearchApiConst.RELATED, true);

        requestParams.putAll(searchParameter);

        return requestParams;
    }

    private Subscriber<InitiateSearchModel> getInitiateSearchSubscriber() {
        return new Subscriber<InitiateSearchModel>() {
            @Override
            public void onNext(InitiateSearchModel initiateSearchModel) {
                initiateSearchOnNext(initiateSearchModel);
            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                initiateSearchOnError(e);
            }
        };
    }

    private void initiateSearchOnNext(InitiateSearchModel initiateSearchModel) {
        if(initiateSearchModel == null) {
            getView().initiateSearchHandleResponseError();
            return;
        }

        String redirectApplink = getRedirectApplink(initiateSearchModel);

        switch (defineRedirectApplink(redirectApplink)) {
            case DISCOVERY_URL_SEARCH :
                onHandleSearch(initiateSearchModel);
                break;
            case DISCOVERY_APPLINK :
                getView().initiateSearchHandleApplink(redirectApplink);
                break;
            default :
                getView().initiateSearchHandleResponseUnknown();
        }
    }

    private String getRedirectApplink(InitiateSearchModel initiateSearchModel) {
        // in Kotlin:
        // initiateSearchModel?.searchProduct?.redirection?.redirectApplink ?: ""

        return initiateSearchModel == null ? "" :
                initiateSearchModel.getSearchProduct() == null ? "" :
                        initiateSearchModel.getSearchProduct().getRedirection() == null ? "" :
                                initiateSearchModel.getSearchProduct().getRedirection().getRedirectApplink() == null ? "" :
                                        initiateSearchModel.getSearchProduct().getRedirection().getRedirectApplink();
    }

    private int defineRedirectApplink(String applink) {
        return applink.equals("") ? DISCOVERY_URL_SEARCH : DISCOVERY_APPLINK;
    }

    private void onHandleSearch(InitiateSearchModel initiateSearchModel) {
        boolean isHasCatalog = ListHelper.isContainItems(
                // in Kotlin:
                // initiateSearchModel?.searchProduct?.catalogs
                initiateSearchModel == null ? null :
                        initiateSearchModel.getSearchProduct() == null ? null :
                                initiateSearchModel.getSearchProduct().getCatalogs()
        );

        getView().initiateSearchHandleResponseSearch(isHasCatalog);
    }

    private void initiateSearchOnError(Throwable e) {
        if(e != null) {
            e.printStackTrace();
        }

        getView().initiateSearchHandleResponseError();
    }
}
