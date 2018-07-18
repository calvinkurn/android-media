package com.tokopedia.digital_deals.view.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.digital_deals.R;
import com.tokopedia.digital_deals.di.DealsComponent;
import com.tokopedia.digital_deals.view.adapter.DealsCategoryAdapter;
import com.tokopedia.digital_deals.view.contractor.BrandDetailsContract;
import com.tokopedia.digital_deals.view.customview.ExpandableTextView;
import com.tokopedia.digital_deals.view.model.Brand;
import com.tokopedia.digital_deals.view.model.ProductItem;
import com.tokopedia.digital_deals.view.presenter.BrandDetailsPresenter;
import com.tokopedia.digital_deals.view.utils.Utils;
import com.tokopedia.usecase.RequestParams;

import java.util.List;

import javax.inject.Inject;

;

public class BrandDetailsFragment extends BaseDaggerFragment implements BrandDetailsContract.View, View.OnClickListener, DealsCategoryAdapter.INavigateToActivityRequest {
    private final boolean isShortLayout = true;

    private CollapsingToolbarLayout collapsingToolbarLayout;
    private LinearLayout tvSeeMoreBtn;
    private AppBarLayout appBarLayout;
    private CoordinatorLayout mainContent;
    private ConstraintLayout baseMainContent;
    private FrameLayout flHeader;

    private TextView tvDealsCount;
    private TextView tvCityName;
    private TextView tvSeeMore;
    private ImageView ivHeader;
    private ImageView ivBrandLogo;
    private ImageView ivArrowSeeMore;
    private RecyclerView recyclerViewDeals;
    private View progressBarLayout;
    private ExpandableTextView tvExpandableDesc;
    private ProgressBar progBar;
    private LinearLayout noContent;
    @Inject
    public BrandDetailsPresenter mPresenter;
    private DealsCategoryAdapter categoryAdapter;
    private LinearLayoutManager layoutManager;
    private Toolbar toolbar;

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
        mPresenter.getBrandDetails();
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                verticalOffset = Math.abs(verticalOffset);
                int difference = appBarLayout.getTotalScrollRange() - toolbar.getHeight();
                if (verticalOffset >= difference) {
                    DrawableCompat.setTint(toolbar.getNavigationIcon(), ContextCompat.getColor(getContext(), R.color.tkpd_dark_gray_toolbar));
                } else {
                    DrawableCompat.setTint(toolbar.getNavigationIcon(), ContextCompat.getColor(getContext(), R.color.white));
                }
            }
        });
        return view;
    }


    private void setViewIds(View view) {
        tvExpandableDesc = view.findViewById(R.id.tv_expandable_description);
        tvSeeMoreBtn = view.findViewById(R.id.expand_view_description);
        tvDealsCount = view.findViewById(R.id.number_of_locations);
        tvCityName = view.findViewById(R.id.tv_popular);
        collapsingToolbarLayout = view.findViewById(R.id.collapsing_toolbar);
        appBarLayout = view.findViewById(R.id.app_bar_layout);
        ivHeader = view.findViewById(R.id.header_image);
        ivBrandLogo = view.findViewById(R.id.iv_brand_logo);
        recyclerViewDeals = view.findViewById(R.id.recycler_view);
        tvSeeMore = view.findViewById(R.id.seemorebutton_description);
        ivArrowSeeMore = view.findViewById(R.id.down_arrow_description);
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
        tvSeeMoreBtn.setOnClickListener(this);
        tvExpandableDesc.setInterpolator(new OvershootInterpolator());
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerViewDeals.setLayoutManager(layoutManager);

    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.expand_view_description) {
            if (tvExpandableDesc.isExpanded()) {
                tvSeeMore.setText(R.string.expand);
                ivArrowSeeMore.animate().rotation(0f);

            } else {
                tvSeeMore.setText(R.string.collapse);
                ivArrowSeeMore.animate().rotation(180f);

            }
            tvExpandableDesc.toggle();
        }
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
        tvExpandableDesc.setText(brand.getDescription());
        tvCityName.setText(String.format(getResources().getString(R.string.deals_brand_detail_location), Utils.getSingletonInstance().getLocation(getActivity()).getName()));
        loadBrandImage(ivHeader, brand.getFeaturedImage());
        ImageHandler.loadImage(getActivity(), ivBrandLogo, brand.getFeaturedThumbnailImage(), R.color.grey_1100, R.color.grey_1100);
        if (productItems!=null && productItems.size() > 0) {
            for (ProductItem productItem : productItems) {
                productItem.setBrand(brand);
            }
            if (count == 0)
                tvDealsCount.setText(String.format(getResources().getString(R.string.number_of_items), productItems.size()));
            else
                tvDealsCount.setText(String.format(getResources().getString(R.string.number_of_items), count));
            categoryAdapter = new DealsCategoryAdapter(productItems, this, !isShortLayout, true);

            recyclerViewDeals.setAdapter(categoryAdapter);
            recyclerViewDeals.setVisibility(View.VISIBLE);
            noContent.setVisibility(View.GONE);
            recyclerViewDeals.addOnScrollListener(rvOnScrollListener);
        } else {
            tvDealsCount.setText(String.format(getResources().getString(R.string.number_of_items), count));
            recyclerViewDeals.setVisibility(View.GONE);
            noContent.setVisibility(View.VISIBLE);
            recyclerViewDeals.removeOnScrollListener(rvOnScrollListener);
        }

        baseMainContent.setVisibility(View.VISIBLE);

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
                    imageView.setImageBitmap(Utils.getSingletonInstance().setBlur(resource,3.0f, getContext()));
                }else{
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
        requestParams.putInt(Utils.BRAND_QUERY_PARAM_CITY_ID, Utils.getSingletonInstance().getLocation(getActivity()).getId());
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
        if(categoryList!=null) {
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
        return null;
    }

    @Override
    public void onNavigateToActivityRequest(Intent intent, int requestCode) {
        navigateToActivityRequest(intent, requestCode);
    }
}
