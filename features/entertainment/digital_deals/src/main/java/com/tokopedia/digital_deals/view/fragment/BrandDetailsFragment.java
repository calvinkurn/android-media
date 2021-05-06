package com.tokopedia.digital_deals.view.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.digital_deals.R;
import com.tokopedia.digital_deals.di.DealsComponent;
import com.tokopedia.digital_deals.view.adapter.DealsCategoryAdapter;
import com.tokopedia.digital_deals.view.contractor.BrandDetailsContract;
import com.tokopedia.digital_deals.view.model.Brand;
import com.tokopedia.digital_deals.view.model.Location;
import com.tokopedia.digital_deals.view.model.ProductItem;
import com.tokopedia.digital_deals.view.presenter.BrandDetailsPresenter;
import com.tokopedia.digital_deals.view.utils.DealsAnalytics;
import com.tokopedia.digital_deals.view.utils.Utils;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.List;

import javax.inject.Inject;

import static android.app.Activity.RESULT_OK;

public class BrandDetailsFragment extends BaseDaggerFragment implements BrandDetailsContract.View, DealsCategoryAdapter.INavigateToActivityRequest {
    private final boolean isShortLayout = true;
    private static final String SCREEN_NAME = "/digital/deals/pdp brand";

    private CollapsingToolbarLayout collapsingToolbarLayout;
    private AppBarLayout appBarLayout;
    private CoordinatorLayout mainContent;
    private LinearLayout baseMainContent;
    private FrameLayout flHeader;

    private ImageView ivHeader;
    private ImageView ivBrandLogo;
    private RecyclerView recyclerViewDeals;
    private View progressBarLayout;

    private ProgressBar progBar;
    private LinearLayout noContent;
    @Inject
    public BrandDetailsPresenter mPresenter;
    @Inject
    DealsAnalytics dealsAnalytics;
    private DealsCategoryAdapter dealsAdapter;
    private LinearLayoutManager layoutManager;
    private Toolbar toolbar;
    private String locationName;
    private int adapterPosition = -1;
    private boolean forceRefresh;
    private UserSession userSession;

    public static Fragment createInstance(Bundle bundle) {
        Fragment fragment = new BrandDetailsFragment();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(com.tokopedia.digital_deals.R.layout.fragment_brand_detail, container, false);
        setViewIds(view);
        setHasOptionsMenu(true);
        userSession = new UserSession(getActivity());
        mPresenter.getBrandDetails(true);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                verticalOffset = Math.abs(verticalOffset);
                int difference = appBarLayout.getTotalScrollRange() - toolbar.getHeight();
                if (verticalOffset >= difference) {
                    setDrawableColorFilter(toolbar.getNavigationIcon(), ContextCompat.getColor(getActivity(), com.tokopedia.unifyprinciples.R.color.Unify_N400));
                } else {
                    setDrawableColorFilter(toolbar.getNavigationIcon(), ContextCompat.getColor(getActivity(), com.tokopedia.unifyprinciples.R.color.Unify_N0));
                }
            }
        });
        if (userSession.isLoggedIn()) {
            mPresenter.sendNSQEvent(userSession.getUserId(), "brand-detail");
        }
        return view;
    }

    public void setDrawableColorFilter(Drawable drawable, int color) {
        if (drawable != null) {
            drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        }
    }


    @SuppressLint("WrongConstant")
    private void setViewIds(View view) {
        collapsingToolbarLayout = view.findViewById(com.tokopedia.digital_deals.R.id.collapsing_toolbar);
        appBarLayout = view.findViewById(com.tokopedia.digital_deals.R.id.app_bar_layout);
        ivHeader = view.findViewById(com.tokopedia.digital_deals.R.id.header_image);
        ivBrandLogo = view.findViewById(com.tokopedia.digital_deals.R.id.iv_brand_logo);
        recyclerViewDeals = view.findViewById(com.tokopedia.digital_deals.R.id.recycler_view);
        progressBarLayout = view.findViewById(com.tokopedia.digital_deals.R.id.progress_bar_layout);
        mainContent = view.findViewById(com.tokopedia.digital_deals.R.id.main_content);
        baseMainContent = view.findViewById(com.tokopedia.digital_deals.R.id.base_main_content);
        progBar = view.findViewById(com.tokopedia.digital_deals.R.id.prog_bar);
        flHeader = view.findViewById(com.tokopedia.digital_deals.R.id.fl_header);
        noContent = view.findViewById(com.tokopedia.digital_deals.R.id.no_content);
        toolbar = view.findViewById(com.tokopedia.digital_deals.R.id.toolbar);

        ((BaseSimpleActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(ContextCompat.getDrawable(getActivity(), com.tokopedia.abstraction.R.drawable.ic_action_back));
        collapsingToolbarLayout.setTitle(" ");
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerViewDeals.setLayoutManager(layoutManager);
        dealsAdapter = new DealsCategoryAdapter(null, DealsCategoryAdapter.BRAND_PAGE, this, getArguments().getString("fromApplink"),!isShortLayout, true);
        dealsAdapter.setDealType(DealsAnalytics.BRAND_DEALS);
        recyclerViewDeals.setAdapter(dealsAdapter);

    }


    @Override
    protected void initInjector() {
        getComponent(DealsComponent.class).inject(this);
        mPresenter.attachView(this);
    }


    @Override
    public void navigateToActivityRequest(Intent intent, int requestCode) {
        startActivityForResult(intent, requestCode);
    }

    @Override
    public void renderBrandDetails(List<ProductItem> productItems, Brand brand, int count) {
        dealsAnalytics.sendScreenNameEvent(getScreenName());
        collapsingToolbarLayout.setTitle(brand.getTitle());
        Location location = Utils.getSingletonInstance().getLocation(getActivity());
        if (location != null) {
            locationName = location.getName();
        }
        loadBrandImage(ivHeader, brand.getFeaturedImage());
        ImageHandler.loadImage(getActivity(), ivBrandLogo, brand.getFeaturedThumbnailImage(), com.tokopedia.unifyprinciples.R.color.Unify_N50, com.tokopedia.unifyprinciples.R.color.Unify_N50);
        if (productItems != null && productItems.size() > 0) {
            dealsAdapter.clearList();
            recyclerViewDeals.clearOnScrollListeners();
            dealsAdapter.addAll(productItems, false);
            dealsAdapter.notifyDataSetChanged();
            dealsAdapter.setBrand(brand);
            dealsAdapter.addBrandHeader(brand.getDescription(), brand.getTitle(), count);
            noContent.setVisibility(View.GONE);
            recyclerViewDeals.addOnScrollListener(rvOnScrollListener);
        } else {
            dealsAdapter.clearList();
            recyclerViewDeals.clearOnScrollListeners();
            dealsAdapter.addBrandHeader(brand.getDescription(), brand.getTitle(), 0);
            noContent.setVisibility(View.VISIBLE);
        }
        baseMainContent.setVisibility(View.VISIBLE);
        dealsAnalytics.sendEventDealsDigitalView(DealsAnalytics.EVENT_VIEW_BRAND_DETAIL,
                brand.getTitle());
    }

    private void loadBrandImage(ImageView imageView, String featuredImageUrl) {
        ImageHandler.loadImageWithTarget(getContext(), featuredImageUrl, new CustomTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    try {
                        imageView.setImageBitmap(Utils.getSingletonInstance().setBlur(resource, 3.0f, getContext()));
                    } catch (Exception e) {

                    }
                } else {
                    imageView.setImageBitmap(resource);
                }
            }

            @Override
            public void onLoadCleared(@Nullable Drawable placeholder) {

            }

            @Override
            public void onLoadStarted(Drawable placeholder) {
                super.onLoadStarted(placeholder);
                imageView.setImageResource(com.tokopedia.unifyprinciples.R.color.Unify_N50);
            }

            @Override
            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                super.onLoadFailed(errorDrawable);
                imageView.setImageResource(com.tokopedia.unifyprinciples.R.color.Unify_N50);
            }
        });
    }


    @Override
    public void showProgressBar() {
        progBar.setVisibility(View.VISIBLE);
        progressBarLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        progBar.setVisibility(View.GONE);
        progressBarLayout.setVisibility(View.GONE);
    }

    @Override
    public void hideCollapsingHeader() {
        flHeader.setVisibility(View.GONE);
    }

    @Override
    public void showCollapsingHeader() {
        flHeader.setVisibility(View.VISIBLE);
    }


    @Override
    public RequestParams getParams() {
        RequestParams requestParams = RequestParams.create();
        if (getArguments() != null) {
            Brand brand = getArguments().getParcelable(BrandDetailsPresenter.BRAND_DATA);
            requestParams.putString(BrandDetailsPresenter.TAG, brand.getUrl());
            Location location = Utils.getSingletonInstance().getLocation(getActivity());
            if (location != null) {
                if (!TextUtils.isEmpty(location.getCoordinates())) {
                    requestParams.putString(Utils.LOCATION_COORDINATES, location.getCoordinates());
                }
            }
        }
        return requestParams;
    }

    @Override
    public View getRootView() {
        return mainContent;
    }

    @Override
    public void removeFooter() {
        ((DealsCategoryAdapter) recyclerViewDeals.getAdapter()).removeFooter();

    }

    @Override
    public LinearLayoutManager getLayoutManager() {
        return layoutManager;
    }

    @Override
    public void addFooter() {
        ((DealsCategoryAdapter) recyclerViewDeals.getAdapter()).addFooter();
    }

    @Override
    public void addDealsToCards(List<ProductItem> categoryList) {
        if (categoryList != null) {
            ((DealsCategoryAdapter) recyclerViewDeals.getAdapter()).addAll(categoryList);
        }
    }

    private RecyclerView.OnScrollListener rvOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            mPresenter.onRecyclerViewScrolled(layoutManager);
        }
    };


    @Override
    public void onDestroy() {
        mPresenter.onDestroy();
        super.onDestroy();
    }

    @Override
    protected String getScreenName() {
        return SCREEN_NAME;
    }

    @Override
    public void onNavigateToActivityRequest(Intent intent, int requestCode, int position) {
        this.adapterPosition = position;
        navigateToActivityRequest(intent, requestCode);
    }

    @Override
    public void onStop() {
        forceRefresh = true;
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (forceRefresh) {
            if (dealsAdapter != null)
                dealsAdapter.notifyDataSetChanged();
            forceRefresh = false;
        }
    }
}
