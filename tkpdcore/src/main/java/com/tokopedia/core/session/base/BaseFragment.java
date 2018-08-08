package com.tokopedia.core.session.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.tokopedia.core.app.IAnalyticsFragment;
import com.tokopedia.core.presenter.BaseView;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by m.normansyah on 1/27/16.
 */
public abstract class BaseFragment<T extends Base> extends Fragment implements BaseView, IAnalyticsFragment{
    protected T presenter;
    protected View parentView;
    protected Unbinder unbinder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initPresenter();
        presenter.fetchArguments(getArguments());
        presenter.fetchFromPreference(getActivity());
        presenter.fetchRotationData(savedInstanceState);
        presenter.initDataInstance(getActivity());
    }

    @Override
    public String getScreenName() {
        return this.getClass().getSimpleName();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View parentView = inflater.inflate(getLayoutId(), container, false);
        inflateOtherView(parentView);
        unbinder = ButterKnife.bind(this, parentView);
        customView();
        return this.parentView = onCreateView(parentView, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        presenter.initData(getActivity());
    }

    public View onCreateView(View parentView, Bundle savedInstanceState){ return parentView; }

    protected void customView(){

    };

    public void inflateOtherView(View parentView){
        Log.i(TAG, "inflateOtherView called !!");
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.subscribe();
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.unSubscribe();
        presenter.moveToOtherView();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        Glide.get(getContext()).clearMemory();
        Runtime.getRuntime().gc();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        presenter.saveDataBeforeRotation(outState);
    }

    /**
     * called this when {@link Fragment#onCreate(Bundle)}
     */
    protected abstract void initPresenter();

    /**
     * supply this for build using butterKnife
     * @return layout id
     */
    protected abstract int getLayoutId();
}
