package com.tokopedia.pms.paymentlist.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.pms.R
import com.tokopedia.pms.paymentlist.domain.data.BasePaymentModel
import com.tokopedia.pms.paymentlist.domain.data.TransactionActionModel
import com.tokopedia.pms.paymentlist.presentation.adapter.PaymentTransactionActionAdapter
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.toDp
import kotlinx.android.synthetic.main.pms_base_recycler_bottom_sheet.*

class PaymentTransactionActionSheet: BottomSheetUnify() {
    private val childLayoutRes = R.layout.pms_base_recycler_bottom_sheet
    private var actionList: ArrayList<TransactionActionModel> = arrayListOf()
    private lateinit var model: BasePaymentModel
    private var sheetTitle: String = "Lainnya"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getArgumentData()
        setDefaultParams()
        initBottomSheet()
    }

    private fun getArgumentData() {

    }

    private fun setDefaultParams() {
        setTitle(sheetTitle)
        isDragable = true
        isHideable = true
        showCloseIcon = true
        showHeader = true
        customPeekHeight = (getScreenHeight() / 2).toDp()    }

    private fun initBottomSheet() {
        val childView = LayoutInflater.from(context).inflate(childLayoutRes,
            null, false)
        setChild(childView)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
    }

    private fun initAdapter() {
        baseRecyclerView.adapter = PaymentTransactionActionAdapter(arrayListOf(), {

        })
        baseRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    companion object {
        private const val TAG = "PaymentTransactionActionSheet"
        fun show(bundle: Bundle, childFragmentManager: FragmentManager) {
            val fragment = PaymentTransactionActionSheet().apply {
                arguments = bundle
            }
            fragment.show(childFragmentManager, TAG)
        }
    }
}