package com.tokopedia.checkout.view.view.shippingoptions;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.checkout.R;
import com.tokopedia.checkout.domain.datamodel.shipmentrates.ShipmentDetailData;
import com.tokopedia.checkout.domain.datamodel.shipmentrates.ShipmentItemData;
import com.tokopedia.checkout.view.adapter.ShipmentChoiceAdapter;
import com.tokopedia.checkout.view.di.component.CartComponent;
import com.tokopedia.checkout.view.di.component.DaggerShipmentChoiceComponent;
import com.tokopedia.checkout.view.di.component.ShipmentChoiceComponent;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Irfan Khoirul on 30/01/18.
 */

public class ShipmentChoiceBottomSheet extends BottomSheetDialog
        implements IShipmentChoiceView, ShipmentChoiceAdapter.ViewListener {

    private Toolbar toolbar;
    private RecyclerView rvShipmentChoice;
    private LinearLayout llNetworkErrorView;
    private ProgressBar pbLoading;
    private LinearLayout llShipmentInfoTicker;
    private ImageView imgBtCloseTicker;
    private TextView tvShipmentInfoTicker;
    private ImageButton imgBtClose;

    private ActionListener listener;
    private View bottomSheetView;

    @Inject
    ShipmentChoiceAdapter shipmentChoiceAdapter;

    @Inject
    IShipmentChoicePresenter presenter;

    public ShipmentChoiceBottomSheet(@NonNull Context context,
                                     @NonNull ShipmentDetailData shipmentDetailData,
                                     @Nullable ShipmentItemData selectedShipment) {
        super(context);
        initializeView(context);
        initializeData(shipmentDetailData, selectedShipment);
    }

    private void initializeView(Context context) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        bottomSheetView = layoutInflater.inflate(R.layout.fragment_shipment_choice, null);
        setContentView(bottomSheetView);
        toolbar = bottomSheetView.findViewById(R.id.toolbar);
        rvShipmentChoice = bottomSheetView.findViewById(R.id.rv_shipment_choice);
        llNetworkErrorView = bottomSheetView.findViewById(R.id.ll_network_error_view);
        pbLoading = bottomSheetView.findViewById(R.id.pb_loading);
        llShipmentInfoTicker = bottomSheetView.findViewById(R.id.ll_shipment_info_ticker);
        imgBtCloseTicker = bottomSheetView.findViewById(R.id.img_bt_close_ticker);
        tvShipmentInfoTicker = bottomSheetView.findViewById(R.id.tv_shipment_info_ticker);
        imgBtClose = bottomSheetView.findViewById(R.id.img_bt_close);

        imgBtCloseTicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCloseTickerClick();
            }
        });

        imgBtClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCloseClick();
            }
        });

        initializeInjector();
    }

    private void initializeData(ShipmentDetailData shipmentDetailData, ShipmentItemData selectedShipment) {
        presenter.attachView(this);
        presenter.loadShipmentChoice(shipmentDetailData, selectedShipment);
        setupRecyclerView();
    }

    private void initializeInjector() {
        ShipmentChoiceComponent shipmentChoiceComponent = DaggerShipmentChoiceComponent.builder()
                .build();
        shipmentChoiceComponent.inject(this);
    }

    public void updateData(List<ShipmentItemData> shipmentItemDataList) {
        presenter.setShipmentChoices(shipmentItemDataList);
        shipmentChoiceAdapter.notifyDataSetChanged();
    }

    public void updateHeight() {
        BottomSheetBehavior behavior = BottomSheetBehavior.from((View) bottomSheetView.getParent());
        RelativeLayout.LayoutParams layoutParams =
                (RelativeLayout.LayoutParams) toolbar.getLayoutParams();
        int bottomSheetHeight = rvShipmentChoice.computeVerticalScrollRange() +
                toolbar.getHeight() + layoutParams.bottomMargin;
        behavior.setPeekHeight(bottomSheetHeight);
    }

    public void setListener(ActionListener listener) {
        this.listener = listener;
    }

    private void setupRecyclerView() {
        shipmentChoiceAdapter.setShipments(presenter.getShipmentChoices());
        shipmentChoiceAdapter.setViewListener(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                getContext(), LinearLayoutManager.VERTICAL, false);
        rvShipmentChoice.setLayoutManager(linearLayoutManager);
        rvShipmentChoice.setAdapter(shipmentChoiceAdapter);
    }

    @Override
    public void showLoading() {
        pbLoading.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        pbLoading.setVisibility(View.GONE);
    }

    @Override
    public void showNoConnection(String message) {
        rvShipmentChoice.setVisibility(View.GONE);
        NetworkErrorHelper.showEmptyState(getContext(), llNetworkErrorView, message,
                new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        presenter.loadShipmentChoice(null, null);
                    }
                });
    }

    @Override
    public void showData() {
        if (!TextUtils.isEmpty(presenter.getShipmentDetailData().getShipmentTickerInfo())) {
            tvShipmentInfoTicker.setText(presenter.getShipmentDetailData().getShipmentTickerInfo());
            llShipmentInfoTicker.setVisibility(View.VISIBLE);
        } else {
            llShipmentInfoTicker.setVisibility(View.GONE);
        }

        if (shipmentChoiceAdapter != null) {
            shipmentChoiceAdapter.notifyDataSetChanged();
            rvShipmentChoice.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onShipmentItemClick(ShipmentItemData shipmentItemData) {
        listener.onShipmentItemClick(shipmentItemData);
        this.dismiss();
    }

    void onCloseTickerClick() {
        llShipmentInfoTicker.setVisibility(View.GONE);
    }

    void onCloseClick() {
        ShipmentChoiceBottomSheet.this.dismiss();
    }

    public interface ActionListener {
        void onShipmentItemClick(ShipmentItemData shipmentItemData);

        CartComponent getCartComponent();
    }
}