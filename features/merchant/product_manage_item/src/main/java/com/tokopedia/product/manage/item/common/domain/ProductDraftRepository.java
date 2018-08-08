package com.tokopedia.product.edit.common.domain;

import com.tokopedia.product.edit.common.model.ProductDraftViewModel;
import com.tokopedia.product.edit.common.model.edit.ProductViewModel;
import com.tokopedia.product.edit.common.model.ProductDraftViewModel;
import com.tokopedia.product.edit.common.model.edit.ProductViewModel;

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
