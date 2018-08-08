package com.tokopedia.core.talk.talkproduct.activity;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.core.R;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.talk.receiver.intentservice.InboxTalkIntentService;
import com.tokopedia.core.talk.receiver.intentservice.InboxTalkResultReceiver;
import com.tokopedia.core.talk.talkproduct.fragment.TalkProductFragment;

public class TalkProductActivity extends TActivity implements InboxTalkResultReceiver.Receiver {

    public final static String RESULT_TALK_HAS_ADDED = "RESULT_TALK_HAS_ADDED";
    public String productImage;
    public String productID;
    public String shopID;
    public String isShopOwner;
    public String productName;
    public boolean stateHasAdded = false;

    InboxTalkResultReceiver mReceiver;

    @DeepLink(Constants.Applinks.PRODUCT_TALK)
    public static Intent getCallingTaskStack(Context context, Bundle extras) {
        Uri.Builder uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon();
        return new Intent(context, TalkProductActivity.class)
                .setData(uri.build())
                .putExtras(extras);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.activity_talk_product2);

        mReceiver = new InboxTalkResultReceiver(new Handler());
        mReceiver.setReceiver(this);

        productID = getIntent().getExtras().getString("product_id");
        shopID = getIntent().getExtras().getString("shop_id");
        isShopOwner = getIntent().getExtras().getString("is_owner");
        productName = getIntent().getExtras().getString("prod_name");
        productImage = getIntent().getExtras().getString("product_image");

        if (savedInstanceState == null) {
            Fragment fragment = TalkProductFragment.createInstance(getIntent().getExtras());
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.container, fragment, TalkProductActivity.class.getSimpleName());
            fragmentTransaction.commit();
        }
    }

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_PRODUCT_TALK;
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        TalkProductFragment fragment = (TalkProductFragment) getFragmentManager()
                .findFragmentByTag(TalkProductActivity.class.getSimpleName());
        if (fragment != null) {
            switch (resultCode) {
                case InboxTalkIntentService.STATUS_SUCCESS_FOLLOW:
                case InboxTalkIntentService.STATUS_SUCCESS_DELETE:
                case InboxTalkIntentService.STATUS_SUCCESS_REPORT:
                    onReceiveResultSuccess(fragment, resultData, resultCode);
                    break;
                case InboxTalkIntentService.STATUS_ERROR_FOLLOW:
                case InboxTalkIntentService.STATUS_ERROR_DELETE:
                case InboxTalkIntentService.STATUS_ERROR_REPORT:
                    onReceiveResultError(fragment, resultData, resultCode);
                    break;
            }
        }
    }

    private void onReceiveResultError(TalkProductFragment fragment, Bundle resultData, int resultCode) {
        fragment.onErrorAction(resultData, resultCode);
    }

    private void onReceiveResultSuccess(TalkProductFragment fragment, Bundle resultData, int resultCode) {
        fragment.onSuccessAction(resultData, resultCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void followTalk(Bundle param) {
        InboxTalkIntentService.startAction(this, param, mReceiver,
                InboxTalkIntentService.ACTION_FOLLOW);
    }

    public void deleteTalk(Bundle param) {
        InboxTalkIntentService.startAction(this, param, mReceiver,
                InboxTalkIntentService.ACTION_DELETE);
    }

    public void reportTalk(Bundle param) {
        InboxTalkIntentService.startAction(this, param, mReceiver,
                InboxTalkIntentService.ACTION_REPORT);
    }

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }
}
