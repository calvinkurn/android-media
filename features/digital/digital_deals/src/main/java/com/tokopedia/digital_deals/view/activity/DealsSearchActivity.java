package com.tokopedia.digital_deals.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.google.android.material.appbar.AppBarLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.digital_deals.R;
import com.tokopedia.digital_deals.di.DealsComponentInstance;
import com.tokopedia.digital_deals.view.TopDealsCacheHandler;
import com.tokopedia.digital_deals.view.adapter.DealsCategoryAdapter;
import com.tokopedia.digital_deals.view.contractor.DealsSearchContract;
import com.tokopedia.digital_deals.view.customview.SearchInputView;
import com.tokopedia.digital_deals.view.fragment.SelectLocationBottomSheet;
import com.tokopedia.digital_deals.view.model.Brand;
import com.tokopedia.digital_deals.view.model.CategoriesModel;
import com.tokopedia.digital_deals.view.model.Location;
import com.tokopedia.digital_deals.view.model.ProductItem;
import com.tokopedia.digital_deals.view.presenter.BrandDetailsPresenter;
import com.tokopedia.digital_deals.view.presenter.DealsSearchPresenter;
import com.tokopedia.digital_deals.view.utils.DealsAnalytics;
import com.tokopedia.digital_deals.view.utils.Utils;
import com.tokopedia.unifycomponents.Toaster;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class DealsSearchActivity extends DealsBaseActivity implements
        DealsSearchContract.View, SearchInputView.Listener, SearchInputView.FocusChangeListener, android.view.View.OnClickListener, DealsCategoryAdapter.INavigateToActivityRequest, SelectLocationBottomSheet.SelectedLocationListener {

    private final boolean IS_SHORT_LAYOUT = true;
    public static final String EXTRA_LIST = "list";
    private static final String SEARCH_PAGE = "DealsSearchActivity";
    public static final String SCREEN_NAME = "/digital/deals/search";

    private CoordinatorLayout mainContent;
    private ScrollView noContent;
    private LinearLayout brandLayout;
    private LinearLayout noBrandsFound;
    private ConstraintLayout clLocation;
    private AppBarLayout appBarBrands;
    private TextView dealsHeading;
    private TextView brandsHeading, brandCount;
    private View divider;
    private LinearLayoutManager layoutManager;
    private SearchInputView searchInputView;
    private RecyclerView rvDeals;
    private ImageView back;
    private TextView tvCityName;
    private List<Brand> brands = new ArrayList<>();
    List<CategoriesModel> categoryList = new ArrayList<>();
    private String categoryId;

    @Inject
    public DealsSearchPresenter mPresenter;
    @Inject
    DealsAnalytics dealsAnalytics;
    private DealsCategoryAdapter dealsCategoryAdapter;
    private int listCount;
    private String searchText;
    private int adapterPosition = -1;
    private boolean forceRefresh;
    private AppBarLayout appBarToolbar;
    private boolean isLocationUpdated;

    @Override
    public int getLayoutRes() {
        return com.tokopedia.digital_deals.R.layout.activity_deals_search;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initInjector();
        setUpVariables();
        searchInputView.setListener(this);
        searchInputView.setFocusChangeListener(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        categoryList = getIntent().getParcelableArrayListExtra(EXTRA_LIST);
        mPresenter.attachView(this);
        mPresenter.initialize();
    }

    @Override
    protected int getToolbarResourceID(){
        return R.id.deals_toolbar;
    }
    @Override
    protected void onStart() {
        super.onStart();
        Location location = Utils.getSingletonInstance().getLocation(getActivity());
        if (location != null && !TextUtils.isEmpty(tvCityName.getText()) && !TextUtils.isEmpty(location.getName()) && !tvCityName.getText().equals(location.getName())) {
            tvCityName.setText(location.getName());
            Toaster.INSTANCE.showNormalWithAction(mainContent, String.format("%s %s", this.getResources().getString(com.tokopedia.digital_deals.R.string.location_deals_changed_toast), location.getName()), Snackbar.LENGTH_SHORT, this.getResources().getString(com.tokopedia.digital_deals.R.string.location_deals_changed_toast_oke), v1 -> {
            });
            mPresenter.getDealsListBySearch(searchInputView.getSearchText());
        }
        dealsAnalytics.sendScreenNameEvent(SCREEN_NAME);
    }

    private void setUpVariables() {
        rvDeals = findViewById(com.tokopedia.digital_deals.R.id.rv_search_results);
        searchInputView = findViewById(com.tokopedia.digital_deals.R.id.search_input_view);
        noBrandsFound = findViewById(com.tokopedia.digital_deals.R.id.no_brands);
        appBarBrands = findViewById(com.tokopedia.digital_deals.R.id.app_bar_brands);
        appBarToolbar = findViewById(com.tokopedia.digital_deals.R.id.app_bar_toolbar);
        mainContent = findViewById(com.tokopedia.digital_deals.R.id.main_content);
        dealsHeading = findViewById(com.tokopedia.digital_deals.R.id.tv_topevents);
        brandsHeading = findViewById(com.tokopedia.digital_deals.R.id.brand_list);
        brandCount = findViewById(com.tokopedia.digital_deals.R.id.num_of_brands);
        back = findViewById(com.tokopedia.digital_deals.R.id.imageViewBack);
        divider = findViewById(com.tokopedia.digital_deals.R.id.divider);
        noContent = findViewById(com.tokopedia.digital_deals.R.id.no_content);
        brandLayout = findViewById(com.tokopedia.digital_deals.R.id.brand_layout);
        tvCityName = findViewById(com.tokopedia.digital_deals.R.id.tv_location);
        tvCityName.setCompoundDrawablesWithIntrinsicBounds(null, null, MethodChecker.getDrawable
                (this, com.tokopedia.digital_deals.R.drawable.location_arrow_down), null);
        clLocation = findViewById(com.tokopedia.digital_deals.R.id.cl_location);
        back.setOnClickListener(this);
        tvCityName.setOnClickListener(this);
        searchInputView.setSearchHint(getResources().getString(com.tokopedia.digital_deals.R.string.search_input_hint_deals));
        searchInputView.setSearchTextSize(getResources().getDimension(com.tokopedia.design.R.dimen.sp_16));
        searchInputView.setSearchImageView(MethodChecker.getDrawable(this, com.tokopedia.digital_deals.R.drawable.ic_search_deal));
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvDeals.setLayoutManager(layoutManager);
        dealsCategoryAdapter = new DealsCategoryAdapter(null, DealsCategoryAdapter.SEARCH_PAGE, this, !IS_SHORT_LAYOUT);
        rvDeals.setAdapter(dealsCategoryAdapter);
        appBarBrands.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset != 0) {
                    divider.setVisibility(View.GONE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        appBarToolbar.setElevation(getResources().getDimension(com.tokopedia.design.R.dimen.dp_4));
                    }
                } else {
                    divider.setVisibility(View.VISIBLE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        appBarToolbar.setElevation(getResources().getDimension(com.tokopedia.design.R.dimen.dp_0));
                    }
                }
            }
        });

        brandCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent brandIntent = new Intent(DealsSearchActivity.this, AllBrandsActivity.class);
                brandIntent.putParcelableArrayListExtra(AllBrandsActivity.EXTRA_LIST, (ArrayList<? extends Parcelable>) categoryList);
                brandIntent.putExtra(AllBrandsActivity.SEARCH_TEXT, searchInputView.getSearchText());
                brandIntent.putExtra("cat_id", categoryId);
                navigateToActivityRequest(brandIntent, DealsHomeActivity.REQUEST_CODE_DEALSLOCATIONACTIVITY);
            }
        });
    }

    @Override
    public void onSearchSubmitted(String text) {
        if (text.length() > 2)
            dealsAnalytics.sendEventDealsDigitalClick(DealsAnalytics.EVENT_SEARCH_VOUCHER_OR_OUTLET, text);
        KeyboardHandler.hideSoftKeyboard(getActivity());
        mPresenter.searchTextChanged(text);
    }

    @Override
    public void onSearchTextChanged(String text) {
        if (text.length() > 2)
            dealsAnalytics.sendEventDealsDigitalClick(DealsAnalytics.EVENT_SEARCH_VOUCHER_OR_OUTLET, text);
        mPresenter.searchTextChanged(text);
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
    public void navigateToActivity(Intent intent) {
        startActivity(intent);
    }

    @Override
    public void startLocationFragment(List<Location> locationList, boolean isForFirstTime) {
        Location location = Utils.getSingletonInstance().getLocation(this);
        Fragment fragment = SelectLocationBottomSheet.createInstance(tvCityName.getText().toString(), location);
        getSupportFragmentManager().beginTransaction().setCustomAnimations(com.tokopedia.digital_deals.R.anim.deals_slide_in_up, com.tokopedia.digital_deals.R.anim.deals_slide_in_down,
                com.tokopedia.digital_deals.R.anim.deals_slide_out_down, com.tokopedia.digital_deals.R.anim.deals_slide_out_up)
                .add(com.tokopedia.digital_deals.R.id.main_content, fragment).addToBackStack(SEARCH_PAGE).commit();
    }

    @Override
    public void renderFromSearchResults() {
        Location location = Utils.getSingletonInstance().getLocation(getActivity());
        rvDeals.addOnScrollListener(rvOnScrollListener);
        tvCityName.setText(location.getName());
    }

    private SpannableString getHeaderFormattedText(String searchText, int count) {
        String text = String.format(getString(com.tokopedia.digital_deals.R.string.deals_search_location_result), searchText);
        int startIndexOfLink = text.length();
        text += " " + String.format(getActivity().getResources().getString(com.tokopedia.digital_deals.R.string.number_of_items), count);
        SpannableString spannableString = new SpannableString(text);
        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View view) {
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
                ds.setColor(getResources().getColor(com.tokopedia.design.R.color.black_38));
            }
        }, startIndexOfLink, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    @Override
    public RequestParams getParams() {
        RequestParams requestParams = RequestParams.create();
        if (!TextUtils.isEmpty(searchInputView.getSearchText())) {
            requestParams.putString(Utils.BRAND_QUERY_TAGS, searchInputView.getSearchText());
        }
        Location location = Utils.getSingletonInstance().getLocation(this);
        if (!TextUtils.isEmpty(location.getCoordinates())) {
            requestParams.putString(Utils.LOCATION_COORDINATES, location.getCoordinates());
        }
        requestParams.putString(Utils.BRAND_QUERY_PARAM_TREE, Utils.BRAND_QUERY_PARAM_BRAND_AND_PRODUCT);
        if (!TextUtils.isEmpty(categoryId)) {
            requestParams.putString(Utils.QUERY_PARAM_CHILD_CATEGORY_ID, categoryId);
        }
        return requestParams;
    }

    @Override
    public android.view.View getRootView() {
        return mainContent;
    }

    @Override
    public void setTrendingDealsOrSuggestions(List<ProductItem> productItems, boolean isTrendingDeals, String highlight, int count) {
        Location location = Utils.getSingletonInstance().getLocation(getActivity());
        searchText = highlight;
        if (productItems != null && !productItems.isEmpty()) {
            dealsHeading.setText(getResources().getString(com.tokopedia.digital_deals.R.string.products_title_search_default));
            dealsHeading.setVisibility(View.VISIBLE);
            rvDeals.clearOnScrollListeners();
            dealsCategoryAdapter.clearList();
            dealsCategoryAdapter.setSearchText(searchText);
            dealsCategoryAdapter.setFromSearchResult(true);
            dealsCategoryAdapter.addAll(productItems, false);
            dealsCategoryAdapter.setTopDealsLayout(true);
            if (productItems.size() > 0) {
                if (count == 0)
                    listCount = productItems.size();
                else
                    listCount = count;
            }
            if (!isTrendingDeals) {
                rvDeals.addOnScrollListener(rvOnScrollListener);
            }
            dealsCategoryAdapter.notifyDataSetChanged();

            rvDeals.setVisibility(View.VISIBLE);

            noContent.setVisibility(View.GONE);
        } else if (this.brands != null && this.brands.size() > 0 && productItems != null && productItems.size() == 0) {
            rvDeals.setVisibility(View.GONE);
            dealsHeading.setVisibility(View.GONE);
        } else {
            dealsAnalytics.sendEventDealsDigitalView(DealsAnalytics.EVENT_NO_DEALS,
                    searchText);
            noBrandsFound.setVisibility(View.VISIBLE);
            brandsHeading.setVisibility(View.GONE);
            brandLayout.setVisibility(View.GONE);
            rvDeals.setVisibility(View.GONE);
            dealsHeading.setVisibility(View.GONE);
        }
        if (location != null)
            tvCityName.setText(location.getName());
    }

    @Override
    public void setSuggestedBrands(List<Brand> brandList, int count) {
        if (brandList != null && brandList.size() > 0) {
            this.brands = brandList;
            brandLayout.setVisibility(View.VISIBLE);
            brandsHeading.setVisibility(View.VISIBLE);
            if (count > 4) {
                brandCount.setVisibility(View.VISIBLE);
                brandCount.setText(String.format(getResources().getString(com.tokopedia.digital_deals.R.string.brand_count_text), searchInputView.getSearchText(), count));
            } else {
                brandCount.setVisibility(View.GONE);
            }
            noBrandsFound.setVisibility(View.GONE);
            LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) dealsHeading.getLayoutParams();
            params1.setMargins(getResources().getDimensionPixelOffset(com.tokopedia.design.R.dimen.dp_16), getResources().getDimensionPixelOffset(com.tokopedia.design.R.dimen.dp_12), 0, 0);
            dealsHeading.setLayoutParams(params1);
            brandLayout.removeAllViews();
            View view;
            int itemCount = Utils.getScreenWidth() / (int) (getResources().getDimension(com.tokopedia.digital_deals.R.dimen.dp_66) + getResources().getDimension(com.tokopedia.design.R.dimen.dp_8));//Divide by item width including margin
            int maxBrands = Math.min(brandList.size(), itemCount);

            dealsAnalytics.sendBrandsSuggestionImpressionEvent(brandList, searchText);
            ImageView imageViewBrandItem;
            TextView brandName;
            for (int index = 0; index < maxBrands; index++) {
                LayoutInflater inflater = getLayoutInflater();
                view = inflater.inflate(com.tokopedia.digital_deals.R.layout.item_brand_home, brandLayout, false);
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
                imageViewBrandItem = view.findViewById(com.tokopedia.digital_deals.R.id.iv_brand);
                brandName = view.findViewById(com.tokopedia.digital_deals.R.id.brandName);
                brandName.setText(brandList.get(index).getTitle());
                ImageHandler.loadImage(this, imageViewBrandItem, brandList.get(index).getFeaturedThumbnailImage(), com.tokopedia.design.R.color.grey_1100, com.tokopedia.design.R.color.grey_1100);
                view.setLayoutParams(params);
                brandLayout.addView(view);
                final int position1 = index;
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dealsAnalytics.sendBrandsSuggestionClickEvent(brandList.get(position1), position1, searchText, DealsAnalytics.CLICK_BRANDS_SEARCH);
                        Intent detailsIntent = new Intent(DealsSearchActivity.this, BrandDetailsActivity.class);
                        detailsIntent.putExtra(BrandDetailsPresenter.BRAND_DATA, brandList.get(position1));
                        startActivity(detailsIntent);
                    }
                });
            }
        } else {
            this.brands.clear();
            brandLayout.removeAllViews();
            brandsHeading.setVisibility(View.GONE);
            brandCount.setVisibility(View.GONE);
            noBrandsFound.setVisibility(View.GONE);
        }
    }

    @Override
    public void showSuggestedDeals(List<ProductItem> items, boolean showList) {
        Location location = Utils.getSingletonInstance().getLocation(getActivity());
        if (showList) {
            if (location != null) {
                dealsHeading.setText(String.format(getResources().getString(com.tokopedia.digital_deals.R.string.products_title_search), location.getName().toUpperCase()));
                tvCityName.setText(location.getName());
            }
            rvDeals.clearOnScrollListeners();
            dealsCategoryAdapter.clearList();
            dealsCategoryAdapter.addAll(items, false);
            dealsCategoryAdapter.setTopDealsLayout(true);
            dealsCategoryAdapter.setFromSearchResult(false);
            dealsCategoryAdapter.notifyDataSetChanged();
            rvDeals.setVisibility(View.VISIBLE);
            brandsHeading.setVisibility(View.GONE);
            brandLayout.setVisibility(View.GONE);
            brandCount.setVisibility(View.GONE);
            dealsHeading.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void removeFooter() {
        ((DealsCategoryAdapter) rvDeals.getAdapter()).removeFooter();
    }

    @Override
    public void addFooter() {
        ((DealsCategoryAdapter) rvDeals.getAdapter()).addFooter();
    }

    @Override
    public void addDealsToCards(List<ProductItem> productItems) {
        ((DealsCategoryAdapter) rvDeals.getAdapter()).addAll(productItems);
    }

    @Override
    public LinearLayoutManager getLayoutManager() {
        return layoutManager;
    }

    private void initInjector() {
        DealsComponentInstance.getDealsComponent(getApplication())
                .inject(this);
    }

    private RecyclerView.OnScrollListener rvOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            KeyboardHandler.hideSoftKeyboard(getActivity());
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            mPresenter.onRecyclerViewScrolled(layoutManager);
        }
    };

    @Override
    protected Fragment getNewFragment() {
        return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case DealsHomeActivity.REQUEST_CODE_DEALDETAILACTIVITY:
                if (resultCode == RESULT_OK) {
                    Location location = Utils.getSingletonInstance().getLocation(getActivity());
                    if (location != null && !TextUtils.isEmpty(tvCityName.getText()) && !TextUtils.isEmpty(location.getName()) && !tvCityName.getText().equals(location.getName())) {
                        tvCityName.setText(location.getName());
                    }
                    if (!TextUtils.isEmpty(searchInputView.getSearchText()))
                        mPresenter.getDealsListBySearch(searchInputView.getSearchText());

                }
                break;
            case DealsHomeActivity.REQUEST_CODE_LOGIN:
                if (resultCode == RESULT_OK) {
                    UserSessionInterface userSession = new UserSession(this);
                    if (userSession.isLoggedIn()) {
                        if (adapterPosition != -1) {
                            if (dealsCategoryAdapter != null)
                                dealsCategoryAdapter.setLike(adapterPosition);
                        }
                    }
                }
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(android.view.View v) {
        mPresenter.onItemClick(v.getId());
    }

    @Override
    protected void onDestroy() {
        mPresenter.onDestroy();
        TopDealsCacheHandler.init().setTopDeals(null);
        TopDealsCacheHandler.deInit();
        super.onDestroy();
    }

    @Override
    public void goBack() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        super.onBackPressed();
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
            if (dealsCategoryAdapter != null)
                dealsCategoryAdapter.notifyDataSetChanged();
            forceRefresh = false;
        }
    }

    @Override
    public void onNavigateToActivityRequest(Intent intent, int requestCode, int position) {
        this.adapterPosition = position;
        navigateToActivityRequest(intent, requestCode);
    }

    @Override
    public void onLocationItemUpdated(boolean isLocationUpdated) {
        this.isLocationUpdated = isLocationUpdated;
        Location location = Utils.getSingletonInstance().getLocation(getActivity());
        if (location != null && !TextUtils.isEmpty(tvCityName.getText()) && !TextUtils.isEmpty(location.getName()) && !tvCityName.getText().equals(location.getName())) {
            tvCityName.setText(location.getName());
            Toaster.INSTANCE.showNormalWithAction(mainContent, String.format("%s %s", this.getResources().getString(com.tokopedia.digital_deals.R.string.location_deals_changed_toast), location.getName()), Snackbar.LENGTH_SHORT, this.getResources().getString(com.tokopedia.digital_deals.R.string.location_deals_changed_toast_oke), v1 -> {
            });
            mPresenter.getDealsListBySearch(searchInputView.getSearchText());
        }
    }

    @Override
    public void setDefaultLocationOnHomePage() {
        Location location = new Location();
        location.setName(Utils.LOCATION_NAME);
        location.setId(Utils.LOCATION_ID);
        Utils.getSingletonInstance().updateLocation(this, location);
        tvCityName.setText(location.getName());
        mPresenter.getDealsListBySearch(searchInputView.getSearchText());
    }

    @Override
    public void onFocusChanged(boolean hasFocus) {
    }
}
