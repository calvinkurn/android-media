package com.tokopedia.core.myproduct.model.temp;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by m.normansyah on 12/9/15.
 */
@Deprecated
public class MergeChilds {
    @SerializedName("d_id")
    String d_id;
    @SerializedName("name")
    String name;
    @SerializedName("level3List")
    List<Level3> level3List;

    public String getD_id() {
        return d_id;
    }

    public void setD_id(String d_id) {
        this.d_id = d_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Level3> getLevel3List() {
        return level3List;
    }

    public void setLevel3List(List<Level3> level3List) {
        this.level3List = level3List;
    }

    @Deprecated
    public static class Level3{
        @SerializedName("d_id")
        String d_id;
        @SerializedName("name")
        String name;

        public String getD_id() {
            return d_id;
        }

        public void setD_id(String d_id) {
            this.d_id = d_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
