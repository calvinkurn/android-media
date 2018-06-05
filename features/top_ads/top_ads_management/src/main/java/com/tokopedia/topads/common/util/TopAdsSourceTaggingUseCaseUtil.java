package com.tokopedia.topads.common.util;

import android.content.Context;

import com.tokopedia.topads.sourcetagging.constant.TopAdsSourceTaggingConstant;
import com.tokopedia.topads.sourcetagging.data.repository.TopAdsSourceTaggingRepositoryImpl;
import com.tokopedia.topads.sourcetagging.data.source.TopAdsSourceTaggingDataSource;
import com.tokopedia.topads.sourcetagging.data.source.TopAdsSourceTaggingLocal;
import com.tokopedia.topads.sourcetagging.domain.interactor.TopAdsAddSourceTaggingUseCase;
import com.tokopedia.topads.sourcetagging.domain.interactor.TopAdsGetSourceTaggingUseCase;

/**
 * Created by nakama on 02/05/18.
 */

public class TopAdsSourceTaggingUseCaseUtil {

    public static TopAdsAddSourceTaggingUseCase getTopAdsAddSourceTaggingUseCase(Context context){
        return new TopAdsAddSourceTaggingUseCase(new TopAdsSourceTaggingRepositoryImpl(
                new TopAdsSourceTaggingDataSource(new TopAdsSourceTaggingLocal(context))));
    }

    public static TopAdsGetSourceTaggingUseCase getTopAdsGetSourceTaggingUseCase(Context context){
        return new TopAdsGetSourceTaggingUseCase(new TopAdsSourceTaggingRepositoryImpl(
                new TopAdsSourceTaggingDataSource(new TopAdsSourceTaggingLocal(context))));
    }

}
