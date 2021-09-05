package com.tokopedia.sellerhome.settings.view.viewholder

import android.content.Context
import android.util.TypedValue
import android.view.View
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.elyeproj.loaderviewlibrary.LoaderTextView
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.recyclerview.VerticalRecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.seller.menu.common.view.typefactory.OtherMenuAdapterTypeFactory
import com.tokopedia.seller.menu.common.view.uimodel.UserShopInfoWrapper
import com.tokopedia.seller.menu.common.view.uimodel.base.PowerMerchantProStatus
import com.tokopedia.seller.menu.common.view.uimodel.base.SettingResponseState
import com.tokopedia.seller.menu.common.view.uimodel.base.SettingUiModel
import com.tokopedia.seller.menu.common.view.uimodel.shopinfo.ShopStatusUiModel
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.settings.view.adapter.OtherMenuAdapter
import com.tokopedia.sellerhome.settings.view.adapter.ShopSecondaryInfoAdapter
import com.tokopedia.sellerhome.settings.view.adapter.ShopSecondaryInfoAdapterTypeFactory
import com.tokopedia.sellerhome.settings.view.adapter.uimodel.ShopOperationalData
import com.tokopedia.sellerhome.settings.view.animator.OtherMenuContentAnimator
import com.tokopedia.sellerhome.settings.view.animator.OtherMenuHeaderAnimator
import com.tokopedia.sellerhome.settings.view.animator.SecondaryShopInfoAnimator
import com.tokopedia.sellerhome.settings.view.customview.TopadsTopupView
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSessionInterface

class NewOtherMenuViewHolder(
    private val view: View?,
    private val context: Context,
    private val lifecycleOwner: LifecycleOwner?,
    private val userSession: UserSessionInterface,
    private var listener: Listener
) : LifecycleObserver, OtherMenuContentAnimator.Listener {

    private val otherMenuAdapter by lazy {
        listener.getFragmentAdapter() as? OtherMenuAdapter
    }

    private val secondaryInfoAdapter by lazy {
        val adapterTypeFactory = ShopSecondaryInfoAdapterTypeFactory()
        ShopSecondaryInfoAdapter(adapterTypeFactory, context)
    }

    private var scrollView: NestedScrollView? = null
    private var otherMenuHeader: ConstraintLayout? = null
    private var contentMotionLayout: MotionLayout? = null
    private var secondaryInfoRecyclerView: RecyclerView? = null

    private var headerShopNameTextView: Typography? = null
    private var shopStatusCurvedImage: AppCompatImageView? = null
    private var shopAvatarImage: ImageUnify? = null
    private var shopNameTextView: Typography? = null
    private var topadsAutoTopupIcon: IconUnify? = null

    private var balanceSaldoTextView: Typography? = null
    private var balanceTopadsTopupView: TopadsTopupView? = null
    private var shimmerSaldo: LoaderTextView? = null
    private var shimmerTopads: LoaderTextView? = null
    private var errorLayoutSaldo: ConstraintLayout? = null
    private var errorLayoutTopads: ConstraintLayout? = null

    private var motionLayoutAnimator: OtherMenuContentAnimator? = null
    private var scrollHeaderAnimator: OtherMenuHeaderAnimator? = null
    private var secondaryShopInfoAnimator: SecondaryShopInfoAnimator? = null

    init {
        initView()
        setupView()
        registerLifecycleOwner()
    }

    override fun onInitialAnimationCompleted() {
        motionLayoutAnimator?.animateShareButtonSlideIn()
    }

    override fun onShareButtonAnimationCompleted() {
        balanceTopadsTopupView?.run {
            dummySuccess()
            toggleTopadsTopupWithAnimation()
            setOnAnimationFinishedListener {
                secondaryShopInfoAnimator?.swipeRecyclerViewGently()
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        setInitialValues()
    }

    fun setIsTopadsAutoTopup(isAutoTopup: Boolean) {
        val color =
            if (isAutoTopup) {
                com.tokopedia.unifyprinciples.R.color.Unify_GN500
            } else {
                com.tokopedia.unifyprinciples.R.color.Unify_NN500
            }
        topadsAutoTopupIcon?.setImage(
            newLightEnable = ContextCompat.getColor(
                context,
                color
            )
        )
    }

    fun setBalanceSaldoSuccess(saldoString: String) {
        balanceSaldoTextView?.run {
            text = saldoString
            show()
        }
        errorLayoutSaldo?.gone()
        shimmerSaldo?.gone()
    }

    fun setBalanceTopadsSuccess(topadsValueString: String) {
        balanceTopadsTopupView?.run {
            setTopadsValue(topadsValueString)
            show()
        }
        errorLayoutTopads?.gone()
        shimmerTopads?.gone()
    }

    fun setBalanceSaldoLoading() {
        balanceSaldoTextView?.invisible()
        errorLayoutSaldo?.gone()
        shimmerSaldo?.show()
    }

    fun setBalanceTopadsLoading() {
        balanceTopadsTopupView?.invisible()
        errorLayoutTopads?.gone()
        shimmerTopads?.show()
    }

    fun setBalanceSaldoFailed() {
        balanceSaldoTextView?.invisible()
        errorLayoutSaldo?.show()
        shimmerSaldo?.gone()
    }

    fun setBalanceTopadsFailed() {
        balanceTopadsTopupView?.invisible()
        errorLayoutTopads?.show()
        shimmerTopads?.gone()
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

    fun setShopFollowersData(state: SettingResponseState<String>) {
        secondaryInfoRecyclerView?.post {
            secondaryInfoAdapter.setShopFollowersData(state)
        }
    }

    fun setFreeShippingData(state: SettingResponseState<String>) {
        secondaryInfoRecyclerView?.post {
            secondaryInfoAdapter.setFreeShippingData(state)
        }
    }

    private fun initView() {
        view?.run {
            contentMotionLayout = findViewById(R.id.motion_layout_sah_new_other)
            scrollView = findViewById(R.id.sv_sah_new_other)
            otherMenuHeader = findViewById(R.id.view_sah_new_other_header)
            secondaryInfoRecyclerView = findViewById(R.id.rv_sah_new_other_secondary_info)

            headerShopNameTextView = findViewById(R.id.tv_sah_new_other_header_name)
            shopStatusCurvedImage = findViewById(R.id.iv_sah_new_other_curved_header)
            shopAvatarImage = findViewById(R.id.iv_sah_new_other_shop_avatar)
            shopNameTextView = findViewById(R.id.tv_sah_new_other_shop_name)
            topadsAutoTopupIcon = findViewById(R.id.ic_sah_new_other_balance_topads)

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
            scrollHeaderAnimator =
                OtherMenuHeaderAnimator(scrollView, otherMenuHeader, actionBarHeight).also {
                    it.init()
                }
        }
    }

    private fun setupContentAnimator() {
        motionLayoutAnimator = OtherMenuContentAnimator(contentMotionLayout, this).also {
            it.animateInitialSlideIn()
        }
    }

    private fun setupSecondaryInfoAdapter() {
        secondaryInfoRecyclerView?.run {
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            adapter = secondaryInfoAdapter
            secondaryShopInfoAnimator = SecondaryShopInfoAnimator(this)
        }
    }

    private fun registerLifecycleOwner() {
        lifecycleOwner?.let { lifecycleOwner ->
            (lifecycleOwner as? Fragment)?.viewLifecycleOwnerLiveData?.observe(lifecycleOwner) {
                it.lifecycle.addObserver(this)
            }
        }
    }

    private fun setShopAvatar() {
        shopAvatarImage?.urlSrc = userSession.shopAvatar
    }

    private fun setShopName() {
        val shopName = userSession.shopName
        headerShopNameTextView?.text = shopName
        shopNameTextView?.text = shopName
    }

    private fun setShopStatus() {
        val imageResource =
            when {
                userSession.isShopOfficialStore -> R.drawable.bg_sah_new_other_curved_header_os
                userSession.isGoldMerchant -> R.drawable.bg_sah_new_other_curved_header_pm
                else -> R.drawable.bg_sah_new_other_curved_header_rm
            }
        shopStatusCurvedImage?.setImageResource(imageResource)
    }

    private fun setInitialValues() {
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

    fun dummySuccess() {
        setShopAvatar()
        setShopName()
        setShopStatus()
        setIsTopadsAutoTopup(true)
        setBalanceSaldoSuccess("Rp100.000")
        setBalanceTopadsSuccess("Rp0")
        setFreeShippingData(SettingResponseState.SettingSuccess("https://images.tokopedia.net/img/restriction-engine/bebas-ongkir/BOE_Badge.png"))
        setReputationBadgeData(SettingResponseState.SettingSuccess("https://ecs7.tokopedia.net/img/repsys/badges-off-hd.jpg"))
        setShopFollowersData(SettingResponseState.SettingSuccess("100rb"))
        setShopOperationalData(
            SettingResponseState.SettingSuccess(
                ShopOperationalData(
                    false,
                    true,
                    IconUnify.SHOP,
                    MethodChecker.getColor(
                        context,
                        com.tokopedia.unifyprinciples.R.color.Unify_RN500
                    ),
                    "10.00 WIB"
                )
            ))
        setShopStatusData(SettingResponseState.SettingSuccess(
            ShopStatusUiModel(
                userShopInfoWrapper = UserShopInfoWrapper(
                    shopType = PowerMerchantProStatus.Advanced,
                    userShopInfoUiModel = UserShopInfoWrapper.UserShopInfoUiModel(totalTransaction = 50)),
                user = null
            )
        ))

    }

    interface Listener {
        fun getRecyclerView(): RecyclerView?
        fun getFragmentAdapter(): BaseListAdapter<SettingUiModel, OtherMenuAdapterTypeFactory>?
    }

}