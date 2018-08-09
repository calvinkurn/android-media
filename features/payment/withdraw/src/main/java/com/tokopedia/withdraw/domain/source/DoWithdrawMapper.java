package com.tokopedia.withdraw.domain.source;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.withdraw.domain.model.DoWithdrawPojo;
import com.tokopedia.withdraw.domain.model.InfoDepositDomainModel;
import com.tokopedia.withdraw.view.model.BankAccount;
import com.tokopedia.withdraw.view.viewmodel.BankAccountViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author by StevenFredian on 30/07/18.
 */

public class DoWithdrawMapper implements Func1<Response<DataResponse<DoWithdrawPojo>>, String> {

    @Inject
    public DoWithdrawMapper(){

    }

    @Override
    public String call(Response<DataResponse<DoWithdrawPojo>> response) {
        DoWithdrawPojo temp = response.body().getData();
        InfoDepositDomainModel model = new InfoDepositDomainModel();
        model.setUseableDeposit(temp.getUseableDeposit());
        model.setUseableDepositIdr(temp.getUseableDepositIdr());
        List<BankAccountViewModel> list = new ArrayList<>();
        for (int i = 0; i < temp.getBankAccount().size(); i++) {
            BankAccountViewModel item = new BankAccountViewModel();
            BankAccount data = temp.getBankAccount().get(i);

            item.setBankAccountName(data.getBankAccountName());
            item.setBankName(data.getBankName());

            list.add(item);
        }
        list.get(0).setChecked(true);
        model.setBankAccount(list);
        return "";
    }

}
