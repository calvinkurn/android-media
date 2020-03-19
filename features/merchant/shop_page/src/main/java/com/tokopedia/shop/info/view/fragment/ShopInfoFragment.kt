package com.tokopedia.shop.info.view.fragment

import android.os.Bundle
import android.text.TextUtils
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.BaseEmptyViewHolder
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.FindAndReplaceHelper
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.removeObservers
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.shop.R
import com.tokopedia.shop.ShopModuleRouter
import com.tokopedia.shop.analytic.ShopPageTrackingShopPageInfo
import com.tokopedia.shop.analytic.model.CustomDimensionShopPage
import com.tokopedia.shop.common.data.model.ShopInfoData
import com.tokopedia.shop.common.di.component.ShopComponent
import com.tokopedia.shop.extension.transformToVisitable
import com.tokopedia.shop.info.data.model.ShopStatisticsResp
import com.tokopedia.shop.info.di.component.DaggerShopInfoComponent
import com.tokopedia.shop.info.di.module.ShopInfoModule
import com.tokopedia.shop.info.view.activity.ShopInfoActivity.Companion.EXTRA_SHOP_INFO
import com.tokopedia.shop.info.view.adapter.ShopInfoLogisticAdapter
import com.tokopedia.shop.info.view.adapter.ShopInfoLogisticAdapterTypeFactory
import com.tokopedia.shop.info.view.viewmodel.ShopInfoViewModel
import com.tokopedia.shop.note.view.activity.ShopNoteDetailActivity
import com.tokopedia.shop.note.view.adapter.ShopNoteAdapterTypeFactory
import com.tokopedia.shop.note.view.adapter.viewholder.ShopNoteViewHolder
import com.tokopedia.shop.note.view.model.ShopNoteViewModel
import com.tokopedia.shop.oldpage.view.activity.ShopPageActivity
import com.tokopedia.shop.oldpage.view.activity.ShopPageActivity.Companion.SHOP_ID
import com.tokopedia.shop.common.config.ShopPageConfig
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

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var remoteConfig: RemoteConfig
    private lateinit var shopPageConfig: ShopPageConfig
    private lateinit var shopViewModel: ShopInfoViewModel
    private lateinit var shopPageTracking: ShopPageTrackingShopPageInfo
    private lateinit var noteAdapter: BaseListAdapter<ShopNoteViewModel, ShopNoteAdapterTypeFactory>

    private var shopInfo: ShopInfoData? = null

    // Will be deleted once old shop page removed
    private var shouldInitView = true
    private val isOfficial: Boolean
        get() = shopInfo?.isOfficial == 1
    private val isGold: Boolean
        get() = shopInfo?.isGold == 1
    private val customDimensionShopPage: CustomDimensionShopPage by lazy {
        CustomDimensionShopPage.create(getShopId(),isOfficial,isGold)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_shop_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        shopInfo = arguments?.getParcelable(EXTRA_SHOP_INFO)
        shopPageTracking = ShopPageTrackingShopPageInfo(TrackingQueue(context!!))
        remoteConfig = FirebaseRemoteConfigImpl(context)
        shopPageConfig = ShopPageConfig(context)

        val newShopPageEnabled = shopPageConfig.isNewShopPageEnabled()
        setHasOptionsMenu(newShopPageEnabled)

        initViewModel()
        initObservers()
        initView()
    }

    override fun onPause() {
        super.onPause()
        shopPageTracking.sendAllTrackingQueue()
    }

    override fun onDestroy() {
        removeObservers(shopViewModel.shopInfo)
        removeObservers(shopViewModel.shopNotesResp)
        removeObservers(shopViewModel.shopStatisticsResp)
        shopViewModel.flush()
        super.onDestroy()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater?.inflate(R.menu.menu_shop_info, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_share  ->  {
                onClickShareShop()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onEmptyButtonClicked() {
        shopInfo?.run {
            shopPageTracking.clickAddNote(CustomDimensionShopPage
                    .create(shopId, isOfficial == 1, isGold == 1))
            RouteManager.route(activity, ApplinkConstInternalMarketplace.SHOP_SETTINGS_NOTES)
        }
    }

    override fun onNoteClicked(position: Long, shopNoteViewModel: ShopNoteViewModel) {
        shopInfo?.run {
            shopPageTracking.clickReadNotes(
                    shopViewModel.isMyShop(shopId), position.toInt(),
                    CustomDimensionShopPage.create(shopId, isOfficial == 1,
                            isGold == 1))
            startActivity(ShopNoteDetailActivity.createIntent(
                    activity,
                    shopId,
                    shopNoteViewModel.shopNoteId.toString()
            ))
        }
    }

    override fun onEmptyContentItemTextClicked() {}

    override fun getScreenName() = null

    override fun initInjector() {
        DaggerShopInfoComponent.builder().shopInfoModule(ShopInfoModule())
                .shopComponent(getComponent(ShopComponent::class.java))
                .build()
                .inject(this)
    }

    private fun initViewModel() {
        shopViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(ShopInfoViewModel::class.java)
    }

    private fun initObservers() {
        observeShopNotes()
        observeShopInfo()
        observeShopStats()
    }

    private fun observeShopNotes() {
        observe(shopViewModel.shopNotesResp) {
            when (it) {
                is Success -> renderListNote(it.data)
                is Fail -> hideNoteLoading()
            }
        }
    }

    private fun observeShopInfo() {
        observe(shopViewModel.shopInfo) {
            shopInfo = it
            customDimensionShopPage.updateCustomDimensionData(getShopId(), isOfficial, isGold)
            showShopInfo()
        }
    }

    private fun observeShopStats() {
        if(!shopPageConfig.isNewShopPageEnabled()) {
            observe(shopViewModel.shopStatisticsResp) {
                displayShopStatistics(it)
            }
        }
    }

    private fun initView() {
        getShopId()?.let { shopId ->
            setupShopNotesList()
            setStatisticsVisibility()

            if (shopInfo == null) {
                getShopInfo(shopId)
            } else {
                showShopInfo()
            }

            getShopNotes(shopId)
            getShopStats(shopId)
        }
    }

    private fun getShopInfo(shopId: String) {
        shopViewModel.getShopInfo(shopId)
    }

    private fun showShopInfo() {
        shopInfo?.let {
            setToolbarTitle(it.name)
            displayImageBackground(it)
            displayShopDescription(it)
            displayShopLogistic(it)
        }
    }

    private fun setupShopNotesList() {
        noteAdapter = BaseListAdapter(ShopNoteAdapterTypeFactory(this))

        recyclerViewNote.apply {
            adapter = noteAdapter
            isNestedScrollingEnabled = false
            isFocusable = false
        }
    }

    private fun getShopNotes(shopId: String) {
        showNoteLoading()
        shopViewModel.getShopNotes(shopId)
    }

    private fun getShopStats(shopId: String) {
        if(!shopPageConfig.isNewShopPageEnabled()) {
            shopViewModel.getShopStats(shopId)
        }
    }

    private fun setStatisticsVisibility() {
        if(shopPageConfig.isNewShopPageEnabled()) {
            shopInfoStatistics.visibility = View.GONE
        } else {
            shopInfoStatistics.visibility = View.VISIBLE
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

    private fun displayShopLogistic(shopInfo: ShopInfoData) {
        setupLogisticList(shopInfo)

        if (shopViewModel.isMyShop(shopInfo.shopId)) {
            labelViewLogisticTitle.setContent(getString(R.string.shop_info_label_manage_note))
            labelViewLogisticTitle.setOnClickListener { goToManageLogistic() }
        }
    }

    private fun setupLogisticList(shopInfo: ShopInfoData) {
        val visitable = shopInfo.shipments.map { it.transformToVisitable() }
        val shopLogisticAdapter = ShopInfoLogisticAdapter(ShopInfoLogisticAdapterTypeFactory(), visitable)

        recyclerViewLogistic.apply {
            adapter = shopLogisticAdapter
            isNestedScrollingEnabled = false
            isFocusable = false
        }
    }

    private fun goToManageLogistic() {
        val shippingIntent = RouteManager.getIntent(activity,
                ApplinkConstInternalMarketplace.SHOP_SETTINGS_ADDRESS) ?: return
        startActivity(shippingIntent)
    }

    private fun displayShopDescription(shopInfo: ShopInfoData) {
        if (TextUtils.isEmpty(shopInfo.tagLine) && TextUtils.isEmpty(shopInfo.description)) {
            shopInfoDescription.hide()
        } else {
            shopInfoDescription.show()
            shopInfoDescription.text = MethodChecker
                    .fromHtmlPreserveLineBreak("${shopInfo.tagLine}<br/><br/>${shopInfo.description}")
        }
        shopInfoLocation.text = shopInfo.location
        shopInfoOpenSince.text = getString(R.string.shop_info_label_open_since_v3, shopInfo.openSince)
    }

    private fun displayImageBackground(shopInfo: ShopInfoData) {
        if (shopInfo.isOfficial == 1 || shopInfo.isGold == 1 && !shopPageConfig.isNewShopPageEnabled()) {
            shopBackgroundImageView.visibility = View.VISIBLE
            ImageHandler.LoadImage(shopBackgroundImageView, shopInfo.imageCover)
        } else {
            shopBackgroundImageView.visibility = View.GONE
        }
    }

    private fun goToReviewQualityDetail() {
        shopInfo?.run {
            shopPageTracking.clickReview(shopViewModel.isMyShop(shopId),
                    CustomDimensionShopPage.create(shopId, isOfficial == 1,
                            isGold == 1))

            shopPageTracking.clickReviewMore(shopId, shopViewModel.isMyShop(shopId))

            val intent = RouteManager.getIntent(
                    activity,
                    ApplinkConst.SHOP_REVIEW,
                    shopId
            ) ?: return@run

            startActivity(intent)
        }
    }

    private fun gotoShopDiscussion() {
        shopInfo?.run {
            val dimension = CustomDimensionShopPage.create(shopId, isOfficial == 1, isGold == 1)
            shopPageTracking.clickDiscussion(shopViewModel.isMyShop(shopId), dimension)

            val talkIntent = RouteManager.getIntent(context, ApplinkConst.SHOP_TALK, shopId)
                    ?: return@run
            startActivity(talkIntent)
        }
    }

    private fun renderListNote(notes: List<ShopNoteViewModel>) {
        getShopId()?.let {
            val isMyShop = shopViewModel.isMyShop(it)

            hideNoteLoading()
            noteAdapter.clearAllElements()

            if (notes.isEmpty()) {
                showEmptyShopNotes(isMyShop)
            } else {
                showShopNotes(notes)
            }

            if (isMyShop) {
                showManageNotesLabel()
            }
        }
    }

    private fun showManageNotesLabel() {
        noteLabelView.setContent(getString(R.string.shop_info_label_manage_note))
        noteLabelView.setOnClickListener { onEmptyButtonClicked() }
    }

    private fun showShopNotes(notes: List<ShopNoteViewModel>) {
        noteAdapter.addElement(notes)
    }

    private fun showEmptyShopNotes(isMyShop: Boolean) {
        noteAdapter.addElement(EmptyModel().apply {
            if (isMyShop) {
                title = getString(R.string.shop_note_empty_note_title_seller)
                callback = this@ShopInfoFragment
            } else {
                title = getString(R.string.shop_note_empty_note_title_buyer)
            }
        })
    }

    private fun displayShopStatistics(shopStatisticsResp: ShopStatisticsResp) {
        setLabelViewClickListener()
        showShopRating(shopStatisticsResp)
        showShopSatisfaction(shopStatisticsResp)
        shopShopReputation(shopStatisticsResp)
        showProcessOrderLabel(shopStatisticsResp)
    }

    private fun showShopRating(shopStatisticsResp: ShopStatisticsResp) {
        shopStatisticsResp.shopRatingStats?.let {
            productQualityValue.text = it.ratingScore.toString()
            productRating.rating = it.ratingScore
            totalReview.text = getString(R.string.shop_info_content_total_review, it.totalReview.toString())
        }
    }

    private fun shopShopReputation(shopStatisticsResp: ShopStatisticsResp) {
        shopStatisticsResp.shopReputation?.let {
            totalPoin.text = getString(R.string.dashboard_x_points, it.score)
            context?.run { ImageHandler.LoadImage(shopReputationView, it.badgeHD) }
        }
    }

    private fun showShopSatisfaction(shopStatisticsResp: ShopStatisticsResp) {
        shopStatisticsResp.shopSatisfaction?.let {
            textViewScoreGood.text = it.recentOneYear.good.toString()
            textViewScoreNeutral.text = it.recentOneYear.neutral.toString()
            textViewScoreBad.text = it.recentOneYear.bad.toString()
        }
    }

    private fun showProcessOrderLabel(shopStatisticsResp: ShopStatisticsResp) {
        shopStatisticsResp.shopPackSpeed?.let {
            onSuccessGetReputation(it.speedFmt)
        }
    }

    private fun setLabelViewClickListener() {
        labelViewReview.setOnClickListener { goToReviewQualityDetail() }
        labelViewDiscussion.setOnClickListener { gotoShopDiscussion() }
    }

    private fun onSuccessGetReputation(speedFmt: String) {
        if (TextUtils.isEmpty(speedFmt)) {
            labelViewProcessOrder.setContent(getString(R.string.shop_page_speed_shop_not_available))
        } else {
            labelViewProcessOrder.setContent(speedFmt)
        }
    }

    private fun onClickShareShop() {
        shopInfo?.let {
            trackClickShareButton(it)
            goToShareShop(it)
        }
    }

    private fun goToShareShop(shopInfo: ShopInfoData) {
        val shopShareMsg = getShopShareMessage(shopInfo)
        (activity?.applicationContext as? ShopModuleRouter)?.goToShareShop(
                activity,
                shopInfo.shopId,
                shopInfo.url,
                shopShareMsg
        )
    }

    private fun trackClickShareButton(it: ShopInfoData) {
        val dimension = CustomDimensionShopPage.create(it.shopId, it.isOfficial == 1, it.isGold == 1)
        shopPageTracking.clickShareButton(dimension)
    }

    private fun getShopShareMessage(shopInfo: ShopInfoData): String {
        var shopShareMsg = remoteConfig.getString(RemoteConfigKey.SHOP_SHARE_MSG)
        val shopName = MethodChecker.fromHtml(shopInfo.name).toString()

        shopShareMsg = if (!TextUtils.isEmpty(shopShareMsg)) {
            FindAndReplaceHelper.findAndReplacePlaceHolders(shopShareMsg,
                    ShopPageActivity.SHOP_NAME_PLACEHOLDER, shopName,
                    ShopPageActivity.SHOP_LOCATION_PLACEHOLDER, shopInfo.location)
        } else {
            getString(R.string.shop_label_share_formatted, shopName, shopInfo.location)
        }

        return shopShareMsg
    }

    private fun setToolbarTitle(title: String) {
        val toolbar = (activity as? AppCompatActivity)?.supportActionBar
        toolbar?.title = title
    }

    private fun getShopId(): String? {
        return arguments?.getString(SHOP_ID) ?: shopInfo?.shopId
    }

    // Will be deleted once old shop page removed
    fun setShopInfo(shopInfo: ShopInfoData) {
        if(!shouldInitView) return
        this.shopInfo = shopInfo

        view?.let {
            initView()
            shouldInitView = false
        }
    }

    fun onBackPressed() {
        shopPageTracking.clickBackArrow(false, customDimensionShopPage)
    }

    companion object {
        fun createInstance(
                shopId: String? = null,
                shopInfo: ShopInfoData? = null
        ): ShopInfoFragment {
            return ShopInfoFragment().apply {
                val bundle = Bundle()
                bundle.putString(SHOP_ID, shopId)
                bundle.putParcelable(EXTRA_SHOP_INFO, shopInfo)
                arguments = bundle
            }
        }
    }
}