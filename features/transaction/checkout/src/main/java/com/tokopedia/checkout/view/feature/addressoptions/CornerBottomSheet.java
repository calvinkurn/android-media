package com.tokopedia.checkout.view.feature.addressoptions;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.checkout.R;
import com.tokopedia.checkout.domain.datamodel.addressoptions.CornerAddressModel;
import com.tokopedia.checkout.view.feature.addressoptions.adapter.CornerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fajarnuha on 09/02/19.
 */
public class CornerBottomSheet extends BottomSheetDialogFragment {

    private static final String ARGUMENTS_BRANCH_LIST = "ARGUMENTS_BRANCH_LIST";

    private RecyclerView mRvCorner;
    private CornerAdapter mAdapter;
    private TextView mTvCornerName;
    private List<CornerAddressModel> mBranchList;

    public static CornerBottomSheet newInstance(List<CornerAddressModel> modelList) {

        Bundle args = new Bundle();
        args.putParcelableArrayList(ARGUMENTS_BRANCH_LIST, new ArrayList<>(modelList));
        CornerBottomSheet fragment = new CornerBottomSheet();
        fragment.setArguments(args);
        return fragment;
    }

    public CornerBottomSheet() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mBranchList = getArguments().getParcelableArrayList(ARGUMENTS_BRANCH_LIST);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bottom_sheet_corner, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mTvCornerName = view.findViewById(R.id.text_view_corner_name);
        mRvCorner = view.findViewById(R.id.rv_corner_list);
        mAdapter = new CornerAdapter(mBranchList);

        mRvCorner.setHasFixedSize(true);
        mRvCorner.setLayoutManager(new LinearLayoutManager(getContext()));
        mRvCorner.setAdapter(mAdapter);
        mTvCornerName.setText(mBranchList.get(0).getCornerName());
    }
}
