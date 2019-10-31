package com.tokopedia.filter.newdynamicfilter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.base.view.activity.BaseActivity;
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler;
import com.tokopedia.design.keyboard.KeyboardHelper;
import com.tokopedia.discovery.common.constants.SearchApiConst;
import com.tokopedia.filter.R;
import com.tokopedia.filter.common.data.CategoryFilterModel;
import com.tokopedia.filter.common.data.Filter;
import com.tokopedia.filter.common.data.Option;
import com.tokopedia.filter.newdynamicfilter.adapter.DynamicFilterAdapter;
import com.tokopedia.filter.newdynamicfilter.adapter.typefactory.DynamicFilterTypeFactory;
import com.tokopedia.filter.newdynamicfilter.adapter.typefactory.DynamicFilterTypeFactoryImpl;
import com.tokopedia.filter.newdynamicfilter.controller.FilterController;
import com.tokopedia.filter.newdynamicfilter.helper.DynamicFilterDbManager;
import com.tokopedia.filter.newdynamicfilter.helper.FilterDbHelper;
import com.tokopedia.filter.newdynamicfilter.helper.FilterDetailActivityRouter;
import com.tokopedia.filter.newdynamicfilter.helper.FilterHelper;
import com.tokopedia.filter.newdynamicfilter.helper.OptionHelper;
import com.tokopedia.filter.newdynamicfilter.view.DynamicFilterView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static com.tokopedia.filter.common.data.Option.KEY_CATEGORY;

/**
 * Created by henrypriyono on 8/8/17.
 */

public class RevampedDynamicFilterActivity extends BaseActivity implements DynamicFilterView {

    public static final String EXTRA_SELECTED_FILTERS = "EXTRA_SELECTED_FILTERS";
    public static final String EXTRA_SELECTED_OPTIONS = "EXTRA_SELECTED_OPTIONS";
    public static final String EXTRA_CALLER_SCREEN_NAME = "EXTRA_CALLER_SCREEN_NAME";
    public static final String EXTRA_QUERY_PARAMETERS = "EXTRA_QUERY_PARAMETERS";

    private RecyclerView recyclerView;
    private DynamicFilterAdapter adapter;
    private TextView buttonApply;
    private TextView buttonReset;
    private View buttonClose;
    private View mainLayout;
    private View loadingView;

    private FilterController filterController;

    private int selectedExpandableItemPosition;

    private CompositeSubscription compositeSubscription = new CompositeSubscription();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revamped_dynamic_filter);
        bindView();
        initKeyboardVisibilityListener();
        initRecyclerView();
        loadFilterItems();
    }

    private void bindView() {
        recyclerView = findViewById(R.id.dynamic_filter_recycler_view);
        buttonClose = findViewById(R.id.top_bar_close_button);
        buttonClose.setOnClickListener(v -> onBackPressed());
        buttonReset = findViewById(R.id.top_bar_button_reset);
        buttonReset.setVisibility(View.VISIBLE);
        buttonReset.setOnClickListener(v -> resetAllFilter());
        buttonApply = findViewById(R.id.button_finish);
        buttonApply.setOnClickListener(v -> applyFilter());
        mainLayout = findViewById(R.id.main_layout);
        loadingView = findViewById(R.id.loading_view);
    }

    private void initKeyboardVisibilityListener() {
        KeyboardHelper.setKeyboardVisibilityChangedListener(mainLayout, new KeyboardHelper.OnKeyboardVisibilityChangedListener() {
            @Override
            public void onKeyboardShown() {
                buttonApply.setVisibility(View.GONE);
            }

            @Override
            public void onKeyboardHide() {
                buttonApply.setVisibility(View.VISIBLE);
                mainLayout.requestFocus();
            }
        });
    }

    private void initRecyclerView() {
        filterController = new FilterController();
        DynamicFilterTypeFactory dynamicFilterTypeFactory = new DynamicFilterTypeFactoryImpl(this);
        adapter = new DynamicFilterAdapter(dynamicFilterTypeFactory);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        DividerItemDecoration dividerItemDecoration
                = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.bg_line_separator));
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    KeyboardHandler.hideSoftKeyboard(RevampedDynamicFilterActivity.this);
                }
            }
        });
    }

    @SuppressWarnings("unchecked")
    private void loadFilterItems() {
        compositeSubscription.add(
                Observable.just(new DynamicFilterDbManager())
                        .map(this::getFilterListFromDbManager)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(getSubsriberGetFilterListFromDb())
        );
    }

    private List<Filter> getFilterListFromDbManager(DynamicFilterDbManager manager) throws RuntimeException {
        String data = DynamicFilterDbManager.getFilterData(this, getIntent().getStringExtra(EXTRA_CALLER_SCREEN_NAME));
        if (data == null) {
            throw new RuntimeException("error get filter cache");
        } else {
            Type listType = new TypeToken<List<Filter>>() {}.getType();
            Gson gson = new Gson();
            return gson.fromJson(data, listType);
        }
    }

    private Subscriber<List<Filter>> getSubsriberGetFilterListFromDb() {
        return new Subscriber<List<Filter>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable throwable) {
                throwable.printStackTrace();
                Toast.makeText(RevampedDynamicFilterActivity.this, getString(R.string.error_get_local_dynamic_filter), Toast.LENGTH_LONG).show();
                finish();
            }

            @Override
            public void onNext(List<Filter> filterList) {
                List<Filter> initializedFilterList = FilterHelper.initializeFilterList(filterList);
                filterController.initFilterController(getSearchParameterFromIntent(), initializedFilterList);
                adapter.setFilterList(initializedFilterList);
            }
        };
    }

    private Map<String, String> getSearchParameterFromIntent() {
        Map<?, ?> searchParameterMapIntent = (Map<?, ?>)getIntent().getSerializableExtra(EXTRA_QUERY_PARAMETERS);

        Map<String, String> searchParameter = new HashMap<>(searchParameterMapIntent.size());

        for(Map.Entry<?, ?> entry: searchParameterMapIntent.entrySet()) {
            searchParameter.put(entry.getKey().toString(), entry.getValue().toString());
        }

        return searchParameter;
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(android.R.anim.fade_in, R.anim.push_down);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case AbstractDynamicFilterDetailActivity.REQUEST_CODE:
                    handleResultFromDetailPage(data);
                    break;
                case DynamicFilterLocationActivity.REQUEST_CODE:
                    handleResultFromLocationPage();
                    break;
                case DynamicFilterCategoryActivity.REQUEST_CODE:
                    handleResultFromCategoryPage(data);
                    break;
            }

            adapter.notifyItemChanged(selectedExpandableItemPosition);
        }

        hideLoading();
    }

    private void handleResultFromDetailPage(Intent data) {
        List<Option> optionList
                = data.getParcelableArrayListExtra(AbstractDynamicFilterDetailActivity.EXTRA_RESULT);

        filterController.setFilter(optionList);
    }

    private void handleResultFromLocationPage() {
        Observable.create(
                (Observable.OnSubscribe<List<Option>>) subscriber -> subscriber.onNext(FilterDbHelper.loadLocationFilterOptions(RevampedDynamicFilterActivity.this)))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Option>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<Option> optionList) {
                        filterController.setFilter(optionList);
                        adapter.notifyItemChanged(selectedExpandableItemPosition);
                        hideLoading();
                    }
                });
    }

    private void handleResultFromCategoryPage(Intent data) {
        String selectedCategoryId = data.getStringExtra(DynamicFilterCategoryActivity.EXTRA_SELECTED_CATEGORY_ID);

        CategoryFilterModel category = FilterHelper.getSelectedCategoryDetailsFromFilterList(adapter.getFilterList(), selectedCategoryId);

        String selectedCategoryNameFromList = category != null ? category.getCategoryName() : "";
        Option categoryOption = OptionHelper.generateOptionFromCategory(selectedCategoryId, selectedCategoryNameFromList);

        filterController.setFilter(categoryOption, true, true);
    }

    @Override
    public void onPriceSliderRelease(int minValue, int maxValue) {

    }

    @Override
    public void onPriceSliderPressed(int minValue, int maxValue) {

    }

    @Override
    public void onPriceEditedFromTextInput(int minValue, int maxValue) {

    }

    public void applyFilter() {
        renderFilterResult();
        finish();
    }

    private void renderFilterResult() {
        Intent intent = new Intent();
        HashMap<String, String> filterParameterHashMap = new HashMap<>(filterController.getParameter());
        HashMap<String, String> activeFilterParameterHashMap = new HashMap<>(filterController.getActiveFilterMap());

        intent.putExtra(EXTRA_QUERY_PARAMETERS, filterParameterHashMap);
        intent.putExtra(EXTRA_SELECTED_FILTERS, activeFilterParameterHashMap);
        intent.putParcelableArrayListExtra(EXTRA_SELECTED_OPTIONS, new ArrayList<>(filterController.getActiveFilterOptionList()));
        setResult(RESULT_OK, intent);
    }

    public void resetAllFilter() {
        filterController.resetAllFilters();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onExpandableItemClicked(Filter filter) {
        showLoading();
        selectedExpandableItemPosition = adapter.getItemPosition(filter);
        if (filter.isCategoryFilter()) {
            launchFilterCategoryPage(filter);
        } else {
            enrichWithInputState(filter);
            FilterDetailActivityRouter.launchDetailActivity(this, filter);
        }
    }

    private void launchFilterCategoryPage(Filter filter) {
        String categoryId = filterController.getFilterValue(SearchApiConst.SC);
        CategoryFilterModel selectedCategory = FilterHelper.getSelectedCategoryDetails(filter, categoryId);
        String selectedCategoryRootId = selectedCategory != null ? selectedCategory.getCategoryRootId() : "";

        FilterDetailActivityRouter
                .launchCategoryActivity(this, filter, selectedCategoryRootId, categoryId);
    }

    private void showLoading() {
        loadingView.setVisibility(View.VISIBLE);
    }

    private void hideLoading() {
        loadingView.setVisibility(View.GONE);
    }

    private void enrichWithInputState(Filter filter) {
        for (Option option : filter.getOptions()) {
            option.setInputState(
                    String.valueOf(filterController.getFilterViewState(option.getUniqueId()))
            );
        }
    }

    @Override
    public Boolean loadLastCheckedState(Option option) {
        return filterController.getFilterViewState(option);
    }

    @Override
    public void saveCheckedState(Option option, Boolean isChecked) {
        filterController.setFilter(option, isChecked);
    }

    @Override
    public void removeSavedTextInput(String uniqueId) {
        filterController.setFilter(OptionHelper.generateOptionFromUniqueId(uniqueId), false, true);

        String optionKey = OptionHelper.parseKeyFromUniqueId(uniqueId);
    }

    @Override
    public void saveTextInput(String uniqueId, String textInput) {
        Option textInputOption = OptionHelper.generateOptionFromUniqueId(uniqueId);
        textInputOption.setValue(textInput);
        filterController.setFilter(textInputOption, true, true);

        String key = textInputOption.getKey();
    }

    @Override
    public List<Option> getSelectedOptions(Filter filter) {
        return filterController.getSelectedOptions(filter);
    }

    @Override
    public void removeSelectedOption(Option option) {
        if (KEY_CATEGORY.equals(option.getKey())) {
            filterController.setFilter(option, false, true);
        } else {
            saveCheckedState(option, false);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeSubscription.unsubscribe();
    }

    @Override
    public String getFilterValue(String key) {
        return filterController.getFilterValue(key);
    }

    @Override
    public boolean getFilterViewState(String uniqueId) {
        return filterController.getFilterViewState(uniqueId);
    }

    @Override
    public void onPriceRangeClicked() {

    }
}
