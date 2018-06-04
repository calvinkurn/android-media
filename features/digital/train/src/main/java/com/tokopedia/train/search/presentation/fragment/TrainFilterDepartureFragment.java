package com.tokopedia.train.search.presentation.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

/**
 * Created by nabillasabbaha on 3/23/18.
 */

public class TrainFilterDepartureFragment extends BaseTrainFilterSearchFragment {

    public static Fragment newInstance() {
        TrainFilterDepartureFragment fragment = new TrainFilterDepartureFragment();
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listener.setTitleToolbar("Waktu Berangkat");
        adapter.addList(filterSearchData.getDepartureTimeList(),
                filterSearchData.getSelectedDepartureTimeList());
        adapter.setListener(listObjectFilter -> {
            filterSearchData.setSelectedDepartureTimeList(listObjectFilter);
            listener.onChangeFilterSearchData(filterSearchData);
        });
    }
}
