package com.tokopedia.core.database.manager;

import android.util.Log;

import com.raizlabs.android.dbflow.sql.language.ConditionGroup;
import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.tokopedia.core.database.model.Bank;
import com.tokopedia.core.database.model.Bank_Table;
import com.tokopedia.core.database.model.City;
import com.tokopedia.core.database.model.City_Table;
import com.tokopedia.core.database.model.Province;
import com.tokopedia.core.database.model.Province_Table;
import com.tokopedia.core.etalase.EtalaseVariable;
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

    public List<Bank> getListBankFromDB(String query) {

        List<Bank> bankList = new Select()
                .from(Bank.class)
                .where(Bank_Table.bank_name.like("%"+query+"%"))
                .queryList();

        return bankList;
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
