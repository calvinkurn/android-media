package com.tokopedia.core.database.manager;

import com.tokopedia.core.database.model.Bank;
import com.tokopedia.core.database.model.CategoryDB;
import com.tokopedia.core.database.model.City;
import com.tokopedia.core.database.model.CurrencyDB;
import com.tokopedia.core.database.model.EtalaseDB;
import com.tokopedia.core.database.model.PictureDB;
import com.tokopedia.core.database.model.ProductDB;
import com.tokopedia.core.database.model.Province;
import com.tokopedia.core.database.model.WholesalePriceDB;
import com.tokopedia.core.myproduct.model.CatalogDataModel;
import com.tokopedia.core.myproduct.model.DepartmentParentModel;
import com.tokopedia.core.myproduct.model.GetEtalaseModel;
import com.tokopedia.core.myproduct.model.WholeSaleAdapterModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by noiz354 on 5/18/16.
 */
public interface DbManager {
    String TAG = "STUART";
    String messageTAG = "DbManager";

    void saveDepartment(DepartmentParentModel departmentParentModel, int depId);
    void saveDepartmentParent(DepartmentParentModel departmentParentModel);
    void saveGudangIfNotInDb();
    long saveEtalase(GetEtalaseModel.EtalaseModel etalaseModel);
    EtalaseDB getEtalase(String etalaseId);
    List<EtalaseDB> getEtalases();
    CategoryDB getKategoriByDepId(int depId);
    List<CurrencyDB> getCurrencyDB();
    PictureDB getGambarById(long photoId);
    long addHargaGrosir(WholeSaleAdapterModel wholeSaleAdapterModel);
    void removeHargaGrosir(WholeSaleAdapterModel wholeSaleAdapterModel);
    long addHargaGrosir(double min, double max, double price);
    boolean isDepartmentParentFetch();
    List<CategoryDB> getDepartmentParent();
    List<CategoryDB> getDepartmentChild(int level, int depId);
    void removeAllEtalase();
    void saveCatalog(CatalogDataModel catalogDataModel, String productDepId, String productName);
    ArrayList<CatalogDataModel.Catalog> getCatalogList(String productDepId, String productName);
    void removePictureWithId(long imageDbId);
    ProductDB getProductDb(long pictureDbId);
    CategoryDB getCategoryDb(String identifier);
    List<Bank> getListBankFromDB(String query);
    List<WholesalePriceDB> removeWholeSaleDb(long dbId);
    List<EtalaseDB> removeEtalaseDb(int etalaseId);
    List<Bank> getBankBasedOnText(String query);
    Province getProvinceFromProvinceId(String provinceId);
    City getCity(String cityId);
    boolean isEtalaseEmpty(String EtalaseName);
}
