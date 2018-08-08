package com.tokopedia.checkout.view.view.shippingrecommendation.shippingduration.view;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.checkout.R;
import com.tokopedia.checkout.domain.datamodel.shipmentrates.ShipmentDetailData;
import com.tokopedia.checkout.domain.usecase.GetCourierRecommendationUseCase;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.GetRatesCourierRecommendationData;
import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.ServiceData;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by Irfan Khoirul on 06/08/18.
 */

public class ShippingDurationPresenter extends BaseDaggerPresenter<ShippingDurationContract.View>
        implements ShippingDurationContract.Presenter {

    private final GetCourierRecommendationUseCase getCourierRecommendationUseCase;
    private final ShippingDurationConverter shippingDurationConverter;

    private List<ShippingDurationViewModel> shippingDurationViewModelList;

    @Inject
    public ShippingDurationPresenter(GetCourierRecommendationUseCase getCourierRecommendationUseCase,
                                     ShippingDurationConverter shippingDurationConverter) {
        this.getCourierRecommendationUseCase = getCourierRecommendationUseCase;
        this.shippingDurationConverter = shippingDurationConverter;
    }

    @Override
    public void attachView(ShippingDurationContract.View view) {
        super.attachView(view);
    }

    @Override
    public void detachView() {
        super.detachView();
        getCourierRecommendationUseCase.unsubscribe();
    }

    @Override
    public void loadCourierRecommendation(ShipmentDetailData shipmentDetailData) {
        if (getView() != null) {
            getView().showLoading();
            String query = GraphqlHelper.loadRawString(getView().getActivity().getResources(), R.raw.rates_v3_query);
            getCourierRecommendationUseCase.execute(query, shipmentDetailData,
                    new Subscriber<GraphqlResponse>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            if (getView() != null) {
                                getView().showNoConnection(ErrorHandler.getErrorMessage(getView().getActivity(), e));
                            }
                        }

                        @Override
                        public void onNext(GraphqlResponse graphqlResponse) {
                            if (getView() != null) {
                                getView().hideLoading();
                                GetRatesCourierRecommendationData data = graphqlResponse.getData(GetRatesCourierRecommendationData.class);
                                if (data != null && data.getRatesData() != null &&
                                        data.getRatesData().getRatesDetailData() != null &&
                                        data.getRatesData().getRatesDetailData().getServices() != null) {
                                    if (shippingDurationViewModelList == null) {
                                        shippingDurationViewModelList = new ArrayList<>();
                                    }
                                    shippingDurationViewModelList.clear();
                                    List<ShippingDurationViewModel> shippingDurationViewModels =
                                            shippingDurationConverter.convertToViewModel(
                                                    data.getRatesData().getRatesDetailData().getServices());
                                    shippingDurationViewModelList.addAll(shippingDurationViewModels);
                                    shippingDurationViewModelList.addAll(shippingDurationViewModels);
                                    shippingDurationViewModelList.addAll(shippingDurationViewModels);
                                    shippingDurationViewModelList.addAll(shippingDurationViewModels);
                                    shippingDurationViewModelList.addAll(shippingDurationViewModels);
                                    shippingDurationViewModelList.addAll(shippingDurationViewModels);
                                    shippingDurationViewModelList.addAll(shippingDurationViewModels);
                                    shippingDurationViewModelList.addAll(shippingDurationViewModels);
                                    shippingDurationViewModelList.addAll(shippingDurationViewModels);
                                    shippingDurationViewModelList.addAll(shippingDurationViewModels);
                                    getView().showData(shippingDurationViewModelList);
                                }
                            }
                        }
                    });
        }
    }

    @Override
    public List<ShippingDurationViewModel> getShippingDurationViewModels() {
        if (shippingDurationViewModelList == null) {
            shippingDurationViewModelList = new ArrayList<>();
        }
        return shippingDurationViewModelList;
    }

}
