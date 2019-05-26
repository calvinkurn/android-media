package com.tokopedia.checkout.view.feature.addressoptions.bottomsheet;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.checkout.R;
import com.tokopedia.checkout.domain.datamodel.addressoptions.CornerAddressModel;
import com.tokopedia.checkout.view.di.component.CartComponent;
import com.tokopedia.checkout.view.di.component.ShipmentAddressListComponent;
import com.tokopedia.checkout.view.di.module.ShipmentAddressListModule;
import com.tokopedia.checkout.view.di.module.TrackingAnalyticsModule;
import com.tokopedia.design.text.SearchInputView;
import com.tokopedia.shipping_recommendation.domain.shipping.RecipientAddressModel;
import com.tokopedia.checkout.view.di.component.DaggerShipmentAddressListComponent;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by fajarnuha on 09/02/19.
 */
public class CornerListFragment extends BaseDaggerFragment implements CornerAdapter.OnItemCliciListener {

    private static final String ARGUMENTS_BRANCH_LIST = "ARGUMENTS_BRANCH_LIST";

    private List<RecipientAddressModel> mBranchList = new ArrayList<>();
    private BranchChosenListener mListener;
    private final CornerAdapter mAdapter = new CornerAdapter(mBranchList, this);

    private View mEmptyVIew;

    @Inject CornerListPresenter mPresenter;
    private SearchInputView mSearchView;

    public static CornerListFragment newInstance() {
        return new CornerListFragment();
    }

    public static CornerListFragment newInstance(List<CornerAddressModel> modelList) {
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARGUMENTS_BRANCH_LIST, new ArrayList<>(modelList));
        CornerListFragment fragment = new CornerListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public CornerListFragment() {
    }

    @Override
    protected void initInjector() {
        ShipmentAddressListComponent component = DaggerShipmentAddressListComponent.builder()
                .cartComponent(getComponent(CartComponent.class))
                .shipmentAddressListModule(new ShipmentAddressListModule(getActivity()))
                .trackingAnalyticsModule(new TrackingAnalyticsModule())
                .build();
        component.inject(this);
    }

    @Override
    protected String getScreenName() {
        return "Pilih Lokasi Tokopedia Corner";
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BranchChosenListener) {
            mListener = (BranchChosenListener) context;
        }
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
        mSearchView = view.findViewById(R.id.sv_address_search_box);
        mEmptyVIew = view.findViewById(R.id.ll_no_result);
        RecyclerView mRvCorner = view.findViewById(R.id.rv_corner_list);

        mRvCorner.setHasFixedSize(true);
        mRvCorner.setLayoutManager(new LinearLayoutManager(getContext()));
        mRvCorner.setAdapter(mAdapter);
        mPresenter.attachView(this);
        mPresenter.getCorner();
    }

    @Override
    public void onItemClick(RecipientAddressModel address) {
        if (mListener != null) {
            mListener.onCornerChosen(address);
        }
    }

    public void showEmptyView() {
        mEmptyVIew.setVisibility(View.VISIBLE);
    }

    public void setData(List<RecipientAddressModel> data) {
        mAdapter.addAll(data);
    }

    public interface BranchChosenListener {
        void onCornerChosen(RecipientAddressModel corner);
    }

}
