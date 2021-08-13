package com.tokopedia.digital.newcart.presentation.fragment;


import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.ColorUtils;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData;
import com.tokopedia.digital.R;
import com.tokopedia.digital.newcart.di.DaggerDigitalCartDealsComponent;
import com.tokopedia.digital.newcart.di.DigitalCartComponent;
import com.tokopedia.digital.newcart.di.DigitalCartDealsComponent;
import com.tokopedia.digital.newcart.domain.model.DealCategoryViewModel;
import com.tokopedia.digital.newcart.domain.model.DealProductViewModel;
import com.tokopedia.digital.newcart.presentation.contract.DigitalCartDealsContract;
import com.tokopedia.digital.newcart.presentation.fragment.adapter.DigitalDealsPagerAdapter;
import com.tokopedia.digital.newcart.presentation.fragment.listener.DigitalDealListListener;
import com.tokopedia.digital.newcart.presentation.fragment.listener.DigitalDealNatigationListener;
import com.tokopedia.digital.newcart.presentation.model.cart.CartDigitalInfoData;
import com.tokopedia.digital.newcart.presentation.presenter.DigitalCartDealsPresenter;
import com.tokopedia.unifycomponents.Toaster;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 */
public class DigitalCartDealsFragment extends BaseDaggerFragment implements DigitalCartDealsContract.View,
        DigitalCartDealsListFragment.InteractionListener, DigitalDealCheckoutFragment.InteractionListener,
        DigitalDealNatigationListener {
    private static final String EXTRA_PASS_DATA = "EXTRA_PASS_DATA";
    private static final String EXTRA_CART_DATA = "EXTRA_CART_DATA";
    private static final String TAG_DIGITAL_CHECKOUT = "digital_deals_checkout_fragment";

    private static final long DEFAULT_MAX_DIM = 200;
    private static final long DEFAULT_DELAY_ONBOARD = 800;

    private ProgressBar progressBar;
    private TabLayout dealTabLayout;
    private ViewPager dealViewPager;
    private LinearLayout dealContainer;
    private FrameLayout checkoutContainer;
    private View checkoutDim;
    private List<DealProductViewModel> selectedProducts;
    private Map<DealProductViewModel, Integer> selectedDealsMap;

    private CartDigitalInfoData cartInfoData;
    private DigitalCheckoutPassData cartPassData;
    private DigitalDealsPagerAdapter pagerAdapter;
    private InteractionListener interactionListener;

    @Override
    public boolean canGoBack() {
        Fragment checkoutFragment = getChildFragmentManager().findFragmentByTag(TAG_DIGITAL_CHECKOUT);
        if (checkoutFragment instanceof DigitalDealCheckoutFragment) {
            return !((DigitalDealCheckoutFragment) checkoutFragment).isCartDetailViewVisible();
        }
        return true;
    }

    @Override
    public void goBack() {
        Fragment checkoutFragment = getChildFragmentManager().findFragmentByTag(TAG_DIGITAL_CHECKOUT);
        if (checkoutFragment instanceof DigitalDealCheckoutFragment) {
            ((DigitalDealCheckoutFragment) checkoutFragment).hideCartDetailView();
            ((DigitalDealCheckoutFragment) checkoutFragment).hideDealsContainerView();
            ((DigitalDealCheckoutFragment) checkoutFragment).renderIconToExpand();
        }
    }

    public interface InteractionListener {
        void updateToolbarTitle(String title);
    }

    public static DigitalCartDealsFragment newInstance(DigitalCheckoutPassData passData, CartDigitalInfoData cartDigitalInfoData) {
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
        selectedProducts = new ArrayList<>();
        selectedDealsMap = new HashMap<>();
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_digital_deals_cart, container, false);
        progressBar = view.findViewById(R.id.progress_bar);
        dealTabLayout = view.findViewById(R.id.tab_deal);
        dealViewPager = view.findViewById(R.id.pager_deals);
        dealContainer = view.findViewById(R.id.deal_container);
        checkoutContainer = view.findViewById(R.id.checkout_fragment);
        checkoutDim = view.findViewById(R.id.checkout_dim);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.attachView(this);
        presenter.onViewCreated();
        checkoutDim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
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
    public void renderGetCategoriesLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideDealsPage() {
        dealContainer.setVisibility(View.GONE);
    }

    @Override
    public void renderGetCategoriesError(String message) {
        Toaster.build(getView(), message, Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR,
                getString(com.tokopedia.abstraction.R.string.close), v->{}).show();
    }

    @Override
    public void hideGetCategoriesLoading() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void renderDealsTab(List<DealCategoryViewModel> dealCategoryViewModels) {
        pagerAdapter = new DigitalDealsPagerAdapter(
                dealCategoryViewModels,
                getChildFragmentManager(),
                this);
        dealTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        dealTabLayout.setupWithViewPager(dealViewPager);
        dealViewPager.setOffscreenPageLimit(dealCategoryViewModels.size());
        dealViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(dealTabLayout));
        dealViewPager.setAdapter(pagerAdapter);
        dealTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                presenter.onDealsTabSelected(tab.getText());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        pagerAdapter.notifyDataSetChanged();
    }

    @Override
    public void showDealsPage() {
        dealContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public List<DealProductViewModel> getSelectedDeals() {
        return selectedProducts;
    }

    @Override
    public void renderErrorInRedSnackbar(int resId) {
        Toaster.build(getView(), getString(resId), Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR,
                getString(com.tokopedia.abstraction.R.string.close), v->{}).show();
    }

    @Override
    public void notifySelectedDeal() {
        Object fragment = pagerAdapter.instantiateItem(dealViewPager, dealViewPager.getCurrentItem());
        if (fragment instanceof DigitalDealListListener) {
            ((DigitalDealListListener) fragment).notifySelectedDeal();
        }
    }

    @Override
    public void notifySelectedDealsInCheckout(DealProductViewModel viewModel) {
        Fragment checkoutFragment = getChildFragmentManager().findFragmentByTag(TAG_DIGITAL_CHECKOUT);
        if (checkoutFragment instanceof DigitalDealCheckoutFragment) {
            ((DigitalDealCheckoutFragment) checkoutFragment).updateSelectedDeal(viewModel);
        }
    }

    @Override
    public void updateSelectedDeal(int currentFragmentPosition, DealProductViewModel viewModel) {
        selectedDealsMap.put(viewModel, currentFragmentPosition);
    }

    @Override
    public Map<DealProductViewModel, Integer> getSelectedDealsMap() {
        return selectedDealsMap;
    }

    @Override
    public void notifyAdapterInSpecifyFragment(Integer position) {
        Object fragment = pagerAdapter.instantiateItem(dealViewPager, position);
        if (fragment instanceof DigitalCartDealsListFragment) {
            ((DigitalCartDealsListFragment) fragment).notifySelectedDeal();
        }
    }

    @Override
    public void notifyCheckoutPageToStartAnimation() {
        Fragment checkoutFragment = getChildFragmentManager().findFragmentByTag(TAG_DIGITAL_CHECKOUT);
        if (checkoutFragment instanceof DigitalDealCheckoutFragment) {
            ((DigitalDealCheckoutFragment) checkoutFragment).startAutomaticCollapse();
        }
    }

    @Override
    public DigitalCheckoutPassData getCartPassData() {
        return cartPassData;
    }

    @Override
    public CartDigitalInfoData getCartInfoData() {
        return cartInfoData;
    }

    @Override
    public void renderCheckoutView(DigitalCheckoutPassData cartPassData, CartDigitalInfoData cartInfoData) {
        checkoutContainer.setVisibility(View.VISIBLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            checkoutContainer.setElevation(60);
            checkoutContainer.setBackgroundResource(com.tokopedia.unifyprinciples.R.color.Unify_N0);
        } else {
            checkoutContainer.setBackgroundResource(R.drawable.digital_bg_drop_shadow);
        }
        DigitalDealCheckoutFragment fragment = DigitalDealCheckoutFragment.newInstance(cartPassData, cartInfoData);
        fragment.setInteractionListener(this);
        getChildFragmentManager().beginTransaction()
                .replace(R.id.checkout_fragment, fragment, TAG_DIGITAL_CHECKOUT)
                .commit();
    }


    @Override
    public void selectDeal(DealProductViewModel viewModel) {
        presenter.onSelectDealProduct(viewModel, dealViewPager.getCurrentItem());
    }

    @Override
    public void unSelectDeal(DealProductViewModel viewModel) {
        presenter.unSelectDealFromCheckoutView(viewModel);
    }

    @Override
    public void updateToolbarTitle(String title) {
        interactionListener.updateToolbarTitle(title);
    }

    @Override
    public void hideCartPage() {
        dealContainer.setVisibility(View.GONE);
        checkoutContainer.setVisibility(View.GONE);
    }

    @Override
    public void showFullpageLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void showCartPage() {
        dealContainer.setVisibility(View.VISIBLE);
        checkoutContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideFullpageLoading() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public int getParentMeasuredHeight() {
        return getView().getHeight();
    }

    @Override
    public void showDim(float percentage, int height) {
        checkoutDim.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        checkoutDim.setVisibility(View.VISIBLE);
        checkoutDim.setBackgroundColor(ColorUtils.setAlphaComponent(androidx.core.content.ContextCompat.getColor(getActivity(), com.tokopedia.unifyprinciples.R.color.Unify_N0), (int) (DEFAULT_MAX_DIM * percentage)));
    }

    @Override
    public void hideDim(float percentage) {
        checkoutDim.setBackgroundColor(ColorUtils.setAlphaComponent(androidx.core.content.ContextCompat.getColor(getActivity(), com.tokopedia.unifyprinciples.R.color.Unify_N0), (int) (DEFAULT_MAX_DIM * percentage)));
        if (percentage == 0.0) {
            checkoutDim.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onAttachActivity(Context context) {
        super.onAttachActivity(context);
        interactionListener = (InteractionListener) context;
    }
}
