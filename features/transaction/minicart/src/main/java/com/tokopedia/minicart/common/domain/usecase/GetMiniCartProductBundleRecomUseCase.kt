package com.tokopedia.minicart.common.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.localizationchooseaddress.common.ChosenAddressRequestHelper
import com.tokopedia.minicart.common.domain.data.MiniCartProductBundleRecomData
import com.tokopedia.minicart.common.domain.query.GetProductBundleRecomQuery
import com.tokopedia.minicart.common.domain.query.GetProductBundleRecomQuery.PARAM_EXCLUDE_BUNDLE_IDS
import com.tokopedia.minicart.common.domain.query.GetProductBundleRecomQuery.PARAM_PRODUCT_IDS
import com.tokopedia.minicart.common.domain.query.GetProductBundleRecomQuery.PARAM_QUERY_PARAM
import com.tokopedia.minicart.common.domain.query.GetProductBundleRecomQuery.PARAM_WAREHOUSE_ID
import com.tokopedia.network.data.model.response.Header
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class GetMiniCartProductBundleRecomUseCase @Inject constructor(
    @ApplicationContext private val graphqlRepository: GraphqlRepository,
    private val chosenAddressRequestHelper: ChosenAddressRequestHelper
): GraphqlUseCase<MiniCartProductBundleRecomData>(graphqlRepository) {

    init {
        setTypeClass(MiniCartProductBundleRecomData::class.java)
        setGraphqlQuery(GetProductBundleRecomQuery)
    }

    suspend fun execute(
        productIds: List<String>,
        excludeBundleIds: List<String>,
        queryParam: String = "type=SINGLE,MULTIPLE"
    ): MiniCartProductBundleRecomData {
//        setRequestParams(RequestParams.create().apply {
//            putString(PARAM_WAREHOUSE_ID, chosenAddressRequestHelper.getChosenAddress().tokonow.warehouseId)
//            putObject(PARAM_PRODUCT_IDS, productIds)
//            putObject(PARAM_EXCLUDE_BUNDLE_IDS, excludeBundleIds)
//            putString(PARAM_QUERY_PARAM, queryParam)
//        }.parameters)
//        return executeOnBackground()
//        return MiniCartProductBundleRecomData(
//            data = MiniCartProductBundleRecomData.Data(
//                widgetName = "aaaa",
//                widgetData = listOf()),
//            header = Header())

        return  MiniCartProductBundleRecomData(
            data = MiniCartProductBundleRecomData.Data(
                widgetName = "Hello World",
                widgetData = listOf(
                    MiniCartProductBundleRecomData.Data.Bundle(
                        bundleDetails = listOf(
                            MiniCartProductBundleRecomData.Data.Bundle.BundleDetail(
                                bundleID = "123",
                                discountPercentage = 12,
                                displayPrice = "12000",
                                displayPriceRaw = 1231,
                                isPO = false,
                                isProductsHaveVariant = false,
                                minOrder = 1,
                                minOrderWording = "",
                                originalPrice = "",
                                originalPriceRaw = "",
                                preorderInfo = "",
                                preorderInfoRaw = 12,
                                savingAmount = 11,
                                savingAmountWording = ""
                            )
                        ),
                        bundleGroupID = "33333",
                        bundleName = "",
                        bundleProducts = listOf(
                            MiniCartProductBundleRecomData.Data.Bundle.BundleProduct(
                            appLink = "",
                            imageUrl = "",
                            productID = "12121",
                            productName = "hello world",
                            webLink = ""
                            ),MiniCartProductBundleRecomData.Data.Bundle.BundleProduct(
                                appLink = "",
                                imageUrl = "",
                                productID = "14444",
                                productName = "helo helo",
                                webLink = ""
                        )),
                        bundleType = "",
                        shopID = "",
                        startTimeUnix = 0,
                        stopTimeUnix = 0,
                        warehouseID = "123121"
                    ),
                    MiniCartProductBundleRecomData.Data.Bundle(
                        bundleDetails = listOf(
                            MiniCartProductBundleRecomData.Data.Bundle.BundleDetail(
                                bundleID = "123",
                                discountPercentage = 12,
                                displayPrice = "12000",
                                displayPriceRaw = 1231,
                                isPO = false,
                                isProductsHaveVariant = false,
                                minOrder = 1,
                                minOrderWording = "",
                                originalPrice = "",
                                originalPriceRaw = "",
                                preorderInfo = "",
                                preorderInfoRaw = 12,
                                savingAmount = 11,
                                savingAmountWording = ""
                            )
                        ),
                        bundleGroupID = "111111",
                        bundleName = "",
                        bundleProducts = listOf(
                            MiniCartProductBundleRecomData.Data.Bundle.BundleProduct(
                                appLink = "",
                                imageUrl = "",
                                productID = "12121",
                                productName = "hello world",
                                webLink = ""
                            ),MiniCartProductBundleRecomData.Data.Bundle.BundleProduct(
                                appLink = "",
                                imageUrl = "",
                                productID = "14444",
                                productName = "helo helo",
                                webLink = ""
                            )),
                        bundleType = "",
                        shopID = "",
                        startTimeUnix = 0,
                        stopTimeUnix = 0,
                        warehouseID = "123121"
                    ),
                    MiniCartProductBundleRecomData.Data.Bundle(
                        bundleDetails = listOf(
                            MiniCartProductBundleRecomData.Data.Bundle.BundleDetail(
                                bundleID = "123",
                                discountPercentage = 12,
                                displayPrice = "12000",
                                displayPriceRaw = 1231,
                                isPO = false,
                                isProductsHaveVariant = false,
                                minOrder = 1,
                                minOrderWording = "",
                                originalPrice = "",
                                originalPriceRaw = "",
                                preorderInfo = "",
                                preorderInfoRaw = 12,
                                savingAmount = 11,
                                savingAmountWording = ""
                            )
                        ),
                        bundleGroupID = "213112",
                        bundleName = "",
                        bundleProducts = listOf(
                            MiniCartProductBundleRecomData.Data.Bundle.BundleProduct(
                                appLink = "",
                                imageUrl = "",
                                productID = "12121",
                                productName = "hello world",
                                webLink = ""
                            ),MiniCartProductBundleRecomData.Data.Bundle.BundleProduct(
                                appLink = "",
                                imageUrl = "",
                                productID = "14444",
                                productName = "helo helo",
                                webLink = ""
                            )),
                        bundleType = "",
                        shopID = "",
                        startTimeUnix = 0,
                        stopTimeUnix = 0,
                        warehouseID = "123121"
                    )

                )
            ),
            header = Header()
        )
    }

}