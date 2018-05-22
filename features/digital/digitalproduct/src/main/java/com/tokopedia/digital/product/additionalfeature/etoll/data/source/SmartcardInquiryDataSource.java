package com.tokopedia.digital.product.additionalfeature.etoll.data.source;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tokopedia.core.network.retrofit.response.TkpdDigitalResponse;
import com.tokopedia.digital.common.data.apiservice.DigitalEndpointService;
import com.tokopedia.digital.product.additionalfeature.etoll.data.entity.requestbody.smartcardinquiry.RequestBodySmartcardInquiry;
import com.tokopedia.digital.product.additionalfeature.etoll.data.entity.response.ResponseSmartcardInquiry;
import com.tokopedia.digital.product.additionalfeature.etoll.data.mapper.SmartcardInquiryMapper;
import com.tokopedia.digital.product.additionalfeature.etoll.view.model.InquiryBalanceModel;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Rizky on 18/05/18.
 */
public class SmartcardInquiryDataSource {

    private Context context;
    private DigitalEndpointService digitalEndpointService;
    private SmartcardInquiryMapper smartcardInquiryMapper;

    public SmartcardInquiryDataSource(Context context, DigitalEndpointService digitalEndpointService,
                                      SmartcardInquiryMapper smartcardInquiryMapper) {
        this.context = context;
        this.digitalEndpointService = digitalEndpointService;
        this.smartcardInquiryMapper = smartcardInquiryMapper;
    }

    public Observable<InquiryBalanceModel> inquiryBalance(RequestBodySmartcardInquiry requestBodySmartcardInquiry) {
//        Gson gson = new Gson();
//        Response response;
//        String json = null;
//        try {
//            InputStream inputStream = context.getAssets().open("json/inquiry_balance_0.json");
//            int size = inputStream.available();
//            byte [] buffer = new byte[size];
//            inputStream.read(buffer);
//            inputStream.close();
//            json = new String(buffer, "UTF-8");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        response = gson.fromJson(json, Response.class);

//        return Observable.just(response)
//                .delay(2, TimeUnit.SECONDS)
//                .map(new Func1<Response, InquiryBalanceModel>() {
//                    @Override
//                    public InquiryBalanceModel call(Response inquiryBalanceResponse) {
//                        return smartcardInquiryMapper.map(inquiryBalanceResponse);
//                    }
//                });

        JsonElement jsonElement = new JsonParser().parse(new Gson().toJson(requestBodySmartcardInquiry));
        final JsonObject requestBody = new JsonObject();
        requestBody.add("data", jsonElement);
        return digitalEndpointService.getApi().smartcardInquiry(requestBody)
                .map(new Func1<Response<TkpdDigitalResponse>, InquiryBalanceModel>() {
                    @Override
                    public InquiryBalanceModel call(Response<TkpdDigitalResponse> response) {
                        return smartcardInquiryMapper.map(response.body().convertDataObj(ResponseSmartcardInquiry.class));
                    }
                });
    }

}
