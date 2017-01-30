package com.tokopedia.core.base.presentation;

import android.os.Bundle;

import com.tokopedia.core.app.TkpdBaseV4Fragment;
import com.tokopedia.core.base.di.component.HasComponent;


/**
 * @author kulomady on 1/9/17.
 */
public abstract class BaseFragment extends TkpdBaseV4Fragment {

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
