package com.tokopedia.logisticcart.shipping.features.shippingcourier.view;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.design.component.BottomSheets;
import com.tokopedia.logisticcart.R;
import com.tokopedia.logisticcart.shipping.features.shippingcourier.di.DaggerShippingCourierComponent;
import com.tokopedia.logisticcart.shipping.features.shippingcourier.di.ShippingCourierComponent;
import com.tokopedia.logisticcart.shipping.features.shippingcourier.di.ShippingCourierModule;
import com.tokopedia.logisticcart.shipping.model.CourierItemData;
import com.tokopedia.logisticdata.data.entity.address.RecipientAddressModel;
import com.tokopedia.logisticcart.shipping.model.ShipmentCartItemModel;
import com.tokopedia.logisticcart.shipping.model.ShippingCourierUiModel;
import com.tokopedia.logisticcart.shipping.model.ShopShipment;
import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.ErrorProductData;
import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.ProductData;
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.remoteconfig.RemoteConfig;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Irfan Khoirul on 06/08/18.
 */

public class ShippingCourierBottomsheet extends BottomSheets
        implements ShippingCourierContract.View, ShippingCourierAdapterListener {

    public static final String ARGUMENT_SHIPPING_COURIER_VIEW_MODEL_LIST = "ARGUMENT_SHIPPING_COURIER_VIEW_MODEL_LIST";
    public static final String ARGUMENT_CART_POSITION = "ARGUMENT_CART_POSITION";
    public static final String ARGUMENT_RECIPIENT_ADDRESS_MODEL = "ARGUMENT_RECIPIENT_ADDRESS_MODEL";

    private LinearLayout llContent;
    private RecyclerView rvCourier;
    private LinearLayout llNetworkErrorView;
    private ProgressBar pbLoading;

    private ShippingCourierBottomsheetListener shippingCourierBottomsheetListener;
    private List<ShippingCourierUiModel> mCourierModelList = new ArrayList<>();
    private RecipientAddressModel mRecipientAddress;

    @Inject
    ShippingCourierAdapter shippingCourierAdapter;
    @Inject
    ShippingCourierConverter courierConverter;

    public static ShippingCourierBottomsheet newInstance(List<ShippingCourierUiModel> shippingCourierUiModels,
                                                         RecipientAddressModel recipientAddressModel,
                                                         int cartPosition) {
        ShippingCourierBottomsheet shippingCourierBottomsheet = new ShippingCourierBottomsheet();
        Bundle bundle = new Bundle();
        if (shippingCourierUiModels != null) {
            bundle.putParcelableArrayList(ARGUMENT_SHIPPING_COURIER_VIEW_MODEL_LIST, new ArrayList<>(shippingCourierUiModels));
        }
        bundle.putParcelable(ARGUMENT_RECIPIENT_ADDRESS_MODEL, recipientAddressModel);
        bundle.putInt(ARGUMENT_CART_POSITION, cartPosition);
        shippingCourierBottomsheet.setArguments(bundle);

        return shippingCourierBottomsheet;
    }

    public static ShippingCourierBottomsheet newInstance() {
        return new ShippingCourierBottomsheet();
    }

    @Override
    protected BottomSheetsState state() {
        return BottomSheetsState.FLEXIBLE;
    }

    public void setShippingCourierBottomsheetListener(ShippingCourierBottomsheetListener shippingCourierBottomsheetListener) {
        this.shippingCourierBottomsheetListener = shippingCourierBottomsheetListener;
    }

    public void updateArguments(List<ShippingCourierUiModel> shippingCourierUiModels) {
        Bundle bundle = new Bundle();
        if (shippingCourierUiModels != null) {
            bundle.putParcelableArrayList(ARGUMENT_SHIPPING_COURIER_VIEW_MODEL_LIST, new ArrayList<>(shippingCourierUiModels));
        }
        setArguments(bundle);
    }

    public void setShippingCourierViewModels(List<ShippingCourierUiModel> shippingCourierUiModels,
                                             int cartPosition, ShipmentCartItemModel shipmentCartItemModel,
                                             List<ShopShipment> shopShipmentList) {
        hideLoading();
        if (shippingCourierUiModels != null && shippingCourierUiModels.size() > 0) {
            mCourierModelList = shippingCourierUiModels;
            setupRecyclerView(cartPosition);
            updateHeight();
        } else {
            showErrorPage("Terjadi kesalahan", shipmentCartItemModel, cartPosition, shopShipmentList);
            updateHeight();
        }
    }

    private void initializeInjector() {
        ShippingCourierComponent component = DaggerShippingCourierComponent.builder()
                .shippingCourierModule(new ShippingCourierModule())
                .build();

        component.inject(this);
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.fragment_shipment_courier_choice;
    }

    @Override
    public int getBaseLayoutResourceId() {
        return R.layout.widget_bottomsheet_shipping;
    }

    @Override
    public void setupDialog(android.app.Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        if (dialog != null) {
            dialog.findViewById(R.id.design_bottom_sheet).setBackgroundResource(android.R.color.transparent);
        }
    }

    @Override
    protected String title() {
        return getString(R.string.title_shipment_courier_bottomsheet);
    }

    @Override
    public void initView(View view) {
        llContent = view.findViewById(R.id.ll_content);
        rvCourier = view.findViewById(R.id.rv_courier);
        llNetworkErrorView = view.findViewById(R.id.ll_network_error_view);
        pbLoading = view.findViewById(R.id.pb_loading);

        initializeInjector();
        if (getArguments() != null) {
            mRecipientAddress = getArguments().getParcelable(ARGUMENT_RECIPIENT_ADDRESS_MODEL);
            int cartPosition = getArguments().getInt(ARGUMENT_CART_POSITION);
            List<ShippingCourierUiModel> shippingCourierUiModels =
                    getArguments().getParcelableArrayList(ARGUMENT_SHIPPING_COURIER_VIEW_MODEL_LIST);
            if (shippingCourierUiModels != null) {
                mCourierModelList = shippingCourierUiModels;
                setupRecyclerView(cartPosition);
            } else {
                showLoading();
            }
        }
    }

    private void setupRecyclerView(int cartPosition) {
        shippingCourierAdapter.setShippingCourierAdapterListener(this);
        shippingCourierAdapter.setShippingCourierViewModels(mCourierModelList);
        shippingCourierAdapter.setCartPosition(cartPosition);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                getContext(), LinearLayoutManager.VERTICAL, false);
        rvCourier.setLayoutManager(linearLayoutManager);
        rvCourier.setAdapter(shippingCourierAdapter);
    }

    @Override
    protected void onCloseButtonClick() {
        super.onCloseButtonClick();
        if (shippingCourierBottomsheetListener != null) {
            shippingCourierBottomsheetListener.onCourierShipmentRecpmmendationCloseClicked();
        }
    }

    @Override
    public void onCourierChoosen(ShippingCourierUiModel shippingCourierUiModel, int cartPosition, boolean isNeedPinpoint) {
        ProductData productData = shippingCourierUiModel.getProductData();
        int spId = shippingCourierUiModel.getProductData().getShipperProductId();
        if (shippingCourierUiModel.getProductData().getError() != null) {
            // Not updating when it has Error Pinpoint Needed
            if (!shippingCourierUiModel.getProductData().getError().getErrorId().equals(ErrorProductData.ERROR_PINPOINT_NEEDED)) {
                courierConverter.updateSelectedCourier(mCourierModelList, spId);
            }
        } else {
            courierConverter.updateSelectedCourier(mCourierModelList, spId);
        }
        CourierItemData courierItemData = courierConverter.convertToCourierItemData(shippingCourierUiModel);
        boolean isCod = productData.getCodProductData() != null && (productData.getCodProductData().getIsCodAvailable() == 1);
        if (shippingCourierBottomsheetListener != null) {
            shippingCourierBottomsheetListener.onCourierChoosen(
                    shippingCourierUiModel, courierItemData, mRecipientAddress, cartPosition, isCod,
                    !TextUtils.isEmpty(productData.getPromoCode()), isNeedPinpoint);
        }
        dismiss();
    }

    @Override
    public boolean isToogleYearEndPromotionOn() {
        if (getActivity() != null) {
            RemoteConfig remoteConfig = new FirebaseRemoteConfigImpl(getActivity());
            return remoteConfig.getBoolean("mainapp_enable_year_end_promotion");
        }
        return false;
    }

    @Override
    public void showLoading() {
        llContent.setVisibility(View.GONE);
        llNetworkErrorView.setVisibility(View.GONE);
        pbLoading.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        pbLoading.setVisibility(View.GONE);
        llNetworkErrorView.setVisibility(View.GONE);
        llContent.setVisibility(View.VISIBLE);
    }

    private void showErrorPage(String message, ShipmentCartItemModel shipmentCartItemModel, int cartPosition, List<ShopShipment> shopShipmentList) {
        pbLoading.setVisibility(View.GONE);
        llContent.setVisibility(View.GONE);
        llNetworkErrorView.setVisibility(View.VISIBLE);
        NetworkErrorHelper.showEmptyState(getContext(), llNetworkErrorView, message,
                new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        showLoading();
                        if (shippingCourierBottomsheetListener != null) {
                            shippingCourierBottomsheetListener.onRetryReloadCourier(shipmentCartItemModel, cartPosition, shopShipmentList);
                        }
                    }
                });
        updateHeight();
    }

}
