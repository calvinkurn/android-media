package com.tokopedia.pms.bankdestination.data;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.pms.R;
import com.tokopedia.pms.bankdestination.view.model.BankListModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by zulfikarrahman on 7/5/18.
 */

public class BankListDataSourceLocal {

    private Context context;

    @Inject
    public BankListDataSourceLocal(Context context) {
        this.context = context;
    }

    public Observable<List<BankListModel>> getBankList() {
        String[] listOfBank = context.getResources().getStringArray(R.array.payment_list_bank_name);
        String[] listOfBankCode = context.getResources().getStringArray(R.array.payment_list_bank_code);
        List<BankListModel> bankListModels = new ArrayList<>();
        for(int i = 0; i < listOfBank.length; i++){
            BankListModel bankListModel = new BankListModel();
            bankListModel.setBankName(listOfBank[i]);
            bankListModel.setId(listOfBankCode[i]);
            bankListModels.add(bankListModel);
        }
        return Observable.just(bankListModels);
    }
}
