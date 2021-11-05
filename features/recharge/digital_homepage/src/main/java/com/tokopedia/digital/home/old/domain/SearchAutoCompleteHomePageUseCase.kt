package com.tokopedia.digital.home.old.domain

import com.google.gson.Gson
import com.tokopedia.digital.home.model.DigitalHomePageSearchAutoComplete
import com.tokopedia.digital.home.old.model.DigitalHomePageSearchCategoryModel
import com.tokopedia.digital.home.presentation.util.RechargeHomepageSectionMapper.mapSearchAutoCompletetoSearch
import com.tokopedia.digital.home.util.DigitalHomepageGqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy

class SearchAutoCompleteHomePageUseCase (graphqlRepository: GraphqlRepository): GraphqlUseCase<DigitalHomePageSearchAutoComplete>(graphqlRepository) {

    suspend fun searchAutoCompleteList(mapParams: Map<String, Any>, searchQuery: String): List<DigitalHomePageSearchCategoryModel> {
        setGraphqlQuery(DigitalHomepageGqlQuery.searchAutoComplete)
        setTypeClass(DigitalHomePageSearchAutoComplete::class.java)
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        setRequestParams(mapParams)

        val data = Gson().fromJson("{\n" +
                "    \"digiPersoSearchSuggestion\": {\n" +
                "      \"data\": {\n" +
                "        \"id\": \"suggestion\",\n" +
                "        \"name\": \"SUGGESTION\",\n" +
                "        \"tracking\": {\n" +
                "          \"userType\": \"non login\",\n" +
                "          \"keyword\": \"pake\"\n" +
                "        },\n" +
                "        \"items\": [\n" +
                "          {\n" +
                "            \"template\": \"header\",\n" +
                "            \"type\": \"\",\n" +
                "            \"appLink\": \"\",\n" +
                "            \"url\": \"\",\n" +
                "            \"title\": \"Pilihan Kategori Produk\",\n" +
                "            \"subtitle\": \"\",\n" +
                "            \"iconTitle\": \"\",\n" +
                "            \"iconSubtitle\": \"\",\n" +
                "            \"shortcutImage\": \"\",\n" +
                "            \"imageURL\": \"\",\n" +
                "            \"urlTracker\": \"\",\n" +
                "            \"discountPercentage\": \"\",\n" +
                "            \"discountedPrice\": \"\",\n" +
                "            \"originalPrice\": \"\",\n" +
                "            \"tracking\": {\n" +
                "              \"itemType\": \"\",\n" +
                "              \"categoryID\": \"\",\n" +
                "              \"categoryName\": \"\",\n" +
                "              \"operatorID\": \"\",\n" +
                "              \"operatorName\": \"\"\n" +
                "            },\n" +
                "            \"childItems\": []\n" +
                "          },\n" +
                "          {\n" +
                "            \"template\": \"list_single_line\",\n" +
                "            \"type\": \"\",\n" +
                "            \"appLink\": \"tokopedia://digital/form?category_id=2\",\n" +
                "            \"url\": \"https://staging.tokopedia.com/paket-data/\",\n" +
                "            \"title\": \"Paket Data\",\n" +
                "            \"subtitle\": \"\",\n" +
                "            \"iconTitle\": \"\",\n" +
                "            \"iconSubtitle\": \"\",\n" +
                "            \"shortcutImage\": \"\",\n" +
                "            \"imageURL\": \"https://ecs7.tokopedia.net/img/cache/100-square/attachment/2019/10/22/21181130/21181130_907dac9a-c185-43d1-b459-2389f0b6efea.png\",\n" +
                "            \"urlTracker\": \"\",\n" +
                "            \"discountPercentage\": \"\",\n" +
                "            \"discountedPrice\": \"\",\n" +
                "            \"originalPrice\": \"\",\n" +
                "            \"tracking\": {\n" +
                "              \"itemType\": \"autocomplete\",\n" +
                "              \"categoryID\": \"2\",\n" +
                "              \"categoryName\": \"Paket Data\",\n" +
                "              \"operatorID\": \"\",\n" +
                "              \"operatorName\": \"\"\n" +
                "            },\n" +
                "            \"childItems\": []\n" +
                "          },\n" +
                "          {\n" +
                "            \"template\": \"header\",\n" +
                "            \"type\": \"\",\n" +
                "            \"appLink\": \"\",\n" +
                "            \"url\": \"\",\n" +
                "            \"title\": \"Pilihan Operator\",\n" +
                "            \"subtitle\": \"\",\n" +
                "            \"iconTitle\": \"\",\n" +
                "            \"iconSubtitle\": \"\",\n" +
                "            \"shortcutImage\": \"\",\n" +
                "            \"imageURL\": \"\",\n" +
                "            \"urlTracker\": \"\",\n" +
                "            \"discountPercentage\": \"\",\n" +
                "            \"discountedPrice\": \"\",\n" +
                "            \"originalPrice\": \"\",\n" +
                "            \"tracking\": {\n" +
                "              \"itemType\": \"\",\n" +
                "              \"categoryID\": \"\",\n" +
                "              \"categoryName\": \"\",\n" +
                "              \"operatorID\": \"\",\n" +
                "              \"operatorName\": \"\"\n" +
                "            },\n" +
                "            \"childItems\": []\n" +
                "          },\n" +
                "          {\n" +
                "            \"template\": \"list_double_line\",\n" +
                "            \"type\": \"\",\n" +
                "            \"appLink\": \"tokopedia://digital/form?category_id=2&operator_id=1\",\n" +
                "            \"url\": \"https://staging.tokopedia.com/paket-data/?action=edit_data&operator_id=1\",\n" +
                "            \"title\": \"Axis\",\n" +
                "            \"subtitle\": \"Paket Data\",\n" +
                "            \"iconTitle\": \"\",\n" +
                "            \"iconSubtitle\": \"\",\n" +
                "            \"shortcutImage\": \"\",\n" +
                "            \"imageURL\": \"https://ecs7.tokopedia.net/img/recharge/operator/axis_2.png\",\n" +
                "            \"urlTracker\": \"\",\n" +
                "            \"discountPercentage\": \"\",\n" +
                "            \"discountedPrice\": \"\",\n" +
                "            \"originalPrice\": \"\",\n" +
                "            \"tracking\": {\n" +
                "              \"itemType\": \"autocomplete\",\n" +
                "              \"categoryID\": \"2\",\n" +
                "              \"categoryName\": \"Paket Data\",\n" +
                "              \"operatorID\": \"1\",\n" +
                "              \"operatorName\": \"Axis\"\n" +
                "            },\n" +
                "            \"childItems\": []\n" +
                "          },\n" +
                "          {\n" +
                "            \"template\": \"list_double_line\",\n" +
                "            \"type\": \"\",\n" +
                "            \"appLink\": \"tokopedia://digital/form?category_id=2&operator_id=584\",\n" +
                "            \"url\": \"https://staging.tokopedia.com/paket-data/?action=edit_data&operator_id=584\",\n" +
                "            \"title\": \"Hinet\",\n" +
                "            \"subtitle\": \"Paket Data\",\n" +
                "            \"iconTitle\": \"\",\n" +
                "            \"iconSubtitle\": \"\",\n" +
                "            \"shortcutImage\": \"\",\n" +
                "            \"imageURL\": \"https://ecs7.tokopedia.net/img/recharge/operator/hinet.png\",\n" +
                "            \"urlTracker\": \"\",\n" +
                "            \"discountPercentage\": \"\",\n" +
                "            \"discountedPrice\": \"\",\n" +
                "            \"originalPrice\": \"\",\n" +
                "            \"tracking\": {\n" +
                "              \"itemType\": \"autocomplete\",\n" +
                "              \"categoryID\": \"2\",\n" +
                "              \"categoryName\": \"Paket Data\",\n" +
                "              \"operatorID\": \"584\",\n" +
                "              \"operatorName\": \"Hinet\"\n" +
                "            },\n" +
                "            \"childItems\": []\n" +
                "          },\n" +
                "          {\n" +
                "            \"template\": \"list_double_line\",\n" +
                "            \"type\": \"\",\n" +
                "            \"appLink\": \"tokopedia://digital/form?category_id=2&operator_id=2\",\n" +
                "            \"url\": \"https://staging.tokopedia.com/paket-data/?action=edit_data&operator_id=2\",\n" +
                "            \"title\": \"Indosat - IM3\",\n" +
                "            \"subtitle\": \"Paket Data\",\n" +
                "            \"iconTitle\": \"\",\n" +
                "            \"iconSubtitle\": \"\",\n" +
                "            \"shortcutImage\": \"\",\n" +
                "            \"imageURL\": \"https://ecs7.tokopedia.net/img/recharge/operator/im3_2.png\",\n" +
                "            \"urlTracker\": \"\",\n" +
                "            \"discountPercentage\": \"\",\n" +
                "            \"discountedPrice\": \"\",\n" +
                "            \"originalPrice\": \"\",\n" +
                "            \"tracking\": {\n" +
                "              \"itemType\": \"autocomplete\",\n" +
                "              \"categoryID\": \"2\",\n" +
                "              \"categoryName\": \"Paket Data\",\n" +
                "              \"operatorID\": \"2\",\n" +
                "              \"operatorName\": \"Indosat - IM3\"\n" +
                "            },\n" +
                "            \"childItems\": []\n" +
                "          },\n" +
                "          {\n" +
                "            \"template\": \"list_double_line\",\n" +
                "            \"type\": \"\",\n" +
                "            \"appLink\": \"tokopedia://digital/form?category_id=2&operator_id=114\",\n" +
                "            \"url\": \"https://staging.tokopedia.com/paket-data/?action=edit_data&operator_id=114\",\n" +
                "            \"title\": \"Indosat - Matrix\",\n" +
                "            \"subtitle\": \"Paket Data\",\n" +
                "            \"iconTitle\": \"\",\n" +
                "            \"iconSubtitle\": \"\",\n" +
                "            \"shortcutImage\": \"\",\n" +
                "            \"imageURL\": \"https://ecs7.tokopedia.net/img/recharge/operator/indosat.png\",\n" +
                "            \"urlTracker\": \"\",\n" +
                "            \"discountPercentage\": \"\",\n" +
                "            \"discountedPrice\": \"\",\n" +
                "            \"originalPrice\": \"\",\n" +
                "            \"tracking\": {\n" +
                "              \"itemType\": \"autocomplete\",\n" +
                "              \"categoryID\": \"2\",\n" +
                "              \"categoryName\": \"Paket Data\",\n" +
                "              \"operatorID\": \"114\",\n" +
                "              \"operatorName\": \"Indosat - Matrix\"\n" +
                "            },\n" +
                "            \"childItems\": []\n" +
                "          },\n" +
                "          {\n" +
                "            \"template\": \"list_double_line\",\n" +
                "            \"type\": \"\",\n" +
                "            \"appLink\": \"tokopedia://digital/form?category_id=2&operator_id=17\",\n" +
                "            \"url\": \"https://staging.tokopedia.com/paket-data/?action=edit_data&operator_id=17\",\n" +
                "            \"title\": \"Indosat - Mentari\",\n" +
                "            \"subtitle\": \"Paket Data\",\n" +
                "            \"iconTitle\": \"\",\n" +
                "            \"iconSubtitle\": \"\",\n" +
                "            \"shortcutImage\": \"\",\n" +
                "            \"imageURL\": \"https://ecs7.tokopedia.net/img/recharge/operator/im3_2.png\",\n" +
                "            \"urlTracker\": \"\",\n" +
                "            \"discountPercentage\": \"\",\n" +
                "            \"discountedPrice\": \"\",\n" +
                "            \"originalPrice\": \"\",\n" +
                "            \"tracking\": {\n" +
                "              \"itemType\": \"autocomplete\",\n" +
                "              \"categoryID\": \"2\",\n" +
                "              \"categoryName\": \"Paket Data\",\n" +
                "              \"operatorID\": \"17\",\n" +
                "              \"operatorName\": \"Indosat - Mentari\"\n" +
                "            },\n" +
                "            \"childItems\": []\n" +
                "          },\n" +
                "          {\n" +
                "            \"template\": \"list_double_line\",\n" +
                "            \"type\": \"\",\n" +
                "            \"appLink\": \"tokopedia://digital/form?category_id=2&operator_id=2557\",\n" +
                "            \"url\": \"https://staging.tokopedia.com/paket-data/?action=edit_data&operator_id=2557\",\n" +
                "            \"title\": \"KOTA YOGYAKARTA\",\n" +
                "            \"subtitle\": \"Paket Data\",\n" +
                "            \"iconTitle\": \"\",\n" +
                "            \"iconSubtitle\": \"\",\n" +
                "            \"shortcutImage\": \"\",\n" +
                "            \"imageURL\": \"https://ecs7.tokopedia.net/img/recharge/operator/tirtadharma.png\",\n" +
                "            \"urlTracker\": \"\",\n" +
                "            \"discountPercentage\": \"\",\n" +
                "            \"discountedPrice\": \"\",\n" +
                "            \"originalPrice\": \"\",\n" +
                "            \"tracking\": {\n" +
                "              \"itemType\": \"autocomplete\",\n" +
                "              \"categoryID\": \"2\",\n" +
                "              \"categoryName\": \"Paket Data\",\n" +
                "              \"operatorID\": \"2557\",\n" +
                "              \"operatorName\": \"KOTA YOGYAKARTA\"\n" +
                "            },\n" +
                "            \"childItems\": []\n" +
                "          },\n" +
                "          {\n" +
                "            \"template\": \"list_double_line\",\n" +
                "            \"type\": \"\",\n" +
                "            \"appLink\": \"tokopedia://digital/form?category_id=2&operator_id=2706\",\n" +
                "            \"url\": \"https://staging.tokopedia.com/paket-data/?action=edit_data&operator_id=2706\",\n" +
                "            \"title\": \"Live.On\",\n" +
                "            \"subtitle\": \"Paket Data\",\n" +
                "            \"iconTitle\": \"\",\n" +
                "            \"iconSubtitle\": \"\",\n" +
                "            \"shortcutImage\": \"\",\n" +
                "            \"imageURL\": \"https://ecs7.tokopedia.net/img/attachment/2021/1/28/59205941/59205941_105f73bb-7a1e-4c33-8cda-a4031ed6142a.jpg\",\n" +
                "            \"urlTracker\": \"\",\n" +
                "            \"discountPercentage\": \"\",\n" +
                "            \"discountedPrice\": \"\",\n" +
                "            \"originalPrice\": \"\",\n" +
                "            \"tracking\": {\n" +
                "              \"itemType\": \"autocomplete\",\n" +
                "              \"categoryID\": \"2\",\n" +
                "              \"categoryName\": \"Paket Data\",\n" +
                "              \"operatorID\": \"2706\",\n" +
                "              \"operatorName\": \"Live.On\"\n" +
                "            },\n" +
                "            \"childItems\": []\n" +
                "          },\n" +
                "          {\n" +
                "            \"template\": \"list_double_line\",\n" +
                "            \"type\": \"\",\n" +
                "            \"appLink\": \"tokopedia://digital/form?category_id=2&operator_id=7\",\n" +
                "            \"url\": \"https://staging.tokopedia.com/paket-data/?action=edit_data&operator_id=7\",\n" +
                "            \"title\": \"Smartfren\",\n" +
                "            \"subtitle\": \"Paket Data\",\n" +
                "            \"iconTitle\": \"\",\n" +
                "            \"iconSubtitle\": \"\",\n" +
                "            \"shortcutImage\": \"\",\n" +
                "            \"imageURL\": \"https://ecs7.tokopedia.net/img/recharge/operator/smartfren_3.png\",\n" +
                "            \"urlTracker\": \"\",\n" +
                "            \"discountPercentage\": \"\",\n" +
                "            \"discountedPrice\": \"\",\n" +
                "            \"originalPrice\": \"\",\n" +
                "            \"tracking\": {\n" +
                "              \"itemType\": \"autocomplete\",\n" +
                "              \"categoryID\": \"2\",\n" +
                "              \"categoryName\": \"Paket Data\",\n" +
                "              \"operatorID\": \"7\",\n" +
                "              \"operatorName\": \"Smartfren\"\n" +
                "            },\n" +
                "            \"childItems\": []\n" +
                "          },\n" +
                "          {\n" +
                "            \"template\": \"list_double_line\",\n" +
                "            \"type\": \"\",\n" +
                "            \"appLink\": \"tokopedia://digital/form?category_id=2&operator_id=15\",\n" +
                "            \"url\": \"https://staging.tokopedia.com/paket-data/?action=edit_data&operator_id=15\",\n" +
                "            \"title\": \"Telkomsel - As\",\n" +
                "            \"subtitle\": \"Paket Data\",\n" +
                "            \"iconTitle\": \"\",\n" +
                "            \"iconSubtitle\": \"\",\n" +
                "            \"shortcutImage\": \"\",\n" +
                "            \"imageURL\": \"https://ecs7.tokopedia.net/img/recharge/operator/as_2.png\",\n" +
                "            \"urlTracker\": \"\",\n" +
                "            \"discountPercentage\": \"\",\n" +
                "            \"discountedPrice\": \"\",\n" +
                "            \"originalPrice\": \"\",\n" +
                "            \"tracking\": {\n" +
                "              \"itemType\": \"autocomplete\",\n" +
                "              \"categoryID\": \"2\",\n" +
                "              \"categoryName\": \"Paket Data\",\n" +
                "              \"operatorID\": \"15\",\n" +
                "              \"operatorName\": \"Telkomsel - As\"\n" +
                "            },\n" +
                "            \"childItems\": []\n" +
                "          },\n" +
                "          {\n" +
                "            \"template\": \"list_double_line\",\n" +
                "            \"type\": \"\",\n" +
                "            \"appLink\": \"tokopedia://digital/form?category_id=2&operator_id=113\",\n" +
                "            \"url\": \"https://staging.tokopedia.com/paket-data/?action=edit_data&operator_id=113\",\n" +
                "            \"title\": \"Telkomsel - Halo\",\n" +
                "            \"subtitle\": \"Paket Data\",\n" +
                "            \"iconTitle\": \"\",\n" +
                "            \"iconSubtitle\": \"\",\n" +
                "            \"shortcutImage\": \"\",\n" +
                "            \"imageURL\": \"https://ecs7.tokopedia.net/img/recharge/operator/telkomsel.png\",\n" +
                "            \"urlTracker\": \"\",\n" +
                "            \"discountPercentage\": \"\",\n" +
                "            \"discountedPrice\": \"\",\n" +
                "            \"originalPrice\": \"\",\n" +
                "            \"tracking\": {\n" +
                "              \"itemType\": \"autocomplete\",\n" +
                "              \"categoryID\": \"2\",\n" +
                "              \"categoryName\": \"Paket Data\",\n" +
                "              \"operatorID\": \"113\",\n" +
                "              \"operatorName\": \"Telkomsel - Halo\"\n" +
                "            },\n" +
                "            \"childItems\": []\n" +
                "          }\n" +
                "        ]\n" +
                "      }\n" +
                "    }\n" +
                "}", DigitalHomePageSearchAutoComplete::class.java)

        val searchCategoryData = mapSearchAutoCompletetoSearch(data, searchQuery)
        return searchCategoryData
    }

}