//package com.tokopedia.saldodetails.usecase;
//
//import com.google.gson.reflect.TypeToken;
//import com.tokopedia.abstraction.common.data.model.response.DataResponse;
//import com.tokopedia.common.network.data.model.CacheType;
//import com.tokopedia.common.network.data.model.RestCacheStrategy;
//import com.tokopedia.common.network.data.model.RestRequest;
//import com.tokopedia.common.network.domain.RestRequestUseCase;
//import com.tokopedia.saldodetails.response.model.SummaryWithdraw;
//import com.tokopedia.usecase.RequestParams;
//
//import java.lang.reflect.Type;
//import java.util.ArrayList;
//import java.util.List;
//
//import javax.inject.Inject;
//
//
//public class GetSaldoSummaryUseCase extends RestRequestUseCase {
//
//    private RequestParams params;
//
//    /*@Inject
//    GetSaldoSummaryUseCase() {
//    }*/
//
//    public void setRequestParams(RequestParams requestParams) {
//        this.params = requestParams;
//    }
//
//    private final String url = "https://ws.tokopedia.com/v4/deposit/get_summary.pl";
//
//    @Override
//    protected List<RestRequest> buildRequest() {
//        List<RestRequest> restRequestList = new ArrayList<>();
//
//        Type typeOfT = new TypeToken<DataResponse<SummaryWithdraw>>() {
//        }.getType();
//
//        RestCacheStrategy restCacheStrategy = new RestCacheStrategy.Builder(CacheType.CACHE_FIRST).build();
//        RestRequest restRequest = new RestRequest.Builder(url, typeOfT)
//                .setQueryParams(params.getParameters())
//                .setCacheStrategy(restCacheStrategy)
//                .build();
//
//        restRequestList.add(restRequest);
//        return restRequestList;
//    }
//}
