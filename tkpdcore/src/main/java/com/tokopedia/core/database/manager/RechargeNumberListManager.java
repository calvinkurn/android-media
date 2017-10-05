package com.tokopedia.core.database.manager;

import android.util.Log;

import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.tokopedia.core.database.DbFlowDatabase;
import com.tokopedia.core.database.DbFlowOperation;
import com.tokopedia.core.database.model.RechargeNumberListModelDB;
import com.tokopedia.core.database.model.RechargeNumberListModelDB_Table;
import com.tokopedia.core.database.recharge.numberList.NumberBean;

import java.util.List;

/**
 * @author rizkyfadillah on 9/28/2017.
 */

public class RechargeNumberListManager implements DbFlowOperation<RechargeNumberListModelDB> {

    private NumberBean numberBean;

    @SuppressWarnings("unused")
    public NumberBean getNumberBean() {
        return numberBean;
    }

    @SuppressWarnings("unused")
    public void setNumberBean(NumberBean numberBean) {
        this.numberBean = numberBean;
    }

    @Override
    public void store() {
        store(numberBean);
    }

    @Override
    public void store(RechargeNumberListModelDB data) {

    }

    public void store(NumberBean number) {
        try {
            RechargeNumberListModelDB db = new RechargeNumberListModelDB();
            db.clientNumber = number.getAttributes().getClient_number();
            db.name = number.getAttributes().getLabel();
            db.lastProduct = number.getAttributes().getLast_product();
            db.lastUpdated = number.getAttributes().getLast_updated();
            db.categoryId = number.getRelationships().getCategory().getData().getId();
            db.save();
        } catch (NullPointerException e) {
            Log.d(RechargeNumberListManager.class.getSimpleName(), e.toString());
        }
    }

    @Override
    public void delete(String key) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public boolean isExpired(long time) {
        return false;
    }

    @Override
    public RechargeNumberListModelDB getData(String key) {
        return null;
    }

    @Override
    public List<RechargeNumberListModelDB> getDataList(String key) {
        return null;
    }

    @Override
    public String getValueString(String key) {
        return null;
    }

    @Override
    public <Z> Z getConvertObjData(String key, Class<Z> clazz) {
        return null;
    }

    public void bulkInsert(List<NumberBean> numberListBean) {
        final DatabaseWrapper database = FlowManager.getDatabase(DbFlowDatabase.NAME).getWritableDatabase();
        database.beginTransaction();
        try {
            for (int i = 0; i < numberListBean.size(); i++) {
                store(numberListBean.get(i));
            }
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
    }

    public List<RechargeNumberListModelDB> getNumberListByCategory(int id) {
        List<RechargeNumberListModelDB> db = new Select().from(RechargeNumberListModelDB.class)
                .where(RechargeNumberListModelDB_Table.categoryId.is(id))
                .queryList();

        return db;
    }

    public RechargeNumberListModelDB getLastOrderById(int id) {
        RechargeNumberListModelDB db = new Select().from(RechargeNumberListModelDB.class)
                .where(RechargeNumberListModelDB_Table.categoryId.is(id),
                        RechargeNumberListModelDB_Table.lastProduct.isNot(""))
                .orderBy(RechargeNumberListModelDB_Table.lastUpdated, false)
                .querySingle();

        return db;
    }

}
