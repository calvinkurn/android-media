package com.tokopedia.core.shipping.presenter;


import android.os.Parcelable;
import android.view.View;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.R;
import com.tokopedia.core.network.NetworkHandler;
import com.tokopedia.core.shipping.model.EditShippingModel;
import com.tokopedia.core.var.TkpdUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kris on 11/6/2015.
 */
public class ShopShippingImpl implements EditShippingInterface{

    EditShippingViewInterface view;
    private ArrayList<EditShippingModel.CourierAttribute> courierModel;
    private EditShippingModel.ParamEditShop shopParameter;
    private ArrayList<EditShippingModel.ProvinceAttribute> provinceModelList;
    private ArrayList<String> provinceNamesList;
    private boolean additionalOptionChanged;
    private boolean provinceChanged;
    private boolean districtChanged;
    private ArrayList<String> checkedCourierName = new ArrayList<>();
    private ArrayList<String> checkedCourierID = new ArrayList<>();
    private ArrayList<Integer> instantCourierID = new ArrayList<>();
    private ArrayList<Boolean> originallyCheckedPackage = new ArrayList<>();
    private ArrayList<Boolean> checkedPackage = new ArrayList<>();

    public ShopShippingImpl(EditShippingViewInterface view){
        this.view = view;
    }


    @Override
    public void fetchData() {
        RequestShippingData();
    }

    @Override
    public void prepareCourierSelections() {
        prepareShippingData();
    }

    @Override
    public void sendData(ArrayList<Boolean> checkBoxStatus) {
        SendShippingData(checkBoxStatus);
    }

    @Override
    public void requestCourierParameters(ArrayList<Boolean> checkBoxStatus, NetworkHandler network) {
        addCourierParams(checkBoxStatus, network);
    }

    @Override
    public void updateShopParameters(Parcelable shop) {
        shopParameter = (EditShippingModel.ParamEditShop)shop;
    }

    @Override
    public EditShippingModel.ParamEditShop getUpdatedShopParameters() {
        return shopParameter;
    }

    @Override
    public void districtSpinnerChanged(int provinceSpinnerPosition, int citySpinnerPosition, int districtSpinnerPosition) {
        if(districtSpinnerPosition>0){
            switchCourierVisibility(provinceSpinnerPosition, citySpinnerPosition, districtSpinnerPosition);
            view.setCourierOptionsVisibility(View.VISIBLE);
            districtChanged = true;
        }
    }

    @Override
    public void citySpinnerChanged(int citySpinnerPosition, int provinceSpinnerPosition) {
        if(view.currentCitySpinnerPosition() == 0){
            view.setDistrictSpinnerSelection(0);
            view.setDistrictSpinnerVisibility(View.GONE);
        }else if(view.currentCitySpinnerPosition() > 0){
            view.setDistrictSpinnerVisibility(View.VISIBLE);
            view.fillDistrictSpinner(provinceModelList.get(provinceSpinnerPosition).cityList.get(citySpinnerPosition).districtNames);
        }
        view.setCourierOptionsVisibility(View.GONE);
    }

    @Override
    public void provinceSpinnerChanged(int position) {
        if(isJakarta(position)){
            //switchCourierVisibility(position, 1, 1);
            setJakartaCourierVisibility();
            view.setCitySpinnerVisibility(View.GONE);
            view.setCourierOptionsVisibility(View.VISIBLE);
            view.setDistrictSpinnerVisibility(View.GONE);
            provinceChanged = true;
        }
        else if(position>0){
            view.setCitySpinnerVisibility(View.VISIBLE);
            view.fillCitySpinner(provinceModelList.get(position).cityNames);
            view.setCourierOptionsVisibility(View.GONE);
            //view.setLocationOptionVisibility(View.GONE);
            provinceChanged = true;
        }else if(position == 0){
            view.setCitySpinnerSelection(0);
            view.setCitySpinnerVisibility(View.GONE);
            view.setDistrictSpinnerVisibility(View.GONE);
            view.setCourierOptionsVisibility(View.GONE);
            //view.setLocationOptionVisibility(View.GONE);
        }
    }

    @Override
    public void setOkeActivatedProperties(boolean checkBoxState) {
        shopParameter.jneOkeActivated = checkBoxState;
    }

    @Override
    public void getOkeActivatedProperties(boolean isJNEOkeChecked) {
        shopParameter.jneOkeActivated = isJNEOkeChecked;
    }

    @Override
    public void setMapPosition(String longitude, String latitude) {
        shopParameter.longitude = longitude;
        shopParameter.latitude = latitude;
    }

    @Override
    public void additionalOptionChanged() {
        additionalOptionChanged = true;
    }

    @Override
    public String getMapLatitude() {
        return shopParameter.latitude;
    }

    @Override
    public String getMapLongitude() {
        return shopParameter.longitude;
    }

    @Override
    public String getWhiteListStatus() {
        return shopParameter.isWhiteList;
    }

    private ArrayList<EditShippingModel.CourierAttribute> GetShippingData(JSONObject Result) throws JSONException {
        EditShippingModel.CourierAttribute model;
        ArrayList<EditShippingModel.CourierAttribute> modelList = new ArrayList<>();
        JSONArray listShipping = Result.getJSONArray("shipments");
        JSONObject ShipmentDetail;
        for(int i = 0; i < listShipping.length(); i++){
            model = new EditShippingModel.CourierAttribute();
            ShipmentDetail = listShipping.getJSONObject(i);
            model.ShippingName = ShipmentDetail.getString("shipping_name");
            model.ShippingID = ShipmentDetail.getString("shipping_id");
            model.ShippingImageUri = ShipmentDetail.getString("shipping_logo");
            model.ShippingMaxAddFee = ShipmentDetail.optInt("shipping_max_add_fee", 0);
            model.ShippingWeightNotice = ShipmentDetail.getString("shipping_weight_note");
            model.ExtraError = "";
            model.isAllowShipping = true;
            model.packageAttributes = GetPackageData(Result, ShipmentDetail.getString("shipping_id"));

            modelList.add(model);
        }
        courierModel = modelList;
        return modelList;
    }

    private List<EditShippingModel.PackageAttribute> GetPackageData(JSONObject Result, String shippingID) throws  JSONException {
        EditShippingModel.PackageAttribute model;
        ArrayList<EditShippingModel.PackageAttribute> modelList = new ArrayList<>();
        JSONObject listPackage = Result.getJSONObject("package");
        JSONArray currentShippingPackageList = listPackage.getJSONArray(shippingID);
        JSONObject currentPackage;
        for (int i = 0; i < currentShippingPackageList.length(); i++) {
            model = new EditShippingModel.PackageAttribute();
            currentPackage = currentShippingPackageList.getJSONObject(i);
            model.ShippingChecked = !currentPackage.isNull("active");
            originallyCheckedPackage.add(model.ShippingChecked);
            model.ShippingPackage = currentPackage.getString("name");
            model.ShippingPackageID = currentPackage.getInt("sp_id");
            model.ShippingDescription = currentPackage.getString("desc");
            //TODO DUMMY VALUE NEEEEEH
            //model.isInstantCourier = currentPackage.optInt("is_instant_courier", 0);
            modelList.add(model);
        }
        return modelList;
    }

    private ArrayList<EditShippingModel.ProvinceAttribute> GetDistrictNameList (JSONObject Result) throws JSONException{
        ArrayList<EditShippingModel.ProvinceAttribute> registeredProvinces = new ArrayList<>();
        ArrayList<String> provinceNames = new ArrayList<>();
        EditShippingModel.ProvinceAttribute dummyProvincesData = new EditShippingModel.ProvinceAttribute();
        registeredProvinces.add(0, dummyProvincesData);
        provinceNames.add(0, view.getMainContext().getString(R.string.title_select_province));
        JSONArray provinceData = Result.getJSONArray("provinces_cities_districts");
        for(int i = 0 ; i< provinceData.length(); i++){
            registeredProvinces.add(getProvinceData(provinceData.getJSONObject(i), provinceNames));
        }
        provinceNamesList = provinceNames;
        return registeredProvinces;
    }

    private EditShippingModel.ProvinceAttribute getProvinceData(JSONObject selectedProvince, ArrayList<String> provinceNameList) throws JSONException{
        EditShippingModel.ProvinceAttribute provinceDatas = new EditShippingModel.ProvinceAttribute();
        EditShippingModel.CityAttribute dummyModel = new EditShippingModel.CityAttribute();
        provinceDatas.provinceID = selectedProvince.getString("province_id");
        provinceNameList.add(selectedProvince.getString("province_name"));
        provinceDatas.cityList.add(0, dummyModel);
        provinceDatas.cityNames.add(0, view.getMainContext().getString(R.string.title_select_city));
        for (int i = 0; i<selectedProvince.getJSONArray("cities").length(); i++){
            provinceDatas.cityList.add(getCityData(selectedProvince.getJSONArray("cities").getJSONObject(i), provinceDatas.cityNames));
        }
        return provinceDatas;
    }

    private EditShippingModel.CityAttribute getCityData(JSONObject selectedCity, ArrayList<String> cityNameList) throws JSONException{
        EditShippingModel.CityAttribute cityDatas = new EditShippingModel.CityAttribute();
        EditShippingModel.DistrictAttribute dummyModel = new EditShippingModel.DistrictAttribute();
        cityDatas.cityID = selectedCity.getString("city_id");
        cityNameList.add(selectedCity.getString("city_name"));
        cityDatas.districtList.add(0, dummyModel);
        cityDatas.districtNames.add(0, view.getMainContext().getString(R.string.title_select_district));
        for(int j = 0; j<selectedCity.getJSONArray("districts").length() ;j++){
            cityDatas.districtList.add(getDistrictName(selectedCity.getJSONArray("districts").getJSONObject(j), cityDatas.districtNames));
        }
        return cityDatas;
    }

    private EditShippingModel.DistrictAttribute getDistrictName(JSONObject selectedDistrict, ArrayList<String> districtNameList) throws  JSONException{
        EditShippingModel.DistrictAttribute districtDatas = new EditShippingModel.DistrictAttribute();
        districtDatas.districtID = selectedDistrict.getString("district_id");
        districtNameList.add(selectedDistrict.getString("district_name"));
        for(int k = 0; k<selectedDistrict.getJSONArray("district_shipping_supported").length(); k++){
            districtDatas.supportedCourierID.add(selectedDistrict.getJSONArray("district_shipping_supported").getString(k));
        }
        return districtDatas;
    }

    private void prepareShippingData(){
        NetworkHandler network = new NetworkHandler(view.getMainContext(), TkpdUrl.CREATE_SHOP);
        network.AddParam("act", "get_shop_creator_detail");
        network.Commit(prepareDataListener());
    }

    public void RequestShippingData(){
        NetworkHandler network = new NetworkHandler(view.getMainContext(), TkpdUrl.SHOP_EDITOR);
        network.AddParam("act", "get_shipping_info");
        network.Commit(shippingDataListener());
    }

    private void addCourierParams(ArrayList<Boolean> checkBoxValue, NetworkHandler network){
        ArrayList<String> listCourierPackage = courierPackageData(checkBoxValue);
        updateShopModel(checkBoxValue);
        for(int i = 0;i< listCourierPackage.size(); i++){
            network.AddParam(listCourierPackage.get(i), "on");
        }
        network.AddParam("sel-courier-city", selectedCourierCity());
        network.AddParam("shipment_ids", compiledShippingId());
        network.AddParam("postal", view.getZipCode());
        if(shipmentSettingsValid()){
            addDetailedOptionParams(network);
        }
    }

    public void SendShippingData(ArrayList<Boolean> checkBoxValue){
        ArrayList<String> listCourierPackage = courierPackageData(checkBoxValue);
        checkedPackage = checkBoxValue;

        NetworkHandler network = new NetworkHandler(view.getMainContext(), TkpdUrl.SHOP_EDITOR);
        network.AddParam("act", "update_shipping_info");
        updateShopModel(checkBoxValue);
        for(int i = 0;i< listCourierPackage.size(); i++){
            network.AddParam(listCourierPackage.get(i), "on");
        }
        network.AddParam("sel-courier-city", selectedCourierCity());
        network.AddParam("shipment_ids", compiledShippingId());
        network.AddParam("postal", view.getZipCode());
        addDetailedOptionParams(network);

        if(shipmentSettingsValid()){
            network.Commit(sendShippingDataListener());
        }
    }

    private void addDetailedOptionParams(NetworkHandler network){
        if(view.chooseLocationDisplayed()){
            network.AddParam("latitude", shopParameter.latitude);
            network.AddParam("longitude", shopParameter.longitude);
            network.AddParam("addr_street", view.getStreetAddress());
        }
        if(checkedCourierName.contains("jne")){
            if(!shopParameter.jneFeeValue.isEmpty() && !shopParameter.jneFeeValue.equals("0")){
                network.AddParam("jne_fee", "on");
                network.AddParam("jne_fee_flag", "on");
                network.AddParam("jne_fee_value", shopParameter.jneFeeValue);
                network.AddParam("jne_tiket", shopParameter.jneAWB);
            }
            if(!shopParameter.okeMinWeightValue.isEmpty() && !shopParameter.okeMinWeightValue.equals("0")){
                network.AddParam("jne_min_weight","on");
                network.AddParam("jne_min_weight_value", shopParameter.okeMinWeightValue);
                network.AddParam("min_weight", "on");
                network.AddParam("min_weight_value", shopParameter.okeMinWeightValue);
            }
            if(shopParameter.DiffDistrict.equals("1")){
                network.AddParam("jne_diff_district", "on");
                network.AddParam("diff_district", "on");
            }

            if(shopParameter.jneAWB.equals("1"))
                network.AddParam("jne_tiket", "on");
        }
        if(checkedCourierName.contains("tiki")){
            if(!shopParameter.feeTikiValue.isEmpty() &&  !shopParameter.feeTikiValue.equals("0")){
                network.AddParam("tiki_fee_flag", "on");
                network.AddParam("tiki_fee_value", shopParameter.feeTikiValue);
            }
        }
        if(checkedCourierName.contains("posindonesia")){
            if(!shopParameter.posFeeValue.isEmpty() &&  !shopParameter.posFeeValue.equals("0")){
                network.AddParam("pos_fee_flag", "on");
                network.AddParam("pos_fee_value", shopParameter.posFeeValue);
            }
            if(!shopParameter.posMinWeightValue.isEmpty() && !shopParameter.posMinWeightValue.equals("0")){
                network.AddParam("pos_min_weight_flag", "on");
                network.AddParam("pos_min_weight_value", shopParameter.posMinWeightValue);
            }
        }
    }

    private NetworkHandler.NetworkHandlerListener prepareDataListener(){
        return new NetworkHandler.NetworkHandlerListener() {
            @Override
            public void onSuccess(Boolean status) {

            }

            @Override
            public void getResponse(JSONObject Result) {
                try {
                    shopParameter = new EditShippingModel.ParamEditShop();
                    setInstantCourierVisibility(Result);
                    view.PrepareCourierData(GetShippingData(Result), shopParameter);
                    provinceModelList = GetDistrictNameList(Result);
                    prepareAddressSpinner();
                    view.setZipCode("");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void getMessageError(ArrayList<String> MessageError) {

            }
        };
    }

    private NetworkHandler.NetworkHandlerListener shippingDataListener(){
        return new NetworkHandler.NetworkHandlerListener() {
            @Override
            public void onSuccess(Boolean status) {
                view.updateOptionsMenu();
            }

            @Override
            public void getResponse(JSONObject Result) {
                try {
                    view.GetCourierData(GetShippingData(Result), editShopParameter(Result));
                    provinceModelList = GetDistrictNameList(Result);
                    setInstantCourierVisibility(Result);
                    setUserAddress(Result);
                    view.setZipCode(Result.getJSONObject("shop_shipping").getString("postal_code"));
                    shopParameter.zipCode = Result.getJSONObject("shop_shipping").getString("postal_code");
                    //TODO TES NYEEEEET CUMAN DUMMY VALUE NYEEEEEET
                    view.setShopAddress(Result.getJSONObject("shop_shipping").getString("addr_street"));
                    shopParameter.fullAdress = Result.getJSONObject("shop_shipping").getString("addr_street");
                    if(!shopParameter.longitude.isEmpty() && !shopParameter.latitude.isEmpty()){
                        view.setGoogleMapAddress(shopParameter.longitude, shopParameter.latitude);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void getMessageError(ArrayList<String> MessageError) {
                CommonUtils.dumper("Error Nih");
            }
        };
    }

    private NetworkHandler.NetworkHandlerListener sendShippingDataListener(){
        return new NetworkHandler.NetworkHandlerListener() {
            @Override
            public void onSuccess(Boolean status) {
                view.finishLoading();
            }

            @Override
            public void getResponse(JSONObject Result) {
                try {
                    if(Result.getString("success").equals("1")){
                        if(shipmentSettingsChanged()
                                || finalCheckBoxValueChanged()
                                || shopAttributesChanged())
                            view.finishActivity(view.getMainContext()
                                    .getString(R.string.message_shipping_success));
                        else
                            view.finishActivity(view.getMainContext()
                                    .getString(R.string.error_returnable_policy_same));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void getMessageError(ArrayList<String> MessageError) {
                view.showError(MessageError.get(0));
            }
        };
    }

    private void setUserAddress(JSONObject Result) throws JSONException{
        int indexOfShopProvince = provinceNamesList.indexOf(Result.getJSONObject("shop_shipping").getString("province_name"));
        int indexOfShopCity;
        int indexOfShopDistrict;
        view.fillProvinceSpinner(provinceNamesList);
        if(Result.getJSONObject("shop_shipping").getString("province_name").contains("Jakarta")||Result.getJSONObject("shop_shipping").getString("province_id").equals("13")){
            view.setProvinceSpinnerSelection(indexOfShopProvince);
            view.setCitySpinnerVisibility(View.GONE);
            view.setDistrictSpinnerVisibility(View.GONE);
            setJakartaCourierVisibility();
        } else {
            indexOfShopCity = provinceModelList.get(indexOfShopProvince).cityNames.indexOf(Result.getJSONObject("shop_shipping").getString("city_name"));
            indexOfShopDistrict = provinceModelList.get(indexOfShopProvince).cityList.get(indexOfShopCity).districtNames.indexOf(Result.getJSONObject("shop_shipping").getString("district_name"));
            view.fillCitySpinner(provinceModelList.get(indexOfShopProvince).cityNames);
            view.fillDistrictSpinner(provinceModelList.get(indexOfShopProvince).cityList.get(indexOfShopCity).districtNames);
            view.setProvinceSpinnerSelection(indexOfShopProvince);
            view.setCitySpinnerSelection(indexOfShopCity);
            view.setDistrictSpinnerSelection(indexOfShopDistrict);
            switchCourierVisibility(indexOfShopProvince, indexOfShopCity, indexOfShopDistrict);
        }
        view.setSpinnerListener();

    }

    private void prepareAddressSpinner(){
        view.fillProvinceSpinner(provinceNamesList);
        view.setProvinceSpinnerSelection(0);
        view.setSpinnerListener();
        view.setCitySpinnerVisibility(View.GONE);
        view.setDistrictSpinnerVisibility(View.GONE);
    }


    private ArrayList<String> courierPackageData(ArrayList<Boolean> checkboxValue){
        ArrayList<String> packageList = new ArrayList<>();
        int currentCheckBox = 0;
        for(int i = 0; i< courierModel.size(); i++){
            for(int j = 0; j< courierModel.get(i).packageAttributes.size(); j++){
                if(checkboxValue.get(currentCheckBox)&&courierModel.get(i).isAllowShipping){
                    insertPackageData(courierModel.get(i).ShippingID,
                            courierModel.get(i).packageAttributes.get(j).ShippingPackageID,
                            packageList);
                }
                currentCheckBox++;
            }
        }
        return packageList;
    }

    private void insertPackageData(String shippingID, int packageID, ArrayList<String> packageList){
        packageList.add("chk-courier-" + shippingID + "-" + Integer.toString(packageID));
    }

    private String compiledShippingId(){
        JSONObject jsonCompiled = new JSONObject();
        JSONObject shippingPackage;
        try{
            for(int i =0;i< courierModel.size(); i++){
                if(courierModel.get(i).courierChecked && courierModel.get(i).isAllowShipping){
                    shippingPackage = new JSONObject();
                    for(int j = 0; j< courierModel.get(i).checkedPackage.size(); j++){
                        shippingPackage.put(courierModel.get(i).checkedPackage.get(j).toString(), "1");
                    }
                    jsonCompiled.put(courierModel.get(i).ShippingID, shippingPackage);
                }
            }
            return  jsonCompiled.toString();
        }catch (JSONException e){
            e.printStackTrace();
            return "";
        }
    }
    private void updateShopModel(ArrayList<Boolean> checkboxValue){
        int currentCheckBox = 0;
        clearAllCheckBoxesState();
        for(int i = 0; i< courierModel.size(); i++){
            ArrayList<Integer> checkedPackage = new ArrayList<>();
            courierModel.get(i).courierChecked = false;
            for(int j = 0; j< courierModel.get(i).packageAttributes.size(); j++){
                courierModel.get(i).packageAttributes.get(j).ShippingChecked = checkboxValue.get(currentCheckBox);
                if(courierModel.get(i).packageAttributes.get(j).ShippingChecked && courierModel.get(i).isAllowShipping){
                    courierModel.get(i).courierChecked = true;
                    checkedPackage.add(courierModel.get(i).packageAttributes.get(j).ShippingPackageID);
                    checkedCourierName.add(courierModel.get(i).ShippingName.toLowerCase().replace(" ", ""));
                    //TODO masih hardcode buat GO-Kilat
                    if(courierModel.get(i).packageAttributes.get(j).ShippingPackageID  == 20){
                        instantCourierID.add(courierModel.get(i).packageAttributes.get(j).ShippingPackageID);
                    }
                }
                currentCheckBox++;
            }
            courierModel.get(i).checkedPackage = checkedPackage;
        }
    }

    private void clearAllCheckBoxesState(){
        checkedCourierName.clear();
        checkedCourierID.clear();
        instantCourierID.clear();
    }

    //TODO tes validasinya nyeeeet
    private boolean shipmentSettingsValid(){
        if(anyPackageTicked() && instantCourierIgnored()){
            return true;
        }else if (anyPackageTicked() && !instantCourierIgnored() && mapAddressValid()){
            return true;
        }else if(anyPackageTicked() && !instantCourierIgnored() && !mapAddressValid()){
            return false;
        }else{
            view.noPackageTicked();
            return false;
        }
    }

    private boolean shipmentSettingsChanged(){
        if(!additionalOptionChanged){
            return false;
        }else{
            return true;
        }
    }

    private boolean instantCourierIgnored(){
        if(!view.chooseLocationDisplayed()){
            return true;
        }else if(view.chooseLocationDisplayed() && !instantCourierTicked()){
            return true;
        }else if(view.chooseLocationDisplayed() && instantCourierTicked()){
            return false;
        }else{
            return false;
        }
    }

    private boolean mapAddressValid(){
        CommonUtils.dumper(view.getShopGoogleMapAddress());
        if(view.getShopGoogleMapAddress().isEmpty()){
            view.locationIsNotChosen();
            return false;
        }else if(view.getStreetAddress().isEmpty()){
            view.addressNotFilled();
            return false;
        }else{
            return true;
        }
    }

    private boolean anyPackageTicked(){
        return !checkedCourierName.isEmpty();
    }

    private boolean instantCourierTicked(){
        return !instantCourierID.isEmpty();
    }

    private boolean isJakarta(int position){
        return provinceModelList.get(position).provinceID.equals("13") || provinceNamesList.get(position).contains("Jakarta");
    }

    private String selectedCourierCity(){
        if(isJakarta(view.currentProvinceSpinnerPosition())){
            return "5573";
        }else{
            return provinceModelList.get(view.currentProvinceSpinnerPosition()).cityList.get(view.currentCitySpinnerPosition()).districtList.get(view.currentDistrictSpinnerPosition()).districtID;
        }
    }

    private void setInstantCourierVisibility(JSONObject Result){
        //TODO GOJEK isAllowed will change later
        if(Result.has("allow_activate_gojek")){
            shopParameter.isAllowGojek = Result.optString("allow_activate_gojek", "0");
        }else{
            shopParameter.isAllowGojek = Result.optJSONObject("gojek").optString("whitelisted", "1");
        }
    }

    private EditShippingModel.ParamEditShop editShopParameter(JSONObject Result) throws JSONException{
        EditShippingModel.ParamEditShop model = new EditShippingModel.ParamEditShop();
        model.jneFeeValue = Result.getJSONObject("jne").getString("jne_fee");
        model.feeTikiValue = Result.getString("tiki_fee");
        model.jneAWB = Result.getJSONObject("jne").getString("jne_tiket");
        model.DiffDistrict = Result.getJSONObject("jne").getString("jne_diff_district");
        model.posFeeValue = Result.getString("pos_fee");
        model.okeMinWeightValue = Result.getJSONObject("jne").getString("jne_min_weight");
        model.posMinWeightValue = Result.getJSONObject("pos_min_weight").getString("min_weight");
        model.isAllow = Result.getString("is_allow");
        model.longitude = Result.getJSONObject("shop_shipping").optString("longitude", "");
        model.latitude = Result.getJSONObject("shop_shipping").optString("latitude", "");

        model.msisdnVerified = Result.getJSONObject("contact").getString("msisdn_verification");
        model.phoneNumber = Result.getJSONObject("contact").getString("msisdn_enc");

        //TODO TES BUAT RPX TAI
        model.isWhiteList = Result.getJSONObject("rpx").optString("whitelisted_idrop", "0");
        shopParameter = model;
        return  model;
    }

    private void switchCourierVisibility(int provinceSpinnerPosition, int citySpinnerPosition, int districtSpinnerPosition){
        //view.setLocationOptionVisibility(View.GONE);
        for(int i = 0; i<courierModel.size(); i++){
            courierModel.get(i).isAllowShipping = false;
            if(provinceModelList.get(provinceSpinnerPosition).cityList.get(citySpinnerPosition).districtList.get(districtSpinnerPosition).supportedCourierID.contains(courierModel.get(i).ShippingID)){
                courierModel.get(i).isAllowShipping= true;

                //TODO cek dia instant courier apa bukan
                for(int j = 0; j < courierModel.get(i).packageAttributes.size(); j++){
                    setInstantCourierAvailabilityMode(i, j);
                }
            }
            view.setCourierVisibility(i, courierModel.get(i).isAllowShipping);
        }
    }

    private void setJakartaCourierVisibility(){
        //view.setLocationOptionVisibility(View.GONE);
        for(int i = 0; i<courierModel.size(); i++){
            courierModel.get(i).isAllowShipping= true;
            view.setCourierVisibility(i, true);
            for (int j = 0; j < courierModel.get(i).packageAttributes.size(); j++){
                setInstantCourierAvailabilityMode(i, j);
            }
        }
    }

    private void setInstantCourierAvailabilityMode(int i, int j){
        //TODO untuk sementara hardcode dulu bang
        if(courierModel.get(i).packageAttributes.get(j).ShippingPackageID == 20 && !shopParameter.isAllowGojek.equals("0")){
            //view.setLocationOptionVisibility(View.VISIBLE);
            courierModel.get(i).isAllowShipping = true;
        }else if(courierModel.get(i).packageAttributes.get(j).ShippingPackageID == 20 && shopParameter.isAllowGojek.equals("0")){
            courierModel.get(i).isAllowShipping = false;
            view.setCourierVisibility(i, false);
        }
    }

    private boolean finalCheckBoxValueChanged(){
        for(int i = 0; i<checkedPackage.size(); i++){
            if(!checkedPackage.get(i).equals(originallyCheckedPackage.get(i))){
                return true;
            }
        }
        return false;
    }

    private boolean shopAttributesChanged(){
        if(!view.getZipCode().equals(shopParameter.zipCode)
                || !view.getStreetAddress().equals(shopParameter.fullAdress)
                || provinceChanged
                || districtChanged)
            return true;
        else return false;
    }

}