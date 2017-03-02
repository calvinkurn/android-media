package com.tokopedia.core.talkview.presenter;

import android.content.Context;
import android.os.Bundle;

import com.tokopedia.core.R;
import com.tokopedia.core.talk.receiver.intentservice.InboxTalkIntentService;
import com.tokopedia.core.talkview.activity.TalkViewActivity;
import com.tokopedia.core.talkview.fragment.TalkViewFragment;
import com.tokopedia.core.talkview.inbox.model.TalkDetail;
import com.tokopedia.core.talkview.intentservice.TalkDetailIntentService;
import com.tokopedia.core.talkview.intentservice.TalkViewIntentService;
import com.tokopedia.core.talkview.interactor.TalkViewRetrofitInteractor;
import com.tokopedia.core.talkview.interactor.TalkViewRetrofitInteractorImpl;
import com.tokopedia.core.talkview.listener.TalkViewListener;
import com.tokopedia.core.talkview.model.ReplyCommentPass;
import com.tokopedia.core.talkview.model.TalkBaseModel;
import com.tokopedia.core.talkview.model.TalkPass;
import com.tokopedia.core.talkview.product.model.CommentTalk;

import org.json.JSONObject;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by stevenfredian on 4/28/16.
 */
public class TalkViewPresenterImpl implements TalkViewPresenter{

    TalkViewListener view;
    TalkViewActivity listener;
    String from;
    TalkViewRetrofitInteractor facade;

    public TalkViewPresenterImpl(TalkViewFragment talkViewFragment, String from) {
        view = talkViewFragment;
        listener = (TalkViewActivity) talkViewFragment.getActivity();
        this.from = from;
        this.facade = TalkViewRetrofitInteractorImpl.createInstance();
    }

    @Override
    public void getComment(final Context context, Map<String, String> paramComment) {
        facade. getComment(context,paramComment,from, new TalkViewRetrofitInteractor.GetCommentListener() {
            @Override
            public void onSuccess(JSONObject Result) {
                view.successGet(Result);
            }

            @Override
            public void onError(String error) {
                view.showError(error);
            }

            @Override
            public void onThrowable(Throwable e) {

                String error;
                if (e instanceof UnknownHostException) {
                    error = context.getString(R.string.default_request_error_unknown);
                } else if (e instanceof SocketTimeoutException) {
                    error = context.getString(R.string.default_request_error_timeout);
                } else {
                    error = context.getString(R.string.default_request_error_internal_server);
                }

                view.showError(error);
            }

            @Override
            public void onTimeout() {
                view.showError(context.getResources().getString(R.string.msg_connection_timeout));
            }
        });
    }

    @Override
    public void reply(final Context context, Map<String, String> param) {
        ReplyCommentPass pass = new ReplyCommentPass();
        pass.setTalkID(view.getTalkID());
        pass.setProductID(view.getProductID());
        pass.setCommentContent(view.getCommentContent());
        Bundle bundle = new Bundle();
        bundle.putParcelable(TalkViewIntentService.PARAM_REPLY, pass);
        listener.replyComment(bundle);
    }

    @Override
    public void unSubscribe() {
        facade.unSubscribe();
    }

    @Override
    public void deleteTalk() {
        TalkPass pass = new TalkPass();
        pass.setTalkId(view.getTalkID());
        pass.setShopId(view.getShopID());
        Bundle bundle = new Bundle();
        bundle.putParcelable(InboxTalkIntentService.PARAM_DELETE,pass);
        listener.deleteTalk(bundle);
    }

    @Override
    public void followTalk() {
        TalkPass pass = new TalkPass();
        pass.setTalkId(view.getTalkID());
        pass.setShopId(view.getShopID());
        pass.setProductId(view.getProductID());
        Bundle bundle = new Bundle();
        bundle.putParcelable(InboxTalkIntentService.PARAM_FOLLOW,pass);
        listener.followTalk(bundle);
    }

    @Override
    public void reportTalk() {
        TalkPass param = new TalkPass();
        param.setTalkId(view.getTalkID());
        param.setShopId(view.getShopID());
        param.setProductId(view.getProductID());
        param.setTextMessage(view.getTextMessage());
        Bundle bundle = new Bundle();
        bundle.putParcelable(InboxTalkIntentService.PARAM_REPORT,param);
        listener.reportTalk(bundle);
    }

    @Override
    public void deleteCommentTalk(TalkDetail talk, int position) {
        TalkPass param = new TalkPass();
        param.setTalkId(talk.getCommentTalkId());
        param.setShopId(talk.getCommentShopId());
        param.setCommentId(talk.getCommentId());
        param.setPosition(position);
        Bundle bundle = new Bundle();
        bundle.putParcelable(TalkDetailIntentService.PARAM_DELETE,param);
        listener.deleteCommentTalk(bundle);
    }

    @Override
    public void reportCommentTalk(TalkDetail talk, int position) {
        TalkPass param = new TalkPass();
        param.setTalkCommentId(talk.getCommentId());
        param.setShopId(talk.getCommentShopId());
        param.setTalkId(talk.getCommentTalkId());
        param.setTextMessage(talk.getCommentMessage());
        param.setPosition(position);
        Bundle bundle = new Bundle();
        bundle.putParcelable(TalkDetailIntentService.PARAM_REPORT,param);
        listener.reportCommentTalk(bundle);
    }

    @Override
    public void deleteCommentTalk(CommentTalk talk, int position) {
        TalkPass param = new TalkPass();
        param.setTalkId(talk.getCommentTalkId());
        param.setShopId(talk.getCommentShopId());
        param.setCommentId(talk.getCommentId());
        param.setPosition(position);
        Bundle bundle = new Bundle();
        bundle.putParcelable(TalkDetailIntentService.PARAM_DELETE,param);
        listener.deleteCommentTalk(bundle);
    }

    @Override
    public void reportCommentTalk(CommentTalk talk, int position) {
        TalkPass param = new TalkPass();
        param.setTalkCommentId(talk.getCommentId());
        param.setShopId(talk.getCommentShopId());
        param.setTalkId(talk.getCommentTalkId());
        param.setTextMessage(talk.getCommentMessage());
        param.setPosition(position);
        Bundle bundle = new Bundle();
        bundle.putParcelable(TalkDetailIntentService.PARAM_REPORT,param);
        listener.reportCommentTalk(bundle);
    }

    @Override
    public void saveState(Bundle state, ArrayList<TalkBaseModel> items, int position,
                          int page, boolean hasNext) {
        if(items!=null){
            state.putSerializable("list", items);
            state.putInt("paging", page);
            state.putBoolean("hasNext", hasNext);
            if(hasNext) state.putInt("position", position-1);
            else        state.putInt("position", position);
        }
    }

    @Override
    public void restoreState(Bundle savedState) {
        List<TalkBaseModel> list = (List<TalkBaseModel>) savedState.getSerializable("list");
        int position = savedState.getInt("position");
        int page = savedState.getInt("paging");
        boolean hasNext = savedState.getBoolean("hasNext");
        view.onStateResponse(list, position, page, hasNext);
    }
}
