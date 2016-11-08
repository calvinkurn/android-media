package com.tokopedia.core.myproduct.model.temp;

import com.google.gson.annotations.SerializedName;

/**
 * Created by m.normansyah on 12/9/15.
 */
public class MyPojo {
    @SerializedName("parent")
    private String parent;

    @SerializedName("childs")
    private Childs[] childs;

    public String getParent ()
    {
        return parent;
    }

    public void setParent (String parent)
    {
        this.parent = parent;
    }

    public Childs[] getChilds ()
    {
        return childs;
    }

    public void setChilds (Childs[] childs)
    {
        this.childs = childs;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [parent = "+parent+", childs = "+childs+"]";
    }
}
