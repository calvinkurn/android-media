
package com.tokopedia.core.network.entity.categoriesHades;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.core.discovery.model.HotListBannerModel;
import com.tokopedia.core.discovery.model.ObjContainer;

public class CategoriesHadesModel {

    @SerializedName("data")
    @Expose
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static final class CategoriesHadesContainer implements ObjContainer<CategoriesHadesModel> {
        CategoriesHadesModel categoriesHadesModel;

        public CategoriesHadesContainer(CategoriesHadesModel categoriesHadesModel) {
            this.categoriesHadesModel = categoriesHadesModel;
        }

        @Override
        public CategoriesHadesModel body() {
            return categoriesHadesModel;
        }
    }

}
