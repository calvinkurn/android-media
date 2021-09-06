package com.tokopedia.home_account.view.fragment

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.home_account.AccountConstants
import com.tokopedia.home_account.R
import com.tokopedia.home_account.databinding.FundsAndInvestmentFragmentBinding
import com.tokopedia.home_account.di.HomeAccountUserComponents
import com.tokopedia.home_account.view.HomeAccountUserViewModel
import com.tokopedia.home_account.view.activity.HomeAccountUserActivity
import com.tokopedia.home_account.view.adapter.HomeAccountFundsAndInvestmentAdapter
import com.tokopedia.home_account.view.adapter.uimodel.SubtitleUiModel
import com.tokopedia.home_account.view.adapter.uimodel.TitleUiModel
import com.tokopedia.home_account.view.adapter.uimodel.WalletUiModel
import com.tokopedia.home_account.view.listener.WalletListener
import com.tokopedia.home_account.view.listener.onAppBarCollapseListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.utils.view.DarkModeUtil.isDarkMode
import com.tokopedia.utils.view.binding.noreflection.viewBinding
import javax.inject.Inject


class FundsAndInvestmentFragment : BaseDaggerFragment(), WalletListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModelFragmentProvider by lazy { ViewModelProviders.of(this, viewModelFactory) }
    private val viewModel by lazy { viewModelFragmentProvider.get(HomeAccountUserViewModel::class.java) }

    private val binding by viewBinding(FundsAndInvestmentFragmentBinding::bind)
    private var adapter: HomeAccountFundsAndInvestmentAdapter? = null

    var appBarCollapseListener: onAppBarCollapseListener? = null

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(HomeAccountUserComponents::class.java).inject(this)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is HomeAccountUserActivity) appBarCollapseListener = context
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.funds_and_investment_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun onClickWallet(type: String) {
        when (type) {
            AccountConstants.WALLET.OVO -> {
            }
            AccountConstants.WALLET.GOPAY -> {
            }
        }
    }

    private fun initView() {
//        setupStatusBar()
//        setupToolbarTransition()
        setupAdapter()
        setupSwipeRefresh()

        hideLoading()
        addTitleView()
        addWalletView(listOf(WalletUiModel("Saldo Tokopedia", "Rp.200.000.000", AccountConstants.Url.SALDO_ICON, false, "", AccountConstants.WALLET.SALDO, false)))
        addSubtitleView()
        addWalletView(listOf(WalletUiModel("Saldo Tokopedia", "Gagal memuat", AccountConstants.Url.SALDO_ICON, true, "", AccountConstants.WALLET.SALDO, true)))
    }

//    private fun setupStatusBar() {
//        activity?.let {
//            binding?.statusBarBg?.background = ColorDrawable(
//                ContextCompat.getColor(it, com.tokopedia.unifyprinciples.R.color.Unify_G500)
//            )
//        }
//        binding?.statusBarBg?.layoutParams?.height = ViewHelper.getStatusBarHeight(activity)
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            binding?.statusBarBg?.visibility = View.INVISIBLE
//        } else {
//            binding?.statusBarBg?.visibility = View.VISIBLE
//        }
//        setStatusBarAlpha(0f)
//    }
//
//    private fun setStatusBarAlpha(alpha: Float) {
//        val drawable = binding?.statusBarBg?.background
//        drawable?.alpha = alpha.toInt()
//        binding?.statusBarBg?.background = drawable
//    }

    private fun setupAdapter() {
        context?.let { context ->
            adapter = HomeAccountFundsAndInvestmentAdapter(this)
            binding?.fundsAndInvestmentRv?.adapter = adapter
            val layoutManager = LinearLayoutManager(
                binding?.fundsAndInvestmentRv?.context,
                LinearLayoutManager.VERTICAL,
                false
            )
            val verticalDivider =
                ContextCompat.getDrawable(context, R.drawable.horizontal_devider)
            if (context.isDarkMode()) {
                verticalDivider?.mutate()?.setColorFilter(
                    ContextCompat.getColor(context, R.color.vertical_divider_dms_dark),
                    PorterDuff.Mode.SRC_IN
                )
            } else {
                verticalDivider?.mutate()?.setColorFilter(
                    ContextCompat.getColor(context, R.color.vertical_divider_dms_light),
                    PorterDuff.Mode.SRC_IN
                )
            }
            val dividerItemDecoration = object : DividerItemDecoration(
                context,
                layoutManager.orientation
            ) {
                override fun getItemOffsets(
                    outRect: Rect,
                    view: View,
                    parent: RecyclerView,
                    state: RecyclerView.State
                ) {
                    val position: Int = parent.getChildAdapterPosition(view)
                    when (adapter?.getItem(position)) {
                        is TitleUiModel, is SubtitleUiModel -> {
                            outRect.setEmpty()
                        }
                        is WalletUiModel -> {
                            super.getItemOffsets(outRect, view, parent, state)
                        }
                    }
                }
            }

            verticalDivider?.run {
                dividerItemDecoration.setDrawable(this)
            }

            binding?.fundsAndInvestmentRv?.itemDecorationCount?.let {
                if (it < 1) {
                    binding?.fundsAndInvestmentRv?.addItemDecoration(dividerItemDecoration)
                }
            }
            binding?.fundsAndInvestmentRv?.layoutManager = layoutManager
        }
    }

//    private fun setupToolbarTransition() {
//        binding?.homeAccountUserToolbar?.let {
//            NavRecyclerViewScrollListener(
//                navToolbar = it,
//                startTransitionPixel = 200,
//                toolbarTransitionRangePixel = 50,
//                navScrollCallback = object : NavRecyclerViewScrollListener.NavScrollCallback {
//                    override fun onAlphaChanged(offsetAlpha: Float) {
//                        setStatusBarAlpha(offsetAlpha)
//                    }
//
//                    override fun onSwitchToDarkToolbar() {
//                        binding?.homeAccountUserToolbar?.switchToLightToolbar()
//                    }
//
//                    override fun onSwitchToLightToolbar() {
//                    }
//
//                    override fun onYposChanged(yOffset: Int) {
//                    }
//                }
//            )
//        }?.let {
//            binding?.fundsAndInvestmentRv?.addOnScrollListener(it)
//        }
//    }

    private fun setupSwipeRefresh() {
        binding?.fundsAndInvestmentRv?.swipeLayout = binding?.homeAccountUserFragmentSwipeRefresh
        binding?.homeAccountUserFragmentSwipeRefresh?.setOnRefreshListener {
            onRefresh()
            binding?.homeAccountUserFragmentSwipeRefresh?.isRefreshing = false
        }
    }

    private fun addTitleView() {
        adapter?.addItemAndAnimateChanges(TitleUiModel(getString(R.string.funds_and_investment_balance_and_points)))
    }

    private fun addSubtitleView() {
        adapter?.addItemAndAnimateChanges(SubtitleUiModel(getString(R.string.funds_and_investment_try_another)))
    }

    private fun addWalletView(walletUiModel: List<WalletUiModel>) {
        adapter?.addItemsAndAnimateChanges(walletUiModel)
    }

    private fun onRefresh() {
        showLoading()
        adapter?.clearAllItems()
    }

    private fun showLoading() {
        binding?.fundsAndInvestmentShimmer?.root?.show()
        binding?.fundsAndInvestmentRv?.hide()
    }

    private fun hideLoading() {
        binding?.fundsAndInvestmentShimmer?.root?.hide()
        binding?.fundsAndInvestmentRv?.show()
    }

    companion object {
        fun newInstance(bundle: Bundle?): Fragment {
            return FundsAndInvestmentFragment().apply {
                arguments = bundle
            }
        }
    }
}