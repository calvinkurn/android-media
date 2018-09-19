package com.tokopedia.browse.homepage.presentation.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
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
import com.tokopedia.browse.common.data.DigitalBrowseServiceAnalyticsModel;
import com.tokopedia.browse.common.util.DigitalBrowseAnalytics;
import com.tokopedia.browse.homepage.di.DigitalBrowseHomeComponent;
import com.tokopedia.browse.homepage.presentation.adapter.DigitalBrowseServiceAdapter;
import com.tokopedia.browse.homepage.presentation.adapter.DigitalBrowseServiceAdapterTypeFactory;
import com.tokopedia.browse.homepage.presentation.adapter.viewholder.DigitalBrowseServiceViewHolder;
import com.tokopedia.browse.homepage.presentation.contract.DigitalBrowseServiceContract;
import com.tokopedia.browse.homepage.presentation.model.DigitalBrowseServiceCategoryViewModel;
import com.tokopedia.browse.homepage.presentation.model.DigitalBrowseServiceViewModel;
import com.tokopedia.browse.homepage.presentation.presenter.DigitalBrowseServicePresenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @author by furqan on 30/08/18.
 */

public class DigitalBrowseServiceFragment extends BaseDaggerFragment
        implements DigitalBrowseServiceContract.View, DigitalBrowseServiceViewHolder.CategoryListener {

    private static final String EXTRA_CATEGORY_ID = "CATEGORY_ID";
    private static final int COLUMN_NUMBER = 4;
    private static final String KEY_SERVICE_DATA = "KEY_SERVICE_DATA";

    @Inject
    DigitalBrowseServicePresenter presenter;
    @Inject
    DigitalBrowseAnalytics digitalBrowseAnalytics;

    private TabLayout tabLayout;
    private RecyclerView rvCategory;
    private LinearSmoothScroller smoothScroller;
    private GridLayoutManager layoutManager;
    private LinearLayout containerData;

    private RecyclerView.OnScrollListener scrollSelected;
    private TabLayout.OnTabSelectedListener tabSelectedListener;

    private int oldTitlePosition = 0, currentTitlePosition = 0;
    private int currentScrollIndex = 0;

    private int selectedCategoryId = -1;

    private DigitalBrowseServiceViewModel viewModel;
    private DigitalBrowseServiceAdapter serviceAdapter;

    public static Fragment getFragmentInstance() {
        return new DigitalBrowseServiceFragment();
    }

    public static Fragment getFragmentInstance(int categoryId) {
        DigitalBrowseServiceFragment fragment = new DigitalBrowseServiceFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(EXTRA_CATEGORY_ID, categoryId);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_digital_browse_service, container, false);

        containerData = view.findViewById(R.id.container_data);
        tabLayout = view.findViewById(R.id.tab_layout);
        rvCategory = view.findViewById(R.id.recycler_view);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView();

        presenter.attachView(this);
        presenter.onInit();

        if (savedInstanceState != null) {
            viewModel = savedInstanceState.getParcelable(KEY_SERVICE_DATA);
        }

        if (getArguments() != null &&
                getArguments().containsKey(EXTRA_CATEGORY_ID)) {
            selectedCategoryId = getArguments().getInt(EXTRA_CATEGORY_ID);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(KEY_SERVICE_DATA, viewModel);
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {
        getComponent(DigitalBrowseHomeComponent.class).inject(this);
    }

    private void initView() {

        DigitalBrowseServiceAdapterTypeFactory adapterTypeFactory = new DigitalBrowseServiceAdapterTypeFactory(this);
        serviceAdapter = new DigitalBrowseServiceAdapter(adapterTypeFactory, new ArrayList<>());

        layoutManager = new GridLayoutManager(getContext(), COLUMN_NUMBER);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (serviceAdapter.isLoadingObject(position)) {
                    return 4;
                } else if (viewModel.getCategoryViewModelList().get(position).isTitle()) {
                    return 4;
                } else {
                    return 1;
                }
            }
        });
        smoothScroller = new LinearSmoothScroller(getContext()) {
            @Override
            protected int getVerticalSnapPreference() {
                return LinearSmoothScroller.SNAP_TO_START;
            }
        };

        tabSelectedListener = new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (!tab.getText().equals("")) {
                    digitalBrowseAnalytics.eventClickHeaderTabLayanan(tab.getText().toString());

                    rvCategory.removeOnScrollListener(scrollSelected);

                    oldTitlePosition = currentTitlePosition = currentScrollIndex =
                            viewModel.getTitleMap().get(tab.getText()).getIndexPositionInList();

                    smoothScroller.setTargetPosition(viewModel.getTitleMap().get(tab.getText())
                            .getIndexPositionInList());
                    layoutManager.startSmoothScroll(smoothScroller);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        };

        rvCategory.setLayoutManager(layoutManager);
        rvCategory.setHasFixedSize(true);
        rvCategory.setAdapter(serviceAdapter);

        serviceAdapter.showLoading();

    }

    @Override
    public void renderData(DigitalBrowseServiceViewModel viewModel) {
        serviceAdapter.clearAllElements();
        tabLayout.removeAllTabs();

        this.viewModel = viewModel;

        serviceAdapter.hideLoading();

        presenter.processTabData(viewModel.getTitleMap(), viewModel, selectedCategoryId);

        renderDataList(viewModel.getCategoryViewModelList());
        setRecyclerViewListener();
    }

    @Override
    public void showTab() {
        tabLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideTab() {
        tabLayout.setVisibility(View.GONE);
    }

    @Override
    public void addTab(String key) {
        tabLayout.addTab(tabLayout.newTab().setText(key));
    }

    private void renderDataList(List<DigitalBrowseServiceCategoryViewModel> dataList) {
        serviceAdapter.addElement(dataList);
    }

    @Override
    public void renderTab(int selectedTabIndex) {
        tabLayout.addOnTabSelectedListener(tabSelectedListener);
        tabLayout.getTabAt(selectedTabIndex).select();
    }

    @Override
    public void showGetDataError(Throwable e) {
        serviceAdapter.hideLoading();
        containerData.setVisibility(View.GONE);
        NetworkErrorHelper.showEmptyState(getActivity(), getActivity().getWindow().getDecorView().getRootView(),
                ErrorHandler.getErrorMessage(getContext(), e),
                new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        containerData.setVisibility(View.VISIBLE);
                        serviceAdapter.showLoading();
                        presenter.getDigitalCategoryCloud();
                    }
                });
    }

    @Override
    public int getItemCount() {
        return serviceAdapter.getItemCount();
    }

    private void setRecyclerViewListener() {
        scrollSelected = new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                currentScrollIndex = layoutManager.findFirstVisibleItemPosition();
                if (currentScrollIndex > -1 && viewModel != null &&
                        viewModel.getCategoryViewModelList() != null &&
                        viewModel.getCategoryViewModelList().size() > 0) {

                    if (viewModel.getCategoryViewModelList().get(currentScrollIndex).isTitle()) {
                        currentTitlePosition = currentScrollIndex;
                        int indexTab = viewModel.getTitleMap()
                                .get(viewModel.getCategoryViewModelList()
                                        .get(currentScrollIndex).getName()).getIndexPositionInTab();
                        tabLayout.removeOnTabSelectedListener(tabSelectedListener);
                        tabLayout.getTabAt(indexTab).select();
                        tabLayout.addOnTabSelectedListener(tabSelectedListener);
                        oldTitlePosition = currentTitlePosition;
                    } else {
                        if (currentScrollIndex < oldTitlePosition) {
                            tabLayout.removeOnTabSelectedListener(tabSelectedListener);
                            tabLayout.getTabAt(tabLayout.getSelectedTabPosition() - 1).select();
                            tabLayout.addOnTabSelectedListener(tabSelectedListener);

                            int indexList = viewModel.getTitleMap()
                                    .get(tabLayout.getTabAt(tabLayout.getSelectedTabPosition()).getText()).getIndexPositionInList();
                            oldTitlePosition = indexList;
                            currentTitlePosition = oldTitlePosition;
                        } else {
                            int indexTab = tabLayout.getSelectedTabPosition() + 1;
                            if (indexTab < tabLayout.getTabCount()) {
                                int indexList = viewModel.getTitleMap()
                                        .get(tabLayout.getTabAt(indexTab).getText()).getIndexPositionInList();
                                if (currentScrollIndex > indexList) {
                                    tabLayout.removeOnTabSelectedListener(tabSelectedListener);
                                    tabLayout.getTabAt(indexTab).select();
                                    tabLayout.addOnTabSelectedListener(tabSelectedListener);

                                    oldTitlePosition = indexList;
                                    currentTitlePosition = oldTitlePosition;
                                }
                            }
                        }
                    }
                }
            }
        };

        rvCategory.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (currentTitlePosition == layoutManager.findFirstVisibleItemPosition()) {
                    rvCategory.removeOnScrollListener(scrollSelected);
                    rvCategory.addOnScrollListener(scrollSelected);
                }
            }
        });

        rvCategory.addOnScrollListener(scrollSelected);
    }

    @Override
    public void onCategoryItemClicked(DigitalBrowseServiceCategoryViewModel viewModel, int itemPosition) {
        DigitalBrowseServiceAnalyticsModel analyticsModel = presenter.getItemPositionInGroup(this.viewModel.getTitleMap(), itemPosition);
        analyticsModel.setIconName(viewModel.getName());

        digitalBrowseAnalytics.eventClickIconLayanan(analyticsModel);

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
    public void sendImpressionAnalytics(DigitalBrowseServiceCategoryViewModel viewModel, int itemPosition) {
        DigitalBrowseServiceAnalyticsModel analyticsModel = presenter.getItemPositionInGroup(this.viewModel.getTitleMap(), itemPosition);
        analyticsModel.setIconName(viewModel.getName());

        digitalBrowseAnalytics.eventImpressionIconLayanan(analyticsModel);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestroyView();
    }
}
