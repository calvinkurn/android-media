package com.tokopedia.posapp.product.management.view.presenter;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.posapp.product.management.view.ProductManagement;
import com.tokopedia.posapp.product.management.view.viewmodel.ProductViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author okasurya on 3/14/18.
 */

public class ProductManagementPresenter implements ProductManagement.Presenter {
    private ProductManagement.View view;

    @Override
    public void attachView(ProductManagement.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    @Override
    public void reload() {
        view.onReloadData(getMockList());
    }

    private List<Visitable> getMockList() {
        List<Visitable> list = new ArrayList<>();
        list.add(mock(1));
        list.add(mock(2));
        list.add(mock(3));
        list.add(mock(4));
        list.add(mock(5));
        return list;
    }

    private ProductViewModel mock(int i) {
        ProductViewModel pvm = new ProductViewModel();
        pvm.setId(Integer.toString(i));
        pvm.setImageUrl("https://dummyimage.com/64x64/000/fff");
        pvm.setName("Samsung Galaxy J8 2017 Gold Garansi 1 Tahun TAM asdfsd " + i);
        pvm.setOnlinePrice(1000000);
        pvm.setOutletPrice(1000000);
        pvm.setShown(true);
        return pvm;
    }

    @Override
    public void loadMore() {

    }
}
