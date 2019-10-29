package com.tokopedia.product.manage.item.main.draft.data.repository;

import android.content.Context;

import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.product.manage.item.main.base.data.model.ProductViewModel;
import com.tokopedia.product.manage.item.main.draft.data.mapper.ProductDraftListMapper;
import com.tokopedia.product.manage.item.main.draft.data.mapper.ProductDraftMapper;
import com.tokopedia.product.manage.item.main.draft.data.model.ProductDraftViewModel;
import com.tokopedia.product.manage.item.main.draft.data.source.ProductDraftDataSource;
import com.tokopedia.product.manage.item.main.draft.domain.ProductDraftRepository;

import java.util.List;

import rx.Observable;

/**
 * @author sebastianuskh on 4/13/17.
 */

public class ProductDraftRepositoryImpl implements ProductDraftRepository {
    private final ProductDraftDataSource productDraftDataSource;
    private Context context;

    public ProductDraftRepositoryImpl(ProductDraftDataSource productDraftDataSource, Context context) {
        this.productDraftDataSource = productDraftDataSource;
        this.context = context;
    }


    @Override
    public Observable<Long> saveDraft(ProductViewModel domainModel, boolean isUploading) {
        String productDraftJson = ProductDraftMapper.mapFromDomain(domainModel);
        String shopId = SessionHandler.getShopID(context);
        return productDraftDataSource.saveDraft(productDraftJson, isUploading, shopId);
    }

    @Override
    public Observable<ProductViewModel> getDraft(long productId) {
        return productDraftDataSource.getDraft(productId)
                .map(new ProductDraftMapper());
    }

    @Override
    public Observable<List<ProductDraftViewModel>> getAllDraft() {
        String shopId = SessionHandler.getShopID(context);
        return productDraftDataSource.getAllDraft(shopId)
                .flatMap(Observable::from)
                .map(productDraftDataBase -> {
                    final long id = productDraftDataBase.getId();
                    return Observable.just(productDraftDataBase)
                            .map(new ProductDraftMapper())
                            .map(productViewModel -> ProductDraftListMapper.mapDomainToView(productViewModel, id))
                            .toBlocking().first();
                })
                .toSortedList((productViewModel, productViewModel2) ->
                        (int) (productViewModel2.getProductDraftId() - productViewModel.getProductDraftId()));
    }

    @Override
    public Observable<Long> getAllDraftCount() {
        String shopId = SessionHandler.getShopID(context);
        return productDraftDataSource.getAllDraftCount(shopId);
    }

    @Override
    public Observable<Boolean> clearAllDraft() {
        String shopID = SessionHandler.getShopID(context);
        return productDraftDataSource.clearAllDraft(shopID);
    }

    @Override
    public Observable<Boolean> deleteDraft(long productId) {
        return productDraftDataSource.deleteDraft(productId);
    }

    @Override
    public Observable<Long> updateDraftToUpload(long draftProductIdToUpdate, ProductViewModel domainModel,
                                                boolean isUploading) {
        String productDraftJson = ProductDraftMapper.mapFromDomain(domainModel);
        return productDraftDataSource.updateDraft(draftProductIdToUpdate, productDraftJson, isUploading);
    }

    @Override
    public Observable<Boolean> updateuploadingStatusDraft(long productId, boolean isUploading) {
        return productDraftDataSource.updateUploadingStatusDraft(productId, isUploading);
    }
}
