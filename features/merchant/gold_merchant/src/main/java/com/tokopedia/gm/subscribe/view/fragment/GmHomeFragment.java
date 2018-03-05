package com.tokopedia.gm.subscribe.view.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.gm.R;
import com.tokopedia.gm.subscribe.di.component.DaggerGmSubscribeComponent;
import com.tokopedia.gm.subscribe.di.module.GmSubscribeModule;
import com.tokopedia.gm.subscribe.view.presenter.GmHomePresenterImpl;


/**
 * @author sebastianuskh on 12/2/16.
 */

public class GmHomeFragment extends BasePresenterFragment<GmHomePresenterImpl> implements GmHomeView {

    public static final String TAG = "GMHomeFragment";
    private GmHomeFragmentCallback listener;
    private TkpdProgressDialog progressDialog;

    /**
     * CONSTRUCTOR AREA
     */
    public static Fragment createFragment() {
        return new GmHomeFragment();
    }

    public void goToProductSelection() {
        listener.goToGMProductFristTime();
    }

    @Override
    protected boolean isRetainInstance() {
        return false;
    }

    @Override
    protected void onFirstTimeLaunched() {

    }

    @Override
    public void onSaveState(Bundle bundle) {

    }

    @Override
    public void onRestoreState(Bundle bundle) {

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected boolean getOptionsMenuEnable() {
        return false;
    }

    @Override
    protected void initialPresenter() {
        presenter = DaggerGmSubscribeComponent
                .builder()
                .appComponent(((HasComponent<AppComponent>)getActivity()).getComponent())
                .gmSubscribeModule(new GmSubscribeModule())
                .build()
                .getHomePresenter();
    }

    @Override
    protected void initialListener(Activity activity) {
        if (activity instanceof GmHomeFragmentCallback) {
            listener = (GmHomeFragmentCallback) activity;
        } else {
            throw new RuntimeException("Please implement GMHomeFragmentListener in the Activity");
        }
    }

    @Override
    protected void setupArguments(Bundle bundle) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        view.findViewById(R.id.button_home_to_select_product).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToProductSelection();
            }
        });
        return view;
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_gm_subscribe_home;
    }

    @Override
    protected void initView(View view) {
        presenter.attachView(this);
    }

    @Override
    public void onDestroyView() {
        presenter.detachView();
        super.onDestroyView();
    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initialVar() {
        progressDialog = new TkpdProgressDialog(getActivity(), TkpdProgressDialog.NORMAL_PROGRESS);
    }

    @Override
    protected void setActionVar() {
        presenter.clearGMProductCache();
    }


    @Override
    public void showProgressDialog() {
        progressDialog.showDialog();
    }

    @Override
    public void dismissProgressDialog() {
        progressDialog.dismiss();
    }
}
