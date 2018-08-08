package com.tokopedia.core.talkview.activity;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.TaskStackBuilder;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.core.R;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.router.InboxRouter;
import com.tokopedia.core.router.SellerAppRouter;
import com.tokopedia.core.router.SellerRouter;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.core.shopinfo.fragment.ShopTalkViewFragment;
import com.tokopedia.core.talk.receiver.intentservice.InboxTalkIntentService;
import com.tokopedia.core.talk.receiver.intentservice.InboxTalkResultReceiver;
import com.tokopedia.core.talkview.fragment.InboxTalkViewFragment;
import com.tokopedia.core.talkview.fragment.ProductTalkViewFragment;
import com.tokopedia.core.talkview.fragment.TalkViewFragment;
import com.tokopedia.core.talkview.intentservice.TalkDetailIntentService;
import com.tokopedia.core.talkview.intentservice.TalkDetailResultReceiver;
import com.tokopedia.core.talkview.intentservice.TalkViewIntentService;
import com.tokopedia.core.talkview.intentservice.TalkViewResultReceiver;
import com.tokopedia.core.util.GlobalConfig;

public class TalkViewActivity extends TActivity
        implements TalkViewResultReceiver.Receiver,
        InboxTalkResultReceiver.Receiver,
        TalkDetailResultReceiver.Receiver {

    public static final String INBOX_TALK = "inbox_talk";
    public static final String PRODUCT_TALK = "product_talk";
    public static final String SHOP_TALK = "shop_talk";

    TalkViewResultReceiver mReceiver;
    InboxTalkResultReceiver mReceiverTalk;
    TalkDetailResultReceiver mReceiverTalkDetail;

    @DeepLink(Constants.Applinks.TALK_DETAIL)
    public static TaskStackBuilder getCallingTaskStack(Context context, Bundle extras) {
        extras.putString("from", INBOX_TALK);
        Intent homeIntent = null;
        if (GlobalConfig.isSellerApp()) {
            homeIntent = SellerAppRouter.getSellerHomeActivity(context);
        } else {
            homeIntent = HomeRouter.getHomeActivity(context);
        }
        Intent detailsIntent = new Intent(context, TalkViewActivity.class).putExtras(extras);
        Intent parentIntent = InboxRouter.getInboxTalkActivityIntent(context);
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
        taskStackBuilder.addNextIntent(homeIntent);
        taskStackBuilder.addNextIntent(parentIntent);
        taskStackBuilder.addNextIntent(detailsIntent);
        return taskStackBuilder;
    }

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_PRODUCT_TALK_VIEW;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.activity_talk_view2);

        mReceiver = new TalkViewResultReceiver(new Handler());
        mReceiverTalk = new InboxTalkResultReceiver(new Handler());
        mReceiverTalkDetail = new TalkDetailResultReceiver(new Handler());
        mReceiver.setReceiver(this);
        mReceiverTalk.setReceiver(this);
        mReceiverTalkDetail.setReceiver(this);
        Bundle bundle = getIntent().getExtras();
        if (savedInstanceState == null) {
            Fragment fragment;
            if (bundle.getString("from").equals(INBOX_TALK)) {
                fragment = InboxTalkViewFragment.createInstance(bundle);
            } else if (bundle.getString("from").equals(SHOP_TALK)) {
                fragment = ShopTalkViewFragment.createInstance(bundle);
            } else {
                fragment = ProductTalkViewFragment.createInstance(bundle);
            }
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.container, fragment, TalkViewActivity.class.getSimpleName());
            fragmentTransaction.commit();
        }
        UnifyTracking.eventDiscussionProductDetail(bundle.getString("from", "N/A"));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {

        Fragment fragment = getFragmentManager().findFragmentById(R.id.container);

        if (fragment != null) {
            switch (resultCode) {
                case TalkViewIntentService.STATUS_SUCCESS:
                    ((TalkViewFragment) fragment).successReply(resultData.getString(TalkViewIntentService.EXTRA_RESULT));

                    UnifyTracking.eventDiscussionProductSend(getIntent().getExtras() != null ?
                            getIntent().getExtras().getString("from", "N/A") : "N/A");
                    break;
                case TalkViewIntentService.STATUS_ERROR:
                    ((TalkViewFragment) fragment).errorReply(resultData.getString(TalkViewIntentService.EXTRA_RESULT));
                    break;
                case InboxTalkIntentService.STATUS_SUCCESS_DELETE:
                    ((TalkViewFragment) fragment).successDelete(resultData.getString(InboxTalkIntentService.EXTRA_RESULT));
                    break;
                case InboxTalkIntentService.STATUS_ERROR_DELETE:
                    ((TalkViewFragment) fragment).errorDelete(resultData.getString(InboxTalkIntentService.EXTRA_RESULT));
                    break;
                case InboxTalkIntentService.STATUS_SUCCESS_FOLLOW:
                    ((TalkViewFragment) fragment).successFollow(resultData.getString(InboxTalkIntentService.EXTRA_RESULT));
                    break;
                case InboxTalkIntentService.STATUS_ERROR_FOLLOW:
                    ((TalkViewFragment) fragment).errorFollow(resultData.getString(InboxTalkIntentService.EXTRA_RESULT));
                    break;
                case InboxTalkIntentService.STATUS_SUCCESS_REPORT:
                    ((TalkViewFragment) fragment).successReport(resultData.getString(InboxTalkIntentService.EXTRA_RESULT));
                    break;
                case InboxTalkIntentService.STATUS_ERROR_REPORT:
                    ((TalkViewFragment) fragment).errorReport(resultData.getString(InboxTalkIntentService.EXTRA_RESULT));
                    break;
                case TalkDetailIntentService.STATUS_SUCCESS_DELETE:
                    ((TalkViewFragment) fragment).successDeleteComment
                            (resultData.getString(TalkDetailIntentService.EXTRA_RESULT),
                                    resultData.getInt(TalkDetailIntentService.POSITION));
                    break;
                case TalkDetailIntentService.STATUS_ERROR_DELETE:
                    ((TalkViewFragment) fragment).errorDeleteComment
                            (resultData.getString(TalkDetailIntentService.EXTRA_RESULT));
                    break;

                case TalkDetailIntentService.STATUS_SUCCESS_REPORT:
                    ((TalkViewFragment) fragment).successReportComment
                            (resultData.getString(TalkDetailIntentService.EXTRA_RESULT),
                                    resultData.getInt(TalkDetailIntentService.POSITION));
                    break;
                case TalkDetailIntentService.STATUS_ERROR_REPORT:
                    ((TalkViewFragment) fragment).errorReportComment
                            (resultData.getString(TalkDetailIntentService.EXTRA_RESULT));
                    break;


                default:
                    break;
            }
        }
    }

    public void replyComment(Bundle bundle) {
        TalkViewIntentService.replyComment(this, bundle, mReceiver, TalkViewIntentService.PARAM_REPLY);
    }

    public void deleteTalk(Bundle bundle) {
        InboxTalkIntentService.startAction(this, bundle, mReceiverTalk, InboxTalkIntentService.ACTION_DELETE);
    }

    public void followTalk(Bundle bundle) {
        InboxTalkIntentService.startAction(this, bundle, mReceiverTalk, InboxTalkIntentService.ACTION_FOLLOW);
    }

    public void reportTalk(Bundle bundle) {
        InboxTalkIntentService.startAction(this, bundle, mReceiverTalk, InboxTalkIntentService.ACTION_REPORT);
    }

    public void deleteCommentTalk(Bundle bundle) {
        TalkDetailIntentService.startAction(this, bundle, mReceiverTalkDetail, TalkDetailIntentService.ACTION_DELETE);
    }

    public void reportCommentTalk(Bundle bundle) {
        TalkDetailIntentService.startAction(this, bundle, mReceiverTalkDetail, TalkDetailIntentService.ACTION_REPORT);
    }

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }
}
