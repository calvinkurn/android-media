package com.tokopedia.mitra.homepage.domain;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.graphql.data.GraphqlClient;
import com.tokopedia.graphql.data.model.CacheType;
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.mitra.R;
import com.tokopedia.mitra.homepage.domain.model.CategoryGroup;
import com.tokopedia.mitra.homepage.domain.model.CategoryRow;
import com.tokopedia.mitra.homepage.domain.model.DynamicHomeIconWrapper;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.functions.Func1;

public class MitraHomepageCategoriesUseCase extends UseCase<List<CategoryRow>> {
    private static final String PARAM_QUERY = "query";
    private static final long ON_DAY_CACHE_DURATION = TimeUnit.DAYS.toSeconds(1);

    private Context context;
    private GraphqlUseCase graphqlUseCase;

    public MitraHomepageCategoriesUseCase(@ApplicationContext Context context, GraphqlUseCase graphqlUseCase) {
        this.context = context;
        this.graphqlUseCase = graphqlUseCase;
    }

    @Override
    public Observable<List<CategoryRow>> createObservable(RequestParams requestParams) {
        String query = requestParams.getString(PARAM_QUERY, "");
        requestParams.clearValue(PARAM_QUERY);
        GraphqlClient.init(context);

        GraphqlRequest graphqlRequest = new GraphqlRequest(query, DynamicHomeIconWrapper.class, requestParams.getParameters());
        GraphqlCacheStrategy cacheStrategy =
                new GraphqlCacheStrategy
                        .Builder(CacheType.CACHE_FIRST)
                        .setExpiryTime(ON_DAY_CACHE_DURATION)
                        .setSessionIncluded(false)
                        .build();
        graphqlUseCase.clearRequest();
        graphqlUseCase.setCacheStrategy(cacheStrategy);
        graphqlUseCase.addRequest(graphqlRequest);
        return graphqlUseCase.createObservable(null)
                .map(new Func1<GraphqlResponse, List<CategoryRow>>() {
                    @Override
                    public List<CategoryRow> call(GraphqlResponse graphqlResponse) {
                        DynamicHomeIconWrapper wrapper = graphqlResponse.getData(DynamicHomeIconWrapper.class);
                        List<CategoryRow> categoryRows = new ArrayList<>();
                        for (CategoryGroup group : wrapper.getDynamicIcon().getGroups()) {
                            categoryRows.addAll(group.getRows());
                        }
                        return categoryRows;
                    }
                }).onErrorReturn(new Func1<Throwable, List<CategoryRow>>() {
                    @Override
                    public List<CategoryRow> call(Throwable throwable) {
                        List<CategoryRow> rows = new ArrayList<>();
                        rows.add(buildRow("Listrik PLN", "tokopedia://digital/form?category_id=3", "https://ecs7.tokopedia.net/img/category/new/v1/icon_listrik.png"));
                        rows.add(buildRow("Pulsa", "tokopedia://digital/form?category_id=1", "https://ecs7.tokopedia.net/img/category/new/v1/icon_paket.png"));
                        rows.add(buildRow("Air PDAM", "tokopedia://digital/form?category_id=5", "https://ecs7.tokopedia.net/img/category/new/v1/icon_pdam.png"));
                        rows.add(buildRow("Voucher Game", "tokopedia://digital/form?category_id=6", "https://ecs7.tokopedia.net/img/recharge/category/game.png"));
                        rows.add(buildRow("Telkom", "tokopedia://digital/form?category_id=10", "https://ecs7.tokopedia.net/img/category/new/v1/icon_telepon.png"));
                        rows.add(buildRow("e-money", "tokopedia://digital/form?category_id=34", "https://ecs7.tokopedia.net/img/recharge/category/smartcard.png"));
                        rows.add(buildRow("M-Tix XXI", "tokopedia://digital/form?category_id=31", "https://ecs7.tokopedia.net/img/recharge/category/m-tix.png"));
                        return rows;
                    }
                });
    }

    private CategoryRow buildRow(String name, String applinks, String image) {
        CategoryRow row = new CategoryRow();
        row.setName(name);
        row.setApplinks(applinks);
        row.setImageUrl(image);
        return row;
    }

    public RequestParams create() {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(PARAM_QUERY, GraphqlHelper.loadRawString(context.getResources(), R.raw.query_mitra_homepage));
        return requestParams;
    }
}
