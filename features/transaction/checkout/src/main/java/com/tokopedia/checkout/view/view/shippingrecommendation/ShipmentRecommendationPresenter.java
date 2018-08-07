package com.tokopedia.checkout.view.view.shippingrecommendation;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.checkout.R;
import com.tokopedia.checkout.domain.datamodel.shipmentrates.ShipmentDetailData;
import com.tokopedia.checkout.domain.usecase.GetCourierRecommendationUseCase;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.GetCourierRecommendationData;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by Irfan Khoirul on 06/08/18.
 */

public class ShipmentRecommendationPresenter extends BaseDaggerPresenter<ShipmentRecommendationContract.View>
        implements ShipmentRecommendationContract.Presenter {

    private final GetCourierRecommendationUseCase getCourierRecommendationUseCase;

    @Inject
    public ShipmentRecommendationPresenter(GetCourierRecommendationUseCase getCourierRecommendationUseCase) {
        this.getCourierRecommendationUseCase = getCourierRecommendationUseCase;
    }

    @Override
    public void attachView(ShipmentRecommendationContract.View view) {
        super.attachView(view);
    }

    @Override
    public void detachView() {
        super.detachView();
        getCourierRecommendationUseCase.unsubscribe();
    }

    @Override
    public void loadCourierRecommendation(ShipmentDetailData shipmentDetailData) {
        String query = GraphqlHelper.loadRawString(getView().getActivity().getResources(), R.raw.rates_v3_query);
        getCourierRecommendationUseCase.execute(query, shipmentDetailData,
                new Subscriber<GraphqlResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(GraphqlResponse graphqlResponse) {
                        GetCourierRecommendationData data = graphqlResponse.getData(GetCourierRecommendationData.class);

                    }
                });
    }
}
