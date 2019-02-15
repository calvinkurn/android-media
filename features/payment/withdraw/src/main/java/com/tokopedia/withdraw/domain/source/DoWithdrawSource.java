//package com.tokopedia.withdraw.domain.source;
//
//import com.tokopedia.withdraw.data.DepositActionApi;
//import com.tokopedia.withdraw.domain.model.DoWithdrawDomainModel;
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
//public class DoWithdrawSource {
//
//    @Inject
//    DepositActionApi api;
//
//    @Inject
//    DoWithdrawMapper mapper;
//
//    @Inject
//    public DoWithdrawSource(){
//
//    }
//
//    public Observable<DoWithdrawDomainModel> doWithdraw(HashMap<String,Object> parameters) {
//        return api.doWithdraw(parameters).map(mapper);
//    }
//}
