package com.tokopedia.shop.common.graphql.data.shopnote.gql;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.shop.common.graphql.data.shopnote.ShopNoteModel;
import com.tokopedia.shop.common.graphql.domain.mapper.HasGraphQLResult;
import com.tokopedia.shop.common.graphql.data.GraphQLResult;

import java.util.ArrayList;

/**
 * Created by hendry on 08/08/18.
 */

public class ShopNoteQuery implements HasGraphQLResult<ArrayList<ShopNoteModel>>{
    @SerializedName("shopNotes")
    @Expose
    GraphQLResult<ArrayList<ShopNoteModel>> shopNotesResult;

    @Override
    public GraphQLResult<ArrayList<ShopNoteModel>> getResult() {
        return shopNotesResult;
    }
}
