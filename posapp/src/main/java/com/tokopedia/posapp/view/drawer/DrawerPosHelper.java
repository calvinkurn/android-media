package com.tokopedia.posapp.view.drawer;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.ui.view.LinearLayoutManager;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerProfile;
import com.tokopedia.core.drawer2.view.DrawerAdapter;
import com.tokopedia.core.drawer2.view.DrawerHelper;
import com.tokopedia.core.drawer2.view.databinder.DrawerHeaderDataBinder;
import com.tokopedia.core.drawer2.view.databinder.DrawerItemDataBinder;
import com.tokopedia.core.drawer2.view.viewmodel.DrawerItem;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.posapp.R;

import java.util.ArrayList;

/**
 * Created by Herdi_WORK on 07.09.17.
 */

public class DrawerPosHelper extends DrawerHelper
        implements DrawerItemDataBinder.DrawerItemListener,
        DrawerPosHeaderDataBinder.DrawerHeaderListener {

    private TextView shopName;
    private TextView shopLabel;
    private ImageView shopIcon;
    private View shopLayout;
    private View footerShadow;
    private SessionHandler sessionHandler;

    public DrawerPosHelper(Activity activity,
                              SessionHandler sessionHandler,
                              LocalCacheHandler drawerCache) {
        super(activity);
        this.sessionHandler = sessionHandler;
        this.drawerCache = drawerCache;
        shopName = activity.findViewById(R.id.label);
        shopLabel = activity.findViewById(R.id.sublabel);
        shopIcon = activity.findViewById(R.id.icon);
        shopLayout = activity.findViewById(R.id.drawer_shop);
        footerShadow = activity.findViewById(R.id.drawer_footer_shadow);
        shopLayout.setVisibility(View.GONE);
        footerShadow.setVisibility(View.GONE);
    }

    public static DrawerPosHelper createInstance(Activity activity,
                                                   SessionHandler sessionHandler,
                                                   LocalCacheHandler drawerCache) {
        return new DrawerPosHelper(activity, sessionHandler, drawerCache);
    }

    @Override
    public ArrayList<DrawerItem> createDrawerData() {
        ArrayList<DrawerItem> data = new ArrayList<>();

        data.add(new DrawerItem(context.getString(R.string.drawer_title_home),
                R.drawable.icon_home,
                TkpdState.DrawerPosition.SELLER_INDEX_HOME,
                true));
        data.add(new DrawerItem(context.getString(R.string.drawer_title_pos_riwayat_tx),
                R.drawable.statistik_icon,
                TkpdState.DrawerPosition.SELLER_GM_STAT,
                true));
        data.add(new DrawerItem(context.getString(R.string.drawer_title_pos_choose_outlet),
                R.drawable.ic_top_ads,
                TkpdState.DrawerPosition.SELLER_TOP_ADS,
                true));
        data.add(new DrawerItem(context.getString(R.string.drawer_title_logout),
                R.drawable.ic_menu_logout,
                TkpdState.DrawerPosition.LOGOUT,
                true));
        return data;
    }

    @Override
    public void initDrawer(Activity activity) {
        this.adapter = DrawerAdapter.createAdapter(activity, this, drawerCache);
        this.adapter.setData(createDrawerData());
        this.adapter.setHeader(new DrawerPosHeaderDataBinder(adapter, activity, this, drawerCache));
        recyclerView.setLayoutManager(new LinearLayoutManager(activity,
                LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
        setExpand();
        closeDrawer();
    }

    @Override
    public boolean isOpened() {
        return false;
    }

    @Override
    public void setFooterData(DrawerProfile profile) {

    }

    @Override
    public void setExpand() {
        adapter.notifyDataSetChanged();
    }
}
