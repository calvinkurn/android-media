package com.tokopedia.posapp.database.manager;

import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.ConditionGroup;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.raizlabs.android.dbflow.structure.database.transaction.ITransaction;
import com.raizlabs.android.dbflow.structure.database.transaction.Transaction;
import com.tokopedia.core.database.DbFlowDatabase;
import com.tokopedia.core.database.model.ProductDB;
import com.tokopedia.core.database.model.ProductDB_Table;

import java.util.List;

/**
 * Created by okasurya on 8/30/17.
 */

public class ProductDbManager implements DbManagerOperation<ProductDB, ProductDB> {
    @Override
    public void store(ProductDB data, TransactionListener callback) {
        data.save();
    }

    @Override
    public void store(final List<ProductDB> data, final TransactionListener callback) {
        DatabaseDefinition database = FlowManager.getDatabase(DbFlowDatabase.class);
        Transaction transaction = database.beginTransactionAsync(new ITransaction() {
            @Override
            public void execute(DatabaseWrapper databaseWrapper) {
                for(ProductDB product: data) {
                    ProductDB existing = getExistingProduct(product.getProductId());
                    if(existing != null) {
                        product.setId(existing.getId());
                        product.update();
                    } else {
                        product.save();
                    }
                }
            }
        }).success(new Transaction.Success() {
            @Override
            public void onSuccess(Transaction transaction) {
                callback.onTransactionSuccess();
            }
        }).error(new Transaction.Error() {
            @Override
            public void onError(Transaction transaction, Throwable error) {
                callback.onError(error);
            }
        }).build();

        transaction.execute();
    }

    private ProductDB getExistingProduct(int productId) {
        return first(ConditionGroup.clause().and(ProductDB_Table.productId.eq(productId)));
    }

    @Override
    public void update(ProductDB data, TransactionListener callback) {

    }

    @Override
    public void delete(ConditionGroup conditions, TransactionListener callback) {
        SQLite.select().from(ProductDB.class).where(conditions).querySingle().delete();
    }

    @Override
    public void deleteAll(TransactionListener callback) {
    }

    @Override
    public ProductDB first(ConditionGroup conditions) {
        return SQLite.select().from(ProductDB.class).where(conditions).querySingle();
    }

    @Override
    public ProductDB first() {
        return SQLite.select().from(ProductDB.class).querySingle();
    }

    @Override
    public List<ProductDB> getListData(ConditionGroup conditions) {
        return SQLite.select().from(ProductDB.class).where(conditions).queryList();
    }

    @Override
    public List<ProductDB> getListData(int offset, int limit) {
        return SQLite.select().from(ProductDB.class).offset(offset).limit(limit).queryList();
    }

    @Override
    public List<ProductDB> getAllData() {
        return SQLite.select().from(ProductDB.class).queryList();
    }

    @Override
    public boolean isTableEmpty() {
        return getAllData().size() == 0;
    }
}
