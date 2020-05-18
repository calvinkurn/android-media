package com.tokopedia.search.result.domain.usecase.searchproduct;

import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.search.result.domain.model.SearchProductModel;
import com.tokopedia.search.utils.UrlParamUtils;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.functions.Func1;

import static com.tokopedia.discovery.common.constants.SearchConstant.GQL.KEY_PARAMS;

class SearchProductLoadMoreGqlUseCase extends UseCase<SearchProductModel> {

    private GraphqlRequest graphqlRequest;
    private GraphqlUseCase graphqlUseCase;
    private Func1<GraphqlResponse, SearchProductModel> searchProductModelMapper;

    SearchProductLoadMoreGqlUseCase(GraphqlUseCase graphqlUseCase,
                                    Func1<GraphqlResponse, SearchProductModel> searchProductModelMapper) {
        graphqlRequest = new GraphqlRequest(
                GQL_QUERY,
                SearchProductModel.class
        );
        this.graphqlUseCase = graphqlUseCase;
        this.searchProductModelMapper = searchProductModelMapper;
    }

    @Override
    public Observable<SearchProductModel> createObservable(RequestParams requestParams) {
        Map<String, Object> variables = createParametersForQuery(requestParams.getParameters());

        graphqlRequest.setVariables(variables);

        graphqlUseCase.clearRequest();
        graphqlUseCase.addRequest(graphqlRequest);

        return graphqlUseCase
                .createObservable(RequestParams.EMPTY)
                .map(searchProductModelMapper);
    }

    private Map<String, Object> createParametersForQuery(Map<String, Object> parameters) {
        Map<String, Object> variables = new HashMap<>();
        variables.put(KEY_PARAMS, UrlParamUtils.generateUrlParamString(parameters));

        return variables;
    }

    private static final String GQL_QUERY = "query SearchProduct(\n" +
            "    $params: String\n" +
            ") {\n" +
            "    searchProduct(params: $params) {\n" +
            "        query\n" +
            "        source\n" +
            "        shareUrl\n" +
            "        isFilter\n" +
            "        count\n" +
            "        additional_params\n" +
            "        isQuerySafe\n" +
            "        suggestion {\n" +
            "            currentKeyword\n" +
            "            suggestion\n" +
            "            suggestionCount\n" +
            "            instead\n" +
            "            insteadCount\n" +
            "            text\n" +
            "            query\n" +
            "        }\n" +
            "        products {\n" +
            "            id\n" +
            "            warehouse_id_default\n" +
            "            name\n" +
            "            childs\n" +
            "            url\n" +
            "            image_url\n" +
            "            image_url_700\n" +
            "            price\n" +
            "            price_range\n" +
            "            wishlist\n" +
            "            whole_sale_price {\n" +
            "                quantity_min\n" +
            "                quantity_max\n" +
            "                price\n" +
            "            }\n" +
            "            courier_count\n" +
            "            condition\n" +
            "            category_id\n" +
            "            category_name\n" +
            "            category_breadcrumb\n" +
            "            department_id\n" +
            "            department_name\n" +
            "          \tfree_ongkir {\n" +
            "              is_active\n" +
            "              img_url\n" +
            "            }\n" +
            "            labels {\n" +
            "                title\n" +
            "                color\n" +
            "            }\n" +
            "          \tlabel_groups {\n" +
            "                position\n" +
            "                type\n" +
            "                title\n" +
            "            }\n" +
            "            badges {\n" +
            "                title\n" +
            "                image_url\n" +
            "                show\n" +
            "            }\n" +
            "            is_featured\n" +
            "            rating\n" +
            "            rating_average\n" +
            "            count_review\n" +
            "            original_price\n" +
            "            discount_expired_time\n" +
            "            discount_start_time\n" +
            "            discount_percentage\n" +
            "            sku\n" +
            "            stock\n" +
            "            ga_key\n" +
            "            is_preorder\n" +
            "            shop {\n" +
            "                id\n" +
            "                name\n" +
            "                url\n" +
            "                is_gold_shop\n" +
            "                is_power_badge\n" +
            "                location\n" +
            "                city\n" +
            "                reputation\n" +
            "                clover\n" +
            "                is_official\n" +
            "            }\n" +
            "        }\n" +
            "    }\n" +
            "\n" +
            "    productAds: displayAdsV3(displayParams: $params) {\n" +
            "        status {\n" +
            "            error_code\n" +
            "            message\n" +
            "        }\n" +
            "        header {\n" +
            "            process_time\n" +
            "            total_data\n" +
            "        }\n" +
            "        data {\n" +
            "            id\n" +
            "            ad_ref_key\n" +
            "            redirect\n" +
            "            sticker_id\n" +
            "            sticker_image\n" +
            "            product_click_url\n" +
            "            product_wishlist_url\n" +
            "            shop_click_url\n" +
            "            product {\n" +
            "                id\n" +
            "                name\n" +
            "                wishlist\n" +
            "                image {\n" +
            "                    m_url\n" +
            "                    s_url\n" +
            "                    xs_url\n" +
            "                    m_ecs\n" +
            "                    s_ecs\n" +
            "                    xs_ecs\n" +
            "                }\n" +
            "                uri\n" +
            "                relative_uri\n" +
            "                price_format\n" +
            "                wholesale_price {\n" +
            "                    price_format\n" +
            "                    quantity_max_format\n" +
            "                    quantity_min_format\n" +
            "                }\n" +
            "                count_talk_format\n" +
            "                count_review_format\n" +
            "                category {\n" +
            "                    id\n" +
            "                }\n" +
            "                product_preorder\n" +
            "                product_wholesale\n" +
            "                free_return\n" +
            "                product_cashback\n" +
            "                product_new_label\n" +
            "                product_cashback_rate\n" +
            "                product_rating\n" +
            "                product_rating_format\n" +
            "              \tfree_ongkir {\n" +
            "                  is_active\n" +
            "                  img_url\n" +
            "                }\n" +
            "                campaign {\n" +
            "                    original_price\n" +
            "                    discount_percentage\n" +
            "                }\n" +
            "                label_group {\n" +
            "                    position\n" +
            "                    type\n" +
            "                    title\n" +
            "                }\n" +
            "            }\n" +
            "            shop {\n" +
            "                id\n" +
            "                name\n" +
            "                domain\n" +
            "                location\n" +
            "                city\n" +
            "                gold_shop\n" +
            "                gold_shop_badge\n" +
            "                lucky_shop\n" +
            "                uri\n" +
            "                owner_id\n" +
            "                is_owner\n" +
            "                shop_is_official\n" +
            "                badges {\n" +
            "                    title\n" +
            "                    image_url\n" +
            "                    show\n" +
            "                }\n" +
            "            }\n" +
            "            applinks\n" +
            "        }\n" +
            "        template {\n" +
            "            is_ad\n" +
            "        }\n" +
            "    }\n" +
            "}";
}
