package com.tokopedia.core.analytics.nishikino.singleton;

import com.google.android.gms.tagmanager.ContainerHolder;

/**
 * Created by ricoharisin on 7/7/15.
 */
public class ContainerHolderSingleton {

    private static ContainerHolder Container;

    public static void setContainerHolder(ContainerHolder container) {
        Container = container;
    }

    public static ContainerHolder getContainerHolder() {
        return Container;
    }

    public static Boolean isContainerHolderAvailable() {
        return Container != null && Container.getContainer() != null;
    }
}
