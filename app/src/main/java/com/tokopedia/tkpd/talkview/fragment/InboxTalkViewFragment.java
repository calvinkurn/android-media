package com.tokopedia.tkpd.talkview.fragment;

import android.os.Bundle;
import android.os.Parcelable;

import com.google.gson.GsonBuilder;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.talk.inboxtalk.model.InboxTalk;
import com.tokopedia.tkpd.talkview.adapter.InboxTalkViewAdapter;
import com.tokopedia.tkpd.talkview.adapter.TalkViewAdapter;
import com.tokopedia.tkpd.talkview.inbox.model.TalkDetail;
import com.tokopedia.tkpd.talkview.inbox.model.TalkDetailModel;
import com.tokopedia.tkpd.util.SessionHandler;

import org.json.JSONObject;

/**
 * Created by stevenfredian on 5/17/16.
 */
public class InboxTalkViewFragment extends TalkViewFragment{

    public static TalkViewFragment createInstance(Bundle bundle) {
        InboxTalkViewFragment fragment = new InboxTalkViewFragment();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    protected TalkViewAdapter getAdapter() {
        return InboxTalkViewAdapter.createAdapter(this, items);
    }

    @Override
    protected void getResultType(JSONObject result) {
        TalkDetailModel model = new GsonBuilder().create()
                .fromJson(result.toString(), TalkDetailModel.class);
        items.addAll(0, model.getTalkDetail());
    }

    @Override
    protected int getSize(JSONObject result) {
        TalkDetailModel model = new GsonBuilder().create()
                .fromJson(result.toString(), TalkDetailModel.class);
        return model.getTalkDetail().size();
    }

    @Override
    protected void getFromBundle(Parcelable parcelable) {
        InboxTalk bundle = (InboxTalk) parcelable;
        talkID = bundle.getTalkId();
        message = bundle.getTalkMessageSpanned();
        productName = String.valueOf(bundle.getTalkProductName());
        prodImgUri = String.valueOf(bundle.getTalkProductImage());
        userName = bundle.getTalkUserName();
        reputationHeader = bundle.getTalkUserReputation().getPositivePercentage();
        positiveHeader = bundle.getTalkUserReputation().getPositive();
        negativeHeader = bundle.getTalkUserReputation().getNegative();
        neutralHeader = bundle.getTalkUserReputation().getNeutral();
        noReputationHeader = bundle.getTalkUserReputation().getNoReputation();
        createTime = bundle.getTalkCreateTime();
        userImgUri = bundle.getTalkUserImage();
        userIDTalk = bundle.getTalkUserId();
        productID = String.valueOf(bundle.getTalkProductId());
        shopID = bundle.getTalkShopId();
        headUserLabel = bundle.getTalkUserLabel();
        isOwner = bundle.getTalkOwn();
        isFollow = bundle.getTalkFollowStatus();
        totalComment = Integer.parseInt(bundle.getTalkTotalComment());
        readStatus = bundle.getTalkReadStatus();
    }

    @Override
    protected void addDummy() {
        TalkDetail detail = new TalkDetail();
        detail.setCommentMessage(content);
        detail.setCommentCreateTime(getString(R.string.title_sending));
        detail.setCommentUserName(SessionHandler.getLoginName(context));
        detail.setCommentShopName(SessionHandler.getShopDomain(context));
        items.add(detail);
    }
}
