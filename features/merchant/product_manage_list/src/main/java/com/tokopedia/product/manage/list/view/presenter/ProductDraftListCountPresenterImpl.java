package com.tokopedia.product.manage.list.view.presenter;

import androidx.lifecycle.LiveData;

import com.tokopedia.product.manage.common.draft.domain.usecase.GetAllProductsCountDraftUseCase;
import com.tokopedia.product.manage.item.main.draft.domain.UpdateUploadingDraftProductUseCase;
import com.tokopedia.product.manage.list.domain.ClearAllDraftProductUseCase;
import com.tokopedia.product.manage.list.domain.FetchAllDraftProductCountUseCase;
import com.tokopedia.usecase.RequestParams;
import javax.inject.Inject;
import rx.Subscriber;

/**
 * Created by hendry on 6/20/2017.
 */

public class ProductDraftListCountPresenterImpl extends ProductDraftListCountPresenter {
    private FetchAllDraftProductCountUseCase fetchAllDraftProductCountUseCase;
    private GetAllProductsCountDraftUseCase getAllProductsCountDraftUseCase;
    private ClearAllDraftProductUseCase clearAllDraftProductUseCase;
    private UpdateUploadingDraftProductUseCase updateUploadingDraftProductUseCase;

    @Inject
    public ProductDraftListCountPresenterImpl(FetchAllDraftProductCountUseCase fetchAllDraftProductCountUseCase,
                                              GetAllProductsCountDraftUseCase getAllProductsCountDraftUseCase,
                                              ClearAllDraftProductUseCase clearAllDraftProductUseCase,
                                              UpdateUploadingDraftProductUseCase updateUploadingDraftProductUseCase){
        this.fetchAllDraftProductCountUseCase = fetchAllDraftProductCountUseCase;
        this.getAllProductsCountDraftUseCase = getAllProductsCountDraftUseCase;
        this.clearAllDraftProductUseCase = clearAllDraftProductUseCase;
        this.updateUploadingDraftProductUseCase = updateUploadingDraftProductUseCase;
    }

    @Override
    public LiveData<Integer> getAllDraftCount() {
        return getAllProductsCountDraftUseCase.execute();
    }

    @Override
    public void fetchAllDraftCountWithUpdateUploading() {
        updateUploadingDraftProductUseCase.execute(UpdateUploadingDraftProductUseCase.createRequestParamsUpdateAll(false),
                getUploadingSubscriber());
    }

    @Override
    public void clearAllDraft() {
        clearAllDraftProductUseCase.execute(RequestParams.EMPTY, new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {
                // no op
            }

            @Override
            public void onError(Throwable e) {
                // no op
            }

            @Override
            public void onNext(Boolean aBoolean) {
                // no op
            }
        });
    }

    @Override
    public void detachView() {
        super.detachView();
        fetchAllDraftProductCountUseCase.unsubscribe();
        clearAllDraftProductUseCase.unsubscribe();
        updateUploadingDraftProductUseCase.unsubscribe();
    }

    private Subscriber<Boolean> getUploadingSubscriber() {
        return new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().observeDraftCount();
            }

            @Override
            public void onNext(Boolean aBoolean) {
                getView().observeDraftCount();
            }
        };
    }
}
