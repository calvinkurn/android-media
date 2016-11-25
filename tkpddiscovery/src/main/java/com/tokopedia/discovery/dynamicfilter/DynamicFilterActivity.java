package com.tokopedia.discovery.dynamicfilter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.google.gson.Gson;
import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.discovery.dynamicfilter.fragments.DynamicFilterCategoryFragment;
import com.tokopedia.discovery.dynamicfilter.fragments.DynamicFilterListFragment;
import com.tokopedia.discovery.dynamicfilter.fragments.DynamicFilterOtherFragment;
import com.tokopedia.discovery.dynamicfilter.model.DynamicFilterModel;
import com.tokopedia.discovery.dynamicfilter.presenter.DynamicFilterListView;
import com.tokopedia.discovery.dynamicfilter.presenter.DynamicFilterPresenter;
import com.tokopedia.discovery.dynamicfilter.presenter.DynamicFilterPresenterImpl;
import com.tokopedia.discovery.dynamicfilter.presenter.DynamicFilterView;
import com.tokopedia.discovery.model.Breadcrumb;

import org.parceler.Parcels;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by noiz354 on 7/11/16.
 */
public class DynamicFilterActivity extends AppCompatActivity implements DynamicFilterView {

    private static final String FILTER_SELECTED_PRICE_MIN = "pmin";
    private static final String FILTER_SELECTED_PRICE_MAX = "pmax";

    @Bind(R2.id.dynamic_filter_list)
    FrameLayout dynamicFilterList;
    @Bind(R2.id.toolbar)
    Toolbar toolbar;
    @Bind(R2.id.dynamic_filter_detail)
    FrameLayout dynamicFilterDetail;
    private static final String TAG = DynamicFilterActivity.class.getSimpleName();
    DynamicFilterPresenter dynamicFilterPresenter;

    HashMap<String, String> selectedFilter = new HashMap<>();
    HashMap<String, Boolean> selectedPositions = new HashMap<>();
    HashMap<String, String> savedTextInputPositions = new HashMap<>();
    public static final String FILTER_SELECTED_POS_PREF = "filter_selected_pos";
    public static final String FILTER_SELECTED_PREF = "filter_selected";
    public static final String FILTER_TEXT_PREF = "filter_text";
    public static final String ACTION_RESET_FILTER = "ACTION_RESET_FILTER";
    public static final String ACTION_SELECT_FILTER = "ACTION_SELECT_FILTER";
    public static final String EXTRA_FILTER_KEY = "EXTRA_FILTER_KEY";
    public static final String EXTRA_FILTER_VALUE = "EXTRA_FILTER_VALUE";
    @Bind(R2.id.root)
    CoordinatorLayout root;
    private SharedPreferences preferences;
    private FragmentManager fragmentManager;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dynamic_filter_activity);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (savedInstanceState != null) {
            selectedFilter = Parcels.unwrap(savedInstanceState.getParcelable(FILTER_SELECTED_PREF));
            selectedPositions = Parcels.unwrap(savedInstanceState.getParcelable(FILTER_SELECTED_POS_PREF));
            savedTextInputPositions = Parcels.unwrap(savedInstanceState.getParcelable(FILTER_TEXT_PREF));
        } else {
            String savedFilterPos = preferences.getString(FILTER_SELECTED_POS_PREF, new Gson().toJson(selectedPositions));
            selectedPositions = new Gson().fromJson(savedFilterPos, selectedPositions.getClass());
            String savedtext = preferences.getString(FILTER_TEXT_PREF, new Gson().toJson(savedTextInputPositions));
            savedTextInputPositions = new Gson().fromJson(savedtext, savedTextInputPositions.getClass());
            String savedFilter = preferences.getString(FILTER_SELECTED_PREF, new Gson().toJson(selectedFilter));
            selectedFilter = new Gson().fromJson(savedFilter, selectedFilter.getClass());
        }

        fragmentManager = getSupportFragmentManager();
        dynamicFilterPresenter = new DynamicFilterPresenterImpl(this);
        dynamicFilterPresenter.fetchExtras(getIntent());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_sort, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R2.id.action_close:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void setFragmentForFirstTime3(List<DynamicFilterModel.Filter> data) {
        setFragmentBasedOnData(DynamicFilterModel.Filter.createCategory());
        Fragment dynamicFilterListFragment = DynamicFilterListFragment.newInstance2(data);
        setFragment(dynamicFilterListFragment,
                DynamicFilterListView.FRAGMENT_TAG, R.id.dynamic_filter_list);
    }

    @Override
    public void setFragmentBasedOnData(DynamicFilterModel.Filter data) {
        if (data.getTitle().equals(DynamicFilterModel.Filter.TITLE_CATEGORY)) {
            DynamicFilterCategoryFragment categoryFragment =
                    DynamicFilterCategoryFragment.newInstance(
                            dynamicFilterPresenter.getBreadCrumb(), dynamicFilterPresenter.getFilterCategory(),
                            dynamicFilterPresenter.getCurrentCategory());
            setFragment(categoryFragment, DynamicFilterCategoryFragment.FRAGMENT_TAG, R.id.dynamic_filter_detail);
        } else {
            setFragment(DynamicFilterOtherFragment.newInstance(data), DynamicFilterOtherFragment.FRAGMENT_TAG, R.id.dynamic_filter_detail);
        }
    }

    @Override
    public void setFragment(Fragment fragment, String TAG, int layoutId) {
        fragmentManager.beginTransaction().replace(layoutId, fragment, TAG).commit();
    }


    @Override
    public void putSelectedFilter(String key, String value) {
        selectedFilter.put(key, value);
        Intent intent = new Intent(ACTION_SELECT_FILTER);
        intent.putExtra(EXTRA_FILTER_KEY, key);
        intent.putExtra(EXTRA_FILTER_VALUE, true);
        sendBroadcast(intent);
    }

    @Override
    public String getTextInput(String key) {
        return savedTextInputPositions.get(key);
    }

    @Override
    public void saveTextInput(String key, String textInput) {
        Log.d(TAG, "saveTextInput " + key + " " + textInput);
        savedTextInputPositions.put(key, textInput);
    }

    @Override
    public void removeTextInput(String key) {
        savedTextInputPositions.remove(key);
    }

    @Override
    public void saveCheckedPosition(String key, Boolean status) {
        selectedPositions.put(key, status);
        Log.d(TAG, selectedPositions.toString());
    }

    @Override
    public Boolean getCheckedPosition(String key) {
        return selectedPositions.get(key);
    }

    @Override
    public Map<String, Boolean> getSelectedPositions() {
        return selectedPositions;
    }

    @Override
    public Map<String, String> getSelectedFilter() {
        return selectedFilter;
    }

    @Override
    public void removeCheckedPosition(String key) {
        selectedPositions.remove(key);
    }

    @Override
    public void removeSelecfedFilter(String key) {
        selectedFilter.remove(key);
        Intent intent = new Intent(ACTION_SELECT_FILTER);
        intent.putExtra(EXTRA_FILTER_KEY, key);
        intent.putExtra(EXTRA_FILTER_VALUE, false);
        sendBroadcast(intent);
    }

    public void resetSelectedFilter() {
        selectedPositions.clear();
        savedTextInputPositions.clear();
        selectedFilter.clear();
    }

    @Override
    public void finishThis() {
        if (saveFilterSelectionPosition() && saveFilterSelection() && saveFilterText()) {
            if (isFormValid()) {
                Intent intent = new Intent();
                intent.putExtra(EXTRA_FILTERS, Parcels.wrap(selectedFilter));
                setResult(RESULT_OK, intent);
                finish();
            } else {
                showFailedFormValidationMessage();
            }
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(android.R.anim.fade_in, R.anim.push_down);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
//        saveFilterSelectionPosition();
//        saveFilterSelection();
//        saveFilterText();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(FILTER_SELECTED_POS_PREF, Parcels.wrap(selectedPositions));
        outState.putParcelable(FILTER_SELECTED_PREF, Parcels.wrap(selectedFilter));
        outState.putParcelable(FILTER_TEXT_PREF, Parcels.wrap(savedTextInputPositions));
    }

    private boolean saveFilterText() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(FILTER_TEXT_PREF, new Gson().toJson(savedTextInputPositions));
        editor.apply();
        return true;
    }

    private boolean saveFilterSelectionPosition() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(FILTER_SELECTED_POS_PREF, new Gson().toJson(selectedPositions));
        editor.apply();
        return true;
    }

    private boolean saveFilterSelection() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(FILTER_SELECTED_PREF, new Gson().toJson(selectedFilter));
        editor.apply();
        return true;
    }

    public static void moveTo(FragmentActivity fragmentActivity, Map<String, String> filterList,
                              List<Breadcrumb> productBreadCrumbList,
                              List<DynamicFilterModel.Filter> filterCategoryList,
                              String currentCategory, String source) {
        if (fragmentActivity != null) {
            Intent intent = new Intent(fragmentActivity, DynamicFilterActivity.class);
            intent.putExtra(DynamicFilterView.EXTRA_FILTERS, Parcels.wrap(filterList));
            intent.putExtra(DynamicFilterPresenter.EXTRA_PRODUCT_BREADCRUMB_LIST, Parcels.wrap(productBreadCrumbList));
            intent.putExtra(DynamicFilterPresenter.EXTRA_FILTER_CATEGORY_LIST, Parcels.wrap(filterCategoryList));
            intent.putExtra(DynamicFilterPresenter.EXTRA_FILTER_SOURCE, source);
            intent.putExtra(DynamicFilterPresenter.EXTRA_CURRENT_CATEGORY, currentCategory);
            fragmentActivity.startActivityForResult(intent, REQUEST_CODE);
            fragmentActivity.overridePendingTransition(R.anim.pull_up, android.R.anim.fade_out);
        }
    }

    private boolean isFormValid() {
        boolean isFormValid = false;
        if (selectedFilter != null && selectedFilter.size() > 0) {
            double priceMin = -1;
            double priceMax = -1;
            for (Map.Entry<String, String> entry : selectedFilter.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (FILTER_SELECTED_PRICE_MIN.equals(key)) {
                    priceMin = getPriceFromSelectedFilter(priceMin, value);

                } else if (FILTER_SELECTED_PRICE_MAX.equals(key)) {
                    priceMax = getPriceFromSelectedFilter(priceMax, value);
                }
                isFormValid = priceMax != -1 && priceMin != -1 && priceMin < priceMax;
            }
        } else {
            isFormValid = true;
        }
        return isFormValid;
    }

    private double getPriceFromSelectedFilter(double price, String value) {
        if (!value.startsWith(".")) {
            try {
                price = Double.parseDouble(value);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                price = -1;
            }

        }
        return price;
    }


    private void showFailedFormValidationMessage() {
        Snackbar.make(root, getString(R.string.msg_filter_invalid_amount),
                Snackbar.LENGTH_SHORT).show();
    }
}
