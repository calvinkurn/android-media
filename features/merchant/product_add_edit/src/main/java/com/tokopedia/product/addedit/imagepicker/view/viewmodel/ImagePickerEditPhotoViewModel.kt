package com.tokopedia.product.addedit.imagepicker.view.viewmodel

import com.tokopedia.product.addedit.imagepicker.domain.interactor.SaveProductDraftUseCase
import com.tokopedia.product.addedit.imagepicker.domain.interactor.SaveProductDraftUseCase.Companion.createRequestParams
import com.tokopedia.product.manage.common.draft.data.model.ProductDraft
import javax.inject.Inject

class ImagePickerEditPhotoViewModel @Inject constructor(private val saveProductDraftUseCase: SaveProductDraftUseCase) {

    fun saveProductDraft(productDraft: ProductDraft, draftId: Long, isUploading: Boolean) {
        saveProductDraftUseCase.createObservable(createRequestParams(productDraft, draftId, isUploading))
    }
}