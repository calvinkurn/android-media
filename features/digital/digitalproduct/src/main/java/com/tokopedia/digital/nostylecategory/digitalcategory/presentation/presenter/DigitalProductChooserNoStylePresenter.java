package com.tokopedia.digital.nostylecategory.digitalcategory.presentation.presenter;

import android.util.Log;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.common_digital.product.presentation.model.RenderProductModel;
import com.tokopedia.digital.nostylecategory.digitalcategory.data.api.entity.ResponseAgentDigitalCategory;
import com.tokopedia.digital.nostylecategory.digitalcategory.domain.usecase.DigitalCategoryNoStyleUseCase;
import com.tokopedia.digital.nostylecategory.digitalcategory.presentation.mapper.RechargeCategoryDetailMapper;
import com.tokopedia.digital.nostylecategory.digitalcategory.presentation.model.DigitalCategoryModel;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.usecase.RequestParams;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by Rizky on 10/09/18.
 */
public class DigitalProductChooserNoStylePresenter extends BaseDaggerPresenter<DigitalProductChooserNoStyleContract.View>
        implements DigitalProductChooserNoStyleContract.Presenter {

    private final String TAG = DigitalProductChooserNoStylePresenter.class.getSimpleName();

    private DigitalCategoryNoStyleUseCase digitalCategoryNoStyleUseCase;
    private RechargeCategoryDetailMapper rechargeCategoryDetailMapper;

    @Inject
    public DigitalProductChooserNoStylePresenter(DigitalCategoryNoStyleUseCase digitalCategoryNoStyleUseCase,
                                                 RechargeCategoryDetailMapper rechargeCategoryDetailMapper) {
        this.digitalCategoryNoStyleUseCase = digitalCategoryNoStyleUseCase;
        this.rechargeCategoryDetailMapper = rechargeCategoryDetailMapper;
    }

    @Override
    public void getProducts(int categoryId, String operatorId) {
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
                ResponseAgentDigitalCategory responseAgentDigitalCategory = graphqlResponse.getData(ResponseAgentDigitalCategory.class);
                DigitalCategoryModel digitalCategoryModel = rechargeCategoryDetailMapper.map(responseAgentDigitalCategory);

                for (RenderProductModel renderProductModel : digitalCategoryModel.getRenderOperatorModel().getRenderProductModels()) {
                    if (renderProductModel.getOperator().getOperatorId().equals(operatorId)) {
                        getView().renderProducts(renderProductModel.getOperator().getProductList());
                    }
                }
            }
        });
    }

}
