package com.tokopedia.abstraction.base.view.fragment;

import android.os.Bundle;

import com.tokopedia.abstraction.common.di.component.HasComponent;


/**
 * @author kulomady on 1/9/17.
 */
public abstract class BaseDaggerFragment extends TkpdBaseV4Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initInjector();
    }

    @SuppressWarnings("unchecked")
    protected <C> C getComponent(Class<C> componentType) {
        return componentType.cast(((HasComponent<C>) getActivity()).getComponent());
    }

    protected abstract void initInjector();
}
