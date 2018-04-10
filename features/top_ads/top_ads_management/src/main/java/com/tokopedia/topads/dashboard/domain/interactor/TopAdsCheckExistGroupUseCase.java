package com.tokopedia.topads.dashboard.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.topads.dashboard.domain.TopAdsGroupAdsRepository;
import com.tokopedia.topads.dashboard.data.model.data.GroupAd;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by zulfikarrahman on 2/20/17.
 */

public class TopAdsCheckExistGroupUseCase extends UseCase<Boolean> {
    private static final String KEYWORD_NAME = "KEYWORD_NAME";
    private final TopAdsGroupAdsRepository topAdsGroupAdsRepository;

    public TopAdsCheckExistGroupUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread, TopAdsGroupAdsRepository topAdsGroupAdsRepository) {
        super(threadExecutor, postExecutionThread);
        this.topAdsGroupAdsRepository = topAdsGroupAdsRepository;
    }

    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        return topAdsGroupAdsRepository.searchGroupAds(requestParams.getString(KEYWORD_NAME, ""))
                .map(checkExistGroupName(requestParams.getString(KEYWORD_NAME, "")));
    }

    private Func1<List<GroupAd>,Boolean> checkExistGroupName(final String groupName) {
        return new Func1<List<GroupAd>, Boolean>() {
            @Override
            public Boolean call(List<GroupAd> groupAds) {
                if(groupAds != null && groupAds.size() > 0 ){
                    for(GroupAd groupAd : groupAds){
                        if(groupName.equalsIgnoreCase(groupAd.getName())){
                            return true;
                        }
                    }
                    return false;
                }else {
                    return false;
                }
            }
        };
    }

    public static RequestParams createRequestParams(String keywordName){
        RequestParams params = RequestParams.create();
        params.putString(KEYWORD_NAME, keywordName);
        return params;
    }
}
