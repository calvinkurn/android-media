package com.tokopedia.product.manage.item.main.draft.domain;

import com.tokopedia.product.manage.item.main.base.data.model.ProductViewModel;
import com.tokopedia.product.manage.item.main.draft.data.model.ProductDraftViewModel;

import java.util.List;

import rx.Observable;

/**
 * @author sebastianuskh on 4/13/17.
 */

public interface ProductDraftRepository {
    Observable<Long> saveDraft(ProductViewModel domainModel, boolean isUploading);
    Observable<ProductViewModel> getDraft(long productId);

    Observable<Boolean> clearAllDraft();

    Observable<Boolean> deleteDraft(long productId);

    Observable<List<ProductDraftViewModel>> getAllDraft();

    Observable<Long> getAllDraftCount();

    Observable<Long> updateDraftToUpload(long draftProductIdToUpdate, ProductViewModel domainModel, boolean isUploading);

    Observable<Boolean> updateuploadingStatusDraft(long productId, boolean isUploading);
}
