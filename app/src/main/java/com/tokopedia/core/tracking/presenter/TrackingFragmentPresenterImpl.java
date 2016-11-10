package com.tokopedia.core.tracking.presenter;

import android.support.annotation.NonNull;
import android.util.Log;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.selling.fragment.TrackingFragment;
import com.tokopedia.core.selling.interactor.TrackingRetrofitInteractor;
import com.tokopedia.core.selling.interactor.TrackingRetrofitInteractorImpl;
import com.tokopedia.core.selling.listener.TrackingFragmentView;
import com.tokopedia.core.selling.model.tracking.TrackingResponse;

/**
 * Created by Alifa on 10/12/2016.
 */

public class TrackingFragmentPresenterImpl implements  TrackingFragmentPresenter {

    public static final int REQUEST_TRACKING_CODE = 1;
    TrackingFragmentView viewListener;
    TrackingRetrofitInteractor networkInteractor;
    String orderId;

    public TrackingFragmentPresenterImpl(TrackingFragment viewListener) {
        this.viewListener = viewListener;
        this.networkInteractor = new TrackingRetrofitInteractorImpl();
    }


    private TKPDMapParam<String, String> getParamTracking() {
        TKPDMapParam<String, String> params = new TKPDMapParam<>();
        if (orderId!=null)
            params.put("order_id", this.orderId);
        return params;
    }



    @Override
    public void loadTrackingData(String orderId) {

        this.orderId = orderId;
        Log.d("alifa", "loadTrackingData: " + this.orderId);
        networkInteractor.getDataTracking(viewListener.getActivity(), getParamTracking(),
                new TrackingRetrofitInteractor.TrackingListener() {


                    @Override
                    public void onSuccess(@NonNull TrackingResponse data) {
                        showData(data);
                    }

                    @Override
                    public void onTimeout(String message) {
                        viewListener.finishLoading();
                        viewListener.showErrorMessage();
                    }

                    @Override
                    public void onError(String error) {
                        viewListener.finishLoading();
                        viewListener.showErrorMessage();

                    }

                    @Override
                    public void onNullData() {
                        viewListener.finishLoading();
                        viewListener.showErrorMessage();
                    }

                    @Override
                    public void onNoNetworkConnection() {
                        viewListener.finishLoading();

                    }
                });
    }

    public void showData(TrackingResponse data) {
        if (data.getTrackOrder().getInvalid() != 1) {
            viewListener.updateHeaderView(data);
            viewListener.renderTrackingData(data);
        } else {
            viewListener.showErrorMessage();
        }
    }


    @Override
    public void onDestroyView() {

    }
}
