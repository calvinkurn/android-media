package com.tokopedia.chatbot.attachinvoice.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.base.view.recyclerview.VerticalRecyclerView
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.attachinvoice.di.DaggerAttachInvoiceComponent
import com.tokopedia.chatbot.attachinvoice.view.AttachInvoiceContract
import com.tokopedia.chatbot.attachinvoice.view.adapter.AttachInvoiceListAdapter
import com.tokopedia.chatbot.attachinvoice.view.adapter.AttachInvoiceListAdapterTypeFactory
import com.tokopedia.chatbot.attachinvoice.view.model.InvoiceViewModel
import com.tokopedia.chatbot.attachinvoice.view.presenter.AttachInvoicePresenter
import com.tokopedia.chatbot.attachinvoice.view.resultmodel.SelectedInvoice
import com.tokopedia.chatbot.view.ChatbotInternalRouter
import javax.inject.Inject

/**
 * Created by Hendri on 22/03/18.
 */

class AttachInvoiceFragment : BaseListFragment<InvoiceViewModel, AttachInvoiceListAdapterTypeFactory>(), AttachInvoiceContract.View {

    @Inject
    lateinit var presenter: AttachInvoicePresenter

    lateinit var activity: AttachInvoiceContract.Activity
    private var swipeRefreshLayout: SwipeRefreshLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        swipeRefreshLayout = view?.findViewById(R.id.swipe_refresh_layout)
        val recyclerView = super.getRecyclerView(view)
        if (recyclerView is VerticalRecyclerView) {
            recyclerView.clearItemDecoration()
        }
        return view
    }

    fun setActivityContract(activityContract: AttachInvoiceContract.Activity) {
        this.activity = activityContract
    }

    override fun getScreenName(): String {
        return "Attach Invoice"
    }

    override fun initInjector() {
        if ((activity as Activity).application != null) {
            val appComponent = ((activity as Activity).application as BaseMainApplication)
                    .baseAppComponent
            val daggerAttachInvoiceComponent = DaggerAttachInvoiceComponent.builder()
                    .baseAppComponent(appComponent).build() as DaggerAttachInvoiceComponent
            daggerAttachInvoiceComponent.inject(this)
            presenter.attachView(this)
            presenter.attachActivityContract(activity)
        }
    }

    override fun onItemClicked(invoiceViewModel: InvoiceViewModel) {
        val data = Intent()
        data.putExtra(ChatbotInternalRouter.Companion
                .TOKOPEDIA_ATTACH_INVOICE_SELECTED_INVOICE_KEY, SelectedInvoice
        (invoiceViewModel))
        getActivity()!!.setResult(Activity.RESULT_OK, data)
        getActivity()!!.finish()
    }

    override fun loadData(page: Int) {
        //Query search are disabled for this release therefore sending empty string
        presenter!!.loadInvoiceData("", activity.userId, page, activity.messageId,
                context!!)
    }

    override fun getAdapterTypeFactory(): AttachInvoiceListAdapterTypeFactory {
        return AttachInvoiceListAdapterTypeFactory()
    }

    override fun createAdapterInstance(): BaseListAdapter<InvoiceViewModel, AttachInvoiceListAdapterTypeFactory> {
        return AttachInvoiceListAdapter(adapterTypeFactory, this)
    }

    override fun addInvoicesToList(invoices: List<InvoiceViewModel>, hasNextPage: Boolean) {
        renderList(invoices, hasNextPage)
    }

    override fun hideAllLoadingIndicator() {
        swipeRefreshLayout!!.isRefreshing = false
        super.hideLoading()
    }

    override fun showErrorMessage(throwable: Throwable) {
        throwable.printStackTrace()
        hideAllLoadingIndicator()
    }

    override fun getSwipeRefreshLayout(view: View): SwipeRefreshLayout? {
        return swipeRefreshLayout
    }

    override fun onDestroy() {
        if (presenter != null) presenter!!.detachView()
        super.onDestroy()
    }

    companion object {

        fun newInstance(checkedUIView: AttachInvoiceContract.Activity): AttachInvoiceFragment {
            val args = Bundle()
            val fragment = AttachInvoiceFragment()
            fragment.setActivityContract(checkedUIView)
            fragment.arguments = args
            return fragment
        }
    }
}
