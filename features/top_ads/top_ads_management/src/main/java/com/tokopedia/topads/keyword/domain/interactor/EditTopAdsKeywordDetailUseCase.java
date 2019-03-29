package com.tokopedia.topads.keyword.domain.interactor;

import com.tokopedia.topads.keyword.domain.TopAdsKeywordRepository;
import com.tokopedia.topads.keyword.domain.model.EditTopAdsKeywordDetailDomainModel;
import com.tokopedia.topads.keyword.domain.model.TopAdsKeywordEditDetailInputDomainModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author sebastianuskh on 5/29/17.
 */

public class EditTopAdsKeywordDetailUseCase extends UseCase<EditTopAdsKeywordDetailDomainModel> {

    public static final String TOPADS_KEYWORD_EDIT_DETAIL_INPUT = "TOPADS_KEYWORD_EDIT_DETAIL_INPUT";
    private final TopAdsKeywordRepository topAdsKeywordRepository;

    @Inject
    public EditTopAdsKeywordDetailUseCase(TopAdsKeywordRepository topAdsKeywordRepository) {
        super();
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
