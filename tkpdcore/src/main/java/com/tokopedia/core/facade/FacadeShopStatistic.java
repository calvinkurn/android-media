package com.tokopedia.core.facade;

import com.tokopedia.core.database.CacheUtil;
import com.tokopedia.core.fragment.shopstatistic.ShopStatisticTransaction;
import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel;
import com.tokopedia.core.shopinfo.models.shopmodel.ShopTxStats;

/**
 * modified by mnormansyah 10 april 2017
 */
public class FacadeShopStatistic {

    private static final String TAG = "FacadeShopStatistic";
    private ShopModel shopModel;

    public static FacadeShopStatistic createInstance(String shopInfo){
        FacadeShopStatistic facade = new FacadeShopStatistic();

        facade.shopModel = CacheUtil.convertStringToModel(shopInfo, ShopModel.class);
        return facade;
    }

    public ShopStatisticTransaction.Model getTransactionModel(){
        ShopStatisticTransaction.Model model = new ShopStatisticTransaction.Model();
        getTransactionFromJSON(model);
        return model;
    }

    private void getTransactionFromJSON(ShopStatisticTransaction.Model model){
        ShopTxStats transaction = shopModel.shopTxStats;
        model.hasTransaction = transaction.shopTxHasTransaction;
        model.oneMonth.successPercentage = Float.valueOf(transaction.shopTxSuccessRate1Month);
        model.threeMonth.successPercentage = Float.valueOf(transaction.shopTxSuccessRate3Month);
        model.twelveMonth.successPercentage = Float.valueOf(transaction.shopTxSuccessRate1Year);
        model.oneMonth.totalTransaction = transaction.shopTxSuccess1MonthFmt;
        model.threeMonth.totalTransaction = transaction.shopTxSuccess3MonthFmt;
        model.twelveMonth.totalTransaction = transaction.shopTxSuccess1YearFmt;
        model.oneMonth.showPercentage = transaction.shopTxShowPercentage1Month;
        model.threeMonth.showPercentage = transaction.shopTxShowPercentage3Month;
        model.twelveMonth.showPercentage = transaction.shopTxShowPercentage1Year;
        model.oneMonth.hasTransaction = transaction.shopTxHasTransaction1Month;
        model.threeMonth.hasTransaction = transaction.shopTxHasTransaction3Month;
        model.twelveMonth.hasTransaction = transaction.shopTxHasTransaction1Year;
    }
}
