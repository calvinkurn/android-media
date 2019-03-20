package com.tokopedia.core.analytics.container;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.tagmanager.ContainerHolder;
import com.google.android.gms.tagmanager.DataLayer;
import com.google.android.gms.tagmanager.TagManager;
import com.tkpd.library.utils.legacy.CommonUtils;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.abstraction.common.utils.LocalCacheHandler;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.PurchaseTracking;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.analytics.model.Hotlist;
import com.tokopedia.core.analytics.nishikino.model.Authenticated;
import com.tokopedia.core.analytics.nishikino.model.Campaign;
import com.tokopedia.core.analytics.nishikino.model.Checkout;
import com.tokopedia.core.analytics.nishikino.model.GTMCart;
import com.tokopedia.core.analytics.nishikino.model.ProductDetail;
import com.tokopedia.core.analytics.nishikino.model.Purchase;
import com.tokopedia.core.analytics.nishikino.singleton.ContainerHolderSingleton;
import com.tokopedia.core.deprecated.SessionHandler;
import com.tokopedia.core.gcm.utils.RouterUtils;
import com.tokopedia.core.var.TkpdCache;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.tokopedia.core.analytics.TrackingUtils.getAfUniqueId;

@Deprecated
public class GTMContainer implements IGTMContainer {

    private static final int EXPIRE_CONTAINER_TIME = 7200;
    private static final long EXPIRE_CONTAINER_TIME_DEFAULT = 7200000;
    private static final int EXPIRE_CONTAINER_TIME_DEBUG = 900;

    private static final String TAG = GTMContainer.class.getSimpleName();

    private Context context;
    private SessionHandler sessionHandler;

    public static GTMContainer newInstance(Context context) {
        return new GTMContainer(context);
    }


    public GTMContainer(Context context) {
        this.context = context;
        this.sessionHandler = RouterUtils.getRouterFromContext(context).legacySessionHandler();
    }

    /**
     * {@link GTMAnalytics#getTagManager()}
     *
     * @return
     */
    public TagManager getTagManager() {
        return TagManager.getInstance(context);
    }


    public void loadContainer() {
        try {
            Bundle bundle = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA).metaData;
            TagManager tagManager = getTagManager();
            PendingResult<ContainerHolder> pResult = tagManager.loadContainerPreferFresh(bundle.getString(AppEventTracking.GTM.GTM_ID),
                    bundle.getInt(AppEventTracking.GTM.GTM_RESOURCE));

            pResult.setResultCallback(ContainerHolderSingleton::setContainerHolder, 2, TimeUnit.SECONDS);
        } catch (Exception e) {
            TrackingUtils.eventError(context, context.getClass().toString(), e.toString());
        }
    }

    private DataLayer getDataLayer() {
        return getTagManager().getDataLayer();
    }


    private void clearEventTracking() {
        GTMDataLayer.pushGeneral(
                context,
                DataLayer.mapOf("event", null,
                        "eventCategory", null,
                        "eventAction", null,
                        "eventLabel", null
                )
        );
    }


}