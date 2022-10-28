package com.tokopedia.sellerhome.settings.view.viewholder

import android.content.Context
import android.util.TypedValue
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.recyclerview.VerticalRecyclerView
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.media.loader.loadImageWithoutPlaceholder
import com.tokopedia.seller.menu.common.analytics.NewOtherMenuTracking
import com.tokopedia.seller.menu.common.analytics.sendClickShopNameTracking
import com.tokopedia.seller.menu.common.analytics.sendShopInfoClickNextButtonTracking
import com.tokopedia.seller.menu.common.view.typefactory.OtherMenuAdapterTypeFactory
import com.tokopedia.seller.menu.common.view.uimodel.base.SettingResponseState
import com.tokopedia.seller.menu.common.view.uimodel.base.SettingUiModel
import com.tokopedia.seller.menu.common.view.uimodel.base.ShopType
import com.tokopedia.seller.menu.common.view.uimodel.shopinfo.ShopStatusUiModel
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.settings.view.adapter.OtherMenuAdapter
import com.tokopedia.sellerhome.settings.view.adapter.ShopSecondaryInfoAdapter
import com.tokopedia.sellerhome.settings.view.adapter.ShopSecondaryInfoAdapterTypeFactory
import com.tokopedia.sellerhome.settings.view.adapter.uimodel.ShopOperationalData
import com.tokopedia.sellerhome.settings.view.animator.OtherMenuContentAnimator
import com.tokopedia.sellerhome.settings.view.animator.OtherMenuHeaderAnimator
import com.tokopedia.sellerhome.settings.view.animator.OtherMenuShareButtonAnimator
import com.tokopedia.sellerhome.settings.view.animator.SecondaryShopInfoAnimator
import com.tokopedia.sellerhome.settings.view.customview.TopadsTopupView
import com.tokopedia.shop.common.view.model.TokoPlusBadgeUiModel
import com.tokopedia.unifycomponents.CardUnify
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSessionInterface

class OtherMenuViewHolder(
    private val view: View?,
    private val context: Context,
    private val lifecycleOwner: LifecycleOwner?,
    private val userSession: UserSessionInterface,
    private var listener: Listener
) : LifecycleObserver {

    companion object {
        const val SCROLLVIEW_INITIAL_POSITION = 0
    }

    private val otherMenuAdapter by lazy {
        listener.getFragmentAdapter() as? OtherMenuAdapter
    }

    private val secondaryInfoAdapter by lazy {
        val adapterTypeFactory = ShopSecondaryInfoAdapterTypeFactory(listener)
        ShopSecondaryInfoAdapter(adapterTypeFactory, context)
    }

    private var scrollView: NestedScrollView? = null
    private var otherMenuHeader: LinearLayout? = null
    private var contentMotionLayout: MotionLayout? = null
    private var secondaryInfoRecyclerView: RecyclerView? = null
    private var shareButtonImage: AppCompatImageView? = null

    private var headerShopNameTextView: Typography? = null
    private var headerShopNextButton: IconUnify? = null
    private var headerShopShareButton: IconUnify? = null
    private var shopStatusCurvedImage: AppCompatImageView? = null
    private var shopAvatarImage: ImageUnify? = null
    private var shopNameTextView: Typography? = null
    private var shopNextButton: IconUnify? = null
    private var topadsAutoTopupIcon: IconUnify? = null

    private var balanceSaldoCard: CardUnify? = null
    private var balanceTopadsCard: CardUnify? = null
    private var balanceSaldoTextView: Typography? = null
    private var balanceTopadsTopupView: TopadsTopupView? = null
    private var shimmerSaldo: LoaderUnify? = null
    private var shimmerTopads: LoaderUnify? = null
    private var errorLayoutSaldo: ConstraintLayout? = null
    private var errorLayoutTopads: ConstraintLayout? = null

    private var motionLayoutAnimator: OtherMenuContentAnimator? = null
    private var scrollHeaderAnimator: OtherMenuHeaderAnimator? = null
    private var shareButtonAnimator: OtherMenuShareButtonAnimator? = null
    private var secondaryShopInfoAnimator: SecondaryShopInfoAnimator? = null

    private val saldoImpressHolder = ImpressHolder()
    private val topadsImpressHolder = ImpressHolder()
    private val shopAvatarImpressHolder = ImpressHolder()

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        setInitialValues()
        balanceTopadsTopupView?.stopAnimation()
    }

    fun setInitialLayouts() {
        initView()
        setupView()
        setupClickListener()
        registerLifecycleOwner()
    }

    fun setIsTopadsAutoTopup(isAutoTopup: Boolean) {
        val color =
            if (isAutoTopup) {
                com.tokopedia.unifyprinciples.R.color.Unify_GN500
            } else {
                com.tokopedia.unifyprinciples.R.color.Unify_NN500
            }
        topadsAutoTopupIcon?.run {
            setImage(
                newLightEnable = ContextCompat.getColor(
                    context,
                    color
                )
            )
            setOnClickListener {
                listener.onTopAdsTooltipClicked(isAutoTopup)
            }
        }
    }

    fun setBalanceSaldoData(state: SettingResponseState<String>) {
        when (state) {
            is SettingResponseState.SettingSuccess -> {
                setBalanceSaldoSuccess(state.data)
            }
            is SettingResponseState.SettingError -> {
                setBalanceSaldoFailed()
            }
            else -> {
                setBalanceSaldoLoading()
            }
        }
    }

    fun setBalanceTopadsData(state: SettingResponseState<String>) {
        when (state) {
            is SettingResponseState.SettingSuccess -> {
                setBalanceTopadsSuccess(state.data)
            }
            is SettingResponseState.SettingError -> {
                setBalanceTopadsFailed()
            }
            else -> {
                setBalanceTopadsLoading()
            }
        }
    }

    fun setShopOperationalData(state: SettingResponseState<ShopOperationalData>) {
        secondaryInfoRecyclerView?.post {
            secondaryInfoAdapter.setShopOperationalData(state)
        }
    }

    fun setShopStatusData(state: SettingResponseState<ShopStatusUiModel>) {
        secondaryInfoRecyclerView?.post {
            secondaryInfoAdapter.setShopStatusData(state)
        }
    }

    fun setReputationBadgeData(state: SettingResponseState<String>) {
        secondaryInfoRecyclerView?.post {
            secondaryInfoAdapter.setReputationBadgeData(state)
        }
    }

    fun setTotalTokoMemberData(state: SettingResponseState<String>) {
        secondaryInfoRecyclerView?.post {
            secondaryInfoAdapter.setTokoMemberData(state)
        }
    }

    fun setShopFollowersData(state: SettingResponseState<String>) {
        secondaryInfoRecyclerView?.post {
            secondaryInfoAdapter.setShopFollowersData(state)
        }
    }

    fun setFreeShippingData(state: SettingResponseState<TokoPlusBadgeUiModel>) {
        secondaryInfoRecyclerView?.post {
            secondaryInfoAdapter.setFreeShippingData(state)
        }
    }

    fun runShareButtonAnimation() {
        animateShareButton()
    }

    fun swipeSecondaryInfoGently() {
        secondaryShopInfoAnimator?.swipeRecyclerViewGently()
    }

    fun toggleTopadsTopup() {
        balanceTopadsTopupView?.run {
            setOnAnimationFinishedListener {
                listener.onTopadsValueSet()
            }
            toggleTopadsTopupWithAnimation()
        }
    }

    fun scrollToTop() {
        scrollView?.post {
            scrollView?.smoothScrollTo(
                SCROLLVIEW_INITIAL_POSITION,
                SCROLLVIEW_INITIAL_POSITION
            )
        }
    }

    fun setCentralizePromoTag(state: SettingResponseState<Boolean>) {
        if (state is SettingResponseState.SettingSuccess) {
            otherMenuAdapter?.setCentralizedPromoTag(state.data)
        }
    }

    private fun initView() {
        view?.run {
            contentMotionLayout = findViewById(R.id.motion_layout_sah_new_other)
            scrollView = findViewById(R.id.sv_sah_new_other)
            otherMenuHeader = findViewById(R.id.view_sah_new_other_header)
            secondaryInfoRecyclerView = findViewById(R.id.rv_sah_new_other_secondary_info)
            shareButtonImage = findViewById(R.id.iv_sah_new_other_share)

            headerShopNameTextView = findViewById(R.id.tv_sah_new_other_header_name)
            headerShopNextButton = findViewById(R.id.ic_sah_new_other_header_name)
            headerShopShareButton = findViewById(R.id.ic_sah_new_other_header_share)
            shopStatusCurvedImage = findViewById(R.id.iv_sah_new_other_curved_header)
            shopAvatarImage = findViewById(R.id.iv_sah_new_other_shop_avatar)
            shopNameTextView = findViewById(R.id.tv_sah_new_other_shop_name)
            shopNextButton = findViewById(R.id.iv_sah_new_other_shop_name)
            topadsAutoTopupIcon = findViewById(R.id.ic_sah_new_other_balance_topads)

            balanceSaldoCard = findViewById(R.id.card_sah_new_other_saldo)
            balanceTopadsCard = findViewById(R.id.card_sah_new_other_topads)
            balanceSaldoTextView = findViewById(R.id.tv_sah_new_other_saldo_value)
            balanceTopadsTopupView = findViewById(R.id.topads_topup_view_sah)
            shimmerSaldo = findViewById(R.id.shimmer_sah_new_other_balance_saldo)
            shimmerTopads = findViewById(R.id.shimmer_sah_new_other_topads)
            errorLayoutSaldo = findViewById(R.id.error_state_sah_new_other_saldo)
            errorLayoutTopads = findViewById(R.id.error_state_sah_new_other_topads)
        }
    }

    private fun setupView() {
        setupRecyclerView()
        setupSecondaryInfoAdapter()
        setupScrollHeaderAnimator()
        setupShareButtonAnimator()
        setupContentAnimator()
    }

    private fun setupRecyclerView() {
        (listener.getRecyclerView() as? VerticalRecyclerView)?.clearItemDecoration()
        otherMenuAdapter?.populateAdapterData()
    }

    private fun setupScrollHeaderAnimator() {
        val tv = TypedValue()
        if ((lifecycleOwner as? Fragment)?.activity?.theme?.resolveAttribute(
                android.R.attr.actionBarSize,
                tv,
                true
            ) == true
        ) {
            val actionBarHeight =
                TypedValue.complexToDimensionPixelSize(tv.data, context.resources?.displayMetrics)
            val statusBarHeight =
                context.resources?.getDimensionPixelSize(R.dimen.sah_status_bar_height).orZero()
            scrollHeaderAnimator =
                OtherMenuHeaderAnimator(
                    scrollView,
                    otherMenuHeader,
                    actionBarHeight + statusBarHeight
                ).also {
                    it.init()
                }
        }
    }

    private fun setupContentAnimator() {
        motionLayoutAnimator = OtherMenuContentAnimator(contentMotionLayout).also {
            it.animateInitialSlideIn()
        }
    }

    private fun setupShareButtonAnimator() {
        shareButtonAnimator = OtherMenuShareButtonAnimator(shareButtonImage).also {
            it.setInitialButtonState()
        }
    }

    private fun setupSecondaryInfoAdapter() {
        secondaryInfoRecyclerView?.run {
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            adapter = secondaryInfoAdapter
            itemAnimator = null
            val decorationDrawable =
                context?.resources?.getDrawable(R.drawable.divider_sah_new_other_secondary, null)
            val itemDecoration =
                DividerItemDecoration(context, DividerItemDecoration.HORIZONTAL).apply {
                    decorationDrawable?.let {
                        setDrawable(it)
                    }
                }
            addItemDecoration(itemDecoration)
            secondaryShopInfoAnimator = SecondaryShopInfoAnimator(this)
        }
    }

    private fun setupClickListener() {
        setupShopInfoClickListener()
        setupBalanceClickListener()
    }

    private fun setupShopInfoClickListener() {
        headerShopNameTextView?.setOnClickListener {
            listener.onShopInfoClicked()
            sendClickShopNameTracking()
        }
        headerShopNextButton?.setOnClickListener {
            listener.onShopInfoClicked()
            sendShopInfoClickNextButtonTracking()
        }
        shopAvatarImage?.setOnClickListener {
            NewOtherMenuTracking.sendEventClickShopAvatar()
            listener.onShopInfoClicked()
        }
        shopNameTextView?.setOnClickListener {
            listener.onShopInfoClicked()
            sendClickShopNameTracking()
        }
        shopNextButton?.setOnClickListener {
            listener.onShopInfoClicked()
            sendShopInfoClickNextButtonTracking()
        }
    }

    private fun setupBalanceClickListener() {
        balanceSaldoCard?.setOnClickListener {
            listener.onSaldoClicked()
        }
        balanceTopadsCard?.setOnClickListener {
            listener.onKreditTopadsClicked()
        }
        errorLayoutSaldo?.setOnClickListener {
            listener.onSaldoBalanceRefresh()
        }
        errorLayoutTopads?.setOnClickListener {
            listener.onKreditTopAdsRefresh()
        }
    }

    private fun registerLifecycleOwner() {
        lifecycleOwner?.let { lifecycleOwner ->
            (lifecycleOwner as? Fragment)?.viewLifecycleOwnerLiveData?.observe(lifecycleOwner) {
                it.lifecycle.addObserver(this)
            }
        }
    }

    private fun setBalanceSaldoSuccess(saldoString: String) {
        balanceSaldoTextView?.run {
            text = saldoString
            show()
            addOnImpressionListener(saldoImpressHolder) {
                NewOtherMenuTracking.sendEventImpressionSaldoBalance()
            }
        }
        errorLayoutSaldo?.invisible()
        shimmerSaldo?.gone()
    }

    private fun setBalanceTopadsSuccess(topadsValueString: String) {
        balanceTopadsTopupView?.run {
            setTopadsValue(topadsValueString)
            show()
            addOnImpressionListener(topadsImpressHolder) {
                NewOtherMenuTracking.sendEventImpressionTopadsBalance()
            }
            listener.onTopadsValueSet()
        }
        errorLayoutTopads?.invisible()
        shimmerTopads?.gone()
    }

    private fun setBalanceSaldoLoading() {
        balanceSaldoTextView?.invisible()
        errorLayoutSaldo?.invisible()
        shimmerSaldo?.show()
    }

    private fun setBalanceTopadsLoading() {
        balanceTopadsTopupView?.invisible()
        errorLayoutTopads?.invisible()
        shimmerTopads?.show()
    }

    private fun setBalanceSaldoFailed() {
        balanceSaldoTextView?.invisible()
        errorLayoutSaldo?.show()
        shimmerSaldo?.gone()
    }

    private fun setBalanceTopadsFailed() {
        balanceTopadsTopupView?.invisible()
        errorLayoutTopads?.show()
        shimmerTopads?.gone()
    }

    private fun setShopAvatar() {
        shopAvatarImage?.run {
            urlSrc = userSession.shopAvatar
            addOnImpressionListener(shopAvatarImpressHolder) {
                NewOtherMenuTracking.sendEventImpressionShopAvatar()
            }
        }
    }

    private fun setShopName() {
        val shopName = userSession.shopName
        headerShopNameTextView?.text = shopName
        shopNameTextView?.text = shopName
    }

    private fun setShopStatus() {
        val imageResource: Int
        val headerBackgroundResource: Int
        when {
            userSession.isShopOfficialStore -> {
                imageResource = R.drawable.bg_sah_new_other_curved_header_os
                headerBackgroundResource = R.drawable.bg_sah_new_other_header_os
            }
            userSession.isGoldMerchant -> {
                imageResource = R.drawable.bg_sah_new_other_curved_header_pm
                headerBackgroundResource = R.drawable.bg_sah_new_other_header_pm
            }
            else -> {
                imageResource = R.drawable.bg_sah_new_other_curved_header_rm
                headerBackgroundResource = R.drawable.bg_sah_new_other_header_rm
            }
        }
        shopStatusCurvedImage?.setImageResource(imageResource)
        otherMenuHeader?.setBackgroundResource(headerBackgroundResource)
    }

    fun setInitialValues() {
        secondaryInfoAdapter.showInitialInfo()
        setHeaderValues()
        setInitialBalanceInfoLoading()
    }

    private fun setHeaderValues() {
        setShopAvatar()
        setShopName()
        setShopStatus()
    }

    private fun setInitialBalanceInfoLoading() {
        setBalanceSaldoLoading()
        setBalanceTopadsLoading()
    }

    private fun animateShareButton() {
        if (shareButtonAnimator?.isShareButtonShowing() != true) {
            shareButtonAnimator?.animateShareButtonSlideIn()
        }
        showShareButtons()
    }

    private fun showShareButtons() {
        shareButtonImage?.run {
            show()
            setOnClickListener {
                listener.onShareButtonClicked()
            }
        }
        headerShopShareButton?.run {
            show()
            setOnClickListener {
                listener.onShareButtonClicked()
            }
        }
    }

    interface Listener {
        fun getRecyclerView(): RecyclerView?
        fun getFragmentAdapter(): BaseListAdapter<SettingUiModel, OtherMenuAdapterTypeFactory>?
        fun onShopInfoClicked()
        fun onRmTransactionClicked()
        fun onShopBadgeClicked()
        fun onFollowersCountClicked()
        fun onTokoMemberCountClicked()
        fun onSaldoClicked()
        fun onKreditTopadsClicked()
        fun onRefreshShopInfo()
        fun onFreeShippingClicked()
        fun onShopOperationalClicked()
        fun onGoToPowerMerchantSubscribe(tab: String?, isUpdate: Boolean)
        fun onShopBadgeRefresh()
        fun onTotalTokoMemberRefresh()
        fun onShopTotalFollowersRefresh()
        fun onUserInfoRefresh()
        fun onOperationalHourRefresh()
        fun onSaldoBalanceRefresh()
        fun onKreditTopAdsRefresh()
        fun onFreeShippingRefresh()
        fun onTopAdsTooltipClicked(isTopAdsActive: Boolean)
        fun onTopadsValueSet()
        fun onShareButtonClicked()
        fun onShopStatusImpression(shopType: ShopType)
        fun onFreeShippingImpression()
        fun onTokoPlusClicked()
        fun onTokoPlusImpressed()
        fun onImpressionTokoMember()
    }
}
