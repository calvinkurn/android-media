package com.tokopedia.tkpd.deeplink.domain.branchio;

import com.tokopedia.tkpd.deeplink.source.BranchIODataRepository;
import com.tokopedia.tkpd.deeplink.source.entity.BranchIOAndroidDeepLink;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by sandeepgoyal on 16/03/18.
 */

public class BranchIODeeplinkUseCase extends UseCase<BranchIOAndroidDeepLink>  {


    BranchIODataRepository branchIODataRepository;
    @Inject
    public BranchIODeeplinkUseCase(BranchIODataRepository branchIODataRepository) {
        this.branchIODataRepository = branchIODataRepository;
    }


    @Override
    public Observable<BranchIOAndroidDeepLink> createObservable(RequestParams requestParams) {
        return branchIODataRepository.getDeepLink(requestParams.getParameters());
    }
}
