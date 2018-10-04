package com.tokopedia.topads.keyword.domain.interactor;

import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.topads.keyword.domain.TopAdsKeywordRepository;
import com.tokopedia.topads.keyword.domain.model.EditTopAdsKeywordDetailDomainModel;
import com.tokopedia.topads.keyword.domain.model.TopAdsKeywordEditDetailInputDomainModel;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author sebastianuskh on 5/29/17.
 */

public class EditTopAdsKeywordDetailUseCase extends UseCase<EditTopAdsKeywordDetailDomainModel> {

    public static final String TOPADS_KEYWORD_EDIT_DETAIL_INPUT = "TOPADS_KEYWORD_EDIT_DETAIL_INPUT";
    private final TopAdsKeywordRepository topAdsKeywordRepository;

    @Inject
    public EditTopAdsKeywordDetailUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread, TopAdsKeywordRepository topAdsKeywordRepository) {
        super(threadExecutor, postExecutionThread);
        this.topAdsKeywordRepository = topAdsKeywordRepository;
    }

    @Override
    public Observable<EditTopAdsKeywordDetailDomainModel> createObservable(RequestParams requestParams) {
        TopAdsKeywordEditDetailInputDomainModel modelInput =
                (TopAdsKeywordEditDetailInputDomainModel) requestParams
                        .getObject(TOPADS_KEYWORD_EDIT_DETAIL_INPUT);
        return topAdsKeywordRepository.editTopAdsKeywordDetail(modelInput);
    }

    public static RequestParams generateRequestParam(TopAdsKeywordEditDetailInputDomainModel model) {
        RequestParams params = RequestParams.create();
        params.putObject(TOPADS_KEYWORD_EDIT_DETAIL_INPUT, model);
        return params;
    }
}
