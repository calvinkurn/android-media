package com.tokopedia.core.gcm.data.source;

import android.content.Context;

import com.raizlabs.android.dbflow.sql.language.ConditionGroup;
import com.raizlabs.android.dbflow.sql.language.OrderBy;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.tokopedia.core.gcm.FCMCacheManager;
import com.tokopedia.core.gcm.database.PushNotificationDbManager;
import com.tokopedia.core.gcm.database.model.DbPushNotification;
import com.tokopedia.core.gcm.database.model.DbPushNotification_Table;
import com.tokopedia.core.gcm.domain.PushNotification;
import com.tokopedia.core.gcm.data.DbPushNotificationMapper;
import com.tokopedia.core.gcm.data.PushNotificationDataStore;
import com.tokopedia.core.gcm.data.entity.FCMTokenUpdateEntity;
import com.tokopedia.core.gcm.model.DeviceRegistrationDataResponse;
import com.tokopedia.core.gcm.model.FCMTokenUpdate;
import com.tokopedia.core.util.PasswordGenerator;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

import static com.tokopedia.core.gcm.Constants.REGISTRATION_MESSAGE_ERROR;
import static com.tokopedia.core.gcm.Constants.REGISTRATION_STATUS_ERROR;

/**
 * @author by alvarisi on 1/5/17.
 */

public class DiskPushNotificationDataStore implements PushNotificationDataStore {
    private final Context mContext;
    private final PushNotificationDbManager pushNotificationDbManager;
    private final DbPushNotificationMapper dbPushNotificationMapper;

    public DiskPushNotificationDataStore(Context context) {
        mContext = context;
        pushNotificationDbManager = new PushNotificationDbManager();
        dbPushNotificationMapper = new DbPushNotificationMapper();
    }

    @Override
    public Observable<FCMTokenUpdateEntity> updateTokenServer(FCMTokenUpdate data) {
        return null;
    }

    public Observable<DeviceRegistrationDataResponse> deviceRegistration() {
        return Observable.just(true).map(new Func1<Boolean, DeviceRegistrationDataResponse>() {
            @Override
            public DeviceRegistrationDataResponse call(Boolean aBoolean) {
                DeviceRegistrationDataResponse response = new DeviceRegistrationDataResponse();
                response.setStatusCode(REGISTRATION_STATUS_ERROR);
                response.setDeviceRegistration(PasswordGenerator.getAppId(mContext));
                response.setStatusMessage(REGISTRATION_MESSAGE_ERROR);
                return response;
            }
        });
    }

    public Observable<Boolean> saveRegistrationDevice(String registrationDevice) {
        return Observable.just(registrationDevice).map(new Func1<String, Boolean>() {
            @Override
            public Boolean call(String req) {
                FCMCacheManager.storeRegId(req, mContext);
                return true;
            }
        });
    }

    public Observable<String> getRegistrationDevice() {
        return Observable.just( FCMCacheManager.getRegistrationId(mContext) );
    }

    @Override
    public Observable<List<PushNotification>> getSavedPushNotification() {
        return Observable.just(pushNotificationDbManager.getData()).map(new Func1<List<DbPushNotification>, List<PushNotification>>() {
            @Override
            public List<PushNotification> call(List<DbPushNotification> dbPushNotifications) {
                return dbPushNotificationMapper.transform(dbPushNotifications);
            }
        });
    }

    @Override
    public Observable<List<PushNotification>> getPushSavedPushNotification(String category) {
        ConditionGroup conditionGroup = ConditionGroup.clause();
        conditionGroup.and(DbPushNotification_Table.category.eq(category));
        return Observable.just(pushNotificationDbManager.getData(conditionGroup))
                .map(new Func1<List<DbPushNotification>, List<PushNotification>>() {
                    @Override
                    public List<PushNotification> call(List<DbPushNotification> dbPushNotifications) {
                        return dbPushNotificationMapper.transform(dbPushNotifications);
                    }
                }).onErrorReturn(new Func1<Throwable, List<PushNotification>>() {
                    @Override
                    public List<PushNotification> call(Throwable throwable) {
                        return new ArrayList<>();
                    }
                });
    }

    @Override
    public Observable<List<PushNotification>> getPushSavedPushNotificationWithOrderBy(String category, boolean ascendant) {
        ConditionGroup conditionGroup = ConditionGroup.clause();
        conditionGroup.and(DbPushNotification_Table.category.eq(category));
        List<OrderBy> orderBies = new ArrayList<>();
        OrderBy orderBy = OrderBy.fromProperty(DbPushNotification_Table.customIndex);
        if (ascendant) {
            orderBy.ascending();
        } else {
            orderBy.descending();
        }
        orderBies.add(orderBy);
        OrderBy idOrderBy = OrderBy.fromProperty(DbPushNotification_Table.id);
        idOrderBy.descending();
        orderBies.add(idOrderBy);
        return Observable.just(pushNotificationDbManager.getDataWithOrderBy(conditionGroup, orderBies))
                .map(new Func1<List<DbPushNotification>, List<PushNotification>>() {
                    @Override
                    public List<PushNotification> call(List<DbPushNotification> dbPushNotifications) {
                        return dbPushNotificationMapper.transform(dbPushNotifications);
                    }
                }).onErrorReturn(new Func1<Throwable, List<PushNotification>>() {
                    @Override
                    public List<PushNotification> call(Throwable throwable) {
                        return new ArrayList<>();
                    }
                });
    }

    @Override
    public Observable<Boolean> deleteSavedPushNotificationByCategory(String category) {
        return Observable.just(category).map(new Func1<String, Boolean>() {
            @Override
            public Boolean call(String category) {
                ConditionGroup conditionGroup = ConditionGroup.clause();
                conditionGroup.and(DbPushNotification_Table.category.eq(category));
                pushNotificationDbManager.delete(conditionGroup);
                return true;
            }
        }).onErrorReturn(new Func1<Throwable, Boolean>() {
            @Override
            public Boolean call(Throwable throwable) {
                return false;
            }
        });
    }

    @Override
    public Observable<Boolean> deleteSavedPushNotificationByCategoryAndServerId(String category, final String serverId) {
        return Observable.just(category).map(new Func1<String, Boolean>() {
            @Override
            public Boolean call(String category) {
                ConditionGroup conditionGroup = ConditionGroup.clause();
                conditionGroup.and(DbPushNotification_Table.category.eq(category));
                conditionGroup.and(DbPushNotification_Table.serverId.eq(serverId));
                pushNotificationDbManager.delete(conditionGroup);
                return true;
            }
        }).onErrorReturn(new Func1<Throwable, Boolean>() {
            @Override
            public Boolean call(Throwable throwable) {
                return false;
            }
        });
    }

    @Override
    public Observable<Boolean> deleteSavedPushNotification() {
        return Observable.just(true).map(new Func1<Boolean, Boolean>() {
            @Override
            public Boolean call(Boolean aBoolean) {
                pushNotificationDbManager.delete();
                return true;
            }
        }).onErrorReturn(new Func1<Throwable, Boolean>() {
            @Override
            public Boolean call(Throwable throwable) {
                return false;
            }
        });
    }

    @Override
    public Observable<Boolean> savePushNotification(String category, String response, String customIndex) {
        DbPushNotification dbPushNotification = new DbPushNotification();
        dbPushNotification.setCategory(category);
        dbPushNotification.setResponse(response);
        dbPushNotification.setCustomIndex(customIndex);
        dbPushNotification.setServerId("");

        return Observable
                .just(dbPushNotification)
                .map(new Func1<DbPushNotification, Boolean>() {
                    @Override
                    public Boolean call(DbPushNotification dbPushNotification) {
                        dbPushNotification.save();
                        return true;
                    }
                })
                .onErrorReturn(new Func1<Throwable, Boolean>() {
                    @Override
                    public Boolean call(Throwable throwable) {
                        return false;
                    }
                });
    }

    @Override
    public Observable<Boolean> savePushNotification(String category, String response) {
        DbPushNotification dbPushNotification = new DbPushNotification();
        dbPushNotification.setCategory(category);
        dbPushNotification.setResponse(response);
        dbPushNotification.setCustomIndex("");
        dbPushNotification.setServerId("");
        return Observable
                .just(dbPushNotification)
                .map(new Func1<DbPushNotification, Boolean>() {
                    @Override
                    public Boolean call(DbPushNotification dbPushNotification) {
                        dbPushNotification.save();
                        return true;
                    }
                })
                .onErrorReturn(new Func1<Throwable, Boolean>() {
                    @Override
                    public Boolean call(Throwable throwable) {
                        return false;
                    }
                });
    }

    @Override
    public Observable<String> savePushNotification(String category, String response, String customIndex, String serverId) {
        DbPushNotification dbPushNotification = new DbPushNotification();
        dbPushNotification.setCategory(category);
        dbPushNotification.setResponse(response);
        dbPushNotification.setCustomIndex(customIndex);
        dbPushNotification.setServerId(serverId);
        return Observable
                .just(dbPushNotification)
                .map(new Func1<DbPushNotification, String>() {
                    @Override
                    public String call(DbPushNotification dbPushNotification) {
                        dbPushNotification.save();
                        return dbPushNotification.getCategory();
                    }
                })
                .onErrorReturn(new Func1<Throwable, String>() {
                    @Override
                    public String call(Throwable throwable) {
                        return null;
                    }
                });
    }
}
