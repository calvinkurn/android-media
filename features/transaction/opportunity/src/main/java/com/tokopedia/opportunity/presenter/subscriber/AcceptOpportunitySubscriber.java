package com.tokopedia.opportunity.presenter.subscriber;

import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.network.retrofit.response.ErrorListener;
import com.tokopedia.seller.R;
import com.tokopedia.opportunity.data.AcceptReplacementModel;
import com.tokopedia.opportunity.listener.OpportunityView;
import com.tokopedia.opportunity.presentation.ActionViewData;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import rx.Subscriber;

/**
 * Created by nisie on 4/6/17.
 */

public class AcceptOpportunitySubscriber extends Subscriber<AcceptReplacementModel> {

    private final OpportunityView view;

    public AcceptOpportunitySubscriber(OpportunityView view) {
        this.view = view;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        view.onErrorTakeOpportunity(ErrorHandler.getErrorMessage(e));
    }

    @Override
    public void onNext(AcceptReplacementModel acceptReplacementModel) {
        if (acceptReplacementModel.isSuccess())
            view.onSuccessTakeOpportunity(mappingAcceptReplacementViewModel(acceptReplacementModel));
        else
            view.onErrorTakeOpportunity(mappingAcceptReplacementViewModel(acceptReplacementModel).getMessage());
    }

    private ActionViewData mappingAcceptReplacementViewModel(AcceptReplacementModel model) {
        ActionViewData data = new ActionViewData();
        data.setSuccess(model.isSuccess());
        data.setMessage(model.getMessage());
        return data;
    }
}
