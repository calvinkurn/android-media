package com.tokopedia.posapp.database.manager;

import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.ConditionGroup;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.raizlabs.android.dbflow.structure.database.transaction.ITransaction;
import com.raizlabs.android.dbflow.structure.database.transaction.Transaction;
import com.tokopedia.core.database.DbFlowDatabase;
import com.tokopedia.posapp.database.model.ProductDb;
import com.tokopedia.posapp.database.model.ProductDb_Table;

import java.util.List;

/**
 * Created by okasurya on 8/30/17.
 */

public class ProductDbManager implements DbManagerOperation<ProductDb, ProductDb> {
    @Override
    public void store(ProductDb data, TransactionListener callback) {
        ProductDb existing = getExistingProduct(data.getProductId());
        if(existing != null) {
            data.setId(existing.getId());
            data.update();
        } else {
            data.save();
        }
    }

    @Override
    public void store(final List<ProductDb> data, final TransactionListener callback) {
        DatabaseDefinition database = FlowManager.getDatabase(DbFlowDatabase.class);
        Transaction transaction = database.beginTransactionAsync(new ITransaction() {
            @Override
            public void execute(DatabaseWrapper databaseWrapper) {
                for(ProductDb product: data) {
                    store(product, null);
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

    private ProductDb getExistingProduct(int productId) {
        return first(ConditionGroup.clause().and(ProductDb_Table.productId.eq(productId)));
    }

    @Override
    public void update(ProductDb data, TransactionListener callback) {
        data.update();
    }

    @Override
    public void delete(ConditionGroup conditions, TransactionListener callback) {
        ProductDb productDb = SQLite.select().from(ProductDb.class).where(conditions).querySingle();
        if(productDb != null) productDb.delete();
    }

    @Override
    public void deleteAll(TransactionListener callback) {
    }

    @Override
    public ProductDb first(ConditionGroup conditions) {
        return SQLite.select().from(ProductDb.class).where(conditions).querySingle();
    }

    @Override
    public ProductDb first() {
        return SQLite.select().from(ProductDb.class).querySingle();
    }

    @Override
    public List<ProductDb> getListData(ConditionGroup conditions) {
        return SQLite.select().from(ProductDb.class).where(conditions).queryList();
    }

    @Override
    public List<ProductDb> getListData(int offset, int limit) {
        return SQLite.select().from(ProductDb.class).offset(offset).limit(limit).queryList();
    }

    @Override
    public List<ProductDb> getAllData() {
        return SQLite.select().from(ProductDb.class).queryList();
    }

    @Override
    public boolean isTableEmpty() {
        return getAllData().size() == 0;
    }
}
