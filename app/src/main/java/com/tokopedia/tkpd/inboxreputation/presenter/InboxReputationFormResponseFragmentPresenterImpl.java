package com.tokopedia.tkpd.inboxreputation.presenter;

import android.os.Bundle;

import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.inboxreputation.fragment.InboxReputationFormResponseFragment;
import com.tokopedia.tkpd.inboxreputation.intentservice.InboxReviewIntentService;
import com.tokopedia.tkpd.inboxreputation.interactor.ActReputationRetrofitInteractor;
import com.tokopedia.tkpd.inboxreputation.interactor.ActReputationRetrofitInteractorImpl;
import com.tokopedia.tkpd.inboxreputation.listener.InboxReputationFormResponseFragmentView;
import com.tokopedia.tkpd.inboxreputation.model.param.ActReviewPass;

import org.parceler.Parcels;

/**
 * Created by Nisie on 2/29/16.
 */
public class InboxReputationFormResponseFragmentPresenterImpl implements InboxReputationFormResponseFragmentPresenter {

    private InboxReputationFormResponseFragmentView viewListener;
    ActReputationRetrofitInteractor actReputationRetrofitInteractor;

    InboxReputationFormResponseFragment.DoActionReputationListener listener;

    public InboxReputationFormResponseFragmentPresenterImpl(InboxReputationFormResponseFragment viewListener) {
        this.actReputationRetrofitInteractor = new ActReputationRetrofitInteractorImpl();
        this.viewListener = viewListener;
        this.listener = (InboxReputationFormResponseFragment.DoActionReputationListener) viewListener.getActivity();
    }

    @Override
    public int generateRating(int rating) {
        switch (rating) {
            case 0:
                return R.drawable.ic_star_none;
            case 1:
                return R.drawable.ic_star_one;
            case 2:
                return R.drawable.ic_star_two;
            case 3:
                return R.drawable.ic_star_three;
            case 4:
                return R.drawable.ic_star_four;
            case 5:
                return R.drawable.ic_star_five;
            default:
                return R.drawable.ic_star_none;
        }
    }

    @Override
    public void onPreviewImageClicked() {

    }

    @Override
    public void postResponse(String response) {

        viewListener.showLoading();
        viewListener.setActionsEnabled(false);

        Bundle param = new Bundle();
        param.putParcelable(InboxReviewIntentService.PARAM_POST_RESPONSE,
                getParamReply(response));
        listener.postResponse(param);

    }

    @Override
    public void onDestroyView() {
        actReputationRetrofitInteractor.unSubscribeObservable();
    }

    private ActReviewPass getParamReply(String response) {
        ActReviewPass paramReply = new ActReviewPass();
        paramReply.setReputationId(viewListener.getInboxReputation().getReputationId());
        paramReply.setResponseMessage(response);
        paramReply.setReviewId(viewListener.getInboxReputationDetail().getReviewId());
        paramReply.setShopId(viewListener.getInboxReputationDetail().getShopId());
        return paramReply;
    }

}
