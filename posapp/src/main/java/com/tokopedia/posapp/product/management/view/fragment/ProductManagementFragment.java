package com.tokopedia.posapp.product.management.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.posapp.base.fragment.PosBaseListFragment;
import com.tokopedia.posapp.product.management.di.component.ProductManagementComponent;
import com.tokopedia.posapp.product.management.di.component.DaggerProductManagementComponent;
import com.tokopedia.posapp.product.management.view.ProductManagement;
import com.tokopedia.posapp.product.management.view.adapter.ProductManagementTypeFactory;
import com.tokopedia.posapp.product.management.view.viewmodel.ProductHeaderViewModel;
import com.tokopedia.posapp.product.management.view.viewmodel.ProductViewModel;
import com.tokopedia.posapp.product.management.view.adapter.ProductManagementAdapterTypeFactory;

import java.util.List;

import javax.inject.Inject;

/**
 * @author okasurya on 3/12/18.
 */

public class ProductManagementFragment
        extends PosBaseListFragment<Visitable, ProductManagementAdapterTypeFactory>
        implements ProductManagementTypeFactory.Listener, ProductManagement.View {

    public static final String TAG = ProductManagementFragment.class.getSimpleName();

    @Inject
    ProductManagement.Presenter presenter;

    public static Fragment newInstance() {
        return new ProductManagementFragment();
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {
        ProductManagementComponent component = DaggerProductManagementComponent
                .builder()
                .baseAppComponent(((BaseMainApplication) getActivity().getApplication()).getBaseAppComponent())
                .build();
        component.inject(this);
        presenter.attachView(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.reload();
    }

    @Override
    public void loadData(int page) {

    }

    @Override
    protected ProductManagementAdapterTypeFactory getAdapterTypeFactory() {
        return new ProductManagementAdapterTypeFactory(this);
    }

    @Override
    public void onClickEditProduct(View v, ProductViewModel productViewModel) {
        EditProductDialogFragment.show(getActivity().getSupportFragmentManager(), productViewModel);
    }

    @Override
    public void onShowProductCheckedChange(ProductViewModel element, boolean isChecked) {

    }

    @Override
    public void onItemClicked(Visitable visitable) {
    }

    @Override
    public void onReloadData(List<Visitable> list) {
        loadInitialData();
        list.add(0, new ProductHeaderViewModel());
        renderList(list);
    }

    @Override
    public void onLoadMore(List<Visitable> list) {
        renderList(list);
    }
}
