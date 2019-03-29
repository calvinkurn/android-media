package com.tokopedia.tkpd.drawer;

import android.app.Activity;

import com.tokopedia.core.drawer2.data.viewmodel.DrawerProfile;
import com.tokopedia.core.drawer2.view.DrawerHelper;
import com.tokopedia.core.drawer2.view.viewmodel.DrawerItem;

import java.util.ArrayList;

/**
 * @author okasurya on 8/7/18.
 */
public class NoOpDrawerHelper extends DrawerHelper {
    public NoOpDrawerHelper(Activity activity) {
        super(activity);
    }

    @Override
    public ArrayList<DrawerItem> createDrawerData() {
        return null;
    }

    @Override
    public void initDrawer(Activity activity) {

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

    }
}
