package com.tokopedia.checkout.view.view.shippingoptions;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.checkout.R;
import com.tokopedia.checkout.domain.datamodel.shipmentrates.CourierItemData;
import com.tokopedia.checkout.domain.datamodel.shipmentrates.ShipmentDetailData;
import com.tokopedia.checkout.view.di.component.CartComponent;
import com.tokopedia.checkout.view.di.component.CartComponentInjector;
import com.tokopedia.checkout.view.view.shippingoptions.di.CourierComponent;
import com.tokopedia.checkout.view.view.shippingoptions.di.CourierModule;
import com.tokopedia.checkout.view.view.shippingoptions.di.DaggerCourierComponent;
import com.tokopedia.transactionanalytics.CheckoutAnalyticsCartShipmentPage;

import javax.inject.Inject;

/**
 * @author Irfan Khoirul on 04/05/18.
 */

public class CourierBottomsheet extends BottomSheetDialog implements CourierContract.View,
        CourierAdapterActionListener, HasComponent<CartComponent> {

    private ImageButton imgBtClose;
    private Toolbar toolbar;
    private TextView tvShipmentInfoTicker;
    private ImageButton imgBtCloseTicker;
    private LinearLayout llShipmentInfoTicker;
    private RecyclerView rvShipmentChoice;
    private LinearLayout llNetworkErrorView;
    private ProgressBar pbLoading;

    private View bottomSheetView;
    private ActionListener actionListener;
    private int cartItemPosition;
    private ShipmentDetailData shipmentDetailData;
    private Activity activity;

    @Inject
    CourierContract.Presenter presenter;

    @Inject
    CourierAdapter courierAdapter;
    @Inject
    CheckoutAnalyticsCartShipmentPage checkoutAnalyticsCartShipmentPage;

    private BottomSheetBehavior behavior;

    public CourierBottomsheet(@NonNull Activity activity, @NonNull ShipmentDetailData shipmentDetailData,
                              @NonNull int cartItemPosition) {
        super(activity);
        this.activity = activity;
        this.cartItemPosition = cartItemPosition;
        this.shipmentDetailData = shipmentDetailData;
        initializeInjector();
        initializeView(activity);
        initializeData(shipmentDetailData);
    }

    private void initializeView(Context context) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        bottomSheetView = layoutInflater.inflate(R.layout.fragment_shipment_choice, null);
        setContentView(bottomSheetView);

        imgBtClose = bottomSheetView.findViewById(R.id.img_bt_close);
        toolbar = bottomSheetView.findViewById(R.id.toolbar);
        tvShipmentInfoTicker = bottomSheetView.findViewById(R.id.tv_shipment_info_ticker);
        imgBtCloseTicker = bottomSheetView.findViewById(R.id.img_bt_close_ticker);
        llShipmentInfoTicker = bottomSheetView.findViewById(R.id.ll_shipment_info_ticker);
        rvShipmentChoice = bottomSheetView.findViewById(R.id.rv_shipment_choice);
        llNetworkErrorView = bottomSheetView.findViewById(R.id.ll_network_error_view);
        pbLoading = bottomSheetView.findViewById(R.id.pb_loading);

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
                checkoutAnalyticsCartShipmentPage.eventClickShipmentClickXOnCourierOption();
            }
        });
    }

    public void updateHeight() {
        behavior = BottomSheetBehavior.from((View) bottomSheetView.getParent());
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    CourierBottomsheet.this.dismiss();
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
        behavior.setPeekHeight(0);
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    private void initializeData(ShipmentDetailData shipmentDetailData) {
        presenter.attachView(this);
        presenter.loadCourier(shipmentDetailData);
        setupRecyclerView();
    }

    private void initializeInjector() {
        CourierComponent component = DaggerCourierComponent.builder()
                .cartComponent(getComponent())
                .courierModule(new CourierModule())
                .build();

        component.inject(this);
    }

    public int getLastCartItemPosition() {
        return cartItemPosition;
    }

    public void setListener(ActionListener listener) {
        this.actionListener = listener;
    }

    private void setupRecyclerView() {
        courierAdapter.setShipmentDataList(presenter.getShipmentDataList());
        courierAdapter.setViewListener(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                getContext(), LinearLayoutManager.VERTICAL, false);
        rvShipmentChoice.setLayoutManager(linearLayoutManager);
        rvShipmentChoice.setAdapter(courierAdapter);
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
                        presenter.loadCourier(shipmentDetailData);
                    }
                });
    }

    @Override
    public void showData() {
        if (!TextUtils.isEmpty(presenter.getShipmentTickerInfo())) {
            tvShipmentInfoTicker.setText(presenter.getShipmentTickerInfo());
            llShipmentInfoTicker.setVisibility(View.VISIBLE);
        } else {
            llShipmentInfoTicker.setVisibility(View.GONE);
        }

        if (courierAdapter != null) {
            courierAdapter.notifyDataSetChanged();
            rvShipmentChoice.setVisibility(View.VISIBLE);
        }
    }

    void onCloseTickerClick() {
        llShipmentInfoTicker.setVisibility(View.GONE);
    }

    void onCloseClick() {
        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    @Override
    public void onBackPressed() {
        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    @Override
    public void onCourierItemClick(CourierItemData courierItemData) {
        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        actionListener.onShipmentItemClick(courierItemData, cartItemPosition);
    }

    @Override
    public void onSelectedCourierItemLoaded(CourierItemData courierItemData) {

    }

    @Override
    public CartComponent getComponent() {
        return CartComponentInjector.newInstance(activity.getApplication()).getCartApiServiceComponent();
    }

    public interface ActionListener {
        void onShipmentItemClick(CourierItemData courierItemData, int cartItemPosition);
    }
}
