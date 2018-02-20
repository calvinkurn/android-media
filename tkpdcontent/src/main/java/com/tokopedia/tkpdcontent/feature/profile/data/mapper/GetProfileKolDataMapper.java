package com.tokopedia.tkpdcontent.feature.profile.data.mapper;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.tkpdcontent.feature.profile.view.viewmodel.KolViewModel;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author by milhamj on 20/02/18.
 */

public class GetProfileKolDataMapper implements Func1<Response<DataResponse<List<String>>>, List<KolViewModel>> {

    @Inject
    public GetProfileKolDataMapper() {
    }

    @Override
    public List<KolViewModel> call(Response<DataResponse<List<String>>> dataResponseResponse) {
        return null;
    }
}
