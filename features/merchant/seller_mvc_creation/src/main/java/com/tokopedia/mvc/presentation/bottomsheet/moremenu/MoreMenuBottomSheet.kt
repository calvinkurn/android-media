package com.tokopedia.mvc.presentation.bottomsheet.moremenu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.mvc.databinding.SmvcBottomsheetThreeDotsMenuBinding
import com.tokopedia.mvc.di.component.DaggerMerchantVoucherCreationComponent
import com.tokopedia.mvc.domain.entity.Voucher
import com.tokopedia.mvc.domain.entity.enums.VoucherStatus
import com.tokopedia.mvc.presentation.bottomsheet.adapter.MoreMenuAdapter
import com.tokopedia.mvc.presentation.bottomsheet.viewmodel.MoreMenuViewModel
import com.tokopedia.mvc.presentation.list.model.MoreMenuUiModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class MoreMenuBottomSheet : BottomSheetUnify() {

    private var context: FragmentActivity? = null
    private var voucher: Voucher? = null
    private var menuItem: List<MoreMenuUiModel> = emptyList()
    private var sheetTitle: String = ""
    private var binding by autoClearedNullable<SmvcBottomsheetThreeDotsMenuBinding>()
    private var adapter: MoreMenuAdapter? = null
    private var voucherStatus: VoucherStatus? = null
    private var isFromVoucherDetailPage: Boolean = false
    private var isFromRecurringBottomSheet: Boolean = false
    init {
        isFullpage = false
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: MoreMenuViewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProvider(this, viewModelFactory).get(MoreMenuViewModel::class.java)
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
        binding?.recyclerView?.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        this.menuItem = viewModel.getMenuList(
            voucher,
            isFromVoucherDetailPage,
            voucherStatus,
            isFromRecurringBottomSheet
        )

        adapter?.clearAllElements()
        adapter?.addElement(menuItem)

        binding?.recyclerView?.adapter = adapter
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun initInjector() {
        DaggerMerchantVoucherCreationComponent.builder()
            .baseAppComponent(
                (activity?.applicationContext as? BaseMainApplication)?.baseAppComponent
            )
            .build()
            .inject(this)
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
            isFromVoucherDetailPage: Boolean = false,
            voucherStatus: VoucherStatus = VoucherStatus.PROCESSING,
            isFromRecurringBottomSheet: Boolean = false
        ): MoreMenuBottomSheet {
            return MoreMenuBottomSheet().apply {
                this.context = context
                this.voucher = voucher
                this.sheetTitle = title
                this.isFromVoucherDetailPage = isFromVoucherDetailPage
                this.voucherStatus = voucherStatus
                this.isFromRecurringBottomSheet = isFromRecurringBottomSheet
            }
        }
    }
}
