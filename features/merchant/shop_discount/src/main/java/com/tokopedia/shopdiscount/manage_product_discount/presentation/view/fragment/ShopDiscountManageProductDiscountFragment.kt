package com.tokopedia.shopdiscount.manage_product_discount.presentation.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.header.HeaderUnify
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.shopdiscount.R
import com.tokopedia.shopdiscount.bulk.domain.entity.DiscountSettings
import com.tokopedia.shopdiscount.bulk.presentation.DiscountBulkApplyBottomSheet
import com.tokopedia.shopdiscount.databinding.FragmentManageDiscountBinding
import com.tokopedia.shopdiscount.databinding.FragmentManageProductDiscountBinding
import com.tokopedia.shopdiscount.di.component.DaggerShopDiscountComponent
import com.tokopedia.shopdiscount.manage_discount.data.uimodel.ShopDiscountSetupProductUiModel
import com.tokopedia.shopdiscount.manage_discount.presentation.adapter.ShopDiscountManageDiscountAdapter
import com.tokopedia.shopdiscount.manage_discount.presentation.adapter.ShopDiscountManageDiscountTypeFactoryImpl
import com.tokopedia.shopdiscount.manage_discount.presentation.adapter.viewholder.ShopDiscountManageDiscountGlobalErrorViewHolder
import com.tokopedia.shopdiscount.manage_discount.presentation.adapter.viewholder.ShopDiscountSetupProductItemViewHolder
import com.tokopedia.shopdiscount.manage_discount.presentation.view.viewmodel.ShopDiscountManageDiscountViewModel
import com.tokopedia.shopdiscount.manage_discount.util.ShopDiscountManageDiscountMode
import com.tokopedia.shopdiscount.product_detail.presentation.ShopDiscountProductDetailDividerItemDecoration
import com.tokopedia.shopdiscount.utils.navigation.FragmentRouter
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class ShopDiscountManageProductDiscountFragment : BaseDaggerFragment(){

    companion object {
//        const val REQUEST_ID_ARG = "request_id_arg"
//        const val STATUS_ARG = "status_arg"
//        const val MODE_ARG = "mode_arg"
//        private const val URL_EDU_ABUSIVE_PRODUCT =
//            "https://seller.tokopedia.com/edu/ketentuan-baru-diskon-toko/"

        fun createInstance() =
            ShopDiscountManageProductDiscountFragment().apply {
//                arguments = Bundle().apply {
//                    putString(REQUEST_ID_ARG, requestId)
//                    putInt(STATUS_ARG, status)
//                    putString(MODE_ARG, mode)
//                }
            }
    }

    private var viewBinding by autoClearedNullable<FragmentManageProductDiscountBinding>()
    override fun getScreenName(): String =
        ShopDiscountManageProductDiscountFragment::class.java.canonicalName.orEmpty()

//    private var rvProductList: RecyclerView? = null
//    private var containerButtonSubmit: ViewGroup? = null
//    private var buttonSubmit: UnifyButton? = null
//    private var cardLabelBulkManage: CardUnify2? = null
//    private var bulkManageTitle: Typography? = null
//    private var headerUnify: HeaderUnify? = null
//    private var tickerAbusiveProducts: Ticker? = null


    override fun initInjector() {
        DaggerShopDiscountComponent.builder()
            .baseAppComponent((activity?.applicationContext as? BaseMainApplication)?.baseAppComponent)
            .build()
            .inject(this)
    }

//    @Inject
//    lateinit var viewModelFactory: ViewModelFactory

//    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
//    private val viewModel by lazy { viewModelProvider.get(ShopDiscountManageDiscountViewModel::class.java) }
//    private var requestId: String = ""
//    private var status: Int = -1
//    private var mode: String = ""

//    private val adapter by lazy {
//        ShopDiscountManageDiscountAdapter(
//            typeFactory = ShopDiscountManageDiscountTypeFactoryImpl(
//                this,
//                this
//            )
//        )
//    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentManageProductDiscountBinding.inflate(inflater, container, false)
        return viewBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }


}