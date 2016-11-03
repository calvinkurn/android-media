package com.tokopedia.tkpd.dynamicfilter.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.dynamicfilter.presenter.DynamicFilterBase;
import com.tokopedia.tkpd.dynamicfilter.presenter.DynamicFilterBaseDetailView;
import com.tokopedia.tkpd.dynamicfilter.presenter.DynamicFilterBaseImpl;
import com.tokopedia.tkpd.dynamicfilter.presenter.DynamicFilterView;
import com.tokopedia.tkpd.session.base.BaseFragment;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by noiz354 on 7/11/16.
 */
public class DynamicFilterFirstTimeFragment extends BaseFragment<DynamicFilterBase> implements DynamicFilterBaseDetailView {
    public static final int FRAGMENT_ID = 123_123_123;
    public static final String FRAGMENT_TAG = "DynamicFilterFirstTimeFragment";

    @Bind(R.id.dynamic_filter_first_time_detail)
    ImageView dynamicFilterFirstTimeDetail;

    @Bind(R.id.dynamic_filter_first_time_text)
    TextView dynamicFilterFirstTimeText;

    @Bind(R.id.finish_first_time_btn)
    Button finishFirstTimeBtn;

    @Override
    protected void initPresenter() {
        presenter = new DynamicFilterBaseImpl(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dynamic_filter_first_time_layout;
    }

    @Override
    public int getFragmentId() {
        return FRAGMENT_ID;
    }

    @Override
    public void ariseRetry(int type, Object... data) {

    }

    @Override
    public void setData(int type, Bundle data) {

    }

    @Override
    public void onNetworkError(int type, Object... data) {

    }

    @Override
    public void onMessageError(int type, Object... data) {

    }

    @Override
    public void setImageUsingGlide() {
        ImageHandler.loadImageWithId(dynamicFilterFirstTimeDetail, R.drawable.filter_nostate);
        dynamicFilterFirstTimeText.setText(getString(R.string.filter_description));
    }

    @Override
    public void hitHadesDemo() {
        presenter.hitHadesDemo();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.finish_first_time_btn)
    public void onClick() {
        if(getActivity() != null && getActivity() instanceof DynamicFilterView){
            ((DynamicFilterView) getActivity()).finishThis();
        }
    }
}
