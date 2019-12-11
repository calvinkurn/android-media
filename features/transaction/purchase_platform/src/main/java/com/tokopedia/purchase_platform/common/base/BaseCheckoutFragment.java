package com.tokopedia.purchase_platform.common.base;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment;

/**
 * @author anggaprasetiyo on 18/04/18.
 */
public abstract class BaseCheckoutFragment extends TkpdBaseV4Fragment {

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        setHasOptionsMenu(getOptionsMenuEnable());
    }

    @Override
    protected String getScreenName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initInjector();
    }

    protected abstract void initInjector();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(getFragmentLayout(), container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isResumed()) {
            onResume();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!getUserVisibleHint()) {
            return;
        }
    }

    /**
     * apakah fragment ini support options menu?
     *
     * @return iya atau tidak
     */
    protected abstract boolean getOptionsMenuEnable();

    /**
     * Layout xml untuk si fragment
     *
     * @return layout id
     */
    protected abstract int getFragmentLayout();

    /**
     * initial view atau widget.. misalkan textView = (TextView) findById...
     *
     * @param view root view si fragment
     */
    protected abstract void initView(View view);

}
