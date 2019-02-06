package com.tokopedia.gm.subscribe.view.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.core.customadapter.RetryDataBinder;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.rxjava.RxUtils;
import com.tokopedia.gm.subscribe.R;
import com.tokopedia.gm.subscribe.di.component.DaggerGmSubscribeComponent;
import com.tokopedia.gm.subscribe.di.component.GmSubscribeComponent;
import com.tokopedia.gm.subscribe.di.module.GmSubscribeModule;
import com.tokopedia.gm.subscribe.view.presenter.GmProductPresenterImpl;
import com.tokopedia.gm.subscribe.view.recyclerview.GmProductAdapter;
import com.tokopedia.gm.subscribe.view.recyclerview.GmProductAdapterCallback;
import com.tokopedia.gm.subscribe.view.viewmodel.GmProductViewModel;

import java.util.List;

import rx.subscriptions.CompositeSubscription;

/**
 * @author sebastianuskh on 11/23/16.
 */

public abstract class GmProductFragment
        extends BasePresenterFragment<GmProductPresenterImpl>
        implements GmProductView, GmProductAdapterCallback {

    public static final String TAG = "GMProductFragment";
    public static final String DEFAULT_SELECTED_PRODUCT = "DEFAULT_SELECTED_PRODUCT";
    public static final int UNDEFINED_DEFAULT_SELECTED = -1;
    public static final String RETURN_TYPE = "RETURN_TYPE";
    public static final String SELECTED_PRODUCT = "SELECTED_PRODUCT";
    public static final String SELECTED_PRODUCT_NAME = "SELECTED_PRODUCT_NAME";
    public static final String SELECTED_PRODUCT_PRICE = "SELECTED_PRODUCT_PRICE";
    public static final String STRING_BUTTON_SELECT = "STRING_BUTTON_SELECT";
    public RecyclerView recyclerView;

    public Button buttonSelectProduct;
    private CompositeSubscription subscriber;
    private String stringButton;
    public int returnType;
    public Integer currentSelectedProductId = UNDEFINED_DEFAULT_SELECTED;
    public String currentSelectedProductName;
    public String currentSelectedProductPrice;
    private GmProductAdapter adapter;
    public GmProductFragmentListener listener;
    private GmSubscribeComponent component;

    public static GmProductFragment createFragment(GmProductFragment fragment,
                                                   String buttonString,
                                                   int defaultSelected,
                                                   int returnType) {
        Bundle args = new Bundle();
        args.putString(STRING_BUTTON_SELECT, buttonString);
        args.putInt(RETURN_TYPE, returnType);
        args.putInt(DEFAULT_SELECTED_PRODUCT, defaultSelected);
        fragment.setArguments(args);
        return fragment;
    }

    public void confirmSelection() {
        if (currentSelectedProductId != UNDEFINED_DEFAULT_SELECTED) {
            UnifyTracking.eventClickSubscribeGoldMerchant(getActivity(), adapter.getProductSelection());
            listener.finishProductSelection(currentSelectedProductId, returnType);
        } else {
            NetworkErrorHelper.showSnackbar(getActivity(), getString(R.string.gm_subscribe_no_product_selected));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        listener.setDrawer(false);
        listener.changeActionBarTitle(getTitle());
    }

    @Override
    protected boolean isRetainInstance() {
        return false;
    }

    @Override
    protected void onFirstTimeLaunched() {

    }

    @Override
    public void onSaveState(Bundle bundle) {
        bundle.putInt(SELECTED_PRODUCT, currentSelectedProductId);
        bundle.putString(SELECTED_PRODUCT_NAME, currentSelectedProductName);
        bundle.putString(SELECTED_PRODUCT_PRICE, currentSelectedProductPrice);
    }

    @Override
    public void onRestoreState(Bundle bundle) {
        currentSelectedProductId = bundle.getInt(SELECTED_PRODUCT, UNDEFINED_DEFAULT_SELECTED);
        currentSelectedProductName = bundle.getString(SELECTED_PRODUCT_NAME);
        currentSelectedProductPrice = bundle.getString(SELECTED_PRODUCT_PRICE);
    }

    @Override
    protected boolean getOptionsMenuEnable() {
        return false;
    }

    @Override
    protected void initialPresenter() {
        subscriber = RxUtils.getNewCompositeSubIfUnsubscribed(subscriber);
        presenter = DaggerGmSubscribeComponent
                .builder()
                .appComponent(((HasComponent<AppComponent>)getActivity()).getComponent())
                .gmSubscribeModule(new GmSubscribeModule())
                .build()
                .getProductPresenter();
    }

    @Override
    protected void initialListener(Activity activity) {
        if (activity instanceof GmProductFragmentListener) {
            listener = (GmProductFragmentListener) activity;
        } else {
            throw new RuntimeException("Please implement GMProductFragmentListener in the Activity");
        }
    }

    @Override
    protected void setupArguments(Bundle bundle) {
        stringButton = bundle.getString(STRING_BUTTON_SELECT);
        returnType = bundle.getInt(RETURN_TYPE);
        if (currentSelectedProductId == UNDEFINED_DEFAULT_SELECTED) {
            currentSelectedProductId = bundle.getInt(DEFAULT_SELECTED_PRODUCT, UNDEFINED_DEFAULT_SELECTED);
        }
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_gm_subscribe_product;
    }

    @Override
    protected void initView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_package_chooser);
        buttonSelectProduct = (Button) view.findViewById(R.id.button_select_product);
        buttonSelectProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmSelection();
            }
        });
        presenter.attachView(this);
        buttonSelectProduct.setText(stringButton);
        adapter = new GmProductAdapter(this);
        recyclerView.setAdapter(adapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

    }

    @Override
    public void onDestroyView() {
        presenter.detachView();
        super.onDestroyView();
    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initialVar() {

    }

    @Override
    protected void setActionVar() {
        getPackage();
    }

    @Override
    public void onPause() {
        super.onPause();
        RxUtils.unsubscribeIfNotNull(subscriber);
    }

    @Override
    public void renderProductList(List<GmProductViewModel> gmProductDomainModels) {
        if (getSelectedProductId().equals(UNDEFINED_DEFAULT_SELECTED)) {
            int selected = findBestDeal(gmProductDomainModels);
            selectedProductId(selected);
        }
        adapter.addItem(gmProductDomainModels);
    }

    public int findBestDeal(List<GmProductViewModel> gmProductDomainModels) {
        int selected = 0;
        for (int i = 0; i < gmProductDomainModels.size(); i++) {
            if (gmProductDomainModels.get(i).isBestDeal()) {
                selected = Integer.parseInt(gmProductDomainModels.get(i).getProductId());
                break;
            }
        }
        return selected;
    }

    @Override
    public void errorGetProductList() {
        adapter.setOnRetryListenerRV(new RetryDataBinder.OnRetryListener() {
            @Override
            public void onRetryCliked() {
                getPackage();
            }
        });
        adapter.showRetryFull(true);
    }

    @Override
    public void selectedProductId(Integer selectedProductId) {
        currentSelectedProductId = selectedProductId;
    }

    @Override
    public void selectedProductName(String selectedProductName) {
        currentSelectedProductName = selectedProductName;
    }

    @Override
    public void selectedProductPrice(String selectedProductPrice) {
        currentSelectedProductPrice = selectedProductPrice;
    }

    @Override
    public void showProgressDialog() {
        adapter.showLoadingFull(true);
    }

    @Override
    public void dismissProgressDialog() {
        adapter.showLoadingFull(false);
    }

    @Override
    public Integer getSelectedProductId() {
        return currentSelectedProductId;
    }

    @Override
    public void clearPackage() {
        adapter.clearDatas();
    }

    @Override
    public void setVisibilitySelectButton(boolean isView) {
        buttonSelectProduct.setVisibility(isView ? View.VISIBLE : View.GONE);
    }

    protected abstract String getTitle();

    protected abstract void getPackage();

}
