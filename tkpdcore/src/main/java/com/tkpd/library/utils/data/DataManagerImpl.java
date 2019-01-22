package com.tkpd.library.utils.data;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.google.gson.GsonBuilder;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tkpd.library.utils.data.model.ListBank;
import com.tkpd.library.utils.data.model.ListCity;
import com.tkpd.library.utils.data.model.ListDistricts;
import com.tkpd.library.utils.data.model.ListProvince;
import com.tkpd.library.utils.data.model.ListShippingCity;
import com.tokopedia.core.database.DbFlowDatabase;
import com.tokopedia.core2.R;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.database.CacheDuration;
import com.tokopedia.core.database.model.Bank;
import com.tokopedia.core.database.model.City;
import com.tokopedia.core.database.model.City_Table;
import com.tokopedia.core.database.model.District;
import com.tokopedia.core.database.model.District_Table;
import com.tokopedia.core.database.model.Province;
import com.tokopedia.core.database.model.Province_Table;
import com.tokopedia.core.network.apiservices.etc.AddressService;
import com.tokopedia.core.network.apiservices.etc.apis.AddressApi;
import com.tokopedia.core.network.apiservices.user.PeopleService;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.network.retrofit.response.ErrorListener;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.NetworkCalculator;
import com.tokopedia.core.network.retrofit.utils.RetrofitUtils;
import com.tokopedia.core.network.v4.NetworkConfig;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by m.normansyah on 2/2/16.
 * migrate retrofit 2 by Angga.Prasetiyo
 */
public class DataManagerImpl implements DataManager {
    public static final String PROVINCE_ID = "province_id";
    public static final String CITY_ID = "city_id";
    public static final String DATA_LOC = "dataLoc";
    public static final String DATA_LOC_VALUE = "dataLocValue";
    public static final String SHIPPING_CITY_DURATION_STORAGE = "shipping_city_storage";
    private static final String CACHE_DURATION = "cache_duration";
    public static DataManagerImpl dataManager;

    public static final DataManager getDataManager() {
        if (dataManager == null)
            return dataManager = new DataManagerImpl();
        else
            return dataManager;
    }

    /**
     * add location jabodetabek for filter in browse product or hot product
     *
     * @return
     */
    public static String addJabodetabek() {
        String[] jabodetabek = {"Jakarta", "Bogor", "Depok", "Tangerang", "Bekasi"};
        City city = new Select().from(City.class)
                .where(City_Table.cityId.eq("-1"))
                .querySingle();

        List<District> districts = new ArrayList<>();
        String result = "";
        for (String jbdtb : jabodetabek) {
            District d = new Select().from(District.class)
                    .where(District_Table.districtName.eq(jbdtb))
                    .querySingle();
            districts.add(d);
            if (d != null)
                result += d.getDistrictId() + ",";
        }
        result = result.substring(0, result.length() - 1);// delete last comma
        return result;
    }

    /**
     * create map for data location and value of data location for browse product and browse hot list
     *
     * @return
     */
    public static Map<String, ArrayList<String>> createDataLoc() {
        List<District> districts = getListShippingCity();
        ArrayList<String> dataLoc = new ArrayList<>();
        ArrayList<String> dataLocValue = new ArrayList<>();
        dataLoc.add(0, "Jabodetabek");
        dataLocValue.add(addJabodetabek());

        for (District d : districts) {
            dataLoc.add(d.getDistrictName());
            dataLocValue.add(d.getDistrictId());
        }

        Map<String, ArrayList<String>> map = new HashMap();
        map.put(DATA_LOC, dataLoc);
        map.put(DATA_LOC_VALUE, dataLocValue);
        return map;
    }

    public static Map<String, ArrayList<String>> createDataLoc(List<District> districts) {
        ArrayList<String> dataLoc = new ArrayList<>();
        ArrayList<String> dataLocValue = new ArrayList<>();
        dataLoc.add(0, "Jabodetabek");
        dataLocValue.add(addJabodetabek());

        for (District d : districts) {
            dataLoc.add(d.getDistrictName());
            dataLocValue.add(d.getDistrictId());
        }

        Map<String, ArrayList<String>> map = new HashMap();
        map.put(DATA_LOC, dataLoc);
        map.put(DATA_LOC_VALUE, dataLocValue);
        return map;
    }

    public static List<District> getListShippingCity() {
        City city = new Select().from(City.class)
                .where(City_Table.cityId.eq("-1"))
                .querySingle();
        return city.getDistricts();
    }

    @Override
    public void getListProvince(final Context context, final DataReceiver dataReceiver) {

//        final List<Province> provinces =
//                new Select().from(Province.class).execute();
//
//        if (provinces == null || provinces.size() <= 0) {
        dataReceiver.getSubscription().add(
                //[START] HIT NETWORK
                new AddressService().getApi().getProvince(
                        AuthUtil.generateParams(context, new HashMap<String, String>())
                )
                        .map(new Func1<Response<TkpdResponse>, Response<TkpdResponse>>() {
                            @Override
                            public Response<TkpdResponse> call(Response<TkpdResponse> response) {
                                ListProvince.Data datas = new GsonBuilder().create().fromJson(response.body().getStringData(), ListProvince.Data.class);
                                DatabaseWrapper database = FlowManager.getDatabase(DbFlowDatabase.NAME).getWritableDatabase();
                                database.beginTransaction();
                                try {
                                    for (Province a : Province.toDbs(datas.getProvinces())) {
                                        a.save();
                                    }
                                    database.setTransactionSuccessful();
                                } finally {
                                    database.endTransaction();
                                }
                                return response;
                            }
                        })
                        .subscribeOn(Schedulers.newThread())
                        .unsubscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
//                            .retry(5)
                        .subscribe(new Subscriber<Response<TkpdResponse>>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e("MNORMANSYAH", "getListProvince " + e.getLocalizedMessage());
                                if (e instanceof UnknownHostException) {
                                    dataReceiver.onNetworkError(context.getString(R.string.default_request_error_unknown));
                                } else {
                                    dataReceiver.onTimeout();
                                }
                            }

                            @Override
                            public void onNext(Response<TkpdResponse> response) {
                                if (response.isSuccessful()) {
                                    if (!response.body().isError()) {
                                        Log.d("MNORMANSYAH", DataManagerImpl.class.getSimpleName() + " > " + response.body().getStringData());
//                                            listener.onSuccess(response.body().convertDataObj(ProductDetailData.class));
                                        ListProvince.Data datas = new GsonBuilder().create().fromJson(response.body().getStringData(), ListProvince.Data.class);
                                        Log.d("MNORMANSYAH", datas + "");
                                        ArrayList<Province> provinces = new ArrayList<Province>();
                                        for (ListProvince.Province a : datas.getProvinces()) {
                                            provinces.add(Province.fromNetwork(a));
                                        }

                                        dataReceiver.setProvinces(provinces);
                                    } else {
                                        Log.d("MNORMANSYAH", DataManagerImpl.class.getSimpleName() + " > " + response.body().getErrorMessages().get(0));
                                    }
                                } else {
                                    new ErrorHandler(new ErrorListener() {
                                        @Override
                                        public void onUnknown() {
                                            dataReceiver.onNetworkError("Network Unknown Error!");
                                        }

                                        @Override
                                        public void onTimeout() {
                                            dataReceiver.onNetworkError("Network Timeout Error!");
                                            dataReceiver.onTimeout();
                                        }

                                        @Override
                                        public void onServerError() {
                                            dataReceiver.onNetworkError("Network Internal Server Error!");
                                        }

                                        @Override
                                        public void onBadRequest() {
                                            dataReceiver.onNetworkError("Network Bad Request Error!");
                                        }

                                        @Override
                                        public void onForbidden() {
                                            dataReceiver.onNetworkError("Network Forbidden Error!");
                                            dataReceiver.onFailAuth();
                                        }
                                    }, response.code());
                                }
                            }
                        })
                //[END] HIT NETWORK
        );
//        } else {
//            dataReceiver.setProvinces(provinces);
//        }
    }

    @Override
    public void getListCity(Context context, final DataReceiver dataReceiver, String provinceId) {

        final Province province = new Select()
                .from(Province.class)
                .where(Province_Table.provinceId.eq(provinceId))
                .querySingle();
        final List<City> cities = province.getCities();

        Log.d("MNORMANSYAH", "cities -> " + cities + " province -> " + province);

        if (cities == null || cities.size() <= 0) {
            HashMap<String, String> param = new HashMap<>();
            param.put(PROVINCE_ID, provinceId);

            dataReceiver.getSubscription().add(
                    new AddressService().getApi().getCity(
                            AuthUtil.generateParams(context, param)
                    )
                            .map(new Func1<Response<TkpdResponse>, Response<TkpdResponse>>() {
                                @Override
                                public Response<TkpdResponse> call(Response<TkpdResponse> tkpdResponseResponse) {
                                    ListCity.Data datas = new GsonBuilder().create().fromJson(tkpdResponseResponse.body().getStringData(), ListCity.Data.class);
                                    DatabaseWrapper database = FlowManager.getDatabase(DbFlowDatabase.NAME).getWritableDatabase();
                                    database.beginTransaction();
                                    try {
                                        for (City city : City.toDbs(datas.getCities())) {
                                            city.setProvince(province);
                                            city.save();
                                        }
                                        database.setTransactionSuccessful();
                                    } finally {
                                        database.endTransaction();
                                    }
                                    return tkpdResponseResponse;
                                }
                            })
                            .subscribeOn(Schedulers.newThread())
                            .unsubscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
//                            .retry(5)
                            .subscribe(
                                    new Subscriber<Response<TkpdResponse>>() {
                                        @Override
                                        public void onCompleted() {

                                        }

                                        @Override
                                        public void onError(Throwable e) {
                                            Log.e("MNORMANSYAH", "getListCity " + e.getLocalizedMessage());
                                            if (e instanceof UnknownHostException) {
                                                dataReceiver.onNetworkError("No Connection");
                                            } else {
                                                dataReceiver.onTimeout();
                                            }
                                        }

                                        @Override
                                        public void onNext(Response<TkpdResponse> response) {
                                            if (response.isSuccessful()) {
                                                if (!response.body().isError()) {
                                                    Log.d("MNORMANSYAH", DataManagerImpl.class.getSimpleName() + " > " + response.body().getStringData());
//                                            listener.onSuccess(response.body().convertDataObj(ProductDetailData.class));
                                                    ListCity.Data datas = new GsonBuilder().create().fromJson(response.body().getStringData(), ListCity.Data.class);
                                                    Log.d("MNORMANSYAH", datas + "");
                                                    ArrayList<City> cities = new ArrayList<City>();
                                                    for (ListCity.Cities a : datas.getCities()) {
                                                        cities.add(City.fromNetwork(a.getCity_id()));
                                                    }
                                                    dataReceiver.setCities(cities);
                                                } else {
                                                    Log.d("MNORMANSYAH", DataManagerImpl.class.getSimpleName() + " > " + response.body().getErrorMessages().get(0));
                                                }
                                            } else {
                                                new ErrorHandler(new ErrorListener() {
                                                    @Override
                                                    public void onUnknown() {
                                                        dataReceiver.onNetworkError("Network Unknown Error!");
                                                    }

                                                    @Override
                                                    public void onTimeout() {
                                                        dataReceiver.onNetworkError("Network Timeout Error!");
                                                        dataReceiver.onTimeout();
                                                    }

                                                    @Override
                                                    public void onServerError() {
                                                        dataReceiver.onNetworkError("Network Internal Server Error!");
                                                    }

                                                    @Override
                                                    public void onBadRequest() {
                                                        dataReceiver.onNetworkError("Network Bad Request Error!");
                                                    }

                                                    @Override
                                                    public void onForbidden() {
                                                        dataReceiver.onNetworkError("Network Forbidden Error!");
                                                        dataReceiver.onFailAuth();
                                                    }
                                                }, response.code());
                                            }
                                        }
                                    }
                            )
            );

        } else {
            dataReceiver.setCities(cities);
        }
    }

    @Override
    public void getListDistrict(Context context, final DataReceiver dataReceiver, String cityId) {
        final City city = new Select().from(City.class)
                .where(City_Table.cityId.eq(cityId))
                .querySingle();
        final List<District> districts = city.getDistricts();

        if (districts == null || districts.size() <= 0) {
            HashMap<String, String> param = new HashMap<>();
            param.put(CITY_ID, cityId);
            dataReceiver.getSubscription().add(
                    new AddressService().getApi().getDistrict(
                            AuthUtil.generateParams(context, param)
                    ).
                            map(new Func1<Response<TkpdResponse>, Response<TkpdResponse>>() {
                                @Override
                                public Response<TkpdResponse> call(Response<TkpdResponse> tkpdResponseResponse) {
                                    ListDistricts.Data datas = new GsonBuilder().create().fromJson(tkpdResponseResponse.body().getStringData(), ListDistricts.Data.class);
                                    DatabaseWrapper database = FlowManager.getDatabase(DbFlowDatabase.NAME).getWritableDatabase();
                                    database.beginTransaction();
                                    try {
                                        for (District district : District.toDbs(datas.getDistricts())) {
                                            district.setDistrictCity(city);
                                            district.save();
                                        }
                                        database.setTransactionSuccessful();
                                    } finally {
                                        database.endTransaction();
                                    }
                                    return tkpdResponseResponse;
                                }
                            })
                            .subscribeOn(Schedulers.newThread())
                            .unsubscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
//                            .retry(5)
                            .subscribe(
                                    new Subscriber<Response<TkpdResponse>>() {
                                        @Override
                                        public void onCompleted() {

                                        }

                                        @Override
                                        public void onError(Throwable e) {
                                            dataReceiver.onNetworkError(e.getLocalizedMessage());
                                        }

                                        @Override
                                        public void onNext(Response<TkpdResponse> response) {
                                            if (response.isSuccessful()) {
                                                if (!response.body().isError()) {
                                                    Log.d("MNORMANSYAH", DataManagerImpl.class.getSimpleName() + " > " + response.body().getStringData());
//                                            listener.onSuccess(response.body().convertDataObj(ProductDetailData.class));
                                                    ListDistricts.Data datas = new GsonBuilder().create().fromJson(response.body().getStringData(), ListDistricts.Data.class);
                                                    Log.d("MNORMANSYAH", datas + "");
                                                    ArrayList<District> districts = new ArrayList<District>();
                                                    for (ListDistricts.Districts a : datas.getDistricts()) {
                                                        districts.add(District.fromNetwork(a));
                                                    }
                                                    dataReceiver.setDistricts(districts);
                                                } else {
                                                    Log.d("MNORMANSYAH", DataManagerImpl.class.getSimpleName() + " > " + response.body().getErrorMessages().get(0));
                                                }
                                            } else {
                                                new ErrorHandler(new ErrorListener() {
                                                    @Override
                                                    public void onUnknown() {
                                                        dataReceiver.onNetworkError("Network Unknown Error!");
                                                    }

                                                    @Override
                                                    public void onTimeout() {
                                                        dataReceiver.onNetworkError("Network Timeout Error!");
                                                        dataReceiver.onTimeout();
                                                    }

                                                    @Override
                                                    public void onServerError() {
                                                        dataReceiver.onNetworkError("Network Internal Server Error!");
                                                    }

                                                    @Override
                                                    public void onBadRequest() {
                                                        dataReceiver.onNetworkError("Network Bad Request Error!");
                                                    }

                                                    @Override
                                                    public void onForbidden() {
                                                        dataReceiver.onNetworkError("Network Forbidden Error!");
                                                        dataReceiver.onFailAuth();
                                                    }
                                                }, response.code());
                                            }
                                        }
                                    }
                            )
            );

        } else {
            dataReceiver.setDistricts(districts);
        }
    }

    @Override
    public void getListBank(Context context, final DataReceiver dataReceiver) {
        for (int T = 15; T > 0; T--) {
            HashMap<String, String> param = new HashMap<>();
            param.put("keyword", "");
            param.put("page", T + "");
            param.put("profile_user_id", "");
            final int finalT = T;
            new PeopleService().getApi().searchBankAccount(
                    AuthUtil.generateParams(context, param)
            ).map(
                    new Func1<Response<TkpdResponse>, List<Bank>>() {
                        @Override
                        public List<Bank> call(Response<TkpdResponse> tkpdResponseResponse) {
                            ListBank.Data datas = new GsonBuilder().create().fromJson(tkpdResponseResponse.body().getStringData(), ListBank.Data.class);
                            //[START] read page from previous page
                            int page = 1;
                            try {
//                                if(!datas.getPaging().getUriPrevious().equals("0")){
//                                    Uri uri = Uri.parse(datas.getPaging().getUriPrevious());
//                                    page = Integer.parseInt(uri.getQueryParameter("page"))+1;
//                                }

                                if (!datas.getPaging().getUriNext().equals("0")) {
                                    Uri uri = Uri.parse(datas.getPaging().getUriNext());
                                    page = Integer.parseInt(uri.getQueryParameter("page")) - 1;
                                }
                            } catch (Exception e) {
                                Log.e("MNORMANSYAH", DataManagerImpl.class.getSimpleName() + e.getLocalizedMessage());
                            }
                            //[START] read page from previous page

                            List<Bank> bankList = null;
                            DatabaseWrapper database = FlowManager.getDatabase(DbFlowDatabase.NAME).getWritableDatabase();
                            database.beginTransaction();
                            try {
                                bankList = Bank.toDbs(datas.getList());
                                for (Bank bank : bankList) {

                                    bank.setBankPage(page);
                                    bank.save();
                                }
                                database.setTransactionSuccessful();
                            } finally {
                                database.endTransaction();
                            }
                            return bankList;
                        }
                    }
            ).subscribeOn(Schedulers.newThread())
                    .unsubscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            new Subscriber<List<Bank>>() {
                                @Override
                                public void onCompleted() {
                                    Log.d("MNORMANSYAH", DataManagerImpl.class.getSimpleName() + " onCompleted() ");
                                }

                                @Override
                                public void onError(Throwable e) {
                                    Log.e("MNORMANSYAH", DataManagerImpl.class.getSimpleName() + " onError : " + e.getLocalizedMessage());
                                    if (e instanceof UnknownHostException) {
                                        dataReceiver.onNetworkError("No Connection");
                                    } else {
                                        dataReceiver.onTimeout();
                                    }
//                                    dataReceiver.onMessageError(e.getLocalizedMessage());
                                }

                                @Override
                                public void onNext(List<Bank> banks) {
                                    Log.d("MNORMANSYAH", DataManagerImpl.class.getSimpleName() + " do nothing just log ");
                                    if (finalT == 1) {
                                        List<Bank> listBank = new Select().from(Bank.class).queryList();
                                        Log.d("MNORMANSYAH", "banks -> " + banks);
                                        if (banks.size() > 0)
                                            dataReceiver.setBank(listBank);
                                        else
                                            dataReceiver.onMessageError("Gagal mengambil data bank");
                                    }
                                }
                            }
                    );
        }

    }

    @Override
    public void getListShippingCity(Context context, final DataReceiver dataReceiver) {

        City city = new Select().from(City.class)
                .where(City_Table.cityId.eq("-1"))
                .querySingle();
        List<District> districts = null;
        if (city != null)
            districts = city.getDistricts();

        if (isShippingCityExpired() || districts == null || districts.size() <= 0) {
            final String url = TkpdBaseURL.Etc.URL_ADDRESS + TkpdBaseURL.Etc.PATH_GET_SHIPPING_CITY;
            NetworkCalculator networkCalculator = new NetworkCalculator(
                    NetworkConfig.GET, context,
                    url
            ).setIdentity()
                    .compileAllParam()
                    .finish();

            dataReceiver.getSubscription().add(
                    RetrofitUtils
                            .createRetrofit(TkpdBaseURL.BASE_DOMAIN)// DeveloperOptions.getUrlWsV4(context)
                            .create(AddressApi.class)
                            .getShippingCity(
                                    NetworkCalculator.getContentMd5(networkCalculator),
                                    NetworkCalculator.getDate(networkCalculator),
                                    NetworkCalculator.getAuthorization(networkCalculator),
                                    NetworkCalculator.getxMethod(networkCalculator),
                                    NetworkCalculator.getUserId(context),
                                    (NetworkCalculator.getDeviceId(context) != null && !NetworkCalculator.getDeviceId(context).equals(""))
                                            ? NetworkCalculator.getDeviceId(context) : "b",
                                    NetworkCalculator.getHash(networkCalculator),
                                    NetworkCalculator.getDeviceTime(networkCalculator)
                            )
                            .map(new Func1<Response<ListShippingCity>, Response<ListShippingCity>>() {
                                     @Override
                                     public Response<ListShippingCity> call(Response<ListShippingCity> listShippingCityResponse) {
                                         ListShippingCity lsc = listShippingCityResponse.body();
                                         ArrayList<ListShippingCity.Shipping_city> shippingCity = lsc.getData().getShippingCity();
                                         if (lsc.getData() != null && shippingCity != null) {
                                             DatabaseWrapper database = FlowManager.getDatabase(DbFlowDatabase.NAME).getWritableDatabase();
                                             database.beginTransaction();
                                             try {
                                                 Province province = new Province();
                                                 province.setProvinceId(-1 + "");
                                                 province.setProvinceName("all");
                                                 province.save();

                                                 City city = new City();
                                                 city.setCityId(-1 + "");
                                                 city.setCityName("all");
                                                 city.setProvince(province);
                                                 city.save();

                                                 for (District d : ListShippingCity.fromShippingCities(shippingCity)) {
                                                     d.setDistrictCity(city);
                                                     d.save();
                                                 }

                                                 String jdbtId = "2210,2228,5573,1940,1640,2197";
                                                 String jdbt = "Jabodetabek";

                                                 District d = new District();
                                                 d.setDistrictId(jdbtId);
                                                 d.setDistrictName(jdbt);
                                                 d.setDistrictCity(city);
                                                 d.setDistrictJneCode("-1");
                                                 database.setTransactionSuccessful();
                                             } finally {
                                                 database.endTransaction();
                                             }
                                         } else {
                                             throw new RuntimeException("tidak dapat mendapatkan daftar district");
                                         }

                                         LocalCacheHandler cache = new LocalCacheHandler(MainApplication.getAppContext(), SHIPPING_CITY_DURATION_STORAGE);
                                         cache.putLong(CACHE_DURATION, System.currentTimeMillis());
                                         cache.applyEditor();

                                         return listShippingCityResponse;
                                     }
                                 }

                            ).subscribeOn(Schedulers.newThread())
//                            .retry(5)
                            .observeOn(AndroidSchedulers.mainThread())
                            .unsubscribeOn(Schedulers.newThread())
                            .subscribe(
                                    new Subscriber<Response<ListShippingCity>>() {
                                        @Override
                                        public void onCompleted() {
                                            Log.d("MNORMANSYAH", "getListShippingCity onCompleted !!");
                                        }

                                        @Override
                                        public void onError(Throwable e) {
                                            e.printStackTrace();
                                            Log.e("MNORMANSYAH", DataManagerImpl.class.getSimpleName() + " -> " + e.getLocalizedMessage());
                                            if (e instanceof UnknownHostException) {
                                                dataReceiver.onNetworkError("No Connection");
                                            } else {
                                                dataReceiver.onTimeout();
                                            }
                                        }

                                        @Override
                                        public void onNext(Response<ListShippingCity> listShippingCityResponse) {
                                            City city = new Select().from(City.class)
                                                    .where(City_Table.cityId.eq("-1"))
                                                    .querySingle();
                                            List<District> districts = city.getDistricts();

                                            dataReceiver.setShippingCity(districts);
                                        }
                                    }

                            )
            );
//            ;
        } else {
            dataReceiver.setShippingCity(districts);
        }


    }



    private Boolean isShippingCityExpired() {
        long currTime = System.currentTimeMillis();
        long cacheTime = new LocalCacheHandler(MainApplication.getAppContext(), SHIPPING_CITY_DURATION_STORAGE).getLong(CACHE_DURATION, 0);
        long duration = currTime - cacheTime;
        return duration > CacheDuration.onDay(3);
    }

}
