package com.tokopedia.posapp.product.management.view.fragment;

import android.support.v4.app.Fragment;
import android.view.View;

import com.tokopedia.posapp.base.fragment.PosBaseListFragment;
import com.tokopedia.posapp.product.common.view.viewmodel.ProductViewModel;
import com.tokopedia.posapp.product.management.view.adapter.ProductManagementAdapterTypeFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author okasurya on 3/12/18.
 */

public class ProductManagementFragment extends PosBaseListFragment<ProductViewModel, ProductManagementAdapterTypeFactory> implements ProductManagementAdapterTypeFactory.Listener {
    public static final String TAG = ProductManagementFragment.class.getSimpleName();

    public static Fragment newInstance() {
        return new ProductManagementFragment();
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {

    }

    @Override
    public void onItemClicked(ProductViewModel productViewModel) {

    }

    @Override
    public void loadData(int page) {
        List<ProductViewModel> list = new ArrayList<>();
        list.add(mockProduct(1));
        list.add(mockProduct(2));
        list.add(mockProduct(3));
        list.add(mockProduct(4));
        list.add(mockProduct(5));
        renderList(list);
    }

    private ProductViewModel mockProduct(int i) {
        ProductViewModel pvm = new ProductViewModel();
        pvm.setId(Integer.toString(i));
        pvm.setImageUrl("https://dummyimage.com/64x64/000/fff");
        pvm.setName("Product " + i);
        pvm.setOnlinePrice(1000000);
        pvm.setOutletPrice(1000000);
        pvm.setShown(true);
        return pvm;
    }

    @Override
    protected ProductManagementAdapterTypeFactory getAdapterTypeFactory() {
        return new ProductManagementAdapterTypeFactory(this);
    }

    @Override
    public void onClickEditProduct(View v, ProductViewModel productViewModel) {

    }

    @Override
    public void onShowProductCheckedChange(ProductViewModel element, boolean isChecked) {

    }
}
