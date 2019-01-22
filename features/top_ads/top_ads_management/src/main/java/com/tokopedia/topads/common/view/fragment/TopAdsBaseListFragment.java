package com.tokopedia.topads.common.view.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.MenuRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.Px;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter;
import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel;
import com.tokopedia.abstraction.base.view.adapter.model.ErrorNetworkModel;
import com.tokopedia.abstraction.base.view.adapter.viewholders.EmptyResultViewHolder;
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment;
import com.tokopedia.abstraction.base.view.listener.EndlessLayoutManagerListener;
import com.tokopedia.design.button.BottomActionView;
import com.tokopedia.design.label.LabelView;
import com.tokopedia.design.text.SearchInputView;
import com.tokopedia.design.utils.DateLabelUtils;
import com.tokopedia.datepicker.range.view.activity.DatePickerActivity;
import com.tokopedia.datepicker.range.view.constant.DatePickerConstant;
import com.tokopedia.topads.R;
import com.tokopedia.topads.common.view.adapter.TopAdsMultipleCheckListAdapter;
import com.tokopedia.topads.common.view.adapter.TopAdsOptionMenuAdapter;
import com.tokopedia.topads.common.view.adapter.viewholder.BaseMultipleCheckViewHolder;
import com.tokopedia.topads.common.view.presenter.TopAdsBaseListPresenter;
import com.tokopedia.topads.common.TopAdsMenuBottomSheets;
import com.tokopedia.topads.dashboard.constant.SortTopAdsOption;
import com.tokopedia.topads.dashboard.constant.TopAdsConstant;
import com.tokopedia.topads.dashboard.constant.TopAdsExtraConstant;
import com.tokopedia.topads.dashboard.utils.TopAdsDatePeriodUtil;
import com.tokopedia.topads.dashboard.view.activity.TopAdsSortByActivity;
import com.tokopedia.topads.dashboard.view.model.Ad;
import com.tokopedia.topads.dashboard.view.model.TopAdsSortByModel;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by hadi.putra on 04/05/18.
 */

public abstract class TopAdsBaseListFragment<V extends Visitable, F extends AdapterTypeFactory, P extends TopAdsBaseListPresenter>
        extends BaseListFragment<V, F>
        implements SearchInputView.Listener, EmptyResultViewHolder.Callback,
        TopAdsMultipleCheckListAdapter.TopAdsItemClickedListener<V>, BaseMultipleCheckViewHolder.CheckedCallback<V>,
        BaseMultipleCheckViewHolder.OptionMoreCallback<V>{

    protected static final long DEFAULT_DELAY_TEXT_CHANGED = TimeUnit.MILLISECONDS.toMillis(300);
    protected static final String EXTRA_STATUS = "EXTRA_STATUS";
    protected static final String EXTRA_KEYWORD = "EXTRA_KEYWORD";
    protected static final int REQUEST_CODE_AD_CHANGE = 2;
    protected static final int REQUEST_CODE_AD_FILTER = 3;
    protected static final int REQUEST_CODE_AD_ADD = 4;
    protected static final int REQUEST_CODE_AD_SORT_BY = 5;

    protected Date startDate;
    protected Date endDate;

    @Px
    private int tempTopPaddingRecycleView;
    @Px
    private int tempBottomPaddingRecycleView;
    private int scrollFlags;
    protected TopAdsSortByModel selectedSort;
    protected int status;
    protected String keyword;
    private boolean isSearchMode;
    private boolean isMenuShown;
    private OnAdListFragmentListener listener;

    protected SearchInputView searchInputView;
    private AppBarLayout appBarLayout;
    private LabelView dateLabelView;
    private BottomActionView buttonActionView;
    private CoordinatorLayout coordinatorLayout;
    private CoordinatorLayout.Behavior appBarBehaviour;
    private RecyclerView recyclerView;
    private MenuItem menuAdd;
    private MenuItem menuMulti;
    private MenuItem menuHelp;
    private ActionMode actionMode;
    private LinearLayoutManager layoutManager;

    @Override
    protected String getScreenName() {
        return getClass().getSimpleName();
    }

    @Override
    protected void initInjector() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnAdListFragmentListener){
            listener = (OnAdListFragmentListener) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_top_ads_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            onFirstTimeLaunched();
        } else {
            onRestoreState (savedInstanceState);
        }
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        tempTopPaddingRecycleView = recyclerView.getPaddingTop();
        tempBottomPaddingRecycleView = recyclerView.getPaddingBottom();
        initComponentView(view);

        initErrorNetworkViewModel();
    }

    private void initErrorNetworkViewModel() {
        ErrorNetworkModel errorNetworkModel = new ErrorNetworkModel();
        errorNetworkModel.setIconDrawableRes(R.drawable.ic_error_network);
        getAdapter().setErrorNetworkModel(errorNetworkModel);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getPresenter().detachView();
    }

    private void initComponentView(View view) {
        coordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.coordinator_layout);
        appBarLayout = (AppBarLayout) view.findViewById(R.id.app_bar_layout);
        dateLabelView = (LabelView) view.findViewById(R.id.date_label_view);
        dateLabelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePicker();
            }
        });
        AppBarLayout.LayoutParams dateLabelViewLayoutParams = (AppBarLayout.LayoutParams) dateLabelView.getLayoutParams();
        scrollFlags = dateLabelViewLayoutParams.getScrollFlags();
        searchInputView = (SearchInputView) view.findViewById(R.id.search_input_view);
        searchInputView.setDelayTextChanged(DEFAULT_DELAY_TEXT_CHANGED);
        searchInputView.setListener(this);
        buttonActionView = (BottomActionView) view.findViewById(R.id.bottom_action_view);
        buttonActionView.setButton1OnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToSortBy();
            }
        });
        buttonActionView.setButton2OnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToFilter();
            }
        });
        appBarBehaviour = new AppBarLayout.Behavior();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == DatePickerConstant.REQUEST_CODE_DATE) {
            if (data != null) {
                handlingResultDateSelection(data);
            }
        } else if (data != null && (requestCode == REQUEST_CODE_AD_ADD || requestCode == REQUEST_CODE_AD_CHANGE)) {
            boolean adChanged = data.getBooleanExtra(TopAdsExtraConstant.EXTRA_AD_CHANGED, false);
            boolean adDeleted = data.getBooleanExtra(TopAdsExtraConstant.EXTRA_AD_DELETED, false);
            if (adChanged || adDeleted) {
                loadInitialData();
                setResultAdListChanged();
            }
        } else if (requestCode == REQUEST_CODE_AD_FILTER) {
            setSearchMode(true);
        } else if (requestCode == REQUEST_CODE_AD_SORT_BY) {
            setSearchMode(false);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(EXTRA_STATUS, status);
        outState.putString(EXTRA_KEYWORD, keyword);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState == null) {
            return;
        }
        status = savedInstanceState.getInt(EXTRA_STATUS);
        keyword = savedInstanceState.getString(EXTRA_KEYWORD);
    }

    @Nullable
    @Override
    public SwipeRefreshLayout getSwipeRefreshLayout(View view) {
        return (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
    }

    protected void finishActionMode(){
        if (actionMode != null){
            actionMode.finish();
        }
    }

    protected void setResultAdListChanged() {
        Intent intent = new Intent();
        intent.putExtra(TopAdsExtraConstant.EXTRA_AD_CHANGED, true);
        getActivity().setResult(Activity.RESULT_OK, intent);
    }

    private void handlingResultDateSelection(Intent data){
        long sDate = data.getLongExtra(DatePickerConstant.EXTRA_START_DATE, -1);
        long eDate = data.getLongExtra(DatePickerConstant.EXTRA_END_DATE, -1);
        int lastSelection = data.getIntExtra(DatePickerConstant.EXTRA_SELECTION_PERIOD, 1);
        int selectionType = data.getIntExtra(DatePickerConstant.EXTRA_SELECTION_TYPE, DatePickerConstant.SELECTION_TYPE_PERIOD_DATE);
        if (sDate != -1 && eDate != -1) {
            startDate = new Date(sDate);
            endDate = new Date(eDate);
            getPresenter().saveDate(startDate, endDate);
            getPresenter().saveSelectionDatePicker(selectionType, lastSelection);
            trackingDateTopAds(lastSelection, selectionType);
            updateLabelDateView(startDate, endDate);
            loadInitialData();
        }
    }

    public abstract void trackingDateTopAds(int lastSelection, int selectionType);

    public abstract void goToFilter();

    public abstract P getPresenter();

    public abstract void onCreateAd();

    public abstract void deleteAd(List<String> ids);

    public abstract Visitable getDefaultEmptyViewModel();

    public abstract void onFirstTimeLaunched();

    public abstract void onRestoreState(Bundle savedInstanceState);

    public abstract void showBulkActionBottomSheet(List<String> adIds);

    public abstract void deleteBulkAction(List<String> adIds);

    public abstract TopAdsMenuBottomSheets.OnMenuItemSelected getOptionMoreBottomSheetItemClickListener(final List<String> ids);

    public void setSearchMode(boolean searchMode) {
        isSearchMode = searchMode;
    }

    public Visitable getSearchEmptyViewModel(){
        EmptyModel emptyModel = new EmptyModel();
        emptyModel.setIconRes(R.drawable.ic_empty_state_kaktus);
        emptyModel.setTitle(getString(R.string.top_ads_empty_promo_not_found_title_empty_text));
        emptyModel.setContent(getString(R.string.top_ads_empty_promo_not_found_content_empty_text));
        return emptyModel;
    }

    @Override
    protected EndlessLayoutManagerListener getEndlessLayoutManagerListener() {
        return new EndlessLayoutManagerListener() {
            public RecyclerView.LayoutManager getCurrentLayoutManager() {
                return recyclerView.getLayoutManager();
            }
        };
    }

    @Override
    protected Visitable getEmptyDataViewModel() {
        if (isSearchMode){
            return getSearchEmptyViewModel();
        } else {
            return getDefaultEmptyViewModel();
        }
    }

    public void openDatePicker(){
        Intent intent = getDatePickerIntent(getActivity(), startDate, endDate);
        startActivityForResult(intent, DatePickerConstant.REQUEST_CODE_DATE);
    }

    public void goToSortBy() {
        Intent intent = TopAdsSortByActivity.createIntent(getActivity(), selectedSort == null?
                SortTopAdsOption.LATEST : selectedSort.getId());
        startActivityForResult(intent, REQUEST_CODE_AD_SORT_BY);
    }

    public void updateLabelDateView(Date startDate, Date endDate) {
        dateLabelView.setContent(DateLabelUtils.getRangeDateFormatted(getActivity(), startDate.getTime(), endDate.getTime()));
    }

    @Override
    protected F getAdapterTypeFactory() {
        return null;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getPresenter().isDateUpdated(startDate, endDate)){
            startDate = getPresenter().getStartDate();
            endDate = getPresenter().getEndDate();
            updateLabelDateView(startDate, endDate);
        }
    }

    @Override
    public void onSearchSubmitted(String text) {
        CommonUtils.hideKeyboard(getActivity(), getActivity().getCurrentFocus());
        if (TextUtils.isEmpty(text)) {
            return;
        }
        setSearchMode(true);
        onSearch(text);
    }

    @Override
    public void onSearchTextChanged(String text) {
        if (TextUtils.isEmpty(text)) {
            setSearchMode(false);
            onSearch(text);
        }
    }

    @NonNull
    @Override
    protected BaseListAdapter<V, F> createAdapterInstance() {
        TopAdsMultipleCheckListAdapter<V, F> adapter = new TopAdsMultipleCheckListAdapter<>(getAdapterTypeFactory());
        adapter.setCheckedCallback(this);
        adapter.setItemClickedListener(this);
        return adapter;
    }

    private void onSearch(String text){
        this.keyword = text;
        loadInitialData();
    }

    public void onSuccessLoadedData(List<V> data, boolean hasNextPage){
        if (!hasNextPage && data.size() < 1){
            if (isSearchMode){
                showOption(true);
            } else {
                showOption(false);
            }
        } else {
            showOption(true);
        }
        super.renderList(data, hasNextPage);
    }

    protected void showOption(boolean show) {
        isMenuShown = show;
        getActivity().invalidateOptionsMenu();
        if(buttonActionView != null)
            buttonActionView.setVisibility(show ? View.VISIBLE : View.GONE);
        showDateLabel(show);
        showSearchView(show);
        if(menuAdd != null){
            menuAdd.setVisible(show);
        }
        if (menuMulti != null){
            menuMulti.setVisible(show);
        }
        if (menuHelp != null){
            menuHelp.setVisible(show);
        }
    }

    protected void showDateLabel(boolean show) {
        @Px int topPadding = 0;
        @Px int bottomPadding = 0;
        if (show) {
            topPadding = tempTopPaddingRecycleView;
            bottomPadding = tempBottomPaddingRecycleView;
        }
        recyclerView.setPadding(0, topPadding, 0, bottomPadding);
        if (appBarLayout != null) {
            AppBarLayout.LayoutParams dateLabelLayoutParams = (AppBarLayout.LayoutParams) dateLabelView.getLayoutParams();
            dateLabelLayoutParams.setScrollFlags(show ? scrollFlags : 0);
            dateLabelView.setLayoutParams(dateLabelLayoutParams);

            CoordinatorLayout.LayoutParams appBarLayoutParams = (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();
            appBarLayoutParams.setBehavior(show ? appBarBehaviour : null);
            appBarLayout.setLayoutParams(appBarLayoutParams);
        }
        if (dateLabelView != null) {
            dateLabelView.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    protected void showSearchView(boolean show) {
        @Px int topPadding = 0;
        @Px int bottomPadding = 0;
        if (show) {
            topPadding = tempTopPaddingRecycleView;
            bottomPadding = tempBottomPaddingRecycleView;
        }
        recyclerView.setPadding(0, topPadding, 0, bottomPadding);
        if (appBarLayout != null) {
            AppBarLayout.LayoutParams dateLabelLayoutParams = (AppBarLayout.LayoutParams) searchInputView.getLayoutParams();
            dateLabelLayoutParams.setScrollFlags(show ? scrollFlags : 0);
            searchInputView.setLayoutParams(dateLabelLayoutParams);

            CoordinatorLayout.LayoutParams appBarLayoutParams = (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();
            appBarLayoutParams.setBehavior(show ? appBarBehaviour : null);
            appBarLayout.setLayoutParams(appBarLayoutParams);
        }
        if (searchInputView != null) {
            searchInputView.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    private Intent getDatePickerIntent(Context context, Date start, Date end){
        Intent intent = new Intent(context, DatePickerActivity.class);
        Calendar maxCalendar = Calendar.getInstance();
        maxCalendar.set(Calendar.HOUR_OF_DAY, 23);
        maxCalendar.set(Calendar.MINUTE, 59);
        maxCalendar.set(Calendar.SECOND, 59);

        Calendar minCalendar = Calendar.getInstance();
        minCalendar.add(Calendar.YEAR, -1);
        minCalendar.set(Calendar.HOUR_OF_DAY, 0);
        minCalendar.set(Calendar.MINUTE, 0);
        minCalendar.set(Calendar.SECOND, 0);
        minCalendar.set(Calendar.MILLISECOND, 0);

        intent.putExtra(DatePickerConstant.EXTRA_START_DATE, start.getTime());
        intent.putExtra(DatePickerConstant.EXTRA_END_DATE, end.getTime());

        intent.putExtra(DatePickerConstant.EXTRA_MIN_START_DATE, minCalendar.getTimeInMillis());
        intent.putExtra(DatePickerConstant.EXTRA_MAX_END_DATE, maxCalendar.getTimeInMillis());
        intent.putExtra(DatePickerConstant.EXTRA_MAX_DATE_RANGE, TopAdsConstant.MAX_DATE_RANGE);

        intent.putExtra(DatePickerConstant.EXTRA_DATE_PERIOD_LIST, TopAdsDatePeriodUtil.getPeriodRangeList(getActivity()));
        intent.putExtra(DatePickerConstant.EXTRA_SELECTION_PERIOD, getPresenter().getLastSelectionDatePickerIndex());
        intent.putExtra(DatePickerConstant.EXTRA_SELECTION_TYPE, getPresenter().getLastSelectionDatePickerType());

        intent.putExtra(DatePickerConstant.EXTRA_PAGE_TITLE, getActivity().getString(R.string.title_date_picker));
        return intent;
    }

    @Override
    public void onCreateOptionsMenu(final Menu menu, MenuInflater inflater) {
        menu.clear();
        initMenuItem(menu, inflater);
        super.onCreateOptionsMenu(menu, inflater);
    }

    protected void initMenuItem(final Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_top_ads_list, menu);
        menuAdd = menu.findItem(R.id.menu_add);
        menuAdd.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                onCreateAd();
                return true;
            }
        });

        menuMulti = menu.findItem(R.id.menu_multi_select);
        menuMulti.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                getActivity().startActionMode(getActionModeCallback());
                return true;
            }
        });

        menuHelp = menu.findItem(R.id.menu_help);
        menuHelp.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (listener != null) {
                    recyclerView.scrollToPosition(0);
                    appBarLayout.setExpanded(true, true);
                    showButtonActionView();
                    listener.startShowCase();
                }
                return true;
            }
        });
    }

    private void showButtonActionView(){
        View containerActionView = getView().findViewById(R.id.container_action_view);
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) containerActionView.getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();
        behavior.onNestedPreScroll(coordinatorLayout, containerActionView, null, 0, -1000, null);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menuAdd.setVisible(isMenuShown);
        menuMulti.setVisible(isMenuShown);
        menuHelp.setVisible(isMenuShown);
    }

    public ActionMode.Callback getActionModeCallback() {
        return new ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                actionMode.setTitle(getString(R.string.topads_multi_select_title,
                    ((TopAdsMultipleCheckListAdapter)getAdapter()).getTotalChecked()));
                TopAdsBaseListFragment.this.actionMode = actionMode;
                getActivity().getMenuInflater().inflate(R.menu.menu_top_ads_action_menu, menu);
                setAdapterActionMode(true);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                final List<String> productIdList = ((TopAdsMultipleCheckListAdapter)getAdapter()).getListChecked();
                if (menuItem.getItemId() == R.id.delete_product_menu) {
                    deleteBulkAction(productIdList);
                } else if (menuItem.getItemId() == R.id.menu_more){
                    showBulkActionBottomSheet(productIdList);
                }
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode actionMode) {
                setAdapterActionMode(false);
                ((TopAdsMultipleCheckListAdapter)getAdapter()).resetCheckedItemSet();
                TopAdsBaseListFragment.this.actionMode = null;
            }
        };
    }

    protected void setAdapterActionMode(boolean isActionMode) {
        ((TopAdsMultipleCheckListAdapter)getAdapter()).setActionMode(isActionMode);
        getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onEmptyContentItemTextClicked() {

    }

    @Override
    public void onEmptyButtonClicked() {
        onCreateAd();
    }

    @Override
    public void onItemChecked(V item, boolean isChecked) {
        if (actionMode != null) {
            int totalChecked = ((TopAdsMultipleCheckListAdapter)getAdapter()).getTotalChecked();
            actionMode.setTitle(getString(R.string.topads_multi_select_title,totalChecked));
            MenuItem deleteMenuItem = actionMode.getMenu().findItem(R.id.delete_product_menu);
            MenuItem moreMenuItem = actionMode.getMenu().findItem(R.id.menu_more);
            deleteMenuItem.setVisible(totalChecked > 0);
            moreMenuItem.setVisible(totalChecked > 0);
        } else {
            ((TopAdsMultipleCheckListAdapter)getAdapter()).setChecked(((Ad) item).getId(), isChecked);
            getAdapter().notifyDataSetChanged();
        }
    }

    protected void showDeleteConfirmation(String title, String content, final List<String> ids) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity(), R.style.AppCompatAlertDialogStyle);
        alertDialog.setTitle(title);
        alertDialog.setMessage(content);
        alertDialog.setPositiveButton(R.string.action_discard, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteAd(ids);
            }
        });
        alertDialog.setNegativeButton(R.string.action_keep, null);
        alertDialog.show();
    }

    protected void showBottomsheetOptionMore(String title, @MenuRes int menuId,
                                             final TopAdsMenuBottomSheets.OnMenuItemSelected listener){
        CommonUtils.hideKeyboard(getActivity(), getActivity().getCurrentFocus());

        final TopAdsMenuBottomSheets bottomSheetMenu = new TopAdsMenuBottomSheets()
                .setContext(getContext())
                .setMode(TopAdsOptionMenuAdapter.MODE_DEFAULT)
                .setTitle(title)
                .setMenu(menuId);

        bottomSheetMenu.setMenuItemSelected(new TopAdsMenuBottomSheets.OnMenuItemSelected() {
            @Override
            public void onItemSelected(int itemId) {
                bottomSheetMenu.dismiss();
                if (listener != null){
                    listener.onItemSelected(itemId);
                }
            }
        });

        bottomSheetMenu.show(getActivity().getSupportFragmentManager(), getClass().getSimpleName());
    }

    @Nullable
    public View getSearchView() {
        return searchInputView;
    }

    public View getFilterView() {
        return buttonActionView;
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public View getDateView() {
        return dateLabelView;
    }

    public View getItemRecyclerView() {
        int position = layoutManager.findFirstCompletelyVisibleItemPosition();
        return layoutManager.findViewByPosition(position);
    }

    public View getFab() {
        if(menuAdd != null)
            return menuAdd.getActionView();
        return null;
    }

    public interface OnAdListFragmentListener {
        void startShowCase();
    }
}
