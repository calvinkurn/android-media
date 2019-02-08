package com.tokopedia.shipping_recommendation.shippingduration.view;

import com.google.gson.Gson;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.GetRatesCourierRecommendationData;
import com.tokopedia.shipping_recommendation.FileUtils;
import com.tokopedia.shipping_recommendation.R;
import com.tokopedia.shipping_recommendation.domain.usecase.GetCourierRecommendationUseCase;
import com.tokopedia.shipping_recommendation.shippingcourier.view.ShippingCourierConverter;
import com.tokopedia.shipping_recommendation.domain.shipping.CourierItemData;
import com.tokopedia.shipping_recommendation.domain.shipping.RecipientAddressModel;
import com.tokopedia.shipping_recommendation.domain.shipping.ShipmentDetailData;
import com.tokopedia.shipping_recommendation.domain.shipping.ShippingCourierViewModel;
import com.tokopedia.shipping_recommendation.domain.shipping.ShippingDurationViewModel;
import com.tokopedia.shipping_recommendation.domain.shipping.ShippingRecommendationData;
import com.tokopedia.shipping_recommendation.domain.shipping.ShopShipment;
import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.ErrorProductData;

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
    private final ShippingCourierConverter shippingCourierConverter;

    private List<ShippingDurationViewModel> shippingDurationViewModelList;
    private RecipientAddressModel recipientAddressModel;

    @Inject
    public ShippingDurationPresenter(GetCourierRecommendationUseCase getCourierRecommendationUseCase,
                                     ShippingDurationConverter shippingDurationConverter,
                                     ShippingCourierConverter shippingCourierConverter) {
        this.getCourierRecommendationUseCase = getCourierRecommendationUseCase;
        this.shippingDurationConverter = shippingDurationConverter;
        this.shippingCourierConverter = shippingCourierConverter;
    }

    @Override
    public RecipientAddressModel getRecipientAddressModel() {
        return recipientAddressModel;
    }

    @Override
    public void setRecipientAddressModel(RecipientAddressModel recipientAddressModel) {
        this.recipientAddressModel = recipientAddressModel;
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
    public void loadCourierRecommendation(ShipmentDetailData shipmentDetailData,
                                          int selectedServiceId,
                                          List<ShopShipment> shopShipmentList) {
        if (getView() != null) {

            // load local response_pilih_durasi
            /*getView().showLoading();
            String raw = new FileUtils().readRawTextFile(getView().getActivity(), R.raw.response_pilih_durasi);
            Gson gson = new Gson();
            GetRatesCourierRecommendationData getRatesCourierRecommendationData = gson.fromJson(raw, GetRatesCourierRecommendationData.class);
            ShippingRecommendationData shippingRecommendationData = new ShippingRecommendationData();
            ShippingDurationConverter shippingDurationConverter = new ShippingDurationConverter();
            shippingRecommendationData.setShippingDurationViewModels(shippingDurationConverter.
                    convertToViewModel(
                            getRatesCourierRecommendationData.getRatesData().getRatesDetailData().getServices(),
                            shopShipmentList, shipmentDetailData, "", 0));
            shippingDurationViewModelList.addAll(shippingRecommendationData.getShippingDurationViewModels());
            getView().hideLoading();
            getView().showData(shippingDurationViewModelList);
            getView().stopTrace();*/

            // load network
            getView().showLoading();
            String query = GraphqlHelper.loadRawString(getView().getActivity().getResources(), R.raw.rates_v3_query_new);
            getCourierRecommendationUseCase.execute(query, shipmentDetailData, selectedServiceId, shopShipmentList,
                    new Subscriber<ShippingRecommendationData>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            if (getView() != null) {
                                getView().showErrorPage(ErrorHandler.getErrorMessage(getView().getActivity(), e));
                                getView().stopTrace();
                            }
                        }

                        @Override
                        public void onNext(ShippingRecommendationData shippingRecommendationData) {
                            if (getView() != null) {
                                getView().hideLoading();
                                if (shippingRecommendationData.getErrorId() != null &&
                                        shippingRecommendationData.getErrorId().equals(ErrorProductData.ERROR_RATES_NOT_AVAILABLE)) {
                                    getView().showNoCourierAvailable(shippingRecommendationData.getErrorMessage());
                                    getView().stopTrace();
                                } else if (shippingRecommendationData.getShippingDurationViewModels() != null &&
                                        shippingRecommendationData.getShippingDurationViewModels().size() > 0) {
                                    shippingDurationViewModelList.addAll(shippingRecommendationData.getShippingDurationViewModels());
                                    getView().showData(shippingDurationViewModelList);
                                    getView().stopTrace();
                                } else {
                                    getView().showNoCourierAvailable(getView().getActivity().getString(R.string.label_no_courier_bottomsheet_message));
                                    getView().stopTrace();
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

    @Override
    public CourierItemData getCourierItemData(List<ShippingCourierViewModel> shippingCourierViewModels) {
        for (ShippingCourierViewModel shippingCourierViewModel : shippingCourierViewModels) {
            if (shippingCourierViewModel.getProductData().isRecommend()) {
                return shippingCourierConverter.convertToCourierItemData(shippingCourierViewModel);
            }
        }

        return null;
    }
}
