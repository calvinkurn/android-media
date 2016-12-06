package com.tokopedia.core.database.manager;

import android.util.Log;

import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.ConditionGroup;
import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.sql.language.Where;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.tokopedia.core.database.DbFlowDatabase;
import com.tokopedia.core.database.model.Bank;
import com.tokopedia.core.database.model.Bank_Table;
import com.tokopedia.core.database.model.CatalogDB;
import com.tokopedia.core.database.model.CatalogDB_Table;
import com.tokopedia.core.database.model.CatalogItemDB;
import com.tokopedia.core.database.model.CatalogItemDB_Table;
import com.tokopedia.core.database.model.CategoryDB;
import com.tokopedia.core.database.model.CategoryDB_Table;
import com.tokopedia.core.database.model.City;
import com.tokopedia.core.database.model.City_Table;
import com.tokopedia.core.database.model.CurrencyDB;
import com.tokopedia.core.database.model.EtalaseDB;
import com.tokopedia.core.database.model.EtalaseDB_Table;
import com.tokopedia.core.database.model.PictureDB;
import com.tokopedia.core.database.model.PictureDB_Table;
import com.tokopedia.core.database.model.ProductDB;
import com.tokopedia.core.database.model.ProductDB_Table;
import com.tokopedia.core.database.model.Province;
import com.tokopedia.core.database.model.Province_Table;
import com.tokopedia.core.database.model.StockStatusDB;
import com.tokopedia.core.database.model.StockStatusDB_Table;
import com.tokopedia.core.database.model.WholesalePriceDB;
import com.tokopedia.core.database.model.WholesalePriceDB_Table;
import com.tokopedia.core.myproduct.model.CatalogDataModel;
import com.tokopedia.core.myproduct.model.DepartmentParentModel;
import com.tokopedia.core.myproduct.model.GetEtalaseModel;
import com.tokopedia.core.myproduct.model.WholeSaleAdapterModel;
import com.tokopedia.core.myproduct.presenter.AddProductView;

import java.util.ArrayList;
import java.util.List;

import static com.tkpd.library.utils.CommonUtils.checkCollectionNotNull;
import static com.tkpd.library.utils.CommonUtils.dumper;
import static com.tokopedia.core.myproduct.presenter.AddProductView.ETALASE_GUDANG;

/**
 * Created by noiz354 on 5/18/16.
 */
public class DbManagerImpl implements DbManager{

    public static final String ID_WHERE = "Id = ? ";
    public static DbManager dbManager;

    private DbManagerImpl(){}

    public static DbManager getInstance(){
        if(dbManager == null){
            dbManager = new DbManagerImpl();
        }
        return dbManager;
    }

    @Override
    public void saveDepartment(DepartmentParentModel departmentParentModel, int depId) {
        DatabaseWrapper database = FlowManager.getDatabase(DbFlowDatabase.NAME).getWritableDatabase();
        database.beginTransaction();
        try{
            for (int i = 0; i < departmentParentModel.getData().getList().length; i++) {
                DepartmentParentModel.DepartmentParent parent = departmentParentModel.getData().getList()[i];
                CategoryDB categoryDB = new CategoryDB(
                        parent.getDepartmentName(),
                        Integer.parseInt(parent.getDepartmentTree()),
                        0,
                        depId,
                        Integer.parseInt(parent.getDepartmentId()),
                        parent.getDepartmentIdentifier());
                Log.d("DbManagerImpl", "sebelum inser "+i + " "+ categoryDB);
                categoryDB.save();
                Log.d("DbManagerImpl", i + " "+ categoryDB);
            }
            database.setTransactionSuccessful();
        }finally {
            database.endTransaction();
        }
    }

    @Override
    public void saveDepartmentParent(DepartmentParentModel departmentParentModel) {
        DatabaseWrapper database = FlowManager.getDatabase(DbFlowDatabase.NAME).getWritableDatabase();
        database.beginTransaction();

        try{
            for (int i = 0; i < departmentParentModel.getData().getList().length; i++) {
                DepartmentParentModel.DepartmentParent parent = departmentParentModel.getData().getList()[i];
                new CategoryDB(
                        parent.getDepartmentName(),
                        Integer.parseInt(parent.getDepartmentTree()),
                        0, 0, Integer.parseInt(parent.getDepartmentId()), parent.getDepartmentIdentifier())
                        .save();
            }
            database.setTransactionSuccessful();
        }finally {
            database.endTransaction();
        }

    }

    public List<CategoryDB> getDepartmentChild(int level, int depId){
        ConditionGroup conditionGroup = ConditionGroup.clause()
                .and(CategoryDB_Table.levelId.eq(level))
                .and(CategoryDB_Table.parentId.eq(depId));


        List<CategoryDB> categoryDBs = new Select().from(CategoryDB.class).where(conditionGroup).queryList();
        return categoryDBs;
    }

    public List<CategoryDB> getDepartmentParent(){
        List<CategoryDB> categoryDBs = new Select().from(CategoryDB.class).where(
                CategoryDB_Table.parentId.is(0)).queryList();
        return categoryDBs;
    }

    /**
     * @return
     */
    public boolean isDepartmentParentFetch(){
        List<CategoryDB> departmentParent = getDepartmentParent();
        return checkCollectionNotNull(departmentParent);
    }

    @Override
    public void saveGudangIfNotInDb() {
        EtalaseDB gudang = new Select().from(EtalaseDB.class)
                .where(EtalaseDB_Table.etalse_name.eq(ETALASE_GUDANG)).querySingle();
        if(gudang ==null){
            new EtalaseDB(-1, ETALASE_GUDANG, -1).save();
            Log.d(TAG, "save gudang term in add product");
        }else{
            dumper("kata-kata gudang term in add product");
        }
    }

    @Override
    public long saveEtalase(GetEtalaseModel.EtalaseModel etalaseModel) {
        //[START] UPDATE FROM SERVER
        EtalaseDB createEtalaseDB = new Select().from(EtalaseDB.class)
                .where(EtalaseDB_Table.etalse_name.eq(etalaseModel.getEtalase_name()))
                .querySingle();

        Log.d(TAG, messageTAG+" etalase : "+ createEtalaseDB);

        long id = -1;
        if(createEtalaseDB !=null&& createEtalaseDB.getEtalaseId()==-2){
            Log.d(TAG, messageTAG+" update etalase created from android : "+ createEtalaseDB);
            createEtalaseDB.setEtalaseId(Integer.parseInt(etalaseModel.getEtalase_id()));
            createEtalaseDB.setEtalaseTotal(Integer.parseInt(etalaseModel.getEtalase_total_product()));
            createEtalaseDB.save();
            id = createEtalaseDB.getId();
        }else{
            int etalaseId = Integer.parseInt(etalaseModel.getEtalase_id());
            EtalaseDB etalaseDB = new EtalaseDB(
                    etalaseId,
                    etalaseModel.getEtalase_name(),
                    Integer.parseInt(etalaseModel.getEtalase_total_product()));
            etalaseDB.save();
            id = etalaseDB.Id;
        }
        //[END] UPDATE FROM SERVER

        return id;
    }

    @Override
    public EtalaseDB getEtalase(String etalaseId){
        int etalaseIdInt = Integer.parseInt(etalaseId);
        EtalaseDB etalaseDB = new Select().from(EtalaseDB.class)
                .where(EtalaseDB_Table.etalaseId.is(etalaseIdInt))
                .querySingle();

        return etalaseDB;
    }

    public void removeAllEtalase(){
        new Delete().from(EtalaseDB.class).execute();
    }

    @Override
    public List<EtalaseDB> getEtalases() {
        List<EtalaseDB> allEtalaseDB = new Select().from(EtalaseDB.class)
                .queryList();
        EtalaseDB gudang = new EtalaseDB(-1, ETALASE_GUDANG, -1);
        for(int i = 0; i< allEtalaseDB.size(); i++){
            if(gudang.getEtalaseName().equals(allEtalaseDB.get(i).getEtalaseName())){
                allEtalaseDB.remove(i);
            }
        }
        return allEtalaseDB;
    }

    @Override
    public CategoryDB getKategoriByDepId(int depId) {
        return new Select().from(CategoryDB.class).where(
                CategoryDB_Table.departmentId.is(depId)
        ).querySingle();
    }

    public PictureDB getGambarById(long photoId){
        return new Select().from(PictureDB.class).where(PictureDB_Table.Id.is(photoId))
                .querySingle();
    }

    public List<CurrencyDB> getCurrencyDB(){
        return new Select().from(CurrencyDB.class).queryList();
    }

    public long addHargaGrosir(double min, double max, double price){
        WholesalePriceDB wholesalePriceDB = new WholesalePriceDB(min, max, price);
        wholesalePriceDB.save();
        return wholesalePriceDB.Id;
    }

    public long addHargaGrosir(WholeSaleAdapterModel wholeSaleAdapterModel){
        return addHargaGrosir(wholeSaleAdapterModel.getQuantityOne(), wholeSaleAdapterModel.getQuantityTwo(), wholeSaleAdapterModel.getWholeSalePrice());
    }

    public void removeHargaGrosir(WholeSaleAdapterModel wholeSaleAdapterModel){
        new Delete().from(WholesalePriceDB.class).where(
                WholesalePriceDB_Table.Id.is(wholeSaleAdapterModel.getbDid())).
                execute();
    }

    public void checkStockStatusDB(){
        StockStatusDB stockStatusDB = new Select().from(StockStatusDB.class).where(
                StockStatusDB_Table.stockDetail.eq(AddProductView.ETALASE_GUDANG)
        ).querySingle();
        if(stockStatusDB == null){
            StockStatusDB emptyStockStatus = new StockStatusDB(AddProductView.ETALASE_GUDANG);
            Log.i(TAG, messageTAG + "create new Stock Gudang in DB");
            emptyStockStatus.save();
            Log.i(TAG, messageTAG + "save : " + emptyStockStatus.Id);
        }
        stockStatusDB = new Select().from(StockStatusDB.class).where(
                StockStatusDB_Table.stockDetail.eq(AddProductView.ETALASE_ETALASE)).querySingle();
        if(stockStatusDB == null){
            StockStatusDB availablStockStatus = new StockStatusDB(AddProductView.ETALASE_ETALASE);
            Log.i(TAG, messageTAG + "create new Stock Etalase in DB");
            availablStockStatus.save();
            Log.i(TAG, messageTAG + "save : " + availablStockStatus.Id);
        }
    }

    @Override
    public void saveCatalog(CatalogDataModel catalogDataModel, String productDepId, String productName) {
        if(productDepId != null && productName != null){

            CategoryDB categoryDB = DbManagerImpl.getInstance().getKategoriByDepId(Integer.parseInt(productDepId));



            // store with product name, category db and catalog item db
            CatalogDB catalogDB = new CatalogDB(productName, categoryDB);
            catalogDB.save();

            List<CatalogItemDB> catalogItemDBs = new ArrayList<>();

            final DatabaseWrapper database = FlowManager.getDatabase(DbFlowDatabase.NAME).getWritableDatabase();
            database.beginTransaction();
            try{
                for(int i = 0; i < catalogDataModel.getList().size(); i ++){
                    // Trying to get the catalog item
                    CatalogDataModel.Catalog catalogData = catalogDataModel.getList().get(i);
                    Log.i(TAG, messageTAG + "try to get catalog data with ID = " + catalogData.getCatalogId());
                    Where<CatalogItemDB> where = new Select().from(CatalogItemDB.class)
                            .where(CatalogItemDB_Table.catalogId.eq(catalogData.getCatalogId()));
                    CatalogItemDB catalogItemDB = where.querySingle();
                    if (catalogItemDB == null){
                        Log.i(TAG,messageTAG + "catalog id doesn't exist, creating... " );
                        catalogItemDB = new CatalogItemDB(
                                catalogData.getCatalogName(),
                                catalogData.getCatalogDescription(),
                                catalogData.getCatalogImage(),
                                catalogData.getCatalogCountShop(),
                                catalogData.getCatalogPrice(),
                                catalogData.getCatalogId(),
                                catalogData.getCatalogUri()
                        );
                        catalogItemDB.setCatalogDB(catalogDB);
                        catalogItemDB.save();
                        // save to collections
                        catalogItemDBs.add(catalogItemDB);

                    }

                    Log.i(TAG, messageTAG + " Continue with creating the selector, checking if selector available.. ");
                }
                database.setTransactionSuccessful();
            } finally {
                database.endTransaction();
            }
        }
    }

    @Override
    public ArrayList<CatalogDataModel.Catalog> getCatalogList(String productDepId, String productName) {

        CategoryDB categoryDB = getKategoriByDepId(Integer.parseInt(productDepId));

        ConditionGroup and = ConditionGroup.clause().and(CatalogDB_Table.productName.eq(productName))
                .and(CatalogDB_Table.categoryDB.eq(categoryDB.Id));

        CatalogDB catalogDB = new Select().from(CatalogDB.class).where(and).querySingle();
        if(catalogDB == null)
            return null;

        List<CatalogItemDB> allCatalogItemDBs = catalogDB.getAllCatalogItemDBs();
        ArrayList<CatalogDataModel.Catalog> catalogs = new ArrayList<>();
        for (CatalogItemDB cat : catalogDB.getAllCatalogItemDBs()){
            catalogs.add(new CatalogDataModel.Catalog(cat));
        }
        return catalogs;
    }

    public void removePictureWithId(long imageDbId){
        new Delete()
                .from(PictureDB.class)
                .where(PictureDB_Table.Id.is(imageDbId))
                .execute();
    }

    @Override
    public ProductDB getProductDb(long productDb) {
        return new Select().from(ProductDB.class).where(ProductDB_Table.Id.is(productDb)).querySingle();
    }

    public CategoryDB getCategoryDb(String identifier){
        return new Select().from(CategoryDB.class)
                .where(CategoryDB_Table.categoryIdentifier.eq(identifier))
                .querySingle();
    }

    public List<Bank> getListBankFromDB(String query) {

        List<Bank> bankList = new Select()
                .from(Bank.class)
                .where(Bank_Table.bank_name.like("%"+query+"%"))
                .queryList();

        return bankList;
    }

    @Override
    public List<WholesalePriceDB> removeWholeSaleDb(long dbId) {
        List<WholesalePriceDB> wholesalePriceDBs = new Select()
                .from(WholesalePriceDB.class)
                .where(WholesalePriceDB_Table.Id.is(dbId))
                .queryList();
        for (WholesalePriceDB wholesalePriceDB : wholesalePriceDBs) {
            wholesalePriceDB.delete();
        }

        return wholesalePriceDBs;
    }

    @Override
    public List<EtalaseDB> removeEtalaseDb(int etalaseId) {
        List<EtalaseDB> etalaseDBs = new Select()
                .from(EtalaseDB.class)
                .where(EtalaseDB_Table.etalaseId.is(etalaseId))
                .queryList();
        for (EtalaseDB etalaseDB : etalaseDBs) {
            etalaseDB.delete();
        }

        return etalaseDBs;
    }

    @Override
    public List<Bank> getBankBasedOnText(String query) {
        return new Select()
                .from(Bank.class)
                .where(Bank_Table.bank_name.like("%"+query+"%"))
                .queryList();
    }

    public Province getProvinceFromProvinceId(String provinceId){
        return new Select()
                .from(Province.class)
                .where(Province_Table.provinceId.eq(provinceId))
                .querySingle();
    }

    public City getCity(String cityId){
        return new Select()
                .from(City.class)
                .where(City_Table.cityId.eq(cityId))
                .querySingle();
    }
}
