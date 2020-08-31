package com.tokopedia.product.addedit.imagepicker.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.product.addedit.imagepicker.domain.interactor.ClearCacheCatalogUseCase;
import com.tokopedia.product.addedit.imagepicker.domain.interactor.GetCatalogImageUseCase;
import com.tokopedia.product.addedit.imagepicker.view.model.CatalogModelView;

import java.util.List;

import rx.Subscriber;

/**
 * Created by zulfikarrahman on 6/5/18.
 */

public class ImagePickerCatalogPresenter extends BaseDaggerPresenter<ImagePickerCatalogContract.View>
        implements ImagePickerCatalogContract.Presenter {

    private GetCatalogImageUseCase getCatalogImageUseCase;
    private ClearCacheCatalogUseCase clearCacheCatalogUseCase;

    public ImagePickerCatalogPresenter(GetCatalogImageUseCase getCatalogImageUseCase,
                                       ClearCacheCatalogUseCase clearCacheCatalogUseCase) {
        this.getCatalogImageUseCase = getCatalogImageUseCase;
        this.clearCacheCatalogUseCase = clearCacheCatalogUseCase;
    }

    @Override
    public void getCatalogImage(String catalogId) {
        getCatalogImageUseCase.execute(getCatalogImageUseCase.createRequestParams(catalogId), getSubscriberCatalogImage());
    }

    private Subscriber<List<CatalogModelView>> getSubscriberCatalogImage() {
        return new Subscriber<List<CatalogModelView>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if(isViewAttached()) {
                    getView().showGetListError(e);
                }
            }

            @Override
            public void onNext(List<CatalogModelView> catalogModelViews) {
                getView().renderList(catalogModelViews);
            }
        };
    }

    @Override
    public void clearCacheCatalog() {
        clearCacheCatalogUseCase.executeSync();
    }

    @Override
    public void detachView() {
        getCatalogImageUseCase.unsubscribe();
        super.detachView();
    }
}
