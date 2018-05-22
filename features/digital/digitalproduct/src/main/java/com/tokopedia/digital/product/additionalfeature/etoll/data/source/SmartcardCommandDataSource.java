package com.tokopedia.digital.product.additionalfeature.etoll.data.source;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.digital.product.additionalfeature.etoll.data.entity.response.Response;
import com.tokopedia.digital.product.additionalfeature.etoll.data.mapper.SmartcardInquiryMapper;
import com.tokopedia.digital.product.additionalfeature.etoll.view.model.InquiryBalanceModel;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Rizky on 18/05/18.
 */
public class SmartcardCommandDataSource {

    private Context context;
    private SmartcardInquiryMapper smartcardInquiryMapper;

    public SmartcardCommandDataSource(Context context, SmartcardInquiryMapper smartcardInquiryMapper) {
        this.context = context;
        this.smartcardInquiryMapper = smartcardInquiryMapper;
    }

    public Observable<InquiryBalanceModel> sendCommand() {
        Gson gson = new Gson();
        Response response;
        String json = null;
        try {
            InputStream inputStream = context.getAssets().open("json/send_command_1.json");
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
                        return smartcardInquiryMapper.map(inquiryBalanceResponse);
                    }
                });
    }

}
