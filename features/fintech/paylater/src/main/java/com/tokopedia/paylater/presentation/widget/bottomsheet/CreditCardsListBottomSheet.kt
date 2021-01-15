package com.tokopedia.paylater.presentation.widget.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.paylater.INTERNAL_URL
import com.tokopedia.paylater.R
import com.tokopedia.paylater.domain.model.CreditCardItem
import com.tokopedia.paylater.presentation.adapter.CreditCardListAdapter
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.floatingbutton.FloatingButtonItem
import kotlinx.android.synthetic.main.base_list_bottomsheet_widget.*


class CreditCardsListBottomSheet : BottomSheetUnify() {

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

    private val childLayoutRes = R.layout.base_list_bottomsheet_widget
    private var creditCardList: ArrayList<CreditCardItem> = arrayListOf()
    private var bankName: String? = ""
    private var bankSlug: String? = ""

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
                    context.getString(R.string.credit_card_view_more)
            ) {
                openUrlWebView("${INTERNAL_URL}bank/$bankSlug")
            }))
        }
    }

    private fun getArgumentData() {
        arguments?.let {
            creditCardList = it.getParcelableArrayList(CREDIT_CARD_DATA) ?: arrayListOf()
            bankName = it.getString(BANK_NAME)
            bankSlug = it.getString(BANK_SLUG)
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
        val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        baseList.adapter = CreditCardListAdapter(creditCardList, bankName) { cardSlug ->
            openUrlWebView("${INTERNAL_URL}bank/${bankSlug}/${cardSlug}")
        }
        baseList.layoutManager = linearLayoutManager
    }


    private fun setDefaultParams() {
        setTitle("Kartu kredit Citibank")
        isDragable = true
        isHideable = true
        showCloseIcon = true
        showHeader = true
        customPeekHeight = getScreenHeight() / 2
    }

    private fun openUrlWebView(urlString: String) {
        val webViewAppLink = ApplinkConst.WEBVIEW + "?url=" + urlString
        RouteManager.route(context, webViewAppLink)
    }

    companion object {
        const val CREDIT_CARD_DATA = "CREDIT_DATA"
        const val BANK_NAME = "BANK_NAME"
        const val BANK_SLUG = "SLUG"
        private const val TAG = "FT_TAG"

        fun show(bundle: Bundle, childFragmentManager: FragmentManager) {
            val creditCardsListBottomSheet = CreditCardsListBottomSheet().apply {
                arguments = bundle
            }
            creditCardsListBottomSheet.show(childFragmentManager, TAG)
        }
    }
}