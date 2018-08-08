package com.tokopedia.gm.featured.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.gm.featured.data.model.GMFeaturedProductSubmitModel;
import com.tokopedia.gm.featured.domain.model.GMFeaturedProductSubmitDomainModel;
import com.tokopedia.gm.featured.repository.GMFeaturedProductRepository;
import com.tokopedia.gm.featured.view.adapter.model.GMFeaturedProductModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by normansyahputa on 9/6/17.
 */

public class GMFeaturedProductSubmitUseCase extends UseCase<GMFeaturedProductSubmitDomainModel> {

    public static final String FEATURED_PRODUCT_MODEL_PARAM = "FEATURED_PRODUCT_MODEL_PARAM";

    private GMFeaturedProductRepository GMFeaturedProductRepository;

    @Inject
    public GMFeaturedProductSubmitUseCase(
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread,
            GMFeaturedProductRepository GMFeaturedProductRepository
    ) {
        super(threadExecutor, postExecutionThread);
        this.GMFeaturedProductRepository = GMFeaturedProductRepository;
    }

    private static GMFeaturedProductSubmitModel createRawParam(List<GMFeaturedProductModel> GMFeaturedProductModels) {
        GMFeaturedProductSubmitModel GMFeaturedProductSubmitModel =
                new GMFeaturedProductSubmitModel();

        ArrayList<GMFeaturedProductSubmitModel.ItemsFeatured> itemsFeatureds = new ArrayList<>();
        for (int i = 1; i <= GMFeaturedProductModels.size(); i++) {
            GMFeaturedProductModel GMFeaturedProductModel = GMFeaturedProductModels.get(i - 1);

            GMFeaturedProductSubmitModel.ItemsFeatured itemsFeatured
                    = new GMFeaturedProductSubmitModel.ItemsFeatured();
            itemsFeatured.setType(1);
            itemsFeatured.setOrder(i);
            itemsFeatured.setProductId(GMFeaturedProductModel.getProductId());

            itemsFeatureds.add(itemsFeatured);
        }

        GMFeaturedProductSubmitModel.setItemsFeatured(itemsFeatureds);

        return GMFeaturedProductSubmitModel;
    }

    public static RequestParams createParam(List<GMFeaturedProductModel> GMFeaturedProductModels) {
        RequestParams requestParams = RequestParams.create();

        requestParams.putObject(FEATURED_PRODUCT_MODEL_PARAM, createRawParam(GMFeaturedProductModels));

        return requestParams;
    }

    @Override
    public Observable<GMFeaturedProductSubmitDomainModel> createObservable(RequestParams requestParams) {
        return GMFeaturedProductRepository.postFeatureProductData(requestParams);
    }
}
