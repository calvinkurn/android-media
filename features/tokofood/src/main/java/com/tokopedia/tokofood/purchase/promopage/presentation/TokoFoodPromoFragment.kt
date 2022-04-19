package com.tokopedia.tokofood.purchase.promopage.presentation

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
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.network.exception.ResponseErrorException
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.common.presentation.view.BaseTokofoodActivity
import com.tokopedia.tokofood.databinding.LayoutFragmentPurchasePromoBinding
import com.tokopedia.tokofood.purchase.promopage.di.DaggerTokoFoodPromoComponent
import com.tokopedia.tokofood.purchase.promopage.presentation.adapter.TokoFoodPromoAdapter
import com.tokopedia.tokofood.purchase.promopage.presentation.adapter.TokoFoodPromoAdapterTypeFactory
import com.tokopedia.tokofood.purchase.promopage.presentation.uimodel.TokoFoodPromoFragmentUiModel
import com.tokopedia.tokofood.purchase.purchasepage.presentation.TokoFoodPurchaseFragment
import com.tokopedia.tokofood.purchase.purchasepage.presentation.toolbar.TokoFoodPromoToolbar
import com.tokopedia.tokofood.purchase.purchasepage.presentation.toolbar.TokoFoodPromoToolbarListener
import com.tokopedia.tokofood.purchase.removeDecimalSuffix
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.setImage
import com.tokopedia.utils.currency.CurrencyFormatUtil
import com.tokopedia.utils.lifecycle.autoClearedNullable
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class TokoFoodPromoFragment : BaseListFragment<Visitable<*>, TokoFoodPromoAdapterTypeFactory>(),
        TokoFoodPromoActionListener, TokoFoodPromoToolbarListener, IBaseMultiFragment {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var viewBinding by autoClearedNullable<LayoutFragmentPurchasePromoBinding>()
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(TokoFoodPromoViewModel::class.java)
    }

    private var toolbar: TokoFoodPromoToolbar? = null

    companion object {
        const val HAS_ELEVATION = 6
        const val NO_ELEVATION = 0

        const val REQUEST_CODE_KYC = 1111

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
        initializeToolbar(view)
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

    private fun initializeToolbar(view: View) {
        activity?.let {
            viewBinding?.toolbarPurchasePromo?.removeAllViews()
            val tokoFoodPurchaseToolbar = TokoFoodPromoToolbar(it).apply {
                listener = this@TokoFoodPromoFragment
            }

            toolbar = tokoFoodPurchaseToolbar

            toolbar?.let {
                viewBinding?.toolbarPurchasePromo?.addView(toolbar)
                it.setContentInsetsAbsolute(0, 0);
                (activity as AppCompatActivity).setSupportActionBar(viewBinding?.toolbarPurchasePromo)
            }

            setToolbarShadowVisibility(false)
        }
    }

    private fun setToolbarShadowVisibility(show: Boolean) {
        if (show) {
            viewBinding?.appBarLayout?.elevation = TokoFoodPurchaseFragment.HAS_ELEVATION.toFloat()
        } else {
            viewBinding?.appBarLayout?.elevation = TokoFoodPurchaseFragment.NO_ELEVATION.toFloat()
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
                if (recyclerView.canScrollVertically(-1)) {
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
            renderTotalAmount(it)
        })
    }

    private fun observeUiEvent() {
        viewModel.uiEvent.observe(viewLifecycleOwner, {
            when (it.state) {
                UiEvent.EVENT_SUCCESS_LOAD_PROMO_PAGE -> renderPromoPage()
                UiEvent.EVENT_FAILED_LOAD_PROMO_PAGE -> renderGlobalError(it.throwable
                        ?: ResponseErrorException())
                UiEvent.EVENT_RENDER_GLOBAL_ERROR_KYC -> renderKycError()
                UiEvent.EVENT_RENDER_GLOBAL_ERROR_PROMO_INELIGIBLE -> renderIneligiblePromoError()
            }
        })
    }

    private fun renderPromoPage() {
        viewBinding?.let {
            it.layoutGlobalErrorPurchasePromo.gone()
            it.recyclerViewPurchasePromo.show()
            it.totalAmountPurchasePromo.show()
        }
    }

    private fun renderTotalAmount(fragmentUiModel: TokoFoodPromoFragmentUiModel) {
        viewBinding?.let {
            it.totalAmountPurchasePromo.amountCtaView.isEnabled = true
            it.totalAmountPurchasePromo.setCtaText("Pakai Promo (${fragmentUiModel.promoCount})")
            it.totalAmountPurchasePromo.setLabelTitle("Kamu bisa hemat")
            val totalAmountString = CurrencyFormatUtil.convertPriceValueToIdrFormat(fragmentUiModel.promoAmount, false).removeDecimalSuffix()
            it.totalAmountPurchasePromo.setAmount(totalAmountString)
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

    private fun renderKycError() {
        viewBinding?.let {
            it.layoutGlobalErrorPurchasePromo.show()
            it.recyclerViewPurchasePromo.gone()
            it.layoutGlobalErrorPurchasePromo.setType(GlobalError.SERVER_ERROR)
            // Todo : set data from API
            it.layoutGlobalErrorPurchasePromo.errorTitle.text = ""
            it.layoutGlobalErrorPurchasePromo.errorDescription.text = ""
            it.layoutGlobalErrorPurchasePromo.errorIllustration.setImage("", 0f)
            it.layoutGlobalErrorPurchasePromo.setActionClickListener {
                val intent = RouteManager.getIntent(context, ApplinkConstInternalGlobal.ADD_PHONE)
                startActivityForResult(intent, REQUEST_CODE_KYC)
            }
        }
    }

    private fun renderIneligiblePromoError() {
        viewBinding?.let {
            it.layoutGlobalErrorPurchasePromo.show()
            it.recyclerViewPurchasePromo.gone()
            it.layoutGlobalErrorPurchasePromo.setType(GlobalError.SERVER_ERROR)
            // Todo : set data from API
            it.layoutGlobalErrorPurchasePromo.errorTitle.text = ""
            it.layoutGlobalErrorPurchasePromo.errorDescription.text = ""
            it.layoutGlobalErrorPurchasePromo.errorIllustration.setImage("", 0f)
            it.layoutGlobalErrorPurchasePromo.setActionClickListener {
                (activity as BaseTokofoodActivity).onBackPressed()
            }
        }
    }

    private fun getGlobalErrorType(throwable: Throwable): Int {
        return if (throwable is UnknownHostException || throwable is SocketTimeoutException || throwable is ConnectException) {
            GlobalError.NO_CONNECTION
        } else {
            GlobalError.SERVER_ERROR
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CODE_KYC -> {

            }
        }
    }

    override fun onClickUnavailablePromoItem() {
        view?.let {
            Toaster.build(it, "Kupon yang terpasang otomatis tidak bisa diubah.").show()
        }
    }

}