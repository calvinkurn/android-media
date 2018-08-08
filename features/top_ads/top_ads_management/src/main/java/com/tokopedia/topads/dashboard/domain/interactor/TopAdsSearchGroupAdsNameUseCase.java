package com.tokopedia.topads.dashboard.domain.interactor;

import android.text.TextUtils;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.topads.dashboard.data.model.data.GroupAd;
import com.tokopedia.topads.dashboard.domain.TopAdsGroupAdsRepository;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by zulfikarrahman on 2/20/17.
 */
public class TopAdsSearchGroupAdsNameUseCase extends UseCase<List<GroupAd>> {

    public static final String KEYWORD_NAME = "KEYWORD_NAME";
    private final TopAdsGroupAdsRepository topAdsGroupAdsRepository;

    @Inject
    public TopAdsSearchGroupAdsNameUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread, TopAdsGroupAdsRepository topAdsGroupAdsRepository) {
        super(threadExecutor, postExecutionThread);
        this.topAdsGroupAdsRepository = topAdsGroupAdsRepository;
    }

    public static RequestParams createRequestParams(String keywordName){
        RequestParams params = RequestParams.create();
        if (!TextUtils.isEmpty(keywordName)) {
            params.putString(KEYWORD_NAME, keywordName);
        }
        return params;
    }

    @Override
    public Observable<List<GroupAd>> createObservable(RequestParams requestParams) {
        return topAdsGroupAdsRepository.searchGroupAds(requestParams.getString(KEYWORD_NAME, ""));
    }
}
