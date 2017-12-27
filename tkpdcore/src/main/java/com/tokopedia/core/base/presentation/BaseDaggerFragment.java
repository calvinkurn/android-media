package com.tokopedia.core.base.presentation;

/**
 * @author kulomady on 1/9/17.
 */
@Deprecated
public abstract class BaseDaggerFragment extends com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment {

    protected <C> C getComponent(Class<C> componentType) {
        return componentType.cast(((HasComponent<C>) getActivity()).getComponent());
    }

}
