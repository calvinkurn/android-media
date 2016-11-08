package com.tokopedia.core.shipping.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.ImageHandler;
import com.tkpd.library.utils.SimpleSpinnerAdapter;
import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.app.V2BaseFragment;
import com.tokopedia.core.geolocation.activity.GeolocationActivity;
import com.tokopedia.core.geolocation.model.LocationPass;
import com.tokopedia.core.geolocation.utils.GeoLocationUtils;
import com.tokopedia.core.network.NetworkHandler;
import com.tokopedia.core.shipping.model.EditShippingModel;
import com.tokopedia.core.shipping.presenter.EditShippingInterface;
import com.tokopedia.core.shipping.presenter.EditShippingViewInterface;
import com.tokopedia.core.shipping.presenter.ShopShippingImpl;
import com.tokopedia.core.util.ToolTipUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Kris on 11/6/2015.
 */
public class FragmentEditShippingOld extends V2BaseFragment implements EditShippingViewInterface{

    private EditShippingInterface shippingPresenter;
    private ArrayList<Integer> checkBoxIdList = new ArrayList<>();
    private View loadingView;
    private TkpdProgressDialog progressDialog;
    private int viewStep;
    private AtomicInteger nextGeneratedId;
    private boolean oldAndroidVersion;

    @Override
    public void GetCourierData(ArrayList<EditShippingModel.CourierAttribute> courierAttributes, EditShippingModel.ParamEditShop paramEditShop) {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for (int i = 0; i < courierAttributes.size(); i++) {
            courierViewHolder courierHolder = new courierViewHolder();
            View courierView = inflater.inflate(R.layout.shipping_courier_adapter_old, (ViewGroup) getRootView(), false);
            setCourierView(courierHolder, courierView);
            getCourierValue(courierHolder, courierAttributes.get(i));
            holder.fragmentShippingMainLayout.addView(courierView);
            setCheckListView(courierAttributes.get(i).packageAttributes, courierHolder);
            setShippingDetailedOptions(courierAttributes.get(i), courierHolder.shippingSettings, paramEditShop, courierAttributes.get(i).ShippingMaxAddFee);
        }
        holder.phoneNumberTitle.setVisibility(View.VISIBLE);
        holder.phoneNumber.setVisibility(View.VISIBLE);
        holder.phoneNumberButton.setVisibility(View.VISIBLE);
        holder.phoneNumber.setText(paramEditShop.phoneNumber);
        showMainView();
        ((ViewGroup) getRootView()).removeView(loadingView);
    }

    @Override
    public void PrepareCourierData(ArrayList<EditShippingModel.CourierAttribute> courierAttributes, EditShippingModel.ParamEditShop paramEditShop) {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for (int i = 0; i < courierAttributes.size(); i++) {
            courierViewHolder courierHolder = new courierViewHolder();
            View courierView = inflater.inflate(R.layout.shipping_courier_adapter_old, (ViewGroup) getRootView(), false);
            setCourierView(courierHolder, courierView);
            getCourierValue(courierHolder, courierAttributes.get(i));
            holder.fragmentShippingMainLayout.addView(courierView);
            tickAllCheckBoxes(courierAttributes.get(i).packageAttributes, courierHolder);
            setShippingDetailedOptions(courierAttributes.get(i), courierHolder.shippingSettings, paramEditShop, courierAttributes.get(i).ShippingMaxAddFee);
        }
        holder.fragmentShippingMainLayout.setVisibility(View.GONE);
        showMainView();
        progressDialog.dismiss();
    }

    @Override
    public Context getMainContext() {
        return getActivity().getApplicationContext();
    }

    @Override
    public void updateOptionsMenu() {

    }

    @Override
    public void fillProvinceSpinner(ArrayList<String> provinceList) {
        holder.provinceSpinnerAdapter = SimpleSpinnerAdapter.createAdapter(getActivity(), provinceList);
        holder.provinceSpinner.setAdapter(holder.provinceSpinnerAdapter);
    }

    @Override
    public void fillCitySpinner(ArrayList<String> cityList) {
        holder.citySpinnerAdapter = SimpleSpinnerAdapter.createAdapter(getActivity(), cityList);
        holder.citySpinner.setAdapter(holder.citySpinnerAdapter);
    }

    @Override
    public void fillDistrictSpinner(ArrayList<String> districtList) {
        holder.districtSpinnerAdapter = SimpleSpinnerAdapter.createAdapter(getActivity(), districtList);
        holder.districtSpinner.setAdapter(holder.districtSpinnerAdapter);
    }

    @Override
    public void setProvinceSpinnerSelection(int selectedProvince) {
        holder.provinceSpinner.setSelection(selectedProvince);
    }

    @Override
    public void setCitySpinnerSelection(int selectedCity) {
        holder.citySpinner.setSelection(selectedCity);
    }

    @Override
    public void setDistrictSpinnerSelection(int selectedDistrict) {
        holder.districtSpinner.setSelection(selectedDistrict);
    }

    @Override
    public void setCitySpinnerVisibility(int visibility) {
        holder.citySpinner.setVisibility(visibility);
    }

    @Override
    public void setDistrictSpinnerVisibility(int visibility) {
        holder.districtSpinner.setVisibility(visibility);
    }

    @Override
    public void setZipCode(String zipCode) {
        holder.zipCodeArea.setText(zipCode);
    }


    @Override
    public void setShopAddress(String shopAddress) {
        holder.addressArea.setText(shopAddress);
    }

    @Override
    public void finishActivity(String finishMessage) {
        Toast.makeText(getActivity().getApplicationContext(), finishMessage, Toast.LENGTH_LONG).show();
        getActivity().finish();
    }

    @Override
    public void setCourierVisibility(int position, boolean visible) {
        if(visible){
            holder.fragmentShippingMainLayout.getChildAt(position).setVisibility(View.VISIBLE);
        }else{
            holder.fragmentShippingMainLayout.getChildAt(position).setVisibility(View.GONE);
        }
    }

    @Override
    public int currentDistrictSpinnerPosition() {
        return holder.districtSpinner.getSelectedItemPosition();
    }

    @Override
    public int currentCitySpinnerPosition() {
        return holder.citySpinner.getSelectedItemPosition();
    }

    @Override
    public int currentProvinceSpinnerPosition() {
        return holder.provinceSpinner.getSelectedItemPosition();
    }

    @Override
    public String getZipCode() {
        return holder.zipCodeArea.getText().toString();
    }

    @Override
    public String getShopGoogleMapAddress() {
        return holder.chooseLocation.getText().toString();
    }

    @Override
    public String getStreetAddress() {
        return holder.addressArea.getText().toString();
    }

    @Override
    public void noPackageTicked() {
        Toast.makeText(getActivity(), getActivity().getString(R.string.error_shipping_must_choose), Toast.LENGTH_LONG).show();
        progressDialog.dismiss();
    }

    @Override
    public void locationIsNotChosen() {
        holder.chooseLocation.setError(getActivity().getString(R.string.error_choose_google_map_location));
        holder.addressLayout.requestFocus();
        if(progressDialog.isProgress()){
            Toast.makeText(getActivity(), getActivity().getString(R.string.error_choose_google_map_location), Toast.LENGTH_LONG).show();
        }
        progressDialog.dismiss();
    }

    @Override
    public void addressNotFilled() {
        holder.addressArea.setError(getActivity().getString(R.string.error_fill_street_address));
        holder.addressArea.requestFocus();
        progressDialog.dismiss();
    }

    @Override
    public void finishLoading() {
        progressDialog.dismiss();
    }

    @Override
    public void showError(String error) {
        Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void setSpinnerListener() {
        holder.provinceSpinner.setOnItemSelectedListener(onProvinceSpinnerChosen());
        holder.citySpinner.setOnItemSelectedListener(onCitySpinnerChosen());
        holder.districtSpinner.setOnItemSelectedListener(onDistrictSpinnerChosenListener());
    }

    @Override
    public boolean chooseLocationDisplayed() {
        return holder.addressLayout.isShown();
    }

    @Override
    public void setLocationOptionVisibility(int visibilty) {
        holder.addressLayout.setVisibility(visibilty);
    }

    @Override
    public void setGoogleMapAddress(String longitude, String latitude) {
        holder.chooseLocation.setText(GeoLocationUtils.reverseGeoCode(getActivity(), latitude, longitude));
    }

    @Override
    public void setCourierOptionsVisibility(int visibility) {
        holder.fragmentShippingMainLayout.setVisibility(visibility);
    }

    private void setCheckListView(List<EditShippingModel.PackageAttribute> attributeList, courierViewHolder courierHolder){
        for(int j = 0; j < attributeList.size(); j++){
            CheckBox courierPackage = new CheckBox(getActivity());
            courierPackage.setChecked(attributeList.get(j).ShippingChecked);
            setCheckBoxAttribute(attributeList.get(j), courierHolder, courierPackage);
        }
    }

    private void tickAllCheckBoxes(List<EditShippingModel.PackageAttribute> attributeList, courierViewHolder courierHolder){
        for(int j = 0; j < attributeList.size(); j++){
            CheckBox courierPackage = new CheckBox(getActivity());
            courierPackage.setChecked(true);
            setCheckBoxAttribute(attributeList.get(j), courierHolder, courierPackage);
        }
    }

    private void setCheckBoxAttribute(EditShippingModel.PackageAttribute packageAttribute, courierViewHolder courierHolder, CheckBox courierPackage){
        courierPackage.setText(packageAttribute.ShippingPackage);
        courierPackage.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.info_icon, 0);
        courierPackage.setOnTouchListener(onInfoTouchListener(courierPackage, packageAttribute.ShippingDescription));
        courierHolder.childrenLayout.addView(courierPackage);
        //int checkBoxId = View.generateViewId();
        int checkBoxId = setCheckBoxId();
        courierPackage.setId(checkBoxId);
        setAdvanceOptionsRequirement(courierPackage, packageAttribute.ShippingPackageID);
        checkBoxIdList.add(checkBoxId);
    }

    private int setCheckBoxId(){
        if(oldAndroidVersion){
            return manuallyGenerateViewId();
        }else {
            return View.generateViewId();
        }
    }

    private class viewHolder{
        LinearLayout fragmentShippingHeader;
        LinearLayout fragmentShippingMainLayout;
        RelativeLayout addressLayout;
        Spinner provinceSpinner;
        Spinner citySpinner;
        Spinner districtSpinner;
        SimpleSpinnerAdapter provinceSpinnerAdapter;
        SimpleSpinnerAdapter citySpinnerAdapter;
        SimpleSpinnerAdapter districtSpinnerAdapter;
        EditText zipCodeArea;
        EditText addressArea;
        EditText chooseLocation;
        TextView titleEditShippingSegment;
        TextView phoneNumber;
        TextView phoneNumberTitle;
        TextView phoneNumberButton;
        View jneSettingsButton;
        View tikiSettingsButton;
        View posSettingsButton;

        //TODO RPX NEEEEEH BRO
        CheckBox rpxNextDayCheckBox;
        CheckBox rpxRegulerCheckBox;
        CheckBox rpxIDropCheckBox;
    }

    private class courierViewHolder{
        TextView courierName;
        ImageView courierImage;
        LinearLayout childrenLayout;
        LinearLayout shippingSettings;
        TextView shippingWeightNotice;
    }

    private void setCourierView(courierViewHolder holder, View courierView){
        holder.courierName = (TextView) courierView.findViewById(R.id.name);
        holder.courierImage = (ImageView) courierView.findViewById(R.id.img_courier);
        holder.childrenLayout = (LinearLayout) courierView.findViewById(R.id.children_layout);
        holder.shippingSettings = (LinearLayout) courierView.findViewById(R.id.shipping_settings);
        holder.shippingWeightNotice = (TextView)courierView.findViewById(R.id.weight_info);
    }

    private void getCourierValue(courierViewHolder courierHolder, EditShippingModel.CourierAttribute courierAttribute){
        courierHolder.courierName.setText(courierAttribute.ShippingName);
        ImageHandler.LoadImage(courierHolder.courierImage, courierAttribute.ShippingImageUri);
        courierHolder.shippingWeightNotice.setText(courierAttribute.ShippingWeightNotice);
        if(courierAttribute.ShippingWeightNotice.length()<3){
            ((LinearLayout)courierHolder.shippingWeightNotice.getParent()).removeView(courierHolder.shippingWeightNotice);
        }
    }

    public static FragmentEditShippingOld createInstance(){
        FragmentEditShippingOld fragment = new FragmentEditShippingOld();
        Bundle bundle = new Bundle();
        bundle.putBoolean(CREATE_SHOP_KEY, false);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static FragmentEditShippingOld createShopInstance(){
        FragmentEditShippingOld fragment = new FragmentEditShippingOld();
        Bundle bundle = new Bundle();
        bundle.putBoolean(CREATE_SHOP_KEY, true);
        fragment.setArguments(bundle);
        return fragment;
    }

    private viewHolder holder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        shippingPresenter = new ShopShippingImpl(this);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        nextGeneratedId = new AtomicInteger(1);
        oldAndroidVersion = checkAndroidVersion();
        super.onCreate(savedInstanceState);
    }

    private boolean checkAndroidVersion(){
        return Build.VERSION.SDK_INT < 17;
    }

    @Override
    protected int getRootViewId() {
        return R.layout.fragment_shop_shipping_old;
    }

    @Override
    protected void onCreateView() {
        viewStep = 0;
        progressDialog = new TkpdProgressDialog(getActivity(), TkpdProgressDialog.NORMAL_PROGRESS);
        holder.fragmentShippingHeader.setVisibility(View.INVISIBLE);
        if(getArguments().getBoolean(CREATE_SHOP_KEY)){
            progressDialog.showDialog();
            shippingPresenter.prepareCourierSelections();
            setHasOptionsMenu(false);
        }
        else {
            addLoadingView();
            shippingPresenter.fetchData();
            setHasOptionsMenu(true);
            holder.titleEditShippingSegment.setVisibility(View.GONE);
        }
    }

    @Override
    protected Object getHolder() {
        return holder;
    }

    @Override
    protected void setHolder(Object holder) {
        this.holder = (viewHolder) holder;
    }

    @Override
    protected void initView() {
        holder = new viewHolder();
        holder.fragmentShippingMainLayout = (LinearLayout) findViewById(R.id.fragment_shipping_main_layout);
        holder.fragmentShippingHeader = (LinearLayout) findViewById(R.id.fragment_shipping_header);
        holder.provinceSpinner = (Spinner) findViewById(R.id.province_spinner);
        holder.citySpinner = (Spinner) findViewById(R.id.city_spinner);
        holder.districtSpinner = (Spinner) findViewById(R.id.district_spinner);
        holder.zipCodeArea = (EditText) findViewById(R.id.postal_code);
        holder.addressArea = (EditText) findViewById(R.id.address_text_field);
        holder.chooseLocation = (EditText)findViewById(R.id.value_location);
        holder.titleEditShippingSegment = (TextView) findViewById(R.id.title_edit_shipping_segment);
        holder.phoneNumber = (TextView) findViewById(R.id.shop_phone_number);
        holder.phoneNumberTitle = (TextView) findViewById(R.id.shop_phone_number_title);
        holder.phoneNumberButton = (TextView) findViewById(R.id.change_phone_number_button);
        holder.addressLayout = (RelativeLayout) findViewById(R.id.shipping_address_layout);
    }

    @Override
    protected void setListener() {
        holder.phoneNumberButton.setOnClickListener(changePhoneNumberClickedListener());
        holder.chooseLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGeolocation();
            }
        });
    }


    private ArrayList<Boolean> checkAllCheckbox(){
        ArrayList<Boolean> booleanList = new ArrayList<>();
        for(int i = 0 ; i<checkBoxIdList.size(); i++){
            booleanList.add(((CheckBox) getRootView().findViewById(checkBoxIdList.get(i))).isChecked());
        }
        return booleanList;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.save_btn, menu);
        MenuItem item = menu.findItem(R.id.action_send);
        item.setTitle(getString(R.string.title_action_save_shipping));
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                getActivity().finish();
                return true;
            case R2.id.action_send:
                submitData();
        }
        return super.onOptionsItemSelected(item);
    }

    private void setShippingDetailedOptions(EditShippingModel.CourierAttribute courierAttribute, View settingButton, EditShippingModel.ParamEditShop shop, int maximumFee){
        if(courierAttribute.ShippingName.toLowerCase().contains("jne")){
            holder.jneSettingsButton = settingButton;
            settingButton.setOnClickListener(jneOnClickListener(shop, maximumFee));
        }else if(courierAttribute.ShippingName.toLowerCase().contains("tiki")){
            holder.tikiSettingsButton = settingButton;
            settingButton.setOnClickListener(tikiOnClickListener(shop, maximumFee));
        }else if(courierAttribute.ShippingName.toLowerCase().contains("pos")){
            holder.posSettingsButton = settingButton;
            settingButton.setOnClickListener(posOnClickListener(shop, maximumFee));
        }else{
            ((LinearLayout)settingButton.getParent()).removeView(settingButton);
        }
    }

    private View.OnClickListener jneOnClickListener(final EditShippingModel.ParamEditShop shopParameters, final int maximumAdditionalFee){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getActivity().getFragmentManager();
                ShippingDetailDialog shippingOptionDialog = ShippingDetailDialog.createJNEDetailDialog(shopParameters, maximumAdditionalFee);
                shippingOptionDialog.setTargetFragment(FragmentEditShippingOld.this, JNE_CODE);
                shippingOptionDialog.show(fm, "jne_detail");
            }
        };
    }

    private View.OnClickListener tikiOnClickListener(final EditShippingModel.ParamEditShop shopParameters, final int maximumAdditionalFee){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getActivity().getFragmentManager();
                ShippingDetailDialog shippingOptionDialog = ShippingDetailDialog.createTikiDetailDialog(shopParameters, maximumAdditionalFee);
                shippingOptionDialog.setTargetFragment(FragmentEditShippingOld.this, TIKI_CODE);
                shippingOptionDialog.show(fm, "tiki_detail");
            }
        };
    }

    private View.OnClickListener posOnClickListener(final EditShippingModel.ParamEditShop shopParameters, final int maximumAdditionalFee){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getActivity().getFragmentManager();
                ShippingDetailDialog shippingOptionDialog = ShippingDetailDialog.createPosDetailDialog(shopParameters, maximumAdditionalFee);
                shippingOptionDialog.setTargetFragment(FragmentEditShippingOld.this, POS_CODE);
                shippingOptionDialog.show(fm, "pos_detail");
            }
        };
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            shippingPresenter.additionalOptionChanged();
            switch (requestCode){
                case JNE_CODE:
                    holder.jneSettingsButton.setOnClickListener(jneOnClickListener(shippingPresenter.getUpdatedShopParameters(), data.getIntExtra(MAXIMUM_FEE, 5000)));
                    break;
                case TIKI_CODE:
                    holder.tikiSettingsButton.setOnClickListener(tikiOnClickListener(shippingPresenter.getUpdatedShopParameters(), data.getIntExtra(MAXIMUM_FEE, 5000)));
                    break;
                case POS_CODE:
                    holder.posSettingsButton.setOnClickListener(posOnClickListener(shippingPresenter.getUpdatedShopParameters(), data.getIntExtra(MAXIMUM_FEE, 5000)));
                    break;
                case MAP_CODE:
                    //TODO DUMMY KEY LONGITUDE & LATITUDE
                    Bundle bundle = data.getExtras();
                    LocationPass locationPass = bundle.getParcelable(GeolocationActivity.EXTRA_EXISTING_LOCATION);
                    if (locationPass != null) {
                        shippingPresenter.setMapPosition(locationPass.getLongitude(), locationPass.getLatitude());
                        holder.chooseLocation.setText(getReverseGeocode(locationPass));
                        holder.chooseLocation.requestFocus();
                    }
                    break;
            }
            if(requestCode != MAP_CODE)
                shippingPresenter.updateShopParameters(data.getParcelableExtra(SHOP_PARAMS_KEY));
        }
    }

    private String getReverseGeocode(LocationPass locationPass) {
        if (locationPass.getGeneratedAddress().equals(getActivity().getString(R.string.choose_this_location))) {
            return locationPass.getLatitude() + ", " + locationPass.getLongitude();
        } else {
            return locationPass.getGeneratedAddress();
        }
    }

    public void AddCourierParams(NetworkHandler network){
        holder.chooseLocation.setError(null);
        holder.addressArea.setError(null);
        shippingPresenter.requestCourierParameters(checkAllCheckbox(), network);
    }

    public boolean courierViewShown(){
        return holder.fragmentShippingHeader.isShown();
    }

    AdapterView.OnItemSelectedListener onDistrictSpinnerChosenListener(){
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int districtSpinnerPosition, long l) {
                if(viewStep>2){
                    shippingPresenter.districtSpinnerChanged(holder.provinceSpinner.getSelectedItemPosition(),
                            holder.citySpinner.getSelectedItemPosition(),
                            districtSpinnerPosition);
                }else if(viewStep==2){
                    viewStep ++;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        };
    }


    AdapterView.OnItemSelectedListener onProvinceSpinnerChosen(){
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int provinceSpinnerPosition, long l) {
                if(viewStep>0){
                    shippingPresenter.provinceSpinnerChanged(provinceSpinnerPosition);
                }else if(viewStep ==0){
                    viewStep++;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        };
    }

    AdapterView.OnItemSelectedListener onCitySpinnerChosen(){
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int citySpinner, long l) {
                if(viewStep>1){
                    shippingPresenter.citySpinnerChanged(citySpinner, holder.provinceSpinner.getSelectedItemPosition());
                }else if(viewStep==1){
                    viewStep++;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        };
    }

    private void submitData(){
        holder.chooseLocation.setError(null);
        holder.addressArea.setError(null);
        if(courierViewShown() && allDataValidated()){
            progressDialog.showDialog();
            shippingPresenter.sendData(checkAllCheckbox());
        }
    }

    private void setAdvanceOptionsRequirement(CheckBox courierPackage, int packageID){
        if(courierPackage.getText().toString().toLowerCase().contains("oke")){
            shippingPresenter.getOkeActivatedProperties(courierPackage.isChecked());
            courierPackage.setOnCheckedChangeListener(onOkeButtonClickedListener());
        }

        //TODO RPX LAGI NYEEEEEET
        if(packageID == 999 && shippingPresenter.getWhiteListStatus().equals("1")){
            courierPackage.setOnCheckedChangeListener(onRPXNextDayChanged());
            holder.rpxNextDayCheckBox = courierPackage;
        }
        if(packageID == 998 && shippingPresenter.getWhiteListStatus().equals("1")){
            courierPackage.setOnCheckedChangeListener(onRPXRegulerChanged());
            holder.rpxRegulerCheckBox = courierPackage;
        }
        if(packageID == 997 && shippingPresenter.getWhiteListStatus().equals("1")){
            courierPackage.setOnCheckedChangeListener(onIDropChecked());
            holder.rpxIDropCheckBox = courierPackage;
        }
    }

    private CompoundButton.OnCheckedChangeListener onOkeButtonClickedListener(){
        return new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checkBoxState) {
                shippingPresenter.setOkeActivatedProperties(checkBoxState);
            }
        };
    }

    public boolean allDataValidated(){
        boolean isJakarta = holder.provinceSpinnerAdapter.getItem(holder.provinceSpinner.getSelectedItemPosition()).contains("Jakarta");
        if (holder.zipCodeArea.getText().toString().isEmpty()){
            holder.zipCodeArea.setError(getActivity().getString(R.string.error_field_required));
            holder.zipCodeArea.requestFocus();
            return false;
        }else if(!isJakarta && holder.provinceSpinner.getSelectedItemPosition() == 0){
            Toast.makeText(getActivity(), getActivity().getString(R.string.title_select_province), Toast.LENGTH_LONG).show();
            return false;
        }else if(!isJakarta && holder.citySpinner.getSelectedItemPosition() == 0){
            Toast.makeText(getActivity(), getActivity().getString(R.string.title_select_city), Toast.LENGTH_LONG).show();
            return false;
        }else if(!isJakarta && holder.districtSpinner.getSelectedItemPosition() == 0){
            Toast.makeText(getActivity(), getActivity().getString(R.string.title_select_district), Toast.LENGTH_LONG).show();
            holder.districtSpinner.requestFocus();
            return false;
        }else{
            return true;
        }
    }

    private void addLoadingView(){
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        loadingView = inflater.inflate(R.layout.loader, (ViewGroup) getRootView(), false);
//        ImageView logo = (ImageView) loadingView.findViewById(R.id.logo);
//        logo.setBackgroundResource(R.drawable.loading_droid);
//        AnimationDrawable logoAnimation = (AnimationDrawable) logo.getBackground();
//        logoAnimation.start();

        ((ViewGroup) getRootView()).addView(loadingView);
    }

    private void showMainView(){
        holder.fragmentShippingHeader.setVisibility(View.VISIBLE);
        holder.addressLayout.setVisibility(View.VISIBLE);
    }

    private View.OnTouchListener onInfoTouchListener(final CheckBox courierPackageCheckBox, final String info){
        return new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                    if(motionEvent.getRawX() >= courierPackageCheckBox.getRight() - courierPackageCheckBox.getTotalPaddingRight()){
                        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                        dialog.setMessage(info);
                        dialog.setNeutralButton(getActivity().getString(R.string.dialog_close), null);
                        dialog.show();
                        return true;
                    }
                }
                return false;
            }
        };
    }

    private View.OnClickListener changePhoneNumberClickedListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                dialog.setMessage(getActivity().getString(R.string.change_phone_number_restriction));
                dialog.setNeutralButton(getActivity().getString(R.string.dialog_close), null);
                dialog.show();
            }
        };
    }

    private View setViewTooltip(final String info){
        return ToolTipUtils.setToolTip(getActivity(), R.layout.view_tooltip_package, new ToolTipUtils.ToolTipListener() {
            @Override
            public void setView(View view) {
                TextView infoTextView = (TextView) view.findViewById(R.id.package_info_test);
                infoTextView.setText(info);
            }

            @Override
            public void setListener() {

            }
        });
    }

    public int manuallyGenerateViewId() {
        for (; ; ) {
            final int result = nextGeneratedId.get();

            // aapt-generated IDs have the high byte nonzero; clamp to the range under that.
            int newValue = result + 1;
            if (newValue > 0x00FFFFFF) newValue = 1; // Roll over to 1, not 0.

            if (nextGeneratedId.compareAndSet(result, newValue)) {
                return result;
            }
        }
    }

    //TODO RPX lagi RPX lagi
    private CompoundButton.OnCheckedChangeListener onRPXNextDayChanged(){
        return new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checkBoxChecked) {
                holder.rpxIDropCheckBox.setChecked(!checkBoxChecked);
                Toast.makeText(getActivity(), getActivity().getString(R.string.toggle_rpx_next_day_i_drop), Toast.LENGTH_SHORT).show();
            }
        };
    }

    private CompoundButton.OnCheckedChangeListener onRPXRegulerChanged(){
        return new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checkBoxChecked) {
                if(!checkBoxChecked){
                    holder.rpxIDropCheckBox.setChecked(false);
                }
            }
        };
    }

    private CompoundButton.OnCheckedChangeListener onIDropChecked(){
        return new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checkBoxChecked) {
                holder.rpxNextDayCheckBox.setChecked(!checkBoxChecked);
                Toast.makeText(getActivity(), getActivity().getString(R.string.toggle_rpx_next_day_i_drop), Toast.LENGTH_SHORT).show();
            }
        };
    }

    public void openGeolocation() {
        GoogleApiAvailability availability = GoogleApiAvailability.getInstance();

        int resultCode = availability.isGooglePlayServicesAvailable(getActivity());

        if (ConnectionResult.SUCCESS == resultCode) {
            LocationPass locationPass = null;
            if (!shippingPresenter.getMapLatitude().isEmpty() && !shippingPresenter.getMapLongitude().isEmpty()) {
                locationPass = new LocationPass();
                locationPass.setLatitude(shippingPresenter.getMapLatitude());
                locationPass.setLongitude(shippingPresenter.getMapLongitude());
                locationPass.setGeneratedAddress(holder.chooseLocation.getText().toString());
            }
            Intent intent = GeolocationActivity.createInstance(getActivity(), locationPass);
            startActivityForResult(intent, MAP_CODE);
        } else {
            CommonUtils.dumper("Google play services unavailable");
            Dialog dialog = availability.getErrorDialog(getActivity(), resultCode, 0);
            dialog.show();
        }
    }
}
