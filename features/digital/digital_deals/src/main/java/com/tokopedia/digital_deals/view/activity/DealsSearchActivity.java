package com.tokopedia.digital_deals.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.view.KeyboardHandler;
import com.tokopedia.design.bottomsheet.CloseableBottomSheetDialog;
import com.tokopedia.digital_deals.R;
import com.tokopedia.digital_deals.di.DealsComponentInstance;
import com.tokopedia.digital_deals.view.TopDealsCacheHandler;
import com.tokopedia.digital_deals.view.adapter.DealsCategoryAdapter;
import com.tokopedia.digital_deals.view.adapter.DealsLocationAdapter;
import com.tokopedia.digital_deals.view.contractor.DealsSearchContract;
import com.tokopedia.digital_deals.view.customview.SearchInputView;
import com.tokopedia.digital_deals.view.fragment.DealsHomeFragment;
import com.tokopedia.digital_deals.view.fragment.SelectLocationBottomSheet;
import com.tokopedia.digital_deals.view.model.Location;
import com.tokopedia.digital_deals.view.model.ProductItem;
import com.tokopedia.digital_deals.view.presenter.DealsSearchPresenter;
import com.tokopedia.digital_deals.view.utils.DealsAnalytics;
import com.tokopedia.digital_deals.view.utils.Utils;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.List;

import javax.inject.Inject;

import static com.tokopedia.digital_deals.view.activity.DealsHomeActivity.REQUEST_CODE_DEALSLOCATIONACTIVITY;

public class DealsSearchActivity extends DealsBaseActivity implements
        DealsSearchContract.View, SearchInputView.Listener, android.view.View.OnClickListener, DealsCategoryAdapter.INavigateToActivityRequest, DealsLocationAdapter.ActionListener {

    private final boolean IS_SHORT_LAYOUT = true;

    private CoordinatorLayout mainContent;
    private LinearLayout noContent;
    private LinearLayout llDeals;
    private CoordinatorLayout baseMainContent;
    private ConstraintLayout clLocation;
    private LinearLayoutManager layoutManager;
    private SearchInputView searchInputView;
    private RecyclerView rvDeals;
    private ImageView back;
    private TextView tvCityName;
    private TextView tvChangeCity;
    private boolean firstTimeRefresh = true;

    @Inject
    public DealsSearchPresenter mPresenter;
    @Inject
    DealsAnalytics dealsAnalytics;
    private DealsCategoryAdapter dealsCategoryAdapter;
    private int listCount;
    private String searchText;
    private int adapterPosition = -1;
    private boolean forceRefresh;
    CloseableBottomSheetDialog selectLocationFragment;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_deals_search;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initInjector();
        setUpVariables();
        searchInputView.setListener(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        mPresenter.attachView(this);
        mPresenter.initialize();
    }

    private void setUpVariables() {
        rvDeals = findViewById(R.id.rv_search_results);
        searchInputView = findViewById(R.id.search_input_view);
        mainContent = findViewById(R.id.main_content);
        back = findViewById(R.id.imageViewBack);
        noContent = findViewById(R.id.no_content);
        llDeals = findViewById(R.id.ll_deals);
        tvCityName = findViewById(R.id.tv_location);
        tvChangeCity = findViewById(R.id.tv_change_city);
        baseMainContent = findViewById(R.id.base_main_content);
        clLocation = findViewById(R.id.cl_location);
        back.setOnClickListener(this);
        tvChangeCity.setOnClickListener(this);
        searchInputView.setSearchHint(getResources().getString(R.string.search_input_hint_deals));
        searchInputView.setSearchTextSize(getResources().getDimension(R.dimen.sp_14));
        searchInputView.setSearchImageViewDimens(getResources().getDimensionPixelSize(R.dimen.dp_18), getResources().getDimensionPixelSize(R.dimen.dp_18));
        searchInputView.setSearchImageView(getResources().getDrawable(R.drawable.ic_search_deal));
        EditText etSearch = searchInputView.findViewById(R.id.edit_text_search);
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvDeals.setLayoutManager(layoutManager);
        dealsCategoryAdapter = new DealsCategoryAdapter(null, DealsCategoryAdapter.SEARCH_PAGE, this, !IS_SHORT_LAYOUT);
        rvDeals.setAdapter(dealsCategoryAdapter);
        etSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    back.setImageResource(R.drawable.ic_close_deals);
                    clLocation.setVisibility(View.GONE);

                    dealsCategoryAdapter.setTopDealsLayout(true);
                    if (firstTimeRefresh)
                        firstTimeRefresh = false;
                    else if (!TextUtils.isEmpty(searchInputView.getSearchText()) && searchInputView.getSearchText().length() > 2)
                        dealsCategoryAdapter.removeHeaderAndFooter();
                    dealsCategoryAdapter.notifyDataSetChanged();
                }
            }
        });

    }

    @Override
    public void onSearchSubmitted(String text) {
        if (text.length() > 2)
            dealsAnalytics.sendEventDealsDigitalClick(DealsAnalytics.EVENT_SEARCH_VOUCHER_OR_OUTLET, text);
        back.setImageResource(R.drawable.ic_action_back);
        DrawableCompat.setTint(back.getDrawable(), ContextCompat.getColor(getActivity(), R.color.toolbar_home));
        mPresenter.searchSubmitted();
    }

    @Override
    public void onSearchTextChanged(String text) {
        if (text.length() > 2)
            dealsAnalytics.sendEventDealsDigitalClick(DealsAnalytics.EVENT_SEARCH_VOUCHER_OR_OUTLET, text);
        back.setImageResource(R.drawable.ic_close_deals);
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
        selectLocationFragment = CloseableBottomSheetDialog.createInstanceRounded(getActivity());
        selectLocationFragment.setContentView(new SelectLocationBottomSheet(this, isForFirstTime, locationList, this));
        selectLocationFragment.show();
    }

    @Override
    public void renderFromSearchResults() {
        if (listCount > 0) {
            Location location = Utils.getSingletonInstance().getLocation(getActivity());

            dealsCategoryAdapter.setTopDealsLayout(false);
            if (firstTimeRefresh)
                firstTimeRefresh = false;
            dealsCategoryAdapter.removeHeaderAndFooter();
            dealsCategoryAdapter.notifyDataSetChanged();
            rvDeals.addOnScrollListener(rvOnScrollListener);
            SpannableString headerString = getHeaderFormattedText(searchText, listCount);
            if (!TextUtils.isEmpty(headerString))
                dealsCategoryAdapter.addHeader(headerString);
            KeyboardHandler.hideSoftKeyboard(getActivity());
            rvDeals.requestFocus();
            clLocation.setVisibility(View.VISIBLE);
            tvCityName.setText(location.getName());
        } else {
            dealsAnalytics.sendEventDealsDigitalView(DealsAnalytics.EVENT_NO_DEALS,
                    searchText);
            llDeals.setVisibility(View.GONE);
            noContent.setVisibility(View.VISIBLE);
        }
    }

    private SpannableString getHeaderFormattedText(String searchText, int count) {
        String text = String.format(getString(R.string.deals_search_location_result), searchText);
        int startIndexOfLink = text.length();
        text += " " + String.format(getActivity().getResources().getString(R.string.number_of_items), count);
        SpannableString spannableString = new SpannableString(text);
        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View view) {
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
                ds.setColor(getResources().getColor(R.color.black_38));
            }
        }, startIndexOfLink, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    @Override
    public RequestParams getParams() {
        return RequestParams.EMPTY;
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
            rvDeals.clearOnScrollListeners();
            dealsCategoryAdapter.clearList();
            dealsCategoryAdapter.addAll(productItems, false);
            dealsCategoryAdapter.setTopDealsLayout(true);
            if (productItems.size() > 0) {
                if (count == 0)
                    listCount = productItems.size();
                else
                    listCount = count;
            }
            if (isTrendingDeals) {
                dealsCategoryAdapter.addHeader(new SpannableString(""));
                dealsCategoryAdapter.showHighLightText(false);
            } else {
                rvDeals.addOnScrollListener(rvOnScrollListener);
                dealsCategoryAdapter.showHighLightText(true);
                dealsCategoryAdapter.setHighLightText(highlight);
            }
            dealsCategoryAdapter.notifyDataSetChanged();

            llDeals.setVisibility(View.VISIBLE);

            noContent.setVisibility(View.GONE);
            clLocation.setVisibility(View.GONE);

        } else {
            dealsAnalytics.sendEventDealsDigitalView(DealsAnalytics.EVENT_NO_DEALS,
                    searchText);
            llDeals.setVisibility(View.GONE);
            noContent.setVisibility(View.VISIBLE);
            clLocation.setVisibility(View.VISIBLE);
        }
        if (location != null)
            tvCityName.setText(location.getName());

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
            case REQUEST_CODE_DEALSLOCATIONACTIVITY:

                if (resultCode == Activity.RESULT_OK) {
                    Location location = Utils.getSingletonInstance().getLocation(getActivity());
                    if (location == null) {
                        finish();
                    } else {
                        if (data != null) {
                            if (DealsHomeFragment.isLocationUpdated)
                                Utils.getSingletonInstance().showSnackBarDeals(location.getName(), getActivity(), mainContent, true);
                        }
                        tvCityName.setText(location.getName());
                        if (!TextUtils.isEmpty(searchInputView.getSearchText()))
                            mPresenter.getDealsListBySearch(searchInputView.getSearchText());

                    }
                }
                break;
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
    public void onLocationItemSelected(boolean locationUpdated) {
        Location location = Utils.getSingletonInstance().getLocation(getActivity());
        if (location == null) {
            finish();
        } else {
            tvCityName.setText(location.getName());
            if (selectLocationFragment != null) {
                selectLocationFragment.dismiss();
            }
            if (!TextUtils.isEmpty(searchInputView.getSearchText()))
                mPresenter.getDealsListBySearch(searchInputView.getSearchText());
        }
    }
}
