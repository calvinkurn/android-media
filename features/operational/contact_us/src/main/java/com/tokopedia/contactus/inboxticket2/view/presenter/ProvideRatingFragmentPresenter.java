package com.tokopedia.contactus.inboxticket2.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.contactus.inboxticket2.data.model.ChipGetInboxDetail;
import com.tokopedia.contactus.inboxticket2.domain.usecase.InboxOptionUseCase;
import com.tokopedia.contactus.inboxticket2.domain.usecase.SubmitRatingUseCase;
import com.tokopedia.contactus.inboxticket2.view.contract.ProvideRatingContract;
import com.tokopedia.contactus.inboxticket2.view.presenter.screenState.FifthScreenState;
import com.tokopedia.contactus.inboxticket2.view.presenter.screenState.FirstScreenState;
import com.tokopedia.contactus.inboxticket2.view.presenter.screenState.FourthScreenState;
import com.tokopedia.contactus.inboxticket2.view.presenter.screenState.ScreenState;
import com.tokopedia.contactus.inboxticket2.view.presenter.screenState.SecondScreenState;
import com.tokopedia.contactus.inboxticket2.view.presenter.screenState.ThirdScreenState;
import com.tokopedia.usecase.RequestParams;

import javax.inject.Inject;

import rx.Subscriber;

public class ProvideRatingFragmentPresenter extends BaseDaggerPresenter<ProvideRatingContract.ProvideRatingView>  implements ProvideRatingContract.ProvideRatingPresenter {
    InboxOptionUseCase inboxOptionUseCase;
    SubmitRatingUseCase submitRatingUseCase;
    int emojiState = 0;

    @Inject
    public ProvideRatingFragmentPresenter(InboxOptionUseCase inboxOptionUseCase, SubmitRatingUseCase submitRatingUseCase) {
        this.inboxOptionUseCase = inboxOptionUseCase;
        this.submitRatingUseCase = submitRatingUseCase;
    }

    @Override
    public void attachView(ProvideRatingContract.ProvideRatingView view) {
        super.attachView(view);
        emojiState = getView().getSelectedEmoji();
        updateScreenState();
        inboxOptionUseCase.createRequestParams(getView().getTicketId());
        inboxOptionUseCase.execute(new Subscriber<ChipGetInboxDetail>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().showErrorMessage(e.getMessage());
            }

            @Override
            public void onNext(ChipGetInboxDetail chipGetInboxDetail) {
                if(chipGetInboxDetail.getMessageError() != null && chipGetInboxDetail.getMessageError().size() > 0) {
                    getView().showErrorMessage(chipGetInboxDetail.getMessageError().get(0));
                }else {
                    if(chipGetInboxDetail.getData().getTickets().getBadCsatReasonList().size() > 0) {
                        getView().setFilterList(chipGetInboxDetail.getData().getTickets().getBadCsatReasonList());
                    }
                }
            }
        });
    }

    public ScreenState getScreenState(int emoji) {
        ScreenState screenState = null;
        switch (emoji) {
            case 1:
                screenState = new FirstScreenState();
                break;
            case 2:
                screenState = new SecondScreenState();
                break;
            case 3:
                screenState = new ThirdScreenState();
                break;
            case 4:
                screenState = new FourthScreenState();
                break;
            case 5:
                screenState = new FifthScreenState();
                break;

        }
        return screenState;

    }

    public void updateScreenState() {
        getView().clearEmoji();
        ScreenState screenState = getScreenState(emojiState);
        getView().setFirstEmoji(screenState.getFirstEmoji());
        getView().setSecondEmoji(screenState.getSecondEmoji());
        getView().setThirdEmoji(screenState.getThirdEmoji());
        getView().setFourthEmoji(screenState.getFourthEmoji());
        getView().setFifthEmoji(screenState.getFifthEmoji());
        getView().setMessage(screenState.getMessage());
        getView().setMessageColor(screenState.getMessageColor());
        getView().setQuestion(screenState.getQuestion());
    }


    @Override
    public void onFirstEmojiClick() {
        emojiState = 1;
        updateScreenState();

    }

    @Override
    public void onSecondEmojiClick() {
        emojiState = 2;
        updateScreenState();
    }

    @Override
    public void onThirdEmojiClick() {
        emojiState = 3;
        updateScreenState();
    }

    @Override
    public void onFourthEmojiClick() {
        emojiState = 4;
        updateScreenState();
    }

    @Override
    public void onFifthEmojiClick() {
        emojiState = 5;
        updateScreenState();
    }

    @Override
    public void onSubmitClick() {
        RequestParams requestParams = submitRatingUseCase.createRequestParams(getView().getTicketId(),emojiState+"",getView().getSelectedItem());
        submitRatingUseCase.execute(requestParams, new Subscriber<ChipGetInboxDetail>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(ChipGetInboxDetail chipGetInboxDetail) {
                if(chipGetInboxDetail.getMessageError() != null && chipGetInboxDetail.getMessageError().size() > 0) {
                    getView().showErrorMessage(chipGetInboxDetail.getMessageError().get(0));
                }else {
                    getView().onSuccessSubmit();
                }
            }
        });
    }
}
