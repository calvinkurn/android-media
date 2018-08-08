package com.tokopedia.product.manage.item.view.presenter;


public interface ProductEditPresenter extends ProductAddPresenter<ProductEditView> {
    void fetchEditProductData(String productId);
}
