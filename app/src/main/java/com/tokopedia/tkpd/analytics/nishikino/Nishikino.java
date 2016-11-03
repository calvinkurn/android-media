package com.tokopedia.tkpd.analytics.nishikino;

import android.content.Context;

import com.tokopedia.tkpd.analytics.container.GTMContainer;
import com.tokopedia.tkpd.analytics.container.IGTMContainer;
import com.tokopedia.tkpd.analytics.nishikino.singleton.ContainerHolderSingleton;

/**
 * Created by ricoharisin on 10/19/15.
 *
 * Nishikino is a class to simplify GTM implementation, and because docs from google is kinda suck so that's why
 */
public class Nishikino {

    private Context context;
    private IGTMContainer gtmContainer;

    private Nishikino (Context context) {
        this.context = context;
        this.gtmContainer = GTMContainer.newInstance(context);
    }

    public static Nishikino init(Context context) {
        return new Nishikino(context);
    }

    public void loadContainer() {
        if (!ContainerHolderSingleton.isContainerHolderAvailable()) gtmContainer.loadContainer();
    }

    public IGTMContainer startAnalytics() {
        return gtmContainer;
    }
}