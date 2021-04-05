package com.tokopedia.logisticcart.shipping.features.shippingcourier.view;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel;
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.ErrorProductData;
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.ProductData;
import com.tokopedia.logisticcart.R;
import com.tokopedia.logisticcart.shipping.features.shippingcourier.di.DaggerShippingCourierComponent;
import com.tokopedia.logisticcart.shipping.features.shippingcourier.di.ShippingCourierComponent;
import com.tokopedia.logisticcart.shipping.features.shippingcourier.di.ShippingCourierModule;
import com.tokopedia.logisticcart.shipping.model.CourierItemData;
import com.tokopedia.logisticcart.shipping.model.ShipmentCartItemModel;
import com.tokopedia.logisticcart.shipping.model.ShippingCourierUiModel;
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.unifycomponents.BottomSheetUnify;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import kotlin.Unit;

/**
 * Created by Irfan Khoirul on 06/08/18.
 */

public class ShippingCourierBottomsheet implements ShippingCourierContract.View, ShippingCourierAdapterListener {

    public static final String ARGUMENT_SHIPPING_COURIER_VIEW_MODEL_LIST = "ARGUMENT_SHIPPING_COURIER_VIEW_MODEL_LIST";
    public static final String ARGUMENT_CART_POSITION = "ARGUMENT_CART_POSITION";
    public static final String ARGUMENT_RECIPIENT_ADDRESS_MODEL = "ARGUMENT_RECIPIENT_ADDRESS_MODEL";

    private LinearLayout llContent;
    private RecyclerView rvCourier;
    private LinearLayout llNetworkErrorView;
    private ProgressBar pbLoading;

    private Activity activity;
    private BottomSheetUnify bottomSheet;
    private Bundle bundle;
    private ShippingCourierBottomsheetListener shippingCourierBottomsheetListener;
    private List<ShippingCourierUiModel> mCourierModelList = new ArrayList<>();
    private RecipientAddressModel mRecipientAddress;

    @Inject
    ShippingCourierAdapter shippingCourierAdapter;
    @Inject
    ShippingCourierConverter courierConverter;

    public void show(Activity activity,
                     FragmentManager fragmentManager,
                     ShippingCourierBottomsheetListener shippingCourierBottomsheetListener,
                     List<ShippingCourierUiModel> shippingCourierUiModels,
                     RecipientAddressModel recipientAddressModel,
                     int cartPosition) {
        this.activity = activity;
        this.shippingCourierBottomsheetListener = shippingCourierBottomsheetListener;
        initData(shippingCourierUiModels, recipientAddressModel, cartPosition);
        initBottomSheet(activity);
        initView(activity);
        bottomSheet.show(fragmentManager, this.getClass().getSimpleName());
    }

    private void initData(List<ShippingCourierUiModel> shippingCourierUiModels, RecipientAddressModel recipientAddressModel, int cartPosition) {
        bundle = new Bundle();
        if (shippingCourierUiModels != null) {
            bundle.putParcelableArrayList(ARGUMENT_SHIPPING_COURIER_VIEW_MODEL_LIST, new ArrayList<>(shippingCourierUiModels));
        }
        bundle.putParcelable(ARGUMENT_RECIPIENT_ADDRESS_MODEL, recipientAddressModel);
        bundle.putInt(ARGUMENT_CART_POSITION, cartPosition);
    }

    private void initBottomSheet(Activity activity) {
        bottomSheet = new BottomSheetUnify();
        bottomSheet.setShowCloseIcon(true);
        bottomSheet.setTitle(activity.getString(R.string.title_shipment_courier_bottomsheet));
        bottomSheet.setClearContentPadding(true);
        bottomSheet.setCustomPeekHeight(Resources.getSystem().getDisplayMetrics().heightPixels / 2);
        bottomSheet.setDragable(true);
        bottomSheet.setHideable(true);
        bottomSheet.setCloseClickListener(view -> {
            if (shippingCourierBottomsheetListener != null) {
                shippingCourierBottomsheetListener.onCourierShipmentRecpmmendationCloseClicked();
            }
            return Unit.INSTANCE;
        });
    }

    public void setShippingCourierViewModels(List<ShippingCourierUiModel> shippingCourierUiModels,
                                             int cartPosition, ShipmentCartItemModel shipmentCartItemModel) {
        hideLoading();
        if (shippingCourierUiModels != null && shippingCourierUiModels.size() > 0) {
            mCourierModelList = shippingCourierUiModels;
            setupRecyclerView(cartPosition);
        } else {
            showErrorPage(activity.getString(R.string.message_error_shipping_general), shipmentCartItemModel, cartPosition);
        }
    }

    private void initializeInjector() {
        ShippingCourierComponent component = DaggerShippingCourierComponent.builder()
                .shippingCourierModule(new ShippingCourierModule())
                .build();

        component.inject(this);
    }


    public void initView(Activity activity) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_shipment_courier_choice, null);

        llContent = view.findViewById(R.id.ll_content);
        rvCourier = view.findViewById(R.id.rv_courier);
        llNetworkErrorView = view.findViewById(R.id.ll_network_error_view);
        pbLoading = view.findViewById(R.id.pb_loading);

        bottomSheet.setChild(view);

        initializeInjector();
        loadData();
    }

    private void loadData() {
        if (bundle != null) {
            mRecipientAddress = bundle.getParcelable(ARGUMENT_RECIPIENT_ADDRESS_MODEL);
            int cartPosition = bundle.getInt(ARGUMENT_CART_POSITION);
            List<ShippingCourierUiModel> shippingCourierUiModels = bundle.getParcelableArrayList(ARGUMENT_SHIPPING_COURIER_VIEW_MODEL_LIST);
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
                activity, LinearLayoutManager.VERTICAL, false);
        rvCourier.setLayoutManager(linearLayoutManager);
        rvCourier.setAdapter(shippingCourierAdapter);
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
        bottomSheet.dismiss();
    }

    @Override
    public boolean isToogleYearEndPromotionOn() {
        if (activity != null) {
            RemoteConfig remoteConfig = new FirebaseRemoteConfigImpl(activity);
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

    private void showErrorPage(String message, ShipmentCartItemModel shipmentCartItemModel, int cartPosition) {
        pbLoading.setVisibility(View.GONE);
        llContent.setVisibility(View.GONE);
        llNetworkErrorView.setVisibility(View.VISIBLE);
        NetworkErrorHelper.showEmptyState(activity, llNetworkErrorView, message,
                () -> {
                    showLoading();
                    if (shippingCourierBottomsheetListener != null) {
                        shippingCourierBottomsheetListener.onRetryReloadCourier(shipmentCartItemModel, cartPosition);
                    }
                });
    }

}
