package com.tokopedia.digital_deals.view.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.google.android.material.appbar.AppBarLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.Layout;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AlignmentSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.LocalCacheHandler;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.abstraction.constant.TkpdCache;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal;
import com.tokopedia.coachmark.CoachMark;
import com.tokopedia.coachmark.CoachMarkBuilder;
import com.tokopedia.coachmark.CoachMarkContentPosition;
import com.tokopedia.coachmark.CoachMarkItem;
import com.tokopedia.common.network.util.NetworkClient;
import com.tokopedia.design.bottomsheet.CloseableBottomSheetDialog;
import com.tokopedia.digital_deals.di.DealsComponent;
import com.tokopedia.digital_deals.view.activity.AllBrandsActivity;
import com.tokopedia.digital_deals.view.activity.CategoryDetailActivity;
import com.tokopedia.digital_deals.view.activity.DealsHomeActivity;
import com.tokopedia.digital_deals.view.adapter.DealsBrandAdapter;
import com.tokopedia.digital_deals.view.adapter.DealsCategoryAdapter;
import com.tokopedia.digital_deals.view.adapter.DealsCategoryItemAdapter;
import com.tokopedia.digital_deals.view.adapter.PromoAdapter;
import com.tokopedia.digital_deals.view.contractor.DealsContract;
import com.tokopedia.digital_deals.view.model.Brand;
import com.tokopedia.digital_deals.view.model.CategoriesModel;
import com.tokopedia.digital_deals.view.model.CategoryItem;
import com.tokopedia.digital_deals.view.model.Location;
import com.tokopedia.digital_deals.view.presenter.DealsHomePresenter;
import com.tokopedia.digital_deals.view.utils.CuratedDealsView;
import com.tokopedia.digital_deals.view.utils.CurrentLocationCallBack;
import com.tokopedia.digital_deals.view.utils.DealsAnalytics;
import com.tokopedia.digital_deals.view.utils.Utils;
import com.tokopedia.permissionchecker.PermissionCheckerHelper;
import com.tokopedia.unifycomponents.Toaster;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static android.app.Activity.RESULT_OK;

public class DealsHomeFragment extends BaseDaggerFragment implements DealsContract.View, View.OnClickListener, DealsCategoryAdapter.INavigateToActivityRequest, DealsCategoryItemAdapter.CategorySelected, CloseableBottomSheetDialog.OnCancelListener, PopupMenu.OnMenuItemClickListener {

    private final long SHOW_CASE_DELAY = 400;
    private final String SCREEN_NAME = "/digital/deals/homepage";
    private static final String HOME_FRAGMENT = "DealsHomeFragment";
    private static final String LOCATION_UPDATE = "isLocationUpdated";

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
    private LinearLayout toolbarNameLayout;
    private TextView topDealsHeading;
    private final boolean IS_SHORT_LAYOUT = false;
    OpenTrendingDeals openTrendingDeals;
    CurrentLocationCallBack currentLocationCallBack;

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
    private CategoryItem categoryItem;
    private boolean isFirstTime = false;
    private TextView promoheading;
    private UserSession userSession;
    private PermissionCheckerHelper permissionCheckerHelper;

    public static DealsHomeFragment createInstance(boolean isLocationUpdated) {
        DealsHomeFragment fragment = new DealsHomeFragment();
        Bundle args = new Bundle();
        args.putBoolean(LOCATION_UPDATE, isLocationUpdated);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            isLocationUpdated = getArguments().getBoolean(LOCATION_UPDATE);
        }
        permissionCheckerHelper = new PermissionCheckerHelper();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(com.tokopedia.digital_deals.R.layout.fragment_deals_home, container, false);

        setHasOptionsMenu(true);
        setUpVariables(view);
        checkLocationStatus();

        categoryAdapter = new DealsCategoryAdapter(null, DealsCategoryAdapter.HOME_PAGE, this, IS_SHORT_LAYOUT);
        userSession = new UserSession(getActivity());
        if (userSession.isLoggedIn()) {
            mPresenter.sendNSQEvent(userSession.getUserId(), "home-page");
        }
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Location location = Utils.getSingletonInstance().getLocation(getActivity());
        if (location != null && !tvLocationName.getText().equals(location.getName())) {
            KeyboardHandler.hideSoftKeyboard(getActivity());
            tvLocationName.setText(location.getName());
            mPresenter.getDealsList(true);
            mPresenter.getBrandsHome();
        }
        mPresenter.sendScreenNameEvent(getScreenName());
    }

    private void checkLocationStatus() {

        permissionCheckerHelper.checkPermission(DealsHomeFragment.this, PermissionCheckerHelper.Companion.PERMISSION_ACCESS_FINE_LOCATION, new PermissionCheckerHelper.PermissionCheckListener() {
            @Override
            public void onPermissionDenied(String permissionText) {
                setDefaultLocation();
            }

            @Override
            public void onNeverAskAgain(String permissionText) {
            }

            @Override
            public void onPermissionGranted() {
                Utils.getSingletonInstance().detectAndSendLocation(getActivity(), permissionCheckerHelper, currentLocationCallBack);
            }
        }, getContext().getResources().getString(com.tokopedia.digital_deals.R.string.deals_use_current_location));
    }

    private void startShowCase() {
        ArrayList<CoachMarkItem> coachItems = new ArrayList<>();
        coachItems.add(new CoachMarkItem(toolbarNameLayout, getString(com.tokopedia.digital_deals.R.string.coachicon_title_location), getString(com.tokopedia.digital_deals.R.string.coachicon_description_location)));
        coachItems.add(new CoachMarkItem(searchInputView, getString(com.tokopedia.digital_deals.R.string.coachicon_title_searchbar), getString(com.tokopedia.digital_deals.R.string.coachicon_description_searchbar)));
        coachItems.add(new CoachMarkItem(promoheading, getString(com.tokopedia.digital_deals.R.string.coachicon_title_promo), getString(com.tokopedia.digital_deals.R.string.coachicon_description_promo), CoachMarkContentPosition.BOTTOM, com.tokopedia.design.R.color.white, nestedScrollView));
        CoachMark coachMark = new CoachMarkBuilder().build();
        coachMark.setShowCaseStepListener(new CoachMark.OnShowCaseStepListener() {
            @Override
            public boolean onShowCaseGoTo(int prev, int next, @NotNull CoachMarkItem coachMarkItem) {
                if (next == 2) {
                    nestedScrollView.smoothScrollTo(0, curatedDealsLayout.getBottom());
                }
                return false;
            }
        });
        if (!Utils.hasShown(getActivity(), DealsHomeFragment.class.getName())) {
            coachMark.show(getActivity(), "SampleCoachMark", coachItems);
            Utils.setShown(getActivity(), DealsHomeFragment.class.getName(), true);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof DealsHomeActivity) {
            openTrendingDeals = (OpenTrendingDeals) context;
            currentLocationCallBack = (CurrentLocationCallBack) context;
        }
    }

    private void setUpVariables(View view) {
        shimmerLayout = view.findViewById(com.tokopedia.digital_deals.R.id.shimmer_layout);
        ((TextView)shimmerLayout.findViewById(com.tokopedia.digital_deals.R.id.location_arrow_down_tv)).setCompoundDrawablesWithIntrinsicBounds(null, null, MethodChecker.getDrawable
                (getActivity(), com.tokopedia.digital_deals.R.drawable.location_arrow_down), null);
        toolbar = view.findViewById(com.tokopedia.digital_deals.R.id.deals_toolbar);
        toolbarNameLayout = view.findViewById(com.tokopedia.digital_deals.R.id.toolbar_home_layout);
        toolbarTitle = view.findViewById(com.tokopedia.digital_deals.R.id.toolbar_title);
        toolbarTitle.setCompoundDrawablesWithIntrinsicBounds(null, null, MethodChecker.getDrawable
                (getActivity(), com.tokopedia.digital_deals.R.drawable.location_arrow_down), null);
        backArrow = view.findViewById(com.tokopedia.digital_deals.R.id.backArraw);
        overFlowIcon = view.findViewById(com.tokopedia.digital_deals.R.id.overFlow_icon);
        catItems = view.findViewById(com.tokopedia.digital_deals.R.id.category_items);
        rvBrandItems = view.findViewById(com.tokopedia.digital_deals.R.id.rv_brand_items);
        rvTrendingDeals = view.findViewById(com.tokopedia.digital_deals.R.id.rv_trending_deals);
        mainContent = view.findViewById(com.tokopedia.digital_deals.R.id.main_content);
        baseMainContent = view.findViewById(com.tokopedia.digital_deals.R.id.base_main_content);
        searchInputView = view.findViewById(com.tokopedia.digital_deals.R.id.search_input_view);
        searchInputView.setCompoundDrawablesWithIntrinsicBounds(MethodChecker.getDrawable
                (getActivity(), com.tokopedia.digital_deals.R.drawable.ic_search_deal), null, null , null);
        tvLocationName = view.findViewById(com.tokopedia.digital_deals.R.id.tv_location_name);
        clBrands = view.findViewById(com.tokopedia.digital_deals.R.id.cl_brands);
        clPromos = view.findViewById(com.tokopedia.digital_deals.R.id.cl_promos);
        rvPromos = view.findViewById(com.tokopedia.digital_deals.R.id.rv_trending_promos);
        noContent = view.findViewById(com.tokopedia.digital_deals.R.id.no_content);
        tvSeeAllBrands = view.findViewById(com.tokopedia.digital_deals.R.id.tv_see_all_brands);
        tvSeeAllTrendingDeals = view.findViewById(com.tokopedia.digital_deals.R.id.tv_see_all_deals);
        curatedDealsLayout = view.findViewById(com.tokopedia.digital_deals.R.id.curated_deals);
        promoheading = view.findViewById(com.tokopedia.digital_deals.R.id.tv_promos);
        topDealsHeading = view.findViewById(com.tokopedia.digital_deals.R.id.tv_popular);
        tvSeeAllBrands.setOnClickListener(this);
        searchInputView.setOnClickListener(this);
        tvLocationName.setOnClickListener(this);
        toolbarTitle.setOnClickListener(this);
        nestedScrollView = view.findViewById(com.tokopedia.digital_deals.R.id.nested_scroll_view);
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        trendingDeals = view.findViewById(com.tokopedia.digital_deals.R.id.cl_topDeals);
        rvTrendingDeals.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        rvTrendingDeals.setNestedScrollingEnabled(false);
        selectLocationFragment = CloseableBottomSheetDialog.createInstanceRounded(getActivity());
        dealsCategoryBottomSheet = CloseableBottomSheetDialog.createInstanceRounded(getActivity());

        Drawable img = getResources().getDrawable(com.tokopedia.digital_deals.R.drawable.ic_search_deal);
        setDrawableTint(img);

        searchInputView.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);


        appBarLayout = view.findViewById(com.tokopedia.digital_deals.R.id.app_bar_layout);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset == 0) {
                    DealsHomeFragment.this.hideSearchButton();
                } else {
                    DealsHomeFragment.this.showSearchButton();
                }
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
                menuInflater.inflate(com.tokopedia.digital_deals.R.menu.menu_deals_home, popupMenu.getMenu());
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
        DrawableCompat.setTint(mutableDrawable, ContextCompat.getColor(getContext(), com.tokopedia.digital_deals.R.color.color_search_icon));
    }

    public void hideSearchButton() {
        if (mMenu != null) {
            MenuItem item = mMenu.findItem(com.tokopedia.digital_deals.R.id.action_menu_search);
            item.setVisible(false);
            item.setEnabled(false);
        }
    }

    public void showSearchButton() {
        if (mMenu != null) {
            MenuItem item = mMenu.findItem(com.tokopedia.digital_deals.R.id.action_menu_search);
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
    public void renderCategoryList(List<CategoryItem> categoryList, List<CategoriesModel> categoriesModels) {
        startShowCase();
        if (categoryList != null) {
            catItems.removeAllViews();
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.weight = 0.5f;
            View view;
            ImageView imageViewCatItem;
            TextView textViewCatItem;

            for (int position = 0; position < 4; position++) {
                LayoutInflater inflater = getLayoutInflater();
                view = inflater.inflate(com.tokopedia.digital_deals.R.layout.category_item, mainContent, false);
                imageViewCatItem = view.findViewById(com.tokopedia.digital_deals.R.id.iv_category);
                textViewCatItem = view.findViewById(com.tokopedia.digital_deals.R.id.tv_category);
                textViewCatItem.setText(categoryList.get(position).getTitle());
                ImageHandler.loadImage(getActivity(), imageViewCatItem, categoryList.get(position).getMediaUrl(), com.tokopedia.design.R.color.grey_1100, com.tokopedia.design.R.color.grey_1100);
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
                        openCategoryDetail(categoriesModel, categoriesModels);
                    }
                });
                view.setLayoutParams(params);
                catItems.addView(view);
            }
            LayoutInflater inflater = getLayoutInflater();
            view = inflater.inflate(com.tokopedia.digital_deals.R.layout.category_item, mainContent, false);
            imageViewCatItem = view.findViewById(com.tokopedia.digital_deals.R.id.iv_category);
            textViewCatItem = view.findViewById(com.tokopedia.digital_deals.R.id.tv_category);
            textViewCatItem.setText(getContext().getResources().getString(com.tokopedia.digital_deals.R.string.sell_all_category));
            imageViewCatItem.setImageResource(com.tokopedia.digital_deals.R.drawable.ic_semua_kategori);
            view.setLayoutParams(params);
            catItems.addView(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startDealsCategoryFragment(categoryList, categoriesModels);
                }
            });
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int id = item.getItemId();
        return mPresenter.onOptionMenuClick(id);
    }

    @Override
    public void renderTopDeals(CategoryItem categoryItem) {
        if (categoryItem.getItems() != null && categoryItem.getItems().size() > 0) {
            this.categoryItem = categoryItem;
            topDealsHeading.setText(categoryItem.getTitle());
            trendingDeals.setVisibility(View.VISIBLE);
            noContent.setVisibility(View.GONE);
            categoryAdapter.clearList();
            categoryAdapter.setDealsHomeLayout(true);
            categoryAdapter.setDealType(DealsAnalytics.TRENDING_DEALS);
            rvTrendingDeals.setAdapter(categoryAdapter);
            categoryAdapter.addAll(categoryItem.getItems(), true);
            rvTrendingDeals.setVisibility(View.VISIBLE);
            if (categoryItem.getItems().size() >= 9) {
                tvSeeAllTrendingDeals.setVisibility(View.VISIBLE);
                tvSeeAllTrendingDeals.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!TextUtils.isEmpty(categoryItem.getCategoryUrl())) {
                            mPresenter.sendSeeAllTrendingDealsEvent();
                            openTrendingDeals.replaceFragment(categoryItem.getCategoryUrl(), getContext().getResources().getString(com.tokopedia.digital_deals.R.string.trending_deals), 0);
                        }
                    }
                });
            }
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
            for (int i=0; i<categoryItems.size(); i++) {
                CategoryItem categoryItem = categoryItems.get(i);
                if (categoryItem.getItems() != null && categoryItem.getItems().size() > 0) {
                    isItemsAvailable = true;
                    DealsCategoryAdapter.INavigateToActivityRequest listener = new DealsCategoryAdapter.INavigateToActivityRequest() {
                        @Override
                        public void onNavigateToActivityRequest(Intent intent, int requestCode, int position) {
                            startActivityForResult(intent, requestCode);
                        }
                    };
                    CuratedDealsView curatedDealsView = new CuratedDealsView(getActivity(), categoryItem, listener, openTrendingDeals, "", mPresenter, i);
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
        searchInputView.setVisibility(View.VISIBLE);
    }

    @Override
    public void startOrderListActivity() {
        RouteManager.route(getActivity(), ApplinkConst.DEALS_ORDER);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionCheckerHelper.onRequestPermissionsResult(getContext(), requestCode, permissions, grantResults);
    }

    @Override
    public int getRequestCode() {
        return DealsHomeActivity.REQUEST_CODE_LOGIN;
    }

    @Override
    public RequestParams getParams() {
        RequestParams requestParams = RequestParams.create();
        Location location = Utils.getSingletonInstance().getLocation(getActivity());
        if (!TextUtils.isEmpty(location.getCoordinates())) {
            requestParams.putString(Utils.LOCATION_COORDINATES, location.getCoordinates());
        }
        if (location.getLocType() != null && !TextUtils.isEmpty(location.getLocType().getName())) {
            requestParams.putString(Utils.LOCATION_TYPE, location.getLocType().getName());
        }
        return requestParams;
    }

    @Override
    public RequestParams getBrandParams() {
        Location location = Utils.getSingletonInstance().getLocation(getActivity());
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(Utils.BRAND_QUERY_PARAM_TREE, Utils.BRAND_QUERY_PARAM_BRAND);
        if (!TextUtils.isEmpty(location.getCoordinates())) {
            requestParams.putString(Utils.LOCATION_COORDINATES, location.getCoordinates());
        }
        if (location.getLocType() != null && !TextUtils.isEmpty(location.getLocType().getName())) {
            requestParams.putString(Utils.LOCATION_TYPE, location.getLocType().getName());
        }
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
        MenuItem item = mMenu.findItem(com.tokopedia.digital_deals.R.id.action_menu_favourite);
        item.setVisible(false);
    }

    @Override
    public void showFavouriteButton() {
        MenuItem item = mMenu.findItem(com.tokopedia.digital_deals.R.id.action_menu_favourite);
        item.setVisible(false);
    }

    @Override
    public void startGeneralWebView(String url) {
        RouteManager.route(getActivity(), ApplinkConstInternalGlobal.WEBVIEW, url);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(com.tokopedia.digital_deals.R.menu.menu_deals_home, menu);
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
            if (categoryAdapter != null)
                categoryAdapter.notifyDataSetChanged();
            forceRefresh = false;
        }
    }

    @Override
    public void startLocationFragment() {
        Location location = Utils.getSingletonInstance().getLocation(getActivity());
        Fragment fragment = SelectLocationBottomSheet.createInstance(tvLocationName.getText().toString(), location);
        getChildFragmentManager().beginTransaction().setCustomAnimations(com.tokopedia.digital_deals.R.anim.deals_slide_in_up, com.tokopedia.digital_deals.R.anim.deals_slide_in_down,
                com.tokopedia.digital_deals.R.anim.deals_slide_out_down, com.tokopedia.digital_deals.R.anim.deals_slide_out_up)
                .add(com.tokopedia.digital_deals.R.id.main_content, fragment).addToBackStack(HOME_FRAGMENT).commit();
    }

    @Override
    public void startDealsCategoryFragment(List<CategoryItem> categoryItems, List<CategoriesModel> categoriesModels) {
        View categoryView = getLayoutInflater().inflate(com.tokopedia.digital_deals.R.layout.deals_category_bottomsheet_layout, null);
        RecyclerView recyclerView = categoryView.findViewById(com.tokopedia.digital_deals.R.id.rv_category_items);
        ImageView crossIcon = categoryView.findViewById(com.tokopedia.digital_deals.R.id.cross_icon_bottomsheet);
        crossIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dealsCategoryBottomSheet.dismiss();
            }
        });
        DealsCategoryItemAdapter adapter = new DealsCategoryItemAdapter(categoryItems, categoriesModels, this);
        recyclerView.setAdapter(adapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 5,
                GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        dealsCategoryBottomSheet.setCustomContentView(categoryView, "", false);
        dealsCategoryBottomSheet.show();
        dealsCategoryBottomSheet.setCanceledOnTouchOutside(true);
    }

    @Override
    public String getSearchInputText() {
        return searchInputView.getText().toString();
    }

    @Override
    public void updateInitialLocation(List<Location> locations) {
        Utils.getSingletonInstance().updateLocation(getContext(), locations.get(0));
        tvLocationName.setText(locations.get(0).getName());
        mPresenter.getDealsList(true);
        mPresenter.getBrandsHome();
    }

    @Override
    public void setDefaultLocation() {
        Location location = new Location();
        location.setName(Utils.LOCATION_NAME);
        location.setId(Utils.LOCATION_ID);
        Utils.getSingletonInstance().updateLocation(getContext(), location);
        if (!Utils.hasShown(getActivity(), DealsHomeFragment.class.getName())) {
            mPresenter.getLocations();
        } else if (location != null){
            tvLocationName.setText(location.getName());
            mPresenter.getDealsList(true);
            mPresenter.getBrandsHome();
        }
    }

    @Override
    public void showErrorMessage() {
        Toaster.INSTANCE.showNormalWithAction(mainContent, Utils.getSingletonInstance().getLocationErrorMessage(getContext()), Snackbar.LENGTH_LONG, getContext().getResources().getString(com.tokopedia.digital_deals.R.string.location_deals_changed_toast_oke), v1 -> {
        });
        setDefaultLocation();
    }

    @Override
    public void openCategoryDetail(CategoriesModel categoriesModel, List<CategoriesModel> categoriesModels) {
        if (dealsCategoryBottomSheet != null) {
            dealsCategoryBottomSheet.dismiss();
        }
        mPresenter.sendCategoryClickEvent(categoriesModel.getName(), categoriesModel.getPosition());
        Intent detailsIntent = new Intent(getActivity(), CategoryDetailActivity.class);
        detailsIntent.putExtra(CategoryDetailActivity.CATEGORIES_DATA, categoriesModel);
        detailsIntent.putExtra(CategoryDetailActivity.CATEGORY_NAME, categoriesModel.getTitle());
        detailsIntent.putExtra(CategoryDetailActivity.FROM_HOME, true);
        detailsIntent.putParcelableArrayListExtra(AllBrandsActivity.EXTRA_LIST, (ArrayList<? extends Parcelable>) categoriesModels);
        detailsIntent.putParcelableArrayListExtra(AllBrandsActivity.EXTRA_CATEGOTRY_LIST, (ArrayList<? extends Parcelable>) this.categoryItem.getItems());
        getActivity().startActivity(detailsIntent);
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        dealsCategoryBottomSheet.dismiss();
        selectLocationFragment.dismiss();
    }

    public void refreshHomePage(boolean isLocationUpdated) {
        Location location = Utils.getSingletonInstance().getLocation(getActivity());
        if (location != null && isLocationUpdated && !tvLocationName.getText().equals(location.getName())) {
            tvLocationName.setText(location.getName());
            Toaster.INSTANCE.showNormalWithAction(mainContent, String.format("%s %s", getContext().getResources().getString(com.tokopedia.digital_deals.R.string.location_deals_changed_toast), location.getName()), Snackbar.LENGTH_SHORT, getContext().getResources().getString(com.tokopedia.digital_deals.R.string.location_deals_changed_toast_oke), v1 -> {
            });
            mPresenter.getDealsList(true);
            mPresenter.getBrandsHome();
        }
    }

    public void setDefaultLocationName() {
        Location location = new Location();
        location.setName(Utils.LOCATION_NAME);
        location.setId(Utils.LOCATION_ID);
        Utils.getSingletonInstance().updateLocation(getContext(), location);
        if (location != null) {
            tvLocationName.setText(location.getName());
            mPresenter.getDealsList(true);
            mPresenter.getBrandsHome();
        }
    }

    public void setCurrentCoordinates() {
        LocalCacheHandler localCacheHandler = new LocalCacheHandler(getActivity(), TkpdCache.DEALS_LOCATION);
        String lattitude = localCacheHandler.getString(Utils.KEY_LOCATION_LAT);
        String longitude = localCacheHandler.getString(Utils.KEY_LOCATION_LONG);
        if (!TextUtils.isEmpty(lattitude) && !TextUtils.isEmpty(longitude)) {
            mPresenter.getNearestLocation(String.format("%s,%s", lattitude, longitude));
        } else {
            mPresenter.getDealsList(true);
            mPresenter.getBrandsHome();
        }
    }


    public interface OpenTrendingDeals {
        void replaceFragment(String url, String title, int position);
    }
}
