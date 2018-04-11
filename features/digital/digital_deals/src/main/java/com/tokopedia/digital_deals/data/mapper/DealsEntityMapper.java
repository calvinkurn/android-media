package com.tokopedia.digital_deals.data.mapper;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.tokopedia.digital_deals.data.entity.response.DealsResponseEntity;
import com.tokopedia.digital_deals.data.entity.response.HomeResponseEntity;
import com.tokopedia.digital_deals.domain.model.DealsCategoryDomain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DealsEntityMapper {

    public List<DealsCategoryDomain> transform(DealsResponseEntity dealsResponseEntity) {
        HomeResponseEntity homeResponseEntity = dealsResponseEntity.getData().getHome();

        JsonObject layout = homeResponseEntity.getLayout();
        List<DealsCategoryDomain> dealsCategoryDomains = new ArrayList<>();
        DealsCategoryDomain dealsCategoryDomain;
        for (Map.Entry<String, JsonElement> entry : layout.entrySet()){
            JsonObject object=entry.getValue().getAsJsonObject();
            dealsCategoryDomain = new Gson().fromJson(object, DealsCategoryDomain.class);
            dealsCategoryDomains.add(dealsCategoryDomain);
        }
        return dealsCategoryDomains;
    }
}
