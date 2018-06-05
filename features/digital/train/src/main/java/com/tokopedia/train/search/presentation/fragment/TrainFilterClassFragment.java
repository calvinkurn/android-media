package com.tokopedia.train.search.presentation.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.tokopedia.train.search.presentation.model.FilterSearchData;

import java.util.ArrayList;

import javax.annotation.Nullable;

/**
 * Created by nabillasabbaha on 04/06/18.
 */
public class TrainFilterClassFragment extends BaseTrainFilterSearchFragment {

    public static Fragment newInstance() {
        TrainFilterClassFragment fragment = new TrainFilterClassFragment();
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listener.setTitleToolbar("Kelas");
        adapter.addList(filterSearchData.getTrainClass(),
                filterSearchData.getSelectedTrainClass());
        adapter.setListener(listObjectFilter -> {
            filterSearchData.setSelectedTrainClass(listObjectFilter);
            listener.onChangeFilterSearchData(filterSearchData);
        });
    }

    @Override
    public void resetFilter() {
        FilterSearchData filterSearchData = listener.getFilterSearchData();
        filterSearchData.setSelectedTrainClass(new ArrayList<>());
        adapter.removeListSelected();
        listener.onChangeFilterSearchData(filterSearchData);
    }
}
