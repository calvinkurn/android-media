package com.tokopedia.shop.info.view.fragment

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.AbstractionRouter
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.BaseEmptyViewHolder
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.reputation.common.data.source.cloud.model.ReputationSpeedV2
import com.tokopedia.shop.R
import com.tokopedia.shop.ShopModuleRouter
import com.tokopedia.shop.analytic.ShopPageTrackingBuyer
import com.tokopedia.shop.analytic.model.CustomDimensionShopPage
import com.tokopedia.shop.common.data.source.cloud.model.ShopInfo
import com.tokopedia.shop.common.di.component.ShopComponent
import com.tokopedia.shop.common.util.TextHtmlUtils
import com.tokopedia.shop.extension.transformToVisitable
import com.tokopedia.shop.info.di.component.DaggerShopInfoComponent
import com.tokopedia.shop.info.di.module.ShopInfoModule
import com.tokopedia.shop.info.view.adapter.ShopInfoLogisticAdapter
import com.tokopedia.shop.info.view.adapter.ShopInfoLogisticAdapterTypeFactory
import com.tokopedia.shop.info.view.listener.ShopInfoView
import com.tokopedia.shop.info.view.presenter.ShopInfoPresenter
import com.tokopedia.shop.note.view.activity.ShopNoteDetailActivity
import com.tokopedia.shop.note.view.adapter.ShopNoteAdapterTypeFactory
import com.tokopedia.shop.note.view.adapter.viewholder.ShopNoteViewHolder
import com.tokopedia.shop.note.view.model.ShopNoteViewModel
import com.tokopedia.trackingoptimizer.TrackingQueue
import kotlinx.android.synthetic.main.fragment_shop_info.*
import kotlinx.android.synthetic.main.partial_shop_info_delivery.*
import kotlinx.android.synthetic.main.partial_shop_info_description.*
import kotlinx.android.synthetic.main.partial_shop_info_note.*
import kotlinx.android.synthetic.main.partial_shop_info_statistics.*
import javax.inject.Inject

class ShopInfoFragment : BaseDaggerFragment(), ShopInfoView, BaseEmptyViewHolder.Callback,
        ShopNoteViewHolder.OnNoteClicked {

    companion object {
        @JvmStatic
        fun createInstance(): ShopInfoFragment = ShopInfoFragment()
    }

    @Inject
    lateinit var presenter: ShopInfoPresenter
    lateinit var shopPageTracking: ShopPageTrackingBuyer
    var shopInfo: ShopInfo? = null
    var hasVisibleOnce = false
    var needLoadData = true
    private val noteAdapter by lazy {
        BaseListAdapter<ShopNoteViewModel, ShopNoteAdapterTypeFactory>(ShopNoteAdapterTypeFactory(this))
    }
    private var shopId: String = "0"
    private var shopDomain: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        shopPageTracking = ShopPageTrackingBuyer(
                TrackingQueue(context!!))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_shop_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerViewNote.isNestedScrollingEnabled = false
        recyclerViewNote.isFocusable = false
        recyclerViewLogistic.isNestedScrollingEnabled = false
        recyclerViewLogistic.isFocusable = false

        shopInfo?.run { updateShopInfo(this) }
    }

    override fun onDestroy() {
        presenter.detachView()
        super.onDestroy()
    }

    fun updateShopInfo(shopInfo: ShopInfo) {
        shopId = shopInfo.info.shopId
        shopDomain = shopInfo.info.shopDomain
        this.shopInfo = shopInfo
        refreshUIFirstTime()
    }

    private fun refreshUIFirstTime() {
        if (needLoadData) {
            shopInfo?.let {
                displayImageBackground(it)
                displayShopDescription(it)
                displayShopStatistics(it)
                displayShopLogistic(it)
                displayShopNote()
                needLoadData = false
            }
        }
    }

    fun reset() {
        hasVisibleOnce = false
        needLoadData = true
    }

    private fun displayShopNote() {
        recyclerViewNote.adapter = noteAdapter
        showLoading()
        presenter.getShopNoteList(shopId)
    }

    private fun showLoading() {
        noteAdapter.removeErrorNetwork()
        recyclerViewNote.visibility = View.GONE
        loading.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        loading.visibility = View.GONE
        recyclerViewNote.visibility = View.VISIBLE
    }

    private fun displayShopLogistic(shopInfo: ShopInfo) {
        recyclerViewLogistic.adapter = ShopInfoLogisticAdapter(ShopInfoLogisticAdapterTypeFactory(),
                shopInfo.shipment.map { it.transformToVisitable() })

        if (!presenter.isMyshop(shopId)) {
            labelViewLogisticTitle.setContent("")
            labelViewLogisticTitle.setOnClickListener { }
        } else {
            labelViewLogisticTitle.setContent(getString(R.string.shop_info_label_manage_note))
            labelViewLogisticTitle.setOnClickListener { goToManageLogistic() }
        }
    }

    private fun goToManageLogistic() {
        val app = activity?.application
        if (app is ShopModuleRouter) {
            app.goToManageShipping(activity)
        }
    }

    private fun displayShopStatistics(shopInfo: ShopInfo) {
        presenter.getShopReputationSpeed(shopId)
        productQualityValue.text = shopInfo.ratings.quality.average
        productRating.rating = try {
            shopInfo.ratings.quality.average.toFloat()
        } catch (e: Exception) {
            shopInfo.ratings.quality.ratingStar
        }
        totalReview.text = getString(R.string.shop_info_content_total_review, shopInfo.ratings.quality.countTotal)
        labelViewReview.setOnClickListener { goToReviewQualityDetail() }
        labelViewDiscussion.setOnClickListener { gotoShopDiscussion() }
    }

    private fun gotoShopDiscussion() {
        if (activity?.application is ShopModuleRouter) {
            shopInfo?.run {
                shopPageTracking.clickDiscussion(
                        presenter.isMyshop(shopId), CustomDimensionShopPage.create(shopInfo))
            }
            (activity?.application as ShopModuleRouter).goToShopDiscussion(activity, shopId)
        }
    }

    private fun displayShopDescription(shopInfo: ShopInfo) {
        shopInfoDescription.text = TextHtmlUtils
                .getTextFromHtml("${shopInfo.info.shopTagline}<br/><br/>${shopInfo.info.shopDescription}")

        shopInfoLocation.text = shopInfo.info.shopLocation
        shopInfoOpenSince.text = getString(R.string.shop_info_label_open_since_v3, shopInfo.info.shopOpenSince)

        val reputaionMedalType = shopInfo.stats.shopBadgeLevel.set.toInt()
        val reputationLevel = shopInfo.stats.shopBadgeLevel.level.toInt()
        val reputationScore = shopInfo.stats.shopReputationScore

        shopReputationView.setValue(reputaionMedalType, reputationLevel, reputationScore)
        totalPoin.text = getString(R.string.dashboard_x_points, reputationScore)

        textViewScoreGood.text = shopInfo.stats.shopLastTwelveMonths.countScoreGood
        textViewScoreNeutral.text = shopInfo.stats.shopLastTwelveMonths.countScoreNeutral
        textViewScoreBad.text = shopInfo.stats.shopLastTwelveMonths.countScoreBad
    }

    private fun displayImageBackground(shopInfo: ShopInfo) {
        if (shopInfo.info.isShopOfficial || shopInfo.info.isGoldMerchant) {
            shopBackgroundImageView.visibility = View.VISIBLE
            ImageHandler.LoadImage(shopBackgroundImageView, shopInfo.info.shopCover)
        } else {
            shopBackgroundImageView.visibility = View.GONE
        }
    }

    private fun goToReviewQualityDetail() {
        shopInfo?.run {
            shopPageTracking.clickReview(presenter.isMyshop(shopId), CustomDimensionShopPage.create(shopInfo))
        }
        if (activity?.application is ShopModuleRouter) {
            (activity?.application as ShopModuleRouter).goToShopReview(activity, shopId, shopDomain)
        }
    }

    override fun getScreenName(): String? = null

    override fun initInjector() {
        DaggerShopInfoComponent.builder().shopInfoModule(ShopInfoModule())
                .shopComponent(getComponent(ShopComponent::class.java))
                .build().inject(this)
        presenter.attachView(this)
    }

    override fun renderListNote(notes: List<ShopNoteViewModel>) {
        hideLoading()
        noteAdapter.clearAllElements()
        noteAdapter.addElement(notes)
        if (notes.isEmpty()) {
            noteAdapter.addElement(EmptyModel().apply {
                if (presenter.isMyshop(shopId)) {
                    title = getString(R.string.shop_note_empty_note_title_seller)
                    callback = this@ShopInfoFragment
                } else {
                    title = getString(R.string.shop_note_empty_note_title_buyer)
                }
            })
        }

        if (notes.isEmpty() || !presenter.isMyshop(shopId)) {
            noteLabelView.setContent("")
            noteLabelView.setOnClickListener {}
        } else if (presenter.isMyshop(shopId)) {
            noteLabelView.setOnClickListener { onEmptyButtonClicked() }
        }
    }

    override fun showListNoteError(throwable: Throwable?) {}

    override fun onEmptyContentItemTextClicked() {}

    override fun onEmptyButtonClicked() {
        val app = activity?.application
        if (app is ShopModuleRouter) {
            shopInfo?.run {
                shopPageTracking.clickAddNote(CustomDimensionShopPage.create(shopInfo))
            }
            app.goToEditShopNote(activity)
        }
    }

    override fun onNoteClicked(position: Long, shopNoteViewModel: ShopNoteViewModel) {
        shopInfo?.run {
            shopPageTracking.clickReadNotes(
                    presenter.isMyshop(shopId), position.toInt(), CustomDimensionShopPage.create(shopInfo))
        }

        startActivity(ShopNoteDetailActivity.createIntent(activity, shopNoteViewModel.getShopNoteId().toString()))
    }

    override fun onErrorGetReputation(throwable: Throwable) {
        labelViewProcessOrder.setContent(getString(R.string.shop_page_speed_shop_not_available))
    }

    override fun onSuccessGetReputation(reputationSpeed: ReputationSpeedV2) {
        val speedLevelDescription = reputationSpeed.speedFmt

        if (TextUtils.isEmpty(speedLevelDescription)) {
            labelViewProcessOrder.setContent(getString(R.string.shop_page_speed_shop_not_available))
        } else {
            labelViewProcessOrder.setContent(speedLevelDescription)
        }
    }

    override fun setUserVisibleHint(isFragmentVisible_: Boolean) {
        super.setUserVisibleHint(true)
        if (this.isVisible) {
            // we check that the fragment is becoming visible
            if (isFragmentVisible_ && !hasVisibleOnce) {
                hasVisibleOnce = true
                refreshUIFirstTime()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        shopPageTracking.sendAllTrackingQueue()
    }
}