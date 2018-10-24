package com.tokopedia.notifications.domain;

import android.content.Context;

import com.tokopedia.abstraction.common.data.model.storage.CacheManager;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.common.network.data.model.RestRequest;
import com.tokopedia.common.network.data.model.RestResponse;
import com.tokopedia.common.network.domain.RestRequestUseCase;
import com.tokopedia.notifications.common.CMNotificationUrls;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 * Created by Ashwani Tyagi on 23/10/18.
 */
public class UpdateFcmTokenUseCase extends RestRequestUseCase {

    private Context context;

    public UpdateFcmTokenUseCase(@ApplicationContext Context context) {
        this.context = context;
    }

    @Override
    protected List<RestRequest> buildRequest() {
        List<RestRequest> tempRequest = new ArrayList<>();

        RestRequest restRequest1 = new RestRequest.Builder(CMNotificationUrls.CM_TOKEN_UPDATE, String.class)
                .build();
        tempRequest.add(restRequest1);

        return tempRequest;
    }
}
