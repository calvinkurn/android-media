package com.tokopedia.product.edit.view.presenter;


public interface ProductEditPresenter extends ProductAddPresenter<ProductEditView> {
    void fetchEditProductData(String productId);
}
