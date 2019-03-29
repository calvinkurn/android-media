package com.tokopedia.logisticinputreceiptshipment.view.confirmshipment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.logisticanalytics.SalesShippingAnalytics;
import com.tokopedia.logisticanalytics.listener.IConfirmShippingAnalyticsActionListener;
import com.tokopedia.logisticcommon.base.BaseSimpleLogisticActivity;
import com.tokopedia.logisticinputreceiptshipment.R;
import com.tokopedia.logisticinputreceiptshipment.di.DaggerOrderCourierComponent;
import com.tokopedia.logisticinputreceiptshipment.di.OrderCourierComponent;
import com.tokopedia.logisticinputreceiptshipment.view.barcodescanner.ReceiptShipmentBarcodeScannerActivity;
import com.tokopedia.logisticinputreceiptshipment.view.data.CourierSelectionModel;
import com.tokopedia.transaction.common.data.order.ListCourierViewModel;
import com.tokopedia.transaction.common.data.order.OrderDetailData;
import com.tokopedia.transaction.common.data.order.OrderDetailShipmentModel;
import com.tokopedia.transaction.common.data.order.OrderShipmentTypeDef;
import com.tokopedia.transaction.common.listener.ToolbarChangeListener;

import javax.inject.Inject;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

/**
 * Created by kris on 1/3/18. Tokopedia
 */

@RuntimePermissions
public class ConfirmShippingActivity extends BaseSimpleLogisticActivity
        implements ConfirmShippingView,
        ServiceSelectionFragment.ServiceSelectionListener,
        CourierSelectionFragment.OrderCourierFragmentListener,
        ToolbarChangeListener, IConfirmShippingAnalyticsActionListener {

    private static final String EXTRA_ORDER_DETAIL_DATA = "EXTRA_ORDER_DETAIL_DATA";
    private static final String EXTRA_ORDER_MODE_KEY = "EXTRA_ORDER_MODE_KEY";
    private static final String SELECT_COURIER_FRAGMENT_TAG = "select_courier";
    public static final String SELECT_SERVICE_FRAGMENT_TAG = "select_service";
    public static final int CONFIRM_SHIPMENT_MODE = 1;
    public static final int CHANGE_COURIER_MODE = 2;

    private OrderDetailShipmentModel editableModel;

    private TextView courierName;
    private EditText barcodeEditText;
    private ProgressDialog progressDialog;

    @Inject
    OrderCourierPresenterImpl presenter;

    @Inject
    SalesShippingAnalytics salesShippingAnalytics;

    private OrderDetailData orderDetailData;

    public static Intent createInstance(Context context, OrderDetailData data) {
        Intent intent = new Intent(context, ConfirmShippingActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_ORDER_DETAIL_DATA, data);
        bundle.putInt(EXTRA_ORDER_MODE_KEY, CONFIRM_SHIPMENT_MODE);
        intent.putExtras(bundle);
        return intent;
    }

    public static Intent createChangeCourierInstance(Context context, OrderDetailData data) {
        Intent intent = new Intent(context, ConfirmShippingActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_ORDER_DETAIL_DATA, data);
        bundle.putInt(EXTRA_ORDER_MODE_KEY, CHANGE_COURIER_MODE);
        intent.putExtras(bundle);
        return intent;
    }

    private boolean isChangeCourierMode(int orderCode) {
        return (orderCode >= OrderShipmentTypeDef.ORDER_WAITING
                && orderCode < OrderShipmentTypeDef.ORDER_DELIVERED)
                || getIntent().getExtras().getInt(EXTRA_ORDER_MODE_KEY) == CHANGE_COURIER_MODE;
    }

    @Override
    public void receiveShipmentData(ListCourierViewModel model) {
        if (model.getCourierViewModelList().size() == 0) {
            NetworkErrorHelper.showSnackbar(
                    ConfirmShippingActivity.this,
                    getString(R.string.error_no_courier_available_logistic_module)
            );
        } else {
            CourierSelectionFragment courierSelectionFragment = CourierSelectionFragment.
                    createInstance(model);
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.animator.enter_bottom, R.animator.enter_bottom)
                    .add(R.id.main_view, courierSelectionFragment, SELECT_COURIER_FRAGMENT_TAG)
                    .commit();
        }
    }

    @Override
    public void onSuccessConfirm(String successMessage) {
        sendAnalyticsOnClickButtonFinishWithSuccessResult();
        Toast.makeText(this, successMessage, Toast.LENGTH_LONG).show();
        setResult(Activity.RESULT_OK);
        finish();
    }

    @NeedsPermission({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE})
    public void onScanBarcode() {
        requestBarcodeScanner(this, ReceiptShipmentBarcodeScannerActivity.class);
    }

    @Override
    public void showLoading() {
        if (!isFinishing() && progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();
    }

    @Override
    public void hideLoading() {
        if (!isFinishing() && progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
    }

    @Override
    public void onShowError(String errorMessage) {
        NetworkErrorHelper.showSnackbar(this, errorMessage);
    }

    @Override
    public void onShowErrorConfirmShipping(String message) {
        sendAnalyticsOnClickButtonFinishWithFailedResult();
        onShowError(message);
    }

    @Override
    public void onFinishSelectShipment(CourierSelectionModel courierSelectionModel) {
        removeServiceSelectionFragment();
        removeCourierSelectionFragment();
        generateShipmentData(courierSelectionModel);
        toolbar.setTitle(getString(R.string.title_confirm_shipment_logistic_module));
    }

    @Override
    public void onCourierAdapterSelected(CourierSelectionModel model) {
        removeCourierSelectionFragment();
        generateShipmentData(model);
    }

    @SuppressLint("SetTextI18n")
    private void generateShipmentData(CourierSelectionModel courierSelectionModel) {
        editableModel.setShipmentName(courierSelectionModel.getCourierName());
        editableModel.setPackageName(courierSelectionModel.getServiceName());
        editableModel.setPackageId(courierSelectionModel.getServiceId());
        editableModel.setShipmentId(courierSelectionModel.getCourierId());
        courierName.setText(
                editableModel.getShipmentName() + " " + editableModel.getPackageName()
        );
    }

    private View.OnClickListener onGetCourierButtonClickedListener(final OrderDetailData data) {
        return view -> presenter.onGetCourierList(ConfirmShippingActivity.this, data);
    }

    private View.OnClickListener onConfirmButtonClickedListener(final EditText barcodeEditText) {
        return view -> {
            if (editableModel.getPackageId() == null || editableModel.getPackageId().isEmpty()) {
                NetworkErrorHelper.showSnackbar(
                        ConfirmShippingActivity.this,
                        getString(R.string.error_no_courier_chosen_logistic_module)
                );
            } else {
                editableModel.setShippingRef(barcodeEditText.getText().toString());
                presenter.onProcessCourier(
                        ConfirmShippingActivity.this, editableModel,
                        isChangeCourierMode(editableModel.getOrderStatusCode()));
            }
        };
    }

    private View.OnClickListener onBarcodeScanClickedListener() {
        return view -> {
            sendAnalyticsOnClickButtonScan();
            ConfirmShippingActivityPermissionsDispatcher
                    .onScanBarcodeWithCheck(ConfirmShippingActivity.this);
        };
    }

    public void initInjector() {
        OrderCourierComponent component = DaggerOrderCourierComponent
                .builder()
                .baseAppComponent(getComponent())
                .build();
        component.inject(this);
    }

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {
        orderDetailData = extras.getParcelable(EXTRA_ORDER_DETAIL_DATA);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void initView() {
        presenter.setView(this);
        editableModel = new OrderDetailShipmentModel();
        if (getIntent().getExtras().getInt(EXTRA_ORDER_MODE_KEY) == CONFIRM_SHIPMENT_MODE) {
            editableModel.setShipmentId(orderDetailData.getShipmentId());
            editableModel.setPackageId(orderDetailData.getShipmentServiceId());
            editableModel.setShipmentName(orderDetailData.getShipmentName());
            editableModel.setPackageName(orderDetailData.getShipmentServiceName());
        } else {
            editableModel.setShipmentName("");
            editableModel.setPackageName("");
        }
        editableModel.setOrderId(orderDetailData.getOrderId());
        editableModel.setOrderStatusCode(Integer.parseInt(orderDetailData.getOrderCode()));


        FrameLayout frameLayout = findViewById(R.id.parent_view);
        LayoutInflater.from(this).inflate(R.layout.activity_confirm_shipping_logistic_module, frameLayout);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.title_loading));
        progressDialog.setCancelable(false);

        courierName = findViewById(R.id.courier_name);
        barcodeEditText = findViewById(R.id.barcode_edit_text);
        ImageView barcodeScanner = findViewById(R.id.icon_scan);
        LinearLayout courierLayout = findViewById(R.id.courier_layout);
        TextView confirmButton = findViewById(R.id.confirm_button);
        if (isChangeCourierMode(Integer.parseInt(orderDetailData.getOrderCode())))
            toolbar.setTitle(getString(R.string.button_order_detail_change_courier_logistic_module));
        courierLayout.setOnClickListener(onGetCourierButtonClickedListener(orderDetailData));
        confirmButton.setOnClickListener(onConfirmButtonClickedListener(barcodeEditText));
        barcodeEditText.setText(orderDetailData.getAwb());
        barcodeScanner.setOnClickListener(onBarcodeScanClickedListener());
        if (!editableModel.getShipmentName().isEmpty())
            courierName.setText(
                    editableModel.getShipmentName() + " " + editableModel.getPackageName()
            );
    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initVar() {
    }

    @Override
    protected void setActionVar() {

    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().findFragmentByTag(SELECT_SERVICE_FRAGMENT_TAG) != null) {
            removeServiceSelectionFragment();
            toolbar.setTitle(R.string.label_select_courier_logistic_module);
        } else if (getSupportFragmentManager().findFragmentByTag(SELECT_COURIER_FRAGMENT_TAG) != null) {
            removeCourierSelectionFragment();
            toolbar.setTitle(R.string.title_confirm_shipment_logistic_module);
        } else super.onBackPressed();
    }

    private void removeCourierSelectionFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .remove(getSupportFragmentManager()
                        .findFragmentByTag(SELECT_COURIER_FRAGMENT_TAG)).commit();
    }

    private void removeServiceSelectionFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.animator.slide_out_right, R.animator.slide_out_right)
                .remove(getSupportFragmentManager()
                        .findFragmentByTag(SELECT_SERVICE_FRAGMENT_TAG)).commit();
    }

    public boolean isLightToolbarThemes() {
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String barcode = getBarcode(requestCode, resultCode, data);
        if (!TextUtils.isEmpty(barcode)) {
            sendAnalyticsOnViewScanAwb();
        }
        barcodeEditText.setText(barcode);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRemoveTitle() {
        toolbar.setTitle("");
    }

    @Override
    public void onChangeTitle(String toolbarTitle) {
        toolbar.setTitle(toolbarTitle);
    }

    @Override
    public void sendAnalyticsOnClickButtonScan() {
        salesShippingAnalytics.eventClickShippingSalesShippingClickTombolScanAwb();
    }

    @Override
    public void sendAnalyticsOnViewScanAwb() {
        salesShippingAnalytics.eventViewShippingSalesShippingViewScanAwbSuccess();
    }

    @Override
    public void sendAnalyticsOnClickButtonFinishWithSuccessResult() {
        salesShippingAnalytics.eventClickShippingSalesShippingClickTombolSelesaiSuccess();
    }

    @Override
    public void sendAnalyticsOnClickButtonFinishWithFailedResult() {
        salesShippingAnalytics.eventClickShippingSalesShippingClickTombolSelesaiFail();
    }

    @Override
    protected Fragment getNewFragment() {
        return null;
    }

    public static String getBarcode(int requestCode, int resultCode, Intent data) {
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (scanResult != null && scanResult.getContents() != null) {
            return scanResult.getContents();
        }
        return "";
    }

    public static void requestBarcodeScanner(Activity activity, Class customClass) {
        IntentIntegrator intentIntegrator = new IntentIntegrator(activity);
        intentIntegrator.setCaptureActivity(customClass).initiateScan();
    }
}
