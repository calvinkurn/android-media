package com.tokopedia.shop.common.graphql.model.shopnote;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.shop.common.graphql.domain.mapper.HasGraphQLResult;
import com.tokopedia.shop.common.graphql.model.GraphQLResult;
import com.tokopedia.shop.common.graphql.model.shoplocation.ShopLocation;

import java.util.ArrayList;

/**
 * Created by hendry on 08/08/18.
 */

public class ShopNoteResponse implements HasGraphQLResult<ArrayList<ShopNotes>>{
    @SerializedName("shopNotes")
    @Expose
    GraphQLResult<ArrayList<ShopNotes>> shopNotesResult;

    @Override
    public GraphQLResult<ArrayList<ShopNotes>> getResult() {
        return shopNotesResult;
    }
}
