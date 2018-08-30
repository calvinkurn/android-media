package com.tokopedia.product.manage.item.catalog.view.presenter;

import com.tokopedia.product.manage.item.catalog.domain.FetchCatalogDataUseCase;
import com.tokopedia.product.manage.item.main.base.data.source.cloud.model.catalogdata.CatalogDataModel;

import rx.Subscriber;

/**
 * @author hendry on 4/3/17.
 */

public class CatalogPickerPresenterImpl extends CatalogPickerPresenter {
    private final FetchCatalogDataUseCase fetchCatalogDataUseCase;

    public CatalogPickerPresenterImpl(FetchCatalogDataUseCase fetchCatalogDataUseCase) {
        this.fetchCatalogDataUseCase = fetchCatalogDataUseCase;
    }

    @Override
    public void fetchCatalogData(String keyword, long departmentId, int start, int rows) {
        fetchCatalogDataUseCase.execute(
                FetchCatalogDataUseCase.createRequestParams(keyword, departmentId, start, rows),
                new Subscriber<CatalogDataModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (!isViewAttached()) {
                            return;
                        }
                        getView().showError(e);
                    }

                    @Override
                    public void onNext(CatalogDataModel catalogDataModel) {
                        getView().successFetchData(
                                catalogDataModel.getResult().getCatalogs(),
                                catalogDataModel.getResult().getTotalRecord());
                    }
                });
    }

    public void detachView() {
        super.detachView();
        fetchCatalogDataUseCase.unsubscribe();
    }


}
