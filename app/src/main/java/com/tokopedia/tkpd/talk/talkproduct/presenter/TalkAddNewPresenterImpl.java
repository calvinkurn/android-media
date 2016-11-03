package com.tokopedia.tkpd.talk.talkproduct.presenter;

import android.content.Context;
import android.os.Bundle;

import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.talk.talkproduct.activity.TalkAddNew;
import com.tokopedia.tkpd.talk.talkproduct.fragment.TalkAddNewFragment;
import com.tokopedia.tkpd.talk.talkproduct.intentservice.TalkAddNewIntentService;
import com.tokopedia.tkpd.talk.talkproduct.interactor.TalkAddNewRetrofitInteractor;
import com.tokopedia.tkpd.talk.talkproduct.interactor.TalkAddNewRetrofitInteractorImpl;
import com.tokopedia.tkpd.talk.talkproduct.listener.TalkAddNewView;
import com.tokopedia.tkpd.talk.talkproduct.model.AddNewTalkPass;

import org.json.JSONObject;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Map;

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
