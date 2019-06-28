package com.tokopedia.product.manage.item.main.draft.data.source;

import com.tokopedia.product.manage.item.common.util.DraftNotFoundException;
import com.tokopedia.productdraftdatabase.DBMetaData;
import com.tokopedia.productdraftdatabase.ProductDraft;
import com.tokopedia.productdraftdatabase.ProductDraftDao;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author sebastianuskh on 4/13/17.
 */

public class ProductDraftDataManager {

    private ProductDraftDao productDraftDao;

    @Inject
    public ProductDraftDataManager(ProductDraftDao productDraftDao) {
        this.productDraftDao = productDraftDao;
    }

    public Observable<Long> saveDraft(String json, boolean isUploading, String shopId){
        return Observable.fromCallable(() ->{
            ProductDraft productDraft = new ProductDraft();
            productDraft.setData(json);
            productDraft.setUploading(isUploading);
            productDraft.setShopId(shopId);
            productDraft.setVersion(DBMetaData.DB_VERSION);
            return productDraftDao.insertSingle(productDraft);
        });
    }

    public Observable<ProductDraft> getDraft(long productId) {
        return Observable.fromCallable(()->{
            ProductDraft draft = productDraftDao.getSingleDraft(productId);
            if (draft == null){
                throw new DraftNotFoundException();
            }
            return draft;
        });
    }

    public Observable<List<ProductDraft>> getAllDraft(String userId) {
        return Observable.fromCallable(()-> productDraftDao.getMyDrafts(userId));
    }

    public Observable<Long> getAllDraftCount(String userId) {
        return Observable.just(productDraftDao.getMyDraftsCount(userId))
                .map(Integer::longValue);
    }

    public Observable<Boolean> clearAllDraft(String userId){
        return Observable.fromCallable(()-> {
            productDraftDao.deleteMyDrafts(userId);
            return true;
        });
    }

    public Observable<Boolean> deleteDraft(long productId) {
        return Observable.fromCallable(() -> {
            ProductDraft draft = new ProductDraft();
            draft.setId(productId);
            productDraftDao.deleteDraft(draft);
            return true;
        });
    }

    public Observable<Long> updateDraft(long productId, String draftData) {
        return getDraft(productId).map(productDraft -> {
            productDraft.setData(draftData);
            productDraft.setVersion(DBMetaData.DB_VERSION);
            return productDraft;
        }).map(productDraft -> {
            long id = productDraftDao.updateSingle(productDraft);
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
            long id = productDraftDao.updateSingle(productDraft);
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
                           productDraftDao.updateSingle(productDraft);
                           return true;
                        });
                    } else {
                        return Observable.fromCallable(() -> {
                            productDraftDao.updateLoadingForAll(!isUploading, isUploading);
                            return true;
                        });
                    }
                });
    }

    public Observable<Boolean> updateBlankShopIdDraft(String shopId) {
        return Observable.fromCallable(() -> {
            productDraftDao.updateShopIdFromNullShopId(shopId);
            return true;
        });
    }

}
