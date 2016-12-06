package com.tokopedia.core.talk.talkproduct.presenter;

import android.content.Context;
import android.os.Bundle;

import com.tokopedia.core.talk.talkproduct.activity.TalkAddNew;
import com.tokopedia.core.talk.talkproduct.fragment.TalkAddNewFragment;
import com.tokopedia.core.talk.talkproduct.intentservice.TalkAddNewIntentService;
import com.tokopedia.core.talk.talkproduct.interactor.TalkAddNewRetrofitInteractor;
import com.tokopedia.core.talk.talkproduct.interactor.TalkAddNewRetrofitInteractorImpl;
import com.tokopedia.core.talk.talkproduct.listener.TalkAddNewView;
import com.tokopedia.core.talk.talkproduct.model.AddNewTalkPass;

/**
 * Created by stevenfredian on 8/2/16.
 */
public class TalkAddNewPresenterImpl implements TalkAddNewPresenter{

    TalkAddNewView view;
    TalkAddNewRetrofitInteractor facade;
    TalkAddNew listener;

    public TalkAddNewPresenterImpl(TalkAddNewFragment fragment) {
        view = fragment;
        facade = TalkAddNewRetrofitInteractorImpl.createInstance(this);
        listener = (TalkAddNew) fragment.getActivity();
    }


    @Override
    public void send(Context context, AddNewTalkPass param) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(TalkAddNewIntentService.PARAM_ADD_TALK,param);
        listener.addNewTalk(bundle);
    }

    @Override
    public void unSubscribe() {
        facade.unSubscribe();
    }

}
