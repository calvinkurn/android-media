package com.tokopedia.train.search.presentation.fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.View;

import com.tokopedia.tkpdtrain.R;
import com.tokopedia.train.search.presentation.model.FilterSearchData;

import java.util.ArrayList;

import javax.annotation.Nullable;

/**
 * Created by nabillasabbaha on 04/06/18.
 */
public class FilterTrainClassFragment extends BaseFilterTrainFragment {

    public static Fragment newInstance() {
        FilterTrainClassFragment fragment = new FilterTrainClassFragment();
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listener.setTitleToolbar(getString(R.string.train_filter_class_name));
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
