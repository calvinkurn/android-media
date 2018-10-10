package com.tokopedia.digital.nostylecategory.digitalcategory.presentation.presenter;

import android.util.Log;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.digital.nostylecategory.digitalcategory.data.api.entity.ResponseAgentDigitalCategory;
import com.tokopedia.digital.nostylecategory.digitalcategory.domain.usecase.MitraDigitalCategoryUseCase;
import com.tokopedia.digital.nostylecategory.digitalcategory.presentation.mapper.RechargeCategoryDetailMapper;
import com.tokopedia.digital.nostylecategory.digitalcategory.presentation.model.DigitalCategoryModel;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.usecase.RequestParams;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by Rizky on 06/09/18.
 */
public class MitraDigitalOperatorChooserPresenter
        extends BaseDaggerPresenter<MitraDigitalOperatorChooserContract.View>
        implements MitraDigitalOperatorChooserContract.Presenter {

    private final String TAG = MitraDigitalOperatorChooserPresenter.class.getSimpleName();

    private MitraDigitalCategoryUseCase mitraDigitalCategoryUseCase;
    private RechargeCategoryDetailMapper rechargeCategoryDetailMapper;

    @Inject
    public MitraDigitalOperatorChooserPresenter(MitraDigitalCategoryUseCase mitraDigitalCategoryUseCase,
                                                RechargeCategoryDetailMapper rechargeCategoryDetailMapper) {
        this.mitraDigitalCategoryUseCase = mitraDigitalCategoryUseCase;
        this.rechargeCategoryDetailMapper = rechargeCategoryDetailMapper;
    }

    @Override
    public void getOperators(int categoryId) {
        RequestParams requestParams = mitraDigitalCategoryUseCase.createRequestParams(categoryId);
        mitraDigitalCategoryUseCase.execute(requestParams, new Subscriber<GraphqlResponse>() {
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
