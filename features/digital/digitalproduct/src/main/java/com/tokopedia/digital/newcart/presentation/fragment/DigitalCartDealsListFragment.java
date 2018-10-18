package com.tokopedia.digital.newcart.presentation.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment;
import com.tokopedia.digital.R;
import com.tokopedia.digital.cart.di.DigitalCartComponent;
import com.tokopedia.digital.newcart.di.DaggerDigitalCartDealsComponent;
import com.tokopedia.digital.newcart.di.DigitalCartDealsComponent;
import com.tokopedia.digital.newcart.domain.model.DealProductViewModel;
import com.tokopedia.digital.newcart.presentation.contract.DigitalCartDealsListContract;
import com.tokopedia.digital.newcart.presentation.fragment.adapter.DigitalDealActionListener;
import com.tokopedia.digital.newcart.presentation.fragment.adapter.DigitalDealsAdapterTypeFactory;
import com.tokopedia.digital.newcart.presentation.presenter.DigitalCartDealsListPresenter;

import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 */
public class DigitalCartDealsListFragment extends BaseListFragment<DealProductViewModel, DigitalDealsAdapterTypeFactory> implements DigitalCartDealsListContract.View, DigitalDealActionListener {
    private static final String EXTRA_URL = "EXTRA_URL";

    private String nextUrl;

    @Inject
    DigitalCartDealsListPresenter presenter;

    public static DigitalCartDealsListFragment newInstance(String url) {
        DigitalCartDealsListFragment fragment = new DigitalCartDealsListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_URL, url);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        nextUrl = getArguments().getString(EXTRA_URL);
        super.onCreate(savedInstanceState);
    }

    public DigitalCartDealsListFragment() {
        // Required empty public constructor
    }

    @Override
    public void loadData(int page) {
        presenter.getProducts(nextUrl);
    }

    @Override
    protected DigitalDealsAdapterTypeFactory getAdapterTypeFactory() {
        return new DigitalDealsAdapterTypeFactory(this);
    }

    @Override
    protected void initInjector() {
        DigitalCartComponent digitalCartComponent = getComponent(DigitalCartComponent.class);
        DigitalCartDealsComponent component = DaggerDigitalCartDealsComponent.builder()
                .digitalCartComponent(digitalCartComponent)
                .build();
        component.inject(this);
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    public void onItemClicked(DealProductViewModel productViewModel) {

    }

    @Override
    public void actionBuyButton(DealProductViewModel productViewModel) {

    }

    @Override
    public void actionCloseButon(DealProductViewModel productViewModel) {

    }

    @Override
    public void actionDetail(DealProductViewModel productViewModel) {

    }

    @Override
    public void setNextUrl(String nextUrl) {
        this.nextUrl = nextUrl;
    }
}
