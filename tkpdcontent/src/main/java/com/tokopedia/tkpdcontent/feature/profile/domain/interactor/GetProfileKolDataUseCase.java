package com.tokopedia.tkpdcontent.feature.profile.domain.interactor;

import com.tokopedia.tkpdcontent.feature.profile.data.source.GetProfileKolDataSourceCloud;
import com.tokopedia.tkpdcontent.feature.profile.view.viewmodel.KolViewModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author by milhamj on 20/02/18.
 */

public class GetProfileKolDataUseCase extends UseCase<List<KolViewModel>> {
    private final GetProfileKolDataSourceCloud getProfileKolDataSourceCloud;

    @Inject
    public GetProfileKolDataUseCase(GetProfileKolDataSourceCloud getProfileKolDataSourceCloud) {
        this.getProfileKolDataSourceCloud = getProfileKolDataSourceCloud;
    }

    @Override
    public Observable<List<KolViewModel>> createObservable(RequestParams requestParams) {
        return getProfileKolDataSourceCloud.getProfileKolData(requestParams);
    }
}
