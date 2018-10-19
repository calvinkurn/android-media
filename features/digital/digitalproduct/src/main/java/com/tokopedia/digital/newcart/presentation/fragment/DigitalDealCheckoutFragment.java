package com.tokopedia.digital.newcart.presentation.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData;
import com.tokopedia.common_digital.cart.view.model.cart.CartDigitalInfoData;
import com.tokopedia.digital.R;
import com.tokopedia.digital.cart.di.DigitalCartComponent;
import com.tokopedia.digital.newcart.domain.model.DealProductViewModel;
import com.tokopedia.digital.newcart.presentation.contract.DigitalDealCheckoutContract;
import com.tokopedia.digital.newcart.presentation.fragment.adapter.DigitalDealActionListener;
import com.tokopedia.digital.newcart.presentation.fragment.adapter.DigitalDealsAdapter;
import com.tokopedia.digital.newcart.presentation.fragment.adapter.DigitalDealsAdapterTypeFactory;
import com.tokopedia.digital.newcart.presentation.presenter.DigitalDealCheckoutPresenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 */
public class DigitalDealCheckoutFragment extends DigitalBaseCartFragment<DigitalDealCheckoutContract.Presenter> implements DigitalDealCheckoutContract.View, DigitalDealActionListener {
    private LinearLayout containerLayout, containerDetail, containerSelectedDeals;
    private AppCompatTextView categoryNameTextView;
    private AppCompatImageView expandCollapseView;
    private RecyclerView selectedDealsRecyclerView;

    private List<DealProductViewModel> selectedDeals;
    private DigitalDealsAdapter adapter;

    private InteractionListener interactionListener;

    public interface InteractionListener {
        void unSelectDeal(DealProductViewModel viewModel);
    }

    @Inject
    DigitalDealCheckoutPresenter presenter;

    public DigitalDealCheckoutFragment() {
        // Required empty public constructor
    }

    public static DigitalDealCheckoutFragment newInstance(DigitalCheckoutPassData cartPassData, CartDigitalInfoData cartInfoData) {
        DigitalDealCheckoutFragment fragment = new DigitalDealCheckoutFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARG_PASS_DATA, cartPassData);
        bundle.putParcelable(ARG_CART_INFO, cartInfoData);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        selectedDeals = new ArrayList<>();
        super.onCreate(savedInstanceState);
        cartDigitalInfoData = getArguments().getParcelable(ARG_CART_INFO);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_digital_deal_checkout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.attachView(this);
        presenter.onDealsCheckout();
    }

    @Override
    protected void setupView(View view) {
        containerLayout = view.findViewById(R.id.container);
        containerDetail = view.findViewById(R.id.container_cart_detail);
        containerSelectedDeals = view.findViewById(R.id.container_selected_deals);
        detailHolderView = view.findViewById(R.id.view_cart_detail);
        checkoutHolderView = view.findViewById(R.id.view_checkout_holder);
        checkoutHolderView = view.findViewById(R.id.view_checkout_holder);
        inputPriceContainer = view.findViewById(R.id.input_price_container);
        inputPriceHolderView = view.findViewById(R.id.input_price_holder_view);
        categoryNameTextView = view.findViewById(R.id.tv_category_name);
        selectedDealsRecyclerView = view.findViewById(R.id.rv_selected_deals);
        expandCollapseView = view.findViewById(R.id.iv_expand_collapse);
        expandCollapseView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onExpandCollapseButtonView();
            }
        });
        DigitalDealsAdapterTypeFactory adapterTypeFactory = new DigitalDealsAdapterTypeFactory(this, true);
        List<Visitable> visitables = new ArrayList<>(selectedDeals);
        adapter = new DigitalDealsAdapter(adapterTypeFactory, visitables);
        selectedDealsRecyclerView.setNestedScrollingEnabled(false);
        selectedDealsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        selectedDealsRecyclerView.setAdapter(adapter);
    }

    @Override
    protected void initInjector() {
        getComponent(DigitalCartComponent.class).inject(this);
        super.presenter = presenter;
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    public void renderCategory(String categoryName) {
        categoryNameTextView.setText(categoryName);
    }

    @Override
    public void hideContent() {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void showContent() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void inflateDealsPage(CartDigitalInfoData cartDigitalInfoData, DigitalCheckoutPassData cartPassData) {

    }

    @Override
    public boolean isCartDetailViewVisible() {
        return containerDetail.getVisibility() == View.VISIBLE;
    }

    @Override
    public void hideCartDetailView() {
        containerDetail.setVisibility(View.GONE);
        containerLayout.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    @Override
    public void showCartDetailView() {
        containerDetail.setVisibility(View.VISIBLE);
        containerLayout.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    @Override
    public void renderIconToExpand() {
        if (getContext() != null)
            expandCollapseView.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_arrow_up_grey));
    }

    @Override
    public void renderIconToCollapse() {
        if (getContext() != null)
            expandCollapseView.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_arrow_down_grey));
    }

    @Override
    public void addSelectedDeal(DealProductViewModel viewModel) {
        if (selectedDeals == null) selectedDeals = new ArrayList<>();
        selectedDeals.add(viewModel);
    }

    @Override
    public List<DealProductViewModel> getSelectedDeals() {
        return selectedDeals;
    }

    @Override
    public void renderCartDealListView(DealProductViewModel newSelectedDeal) {
        containerSelectedDeals.setVisibility(View.VISIBLE);
        adapter.addElement(newSelectedDeal);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void notifySelectedDealListView(DealProductViewModel viewModel) {
        adapter.removeElement(viewModel);
    }

    public void setInteractionListener(InteractionListener interactionListener) {
        this.interactionListener = interactionListener;
    }

    @Override
    public void updateDealListView(DealProductViewModel viewModel) {
        if (interactionListener != null) interactionListener.unSelectDeal(viewModel);
    }

    @Override
    public void hideDealsContainerView() {
        containerSelectedDeals.setVisibility(View.GONE);
    }

    @Override
    public void showDealsContainerView() {
        containerSelectedDeals.setVisibility(View.VISIBLE);
    }

    public void updateSelectedDeal(DealProductViewModel viewModel) {
        presenter.onNewSelectedDeal(viewModel);
    }

    @Override
    public void actionBuyButton(DealProductViewModel productViewModel) {

    }

    @Override
    public void actionCloseButon(DealProductViewModel productViewModel) {
        presenter.onDealRemoved(productViewModel);
    }

    @Override
    public void actionDetail(DealProductViewModel productViewModel) {

    }
}
