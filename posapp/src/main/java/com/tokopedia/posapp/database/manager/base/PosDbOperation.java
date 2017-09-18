package com.tokopedia.posapp.database.manager.base;

import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.ConditionGroup;
import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.raizlabs.android.dbflow.structure.database.transaction.ITransaction;
import com.raizlabs.android.dbflow.structure.database.transaction.Transaction;
import com.tokopedia.posapp.database.PosDatabase;
import com.tokopedia.posapp.database.QueryParameter;

import java.util.List;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by okasurya on 9/15/17.
 */

public abstract class PosDbOperation<T, D extends BaseModel> extends DbOperation<T, D> {
    @Override
    DatabaseDefinition getDatabase() {
        return FlowManager.getDatabase(PosDatabase.class);
    }
}
