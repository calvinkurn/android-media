//package com.tokopedia.withdraw.domain.source;
//
//import com.tokopedia.withdraw.data.DepositApi;
//import com.tokopedia.withdraw.domain.model.InfoDepositDomainModel;
//
//import java.util.HashMap;
//
//import javax.inject.Inject;
//
//import rx.Observable;
//
///**
// * @author by StevenFredian on 30/07/18.
// */
//
//public class DepositSource {
//
//    @Inject
//    DepositApi depositApi;
//
//    @Inject
//    DepositInfoMapper depositInfoMapper;
//
//    @Inject
//    public DepositSource(){
//
//    }
//
//    public Observable<InfoDepositDomainModel> getWithdrawForm(HashMap<String,Object> parameters) {
//        return depositApi.getWithDrawForm(parameters).map(depositInfoMapper);
//    }
//}
