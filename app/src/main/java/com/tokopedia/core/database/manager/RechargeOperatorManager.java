package com.tokopedia.core.database.manager;

import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.tokopedia.core.database.DbFlowDatabase;
import com.tokopedia.core.database.DbFlowOperation;
import com.tokopedia.core.database.model.RechargeOperatorModelDB;
import com.tokopedia.core.database.model.RechargeOperatorModelDB_Table;
import com.tokopedia.core.recharge.model.operator.Operator;

import java.util.List;

/**
 * @author ricoharisin on 7/15/16.
 */
public class RechargeOperatorManager implements DbFlowOperation<RechargeOperatorModelDB> {
    @Override
    public void store() {

    }

    @Override
    public void store(RechargeOperatorModelDB data) {

    }

    public void store(String prefix, String name, int operatorId, int status, String image, int minLength, int maxLength, String nominalText, Boolean showProduct, Boolean showPrice) {
        RechargeOperatorModelDB db = new RechargeOperatorModelDB();
        db.operatorId = operatorId;
        db.image = image;
        db.name = name;
        db.status = status;
        db.prefix = prefix;
        db.minimumLength = minLength;
        db.maximumLength = maxLength;
        db.nominalText =  nominalText;
        db.showPrice =  showPrice;
        db.showProduct = showProduct;
        db.save();
    }

    @Override
    public void delete(String key) {

    }

    @Override
    public void deleteAll(){
        new Delete().from(RechargeOperatorModelDB.class).execute();
    }

    @Override
    public boolean isExpired(long time) {
        return false;
    }

    @Override
    public RechargeOperatorModelDB getData(String prefix) {
        RechargeOperatorModelDB result =  new Select().from(RechargeOperatorModelDB.class)
                .where(RechargeOperatorModelDB_Table.prefix.is(prefix))
                .querySingle();

        if (result == null && prefix.length() == 4){
            result = new Select().from(RechargeOperatorModelDB.class)
                    .where(RechargeOperatorModelDB_Table.prefix.is(prefix.substring(0, 3)))
                    .querySingle();
        }
        return result;
    }

    public RechargeOperatorModelDB getDataOperator(int operatorId) {
        RechargeOperatorModelDB modelDB = new Select().from(RechargeOperatorModelDB.class)
                .where(RechargeOperatorModelDB_Table.operatorId.is(operatorId))
                .querySingle();
        return modelDB;
    }

    @Override
    public List<RechargeOperatorModelDB> getDataList(String key) {
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

    public void bulkInsert(List<Operator> data) {
        final DatabaseWrapper database = FlowManager.getDatabase(DbFlowDatabase.NAME).getWritableDatabase();
        database.beginTransaction();
        deleteAll();
        try {
            for (int i = 0; i < data.size(); i++) {
                Operator operator = data.get(i);
                if (operator.getAttributes().getPrefix().size() > 0) {
                    for (int k = 0; k < operator.getAttributes().getPrefix().size(); k++) {
                        store(operator.getAttributes().getPrefix().get(k),
                                operator.getAttributes().getName(),
                                operator.getId(),
                                operator.getAttributes().getStatus(),
                                operator.getAttributes().getImage(),
                                operator.getAttributes().getMinimumLength(),
                                operator.getAttributes().getMaximumLength(),
                                operator.getAttributes().getRule().getProductText(),
                                operator.getAttributes().getRule().getShowProduct(),
                                operator.getAttributes().getRule().getShowPrice()
                        );
                    }
                } else {
                    store(
                            null,
                            operator.getAttributes().getName(),
                            operator.getId(),
                            operator.getAttributes().getStatus(),
                            operator.getAttributes().getImage(),
                            operator.getAttributes().getMinimumLength(),
                            operator.getAttributes().getMaximumLength(),
                            operator.getAttributes().getRule().getProductText(),
                            operator.getAttributes().getRule().getShowProduct(),
                            operator.getAttributes().getRule().getShowPrice()
                    );
                }
            }
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
    }
}