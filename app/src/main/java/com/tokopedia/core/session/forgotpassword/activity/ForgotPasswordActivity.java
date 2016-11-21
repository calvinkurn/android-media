package com.tokopedia.core.session.forgotpassword.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.MenuItem;

import com.tokopedia.core.R;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.session.forgotpassword.fragment.ForgotPasswordFragment;
import com.tokopedia.core.session.forgotpassword.presenter.ForgotPasswordFragmentPresenterImpl;

/**
 * Created by Alifa on 10/17/2016.
 */

public class ForgotPasswordActivity extends BasePresenterActivity {

    private static final String TAG = "FORGOT_PASSWORD_FRAGMENT";

    public static Intent createInstance(Context context) {
        return new Intent(context, ForgotPasswordActivity.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {

    }

    @Override
    protected void initialPresenter() {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_forgotpassword;
    }

    @Override
    protected void initView() {
        ForgotPasswordFragment fragment = ForgotPasswordFragment.createInstance();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        fragmentTransaction.add(R.id.container, fragment, TAG);
        fragmentTransaction.commit();
    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initVar() {

    }

    @Override
    protected void setActionVar() {

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ForgotPasswordFragmentPresenterImpl.REQUEST_FORGOT_PASSWORD_CODE && resultCode == Activity.RESULT_OK){
            ((ForgotPasswordFragment)getFragmentManager().findFragmentByTag(TAG)).refresh();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
