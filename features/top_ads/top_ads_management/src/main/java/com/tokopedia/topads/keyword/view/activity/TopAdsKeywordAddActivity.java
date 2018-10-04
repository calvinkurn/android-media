package com.tokopedia.topads.keyword.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.seller.base.view.activity.BaseSimpleActivity;
import com.tokopedia.topads.R;
import com.tokopedia.topads.keyword.constant.KeywordTypeDef;
import com.tokopedia.topads.keyword.helper.KeywordTypeMapper;
import com.tokopedia.topads.keyword.view.fragment.TopAdsKeywordAddFragment;

import java.util.ArrayList;

/**
 * Created by nathan on 5/15/17.
 */
@Deprecated
public class TopAdsKeywordAddActivity extends BaseSimpleActivity
        implements HasComponent<AppComponent>,
        TopAdsKeywordAddFragment.OnSuccessSaveKeywordListener {

    public static final String EXTRA_GROUP_ID = "grp_id";
    public static final String EXTRA_GROUP_NAME = "grp_nm";
    public static final String EXTRA_KEYWORD_TYPE = "key_typ";
    public static final String EXTRA_SERVER_COUNT = "server_count";
    public static final String EXTRA_MAX_WORDS = "max_words";
    public static final String EXTRA_LOCAL_WORDS = "lcl_wrds";

    public static final String RESULT_WORDS = "rslt_wrds";

    public static void start(Activity activity, int requestCode,
                             String groupId,
                             String groupName,
                             @KeywordTypeDef int keywordType,
                             int serverCount, int maxWords,
                             @NonNull ArrayList<String> localWords) {
        Intent intent = createIntent(activity, groupId, groupName, keywordType, serverCount, maxWords, localWords);
        activity.startActivityForResult(intent, requestCode);
    }

    public static void start(Fragment fragment, Context context, int requestCode,
                             String groupId,
                             String groupName,
                             @KeywordTypeDef int keywordType,
                             int serverCount, int maxWords,
                             @NonNull ArrayList<String> localWords) {
        Intent intent = createIntent(context,  groupId, groupName, keywordType, serverCount, maxWords, localWords);
        fragment.startActivityForResult(intent, requestCode);
    }

    private static Intent createIntent(Context context,
                                       String groupId,
                                       String groupName,
                                       @KeywordTypeDef int keywordType,
                                       int serverCount,
                                       int maxWords,
                                       @NonNull ArrayList<String> localWords) {
        Intent intent = new Intent(context, TopAdsKeywordAddActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_GROUP_ID, groupId);
        bundle.putString(EXTRA_GROUP_NAME, groupName);
        bundle.putInt(EXTRA_KEYWORD_TYPE, keywordType);
        bundle.putInt(EXTRA_SERVER_COUNT, serverCount);
        bundle.putInt(EXTRA_MAX_WORDS, maxWords);
        bundle.putStringArrayList(EXTRA_LOCAL_WORDS, localWords);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected Fragment getNewFragment() {
        String groupId = getIntent().getStringExtra(EXTRA_GROUP_ID);
        String groupName = getIntent().getStringExtra(EXTRA_GROUP_NAME);
        int keywordType = getIntent().getIntExtra(EXTRA_KEYWORD_TYPE, KeywordTypeDef.KEYWORD_TYPE_EXACT);
        int serverCount = getIntent().getIntExtra(EXTRA_SERVER_COUNT, 0);
        int maxWords = getIntent().getIntExtra(EXTRA_MAX_WORDS, getResources().getInteger(R.integer.top_ads_keyword_max_in_group));
        ArrayList<String> localWords = getIntent().getStringArrayListExtra(EXTRA_LOCAL_WORDS);
        return TopAdsKeywordAddFragment.newInstance(groupId,keywordType, serverCount, maxWords, localWords);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        @KeywordTypeDef int keywordType = getIntent().getIntExtra(EXTRA_KEYWORD_TYPE, KeywordTypeDef.KEYWORD_TYPE_EXACT);
        getSupportActionBar().setTitle(KeywordTypeMapper.mapToKeywordName(this, keywordType));
    }

    @Override
    public String getScreenName() {
        return null;
    }

    @Override
    public void onSuccessSave(ArrayList<String> keyWordsList) {
        Intent intent = new Intent();
        intent.putStringArrayListExtra(RESULT_WORDS, keyWordsList);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        // check if user already make a changes by adding or deleting
        // if so, when backpressed, show dialog if user really want to delete the change or save the changes
        TopAdsKeywordAddFragment fragment = (TopAdsKeywordAddFragment) getSupportFragmentManager().findFragmentByTag(getTagFragment());
        if (fragment!= null && fragment.isButtonSaveEnabled()) {
            AlertDialog dialog = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle)
                    .setMessage(getString(R.string.topads_keyword_add_cancel_dialog))
                    .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            TopAdsKeywordAddActivity.super.onBackPressed();
                        }
                    }).setNegativeButton(getString(R.string.No), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {

                        }
                    }).create();
            dialog.show();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public AppComponent getComponent() {
        return getApplicationComponent();
    }

    @Override
    protected boolean isToolbarWhite() {
        return true;
    }
}