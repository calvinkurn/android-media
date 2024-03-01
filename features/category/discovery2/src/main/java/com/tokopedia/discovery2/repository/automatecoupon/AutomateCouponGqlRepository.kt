package com.tokopedia.discovery2.repository.automatecoupon

import com.tokopedia.basemvvm.repository.BaseRepository
import com.tokopedia.discovery2.data.automatecoupon.AttributeFilter
import com.tokopedia.discovery2.data.automatecoupon.AutomateCouponRequest
import com.tokopedia.discovery2.data.automatecoupon.AutomateCouponResponse
import com.tokopedia.discovery2.data.automatecoupon.CouponListWidgetFilter
import javax.inject.Inject

class AutomateCouponGqlRepository @Inject constructor(val getGQLString: (Int) -> String) :
    BaseRepository(), IAutomateCouponGqlRepository {
    override suspend fun fetchData(
        request: AutomateCouponRequest,
        componentName: String?
    ): AutomateCouponResponse {
        return getGQLData(
            automateCouponGQL,
            AutomateCouponResponse::class.java,
            setParams(request)
        )
    }

    private fun setParams(request: AutomateCouponRequest): Map<String, Any> {
        val attributeFilter = with(request) {
            CouponListWidgetFilter(AttributeFilter(ids, categoryIDs, subCategoryIDs, slugs))
        }

        return with(request) {
            mapOf(
                PARAM_SOURCE to source,
                PARAM_THEME_TYPE to theme,
                PARAM_WIDGET_TYPE to widgetType,
                PARAM_FILTER to attributeFilter
            )
        }
    }

    companion object {
        private const val PARAM_SOURCE = "source"
        private const val PARAM_THEME_TYPE = "themeType"
        private const val PARAM_WIDGET_TYPE = "widgetType"
        private const val PARAM_FILTER = "filter"


        private val automateCouponGQL: String = """query Discovery_getCouponListWidget(
                ${'$'}source: String!,
                ${'$'}themeType: String!,
                ${'$'}widgetType: String!,
                ${'$'}filter: CouponListWidgetFilter!
            ) {
                promoCatalogGetCouponListWidget(
                    source: ${'$'}source,
                    themeType: ${'$'}themeType,
                    widgetType: ${'$'}widgetType,
                    filter: ${'$'}filter
                ) {
                    resultStatus {
                        code
                        status
                    }
                    couponListWidget {
                        widgetInfo {
                            headerList {
                                key
                                parent {
                                    key
                                    text
                                    colorInfo {
                                        colorList
                                    }
                                }
                                child {
                                    key
                                    text
                                    colorInfo {
                                        colorList
                                    }
                                }
                            }
                            titleList {
                                key
                                parent {
                                    key
                                    text
                                    colorInfo {
                                        colorList
                                    }
                                }
                            }
                            subtitleList {
                                key
                                parent {
                                    key
                                    text
                                    colorInfo {
                                        colorList
                                    }
                                }
                            }
                            footerList {
                                key
                                parent {
                                    key
                                    text
                                    colorInfo {
                                        colorList
                                    }
                                }
                                child {
                                    key
                                    text
                                    valueList {
                                        key
                                        value
                                    }
                                    colorInfo {
                                        colorList
                                    }
                                }
                            }
                            ctaList {
                                text
                                type
                                isDisabled
                                jsonMetadata
                                toasters {
                                    type
                                    message
                                }
                            }
                            badgeList {
                                key
                                text
                            }
                            iconURL
                            backgroundInfo {
                                imageURL
                            }
                            actionInfo {
                                text
                                url
                                applink
                                type
                                isDisabled
                                jsonMetadata
                            }
                        }
                    }
                }
            }
        """.trimIndent()
    }
}
