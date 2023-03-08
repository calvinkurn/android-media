package com.tokopedia.mvc.presentation.bottomsheet.moremenu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.campaign.utils.extension.showToasterError
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.mvc.databinding.SmvcBottomsheetThreeDotsMenuBinding
import com.tokopedia.mvc.di.component.DaggerMerchantVoucherCreationComponent
import com.tokopedia.mvc.domain.entity.Voucher
import com.tokopedia.mvc.domain.entity.enums.PromoType
import com.tokopedia.mvc.domain.entity.enums.VoucherStatus
import com.tokopedia.mvc.presentation.bottomsheet.adapter.MoreMenuAdapter
import com.tokopedia.mvc.presentation.bottomsheet.viewmodel.MoreMenuViewModel
import com.tokopedia.mvc.presentation.list.model.MoreMenuUiModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class MoreMenuBottomSheet : BottomSheetUnify() {

    private var context: FragmentActivity? = null
    private var voucher: Voucher? = null
    private var sheetTitle: String = ""
    private var binding by autoClearedNullable<SmvcBottomsheetThreeDotsMenuBinding>()
    private var adapter: MoreMenuAdapter? = null
    private var voucherStatus: VoucherStatus? = null
    private var pageSource: String = ""
    init {
        isFullpage = false
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: MoreMenuViewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProvider(this, viewModelFactory).get(MoreMenuViewModel::class.java)
    }

    private fun initInjector() {
        DaggerMerchantVoucherCreationComponent.builder()
            .baseAppComponent(
                (activity?.applicationContext as? BaseMainApplication)?.baseAppComponent
            )
            .build()
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SmvcBottomsheetThreeDotsMenuBinding.inflate(LayoutInflater.from(context))
        initInjector()
        setChild(binding?.root)
        setTitle(sheetTitle)

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeVoucherCreationMetadata()
        viewModel.getVoucherCreationMetadata()
    }

    private fun observeVoucherCreationMetadata() {
        viewModel.voucherCreationMetadata.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Success -> {
                    binding?.loader?.gone()
                    setupRecyclerView(result.data.discountActive)
                }
                is Fail -> {
                    binding?.loader?.gone()
                    binding?.root?.showToasterError(result.throwable)
                }
            }
        }
    }

    private fun setupRecyclerView(isDiscountPromoTypeEnabled: Boolean) {
        binding?.recyclerView?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        val isDiscountPromoType = voucher?.type == PromoType.DISCOUNT.id

        val menuItem = viewModel.getMenuList(
            voucher,
            voucherStatus,
            pageSource,
            isDiscountPromoTypeEnabled,
            isDiscountPromoType
        )

        adapter?.clearAllElements()
        adapter?.addElement(menuItem)

        binding?.recyclerView?.adapter = adapter
    }



    fun setOnMenuClickListener(callback: (MoreMenuUiModel) -> Unit) {
        this.adapter = MoreMenuAdapter(callback)
    }

    companion object {
        @JvmStatic
        fun newInstance(
            context: FragmentActivity,
            voucher: Voucher?,
            title: String = "",
            pageSource: String = "",
            voucherStatus: VoucherStatus = VoucherStatus.PROCESSING
        ): MoreMenuBottomSheet {
            return MoreMenuBottomSheet().apply {
                this.context = context
                this.voucher = voucher
                this.sheetTitle = title
                this.voucherStatus = voucherStatus
                this.pageSource = pageSource
            }
        }
    }
}
