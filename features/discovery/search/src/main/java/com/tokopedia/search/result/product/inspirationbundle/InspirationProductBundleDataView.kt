package com.tokopedia.search.result.product.inspirationbundle

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.discovery.common.analytics.SearchComponentTracking
import com.tokopedia.discovery.common.analytics.searchComponentTracking
import com.tokopedia.search.result.presentation.view.typefactory.ProductListTypeFactory
import com.tokopedia.search.result.product.separator.VerticalSeparable
import com.tokopedia.search.result.product.separator.VerticalSeparator
import com.tokopedia.shop.common.widget.bundle.enum.BundleTypes
import com.tokopedia.shop.common.widget.bundle.model.BundleUiModel
import com.tokopedia.topads.sdk.domain.model.ImpressHolder

data class InspirationProductBundleDataView(
    val title: String = "",
    val type: String = "",
    val position: Int = 0,
    val layout: String = "",
    val trackingOption: Int = 0,
    val bundleList: List<Bundle> = emptyList(),
    override val verticalSeparator: VerticalSeparator = VerticalSeparator.Both,
) : Visitable<ProductListTypeFactory>, VerticalSeparable {
    override fun type(typeFactory: ProductListTypeFactory): Int {
        return typeFactory.type(this)
    }

    override fun addTopSeparator(): VerticalSeparable = this
    override fun addBottomSeparator(): VerticalSeparable = this

    fun asBundleUiModel(): List<BundleUiModel> {
        return bundleList.map { it.bundleDetail }
    }

    data class Bundle(
        val carouselTitle: String = "",
        val applink: String = "",
        val componentId: String = "",
        val bundleDetail: BundleUiModel = BundleUiModel(),
        val trackingOption: Int = 0,
        val dimension90: String = "",
        val keyword: String = "",
        val externalReference: String = "",
        val type: String = "",
    ) : ImpressHolder(), SearchComponentTracking by searchComponentTracking(
        trackingOption = trackingOption,
        componentId = componentId,
        dimension90 = dimension90,
        applink = applink,
        keyword = keyword,
        valueName = "$carouselTitle - ${bundleDetail.bundleName}",
    ) {

        companion object {
            private const val DEFAULT_BUNDLE_ID = "0"
            private val BASE_PRODUCT_BUNDLE_APPLINK =
                ApplinkConst.PRODUCT_BUNDLE.replace("{product_id}", "0")
            private val SINGLE_BUNDLE_APPLINK =
                BASE_PRODUCT_BUNDLE_APPLINK.plus("?bundleId=%s&source=srp&srp_component_id=%s")
        }

        private val selectedSingleBundleId: String
            get() {
                val selectedBundleId = bundleDetail.selectedBundleId
                val isNotDefaultOrEmptyId =
                    selectedBundleId.isNotEmpty() && DEFAULT_BUNDLE_ID != selectedBundleId
                return if (isNotDefaultOrEmptyId) {
                    selectedBundleId
                } else bundleDetail.bundleDetails.firstOrNull()?.bundleId ?: ""
            }

        private val singleBundleApplink: String
            get() = SINGLE_BUNDLE_APPLINK.format(selectedSingleBundleId, componentId)

        val activeApplink: String
            get() {
                return if (bundleDetail.bundleType == BundleTypes.MULTIPLE_BUNDLE) applink
                else singleBundleApplink
            }
    }
}
