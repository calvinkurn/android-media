package com.tokopedia.mitra.digitalcategory.presentation.presenter;

import android.util.Log;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.common_digital.product.presentation.model.ClientNumber;
import com.tokopedia.common_digital.product.presentation.model.InputFieldModel;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.mitra.digitalcategory.data.api.entity.ResponseAgentDigitalCategory;
import com.tokopedia.mitra.digitalcategory.domain.usecase.AgentDigitalCategoryUseCase;
import com.tokopedia.mitra.digitalcategory.presentation.mapper.RechargeCategoryDetailMapper;
import com.tokopedia.mitra.digitalcategory.presentation.model.DigitalCategoryModel;
import com.tokopedia.usecase.RequestParams;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by Rizky on 30/08/18.
 */
public class MitraDigitalCategoryPresenter extends BaseDaggerPresenter<AgentDigitalCategoryContract.View>
        implements AgentDigitalCategoryContract.Presenter {

    private final String TAG = MitraDigitalCategoryPresenter.class.getSimpleName();

    private AgentDigitalCategoryUseCase agentDigitalCategoryUseCase;
    private RechargeCategoryDetailMapper rechargeCategoryDetailMapper;

    @Inject
    public MitraDigitalCategoryPresenter(AgentDigitalCategoryUseCase agentDigitalCategoryUseCase,
                                         RechargeCategoryDetailMapper rechargeCategoryDetailMapper) {
        this.agentDigitalCategoryUseCase = agentDigitalCategoryUseCase;
        this.rechargeCategoryDetailMapper = rechargeCategoryDetailMapper;
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

                ResponseAgentDigitalCategory responseAgentDigitalCategory = graphqlResponse.getData(ResponseAgentDigitalCategory.class);
                DigitalCategoryModel digitalCategoryModel = rechargeCategoryDetailMapper.map(responseAgentDigitalCategory);

                getView().renderWidgetView(digitalCategoryModel.getRenderOperatorModel());
            }
        });
    }

    private ClientNumber transforInputFieldToClientNumber(InputFieldModel inputFieldModel) {
        return new ClientNumber(inputFieldModel.getName(), inputFieldModel.getType(), inputFieldModel.getText(),
                inputFieldModel.getPlaceholder(), inputFieldModel.getDefault(), inputFieldModel.getValidation());
    }

}
