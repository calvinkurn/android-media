package com.tokopedia.digital_deals.view.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import java.util.List;

import javax.inject.Inject;

import static android.app.Activity.RESULT_OK;

public class DealsHomeFragment extends BaseDaggerFragment implements DealsContract.View, View.OnClickListener, DealsCategoryAdapter.INavigateToActivityRequest, DealsCategoryItemAdapter.CategorySelected, DealsLocationAdapter.ActionListener, CloseableBottomSheetDialog.OnCancelListener, SelectLocationBottomSheet.CloseSelectLocationBottomSheet {


    private Menu mMenu;
    @Inject
    public DealsHomePresenter mPresenter;
    private CoordinatorLayout mainContent;
    private View progressBarLayout;
    private ProgressBar progBar;
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
    private AppBarLayout appBarLayout;
    private LinearLayout curatedDealsLayout;
    private final boolean IS_SHORT_LAYOUT = false;
    OpenTrendingDeals openTrendingDeals;

    private ConstraintLayout clSearch;
    private TextView tvLocationName;
    private LinearLayoutManager layoutManager;
    private TextView tvSeeAllBrands;
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

        return view;
    }

    private void checkLocationStatus() {

        Location location = Utils.getSingletonInstance().getLocation(getActivity());

        if (location != null) {
            tvLocationName.setText(location.getName());
            mPresenter.getDealsList(true);

        } else {
//            mPresenter.getDealsList(false);
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
        toolbar = view.findViewById(R.id.deals_toolbar);
        catItems = view.findViewById(R.id.category_items);
        rvBrandItems = view.findViewById(R.id.rv_brand_items);
        rvTrendingDeals = view.findViewById(R.id.rv_trending_deals);
        mainContent = view.findViewById(R.id.main_content);
        progressBarLayout = view.findViewById(R.id.progress_bar_layout);
        progBar = view.findViewById(R.id.prog_bar);
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
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        trendingDeals = view.findViewById(R.id.cl_topDeals);
        rvTrendingDeals.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        rvTrendingDeals.setNestedScrollingEnabled(false);
        selectLocationFragment = CloseableBottomSheetDialog.createInstanceRounded(getActivity());
        dealsCategoryBottomSheet = CloseableBottomSheetDialog.createInstanceRounded(getActivity());

        Drawable img = getResources().getDrawable(R.drawable.ic_search_deal);
        setDrawableTint(img);

        Drawable threeDotMenu = ContextCompat.getDrawable(getContext(), R.drawable.ic_three_dot_menu);
        toolbar.setOverflowIcon(threeDotMenu);

        searchInputView.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);

        toolbar.setNavigationIcon(getActivity().getResources().getDrawable(R.drawable.ic_arrow_back_black));

        if (getActivity() instanceof DealsBaseActivity) {
            ((DealsBaseActivity) getActivity()).setSupportActionBar(toolbar);
            ((DealsBaseActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ((DealsBaseActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);

        }

        appBarLayout = view.findViewById(R.id.app_bar_layout);
        appBarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            if (verticalOffset == 0) {
                hideSearchButton();
            } else {
                showSearchButton();
            }
            Log.d("Offest Changed", "Offset : " + verticalOffset);
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
            case DealsHomeActivity.REQUEST_CODE_DEALSLOCATIONACTIVITY:
                Location location = Utils.getSingletonInstance().getLocation(getActivity());
                if (location == null) {
                    if (getActivity() != null)
                        getActivity().finish();
                } else {
                    if (data != null) {
                        if (isLocationUpdated) {
                            Utils.getSingletonInstance().showSnackBarDeals(location.getName(), getActivity(), mainContent, true);
                        }
                        mPresenter.getDealsList(true);
                    }
                    tvLocationName.setText(location.getName());
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
    public void renderCategoryList(List<CategoryItem> categoryList, CategoryItem
            carousel, CategoryItem top) {

        if (top.getItems() != null && top.getItems().size() > 0) {
            trendingDeals.setVisibility(View.VISIBLE);
            tvSeeAllTrendingDeals.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openTrendingDeals.replaceFragment(top.getItems(), 0);
                }
            });
            noContent.setVisibility(View.GONE);
            categoryAdapter = new DealsCategoryAdapter(null, DealsCategoryAdapter.HOME_PAGE, this, IS_SHORT_LAYOUT);
            categoryAdapter.setDealsHomeLayout(true);
            rvTrendingDeals.setAdapter(categoryAdapter);
            categoryAdapter.addAll(top.getItems(), false);
            categoryAdapter.notifyDataSetChanged();
        } else {
            mPresenter.sendEventView(DealsAnalytics.EVENT_NO_DEALS_AVAILABLE_ON_YOUR_LOCATION,
                    tvLocationName.getText().toString());
            trendingDeals.setVisibility(View.GONE);
            rvTrendingDeals.setVisibility(View.GONE);
            noContent.setVisibility(View.VISIBLE);
        }
        if (carousel.getItems() != null && carousel.getItems().size() > 0) {
            clPromos.setVisibility(View.VISIBLE);
            rvPromos.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
            rvPromos.setAdapter(new PromoAdapter(carousel.getItems(), mPresenter));
        } else {
            clPromos.setVisibility(View.GONE);
        }
        if (categoryList != null) {
            catItems.removeAllViews();
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.weight = 1.0f;
            View view;
            ImageView imageViewCatItem;
            TextView textViewCatItem;

            for (int position = 0; position < 4; position++) {
                if (categoryList.get(position).getIsCard() != 1) {
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

    @Override
    public void renderBrandList(List<Brand> brandList) {
        int spanCount = 2;
        if (brandList != null) {
            if (brandList.size() == 1) {
                spanCount = 1;
            }
            clBrands.setVisibility(View.VISIBLE);
            rvBrandItems.setLayoutManager(new GridLayoutManager(getActivity(), spanCount,
                    GridLayoutManager.HORIZONTAL, false));
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
            noContent.setVisibility(View.GONE);
            for (CategoryItem categoryItem : categoryItems) {
                CuratedDealsView curatedDealsView = new CuratedDealsView(getActivity(), categoryItem, openTrendingDeals, "");
                curatedDealsLayout.addView(curatedDealsView);
            }
        } else {
            curatedDealsLayout.setVisibility(View.GONE);
        }
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
        requestParams.putString(DealsHomePresenter.TAG, location.getSearchName());
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
        progBar.setVisibility(View.VISIBLE);
        progressBarLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        progBar.setVisibility(View.GONE);
        progressBarLayout.setVisibility(View.GONE);
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
            }
            mPresenter.getDealsList(true);
            tvLocationName.setText(location.getName());
            if (selectLocationFragment != null) {
                selectLocationFragment.dismiss();
            }
        }
    }

    @Override
    public void startLocationFragment(List<Location> locationList, boolean isForFirstime) {
        Utils.getSingletonInstance().updateLocation(getContext(), locationList.get(0));
        mPresenter.getDealsList(false);
        selectLocationFragment.setContentView(new SelectLocationBottomSheet(getContext(), isForFirstime, locationList, this, tvLocationName.getText().toString(), this));
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
        dealsCategoryBottomSheet.setContentView(categoryView);
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
        void replaceFragment(List<ProductItem> trendingDeals, int flag);
    }
}
