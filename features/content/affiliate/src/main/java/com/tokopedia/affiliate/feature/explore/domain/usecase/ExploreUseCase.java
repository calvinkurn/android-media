package com.tokopedia.affiliate.feature.explore.domain.usecase;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.affiliate.R;
import com.tokopedia.affiliate.feature.explore.data.pojo.ExploreData;
import com.tokopedia.affiliate.feature.explore.view.viewmodel.ExploreParams;
import com.tokopedia.affiliate.feature.explore.view.viewmodel.FilterViewModel;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.usecase.RequestParams;

import java.util.List;

import javax.inject.Inject;

/**
 * @author by yfsx on 08/10/18.
 */
public class ExploreUseCase extends GraphqlUseCase {
    private final Context context;
    private final static String PARAM_CURSOR = "nextCursor";
    private final static String PARAM_KEYWORD = "keyword";
    private final static String PARAM_FILTER= "filter";
    private final static String PARAM_SORT = "sort";
    private final static String PARAM_FILTER_KEY = "key";
    private final static String PARAM_FILTER_KEY_DATA = "d_id";
    private final static String PARAM_FILTER_VALUE = "value";

    @Inject
    public ExploreUseCase(@ApplicationContext Context context) {
        this.context = context;
    }

    public GraphqlRequest getRequest(ExploreParams exploreParams) {
        String query = GraphqlHelper.loadRawString(
                context.getResources(),
                R.raw.query_explore
        );
        return new GraphqlRequest(
                query,
                ExploreData.class,
                getParam(exploreParams).getParameters()
        );
    }

    public GraphqlRequest getRequestLoadMore(ExploreParams exploreParams) {
        String query = GraphqlHelper.loadRawString(
                context.getResources(),
                R.raw.query_explore_load_more
        );
        return new GraphqlRequest(
                query,
                ExploreData.class,
                getParam(exploreParams).getParameters()
        );
    }

    public static RequestParams getParam(ExploreParams exploreParams) {
        RequestParams params = RequestParams.create();
        if (!TextUtils.isEmpty(exploreParams.getKeyword())) {
            params.putString(PARAM_KEYWORD, exploreParams.getKeyword());
        }
        if (!TextUtils.isEmpty(exploreParams.getCursor())) {
            params.putString(PARAM_CURSOR, exploreParams.getCursor());
        }
        if (exploreParams.getFilters().size() != 0) {
            params.putObject(PARAM_FILTER, constructFilterParams(exploreParams.getFilters()));
        }
        return params;
    }

    private static JsonArray constructFilterParams(List<FilterViewModel> filterList) {
        JsonArray dataArray = new JsonArray();
        for (FilterViewModel filter : filterList) {
            JsonObject object = new JsonObject();
            object.addProperty(PARAM_FILTER_KEY, PARAM_FILTER_KEY_DATA);
            object.addProperty(PARAM_FILTER_VALUE, appendIdValue(filter.getIds()));
            dataArray.add(object);
        }
        return dataArray;
    }

    private static String appendIdValue(List<Integer> idList) {
        StringBuilder value = new StringBuilder();
        for(int i = 0; i <idList.size(); i++) {
            value.append(idList.get(i));
            if (i != idList.size() - 1) {
                value.append(",");
            }
        }
        return value.toString();
    }
}
