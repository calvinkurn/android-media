package com.tokopedia.product.edit.view.presenter;


interface ProductDraftPresenter extends ProductAddPresenter<ProductDraftView> {
    void fetchDraftData(long draftProductId);
}
