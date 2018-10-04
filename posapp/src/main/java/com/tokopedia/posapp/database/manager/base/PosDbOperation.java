package com.tokopedia.posapp.database.manager.base;

import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.tokopedia.posapp.database.PosDatabase;

/**
 * Created by okasurya on 9/15/17.
 */

public abstract class PosDbOperation<T, D extends BaseModel> extends DbOperation<T, D> {
    @Override
    protected DatabaseDefinition getDatabase() {
        return FlowManager.getDatabase(PosDatabase.class);
    }
}
