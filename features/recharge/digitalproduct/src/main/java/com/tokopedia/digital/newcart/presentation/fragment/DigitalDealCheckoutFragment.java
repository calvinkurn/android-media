package com.tokopedia.digital.newcart.presentation.fragment;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal;
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData;
import com.tokopedia.digital.R;
import com.tokopedia.digital.newcart.di.DigitalCartComponent;
import com.tokopedia.digital.newcart.domain.model.DealProductViewModel;
import com.tokopedia.digital.newcart.presentation.contract.DigitalDealCheckoutContract;
import com.tokopedia.digital.newcart.presentation.fragment.adapter.DigitalDealActionListener;
import com.tokopedia.digital.newcart.presentation.fragment.adapter.DigitalDealsAdapter;
import com.tokopedia.digital.newcart.presentation.fragment.adapter.DigitalDealsAdapterTypeFactory;
import com.tokopedia.digital.newcart.presentation.model.cart.CartDigitalInfoData;
import com.tokopedia.digital.newcart.presentation.presenter.DigitalDealCheckoutPresenter;
import com.tokopedia.unifycomponents.Toaster;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 */
public class DigitalDealCheckoutFragment extends DigitalBaseCartFragment<DigitalDealCheckoutContract.Presenter>
        implements DigitalDealCheckoutContract.View, DigitalDealActionListener {
    private static final long ANIMATION_DURATION = TimeUnit.MILLISECONDS.toMillis(800);
    private LinearLayout containerLayout;
    private LinearLayout containerSelectedDeals;
    private LinearLayout containerDetail;
    private LinearLayout containerCategoryLabel;
    private NestedScrollView containerScroll;
    private AppCompatTextView categoryNameTextView;
    private AppCompatImageView expandCollapseView;
    private RecyclerView selectedDealsRecyclerView;

    private List<DealProductViewModel> selectedDeals;
    private DigitalDealsAdapter adapter;

    private int lastCollapseHeight;
    private boolean isAlreadyCollapsByUser = false;

    private InteractionListener interactionListener;


    public interface InteractionListener {
        void unSelectDeal(DealProductViewModel viewModel);

        void updateToolbarTitle(String string);

        void hideCartPage();

        void showFullpageLoading();

        void showCartPage();

        void hideFullpageLoading();

        int getParentMeasuredHeight();

        void showDim(float procentage, int height);

        void hideDim(float procentage);
    }

    @Inject
    DigitalDealCheckoutPresenter presenter;

    public DigitalDealCheckoutFragment() {
        // Required empty public constructor
    }

    public static DigitalDealCheckoutFragment newInstance(DigitalCheckoutPassData cartPassData,
                                                          CartDigitalInfoData cartInfoData) {
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
        return inflater.inflate(R.layout.fragment_digital_checkout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (interactionListener == null) {
            if (getParentFragment() instanceof InteractionListener) {
                interactionListener = (InteractionListener) getParentFragment();
            }
        }
        presenter.attachView(this);
        presenter.onDealsCheckout();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            containerLayout.setElevation(60);
            containerLayout.setBackgroundResource(com.tokopedia.unifyprinciples.R.color.Unify_N0);
        } else {
            containerLayout.setBackgroundResource(R.drawable.digital_bg_drop_shadow);
        }
    }

    @Override
    protected void setupView(View view) {
        containerScroll = view.findViewById(R.id.container_scroll);
        containerLayout = view.findViewById(R.id.container);
        containerDetail = view.findViewById(R.id.container_cart_detail);
        containerCategoryLabel = view.findViewById(R.id.container_category_label);
        containerSelectedDeals = view.findViewById(R.id.container_selected_deals);
        detailHolderView = view.findViewById(R.id.view_cart_detail);
        checkoutHolderView = view.findViewById(R.id.view_checkout_holder);
        checkoutHolderView = view.findViewById(R.id.view_checkout_holder);
        inputPriceContainer = view.findViewById(R.id.input_price_container);
        inputPriceHolderView = view.findViewById(R.id.input_price_holder_view);
        categoryNameTextView = view.findViewById(R.id.tv_category_name);
        selectedDealsRecyclerView = view.findViewById(R.id.rv_selected_deals);
        DigitalDealsAdapterTypeFactory adapterTypeFactory =
                new DigitalDealsAdapterTypeFactory(this, true);
        List<Visitable> visitables = new ArrayList<>(selectedDeals);
        adapter = new DigitalDealsAdapter(adapterTypeFactory, visitables);
        selectedDealsRecyclerView.setNestedScrollingEnabled(false);
        selectedDealsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        selectedDealsRecyclerView.setAdapter(adapter);
        expandCollapseView = view.findViewById(R.id.iv_expand_collapse);
        expandCollapseView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onExpandCollapseButtonView();
            }
        });
        containerCategoryLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onExpandCollapseButtonView();
            }
        });
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
    public void renderCategoryInfo(String categoryName) {
        categoryNameTextView.setText(categoryName);
    }

    @Override
    public void hideCartView() {
        interactionListener.hideCartPage();
    }

    @Override
    public void showFullPageLoading() {
        interactionListener.showFullpageLoading();
    }

    @Override
    public void showCartView() {
        interactionListener.showCartPage();
    }

    @Override
    public void hideFullPageLoading() {
        interactionListener.hideFullpageLoading();
    }

    @Override
    public boolean isCartDetailViewVisible() {
        return containerDetail.getVisibility() == View.VISIBLE;
    }

    @Override
    public void hideCartDetailView() {
        isAlreadyCollapsByUser = true;
        containerDetail.setVisibility(View.GONE);
        collapseCheckoutView(containerLayout);
    }

    @Override
    public void showCartDetailView() {
        isAlreadyCollapsByUser = true;
        containerDetail.setVisibility(View.VISIBLE);
        expandCheckoutView(containerLayout);
    }

    public void expandCheckoutView(final View containerLayout) {
        int currentHeight = containerLayout.getHeight();
        lastCollapseHeight = currentHeight;
        containerScroll.setVisibility(View.VISIBLE);
        final int targetHeight;
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        containerLayout.setLayoutParams(
                layoutParams
        );
        if (selectedDeals.size() == 0 && !promoData.getPromoCode().isEmpty()) {
            containerLayout.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            targetHeight = containerLayout.getMeasuredHeight();
            containerLayout.getLayoutParams().height = currentHeight;
        } else {
            containerLayout.getLayoutParams().height = currentHeight;
            containerLayout.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            targetHeight = interactionListener.getParentMeasuredHeight();
        }

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                int measureWithInterpolate = (int) ((targetHeight - currentHeight) * interpolatedTime) + currentHeight;
                int height = targetHeight;
                if ((int) interpolatedTime != 1) {
                    if (measureWithInterpolate >= currentHeight) {
                        height = measureWithInterpolate;
                    }
                }
                interactionListener.showDim(interpolatedTime, height);
                containerLayout.getLayoutParams().height = height;
                containerLayout.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        a.setDuration(ANIMATION_DURATION);
        containerLayout.startAnimation(a);
    }

    public void collapseCheckoutView(final View containerLayout) {
        int currentHeight = containerLayout.getHeight();
        containerLayout.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (lastCollapseHeight == 0) {
            lastCollapseHeight = checkoutHolderView.getVoucherViewHeight() +
                    checkoutHolderView.getCheckoutViewHeight() +
                    containerCategoryLabel.getMeasuredHeight() +
                    getResources().getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.layout_lvl1);
        }

        final int targetHeight = lastCollapseHeight == 0 || lastCollapseHeight >= currentHeight ?
                containerLayout.getMeasuredHeight() : lastCollapseHeight;
        containerLayout.getLayoutParams().height = currentHeight;
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                int measureWithInterpolate = (int) (currentHeight - (currentHeight * interpolatedTime));
                int height = targetHeight;
                interactionListener.hideDim(1 - interpolatedTime);
                if ((int) interpolatedTime != 1) {
                    if (measureWithInterpolate > targetHeight) {
                        height = measureWithInterpolate;
                    }
                    containerLayout.getLayoutParams().height = height;
                    containerLayout.requestLayout();
                } else {
                    if (containerScroll.getVisibility() != View.GONE) {
                        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                        );
                        layoutParams.setMargins(0, getResources().getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.layout_lvl1), 0, 0);
                        containerLayout.setLayoutParams(
                                layoutParams
                        );

                        containerScroll.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };
        a.setDuration(ANIMATION_DURATION);
        containerLayout.startAnimation(a);
    }

    @Override
    public void renderIconToExpand() {
        if (getContext() != null) {
            expandCollapseView.setImageDrawable(
                    ContextCompat.getDrawable(getContext(), com.tokopedia.resources.common.R.drawable.ic_system_action_arrow_up_gray_24)
            );
        }
    }

    @Override
    public void renderIconToCollapse() {
        if (getContext() != null) {
            expandCollapseView.setImageDrawable(
                    ContextCompat.getDrawable(getContext(), com.tokopedia.resources.common.R.drawable.ic_system_action_arrow_down_gray_24)
            );
        }
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

    @Override
    public void updateToolbarTitle(int resId) {
        interactionListener.updateToolbarTitle(getString(resId));
    }

    @Override
    public void updateToolbarTitle(String toolbarTitle) {
        if (interactionListener != null) {
            interactionListener.updateToolbarTitle(toolbarTitle);
        }
    }

    @Override
    public void updateCheckoutButtonText(String checkoutButtonText) {
        checkoutHolderView.setTextButton(checkoutButtonText);
    }

    @Override
    public void renderSkipToCheckoutMenu() {
        setHasOptionsMenu(true);
    }

    @Override
    public boolean isAlreadyCollapsByUser() {
        return isAlreadyCollapsByUser;
    }

    @Override
    public void showPromoOnlyForTopUpAndBillMessage() {
        Toaster.build(getView(), getString(R.string.digital_deal_promo_restriction_message),
                Snackbar.LENGTH_LONG, Toaster.TYPE_NORMAL, getString(R.string.digital_deal_promo_restriction_action_label), v->{}).show();
    }

    public void updateSelectedDeal(DealProductViewModel viewModel) {
        presenter.onNewSelectedDeal(viewModel);
    }

    @Override
    public void setMinHeight(int resId) {
        containerScroll.setMinimumHeight(getResources().getDimensionPixelSize(resId));
    }

    @Override
    public void navigateToDealDetailPage(String slug) {
        Intent intent = RouteManager.getIntent(getActivity(),
                ApplinkConstInternalGlobal.GLOBAL_INTERNAL_DIGITAL_DEAL_SLUG);
        startActivity(intent);
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
        presenter.onDealDetailClicked(productViewModel);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.digital_cart_checkout_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.digital_deal_action_menu_skip) {
            presenter.onSkipMenuClicked();
        }
        return true;
    }

    @Override
    public long getCheckoutDiscountPricePlain() {
        return checkoutHolderView.getDiscountPricePlain();
    }

    @Override
    public void disableVoucherCheckoutDiscount() {
        super.disableVoucherCheckoutDiscount();
        if (!isCartDetailViewVisible()) {
            containerLayout.setLayoutParams(new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            );
        }
    }

    @Override
    public void renderPromo() {
        super.renderPromo();
        if (!isCartDetailViewVisible()) {
            containerLayout.setLayoutParams(new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }
    }


    public void startAutomaticCollapse() {
        presenter.autoCollapseCheckoutView();
    }
}
