package com.tokopedia.filter.newdynamicfilter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.tokopedia.filter.common.data.Option;
import com.tokopedia.filter.newdynamicfilter.adapter.DynamicFilterDetailAdapter;
import com.tokopedia.filter.newdynamicfilter.adapter.DynamicFilterDetailColorAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by henrypriyono on 8/16/17.
 */

public class DynamicFilterColorActivity extends DynamicFilterDetailGeneralActivity {

    @Override
    protected DynamicFilterDetailAdapter getAdapter() {
        return new DynamicFilterDetailColorAdapter(this);
    }

    public static void moveTo(AppCompatActivity activity,
                              String pageTitle,
                              List<Option> optionList,
                              boolean isSearchable,
                              String searchHint,
                              boolean isUsingTracking,
                              String trackingPrefix) {

        if (activity != null) {
            Intent intent = new Intent(activity, DynamicFilterColorActivity.class);
            intent.putExtra(EXTRA_PAGE_TITLE, pageTitle);
            intent.putParcelableArrayListExtra(EXTRA_OPTION_LIST, new ArrayList<>(optionList));
            intent.putExtra(EXTRA_IS_SEARCHABLE, isSearchable);
            intent.putExtra(EXTRA_SEARCH_HINT, searchHint);
            intent.putExtra(EXTRA_IS_USING_TRACKING, isUsingTracking);
            intent.putExtra(EXTRA_EVENT_CATEGORY_PREFIX, trackingPrefix);
            activity.startActivityForResult(intent, REQUEST_CODE);
        }
    }
}
