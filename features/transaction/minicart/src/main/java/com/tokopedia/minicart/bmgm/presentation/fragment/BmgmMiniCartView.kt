package com.tokopedia.minicart.bmgm.presentation.fragment

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewTreeViewModelStoreOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.minicart.bmgm.common.di.DaggerBmgmComponent
import com.tokopedia.minicart.bmgm.domain.model.BmgmParamModel
import com.tokopedia.minicart.bmgm.presentation.adapter.BmgmMiniCartAdapter
import com.tokopedia.minicart.bmgm.presentation.adapter.itemdecoration.BmgmMiniCartItemDecoration
import com.tokopedia.minicart.bmgm.presentation.viewmodel.BmgmMiniCartViewModel
import com.tokopedia.minicart.databinding.FragmentBmgmMiniCartWidgetBinding
import com.tokopedia.minicart.databinding.ViewBmgmMiniCartSubTotalBinding
import com.tokopedia.purchase_platform.common.feature.bmgm.uimodel.BmgmCommonDataUiModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * Created by @ilhamsuaib on 31/07/23.
 */

class BmgmMiniCartView : ConstraintLayout, BmgmMiniCartAdapter.Listener,
    DefaultLifecycleObserver {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context, attrs, defStyleAttr
    )

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private var binding: FragmentBmgmMiniCartWidgetBinding? = null
    private val miniCartAdapter by lazy { BmgmMiniCartAdapter(this) }
    private val viewModel by lazy {
        ViewModelProvider(
            ViewTreeViewModelStoreOwner.get(this)!!,
            viewModelFactory
        )[BmgmMiniCartViewModel::class.java]
    }
    private var data: BmgmCommonDataUiModel? = null

    init {
        binding = FragmentBmgmMiniCartWidgetBinding.inflate(
            LayoutInflater.from(context), this, true
        )
        setAsLifecycleObserver()
        setupRecyclerView()
    }

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        initInjector()
        fetchMiniCartData()
    }

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        observeCartData()
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        binding = null
        data = null
    }

    override fun setOnItemClickedListener() {
        data?.let {
            RouteManager.route(context, ApplinkConstInternalGlobal.BMGM_MINI_CART)
        }
    }

    fun fetchMiniCartData() {
        getLifecycleOwner()?.lifecycleScope?.launchWhenStarted {
            val param = BmgmParamModel(0L, 0L, listOf())
            viewModel.getMiniCartData(param)
        }
    }

    private fun setAsLifecycleObserver() {
        getLifecycleOwner()?.lifecycle?.addObserver(this)
    }

    private fun observeCartData() {
        getLifecycleOwner()?.lifecycleScope?.launchWhenStarted {
            getLifecycleOwner()?.observe(viewModel.cartData) {
                when (it) {
                    is Success -> setOnSuccessGetCartData(it.data)
                    is Fail -> {
                        it.throwable.printStackTrace()
                    }
                }
            }
        }
    }

    private fun getLifecycleOwner() = (context as? LifecycleOwner)

    private fun setOnSuccessGetCartData(data: BmgmCommonDataUiModel) {
        this.data = data
        binding?.run {
            val checkoutView = ViewBmgmMiniCartSubTotalBinding.bind(root)
            if (data.products.isNotEmpty()) {
                tvBmgmCartDiscount.text = data.offerMessage.parseAsHtml()
                tvBmgmCartDiscount.visible()
                rvBmgmMiniCart.visible()
                checkoutView.bmgmBtnOpenCart.isEnabled = true
            } else {
                tvBmgmCartDiscount.gone()
                rvBmgmMiniCart.gone()
                checkoutView.bmgmBtnOpenCart.isEnabled = false
            }

            miniCartAdapter.data.clear()
            miniCartAdapter.data.addAll(data.products)
            val lastIndex = miniCartAdapter.itemCount.minus(Int.ONE)
            miniCartAdapter.notifyItemRangeChanged(Int.ZERO, lastIndex)
        }
    }

    private fun setupRecyclerView() {
        binding?.rvBmgmMiniCart?.run {
            addItemDecoration(BmgmMiniCartItemDecoration())
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = miniCartAdapter
        }
    }

    private fun initInjector() {
        DaggerBmgmComponent.builder()
            .baseAppComponent((context.applicationContext as BaseMainApplication).baseAppComponent)
            .build().inject(this)
    }
}