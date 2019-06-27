package com.tokopedia.gm.subscribe.view.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.gm.common.constant.GMParamConstant;
import com.tokopedia.gm.resource.GMConstant;
import com.tokopedia.gm.subscribe.R;
import com.tokopedia.gm.subscribe.di.component.DaggerGmSubscribeComponent;
import com.tokopedia.gm.subscribe.di.module.GmSubscribeModule;
import com.tokopedia.gm.subscribe.tracking.GMTracking;
import com.tokopedia.gm.subscribe.view.presenter.GmHomePresenterImpl;
import com.tokopedia.gm.subscribe.view.widget.home.GmMainFeatureView;
import com.tokopedia.gm.subscribe.view.widget.home.GmSubFeatureView;


/**
 * @author sebastianuskh on 12/2/16.
 */

public class GmHomeFragment extends BasePresenterFragment<GmHomePresenterImpl> implements GmHomeView {

    public static final String TAG = "GMHomeFragment";
    private GmHomeFragmentCallback listener;
    private TkpdProgressDialog progressDialog;
    private boolean isFromFeatured = false;

    /**
     * CONSTRUCTOR AREA
     */
    public static Fragment createFragment() {
        return new GmHomeFragment();
    }

    public static Fragment createFragment(boolean isFromFeatured){
        Bundle bundle = new Bundle();
        bundle.putBoolean(GMParamConstant.PARAM_KEY_FROM_FEATURE, isFromFeatured);
        Fragment fragment = createFragment();
        fragment.setArguments(bundle);
        return fragment;
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
        isFromFeatured = getArguments().getBoolean(GMParamConstant.PARAM_KEY_FROM_FEATURE, false);
        progressDialog = new TkpdProgressDialog(getActivity(), TkpdProgressDialog.NORMAL_PROGRESS);
    }

    @Override
    protected void setActionVar() {
        presenter.clearGMProductCache();
        GmMainFeatureView goldBadge = getView().findViewById(R.id.feature_gold_badge);
        goldBadge.setImageViewFeature(GMConstant.getGMSubscribeBadgeDrawable(getActivity()));
        int badgeTitleRes = GMConstant.getGMBadgeTitleResource(getActivity());
        goldBadge.setTitleFeature(badgeTitleRes);
        goldBadge.setDescFeature(getString(R.string.gmsubscribe_feature_gold_badge_desc, getString(badgeTitleRes)));
        String gm = getString(GMConstant.getGMTitleResource(getActivity()));
        TextView titleSubFeature = getView().findViewById(R.id.title_gm_sub_features);
        titleSubFeature.setText(getString(R.string.gmsubscribe_subfeature_title, gm));
        GmSubFeatureView filter = getView().findViewById(R.id.sub_feature_video);
        filter.setDescSubFeature(getString(R.string.gmsubscribe_feature_filter_gold_merchant, gm));

        if (GMConstant.isPowerMerchantEnabled(getActivity())){
            getView().findViewById(R.id.feature_wawasan).setVisibility(View.VISIBLE);
            getView().findViewById(R.id.feature_topads).setVisibility(View.GONE);
            getView().findViewById(R.id.title_gm_sub_features).setVisibility(View.GONE);
            getView().findViewById(R.id.gmsubscribe_subfeature_container_top).setVisibility(View.GONE);
            getView().findViewById(R.id.gmsubscribe_subfeature_container_bottom).setVisibility(View.GONE);
        } else {
            getView().findViewById(R.id.feature_wawasan).setVisibility(View.GONE);
            getView().findViewById(R.id.feature_topads).setVisibility(View.VISIBLE);
            getView().findViewById(R.id.title_gm_sub_features).setVisibility(View.VISIBLE);
            getView().findViewById(R.id.gmsubscribe_subfeature_container_top).setVisibility(View.VISIBLE);
            getView().findViewById(R.id.gmsubscribe_subfeature_container_bottom).setVisibility(View.VISIBLE);
        }

        getView().findViewById(R.id.button_home_to_select_product).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity() != null && getActivity().getApplication() instanceof AbstractionRouter){
                    new GMTracking().sendClickGMSubscribingEvent(isFromFeatured);
                }
                goToProductSelection();
            }
        });
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
