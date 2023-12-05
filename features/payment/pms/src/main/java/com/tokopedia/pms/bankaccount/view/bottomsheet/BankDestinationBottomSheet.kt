package com.tokopedia.pms.bankaccount.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.pms.R
import com.tokopedia.pms.bankaccount.data.model.BankListModel
import com.tokopedia.pms.bankaccount.domain.BankListDataUseCase
import com.tokopedia.pms.bankaccount.view.activity.ChangeBankListener
import com.tokopedia.pms.bankaccount.view.adapter.BankListAdapter
import com.tokopedia.pms.databinding.PmsBaseRecyclerBottomSheetBinding
import com.tokopedia.pms.paymentlist.di.DaggerPmsComponent
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.toDp
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

/**
 * Created by zulfikarrahman on 7/5/18.
 */
class BankDestinationBottomSheet : BottomSheetUnify() {

    @Inject
    lateinit var bankListDataUseCase: BankListDataUseCase
    private val childLayoutRes = R.layout.pms_base_recycler_bottom_sheet

    private var binding by autoClearedNullable<PmsBaseRecyclerBottomSheetBinding>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
        setDefaultParams()
        initBottomSheet()
    }

    private fun initBottomSheet() {
        binding = PmsBaseRecyclerBottomSheetBinding.inflate(LayoutInflater.from(context))
        setChild(binding?.root)
    }

    private fun initInjector() {
        val component =
            DaggerPmsComponent::class.java.cast((activity as (HasComponent<DaggerPmsComponent>)).component)
        component?.inject(this) ?: dismiss()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initBottomSheet()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (::bankListDataUseCase.isInitialized) {
            binding?.run {
                baseRecyclerView.adapter = BankListAdapter(bankListDataUseCase.bankList) {
                    onDestinationBankSelected(it)
                    dismiss()
                }
                baseRecyclerView.layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            }
        } else {
            dismiss()
        }
    }

    private fun onDestinationBankSelected(bankListModel: BankListModel) {
        (activity as ChangeBankListener).onBankSelected(bankListModel)
    }

    private fun setDefaultParams() {
        setTitle(context?.getString(R.string.pms_title_bottomsheet_dest_bank) ?: "")
        isDragable = true
        isHideable = true
        showCloseIcon = true
        showHeader = true
        customPeekHeight = (getScreenHeight()).toDp()
    }

    companion object {
        private const val TAG = "BankDestinationFragment"
        fun show(childFragmentManager: FragmentManager): BankDestinationBottomSheet {
            val fragment = BankDestinationBottomSheet()
            fragment.show(childFragmentManager, TAG)
            return fragment
        }
    }
}
