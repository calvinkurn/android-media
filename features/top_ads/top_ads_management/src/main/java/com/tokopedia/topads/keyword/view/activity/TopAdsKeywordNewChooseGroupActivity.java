package com.tokopedia.topads.keyword.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.seller.base.view.activity.BaseStepperActivity;
import com.tokopedia.seller.base.view.model.StepperModel;
import com.tokopedia.topads.R;
import com.tokopedia.topads.dashboard.constant.TopAdsExtraConstant;
import com.tokopedia.topads.keyword.view.fragment.TopAdsKeywordAddFragment;
import com.tokopedia.topads.keyword.view.fragment.TopAdsKeywordNewChooseGroupFragment;
import com.tokopedia.topads.keyword.view.model.TopAdsKeywordStepperModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nathan on 5/17/17.
 */

public class TopAdsKeywordNewChooseGroupActivity extends BaseStepperActivity
        implements HasComponent<AppComponent>,
        TopAdsKeywordAddFragment.OnSuccessSaveKeywordListener {

    public static final String RESULT_WORDS = "rslt_wrds";
    public static final String TAG = TopAdsKeywordNewChooseGroupActivity.class.getSimpleName();
    private static final String EXTRA_IS_POSITIVE = "is_pos";
    private static final String EXTRA_CHOOSEN_GROUP = "EXTRA_CHOOSEN_GROUP";
    List<Fragment> fragmentList;

    public static void start(Activity activity, int requestCode,
                             boolean isPositive) {
        Intent intent = createIntent(activity, isPositive, null);
        activity.startActivityForResult(intent, requestCode);
    }

    public static void start(Fragment fragment, Context context, int requestCode,
                             boolean isPositive) {
        Intent intent = createIntent(context, isPositive, null);
        fragment.startActivityForResult(intent, requestCode);
    }

    public static void start(Activity activity, int requestCode,
                             boolean isPositive, @Nullable String groupId) {
        Intent intent = createIntent(activity, isPositive, groupId);
        activity.startActivityForResult(intent, requestCode);
    }

    public static void start(Fragment fragment, Context context, int requestCode,
                             boolean isPositive, @Nullable String groupId) {
        Intent intent = createIntent(context, isPositive, groupId);
        fragment.startActivityForResult(intent, requestCode);
    }

    private static Intent createIntent(Context context, boolean isPositive, String groupId) {
        Intent intent = new Intent(context, TopAdsKeywordNewChooseGroupActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean(EXTRA_IS_POSITIVE, isPositive);
        bundle.putString(EXTRA_CHOOSEN_GROUP, groupId);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    public StepperModel createNewStepperModel() {
        boolean isPositive = false;
        String groupId = null;
        if (getIntent() != null && getIntent().getExtras() != null) {
            isPositive = getIntent().getBooleanExtra(EXTRA_IS_POSITIVE, true);
            groupId = getIntent().getStringExtra(EXTRA_CHOOSEN_GROUP);
        }
        TopAdsKeywordStepperModel stepperModel = new TopAdsKeywordStepperModel();
        stepperModel.setGroupId(groupId);
        stepperModel.setPositive(isPositive);
        return stepperModel;
    }

    @NonNull
    @Override
    protected List<Fragment> getListFragment() {
        if (fragmentList == null) {
            fragmentList = new ArrayList<>();
            fragmentList.add(TopAdsKeywordNewChooseGroupFragment.newInstance());
            fragmentList.add(TopAdsKeywordAddFragment.newInstance());
            return fragmentList;
        } else {
            return fragmentList;
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

    @Override
    public void finishPage() {
        setResultAdSaved();
        super.finishPage();
    }

    @Override
    public void onBackPressed() {
        if(!exitConfirmation())
            super.onBackPressed();
    }

    private boolean exitConfirmation(){
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.parent_view);
        if(fragment != null && fragment instanceof TopAdsKeywordAddFragment){
            if(((TopAdsKeywordAddFragment)fragment).isButtonSaveEnabled()){
                AlertDialog dialog = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle)
                        .setMessage(getString(R.string.topads_keyword_add_cancel_dialog))
                        .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                TopAdsKeywordNewChooseGroupActivity.super.onBackPressed();
                            }
                        }).setNegativeButton(getString(R.string.No), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {

                            }
                        }).create();
                dialog.show();
                return true;
            }
        }
        return false;
    }


    private void setResultAdSaved() {
        Intent intent = new Intent();
        setResult(Activity.RESULT_OK, intent);
    }

    @Override
    public void onSuccessSave(ArrayList<String> keyWordsList) {
        Intent intent = new Intent();
        intent.putStringArrayListExtra(RESULT_WORDS, keyWordsList);
        intent.putExtra(TopAdsExtraConstant.EXTRA_AD_CHANGED, true);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}