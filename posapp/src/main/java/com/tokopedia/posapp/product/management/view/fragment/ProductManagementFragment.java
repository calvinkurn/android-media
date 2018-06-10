package com.tokopedia.posapp.product.management.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel;
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment;
import com.tokopedia.posapp.PosApplication;
import com.tokopedia.posapp.R;
import com.tokopedia.posapp.base.domain.model.DataStatus;
import com.tokopedia.posapp.base.fragment.PosBaseListFragment;
import com.tokopedia.posapp.product.common.ProductConstant;
import com.tokopedia.posapp.product.management.di.component.DaggerProductManagementComponent;
import com.tokopedia.posapp.product.management.di.component.ProductManagementComponent;
import com.tokopedia.posapp.product.management.view.ProductManagement;
import com.tokopedia.posapp.product.management.view.adapter.ProductManagementAdapterTypeFactory;
import com.tokopedia.posapp.product.management.view.adapter.ProductManagementTypeFactory;
import com.tokopedia.posapp.product.management.view.listener.EditProductListener;
import com.tokopedia.posapp.product.management.view.viewmodel.ProductHeaderViewModel;
import com.tokopedia.posapp.product.management.view.viewmodel.ProductViewModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

/**
 * @author okasurya on 3/12/18.
 */

public class ProductManagementFragment
        extends BaseListFragment<Visitable, ProductManagementAdapterTypeFactory>
        implements ProductManagementTypeFactory.Listener, ProductManagement.View{

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
                .posAppComponent(((PosApplication) getActivity().getApplication()).getPosAppComponent())
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pos_base_list, container, false);
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
    public void onClickEditProduct(View v, ProductViewModel productViewModel, int position) {
        EditProductDialogFragment.show(getActivity().getSupportFragmentManager(), productViewModel, position);
    }

    @Override
    public void onShowProductCheckedChange(ProductViewModel element, boolean isChecked, int position) {
        presenter.editStatus(element, isChecked, position);
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
    public void onSuccessEditStatus(int position, ProductViewModel productViewModel) {
        if (productViewModel.getStatus() == ProductConstant.Status.LOCAL_PRICE_SHOW
                || productViewModel.getStatus() == ProductConstant.Status.ONLINE_PRICE_SHOW) {
            productViewModel.setStatus(ProductConstant.Status.LOCAL_PRICE_HIDDEN);
        } else {
            productViewModel.setStatus(ProductConstant.Status.LOCAL_PRICE_SHOW);
        }
        getAdapter().setElement(position, productViewModel);
        Toast.makeText(getContext(), getString(R.string.editstatus_message_success), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onErorEditStatus(int position) {
        Toast.makeText(getContext(), getString(R.string.error_password_unknown), Toast.LENGTH_SHORT).show();
        getAdapter().notifyDataSetChanged();
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
        return view.findViewById(R.id.swipe_refresh_layout);
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

    @Override
    public LoadingModel getLoadingModel() {
        if (loadingModel == null) {
            loadingModel = new LoadingModel();
        }
        return loadingModel;
    }

    public void onSucessUpdateLocalPrice(ProductViewModel productViewModel, int position) {
        getAdapter().setElement(position, productViewModel);
    }
}
