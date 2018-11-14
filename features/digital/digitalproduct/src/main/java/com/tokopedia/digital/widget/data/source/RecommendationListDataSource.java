package com.tokopedia.digital.widget.data.source;

import android.content.res.Resources;

import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.digital.R;
import com.tokopedia.digital.common.data.apiservice.DigitalGqlApiService;
import com.tokopedia.digital.widget.view.model.Recommendation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Created by Rizky on 13/11/18.
 */
public class RecommendationListDataSource {

    private DigitalGqlApiService digitalGqlApiService;

    public RecommendationListDataSource(DigitalGqlApiService digitalGqlApiService) {
        this.digitalGqlApiService = digitalGqlApiService;
    }

//    public List<Recommendation> getRecommendationList() {
//        digitalGqlApiService.getApi().getRecommendationList()
//    }

//    private String getCategoryAndFavRequestPayload(String categoryId, String operatorId, String clientNumber, String productId) {
//        String query = loadRawString(context.getResources(), R.raw.digital_category_favourites_query);
//
//        String isSeller = GlobalConfig.isSellerApp() ? "1" : "0";
//
//        if (operatorId == null) {
//            operatorId = "";
//        }
//
//        if (clientNumber == null) {
//            clientNumber = "";
//        }
//
//        if (productId == null) {
//            productId = "";
//        }
//
//        return String.format(query, categoryId, isSeller, categoryId, operatorId, productId, clientNumber);
//    }

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
