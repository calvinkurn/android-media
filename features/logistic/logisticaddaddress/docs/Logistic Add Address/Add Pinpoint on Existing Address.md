---
title: Add Pinpoint on Existing Address
labels:
- kb-how-to-article
---


| **Status** | ​<!--start status:GREEN-->DONE<!--end status--> |
| --- | --- |
| Contributors | [Fakhira Devina](https://tokopedia.atlassian.net/wiki/people/61077e53b704b40068e80a8e?ref=confluence)  |
| Product Manager | [Wahyu Ivan Satyagraha](https://tokopedia.atlassian.net/wiki/people/61ad4312c15977006a17ce75?ref=confluence)  |
| Team | [Minion Bob](https://tokopedia.atlassian.net/people/team/2373d8a6-1afc-4f2a-aa7a-63855c273051) |
| Release date | ​10 Feb 2023 / ​<!--start status:GREY-->MA-3.208<!--end status-->  |
| Module type | ​<!--start status:YELLOW-->FEATURE<!--end status--> |
| Module Location | `features/logistic/logisticaddress` |

<!--toc-->

## Overview

### Background

When user wants to use Same Day/Instant Courier, they need to provide the address' pinpoint so courier would know the exact location of the delivery. For users that doesn’t have pinpoint data on their existing address, user can add pinpoint data only instead of going to edit address full flow by redirecting to pinpoint page. 

Example can be seen in features/transaction/checkout/src/main/java/com/tokopedia/checkout/view/ShipmentFragment.java (`navigateToPinpointActivity`)

![](../res/addpinpointonexistingaddress/background.png)

## How-to

### Dependency

Add `logisticCommon` to your module’s dependencies and `localizationChooseAddress` for updating LCA data



```
implementation projectOrAar(rootProject.ext.features.logisticCommon)
implementation projectOrAar(rootProject.ext.features.localizationchooseaddress)
```

### Instructions

1. When user doesn’t have pinpoint, redirect them to `ApplinkConstInternalMarketplace.GEOLOCATION` by passing `LocationData` object that contains current address **district name** and **city name**.



```
LocationPass existingAddressData = new LocationPass();
locationPass.setCityName(city);
locationPass.setDistrictName(district);

Intent intent = RouteManager.getIntent(getActivity(), ApplinkConstInternalMarketplace.GEOLOCATION);
Bundle bundle = new Bundle();
bundle.putParcelable(LogisticConstant.EXTRA_EXISTING_LOCATION, existingAddressData);
intent.putExtras(bundle);
startActivityForResult(intent, REQUEST_CODE_COURIER_PINPOINT);
```
2. After user succeed adding the pinpoint, GeolocationActivity will give the selected latitude and longitude from `locationPass.getLatitude(), locationPass.getLongitude()` 



```
LocationPass locationPass = data.getExtras().getParcelable(LogisticConstant.EXTRA_EXISTING_LOCATION);
```
3. Save the pinpoint data by using `EditAddressUseCase` and provide the whole address data in the parameter



```
params.put(EditAddressParam.ADDRESS_ID, addressId);
params.put(EditAddressParam.ADDRESS_NAME, addressName);
params.put(EditAddressParam.ADDRESS_STREET, addressStreet);
params.put(EditAddressParam.POSTAL_CODE, postalCode);
params.put(EditAddressParam.DISTRICT_ID, districtId);
params.put(EditAddressParam.CITY_ID, cityId);
params.put(EditAddressParam.PROVINCE_ID, provinceId);
params.put(EditAddressParam.LATITUDE, latitude);
params.put(EditAddressParam.LONGITUDE, longitude);
params.put(EditAddressParam.RECEIVER_NAME, receiverName);
params.put(EditAddressParam.RECEIVER_PHONE, receiverPhone);
```



```
editAddressUseCase.createObservable(params)
.subscribeOn(executorSchedulers.getIo())
.observeOn(executorSchedulers.getMain())
.unsubscribeOn(executorSchedulers.getIo())
.subscribe(new Subscriber<String>() {
    @Override
    public void onNext(String stringResponse) {
        if (getView() != null) {
            JsonObject response = null;
            String messageError = null;
            boolean statusSuccess;
            try {
                response = new JsonParser().parse(stringResponse).getAsJsonObject();
                int statusCode = response.getAsJsonObject().getAsJsonObject(EditAddressUseCase.RESPONSE_DATA)
                        .get(EditAddressUseCase.RESPONSE_IS_SUCCESS).getAsInt();
                statusSuccess = statusCode == 1;
                if (!statusSuccess) {
                    messageError = response.getAsJsonArray("message_error").get(0).getAsString();
                }
            } catch (Exception e) {
                Timber.d(e);
                statusSuccess = false;
            }

            if (response != null && statusSuccess) {
                // do success
            } else {
                // do error
            }
        }
    }
})
```
4. Update pinpoint to local choose address data. This step is important to make sure the current chosen address data has the saved pinpoint.



```
fun Context.updateLocalChosenAddressPinpoint(latitude: String, longitude: String) {
    val currentAddressData = ChooseAddressUtils.getLocalizingAddressData(this)
    currentAddressData.let { chooseAddressData ->
        ChooseAddressUtils.updateLocalizingAddressDataFromOther(
            context = this,
            addressId = chooseAddressData.address_id,
            cityId = chooseAddressData.city_id,
            districtId = chooseAddressData.district_id,
            lat = latitude,
            long = longitude,
            label = chooseAddressData.label,
            postalCode = chooseAddressData.postal_code,
            warehouseId = chooseAddressData.warehouse_id,
            shopId = chooseAddressData.shop_id,
            warehouses = chooseAddressData.warehouses,
            serviceType = chooseAddressData.service_type,
            lastUpdate = chooseAddressData.tokonow_last_update
        )
    }
}
```
5. Refresh your page with the latest LocalCacheModel data



```
private fun refresh() {
    showLoadingLayout()
    context?.let {
        ChooseAddressUtils.getLocalizingAddressData(it).let { addressData ->
            viewModel.loadData(addressData)
        }
    }
}
```