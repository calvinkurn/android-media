package com.tokopedia.search.result.domain.usecase.searchproduct;

import com.tokopedia.discovery.common.constants.SearchApiConst;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.search.result.domain.model.SearchProductModel;
import com.tokopedia.search.utils.UrlParamUtils;
import com.tokopedia.topads.sdk.domain.TopAdsParams;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.functions.Func1;

import static com.tokopedia.discovery.common.constants.SearchConstant.GQL.KEY_HEADLINE_PARAMS;
import static com.tokopedia.discovery.common.constants.SearchConstant.GQL.KEY_PARAMS;
import static com.tokopedia.discovery.common.constants.SearchConstant.GQL.KEY_QUERY;
import static com.tokopedia.discovery.common.constants.SearchConstant.SearchProduct.HEADLINE;
import static com.tokopedia.discovery.common.constants.SearchConstant.SearchProduct.HEADLINE_ITEM_VALUE;
import static com.tokopedia.discovery.common.constants.SearchConstant.SearchProduct.HEADLINE_TEMPLATE_VALUE;

class SearchProductFirstPageGqlUseCase extends UseCase<SearchProductModel> {

    public static final int HEADLINE_PRODUCT_COUNT = 3;
    private GraphqlRequest graphqlRequest;
    private GraphqlUseCase graphqlUseCase;
    private Func1<GraphqlResponse, SearchProductModel> searchProductModelMapper;

    SearchProductFirstPageGqlUseCase(GraphqlUseCase graphqlUseCase,
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

        variables.put(KEY_QUERY, getQueryFromParameters(parameters));
        variables.put(KEY_PARAMS, UrlParamUtils.generateUrlParamString(parameters));
        variables.put(KEY_HEADLINE_PARAMS, createHeadlineParams(parameters));

        return variables;
    }

    private Object getQueryFromParameters(Map<String, Object> parameters) {
        Object query = parameters.get(SearchApiConst.Q);

        return query == null ? "" : query;
    }

    private String createHeadlineParams(Map<String, Object> parameters) {
        Map<String, Object> headlineParams = new HashMap<>(parameters);
        headlineParams.put(TopAdsParams.KEY_EP, HEADLINE);
        headlineParams.put(TopAdsParams.KEY_TEMPLATE_ID, HEADLINE_TEMPLATE_VALUE);
        headlineParams.put(TopAdsParams.KEY_ITEM, HEADLINE_ITEM_VALUE);
        headlineParams.put(TopAdsParams.KEY_HEADLINE_PRODUCT_COUNT, HEADLINE_PRODUCT_COUNT);

        return UrlParamUtils.generateUrlParamString(headlineParams);
    }

    private static final String GQL_QUERY = "query SearchProduct(\n" +
            "    $params: String!,\n" +
            "    $query: String!,\n" +
            "    $headline_params: String\n" +
            ") {\n" +
            "    searchProduct(params: $params) {\n" +
            "        query\n" +
            "        source\n" +
            "        isFilter\n" +
            "        response_code\n" +
            "        keyword_process\n" +
            "        count\n" +
            "        count_text\n" +
            "        additional_params\n" +
            "        isQuerySafe\n" +
            "        autocomplete_applink\n" +
            "        errorMessage\n" +
            "        lite_url\n" +
            "        default_view\n" +
            "        redirection {\n" +
            "            redirect_applink\n" +
            "        }\n" +
            "        ticker {\n" +
            "            text\n" +
            "            query\n" +
            "            type_id\n" +
            "        }\n" +
            "        suggestion {\n" +
            "            currentKeyword\n" +
            "            suggestion\n" +
            "            suggestionCount\n" +
            "            instead\n" +
            "            insteadCount\n" +
            "            text\n" +
            "            query\n" +
            "        }\n" +
            "        related {\n" +
            "            related_keyword\n" +
            "            other_related {\n" +
            "                keyword\n" +
            "                url\n" +
            "                applink\n" +
            "                product {\n" +
            "                    id\n" +
            "                    name\n" +
            "                    price\n" +
            "                    image_url\n" +
            "                    rating\n" +
            "                    count_review\n" +
            "                    url\n" +
            "                    applink\n" +
            "                    price_str\n" +
            "                }\n" +
            "            }\n" +
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
            "            booster_list\n" +
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
            "              \tis_power_badge\n" +
            "                location\n" +
            "                city\n" +
            "                reputation\n" +
            "                clover\n" +
            "                is_official\n" +
            "            }\n" +
            "        }\n" +
            "    }\n" +
            "\n" +
            "    quick_filter(query: $query, extraParams: $params) {\n" +
            "        filter {\n" +
            "            options {\n" +
            "                name\n" +
            "                key\n" +
            "                icon\n" +
            "                value\n" +
            "                is_new\n" +
            "                input_type\n" +
            "                total_data\n" +
            "                val_max\n" +
            "                val_min\n" +
            "                hex_color\n" +
            "                child {\n" +
            "                    key\n" +
            "                    value\n" +
            "                    name\n" +
            "                    icon\n" +
            "                    input_type\n" +
            "                    total_data\n" +
            "                    child {\n" +
            "                        key\n" +
            "                        value\n" +
            "                        name\n" +
            "                        icon\n" +
            "                        input_type\n" +
            "                        total_data\n" +
            "                    }\n" +
            "                }\n" +
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
            "        data{\n" +
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
            "            shop{\n" +
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
            "\n" +
            "    headlineAds: displayAdsV3(displayParams: $headline_params) {\n" +
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
            "            ad_click_url\n" +
            "            headline{\n" +
            "            template_id\n" +
            "            name\n" +
            "            image {\n" +
            "                full_url\n" +
            "                full_ecs\n" +
            "            }\n" +
            "            shop {\n" +
            "                id\n" +
            "                name\n" +
            "                domain\n" +
            "                tagline\n" +
            "                slogan\n" +
            "                location\n" +
            "                city\n" +
            "                gold_shop\n" +
            "                gold_shop_badge\n" +
            "                shop_is_official\n" +
            "                product {\n" +
            "                    id\n" +
            "                    name\n" +
            "                    price_format\n" +
            "                    applinks\n" +
            "                    product_rating\n" +
            "                    product_cashback\n" +
            "                    product_cashback_rate\n" +
            "                    product_new_label\n" +
            "                    count_review_format\n" +
            "                    image_product{\n" +
            "                        product_id\n" +
            "                        product_name\n" +
            "                        image_url\n" +
            "                        image_click_url\n" +
            "                    }\n" +
            "                    campaign {\n" +
            "                        original_price\n" +
            "                        discount_percentage\n" +
            "                    }\n" +
            "                }\n" +
            "                image_shop {\n" +
            "                    cover\n" +
            "                    s_url\n" +
            "                    xs_url\n" +
            "                    cover_ecs\n" +
            "                    s_ecs\n" +
            "                    xs_ecs\n" +
            "                }\n" +
            "            }\n" +
            "            badges{\n" +
            "            image_url\n" +
            "            show\n" +
            "            title\n" +
            "            }\n" +
            "            button_text\n" +
            "            promoted_text\n" +
            "            description\n" +
            "            uri\n" +
            "            }\n" +
            "            applinks\n" +
            "        }\n" +
            "    }\n" +
            "\n" +
            "    global_search_navigation(keyword:$query, device:\"android\", size:5, params:$params) {\n" +
            "        data {\n" +
            "            source\n" +
            "            title\n" +
            "            keyword\n" +
            "            nav_template\n" +
            "            background\n" +
            "            see_all_applink\n" +
            "            see_all_url\n" +
            "            show_topads\n" +
            "            list {\n" +
            "                category_name\n" +
            "                name\n" +
            "                info\n" +
            "                image_url\n" +
            "                applink\n" +
            "                url\n" +
            "                subtitle\n" +
            "                strikethrough\n" +
            "                background_url\n" +
            "                logo_url\n" +
            "            }\n" +
            "        }\n" +
            "    }\n" +
            "\n" +
            "    searchInspirationCarousel(params: $params) {\n" +
            "        data {\n" +
            "            title\n" +
            "            type\n" +
            "            position\n" +
            "            options {\n" +
            "                title\n" +
            "                url\n" +
            "                applink\n" +
            "                product {\n" +
            "                    id\n" +
            "                    name\n" +
            "                    price\n" +
            "                    price_str\n" +
            "                    image_url\n" +
            "                    rating\n" +
            "                    count_review\n" +
            "                    url\n" +
            "                    applink\n" +
            "                }\n" +
            "            }\n" +
            "        }\n" +
            "    }\n" +
            "}\n";
}
