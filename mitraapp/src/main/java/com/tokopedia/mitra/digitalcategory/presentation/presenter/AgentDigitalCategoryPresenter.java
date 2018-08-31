package com.tokopedia.mitra.digitalcategory.presentation.presenter;

import android.util.Log;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.mitra.digitalcategory.data.api.entity.ResponseAgentDigitalCategory;
import com.tokopedia.mitra.digitalcategory.domain.usecase.AgentDigitalCategoryUseCase;
import com.tokopedia.usecase.RequestParams;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by Rizky on 30/08/18.
 */
public class AgentDigitalCategoryPresenter extends BaseDaggerPresenter<AgentDigitalCategoryContract.View>
        implements AgentDigitalCategoryContract.Presenter {

    private final String TAG = AgentDigitalCategoryPresenter.class.getSimpleName();

    private AgentDigitalCategoryUseCase agentDigitalCategoryUseCase;

    @Inject
    public AgentDigitalCategoryPresenter(AgentDigitalCategoryUseCase agentDigitalCategoryUseCase) {
        this.agentDigitalCategoryUseCase = agentDigitalCategoryUseCase;
    }

    @Override
    public void getCategory(int categoryId) {
        RequestParams requestParams = agentDigitalCategoryUseCase.createRequestParams(categoryId);
        agentDigitalCategoryUseCase.execute(requestParams, new Subscriber<GraphqlResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, e.getMessage());
            }

            @Override
            public void onNext(GraphqlResponse graphqlResponse) {
                Log.d(TAG, graphqlResponse.getData(ResponseAgentDigitalCategory.class).toString());
            }
        });
    }

}
