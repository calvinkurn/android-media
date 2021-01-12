package com.tokopedia.paylater.presentation.widget.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.paylater.R
import com.tokopedia.paylater.domain.model.CreditCardBank
import com.tokopedia.paylater.presentation.adapter.CreditCardRegistrationAdapter
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.floatingbutton.FloatingButtonItem
import kotlinx.android.synthetic.main.base_list_bottomsheet_widget.*


class CreditCardRegistrationBottomSheet : BottomSheetUnify() {

    init {
        setShowListener {
            bottomSheet.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onSlide(view: View, slideOffset: Float) {
                }

                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                        dismiss()
                    }
                }
            })
        }
    }

    private var listener: Listener? = null
    private val childLayoutRes = R.layout.base_list_bottomsheet_widget
    private var bankList: ArrayList<CreditCardBank> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getArgumentData()
        setDefaultParams()
        initBottomSheet()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initAdapter()
        fbViewAllCards.apply {
            visible()
            addItem(arrayListOf(FloatingButtonItem(
                    context.getString(R.string.credit_card_view_all_cards)
            ) {
                openUrlWebView("ccpage")
            }))
        }
    }


    private fun getArgumentData() {
        arguments?.let {
            bankList = it.getParcelableArrayList(CREDIT_CARD_BANK_DATA) ?: arrayListOf()
        }
    }

    private fun initBottomSheet() {
        val childView = LayoutInflater.from(context).inflate(childLayoutRes,
                null, false)
        setChild(childView)
    }

    private fun initAdapter() {
        baseList.setOnTouchListener { v, event ->
            v.parent.requestDisallowInterceptTouchEvent(true)
            v.onTouchEvent(event)
            true
        }
        baseList.adapter = CreditCardRegistrationAdapter(bankList) {
            listener?.showCreditCardList()
            dismiss()
        }
        baseList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    private fun setDefaultParams() {
        setTitle("Tersedia cicilan 6 bulan")
        isDragable = true
        isHideable = true
        showCloseIcon = true
        showHeader = true
        customPeekHeight = getScreenHeight() / 2
    }

    fun setActionListener(listener: Listener) {
        this.listener = listener
    }

    private fun openUrlWebView(urlString: String) {
        val webViewApplink = ApplinkConst.WEBVIEW + "?url=" + urlString
        RouteManager.route(context, webViewApplink)
    }

    companion object {
        const val CREDIT_CARD_BANK_DATA = "BANK_DATA"
        const val TAG = "FT_TAG"

        fun getInstance(bundle: Bundle): CreditCardRegistrationBottomSheet {
            return CreditCardRegistrationBottomSheet().apply {
                arguments = bundle
            }
        }
    }

    interface Listener {
        fun showCreditCardList()
    }
}