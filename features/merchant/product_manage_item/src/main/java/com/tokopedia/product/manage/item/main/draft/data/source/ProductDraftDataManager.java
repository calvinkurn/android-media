package com.tokopedia.product.manage.item.main.draft.data.source;

import com.tokopedia.product.manage.item.common.util.DraftNotFoundException;
import com.tokopedia.productdraftdatabase.DBMetaData;
import com.tokopedia.productdraftdatabase.ProductDraft;
import com.tokopedia.productdraftdatabase.ProductDraftDB;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author sebastianuskh on 4/13/17.
 */

public class ProductDraftDataManager {

    private ProductDraftDB productDraftDb;

    @Inject
    public ProductDraftDataManager(ProductDraftDB productDraftDb) {
        this.productDraftDb = productDraftDb;
    }

    public Observable<Long> saveDraft(String json, boolean isUploading, String shopId){
        return Observable.fromCallable(() ->{
            ProductDraft productDraft = new ProductDraft();
            productDraft.setData(json);
            productDraft.setUploading(isUploading);
            productDraft.setShopId(shopId);
            productDraft.setVersion(DBMetaData.DB_VERSION);
            return productDraftDb.getProductDraftDao().insertSingle(productDraft);
        });
    }

    public Observable<ProductDraft> getDraft(long productId) {
        return Observable.fromCallable(()->{
            ProductDraft draft = productDraftDb.getProductDraftDao().getSingleDraft(productId);
            if (draft == null){
                throw new DraftNotFoundException();
            }
            return draft;
        });
    }

    public Observable<List<ProductDraft>> getAllDraft(String userId) {
        return Observable.fromCallable(()-> productDraftDb.getProductDraftDao().getMyDrafts(userId));
    }

    public Observable<Long> getAllDraftCount(String userId) {
        return Observable.fromCallable(()-> productDraftDb.getProductDraftDao().getMyDraftsCount(userId))
                .map(Integer::longValue);
    }

    public Observable<Boolean> clearAllDraft(String userId){
        return Observable.fromCallable(()-> {
            productDraftDb.getProductDraftDao().deleteMyDrafts(userId);
            return true;
        });
    }

    public Observable<Boolean> deleteDeraft(long productId) {
        return Observable.fromCallable(() -> {
            ProductDraft draft = new ProductDraft();
            draft.setId(productId);
            productDraftDb.getProductDraftDao().deleteDraft(draft);
            return true;
        });
    }

    public Observable<Long> updateDraft(long productId, String draftData) {
        return getDraft(productId).map(productDraft -> {
            productDraft.setData(draftData);
            productDraft.setVersion(DBMetaData.DB_VERSION);
            return productDraft;
        }).map(productDraft -> {
            long id = productDraftDb.getProductDraftDao().updateSingle(productDraft);
            if (id < 1) throw new DraftNotFoundException();
            return id;
        });
    }

    public Observable<Long> updateDraft(long draftProductId, String draftData, boolean isUploading) {
        return getDraft(draftProductId).map(productDraft -> {
            productDraft.setData(draftData);
            productDraft.setUploading(isUploading);
            productDraft.setVersion(DBMetaData.DB_VERSION);
            return productDraft;
        }).map(productDraft -> {
            long id = productDraftDb.getProductDraftDao().updateSingle(productDraft);
            if (id < 1) throw new DraftNotFoundException();
            return id;
        });
    }

    public Observable<Boolean> updateUploadingStatusDraft(long productId, boolean isUploading) {
        return Observable.just(productId)
                .switchMap(pid -> {
                    if (pid > 0){
                        return getDraft(productId).map(productDraft -> {
                           if (productDraft == null)
                               throw new DraftNotFoundException();
                           productDraft.setUploading(isUploading);
                           productDraftDb.getProductDraftDao().updateSingle(productDraft);
                           return true;
                        });
                    } else {
                        return Observable.fromCallable(() -> {
                            productDraftDb.getProductDraftDao().updateLoadingForAll(!isUploading, isUploading);
                            return true;
                        });
                    }
                });
    }

    public Observable<Boolean> updateBlankShopIdDraft(String shopId) {
        return Observable.fromCallable(() -> {
            productDraftDb.getProductDraftDao().updateShopIdFromNullShopId(shopId);
            return true;
        });
    }

}
