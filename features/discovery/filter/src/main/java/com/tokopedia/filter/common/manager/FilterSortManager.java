package com.tokopedia.filter.common.manager;

import android.app.Activity;
import android.content.Intent;
import androidx.fragment.app.Fragment;

import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery;
import com.tokopedia.filter.R;
import com.tokopedia.filter.common.data.Option;
import com.tokopedia.filter.common.data.Sort;
import com.tokopedia.filter.newdynamicfilter.analytics.FilterTracking;
import com.tokopedia.filter.newdynamicfilter.analytics.FilterTrackingData;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tokopedia.filter.newdynamicfilter.RevampedDynamicFilterActivity.EXTRA_CALLER_SCREEN_NAME;
import static com.tokopedia.filter.newdynamicfilter.RevampedDynamicFilterActivity.EXTRA_QUERY_PARAMETERS;
import static com.tokopedia.filter.newdynamicfilter.RevampedDynamicFilterActivity.EXTRA_SELECTED_FILTERS;
import static com.tokopedia.filter.newdynamicfilter.RevampedDynamicFilterActivity.EXTRA_SELECTED_OPTIONS;
import static com.tokopedia.filter.newdynamicfilter.SortProductActivity.EXTRA_AUTO_APPLY_FILTER;
import static com.tokopedia.filter.newdynamicfilter.SortProductActivity.EXTRA_SORT_DATA;
import static com.tokopedia.filter.newdynamicfilter.SortProductActivity.EXTRA_SELECTED_SORT_NAME;
import static com.tokopedia.filter.newdynamicfilter.SortProductActivity.EXTRA_SELECTED_SORT;

public class FilterSortManager {

    private static final int SORT_REQUEST_CODE = 1233;
    private static final int FILTER_REQUEST_CODE = 4320;

    public static void openFilterPage(FilterTrackingData trackingData, Fragment fragment, String callerScreenName, HashMap<String, String> queryParams) {
        if (fragment == null) return;

        FilterTracking.eventOpenFilterPage(trackingData);

        Intent intent = RouteManager.getIntent(fragment.getContext(), ApplinkConstInternalDiscovery.FILTER);
        intent.putExtra(EXTRA_CALLER_SCREEN_NAME, callerScreenName);
        intent.putExtra(EXTRA_QUERY_PARAMETERS, queryParams);

        fragment.startActivityForResult(intent, FILTER_REQUEST_CODE);

        if (fragment.getActivity() != null) {
            fragment.getActivity().overridePendingTransition(R.anim.pull_up, android.R.anim.fade_out);
        }
    }

    public static boolean openSortActivity(Fragment fragment, ArrayList<Sort> sort, HashMap<String, String> selectedSort) {
        if (!isSortDataAvailable(sort) || fragment == null) {
            return false;
        }
        Intent intent = RouteManager.getIntent(fragment.getContext(), ApplinkConstInternalDiscovery.SORT);
        intent.putParcelableArrayListExtra(EXTRA_SORT_DATA, sort);
        if (selectedSort != null) {
            intent.putExtra(EXTRA_SELECTED_SORT, selectedSort);
        }

        fragment.startActivityForResult(intent, SORT_REQUEST_CODE);

        if(fragment.getActivity() != null) {
            fragment.getActivity().overridePendingTransition(R.anim.pull_up, android.R.anim.fade_out);
        }
        return true;
    }

    private static boolean isSortDataAvailable(ArrayList<Sort> sort) {
        return sort != null && !sort.isEmpty();
    }

    public static void handleOnActivityResult(int requestCode, int resultCode, Intent data, Callback callback) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SORT_REQUEST_CODE) {
                Map<String, String> selectedSort = getMapFromIntent(data, EXTRA_SELECTED_SORT);
                String selectedSortName = data.getStringExtra(EXTRA_SELECTED_SORT_NAME);
                String autoApplyFilterParams = data.getStringExtra(EXTRA_AUTO_APPLY_FILTER);
                callback.onSortResult(selectedSort, selectedSortName, autoApplyFilterParams);

            } else if (requestCode == FILTER_REQUEST_CODE) {
                Map<String, String> queryParams = getMapFromIntent(data, EXTRA_QUERY_PARAMETERS);
                Map<String, String> selectedFilters = getMapFromIntent(data, EXTRA_SELECTED_FILTERS);
                List<Option> selectedOptions = data.getParcelableArrayListExtra(EXTRA_SELECTED_OPTIONS);
                callback.onFilterResult(queryParams, selectedFilters, selectedOptions);

            }
        }
    }

    private static Map<String, String> getMapFromIntent(Intent data, String extraName) {
        Serializable serializableExtra = data.getSerializableExtra(extraName);

        if(serializableExtra == null) return new HashMap<>();

        Map<?, ?> filterParameterMapIntent = (Map<?, ?>)data.getSerializableExtra(extraName);
        Map<String, String> filterParameter = new HashMap<>(filterParameterMapIntent.size());

        for(Map.Entry<?, ?> entry: filterParameterMapIntent.entrySet()) {
            filterParameter.put(entry.getKey().toString(), entry.getValue().toString());
        }

        return filterParameter;
    }

    public interface Callback {
        void onFilterResult(Map<String, String> queryParams, Map<String, String> selectedFilters, List<Option> selectedOptions);
        void onSortResult(Map<String, String> selectedSort, String selectedSortName, String autoApplyFilter);
    }
}
