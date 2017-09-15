package com.tokopedia.posapp.database.manager;

import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.ConditionGroup;
import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.raizlabs.android.dbflow.structure.database.transaction.ITransaction;
import com.raizlabs.android.dbflow.structure.database.transaction.Transaction;
import com.tokopedia.posapp.database.PosDatabase;
import com.tokopedia.posapp.database.model.CartDb_Table;
import com.tokopedia.posapp.database.model.CartDb;
import com.tokopedia.posapp.domain.model.cart.CartDomain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by okasurya on 8/23/17.
 */

public class CartDbManager implements DbManager<CartDomain, CartDomain> {

    @Override
    public void store(CartDomain data, TransactionListener callback) {
        store(mapToDb(data), callback);
    }

    @Override
    public void store(final List<CartDomain> data, final TransactionListener callback) {
        DatabaseDefinition database = FlowManager.getDatabase(PosDatabase.class);
        Transaction transaction = database.beginTransactionAsync(new ITransaction() {
            @Override
            public void execute(DatabaseWrapper databaseWrapper) {
                for(CartDomain cartDomain : data) {
                    mapToDb(cartDomain).save();
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

    @Override
    public void update(CartDomain data, TransactionListener callback) {
        update(mapToDb(data), callback);
    }

    @Override
    public void delete(final ConditionGroup conditions, final TransactionListener callback) {
        DatabaseDefinition database = FlowManager.getDatabase(PosDatabase.class);
        Transaction transaction = database.beginTransactionAsync(new ITransaction() {
            @Override
            public void execute(DatabaseWrapper databaseWrapper) {
                Delete.table(CartDb.class, conditions);
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

    @Override
    public void delete(final CartDomain data, final TransactionListener callback) {
        DatabaseDefinition database = FlowManager.getDatabase(PosDatabase.class);
        Transaction transaction = database.beginTransactionAsync(new ITransaction() {
            @Override
            public void execute(DatabaseWrapper databaseWrapper) {
                Delete.table(CartDb.class, CartDb_Table.productId.eq(data.getProductId()));
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

    @Override
    public void deleteAll(final TransactionListener callback) {
        DatabaseDefinition database = FlowManager.getDatabase(PosDatabase.class);
        Transaction transaction = database.beginTransactionAsync(new ITransaction() {
            @Override
            public void execute(DatabaseWrapper databaseWrapper) {
                Delete.table(CartDb.class);
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

    @Override
    public CartDomain first(ConditionGroup conditions) {
        return mapToDomain(SQLite.select().from(CartDb.class).where(conditions).querySingle());
    }

    @Override
    public CartDomain first() {
        return mapToDomain(SQLite.select().from(CartDb.class).querySingle());
    }

    @Override
    public List<CartDomain> getListData(ConditionGroup conditions) {
        return mapToDomain(SQLite.select().from(CartDb.class).where(conditions).queryList());

    }

    @Override
    public List<CartDomain> getListData(int offset, int limit) {
        return mapToDomain(SQLite.select().from(CartDb.class).offset(offset).limit(limit).queryList());
    }

    @Override
    public List<CartDomain> getAllData() {
        return mapToDomain(SQLite.select().from(CartDb.class).queryList());
    }

    @Override
    public boolean isTableEmpty() {
        return getAllData().size() == 0;
    }

    private void store(final CartDb data, final TransactionListener callback) {
        DatabaseDefinition database = FlowManager.getDatabase(PosDatabase.class);
        Transaction transaction = database.beginTransactionAsync(new ITransaction() {
            @Override
            public void execute(DatabaseWrapper databaseWrapper) {
                data.save();
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

    private void update(final CartDb cartDb, final TransactionListener callback) {
        DatabaseDefinition database = FlowManager.getDatabase(PosDatabase.class);
        Transaction transaction = database.beginTransactionAsync(new ITransaction() {
            @Override
            public void execute(DatabaseWrapper databaseWrapper) {
                cartDb.update();
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

    private List<CartDomain> mapToDomain(List<CartDb> cartDbs) {
        List<CartDomain> cartDomains = new ArrayList<>();
        for(CartDb cartDb : cartDbs) {
            CartDomain cartDomain = mapToDomain(cartDb);
            if(cartDomain != null) cartDomains.add(cartDomain);
        }

        return cartDomains;
    }

    private CartDomain mapToDomain(CartDb cartDb) {
        if(cartDb != null) {
            CartDomain cartDomain = new CartDomain();
            cartDomain.setProductId(cartDb.getProductId());
            cartDomain.setQuantity(cartDb.getQuantity());
            cartDomain.setOutletId(cartDb.getOutletId());
            return cartDomain;
        }

        return null;
    }

    private List<CartDb> mapToDb(List<CartDomain> cartDomains) {
        List<CartDb> cartDbs = new ArrayList<>();
        for(CartDomain cartDomain : cartDomains) {
            CartDb cartDb = mapToDb(cartDomain);
            if(cartDb != null) cartDbs.add(cartDb);
        }

        return cartDbs;
    }

    private CartDb mapToDb(CartDomain cartDomain) {
        if(cartDomain != null) {
            CartDb cartDb = new CartDb();
            cartDb.setProductId(cartDomain.getProductId());
            cartDb.setQuantity(cartDomain.getQuantity());
            cartDb.setOutletId(cartDomain.getOutletId());
            return cartDb;
        }

        return null;
    }
}
