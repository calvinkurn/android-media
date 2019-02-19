package com.tokopedia.common.travel.presentation.dialog;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tokopedia.common.travel.R;
import com.tokopedia.common.travel.presentation.adapter.TravelSearchSortAdapter;
import com.tokopedia.design.component.BottomSheets;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nabillasabbaha on 21/08/18.
 */
public class TravelSearchSortBottomSheet extends BottomSheets {

    private RecyclerView sortRecyclerView;
    private ActionListener listener;
    private int idSortSelected;

    public void setIdSortSelected(int idSortSelected) {
        this.idSortSelected = idSortSelected;
    }

    public void setListener(ActionListener listener) {
        this.listener = listener;
    }

    @Override
    protected BottomSheetsState state() {
        return BottomSheetsState.FULL;
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.bottom_sheet_travel_sort_search;
    }

    @Override
    public void initView(View view) {
        sortRecyclerView = view.findViewById(R.id.rv_train_sort_search);
        String[] sortLabelList = getResources().getStringArray(R.array.travel_sort_label);
        List<SearchSortModel> searchSortModels = new ArrayList<>();
        for (int i = 0; i < sortLabelList.length; i++) {
            int idSort = i+1;
            SearchSortModel sortModel = new SearchSortModel();
            sortModel.setIdSort(idSort);
            sortModel.setSortName(sortLabelList[i]);
            if (idSort == idSortSelected) {
                sortModel.setSelected(true);
            } else {
                sortModel.setSelected(false);
            }
            searchSortModels.add(sortModel);
        }

        TravelSearchSortAdapter adapter = new TravelSearchSortAdapter(searchSortModels);
        adapter.setListener(searchSortModel -> {
            listener.onClickSortLabel(searchSortModel.getIdSort());
            dismiss();
        });
        sortRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        sortRecyclerView.setAdapter(adapter);
    }

    @Override
    protected String title() {
        return getString(R.string.travel_title_sort_search);
    }

    public interface ActionListener {
        void onClickSortLabel(int idSort);
    }
}
