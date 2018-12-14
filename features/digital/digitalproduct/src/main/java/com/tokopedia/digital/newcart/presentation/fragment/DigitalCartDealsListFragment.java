package com.tokopedia.digital.newcart.presentation.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment;
import com.tokopedia.digital.R;
import com.tokopedia.digital.cart.di.DigitalCartComponent;
import com.tokopedia.digital.common.router.DigitalModuleRouter;
import com.tokopedia.digital.newcart.di.DaggerDigitalCartDealsComponent;
import com.tokopedia.digital.newcart.di.DigitalCartDealsComponent;
import com.tokopedia.digital.newcart.domain.model.DealProductViewModel;
import com.tokopedia.digital.newcart.presentation.contract.DigitalCartDealsListContract;
import com.tokopedia.digital.newcart.presentation.fragment.adapter.DigitalDealActionListener;
import com.tokopedia.digital.newcart.presentation.fragment.adapter.DigitalDealsAdapterTypeFactory;
import com.tokopedia.digital.newcart.presentation.fragment.adapter.viewHolder.model.DigitalDealEmptyViewModel;
import com.tokopedia.digital.newcart.presentation.fragment.listener.DigitalDealListListener;
import com.tokopedia.digital.newcart.presentation.presenter.DigitalCartDealsListPresenter;

import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 */
public class DigitalCartDealsListFragment extends BaseListFragment<DealProductViewModel, DigitalDealsAdapterTypeFactory>
        implements DigitalCartDealsListContract.View, DigitalDealActionListener,
        DigitalDealListListener {
    private static final String EXTRA_URL = "EXTRA_URL";
    private static final String EXTRA_CATEGORY_NAME = "EXTRA_CATEGORY_NAME";

    private String nextUrl;

    @Inject
    DigitalCartDealsListPresenter presenter;
    @Inject
    DigitalModuleRouter digitalModuleRouter;

    private InteractionListener interactionListener;

    public interface InteractionListener {
        void selectDeal(DealProductViewModel viewModel);
    }

    public static DigitalCartDealsListFragment newInstance(String url, String categoryName) {
        DigitalCartDealsListFragment fragment = new DigitalCartDealsListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_URL, url);
        bundle.putString(EXTRA_CATEGORY_NAME, categoryName);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        nextUrl = getArguments().getString(EXTRA_URL);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void loadData(int page) {
        presenter.getProducts(nextUrl, getCategoryName());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_digital_cart_deals_list, container, false);
        return view;
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
        presenter.attachView(this);
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
        if (interactionListener != null) {
            interactionListener.selectDeal(productViewModel);
        }
        presenter.onBuyButtonClicked(productViewModel);
    }

    @Override
    public void actionCloseButon(DealProductViewModel productViewModel) {

    }

    @Override
    public void actionDetail(DealProductViewModel productViewModel) {
        presenter.onDealDetailClicked(productViewModel);
    }

    @Override
    public void setNextUrl(String nextUrl) {
        this.nextUrl = nextUrl;
    }

    @Override
    public void navigateToDetailPage(DealProductViewModel viewModel) {
        startActivity(digitalModuleRouter.getDealDetailIntent(getActivity(),
                viewModel.getUrl(),
                false,
                false,
                false,
                false)
        );
    }

    @Override
    public String getCategoryName() {
        return getArguments().getString(EXTRA_CATEGORY_NAME);
    }

    public void setInteractionListener(InteractionListener interactionListener) {
        this.interactionListener = interactionListener;
    }

    @Override
    public void notifySelectedDeal() {
        getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        presenter.detachView();
        super.onDestroyView();
    }

    @Override
    protected Visitable getEmptyDataViewModel() {
        return new DigitalDealEmptyViewModel();
    }
}
