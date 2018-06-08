package com.tokopedia.core.referral.model;

/**
 * Created by ashwanityagi on 06/06/18.
 */

public class ShareApps {
    private int id;
    private String name;
    private String packageNmae;
    private  int icon;
    private int index;

    public ShareApps(String packageNmae, int icon) {
        this.packageNmae = packageNmae;
        this.icon = icon;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPackageNmae() {
        return packageNmae;
    }

    public void setPackageNmae(String packageNmae) {
        this.packageNmae = packageNmae;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
