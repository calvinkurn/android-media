package com.tokopedia.tokofood.feature.purchase.promopage.presentation

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseMultiFragActivity
import com.tokopedia.abstraction.base.view.activity.BaseToolbarActivity
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.base.view.fragment.IBaseMultiFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.exception.ResponseErrorException
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.common.presentation.view.BaseTokofoodActivity
import com.tokopedia.tokofood.common.util.TokofoodErrorLogger
import com.tokopedia.tokofood.databinding.LayoutFragmentPurchasePromoBinding
import com.tokopedia.tokofood.feature.purchase.analytics.TokoFoodPurchaseAnalytics
import com.tokopedia.tokofood.feature.purchase.promopage.di.DaggerTokoFoodPromoComponent
import com.tokopedia.tokofood.feature.purchase.promopage.domain.model.PromoListTokoFoodButton
import com.tokopedia.tokofood.feature.purchase.promopage.domain.model.PromoListTokoFoodEmptyState
import com.tokopedia.tokofood.feature.purchase.promopage.domain.model.PromoListTokoFoodErrorPage
import com.tokopedia.tokofood.feature.purchase.promopage.presentation.adapter.TokoFoodPromoAdapter
import com.tokopedia.tokofood.feature.purchase.promopage.presentation.adapter.TokoFoodPromoAdapterTypeFactory
import com.tokopedia.tokofood.feature.purchase.promopage.presentation.uimodel.TokoFoodPromoFragmentUiModel
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.TokoFoodPurchaseFragment
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.toolbar.TokoFoodPromoToolbar
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.toolbar.TokoFoodPromoToolbarListener
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.setImage
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class TokoFoodPromoFragment : BaseListFragment<Visitable<*>, TokoFoodPromoAdapterTypeFactory>(),
        TokoFoodPromoActionListener, TokoFoodPromoToolbarListener, IBaseMultiFragment {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var userSession: UserSessionInterface

    private var viewBinding by autoClearedNullable<LayoutFragmentPurchasePromoBinding>()
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(TokoFoodPromoViewModel::class.java)
    }

    private var toolbar: TokoFoodPromoToolbar? = null

    companion object {
        const val RV_DIRECTION_UP = -1

        const val HAS_ELEVATION = 6
        const val NO_ELEVATION = 0

        const val REQUEST_CODE_KYC = 1111

        private const val PAGE_NAME = "promo_page"

        fun createInstance(): TokoFoodPromoFragment {
            return TokoFoodPromoFragment()
        }
    }

    override fun onResume() {
        super.onResume()
        val actvt = activity
        if (actvt != null && actvt is BaseToolbarActivity) {
            actvt.title = getFragmentTitle()
            actvt.setUpActionBar(getFragmentToolbar())
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewBinding = LayoutFragmentPurchasePromoBinding.inflate(inflater, container, false)
        val view = viewBinding?.root
        getRecyclerView(view)?.let {
            it.addItemDecoration(TokoFoodPromoDecoration())
            (it.itemAnimator as? SimpleItemAnimator)?.supportsChangeAnimations = false
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBackground()
        initializeToolbar()
        initializeRecyclerViewScrollListener()
        observeList()
        observeFragmentUiModel()
        observeUiEvent()
        loadData()
    }

    override fun getFragmentToolbar(): Toolbar? {
        return null
    }

    override fun getFragmentTitle(): String? {
        return ""
    }

    override fun navigateToNewFragment(fragment: Fragment) {
        (activity as? BaseMultiFragActivity)?.navigateToNewFragment(fragment)
    }

    override fun onItemClicked(t: Visitable<*>?) {
        // No-op
    }

    override fun getScreenName(): String {
        return ""
    }

    override fun getRecyclerViewResourceId() = R.id.recycler_view_purchase_promo

    override fun initInjector() {
        activity?.let {
            DaggerTokoFoodPromoComponent
                    .builder()
                    .baseAppComponent((it.applicationContext as BaseMainApplication).baseAppComponent)
                    .build()
                    .inject(this)
        }
    }

    override fun loadData(page: Int) {

    }

    private fun loadData() {
        viewBinding?.layoutGlobalErrorPurchasePromo?.gone()
        viewBinding?.recyclerViewPurchasePromo?.show()
        adapter.clearAllElements()
        showLoading()
        viewModel.loadData()
    }

    override fun isLoadMoreEnabledByDefault(): Boolean {
        return false
    }

    override fun createAdapterInstance(): BaseListAdapter<Visitable<*>, TokoFoodPromoAdapterTypeFactory> {
        return TokoFoodPromoAdapter(adapterTypeFactory)
    }

    override fun getAdapterTypeFactory(): TokoFoodPromoAdapterTypeFactory {
        return TokoFoodPromoAdapterTypeFactory(this)
    }

    override fun onBackPressed() {
        (activity as BaseTokofoodActivity).onBackPressed()
    }

    private fun initializeToolbar() {
        activity?.let { ctx ->
            viewBinding?.toolbarPurchasePromo?.removeAllViews()
            val tokoFoodPurchaseToolbar = TokoFoodPromoToolbar(ctx).apply {
                listener = this@TokoFoodPromoFragment
            }

            toolbar = tokoFoodPurchaseToolbar

            toolbar?.let {
                viewBinding?.toolbarPurchasePromo?.addView(toolbar)
                it.setContentInsetsAbsolute(Int.ZERO, Int.ZERO)
                (activity as AppCompatActivity).setSupportActionBar(viewBinding?.toolbarPurchasePromo)
            }

            setToolbarShadowVisibility(false)
        }
    }

    private fun setToolbarShadowVisibility(show: Boolean) {
        if (show) {
            viewBinding?.appBarLayout?.elevation = HAS_ELEVATION.toFloat()
        } else {
            viewBinding?.appBarLayout?.elevation = NO_ELEVATION.toFloat()
        }
    }

    private fun setBackground() {
        activity?.let {
            it.window.decorView.setBackgroundColor(ContextCompat.getColor(it, com.tokopedia.unifyprinciples.R.color.Unify_NN0))
        }
    }

    private fun initializeRecyclerViewScrollListener() {
        getRecyclerView(view)?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                // No-op
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (recyclerView.canScrollVertically(RV_DIRECTION_UP)) {
                    setToolbarShadowVisibility(true)
                } else {
                    setToolbarShadowVisibility(false)
                }
            }
        })
    }

    private fun observeList() {
        viewModel.visitables.observe(viewLifecycleOwner, {
            (adapter as TokoFoodPromoAdapter).updateList(it)
        })
    }

    private fun observeFragmentUiModel() {
        viewModel.fragmentUiModel.observe(viewLifecycleOwner, {
            toolbar?.setTitle(it.pageTitle)
            renderTotalAmount(it)
        })
    }

    private fun observeUiEvent() {
        viewModel.uiEvent.observe(viewLifecycleOwner, {
            when (it.state) {
                UiEvent.EVENT_SUCCESS_LOAD_PROMO_PAGE -> renderPromoPage()
                UiEvent.EVENT_ERROR_PAGE_PROMO_PAGE -> {
                    (it.data as? PromoListTokoFoodErrorPage)?.let { errorPage ->
                        renderGlobalError(errorPage)
                        logError(MessageErrorException(errorPage.description))
                    }
                }
                UiEvent.EVENT_FAILED_LOAD_PROMO_PAGE -> {
                    renderGlobalError(it.throwable ?: ResponseErrorException())
                    it.throwable?.let { throwable ->
                        renderGlobalError(throwable)
                        logError(throwable)
                    }
                }
                UiEvent.EVENT_SHOW_TOASTER -> {
                    (it.data as? String)?.let { toasterMessage ->
                        showToaster(toasterMessage)
                    }
                }
                UiEvent.EVENT_NO_COUPON -> {
                    (it.data as? PromoListTokoFoodEmptyState)?.let { emptyState ->
                        renderEmptyState(emptyState)
                    }
                }
            }
        })
    }

    private fun renderPromoPage() {
        viewBinding?.let {
            it.layoutGlobalErrorPurchasePromo.gone()
            it.recyclerViewPurchasePromo.show()
            it.totalAmountPurchasePromo.show()
        }
        TokoFoodPurchaseAnalytics.sendLoadPromoPageTracking()
    }

    private fun renderTotalAmount(fragmentUiModel: TokoFoodPromoFragmentUiModel) {
        viewBinding?.let {
            it.totalAmountPurchasePromo.amountCtaView.isEnabled = true
            it.totalAmountPurchasePromo.setCtaText(
                context?.getString(com.tokopedia.tokofood.R.string.text_purchase_use_promo, fragmentUiModel.promoCount).orEmpty())
            it.totalAmountPurchasePromo.setLabelTitle(fragmentUiModel.promoTitle)
            it.totalAmountPurchasePromo.setAmount(fragmentUiModel.promoAmountStr)
            it.totalAmountPurchasePromo.amountCtaView.setOnClickListener {
                (activity as BaseTokofoodActivity).onBackPressed()
            }
        }
    }

    private fun renderGlobalError(throwable: Throwable) {
        viewBinding?.let {
            it.layoutGlobalErrorPurchasePromo.show()
            it.recyclerViewPurchasePromo.gone()
            val errorType = getGlobalErrorType(throwable)
            it.layoutGlobalErrorPurchasePromo.setType(errorType)
            it.layoutGlobalErrorPurchasePromo.setActionClickListener {
                loadData()
            }
        }
    }

    private fun renderGlobalError(errorPage: PromoListTokoFoodErrorPage) {
        viewBinding?.layoutGlobalErrorPurchasePromo?.run {
            setType(GlobalError.SERVER_ERROR)
            errorTitle.text = errorPage.title
            errorDescription.text = errorPage.description
            errorIllustration.loadImage(errorPage.image)
            errorAction.text = errorPage.button.text
            setActionClickListener {
                when(errorPage.button.action) {
                    PromoListTokoFoodButton.REFRESH_ACTION -> {
                        loadData()
                    }
                    PromoListTokoFoodButton.REDIRECT_ACTION -> {
                        RouteManager.route(context, errorPage.button.link)
                    }
                    else -> {

                    }
                }
            }
            show()
        }
        viewBinding?.recyclerViewPurchasePromo?.gone()
    }

    private fun renderEmptyState(emptyState: PromoListTokoFoodEmptyState) {
        viewBinding?.layoutGlobalErrorPurchasePromo?.run {
            setType(GlobalError.SERVER_ERROR)
            errorTitle.text = emptyState.title
            errorDescription.text = emptyState.description
            errorIllustration.loadImage(emptyState.imageUrl)
            errorAction.gone()
            show()
        }
        viewBinding?.recyclerViewPurchasePromo?.gone()
    }

    private fun getGlobalErrorType(throwable: Throwable): Int {
        return if (throwable is UnknownHostException || throwable is SocketTimeoutException || throwable is ConnectException) {
            GlobalError.NO_CONNECTION
        } else {
            GlobalError.SERVER_ERROR
        }
    }

    private fun showToaster(toasterMessage: String) {
        view?.let {
            Toaster.build(
                view = it,
                text = toasterMessage,
                duration = Toaster.LENGTH_SHORT,
                type = Toaster.TYPE_NORMAL
            ).show()
        }
    }

    private fun logError(throwable: Throwable) {
        TokofoodErrorLogger.logExceptionToServerLogger(
            TokofoodErrorLogger.PAGE.PROMO,
            throwable,
            TokofoodErrorLogger.ErrorType.ERROR_PAGE,
            userSession.deviceId.orEmpty(),
            TokofoodErrorLogger.ErrorDescription.RENDER_PAGE_ERROR,
            mapOf(
                TokofoodErrorLogger.PAGE_KEY to PAGE_NAME
            )
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CODE_KYC -> {

            }
        }
    }

    override fun onClickUnavailablePromoItem() {
        viewModel.showChangeRestrictionMessage()
    }

}