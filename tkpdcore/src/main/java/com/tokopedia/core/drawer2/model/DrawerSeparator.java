package com.tokopedia.core.drawer2.model;

/**
 * Created by nisie on 1/20/17.
 */

public class DrawerSeparator extends DrawerItem {

    public DrawerSeparator(){
        super("",0,0,false);
    }

    public DrawerSeparator(String label, int iconId, int position, boolean isExpanded) {
        super(label, iconId, position, isExpanded);
    }
}
