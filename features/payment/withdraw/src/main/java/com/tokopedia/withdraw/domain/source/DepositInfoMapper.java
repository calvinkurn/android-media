package com.tokopedia.withdraw.domain.source;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.withdraw.domain.model.InfoDepositDomainModel;
import com.tokopedia.withdraw.domain.model.InfoDepositPojo;
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

public class DepositInfoMapper implements Func1<Response<DataResponse<InfoDepositPojo>>, InfoDepositDomainModel> {

    @Inject
    public DepositInfoMapper(){

    }

    @Override
    public InfoDepositDomainModel call(Response<DataResponse<InfoDepositPojo>> response) {
        InfoDepositPojo temp = response.body().getData();
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
        model.setBankAccount(temp.getBankAccount());
        return model;
    }

}
