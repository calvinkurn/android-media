package com.tokopedia.digital.widget.data.entity.response;

import com.google.gson.annotations.SerializedName;
import com.tokopedia.digital.common.data.entity.response.Category;
import com.tokopedia.digital.common.data.entity.response.RelationData;

/**
 * @author rizkyfadillah on 10/2/2017.
 */

public class Relationships {

    @SerializedName("category")
    private Category category;

    @SerializedName("operator")
    private Operator operator;

    public Category getCategory() {
        return category;
    }

    public Operator getOperator() {
        return operator;
    }

    public class Operator {

        @SerializedName("data")
        private RelationData data;

        public RelationData getData() {
            return data;
        }
    }

}
