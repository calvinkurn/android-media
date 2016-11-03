package com.tokopedia.tkpd.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.design.widget.Snackbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.ListViewHelper;
import com.tkpd.library.utils.SimpleSpinnerAdapter;
import com.tkpd.library.utils.SnackbarManager;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.ShippingConfirmationDetail;
import com.tokopedia.tkpd.analytics.TrackingUtils;
import com.tokopedia.tkpd.analytics.UnifyTracking;
import com.tokopedia.tkpd.customadapter.ListViewShopTxDetailProdListV2;
import com.tokopedia.tkpd.facade.FacadeActionShopTransaction;
import com.tokopedia.tkpd.rxjava.RxUtils;
import com.tokopedia.tkpd.selling.view.activity.SellingDetailActivity;
import com.tokopedia.tkpd.selling.constant.shopshippingdetail.ShopShippingDetailView;
import com.tokopedia.tkpd.network.NetworkErrorHelper;
import com.tokopedia.tkpd.product.activity.ProductInfoActivity;
import com.tokopedia.tkpd.rxjava.RxUtils;
import com.tokopedia.tkpd.selling.SellingService;
import com.tokopedia.tkpd.selling.presenter.listener.SellingView;
import com.tokopedia.tkpd.selling.model.ModelParamSelling;
import com.tokopedia.tkpd.selling.model.modelConfirmShipping.Data;
import com.tokopedia.tkpd.selling.model.orderShipping.OrderDestination;
import com.tokopedia.tkpd.selling.model.orderShipping.OrderDetail;
import com.tokopedia.tkpd.selling.model.orderShipping.OrderShipment;
import com.tokopedia.tkpd.selling.model.orderShipping.OrderShippingList;
import com.tokopedia.tkpd.selling.model.orderShipping.OrderShop;
import com.tokopedia.tkpd.people.activity.PeopleInfoNoDrawerActivity;
import com.tokopedia.tkpd.util.AppUtils;
import com.tokopedia.tkpd.util.RequestPermissionUtil;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Tkpd_Eka on 2/17/2015.
 * modified by m.normansyah on 11/05/2016, ButterKnife and change json String to model
 */
@RuntimePermissions
public class FragmentShopShippingDetailV2 extends Fragment implements ShopShippingDetailView, SellingView {

    private static final String DATAPROCESSORDER = "data_process_order";
    private Bundle bundle;
    private boolean isConfirmDone = false;

    public FragmentShopShippingDetailV2() {

    }

    public static FragmentShopShippingDetailV2 createInstance(Param param, int position) {
        FragmentShopShippingDetailV2 fragment = new FragmentShopShippingDetailV2();
        Bundle bundle = new Bundle();
        bundle.putString(ORDER_ID, param.orderId);
        bundle.putParcelable(ORDER_DATA, Parcels.wrap(param.orderShippingList));
        bundle.putString(USER_ID, param.userId);
        bundle.putString(PERMISSION, param.permission);

        bundle.putInt(POSITION, position);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static class Param {
        public String orderId;
        public OrderShippingList orderShippingList;
        public String userId;
        public String permission;
    }

    private View rootView;
    private ListViewShopTxDetailProdListV2 adapter;
    private TkpdProgressDialog progressDialog;

    OrderShippingList orderShippingList;
    private String orderId;
    private String userId;
    private String invoicePdf;
    private String invoiceUrl;
    private String permission;
    private FacadeActionShopTransaction facadeAction;
    private List<ShippingServices> serviceList;
    private String shippingID;
    private String pickupAddress;
    private int position;
    private CompositeSubscription _subscriptions = new CompositeSubscription();


    @Bind(R.id.buyer_name)
    TextView BuyerName;
    @Bind(R.id.invoice_text)
    TextView Invoice;
    @Bind(R.id.courier)
    TextView Courier;
    @Bind(R.id.total_item)
    TextView TotalItem;
    @Bind(R.id.value)
    TextView Value;
    @Bind(R.id.receiver_name)
    TextView ReceiverName;
    @Bind(R.id.destination)
    TextView Destination;
    @Bind(R.id.error_message)
    TextView ErrorMessage;
    @Bind(R.id.scan)
    ImageView ScanBarcode;
    @Bind(R.id.confirm_button)
    TextView ConfirmButton;
    @Bind(R.id.detail_button)
    TextView DetailButton;
    @Bind(R.id.ship_ref_number)
    EditText ReferenceNumber;
    @Bind(R.id.cancel_button)
    TextView CancelButton;
    @Bind(R.id.sender_name)
    TextView SenderName;
    @Bind(R.id.sender_phone)
    TextView SenderPhone;
    @Bind(R.id.error_spinner)
    TextView errorSpinner;
    @Bind(R.id.checkBoxSwitchCourier)
    CheckBox SwitchCourier;
    @Bind(R.id.spinner_kurir)
    Spinner SpinnerAgency;
    @Bind(R.id.spinner_type)
    Spinner SpinnerService;
    @Bind(R.id.sender_form)
    View SenderForm;
    @Bind(R.id.product_list)
    ListView ProductListView;
    @Bind(R.id.layout)
    LinearLayout ShippingLayout;
    @Bind(R.id.scroll_view)
    ScrollView mainScroll;
    @Bind(R.id.loadingSpinner)
    ProgressBar editFormProgress;
    @Bind(R.id.layout_destination_default)
    public View viewDefaultDestination;
    @Bind(R.id.layout_pickup_instant_shipping_courier)
    public View viewPickupLocationCourier;
    @Bind(R.id.pickup_detail_location)
    public TextView pickupLocationDetail;
    @Bind(R.id.destination_detail_location)
    public TextView deliveryLocationDetail;

    public static class ShippingServices {
        public String serviceName;
        public String serviceId;
        public List<String> packageList;
        public List<String> packageId;

        public ShippingServices() {
            packageId = new ArrayList<>();
            packageList = new ArrayList<>();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            isConfirmDone = savedInstanceState.getBoolean(DATAPROCESSORDER, false);
            if (isConfirmDone) {
                finishShipping(true);
            }
        }
        initVar(savedInstanceState);
        getOrderData();
    }

    private void initVar(Bundle savedInstanceState) {
        position = getArguments().getInt(POSITION);
        orderId = getArguments().getString(ORDER_ID);
        orderShippingList = Parcels.unwrap(getArguments().getParcelable(ORDER_DATA));
        userId = getArguments().getString(USER_ID);
        permission = getArguments().getString(PERMISSION);
        serviceList = new ArrayList<>();
        facadeAction = FacadeActionShopTransaction.createInstance(getActivity(), orderId);
        progressDialog = new TkpdProgressDialog(getActivity(), TkpdProgressDialog.NORMAL_PROGRESS);
    }

    private void getOrderData() {
        orderId = orderShippingList.getOrderDetail().getDetailOrderId() + "";
        invoicePdf = orderShippingList.getOrderDetail().getDetailPdf();
        invoiceUrl = orderShippingList.getOrderDetail().getDetailPdfUri();
    }

    private void getEditShippingForm() {
        editFormProgress.setVisibility(View.VISIBLE);
        facadeAction.setCompositeSubscription(_subscriptions);
        facadeAction.getShippingForm(onGetEditShippingForm());
    }

    private FacadeActionShopTransaction.OnGetEditShippingListener onGetEditShippingForm() {
        return new FacadeActionShopTransaction.OnGetEditShippingListener() {
            @Override
            public void onSuccess(List<ShippingServices> serviceLists) {
                serviceList = serviceLists;
                editFormProgress.setVisibility(View.GONE);
                initEditShipping();
            }

            @Override
            public void onFailed() {

            }
        };
    }

    private void initEditShipping() {
        List<String> serviceName = new ArrayList<>();
        for (int i = 0; i < serviceList.size(); i++) {
            serviceName.add(serviceList.get(i).serviceName);
        }
        SimpleSpinnerAdapter servicePackageAdapter = SimpleSpinnerAdapter.createAdapter(getActivity(), serviceName);
        SpinnerAgency.setAdapter(servicePackageAdapter);
        SpinnerAgency.setVisibility(View.VISIBLE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initCreateView(inflater, container);
        _subscriptions = RxUtils.getNewCompositeSubIfUnsubscribed(_subscriptions);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        RxUtils.unsubscribeIfNotNull(_subscriptions);
    }

    private void initCreateView(LayoutInflater inflater, ViewGroup container) {
        rootView = inflater.inflate(R.layout.activity_shipping_confirmation_prod_conf, container, false);
        ButterKnife.bind(this, rootView);
        setViewDataV4();
        setAdapter();
        getEditShippingForm();
    }

    private void setViewDataV4() {
        OrderDetail orderDetail = orderShippingList.getOrderDetail();
        TotalItem.setText(Html.fromHtml(getString(R.string.title_total_item) + ": <b>" + orderDetail.getDetailQuantity() + " ( " + orderDetail.getDetailTotalWeight() + "kg )</b>"));
        Invoice.setText(orderDetail.getDetailInvoice());
        Value.setText(Html.fromHtml(getString(R.string.title_transaction_value) + " : <b>" + orderDetail.getDetailOpenAmountIdr() + "</b>"));

        if (CommonUtils.checkNullForZeroJson(orderDetail.getDetailDropshipName())
                && CommonUtils.checkNullForZeroJson(orderDetail.getDetailDropshipTelp())) {
            SenderName.setText(orderDetail.getDetailDropshipName());
            SenderPhone.setText(orderDetail.getDetailDropshipTelp());
            SenderForm.setVisibility(View.VISIBLE);
        } else {
            SenderForm.setVisibility(View.GONE);
        }

        BuyerName.setText(orderShippingList.getOrderCustomer().getCustomerName());

        OrderShipment orderShipment = orderShippingList.getOrderShipment();
        Courier.setText(orderShipment.getShipmentName() + "( " + orderShipment.getShipmentProduct() + " )");
        shippingID = orderShipment.getShipmentId();
        if (orderShippingList.getIsPickUp() == 1) {
            ConfirmButton.setText(getString(R.string.title_pickup_button));
            viewDefaultDestination.setVisibility(View.GONE);
            viewPickupLocationCourier.setVisibility(View.VISIBLE);
        } else {
            ConfirmButton.setText(getString(R.string.title_confirm_button));
            viewDefaultDestination.setVisibility(View.VISIBLE);
            viewPickupLocationCourier.setVisibility(View.GONE);
        }

        OrderDestination orderDestination = orderShippingList.getOrderDestination();
        ReceiverName.setText(Html.fromHtml(orderDestination.getReceiverName()));
        String vDest = Html.fromHtml(orderDestination.getAddressStreet()).toString()
                + "\n" + orderDestination.getAddressDistrict() + "    " + orderDestination.getAddressCity() + ", " + orderDestination.getAddressPostal()
                + "\n" + orderDestination.getAddressProvince() + "\n" + getString(R.string.title_phone) + " : " + orderDestination.getReceiverPhone();
        vDest = vDest.replaceAll("&#39;", "'");
        vDest = vDest.replaceAll("&amp;", "'");
        Destination.setText(vDest);
        deliveryLocationDetail.setText(vDest);

        OrderShop orderShop = orderShippingList.getOrderShop();
        pickupAddress = Html.fromHtml(orderShop.getAddressStreet())
                + "\n" + Html.fromHtml(orderShop.getAddressCity()).toString() + ", " + Html.fromHtml(orderShop.getAddressPostal())
                + "\n" + orderShop.getAddressProvince()
                + "\n" + getString(R.string.title_phone) + ":" + orderShop.getShipperPhone();

        pickupLocationDetail.setText(pickupAddress);
        if (isConfirmDone) {
            finishShipping(true);
        }
    }

    @SuppressWarnings("EmptyCatchBlock")
    private void setAdapter() {
        adapter = ListViewShopTxDetailProdListV2.createInstance(getActivity(), orderShippingList.getOrderProducts());
        ProductListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        ProductListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(ProductInfoActivity.createInstance(getActivity(), orderShippingList.getOrderProducts().get(position).getProductId().toString()));
            }
        });
        ListViewHelper.getListViewSize(ProductListView);
    }

    @OnClick(R.id.buyer_name)
    public void onBuyerName() {
        startActivity(
                PeopleInfoNoDrawerActivity.createInstance(getActivity(), userId)
        );
    }

    @OnClick(R.id.detail_button)
    public void onDetailClick() {
        startActivity(ShippingConfirmationDetail.createInstance(getActivity(), orderShippingList, permission, userId, invoiceUrl, invoicePdf));
    }

    @OnItemSelected(R.id.spinner_kurir)
    public void onAgencySelect(int position) {
        if (position == 0) {
            SpinnerService.setVisibility(View.INVISIBLE);
        } else {
            setSpinnerService(position);
        }
    }

    @OnCheckedChanged(R.id.checkBoxSwitchCourier)
    public void onSwitchCourierChecked(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            ShippingLayout.setVisibility(View.VISIBLE);
        } else {
            resetError();
            ShippingLayout.setVisibility(View.GONE);
            SpinnerAgency.setSelection(0);
            SpinnerService.setSelection(0);
        }
    }

    @OnClick(R.id.confirm_button)
    public void onConfirmClick() {
        if (checkConfirmationError()) {
            confirmShipping();
            TrackingUtils.eventLoca(getActivity().getString(R.string.shipping_confirmation));
        }
    }

    @OnClick(R.id.invoice_text)
    public void onInvoiceClick() {
        AppUtils.InvoiceDialog(getActivity(), invoiceUrl, invoicePdf, Invoice.getText().toString());
    }

    @OnClick(R.id.scan)
    public void scanBarCode() {
        FragmentShopShippingDetailV2PermissionsDispatcher.onScanBarcodeWithCheck(FragmentShopShippingDetailV2.this);
    }

    @NeedsPermission({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE})
    public void onScanBarcode() {
        startActivityForResult(CommonUtils.requestBarcodeScanner(), 0);
    }

    @OnClick(R.id.cancel_button)
    public void cancelDialog() {
        createCancelDialog();
    }

    private void createCancelDialog() {
        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_cancel_order);
        final EditText Remark = (EditText) dialog.findViewById(R.id.remark);
        TextView ConfirmButton = (TextView) dialog.findViewById(R.id.confirm_button);
        ConfirmButton.setOnClickListener(onCancelListener(dialog, Remark));
        dialog.show();
    }

    private View.OnClickListener onCancelListener(final Dialog dialog, final EditText remark) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (remark.length() == 0) {
                    remark.setError(getActivity().getString(R.string.error_field_required));
                } else if (remark.length() < 5 && remark.length() > 0) {
                    remark.setError("Minimal 5 karakter");
                } else if (remark.length() >= 5) {
                    cancelShipping(remark.getText().toString());
                    dialog.dismiss();
                }
            }
        };
    }

    private void cancelShipping(String remark) {
        bundle = new Bundle();
        ModelParamSelling modelParamSelling = new ModelParamSelling();
        modelParamSelling.setActionType("reject");
        modelParamSelling.setOrderId(orderId);
        modelParamSelling.setReason(remark);

        modelParamSelling.setPosition(position);
        bundle.putParcelable(SellingService.MODEL_PARAM_SELLING_KEY, Parcels.wrap(modelParamSelling));

        ((SellingDetailActivity) getActivity()).SellingAction(SellingService.CANCEL_SHIPPING, bundle);

//        progressDialog.showDialog();
//        facadeAction.cancelShipping(remark, onProcessShippingListener());
    }

    private void confirmShipping() {
        bundle = new Bundle();
        ModelParamSelling modelParamSelling = new ModelParamSelling();
        modelParamSelling.setActionType("confirm");
        modelParamSelling.setOrderId(orderId);
        modelParamSelling.setPosition(position);
        modelParamSelling.setRefNum(ReferenceNumber.getText().toString());
        modelParamSelling.setShipmentId(getAgencyId());
        modelParamSelling.setShipmentName(getAgencyName());
        modelParamSelling.setSpId(getServiceId());
        bundle.putParcelable(SellingService.MODEL_PARAM_SELLING_KEY, Parcels.wrap(modelParamSelling));
        UnifyTracking.eventConfirmShipping();
        ((SellingDetailActivity) getActivity()).SellingAction(SellingService.CONFIRM_SHIPPING, bundle);
    }

    private boolean checkConfirmationError() {
        resetError();
        if (orderShippingList.getIsPickUp() != 1 && (ReferenceNumber.length() < 7 || ReferenceNumber.length() > 17)) {
            ReferenceNumber.requestFocus();
            ReferenceNumber.setError(getString(R.string.error_receipt_number));
            return false;
        }
        if (SwitchCourier.isChecked()) {
            if (SpinnerAgency.getSelectedItemPosition() == 0) {
                errorSpinner.setText(getString(R.string.error_shipping_must_choose));
                errorSpinner.setVisibility(View.VISIBLE);
                return false;
            }
            if (SpinnerService.getSelectedItemPosition() == 0) {
                errorSpinner.setText(getString(R.string.error_service_must_choose));
                errorSpinner.setVisibility(View.VISIBLE);
                return false;
            }
        }
        return true;
    }

    private String getAgencyId() {
        try {
            return serviceList.get(SpinnerAgency.getSelectedItemPosition()).serviceId;
        } catch (Exception e) {
            return "";
        }
    }

    private String getAgencyName() {
        try {
            return serviceList.get(SpinnerAgency.getSelectedItemPosition()).serviceName;
        } catch (Exception e) {
            return "";
        }
    }

    private String getServiceId() {
        try {
            return serviceList.get(SpinnerAgency.getSelectedItemPosition()).packageId.get(SpinnerService.getSelectedItemPosition());
        } catch (Exception e) {
            return "";
        }
    }

    private FacadeActionShopTransaction.OnConfirmMultiShippingListener onProcessShippingListener() {
        return new FacadeActionShopTransaction.OnConfirmMultiShippingListener() {
            @Override
            public void onSuccess() {
                progressDialog.dismiss();
                finishShipping(false);
            }

            @Override
            public void onFailed() {
                progressDialog.dismiss();
            }
        };
    }

    private void finishShipping(boolean isAfterSaveInstance) {
        CancelButton.setVisibility(View.GONE);
        ConfirmButton.setText(getActivity().getString(R.string.title_order_processed));
        ConfirmButton.setOnClickListener(null);
        ReferenceNumber.setClickable(false);
        ReferenceNumber.setFocusable(false);
        ScanBarcode.setClickable(false);
        SwitchCourier.setClickable(false);
        isConfirmDone = true;
        if(!isAfterSaveInstance) {
            getActivity().setResult(getActivity().RESULT_OK);
//            ((SellingDetailActivity) getActivity()).notifyChangeShipping();
        }

    }

    private void setSpinnerService(int pos) {
        List<String> packageName = new ArrayList<>();
        for (int i = 0; i < serviceList.get(pos).packageList.size(); i++) {
            packageName.add(serviceList.get(pos).packageList.get(i));
        }
        SimpleSpinnerAdapter servicePackageAdapter = SimpleSpinnerAdapter.createAdapter(getActivity(), packageName);
        SpinnerService.setAdapter(servicePackageAdapter);
        SpinnerService.setVisibility(View.VISIBLE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK)
            ReferenceNumber.setText(CommonUtils.getBarcode(data));
    }

    @Override
    public void onResume() {
        super.onResume();
        CommonUtils.dumper("NISTAG : ONRESUME");
        getEditShippingForm();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (isConfirmDone) {
            outState.putBoolean(DATAPROCESSORDER, true);
        }
    }

    @Override
    public void showProgress() {
        progressDialog.showDialog();
    }


    @Override
    public int getFragmentId() {
        return 0;
    }

    @Override
    public void ariseRetry(int type, Object... data) {
        switch (type) {
            case SellingService.CONFIRM_SHIPPING:
            case SellingService.CANCEL_SHIPPING:
                progressDialog.dismiss();
                break;
        }
    }

    @Override
    public void setData(int type, Bundle data) {
        switch (type) {
            case SellingService.CONFIRM_SHIPPING:
            case SellingService.CANCEL_SHIPPING:
                Data result = Parcels.unwrap(data.getParcelable(SellingService.MODEL_CONFIRM_SHIPPING_KEY));
                if (result.getIsSuccess() == 1) {
                    progressDialog.dismiss();
                    finishShipping(false);
                }
                break;
        }
    }

    @Override
    public void onNetworkError(int type, Object... data) {
        switch (type) {
            case SellingService.CONFIRM_SHIPPING:
                progressDialog.dismiss();
                NetworkErrorHelper.showDialog(getActivity(), new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        if (bundle != null) {
                            ((SellingDetailActivity) getActivity()).SellingAction(SellingService.CONFIRM_SHIPPING, bundle);
                        }
                    }
                });
                break;
            case SellingService.CANCEL_SHIPPING:
                progressDialog.dismiss();
                NetworkErrorHelper.showDialog(getActivity(), new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        if (bundle != null) {
                            ((SellingDetailActivity) getActivity()).SellingAction(SellingService.CANCEL_SHIPPING, bundle);
                        }
                    }
                });
                break;
        }
    }

    @Override
    public void onMessageError(int type, Object... data) {
        String error = (String) data[0];

        switch (type) {
            case SellingService.CONFIRM_SHIPPING:
            case SellingService.CANCEL_SHIPPING:
                progressDialog.dismiss();
                showSnackBarError(error);
                break;
        }
    }

    public void showSnackBarError(String error) {
        final Snackbar snackbar = SnackbarManager.make(getActivity(),
                error,
                Snackbar.LENGTH_LONG);
        snackbar.show();
    }


    private void resetError() {
        errorSpinner.setVisibility(View.GONE);
    }

    private void showDialogError(Context context, String error) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        LayoutInflater li = LayoutInflater.from(context);
        @SuppressLint("InflateParams")
        View promptsView = li.inflate(R.layout.error_network_dialog, null);
        TextView msg = (TextView) promptsView.findViewById(R.id.msg);
        msg.setText(error);
        dialog.setView(promptsView);
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.create().show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        FragmentShopShippingDetailV2PermissionsDispatcher.onRequestPermissionsResult(
                FragmentShopShippingDetailV2.this, requestCode, grantResults);
    }


    @OnShowRationale({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE})
    void showRationaleForStorageAndCamera(final PermissionRequest request) {
        List<String> listPermission = new ArrayList<>();
        listPermission.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        listPermission.add(Manifest.permission.CAMERA);

        RequestPermissionUtil.onShowRationale(getActivity(), request, listPermission);
    }

    @OnPermissionDenied(Manifest.permission.CAMERA)
    void showDeniedForCamera() {
        RequestPermissionUtil.onPermissionDenied(getActivity(),Manifest.permission.CAMERA);
    }

    @OnNeverAskAgain(Manifest.permission.CAMERA)
    void showNeverAskForCamera() {
        RequestPermissionUtil.onNeverAskAgain(getActivity(),Manifest.permission.CAMERA);
    }

    @OnPermissionDenied(Manifest.permission.READ_EXTERNAL_STORAGE)
    void showDeniedForStorage() {
        RequestPermissionUtil.onPermissionDenied(getActivity(),Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    @OnNeverAskAgain(Manifest.permission.READ_EXTERNAL_STORAGE)
    void showNeverAskForStorage() {
        RequestPermissionUtil.onNeverAskAgain(getActivity(),Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    @OnPermissionDenied({Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE})
    void showDeniedForStorageAndCamera() {
        List<String> listPermission = new ArrayList<>();
        listPermission.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        listPermission.add(Manifest.permission.CAMERA);

        RequestPermissionUtil.onPermissionDenied(getActivity(),listPermission);
    }

    @OnNeverAskAgain({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE})
    void showNeverAskForStorageAndCamera() {
        List<String> listPermission = new ArrayList<>();
        listPermission.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        listPermission.add(Manifest.permission.CAMERA);

        RequestPermissionUtil.onNeverAskAgain(getActivity(),listPermission);
    }

}
