package com.tokopedia.core.database.manager;

import android.util.Log;

import com.raizlabs.android.dbflow.sql.language.ConditionGroup;
import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.tokopedia.core.database.model.Bank;
import com.tokopedia.core.database.model.Bank_Table;
import com.tokopedia.core.database.model.CategoryDB;
import com.tokopedia.core.database.model.CategoryDB_Table;
import com.tokopedia.core.database.model.City;
import com.tokopedia.core.database.model.City_Table;
import com.tokopedia.core.database.model.EtalaseDB;
import com.tokopedia.core.database.model.EtalaseDB_Table;
import com.tokopedia.core.database.model.PictureDB;
import com.tokopedia.core.database.model.PictureDB_Table;
import com.tokopedia.core.database.model.Province;
import com.tokopedia.core.database.model.Province_Table;
import com.tokopedia.core.database.model.WholesalePriceDB;
import com.tokopedia.core.database.model.WholesalePriceDB_Table;
import com.tokopedia.core.etalase.EtalaseVariable;
import com.tokopedia.core.myproduct.model.GetEtalaseModel;
import com.tokopedia.core.myproduct.model.WholeSaleAdapterModel;

import java.util.List;

import static com.tkpd.library.utils.CommonUtils.dumper;

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

    @Override
    public void saveGudangIfNotInDb() {
        EtalaseDB gudang = new Select().from(EtalaseDB.class)
                .where(EtalaseDB_Table.etalse_name.eq(EtalaseVariable.ETALASE_GUDANG)).querySingle();
        if(gudang ==null){
            new EtalaseDB(-1, EtalaseVariable.ETALASE_GUDANG, -1).save();
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
    public boolean isEtalaseEmpty(String etalaseName) {
        List<EtalaseDB> etalaseDBs = new Select().from(EtalaseDB.class)
                .where(EtalaseDB_Table.etalse_name.like(etalaseName))
                .queryList();

        if(etalaseDBs != null && etalaseDBs.size() > 0){
            return false;
        }else {
            return true;
        }
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
        EtalaseDB gudang = new EtalaseDB(-1, EtalaseVariable.ETALASE_GUDANG, -1);
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

    @Override
    public List<Bank> getBankBasedOnText(String query) {
        return new Select()
                .from(Bank.class)
                .where(Bank_Table.bank_name.like("%" + query + "%"))
                .queryList();
    }
}
