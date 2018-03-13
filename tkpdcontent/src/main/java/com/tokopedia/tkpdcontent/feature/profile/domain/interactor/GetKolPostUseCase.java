package com.tokopedia.tkpdcontent.feature.profile.domain.interactor;

import com.tokopedia.tkpdcontent.feature.profile.data.source.GetKolPostSourceCloud;
import com.tokopedia.tkpdcontent.feature.profile.domain.model.KolProfileModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author by milhamj on 20/02/18.
 */

public class GetKolPostUseCase extends UseCase<KolProfileModel> {
    public static final String PARAM_USER_ID = "userID";
    public static final String PARAM_CURSOR = "cursor";
    public static final String PARAM_LIMIT = "limit";
    public static final int KOL_POST_LIMIT = 5;

    private final GetKolPostSourceCloud getKolPostSourceCloud;

    @Inject
    public GetKolPostUseCase(GetKolPostSourceCloud getKolPostSourceCloud) {
        this.getKolPostSourceCloud = getKolPostSourceCloud;
    }

    @Override
    public Observable<KolProfileModel> createObservable(RequestParams requestParams) {
        return getKolPostSourceCloud.getProfileKolData(requestParams);
    }

    public static RequestParams getParams(String userId, String lastCursor) {
        RequestParams param = RequestParams.create();
        param.putInt(PARAM_USER_ID, Integer.valueOf(userId));
        param.putString(PARAM_CURSOR, lastCursor);
        param.putInt(PARAM_LIMIT, KOL_POST_LIMIT);
        return param;
    }
}
