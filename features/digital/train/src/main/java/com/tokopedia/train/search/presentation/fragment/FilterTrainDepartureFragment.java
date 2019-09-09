package com.tokopedia.train.search.presentation.fragment;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.View;

import com.tokopedia.tkpdtrain.R;
import com.tokopedia.train.search.presentation.model.FilterSearchData;

import java.util.ArrayList;

/**
 * Created by nabillasabbaha on 3/23/18.
 */

public class FilterTrainDepartureFragment extends BaseFilterTrainFragment {

    public static Fragment newInstance() {
        FilterTrainDepartureFragment fragment = new FilterTrainDepartureFragment();
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listener.setTitleToolbar(getString(R.string.train_filter_time_departure));
        adapter.addList(filterSearchData.getDepartureTimeList(),
                filterSearchData.getSelectedDepartureTimeList());
        adapter.setListener(listObjectFilter -> {
            filterSearchData.setSelectedDepartureTimeList(listObjectFilter);
            listener.onChangeFilterSearchData(filterSearchData);
        });
    }

    @Override
    public void resetFilter() {
        FilterSearchData filterSearchData = listener.getFilterSearchData();
        filterSearchData.setSelectedDepartureTimeList(new ArrayList<>());
        adapter.removeListSelected();
        listener.onChangeFilterSearchData(filterSearchData);
    }
}
