package com.tokopedia.logisticaddaddress.domain;

import com.tokopedia.logisticCommon.data.entity.geolocation.autocomplete.LocationPass;

import java.util.HashMap;

/**
 * Created by Fajar Ulin Nuha on 31/10/18.
 */
public class LocationPassMapper {

    /**
     * A workaround method to resolve shared-data-with-different-package name issue
     * @param hashMap generated from LocationPass
     * @return
     */
    public static LocationPass unBundleLocationMap(HashMap<String, String> hashMap) {
        LocationPass result = new LocationPass();
        result.setCityName(hashMap.get("city_name"));
        result.setDistrictName(hashMap.get("district_name"));
        result.setGeneratedAddress(hashMap.get("generated_address"));
        result.setLatitude(hashMap.get("latitude"));
        result.setLongitude(hashMap.get("longitude"));
        return result;
    }

    /**
     * A workaround method to resolve shared-data-with-different-package name issue
     * @param locationPass to be passed to address module
     * @return
     */
    public static HashMap<String, String> bundleLocationMap(LocationPass locationPass) {
        HashMap<String, String> result = new HashMap<>();
        result.put("city_name", locationPass.getCityName());
        result.put("district_name", locationPass.getDistrictName());
        result.put("generated_address", locationPass.getGeneratedAddress());
        result.put("latitude", locationPass.getLatitude());
        result.put("longitude", locationPass.getLongitude());
        result.put("manual_address", locationPass.getManualAddress());
        return result;
    }

}
