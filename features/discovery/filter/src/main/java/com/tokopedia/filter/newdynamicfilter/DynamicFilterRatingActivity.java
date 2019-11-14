package com.tokopedia.filter.newdynamicfilter;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import com.tokopedia.filter.common.data.Option;
import com.tokopedia.filter.newdynamicfilter.adapter.DynamicFilterDetailAdapter;
import com.tokopedia.filter.newdynamicfilter.adapter.DynamicFilterDetailRatingAdapter;
import com.tokopedia.filter.newdynamicfilter.analytics.FilterTrackingData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by henrypriyono on 8/24/17.
 */

public class DynamicFilterRatingActivity extends DynamicFilterDetailGeneralActivity {

    @Override
    protected DynamicFilterDetailAdapter getAdapter() {
        return new DynamicFilterDetailRatingAdapter(this);
    }

    public static void moveTo(AppCompatActivity activity,
                              String pageTitle,
                              List<Option> optionList,
                              boolean isSearchable,
                              String searchHint,
                              boolean isUsingTracking,
                              FilterTrackingData trackingData) {

        if (activity != null) {
            Intent intent = new Intent(activity, DynamicFilterRatingActivity.class);
            intent.putExtra(EXTRA_PAGE_TITLE, pageTitle);
            intent.putParcelableArrayListExtra(EXTRA_OPTION_LIST, new ArrayList<>(optionList));
            intent.putExtra(EXTRA_IS_SEARCHABLE, isSearchable);
            intent.putExtra(EXTRA_SEARCH_HINT, searchHint);
            intent.putExtra(EXTRA_IS_USING_TRACKING, isUsingTracking);
            intent.putExtra(EXTRA_TRACKING_DATA, trackingData);
            activity.startActivityForResult(intent, REQUEST_CODE);
        }
    }
}
