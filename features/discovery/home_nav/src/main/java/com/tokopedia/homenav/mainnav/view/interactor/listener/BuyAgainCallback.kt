package com.tokopedia.homenav.mainnav.view.interactor.listener

import android.app.Activity
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.fragment.app.Fragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.homenav.mainnav.view.adapter.viewholder.buyagain.BuyAgainViewHolder
import com.tokopedia.homenav.mainnav.view.analytics.BuyAgainTracker
import com.tokopedia.homenav.mainnav.view.interactor.MainNavListener
import com.tokopedia.homenav.mainnav.view.widget.BuyAgainModel
import com.tokopedia.iris.Iris
import com.tokopedia.iris.IrisAnalytics
import com.tokopedia.product.detail.common.AtcVariantHelper
import com.tokopedia.product.detail.common.VariantPageSource
import com.tokopedia.searchbar.navigation_component.NavSource

class BuyAgainCallback(
    private val fragment: Fragment,
    private val mainNavListener: MainNavListener,
    private val addToCart: (String, String) -> Unit
) : BuyAgainViewHolder.Listener {
    val irisInstance = fragment.context?.let { IrisAnalytics.getInstance(it) }
    /**
     * [mDetail] contains the page source details due
     * the home-nav could be opened any entry points.
     */
    private var mDetail: PageDetail? = null

    /**
     * Since the product's ATC has ability to multiple variant and
     * to get the model and position through [onAtcButtonClicked], hence
     * we have to expose it globally to able to consume in [atcVariantLauncher].
     */
    private var atcBuyAgainParam = Pair<BuyAgainModel?, Int>(null, 0)

    private val atcVariantLauncher = fragment.registerForActivityResult(StartActivityForResult()) {
        if (it.resultCode != Activity.RESULT_OK) return@registerForActivityResult

        AtcVariantHelper.onActivityResultAtcVariant(
            fragment.requireContext(),
            it.resultCode,
            it.data
        ) {
            val (model, position) = atcBuyAgainParam
            val updatedModelForVariant = model?.copy(
                variant = this.listOfVariantSelected
                    ?.firstOrNull()
                    ?.getSelectedOption()
                    ?.variantId
                    .orEmpty()
            )

            onAtcTrackClicked(updatedModelForVariant, position)
        }
    }

    fun setPageDetail(source: NavSource, path: String, pageName: String) = apply {
        mDetail = PageDetail(source, path, pageName)
    }

    override fun onProductCardClicked(model: BuyAgainModel, position: Int) {
        onProductTrackClicked(model, position)

        fragment.startActivity(
            RouteManager.getIntent(
                fragment.requireContext(),
                ApplinkConstInternalMarketplace.PRODUCT_DETAIL,
                model.productId
            )
        )
    }

    override fun onAtcButtonClicked(model: BuyAgainModel, position: Int) {
        if (model.hasVariant) {
            AtcVariantHelper.goToAtcVariant(
                context = fragment.requireContext(),
                productId = model.productId,
                pageSource = VariantPageSource.ME_PAGE_PAGESOURCE,
                shopId = model.shopId,
                startActivitResult = { intent, _ ->
                    shouldExposeAtcParam(model, position)
                    atcVariantLauncher.launch(intent)
                }
            )
        } else {
            addToCart(model.productId, model.shopId)
            onAtcTrackClicked(model, position)
        }
    }

    override fun onBuyAgainWidgetImpression(models: List<BuyAgainModel>, position: Int) {
        irisInstance?.let {
            BuyAgainTracker.impression(
                irisInstance = it,
                userId = mainNavListener.getUserId(),
                pageDetail = mDetail,
                position = position,
                models = models
            )
        }
    }

    private fun onProductTrackClicked(model: BuyAgainModel, position: Int) {
        BuyAgainTracker.productClick(
            userId = mainNavListener.getUserId(),
            pageDetail = mDetail,
            position = position,
            model = model
        )
    }

    private fun onAtcTrackClicked(model: BuyAgainModel?, position: Int) {
        if (model == null) return

        BuyAgainTracker.productAtcClick(
            userId = mainNavListener.getUserId(),
            pageDetail = mDetail,
            position = position,
            model = model
        )
    }

    private fun shouldExposeAtcParam(model: BuyAgainModel, position: Int) {
        atcBuyAgainParam = Pair(model, position)
    }

    data class PageDetail(val source: NavSource, val path: String, val pageName: String? = null)
}
