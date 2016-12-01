package com.tokopedia.core.database.container;

import android.support.annotation.NonNull;

import com.raizlabs.android.dbflow.structure.container.ForeignKeyContainer;
import com.raizlabs.android.dbflow.structure.container.ModelContainer;
import com.tokopedia.core.database.model.ProductDB;

import java.util.Map;

/**
 * Created by normansyahputa on 10/6/16.
 */

public class ProductDbContainer extends ForeignKeyContainer<ProductDB> {
    public ProductDbContainer(Class<ProductDB> table) {
        super(table);
    }

    public ProductDbContainer(Class<ProductDB> table, Map<String, Object> data) {
        super(table, data);
    }

    public ProductDbContainer(@NonNull ModelContainer<ProductDB, ?> existingContainer) {
        super(existingContainer);
    }
}
