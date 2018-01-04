package com.tokopedia.core.database.manager;

import com.tokopedia.core.database.model.Bank;
import com.tokopedia.core.database.model.CategoryDB;
import com.tokopedia.core.database.model.City;
import com.tokopedia.core.database.model.EtalaseDB;
import com.tokopedia.core.database.model.PictureDB;
import com.tokopedia.core.database.model.Province;
import com.tokopedia.core.database.model.WholesalePriceDB;
import com.tokopedia.core.myproduct.model.GetEtalaseModel;
import com.tokopedia.core.myproduct.model.WholeSaleAdapterModel;

import java.util.List;

/**
 * Created by noiz354 on 5/18/16.
 */
public interface DbManager {
    String TAG = "STUART";
    String messageTAG = "DbManager";

    EtalaseDB getEtalase(String etalaseId);
    PictureDB getGambarById(long photoId);
    long addHargaGrosir(WholeSaleAdapterModel wholeSaleAdapterModel);
    void removeHargaGrosir(WholeSaleAdapterModel wholeSaleAdapterModel);
    long addHargaGrosir(double min, double max, double price);
    void removeAllEtalase();
    List<Bank> getListBankFromDB(String query);
    List<WholesalePriceDB> removeWholeSaleDb(long dbId);
    Province getProvinceFromProvinceId(String provinceId);
    City getCity(String cityId);
    boolean isEtalaseEmpty(String EtalaseName);

    List<Bank> getBankBasedOnText(String query);
}
