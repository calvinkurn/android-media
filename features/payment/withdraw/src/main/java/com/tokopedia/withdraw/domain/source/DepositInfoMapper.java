//package com.tokopedia.withdraw.domain.source;
//
//import com.tokopedia.abstraction.common.data.model.response.DataResponse;
//import com.tokopedia.withdraw.domain.model.InfoDepositDomainModel;
//import com.tokopedia.withdraw.domain.model.InfoDepositPojo;
//import com.tokopedia.withdraw.view.model.BankAccount;
//import com.tokopedia.withdraw.view.viewmodel.BankAccountViewModel;
//import retrofit2.Response;
//import rx.functions.Func1;
//
//import javax.inject.Inject;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * @author by StevenFredian on 30/07/18.
//// */
//
//public class DepositInfoMapper implements Func1<Response<DataResponse<InfoDepositPojo>>, InfoDepositDomainModel> {
//
//    @Inject
//    public DepositInfoMapper(){
//
//    }
//
//    @Override
//    public InfoDepositDomainModel call(Response<DataResponse<InfoDepositPojo>> response) {
//        InfoDepositPojo temp = response.body().getData();
//        InfoDepositDomainModel model = new InfoDepositDomainModel();
//        model.setUseableDeposit(temp.getUseableDeposit());
//        model.setUseableDepositIdr(temp.getUseableDepositIdr());
//        List<BankAccountViewModel> list = new ArrayList<>();
//        for (int i = 0; i < temp.getBankAccount().size(); i++) {
//            BankAccountViewModel item = new BankAccountViewModel();
//            BankAccount data = temp.getBankAccount().get(i);
//
//            item.setBankAccountId(data.getBankAccountId());
//            item.setBankAccountName(data.getBankAccountName());
//            item.setBankAccountNumber(data.getBankAccountNumber());
//
////            item.setBankId(data.getBankId());
//            item.setBankName(data.getBankName());
//            item.setBankBranch(data.getBankBranch());
//
//            item.setChecked(data.getIsDefaultBank() == 1);
//            if(data.getIsDefaultBank() == 1){
//                model.setDefaultBank(i);
//            }
//
//            list.add(item);
//        }
//        if(list.size() > 0) {
//            list.get(0).setChecked(true);
//        }
//        model.setBankAccount(list);
//        model.setVerifiedAccount(temp.getMsisdnVerified() == 1);
//        return model;
//    }
//
//}
