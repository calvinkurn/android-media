package com.tokopedia.filter.widget;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.view.KeyboardHandler;
import com.tokopedia.design.base.BaseCustomView;
import com.tokopedia.design.keyboard.KeyboardHelper;
import com.tokopedia.discovery.common.constants.SearchApiConst;
import com.tokopedia.filter.R;

import com.tokopedia.filter.common.data.CategoryFilterModel;
import com.tokopedia.filter.common.data.Option;
import com.tokopedia.filter.newdynamicfilter.AbstractDynamicFilterDetailActivity;
import com.tokopedia.filter.newdynamicfilter.DynamicFilterCategoryActivity;
import com.tokopedia.filter.newdynamicfilter.DynamicFilterLocationActivity;
import com.tokopedia.filter.newdynamicfilter.adapter.DynamicFilterAdapter;
import com.tokopedia.filter.newdynamicfilter.adapter.typefactory.BottomSheetDynamicFilterTypeFactoryImpl;
import com.tokopedia.filter.newdynamicfilter.adapter.typefactory.DynamicFilterTypeFactory;
import com.tokopedia.filter.newdynamicfilter.analytics.FilterTracking;
import com.tokopedia.filter.newdynamicfilter.analytics.FilterTrackingData;
import com.tokopedia.filter.newdynamicfilter.controller.FilterController;
import com.tokopedia.filter.newdynamicfilter.helper.FilterDbHelper;
import com.tokopedia.filter.newdynamicfilter.helper.FilterDetailActivityRouter;
import com.tokopedia.filter.newdynamicfilter.helper.FilterHelper;
import com.tokopedia.filter.newdynamicfilter.helper.OptionHelper;
import com.tokopedia.filter.common.data.Filter;
import com.tokopedia.filter.newdynamicfilter.view.BottomSheetDynamicFilterView;

import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.app.Activity.RESULT_OK;

public class BottomSheetFilterView extends BaseCustomView implements BottomSheetDynamicFilterView {

    private RecyclerView filterMainRecyclerView;
    private DynamicFilterAdapter filterMainAdapter;
    private TextView buttonReset;
    private View buttonClose;
    private TextView buttonFinish;
    private View loadingView;
    private View bottomSheetLayout;
    private UserLockBottomSheetBehavior bottomSheetBehavior;
    private View rootView;

    private Callback callback;

    private int selectedExpandableItemPosition;

    private FilterController filterController;
    private FilterTrackingData trackingData;

    public BottomSheetFilterView(@NonNull Context context) {
        super(context);
        init();
    }

    public BottomSheetFilterView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public BottomSheetFilterView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        init();
    }

    private void init() {
        rootView = inflate(getContext(), R.layout.filter_bottom_sheet, this);
        filterMainRecyclerView = rootView.findViewById(R.id.dynamic_filter_recycler_view);
        buttonClose = rootView.findViewById(R.id.top_bar_close_button);
        buttonReset = rootView.findViewById(R.id.top_bar_button_reset);
        bottomSheetLayout = this;
        buttonFinish = rootView.findViewById(R.id.button_finish);
        loadingView = rootView.findViewById(R.id.filterProgressBar);
        initKeyboardVisibilityListener();
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public void setFilterResultCount(String formattedResultCount) {
        buttonFinish.setText(String.format(getContext().getString(R.string.bottom_sheet_filter_finish_button_template_text), formattedResultCount));
        loadingView.setVisibility(View.GONE);
    }

    private void closeView() {
        if (bottomSheetBehavior != null
                && bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_HIDDEN
                && buttonFinish.getVisibility() == View.VISIBLE) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        }
    }

    @Override
    public void onExpandableItemClicked(Filter filter) {
        selectedExpandableItemPosition = filterMainAdapter.getItemPosition(filter);
        if (filter.isCategoryFilter()) {
            launchFilterCategoryPage(filter);
        } else {
            enrichWithInputState(filter);


            FilterTracking.eventNavigateToFilterDetail(trackingData, filter.getTitle());
            FilterDetailActivityRouter.launchDetailActivity(callback.getActivity(), filter, true, trackingData);
        }
    }

    private void launchFilterCategoryPage(Filter filter) {
        String categoryId = filterController.getFilterValue(SearchApiConst.SC);
        CategoryFilterModel selectedCategory = FilterHelper.getSelectedCategoryDetails(filter, categoryId);
        String selectedCategoryRootId = selectedCategory != null ? selectedCategory.getCategoryRootId() : "";

        FilterTracking.eventNavigateToFilterDetail(trackingData, getResources().getString(R.string.title_category));
        FilterDetailActivityRouter.launchCategoryActivity(callback.getActivity(),
                filter, selectedCategoryRootId, categoryId, true, trackingData);
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
        saveCheckedState(option, isChecked, "");
    }

    public void saveCheckedState(Option option, Boolean isChecked, String filterTitle) {
        FilterTracking.eventFilterJourney(trackingData, filterTitle, option.getName(),
                false, isChecked, option.isAnnotation());
        filterController.setFilter(option, isChecked);
        applyFilter();
    }

    private void updateResetButtonVisibility() {
        if (buttonReset != null) {
            buttonReset.setVisibility(filterController.isFilterActive() ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public void removeSavedTextInput(String uniqueId) {
        Option option = OptionHelper.generateOptionFromUniqueId(uniqueId);
        filterController.setFilter(option, false, true);

        String key = OptionHelper.parseKeyFromUniqueId(uniqueId);
        FilterTracking.eventFilterJourney(trackingData, key, "",
                false, false, option.isAnnotation());
        updateResetButtonVisibility();
    }

    @Override
    public void saveTextInput(String uniqueId, String textInput) {
        Option textInputOption = OptionHelper.generateOptionFromUniqueId(uniqueId);
        textInputOption.setValue(textInput);
        filterController.setFilter(textInputOption, true, true);

        String key = textInputOption.getKey();
        FilterTracking.eventFilterJourney(trackingData, key, textInput,
                false, true, textInputOption.isAnnotation());
        updateResetButtonVisibility();
    }

    @Override
    public List<Option> getSelectedOptions(Filter filter) {
        return filterController.getSelectedAndPopularOptions(filter);
    }

    @Override
    public void removeSelectedOption(Option option) {
        removeSelectedOption(option, "");
    }

    @Override
    public void removeSelectedOption(Option option, String filterTitle) {
        if (Option.KEY_CATEGORY.equals(option.getKey())) {
            FilterTracking.eventFilterJourney(trackingData, filterTitle, option.getName(),
                    false, false, option.isAnnotation());
            filterController.setFilter(option, false, true);
            applyFilter();
        } else {
            saveCheckedState(option, false, filterTitle);
        }
    }

    private void resetAllFilter() {
        filterController.resetAllFilters();
        filterMainAdapter.notifyDataSetChanged();

        applyFilter();
    }

    public void initFilterBottomSheet(FilterTrackingData trackingData) {
        this.trackingData = trackingData;
        initBottomSheetListener();
        initFilterMainRecyclerView();
    }

    private void initFilterMainRecyclerView() {
        filterController = new FilterController();
        DynamicFilterTypeFactory dynamicFilterTypeFactory = new BottomSheetDynamicFilterTypeFactoryImpl(this);
        filterMainAdapter = new DynamicFilterAdapter(dynamicFilterTypeFactory);
        filterMainRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        DividerItemDecoration dividerItemDecoration
                = new DividerItemDecoration(filterMainRecyclerView.getContext(), DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.bg_line_separator));
        filterMainRecyclerView.addItemDecoration(dividerItemDecoration);
        filterMainRecyclerView.setAdapter(filterMainAdapter);
        filterMainRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    KeyboardHandler.hideSoftKeyboard(callback.getActivity());
                }
            }
        });
    }

    public void launchFilterBottomSheet() {
        if (bottomSheetBehavior != null) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
    }

    public void loadFilterItems(List<Filter> filterList, Map<String, String> searchParameter) {
        List<Filter> initializedFilterList = FilterHelper.initializeFilterList(filterList);

        filterController.initFilterController(searchParameter, initializedFilterList);
        updateResetButtonVisibility();
        filterMainAdapter.setFilterList(initializedFilterList);
    }

    private void initBottomSheetListener() {
        bottomSheetBehavior = (UserLockBottomSheetBehavior) UserLockBottomSheetBehavior.from(bottomSheetLayout);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    callback.onHide();
                } else {
                    callback.onShow();
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
        buttonClose.setOnClickListener(v -> closeView());
        buttonFinish.setOnClickListener(v -> closeView());
        buttonReset.setOnClickListener(v -> resetAllFilter());
    }

    @Override
    public void onPriceSliderRelease(int minValue, int maxValue) {
        if (filterController.isSliderValueHasChanged(minValue, maxValue)) {
            applyFilter();
        }
    }

    @Override
    public void onPriceSliderPressed(int minValue, int maxValue) {
        filterController.saveSliderValueStates(minValue, maxValue);
    }

    @Override
    public void onPriceEditedFromTextInput(int minValue, int maxValue) {
        applyFilter();
    }

    @Override
    public boolean isSelectedCategory(Option option) {
        return filterController.getFilterViewState(option);
    }

    @Override
    public void selectCategory(Option option, String filterTitle) {
        FilterTracking.eventFilterJourney(trackingData, filterTitle, option.getName(),
                false, true, option.isAnnotation());
        filterController.setFilter(option, true, true);
        applyFilter();
    }

    private boolean isBottomSheetShown() {
        return bottomSheetBehavior != null
                && bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_HIDDEN;
    }

    public boolean onBackPressed() {
        if (isBottomSheetShown()) {
            closeView();
            return true;
        } else {
            return false;
        }
    }

    private void applyFilter() {
        updateResetButtonVisibility();
        loadingView.setVisibility(View.VISIBLE);
        buttonFinish.setText("");
        callback.onApplyFilter(filterController.getParameter());
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
            updateResetButtonVisibility();
        }
    }

    private void handleResultFromDetailPage(Intent data) {
        List<Option> optionList
                = data.getParcelableArrayListExtra(AbstractDynamicFilterDetailActivity.EXTRA_RESULT);

        filterController.setFilter(optionList);
        applyFilterFromDetailPage();
    }

    private void handleResultFromLocationPage() {
        Observable.create(
                (Observable.OnSubscribe<List<Option>>) subscriber -> subscriber.onNext(FilterDbHelper.loadLocationFilterOptions(getContext())))
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
                        applyFilterFromDetailPage();
                    }
                });
    }

    private void handleResultFromCategoryPage(Intent data) {
        String selectedCategoryId = data.getStringExtra(DynamicFilterCategoryActivity.EXTRA_SELECTED_CATEGORY_ID);

        CategoryFilterModel category = FilterHelper.getSelectedCategoryDetailsFromFilterList(filterMainAdapter.getFilterList(), selectedCategoryId);

        String selectedCategoryName = category != null ? category.getCategoryName() : "";
        Option categoryOption = OptionHelper.generateOptionFromCategory(selectedCategoryId, selectedCategoryName);

        filterController.setFilter(categoryOption, true, true);
        applyFilterFromDetailPage();
    }

    private void applyFilterFromDetailPage() {
        filterMainAdapter.notifyItemChanged(selectedExpandableItemPosition);
        applyFilter();
    }

    private void initKeyboardVisibilityListener() {
        KeyboardHelper.setKeyboardVisibilityChangedListener(bottomSheetLayout, new KeyboardHelper.OnKeyboardVisibilityChangedListener() {
            @Override
            public void onKeyboardShown() {
                if (bottomSheetBehavior != null
                        && bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                    buttonFinish.setVisibility(View.GONE);
                }
            }

            @Override
            public void onKeyboardHide() {
                if (bottomSheetBehavior != null
                        && bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                    buttonFinish.setVisibility(View.VISIBLE);
                }
            }
        });
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
        applyFilter();
    }

    public interface Callback {
        void onApplyFilter(Map<String, String> filterParameter);
        void onShow();
        void onHide();
        AppCompatActivity getActivity();
    }
}
