package com.tokopedia.shipping_recommendation.shippingcourier.view;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.design.component.BottomSheets;
import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.ErrorProductData;
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.shipping_recommendation.R;
import com.tokopedia.shipping_recommendation.domain.shipping.CourierItemData;
import com.tokopedia.shipping_recommendation.domain.shipping.RecipientAddressModel;
import com.tokopedia.shipping_recommendation.domain.shipping.ShipmentCartItemModel;
import com.tokopedia.shipping_recommendation.domain.shipping.ShippingCourierViewModel;
import com.tokopedia.shipping_recommendation.domain.shipping.ShopShipment;
import com.tokopedia.shipping_recommendation.shippingcourier.di.DaggerShippingCourierComponent;
import com.tokopedia.shipping_recommendation.shippingcourier.di.ShippingCourierComponent;
import com.tokopedia.shipping_recommendation.shippingcourier.di.ShippingCourierModule;

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

    @Inject
    ShippingCourierContract.Presenter presenter;
    @Inject
    ShippingCourierAdapter shippingCourierAdapter;

    public static ShippingCourierBottomsheet newInstance(List<ShippingCourierViewModel> shippingCourierViewModels,
                                                         RecipientAddressModel recipientAddressModel,
                                                         int cartPosition) {
        ShippingCourierBottomsheet shippingCourierBottomsheet =
                new ShippingCourierBottomsheet();
        Bundle bundle = new Bundle();
        if (shippingCourierViewModels != null) {
            bundle.putParcelableArrayList(ARGUMENT_SHIPPING_COURIER_VIEW_MODEL_LIST, new ArrayList<>(shippingCourierViewModels));
        }
        bundle.putParcelable(ARGUMENT_RECIPIENT_ADDRESS_MODEL, recipientAddressModel);
        bundle.putInt(ARGUMENT_CART_POSITION, cartPosition);
        shippingCourierBottomsheet.setArguments(bundle);

        return shippingCourierBottomsheet;
    }

    public void setShippingCourierBottomsheetListener(ShippingCourierBottomsheetListener shippingCourierBottomsheetListener) {
        this.shippingCourierBottomsheetListener = shippingCourierBottomsheetListener;
    }

    public void setShippingCourierViewModels(List<ShippingCourierViewModel> shippingCourierViewModels,
                                             int cartPosition, ShipmentCartItemModel shipmentCartItemModel,
                                             List<ShopShipment> shopShipmentList) {
        hideLoading();
        if (shippingCourierViewModels != null && shippingCourierViewModels.size() > 0) {
            presenter.setData(shippingCourierViewModels);
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
        presenter.attachView(this);
        if (getArguments() != null) {
            RecipientAddressModel recipientAddressModel = getArguments().getParcelable(ARGUMENT_RECIPIENT_ADDRESS_MODEL);
            presenter.setRecipientAddressModel(recipientAddressModel);
            int cartPosition = getArguments().getInt(ARGUMENT_CART_POSITION);
            List<ShippingCourierViewModel> shippingCourierViewModels =
                    getArguments().getParcelableArrayList(ARGUMENT_SHIPPING_COURIER_VIEW_MODEL_LIST);
            if (shippingCourierViewModels != null) {
                presenter.setData(shippingCourierViewModels);
                setupRecyclerView(cartPosition);
            } else {
                showLoading();
            }
        }
    }

    private void setupRecyclerView(int cartPosition) {
        shippingCourierAdapter.setShippingCourierAdapterListener(this);
        shippingCourierAdapter.setShippingCourierViewModels(presenter.getShippingCourierViewModels());
        shippingCourierAdapter.setCartPosition(cartPosition);
        boolean hasCourierPromo = false;
        for (ShippingCourierViewModel shippingCourierViewModel : presenter.getShippingCourierViewModels()) {
            if (!TextUtils.isEmpty(shippingCourierViewModel.getProductData().getPromoCode())) {
                hasCourierPromo = true;
                break;
            }
        }
        shippingCourierAdapter.setHasCourierPromo(hasCourierPromo);
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
    public void onCourierChoosen(ShippingCourierViewModel shippingCourierViewModel, int cartPosition, boolean hasCourierPromo, boolean isNeedPinpoint) {
        if (shippingCourierViewModel.getProductData().getError() != null) {
            if (!shippingCourierViewModel.getProductData().getError().getErrorId().equals(ErrorProductData.ERROR_PINPOINT_NEEDED)) {
                presenter.updateSelectedCourier(shippingCourierViewModel);
            }
        } else {
            presenter.updateSelectedCourier(shippingCourierViewModel);
        }
        CourierItemData courierItemData = presenter.getCourierItemData(shippingCourierViewModel);
        if (shippingCourierBottomsheetListener != null) {
            shippingCourierBottomsheetListener.onCourierChoosen(
                    courierItemData, presenter.getRecipientAddressModel(), cartPosition, hasCourierPromo,
                    !TextUtils.isEmpty(shippingCourierViewModel.getProductData().getPromoCode()), isNeedPinpoint);
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

    @Override
    public void showErrorPage(String message, ShipmentCartItemModel shipmentCartItemModel, int cartPosition, List<ShopShipment> shopShipmentList) {
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
