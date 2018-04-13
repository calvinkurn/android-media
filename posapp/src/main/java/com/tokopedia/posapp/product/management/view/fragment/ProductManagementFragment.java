package com.tokopedia.posapp.product.management.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.Toast;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel;
import com.tokopedia.posapp.base.fragment.PosBaseListFragment;
import com.tokopedia.posapp.product.management.di.component.DaggerProductManagementComponent;
import com.tokopedia.posapp.product.management.di.component.ProductManagementComponent;
import com.tokopedia.posapp.product.management.view.ProductManagement;
import com.tokopedia.posapp.product.management.view.adapter.ProductManagementAdapterTypeFactory;
import com.tokopedia.posapp.product.management.view.adapter.ProductManagementTypeFactory;
import com.tokopedia.posapp.product.management.view.viewmodel.ProductHeaderViewModel;
import com.tokopedia.posapp.product.management.view.viewmodel.ProductViewModel;

import java.util.ArrayList;
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

    private LoadingModel loadingModel;
    private ProductHeaderViewModel productHeaderViewModel;
    private TkpdProgressDialog progressDialog;

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
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressDialog = new TkpdProgressDialog(getContext(), TkpdProgressDialog.NORMAL_PROGRESS);
        loadInitialData();
    }

    @Override
    public void loadInitialData() {
        setLoadingView();
        presenter.reload();
    }

    @Override
    public void loadData(int page) {
        presenter.loadMore();
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
        presenter.editStatus(element, isChecked);
    }

    @Override
    public void onItemClicked(Visitable visitable) {
    }

    @Override
    public void showLoadingDialog() {
        if (progressDialog != null) progressDialog.showDialog();
    }

    @Override
    public void hideLoadingDialog() {
        if (progressDialog != null) progressDialog.dismiss();
    }

    @Override
    public void onReloadData(List<Visitable> list) {
        isLoadingInitialData = true;
        list.add(0, getHeaderModel());
        renderList(list, true);
    }

    @Override
    public void onLoadMore(List<Visitable> list) {
        isLoadingInitialData = false;
        renderList(list, list.size() != 0);
    }

    @Override
    public void onErrorLoadData(Throwable e) {
        showGetListError(e);
        e.printStackTrace();
    }

    @Override
    public void onSuccessEditStatus() {
        loadInitialData();
    }

    @Override
    public void onErorEditStatus(String errorMessage) {
        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected boolean isLoadMoreEnabledByDefault() {
        return true;
    }

    @Override
    protected boolean callInitialLoadAutomatically() {
        return false;
    }

    @Nullable
    @Override
    public SwipeRefreshLayout getSwipeRefreshLayout(View view) {
        return super.getSwipeRefreshLayout(view);
    }

    private void setLoadingView() {
        isLoadingInitialData = true;
        List<Visitable> list = new ArrayList<>();
        list.add(getLoadingModel());
        renderList(list);
    }

    private Visitable getHeaderModel() {
        if (productHeaderViewModel == null) productHeaderViewModel = new ProductHeaderViewModel();
        return productHeaderViewModel;
    }

    private Visitable getLoadingModel() {
        if (loadingModel == null) {
            loadingModel = new LoadingModel();
            loadingModel.setFullScreen(true);
        }
        return loadingModel;
    }
}
