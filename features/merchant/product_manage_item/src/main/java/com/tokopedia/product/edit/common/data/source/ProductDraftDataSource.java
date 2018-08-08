package com.tokopedia.product.edit.common.data.source;

import com.tokopedia.productdraftdatabase.ProductDraftDataBase;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author sebastianuskh on 4/13/17.
 */

public class ProductDraftDataSource {
    private final ProductDraftDataManager productDraftDataManager;
    private boolean hasUpdateBlankShopId = false;

    @Inject
    public ProductDraftDataSource(ProductDraftDataManager productDraftDataManager) {
        this.productDraftDataManager = productDraftDataManager;
    }

    public Observable<Long> saveDraft(String productDraftDataBase, boolean isUploading, String shopId){
        return productDraftDataManager.saveDraft(productDraftDataBase, isUploading, shopId);
    }

    public Observable<ProductDraftDataBase> getDraft(long productId) {
        return productDraftDataManager.getDraft(productId);
    }

    public Observable<List<ProductDraftDataBase>> getAllDraft(String shopId) {
        updateBlankShopId(shopId);
        return productDraftDataManager.getAllDraft(shopId);
    }

    private void updateBlankShopId(String shopId){
        if (!hasUpdateBlankShopId) {
            productDraftDataManager.updateBlankShopIdDraft(shopId);
            hasUpdateBlankShopId = true;
        }
    }

    public Observable<Long> getAllDraftCount(String shopId) {
        updateBlankShopId(shopId);
        return productDraftDataManager.getAllDraftCount(shopId);
    }

    public Observable<Boolean> clearAllDraft(String shopId) {
        return productDraftDataManager.clearAllDraft(shopId);
    }

    public Observable<Boolean> deleteDraft(long productId) {
        return productDraftDataManager.deleteDeraft(productId);
    }

    public Observable<Long> updateDraft(long productId, String draftDataBase) {
        return productDraftDataManager.updateDraft(productId, draftDataBase);
    }

    public Observable<Long> updateDraft(long draftProductId, String draftDataBaseJson, boolean isUploading) {
        return productDraftDataManager.updateDraft(draftProductId, draftDataBaseJson, isUploading);
    }

    public Observable<Boolean> updateUploadingStatusDraft(long productId, boolean draftDataBase) {
        return productDraftDataManager.updateUploadingStatusDraft(productId, draftDataBase);
    }
}
