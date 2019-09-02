package com.tokopedia.train.search.presentation.fragment;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.View;

import com.tokopedia.tkpdtrain.R;
import com.tokopedia.train.search.presentation.adapter.TrainFilterAdapter;
import com.tokopedia.train.search.presentation.model.FilterSearchData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nabillasabbaha on 3/23/18.
 */

public class FilterTrainNameFragment extends BaseFilterTrainFragment {

    public static Fragment newInstance() {
        FilterTrainNameFragment fragment = new FilterTrainNameFragment();
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listener.setTitleToolbar(getString(R.string.train_filter_train_name));
        adapter.addList(filterSearchData.getTrains(), filterSearchData.getSelectedTrains());
        adapter.setListener(new TrainFilterAdapter.ActionListener() {
            @Override
            public void onCheckChanged(List<String> listObjectFilter) {
                filterSearchData.setSelectedTrains(listObjectFilter);
                listener.onChangeFilterSearchData(filterSearchData);
            }
        });
    }

    @Override
    public void resetFilter() {
        FilterSearchData filterSearchData = listener.getFilterSearchData();
        filterSearchData.setSelectedTrains(new ArrayList<>());
        adapter.removeListSelected();
        listener.onChangeFilterSearchData(filterSearchData);
    }
}
