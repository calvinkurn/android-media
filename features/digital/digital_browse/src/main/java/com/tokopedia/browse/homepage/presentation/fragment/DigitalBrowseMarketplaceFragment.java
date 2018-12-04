package com.tokopedia.browse.homepage.presentation.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.browse.R;
import com.tokopedia.browse.common.DigitalBrowseRouter;
import com.tokopedia.browse.common.data.DigitalBrowsePopularAnalyticsModel;
import com.tokopedia.browse.common.util.DigitalBrowseAnalytics;
import com.tokopedia.browse.homepage.di.DigitalBrowseHomeComponent;
import com.tokopedia.browse.homepage.presentation.adapter.DigitalBrowseMarketplaceAdapter;
import com.tokopedia.browse.homepage.presentation.adapter.DigitalBrowseMarketplaceAdapterTypeFactory;
import com.tokopedia.browse.homepage.presentation.adapter.viewholder.DigitalBrowseCategoryViewHolder;
import com.tokopedia.browse.homepage.presentation.adapter.viewholder.DigitalBrowsePopularViewHolder;
import com.tokopedia.browse.homepage.presentation.contract.DigitalBrowseMarketplaceContract;
import com.tokopedia.browse.homepage.presentation.model.DigitalBrowseMarketplaceViewModel;
import com.tokopedia.browse.homepage.presentation.model.DigitalBrowsePopularBrandsViewModel;
import com.tokopedia.browse.homepage.presentation.model.DigitalBrowseRowViewModel;
import com.tokopedia.browse.homepage.presentation.presenter.DigitalBrowseMarketplacePresenter;
import com.tokopedia.design.component.TextViewCompat;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @author by furqan on 30/08/18.
 */

public class DigitalBrowseMarketplaceFragment extends BaseDaggerFragment
        implements DigitalBrowseMarketplaceContract.View, DigitalBrowseCategoryViewHolder.CategoryListener,
        DigitalBrowsePopularViewHolder.PopularBrandListener {

    private static final int COLUMN_NUMBER = 4;
    private static final String OFFICIAL_STORES = "tokopedia://official-stores";
    private static final String KEY_MARKETPLACE_DATA = "KEY_MARKETPLACE_DATA";

    @Inject
    DigitalBrowseMarketplacePresenter presenter;
    @Inject
    DigitalBrowseAnalytics digitalBrowseAnalytics;

    private LinearLayout containerPopularBrand;
    private TextViewCompat tvAllPopularBrand;
    private RecyclerView rvPopularBrand;
    private RecyclerView rvCategory;

    private DigitalBrowseMarketplaceAdapter categoryAdapter;
    private DigitalBrowseMarketplaceAdapter popularAdapter;

    private DigitalBrowseMarketplaceViewModel digitalBrowseMarketplaceViewModel;


    public static Fragment getFragmentInstance() {
        return new DigitalBrowseMarketplaceFragment();
    }

    public DigitalBrowseMarketplaceFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_digital_browse_marketplace, container, false);

        containerPopularBrand = view.findViewById(R.id.container_popular_title);
        tvAllPopularBrand = view.findViewById(R.id.tv_all_popular);
        rvPopularBrand = view.findViewById(R.id.rv_popular_brand);
        rvCategory = view.findViewById(R.id.rv_category);

        tvAllPopularBrand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                digitalBrowseAnalytics.eventClickViewAllOnBelanjaPage();
                RouteManager.route(getContext(), OFFICIAL_STORES);
            }
        });

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        presenter.attachView(this);
        presenter.onInit();

        initializeCategoryView();
        initializePopularView();

        if (savedInstanceState != null) {
            digitalBrowseMarketplaceViewModel = savedInstanceState.getParcelable(KEY_MARKETPLACE_DATA);

            if (digitalBrowseMarketplaceViewModel != null) {
                renderCategory(digitalBrowseMarketplaceViewModel.getRowViewModelList());
                renderPopularBrands(digitalBrowseMarketplaceViewModel.getPopularBrandsList());
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(KEY_MARKETPLACE_DATA, digitalBrowseMarketplaceViewModel);
    }

    @Override
    protected void initInjector() {
        getComponent(DigitalBrowseHomeComponent.class).inject(this);
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    public void renderData(DigitalBrowseMarketplaceViewModel marketplaceData) {
        this.digitalBrowseMarketplaceViewModel = marketplaceData;

        renderCategory(marketplaceData.getRowViewModelList());
        renderPopularBrands(marketplaceData.getPopularBrandsList());
    }

    @Override
    public void showGetDataError(Throwable e) {
        categoryAdapter.hideLoading();
        NetworkErrorHelper.showEmptyState(getActivity(), getActivity().getWindow().getDecorView().getRootView(),
                ErrorHandler.getErrorMessage(getContext(), e),
                new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        categoryAdapter.showLoading();
                        presenter.getMarketplaceDataCloud();
                    }
                });
    }

    @Override
    public int getCategoryItemCount() {
        return categoryAdapter.getItemCount();
    }

    @Override
    public void sendPopularImpressionAnalytics(List<DigitalBrowsePopularAnalyticsModel> analyticsModelList) {
        digitalBrowseAnalytics.eventPromoImpressionPopularBrand(analyticsModelList);
    }

    private void initializeCategoryView() {
        tvAllPopularBrand.setVisibility(View.GONE);
        containerPopularBrand.setVisibility(View.GONE);
        rvPopularBrand.setVisibility(View.GONE);

        DigitalBrowseMarketplaceAdapterTypeFactory digitalBrowseMarketplaceAdapterTypeFactory = new DigitalBrowseMarketplaceAdapterTypeFactory(this, this);
        categoryAdapter = new DigitalBrowseMarketplaceAdapter(digitalBrowseMarketplaceAdapterTypeFactory, new ArrayList<>());
        popularAdapter = new DigitalBrowseMarketplaceAdapter(digitalBrowseMarketplaceAdapterTypeFactory, new ArrayList<>());

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), COLUMN_NUMBER);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (categoryAdapter.isLoadingObject(position)) {
                    return 4;
                } else {
                    return 1;
                }
            }
        });

        rvCategory.setLayoutManager(layoutManager);
        rvCategory.setHasFixedSize(true);
        rvCategory.setAdapter(categoryAdapter);

        categoryAdapter.showLoading();
    }

    private void initializePopularView() {
        hidePopularBrand();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        rvPopularBrand.setLayoutManager(layoutManager);
        rvPopularBrand.setHasFixedSize(false);
        rvPopularBrand.setAdapter(popularAdapter);
    }

    private void renderCategory(List<DigitalBrowseRowViewModel> digitalBrowseRowViewModels) {
        categoryAdapter.hideLoading();
        categoryAdapter.clearAllElements();
        categoryAdapter.addElement(digitalBrowseRowViewModels);
    }

    private void renderPopularBrands(List<DigitalBrowsePopularBrandsViewModel> digitalBrowsePopularBrandsViewModels) {
        showPopularBrand();

        popularAdapter.clearAllElements();
        popularAdapter.addElement(digitalBrowsePopularBrandsViewModels);
    }

    private void showPopularBrand() {
        containerPopularBrand.setVisibility(View.VISIBLE);
        tvAllPopularBrand.setVisibility(View.VISIBLE);
        rvPopularBrand.setVisibility(View.VISIBLE);
    }

    private void hidePopularBrand() {
        containerPopularBrand.setVisibility(View.GONE);
        tvAllPopularBrand.setVisibility(View.GONE);
        rvPopularBrand.setVisibility(View.GONE);
    }

    @Override
    public void onPopularItemClicked(DigitalBrowsePopularBrandsViewModel viewModel, int position) {
        digitalBrowseAnalytics.eventPromoClickPopularBrand(
                presenter.getPopularAnalyticsModel(viewModel, position));

        if (viewModel.getUrl() != null &&
                RouteManager.isSupportApplink(getContext(), viewModel.getUrl())) {
            RouteManager.route(getContext(), viewModel.getUrl());
        } else {
            if (getActivity().getApplication() instanceof DigitalBrowseRouter) {
                ((DigitalBrowseRouter) getActivity().getApplication())
                        .goToWebview(getActivity(), viewModel.getUrl());
            }
        }
    }

    @Override
    public void onCategoryItemClicked(DigitalBrowseRowViewModel viewModel, int itemPosition) {

        digitalBrowseAnalytics.eventClickOnCategoryBelanja(viewModel.getName(), itemPosition + 1);

        if (viewModel.getAppLinks() != null &&
                RouteManager.isSupportApplink(getContext(), viewModel.getAppLinks())) {
            RouteManager.route(getContext(), viewModel.getAppLinks());
        } else if (RouteManager.isSupportApplink(getContext(), viewModel.getUrl())) {
            RouteManager.route(getContext(), viewModel.getUrl());
        } else if (getActivity().getApplication() instanceof DigitalBrowseRouter) {
            ((DigitalBrowseRouter) getActivity().getApplication())
                    .goToWebview(getActivity(), viewModel.getUrl());
        }
    }

    @Override
    public void sendImpressionAnalytics(String iconName, int iconPosition) {
        digitalBrowseAnalytics.eventImpressionHomePage(iconName, iconPosition);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestroyView();
    }
}
