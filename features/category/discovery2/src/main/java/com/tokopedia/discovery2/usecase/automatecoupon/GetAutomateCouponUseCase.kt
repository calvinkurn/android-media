package com.tokopedia.discovery2.usecase.automatecoupon

import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.data.automatecoupon.AutomateCouponCtaState
import com.tokopedia.discovery2.data.automatecoupon.AutomateCouponRequest
import com.tokopedia.discovery2.data.automatecoupon.AutomateCouponUiModel
import com.tokopedia.discovery2.data.automatecoupon.CouponInfo
import com.tokopedia.discovery2.data.automatecoupon.CouponListWidgets
import com.tokopedia.discovery2.data.automatecoupon.CtaRedirectionMetadata
import com.tokopedia.discovery2.data.automatecoupon.Layout
import com.tokopedia.discovery2.datamapper.getComponent
import com.tokopedia.discovery2.repository.automatecoupon.IAutomateCouponGqlRepository
import com.tokopedia.discovery_component.widgets.automatecoupon.AutomateCouponModel
import com.tokopedia.discovery_component.widgets.automatecoupon.DynamicColorText
import com.tokopedia.discovery_component.widgets.automatecoupon.TimeLimit
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.asCamelCase
import java.util.*
import javax.inject.Inject

class GetAutomateCouponUseCase @Inject constructor(
    private val repository: IAutomateCouponGqlRepository
) {

    suspend fun execute(componentId: String, pageIdentifier: String): State {
        val component = getComponent(componentId, pageIdentifier)

        if (component?.noOfPagesLoaded == Int.ONE) {
            return State.ALREADY_LOADED
        }

        component?.let {
            val items = mutableListOf<ComponentsItem>()

            val data = it.data?.firstOrNull() ?: return State.FAILED

            val response = repository.fetchData(data.mapToCouponRequest(), it.name)

            response.promoCatalog?.catalogWithCouponList?.let { coupons ->
                if (coupons.isNotEmpty()) {
                    items.addAll(coupons.mapToComponentList(it))
                }
            }

            it.setComponentsItem(items, component.tabName)
            it.noOfPagesLoaded = Int.ONE

            return State.LOADED
        }

        return State.FAILED
    }

    private fun DataItem.mapToCouponRequest(): AutomateCouponRequest {
        val slugs = catalogSlug?.filterNotNull()

        return AutomateCouponRequest(
            source = SOURCE,
            theme = String.EMPTY,
            widgetType = couponLayout.orEmpty(),
            ids = catalogIds.orEmpty(),
            categoryIDs = catalogCategoryIds.orEmpty(),
            subCategoryIDs = catalogSubCategoryIds.orEmpty(),
            slugs = slugs.orEmpty()
        )
    }

    private fun List<CouponListWidgets>.mapToComponentList(
        component: ComponentsItem
    ): ArrayList<ComponentsItem> {
        val list = ArrayList<ComponentsItem>()
        forEachIndexed { index, it ->
            if (it.info == null) return@forEachIndexed

            val compName = when(component.data?.firstOrNull()?.couponLayout?.asCamelCase()) {
                Layout.Single.name -> ComponentNames.SingleAutomateCoupon.componentName
                Layout.Double.name -> ComponentNames.GridAutomateCouponItem.componentName
                Layout.Carousel.name -> ComponentNames.CarouselAutomateCouponItem.componentName
                else -> { return@forEachIndexed }
            }

            val componentsItem = ComponentsItem().apply {
                position = index
                name = compName
                parentListSize = this@mapToComponentList.size
                parentComponentName = component.name
                parentComponentId = component.id
                creativeName = component.creativeName
                properties = component.properties
                data = component.data

                val automateCouponModel = if (component.name == ComponentNames.GridAutomateCoupon.componentName) {
                    it.info.mapToGridModel()
                } else {
                    it.info.mapToListModel()
                }

                automateCoupons = listOf(
                    AutomateCouponUiModel(
                        data = automateCouponModel,
                        ctaState = it.info.mapToCtaState()
                    )
                )
            }

            list.add(componentsItem)
        }

        return list
    }

    private fun CouponInfo.mapToCtaState(): AutomateCouponCtaState {
        val cta = ctaList?.firstOrNull() ?: return AutomateCouponCtaState.OutOfStock

        val jsonMetadata = CtaRedirectionMetadata.parse(cta.metadata.orEmpty())

        val properties = AutomateCouponCtaState.Properties(
            isDisabled = cta.isDisabled.orFalse(),
            text = cta.text.orEmpty(),
            appLink = jsonMetadata.appLink,
            url = jsonMetadata.url
        )

        return when (cta.type.orEmpty()) {
            CTA_CLAIM -> AutomateCouponCtaState.Claim(properties)
            CTA_REDIRECT -> AutomateCouponCtaState.Redirect(properties)
            else -> AutomateCouponCtaState.OutOfStock
        }
    }

    private fun CouponInfo.mapToListModel(): AutomateCouponModel.List {
        val type = DynamicColorText(
            header?.firstOrNull()?.parent?.text.orEmpty(),
            header?.firstOrNull()?.parent?.colorList?.hexColors?.firstOrNull().orEmpty()
        )

        val shopName = DynamicColorText(
            header?.firstOrNull()?.children?.firstOrNull()?.text.orEmpty(),
            header?.firstOrNull()?.children?.firstOrNull()?.colorList?.hexColors?.firstOrNull()
                .orEmpty()
        )

        val benefit = DynamicColorText(
            title?.firstOrNull()?.parent?.text.orEmpty(),
            title?.firstOrNull()?.parent?.colorList?.hexColors?.firstOrNull().orEmpty()
        )

        val tnc = DynamicColorText(
            subtitle?.firstOrNull()?.parent?.text.orEmpty(),
            subtitle?.firstOrNull()?.parent?.colorList?.hexColors?.firstOrNull().orEmpty()
        )

        val prefixTimeLimit = DynamicColorText(
            footer?.firstOrNull()?.parent?.text.orEmpty(),
            footer?.firstOrNull()?.parent?.colorList?.hexColors?.firstOrNull().orEmpty()
        )

        val epoch = footer?.firstOrNull()?.children?.firstOrNull()?.values?.firstOrNull()?.value
        val endDate = epoch?.epochToDate()
        val timeLimit = TimeLimit.Timer(prefixTimeLimit, endDate)

        val badge = badges?.firstOrNull()?.value

        return AutomateCouponModel.List(
            type = type,
            benefit = benefit,
            tnc = tnc,
            backgroundUrl = background?.imageURL.orEmpty(),
            timeLimit = timeLimit,
            iconUrl = iconURL.orEmpty(),
            shopName = shopName,
            badgeText = badge
        )
    }

    private fun CouponInfo.mapToGridModel(): AutomateCouponModel.Grid {
        val type = DynamicColorText(
            header?.firstOrNull()?.parent?.text.orEmpty(),
            header?.firstOrNull()?.parent?.colorList?.hexColors?.firstOrNull().orEmpty()
        )

        val shopName = DynamicColorText(
            header?.firstOrNull()?.children?.firstOrNull()?.text.orEmpty(),
            header?.firstOrNull()?.children?.firstOrNull()?.colorList?.hexColors?.firstOrNull()
                .orEmpty()
        )

        val benefit = DynamicColorText(
            title?.firstOrNull()?.parent?.text.orEmpty(),
            title?.firstOrNull()?.parent?.colorList?.hexColors?.firstOrNull().orEmpty()
        )

        val tnc = DynamicColorText(
            subtitle?.firstOrNull()?.parent?.text.orEmpty(),
            subtitle?.firstOrNull()?.parent?.colorList?.hexColors?.firstOrNull().orEmpty()
        )

        val badge = badges?.firstOrNull()?.value

        return AutomateCouponModel.Grid(
            type = type,
            benefit = benefit,
            tnc = tnc,
            backgroundUrl = background?.imageURL.orEmpty(),
            iconUrl = iconURL.orEmpty(),
            shopName = shopName,
            badgeText = badge
        )
    }

    private fun String.epochToDate(): Date {
        val calendar = Calendar.getInstance()
        calendar.time = Date(this.toLong() * 1_000)
        return calendar.time
    }

    enum class State {
        FAILED,
        LOADED,
        ALREADY_LOADED
    }

    companion object {
        private const val SOURCE = "discovery-page"

        private const val CTA_CLAIM = "claim"
        private const val CTA_REDIRECT = "redirect"
    }
}
