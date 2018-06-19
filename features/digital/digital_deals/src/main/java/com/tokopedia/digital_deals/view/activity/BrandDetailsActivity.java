package com.tokopedia.digital_deals.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.digital_deals.R;
import com.tokopedia.digital_deals.di.DaggerDealsComponent;
import com.tokopedia.digital_deals.di.DealsComponent;
import com.tokopedia.digital_deals.di.DealsModule;
import com.tokopedia.digital_deals.view.adapter.DealsCategoryAdapter;
import com.tokopedia.digital_deals.view.contractor.BrandDetailsContract;
import com.tokopedia.digital_deals.view.presenter.BrandDetailsPresenter;
import com.tokopedia.digital_deals.view.utils.Utils;
import com.tokopedia.digital_deals.view.viewmodel.BrandViewModel;
import com.tokopedia.digital_deals.view.viewmodel.CategoryItemsViewModel;
import com.tokopedia.usecase.RequestParams;

import java.util.List;

import javax.inject.Inject;

public class BrandDetailsActivity extends BaseSimpleActivity implements HasComponent<DealsComponent>, BrandDetailsContract.View, View.OnClickListener {
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
    private at.blogc.android.views.ExpandableTextView tvExpandableDesc;
    private ProgressBar progBar;
    private LinearLayout noContent;
    @Inject
    public BrandDetailsPresenter mPresenter;
    private DealsComponent dealsComponent;
    private DealsCategoryAdapter categoryAdapter;

    @Override
    protected Fragment getNewFragment() {
        return null;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setViewIds();
        initInjector();
        executeInjector();
        mPresenter.attachView(this);
        mPresenter.getBrandDetails();

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                verticalOffset = Math.abs(verticalOffset);
                int difference = appBarLayout.getTotalScrollRange() - toolbar.getHeight();
                if (verticalOffset >= difference) {
                    DrawableCompat.setTint(toolbar.getNavigationIcon(), ContextCompat.getColor(getApplicationContext(), R.color.tkpd_dark_gray_toolbar));
                } else {
                    DrawableCompat.setTint(toolbar.getNavigationIcon(), ContextCompat.getColor(getApplicationContext(), R.color.white));
                }
            }
        });
    }

    private void setViewIds() {
        tvExpandableDesc = findViewById(R.id.tv_expandable_description);
        tvSeeMoreBtn = findViewById(R.id.expand_view_description);
        tvDealsCount = findViewById(R.id.number_of_locations);
        tvCityName = findViewById(R.id.tv_popular);
        collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        appBarLayout = findViewById(R.id.app_bar_layout);
        ivHeader = findViewById(R.id.header_image);
        ivBrandLogo = findViewById(R.id.iv_brand_logo);
        recyclerViewDeals = findViewById(R.id.recycler_view);
        tvSeeMore = findViewById(R.id.seemorebutton_description);
        ivArrowSeeMore = findViewById(R.id.down_arrow_description);
        progressBarLayout = findViewById(R.id.progress_bar_layout);
        mainContent = findViewById(R.id.main_content);
        baseMainContent = findViewById(R.id.base_main_content);
        progBar = findViewById(R.id.prog_bar);
        flHeader = findViewById(R.id.fl_header);
        noContent = findViewById(R.id.no_content);
        collapsingToolbarLayout.setTitle(" ");
        tvSeeMoreBtn.setOnClickListener(this);
        tvExpandableDesc.setInterpolator(new OvershootInterpolator());

    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_brand_detail;
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

    private void initInjector() {
        dealsComponent = DaggerDealsComponent.builder()
                .baseAppComponent(((BaseMainApplication) getApplication()).getBaseAppComponent())
                .dealsModule(new DealsModule(this))
                .build();
    }

    private void executeInjector() {
        if (dealsComponent == null) initInjector();
        dealsComponent.inject(this);
    }


    @Override
    public DealsComponent getComponent() {
        if (dealsComponent == null) initInjector();
        return dealsComponent;
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void navigateToActivityRequest(Intent intent, int requestCode) {
        startActivityForResult(intent, requestCode);
    }


    @Override
    public void renderBrandDetails(List<CategoryItemsViewModel> categoryItemsViewModels, BrandViewModel brandViewModel, int count) {
        collapsingToolbarLayout.setTitle(brandViewModel.getTitle());
        tvExpandableDesc.setText(brandViewModel.getDescription());
        tvCityName.setText(String.format(getResources().getString(R.string.deals_brand_detail_location), Utils.getSingletonInstance().getLocation(getActivity()).getName()));

        ImageHandler.loadImage(getActivity(), ivHeader, brandViewModel.getFeaturedImage(), R.color.grey_1100, R.color.grey_1100);
        ImageHandler.loadImage(getActivity(), ivBrandLogo, brandViewModel.getFeaturedThumbnailImage(), R.color.grey_1100, R.color.grey_1100);
        for (CategoryItemsViewModel categoryItemsViewModel : categoryItemsViewModels) {
            categoryItemsViewModel.setBrand(brandViewModel);
        }
        if (categoryItemsViewModels.size() != 0) {
            if (count == 0)
                tvDealsCount.setText(String.format(getResources().getString(R.string.number_of_items), categoryItemsViewModels.size()));
            else
                tvDealsCount.setText(String.format(getResources().getString(R.string.number_of_items), count));
            categoryAdapter = new DealsCategoryAdapter(getActivity(), categoryItemsViewModels, !isShortLayout);

            recyclerViewDeals.setAdapter(categoryAdapter);
            recyclerViewDeals.setVisibility(View.VISIBLE);
            noContent.setVisibility(View.GONE);
        } else {
            tvDealsCount.setText(String.format(getResources().getString(R.string.number_of_items), count));
            recyclerViewDeals.setVisibility(View.GONE);
            noContent.setVisibility(View.VISIBLE);
        }

        baseMainContent.setVisibility(View.VISIBLE);

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
        BrandViewModel brandViewModel = getIntent().getParcelableExtra(BrandDetailsPresenter.BRAND_DATA);
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(mPresenter.TAG, brandViewModel.getUrl());
        return requestParams;
    }

    @Override
    public View getRootView() {
        return mainContent;
    }

    @Override
    protected void onDestroy() {
        mPresenter.onDestroy();
        super.onDestroy();
    }
}