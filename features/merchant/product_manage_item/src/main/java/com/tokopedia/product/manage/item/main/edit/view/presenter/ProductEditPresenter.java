package com.tokopedia.product.manage.item.main.edit.view.presenter;


import com.tokopedia.product.manage.item.main.add.view.presenter.ProductAddPresenter;
import com.tokopedia.product.manage.item.main.edit.view.listener.ProductEditView;

public interface ProductEditPresenter extends ProductAddPresenter<ProductEditView> {
    void fetchEditProductData(String productId);
}
