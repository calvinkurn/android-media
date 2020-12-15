package com.tokopedia.tkpd.thankyou.data.source;

import android.content.Context;
import android.content.res.Resources;

import com.tokopedia.customer_mid_app.R;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.tkpd.thankyou.data.mapper.MarketplaceTrackerMapper;
import com.tokopedia.tkpd.thankyou.data.pojo.marketplace.OrderGraphql;
import com.tokopedia.tkpd.thankyou.data.pojo.marketplace.PaymentGraphql;
import com.tokopedia.tkpd.thankyou.domain.model.ThanksTrackerConst;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.user.session.UserSessionInterface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import rx.Observable;

/**
 * Created by okasurya on 12/5/17.
 */

public class MarketplaceTrackerCloudSource extends ThanksTrackerCloudSource {
    private static final String ANDROID_ENABLE_TYPAGE_GRATIS_ONGKIR = "android_enable_typage_gratisongkir";
    private static final String PAYMENT_ID = "paymentID";
    private Context context;
    private UserSessionInterface userSessionInterface;
    private RemoteConfig remoteConfig;

    public MarketplaceTrackerCloudSource(RequestParams requestParams,
                                         UserSessionInterface userSessionInterface,
                                         Context context) {
        super(requestParams);
        this.context = context;
        this.userSessionInterface = userSessionInterface;
    }

    @Override
    public Observable<Boolean> sendAnalytics() {
        MarketplaceTrackerMapper mapper = new MarketplaceTrackerMapper(userSessionInterface,
                (List<String>) requestParams.getObject(ThanksTrackerConst.Key.SHOP_TYPES),
                requestParams);
        GraphqlUseCase graphqlUseCase = new GraphqlUseCase();
        ArrayList<GraphqlRequest> requests = new ArrayList<>();
        HashMap<String, Object> variables = new HashMap<>();
        variables.put(PAYMENT_ID, Integer.parseInt(requestParams.getString(ThanksTrackerConst.Key.ID, "0")));
        requests.add(new GraphqlRequest(loadRawString(context.getResources(), R.raw.payment_data_query), PaymentGraphql.class, variables));
        requests.add(new GraphqlRequest(loadRawString(context.getResources(), R.raw.order_info_query), OrderGraphql.class, variables));
        graphqlUseCase.addRequests(requests);
        return graphqlUseCase.createObservable(RequestParams.EMPTY).map(graphqlResponse -> {
            PaymentGraphql paymentGraphql = graphqlResponse.getData(PaymentGraphql.class);
            OrderGraphql orderGraphql = graphqlResponse.getData(OrderGraphql.class);
            if (paymentGraphql != null && paymentGraphql.getPayment() != null && orderGraphql != null) {
                paymentGraphql.getPayment().setOrders(orderGraphql.getOrderInfoGraphql().getOrderData());
            }
            return paymentGraphql;
        }).map(mapper);
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
