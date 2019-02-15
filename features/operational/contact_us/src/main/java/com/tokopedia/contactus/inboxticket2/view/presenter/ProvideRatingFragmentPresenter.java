package com.tokopedia.contactus.inboxticket2.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.contactus.inboxticket2.data.model.ChipGetInboxDetail;
import com.tokopedia.contactus.inboxticket2.domain.usecase.InboxOptionUseCase;
import com.tokopedia.contactus.inboxticket2.domain.usecase.SubmitRatingUseCase;
import com.tokopedia.contactus.inboxticket2.view.contract.ProvideRatingContract;

import javax.inject.Inject;

import rx.Subscriber;

public class ProvideRatingFragmentPresenter extends BaseDaggerPresenter<ProvideRatingContract.ProvideRatingView>  implements ProvideRatingContract.ProvideRatingPresenter {
    InboxOptionUseCase inboxOptionUseCase;
    SubmitRatingUseCase submitRatingUseCase;

    @Inject
    public ProvideRatingFragmentPresenter(InboxOptionUseCase inboxOptionUseCase, SubmitRatingUseCase submitRatingUseCase) {
        this.inboxOptionUseCase = inboxOptionUseCase;
        this.submitRatingUseCase = submitRatingUseCase;
    }

    @Override
    public void attachView(ProvideRatingContract.ProvideRatingView view) {
        super.attachView(view);
        inboxOptionUseCase.createRequestParams("5005D000004gzzzQAA");
        inboxOptionUseCase.execute(new Subscriber<ChipGetInboxDetail>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ChipGetInboxDetail chipGetInboxDetail) {
                getView().setFirstOption(chipGetInboxDetail.getData().getTickets().getBadCsatReasonList().get(0).getMessage());
                getView().setSecondOption(chipGetInboxDetail.getData().getTickets().getBadCsatReasonList().get(1).getMessage());
                getView().setThirdOption(chipGetInboxDetail.getData().getTickets().getBadCsatReasonList().get(2).getMessage());
                getView().setFourthOption(chipGetInboxDetail.getData().getTickets().getBadCsatReasonList().get(3).getMessage());
            }
        });
    }
}
