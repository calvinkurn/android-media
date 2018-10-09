package com.tokopedia.mitra.digitalcategory.presentation.presenter;

import android.util.Log;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.common_digital.product.presentation.model.RenderProductModel;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.mitra.digitalcategory.data.api.entity.ResponseAgentDigitalCategory;
import com.tokopedia.mitra.digitalcategory.domain.usecase.MitraDigitalCategoryUseCase;
import com.tokopedia.mitra.digitalcategory.presentation.mapper.RechargeCategoryDetailMapper;
import com.tokopedia.mitra.digitalcategory.presentation.model.DigitalCategoryModel;
import com.tokopedia.usecase.RequestParams;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by Rizky on 10/09/18.
 */
public class MitraDigitalProductChooserPresenter extends BaseDaggerPresenter<MitraDigitalProductChooserContract.View>
        implements MitraDigitalProductChooserContract.Presenter {

    private final String TAG = MitraDigitalProductChooserPresenter.class.getSimpleName();

    private MitraDigitalCategoryUseCase mitraDigitalCategoryUseCase;
    private RechargeCategoryDetailMapper rechargeCategoryDetailMapper;

    @Inject
    public MitraDigitalProductChooserPresenter(MitraDigitalCategoryUseCase mitraDigitalCategoryUseCase,
                                               RechargeCategoryDetailMapper rechargeCategoryDetailMapper) {
        this.mitraDigitalCategoryUseCase = mitraDigitalCategoryUseCase;
        this.rechargeCategoryDetailMapper = rechargeCategoryDetailMapper;
    }

    @Override
    public void getProducts(int categoryId, String operatorId) {
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
