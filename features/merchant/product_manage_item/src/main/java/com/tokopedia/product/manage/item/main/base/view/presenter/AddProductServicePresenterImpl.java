package com.tokopedia.product.manage.item.view.presenter;

import android.text.TextUtils;

import com.tokopedia.imagepicker.common.util.ImageUtils;
import com.tokopedia.product.manage.item.common.util.DraftNotFoundException;
import com.tokopedia.product.manage.item.common.domain.interactor.FetchDraftProductUseCase;
import com.tokopedia.product.manage.item.common.domain.interactor.DeleteSingleDraftProductUseCase;
import com.tokopedia.product.manage.item.common.domain.interactor.UpdateUploadingDraftProductUseCase;
import com.tokopedia.product.manage.item.data.exception.UploadProductException;
import com.tokopedia.product.manage.item.domain.interactor.uploadproduct.SubmitProductUseCase;
import com.tokopedia.product.manage.item.domain.listener.ProductSubmitNotificationListener;
import com.tokopedia.product.manage.item.domain.mapper.ProductUploadMapper;
import com.tokopedia.product.manage.item.common.model.edit.ProductPictureViewModel;
import com.tokopedia.product.manage.item.common.model.edit.ProductViewModel;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * @author sebastianuskh on 4/20/17.
 */

public class AddProductServicePresenterImpl extends AddProductServicePresenter {

    private static final int MIN_NOTIFICATION_PROGRESS = 6;

    private final FetchDraftProductUseCase fetchDraftProductUseCase;
    private final SubmitProductUseCase submitProductUseCase;
    private final DeleteSingleDraftProductUseCase deleteSingleDraftProductUseCase;
    private final UpdateUploadingDraftProductUseCase updateUploadingDraftProductUseCase;
    private final ProductUploadMapper addUploadMapper;

    public AddProductServicePresenterImpl(FetchDraftProductUseCase fetchDraftProductUseCase,
                                          SubmitProductUseCase submitProductUseCase,
                                          DeleteSingleDraftProductUseCase deleteSingleDraftProductUseCase,
                                          UpdateUploadingDraftProductUseCase updateUploadingDraftProductUseCase,
                                          ProductUploadMapper addUploadMapper) {
        this.fetchDraftProductUseCase = fetchDraftProductUseCase;
        this.submitProductUseCase = submitProductUseCase;
        this.deleteSingleDraftProductUseCase = deleteSingleDraftProductUseCase;
        this.updateUploadingDraftProductUseCase = updateUploadingDraftProductUseCase;
        this.addUploadMapper = addUploadMapper;
    }

    @Override
    public void uploadProduct(final long draftProductId, final ProductSubmitNotificationListener notificationCountListener) {
        fetchDraftProductUseCase.execute(FetchDraftProductUseCase.createRequestParams(draftProductId), new Subscriber<ProductViewModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable uploadThrowable) {
                Throwable e = uploadThrowable;
                if (!(e instanceof DraftNotFoundException)) {
                    updateUploadingDraftProductUseCase.executeSync(UpdateUploadingDraftProductUseCase.createRequestParams(draftProductId, false));
                }
                if (isViewAttached()) {
                    getView().onFailedAddProduct(e, notificationCountListener);
                }
            }

            @Override
            public void onNext(ProductViewModel productViewModel) {
                notificationCountListener.setProductViewModel(productViewModel);
                notificationCountListener.setMaxCount(MIN_NOTIFICATION_PROGRESS + getUploadPictureCount(productViewModel));
                notificationCountListener.addProgress();
                submitProduct(draftProductId, productViewModel, notificationCountListener);
            }
        });
    }

    private void submitProduct(final long draftProductId, final ProductViewModel productViewModel, final ProductSubmitNotificationListener notificationCountListener) {
        submitProductUseCase.execute(SubmitProductUseCase.createParams(productViewModel, notificationCountListener), new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable uploadThrowable) {
                Throwable e = uploadThrowable;
                if (e instanceof UploadProductException) {
                    e = ((UploadProductException) uploadThrowable).getThrowable();
                }
                if (isViewAttached()) {
                    getView().onFailedAddProduct(e, notificationCountListener);
                }
            }

            @Override
            public void onNext(Boolean aBoolean) {
                notificationCountListener.addProgress();
                deleteSingleDraftProductUseCase.executeSync(DeleteSingleDraftProductUseCase.createRequestParams(draftProductId));
                deletePictureCacheList(productViewModel);
                notificationCountListener.addProgress();
                getView().onSuccessAddProduct(notificationCountListener);
            }
        });
    }

    private void deletePictureCacheList(ProductViewModel productViewModel) {
        List<ProductPictureViewModel> productPictureViewModelList = productViewModel.getProductPictureViewModelList();
        if (productPictureViewModelList == null || productPictureViewModelList.size() == 0) {
            return;
        }
        ArrayList<String> pathToDeleteList = new ArrayList<>();
        for (ProductPictureViewModel productPictureViewModel : productPictureViewModelList) {
            if (productPictureViewModel == null) {
                continue;
            }
            String imagePath = productPictureViewModel.getFilePath();
            if (!TextUtils.isEmpty(imagePath)) {
                pathToDeleteList.add(imagePath);
            }
        }
        if (pathToDeleteList.size() > 0) {
            ImageUtils.deleteFilesInTokopediaFolder(pathToDeleteList);
        }
    }

    private int getUploadPictureCount(ProductViewModel productViewModel) {
        int count = 0;
        List<ProductPictureViewModel> productPictureViewModelList = productViewModel.getProductPictureViewModelList();
        if (productPictureViewModelList != null) {
            for (ProductPictureViewModel productPictureViewModel : productPictureViewModelList) {
                if (productPictureViewModel.getId() <= 0) {
                    count++;
                }
            }
        }
        count += addUploadMapper.getVariantPictureViewModelList(productViewModel).size();
        if (productViewModel.getProductSizeChart() != null) {
            count++;
        }
        return count;
    }
}