package com.tokopedia.product.manage.item.main.draft.view.presenter;


import com.tokopedia.product.manage.item.main.add.view.presenter.ProductAddPresenter;
import com.tokopedia.product.manage.item.main.draft.view.listener.ProductDraftView;

interface ProductDraftPresenter extends ProductAddPresenter<ProductDraftView> {
    void fetchDraftData(long draftProductId);
}
