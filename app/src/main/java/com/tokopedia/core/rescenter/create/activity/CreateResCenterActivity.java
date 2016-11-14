package com.tokopedia.core.rescenter.create.activity;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;

import com.tokopedia.core.R;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.rescenter.create.fragment.ChooseProductTroubleFragment;
import com.tokopedia.core.rescenter.create.fragment.ChooseSolutionFragment;
import com.tokopedia.core.rescenter.create.listener.CreateResCenterListener;
import com.tokopedia.core.rescenter.create.model.passdata.ActionParameterPassData;
import com.tokopedia.core.rescenter.create.presenter.CreateResCenterImpl;
import com.tokopedia.core.rescenter.create.presenter.CreateResCenterPresenter;
import com.tokopedia.core.rescenter.create.service.CreateResCenterReceiver;
import com.tokopedia.core.rescenter.create.service.CreateResCenterService;

public class CreateResCenterActivity extends BasePresenterActivity<CreateResCenterPresenter>
        implements CreateResCenterListener, CreateResCenterReceiver.Receiver {

    public static final String KEY_PARAM_ORDER_ID = "ORDER_ID";
    public static final String KEY_PARAM_FLAG_RECEIVED = "FLAG_RECEIVED";
    public static final String KEY_PARAM_TROUBLE_ID = "TROUBLE_ID";
    public static final String KEY_PARAM_SOLUTION_ID = "SOLUTION_ID";
    private static final String TAG_STEP_1 = "step_1";
    private static final String TAG_STEP_2 = "step_2";

    private Bundle bundleData;
    private Uri uriData;
    private CreateResCenterReceiver receiver;

    @Override
    protected String getScreenName() {
        return AppScreen.SCREEN_RESOLUTION_CENTER_ADD;
    }

    public static Intent newInstance(Context context, String orderID) {
        Intent intent = new Intent(context, CreateResCenterActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(KEY_PARAM_ORDER_ID, orderID);
        bundle.putInt(KEY_PARAM_FLAG_RECEIVED, 1);
        intent.putExtras(bundle);
        return intent;
    }

    public static Intent newInstancePackageNotReceived(Context context,
                                                       String orderID,
                                                       int troubleID,
                                                       int solutionID) {
        Intent intent = new Intent(context, CreateResCenterActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(KEY_PARAM_ORDER_ID, orderID);
        bundle.putInt(KEY_PARAM_FLAG_RECEIVED, 0);
        bundle.putInt(KEY_PARAM_TROUBLE_ID, troubleID);
        bundle.putInt(KEY_PARAM_SOLUTION_ID, solutionID);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void setupURIPass(Uri data) {
        this.uriData = data;
    }

    @Override
    protected void setupBundlePass(Bundle extras) {
        this.bundleData = extras;
    }

    @Override
    protected void initialPresenter() {
        presenter = new CreateResCenterImpl(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_create_resolution_center;
    }

    @Override
    protected void initView() {
        presenter.initFragment(this, uriData, bundleData);
    }

    @Override
    public void inflateFragment(Fragment fragment, String TAG) {
        if (getFragmentManager().findFragmentByTag(TAG) == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, fragment, TAG)
                    .commit();
        }
    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initVar() {
        receiver = new CreateResCenterReceiver(new Handler());
        receiver.setReceiver(this);
    }

    @Override
    protected void setActionVar() {

    }

    @Override
    public void addSolutionFragmentStacked(ActionParameterPassData passData) {
        if (getFragmentManager().findFragmentByTag(ChooseSolutionFragment.class.getSimpleName()) == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, ChooseSolutionFragment.newInstance(passData), ChooseSolutionFragment.class.getSimpleName())
                    .addToBackStack(TAG_STEP_2)
                    .commit();
        }
    }

    @Override
    public void addProductDetailTroubleFragmentStacked(ActionParameterPassData passData) {
        if (getFragmentManager().findFragmentByTag(ChooseProductTroubleFragment.class.getSimpleName()) == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, ChooseProductTroubleFragment.newInstance(passData), ChooseProductTroubleFragment.class.getSimpleName())
                    .addToBackStack(TAG_STEP_2)
                    .commit();
        }
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void startCreateResCenterService(ActionParameterPassData passData) {
        CreateResCenterService.startActionCreateResolution(this, passData, receiver);
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        String action = resultData.getString(CreateResCenterService.EXTRA_PARAM_ACTION_TYPE, "");
        if (action.equals(CreateResCenterService.ACTION_CREATE_RESOLUTION)) {
            if (getFragmentManager().findFragmentByTag(TAG_STEP_2) == null) {
                ((ChooseSolutionFragment) getFragmentManager()
                        .findFragmentByTag(ChooseSolutionFragment.class.getSimpleName()))
                        .onGetResultCreateResCenter(resultCode, resultData);
            }
        }
    }
}
