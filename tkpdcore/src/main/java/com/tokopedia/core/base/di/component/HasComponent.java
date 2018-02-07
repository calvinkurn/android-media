package com.tokopedia.core.base.di.component;

/**
 * @author kulomady on 1/9/17.
 */
@Deprecated
public interface HasComponent<C> extends com.tokopedia.abstraction.common.di.component.HasComponent<C> {
    C getComponent();
}
