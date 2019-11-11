package com.tokopedia.events.view.fragment;

import android.os.Bundle;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.events.R;
import com.tokopedia.events.view.adapter.LocationDateListAdapter;
import com.tokopedia.events.view.contractor.EventBookTicketContract;
import com.tokopedia.events.view.viewmodel.LocationDateModel;

import java.util.List;

/**
 * Created by pranaymohapatra on 23/02/18.
 */
public class LocationDateBottomSheetFragment extends BottomSheetDialogFragment {
    RecyclerView rvLocationDate;
    View closeBts;
    EventBookTicketContract.BookTicketPresenter mPresenter;
    List<LocationDateModel> mData;
    LocationDateListAdapter mAdapter;


    public LocationDateBottomSheetFragment() {
    }

    public void setData(List<LocationDateModel> dataset) {
        mData = dataset;
        if (mAdapter != null)
            mAdapter.setDataSet(mData);
    }

    public void setPresenter(EventBookTicketContract.BookTicketPresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.locationdate_bottomsheet_layout, container, false);
        rvLocationDate = contentView.findViewById(R.id.rv_locationdate);
        closeBts = contentView.findViewById(R.id.iv_close_bts);
        mAdapter = new LocationDateListAdapter(mData, getActivity(), mPresenter);
        rvLocationDate.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        rvLocationDate.setAdapter(mAdapter);
        DividerItemDecoration decoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        decoration.setDrawable(getActivity().getResources().getDrawable(R.drawable.recycler_view_divider));
        rvLocationDate.addItemDecoration(decoration);

        closeBts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return contentView;
    }
}