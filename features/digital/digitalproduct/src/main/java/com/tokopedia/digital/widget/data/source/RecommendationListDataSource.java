package com.tokopedia.digital.widget.data.source;

import android.content.Context;

import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.digital.R;
import com.tokopedia.digital.common.data.apiservice.DigitalGqlApiService;
import com.tokopedia.digital.widget.data.entity.RecommendationItemEntity;
import com.tokopedia.digital.widget.view.model.Recommendation;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by Rizky on 13/11/18.
 */
public class RecommendationListDataSource {

    private DigitalGqlApiService digitalGqlApiService;
    private Context context;

    @Inject
    public RecommendationListDataSource(DigitalGqlApiService digitalGqlApiService) {
        this.digitalGqlApiService = digitalGqlApiService;
        context = MainApplication.getAppContext();
    }

    public Observable<List<Recommendation>> getRecommendationList(int deviceId) {
        return digitalGqlApiService.getApi().getRecommendationList(getCategoryAndFavRequestPayload(deviceId))
                .map(listResponse -> {
                    List<Recommendation> recommendations = new ArrayList<>();
//                    if (!listResponse.body().isEmpty()) {
//                        if (listResponse.body().get(0).getData() != null) {
//                            if (listResponse.body().get(0)
//                                    .getData().getRechargeFavoriteRecommentaionList() != null) {
//                                if (listResponse.body().get(0)
//                                        .getData().getRechargeFavoriteRecommentaionList().getRecommendationItemEntityList() != null) {
//                                    for (RecommendationItemEntity recommendationItemEntity : listResponse.body().get(0)
//                                            .getData().getRechargeFavoriteRecommentaionList().getRecommendationItemEntityList()) {
//                                        recommendations.add(new Recommendation(
//                                                recommendationItemEntity.getIconUrl(),
//                                                recommendationItemEntity.getTitle(),
//                                                recommendationItemEntity.getClientNumber(),
//                                                recommendationItemEntity.getApplink(),
//                                                recommendationItemEntity.getWebLink()
//                                        ));
//                                    }
//                                }
//                            }
//                        }
//                    }
                    return recommendations;
                });
    }

    private String getCategoryAndFavRequestPayload(int deviceId) {
        String query = GraphqlHelper.loadRawString(context.getResources(), R.raw.digital_recommendation_list);

        return String.format(query, deviceId);
    }

}
