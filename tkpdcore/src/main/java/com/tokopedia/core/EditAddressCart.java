package com.tokopedia.core;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.Logger;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.geolocation.activity.GeolocationActivity;
import com.tokopedia.core.geolocation.model.LocationPass;
import com.tokopedia.core.geolocation.utils.GeoLocationUtils;
import com.tokopedia.core.manage.people.address.ManageAddressConstant;
import com.tokopedia.core.manage.people.address.activity.AddAddressActivity;
import com.tokopedia.core.manage.people.address.activity.ChooseAddressActivity;
import com.tokopedia.core.manage.people.address.model.Destination;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.NetworkHandler;
import com.tokopedia.core.network.NetworkHandler.NetworkHandlerListener;
import com.tokopedia.core.payment.interactor.PaymentNetInteractor;
import com.tokopedia.core.payment.interactor.PaymentNetInteractorImpl;
import com.tokopedia.core.payment.model.responsecalculateshipping.CalculateShipping;
import com.tokopedia.core.payment.model.responsecalculateshipping.Shipment;
import com.tokopedia.core.payment.model.responsecalculateshipping.ShipmentPackage;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.core.var.TkpdUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class EditAddressCart extends TActivity {
    private static final String NO_COURIER_AVAILABLE = "Agen kurir tidak tersedia";
    public static final int CHOOSE_ADDRESS = 0;
    public static final int CREATE_NEW_ADDRESS = 1;
    private static final int CHOOSE_LOCATION = 2;
    private ArrayList<String> AgencyName = new ArrayList<>();
    private ArrayList<String> AgencyID = new ArrayList<>();
    private ArrayList<String> AgencyShowID = new ArrayList<>();
    private ArrayList<String> AgencyShow = new ArrayList<>();
    private ArrayList<String> AddressID = new ArrayList<>();
    private ArrayList<ArrayList<String>> AgencyPrice = new ArrayList<ArrayList<String>>();
    private ArrayList<ArrayList<String>> PriceTotal = new ArrayList<ArrayList<String>>();
    private ArrayList<ArrayList<String>> AgencyService = new ArrayList<ArrayList<String>>();
    private ArrayList<ArrayList<String>> AgencyServiceID = new ArrayList<ArrayList<String>>();
    private ArrayList<ArrayList<Integer>> isShowMap = new ArrayList<>();
    private String AddressString;
    private Spinner SpinnerAgency;
    private ArrayAdapter<String> adapterAgency;
    private ArrayAdapter<String> nullAdapterAgency;
    private Spinner SpinnerAgencyOption;
    private ArrayAdapter<String> adapterAgencyOption;
    private TextView Address;
    private TextView ShippingCost;
    private LinearLayout container;
    private int MinOrder;
    private String QuantityVal = "1";
    private String CurrAddressID;
    private String ShopID;
    private String Weight;
    private String CurrShippingID;
    private String CurrSPid;
    private TextView AddressTitle;
    private TextView ChooseAddressBut;
    private TextView BuyBut;
    private TextView AddAddressBut;
    private ImageView ProdImg;
    private String InsuranceVal;
    private String OldAddressID;
    private String OldShippingID;
    private String OldSPid;
    private TkpdProgressDialog progressdialog;
    private ProgressBar loadingBar;
    private View ShipCostView;
    private Boolean Startup = true;
    private String ReceiverName;
    private String ReceiverPhone;
    private String Province;
    private String Regency;
    private String SubDistrict;
    private String AddressType;
    private String NewAddress;
    private String PostCode;
    private View viewAssignLocation;
    private View viewFieldLocation;
    private EditText valueLocation;
    private int currentShowMapStatus;

    private boolean isAlreadyReverseGeocode = true;
    private int showMap;
    private String latitude = "";
    private String longitude = "";
    private String addressName;
    private String addressStreet;
    private String addressGeoLocation;
    private int isUpdateGeoLocation = 0;
    private PaymentNetInteractor interactor;

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_CART_EDIT_ADDRESS;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.activity_edit_address);

        interactor = new PaymentNetInteractorImpl();
        progressdialog = new TkpdProgressDialog(this, TkpdProgressDialog.NORMAL_PROGRESS);

        if (getActionBar() != null) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }


        loadingBar = (ProgressBar) findViewById(R.id.loading_calculate);
        ShipCostView = (View) findViewById(R.id.shipping_price_view);
        Address = (TextView) findViewById(R.id.address_detail);
        BuyBut = (TextView) findViewById(R.id.buy_button);
        AddressTitle = (TextView) findViewById(R.id.title_address);
        SpinnerAgency = (Spinner) findViewById(R.id.agency);
        SpinnerAgencyOption = (Spinner) findViewById(R.id.service);
        ShippingCost = (TextView) findViewById(R.id.shipping_cost);
        container = (LinearLayout) findViewById(R.id.container);
        ChooseAddressBut = (TextView) findViewById(R.id.choose_address_but);
        ProdImg = (ImageView) findViewById(R.id.image_prod);
        AddAddressBut = (TextView) findViewById(R.id.add_address_but);
        viewAssignLocation = findViewById(R.id.layout_value_location);
        viewFieldLocation = findViewById(R.id.layout_field_location);
        valueLocation = (EditText) findViewById(R.id.value_location);

        SpinnerAgency.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int pos, long arg3) {
                if (AgencyShowID.size() > 0) {
                    CurrShippingID = AgencyShowID.get(pos);
                    adapterAgencyOption = new ArrayAdapter<>(EditAddressCart.this, android.R.layout.simple_spinner_item, AgencyService.get(pos));
                    adapterAgencyOption.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    SpinnerAgencyOption.setAdapter(adapterAgencyOption);
                    ShippingCost.setText(AgencyPrice.get(pos).get(SpinnerAgencyOption.getSelectedItemPosition()));
                    if (Startup) {
                        for (int i = 0; i < AgencyServiceID.get(pos).size(); i++) {
                            if (AgencyServiceID.get(pos).get(i).equals(CurrSPid)) {
                                SpinnerAgencyOption.setSelection(i);
                            }
                        }
                        Startup = false;
                    }
                } else {
                    SpinnerAgencyOption.setAdapter(nullAdapterAgency);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }

        });

        SpinnerAgencyOption.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int pos, long arg3) {

                if (!SpinnerAgency.getAdapter().isEmpty()) {
                    if (AgencyServiceID.size() > 0) {
                        CurrSPid = AgencyServiceID.get(SpinnerAgency.getSelectedItemPosition()).get(pos);
                        ShippingCost.setText(AgencyPrice.get(SpinnerAgency.getSelectedItemPosition()).get(pos));
                        currentShowMapStatus = isShowMap.get(SpinnerAgency.getSelectedItemPosition()).get(pos);
                        if (isUsingCourierWithMap()) {
                            viewFieldLocation.setVisibility(View.VISIBLE);
                        } else {
                            viewFieldLocation.setVisibility(View.GONE);
                        }
                    }
                } else {
                    SpinnerAgencyOption.setAdapter(nullAdapterAgency);
                    CommonUtils.dumper("EditAddresscart None selected adapter");
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }

        });
        ChooseAddressBut.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(EditAddressCart.this, ChooseAddressActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(ChooseAddressActivity.REQUEST_CODE_PARAM_ADDRESS, CurrAddressID);
                intent.putExtras(bundle);
                startActivityForResult(intent, CHOOSE_ADDRESS);

            }

        });

        BuyBut.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                String params = ShopID + "~" + CurrAddressID + "~" + CurrShippingID + "~" + CurrSPid;
                System.out.println("editaddr clicked");
                valueLocation.setError(null);
                if (validateForm()) {
                    EditAddressWS4();
                }
            }

        });

        AddAddressBut.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(EditAddressCart.this, AddAddressActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("page", 1);
                bundle.putBoolean(ManageAddressConstant.IS_EDIT, false);
                intent.putExtras(bundle);
                startActivityForResult(intent, ManageAddressConstant.REQUEST_CODE_PARAM_CREATE);
            }
        });
        progressdialog.showDialog();
        getIntentExtras(getIntent().getExtras());
        AddressTitle.setText(addressName);
        Address.setText(addressStreet);
        RecalculateShippingAddressWS4();

//        RecalculateShippingAddress("calculate_shipping", CurrAddressID);
        viewAssignLocation.setOnClickListener(onChooseLocationClickListener());
        valueLocation.setOnClickListener(onChooseLocationClickListener());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isAlreadyReverseGeocode) {
            reverseGeoCodeByLatLong();
        }
    }

    void reverseGeoCodeByLatLong() {
        Observable<String> observable = Observable.just(true)
                .map(new Func1<Boolean, String>() {
                    @Override
                    public String call(Boolean aBoolean) {
                        return GeoLocationUtils.reverseGeoCode(EditAddressCart.this, latitude, longitude);
                    }
                });
        CompositeSubscription subscription = new CompositeSubscription();
        subscription.add(observable
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        isAlreadyReverseGeocode = false;
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(String s) {
                        valueLocation.setText(s);
                        addressGeoLocation = s;
                    }
                }));
    }

    private boolean validateForm() {
        valueLocation.setError(null);
        if (isUsingCourierWithMap() && !isLatLngExist()) {

            valueLocation.setError(getString(R.string.error_field_required));

            return false;
        } else if (!isDeliverySupported()) {
            valueLocation.setError(getString(R.string.error_field_required));
            CommonUtils.UniversalToast(this, "Alamat tidak didukung agen pengirim.");
            return false;
        } else {
            return true;
        }
    }

    private boolean isLatLngExist() {
        return !latitude.equals("") && !latitude.equals("");
    }

    private boolean isUsingCourierWithMap() {
        return currentShowMapStatus == 1;
    }

    private boolean isDeliverySupported() {
        return AgencyShow.size() > 0;
    }

    private void getIntentExtras(Bundle extras) {
        ShopID = getIntent().getExtras().getString("shop_id");
        OldAddressID = getIntent().getExtras().getString("addr_id");
        QuantityVal = getIntent().getExtras().getString("qty");
        Weight = getIntent().getExtras().getString("weight");
        OldShippingID = getIntent().getExtras().getString("shipping_id");
        OldSPid = getIntent().getExtras().getString("sp_id");
        CurrAddressID = OldAddressID;
        CurrShippingID = OldShippingID;
        CurrSPid = OldSPid;
        addressName = MethodChecker.fromHtml(extras.getString("address_title")).toString();
        addressStreet = MethodChecker.fromHtml(extras.getString("address")).toString();
        latitude = extras.getString("latitude");
        longitude = extras.getString("longitude");
    }

    private OnClickListener onChooseLocationClickListener() {
        return new OnClickListener() {
            @Override
            public void onClick(View view) {
                openGeolocation();
            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.add_to_cart, menu);
        return true;
    }

    public void RecalculateShippingAddress(String... params) {
        progressdialog = new TkpdProgressDialog(this, TkpdProgressDialog.NORMAL_PROGRESS);
        progressdialog.showDialog();
        NetworkHandler network = new NetworkHandler(EditAddressCart.this, TkpdUrl.CART);
        network.AddParam("shipping_agency", CurrShippingID);
        network.AddParam("shipping_product", CurrSPid);
        network.AddParam("addr_street", NewAddress);
        network.AddParam("addr_name", AddressType);
        network.AddParam("receiver_name", ReceiverName);
        network.AddParam("receiver_phone", ReceiverPhone);
        network.AddParam("province", Province);
        network.AddParam("district", SubDistrict);
        network.AddParam("city", Regency);
        network.AddParam("postal_code", PostCode);
        network.AddParam("address", params[1]);
        network.AddParam("min_order", QuantityVal);
        network.AddParam("shop_id", ShopID);
        network.AddParam("weight", Weight);
        network.AddParam("latitude", latitude);
        network.AddParam("longitude", longitude);
        network.AddParam("is_update_geolocation", isUpdateGeoLocation);
        network.AddParam("do", "calculate_address_shipping");
        network.AddParam("act", "recalculate_product_cart");
        network.Commit(new NetworkHandlerListener() {

            @Override
            public void onSuccess(Boolean status) {
                progressdialog.dismiss();

            }

            @Override
            public void getResponse(JSONObject Result) {
                try {
                    Logger.i("hangman editaddress", Result.toString());
                    AgencyPrice.clear();
                    AgencyService.clear();
                    AgencyServiceID.clear();
                    AgencyShow.clear();
                    PriceTotal.clear();
                    AgencyID.clear();
                    AgencyName.clear();
                    AgencyShowID.clear();
                    JSONObject Shipping = new JSONObject(Result.getString("shipping"));
                    JSONArray ShippingAgency = new JSONArray(Shipping.getString("shipping_agency"));
                    for (int i = 0; i < ShippingAgency.length(); i++) {
                        JSONObject ShippingData = new JSONObject(ShippingAgency.getString(i));
                        AgencyName.add(ShippingData.getString("option"));
                        AgencyID.add(ShippingData.getString("shipping_id"));
                    }


                    JSONObject ShippingProd = new JSONObject(Shipping.getString("shipping_product_android"));
                    for (int i = 0; i < AgencyID.size(); i++) {
                        ArrayList<String> data1 = new ArrayList<>();
                        ArrayList<String> data2 = new ArrayList<>();
                        ArrayList<String> data3 = new ArrayList<>();
                        ArrayList<String> data4 = new ArrayList<>();
                        JSONObject ShippingData = new JSONObject(ShippingProd.getString(AgencyID.get(i)));
                        @SuppressWarnings("rawtypes")
                        Iterator keys = ShippingData.keys();
                        while (keys.hasNext()) {
                            // loop to get the dynamic key
                            String currentDynamicKey = (String) keys.next();
                            JSONObject ShippingDeail = new JSONObject(ShippingData.getString(currentDynamicKey));
                            if (!ShippingDeail.getString("price").equals("0")) {
                                data1.add(ShippingDeail.getString("price"));
                                data2.add(ShippingDeail.getString("option"));
                                data4.add(ShippingDeail.getString("price_tot"));
                                data3.add(currentDynamicKey);
                            }
                        }
                        if (data1.size() != 0) {
                            AgencyShow.add(AgencyName.get(i));
                            AgencyPrice.add(data1);
                            AgencyService.add(data2);
                            AgencyServiceID.add(data3);
                            PriceTotal.add(data4);
                        }
                    }
                    for (int i = 0; i < AgencyShow.size(); i++) {
                        for (int k = 0; k < AgencyName.size(); k++) {
                            if (AgencyShow.get(i).equals(AgencyName.get(k))) {
                                AgencyShowID.add(AgencyID.get(k));
                            }
                        }

                    }
                    nullAdapterAgency = new ArrayAdapter<>(EditAddressCart.this, android.R.layout.simple_spinner_item, new String[]{NO_COURIER_AVAILABLE});
                    adapterAgency = new ArrayAdapter<>(EditAddressCart.this, android.R.layout.simple_spinner_item, AgencyShow);
                    adapterAgency.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    SpinnerAgency.setAdapter(adapterAgency);
                    if (AgencyShow.size() == 0) {
                        SpinnerAgency.setAdapter(nullAdapterAgency);
                    }
                    System.out.println(AgencyPrice);

                    if (isUsingCourierWithMap()) {
                        revertSpinner(CurrShippingID);
                    } else {
                        revertSpinner(OldShippingID);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void getMessageError(ArrayList<String> MessageError) {

            }

        });
    }

    public void RecalculateShippingAddressWS4() {
        if (!progressdialog.isProgress())
            progressdialog.showDialog();
        Map<String, String> maps = new HashMap<>();
        maps.put("address_id", CurrAddressID);
        maps.put("shop_id", ShopID);
        maps.put("weight", Weight);
        maps.put("do", "calculate_address_shipping");
        maps.put("recalculate", "1");
        interactor.calculateShipping(this, maps, new PaymentNetInteractor.OnCalculateShipping() {
            @Override
            public void onSuccess(CalculateShipping calculateShipping) {
                AgencyPrice.clear();
                AgencyService.clear();
                AgencyServiceID.clear();
                isShowMap.clear();
                AgencyShow.clear();
                PriceTotal.clear();
                AgencyID.clear();
                AgencyName.clear();
                AgencyShowID.clear();

                for (Shipment shipment : calculateShipping.getShipment()) {
                    if (shipment.getShipmentAvailable() == 0) {
                        continue;
                    }
                    AgencyName.add(shipment.getShipmentName());
                    AgencyID.add(shipment.getShipmentId());
                    ArrayList<String> data1 = new ArrayList<>();
                    ArrayList<String> data2 = new ArrayList<>();
                    ArrayList<String> data3 = new ArrayList<>();
                    ArrayList<String> data4 = new ArrayList<>();
                    ArrayList<Integer> isShowMap = new ArrayList<Integer>();
                    for (ShipmentPackage shipmentPackage : shipment.getShipmentPackage()) {
                        if (shipmentPackage.getPackageAvailable() == 0) {
                            continue;
                        }
                        data1.add(shipmentPackage.getPrice());
                        data2.add(shipmentPackage.getName());
                        data4.add(shipmentPackage.getPriceTotal());
                        data3.add(shipmentPackage.getSpId());
                        isShowMap.add(shipmentPackage.getShowMap());
                    }
                    if (data1.size() > 0) {
                        AgencyShowID.add(shipment.getShipmentId());
                        AgencyShow.add(shipment.getShipmentName());
                        AgencyPrice.add(data1);
                        AgencyService.add(data2);
                        AgencyServiceID.add(data3);
                        EditAddressCart.this.isShowMap.add(isShowMap);
                        PriceTotal.add(data4);
                    }
                }

                nullAdapterAgency = new ArrayAdapter<>(EditAddressCart.this, android.R.layout.simple_spinner_item, new String[]{NO_COURIER_AVAILABLE});
                nullAdapterAgency.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                adapterAgency = new ArrayAdapter<>(EditAddressCart.this, android.R.layout.simple_spinner_item, AgencyShow);
                adapterAgency.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SpinnerAgency.setAdapter(adapterAgency);
                if (AgencyShow.size() == 0) {
                    SpinnerAgency.setAdapter(nullAdapterAgency);
                }

                if (isUsingCourierWithMap()) {
                    revertSpinner(CurrShippingID);
                } else {
                    revertSpinner(OldShippingID);
                }
                container.setVisibility(View.VISIBLE);
                progressdialog.dismiss();
            }

            @Override
            public void onError(String message) {
                container.setVisibility(View.GONE);
                progressdialog.dismiss();
                NetworkErrorHelper.showEmptyState(EditAddressCart.this,
                        getWindow().getDecorView().getRootView(),
                        message,
                        new NetworkErrorHelper.RetryClickedListener() {
                            @Override
                            public void onRetryClicked() {
                                RecalculateShippingAddressWS4();
                            }
                        }
                );
            }

            @Override
            public void onNoConnection() {
                container.setVisibility(View.GONE);
                progressdialog.dismiss();
                NetworkErrorHelper.showEmptyState(EditAddressCart.this,
                        getWindow().getDecorView().getRootView(),
                        new NetworkErrorHelper.RetryClickedListener() {
                            @Override
                            public void onRetryClicked() {
                                RecalculateShippingAddressWS4();
                            }
                        }
                );
            }
        });
    }

    private void revertSpinner(String shippingID) {
        int pos = 0;
        for (int i = 0; i < AgencyShowID.size(); i++) {
            if (shippingID.equals(AgencyShowID.get(i))) {
                pos = i;
            }
        }
        CommonUtils.dumper(OldShippingID);
        CommonUtils.dumper(AgencyShowID);
        CommonUtils.dumper("POS NYA NEH: " + pos);
        try {
            SpinnerAgency.setSelection(pos);
        } catch (Exception e) {
            SpinnerAgency.setSelection(0);
        }
    }

    public void EditAddress() {
        progressdialog = new TkpdProgressDialog(this, TkpdProgressDialog.NORMAL_PROGRESS);
        progressdialog.showDialog();
        NetworkHandler network = new NetworkHandler(EditAddressCart.this, TkpdUrl.CART);
        network.AddParam("shipping_agency", CurrShippingID);
        network.AddParam("shipping_product", CurrSPid);
        network.AddParam("address", CurrAddressID);
        network.AddParam("shop_id", ShopID);
        network.AddParam("o_addr_id", OldAddressID);
        network.AddParam("o_ship_id", OldShippingID);
        network.AddParam("o_sp_id", OldSPid);
        network.AddParam("method", "POST");
        network.AddParam("act", "edit_address_shipping");
        network.AddParam("addr_street", NewAddress);
        network.AddParam("addr_name", AddressType);
        network.AddParam("receiver_name", ReceiverName);
        network.AddParam("receiver_phone", ReceiverPhone);
        network.AddParam("province", Province);
        network.AddParam("district", SubDistrict);
        network.AddParam("city", Regency);
        network.AddParam("postal_code", PostCode);
        network.Commit(new NetworkHandlerListener() {

            @Override
            public void onSuccess(Boolean status) {
                progressdialog.dismiss();

            }

            @Override
            public void getResponse(JSONObject Result) {
                if (!Result.isNull("success")) {
                    Bundle bundle = new Bundle();
                    bundle.putString("response", Result.toString());
                    Intent intent = getIntent();
                    intent.putExtras(bundle);
                    setResult(RESULT_OK, intent);
                    finish();
                }

            }

            @Override
            public void getMessageError(ArrayList<String> MessageError) {

            }
        });
    }

    public void EditAddressWS4() {

        progressdialog = new TkpdProgressDialog(this, TkpdProgressDialog.NORMAL_PROGRESS);
        progressdialog.showDialog();
        Map<String, String> maps = new HashMap<>();
        maps.put("address_id", CurrAddressID);
        maps.put("address_name", null);
        maps.put("address_street", null);
        maps.put("city_id", null);
        maps.put("district_id", null);
        maps.put("old_address_id", OldAddressID);
        maps.put("old_shipment_id", OldShippingID);
        maps.put("old_shipment_package_id", OldSPid);
        maps.put("postal_code", null);
        maps.put("province_id", null);
        maps.put("receiver_name", null);
        maps.put("receiver_phone", null);
        maps.put("shipment_id", CurrShippingID);
        maps.put("shipment_package_id", CurrSPid);
        maps.put("shop_id", ShopID);

        interactor.editAddress(this, maps, new PaymentNetInteractor.OnEditAddress() {
                    @Override
                    public void onSuccess(String message) {
                        NetworkErrorHelper.showSnackbar(EditAddressCart.this,
                                message);
                        Bundle bundle = new Bundle();
                        bundle.putString("response", message);
                        Intent intent = getIntent();
                        intent.putExtras(bundle);
                        setResult(RESULT_OK, intent);
                        finish();
                    }

                    @Override
                    public void onError(String message) {
                        NetworkErrorHelper.showSnackbar(EditAddressCart.this,
                                message);
                        progressdialog.dismiss();
                    }

                    @Override
                    public void onNoConnection() {
                        progressdialog.dismiss();
                    }
                }

        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null && resultCode == RESULT_OK) {
            switch (requestCode) {
                case CHOOSE_ADDRESS:
                    onSuccessSelectAddress(data.getExtras());
                    break;
                case ManageAddressConstant.REQUEST_CODE_PARAM_CREATE:
                    onSuccessSelectAddress(data.getExtras());
                    break;
                case CHOOSE_LOCATION:
                    getNewLocation(data.getExtras());
                    break;
            }
        }
    }

    private void onSuccessSelectAddress(Bundle bundle) {
        Destination temp = bundle.getParcelable(ManageAddressConstant.EXTRA_ADDRESS);
        addressStreet = temp.getAddressDetail();
        addressName = temp.getAddressName();
        CurrAddressID = temp.getAddressId();
        latitude = temp.getLatitude();
        longitude = temp.getLongitude();
        addressGeoLocation = GeoLocationUtils.reverseGeoCode(this, latitude, longitude);
        valueLocation.setText(addressGeoLocation);
        AddressTitle.setText(MethodChecker.fromHtml(addressName));
        Address.setText(MethodChecker.fromHtml(addressStreet));
        addressStreet = temp.getAddressStreet();
        ReceiverName = temp.getReceiverName();
        ReceiverPhone = temp.getReceiverPhone();
        RecalculateShippingAddressWS4();
    }

    private void getNewAddress(Bundle extras) {
        ReceiverName = "";
        ReceiverPhone = "";
        Province = "";
        Regency = "";
        SubDistrict = "";
        AddressType = "";
        NewAddress = "";
        PostCode = "";
        latitude = extras.getString("latitude");
        longitude = extras.getString("longitude");
        valueLocation.setText(extras.getString("full_address", ""));
        AddressTitle.setText(MethodChecker.fromHtml(extras.getString("address_title")));
        Address.setText(MethodChecker.fromHtml(extras.getString("address_detail")));
        CurrAddressID = extras.getString("address_id");
        RecalculateShippingAddressWS4();
    }

    private void getNewLocation(Bundle bundle) {
        LocationPass locationPass = bundle.getParcelable(GeolocationActivity.EXTRA_EXISTING_LOCATION);
        if (locationPass != null) {
            isUpdateGeoLocation = 1;
            latitude = locationPass.getLatitude();
            longitude = locationPass.getLongitude();
            if (locationPass.getGeneratedAddress().equals(getString(R.string.choose_this_location))) {
                addressGeoLocation = latitude + ", " + longitude;
            } else {
                addressGeoLocation = locationPass.getGeneratedAddress();
            }
            valueLocation.setText(addressGeoLocation);
        }
        saveNewLocation();
    }

    private void saveNewLocation() {

        Map<String, String> maps = new HashMap<>();
        maps.put("act", "edit_address");
        maps.put("is_from_cart", "1");
        maps.put("address_id", CurrAddressID);
        maps.put("latitude", latitude);
        maps.put("longitude", longitude);
        interactor.saveNewLocation(this, maps, new PaymentNetInteractor.OnSaveNewLocation() {
            @Override
            public void onSuccess(String message) {
                progressdialog.dismiss();
                RecalculateShippingAddressWS4();
            }

            @Override
            public void onError(String message) {
                progressdialog.dismiss();
                NetworkErrorHelper.showSnackbar(EditAddressCart.this,
                        message
                );
            }

            @Override
            public void onNoConnection() {
                progressdialog.dismiss();
                NetworkErrorHelper.showSnackbar(EditAddressCart.this);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void openGeolocation() {
        GoogleApiAvailability availability = GoogleApiAvailability.getInstance();

        int resultCode = availability.isGooglePlayServicesAvailable(this);

        if (ConnectionResult.SUCCESS == resultCode) {
            LocationPass locationPass = null;
            if (!latitude.isEmpty() && !longitude.isEmpty()) {
                locationPass = new LocationPass();
                locationPass.setLatitude(latitude);
                locationPass.setLongitude(longitude);
                locationPass.setGeneratedAddress(addressGeoLocation);
                locationPass.setManualAddress(addressStreet);
            }
            Intent intent = GeolocationActivity.createInstance(this, locationPass);
            startActivityForResult(intent, CHOOSE_LOCATION);
        } else {
            CommonUtils.dumper("Google play services unavailable");
            Dialog dialog = availability.getErrorDialog(this, resultCode, 0);
            dialog.show();
        }
    }
}
