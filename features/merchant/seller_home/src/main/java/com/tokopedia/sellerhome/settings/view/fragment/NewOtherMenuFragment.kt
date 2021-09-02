package com.tokopedia.sellerhome.settings.view.fragment

import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.base.view.recyclerview.VerticalRecyclerView
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.seller.menu.common.analytics.SettingTrackingListener
import com.tokopedia.seller.menu.common.analytics.sendEventImpressionStatisticMenuItem
import com.tokopedia.seller.menu.common.analytics.sendShopInfoImpressionData
import com.tokopedia.seller.menu.common.constant.SellerBaseUrl
import com.tokopedia.seller.menu.common.view.typefactory.OtherMenuAdapterTypeFactory
import com.tokopedia.seller.menu.common.view.uimodel.StatisticMenuItemUiModel
import com.tokopedia.seller.menu.common.view.uimodel.base.SettingShopInfoImpressionTrackable
import com.tokopedia.seller.menu.common.view.uimodel.base.SettingUiModel
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.di.component.DaggerSellerHomeComponent
import com.tokopedia.sellerhome.settings.view.activity.MenuSettingActivity
import com.tokopedia.sellerhome.settings.view.adapter.OtherMenuAdapter
import com.tokopedia.sellerhome.settings.view.adapter.ShopSecondaryInfoAdapter
import com.tokopedia.sellerhome.settings.view.adapter.ShopSecondaryInfoAdapterTypeFactory
import com.tokopedia.sellerhome.settings.view.animator.OtherMenuContentAnimator
import com.tokopedia.sellerhome.settings.view.animator.OtherMenuHeaderAnimator
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

//TODO: Preserve name OtherMenuFragment and move the older one to different path
class NewOtherMenuFragment : BaseListFragment<SettingUiModel, OtherMenuAdapterTypeFactory>(),
    SettingTrackingListener, OtherMenuAdapter.Listener, OtherMenuContentAnimator.Listener {

    companion object {
        private const val APPLINK_FORMAT_ALLOW_OVERRIDE = "%s?allow_override=%b&url=%s"

        @JvmStatic
        fun createInstance(): NewOtherMenuFragment = NewOtherMenuFragment()
    }

    @Inject
    lateinit var userSession: UserSessionInterface

    private val otherMenuAdapter by lazy {
        adapter as? OtherMenuAdapter
    }

    private val secondaryInfoAdapter by lazy {
        val adapterTypeFactory = ShopSecondaryInfoAdapterTypeFactory()
        context?.let {
            ShopSecondaryInfoAdapter(adapterTypeFactory, it)
        }
    }

    private var scrollView: NestedScrollView? = null
    private var otherMenuHeader: ConstraintLayout? = null
    private var contentMotionLayout: MotionLayout? = null
    private var secondaryInfoRecyclerView: RecyclerView? = null

    private var motionLayoutAnimator: OtherMenuContentAnimator? = null
    private var scrollHeaderAnimator: OtherMenuHeaderAnimator? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_new_other_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        setupView()
    }

    override fun onItemClicked(t: SettingUiModel?) {}

    override fun loadData(page: Int) {}

    override fun getAdapterTypeFactory(): OtherMenuAdapterTypeFactory =
        OtherMenuAdapterTypeFactory(this, userSession = userSession)

    override fun initInjector() {
        DaggerSellerHomeComponent.builder()
            .baseAppComponent((requireContext().applicationContext as BaseMainApplication).baseAppComponent)
            .build()
            .inject(this)
    }

    override fun getScreenName(): String = ""

    override fun sendImpressionDataIris(settingShopInfoImpressionTrackable: SettingShopInfoImpressionTrackable) {
        if (settingShopInfoImpressionTrackable is StatisticMenuItemUiModel) {
            sendEventImpressionStatisticMenuItem(userSession.userId)
        } else {
            settingShopInfoImpressionTrackable.sendShopInfoImpressionData()
        }
    }

    override fun createAdapterInstance(): BaseListAdapter<SettingUiModel, OtherMenuAdapterTypeFactory> {
        return OtherMenuAdapter(context, this, adapterTypeFactory)
    }

    override fun getRecyclerViewResourceId(): Int = R.id.rv_sah_new_other_menu

    override fun goToPrintingPage() {
        val url = "${TokopediaUrl.getInstance().WEB}${SellerBaseUrl.PRINTING}"
        val applink = String.format(APPLINK_FORMAT_ALLOW_OVERRIDE, ApplinkConst.WEBVIEW, false, url)
        RouteManager.getIntent(context, applink)?.let {
            context?.startActivity(it)
        }
    }

    override fun goToSettings() {
        startActivity(Intent(context, MenuSettingActivity::class.java))
    }

    override fun onInitialAnimationCompleted() {
        motionLayoutAnimator?.animateShareButtonSlideIn()
    }

    override fun onShareButtonAnimationCompleted() {
        // TODO: Animate scroll view
    }

    private fun initView() {
        view?.run {
            contentMotionLayout = findViewById(R.id.motion_layout_sah_new_other)
            scrollView = findViewById(R.id.sv_sah_new_other)
            otherMenuHeader = findViewById(R.id.view_sah_new_other_header)
            secondaryInfoRecyclerView = findViewById(R.id.rv_sah_new_other_secondary_info)
        }
    }

    private fun setupView() {
        setupRecyclerView()
        setupSecondaryInfoAdapter()
        setupScrollHeaderAnimator()
        setupContentAnimator()
    }

    private fun setupRecyclerView() {
        (getRecyclerView(view) as? VerticalRecyclerView)?.clearItemDecoration()
        otherMenuAdapter?.populateAdapterData()
    }

    private fun setupScrollHeaderAnimator() {
        val tv = TypedValue()
        if (activity?.theme?.resolveAttribute(android.R.attr.actionBarSize, tv, true) == true) {
            val actionBarHeight =
                TypedValue.complexToDimensionPixelSize(tv.data, resources.displayMetrics)
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
        context?.let {
            secondaryInfoRecyclerView?.run {
                layoutManager = LinearLayoutManager(it, RecyclerView.HORIZONTAL, false)
                adapter = secondaryInfoAdapter
            }
            secondaryInfoAdapter?.showInitialInfo()
        }
    }
}