package com.tokopedia.product.manage.item.main.add.view.presenter;


import com.tokopedia.product.manage.item.common.util.AddEditPageType;
import com.tokopedia.product.manage.item.main.add.view.listener.ProductAddView;
import com.tokopedia.product.manage.item.main.base.data.model.ProductViewModel;

/**
 * @author sebastianuskh on 4/13/17.
 */

public interface ProductAddPresenter<T extends ProductAddView> {
    void saveDraft(ProductViewModel viewModel, boolean isUploading);
    void getShopInfo(AddEditPageType addEditPageType);
    void fetchProductVariantByCat(long categoryId);
}
