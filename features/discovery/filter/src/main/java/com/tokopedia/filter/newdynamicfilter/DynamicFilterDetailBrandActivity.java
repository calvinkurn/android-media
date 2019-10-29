package com.tokopedia.filter.newdynamicfilter;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import com.tokopedia.design.list.widget.AlphabeticalSideBar;
import com.tokopedia.filter.R;
import com.tokopedia.filter.common.data.Option;
import com.tokopedia.filter.newdynamicfilter.adapter.DynamicFilterBrandAdapter;
import com.tokopedia.filter.newdynamicfilter.analytics.FilterTrackingData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by henrypriyono on 8/31/17.
 */

public class DynamicFilterDetailBrandActivity extends AbstractDynamicFilterDetailActivity<DynamicFilterBrandAdapter> {

    AlphabeticalSideBar sidebar;

    public static void moveTo(AppCompatActivity activity,
                              String pageTitle,
                              List<Option> optionList,
                              boolean isSearchable,
                              String searchHint,
                              boolean isUsingTracking,
                              FilterTrackingData trackingData) {

        if (activity != null) {
            Intent intent = new Intent(activity, DynamicFilterDetailBrandActivity.class);
            intent.putExtra(EXTRA_PAGE_TITLE, pageTitle);
            intent.putParcelableArrayListExtra(EXTRA_OPTION_LIST, new ArrayList<>(optionList));
            intent.putExtra(EXTRA_IS_SEARCHABLE, isSearchable);
            intent.putExtra(EXTRA_SEARCH_HINT, searchHint);
            intent.putExtra(EXTRA_IS_USING_TRACKING, isUsingTracking);
            intent.putExtra(EXTRA_TRACKING_DATA, trackingData);
            activity.startActivityForResult(intent, REQUEST_CODE);
        }
    }

    @Override
    protected void bindView() {
        super.bindView();
        sidebar = (AlphabeticalSideBar) findViewById(R.id.filter_detail_sidebar);
    }

    @Override
    protected void initRecyclerView() {
        super.initRecyclerView();
        sidebar.setVisibility(View.VISIBLE);
        sidebar.setRecyclerView(recyclerView);
    }

    @Override
    protected DynamicFilterBrandAdapter getAdapter() {
        return new DynamicFilterBrandAdapter(this);
    }

    @Override
    protected void loadFilterItems(List<Option> options) {
        adapter.setOptionList(options);
    }

    @Override
    protected void resetFilter() {
        super.resetFilter();
        adapter.resetAllOptionsInputState();
    }
}
