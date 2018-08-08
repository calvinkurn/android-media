package com.tokopedia.product.manage.item.view.presenter;


interface ProductDraftPresenter extends ProductAddPresenter<ProductDraftView> {
    void fetchDraftData(long draftProductId);
}
