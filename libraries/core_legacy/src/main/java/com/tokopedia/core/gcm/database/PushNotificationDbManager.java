package com.tokopedia.core.gcm.database;

import com.raizlabs.android.dbflow.sql.language.ConditionGroup;
import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.OrderBy;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.tokopedia.core.gcm.database.model.DbPushNotification;

import java.util.List;

/**
 * Created by alvarisi on 2/22/17.
 */

public class PushNotificationDbManager implements
        DbManagerPushNotificationOperation<DbPushNotification, DbPushNotification> {

    @Override
    public void delete() {
        Delete.table(DbPushNotification.class);
    }

    @Override
    public DbPushNotification first(ConditionGroup conditionGroup) {
        return SQLite.select().from(DbPushNotification.class)
                .where(conditionGroup)
                .querySingle();
    }


    @Override
    public DbPushNotification first() {
        return SQLite.select().from(DbPushNotification.class).querySingle();
    }

    @Override
    public List<DbPushNotification> getData(ConditionGroup conditionGroup) {
        return SQLite.select().from(DbPushNotification.class)
                .where(conditionGroup)
                .queryList();
    }

    @Override
    public List<DbPushNotification> getDataWithOrderBy(ConditionGroup conditionGroup, List<OrderBy> orderBies) {
        return SQLite.select().from(DbPushNotification.class)
                .where(conditionGroup)
                .orderByAll(orderBies)
                .queryList();
    }

    @Override
    public List<DbPushNotification> getDataWithOrderBy(ConditionGroup conditionGroup, OrderBy orderBy) {
        return SQLite.select().from(DbPushNotification.class)
                .where(conditionGroup)
                .orderBy(orderBy)
                .queryList();
    }

    @Override
    public List<DbPushNotification> getData() {
        return SQLite.select().from(DbPushNotification.class)
                .queryList();
    }


    @Override
    public void store(DbPushNotification data) {
        data.save();
    }



    @Override
    public boolean isEmpty() {
        List<DbPushNotification> dbPushNotifications = SQLite.select().from(DbPushNotification.class)
                .queryList();
        return dbPushNotifications.size() == 0;
    }


    @Override
    public void delete(ConditionGroup conditionGroup) {
        SQLite.delete(DbPushNotification.class)
                .where(conditionGroup)
                .execute();
    }
}
