package com.tokopedia.shop.info.view.fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.BaseEmptyViewHolder
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.shop.R
import com.tokopedia.shop.ShopModuleRouter
import com.tokopedia.shop.analytic.ShopPageTrackingBuyer
import com.tokopedia.shop.analytic.model.CustomDimensionShopPage
import com.tokopedia.shop.common.di.component.ShopComponent
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.shop.extension.transformToVisitable
import com.tokopedia.shop.info.data.model.ShopStatisticsResp
import com.tokopedia.shop.info.di.component.DaggerShopInfoComponent
import com.tokopedia.shop.info.di.module.ShopInfoModule
import com.tokopedia.shop.info.view.adapter.ShopInfoLogisticAdapter
import com.tokopedia.shop.info.view.adapter.ShopInfoLogisticAdapterTypeFactory
import com.tokopedia.shop.info.view.viewmodel.ShopInfoViewModel
import com.tokopedia.shop.note.view.activity.ShopNoteDetailActivity
import com.tokopedia.shop.note.view.adapter.ShopNoteAdapterTypeFactory
import com.tokopedia.shop.note.view.adapter.viewholder.ShopNoteViewHolder
import com.tokopedia.shop.note.view.model.ShopNoteViewModel
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_shop_info.*
import kotlinx.android.synthetic.main.partial_shop_info_delivery.*
import kotlinx.android.synthetic.main.partial_shop_info_description.*
import kotlinx.android.synthetic.main.partial_shop_info_note.*
import kotlinx.android.synthetic.main.partial_shop_info_statistics.*
import javax.inject.Inject

class ShopInfoFragment : BaseDaggerFragment(), BaseEmptyViewHolder.Callback,
        ShopNoteViewHolder.OnNoteClicked {

    companion object {
        @JvmStatic
        fun createInstance(): ShopInfoFragment = ShopInfoFragment()
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var shopViewModel: ShopInfoViewModel

    lateinit var shopPageTracking: ShopPageTrackingBuyer
    var shopInfo: ShopInfo? = null
    var hasVisibleOnce = false
    var needLoadData = true
    private val noteAdapter by lazy {
        BaseListAdapter<ShopNoteViewModel, ShopNoteAdapterTypeFactory>(ShopNoteAdapterTypeFactory(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        shopPageTracking = ShopPageTrackingBuyer(
                TrackingQueue(context!!))

        activity?.run {
            shopViewModel = ViewModelProviders.of(this, viewModelFactory).get(ShopInfoViewModel::class.java)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        shopViewModel.shopNotesResp.observe(this, Observer {
            when(it){
                is Success -> renderListNote(it.data)
                is Fail -> { hideNoteLoading() }
            }
        })

        shopViewModel.shopStatisticsResp.observe(this, Observer {shopStatistics ->
            shopStatistics?.let { displayShopStatistics(it) }})
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
        shopViewModel.shopNotesResp.removeObservers(this)
        shopViewModel.shopStatisticsResp.removeObservers(this)
        shopViewModel.clear()
        super.onDestroy()
    }

    fun updateShopInfo(shopInfo: ShopInfo) {
        this.shopInfo = shopInfo
        refreshUIFirstTime()
    }

    private fun refreshUIFirstTime() {
        if (needLoadData) {
            shopInfo?.let {
                displayImageBackground(it)
                displayShopDescription(it)
                displayShopLogistic(it)
                displayShopNote()
                shopViewModel.getShopInfo(it.shopCore.shopID)
                needLoadData = false
            }
        }
    }

    fun reset() {
        hasVisibleOnce = false
        needLoadData = true
    }

    private fun displayShopNote() {
        shopInfo?.let {
            recyclerViewNote.adapter = noteAdapter
            showNoteLoading()
        }
    }

    private fun showNoteLoading() {
        noteAdapter.removeErrorNetwork()
        recyclerViewNote.visibility = View.GONE
        loading.visibility = View.VISIBLE
    }

    private fun hideNoteLoading() {
        loading.visibility = View.GONE
        recyclerViewNote.visibility = View.VISIBLE
    }

    private fun displayShopLogistic(shopInfo: ShopInfo) {
        recyclerViewLogistic.adapter = ShopInfoLogisticAdapter(ShopInfoLogisticAdapterTypeFactory(),
                shopInfo.shipments.map { it.transformToVisitable() })

        if (!shopViewModel.isMyShop(shopInfo.shopCore.shopID)) {
            labelViewLogisticTitle.setContent("")
            labelViewLogisticTitle.setOnClickListener { }
        } else {
            labelViewLogisticTitle.setContent(getString(R.string.shop_info_label_manage_note))
            labelViewLogisticTitle.setOnClickListener { goToManageLogistic() }
        }
    }

    private fun goToManageLogistic() {
        val shippingIntent = RouteManager.getIntent(context, ApplinkConstInternalMarketplace.SHOP_SHIPPING_SETTING) ?: return
        startActivity(shippingIntent)
    }

    private fun displayShopStatistics(shopStatisticsResp: ShopStatisticsResp) {
        shopStatisticsResp.shopRatingStats?.let {
            productQualityValue.text = it.ratingScore.toString()
            productRating.rating = it.ratingScore
            totalReview.text = getString(R.string.shop_info_content_total_review, it.totalReview.toString())
        }

        shopStatisticsResp.shopSatisfaction?.let {
            textViewScoreGood.text = it.recentOneYear.good.toString()
            textViewScoreNeutral.text = it.recentOneYear.neutral.toString()
            textViewScoreBad.text = it.recentOneYear.bad.toString()
        }

        shopStatisticsResp.shopReputation?.let {
            totalPoin.text = getString(R.string.dashboard_x_points, it.score)
            context?.run { ImageHandler.loadImage(this, shopReputationView, it.badgeHD, -1) }
        }

        shopStatisticsResp.shopPackSpeed?.let {
            onSuccessGetReputation(it.speedFmt)
        }

        labelViewReview.setOnClickListener { goToReviewQualityDetail() }
        labelViewDiscussion.setOnClickListener { gotoShopDiscussion() }
    }

    private fun gotoShopDiscussion() {
        shopInfo?.run {
            val shopId = shopCore.shopID
            shopPageTracking.clickDiscussion(
                    shopViewModel.isMyShop(shopId),
                    CustomDimensionShopPage.create(shopId, goldOS.isOfficial == 1, goldOS.isGold == 1))

            val talkIntent = RouteManager.getIntent(context, ApplinkConst.SHOP_TALK, shopId) ?: return@run
            startActivity(talkIntent)
        }
    }

    private fun displayShopDescription(shopInfo: ShopInfo) {
        shopInfoDescription.text = MethodChecker
                .fromHtmlPreserveLineBreak("${shopInfo.shopCore.tagLine}<br/><br/>${shopInfo.shopCore.description}")

        shopInfoLocation.text = shopInfo.location
        shopInfoOpenSince.text = getString(R.string.shop_info_label_open_since_v3, shopInfo.createdInfo.openSince)
    }

    private fun displayImageBackground(shopInfo: ShopInfo) {
        if (shopInfo.goldOS.isOfficial == 1 || shopInfo.goldOS.isGold == 1) {
            shopBackgroundImageView.visibility = View.VISIBLE
            ImageHandler.LoadImage(shopBackgroundImageView, shopInfo.shopAssets.cover)
        } else {
            shopBackgroundImageView.visibility = View.GONE
        }
    }

    private fun goToReviewQualityDetail() {
        shopInfo?.run {
            val shopId = shopCore.shopID
            shopPageTracking.clickReview(shopViewModel.isMyShop(shopId),
                    CustomDimensionShopPage.create(shopId, goldOS.isOfficial == 1,
                            goldOS.isGold == 1))

            shopPageTracking.clickReviewMore(shopId, shopViewModel.isMyShop(shopId))
            val intent = RouteManager.getIntent(activity, ApplinkConst.SHOP_REVIEW, shopId) ?: return@run
            startActivity(intent)
        }
    }

    override fun getScreenName(): String? = null

    override fun initInjector() {
        DaggerShopInfoComponent.builder().shopInfoModule(ShopInfoModule())
                .shopComponent(getComponent(ShopComponent::class.java))
                .build().inject(this)
    }

    private fun renderListNote(notes: List<ShopNoteViewModel>) {
        val shopId = shopInfo?.shopCore?.shopID ?: "0"
        hideNoteLoading()
        noteAdapter.clearAllElements()
        noteAdapter.addElement(notes)
        if (notes.isEmpty()) {
            noteAdapter.addElement(EmptyModel().apply {
                if (shopViewModel.isMyShop(shopId)) {
                    title = getString(R.string.shop_note_empty_note_title_seller)
                    callback = this@ShopInfoFragment
                } else {
                    title = getString(R.string.shop_note_empty_note_title_buyer)
                }
            })
        }

        if (notes.isEmpty() || !shopViewModel.isMyShop(shopId)) {
            noteLabelView.setContent("")
            noteLabelView.setOnClickListener {}
        } else if (shopViewModel.isMyShop(shopId)) {
            noteLabelView.setOnClickListener { onEmptyButtonClicked() }
        }
    }

    override fun onEmptyContentItemTextClicked() {}

    override fun onEmptyButtonClicked() {
        shopInfo?.run {
            shopPageTracking.clickAddNote(CustomDimensionShopPage
                    .create(shopCore.shopID, goldOS.isOfficial == 1, goldOS.isGold == 1))
        }
        RouteManager.route(context, ApplinkConstInternalMarketplace.SHOP_NOTE_SETTING)
    }

    override fun onNoteClicked(position: Long, shopNoteViewModel: ShopNoteViewModel) {
        shopInfo?.run {
            val shopId = shopCore.shopID
            shopPageTracking.clickReadNotes(
                    shopViewModel.isMyShop(shopId), position.toInt(),
                    CustomDimensionShopPage.create(shopId, goldOS.isOfficial == 1,
                            goldOS.isGold == 1))
        }

        startActivity(ShopNoteDetailActivity.createIntent(activity, shopNoteViewModel.getShopNoteId().toString()))
    }

    private fun onSuccessGetReputation(speedFmt: String) {

        if (TextUtils.isEmpty(speedFmt)) {
            labelViewProcessOrder.setContent(getString(R.string.shop_page_speed_shop_not_available))
        } else {
            labelViewProcessOrder.setContent(speedFmt)
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