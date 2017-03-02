package com.tokopedia.core.database.manager;

import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.tokopedia.core.database.DbFlowDatabase;
import com.tokopedia.core.database.DbFlowOperation;
import com.tokopedia.core.database.model.RechargeOperatorModel;
import com.tokopedia.core.database.model.RechargeOperatorModel_Table;
import com.tokopedia.core.database.recharge.operator.Operator;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ricoharisin on 7/15/16.
 */
public class RechargeOperatorManager  implements DbFlowOperation<RechargeOperatorModel> {
    @Override
    public void store() {

    }

    @Override
    public void store(RechargeOperatorModel data) {

    }

    public void store(String prefix, String name, int operatorId, int status, String image,
                      int minLength, int maxLength, String nominalText, Boolean showProduct,
                      Boolean showPrice, int weight) {
        RechargeOperatorModel db = new RechargeOperatorModel();
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
        db.weight = weight;
        db.save();
    }

    @Override
    public void delete(String key) {

    }

    @Override
    public void deleteAll(){
        new Delete().from(RechargeOperatorModel.class).execute();
    }

    @Override
    public boolean isExpired(long time) {
        return false;
    }

    @Override
    public RechargeOperatorModel getData(String prefix) {
        RechargeOperatorModel result =  new Select().from(RechargeOperatorModel.class)
                .where(RechargeOperatorModel_Table.prefix.is(prefix))
                .querySingle();

        if (result == null && prefix.length() == 4){
            result = new Select().from(RechargeOperatorModel.class)
                    .where(RechargeOperatorModel_Table.prefix.is(prefix.substring(0, 3)))
                    .querySingle();
        }
        return result;
    }

    public RechargeOperatorModel getDataOperator(int operatorId) {
        RechargeOperatorModel modelDB = new Select().from(RechargeOperatorModel.class)
                .where(RechargeOperatorModel_Table.operatorId.is(operatorId))
                .querySingle();
        return modelDB;
    }

    public List<RechargeOperatorModel> getListDataOperator(List<Integer> operatorIds) {
        List<RechargeOperatorModel> results = new ArrayList<>();
        for (Integer id: operatorIds) {
            results.add(new Select().from(RechargeOperatorModel.class)
                    .where(RechargeOperatorModel_Table.operatorId.is((int)id))
                    .querySingle());
        }
        return results;
    }

    @Override
    public List<RechargeOperatorModel> getDataList(String key) {
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
                                operator.getAttributes().getRule().getShowPrice(),
                                operator.getAttributes().getWeight()
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
                            operator.getAttributes().getRule().getShowPrice(),
                            operator.getAttributes().getWeight()
                    );
                }
            }
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
    }
}