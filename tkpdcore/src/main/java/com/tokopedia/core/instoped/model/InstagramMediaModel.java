package com.tokopedia.core.instoped.model;

import com.tokopedia.core.var.RecyclerViewItem;
import com.tokopedia.core.var.TkpdState;

import org.parceler.Parcel;

/**
 * Created by Tkpd_Eka on 4/6/2016.
 * modified by m.normansyah on 4/18/2016
 */
@Parcel
public class InstagramMediaModel extends RecyclerViewItem {
    public InstagramMediaModel(){
        setType(TkpdState.RecyclerView.VIEW_INSTOPED);
    }
    public String filter;
    public String link;
    public String thumbnail;
    public String standardResolution;
    public String captionText;


}
