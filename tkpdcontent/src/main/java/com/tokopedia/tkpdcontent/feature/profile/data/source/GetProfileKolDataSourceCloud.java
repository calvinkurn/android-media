package com.tokopedia.tkpdcontent.feature.profile.data.source;

import android.content.Context;
import android.content.res.Resources;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.tkpdcontent.R;
import com.tokopedia.tkpdcontent.common.data.source.api.KolApi;
import com.tokopedia.tkpdcontent.feature.profile.data.mapper.GetProfileKolDataMapper;
import com.tokopedia.tkpdcontent.feature.profile.view.viewmodel.KolViewModel;
import com.tokopedia.usecase.RequestParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;

import static com.tokopedia.tkpdcontent.feature.profile.domain.interactor
        .GetProfileKolDataUseCase.KOL_POST_LIMIT;
import static com.tokopedia.tkpdcontent.feature.profile.domain.interactor
        .GetProfileKolDataUseCase.PARAM_CURSOR;
import static com.tokopedia.tkpdcontent.feature.profile.domain.interactor
        .GetProfileKolDataUseCase.PARAM_LIMIT;
import static com.tokopedia.tkpdcontent.feature.profile.domain.interactor
        .GetProfileKolDataUseCase.PARAM_USER_ID;

/**
 * @author by milhamj on 20/02/18.
 */

public class GetProfileKolDataSourceCloud {
    private final Context context;
    private final KolApi kolApi;
    private final GetProfileKolDataMapper getProfileKolDataMapper;

    @Inject
    public GetProfileKolDataSourceCloud(@ApplicationContext Context context,
                                        KolApi kolApi,
                                        GetProfileKolDataMapper getProfileKolDataMapper) {
        this.context = context;
        this.kolApi = kolApi;
        this.getProfileKolDataMapper = getProfileKolDataMapper;
    }

    @Inject
    public Observable<List<KolViewModel>> getProfileKolData(RequestParams params) {
        return kolApi.getProfileKolData(getRequestPayload(params))
                .map(getProfileKolDataMapper);
    }


    private String getRequestPayload(RequestParams requestParams) {
        return String.format(
                loadRawString(context.getResources(), R.raw.query_get_profile_kol_data),
                requestParams.getString(PARAM_USER_ID, "0"),
                requestParams.getString(PARAM_CURSOR, ""),
                requestParams.getString(PARAM_LIMIT, KOL_POST_LIMIT)
        );
    }

    private String loadRawString(Resources resources, int resId) {
        InputStream rawResource = resources.openRawResource(resId);
        String content = streamToString(rawResource);
        try {
            rawResource.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

    private String streamToString(InputStream in) {
        String temp;
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
        StringBuilder stringBuilder = new StringBuilder();
        try {
            while ((temp = bufferedReader.readLine()) != null) {
                stringBuilder.append(temp).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }
}
