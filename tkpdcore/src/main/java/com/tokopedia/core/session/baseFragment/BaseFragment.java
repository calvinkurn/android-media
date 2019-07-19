package com.tokopedia.core.session.baseFragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.core.presenter.BaseView;


/**
 * Created by m.normansyah on 1/27/16.
 */
public abstract  class BaseFragment<T extends Base> extends Fragment implements BaseView{
    protected T presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initPresenter();
        presenter.fetchArguments(getArguments());
        presenter.fetchFromPreference(getActivity());
        presenter.fetchRotationData(savedInstanceState);
        presenter.initDataInstance(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View parentView = inflater.inflate(getLayoutId(), container, false);
        return parentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.subscribe();
        presenter.initData(getActivity());
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.unSubscribe();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
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
