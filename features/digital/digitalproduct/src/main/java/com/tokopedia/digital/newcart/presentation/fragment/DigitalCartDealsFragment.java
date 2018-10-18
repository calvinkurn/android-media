package com.tokopedia.digital.newcart.presentation.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData;
import com.tokopedia.common_digital.cart.view.model.cart.CartDigitalInfoData;
import com.tokopedia.digital.R;
import com.tokopedia.digital.cart.di.DigitalCartComponent;
import com.tokopedia.digital.newcart.di.DaggerDigitalCartDealsComponent;
import com.tokopedia.digital.newcart.di.DigitalCartDealsComponent;
import com.tokopedia.digital.newcart.domain.model.DealCategoryViewModel;
import com.tokopedia.digital.newcart.presentation.contract.DigitalCartDealsContract;
import com.tokopedia.digital.newcart.presentation.fragment.adapter.DigitalDealsPagerAdapter;
import com.tokopedia.digital.newcart.presentation.presenter.DigitalCartDealsPresenter;

import java.util.List;

import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 */
public class DigitalCartDealsFragment extends BaseDaggerFragment implements DigitalCartDealsContract.View {
    private static final String EXTRA_PASS_DATA = "EXTRA_PASS_DATA";
    private static final String EXTRA_CART_DATA = "EXTRA_CART_DATA";

    private ProgressBar progressBar;
    private TabLayout dealTabLayout;
    private ViewPager dealViewPager;

    private CartDigitalInfoData cartInfoData;
    private DigitalCheckoutPassData cartPassData;

    public DigitalCartDealsFragment newInstance(DigitalCheckoutPassData passData, CartDigitalInfoData cartDigitalInfoData) {
        DigitalCartDealsFragment fragment = new DigitalCartDealsFragment();
        Bundle args = new Bundle();
        args.putParcelable(EXTRA_CART_DATA, cartDigitalInfoData);
        args.putParcelable(EXTRA_PASS_DATA, passData);
        fragment.setArguments(args);
        return fragment;
    }


    @Inject
    DigitalCartDealsPresenter presenter;


    public DigitalCartDealsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        cartInfoData = getArguments().getParcelable(EXTRA_CART_DATA);
        cartPassData = getArguments().getParcelable(EXTRA_PASS_DATA);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_digital_cart_deals, container, false);
        progressBar = view.findViewById(R.id.progress_bar);
        dealTabLayout = view.findViewById(R.id.tab_deal);
        dealViewPager = view.findViewById(R.id.pager_deals);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.attachView(this);
        presenter.onViewCreated();

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
    public void showGetCategoriesLoading() {

    }

    @Override
    public void hideDealsPage() {

    }

    @Override
    public void showGetCategoriesError(String message) {

    }

    @Override
    public void hideGetCategoriesLoading() {

    }

    @Override
    public void renderDealsTab(List<DealCategoryViewModel> dealCategoryViewModels) {
        dealTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        dealViewPager.setOffscreenPageLimit(dealCategoryViewModels.size());
        dealViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(dealTabLayout));
        dealViewPager.setAdapter(new DigitalDealsPagerAdapter(dealCategoryViewModels, getChildFragmentManager()));
    }

    @Override
    public void showDealsPage() {

    }
}
