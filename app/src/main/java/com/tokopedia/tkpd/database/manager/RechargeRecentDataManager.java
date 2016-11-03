package com.tokopedia.tkpd.database.manager;

import android.util.Log;

import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.tokopedia.tkpd.database.DbFlowDatabase;
import com.tokopedia.tkpd.database.DbFlowOperation;
import com.tokopedia.tkpd.database.model.RechargeRecentDataModelDB;
import com.tokopedia.tkpd.database.model.RechargeRecentDataModelDB_Table;
import com.tokopedia.tkpd.recharge.model.recentNumber.RecentDataBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by ricoharisin on 7/14/16.
 *         modified by Kulomady
 */
public class RechargeRecentDataManager implements DbFlowOperation<RechargeRecentDataModelDB> {

    private RecentDataBean recentDataBean;

    @SuppressWarnings("unused")
    public RecentDataBean getRecentDataBean() {
        return recentDataBean;
    }

    @SuppressWarnings("unused")
    public void setRecentDataBean(RecentDataBean recentDataBean) {
        this.recentDataBean = recentDataBean;
    }


    @Override
    public void store() {
        store(recentDataBean);
    }

    @Override
    public void store(RechargeRecentDataModelDB data) {

    }

    private void store(RecentDataBean productRecharge) {
        try {
            RechargeRecentDataModelDB db = new RechargeRecentDataModelDB();
            db.clientNumber = productRecharge.getAttributes().getClient_number();
            db.categoryId = productRecharge.getRelationships().getCategory().getData().getId();
            db.save();
        } catch (NullPointerException e) {
            Log.d(RechargeRecentDataManager.class.getSimpleName(), e.toString());
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
    public RechargeRecentDataModelDB getData(String key) {
        return null;
    }

    @Override
    public List<RechargeRecentDataModelDB> getDataList(String key) {
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

    public void bulkInsert(List<RecentDataBean> recentDataBeanList) {
        final DatabaseWrapper database = FlowManager.getDatabase(DbFlowDatabase.NAME).getWritableDatabase();
        database.beginTransaction();
        try {
            for (int i = 0; i < recentDataBeanList.size(); i++) {
                store(recentDataBeanList.get(i));
            }
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
    }

    public List<String> getListDataByCategory(int id) {
        List<RechargeRecentDataModelDB> db = new Select().from(RechargeRecentDataModelDB.class)
                .where(RechargeRecentDataModelDB_Table.categoryId.is(id))
                .queryList();

        List<String> productList = new ArrayList<>();
        for (int i = 0; i < db.size(); i++) {
            productList.add(db.get(i).clientNumber);
        }
        return productList;
    }
}