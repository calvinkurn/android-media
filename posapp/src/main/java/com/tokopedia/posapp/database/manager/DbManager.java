package com.tokopedia.posapp.database.manager;

import com.raizlabs.android.dbflow.sql.language.ConditionGroup;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.database.transaction.Transaction;
import com.tokopedia.core.analytics.TrackingConfig;

import java.util.List;

/**
 * Created by okasurya on 8/23/17.
 */

public interface DbManager<T, S>  {
    void store(T data, TransactionListener callback);

    void store(List<T> data, TransactionListener callback);

    void update(T data, TransactionListener callback);

    void delete(ConditionGroup conditions, TransactionListener callback);

    void delete(T data, TransactionListener callback);

    void deleteAll(TransactionListener callback);

    S first(ConditionGroup conditions);

    S first();

    List<S> getListData(ConditionGroup conditions);

    List<S> getListData(ConditionGroup conditions, int offset, int limit);

    List<S> getListData(int offset, int limit);

    List<S> getAllData();

    boolean isTableEmpty();

    interface TransactionListener {
        void onTransactionSuccess();

        void onError(Throwable throwable);
    }
}
