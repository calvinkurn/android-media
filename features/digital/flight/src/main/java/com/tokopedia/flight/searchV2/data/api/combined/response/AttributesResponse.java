package com.tokopedia.flight.searchV2.data.api.combined.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Rizky on 19/09/18.
 */
public class AttributesResponse {

    @SerializedName("combos")
    @Expose
    private List<ComboResponse> combos;

    @SerializedName("is_best_pairing")
    @Expose
    private boolean isBestPairing;

    public List<ComboResponse> getCombos() {
        return combos;
    }

    public boolean isBestPairing() {
        return isBestPairing;
    }
}
