package com.tokopedia.posapp.product.management.view.subscriber;

import com.tokopedia.design.utils.CurrencyFormatHelper;
import com.tokopedia.posapp.base.domain.model.DataStatus;
import com.tokopedia.posapp.product.management.ProductManagementConstant;
import com.tokopedia.posapp.product.management.view.EditProduct;
import com.tokopedia.posapp.product.management.view.viewmodel.ProductViewModel;

import rx.Subscriber;

/**
 * @author okasurya on 4/12/18.
 */

public class EditProductSubscriber extends Subscriber<DataStatus> {
    private EditProduct.View view;
    private ProductViewModel productViewModel;
    private int position;
    private String price;

    public EditProductSubscriber(EditProduct.View view, String price, ProductViewModel productViewModel, int position) {
        this.view = view;
        this.position = position;
        this.productViewModel = productViewModel;
        this.price = price;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        view.onErrorSave(ProductManagementConstant.Message.DEFAULT_ERROR_MESSAGE);
    }

    @Override
    public void onNext(DataStatus dataStatus) {
        productViewModel.setOutletPrice(price);
        productViewModel.setOutletPriceUnformatted(CurrencyFormatHelper.convertRupiahToInt(price));
        if(dataStatus.isOk()) view.onSuccessSave(productViewModel, position);
        else view.onErrorSave(dataStatus.getMessage());
    }
}
