package com.tokopedia.core.drawer2.view.viewmodel;

/**
 * Created by nisie on 1/20/17.
 */

public class DrawerSeparator extends DrawerItem {

    public DrawerSeparator(){
        super("",0,0,false);
    }

    public DrawerSeparator(boolean isExpanded) {
        super("", 0, 0, isExpanded);
    }
}
