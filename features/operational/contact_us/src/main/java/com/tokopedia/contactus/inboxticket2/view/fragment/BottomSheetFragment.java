package com.tokopedia.contactus.inboxticket2.view.fragment;

import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.recyclerview.VerticalRecyclerView;
import com.tokopedia.contactus.R;
import com.tokopedia.contactus.R2;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by pranaymohapatra on 23/02/18.
 */
public class BottomSheetFragment extends BottomSheetDialogFragment {
    @BindView(R2.id.rv_filter)
    VerticalRecyclerView rvBottomSheet;
    @BindView(R2.id.tv_bottom_sheet_title)
    TextView title;

    RecyclerView.Adapter mAdapter;


    public BottomSheetFragment() {
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        mAdapter = adapter;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.layout_bottom_sheet_fragment, container, false);
        ButterKnife.bind(this, contentView);
        rvBottomSheet.setAdapter(mAdapter);
        title.setText(R.string.select_bad_reason);
        return contentView;
    }

    @OnClick(R2.id.tv_bottom_sheet_title)
    void closeBottomSheet() {
        dismiss();
    }
}
