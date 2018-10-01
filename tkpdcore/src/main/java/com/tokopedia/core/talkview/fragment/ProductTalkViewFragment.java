package com.tokopedia.core.talkview.fragment;

import android.os.Bundle;
import android.os.Parcelable;

import com.google.gson.GsonBuilder;
import com.tokopedia.core.R;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.talk.talkproduct.model.Talk;
import com.tokopedia.core.talkview.adapter.ProductTalkViewAdapter;
import com.tokopedia.core.talkview.adapter.TalkViewAdapter;
import com.tokopedia.core.talkview.product.model.CommentTalk;
import com.tokopedia.core.talkview.product.model.CommentTalkModel;
import com.tokopedia.core.util.SessionHandler;

import org.json.JSONObject;

/**
 * Created by stevenfredian on 5/17/16.
 */
public class ProductTalkViewFragment extends TalkViewFragment{

    public static TalkViewFragment createInstance(Bundle bundle) {
        ProductTalkViewFragment fragment = new ProductTalkViewFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected TalkViewAdapter getAdapter() {
        return ProductTalkViewAdapter.createAdapter(this, items, (TkpdCoreRouter) getActivity().getApplication());
    }

    @Override
    protected void getResultType(JSONObject result) {
        CommentTalkModel model = new GsonBuilder().create()
                .fromJson(result.toString(), CommentTalkModel.class);
        items.addAll(0, model.getCommentTalk());
    }

    @Override
    protected int getSize(JSONObject result) {
        CommentTalkModel model = new GsonBuilder().create()
                .fromJson(result.toString(), CommentTalkModel.class);
        return model.getCommentTalk().size();
    }


    @Override
    protected void getFromBundle(Parcelable parcelable) {
        Talk bundle = (Talk) parcelable;
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
        CommentTalk detail = new CommentTalk();
        detail.setCommentMessage(content);
        detail.setCommentCreateTime(getString(R.string.title_sending));
        detail.setCommentUserName(SessionHandler.getLoginName(context));
        detail.setCommentShopName(SessionHandler.getShopDomain(context));
        items.add(detail);
    }

}
