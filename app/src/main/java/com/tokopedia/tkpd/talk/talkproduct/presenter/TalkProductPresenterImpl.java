package com.tokopedia.tkpd.talk.talkproduct.presenter;

import android.content.Context;
import android.os.Bundle;

import com.tokopedia.tkpd.talk.inboxtalk.intentservice.InboxTalkIntentService;
import com.tokopedia.tkpd.talk.talkproduct.activity.TalkProductActivity;
import com.tokopedia.tkpd.talk.talkproduct.fragment.TalkProductFragment;
import com.tokopedia.tkpd.talk.talkproduct.interactor.TalkProductRetrofitInteractor;
import com.tokopedia.tkpd.talk.talkproduct.interactor.TalkProductRetrofitInteractorImpl;
import com.tokopedia.tkpd.talk.talkproduct.listener.TalkProductView;
import com.tokopedia.tkpd.talk.talkproduct.model.Talk;
import com.tokopedia.tkpd.talk.talkproduct.model.TalkProductModel;
import com.tokopedia.tkpd.talkview.model.TalkPass;
import com.tokopedia.tkpd.util.NewPagingHandler;
import com.tokopedia.tkpd.var.RecyclerViewItem;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by stevenfredian on 4/5/16.
 */
public class TalkProductPresenterImpl implements TalkProductPresenter{

    TalkProductView view;
    TalkProductRetrofitInteractor facade;
    TalkProductActivity listener;
    int positionAction;
    
    String nextPage;

    public TalkProductPresenterImpl(TalkProductFragment talkProductFragment) {
        view = talkProductFragment;
        nextPage = "0";
        facade = TalkProductRetrofitInteractorImpl.createInstance(this);
        listener = (TalkProductActivity)talkProductFragment.getActivity();
    }

    @Override
    public void getTalkProduct(final Context context, Map<String, String> param) {
        param.put("page",nextPage);
        if(!nextPage.equals("0") && !nextPage.equals("") && nextPage!=null){
            request(context,param);
        }else {
            view.cancelRequest();
        }
    }

    @Override
    public void refreshTalkProduct(Context context, Map<String, String> param) {
        param.put("page","1");
        request(context,param);
    }

    private void request(Context context, Map<String, String> param){
        final int page = Integer.parseInt(param.get("page"));
        facade.getTalkProduct(context,param, new TalkProductRetrofitInteractor.GetTalkProductListener() {
            @Override
            public void onSuccess(TalkProductModel result, NewPagingHandler.PagingBean paging) {
                nextPage = paging.getNextPage();
                if (nextPage != null&&!nextPage.equals("0") && !nextPage.equals("")) {
                    view.setLoadingFooter();
                } else {
                    nextPage = "0";
                    view.removeLoadingFooter();
                }

                view.onSuccessConnection(result, page);
            }

            @Override
            public void onError(String s) {
                view.onTimeoutConnection(s,page);
            }

            @Override
            public void onTimeout() {
                view.onTimeoutConnection(page);
            }

            @Override
            public void onThrowable(Throwable e) {
                view.onTimeoutConnection(page);
            }
        });
    }

    @Override
    public void unSubscribe() {
        facade.unSubscribe();
    }

    @Override
    public void saveState(Bundle state, List<RecyclerViewItem> items, int position) {
        if(items!=null){
            state.putSerializable("list", (Serializable) items);
            state.putInt("paging", Integer.parseInt(nextPage));
            if (nextPage != null&&!nextPage.equals("0") && !nextPage.equals(""))
                state.putInt("position", position - 1);
            else
                state.putInt("position", position);
        }
    }

    @Override
    public void restoreState(Bundle savedState) {
        List list = (List) savedState.getSerializable("list");
        int position = savedState.getInt("position");
        int page = savedState.getInt("paging");
        nextPage = String.valueOf(page);
        if (nextPage != null&&!nextPage.equals("0") && !nextPage.equals("")) {
            view.setLoadingFooter();
        } else {
            nextPage = "0";
            view.removeLoadingFooter();
        }
        view.onStateResponse(list,position,page);
    }


    @Override
    public void followTalk(Talk talk, int position) {
        setPositionAction(position);
        TalkPass pass = new TalkPass();
        pass.setTalkId(talk.getTalkId());
        pass.setShopId(talk.getTalkShopId());
        pass.setProductId(talk.getTalkProductId());
        Bundle bundle = new Bundle();
        bundle.putParcelable(InboxTalkIntentService.PARAM_FOLLOW, pass);
        listener.followTalk(bundle);
    }

    @Override
    public void deleteTalk(Talk talk, int position) {
        setPositionAction(position);
        TalkPass param = new TalkPass();
        param.setTalkId(talk.getTalkId());
        param.setShopId(talk.getTalkShopId());
        Bundle bundle = new Bundle();
        bundle.putParcelable(InboxTalkIntentService.PARAM_DELETE,param);
        listener.deleteTalk(bundle);
    }


    @Override
    public void reportTalk(Talk talk, int position) {
        setPositionAction(position);
        TalkPass param = new TalkPass();
        param.setTalkId(talk.getTalkId());
        param.setShopId(talk.getTalkShopId());
        param.setProductId(talk.getTalkProductId());
        param.setTextMessage(talk.getTalkMessage());
        Bundle bundle = new Bundle();
        bundle.putParcelable(InboxTalkIntentService.PARAM_REPORT,param);
        listener.reportTalk(bundle);
    }

    public void setPositionAction(int position){
        positionAction = position;
    }

    @Override
    public int getPositionAction() {
        return positionAction;
    }

}
