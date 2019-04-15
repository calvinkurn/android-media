package com.tokopedia.digital_deals.view.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AlignmentSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.common.network.util.NetworkClient;
import com.tokopedia.design.bottomsheet.CloseableBottomSheetDialog;
import com.tokopedia.digital_deals.DealsModuleRouter;
import com.tokopedia.digital_deals.R;
import com.tokopedia.digital_deals.di.DealsComponent;
import com.tokopedia.digital_deals.view.activity.CategoryDetailActivity;
import com.tokopedia.digital_deals.view.activity.DealsBaseActivity;
import com.tokopedia.digital_deals.view.activity.DealsHomeActivity;
import com.tokopedia.digital_deals.view.adapter.DealsBrandAdapter;
import com.tokopedia.digital_deals.view.adapter.DealsCategoryAdapter;
import com.tokopedia.digital_deals.view.adapter.DealsCategoryItemAdapter;
import com.tokopedia.digital_deals.view.adapter.DealsLocationAdapter;
import com.tokopedia.digital_deals.view.adapter.PromoAdapter;
import com.tokopedia.digital_deals.view.contractor.DealsContract;
import com.tokopedia.digital_deals.view.model.Brand;
import com.tokopedia.digital_deals.view.model.CategoriesModel;
import com.tokopedia.digital_deals.view.model.CategoryItem;
import com.tokopedia.digital_deals.view.model.Location;
import com.tokopedia.digital_deals.view.model.ProductItem;
import com.tokopedia.digital_deals.view.presenter.DealsHomePresenter;
import com.tokopedia.digital_deals.view.utils.CuratedDealsView;
import com.tokopedia.digital_deals.view.utils.DealsAnalytics;
import com.tokopedia.digital_deals.view.utils.Utils;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import static android.app.Activity.RESULT_OK;

public class DealsHomeFragment extends BaseDaggerFragment implements DealsContract.View, View.OnClickListener, DealsCategoryAdapter.INavigateToActivityRequest, DealsCategoryItemAdapter.CategorySelected, DealsLocationAdapter.ActionListener, CloseableBottomSheetDialog.OnCancelListener, SelectLocationBottomSheet.CloseSelectLocationBottomSheet, PopupMenu.OnMenuItemClickListener {


    private Menu mMenu;
    @Inject
    public DealsHomePresenter mPresenter;
    private CoordinatorLayout mainContent;
    private LinearLayout shimmerLayout;
    private Toolbar toolbar;
    private LinearLayout catItems;
    private RecyclerView rvTrendingDeals;
    private RecyclerView rvBrandItems;
    private RecyclerView rvPromos;
    private CoordinatorLayout baseMainContent;
    private LinearLayout noContent;
    private ConstraintLayout clBrands;
    private ConstraintLayout clPromos;
    private ConstraintLayout trendingDeals;
    private TextView searchInputView;
    private ImageView backArrow, overFlowIcon;
    private AppBarLayout appBarLayout;
    private NestedScrollView nestedScrollView;
    private LinearLayout curatedDealsLayout;
    private final boolean IS_SHORT_LAYOUT = false;
    OpenTrendingDeals openTrendingDeals;

    private ConstraintLayout clSearch;
    private TextView tvLocationName;
    private LinearLayoutManager layoutManager;
    private TextView tvSeeAllBrands;
    private TextView toolbarTitle;
    private TextView tvSeeAllPromo;
    private TextView tvSeeAllTrendingDeals;
    private int adapterPosition = -1;
    private boolean forceRefresh;
    private DealsCategoryAdapter categoryAdapter;
    private CloseableBottomSheetDialog dealsCategoryBottomSheet;
    private CloseableBottomSheetDialog selectLocationFragment;
    private RecyclerView rvSearchResults;
    public static boolean isLocationUpdated = false;

    public static Fragment createInstance() {
        Fragment fragment = new DealsHomeFragment();
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_deals_home, container, false);

        setHasOptionsMenu(true);
        setUpVariables(view);
        checkLocationStatus();

        categoryAdapter = new DealsCategoryAdapter(null, DealsCategoryAdapter.HOME_PAGE, this, IS_SHORT_LAYOUT);
        return view;
    }

    private void checkLocationStatus() {

        Location location = Utils.getSingletonInstance().getLocation(getActivity());

        if (location != null) {
            tvLocationName.setText(location.getName());
            mPresenter.getDealsList(true);

        } else {
            mPresenter.getLocations(true);
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof DealsHomeActivity)
            openTrendingDeals = (OpenTrendingDeals) context;
    }

    private void setUpVariables(View view) {
        shimmerLayout = view.findViewById(R.id.shimmer_layout);
        toolbar = view.findViewById(R.id.deals_toolbar);
        toolbarTitle = view.findViewById(R.id.toolbar_title);
        backArrow = view.findViewById(R.id.backArraw);
        overFlowIcon = view.findViewById(R.id.overFlow_icon);
        catItems = view.findViewById(R.id.category_items);
        rvBrandItems = view.findViewById(R.id.rv_brand_items);
        rvTrendingDeals = view.findViewById(R.id.rv_trending_deals);
        mainContent = view.findViewById(R.id.main_content);
        baseMainContent = view.findViewById(R.id.base_main_content);
        searchInputView = view.findViewById(R.id.search_input_view);
        tvLocationName = view.findViewById(R.id.tv_location_name);
        clBrands = view.findViewById(R.id.cl_brands);
        clPromos = view.findViewById(R.id.cl_promos);
        rvPromos = view.findViewById(R.id.rv_trending_promos);
        noContent = view.findViewById(R.id.no_content);
        clSearch = view.findViewById(R.id.cl_search_view);
        tvSeeAllBrands = view.findViewById(R.id.tv_see_all_brands);
        tvSeeAllTrendingDeals = view.findViewById(R.id.tv_see_all_deals);
        curatedDealsLayout = view.findViewById(R.id.curated_deals);
        tvSeeAllBrands.setOnClickListener(this);
        searchInputView.setOnClickListener(this);
        tvLocationName.setOnClickListener(this);
        toolbarTitle.setOnClickListener(this);
        nestedScrollView = view.findViewById(R.id.nested_scroll_view);
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        trendingDeals = view.findViewById(R.id.cl_topDeals);
        rvTrendingDeals.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        rvTrendingDeals.setNestedScrollingEnabled(false);
        selectLocationFragment = CloseableBottomSheetDialog.createInstanceRounded(getActivity());
        dealsCategoryBottomSheet = CloseableBottomSheetDialog.createInstanceRounded(getActivity());

        Drawable img = getResources().getDrawable(R.drawable.ic_search_deal);
        setDrawableTint(img);

        searchInputView.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);


        appBarLayout = view.findViewById(R.id.app_bar_layout);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset == 0) {
                    DealsHomeFragment.this.hideSearchButton();
                } else {
                    DealsHomeFragment.this.showSearchButton();
                }
                Log.d("Offest Changed", "Offset : " + verticalOffset);
            }
        });

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == 0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        appBarLayout.setElevation(0.0f);
                    }
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        appBarLayout.setElevation(4.0f);
                    }
                }
            }
        });
        overFlowIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(getActivity(), v);
                MenuInflater menuInflater = popupMenu.getMenuInflater();
                menuInflater.inflate(R.menu.menu_deals_home, popupMenu.getMenu());
                mMenu = popupMenu.getMenu();
                for (int i = 0; i < mMenu.size(); i++) {
                    MenuItem item = popupMenu.getMenu().getItem(i);
                    SpannableString s = new SpannableString(item.getTitle());
                    s.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, s.length(), 0);
                    s.setSpan(new StyleSpan(Typeface.NORMAL), 0, s.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    item.setTitle(s);
                }
                popupMenu.setOnMenuItemClickListener(DealsHomeFragment.this);
                popupMenu.show();
            }
        });

    }


    private void setDrawableTint(Drawable img) {
        Drawable wrappedDrawable = DrawableCompat.wrap(img);
        Drawable mutableDrawable = wrappedDrawable.mutate();
        DrawableCompat.setTint(mutableDrawable, ContextCompat.getColor(getContext(), R.color.color_search_icon));
    }

    public void hideSearchButton() {
        if (mMenu != null) {
            MenuItem item = mMenu.findItem(R.id.action_menu_search);
            item.setVisible(false);
            item.setEnabled(false);
        }
    }

    public void showSearchButton() {
        if (mMenu != null) {
            MenuItem item = mMenu.findItem(R.id.action_menu_search);
            item.setVisible(true);
            item.setEnabled(true);
        }
    }

    @Override
    protected void initInjector() {
        NetworkClient.init(getActivity());
        getComponent(DealsComponent.class).inject(this);
        mPresenter.attachView(this);
        mPresenter.initialize();
    }


    @Override
    public void navigateToActivityRequest(Intent intent, int requestCode) {
        startActivityForResult(intent, requestCode);
    }

    @Override
    public void navigateToActivity(Intent intent) {
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case DealsHomeActivity.REQUEST_CODE_DEALSSEARCHACTIVITY:
                if (resultCode == RESULT_OK) {
                    Location location1 = Utils.getSingletonInstance().getLocation(getActivity());
                    if (!tvLocationName.getText().equals(location1.getName())) {
                        tvLocationName.setText(location1.getName());
                        mPresenter.getDealsList(true);
                    }
                }
                break;
            case DealsHomeActivity.REQUEST_CODE_DEALDETAILACTIVITY:
                if (resultCode == RESULT_OK) {
                    Location location1 = Utils.getSingletonInstance().getLocation(getActivity());
                    if (!tvLocationName.getText().equals(location1.getName())) {
                        tvLocationName.setText(location1.getName());
                        mPresenter.getDealsList(true);
                    } else {
                        mPresenter.getDealsList(false);
                    }

                }
                break;
            case DealsHomeActivity.REQUEST_CODE_LOGIN:
                if (resultCode == RESULT_OK) {
                    if (getActivity() != null && getActivity().getApplication() != null) {
                        UserSessionInterface userSession = new UserSession(getActivity());
                        if (userSession.isLoggedIn()) {
                            if (adapterPosition == -1) {
                                startOrderListActivity();
                            } else {
                                if (rvTrendingDeals.getAdapter() != null)
                                    ((DealsCategoryAdapter) rvTrendingDeals.getAdapter()).setLike(adapterPosition);
                            }
                        }
                    }
                }
                break;

        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void renderCategoryList(List<CategoryItem> categoryList) {
        if (categoryList != null) {
            applyFilterOnCategories(categoryList);
            catItems.removeAllViews();
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.weight = 0.5f;
            View view;
            ImageView imageViewCatItem;
            TextView textViewCatItem;

            for (int position = 0; position < 4; position++) {
                LayoutInflater inflater = getLayoutInflater();
                view = inflater.inflate(R.layout.category_item, mainContent, false);
                imageViewCatItem = view.findViewById(R.id.iv_category);
                textViewCatItem = view.findViewById(R.id.tv_category);
                textViewCatItem.setText(categoryList.get(position).getTitle());
                ImageHandler.loadImage(getActivity(), imageViewCatItem, categoryList.get(position).getMediaUrl(), R.color.grey_1100, R.color.grey_1100);
                final int position1 = position;
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CategoriesModel categoriesModel = new CategoriesModel();
                        categoriesModel.setName(categoryList.get(position1).getName());
                        categoriesModel.setTitle(categoryList.get(position1).getTitle());
                        categoriesModel.setCategoryUrl(categoryList.get(position1).getCategoryUrl());
                        categoriesModel.setCategoryId(categoryList.get(position1).getCategoryId());
                        categoriesModel.setPosition(position1);
                        openCategoryDetail(categoriesModel);
                    }
                });
                view.setLayoutParams(params);
                catItems.addView(view);
            }
            LayoutInflater inflater = getLayoutInflater();
            view = inflater.inflate(R.layout.category_item, mainContent, false);
            imageViewCatItem = view.findViewById(R.id.iv_category);
            textViewCatItem = view.findViewById(R.id.tv_category);
            textViewCatItem.setText(getContext().getResources().getString(R.string.sell_all_category));
            imageViewCatItem.setImageResource(R.drawable.ic_semua_kategori);
            view.setLayoutParams(params);
            catItems.addView(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startDealsCategoryFragment(categoryList);
                }
            });
        }
    }

    public void applyFilterOnCategories(List<CategoryItem> categoryRespons) {
        Map<Integer, Integer> sortOrder = new HashMap<>();
        for (CategoryItem categoryItem : categoryRespons) {
            sortOrder.put(categoryItem.getCategoryId(), categoryItem.getPriority());
            if (sortOrder.size() == categoryRespons.size()) {
                Collections.sort(categoryRespons, new CategoryItemComparator(sortOrder));
            }
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int id = item.getItemId();
        return mPresenter.onOptionMenuClick(id);
    }

    private class CategoryItemComparator implements Comparator<CategoryItem> {
        private Map<Integer, Integer> sortOrder;

        public CategoryItemComparator(Map<Integer, Integer> sortOrder) {
            this.sortOrder = sortOrder;
        }

        @Override
        public int compare(CategoryItem i1, CategoryItem i2) {
            Integer id1 = sortOrder.get(i1.getCategoryId());
            if (id1 == null) {
                throw new IllegalArgumentException("Bad id encountered: " +
                        i1.getCategoryId());
            }
            Integer id2 = sortOrder.get(i2.getCategoryId());
            if (id2 == null) {
                throw new IllegalArgumentException("Bad id encountered: " +
                        i2.getCategoryId());
            }
            return id2.compareTo(id1);
        }
    }

    @Override
    public void renderTopDeals(CategoryItem categoryItem) {
        if (categoryItem.getItems() != null && categoryItem.getItems().size() > 0) {
            trendingDeals.setVisibility(View.VISIBLE);
            if (categoryItem.getItems().size() >= 9) {
                tvSeeAllTrendingDeals.setVisibility(View.VISIBLE);
                tvSeeAllTrendingDeals.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!TextUtils.isEmpty(categoryItem.getCategoryUrl())) {
                            mPresenter.getAllTrendingDeals(categoryItem.getCategoryUrl(), getContext().getResources().getString(R.string.trending_deals));
                        }
                    }
                });
            }
            noContent.setVisibility(View.GONE);
            categoryAdapter.clearList();
            categoryAdapter.setDealsHomeLayout(true);
            rvTrendingDeals.setAdapter(categoryAdapter);
            categoryAdapter.addAll(categoryItem.getItems(), true);
            rvTrendingDeals.setVisibility(View.VISIBLE);
            categoryAdapter.notifyDataSetChanged();
        } else {
            mPresenter.sendEventView(DealsAnalytics.EVENT_NO_DEALS_AVAILABLE_ON_YOUR_LOCATION,
                    tvLocationName.getText().toString());
            trendingDeals.setVisibility(View.GONE);
            rvTrendingDeals.setVisibility(View.GONE);
            noContent.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void renderCarousels(CategoryItem carousel) {
        if (carousel.getItems() != null && carousel.getItems().size() > 0) {
            clPromos.setVisibility(View.VISIBLE);
            rvPromos.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
            rvPromos.setAdapter(new PromoAdapter(carousel.getItems(), mPresenter));
        } else {
            clPromos.setVisibility(View.GONE);
        }
    }

    @Override
    public void renderBrandList(List<Brand> brandList) {
        if (brandList != null) {
            clBrands.setVisibility(View.VISIBLE);
            rvBrandItems.setLayoutManager(new LinearLayoutManager(getActivity(),
                    LinearLayoutManager.HORIZONTAL, false));
            DealsBrandAdapter dealsBrandAdapter = new DealsBrandAdapter(brandList, DealsBrandAdapter.ITEM_BRAND_HOME);
            dealsBrandAdapter.setPopularBrands(true);
            rvBrandItems.setAdapter(dealsBrandAdapter);

        } else {
            clBrands.setVisibility(View.GONE);
        }
    }

    @Override
    public void renderCuratedDealsList(List<CategoryItem> categoryItems) {
        if (categoryItems != null && categoryItems.size() > 0) {
            curatedDealsLayout.setVisibility(View.VISIBLE);
            curatedDealsLayout.removeAllViews();
            boolean isItemsAvailable = false;
            for (CategoryItem categoryItem : categoryItems) {
                if (categoryItem.getItems() != null && categoryItem.getItems().size() > 0) {
                    isItemsAvailable = true;
                    DealsCategoryAdapter.INavigateToActivityRequest listener = new DealsCategoryAdapter.INavigateToActivityRequest() {
                        @Override
                        public void onNavigateToActivityRequest(Intent intent, int requestCode, int position) {
                            startActivityForResult(intent, requestCode);
                        }
                    };
                    CuratedDealsView curatedDealsView = new CuratedDealsView(getActivity(), categoryItem, listener, openTrendingDeals, "", mPresenter);
                    curatedDealsLayout.addView(curatedDealsView);
                }
            }
            if (!isItemsAvailable) {
                curatedDealsLayout.setVisibility(View.GONE);
            }
        } else {
            curatedDealsLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void renderAllTrendingDeals(List<ProductItem> items, String title) {
        openTrendingDeals.replaceFragment(items, 0, title);
    }

    @Override
    public void addDealsToCards(CategoryItem top) {
        if (top.getItems() != null) {
            ((DealsCategoryAdapter) rvTrendingDeals.getAdapter()).addAll(top.getItems());
        }
    }

    @Override
    public LinearLayoutManager getLayoutManager() {
        return layoutManager;
    }

    @Override
    public void removeFooter() {
        ((DealsCategoryAdapter) rvTrendingDeals.getAdapter()).removeFooter();
    }

    @Override
    public void addFooter() {
        ((DealsCategoryAdapter) rvTrendingDeals.getAdapter()).addFooter();
    }

    @Override
    public void showViews() {
        baseMainContent.setVisibility(View.VISIBLE);
        clSearch.setVisibility(View.VISIBLE);
    }

    @Override
    public void startOrderListActivity() {
        RouteManager.route(getActivity(), ApplinkConst.DEALS_ORDER);
    }

    @Override
    public int getRequestCode() {
        return DealsHomeActivity.REQUEST_CODE_LOGIN;
    }

    @Override
    public RequestParams getParams() {
        RequestParams requestParams = RequestParams.create();
        Location location = Utils.getSingletonInstance().getLocation(getActivity());
        requestParams.putInt(DealsHomePresenter.TAG, location.getId());
        return requestParams;
    }

    @Override
    public RequestParams getBrandParams() {
        Location location = Utils.getSingletonInstance().getLocation(getActivity());
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(Utils.BRAND_QUERY_PARAM_TREE, Utils.BRAND_QUERY_PARAM_BRAND);
        requestParams.putInt(Utils.QUERY_PARAM_CITY_ID, location.getId());
        return requestParams;
    }

    @Override
    public View getRootView() {
        return mainContent;
    }

    @Override
    public void showProgressBar() {
        shimmerLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        shimmerLayout.setVisibility(View.GONE);
    }

    @Override
    public void hideFavouriteButton() {
        MenuItem item = mMenu.findItem(R.id.action_menu_favourite);
        item.setVisible(false);
    }

    @Override
    public void showFavouriteButton() {
        MenuItem item = mMenu.findItem(R.id.action_menu_favourite);
        item.setVisible(false);
    }

    @Override
    public void startGeneralWebView(String url) {
        ((DealsModuleRouter) getActivity().getApplication())
                .actionOpenGeneralWebView(getActivity(), url);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_deals_home, menu);
        mMenu = menu;
        for (int i = 0; i < mMenu.size(); i++) {
            MenuItem item = menu.getItem(i);
            SpannableString s = new SpannableString(item.getTitle());
            s.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, s.length(), 0);
            s.setSpan(new StyleSpan(Typeface.NORMAL), 0, s.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            item.setTitle(s);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return mPresenter.onOptionMenuClick(id);
    }

    @Override
    public void onClick(View v) {
        mPresenter.onOptionMenuClick(v.getId());
    }

    @Override
    public void onDestroy() {
        mPresenter.onDestroy();
        try {
            if (rvTrendingDeals.getAdapter() != null) {
                ((DealsCategoryAdapter) rvTrendingDeals.getAdapter()).unsubscribeUseCase();
            }
        } catch (Exception e) {

        }
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
            if (categoryAdapter != null)
                categoryAdapter.notifyDataSetChanged();
            forceRefresh = false;
        }
    }

    @Override
    public void onLocationItemSelected(boolean locationUpdated) {
        Location location = Utils.getSingletonInstance().getLocation(getActivity());
        if (location == null) {
            if (getActivity() != null)
                getActivity().finish();
        } else {
            if (locationUpdated) {
                isLocationUpdated = locationUpdated;
                selectLocationFragment.setCanceledOnTouchOutside(true);
                Utils.getSingletonInstance().showSnackBarDeals(location.getName(), getActivity(), mainContent, true);
                mPresenter.getDealsList(true);
                tvLocationName.setText(location.getName());
            }
            if (selectLocationFragment != null) {
                selectLocationFragment.dismiss();
            }
        }
    }

    @Override
    public void startLocationFragment(List<Location> locationList, boolean isForFirstime) {
        Utils.getSingletonInstance().updateLocation(getContext(), locationList.get(0));
        if (isForFirstime) {
            mPresenter.getDealsList(false);
        }
        selectLocationFragment.setCustomContentView(new SelectLocationBottomSheet(getContext(), isForFirstime, locationList, this, tvLocationName.getText().toString(), this), "", false);
        selectLocationFragment.show();
        if (isForFirstime) {
            selectLocationFragment.setCanceledOnTouchOutside(false);
        }
    }

    @Override
    public void startDealsCategoryFragment(List<CategoryItem> categoryItems) {
        View categoryView = getLayoutInflater().inflate(R.layout.deals_category_bottomsheet_layout, null);
        RecyclerView recyclerView = categoryView.findViewById(R.id.rv_category_items);
        ImageView crossIcon = categoryView.findViewById(R.id.cross_icon_bottomsheet);
        crossIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dealsCategoryBottomSheet.dismiss();
            }
        });
        DealsCategoryItemAdapter adapter = new DealsCategoryItemAdapter(categoryItems, this);
        recyclerView.setAdapter(adapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 5,
                GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        dealsCategoryBottomSheet.setCustomContentView(categoryView, "", false);
        dealsCategoryBottomSheet.show();
        dealsCategoryBottomSheet.setCanceledOnTouchOutside(true);
    }

    @Override
    public void openCategoryDetail(CategoriesModel categoriesModel) {
        if (dealsCategoryBottomSheet != null) {
            dealsCategoryBottomSheet.dismiss();
        }
        Intent detailsIntent = new Intent(getActivity(), CategoryDetailActivity.class);
        detailsIntent.putExtra(CategoryDetailActivity.CATEGORIES_DATA, categoriesModel);
        detailsIntent.putExtra(CategoryDetailActivity.CATEGORY_NAME, categoriesModel.getTitle());
        detailsIntent.putExtra(CategoryDetailActivity.FROM_HOME, true);
        getActivity().startActivity(detailsIntent);
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        dealsCategoryBottomSheet.dismiss();
        selectLocationFragment.dismiss();
    }

    @Override
    public void closeBottomsheet() {
        if (selectLocationFragment != null) {
            selectLocationFragment.dismiss();
        }
    }

    public interface OpenTrendingDeals {
        void replaceFragment(List<ProductItem> trendingDeals, int flag, String title);
    }
}
