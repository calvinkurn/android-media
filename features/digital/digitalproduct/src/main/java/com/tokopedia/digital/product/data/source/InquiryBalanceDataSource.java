package com.tokopedia.digital.product.data.source;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.digital.product.data.entity.response.InquiryBalanceResponse;
import com.tokopedia.digital.product.data.entity.response.Response;
import com.tokopedia.digital.product.data.mapper.InquiryBalanceMapper;
import com.tokopedia.digital.product.view.model.InquiryBalanceModel;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Rizky on 18/05/18.
 */
public class InquiryBalanceDataSource {

    private Context context;
    private InquiryBalanceMapper inquiryBalanceMapper;

    public InquiryBalanceDataSource(Context context, InquiryBalanceMapper inquiryBalanceMapper) {
        this.context = context;
        this.inquiryBalanceMapper = inquiryBalanceMapper;
    }

    public Observable<InquiryBalanceModel> inquiryBalance() {
        Gson gson = new Gson();
        Response response;
        String json = null;
        try {
            InputStream inputStream = context.getAssets().open("json/inquiry_balance_0.json");
            int size = inputStream.available();
            byte [] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }

        response = gson.fromJson(json, Response.class);

        return Observable.just(response)
                .delay(2, TimeUnit.SECONDS)
                .map(new Func1<Response, InquiryBalanceModel>() {
                    @Override
                    public InquiryBalanceModel call(Response inquiryBalanceResponse) {
                        return inquiryBalanceMapper.map(inquiryBalanceResponse);
                    }
                });
    }

}
