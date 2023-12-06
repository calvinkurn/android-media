package com.tokopedia.recommendation_widget_common.domain.coroutines

import android.content.Context
import com.google.gson.Gson
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.recommendation_widget_common.data.RecommendationEntity
import com.tokopedia.recommendation_widget_common.domain.coroutines.base.UseCase
import com.tokopedia.recommendation_widget_common.domain.query.ListProductRecommendationQuery
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.recommendation_widget_common.ext.toQueryParam
import com.tokopedia.recommendation_widget_common.extension.mappingToRecommendationModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.user.session.UserSession
import javax.inject.Inject

/**
 * Created by devara fikry on 16/04/19.
 */
open class GetRecommendationUseCase @Inject constructor(
    private val context: Context,
    private val graphqlRepository: GraphqlRepository,
) : UseCase<GetRecommendationRequestParam, List<RecommendationWidget>>() {

    private val graphqlUseCase = GraphqlUseCase<RecommendationEntity>(graphqlRepository)
    init {
        graphqlUseCase.setTypeClass(RecommendationEntity::class.java)
        val remoteConfig: RemoteConfig = FirebaseRemoteConfigImpl(context)
        graphqlUseCase.setGraphqlQuery(ListProductRecommendationQuery())
    }
    override suspend fun getData(inputParameter: GetRecommendationRequestParam): List<RecommendationWidget> {
        return Gson().fromJson(dummy, RecommendationEntity::class.java).productRecommendationWidget.data.mappingToRecommendationModel()
        val userSession = UserSession(context)
        inputParameter.userId = userSession.userId.toIntOrNull() ?: 0
        val queryParam = ChooseAddressUtils.getLocalizingAddressData(context)?.toQueryParam(inputParameter.queryParam) ?: inputParameter.queryParam
        graphqlUseCase.setRequestParams(inputParameter.copy(queryParam = queryParam).toGqlRequest())
        return graphqlUseCase.executeOnBackground().productRecommendationWidget.data.mappingToRecommendationModel()
    }

    val dummy = """
        {
            "productRecommendationWidget": {
              "data": [
                {
                  "tID": "9keSRZkLlF-tF8-AJNaDjos9Y-Q=",
                  "title": "Lagi butuh ini?",
                  "subtitle": "Cek Serum Wajah",
                  "layoutType": "Grid",
                  "pageName": "pdp_steal_the_look",
                  "seeMoreAppLink": "tokopedia://rekomendasi/0/d/?ref=best_seller_switching&category_id=2265",
                  "pagination": {
                    "hasNext": false
                  },
                  "campaign": {
                    "appLandingPageLink": "",
                    "assets": {
                      "banner": {
                        "apps": ""
                      }
                    }
                  },
                  "recommendation": [
                    {
                      "gridPosition": "left",
                      "id": 7127306149,
                      "name": "Amura Gold Serum - Atasi Flek Hitam & Penuaan",
                      "parentID": 0,
                      "appUrl": "tokopedia://product/7127306149?src=topads",
                      "clickUrl": "https://ta.tokopedia.com/promo/v1/clicks/8a-xgVY2gmUEH_rRH_ya6_1hbm-xgVY789CBUsthbm-FQRo2PcB5QiUEHZFiPcBWgZUEH_rFH_Hho_jp6ZFiyRCsUsth6A1Fbm-xgVYpyfYagZUEHpKfbsrN6AjOo_JN6_jFosHObm-pHOYDQfri6i-B812kgJxGgBBXZSgjH7NDZ325q1OAoAuozJJO_1zCo1OJe_uozJJO_1zCo1B2PfBgHO-N3Ao6QVByZM2xe7jfZ320P1N1Z92vzJJO_Bz-8jYJe_u6uJjFZ3BRq3JausujHsBN3jyN8Bja69Bq17jfZ32Cq1hAZSuiHsuk3Bo-ojBW_MWo8Mz2_uz-Hjh1e9Pvuco7_jPzHO2AH3xqucHa_VzZQu29Z9Bo8MDO33O3Qu2_Z9o-Q_BNyuPjrc-D63Wq3J-MyuPzq1Y2Z9P-q9P2ysoGrVtaQIuyH7-Nys-ZHujp1MgxuOV2_fB-P7B2PfBiH72F3s-DPuKp_MYiH7-MyuPzq1Y2Z9P-q9P2yOx3QcoXQcgjz7gXyRB-ojBD6IBoqBjh3I2mgjOc6IPyH_xR3I2mgjOc6IP-q9P2yp-6PMoWuMggQj2fgAo6QJBkQfBo8Mra_c2so1YJqpV6uJ1O_Oz0P7Nk__V-q9P2yp-6PMoWuMgsHBgtyfO6Q7BkQfBoe7tNUiFiP9oBrBY2gmUEUsJf6_HN6_JOUiFirfuprfB5QBY2gmUEUsKFg_zsg_gxgcHNopoMopURgA-sH_uB6_1pg_Hd63yOHf-sHArfgs1ho_Uh6_yR6AnaHfusy_ehHfJhHsyNHp-MosPx6_K7opusHfUOg3-Bo_rdHAjfH_Bxg_PxosgsH3yp6_1dH3VjypBjyfeOHAgBosVjH3VM6cydH_rNHp1po_edy3U7HAJfg_Psy_yaopBigsepHAeNHsKp63Uay31ibm-xQcri6i-ig9o79RoBQchBrBYfHaUDUMNOQ3-BrBY5gBYxgIHi6sKDUMNOQ3-BrBYxgIowrMuhUstdbm-sy9zwq3zpUs2QosJDoAJfbAUaosuPbm-X9foxQMz2gcV7guYxgIHi6seFbm-xyBY7g9o7Usti_iUDUSC5rRzwy3hSUstigcuMUiFiPMuarfB5QiUEUSyaUiFiyfhOrRzBrBY2gVYfHiUEHsK7HseDUSCaq3oB9f-2gmUEH_KOHmFigfYxQVY2gmUEHR7?sid=80e4ce6adc973f727d2c15e953e389f53bc076f515219678023eca413a126932f67a98475c3b5ebe57809619ae7a66c1f39581adc9dcd506e61d1af8f8179353548ab4016e7ca6279bf430492839b2ae&page=1&src=recom_widget_best_seller_switching&t=ios&dv=ios-2.253.0&r=https%3A%2F%2Fwww.tokopedia.com%2Famuraof%2Famura-gold-serum-atasi-flek-hitam-penuaan%3Fsrc%3Dtopads&ob=0&management_type=1",
                      "wishlistUrl": "https://ta.tokopedia.com/promo/v1/wishlists/8a-xgVY2gmUEH_rRH_ya6_1hbm-xgVY789CBUsthbm-FQRo2PcB5QiUEHZFiPcBWgZUEH_rFH_Hho_jp6ZFiyRCsUsth6A1Fbm-xgVYpyfYagZUEHpKfbsrN6AjOo_JN6_jFosHObm-pHOYDQfri6i-B812kgJxGgBBXZSgjH7NDZ325q1OAoAuozJJO_1zCo1OJe_uozJJO_1zCo1B2PfBgHO-N3Ao6QVByZM2xe7jfZ320P1N1Z92vzJJO_Bz-8jYJe_u6uJjFZ3BRq3JausujHsBN3jyN8Bja69Bq17jfZ32Cq1hAZSuiHsuk3Bo-ojBW_MWo8Mz2_uz-Hjh1e9Pvuco7_jPzHO2AH3xqucHa_VzZQu29Z9Bo8MDO33O3Qu2_Z9o-Q_BNyuPjrc-D63Wq3J-MyuPzq1Y2Z9P-q9P2ysoGrVtaQIuyH7-Nys-ZHujp1MgxuOV2_fB-P7B2PfBiH72F3s-DPuKp_MYiH7-MyuPzq1Y2Z9P-q9P2yOx3QcoXQcgjz7gXyRB-ojBD6IBoqBjh3I2mgjOc6IPyH_xR3I2mgjOc6IP-q9P2yp-6PMoWuMggQj2fgAo6QJBkQfBo8Mra_c2so1YJqpV6uJ1O_Oz0P7Nk__V-q9P2yp-6PMoWuMgsHBgtyfO6Q7BkQfBoe7tNUiFiP9oBrBY2gmUEUsJf6_HN6_JOUiFirfuprfB5QBY2gmUEUsKFg_zsg_gxgcHNopoMopURgA-sH_uB6_1pg_Hd63yOHf-sHArfgs1ho_Uh6_yR6AnaHfusy_ehHfJhHsyNHp-MosPx6_K7opusHfUOg3-Bo_rdHAjfH_Bxg_PxosgsH3yp6_1dH3VjypBjyfeOHAgBosVjH3VM6cydH_rNHp1po_edy3U7HAJfg_Psy_yaopBigsepHAeNHsKp63Uay31ibm-xQcri6i-ig9o79RoBQchBrBYfHaUDUMNOQ3-BrBY5gBYxgIHi6sKDUMNOQ3-BrBYxgIowrMuhUstdbm-sy9zwq3zpUs2QosJDoAJfbAUaosuPbm-X9foxQMz2gcV7guYxgIHi6seFbm-xyBY7g9o7Usti_iUDUSC5rRzwy3hSUstigcuMUiFiPMuarfB5QiUEUSyaUiFiyfhOrRzBrBY2gVYfHiUEHsK7HseDUSCaq3oB9f-2gmUEH_KOHmFigfYxQVY2gmUEHR7?dv=ios-2.253.0&sid=80e4ce6adc973f727d2c15e953e389f53bc076f515219678023eca413a126932f67a98475c3b5ebe57809619ae7a66c1f39581adc9dcd506e61d1af8f8179353548ab4016e7ca6279bf430492839b2ae&management_type=1&product_id=7127306149&t=ios&page=1&r=https%3A%2F%2Fwww.tokopedia.com%2Famuraof%2Famura-gold-serum-atasi-flek-hitam-penuaan%3Fsrc%3Dtopads&uid=16939915&src=recom_widget_best_seller_switching&ob=0",
                      "trackerImageUrl": "https://ta.tokopedia.com/promo/v1/views/8a-2rOYDQfPwgcV7yZUEHZFiy3zwq3ei6sJRopJfHsjOHZFiy3zwPIBFgZUEHZFircYpq9z2Qfdi6sJDUSz2Q31i6sJRHAJpH_1NHpjDUSCxPcKi6i-2Q3VSg9HXPcY0QRCBgcBxbMNBPmY2Q3r5yfVsqc15HpnFb9ohP3VagZY3r3-sQ175HsnaHa4hHZ4aoaYxg3-jHsuxoZ7Rgcosb_zjHAjW6_Pi6m7ay3VioAVigcyRg3eXqSCSUiFiy3zwrfo5rM1i6sHdoidR6_KNo_1h6_jNHAypoZFirpowQcYSUstig9BGqMzUZMggQj2fgAo6QJBkQfBoepeO_1zCo1OJe_uozJJO_1zCo1OJe_u-q9P23_oZ8uKp_Mhg3J2ky1o-ojBkqRu6uJBE_7zCo1N1Z92vzJJO_Bz-HJB2PfBxHByOgAUN8u2c692gHsBN3Bo-ojBke3BHe72OysUOqB2_Z_g-Q1N0_92jq1O1Z_-HuJVR_OzsPJNI1_oqepVt3BzsHjh11MOqu7BN_920ouBWuMOq17BpZ37N83V9gICiQAB03BxmgMV913Bvq1BRZ3BRq3UpZSCqHMhO3Aom83Ua1sVgHO-MyuPzq1Y2Z9P-q9P2ysoGrVtaQIuyH7N5ysomgMV913Bvq1BRZ3BRq3oyuMhsQMhMgJPcQMoNZ_g-QAxN_32gHuxEeMgozsxR3AJdPOxEeMgozsxRZ3BRq3Ha_SgsQugM33NGPMep_Mh-qMY2_92SHjhkypuvzcDh_BzVo1Y1qRP6qj7hZ3BRq3Ha_SgsQugMyp-3qcoW_MY-qMY2_1oG6ZUDUSupg9-wq3ei6iUhosjp6_jhoZUDUSoBrRo2QfNwq3ei6iUdHc17yf1fy3zs6_rpgsraofeaypJOg_jOHf1p6ABMo_oiypnRoMyOH_1aH_jfopKFHsoByfJ7H_oxH_Uf6_HagsyRy_jdoArOypoio3uig_1R6AnNosJNy31Ry_yfypVMHpjO6AVxgcHNgcojo_nfg_yhgAVxgsxM6AJR6_HOHp176cVioAnhoM1RyfJfHsrNyMy7Hpn76_UdHpBiHMVBUiFiy3hSUstiyMupPVYpg3hDg9-wPsHibm-XP3Oig9-wQfgwy3zpUstdbm-XP3Oig9-wy3zp9R-BrZUE6mFiQBYsy3Njq3zxPcuwy3zpUst7HmFiy3-wPcupPmUEUjdibm-FQRo79fVDgaUE3a-jg3yi9ZFiPMuarfB5QiUEUSyaUiFiyfhOrRzBrBY2gVYfHiUEHsK7HseDUSCaq3oB9f-2gmUEH_KOHmFigfYxQVY2gmUEHR7?src=recom_widget_best_seller_switching&t=ios&sid=80e4ce6adc973f727d2c15e953e389f53bc076f515219678023eca413a126932f67a98475c3b5ebe57809619ae7a66c1f39581adc9dcd506e61d1af8f8179353548ab4016e7ca6279bf430492839b2ae&page=1&dv=ios-2.253.0&ob=0&management_type=1",
                      "imageUrl": "https://images.tokopedia.net/img/cache/300-square/VqbcmM/2023/11/27/aebd25a5-7dcc-4d09-97b8-2aab41bdf7ed.jpg",
                      "url": "https://www.tokopedia.com/amuraof/amura-gold-serum-atasi-flek-hitam-penuaan?src=topads",
                      "price": "Rp118.300",
                      "priceInt": 118300,
                      "departmentId": 2265,
                      "badges": [
                        {
                          "title": "Power Merchant Pro",
                          "imageUrl": "https://images.tokopedia.net/img/goldmerchant/pm_activation/badge/Power%20Merchant%20Pro@2x.png"
                        }
                      ],
                      "freeOngkir": {
                        "isActive": true,
                        "imageUrl": "https://images.tokopedia.net/img/img/promo/targeting/red_bo_20k.png"
                      },
                      "rating": 5,
                      "ratingAverage": "5.0",
                      "countReview": 261,
                      "isTopads": true,
                      "categoryBreadcrumbs": "Kecantikan/Perawatan Wajah/Serum Wajah",
                      "recommendationType": "best_seller_v3",
                      "isWishlist": false,
                      "slashedPrice": "Rp169.000",
                      "discountPercentage": 30,
                      "shop": {
                        "id": 9897651,
                        "name": "Amura Official",
                        "city": "Bogor",
                        "location": "Bogor"
                      },
                      "labelgroup": [
                        {
                          "title": "Cashback 4%",
                          "type": "lightGreen",
                          "position": "price",
                          "url": ""
                        },
                        {
                          "title": "Cashback",
                          "type": "lightGreen",
                          "position": "promo",
                          "url": ""
                        },
                        {
                          "title": "500+ terjual",
                          "type": "textDarkGrey",
                          "position": "integrity",
                          "url": ""
                        },
                        {
                          "title": "Terlaris",
                          "type": "#E1AA1D",
                          "position": "best_seller",
                          "url": ""
                        }
                      ],
                      "labelGroupVariant": [],
                      "minOrder": 0,
                      "maxOrder": 0,
                      "specificationLabels": [],
                      "warehouseID": 0
                    },
                    {
                      "gridPosition": "top_right",
                      "id": 774161616,
                      "name": "SK-II SKII SK II SK2 FTE ESSENCE GENOPTICS AURA 10 ml CLEAR LOTION",
                      "parentID": 0,
                      "appUrl": "tokopedia://product/774161616?src=topads",
                      "clickUrl": "https://ta.tokopedia.com/promo/v1/clicks/8a-xgVY2gmUEo_Jdo_1h6AUDUMVj9RzNrc1i6sJDUSC5rfB7q3YXUstabm-7q3OBUsthopnhHpJO6_HNbm-srcHi6sHOo_nDUMVj9RosQR-BUstp6AUXoAjp6_J76_JhHsnpHAHDUSHp9fh5gaUEUMuNZM2jZJ2M33NGPMep_Mh-qMY2_32uq1hAZM2jZJ2Myp-3qcoW_MY-qMY2_1o-r7BWPchB3czfyfOZgMHa_SgsQuu2_fB-P7B2PfBiQ_BO3_-uq1Y2ZM2qzJ7p332V81N2HIPozcDp_VzZq7da19zguO1p_MjFHV2WuMBoqj7O_OPGQV2Wu3BHe72fyfODQMV9o3gqzOgR3A-Dq7BkQfBoe7BpZ37N83V9gICiQABRyf7Nqfz9_sCyHMh0Z325q1OAZ9o-Q_BNyuPjrc-D692xzpBR3A-Dq7BkQfBoe7BpZ3NcHu2yZsuyHO-t3sooq1Y2ZMgoqjja_BydPOxEeMgozsBM_1ydPOxEeMgoe7BpZ3N6qMUpZMhyHj2Nysoj8B2_Z_g-qj77_3j7HJY1__uouVJO_uzV81OJ_9Po81BpZ3N6qMUpZMhyH7ND3uxGqMVAZ_g-qjV2gBJYvZUDUSupg9-wq3ei6iUhosjp6_jhoZUDUSoBrRo2QfNwq3ei6iUdHc17yf1fy3zs6_rpgsraofeaypJOg_jOHf1p6ABMo_oiypnRoMyOH_1aH_jfopKFHsoByfJ7H_oxH_Uf6_HagsyRy_jdoArOypoio3uig_1R6AnNosJNy31Ry_yfypVMHpjO6AVxgcHNgcojo_nfg_yhgAVxgsxM6AJR6_HOHp176cVioAnhoM1RyfJfHsrNyMy7Hpn76_UdHpBiHMVBUiFiy3hSUstiyMupPVYpg3hDg9-wPsHibm-XP3Oig9-wQfgwy3zpUstdbm-XP3Oig9-wy3zp9R-BrZUE6mFiyfV79fBjraUE3pyhbAehoiFaHsyO9ZFiQBYsy3Njq3zxPcuwy3zpUst7HmFiy3-wPcupPmUEUjdibm-FQRo79fVDgaUEUMzBgiUDUSgBrSo2Qfdi6i-fHiUDUMoDP9o7g9-wq3zwPsUi6sHFos1pbm-FrMBsguYiq3ei6sHOo_nDUMP5y3hwq3ei6soY?sid=80e4ce6adc973f727d2c15e953e389f53bc076f515219678023eca413a126932f67a98475c3b5ebe57809619ae7a66c1f39581adc9dcd506e61d1af8f8179353548ab4016e7ca6279bf430492839b2ae&ob=0&t=ios&dv=ios-2.253.0&r=https%3A%2F%2Fwww.tokopedia.com%2Fskiifelice%2Fsk-ii-skii-sk-ii-sk2-fte-essence-genoptics-aura-10-ml-clear-lotion%3Fsrc%3Dtopads&src=recom_widget_best_seller_switching&page=1&management_type=1",
                      "wishlistUrl": "https://ta.tokopedia.com/promo/v1/wishlists/8a-xgVY2gmUEo_Jdo_1h6AUDUMVj9RzNrc1i6sJDUSC5rfB7q3YXUstabm-7q3OBUsthopnhHpJO6_HNbm-srcHi6sHOo_nDUMVj9RosQR-BUstp6AUXoAjp6_J76_JhHsnpHAHDUSHp9fh5gaUEUMuNZM2jZJ2M33NGPMep_Mh-qMY2_32uq1hAZM2jZJ2Myp-3qcoW_MY-qMY2_1o-r7BWPchB3czfyfOZgMHa_SgsQuu2_fB-P7B2PfBiQ_BO3_-uq1Y2ZM2qzJ7p332V81N2HIPozcDp_VzZq7da19zguO1p_MjFHV2WuMBoqj7O_OPGQV2Wu3BHe72fyfODQMV9o3gqzOgR3A-Dq7BkQfBoe7BpZ37N83V9gICiQABRyf7Nqfz9_sCyHMh0Z325q1OAZ9o-Q_BNyuPjrc-D692xzpBR3A-Dq7BkQfBoe7BpZ3NcHu2yZsuyHO-t3sooq1Y2ZMgoqjja_BydPOxEeMgozsBM_1ydPOxEeMgoe7BpZ3N6qMUpZMhyHj2Nysoj8B2_Z_g-qj77_3j7HJY1__uouVJO_uzV81OJ_9Po81BpZ3N6qMUpZMhyH7ND3uxGqMVAZ_g-qjV2gBJYvZUDUSupg9-wq3ei6iUhosjp6_jhoZUDUSoBrRo2QfNwq3ei6iUdHc17yf1fy3zs6_rpgsraofeaypJOg_jOHf1p6ABMo_oiypnRoMyOH_1aH_jfopKFHsoByfJ7H_oxH_Uf6_HagsyRy_jdoArOypoio3uig_1R6AnNosJNy31Ry_yfypVMHpjO6AVxgcHNgcojo_nfg_yhgAVxgsxM6AJR6_HOHp176cVioAnhoM1RyfJfHsrNyMy7Hpn76_UdHpBiHMVBUiFiy3hSUstiyMupPVYpg3hDg9-wPsHibm-XP3Oig9-wQfgwy3zpUstdbm-XP3Oig9-wy3zp9R-BrZUE6mFiyfV79fBjraUE3pyhbAehoiFaHsyO9ZFiQBYsy3Njq3zxPcuwy3zpUst7HmFiy3-wPcupPmUEUjdibm-FQRo79fVDgaUEUMzBgiUDUSgBrSo2Qfdi6i-fHiUDUMoDP9o7g9-wq3zwPsUi6sHFos1pbm-FrMBsguYiq3ei6sHOo_nDUMP5y3hwq3ei6soY?sid=80e4ce6adc973f727d2c15e953e389f53bc076f515219678023eca413a126932f67a98475c3b5ebe57809619ae7a66c1f39581adc9dcd506e61d1af8f8179353548ab4016e7ca6279bf430492839b2ae&product_id=774161616&src=recom_widget_best_seller_switching&management_type=1&dv=ios-2.253.0&uid=16939915&r=https%3A%2F%2Fwww.tokopedia.com%2Fskiifelice%2Fsk-ii-skii-sk-ii-sk2-fte-essence-genoptics-aura-10-ml-clear-lotion%3Fsrc%3Dtopads&page=1&t=ios&ob=0",
                      "trackerImageUrl": "https://ta.tokopedia.com/promo/v1/views/8a-2rOYDQfPwgcV7yZUEHZFiy3zwq3ei6s1h6A1OH_Kabm-xgVY789CBUsthbm-FQRo2PcB5QiUEHiFiPcBWgZUEH_rFH_Hho_jp6ZFircV7qmUEUMBWy3PBraN7QfW5rcujq3JXQMu7bfBWgaYsy3otgZ4pHAnWrRVOy9-BbRCaQfzOyReWHZ4aHAUFbpe5oa4Ropnh6_ndbprRHAJNHAxwosrdHsUhgAJWy_HdoZ77ysgBb_KOgAUWo_URos17ysjfoArR9prFHV4RHAnibm-xgVYpyfYagZUEHpKabseNHpjhoAjhH_UFHpnpbm-pHOYDQfri6i-B812kgJxGgBBXZSgjH7NDZ325q1Oku3BHe72kgJxGgMHauMxsQ1N5Z325q1OAZ9o-Q9zDguxjPMoW1MgsHjNfyfOuq1Y2Z9P-q9P2yM7NPujau3Bvq12k3jzoHOBkz9B6q_CR_1z0H7h11MW6HBV73uPuH7N2HACqQug2_32oo1Y9ZMhqQuu2_JoGPMoWQcNxupuM3jP3POKaQcW-qMY2_1o-r7BW69BxufzFyMFNPfoW63Wju7dF3A-Dq7BkQfBoe7BpZ37N83V9gICiQABEy1rNPOKaQcW-qMY2_1o-r7BXzsVq3JtO3AoZqVtp_3Bvq12M_32-HjN36IPy8j-M_1yNgjOc6IPy8j-M_1o-r7BX_M2iH72D3A-G83UpgI2q17jfZ32ooJO2oACvuJ7O_uzzo1O1z9BozJOR_9B-r7BX_M2iH72D3Ao6QVByZM2xe7jfZ32Cq3gzv_7ibm-Orfua9fBjUstiH_yNHpjNH_1ibm-pg9opq3YX9fBjUsti6ACBocoBoMVjypjRHfyRHsPjHMHho31No_oBHpKNgs1pyMHFopgMo_JOHsJNosrdHAUpg3oxoAJpy_JaosjpHMyfofJN6AeRo3HpysuByM1OopKF6_yh63VBofJfoMHhgsHNo_Khy3zs63zsgA1FoM1fH3ehy3ydgsKhopjpo_HOoAxxyseFH_gBofoxosUR63-MoAHFoAja6AHNys-xgZUDUMVDgaUEUM-BrRzwrfuDQcua9RypUiFiQSuWyMua9fYM9fVjraUE6mFiQSuWyMua9fVjrOYag9Ji6sKDUMNwyfVXgcBjy9zB9fVjraUEoAnDUMVi9RzBrRei6i-6UiFircYpPVYxQcri6BDigcuMUB7DUSgBrSo2Qfdi6i-fHiUDUMoDP9o7g9-wq3zwPsUi6sHFos1pbm-FrMBsguYiq3ei6sHOo_nDUMP5y3hwq3ei6soY?sid=80e4ce6adc973f727d2c15e953e389f53bc076f515219678023eca413a126932f67a98475c3b5ebe57809619ae7a66c1f39581adc9dcd506e61d1af8f8179353548ab4016e7ca6279bf430492839b2ae&ob=0&dv=ios-2.253.0&management_type=1&t=ios&page=1&src=recom_widget_best_seller_switching",
                      "imageUrl": "https://images.tokopedia.net/img/cache/300-square/product-1/2020/4/7/7701908/7701908_678221d1-a385-4b6e-85d2-527654b96477_700_700",
                      "url": "https://www.tokopedia.com/skiifelice/sk-ii-skii-sk-ii-sk2-fte-essence-genoptics-aura-10-ml-clear-lotion?src=topads",
                      "price": "Rp560.000",
                      "priceInt": 560000,
                      "departmentId": 2265,
                      "badges": [
                        {
                          "title": "Power Merchant Pro",
                          "imageUrl": "https://images.tokopedia.net/img/goldmerchant/pm_activation/badge/Power%20Merchant%20Pro@2x.png"
                        }
                      ],
                      "freeOngkir": {
                        "isActive": true,
                        "imageUrl": "https://images.tokopedia.net/img/img/promo/targeting/red_bo_20k.png"
                      },
                      "rating": 5,
                      "ratingAverage": "4.9",
                      "countReview": 12,
                      "isTopads": true,
                      "categoryBreadcrumbs": "Kecantikan/Perawatan Wajah/Serum Wajah",
                      "recommendationType": "best_seller_v3",
                      "isWishlist": false,
                      "slashedPrice": "",
                      "discountPercentage": 0,
                      "shop": {
                        "id": 934603,
                        "name": "SK-II Felice Kosmetik",
                        "city": "Jakarta Utara",
                        "location": "Jakarta Utara"
                      },
                      "labelgroup": [
                        {
                          "title": "Cashback 5%",
                          "type": "lightGreen",
                          "position": "price",
                          "url": ""
                        },
                        {
                          "title": "Cashback",
                          "type": "lightGreen",
                          "position": "promo",
                          "url": ""
                        },
                        {
                          "title": "17 terjual",
                          "type": "textDarkGrey",
                          "position": "integrity",
                          "url": ""
                        },
                        {
                          "title": "Terlaris",
                          "type": "#E1AA1D",
                          "position": "best_seller",
                          "url": ""
                        }
                      ],
                      "labelGroupVariant": [],
                      "minOrder": 0,
                      "maxOrder": 0,
                      "specificationLabels": [],
                      "warehouseID": 0
                    },
                    {
                      "gridPosition": "bottom_right",
                      "id": 10967158340,
                      "name": "PROMO Kiehls Clearly Corrective Dark Spot Solution Serum",
                      "parentID": 0,
                      "appUrl": "tokopedia://product/10967158340?src=topads",
                      "clickUrl": "https://ta.tokopedia.com/promo/v1/clicks/8a-xgVY2gmUEH_r76_nFHprdbm-xgVY789CBUsthbm-FQRo2PcB5QiUEHaFiPcBWgZUEH_rFH_Hho_jp6ZFiyRCsUstNo_nDUMVj9RosQR-BUstp6AUXHsHaHsJ7o_n7H_jR6ZFirpowQcYSUstig9BGqMzUZMggQj2fgAo6QJBkQfB6q_zN_Bo-r7BW_sCsQABE3BPc8ujagfBvq1Bd_jH781YJu_oouVVN_7zuH7O119BvzVu2_JoGrB2yQAoiH7203Ao6qMUpZMh-qMY2_1o-r7BWo9giQ1NDZ325quja1926HjBd_32gPJOJe_u68_nF3jzjq7h9zMh68BB7_jPqQVBkZ92vuch23BPqQJB2PfBiH72F3s-DPuKa1MhszsBF3jo-ojBke3BHe72fyfODQMV9o3gsZJ2f3jx3qMzc69Cqe7jfZ32Cq1hAZSgsQ3hXyurOgMHaqIgszsBF3jo-ojBke3BHe72dgVP383u36_CgufzEZ325quxEZ9B6qBgM_1ydPOxEeMgy8j-M_1ydPOxEe3BHe72E3_UN8u2363BsQ_jpyp-uq1Y2Z92vzJBO_32o81Okz_C6uJJF_uz0H7Y_Z9o-QjNkysoGQVKp_Mhg3J2ky1o-ojBke3BM1_7YUiFiP9oBrBY2gmUEUsJf6_HN6_JOUiFirfuprfB5QBY2gmUEUsKFg_zsg_gxgcHNopoMopURgA-sH_uB6_1pg_Hd63yOHf-sHArfgs1ho_Uh6_yR6AnaHfusy_ehHfJhHsyNHp-MosPx6_K7opusHfUOg3-Bo_rdHAjfH_Bxg_PxosgsH3yp6_1dH3VjypBjyfeOHAgBosVjH3VM6cydH_rNHp1po_edy3U7HAJfg_Psy_yaopBigsepHAeNHsKp63Uay31ibm-xQcri6i-ig9o79RoBQchBrBYfHaUDUMNOQ3-BrBY5gBYxgIHi6sKDUMNOQ3-BrBYxgIowrMuhUstdbm-sy9zwq3zpUs2QosJDoAJfbAUaosuPbm-X9foxQMz2gcV7guYxgIHi6seFbm-xyBY7g9o7Usti_iUDUSC5rRzwy3hSUstigcuMUiFiPMuarfB5QiUEUSyaUiFiyfhOrRzBrBY2gVYfHiUEHsKhH_UDUSCaq3oB9f-2gmUE6_1Fbm-SQfVD9fBjUstpwe?management_type=1&src=recom_widget_best_seller_switching&page=1&ob=0&sid=80e4ce6adc973f727d2c15e953e389f53bc076f515219678023eca413a126932f67a98475c3b5ebe57809619ae7a66c1f39581adc9dcd506e61d1af8f8179353548ab4016e7ca6279bf430492839b2ae&t=ios&dv=ios-2.253.0&r=https%3A%2F%2Fwww.tokopedia.com%2Fbeautyandgentle%2Fpromo-kiehls-clearly-corrective-dark-spot-solution-serum-4ml%3Fsrc%3Dtopads",
                      "wishlistUrl": "https://ta.tokopedia.com/promo/v1/wishlists/8a-xgVY2gmUEH_r76_nFHprdbm-xgVY789CBUsthbm-FQRo2PcB5QiUEHaFiPcBWgZUEH_rFH_Hho_jp6ZFiyRCsUstNo_nDUMVj9RosQR-BUstp6AUXHsHaHsJ7o_n7H_jR6ZFirpowQcYSUstig9BGqMzUZMggQj2fgAo6QJBkQfB6q_zN_Bo-r7BW_sCsQABE3BPc8ujagfBvq1Bd_jH781YJu_oouVVN_7zuH7O119BvzVu2_JoGrB2yQAoiH7203Ao6qMUpZMh-qMY2_1o-r7BWo9giQ1NDZ325quja1926HjBd_32gPJOJe_u68_nF3jzjq7h9zMh68BB7_jPqQVBkZ92vuch23BPqQJB2PfBiH72F3s-DPuKa1MhszsBF3jo-ojBke3BHe72fyfODQMV9o3gsZJ2f3jx3qMzc69Cqe7jfZ32Cq1hAZSgsQ3hXyurOgMHaqIgszsBF3jo-ojBke3BHe72dgVP383u36_CgufzEZ325quxEZ9B6qBgM_1ydPOxEeMgy8j-M_1ydPOxEe3BHe72E3_UN8u2363BsQ_jpyp-uq1Y2Z92vzJBO_32o81Okz_C6uJJF_uz0H7Y_Z9o-QjNkysoGQVKp_Mhg3J2ky1o-ojBke3BM1_7YUiFiP9oBrBY2gmUEUsJf6_HN6_JOUiFirfuprfB5QBY2gmUEUsKFg_zsg_gxgcHNopoMopURgA-sH_uB6_1pg_Hd63yOHf-sHArfgs1ho_Uh6_yR6AnaHfusy_ehHfJhHsyNHp-MosPx6_K7opusHfUOg3-Bo_rdHAjfH_Bxg_PxosgsH3yp6_1dH3VjypBjyfeOHAgBosVjH3VM6cydH_rNHp1po_edy3U7HAJfg_Psy_yaopBigsepHAeNHsKp63Uay31ibm-xQcri6i-ig9o79RoBQchBrBYfHaUDUMNOQ3-BrBY5gBYxgIHi6sKDUMNOQ3-BrBYxgIowrMuhUstdbm-sy9zwq3zpUs2QosJDoAJfbAUaosuPbm-X9foxQMz2gcV7guYxgIHi6seFbm-xyBY7g9o7Usti_iUDUSC5rRzwy3hSUstigcuMUiFiPMuarfB5QiUEUSyaUiFiyfhOrRzBrBY2gVYfHiUEHsKhH_UDUSCaq3oB9f-2gmUE6_1Fbm-SQfVD9fBjUstpwe?management_type=1&uid=16939915&t=ios&product_id=10967158340&sid=80e4ce6adc973f727d2c15e953e389f53bc076f515219678023eca413a126932f67a98475c3b5ebe57809619ae7a66c1f39581adc9dcd506e61d1af8f8179353548ab4016e7ca6279bf430492839b2ae&page=1&ob=0&dv=ios-2.253.0&r=https%3A%2F%2Fwww.tokopedia.com%2Fbeautyandgentle%2Fpromo-kiehls-clearly-corrective-dark-spot-solution-serum-4ml%3Fsrc%3Dtopads&src=recom_widget_best_seller_switching",
                      "trackerImageUrl": "https://ta.tokopedia.com/promo/v1/views/8a-2rOYDQfPwgcV7yZUEHZFiy3zwq3ei6sJRoAjFHAHR6mFiy3zwPIBFgZUEHZFircYpq9z2Qfdi6sHDUSz2Q31i6sJRHAJpH_1NHpjDUSCxPcKi6i-2Q3VSg9HXPcY0QRCBgcBxbMNBPmY2Q3r5yfVsqc15HpnFb9ohP3VagZY3r3-sQ175HsnaHa4dbpJ5HpHRHMy7g3JWHp1pom77HsjOb3UFHp1WysKfHfzjy_17gABBbM2FgaUDUMVj9RosQR-BUstp6AUXHsHaHsJ7o_n7H_jR6ZFirpowQcYSUstig9BGqMzUZMggQj2fgAo6QJBkQfB6q_zN_Bo-r7BW_sCsQABE3BPc8ujagfBvq1Bd_jH781YJu_oouVVN_7zuH7O119BvzVu2_JoGrB2yQAoiH7203Ao6qMUpZMh-qMY2_1o-r7BWo9giQ1NDZ325quja1926HjBd_32gPJOJe_u68_nF3jzjq7h9zMh68BB7_jPqQVBkZ92vuch23BPqQJB2PfBiH72F3s-DPuKa1MhszsBF3jo-ojBke3BHe72fyfODQMV9o3gsZJ2f3jx3qMzc69Cqe7jfZ32Cq1hAZSgsQ3hXyurOgMHaqIgszsBF3jo-ojBke3BHe72dgVP383u36_CgufzEZ325quxEZ9B6qBgM_1ydPOxEeMgy8j-M_1ydPOxEe3BHe72E3_UN8u2363BsQ_jpyp-uq1Y2Z92vzJBO_32o81Okz_C6uJJF_uz0H7Y_Z9o-QjNkysoGQVKp_Mhg3J2ky1o-ojBke3BM1_7YUiFiP9oBrBY2gmUEUsJf6_HN6_JOUiFirfuprfB5QBY2gmUEUsKFg_zsg_gxgcHNopoMopURgA-sH_uB6_1pg_Hd63yOHf-sHArfgs1ho_Uh6_yR6AnaHfusy_ehHfJhHsyNHp-MosPx6_K7opusHfUOg3-Bo_rdHAjfH_Bxg_PxosgsH3yp6_1dH3VjypBjyfeOHAgBosVjH3VM6cydH_rNHp1po_edy3U7HAJfg_Psy_yaopBigsepHAeNHsKp63Uay31ibm-xQcri6i-ig9o79RoBQchBrBYfHaUDUMNOQ3-BrBY5gBYxgIHi6sKDUMNOQ3-BrBYxgIowrMuhUstdbm-X9foxQMz2gcV7guYxgIHi6seFbm-xyBY7g9o7Usti_iUDUSC5rRzwy3hSUs2QUMzBgi-Pbm-fg9-pq3YXUstiPsUibm-sQIupPcua9fBj9RyaUsta6AJhHiFirI-2yfuwyMBjUstNo_nDUMP5y3hwq3ei6soY?dv=ios-2.253.0&sid=80e4ce6adc973f727d2c15e953e389f53bc076f515219678023eca413a126932f67a98475c3b5ebe57809619ae7a66c1f39581adc9dcd506e61d1af8f8179353548ab4016e7ca6279bf430492839b2ae&t=ios&management_type=1&page=1&ob=0&src=recom_widget_best_seller_switching",
                      "imageUrl": "https://images.tokopedia.net/img/cache/300-square/VqbcmM/2023/8/1/3372f4ea-3534-4295-b035-b863dda54d9e.jpg",
                      "url": "https://www.tokopedia.com/beautyandgentle/promo-kiehls-clearly-corrective-dark-spot-solution-serum-4ml?src=topads",
                      "price": "Rp74.900",
                      "priceInt": 74900,
                      "departmentId": 2265,
                      "badges": [
                        {
                          "title": "Power Merchant Pro",
                          "imageUrl": "https://images.tokopedia.net/img/goldmerchant/pm_activation/badge/Power%20Merchant%20Pro@2x.png"
                        }
                      ],
                      "freeOngkir": {
                        "isActive": true,
                        "imageUrl": "https://images.tokopedia.net/img/img/promo/targeting/red_bo_20k.png"
                      },
                      "rating": 5,
                      "ratingAverage": "5.0",
                      "countReview": 56,
                      "isTopads": true,
                      "categoryBreadcrumbs": "Kecantikan/Perawatan Wajah/Serum Wajah",
                      "recommendationType": "best_seller_v3",
                      "isWishlist": false,
                      "slashedPrice": "Rp120.000",
                      "discountPercentage": 38,
                      "shop": {
                        "id": 8125555,
                        "name": "Beauty&amp;Gentle",
                        "city": "Tangerang",
                        "location": "Tangerang"
                      },
                      "labelgroup": [
                        {
                          "title": "Cashback 2%",
                          "type": "lightGreen",
                          "position": "price",
                          "url": ""
                        },
                        {
                          "title": "Cashback",
                          "type": "lightGreen",
                          "position": "promo",
                          "url": ""
                        },
                        {
                          "title": "100+ terjual",
                          "type": "textDarkGrey",
                          "position": "integrity",
                          "url": ""
                        },
                        {
                          "title": "Terlaris",
                          "type": "#E1AA1D",
                          "position": "best_seller",
                          "url": ""
                        }
                      ],
                      "labelGroupVariant": [],
                      "minOrder": 0,
                      "maxOrder": 0,
                      "specificationLabels": [],
                      "warehouseID": 0
                    },
                    {
                      "gridPosition": "left",
                      "id": 331801813,
                      "name": "Skin1004 madagascar centella asitica 100 ampoule 100ml",
                      "parentID": 0,
                      "appUrl": "tokopedia://product/331801813?src=topads",
                      "clickUrl": "https://ta.tokopedia.com/promo/v1/clicks/8a-xgVY2gmUEH_Jf6AUFo_1abm-xgVY789CBUsthbm-FQRo2PcB5QiUEomFiPcBWgZUEH_rFH_Hho_jp6ZFiyRCsUstOo_nDUMVj9RosQR-BUstpopUXHsraoAJpoAe7opefbm-pHOYDQfri6i-B812kgJxGgBBXZSgjH7NDZ325q1OAoIBo8MHh_320HjY1z9BouJ1F_1zVHJNAZ9o-Q1dFyfFN8B29zSBgHMP2_fB-8JNAoAo6uVVR_OzS8jNke_-6uV1p_92sq1hAZS-q3cFpysoGqOKp_M2iH72DZ325q1OAZ9o-Q_ufyMO6QJBkQfBgHBVE_s--8JOk39zozJJO_SjFHV2JgcWHu7gD_S2gPJNI3MhgqjBE_OzDqu293Mh-q9P2ysoGrVtaQIuyHB-Dy7yNrV2AZ_g-qjV2_JoGPMoWQcNxupuMy7xGPB2UuM2jzsBF3jo-ojBke3BHe72fyfODQMV9o3gsHMxfy7yNrV2AZ_g-qjV2_JoG8cz9uSBBusjF3uPj8jBkQfBy8jBN_M23gjOc6IPy8j-M3I2mgjOc6IPy8jV2_JoG8Bja69BqusB2yf7NHfHau3Bvq1BE_S2-P1OkyRB6zJuE_jzzHJNE1_--q9P2yp-6PMoWuMgsHBgtyfO6Q7BkQfBoe7tNUiFiP9oBrBY2gmUEUsJf6_HN6_JOUiFirfuprfB5QBY2gmUEUsKFg_zsg_gxgcHNopoMopURgA-sH_uB6_1pg_Hd63yOHf-sHArfgs1ho_Uh6_yR6AnaHfusy_ehHfJhHsyNHp-MosPx6_K7opusHfUOg3-Bo_rdHAjfH_Bxg_PxosgsH3yp6_1dH3VjypBjyfeOHAgBosVjH3VM6cydH_rNHp1po_edy3U7HAJfg_Psy_yaopBigsepHAeNHsKp63Uay31ibm-xQcri6i-ig9o79RoBQchBrBYfHaUDUMNOQ3-BrBY5gBYxgIHi6sKDUMNOQ3-BrBYxgIowrMuhUstdbm-sy9zwq3zpUs2QosJDoAJfbAUaosuPbm-X9foxQMz2gcV7guYxgIHi6seFbm-xyBY7g9o7Usti_iUDUSC5rRzwy3hSUstigcuMUiFiPMuarfB5QiUEUSyaUiFiyfhOrRzBrBY2gVYfHiUEH_jhHseDUSCaq3oB9f-2gmUEo_1Fbm-SQfVD9fBjUstpwe?ob=0&t=ios&management_type=2&sid=80e4ce6adc973f727d2c15e953e389f53bc076f515219678023eca413a126932f67a98475c3b5ebe57809619ae7a66c1f39581adc9dcd506e61d1af8f8179353548ab4016e7ca6279bf430492839b2ae&src=recom_widget_best_seller_switching&page=1&dv=ios-2.253.0&r=https%3A%2F%2Fwww.tokopedia.com%2Fvenuss8%2Fskin1004-madagascar-centella-asitica-100-ampoule-100ml%3Fsrc%3Dtopads",
                      "wishlistUrl": "https://ta.tokopedia.com/promo/v1/wishlists/8a-xgVY2gmUEH_Jf6AUFo_1abm-xgVY789CBUsthbm-FQRo2PcB5QiUEomFiPcBWgZUEH_rFH_Hho_jp6ZFiyRCsUstOo_nDUMVj9RosQR-BUstpopUXHsraoAJpoAe7opefbm-pHOYDQfri6i-B812kgJxGgBBXZSgjH7NDZ325q1OAoIBo8MHh_320HjY1z9BouJ1F_1zVHJNAZ9o-Q1dFyfFN8B29zSBgHMP2_fB-8JNAoAo6uVVR_OzS8jNke_-6uV1p_92sq1hAZS-q3cFpysoGqOKp_M2iH72DZ325q1OAZ9o-Q_ufyMO6QJBkQfBgHBVE_s--8JOk39zozJJO_SjFHV2JgcWHu7gD_S2gPJNI3MhgqjBE_OzDqu293Mh-q9P2ysoGrVtaQIuyHB-Dy7yNrV2AZ_g-qjV2_JoGPMoWQcNxupuMy7xGPB2UuM2jzsBF3jo-ojBke3BHe72fyfODQMV9o3gsHMxfy7yNrV2AZ_g-qjV2_JoG8cz9uSBBusjF3uPj8jBkQfBy8jBN_M23gjOc6IPy8j-M3I2mgjOc6IPy8jV2_JoG8Bja69BqusB2yf7NHfHau3Bvq1BE_S2-P1OkyRB6zJuE_jzzHJNE1_--q9P2yp-6PMoWuMgsHBgtyfO6Q7BkQfBoe7tNUiFiP9oBrBY2gmUEUsJf6_HN6_JOUiFirfuprfB5QBY2gmUEUsKFg_zsg_gxgcHNopoMopURgA-sH_uB6_1pg_Hd63yOHf-sHArfgs1ho_Uh6_yR6AnaHfusy_ehHfJhHsyNHp-MosPx6_K7opusHfUOg3-Bo_rdHAjfH_Bxg_PxosgsH3yp6_1dH3VjypBjyfeOHAgBosVjH3VM6cydH_rNHp1po_edy3U7HAJfg_Psy_yaopBigsepHAeNHsKp63Uay31ibm-xQcri6i-ig9o79RoBQchBrBYfHaUDUMNOQ3-BrBY5gBYxgIHi6sKDUMNOQ3-BrBYxgIowrMuhUstdbm-sy9zwq3zpUs2QosJDoAJfbAUaosuPbm-X9foxQMz2gcV7guYxgIHi6seFbm-xyBY7g9o7Usti_iUDUSC5rRzwy3hSUstigcuMUiFiPMuarfB5QiUEUSyaUiFiyfhOrRzBrBY2gVYfHiUEH_jhHseDUSCaq3oB9f-2gmUEo_1Fbm-SQfVD9fBjUstpwe?page=1&ob=0&t=ios&dv=ios-2.253.0&r=https%3A%2F%2Fwww.tokopedia.com%2Fvenuss8%2Fskin1004-madagascar-centella-asitica-100-ampoule-100ml%3Fsrc%3Dtopads&uid=16939915&product_id=331801813&sid=80e4ce6adc973f727d2c15e953e389f53bc076f515219678023eca413a126932f67a98475c3b5ebe57809619ae7a66c1f39581adc9dcd506e61d1af8f8179353548ab4016e7ca6279bf430492839b2ae&src=recom_widget_best_seller_switching&management_type=2",
                      "trackerImageUrl": "https://ta.tokopedia.com/promo/v1/views/8a-2rOYDQfPwgcV7yZUEHZFiy3zwq3ei6sJhosKaHA1OHiFiy3zwPIBFgZUEHZFircYpq9z2Qfdi6seDUSz2Q31i6sJRHAJpH_1NHpjDUSCxPcKi6i-2Q3VSg9HXPcY0QRCBgcBxbMNBPmY2Q3r5yfVsqc15HpnFb9ohP3VagZYxPIzxyfxWg3N7bpUFH_j5H_U5Hsn5H_1RosKhH_eFHs1FH_rObpJOopydH_J7HAUOHAJRou4RHpURypedyZOBoAndb_zxoMJW6cHN6m77y_ypgMeRy31R6_JXrcNSUiFiy3zwrfo5rM1i6sHRHidaopU7H_H7oAeRoAyDUSHp9fh5gaUEUMuNZM2jZJ2M33NGPMep_Mh-qMY2_1H781OEypVoqMDa_OzV81O1z_CozJ1F_jo-r7BW_sCsQABE3BPc8ujagfBvq1Bd_jH7H7N119PvucPE_M2CHjN1u_oo8Mo2_JoGrB2yQAoiH7203Ao6qMUpZMh-qMY2_1o-r7BWo9giQ1NDZ325quja1926HjBd_32gPJOJe_u68_nF3jzjq7h9zMh68BB7_jPqQVBkZ92vuch23BPqQJB2PfBiH72F3s-DPuKa1MhszsBF3jo-ojBke3BHe72fyfODQMV9o3gsZJ2f3jx3qMzc69Cqe7jfZ32Cq1hAZSgsQ3hXyurOgMHaqIgszsBF3jo-ojBke3BHe72dgVP383u36_CgufzEZ325quxEZ9B6qBgM_1ydPOxEeMgy8j-M_1ydPOxEe3BHe72E3_UN8u2363BsQ_jpyp-uq1Y2Z9268jBO_32s81NJz926zVJF_S2zHjB2PfBsHjNfyfO3gMHauMxsQ1N5Z325q1OAZsjibm-Orfua9fBjUstiH_yNHpjNH_1ibm-pg9opq3YX9fBjUsti6ACBocoBoMVjypjRHfyRHsPjHMHho31No_oBHpKNgs1pyMHFopgMo_JOHsJNosrdHAUpg3oxoAJpy_JaosjpHMyfofJN6AeRo3HpysuByM1OopKF6_yh63VBofJfoMHhgsHNo_Khy3zs63zsgA1FoM1fH3ehy3ydgsKhopjpo_HOoAxxyseFH_gBofoxosUR63-MoAHFoAja6AHNys-xgZUDUMVDgaUEUM-BrRzwrfuDQcua9RypUiFiQSuWyMua9fYM9fVjraUE6mFiQSuWyMua9fVjrOYag9Ji6sKDUMNwyfVXgcBjy9zB9fVjraUEoAnDUMVi9RzBrRei6i-6UiFircYpPVYxQcri6BDigcuMUB7DUSgBrSo2Qfdi6i-fHiUDUMoDP9o7g9-wq3zwPsUi6sJNH_U7bm-FrMBsguYiq3ei6s1OHmFigfYxQVY2gmUEHR7?management_type=2&src=recom_widget_best_seller_switching&page=1&t=ios&dv=ios-2.253.0&sid=80e4ce6adc973f727d2c15e953e389f53bc076f515219678023eca413a126932f67a98475c3b5ebe57809619ae7a66c1f39581adc9dcd506e61d1af8f8179353548ab4016e7ca6279bf430492839b2ae&ob=0",
                      "imageUrl": "https://images.tokopedia.net/img/cache/300-square/attachment/2019/12/20/157681140250175/157681140250175_7327c48a-e408-4a6a-8c98-4a63fd7ae791.png",
                      "url": "https://www.tokopedia.com/venuss8/skin1004-madagascar-centella-asitica-100-ampoule-100ml?src=topads",
                      "price": "Rp172.500",
                      "priceInt": 172500,
                      "departmentId": 2265,
                      "badges": [
                        {
                          "title": "Power Merchant Pro",
                          "imageUrl": "https://images.tokopedia.net/img/goldmerchant/pm_activation/badge/Power%20Merchant%20Pro@2x.png"
                        }
                      ],
                      "freeOngkir": {
                        "isActive": true,
                        "imageUrl": "https://images.tokopedia.net/img/img/promo/targeting/red_bo_20k.png"
                      },
                      "rating": 5,
                      "ratingAverage": "5.0",
                      "countReview": 742,
                      "isTopads": true,
                      "categoryBreadcrumbs": "Kecantikan/Perawatan Wajah/Serum Wajah",
                      "recommendationType": "best_seller_v3",
                      "isWishlist": false,
                      "slashedPrice": "Rp252.000",
                      "discountPercentage": 32,
                      "shop": {
                        "id": 643199,
                        "name": "Venuss8",
                        "city": "Jakarta Barat",
                        "location": "Jakarta Barat"
                      },
                      "labelgroup": [
                        {
                          "title": "Cashback 4%",
                          "type": "lightGreen",
                          "position": "price",
                          "url": ""
                        },
                        {
                          "title": "Cashback",
                          "type": "lightGreen",
                          "position": "promo",
                          "url": ""
                        },
                        {
                          "title": "1 rb+ terjual",
                          "type": "textDarkGrey",
                          "position": "integrity",
                          "url": ""
                        },
                        {
                          "title": "Inspirasi Belanja",
                          "type": "",
                          "position": "campaign",
                          "url": "https://images.tokopedia.net/img/phOWBv/2023/10/3/309c4ff3-f259-4972-b826-b0ce4307e167.png"
                        },
                        {
                          "title": "Terlaris",
                          "type": "#E1AA1D",
                          "position": "best_seller",
                          "url": ""
                        }
                      ],
                      "labelGroupVariant": [],
                      "minOrder": 0,
                      "maxOrder": 0,
                      "specificationLabels": [],
                      "warehouseID": 0
                    },
                    {
                      "gridPosition": "top_right",
                      "id": 12038949089,
                      "name": "L'Oreal Paris Revitalift Crystal Micro Essence Water Serum Skin Care",
                      "parentID": 0,
                      "appUrl": "tokopedia://product/12038949089?src=topads",
                      "clickUrl": "https://ta.tokopedia.com/promo/v1/clicks/8a-xgVY2gmUEopKpH_eposJDUMVj9RzNrc1i6sJDUSC5rfB7q3YXUstObm-7q3OBUsthopnhHpJO6_HNbm-srcHi6sHOHAnDUMVj9RosQR-BUstpopJXH_e7Hpepo_raoAy76_1DUSHp9fh5gaUEUMuNZM2jZJ2M33NGPMep_Mh-qMY2_1o-r7BW_sCsQABE3BPc8ujagfBvq1BN_1o-r7BWPchB3czfyfOZgMHa_SgsQuu2_fB-P7B2PfBiQ_BO3_-uq1Y2ZM2qzJ7p332V81N2HIPozcDp_VzZq7da19zguO1p_MjFHV2WuMBoqj7O_OPGQV2Wu3BHe72fyfODQMV9o3gqzOgR3A-Dq7BkQfBoe7BpZ37N83V9gICiQABRyf7Nqfz9_sCyHMh0Z325q1OAZ9o-Q_BNyuPjrc-D692xzpBR3A-Dq7BkQfBoe7BpZ3NcHu2yZsuyHO-t3sooq1Y2ZMgoqjja_BydPOxEeMgozsBM_1ydPOxEeMgoe7BpZ3N6qMUpZMhyHj2Nysoj8B2_Z_g-qj7p_uH78JNJ1926zJ7h_S2-HJNk1_u617BpZ3N6qMUpZMhyH7ND3uxGqMVAZ_g-qjV2gBJYvZUDUSupg9-wq3ei6iUhosjp6_jhoZUDUSoBrRo2QfNwq3ei6iUdHc17yf1fy3zs6_rpgsraofeaypJOg_jOHf1p6ABMo_oiypnRoMyOH_1aH_jfopKFHsoByfJ7H_oxH_Uf6_HagsyRy_jdoArOypoio3uig_1R6AnNosJNy31Ry_yfypVMHpjO6AVxgcHNgcojo_nfg_yhgAVxgsxM6AJR6_HOHp176cVioAnhoM1RyfJfHsrNyMy7Hpn76_UdHpBiHMVBUiFiy3hSUstiyMupPVYpg3hDg9-wPsHibm-XP3Oig9-wQfgwy3zpUstdbm-XP3Oig9-wy3zp9R-BrZUE6mFiyfV79fBjraUE3pyhbAehoiFaHsyO9ZFiQBYsy3Njq3zxPcuwy3zpUst7HmFiy3-wPcupPmUEUjdibm-FQRo79fVDgaUEUMzBgiUDUSgBrSo2Qfdi6i-fHiUDUMoDP9o7g9-wq3zwPsUi6sHOH_epbm-FrMBsguYiq3ei6sHOHAnDUMP5y3hwq3ei6soY?r=https%3A%2F%2Fwww.tokopedia.com%2Fdears%2Fl-oreal-paris-revitalift-crystal-micro-essence-water-serum-skin-care-65ml-regular-edition-b228e%3Fsrc%3Dtopads&src=recom_widget_best_seller_switching&page=1&dv=ios-2.253.0&management_type=1&t=ios&sid=80e4ce6adc973f727d2c15e953e389f53bc076f515219678023eca413a126932f67a98475c3b5ebe57809619ae7a66c1f39581adc9dcd506e61d1af8f8179353548ab4016e7ca6279bf430492839b2ae&ob=0",
                      "wishlistUrl": "https://ta.tokopedia.com/promo/v1/wishlists/8a-xgVY2gmUEopKpH_eposJDUMVj9RzNrc1i6sJDUSC5rfB7q3YXUstObm-7q3OBUsthopnhHpJO6_HNbm-srcHi6sHOHAnDUMVj9RosQR-BUstpopJXH_e7Hpepo_raoAy76_1DUSHp9fh5gaUEUMuNZM2jZJ2M33NGPMep_Mh-qMY2_1o-r7BW_sCsQABE3BPc8ujagfBvq1BN_1o-r7BWPchB3czfyfOZgMHa_SgsQuu2_fB-P7B2PfBiQ_BO3_-uq1Y2ZM2qzJ7p332V81N2HIPozcDp_VzZq7da19zguO1p_MjFHV2WuMBoqj7O_OPGQV2Wu3BHe72fyfODQMV9o3gqzOgR3A-Dq7BkQfBoe7BpZ37N83V9gICiQABRyf7Nqfz9_sCyHMh0Z325q1OAZ9o-Q_BNyuPjrc-D692xzpBR3A-Dq7BkQfBoe7BpZ3NcHu2yZsuyHO-t3sooq1Y2ZMgoqjja_BydPOxEeMgozsBM_1ydPOxEeMgoe7BpZ3N6qMUpZMhyHj2Nysoj8B2_Z_g-qj7p_uH78JNJ1926zJ7h_S2-HJNk1_u617BpZ3N6qMUpZMhyH7ND3uxGqMVAZ_g-qjV2gBJYvZUDUSupg9-wq3ei6iUhosjp6_jhoZUDUSoBrRo2QfNwq3ei6iUdHc17yf1fy3zs6_rpgsraofeaypJOg_jOHf1p6ABMo_oiypnRoMyOH_1aH_jfopKFHsoByfJ7H_oxH_Uf6_HagsyRy_jdoArOypoio3uig_1R6AnNosJNy31Ry_yfypVMHpjO6AVxgcHNgcojo_nfg_yhgAVxgsxM6AJR6_HOHp176cVioAnhoM1RyfJfHsrNyMy7Hpn76_UdHpBiHMVBUiFiy3hSUstiyMupPVYpg3hDg9-wPsHibm-XP3Oig9-wQfgwy3zpUstdbm-XP3Oig9-wy3zp9R-BrZUE6mFiyfV79fBjraUE3pyhbAehoiFaHsyO9ZFiQBYsy3Njq3zxPcuwy3zpUst7HmFiy3-wPcupPmUEUjdibm-FQRo79fVDgaUEUMzBgiUDUSgBrSo2Qfdi6i-fHiUDUMoDP9o7g9-wq3zwPsUi6sHOH_epbm-FrMBsguYiq3ei6sHOHAnDUMP5y3hwq3ei6soY?dv=ios-2.253.0&product_id=12038949089&t=ios&uid=16939915&ob=0&sid=80e4ce6adc973f727d2c15e953e389f53bc076f515219678023eca413a126932f67a98475c3b5ebe57809619ae7a66c1f39581adc9dcd506e61d1af8f8179353548ab4016e7ca6279bf430492839b2ae&r=https%3A%2F%2Fwww.tokopedia.com%2Fdears%2Fl-oreal-paris-revitalift-crystal-micro-essence-water-serum-skin-care-65ml-regular-edition-b228e%3Fsrc%3Dtopads&src=recom_widget_best_seller_switching&page=1&management_type=1",
                      "trackerImageUrl": "https://ta.tokopedia.com/promo/v1/views/8a-2rOYDQfPwgcV7yZUEHZFiy3zwq3ei6srdHpJ7Hpyhbm-xgVY789CBUsthbm-FQRo2PcB5QiUEoZFiPcBWgZUEH_rFH_Hho_jp6ZFircV7qmUEUMBWy3PBraN7QfW5rcujq3JXQMu7bfBWgaYsy3otgZ4pHAnWrRVOy9-BbOghyMoW_Z4aHAUpbpj5Hs15o_oiH3gx6cUWH_nfya77o_gxb3VMHpnWg_UN6A-xgseNopJhbM2FgaUDUMVj9RosQR-BUstpopJXH_e7Hpepo_raoAy76_1DUSHp9fh5gaUEUMuNZM2jZJ2M33NGPMep_Mh-qMY2_1o-r7BW_sCsQABE3BPc8ujagfBvq1BN_1o-r7BWPchB3czfyfOZgMHa_SgsQuu2_fB-P7B2PfBiQ_BO3_-uq1Y2ZM2qzJ7p332V81N2HIPozcDp_VzZq7da19zguO1p_MjFHV2WuMBoqj7O_OPGQV2Wu3BHe72fyfODQMV9o3gqzOgR3A-Dq7BkQfBoe7BpZ37N83V9gICiQABRyf7Nqfz9_sCyHMh0Z325q1OAZ9o-Q_BNyuPjrc-D692xzpBR3A-Dq7BkQfBoe7BpZ3NcHu2yZsuyHO-t3sooq1Y2ZMgoqjja_BydPOxEeMgozsBM_1ydPOxEeMgoe7BpZ3N6qMUpZMhyHj2Nysoj8B2_Z_g-qj7p_uH78JNJ1926zJ7h_S2-HJNk1_u617BpZ3N6qMUpZMhyH7ND3uxGqMVAZ_g-qjV2gBJYvZUDUSupg9-wq3ei6iUhosjp6_jhoZUDUSoBrRo2QfNwq3ei6iUdHc17yf1fy3zs6_rpgsraofeaypJOg_jOHf1p6ABMo_oiypnRoMyOH_1aH_jfopKFHsoByfJ7H_oxH_Uf6_HagsyRy_jdoArOypoio3uig_1R6AnNosJNy31Ry_yfypVMHpjO6AVxgcHNgcojo_nfg_yhgAVxgsxM6AJR6_HOHp176cVioAnhoM1RyfJfHsrNyMy7Hpn76_UdHpBiHMVBUiFiy3hSUstiyMupPVYpg3hDg9-wPsHibm-XP3Oig9-wQfgwy3zpUstdbm-XP3Oig9-wy3zp9R-BrZUE6mFiQBYsy3Njq3zxPcuwy3zpUst7HmFiy3-wPcupPmUEUjdibm-FQRo79fVDgaUE3a-jg3yi9ZFiPMuarfB5QiUEUSyaUiFiyfhOrRzBrBY2gVYfHiUEHp1hoAHDUSCaq3oB9f-2gmUEHp1FHmFigfYxQVY2gmUEHR7?sid=80e4ce6adc973f727d2c15e953e389f53bc076f515219678023eca413a126932f67a98475c3b5ebe57809619ae7a66c1f39581adc9dcd506e61d1af8f8179353548ab4016e7ca6279bf430492839b2ae&page=1&dv=ios-2.253.0&src=recom_widget_best_seller_switching&ob=0&t=ios&management_type=1",
                      "imageUrl": "https://images.tokopedia.net/img/cache/300-square/VqbcmM/2023/9/25/53b1fa8b-106c-456a-af30-e2982af49711.jpg",
                      "url": "https://www.tokopedia.com/dears/l-oreal-paris-revitalift-crystal-micro-essence-water-serum-skin-care-65ml-regular-edition-b228e?src=topads",
                      "price": "Rp105.000",
                      "priceInt": 105000,
                      "departmentId": 2265,
                      "badges": [
                        {
                          "title": "Power Merchant Pro",
                          "imageUrl": "https://images.tokopedia.net/img/goldmerchant/pm_activation/badge/Power%20Merchant%20Pro@2x.png"
                        }
                      ],
                      "freeOngkir": {
                        "isActive": true,
                        "imageUrl": "https://images.tokopedia.net/img/img/promo/targeting/red_bo_20k.png"
                      },
                      "rating": 5,
                      "ratingAverage": "5.0",
                      "countReview": 68,
                      "isTopads": true,
                      "categoryBreadcrumbs": "Kecantikan/Perawatan Wajah/Serum Wajah",
                      "recommendationType": "best_seller_v3",
                      "isWishlist": false,
                      "slashedPrice": "",
                      "discountPercentage": 0,
                      "shop": {
                        "id": 680836,
                        "name": "desiree",
                        "city": "Jakarta Pusat",
                        "location": "Jakarta Pusat"
                      },
                      "labelgroup": [
                        {
                          "title": "Cashback 15%",
                          "type": "lightGreen",
                          "position": "price",
                          "url": ""
                        },
                        {
                          "title": "Cashback",
                          "type": "lightGreen",
                          "position": "promo",
                          "url": ""
                        },
                        {
                          "title": "100+ terjual",
                          "type": "textDarkGrey",
                          "position": "integrity",
                          "url": ""
                        },
                        {
                          "title": "Produk Terbaru",
                          "type": "textDarkOrange",
                          "position": "gimmick",
                          "url": ""
                        },
                        {
                          "title": "Terlaris",
                          "type": "#E1AA1D",
                          "position": "best_seller",
                          "url": ""
                        }
                      ],
                      "labelGroupVariant": [],
                      "minOrder": 0,
                      "maxOrder": 0,
                      "specificationLabels": [],
                      "warehouseID": 0
                    },
                    {
                      "gridPosition": "bottom_right",
                      "id": 10378775776,
                      "name": "AVOSKIN YOUR SKIN BAE SERUM SERIES - Salicylic Acid",
                      "parentID": 0,
                      "appUrl": "tokopedia://product/10378775776?src=topads",
                      "clickUrl": "https://ta.tokopedia.com/promo/v1/clicks/8a-xgVY2gmUEH_jfo_K7H_1pbm-xgVY789CBUsthbm-FQRo2PcB5QiUEoiFiPcBWgZUEH_rFH_Hho_jp6ZFiyRCsUsthHpnFbm-xgVYpyfYagZUEHpyNbsK7opyaHpedH_jaH_HDUSHp9fh5gaUEUMuNZM2jZJ2M33NGPMep_Mh-qMY2_uH7H7NJgRB6uJ1p_jzS81N1z_o6zcPEZ3BRqujp1SByH7ND3uxGqMVAZ_g-qBuO_M2S8JYJz_zoucPd_7zVoJO1gRB-q9P2y_-3o3ea69BqzsBE3_UN8u2_Z_g-qjV2_JoGP3Uao32q17jfZ3O6q7OEgcBouJja_VzCP7Y1yRz6zOJp3jHhqV21yp-HuV-W3BP-81OEqpugQugW3Bo-r7BW69BxufzFyMFNqO2yeMgxuOV2_fB-P7B2PfBiH72F3s-DPuKpeSBiHBUh3_oZgMV913Bvq1BRZ3BRq3UpZSCqHMhO3Ao6QfUpeMgxuOV2_fB-P7B2PfBs3VgDyfNDgMzIzMNs81jfZ3Fd81Ok3_Vy8j-M_1ydPOKh6IPy8j-M_1ydP7B2PfBsHjNfyfO3gBBXZSgjH7NDZ325q1OE3_uHqMrF_S2g81OE1_zoucWN_uzoq1hAZS2gHsBN3ByN8B29zSBgHMP2_fB-P7BXHA7ibm-Orfua9fBjUstiH_yNHpjNH_1ibm-pg9opq3YX9fBjUsti6ACBocoBoMVjypjRHfyRHsPjHMHho31No_oBHpKNgs1pyMHFopgMo_JOHsJNosrdHAUpg3oxoAJpy_JaosjpHMyfofJN6AeRo3HpysuByM1OopKF6_yh63VBofJfoMHhgsHNo_Khy3zs63zsgA1FoM1fH3ehy3ydgsKhopjpo_HOoAxxyseFH_gBofoxosUR63-MoAHFoAja6AHNys-xgZUDUMVDgaUEUM-BrRzwrfuDQcua9RypUiFiQSuWyMua9fYM9fVjraUE6mFiQSuWyMua9fVjrOYag9Ji6sKDUMoxPVY2gIHi6BDfHZF7H_yDHsUfou7DUMNwyfVXgcBjy9zB9fVjraUEoAnDUMVi9RzBrRei6i-6UiFircYpPVYxQcri6i-jg3yibm-fg9-pq3YXUstiPsUibm-sQIupPcua9fBj9RyaUsthHAH7HiFirI-2yfuwyMBjUsthHpnFbm-SQfVD9fBjUstpwe?r=https%3A%2F%2Fwww.tokopedia.com%2Favoskin-jakarta%2Favoskin-your-skin-bae-serum-series-salicylic-acid-008be%3Fsrc%3Dtopads&t=ios&page=1&management_type=1&sid=80e4ce6adc973f727d2c15e953e389f53bc076f515219678023eca413a126932f67a98475c3b5ebe57809619ae7a66c1f39581adc9dcd506e61d1af8f8179353548ab4016e7ca6279bf430492839b2ae&dv=ios-2.253.0&src=recom_widget_best_seller_switching&ob=0",
                      "wishlistUrl": "https://ta.tokopedia.com/promo/v1/wishlists/8a-xgVY2gmUEH_jfo_K7H_1pbm-xgVY789CBUsthbm-FQRo2PcB5QiUEoiFiPcBWgZUEH_rFH_Hho_jp6ZFiyRCsUsthHpnFbm-xgVYpyfYagZUEHpyNbsK7opyaHpedH_jaH_HDUSHp9fh5gaUEUMuNZM2jZJ2M33NGPMep_Mh-qMY2_uH7H7NJgRB6uJ1p_jzS81N1z_o6zcPEZ3BRqujp1SByH7ND3uxGqMVAZ_g-qBuO_M2S8JYJz_zoucPd_7zVoJO1gRB-q9P2y_-3o3ea69BqzsBE3_UN8u2_Z_g-qjV2_JoGP3Uao32q17jfZ3O6q7OEgcBouJja_VzCP7Y1yRz6zOJp3jHhqV21yp-HuV-W3BP-81OEqpugQugW3Bo-r7BW69BxufzFyMFNqO2yeMgxuOV2_fB-P7B2PfBiH72F3s-DPuKpeSBiHBUh3_oZgMV913Bvq1BRZ3BRq3UpZSCqHMhO3Ao6QfUpeMgxuOV2_fB-P7B2PfBs3VgDyfNDgMzIzMNs81jfZ3Fd81Ok3_Vy8j-M_1ydPOKh6IPy8j-M_1ydP7B2PfBsHjNfyfO3gBBXZSgjH7NDZ325q1OE3_uHqMrF_S2g81OE1_zoucWN_uzoq1hAZS2gHsBN3ByN8B29zSBgHMP2_fB-P7BXHA7ibm-Orfua9fBjUstiH_yNHpjNH_1ibm-pg9opq3YX9fBjUsti6ACBocoBoMVjypjRHfyRHsPjHMHho31No_oBHpKNgs1pyMHFopgMo_JOHsJNosrdHAUpg3oxoAJpy_JaosjpHMyfofJN6AeRo3HpysuByM1OopKF6_yh63VBofJfoMHhgsHNo_Khy3zs63zsgA1FoM1fH3ehy3ydgsKhopjpo_HOoAxxyseFH_gBofoxosUR63-MoAHFoAja6AHNys-xgZUDUMVDgaUEUM-BrRzwrfuDQcua9RypUiFiQSuWyMua9fYM9fVjraUE6mFiQSuWyMua9fVjrOYag9Ji6sKDUMoxPVY2gIHi6BDfHZF7H_yDHsUfou7DUMNwyfVXgcBjy9zB9fVjraUEoAnDUMVi9RzBrRei6i-6UiFircYpPVYxQcri6i-jg3yibm-fg9-pq3YXUstiPsUibm-sQIupPcua9fBj9RyaUsthHAH7HiFirI-2yfuwyMBjUsthHpnFbm-SQfVD9fBjUstpwe?sid=80e4ce6adc973f727d2c15e953e389f53bc076f515219678023eca413a126932f67a98475c3b5ebe57809619ae7a66c1f39581adc9dcd506e61d1af8f8179353548ab4016e7ca6279bf430492839b2ae&t=ios&src=recom_widget_best_seller_switching&page=1&ob=0&product_id=10378775776&dv=ios-2.253.0&uid=16939915&r=https%3A%2F%2Fwww.tokopedia.com%2Favoskin-jakarta%2Favoskin-your-skin-bae-serum-series-salicylic-acid-008be%3Fsrc%3Dtopads&management_type=1",
                      "trackerImageUrl": "https://ta.tokopedia.com/promo/v1/views/8a-2rOYDQfPwgcV7yZUEHZFiy3zwq3ei6sJNos1doAJOHaFiy3zwPIBFgZUEHZFircYpq9z2Qfdi6syDUSz2Q31i6sJRHAJpH_1NHpjDUSCxPcKi6i-2Q3VSg9HXPcY0QRCBgcBxbMNBPmY2Q3r5yfVsqc15HpnFb9ohP3VagZY3r3-sQ175HsnaHa4Nbpr5y3oxHfyfHsjWoAHFoZ776c-Mb_xBHcHWyMH7osnRHpoioMoibM2FgaUDUMVj9RosQR-BUstposjX6AeRosUpoAKh6_UhHaFirpowQcYSUstig9BGqMzUZMggQj2fgAo6QJBkQfBo1pep_jzS81N1z_o6zcPN_BzVH7NJgR2-q9P23_oZ8uKp_Mhg3J2ky1o-ojBku9u6qMPd_7zVoJO1gRxvzJ17_uzS81B2PfBxHByOgAUN8u2c692gHsBN3Bo-ojBke3BHe72OysUOqB2_Z_g-Q1N0_92jq1O1Z_-HuJVR_OzsPJNI1_oqepVt3BzsHjh11MOqu7BN_920ouBWuMOq17BpZ37N83V9gICiQAB03BxmgMV913Bvq1BRZ3BRq3UpZSCqHMhO3Aom83Ua1sVgHO-MyuPzq1Y2Z9P-q9P2ysoGrVtaQIuyH7N5ysomgMV913Bvq1BRZ3BRq3oyuMhsQMhMgJPcQMoNZ_g-QAxN_32gHuxEeMgozsxR3AJdPOxEeMgozsxRZ3BRq3Ha_SgsQugM33NGPMep_Mh-qMY2_92go1hkgpC68BBN_92zoJO1qRBouJO2_JoG8Bja69BqusBE3BPc8ujagfBvq1BRZ3dFvZUDUSupg9-wq3ei6iUhosjp6_jhoZUDUSoBrRo2QfNwq3ei6iUdHc17yf1fy3zs6_rpgsraofeaypJOg_jOHf1p6ABMo_oiypnRoMyOH_1aH_jfopKFHsoByfJ7H_oxH_Uf6_HagsyRy_jdoArOypoio3uig_1R6AnNosJNy31Ry_yfypVMHpjO6AVxgcHNgcojo_nfg_yhgAVxgsxM6AJR6_HOHp176cVioAnhoM1RyfJfHsrNyMy7Hpn76_UdHpBiHMVBUiFiy3hSUstiyMupPVYpg3hDg9-wPsHibm-XP3Oig9-wQfgwy3zpUstdbm-XP3Oig9-wy3zp9R-BrZUE6mFiQBYsy3Njq3zxPcuwy3zpUst7HmFiy3-wPcupPmUEUjdibm-FQRo79fVDgaUE3a-jg3yi9ZFiPMuarfB5QiUEUSyaUiFiyfhOrRzBrBY2gVYfHiUEH_npoAUDUSCaq3oB9f-2gmUEH_HFHmFigfYxQVY2gmUEHR7?management_type=1&sid=80e4ce6adc973f727d2c15e953e389f53bc076f515219678023eca413a126932f67a98475c3b5ebe57809619ae7a66c1f39581adc9dcd506e61d1af8f8179353548ab4016e7ca6279bf430492839b2ae&src=recom_widget_best_seller_switching&page=1&ob=0&dv=ios-2.253.0&t=ios",
                      "imageUrl": "https://images.tokopedia.net/img/cache/300-square/VqbcmM/2023/9/7/aca3f629-4305-48bf-8e0c-bc460733b6cb.jpg",
                      "url": "https://www.tokopedia.com/avoskin-jakarta/avoskin-your-skin-bae-serum-series-salicylic-acid-008be?src=topads",
                      "price": "Rp97.300",
                      "priceInt": 97300,
                      "departmentId": 2265,
                      "badges": [
                        {
                          "title": "Power Merchant Pro",
                          "imageUrl": "https://images.tokopedia.net/img/goldmerchant/pm_activation/badge/Power%20Merchant%20Pro@2x.png"
                        }
                      ],
                      "freeOngkir": {
                        "isActive": true,
                        "imageUrl": "https://images.tokopedia.net/img/img/promo/targeting/red_bo_20k.png"
                      },
                      "rating": 5,
                      "ratingAverage": "5.0",
                      "countReview": 88,
                      "isTopads": true,
                      "categoryBreadcrumbs": "Kecantikan/Perawatan Wajah/Serum Wajah",
                      "recommendationType": "best_seller_v3",
                      "isWishlist": false,
                      "slashedPrice": "Rp139.000",
                      "discountPercentage": 30,
                      "shop": {
                        "id": 548390,
                        "name": "AVOSKIN BEAUTY JAKARTA",
                        "city": "Jakarta Selatan",
                        "location": "Jakarta Selatan"
                      },
                      "labelgroup": [
                        {
                          "title": "Cashback 6%",
                          "type": "lightGreen",
                          "position": "price",
                          "url": ""
                        },
                        {
                          "title": "Cashback",
                          "type": "lightGreen",
                          "position": "promo",
                          "url": ""
                        },
                        {
                          "title": "250+ terjual",
                          "type": "textDarkGrey",
                          "position": "integrity",
                          "url": ""
                        },
                        {
                          "title": "Inspirasi Belanja",
                          "type": "",
                          "position": "campaign",
                          "url": "https://images.tokopedia.net/img/phOWBv/2023/9/21/fb662591-ddae-474e-9b91-bf83b2fd9aea.png"
                        },
                        {
                          "title": "Terlaris",
                          "type": "#E1AA1D",
                          "position": "best_seller",
                          "url": ""
                        }
                      ],
                      "labelGroupVariant": [],
                      "minOrder": 0,
                      "maxOrder": 0,
                      "specificationLabels": [],
                      "warehouseID": 0
                    },
                    {
                      "gridPosition": "left",
                      "id": 10187278640,
                      "name": "Pigeon Teens Serum",
                      "parentID": 0,
                      "appUrl": "tokopedia://product/10187278640?src=topads",
                      "clickUrl": "https://ta.tokopedia.com/promo/v1/clicks/8a-xgVY2gmUEH_jfos1hHsrfbm-xgVY789CBUsthbm-FQRo2PcB5QiUEoaFiPcBWgZUEH_rFH_Hho_jp6ZFiyRCsUstOHAnDUMVj9RosQR-BUstposjXoseR6ArO6ArhopnfHiFirpowQcYSUstig9BGqMzUZMggQj2fgAo6QJBkQfBoe7BpZ3O6HcoD692qu7gN3_-Sq1Y2Z9P-q9P2y_-3o3ea69BqzsBE3_UN8u2_Z_g-qjV2_JoGP3Uao32q17jfZ3O6q7OEgcBouJja_VzCP7Y1yRz6zOJp3jHhqV21yp-HuV-W3BP-81OEqpugQugW3Bo-r7BW69BxufzFyMFNqO2yeMgxuOV2_fB-P7B2PfBiH72F3s-DPuKpeSBiHBUh3_oZgMV913Bvq1BRZ3BRq3UpZSCqHMhO3Ao6QfUpeMgxuOV2_fB-P7B2PfBs3VgDyfNDgMzIzMNs81jfZ3Fd81Ok3_Vy8j-M_1ydPOKh6IPy8j-M_1ydP7B2PfBsHjNfyfO3gBBXZSgjH7NDZ325q1OE3_uHqBjF_S2SH7N1gpooucoR_M2-q1hAZS2gHsBN3ByN8B29zSBgHMP2_fB-P7BXHA7ibm-Orfua9fBjUstiH_yNHpjNH_1ibm-pg9opq3YX9fBjUsti6ACBocoBoMVjypjRHfyRHsPjHMHho31No_oBHpKNgs1pyMHFopgMo_JOHsJNosrdHAUpg3oxoAJpy_JaosjpHMyfofJN6AeRo3HpysuByM1OopKF6_yh63VBofJfoMHhgsHNo_Khy3zs63zsgA1FoM1fH3ehy3ydgsKhopjpo_HOoAxxyseFH_gBofoxosUR63-MoAHFoAja6AHNys-xgZUDUMVDgaUEUM-BrRzwrfuDQcua9RypUiFiQSuWyMua9fYM9fVjraUE6mFiQSuWyMua9fVjrOYag9Ji6sKDUMoxPVY2gIHi6BDfHZF7H_yDHsUfou7DUMNwyfVXgcBjy9zB9fVjraUEoAnDUMVi9RzBrRei6i-6UiFircYpPVYxQcri6i-jg3yibm-fg9-pq3YXUstiPsUibm-sQIupPcua9fBj9RyaUst7Hs176ZFirI-2yfuwyMBjUstOHAnDUMP5y3hwq3ei6soY?src=recom_widget_best_seller_switching&sid=80e4ce6adc973f727d2c15e953e389f53bc076f515219678023eca413a126932f67a98475c3b5ebe57809619ae7a66c1f39581adc9dcd506e61d1af8f8179353548ab4016e7ca6279bf430492839b2ae&management_type=1&dv=ios-2.253.0&page=1&t=ios&r=https%3A%2F%2Fwww.tokopedia.com%2Felbeaute%2Fpigeon-teens-serum-bright-96693%3Fsrc%3Dtopads&ob=0",
                      "wishlistUrl": "https://ta.tokopedia.com/promo/v1/wishlists/8a-xgVY2gmUEH_jfos1hHsrfbm-xgVY789CBUsthbm-FQRo2PcB5QiUEoaFiPcBWgZUEH_rFH_Hho_jp6ZFiyRCsUstOHAnDUMVj9RosQR-BUstposjXoseR6ArO6ArhopnfHiFirpowQcYSUstig9BGqMzUZMggQj2fgAo6QJBkQfBoe7BpZ3O6HcoD692qu7gN3_-Sq1Y2Z9P-q9P2y_-3o3ea69BqzsBE3_UN8u2_Z_g-qjV2_JoGP3Uao32q17jfZ3O6q7OEgcBouJja_VzCP7Y1yRz6zOJp3jHhqV21yp-HuV-W3BP-81OEqpugQugW3Bo-r7BW69BxufzFyMFNqO2yeMgxuOV2_fB-P7B2PfBiH72F3s-DPuKpeSBiHBUh3_oZgMV913Bvq1BRZ3BRq3UpZSCqHMhO3Ao6QfUpeMgxuOV2_fB-P7B2PfBs3VgDyfNDgMzIzMNs81jfZ3Fd81Ok3_Vy8j-M_1ydPOKh6IPy8j-M_1ydP7B2PfBsHjNfyfO3gBBXZSgjH7NDZ325q1OE3_uHqBjF_S2SH7N1gpooucoR_M2-q1hAZS2gHsBN3ByN8B29zSBgHMP2_fB-P7BXHA7ibm-Orfua9fBjUstiH_yNHpjNH_1ibm-pg9opq3YX9fBjUsti6ACBocoBoMVjypjRHfyRHsPjHMHho31No_oBHpKNgs1pyMHFopgMo_JOHsJNosrdHAUpg3oxoAJpy_JaosjpHMyfofJN6AeRo3HpysuByM1OopKF6_yh63VBofJfoMHhgsHNo_Khy3zs63zsgA1FoM1fH3ehy3ydgsKhopjpo_HOoAxxyseFH_gBofoxosUR63-MoAHFoAja6AHNys-xgZUDUMVDgaUEUM-BrRzwrfuDQcua9RypUiFiQSuWyMua9fYM9fVjraUE6mFiQSuWyMua9fVjrOYag9Ji6sKDUMoxPVY2gIHi6BDfHZF7H_yDHsUfou7DUMNwyfVXgcBjy9zB9fVjraUEoAnDUMVi9RzBrRei6i-6UiFircYpPVYxQcri6i-jg3yibm-fg9-pq3YXUstiPsUibm-sQIupPcua9fBj9RyaUst7Hs176ZFirI-2yfuwyMBjUstOHAnDUMP5y3hwq3ei6soY?uid=16939915&src=recom_widget_best_seller_switching&management_type=1&dv=ios-2.253.0&product_id=10187278640&page=1&ob=0&r=https%3A%2F%2Fwww.tokopedia.com%2Felbeaute%2Fpigeon-teens-serum-bright-96693%3Fsrc%3Dtopads&t=ios&sid=80e4ce6adc973f727d2c15e953e389f53bc076f515219678023eca413a126932f67a98475c3b5ebe57809619ae7a66c1f39581adc9dcd506e61d1af8f8179353548ab4016e7ca6279bf430492839b2ae",
                      "trackerImageUrl": "https://ta.tokopedia.com/promo/v1/views/8a-2rOYDQfPwgcV7yZUEHZFiy3zwq3ei6sJNosyOH_URoiFiy3zwPIBFgZUEHZFircYpq9z2Qfdi6srDUSz2Q31i6sJRHAJpH_1NHpjDUSCxPcKi6i-2Q3VSg9HXPcY0QRCBgcBxbMNBPmY2Q3r5yfVsqc15HpnFb9ohP3VagZY3r3-sQ175HsnaHa4RbpUdbp-Myf-jo3VBb_yNy3UWocHpHiOxyprFb3zMHAuMosPMgc1fyaNFQMribm-xgVYpyfYagZUEHpyNbsy7opKRo_KRH_rFosUDUSHp9fh5gaUEUMuNZM2jZJ2M33NGPMep_Mh-qMY2_1o-r7BW_sCsQABE3BPc8ujagfBvq1BRZ3BRq3JausujHsBN3jyN8Bja69Bq17jfZ32Cq1hAZSuiHsuk3Bo-ojBW_MWo8Mz2_uz-Hjh1e9Pvuco7_jPzHO2AH3xqucHa_VzZQu29Z9Bo8MDO33O3Qu2_Z9o-Q_BNyuPjrc-D63Wq3J-MyuPzq1Y2Z9P-q9P2ysoGrVtaQIuyH7-Nys-ZHujp1MgxuOV2_fB-P7B2PfBiH72F3s-DPuKp_MYiH7-MyuPzq1Y2Z9P-q9P2yOx3QcoXQcgjz7gXyRB-ojBD6IBoqBjh3I2mgjOc6IPyH_xR3I2mgjOc6IP-q9P2yp-6PMoWuMggQj2fgAo6QJBkQfBo8BjO_c2gHJNEgpo6ucrp_uzsP7NkZ3BHe72E3_UN8u23692qu7gN3_-Sq1Y2Z9P-QsnYUiFiP9oBrBY2gmUEUsJf6_HN6_JOUiFirfuprfB5QBY2gmUEUsKFg_zsg_gxgcHNopoMopURgA-sH_uB6_1pg_Hd63yOHf-sHArfgs1ho_Uh6_yR6AnaHfusy_ehHfJhHsyNHp-MosPx6_K7opusHfUOg3-Bo_rdHAjfH_Bxg_PxosgsH3yp6_1dH3VjypBjyfeOHAgBosVjH3VM6cydH_rNHp1po_edy3U7HAJfg_Psy_yaopBigsepHAeNHsKp63Uay31ibm-xQcri6i-ig9o79RoBQchBrBYfHaUDUMNOQ3-BrBY5gBYxgIHi6sKDUMNOQ3-BrBYxgIowrMuhUstdbm-X9foxQMz2gcV7guYxgIHi6seFbm-xyBY7g9o7Usti_iUDUSC5rRzwy3hSUs2QUMzBgi-Pbm-fg9-pq3YXUstiPsUibm-sQIupPcua9fBj9RyaUst7Hs176ZFirI-2yfuwyMBjUstOHAnDUMP5y3hwq3ei6soY?ob=0&t=ios&src=recom_widget_best_seller_switching&management_type=1&dv=ios-2.253.0&sid=80e4ce6adc973f727d2c15e953e389f53bc076f515219678023eca413a126932f67a98475c3b5ebe57809619ae7a66c1f39581adc9dcd506e61d1af8f8179353548ab4016e7ca6279bf430492839b2ae&page=1",
                      "imageUrl": "https://images.tokopedia.net/img/cache/300-square/VqbcmM/2023/7/28/2fcbd5ae-69ab-4c32-ac70-df05f67fde6c.png",
                      "url": "https://www.tokopedia.com/elbeaute/pigeon-teens-serum-bright-96693?src=topads",
                      "price": "Rp29.000",
                      "priceInt": 29000,
                      "departmentId": 2265,
                      "badges": [
                        {
                          "title": "Power Merchant Pro",
                          "imageUrl": "https://images.tokopedia.net/img/goldmerchant/pm_activation/badge/Power%20Merchant%20Pro@2x.png"
                        }
                      ],
                      "freeOngkir": {
                        "isActive": true,
                        "imageUrl": "https://images.tokopedia.net/img/img/promo/targeting/red_bo_20k.png"
                      },
                      "rating": 5,
                      "ratingAverage": "5.0",
                      "countReview": 80,
                      "isTopads": true,
                      "categoryBreadcrumbs": "Kecantikan/Perawatan Wajah/Serum Wajah",
                      "recommendationType": "best_seller_v3",
                      "isWishlist": false,
                      "slashedPrice": "",
                      "discountPercentage": 0,
                      "shop": {
                        "id": 7348525,
                        "name": "el beaute",
                        "city": "Tangerang",
                        "location": "Tangerang"
                      },
                      "labelgroup": [
                        {
                          "title": "Cashback",
                          "type": "lightGreen",
                          "position": "promo",
                          "url": ""
                        },
                        {
                          "title": "250+ terjual",
                          "type": "textDarkGrey",
                          "position": "integrity",
                          "url": ""
                        },
                        {
                          "title": "Terlaris",
                          "type": "#E1AA1D",
                          "position": "best_seller",
                          "url": ""
                        }
                      ],
                      "labelGroupVariant": [],
                      "minOrder": 0,
                      "maxOrder": 0,
                      "specificationLabels": [],
                      "warehouseID": 0
                    },
                    {
                      "gridPosition": "top_right",
                      "id": 12218074204,
                      "name": "your skin bae alpha arbutin 3% + grapeseed avoskin",
                      "parentID": 0,
                      "appUrl": "tokopedia://product/12218074204?src=topads",
                      "clickUrl": "https://ta.tokopedia.com/promo/v1/clicks/8a-xgVY2gmUEH_U7osjhH_Uhbm-xgVY789CBUsthbm-FQRo2PcB5QiUE6mFiPcBWgZUEH_rFH_Hho_jp6ZFiyRCsUstNHAnDUMVj9RosQR-BUstposyXopKdHArfo_n7HsHhHArDUSHp9fh5gaUEUMuNZM2jZJ2M33NGPMep_Mh-qMY2_1H7H7OEZ_-ozJJp_92-HjOJe_oo8jjaZ3BRqujp1SByH7ND3uxGqMVAZ_g-qj1hZ3BRq3JausujHsBN3jyN8Bja69Bq17jfZ32Cq1hAZSuiHsuk3Bo-ojBW_MWo8Mz2_uz-Hjh1e9Pvuco7_jPzHO2AH3xqucHa_VzZQu29Z9Bo8MDO33O3Qu2_Z9o-Q_BNyuPjrc-D63Wq3J-MyuPzq1Y2Z9P-q9P2ysoGrVtaQIuyH7-Nys-ZHujp1MgxuOV2_fB-P7B2PfBiH72F3s-DPuKp_MYiH7-MyuPzq1Y2Z9P-q9P2yOx3QcoXQcgjz7gXyRB-ojBD6IBoqBjh3I2mgjOc6IPyH_xR3I2mgjOc6IP-q9P2yp-6PMoWuMggQj2fgAo6QJBkQfBo8Bja_c2soJYJe_o6qBuR_jz-8jO1e_o-q9P2yp-6PMoWuMgsHBgtyfO6Q7BkQfBoe7tNUiFiP9oBrBY2gmUEUsJf6_HN6_JOUiFirfuprfB5QBY2gmUEUsKFg_zsg_gxgcHNopoMopURgA-sH_uB6_1pg_Hd63yOHf-sHArfgs1ho_Uh6_yR6AnaHfusy_ehHfJhHsyNHp-MosPx6_K7opusHfUOg3-Bo_rdHAjfH_Bxg_PxosgsH3yp6_1dH3VjypBjyfeOHAgBosVjH3VM6cydH_rNHp1po_edy3U7HAJfg_Psy_yaopBigsepHAeNHsKp63Uay31ibm-xQcri6i-ig9o79RoBQchBrBYfHaUDUMNOQ3-BrBY5gBYxgIHi6sKDUMNOQ3-BrBYxgIowrMuhUstdbm-sy9zwq3zpUs2QosJDoAJfbAUaosuPbm-X9foxQMz2gcV7guYxgIHi6seFbm-xyBY7g9o7Usti_iUDUSC5rRzwy3hSUstigcuMUiFiPMuarfB5QiUEUSyaUiFiyfhOrRzBrBY2gVYfHiUEHsHR6AjDUSCaq3oB9f-2gmUE6_nFbm-SQfVD9fBjUstpwe?dv=ios-2.253.0&src=recom_widget_best_seller_switching&page=1&sid=80e4ce6adc973f727d2c15e953e389f53bc076f515219678023eca413a126932f67a98475c3b5ebe57809619ae7a66c1f39581adc9dcd506e61d1af8f8179353548ab4016e7ca6279bf430492839b2ae&ob=0&management_type=1&r=https%3A%2F%2Fwww.tokopedia.com%2Favostorebekasi%2Fyour-skin-bae-alpha-arbutin-3-grapeseed-avoskin-alpha-30ml-54fde%3Fsrc%3Dtopads&t=ios",
                      "wishlistUrl": "https://ta.tokopedia.com/promo/v1/wishlists/8a-xgVY2gmUEH_U7osjhH_Uhbm-xgVY789CBUsthbm-FQRo2PcB5QiUE6mFiPcBWgZUEH_rFH_Hho_jp6ZFiyRCsUstNHAnDUMVj9RosQR-BUstposyXopKdHArfo_n7HsHhHArDUSHp9fh5gaUEUMuNZM2jZJ2M33NGPMep_Mh-qMY2_1H7H7OEZ_-ozJJp_92-HjOJe_oo8jjaZ3BRqujp1SByH7ND3uxGqMVAZ_g-qj1hZ3BRq3JausujHsBN3jyN8Bja69Bq17jfZ32Cq1hAZSuiHsuk3Bo-ojBW_MWo8Mz2_uz-Hjh1e9Pvuco7_jPzHO2AH3xqucHa_VzZQu29Z9Bo8MDO33O3Qu2_Z9o-Q_BNyuPjrc-D63Wq3J-MyuPzq1Y2Z9P-q9P2ysoGrVtaQIuyH7-Nys-ZHujp1MgxuOV2_fB-P7B2PfBiH72F3s-DPuKp_MYiH7-MyuPzq1Y2Z9P-q9P2yOx3QcoXQcgjz7gXyRB-ojBD6IBoqBjh3I2mgjOc6IPyH_xR3I2mgjOc6IP-q9P2yp-6PMoWuMggQj2fgAo6QJBkQfBo8Bja_c2soJYJe_o6qBuR_jz-8jO1e_o-q9P2yp-6PMoWuMgsHBgtyfO6Q7BkQfBoe7tNUiFiP9oBrBY2gmUEUsJf6_HN6_JOUiFirfuprfB5QBY2gmUEUsKFg_zsg_gxgcHNopoMopURgA-sH_uB6_1pg_Hd63yOHf-sHArfgs1ho_Uh6_yR6AnaHfusy_ehHfJhHsyNHp-MosPx6_K7opusHfUOg3-Bo_rdHAjfH_Bxg_PxosgsH3yp6_1dH3VjypBjyfeOHAgBosVjH3VM6cydH_rNHp1po_edy3U7HAJfg_Psy_yaopBigsepHAeNHsKp63Uay31ibm-xQcri6i-ig9o79RoBQchBrBYfHaUDUMNOQ3-BrBY5gBYxgIHi6sKDUMNOQ3-BrBYxgIowrMuhUstdbm-sy9zwq3zpUs2QosJDoAJfbAUaosuPbm-X9foxQMz2gcV7guYxgIHi6seFbm-xyBY7g9o7Usti_iUDUSC5rRzwy3hSUstigcuMUiFiPMuarfB5QiUEUSyaUiFiyfhOrRzBrBY2gVYfHiUEHsHR6AjDUSCaq3oB9f-2gmUE6_nFbm-SQfVD9fBjUstpwe?management_type=1&uid=16939915&product_id=12218074204&page=1&ob=0&dv=ios-2.253.0&r=https%3A%2F%2Fwww.tokopedia.com%2Favostorebekasi%2Fyour-skin-bae-alpha-arbutin-3-grapeseed-avoskin-alpha-30ml-54fde%3Fsrc%3Dtopads&t=ios&src=recom_widget_best_seller_switching&sid=80e4ce6adc973f727d2c15e953e389f53bc076f515219678023eca413a126932f67a98475c3b5ebe57809619ae7a66c1f39581adc9dcd506e61d1af8f8179353548ab4016e7ca6279bf430492839b2ae",
                      "trackerImageUrl": "https://ta.tokopedia.com/promo/v1/views/8a-2rOYDQfPwgcV7yZUEHZFiy3zwq3ei6sJaoAyNH_JaHZFiy3zwPIBFgZUEHZFircYpq9z2Qfdi6sKDUSz2Q31i6sJRHAJpH_1NHpjDUSCxPcKi6i-2Q3VSg9HXPcY0QRCBgcBxbMNBPmY2Q3r5yfVsqc15HpnFb9ohP3VagZY3r3-sQ175HsnaHa4hHm4homYio3oj6_yRoa7dH3ypb_eFopKWy_JFHa7F6_CjysyRyM1aysnXqSCSUiFiy3zwrfo5rM1i6sHfoidR6AKFopyOHAeaHpJFoaFirpowQcYSUstig9BGqMzUZMggQj2fgAo6QJBkQfBoepep_92-HjOJe_oo8jja_1zCH7OEZ_--q9P23_oZ8uKp_Mhg3J2ky1o-ojBkz_V-q9P2y_-3o3ea69BqzsBE3_UN8u2_Z_g-qjV2_JoGP3Uao32q17jfZ3O6q7OEgcBouJja_VzCP7Y1yRz6zOJp3jHhqV21yp-HuV-W3BP-81OEqpugQugW3Bo-r7BW69BxufzFyMFNqO2yeMgxuOV2_fB-P7B2PfBiH72F3s-DPuKpeSBiHBUh3_oZgMV913Bvq1BRZ3BRq3UpZSCqHMhO3Ao6QfUpeMgxuOV2_fB-P7B2PfBs3VgDyfNDgMzIzMNs81jfZ3Fd81Ok3_Vy8j-M_1ydPOKh6IPy8j-M_1ydP7B2PfBsHjNfyfO3gBBXZSgjH7NDZ325q1OE3_-HqMH7_7zCH7Nku9P6zJBE_uzCH7B2PfBsHjNfyfO3gMHauMxsQ1N5Z325q1OAZsjibm-Orfua9fBjUstiH_yNHpjNH_1ibm-pg9opq3YX9fBjUsti6ACBocoBoMVjypjRHfyRHsPjHMHho31No_oBHpKNgs1pyMHFopgMo_JOHsJNosrdHAUpg3oxoAJpy_JaosjpHMyfofJN6AeRo3HpysuByM1OopKF6_yh63VBofJfoMHhgsHNo_Khy3zs63zsgA1FoM1fH3ehy3ydgsKhopjpo_HOoAxxyseFH_gBofoxosUR63-MoAHFoAja6AHNys-xgZUDUMVDgaUEUM-BrRzwrfuDQcua9RypUiFiQSuWyMua9fYM9fVjraUE6mFiQSuWyMua9fVjrOYag9Ji6sKDUMNwyfVXgcBjy9zB9fVjraUEoAnDUMVi9RzBrRei6i-6UiFircYpPVYxQcri6BDigcuMUB7DUSgBrSo2Qfdi6i-fHiUDUMoDP9o7g9-wq3zwPsUi6sUpopKNbm-FrMBsguYiq3ei6sjFHmFigfYxQVY2gmUEHR7?sid=80e4ce6adc973f727d2c15e953e389f53bc076f515219678023eca413a126932f67a98475c3b5ebe57809619ae7a66c1f39581adc9dcd506e61d1af8f8179353548ab4016e7ca6279bf430492839b2ae&page=1&dv=ios-2.253.0&src=recom_widget_best_seller_switching&t=ios&management_type=1&ob=0",
                      "imageUrl": "https://images.tokopedia.net/img/cache/300-square/VqbcmM/2023/10/14/b5cd9677-81f3-4078-a103-090db67be2b0.jpg",
                      "url": "https://www.tokopedia.com/avostorebekasi/your-skin-bae-alpha-arbutin-3-grapeseed-avoskin-alpha-30ml-54fde?src=topads",
                      "price": "Rp84.000",
                      "priceInt": 84000,
                      "departmentId": 2265,
                      "badges": [
                        {
                          "title": "Power Merchant Pro",
                          "imageUrl": "https://images.tokopedia.net/img/goldmerchant/pm_activation/badge/Power%20Merchant%20Pro@2x.png"
                        }
                      ],
                      "freeOngkir": {
                        "isActive": true,
                        "imageUrl": "https://images.tokopedia.net/img/img/promo/targeting/red_bo_20k.png"
                      },
                      "rating": 5,
                      "ratingAverage": "5.0",
                      "countReview": 255,
                      "isTopads": true,
                      "categoryBreadcrumbs": "Kecantikan/Perawatan Wajah/Serum Wajah",
                      "recommendationType": "best_seller_v3",
                      "isWishlist": false,
                      "slashedPrice": "Rp139.000",
                      "discountPercentage": 40,
                      "shop": {
                        "id": 5828191,
                        "name": "AVOSTORE BEKASI",
                        "city": "Bekasi",
                        "location": "Bekasi"
                      },
                      "labelgroup": [
                        {
                          "title": "Cashback 12%",
                          "type": "lightGreen",
                          "position": "price",
                          "url": ""
                        },
                        {
                          "title": "Cashback",
                          "type": "lightGreen",
                          "position": "promo",
                          "url": ""
                        },
                        {
                          "title": "500+ terjual",
                          "type": "textDarkGrey",
                          "position": "integrity",
                          "url": ""
                        },
                        {
                          "title": "Produk Terbaru",
                          "type": "textDarkOrange",
                          "position": "gimmick",
                          "url": ""
                        },
                        {
                          "title": "Terlaris",
                          "type": "#E1AA1D",
                          "position": "best_seller",
                          "url": ""
                        }
                      ],
                      "labelGroupVariant": [],
                      "minOrder": 0,
                      "maxOrder": 0,
                      "specificationLabels": [],
                      "warehouseID": 0
                    }
                  ]
                }
              ]
            }
        }
    """.trimIndent()
}
