package com.tokopedia.posapp.etalase;

import com.google.gson.annotations.SerializedName;

/**
 * Created by okasurya on 9/18/17.
 */

public class EtalaseItemResponse {
    @SerializedName("menu_id")
    private String menuId;

    @SerializedName("menu_name")
    private String menuName;

    @SerializedName("menu_alias")
    private String menuAlias;

    @SerializedName("use_ace")
    private int useAce;

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getMenuAlias() {
        return menuAlias;
    }

    public void setMenuAlias(String menuAlias) {
        this.menuAlias = menuAlias;
    }

    public int getUseAce() {
        return useAce;
    }

    public void setUseAce(int useAce) {
        this.useAce = useAce;
    }
}
