package com.tokopedia.digital_deals.view.fragment;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.design.bottomsheet.CloseableBottomSheetDialog;
import com.tokopedia.digital_deals.DealsModuleRouter;
import com.tokopedia.digital_deals.R;
import com.tokopedia.digital_deals.data.source.DealsUrl;
import com.tokopedia.digital_deals.di.DealsComponent;
import com.tokopedia.digital_deals.view.TopDealsCacheHandler;
import com.tokopedia.digital_deals.view.activity.AllBrandsActivity;
import com.tokopedia.digital_deals.view.activity.CategoryDetailActivity;
import com.tokopedia.digital_deals.view.activity.DealsHomeActivity;
import com.tokopedia.digital_deals.view.activity.DealsSearchActivity;
import com.tokopedia.digital_deals.view.adapter.DealsBrandAdapter;
import com.tokopedia.digital_deals.view.adapter.DealsCategoryAdapter;
import com.tokopedia.digital_deals.view.adapter.DealsLocationAdapter;
import com.tokopedia.digital_deals.view.contractor.DealsCategoryDetailContract;
import com.tokopedia.digital_deals.view.model.Brand;
import com.tokopedia.digital_deals.view.model.CategoriesModel;
import com.tokopedia.digital_deals.view.model.Location;
import com.tokopedia.digital_deals.view.model.ProductItem;
import com.tokopedia.digital_deals.view.presenter.DealsCategoryDetailPresenter;
import com.tokopedia.digital_deals.view.utils.DealsAnalytics;
import com.tokopedia.digital_deals.view.utils.Utils;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static android.app.Activity.RESULT_OK;

public class CategoryDetailHomeFragment extends BaseDaggerFragment implements DealsCategoryDetailContract.View, View.OnClickListener, DealsCategoryAdapter.INavigateToActivityRequest, DealsLocationAdapter.ActionListener, SelectLocationBottomSheet.CloseSelectLocationBottomSheet, PopupMenu.OnMenuItemClickListener {

    private final boolean IS_SHORT_LAYOUT = true;
    private final String SCREEN_NAME = "/digital/deals/category";
    private RecyclerView recyclerViewDeals;
    private RecyclerView recyclerViewBrands;
    private View progressBarLayout;
    private ProgressBar progBar;
    private CoordinatorLayout mainContent;
    private ConstraintLayout clBrands;
    private TextView seeAllBrands;
    private TextView popularLocation;
    private TextView numberOfDeals;
    private LinearLayoutManager layoutManager;
    private LinearLayout noContent;
    private Toolbar toolbar;
    private ImageView overFlowIcon;
    @Inject
    DealsAnalytics dealsAnalytics;
    @Inject
    DealsCategoryDetailPresenter mPresenter;
    private CategoriesModel categoriesModel;
    private String locationName;
    private DealsCategoryAdapter dealsAdapter;
    private DealsBrandAdapter dealsBrandAdapter;
    private int adapterPosition = -1;
    private boolean forceRefresh;
    public static final String SEARCH_TEXT = "search_text";
    private TextView toolbarTitle;
    private TextView searchInputView;
    private ImageView backArrow;
    private String searchText;
    CloseableBottomSheetDialog selectLocationFragment;
    List<CategoriesModel> categoryList = new ArrayList<>();
    List<ProductItem> categoryItems = new ArrayList<>();
    private AppBarLayout appBarLayout;
    private UserSession userSession;

    private Menu mMenu;

    @Override
    protected void initInjector() {
        getComponent(DealsComponent.class).inject(this);
        mPresenter.attachView(this);
    }

    public static Fragment createInstance(Bundle bundle) {
        Fragment fragment = new CategoryDetailHomeFragment();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.categoriesModel = getArguments().getParcelable(CategoryDetailActivity.CATEGORIES_DATA);
        this.categoryList = getArguments().getParcelableArrayList(AllBrandsActivity.EXTRA_LIST);
        this.categoryItems = getArguments().getParcelableArrayList(AllBrandsActivity.EXTRA_CATEGOTRY_LIST);
        userSession = new UserSession(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category_detail_home, container, false);
        setHasOptionsMenu(true);
        setViewIds(view);


        mPresenter.getCategoryDetails(true);
        mPresenter.getBrandsList(true);
        return view;
    }


    private void setViewIds(View view) {
        toolbar = view.findViewById(R.id.toolbar_category);
        appBarLayout = view.findViewById(R.id.app_bar_layout);
        searchInputView = view.findViewById(R.id.search_input_view);
        backArrow = view.findViewById(R.id.backArraw);
        toolbarTitle = view.findViewById(R.id.tv_location_name);
        searchInputView.setOnClickListener(this);
        seeAllBrands = view.findViewById(R.id.tv_see_all);
        popularLocation = view.findViewById(R.id.tv_popular);
        overFlowIcon = view.findViewById(R.id.overFlow_icon);
        numberOfDeals = view.findViewById(R.id.number_of_locations);
        recyclerViewDeals = view.findViewById(R.id.rv_deals);
        recyclerViewBrands = view.findViewById(R.id.rv_brands);
        mainContent = view.findViewById(R.id.main_content);
        progressBarLayout = view.findViewById(R.id.progress_bar_layout);
        progBar = view.findViewById(R.id.prog_bar);
        clBrands = view.findViewById(R.id.cl_brands);
        noContent = view.findViewById(R.id.no_content);
        seeAllBrands.setOnClickListener(this);
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerViewDeals.setLayoutManager(layoutManager);
        dealsAdapter = new DealsCategoryAdapter(null, DealsCategoryAdapter.CATEGORY_PAGE, this, !IS_SHORT_LAYOUT);
        dealsAdapter.setCategoryName(categoriesModel.getTitle());
        recyclerViewDeals.setAdapter(dealsAdapter);
        searchText = getArguments().getString(SEARCH_TEXT);
        if (!TextUtils.isEmpty(searchText)) {
            searchInputView.setText(searchText);
        }
        toolbarTitle.setOnClickListener(this);
        KeyboardHandler.DropKeyboard(getContext(), searchInputView);

        Location location = Utils.getSingletonInstance().getLocation(getContext());
        if (location != null) {
            toolbarTitle.setText(location.getName());
        }
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
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
                popupMenu.setOnMenuItemClickListener(CategoryDetailHomeFragment.this);
                popupMenu.show();
            }
        });

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset == 0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        toolbar.setElevation(0.0f);
                    }
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        toolbar.setElevation(4.0f);
                    }
                }
            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();
        Location location = Utils.getSingletonInstance().getLocation(getActivity());
        if (location != null) {
            toolbarTitle.setText(location.getName());
            popularLocation.setText(String.format(getActivity().getResources().getString(R.string.popular_deals_in_location), this.categoriesModel.getTitle(), location.getName()));
            mPresenter.getBrandsList(true);
            mPresenter.getCategoryDetails(true);
        }
        dealsAnalytics.sendScreenNameEvent(getScreenName());
    }

    @Override
    protected String getScreenName() {
        return SCREEN_NAME;
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
    public void renderCategoryList(List<ProductItem> deals, int count) {
        searchInputView.setHint(String.format(getContext().getResources().getString(R.string.search_input_hint_category), this.categoriesModel.getName()));
        Location location = Utils.getSingletonInstance().getLocation(getContext());
        if (location != null)
            locationName = location.getName();
        if (deals != null && deals.size() > 0) {
            this.categoryItems = deals;
            if (count == 0)
                numberOfDeals.setText(String.format(getResources().getString(R.string.number_of_items), deals.size()));
            else
                numberOfDeals.setText(String.format(getResources().getString(R.string.number_of_items), count));
            dealsAdapter.clearList();
            dealsAdapter.addAll(deals, false);
            dealsAdapter.setDealType(DealsAnalytics.CATEGORY_DEALS);
            dealsAdapter.notifyDataSetChanged();
            recyclerViewDeals.setVisibility(View.VISIBLE);
            recyclerViewDeals.addOnScrollListener(rvOnScrollListener);
            noContent.setVisibility(View.GONE);
        } else {
            if (!TextUtils.isEmpty(locationName))
                dealsAnalytics.sendEventDealsDigitalView(DealsAnalytics.EVENT_NO_DEALS_AVAILABLE_ON_YOUR_LOCATION,
                        locationName);
            numberOfDeals.setText(String.format(getResources().getString(R.string.number_of_items), count));
            recyclerViewDeals.setVisibility(View.GONE);
            noContent.setVisibility(View.VISIBLE);
        }

        if (!TextUtils.isEmpty(locationName))
            popularLocation.setText(String.format(getActivity().getResources().getString(R.string.popular_deals_in_location), this.categoriesModel.getTitle(), locationName));
        else
            popularLocation.setText(getActivity().getResources().getString(R.string.text_deals));
    }

    @Override
    public void renderBrandList(List<Brand> brandList) {
        if (brandList != null) {
            clBrands.setVisibility(View.VISIBLE);
            setBrandsAdapter(brandList);
        } else {
            clBrands.setVisibility(View.GONE);
        }
    }

    private void setBrandsAdapter(List<Brand> brandList) {
        int itemCount = Utils.getScreenWidth() / (int) (getResources().getDimension(R.dimen.dp_66) + getResources().getDimension(R.dimen.dp_8));//Divide by item width including margin
        int maxBrands = Math.min(brandList.size(), itemCount);
        recyclerViewBrands.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.HORIZONTAL, false));
        dealsBrandAdapter = new DealsBrandAdapter(brandList.subList(0, maxBrands), DealsBrandAdapter.ITEM_BRAND_HOME);
        dealsBrandAdapter.setPageType("category");
        recyclerViewBrands.setAdapter(new DealsBrandAdapter(brandList.subList(0, maxBrands), DealsBrandAdapter.ITEM_BRAND_HOME));
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.menu_category_detail, menu);
        onPrepareOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return true;
    }

    @Override
    public void removeFooter() {

        ((DealsCategoryAdapter) recyclerViewDeals.getAdapter()).removeFooter();
    }

    @Override
    public void addFooter() {
        ((DealsCategoryAdapter) recyclerViewDeals.getAdapter()).addFooter();

    }

    @Override
    public void addDealsToCards(List<ProductItem> productItems) {
        if (productItems != null) {
            ((DealsCategoryAdapter) recyclerViewDeals.getAdapter()).addAll(productItems);
        }
    }

    @Override
    public LinearLayoutManager getLayoutManager() {
        return layoutManager;
    }

    @Override
    public void showViews() {
        mainContent.setVisibility(View.VISIBLE);
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
    public String getCategoryParams() {
        Uri uri = null;
        Location location = Utils.getSingletonInstance().getLocation(getContext());
        if (location != null) {
            uri = Utils.replaceUriParameter(Uri.parse(categoriesModel.getCategoryUrl()), Utils.QUERY_PARAM_CITY_ID, String.valueOf(location.getId()));
            return uri.toString();
        }
        return categoriesModel.getCategoryUrl();
    }

    @Override
    public RequestParams getBrandParams() {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(Utils.BRAND_QUERY_PARAM_TREE, Utils.BRAND_QUERY_PARAM_BRAND);
        requestParams.putInt(Utils.QUERY_PARAM_CHILD_CATEGORY_ID, categoriesModel.getCategoryId());
        Location location = Utils.getSingletonInstance().getLocation(getContext());
        if (location != null)
            requestParams.putInt(Utils.QUERY_PARAM_CITY_ID, location.getId());
        return requestParams;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (getActivity() == null)
            return;
        switch (requestCode) {
            case DealsHomeActivity.REQUEST_CODE_DEALSLOCATIONACTIVITY:
                if (resultCode == RESULT_OK) {
                    Location location = Utils.getSingletonInstance().getLocation(getActivity());
                    if (location != null) {
                        Intent searchIntent = new Intent(getActivity(), DealsSearchActivity.class);
                        navigateToActivityRequest(searchIntent, DealsHomeActivity.REQUEST_CODE_DEALSSEARCHACTIVITY);
                    }
                }
                break;
            case DealsHomeActivity.REQUEST_CODE_DEALSSEARCHACTIVITY:
                if (resultCode == RESULT_OK) {
                    Location location = Utils.getSingletonInstance().getLocation(getActivity());
                    if (location != null && !TextUtils.isEmpty(locationName) && !TextUtils.isEmpty(location.getName()) && !locationName.equals(location.getName())) {
                        mPresenter.getCategoryDetails(true);
                        mPresenter.getBrandsList(true);
                    }

                }
                break;
            case DealsHomeActivity.REQUEST_CODE_DEALDETAILACTIVITY:
                if (resultCode == RESULT_OK) {
                    Location location = Utils.getSingletonInstance().getLocation(getActivity());
                    if (location != null && !TextUtils.isEmpty(locationName) && !TextUtils.isEmpty(location.getName()) && !locationName.equals(location.getName())) {
                        mPresenter.getCategoryDetails(true);
                        mPresenter.getBrandsList(true);
                    } else {
                        mPresenter.getCategoryDetails(false);
                        mPresenter.getBrandsList(false);
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
    public View getRootView() {
        return mainContent;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_see_all) {
            dealsAnalytics.sendAllBrandsClickEvent(DealsAnalytics.SEE_ALL_BRANDS_CATEGORY);
            Intent brandIntent = new Intent(getContext(), AllBrandsActivity.class);
            brandIntent.putParcelableArrayListExtra(AllBrandsActivity.EXTRA_LIST, (ArrayList<? extends Parcelable>) categoryList);
            brandIntent.putExtra("cat_id", String.valueOf(categoriesModel.getCategoryId()));
            brandIntent.putExtra(AllBrandsActivity.SEARCH_TEXT, searchInputView.getText().toString());
            navigateToActivity(brandIntent);
        } else if (v.getId() == R.id.tv_location_name) {
            mPresenter.getLocations();
        } else if (v.getId() == R.id.search_input_view) {
            Intent searchIntent = new Intent(getContext(), DealsSearchActivity.class);
            TopDealsCacheHandler.init().setTopDeals(this.categoryItems);
            searchIntent.putParcelableArrayListExtra(AllBrandsActivity.EXTRA_LIST, (ArrayList<? extends Parcelable>) categoryList);
            searchIntent.putExtra("cat_id", String.valueOf(categoriesModel.getCategoryId()));
            getContext().startActivity(searchIntent);
        } else {
            mPresenter.onOptionMenuClick(v.getId());
        }
    }

    @Override
    public void onDestroyView() {
        mPresenter.onDestroy();
        super.onDestroyView();
    }

    @Override
    public void onNavigateToActivityRequest(Intent intent, int requestCode, int position) {
        adapterPosition = position;
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

    @Override
    public void onLocationItemSelected(boolean locationUpdated) {
        Location location = Utils.getSingletonInstance().getLocation(getActivity());
        if (locationUpdated && location != null) {
            mPresenter.getBrandsList(true);
            mPresenter.getCategoryDetails(true);
            toolbarTitle.setText(location.getName());
        }
        if (selectLocationFragment != null) {
            selectLocationFragment.dismiss();
        }
    }

    @Override
    public void closeBottomsheet() {
        if (selectLocationFragment != null) {
            selectLocationFragment.dismiss();
        }
    }

    @Override
    public void startLocationFragment(List<Location> locations) {
        selectLocationFragment = CloseableBottomSheetDialog.createInstanceRounded(getActivity());
        selectLocationFragment.setCustomContentView(new SelectLocationBottomSheet(getContext(), false, locations, this, toolbarTitle.getText().toString(), this), "", false);
        selectLocationFragment.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_promo) {
            dealsAnalytics.sendEventDealsDigitalClick(DealsAnalytics.EVENT_CLICK_PROMO,
                    "");
            ((DealsModuleRouter) getActivity().getApplication())
                    .actionOpenGeneralWebView(getActivity(), DealsUrl.WebUrl.PROMOURL);
        } else if (id == R.id.action_booked_history) {
            dealsAnalytics.sendEventDealsDigitalClick(DealsAnalytics.EVENT_CLICK_DAFTAR_TRANSAKSI,
                    "");
            if (userSession.isLoggedIn()) {
                RouteManager.route(getActivity(), ApplinkConst.DEALS_ORDER);
            } else {
                Intent intent = ((DealsModuleRouter) getActivity().getApplication()).
                        getLoginIntent(getActivity());
                navigateToActivityRequest(intent, DealsHomeActivity.REQUEST_CODE_LOGIN);
            }
        } else if (id == R.id.action_faq) {
            dealsAnalytics.sendEventDealsDigitalClick(DealsAnalytics.EVENT_CLICK_BANTUAN,
                    "");
            ((DealsModuleRouter) getActivity().getApplication())
                    .actionOpenGeneralWebView(getActivity(), DealsUrl.WebUrl.FAQURL);
        }
        return true;
    }
}
