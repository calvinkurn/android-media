package com.tokopedia.affiliate.feature.createpost.domain.usecase;

import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author by milhamj on 10/1/18.
 */
public class SubmitPostUseCase extends UseCase<Boolean> {

    private final UploadMultipleImageUseCase uploadMultipleImageUseCase;
    private final GraphqlUseCase graphqlUseCase;

    @Inject
    SubmitPostUseCase(UploadMultipleImageUseCase uploadMultipleImageUseCase,
                             GraphqlUseCase graphqlUseCase) {
        this.uploadMultipleImageUseCase = uploadMultipleImageUseCase;
        this.graphqlUseCase = graphqlUseCase;
    }

    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        return uploadMultipleImageUseCase.createObservable(requestParams).flatMap(submitPostToGraphql());
    }

    private Func1<List<String>, Observable<Boolean>> submitPostToGraphql() {
        return imageUrlList -> {
            graphqlUseCase.clearRequest();
            return graphqlUseCase.createObservable(RequestParams.create()).map(mapResponse());
        };
    }

    private Func1<GraphqlResponse, Boolean> mapResponse() {
        return graphqlResponse -> {
            return false;
        };
    }
}
