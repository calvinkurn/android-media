package com.tokopedia.tkpd.thankyou.data.source;

import android.content.Context;
import android.content.res.Resources;

import com.tokopedia.customer_mid_app.R;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.tkpd.thankyou.data.pojo.marketplace.payment.MonthlyBuyerBase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import rx.Subscriber;

public class MonthlyNewBuyerSource {

    public void executeMonthlyNewBuyerCheck(Context context, Subscriber<GraphqlResponse> subscriber, String orderId) {
        GraphqlUseCase monthlyNewBuyerCheckUseCase = new GraphqlUseCase();
        GraphqlRequest graphqlRequest = new GraphqlRequest(getRequestPayload(context, orderId),
                MonthlyBuyerBase.class);
        monthlyNewBuyerCheckUseCase.addRequest(graphqlRequest);
        monthlyNewBuyerCheckUseCase.execute(subscriber);
    }

    private String getRequestPayload(Context context, String orderId) {
        return String.format(
                loadRawString(context.getResources(), R.raw.monthly_new_buyer),
                orderId
        );
    }

    private String loadRawString(Resources resources, int resId) {
        InputStream rawResource = resources.openRawResource(resId);
        String content = streamToString(rawResource);
        try {
            rawResource.close();
        } catch (IOException e) {
        }
        return content;
    }

    private String streamToString(InputStream in) {
        String temp;
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
        StringBuilder stringBuilder = new StringBuilder();
        try {
            while ((temp = bufferedReader.readLine()) != null) {
                stringBuilder.append(temp + "\n");
            }
        } catch (IOException e) {
        }
        return stringBuilder.toString();
    }
}
