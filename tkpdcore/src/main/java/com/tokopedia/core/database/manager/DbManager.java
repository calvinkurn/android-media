package com.tokopedia.core.database.manager;

import com.tokopedia.core.database.model.Bank;
import com.tokopedia.core.database.model.City;
import com.tokopedia.core.database.model.Province;
import com.tokopedia.core.myproduct.model.WholeSaleAdapterModel;

import java.util.List;

/**
 * Created by noiz354 on 5/18/16.
 */
public interface DbManager {
    String TAG = "STUART";
    String messageTAG = "DbManager";

    long addHargaGrosir(WholeSaleAdapterModel wholeSaleAdapterModel);
    void removeHargaGrosir(WholeSaleAdapterModel wholeSaleAdapterModel);
    long addHargaGrosir(double min, double max, double price);
    List<Bank> getListBankFromDB(String query);
    Province getProvinceFromProvinceId(String provinceId);
    City getCity(String cityId);

    List<Bank> getBankBasedOnText(String query);
}
