package com.tokopedia.core.database.manager;

import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.tokopedia.core.database.CacheUtil;
import com.tokopedia.core.database.DbFlowDatabase;
import com.tokopedia.core.database.DbFlowOperation;
import com.tokopedia.core.database.model.RechargeProductModelDB;
import com.tokopedia.core.database.model.RechargeProductModelDB_Table;
import com.tokopedia.core.recharge.model.product.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by ricoharisin on 7/14/16.
 * modified by Kulomady
 */
public class RechargeProductManager implements DbFlowOperation<RechargeProductModelDB> {

    private Product productRecharge;

    @SuppressWarnings("unused")
    public Product getProductRecharge() {
        return productRecharge;
    }

    @SuppressWarnings("unused")
    public void setProductRecharge(Product productRecharge) {
        this.productRecharge = productRecharge;
    }


    @Override
    public void store() {
        store(productRecharge);
    }

    @Override
    public void store(RechargeProductModelDB data) {

    }

    private void store(Product productRecharge) {
        RechargeProductModelDB db = new RechargeProductModelDB();
        db.id = productRecharge.getId();
        db.categoryId = productRecharge.getRelationships().getCategory().getData().getId();
        db.product = CacheUtil.convertModelToString(productRecharge, Product.class);
        db.OperatorId = productRecharge.getRelationships().getOperator().getData().getId();
        db.status = productRecharge.getAttributes().getStatus();
        db.save();
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
    public RechargeProductModelDB getData(String key) {
        return null;
    }

    @Override
    public List<RechargeProductModelDB> getDataList(String key) {
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

    public void bulkInsert(List<Product> productList) {
        final DatabaseWrapper database = FlowManager.getDatabase(DbFlowDatabase.NAME).getWritableDatabase();
        database.beginTransaction();
        try {
            for (int i = 0; i < productList.size(); i++) {
                store(productList.get(i));
            }
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
    }

    public List<Product> getListDataByCategory(int categoryId) {
        List<RechargeProductModelDB> db = new Select().from(RechargeProductModelDB.class)
                .where(RechargeProductModelDB_Table.categoryId.is(categoryId))
                .and(RechargeProductModelDB_Table.status.isNot(2))
                .queryList();

        List<Product> productList = new ArrayList<>();
        for (int i = 0; i < db.size(); i++) {
            productList.add((Product) CacheUtil.convertStringToModel(db.get(i).product, Product.class));
        }
        return productList;
    }

    @SuppressWarnings("unused")
    public List<Product> getDataByOperator(int operatorId) {
        List<RechargeProductModelDB> db = new Select().from(RechargeProductModelDB.class)
                .where(RechargeProductModelDB_Table.OperatorId.is(operatorId))
                .and(RechargeProductModelDB_Table.status.isNot(2))
                .queryList();
        List<Product> productList = new ArrayList<>();
        for (int i = 0; i < db.size(); i++) {
            productList.add((Product) CacheUtil.convertStringToModel(db.get(i).product, Product.class));
        }
        return productList;
    }

    public List<Product> getListData(int categoryId, int operatorId) {
        List<RechargeProductModelDB> db = new Select().from(RechargeProductModelDB.class)
                .where(RechargeProductModelDB_Table.OperatorId.is(operatorId))
                .and(RechargeProductModelDB_Table.categoryId.is(categoryId))
                .and(RechargeProductModelDB_Table.status.isNot(2))
                .queryList();
        List<Product> productList = new ArrayList<>();
        for (int i = 0; i < db.size(); i++) {
            productList.add((Product) CacheUtil.convertStringToModel(db.get(i).product, Product.class));
        }
        return productList;
    }
}