package com.tokopedia.logisticdata.data.utils;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.tokopedia.logisticdata.data.entity.address.Destination;

import java.util.List;
import java.util.Locale;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class GeoLocationUtils {

    private static final String TAG = GeoLocationUtils.class.getSimpleName();
    private static final String DEFAULT_STREET_NAME = "Unnamed Rd";

    public static void getReverseGeoCodeParallel(Context context,
                                                  double latitude,
                                                  double longitude,
                                                  GeoLocationListener listener) {

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

    public static String reverseGeoCode(Context context, double latitude, double longitude) {
        String reseponseAddress = "";
        try {
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            List<Address> listAddress = geocoder.getFromLocation(latitude, longitude, 1);

            // to handle bug of android support library
            // getMaxAddressLineIndex return 0, but getAddressLine have value
            if (listAddress.get(0).getMaxAddressLineIndex() == 0 &&
                    listAddress.get(0).getAddressLine(0) != null) {
                reseponseAddress = listAddress.get(0).getAddressLine(0);
            }

            for (int j = 0; j < listAddress.get(0).getMaxAddressLineIndex(); j++) {
                if (j == 0) {
                    Address address = listAddress.get(0);
                    reseponseAddress = address.getThoroughfare();
                } else {
                    if (reseponseAddress == null || reseponseAddress.equals("Unnamed Rd")) {
                        reseponseAddress = listAddress.get(0).getAddressLine(j);
                    } else {
                        reseponseAddress = reseponseAddress + " " + listAddress.get(0).getAddressLine(j);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (reseponseAddress == null || reseponseAddress.isEmpty()) {
            reseponseAddress = String.valueOf(latitude) + ", " + String.valueOf(longitude);
        }
        return reseponseAddress;
    }

    public static void reverseGeoCodeParallel(Context context,
                                              String latitude,
                                              String longitude,
                                              GeoLocationListener listener) {
        if (latitude.isEmpty() || longitude.isEmpty()) {
            listener.getGeoCode("");
        } else {
            getReverseGeoCodeParallel(context,
                    Double.parseDouble(latitude),
                    Double.parseDouble(longitude),
                    listener);
        }
    }

    public static String reverseGeoCode(Context context, String latitude, String longitude) {
        if (latitude == null || longitude == null) {
            return "";
        } else if (latitude.isEmpty() || longitude.isEmpty()) {
            return "";
        } else {
            return reverseGeoCode(context, Double.parseDouble(latitude), Double.parseDouble(longitude));
        }
    }

    public static LatLng generateLatLng(String latitude, String longitude) {
        return generateLatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
    }

    public static LatLng generateLatLng(double latitude, double longitude) {
        return new LatLng(latitude, longitude);
    }

    public static LatLngBounds generateBoundary(double latitude, double longitude) {
        LatLng center = generateLatLng(latitude, longitude);

        double radiusDegree = 0.1;

        LatLng northEast = new LatLng(center.latitude + radiusDegree, center.longitude - radiusDegree);
        LatLng southWest = new LatLng(center.latitude - radiusDegree, center.longitude + radiusDegree);

        return LatLngBounds.builder()
                .include(northEast)
                .include(southWest)
                .build();
    }

    public static LatLngBounds generateBoundary(String latitude, String longitude) {
        return generateBoundary(Double.parseDouble(latitude), Double.parseDouble(longitude));
    }

    public interface GeoLocationListener {
        void getGeoCode(String resultAddress);
    }
}
