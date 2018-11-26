package com.tokopedia.digital.nostylecategory.digitalcategory.presentation.presenter;

import android.util.Log;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.digital.nostylecategory.digitalcategory.data.api.entity.ResponseAgentDigitalCategory;
import com.tokopedia.digital.nostylecategory.digitalcategory.domain.usecase.DigitalCategoryNoStyleUseCase;
import com.tokopedia.digital.nostylecategory.digitalcategory.presentation.mapper.RechargeCategoryDetailMapper;
import com.tokopedia.digital.nostylecategory.digitalcategory.presentation.model.DigitalCategoryModel;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.usecase.RequestParams;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by Rizky on 06/09/18.
 */
public class DigitalOperatorChooserNoStylePresenter
        extends BaseDaggerPresenter<DigitalOperatorChooserNoStyleContract.View>
        implements DigitalOperatorChooserNoStyleContract.Presenter {

    private final String TAG = DigitalOperatorChooserNoStylePresenter.class.getSimpleName();

    private DigitalCategoryNoStyleUseCase digitalCategoryNoStyleUseCase;
    private RechargeCategoryDetailMapper rechargeCategoryDetailMapper;

    @Inject
    public DigitalOperatorChooserNoStylePresenter(DigitalCategoryNoStyleUseCase digitalCategoryNoStyleUseCase,
                                                  RechargeCategoryDetailMapper rechargeCategoryDetailMapper) {
        this.digitalCategoryNoStyleUseCase = digitalCategoryNoStyleUseCase;
        this.rechargeCategoryDetailMapper = rechargeCategoryDetailMapper;
    }

    @Override
    public void getOperators(int categoryId) {
        RequestParams requestParams = digitalCategoryNoStyleUseCase.createRequestParams(categoryId);
        digitalCategoryNoStyleUseCase.execute(requestParams, new Subscriber<GraphqlResponse>() {
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

                getView().renderOperators(digitalCategoryModel.getRenderOperatorModel().getRenderProductModels());
            }
        });
    }

}
