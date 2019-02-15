//package com.tokopedia.withdraw.data;
//
//import com.tokopedia.abstraction.common.data.model.response.DataResponse;
//import com.tokopedia.withdraw.domain.model.DoWithdrawPojo;
//
//import java.util.Map;
//
//import retrofit2.Response;
//import retrofit2.http.FieldMap;
//import retrofit2.http.FormUrlEncoded;
//import retrofit2.http.POST;
//import rx.Observable;
//
///**
// * @author by StevenFredian on 30/07/18.
// */
//
//public interface DepositActionApi {
//
//    @FormUrlEncoded
//    @POST(WithdrawUrl.PATH_DO_WITHDRAW)
//    Observable<Response<DataResponse<DoWithdrawPojo>>> doWithdraw(@FieldMap Map<String, Object> params);
//
//
//}
