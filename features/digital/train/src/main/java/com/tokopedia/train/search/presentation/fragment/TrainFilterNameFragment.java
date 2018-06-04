package com.tokopedia.train.search.presentation.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.tokopedia.train.search.presentation.adapter.TrainFilterAdapter;

import java.util.List;

/**
 * Created by nabillasabbaha on 3/23/18.
 */

public class TrainFilterNameFragment extends BaseTrainFilterSearchFragment {

    public static Fragment newInstance() {
        TrainFilterNameFragment fragment = new TrainFilterNameFragment();
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listener.setTitleToolbar("Kereta");
        adapter.addList(filterSearchData.getTrains(), filterSearchData.getSelectedTrains());
        adapter.setListener(new TrainFilterAdapter.ActionListener() {
            @Override
            public void onCheckChanged(List<String> listObjectFilter) {
                filterSearchData.setSelectedTrains(listObjectFilter);
                listener.onChangeFilterSearchData(filterSearchData);
            }
        });
    }
}
