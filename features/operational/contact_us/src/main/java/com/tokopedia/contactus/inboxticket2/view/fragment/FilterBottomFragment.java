package com.tokopedia.contactus.inboxticket2.view.fragment;

import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.recyclerview.VerticalRecyclerView;
import com.tokopedia.contactus.R;
import com.tokopedia.contactus.R2;
import com.tokopedia.contactus.inboxticket2.view.adapter.InboxFilterAdapter;
import com.tokopedia.contactus.inboxticket2.view.contract.InboxListContract;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by pranaymohapatra on 23/02/18.
 */
public class FilterBottomFragment extends BottomSheetDialogFragment {
    @BindView(R2.id.rv_filter)
    VerticalRecyclerView rvLocationDate;
    InboxFilterAdapter mAdapter;


    public FilterBottomFragment() {
    }

    public void setAdapter(InboxFilterAdapter adapter) {
        mAdapter = adapter;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.layout_filter_fragment, container, false);
        ButterKnife.bind(this, contentView);
        rvLocationDate.setAdapter(mAdapter);
        return contentView;
    }

    @OnClick(R2.id.tv_close_filter)
    void closeBottomSheet() {
        dismiss();
    }
}
