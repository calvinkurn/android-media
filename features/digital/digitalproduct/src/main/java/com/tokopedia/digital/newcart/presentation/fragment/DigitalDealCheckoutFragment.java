package com.tokopedia.digital.newcart.presentation.fragment;


import android.animation.ValueAnimator;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
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
import android.widget.RelativeLayout;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.common.utils.view.CommonUtils;
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData;
import com.tokopedia.common_digital.cart.view.model.cart.CartDigitalInfoData;
import com.tokopedia.common_digital.cart.view.model.checkout.CheckoutDataParameter;
import com.tokopedia.digital.R;
import com.tokopedia.digital.cart.di.DigitalCartComponent;
import com.tokopedia.digital.common.router.DigitalModuleRouter;
import com.tokopedia.digital.newcart.domain.model.DealProductViewModel;
import com.tokopedia.digital.newcart.presentation.contract.DigitalDealCheckoutContract;
import com.tokopedia.digital.newcart.presentation.fragment.adapter.DigitalDealActionListener;
import com.tokopedia.digital.newcart.presentation.fragment.adapter.DigitalDealsAdapter;
import com.tokopedia.digital.newcart.presentation.fragment.adapter.DigitalDealsAdapterTypeFactory;
import com.tokopedia.digital.newcart.presentation.presenter.DigitalDealCheckoutPresenter;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 */
public class DigitalDealCheckoutFragment extends DigitalBaseCartFragment<DigitalDealCheckoutContract.Presenter> implements DigitalDealCheckoutContract.View, DigitalDealActionListener {
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

    private InteractionListener interactionListener;

    public interface InteractionListener {
        void unSelectDeal(DealProductViewModel viewModel);

        void updateToolbarTitle(String string);

        void hideCartPage();

        void showFullpageLoading();

        void showCartPage();

        void hideFullpageLoading();
    }

    @Inject
    DigitalDealCheckoutPresenter presenter;
    @Inject
    DigitalModuleRouter digitalModuleRouter;

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
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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
        DigitalDealsAdapterTypeFactory adapterTypeFactory = new DigitalDealsAdapterTypeFactory(this, true);
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
    public void renderCategory(String categoryName) {
        categoryNameTextView.setText(categoryName);
    }

    @Override
    public void hideContent() {
        interactionListener.hideCartPage();
    }

    @Override
    public void showLoading() {
        interactionListener.showFullpageLoading();
    }

    @Override
    public void showContent() {
        interactionListener.showCartPage();
    }

    @Override
    public void hideLoading() {
        interactionListener.hideFullpageLoading();
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
//        containerLayout.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        collapse(containerLayout);
    }

    @Override
    public void showCartDetailView() {
        containerDetail.setVisibility(View.VISIBLE);
//        containerLayout.setLayoutParams( new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        expand(containerLayout);
//
//        ValueAnimator anim = ValueAnimator.ofInt(containerLayout.getMeasuredHeight(), -100);
//        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator valueAnimator) {
//                int val = (Integer) valueAnimator.getAnimatedValue();
//                ViewGroup.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
//                layoutParams.height = val;
//                viewToIncreaseHeight.setLayoutParams(layoutParams);
//            }
//        });
//        anim.setDuration(DURATION);
//        anim.start();
    }

    public void expand(final View v) {
        int currentHeight = v.getHeight();
        v.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        Display display = getActivity().getWindowManager().getDefaultDisplay();
//        int realWidth;
//        int realHeight;
//
//        if (Build.VERSION.SDK_INT >= 17){
//            //new pleasant way to get real metrics
//            DisplayMetrics realMetrics = new DisplayMetrics();
//            display.getRealMetrics(realMetrics);
//            realWidth = realMetrics.widthPixels;
//            realHeight = realMetrics.heightPixels;
//
//        } else if (Build.VERSION.SDK_INT >= 14) {
//            //reflection for this weird in-between time
//            try {
//                Method mGetRawH = Display.class.getMethod("getRawHeight");
//                Method mGetRawW = Display.class.getMethod("getRawWidth");
//                realWidth = (Integer) mGetRawW.invoke(display);
//                realHeight = (Integer) mGetRawH.invoke(display);
//            } catch (Exception e) {
//                //this may not be 100% accurate, but it's all we've got
//                realWidth = display.getWidth();
//                realHeight = display.getHeight();
//                Log.e("Display Info", "Couldn't use reflection to get the real display metrics.");
//            }
//
//        } else {
//            //This should be close, as lower API devices should not have window navigation bars
//            realWidth = display.getWidth();
//            realHeight = display.getHeight();
//        }
        final int targetHeight = v.getMeasuredHeight();
        CommonUtils.dumper("Target : " + targetHeight);
        CommonUtils.dumper("Current : " + currentHeight);

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = currentHeight;
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                int measureWithInterpolate = (int) ((targetHeight - currentHeight) * interpolatedTime) + currentHeight;

                int height = ViewGroup.LayoutParams.MATCH_PARENT;
                if ((int) interpolatedTime != 1) {
                    if (measureWithInterpolate > currentHeight) {
                        height = measureWithInterpolate;
                    } else {
                        height = targetHeight;
                    }
                } else {
                    containerScroll.setVisibility(View.VISIBLE);
                }
                CommonUtils.dumper(interpolatedTime);
                CommonUtils.dumper(height + " " + measureWithInterpolate);

                v.getLayoutParams().height = height;
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        a.setDuration(300);
        v.startAnimation(a);
    }

    public void collapse(final View v) {
        int currentHeight = v.getHeight();
        v.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();
        CommonUtils.dumper("Target : " + targetHeight);
        CommonUtils.dumper("Current : " + currentHeight);
        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = currentHeight;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                int measureWithInterpolate = (int) (currentHeight - (currentHeight * interpolatedTime));
                int height = ViewGroup.LayoutParams.WRAP_CONTENT;
                if ((int) interpolatedTime != 1) {
                    if (measureWithInterpolate > targetHeight) {
                        height = measureWithInterpolate;
                    }
                } else {
                    containerScroll.setVisibility(View.GONE);
                }
                CommonUtils.dumper(interpolatedTime);
                CommonUtils.dumper(height + " " + measureWithInterpolate);

                v.getLayoutParams().height = height;
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };
        // 1dp/ms
        a.setDuration(300);
        v.startAnimation(a);
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

    @Override
    public void updateToolbarTitle(int resId) {
        interactionListener.updateToolbarTitle(getString(resId));
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
        startActivity(digitalModuleRouter.getDealDetailIntent(getActivity(), slug, false, false));
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
}
