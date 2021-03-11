package com.tokopedia.chatbot.attachinvoice.view

import TransactionInvoiceListFragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
import androidx.viewpager.widget.PagerAdapter
import com.tokopedia.abstraction.base.view.widget.TouchViewPager
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.attachinvoice.domain.model.InvoiceConstants.VALUE_PEMBELIAN
import com.tokopedia.chatbot.attachinvoice.domain.model.InvoiceConstants.VALUE_PENARIKAN_DANA
import com.tokopedia.chatbot.attachinvoice.domain.model.InvoiceConstants.VALUE_PENJUALAN
import com.tokopedia.chatbot.view.adapter.ChatBotViewPagerAdapter
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.TabsUnify
import com.tokopedia.unifycomponents.UnifyButton

private const val TITLE_PURCHASE = "Pembelian"
private const val TITLE_SALES = "Penjualan"
private const val TITLE_WITHDRAWING_FUNDS = "Penarikan Dana"

class TransactionInvoiceBottomSheet : BottomSheetUnify(), TransactionInvoiceListFragmentListener {
    private lateinit var tabsUnify: TabsUnify
    private lateinit var touchViewPager: TouchViewPager
    private lateinit var submitButton: UnifyButton
    private lateinit var context: FragmentActivity
    private var messageId: Int = 0

    init {
        isFullpage = true
    }

    companion object {
        @JvmStatic
        fun newInstance(context: FragmentActivity, messageId: Int): TransactionInvoiceBottomSheet {
            return TransactionInvoiceBottomSheet().apply {
                this.context = context
                this.messageId = messageId
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val contentView = View.inflate(context, R.layout.bottomsheet_transaction_invoice, null)
        setChild(contentView)
        contentView.run {
            tabsUnify = findViewById(R.id.transaction_invoice_tab_layout)
            touchViewPager = findViewById(R.id.transaction_invoice_view_pager)
            submitButton = findViewById(R.id.transaction_invoice_btn_submit)
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTitle(getString(R.string.string_attach_invoice_activity_title))
        renderTabAndViewPager()
        submitButton.setOnClickListener {

        }
    }

    private fun renderTabAndViewPager() {
        touchViewPager.adapter = getViewPagerAdapter()
        touchViewPager.offscreenPageLimit = 3
        tabsUnify.addNewTab(TITLE_PURCHASE)
        tabsUnify.addNewTab(TITLE_SALES)
        tabsUnify.addNewTab(TITLE_WITHDRAWING_FUNDS)
        tabsUnify.setupWithViewPager(touchViewPager)
    }

    private fun getViewPagerAdapter(): PagerAdapter {
        val list: ArrayList<Fragment> = ArrayList()
        list.add(TransactionInvoiceListFragment.newInstance(messageId, VALUE_PEMBELIAN, this))
        list.add(TransactionInvoiceListFragment.newInstance(messageId, VALUE_PENJUALAN, this))
        list.add(TransactionInvoiceListFragment.newInstance(messageId, VALUE_PENARIKAN_DANA, this))
        return ChatBotViewPagerAdapter(arrayOf(TITLE_PURCHASE, TITLE_SALES, TITLE_WITHDRAWING_FUNDS),
                childFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT).apply {
            setData(list)
        }
    }

    override fun getButtonView(): UnifyButton {
        return submitButton
    }
}

interface TransactionInvoiceListFragmentListener {
    fun getButtonView(): UnifyButton
}
