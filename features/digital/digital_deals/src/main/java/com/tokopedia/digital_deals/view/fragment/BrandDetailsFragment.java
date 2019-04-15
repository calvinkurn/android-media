package com.tokopedia.digital_deals.view.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.digital_deals.R;
import com.tokopedia.digital_deals.di.DealsComponent;
import com.tokopedia.digital_deals.view.activity.DealsHomeActivity;
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

    public static Fragment createInstance(Bundle bundle) {
        Fragment fragment = new BrandDetailsFragment();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_brand_detail, container, false);
        setViewIds(view);
        setHasOptionsMenu(true);
        mPresenter.getBrandDetails(true);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                verticalOffset = Math.abs(verticalOffset);
                int difference = appBarLayout.getTotalScrollRange() - toolbar.getHeight();
                if (verticalOffset >= difference) {
                    setDrawableColorFilter(toolbar.getNavigationIcon(), ContextCompat.getColor(getActivity(), R.color.tkpd_dark_gray_toolbar));
                } else {
                    setDrawableColorFilter(toolbar.getNavigationIcon(), ContextCompat.getColor(getActivity(), R.color.white));
                }
            }
        });
        return view;
    }

    public void setDrawableColorFilter(Drawable drawable, int color) {
        if (drawable != null) {
            drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        }
    }


    private void setViewIds(View view) {

        collapsingToolbarLayout = view.findViewById(R.id.collapsing_toolbar);
        appBarLayout = view.findViewById(R.id.app_bar_layout);
        ivHeader = view.findViewById(R.id.header_image);
        ivBrandLogo = view.findViewById(R.id.iv_brand_logo);
        recyclerViewDeals = view.findViewById(R.id.recycler_view);
        progressBarLayout = view.findViewById(R.id.progress_bar_layout);
        mainContent = view.findViewById(R.id.main_content);
        baseMainContent = view.findViewById(R.id.base_main_content);
        progBar = view.findViewById(R.id.prog_bar);
        flHeader = view.findViewById(R.id.fl_header);
        noContent = view.findViewById(R.id.no_content);
        toolbar = view.findViewById(R.id.toolbar);

        ((BaseSimpleActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(ContextCompat.getDrawable(getActivity(), R.drawable.ic_action_back));
        collapsingToolbarLayout.setTitle(" ");
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerViewDeals.setLayoutManager(layoutManager);
        dealsAdapter = new DealsCategoryAdapter(null, DealsCategoryAdapter.BRAND_PAGE, this, !isShortLayout, true);
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
        collapsingToolbarLayout.setTitle(brand.getTitle());
        Location location = Utils.getSingletonInstance().getLocation(getActivity());
        if (location != null) {
            locationName = location.getName();
        }
        loadBrandImage(ivHeader, brand.getFeaturedImage());
        ImageHandler.loadImage(getActivity(), ivBrandLogo, brand.getFeaturedThumbnailImage(), R.color.grey_1100, R.color.grey_1100);
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
        ImageHandler.loadImageWithTarget(getContext(), featuredImageUrl, new SimpleTarget<Bitmap>() {
            @Override
            public void onLoadStarted(Drawable placeholder) {
                super.onLoadStarted(placeholder);
                imageView.setImageResource(R.color.grey_1100);
            }

            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
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
            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                super.onLoadFailed(e, errorDrawable);
                imageView.setImageResource(R.color.grey_1100);
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
        Brand brand = getArguments().getParcelable(BrandDetailsPresenter.BRAND_DATA);
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(BrandDetailsPresenter.TAG, brand.getUrl());
        Location location = Utils.getSingletonInstance().getLocation(getActivity());
        if (location != null)
            requestParams.putInt(Utils.QUERY_PARAM_CITY_ID, location.getId());
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (getActivity() == null)
            return;
        switch (requestCode) {
            case DealsHomeActivity.REQUEST_CODE_DEALDETAILACTIVITY:
                if (resultCode == RESULT_OK) {
                    Location location = Utils.getSingletonInstance().getLocation(getActivity());
                    if (location != null && !TextUtils.isEmpty(locationName) && !TextUtils.isEmpty(location.getName()) && !locationName.equals(location.getName())) {
                        mPresenter.getBrandDetails(true);
                    } else {
                        mPresenter.getBrandDetails(false);
                    }
                }
                break;
            case DealsHomeActivity.REQUEST_CODE_LOGIN:
                if (resultCode == RESULT_OK) {
                    UserSessionInterface userSession = new UserSession(getActivity());
                    if (userSession.isLoggedIn()) {
                        if (adapterPosition != -1) {
                            if (dealsAdapter != null)
                                dealsAdapter.setLike(adapterPosition);
                        }
                    }
                }
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onDestroy() {
        mPresenter.onDestroy();
        super.onDestroy();
    }

    @Override
    protected String getScreenName() {
        return null;
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
