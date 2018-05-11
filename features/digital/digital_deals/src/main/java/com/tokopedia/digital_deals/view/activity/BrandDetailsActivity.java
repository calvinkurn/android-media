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
import android.util.Log;
import android.view.MenuItem;
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
import com.tokopedia.digital_deals.view.contractor.DealsBrandDetailsContract;
import com.tokopedia.digital_deals.view.presenter.DealsBrandPresenter;
import com.tokopedia.digital_deals.view.viewmodel.BrandViewModel;
import com.tokopedia.digital_deals.view.viewmodel.CategoryItemsViewModel;
import com.tokopedia.usecase.RequestParams;

import java.util.List;

import javax.inject.Inject;

public class BrandDetailsActivity extends BaseSimpleActivity implements HasComponent<DealsComponent>, DealsBrandDetailsContract.View, View.OnClickListener {
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private at.blogc.android.views.ExpandableTextView tvExpandableDesc;
    private LinearLayout seeMoreButton;
    private TextView numberofDeals;
    private TextView popularCityName;
    private ImageView imageHeader;
    private AppBarLayout appBarLayout;
    private ImageView brandLogo;
    private RecyclerView recyclerViewDeals;
    private TextView seemorebuttonText;
    private ImageView ivArrowSeeMore;
    private DealsComponent mdealsComponent;
    @Inject
    public DealsBrandPresenter mPresenter;
    private DealsCategoryAdapter categoryAdapter;

    private BrandViewModel brandViewModel;
    private View progressBarLayout;
    private ProgressBar progBar;
    private CoordinatorLayout mainContent;
    private ConstraintLayout baseMainContent;
    private FrameLayout flHeader;
    private CollapsingToolbarLayout cTOllBar;

    private final boolean IS_SHORT_LAYOUT = false;

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
        seeMoreButton = findViewById(R.id.expand_view_description);
        numberofDeals = findViewById(R.id.number_of_locations);
        popularCityName = findViewById(R.id.tv_popular);
        collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        appBarLayout = findViewById(R.id.app_bar_layout);
        imageHeader = findViewById(R.id.header_image);
        brandLogo = findViewById(R.id.iv_brand_logo);
        recyclerViewDeals = findViewById(R.id.recyclerView);
        seemorebuttonText = findViewById(R.id.seemorebutton_description);
        ivArrowSeeMore = findViewById(R.id.down_arrow_description);
        progressBarLayout = findViewById(R.id.progress_bar_layout);
        mainContent = findViewById(R.id.main_content);
        baseMainContent = findViewById(R.id.base_main_content);
        progBar = findViewById(R.id.prog_bar);
        flHeader = findViewById(R.id.fl_header);
        collapsingToolbarLayout.setTitle(" ");
        seeMoreButton.setOnClickListener(this);
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
                seemorebuttonText.setText(R.string.expand);
                ivArrowSeeMore.animate().rotation(0f);

            } else {
                seemorebuttonText.setText(R.string.collapse);
                ivArrowSeeMore.animate().rotation(180f);

            }
            tvExpandableDesc.toggle();
        }
    }

    private void initInjector() {
        mdealsComponent = DaggerDealsComponent.builder()
                .baseAppComponent(((BaseMainApplication) getApplication()).getBaseAppComponent())
                .dealsModule(new DealsModule(this))
                .build();
    }

    private void executeInjector() {
        if (mdealsComponent == null) initInjector();
        mdealsComponent.inject(this);
    }


    @Override
    public DealsComponent getComponent() {
        if (mdealsComponent == null) initInjector();
        return mdealsComponent;
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void navigateToActivityRequest(Intent intent, int requestCode) {

    }


    @Override
    public void renderBrandDetails(List<CategoryItemsViewModel> categoryItemsViewModels, BrandViewModel brandViewModel) {
        collapsingToolbarLayout.setTitle(brandViewModel.getTitle());
        tvExpandableDesc.setText(brandViewModel.getDescription());
        ImageHandler.loadImageCover2(imageHeader, brandViewModel.getFeaturedImage());
        ImageHandler.loadImageCover2(brandLogo, brandViewModel.getFeaturedThumbnailImage());
        for (CategoryItemsViewModel categoryItemsViewModel : categoryItemsViewModels) {
            categoryItemsViewModel.setBrand(brandViewModel);
        }
        categoryAdapter = new DealsCategoryAdapter(getActivity(), categoryItemsViewModels, IS_SHORT_LAYOUT);

        Log.d("ListSizeee", " " + categoryItemsViewModels.size());
        numberofDeals.setText(String.format(getString(R.string.number_of_items), categoryItemsViewModels.size()));
        recyclerViewDeals.setAdapter(categoryAdapter);
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
        BrandViewModel brandViewModel = getIntent().getParcelableExtra(DealsBrandPresenter.BRAND_DATA);
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(mPresenter.TAG, brandViewModel.getUrl());
        return requestParams;
    }

    @Override
    public View getRootView() {
        return mainContent;
    }


}