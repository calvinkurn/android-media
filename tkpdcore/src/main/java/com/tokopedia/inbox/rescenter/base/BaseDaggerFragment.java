package com.tokopedia.inbox.rescenter.base;

import android.os.Bundle;

import com.tokopedia.core.app.TkpdFragment;
import com.tokopedia.core.base.di.component.HasComponent;

/**
 * Created by hangnadi on 4/11/17.
 */

public abstract class BaseDaggerFragment extends TkpdFragment {

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
