package com.tokopedia.tkpdcontent.feature.profile.domain.interactor;

import com.tokopedia.tkpdcontent.feature.profile.data.source.GetProfileKolDataSourceCloud;
import com.tokopedia.tkpdcontent.feature.profile.view.viewmodel.KolPostViewModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author by milhamj on 20/02/18.
 */

public class GetProfileKolDataUseCase extends UseCase<List<KolPostViewModel>> {
    public static final String PARAM_USER_ID = "userID";
    public static final String PARAM_CURSOR = "cursor";
    public static final String PARAM_LIMIT = "limit";
    public static final int KOL_POST_LIMIT = 5;

    private final GetProfileKolDataSourceCloud getProfileKolDataSourceCloud;

    @Inject
    public GetProfileKolDataUseCase(GetProfileKolDataSourceCloud getProfileKolDataSourceCloud) {
        this.getProfileKolDataSourceCloud = getProfileKolDataSourceCloud;
    }

    @Override
    public Observable<List<KolPostViewModel>> createObservable(RequestParams requestParams) {
        return getProfileKolDataSourceCloud.getProfileKolData(requestParams);
    }

    public static RequestParams getParams(String userId) {
        RequestParams param = RequestParams.create();
        param.putInt(PARAM_USER_ID, Integer.valueOf(userId));
        param.putString(PARAM_CURSOR, "");
        param.putInt(PARAM_LIMIT, KOL_POST_LIMIT);
        return param;
    }
}
