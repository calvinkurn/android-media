package com.tokopedia.posapp.database.manager;

import com.raizlabs.android.dbflow.sql.language.ConditionGroup;
import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.tokopedia.posapp.database.model.CartDB;

import java.util.List;

/**
 * Created by okasurya on 8/23/17.
 */

public class CartDbManager implements DbManagerOperation<CartDB, CartDB> {

    @Override
    public void store(CartDB data, TransactionListener callback) {
        data.save();
    }

    @Override
    public void store(List<CartDB> data, TransactionListener callback) {

    }

    @Override
    public void update(CartDB data, TransactionListener callback) {
        data.update();
    }

    @Override
    public void delete(ConditionGroup conditions, TransactionListener callback) {
        Delete.table(CartDB.class, conditions);
    }

    @Override
    public void deleteAll(TransactionListener callback) {
        Delete.table(CartDB.class);
    }

    @Override
    public CartDB first(ConditionGroup conditions) {
        return SQLite.select().from(CartDB.class).where(conditions).querySingle();
    }

    @Override
    public CartDB first() {
        return SQLite.select().from(CartDB.class).querySingle();
    }

    @Override
    public List<CartDB> getListData(ConditionGroup conditions) {
        return SQLite.select().from(CartDB.class).where(conditions).queryList();
    }

    @Override
    public List<CartDB> getListData(int offset, int limit) {
        return SQLite.select().from(CartDB.class).offset(offset).limit(limit).queryList();
    }

    @Override
    public List<CartDB> getAllData() {
        return SQLite.select().from(CartDB.class).queryList();
    }

    @Override
    public boolean isTableEmpty() {
        return getAllData().size() == 0;
    }
}
