package com.tokopedia.product.manage.item.main.base.view.presenter;

import android.text.TextUtils;

import com.tokopedia.imagepicker.common.util.FileUtils;
import com.tokopedia.product.manage.item.common.util.DraftNotFoundException;
import com.tokopedia.product.manage.item.main.base.data.exception.UploadProductException;
import com.tokopedia.product.manage.item.main.base.data.model.ProductPictureViewModel;
import com.tokopedia.product.manage.item.main.base.data.model.ProductViewModel;
import com.tokopedia.product.manage.item.main.base.domain.SubmitProductUseCase;
import com.tokopedia.product.manage.item.main.base.domain.mapper.ProductUploadMapper;
import com.tokopedia.product.manage.item.main.base.view.listener.ProductSubmitNotificationListener;
import com.tokopedia.product.manage.item.main.draft.domain.DeleteSingleDraftProductUseCase;
import com.tokopedia.product.manage.item.main.draft.domain.FetchDraftProductUseCase;
import com.tokopedia.product.manage.item.main.draft.domain.UpdateUploadingDraftProductUseCase;

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
        submitProductUseCase.execute(SubmitProductUseCase.createParams(productViewModel, notificationCountListener), new Subscriber<Integer>() {
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
            public void onNext(Integer productId) {
                notificationCountListener.addProgress();
                deleteSingleDraftProductUseCase.executeSync(DeleteSingleDraftProductUseCase.createRequestParams(draftProductId));
                deletePictureCacheList(productViewModel);
                notificationCountListener.addProgress();
                notificationCountListener.getProductViewModel().setProductId(productId.toString());
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
            FileUtils.deleteFilesInTokopediaFolder(pathToDeleteList);
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