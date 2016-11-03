package com.tokopedia.tkpd.var;

import org.parceler.Parcel;

import java.io.Serializable;

/**
 * Created by Nisie on 19/06/15.
 */
@Parcel
public class RecyclerViewItem implements Serializable {
    int type = 0;
    public void setType(int type){
        this.type = type;
    }
    public int getType(){
        return this.type;
    }
}