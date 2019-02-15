//package com.tokopedia.withdraw.domain.source;
//
//import com.tokopedia.abstraction.common.data.model.response.DataResponse;
//import com.tokopedia.withdraw.domain.model.DoWithdrawDomainModel;
//import com.tokopedia.withdraw.domain.model.DoWithdrawPojo;
//
//import javax.inject.Inject;
//
//import retrofit2.Response;
//import rx.functions.Func1;
//
///**
// * @author by StevenFredian on 30/07/18.
// */
//
//public class DoWithdrawMapper implements Func1<Response<DataResponse<DoWithdrawPojo>>, DoWithdrawDomainModel> {
//
//    @Inject
//    public DoWithdrawMapper(){
//
//    }
//
//    @Override
//    public DoWithdrawDomainModel call(Response<DataResponse<DoWithdrawPojo>> response) {
//        DoWithdrawPojo temp = response.body().getData();
//        DoWithdrawDomainModel model = new DoWithdrawDomainModel();
//
//        model.setSuccessWithdraw(temp.getIsSuccess() == 1);
//        return model;
//    }
//
//}
