package com.tokopedia.logisticaddaddress.utils;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import com.tokopedia.logisticdata.data.entity.address.Destination;

import java.util.List;
import java.util.Locale;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Fajar Ulin Nuha on 10/10/18.
 */
public class GeoLocationUtils {

    private static final String DEFAULT_STREET_NAME = "Unnamed Rd";
    private static final String TAG = GeoLocationUtils.class.getSimpleName();

    public static void getReverseGeoCodeParallel(Context context,
                                                 double latitude,
                                                 double longitude,
                                                 GeoLocationListener listener) {

        // todo : optimize this
        Destination destination = new Destination();
        destination.setLatitude(String.valueOf(latitude));
        destination.setLongitude(String.valueOf(longitude));
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        Observable.just(destination).map(new Func1<Destination, List<Address>>() {
            @Override
            public List<Address> call(Destination destination) {
                try {
                    return geocoder.getFromLocation(latitude, longitude, 1);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Address>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.getGeoCode(
                                String.valueOf(latitude) + ", "
                                        + String.valueOf(longitude)
                        );
                    }

                    @Override
                    public void onNext(List<Address> listAddresses) {
                        String responseAddress = "";
                        if (listAddresses.get(0).getMaxAddressLineIndex() == 0 &&
                                listAddresses.get(0).getAddressLine(0) != null) {
                            responseAddress = listAddresses.get(0).getAddressLine(0);
                        }

                        for (int j = 0; j < listAddresses.get(0).getMaxAddressLineIndex(); j++) {
                            if (j == 0) {
                                Address address = listAddresses.get(0);
                                responseAddress = address.getThoroughfare();
                                Log.d(TAG, "reverseGeoCode: 1." + address.getAddressLine(0));
                                Log.d(TAG, "reverseGeoCode: 2." + address.getLocality());
                                Log.d(TAG, "reverseGeoCode: 3." + address.getSubLocality());
                                Log.d(TAG, "reverseGeoCode: 4." + address.getAdminArea());
                                Log.d(TAG, "reverseGeoCode: 5." + address.getSubAdminArea());
                                Log.d(TAG, "reverseGeoCode: 6." + address.getPremises());
                                Log.d(TAG, "reverseGeoCode: 7." + address.getThoroughfare());
                                Log.d(TAG, "reverseGeoCode: 8." + address.getSubThoroughfare());
                            } else {
                                if (responseAddress.equals(DEFAULT_STREET_NAME)) {
                                    responseAddress = listAddresses.get(0).getAddressLine(j);
                                } else {
                                    responseAddress = responseAddress
                                            + " "
                                            + listAddresses.get(0).getAddressLine(j);
                                }

                            }
                        }
                        listener.getGeoCode(responseAddress);
                    }
                });

    }

    public interface GeoLocationListener {
        void getGeoCode(String resultAddress);
    }
}
