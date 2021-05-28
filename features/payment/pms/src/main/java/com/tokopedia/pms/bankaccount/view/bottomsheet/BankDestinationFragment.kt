package com.tokopedia.pms.bankaccount.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.pms.R
import com.tokopedia.pms.bankaccount.data.model.BankListModel
import com.tokopedia.pms.bankaccount.domain.BankListRepository
import com.tokopedia.pms.bankaccount.view.adapter.BankListAdapter
import com.tokopedia.pms.paymentlist.di.DaggerPmsComponent
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.toDp
import kotlinx.android.synthetic.main.pms_base_recycler_bottom_sheet.*
import javax.inject.Inject

/**
 * Created by zulfikarrahman on 7/5/18.
 */
class BankDestinationFragment : BottomSheetUnify() {

    @Inject
    lateinit var bankListRepository: BankListRepository
    private val childLayoutRes = R.layout.pms_base_recycler_bottom_sheet
    private var bankListCallback: OnBankSelectedListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
        setDefaultParams()
        initBottomSheet()
    }

    private fun initBottomSheet() {
        val childView = LayoutInflater.from(context).inflate(
            childLayoutRes,
            null, false
        )
        setChild(childView)
    }

    private fun initInjector() {
        val component =
            DaggerPmsComponent::class.java.cast((activity as (HasComponent<DaggerPmsComponent>)).component)
        component?.inject(this) ?: dismiss()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (::bankListRepository.isInitialized) {
            baseRecyclerView.adapter = BankListAdapter(bankListRepository.bankList) {
                bankListCallback?.onBankSelected(it)
                dismiss()
            }
            baseRecyclerView.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        } else dismiss()
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
        fun show(
            bankListCallback: OnBankSelectedListener,
            childFragmentManager: FragmentManager
        ): BankDestinationFragment {
            val fragment = BankDestinationFragment()
            fragment.bankListCallback = bankListCallback
            fragment.show(childFragmentManager, TAG)
            return fragment
        }
    }
}

interface OnBankSelectedListener {
    fun onBankSelected(bank: BankListModel)
}
