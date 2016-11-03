package com.tokopedia.tkpd.dynamicfilter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.google.gson.Gson;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.network.apiservices.ace.apis.BrowseApi;
import com.tokopedia.tkpd.discovery.model.Breadcrumb;
import com.tokopedia.tkpd.dynamicfilter.fragments.DynamicFilterCategoryFragment;
import com.tokopedia.tkpd.dynamicfilter.fragments.DynamicFilterFirstTimeFragment;
import com.tokopedia.tkpd.dynamicfilter.fragments.DynamicFilterListFragment;
import com.tokopedia.tkpd.dynamicfilter.fragments.DynamicFilterOtherFragment;
import com.tokopedia.tkpd.dynamicfilter.model.DynamicFilterModel;
import com.tokopedia.tkpd.dynamicfilter.presenter.DynamicFilterListView;
import com.tokopedia.tkpd.dynamicfilter.presenter.DynamicFilterPresenter;
import com.tokopedia.tkpd.dynamicfilter.presenter.DynamicFilterPresenterImpl;
import com.tokopedia.tkpd.dynamicfilter.presenter.DynamicFilterView;

import org.parceler.Parcels;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by noiz354 on 7/11/16.
 */
public class DynamicFilterActivity extends AppCompatActivity implements DynamicFilterView {
    @Bind(R.id.dynamic_filter_list)
    FrameLayout dynamicFilterList;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.dynamic_filter_detail)
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
    @Bind(R.id.root)
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
            case R.id.action_close:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setFilterList2(List<DynamicFilterModel.Filter> data) {
        Fragment dynamicFilterListFragment = DynamicFilterListFragment.newInstance2(data);
        setFragment(dynamicFilterListFragment, DynamicFilterListView.FRAGMENT_TAG, R.id.dynamic_filter_list);
    }

    private void setFirstTimeDetail() {
        DynamicFilterFirstTimeFragment dynamicFilterFirstTimeFragment = new DynamicFilterFirstTimeFragment();
        setFragment(dynamicFilterFirstTimeFragment, DynamicFilterFirstTimeFragment.FRAGMENT_TAG, R.id.dynamic_filter_detail);
    }

    public void setFragment(Fragment fragment, String TAG, int layoutId) {
        fragmentManager.beginTransaction().replace(layoutId, fragment, TAG).commit();
    }

    @Override
    public void setFragmentForFirstTime3(List<DynamicFilterModel.Filter> data) {
        setFirstTimeDetail();
        setFilterList2(data);
    }

    @Override
    public void setFragmentBasedOnData(DynamicFilterModel.Filter data) {
        if (data.getTitle().toLowerCase().equals("kategori")) {
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
        if(saveFilterSelectionPosition() && saveFilterSelection() && saveFilterText()) {
            Intent intent = new Intent();
            intent.putExtra(EXTRA_RESULT, Parcels.wrap(selectedFilter));
            setResult(RESULT_OK, intent);
            finish();
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

    public static void moveTo(FragmentActivity context, Map<String, String> filters,
                              List<Breadcrumb> productBreadCrumb,
                              List<DynamicFilterModel.Filter> filterList,
                              String currentCategory, String source) {
        if (context != null) {
            Intent intent = new Intent(context, DynamicFilterActivity.class);
            intent.putExtra(DynamicFilterView.EXTRA_RESULT, Parcels.wrap(filters));
            intent.putExtra(DynamicFilterPresenter.BREADCRUMB, Parcels.wrap(productBreadCrumb));
            intent.putExtra(DynamicFilterPresenter.FILTER_CATEGORY, Parcels.wrap(filterList));
            intent.putExtra(DynamicFilterPresenter.FILTER_SOURCE, source);
            intent.putExtra(DynamicFilterPresenter.CURR_CATEGORY, currentCategory);
            context.startActivityForResult(intent, REQUEST_CODE);
            context.overridePendingTransition(R.anim.pull_up, android.R.anim.fade_out);
        }
    }
}
