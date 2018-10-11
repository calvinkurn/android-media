package com.tokopedia.logisticaddaddress.network;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.GsonBuilder;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.tkpd.library.utils.data.model.ListCity;
import com.tkpd.library.utils.data.model.ListDistricts;
import com.tkpd.library.utils.data.model.ListProvince;
import com.tokopedia.abstraction.common.network.response.TokopediaWsV4Response;
import com.tokopedia.core.database.model.City_Table;
import com.tokopedia.core.database.model.Province_Table;
import com.tokopedia.logisticdata.data.apiservice.AddressApi;
import com.tokopedia.logisticdata.data.apiservice.PeopleActApi;
import com.tokopedia.logisticdata.data.entity.address.FormAddressDomainModel;
import com.tokopedia.logisticdata.data.entity.address.db.City;
import com.tokopedia.logisticdata.data.entity.address.db.District;
import com.tokopedia.logisticdata.data.entity.address.db.Province;
import com.tokopedia.network.utils.AuthUtil;
import com.tokopedia.network.utils.TKPDMapParam;
import com.tokopedia.user.session.UserSession;

import org.json.JSONException;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by nisie on 9/6/16.
 * <p>
 * migrate retrofit 2 by Angga.Prasetiyo
 */
public class AddAddressRetrofitInteractorImpl implements AddressRepository {

    private static final String TAG = AddAddressRetrofitInteractorImpl.class.getSimpleName();
    private static final String DB_NAME = "tokopedia";

    private final CompositeSubscription compositeSubscription;
    private final PeopleActApi peopleActService;
    private final AddressApi addressService;

    public AddAddressRetrofitInteractorImpl(PeopleActApi peopleActApi, AddressApi addressApi) {
        this.compositeSubscription = new CompositeSubscription();
        this.peopleActService = peopleActApi;
        this.addressService = addressApi;
    }

    @Override
    public void addAddress(@NonNull Context context, @NonNull Map<String, String> params, @NonNull final AddAddressListener listener) {
        Observable<Response<TokopediaWsV4Response>> observable = peopleActService
                .addAddress(params);

        Subscriber<Response<TokopediaWsV4Response>> subscriber = new Subscriber<Response<TokopediaWsV4Response>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, e.toString());
                if (e instanceof UnknownHostException) {
                    listener.onNoNetworkConnection();
                } else if (e instanceof SocketTimeoutException) {
                    listener.onTimeout();
                } else {
                    listener.onError("");
                }
            }

            @Override
            public void onNext(Response<TokopediaWsV4Response> response) {
                if (response.isSuccessful()) {
                    if (!response.body().isError()) {
                        try {
                            listener.onSuccess(response.body().getJsonData().getString("address_id"));
                        } catch (JSONException e) {
                            listener.onSuccess("");
                        }
                    } else {
                        if (response.body().isNullData()) listener.onNullData();
                        else listener.onError(response.body().getErrorMessages().get(0));
                    }
                } else {
                    listener.onError(response.message());
                }
            }
        };
        compositeSubscription.add(observable.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));
    }

    @Override
    public void editAddress(@NonNull Context context, @NonNull Map<String, String> params, @NonNull final AddAddressListener listener) {
        Observable<Response<TokopediaWsV4Response>> observable = peopleActService
                .editAddAddress(params);

        Subscriber<Response<TokopediaWsV4Response>> subscriber = new Subscriber<Response<TokopediaWsV4Response>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, e.toString());
                if (e instanceof UnknownHostException) {
                    listener.onNoNetworkConnection();
                } else if (e instanceof SocketTimeoutException) {
                    listener.onTimeout();
                } else {
                    listener.onError("");
                }
            }

            @Override
            public void onNext(Response<TokopediaWsV4Response> response) {
                if (response.isSuccessful()) {
                    if (!response.body().isError()) {
                        try {
                            listener.onSuccess(response.body().getJsonData().getString("address_id"));
                        } catch (JSONException e) {
                            listener.onSuccess("");
                        }
                    } else {
                        if (response.body().isNullData()) listener.onNullData();
                        else listener.onError(response.body().getErrorMessages().get(0));
                    }
                } else {
                    listener.onError(response.message());
                }
            }
        };
        compositeSubscription.add(observable.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));
    }

    @Override
    public void unsubscribe() {
        compositeSubscription.unsubscribe();
    }

    @Override
    public void getListProvince(@NonNull Context context,
                                @NonNull Map<String, String> params,
                                @NonNull final GetListProvinceListener listener) {
        Observable<Response<TokopediaWsV4Response>> observable = addressService
                .getProvince(params)
                .doOnNext(new Action1<Response<TokopediaWsV4Response>>() {
                    @Override
                    public void call(Response<TokopediaWsV4Response> response) {
                        ListProvince.Data datas = new GsonBuilder().create().fromJson(response.body().getStringData(), ListProvince.Data.class);
                        DatabaseWrapper database = FlowManager.getDatabase(DB_NAME).getWritableDatabase();
                        database.beginTransaction();
                        try {
                            for (Province a : Province.toDbs(datas.getProvinces())) {
                                a.save();
                            }
                            database.setTransactionSuccessful();
                        } finally {
                            database.endTransaction();
                        }
                    }
                });

        Subscriber<Response<TokopediaWsV4Response>> subscriber = new Subscriber<Response<TokopediaWsV4Response>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, e.toString());
                if (e instanceof IOException) {
                    listener.onTimeout();
                } else {
                    listener.onError("");
                }
            }

            @Override
            public void onNext(Response<TokopediaWsV4Response> response) {
                if (response.isSuccessful()) {
                    if (!response.body().isError()) {
                        ListProvince.Data datas = new GsonBuilder().create().fromJson(response.body().getStringData(), ListProvince.Data.class);
                        ArrayList<Province> provinces = new ArrayList<Province>();
                        for (ListProvince.Province a : datas.getProvinces()) {
                            provinces.add(Province.fromNetwork(a));
                        }
                        listener.onSuccess(provinces);
                    } else {
                        if (response.body().getErrorMessages() != null
                                && !response.body().getErrorMessages().isEmpty()) {
                            listener.onError(response.body().getErrorMessages().get(0));
                        } else {
                            listener.onNullData();
                        }
                    }
                } else {
                    listener.onError(response.message());
                }
            }
        };
        compositeSubscription.add(observable.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));
    }

    @Override
    public void getListDistrict(@NonNull Context context,
                                @NonNull String cityId,
                                @NonNull final GetListDistrictListener listener) {
        compositeSubscription.add(Observable.concat(getDistrictFromDB(cityId), getDistrictFromNetwork(context, cityId))
                .first(new Func1<FormAddressDomainModel, Boolean>() {
                    @Override
                    public Boolean call(FormAddressDomainModel model) {
                        List<District> districts = model.getDistricts();
                        return districts != null && !districts.isEmpty();
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<FormAddressDomainModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, e.toString());
                        if (e instanceof IOException) {
                            listener.onTimeout();
                        } else {
                            listener.onError("");
                        }
                    }

                    @Override
                    public void onNext(FormAddressDomainModel model) {
                        if (model.isValid()) {
                            listener.onSuccess(model);
                        } else {
                            listener.onError(model.getErrorMessage());
                        }
                    }
                })
        );
    }

    private Observable<FormAddressDomainModel> getDistrictFromNetwork(Context context, String cityId) {
        TKPDMapParam<String, String> cityParam = new TKPDMapParam<>();
        cityParam.put("city_id", cityId);
        TKPDMapParam<String, String> params = generateTKPDParam(context, cityParam);
        return Observable.zip(
                Observable.just(cityId),
                addressService.getDistrict(params),
                new Func2<String, Response<TokopediaWsV4Response>, Response<TokopediaWsV4Response>>() {
                    @Override
                    public Response<TokopediaWsV4Response> call(String cityId, Response<TokopediaWsV4Response> response) {
                        ListDistricts.Data datas = new GsonBuilder().create().fromJson(response.body().getStringData(), ListDistricts.Data.class);
                        DatabaseWrapper database = FlowManager.getDatabase(DB_NAME).getWritableDatabase();
                        database.beginTransaction();
                        try {
                            for (District district : District.toDbs(datas.getDistricts())) {
                                district.setDistrictCity(new Select().from(City.class)
                                        .where(City_Table.cityId.eq(cityId))
                                        .querySingle());
                                district.save();
                            }
                            database.setTransactionSuccessful();
                        } finally {
                            database.endTransaction();
                        }
                        return response;
                    }
                })
                .map(new Func1<Response<TokopediaWsV4Response>, FormAddressDomainModel>() {
                    @Override
                    public FormAddressDomainModel call(Response<TokopediaWsV4Response> response) {
                        FormAddressDomainModel model = new FormAddressDomainModel();
                        if (response.isSuccessful()) {
                            if (!response.body().isError()) {
                                ListDistricts.Data datas = new GsonBuilder().create().fromJson(response.body().getStringData(), ListDistricts.Data.class);
                                ArrayList<District> districts = new ArrayList<District>();
                                for (ListDistricts.Districts a : datas.getDistricts()) {
                                    districts.add(District.fromNetwork(a));
                                }
                                model.setValid(true);
                                model.setDistricts(districts);
                            } else {
                                model.setValid(false);
                                model.setErrorMessage(response.body().getErrorMessages().get(0));
                            }
                        } else {
                            model.setValid(false);
                            model.setErrorCode(response.code());
                        }
                        return model;
                    }
                });
    }

    private Observable<FormAddressDomainModel> getDistrictFromDB(String cityId) {
        return Observable.just(cityId)
                .map(new Func1<String, FormAddressDomainModel>() {
                    @Override
                    public FormAddressDomainModel call(String cityId) {
                        final City city = new Select().from(City.class)
                                .where(City_Table.cityId.eq(cityId))
                                .querySingle();
                        FormAddressDomainModel model = new FormAddressDomainModel();
                        if (city != null) {
                            model.setValid(true);
                            model.setDistricts(city.getDistricts());
                        } else {
                            model.setValid(false);
                            model.setDistricts(null);
                        }
                        return model;
                    }
                });
    }

    @Override
    public void getListCity(@NonNull Context context,
                            @NonNull String provinceId,
                            @NonNull final GetListCityListener listener) {
        compositeSubscription.add(Observable.concat(getCityFromDB(provinceId), getCityFromNetwork(context, provinceId))
                .first(new Func1<FormAddressDomainModel, Boolean>() {
                    @Override
                    public Boolean call(FormAddressDomainModel model) {
                        List<City> cities = model.getCities();
                        return cities != null && !cities.isEmpty();
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<FormAddressDomainModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, e.toString());
                        if (e instanceof IOException) {
                            listener.onTimeout();
                        } else {
                            listener.onError("");
                        }
                    }

                    @Override
                    public void onNext(FormAddressDomainModel model) {
                        if (model.isValid()) {
                            listener.onSuccess(model);
                        } else {
                            listener.onError(model.getErrorMessage());
                        }
                    }
                })
        );
    }

    private Observable<FormAddressDomainModel> getCityFromNetwork(Context context, String provinceId) {
        TKPDMapParam<String, String> provParam = new TKPDMapParam<>();
        provParam.put("province_id", provinceId);
        TKPDMapParam<String, String> params = generateTKPDParam(context, provParam);
        return Observable.zip(
                Observable.just(provinceId),
                addressService.getCity(params),
                new Func2<String, Response<TokopediaWsV4Response>, Response<TokopediaWsV4Response>>() {
                    @Override
                    public Response<TokopediaWsV4Response> call(String provinceId, Response<TokopediaWsV4Response> response) {
                        ListCity.Data datas = new GsonBuilder().create().fromJson(response.body().getStringData(), ListCity.Data.class);
                        DatabaseWrapper database = FlowManager.getDatabase(DB_NAME).getWritableDatabase();
                        database.beginTransaction();
                        try {
                            for (City city : City.toDbs(datas.getCities())) {
                                city.setProvince(new Select()
                                        .from(Province.class)
                                        .where(Province_Table.provinceId.eq(provinceId))
                                        .querySingle());
                                city.save();
                            }
                            database.setTransactionSuccessful();
                        } finally {
                            database.endTransaction();
                        }
                        return response;
                    }
                })
                .map(new Func1<Response<TokopediaWsV4Response>, FormAddressDomainModel>() {
                    @Override
                    public FormAddressDomainModel call(Response<TokopediaWsV4Response> response) {
                        FormAddressDomainModel model = new FormAddressDomainModel();
                        if (response.isSuccessful()) {
                            if (!response.body().isError()) {
                                ListCity.Data datas = new GsonBuilder().create().fromJson(response.body().getStringData(), ListCity.Data.class);
                                ArrayList<City> cities = new ArrayList<City>();
                                for (ListCity.Cities a : datas.getCities()) {
                                    cities.add(City.fromNetwork(a.getCity_id()));
                                }
                                model.setValid(true);
                                model.setCities(cities);
                            } else {
                                model.setValid(false);
                                model.setErrorMessage(response.body().getErrorMessages().get(0));
                            }
                        } else {
                            model.setValid(false);
                            model.setErrorCode(response.code());
                        }
                        return model;
                    }
                });
    }

    private Observable<FormAddressDomainModel> getCityFromDB(String provinceId) {
        return Observable.just(provinceId)
                .map(new Func1<String, FormAddressDomainModel>() {
                    @Override
                    public FormAddressDomainModel call(String provinceId) {
                        final Province province = new Select()
                                .from(Province.class)
                                .where(Province_Table.provinceId.eq(provinceId))
                                .querySingle();
                        FormAddressDomainModel model = new FormAddressDomainModel();
                        if (province != null) {
                            model.setValid(true);
                            model.setCities(province.getCities());
                        } else {
                            model.setValid(false);
                            model.setCities(null);
                        }
                        return model;
                    }
                });
    }

    //todo : refactor this to network repository class
    private TKPDMapParam<String, String> generateTKPDParam(Context context, TKPDMapParam<String, String> param) {
        UserSession session = new UserSession(context);
        return AuthUtil.generateParamsNetwork(session.getUserId(), session.getDeviceId(), param);
    }

}
