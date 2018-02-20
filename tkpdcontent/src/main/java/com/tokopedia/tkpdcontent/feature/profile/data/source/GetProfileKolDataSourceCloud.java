package com.tokopedia.tkpdcontent.feature.profile.data.source;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.tkpdcontent.common.data.source.api.KolApi;
import com.tokopedia.tkpdcontent.feature.profile.data.mapper.GetProfileKolDataMapper;
import com.tokopedia.tkpdcontent.feature.profile.view.viewmodel.KolViewModel;
import com.tokopedia.usecase.RequestParams;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * @author by milhamj on 20/02/18.
 */

public class GetProfileKolDataSourceCloud {
    private final KolApi kolApi;
    private final GetProfileKolDataMapper getProfileKolDataMapper;

    @Inject
    public GetProfileKolDataSourceCloud(KolApi kolApi,
                                        GetProfileKolDataMapper getProfileKolDataMapper) {
        this.kolApi = kolApi;
        this.getProfileKolDataMapper = getProfileKolDataMapper;
    }

    @Inject


    public Observable<List<KolViewModel>> getProfileKolData(RequestParams params) {
        return kolApi.getProfileKolData(params.getString("hah", "hah"))
                .map(getProfileKolDataMapper);
    }
}
