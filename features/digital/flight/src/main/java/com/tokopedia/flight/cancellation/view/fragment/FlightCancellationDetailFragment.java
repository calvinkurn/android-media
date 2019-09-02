package com.tokopedia.flight.cancellation.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.base.view.recyclerview.VerticalRecyclerView;
import com.tokopedia.flight.R;
import com.tokopedia.flight.booking.view.viewmodel.SimpleViewModel;
import com.tokopedia.flight.cancellation.di.FlightCancellationComponent;
import com.tokopedia.flight.cancellation.view.adapter.FlightCancellationDetailPassengerAdapter;
import com.tokopedia.flight.cancellation.view.adapter.FlightCancellationDetailPassengerAdapterTypeFactory;
import com.tokopedia.flight.cancellation.view.adapter.FlightCancellationRefundBottomAdapter;
import com.tokopedia.flight.cancellation.view.adapter.FlightCancellationRefundDetailMiddleAdapter;
import com.tokopedia.flight.cancellation.view.contract.FlightCancellationDetailContract;
import com.tokopedia.flight.cancellation.view.presenter.FlightCancellationDetailPresenter;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationListViewModel;
import com.tokopedia.flight.common.util.FlightDateUtil;
import com.tokopedia.flight.detail.presenter.ExpandableOnClickListener;
import com.tokopedia.flight.detail.view.adapter.FlightDetailOrderAdapter;
import com.tokopedia.flight.detail.view.adapter.FlightDetailOrderTypeFactory;
import com.tokopedia.flight.orderlist.data.cloud.entity.KeyValueEntity;
import com.tokopedia.flight.orderlist.data.cloud.entity.RefundDetailEntity;
import com.tokopedia.unifycomponents.ticker.Ticker;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @author by furqan on 03/05/18.
 */

public class FlightCancellationDetailFragment extends BaseDaggerFragment
        implements FlightCancellationDetailContract.View, ExpandableOnClickListener {

    public static final String EXTRA_CANCELLATION_DETAIL_PASS_DATA = "EXTRA_CANCELLATION_DETAIL_PASS_DATA";
    private static final float JOURNEY_TITLE_FONT_SIZE = 16;

    @Inject
    FlightCancellationDetailPresenter presenter;

    private VerticalRecyclerView rvFlights;
    private VerticalRecyclerView rvPassengers;
    private View layoutExpendablePassenger;
    private AppCompatImageView imageExpendablePassenger;
    private AppCompatTextView txtCancellationStatus;
    private TextView txtCancellationDate;
    private Ticker tickerRefundInfo;
    private RecyclerView rvBottomTopInfo, rvBottomMiddleInfo, rvBottomBottomInfo;

    private FlightDetailOrderAdapter flightDetailOrderAdapter;
    private FlightCancellationDetailPassengerAdapter flightCancellationDetailPassengerAdapter;

    private FlightCancellationListViewModel flightCancellationListViewModel;
    private boolean isPassengerInfoShowed = true;

    public static FlightCancellationDetailFragment createInstance(FlightCancellationListViewModel flightCancellationListViewModel) {
        FlightCancellationDetailFragment fragment = new FlightCancellationDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_CANCELLATION_DETAIL_PASS_DATA, flightCancellationListViewModel);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_flight_cancellation_detail, container, false);

        layoutExpendablePassenger = view.findViewById(R.id.layout_expendable_passenger);
        imageExpendablePassenger = view.findViewById(R.id.image_expendable_passenger);
        rvFlights = view.findViewById(R.id.recycler_view_flight);
        rvPassengers = view.findViewById(R.id.recycler_view_data_passenger);
        txtCancellationStatus = view.findViewById(R.id.cancellation_status);
        txtCancellationDate = view.findViewById(R.id.cancellation_date);
        tickerRefundInfo = view.findViewById(R.id.ticker_refund_info);
        rvBottomTopInfo = view.findViewById(R.id.rv_bottom_top_info);
        rvBottomMiddleInfo = view.findViewById(R.id.rv_bottom_middle_info);
        rvBottomBottomInfo = view.findViewById(R.id.rv_bottom_bottom_info);

        FlightDetailOrderTypeFactory flightDetailOrderTypeFactory = new FlightDetailOrderTypeFactory(this, JOURNEY_TITLE_FONT_SIZE);
        flightDetailOrderAdapter = new FlightDetailOrderAdapter(flightDetailOrderTypeFactory);
        rvFlights.setAdapter(flightDetailOrderAdapter);

        FlightCancellationDetailPassengerAdapterTypeFactory flightCancellationDetailPassengerAdapterTypeFactory = new FlightCancellationDetailPassengerAdapterTypeFactory();
        flightCancellationDetailPassengerAdapter = new FlightCancellationDetailPassengerAdapter(flightCancellationDetailPassengerAdapterTypeFactory);
        rvPassengers.setAdapter(flightCancellationDetailPassengerAdapter);

        layoutExpendablePassenger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageExpendablePassenger.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.rotate_reverse));
                togglePassengerInfo();
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        flightCancellationListViewModel = getArguments().getParcelable(EXTRA_CANCELLATION_DETAIL_PASS_DATA);

        presenter.attachView(this);
        presenter.onViewCreated();
        renderView();
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {
        getComponent(FlightCancellationComponent.class).inject(this);
    }


    @Override
    public void onCloseExpand(int position) {

    }

    private void renderView() {
        txtCancellationStatus.requestFocus();
        txtCancellationStatus.setText(flightCancellationListViewModel.getCancellations().getStatusStr());

        flightDetailOrderAdapter.addElement(flightCancellationListViewModel
                .getCancellations().getJourneys());
        flightDetailOrderAdapter.notifyDataSetChanged();

        flightCancellationDetailPassengerAdapter.addElement(flightCancellationListViewModel
                .getCancellations().getPassengers());
        flightCancellationDetailPassengerAdapter.notifyDataSetChanged();

        if (flightCancellationListViewModel.getCancellations().getRefundInfo().length() > 0) {
            tickerRefundInfo.setTextDescription(flightCancellationListViewModel.getCancellations().getRefundInfo());
            tickerRefundInfo.setVisibility(View.VISIBLE);
        }

        txtCancellationDate.setText(FlightDateUtil.formatDate(FlightDateUtil.YYYY_MM_DD_T_HH_MM_SS_Z,
                FlightDateUtil.DEFAULT_VIEW_FORMAT,
                flightCancellationListViewModel.getCancellations().getCreateTime()));

        renderBottomInfo(flightCancellationListViewModel.getCancellations().getRefundDetail());
    }

    @Override
    public FlightCancellationListViewModel getFlightCancellationList() {
        return flightCancellationListViewModel;
    }

    private void renderBottomInfo(RefundDetailEntity refundDetail) {
        // top info
        FlightCancellationRefundBottomAdapter refundTopAdapter = new FlightCancellationRefundBottomAdapter(FlightCancellationRefundBottomAdapter.TYPE_NORMAL);
        refundTopAdapter.addData(generateSimpleViewModel(refundDetail.getTopInfo()));
        rvBottomTopInfo.setLayoutManager(new LinearLayoutManager(getContext()));
        rvBottomTopInfo.setAdapter(refundTopAdapter);

        // middle info
        FlightCancellationRefundDetailMiddleAdapter refundMiddleAdapter = new FlightCancellationRefundDetailMiddleAdapter(refundDetail.getMiddleInfo());
        rvBottomMiddleInfo.setLayoutManager(new LinearLayoutManager(getContext()));
        rvBottomMiddleInfo.setAdapter(refundMiddleAdapter);

        // bottom info
        FlightCancellationRefundBottomAdapter refundBottomAdapter = new FlightCancellationRefundBottomAdapter(FlightCancellationRefundBottomAdapter.TYPE_RED);
        refundBottomAdapter.addData(generateSimpleViewModel(refundDetail.getBottomInfo()));
        rvBottomBottomInfo.setLayoutManager(new LinearLayoutManager(getContext()));
        rvBottomBottomInfo.setAdapter(refundBottomAdapter);
    }

    private List<SimpleViewModel> generateSimpleViewModel(List<KeyValueEntity> items) {
        List<SimpleViewModel> datas = new ArrayList<>();

        for (KeyValueEntity item : items) {
            datas.add(new SimpleViewModel(item.getKey(), item.getValue()));
        }

        return datas;
    }

    private void togglePassengerInfo() {
        if (isPassengerInfoShowed) {
            hidePassengerInfo();
        } else {
            showPassengerInfo();
        }
    }

    private void hidePassengerInfo() {
        isPassengerInfoShowed = false;
        rvPassengers.setVisibility(View.GONE);
        imageExpendablePassenger.setRotation(180);
    }

    private void showPassengerInfo() {
        isPassengerInfoShowed = true;
        rvPassengers.setVisibility(View.VISIBLE);
        imageExpendablePassenger.setRotation(0);
    }
}
