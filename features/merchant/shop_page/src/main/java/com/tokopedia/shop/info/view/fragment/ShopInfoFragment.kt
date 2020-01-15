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
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.removeObservers
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.shop.R
import com.tokopedia.shop.ShopModuleRouter
import com.tokopedia.shop.analytic.ShopPageTrackingBuyer
import com.tokopedia.shop.analytic.model.CustomDimensionShopPage
import com.tokopedia.shop.common.data.model.ShopInfoData
import com.tokopedia.shop.common.di.component.ShopComponent
import com.tokopedia.shop.extension.transformToVisitable
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
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.partial_shop_info_delivery.*
import kotlinx.android.synthetic.main.partial_shop_info_description.*
import kotlinx.android.synthetic.main.partial_shop_info_note.*
import javax.inject.Inject

class ShopInfoFragment : BaseDaggerFragment(), BaseEmptyViewHolder.Callback,
        ShopNoteViewHolder.OnNoteClicked {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var remoteConfig: RemoteConfig
    private lateinit var shopViewModel: ShopInfoViewModel
    private lateinit var shopPageTracking: ShopPageTrackingBuyer
    private lateinit var noteAdapter: BaseListAdapter<ShopNoteViewModel, ShopNoteAdapterTypeFactory>

    private var shopInfo: ShopInfoData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_shop_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        shopInfo = arguments?.getParcelable(EXTRA_SHOP_INFO)
        shopPageTracking = ShopPageTrackingBuyer(TrackingQueue(context!!))
        remoteConfig = FirebaseRemoteConfigImpl(context)

        initViewModel()
        setupShopNotesList()

        observeShopNotes()
        observeShopInfo()

        loadShopInfo()
    }

    override fun onPause() {
        super.onPause()
        shopPageTracking.sendAllTrackingQueue()
    }

    override fun onDestroy() {
        removeObservers(shopViewModel.shopNotesResp)
        removeObservers(shopViewModel.shopStatisticsResp)
        shopViewModel.clear()
        super.onDestroy()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        val shareMenu = menu?.findItem(R.id.action_share)
        if (shareMenu != null) return

        inflater?.inflate(R.menu.menu_shop_info, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.action_share) {
            onClickShareShop()
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
            showShopInfo()
        }
    }

    private fun loadShopInfo() {
        if (shopInfo != null) {
            showShopInfo()
        } else {
            getShopInfo()
        }
    }

    private fun showShopInfo() {
        shopInfo?.let {
            setToolbarTitle(it.name)
            displayShopDescription(it)
            displayShopLogistic(it)
            displayShopNote(it)
        }
    }

    private fun getShopInfo() {
        val shopId = arguments?.getString(SHOP_ID).orEmpty()

        shopViewModel.getShopInfo(shopId)
        shopViewModel.getShopNotes(shopId)
    }

    private fun setupShopNotesList() {
        noteAdapter = BaseListAdapter(ShopNoteAdapterTypeFactory(this))

        recyclerViewNote.apply {
            adapter = noteAdapter
            isNestedScrollingEnabled = false
            isFocusable = false
        }
    }

    private fun displayShopNote(shopInfo: ShopInfoData) {
        shopViewModel.getShopNotes(shopInfo.shopId)
        showNoteLoading()
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
        shopInfoDescription.text = MethodChecker
                .fromHtmlPreserveLineBreak("${shopInfo.tagLine}<br/><br/>${shopInfo.description}")

        shopInfoLocation.text = shopInfo.location
        shopInfoOpenSince.text = getString(R.string.shop_info_label_open_since_v3, shopInfo.openSince)
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
        shopInfo?.let {
            val isMyShop = shopViewModel.isMyShop(it.shopId)

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
        shopPageTracking.clickShareButton(shopViewModel.isMyShop(it.shopId), dimension)
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

    fun setShopInfo(shopInfo: ShopInfoData) {
        this.shopInfo = shopInfo
        view?.let { loadShopInfo() }
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