package com.tokopedia.posapp.database.model.container;

import android.support.annotation.NonNull;

import com.raizlabs.android.dbflow.structure.container.ForeignKeyContainer;
import com.raizlabs.android.dbflow.structure.container.ModelContainer;
import com.tokopedia.posapp.database.model.BankDb;

import java.util.Map;

/**
 * Created by okasurya on 9/8/17.
 */

public class BankDbContainer extends ForeignKeyContainer<BankDb> {
    public BankDbContainer(Class<BankDb> table) {
        super(table);
    }

    public BankDbContainer(Class<BankDb> table, Map<String, Object> data) {
        super(table, data);
    }

    public BankDbContainer(@NonNull ModelContainer<BankDb, ?> existingContainer) {
        super(existingContainer);
    }
}
