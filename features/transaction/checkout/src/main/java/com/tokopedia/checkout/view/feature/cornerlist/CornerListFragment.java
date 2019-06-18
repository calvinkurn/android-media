package com.tokopedia.checkout.view.feature.cornerlist;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener;
import com.tokopedia.checkout.R;
import com.tokopedia.checkout.domain.datamodel.addressoptions.CornerAddressModel;
import com.tokopedia.checkout.view.di.component.CartComponent;
import com.tokopedia.checkout.view.di.component.ShipmentAddressListComponent;
import com.tokopedia.checkout.view.di.module.ShipmentAddressListModule;
import com.tokopedia.checkout.view.di.module.TrackingAnalyticsModule;
import com.tokopedia.design.text.SearchInputView;
import com.tokopedia.shipping_recommendation.domain.shipping.RecipientAddressModel;
import com.tokopedia.checkout.view.di.component.DaggerShipmentAddressListComponent;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by fajarnuha on 09/02/19.
 */
public class CornerListFragment extends BaseDaggerFragment implements CornerContract.View, CornerAdapter.OnItemClickListener {

    private static final String ARGUMENTS_BRANCH_LIST = "ARGUMENTS_BRANCH_LIST";

    private List<RecipientAddressModel> mBranchList = new ArrayList<>();
    private BranchChosenListener mListener;
    private final CornerAdapter mAdapter = new CornerAdapter(mBranchList, this);

    private View mEmptyView;
    private SearchInputView mSearchView;
    private RecyclerView mRvCorner;
    private EndlessRecyclerViewScrollListener mScrollListener;

    @Inject CornerListPresenter mPresenter;
    private ProgressBar mProgressBar;

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
        mEmptyView = view.findViewById(R.id.ll_no_result);
        mRvCorner = view.findViewById(R.id.rv_corner_list);
        mProgressBar = view.findViewById(R.id.progress_bar);

        mRvCorner.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRvCorner.setLayoutManager(layoutManager);
        mRvCorner.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        mScrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                mPresenter.loadMore(page);
            }
        };
        mRvCorner.addOnScrollListener(mScrollListener);
        mRvCorner.setAdapter(mAdapter);

        mSearchView.setSearchHint(getActivity().getString(R.string.hint_search_corner));
        mSearchView.setListener(new SearchInputView.Listener() {
            @Override
            public void onSearchSubmitted(String text) {
                mPresenter.searchQuery(text);
            }

            @Override
            public void onSearchTextChanged(String text) {

            }
        });

        mPresenter.attachView(this);
        mPresenter.getData();
    }

    @Override
    public void onItemClick(@NonNull RecipientAddressModel address) {
        if (mListener != null) {
            mListener.onCornerChosen(address);
        }
    }

    @Override
    public void showData(List<? extends RecipientAddressModel> data) {
        mAdapter.setAddress(data);
        mScrollListener.resetState();
        mEmptyView.setVisibility(View.GONE);
    }

    @Override
    public void appendData(List<? extends RecipientAddressModel> data) {
        mAdapter.addAddress(data);
        mScrollListener.updateStateAfterGetData();
    }

    @Override
    public void setLoadingState(boolean active) {
        mProgressBar.setVisibility(active ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showError(@NotNull Throwable e) {
        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showEmptyView() {
        mEmptyView.setVisibility(View.VISIBLE);
    }

    public void setCornerListener(BranchChosenListener listener) {
        mListener = listener;
    }

    public interface BranchChosenListener {
        void onCornerChosen(RecipientAddressModel corner);
    }

}
