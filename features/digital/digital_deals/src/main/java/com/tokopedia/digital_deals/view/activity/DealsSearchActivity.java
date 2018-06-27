package com.tokopedia.digital_deals.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.digital_deals.R;
import com.tokopedia.digital_deals.di.DaggerDealsComponent;
import com.tokopedia.digital_deals.di.DealsComponent;
import com.tokopedia.digital_deals.di.DealsModule;
import com.tokopedia.digital_deals.view.adapter.DealsCategoryAdapter;
import com.tokopedia.digital_deals.view.adapter.TopDealsSuggestionsAdapter;
import com.tokopedia.digital_deals.view.contractor.DealsSearchContract;
import com.tokopedia.digital_deals.view.customview.SearchInputView;
import com.tokopedia.digital_deals.view.fragment.SelectLocationFragment;
import com.tokopedia.digital_deals.view.presenter.DealsSearchPresenter;
import com.tokopedia.digital_deals.view.utils.Utils;
import com.tokopedia.digital_deals.view.viewmodel.CategoryItemsViewModel;
import com.tokopedia.digital_deals.view.viewmodel.LocationViewModel;
import com.tokopedia.usecase.RequestParams;

import java.util.List;

import javax.inject.Inject;

import static com.tokopedia.digital_deals.view.activity.DealsHomeActivity.REQUEST_CODE_DEALSLOCATIONACTIVITY;

public class DealsSearchActivity extends BaseSimpleActivity implements
        DealsSearchContract.View, SearchInputView.Listener, android.view.View.OnClickListener {

    private final boolean IS_SHORT_LAYOUT = true;

    private CoordinatorLayout mainContent;
    private LinearLayout noContent;
    private LinearLayout llDeals;
    private CoordinatorLayout baseMainContent;
    private ConstraintLayout clLocation;

    private LinearLayoutManager layoutManager;

    private android.view.View progressBarLayout;
    private SearchInputView searchInputView;
    private RecyclerView rvDeals;
    private TextView tvTopDeals;
    private ImageView back;
    private TextView dealsInCity;
    private TextView tvCityName;
    private TextView tvChangeCity;

    private DealsComponent dealsComponent;
    @Inject
    public DealsSearchPresenter mPresenter;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_deals_search;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initInjector();
        executeInjector();
        setUpVariables();
        searchInputView.setListener(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        mPresenter.attachView(this);
        mPresenter.initialize();

    }

    private void setUpVariables() {
        rvDeals = findViewById(R.id.rv_search_results);
        searchInputView = findViewById(R.id.search_input_view);
        progressBarLayout = findViewById(R.id.progress_bar_layout);
        mainContent = findViewById(R.id.main_content);
        tvTopDeals = findViewById(R.id.tv_topevents);
        back = findViewById(R.id.imageViewBack);
        noContent = findViewById(R.id.no_content);
        llDeals = findViewById(R.id.ll_deals);
        dealsInCity = findViewById(R.id.deals_in_city);
        tvCityName = findViewById(R.id.tv_location);
        tvChangeCity = findViewById(R.id.tv_change_city);
        baseMainContent = findViewById(R.id.base_main_content);
        clLocation = findViewById(R.id.cl_location);
        back.setOnClickListener(this);
        tvChangeCity.setOnClickListener(this);
        searchInputView.setSearchHint(getResources().getString(R.string.search_input_hint_deals));
        searchInputView.setSearchTextSize(getResources().getDimension(R.dimen.sp_14));
        searchInputView.setSearchImageViewDimens(getResources().getDimensionPixelSize(R.dimen.dp_18), getResources().getDimensionPixelSize(R.dimen.dp_18));
        searchInputView.setSearchImageView(getResources().getDrawable(R.drawable.ic_search_grey));
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvDeals.setLayoutManager(layoutManager);

    }

    @Override
    public void onSearchSubmitted(String text) {
        back.setImageResource(R.drawable.ic_action_back);
        DrawableCompat.setTint(back.getDrawable(), ContextCompat.getColor(getActivity(), R.color.toolbar_home));
        mPresenter.searchSubmitted(text);
    }

    @Override
    public void onSearchTextChanged(String text) {
        back.setImageResource(R.drawable.ic_close_deals);
        mPresenter.searchTextChanged(text);
    }

    @Override
    public void showMessage(String message) {

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
    public void renderFromSearchResults(List<CategoryItemsViewModel> categoryItemsViewModels, String searchText, int count) {
        LocationViewModel location = Utils.getSingletonInstance().getLocation(getActivity());

        if (categoryItemsViewModels != null && categoryItemsViewModels.size() != 0) {
            DealsCategoryAdapter dealsCategoryAdapter = new DealsCategoryAdapter(getActivity(), categoryItemsViewModels, !IS_SHORT_LAYOUT);
            dealsInCity.setVisibility(View.VISIBLE);

            rvDeals.addOnScrollListener(rvOnScrollListener);
            rvDeals.setAdapter(dealsCategoryAdapter);
            llDeals.setVisibility(View.VISIBLE);
            noContent.setVisibility(View.GONE);
            tvTopDeals.setVisibility(View.GONE);
            String text = String.format(getString(R.string.deals_search_location_result), searchText);

            int startIndexOfLink = text.length();
            if (categoryItemsViewModels.size() != 0) {
                if (count == 0)
                    count = categoryItemsViewModels.size();
            }
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
                    ds.setColor(getResources().getColor(R.color.black_38)); // specific color for this link
                }
            }, startIndexOfLink, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            dealsInCity.setText(spannableString);
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(searchInputView.getSearchTextView().getWindowToken(), 0);
                rvDeals.requestFocus();
            }


        } else {
            llDeals.setVisibility(View.GONE);
            noContent.setVisibility(View.VISIBLE);
        }
        clLocation.setVisibility(View.VISIBLE);
        tvCityName.setText(location.getName());

    }

    @Override
    public void showProgressBar() {

    }

    @Override
    public void hideProgressBar() {

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
    public FragmentManager getFragmentManagerInstance() {
        return getSupportFragmentManager();
    }

    @Override
    public void setTrendingDeals(List<CategoryItemsViewModel> searchViewModels, LocationViewModel location) {


        if (searchViewModels != null && !searchViewModels.isEmpty()) {
            TopDealsSuggestionsAdapter adapter = new TopDealsSuggestionsAdapter(this, searchViewModels, mPresenter);
            llDeals.setVisibility(View.VISIBLE);
            rvDeals.setAdapter(adapter);
            rvDeals.removeOnScrollListener(rvOnScrollListener);
            tvTopDeals.setVisibility(View.VISIBLE);
            dealsInCity.setVisibility(View.GONE);
            noContent.setVisibility(View.GONE);
            clLocation.setVisibility(View.GONE);

        } else {
            llDeals.setVisibility(View.GONE);
            noContent.setVisibility(View.VISIBLE);
            clLocation.setVisibility(View.VISIBLE);

        }

        tvCityName.setText(location.getName());

    }

    @Override
    public void setSuggestions(List<CategoryItemsViewModel> suggestions, String highlight) {
        LocationViewModel location = Utils.getSingletonInstance().getLocation(getActivity());
        if (suggestions != null && !suggestions.isEmpty()) {
            TopDealsSuggestionsAdapter adapter = new TopDealsSuggestionsAdapter(this, suggestions, mPresenter);
            adapter.setHighLightText(highlight);
            llDeals.setVisibility(View.VISIBLE);
            rvDeals.setAdapter(adapter);
            rvDeals.addOnScrollListener(rvOnScrollListener);
            tvTopDeals.setVisibility(View.GONE);
            dealsInCity.setVisibility(View.GONE);
            noContent.setVisibility(View.GONE);
            clLocation.setVisibility(View.GONE);
        } else {
            llDeals.setVisibility(View.GONE);
            noContent.setVisibility(View.VISIBLE);
            clLocation.setVisibility(View.VISIBLE);
        }
        tvCityName.setText(location.getName());
    }

    @Override
    public void removeFooter(boolean searchSubmitted) {
        if (searchSubmitted) {
            ((DealsCategoryAdapter) rvDeals.getAdapter()).removeFooter();
        } else
            ((TopDealsSuggestionsAdapter) rvDeals.getAdapter()).removeFooter();
    }

    @Override
    public void addFooter(boolean searchSubmitted) {
        if (searchSubmitted)
            ((DealsCategoryAdapter) rvDeals.getAdapter()).addFooter();
        else
            ((TopDealsSuggestionsAdapter) rvDeals.getAdapter()).addFooter();
    }

    @Override
    public void addDealsToCards(List<CategoryItemsViewModel> categoryItemsViewModels) {
        ((DealsCategoryAdapter) rvDeals.getAdapter()).addAll(categoryItemsViewModels);
    }


    @Override
    public void addDeals(List<CategoryItemsViewModel> searchViewModels) {
        ((TopDealsSuggestionsAdapter) rvDeals.getAdapter()).addAll(searchViewModels);
    }

    @Override
    public LinearLayoutManager getLayoutManager() {
        return layoutManager;
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
                    LocationViewModel location = Utils.getSingletonInstance().getLocation(getActivity());
                    if (location == null) {
                        Toast.makeText(getActivity(), getResources().getString(R.string.select_location_first), Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        if (data != null) {
                            boolean isLocationUpdated = data.getBooleanExtra(SelectLocationFragment.EXTRA_CALLBACK_LOCATION, true);
                            if (isLocationUpdated)
                                Utils.getSingletonInstance().setSnackBarLocationChange(location.getName(), getActivity(), mainContent);
                        }
                        tvCityName.setText(location.getName());
                        if (searchInputView.getSearchText() != null && searchInputView.getSearchText() != "")
                            mPresenter.getDealsListBySearch(searchInputView.getSearchText());

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
        super.onDestroy();
    }

    @Override
    public void goBack() {
        setResult(RESULT_OK);
        super.onBackPressed();
    }
}
