package com.tokopedia.contactus.inboxticket2.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.recyclerview.VerticalRecyclerView;
import com.tokopedia.contactus.R;
import com.tokopedia.contactus.inboxticket2.view.contract.InboxBaseContract;

import com.tokopedia.abstraction.common.utils.view.MethodChecker;

public class BottomSheetListFragment extends InboxBottomSheetFragment {

    VerticalRecyclerView rvBottomSheet;

    private RecyclerView.Adapter mAdapter;

    public void setAdapter(RecyclerView.Adapter adapter) {
        mAdapter = adapter;
    }

    @Override
    public void setPresenter(InboxBaseContract.InboxBasePresenter presenter) {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        rvBottomSheet = rootView.findViewById(R.id.rv_filter);
        rvBottomSheet.setAdapter(mAdapter);
        return rootView;
    }
}
