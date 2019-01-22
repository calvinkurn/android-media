package com.tokopedia.core.gcm.database;

import com.raizlabs.android.dbflow.sql.language.ConditionGroup;
import com.raizlabs.android.dbflow.sql.language.OrderBy;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.List;

/**
 * Created by alvarisi on 2/22/17.
 */

public interface DbManagerPushNotificationOperation<T extends BaseModel, S> {
    void store(T data);

    void delete();

    S first(ConditionGroup conditionGroup);

    T first();

    List<S> getData(ConditionGroup conditionGroup);

    List<S> getDataWithOrderBy(ConditionGroup conditionGroup, List<OrderBy> orderBies);

    List<S> getDataWithOrderBy(ConditionGroup conditionGroup, OrderBy orderBy);

    List<S> getData();

    boolean isEmpty();

    void delete(ConditionGroup conditionGroup);
}
