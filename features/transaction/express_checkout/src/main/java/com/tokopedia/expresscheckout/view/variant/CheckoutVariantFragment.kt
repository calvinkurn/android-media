package com.tokopedia.expresscheckout.view.variant

import android.content.Context
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.design.component.ToasterError
import com.tokopedia.design.component.Tooltip
import com.tokopedia.expresscheckout.R
import com.tokopedia.expresscheckout.view.variant.adapter.CheckoutVariantAdapter
import com.tokopedia.expresscheckout.view.variant.adapter.CheckoutVariantAdapterTypefactory

/**
 * Created by Irfan Khoirul on 30/11/18.
 */

class CheckoutVariantFragment : BaseListFragment<Visitable<*>, CheckoutVariantAdapterTypefactory>(),
        CheckoutVariantContract.View, CheckoutVariantActionListener {

    val contextView: Context get() = activity!!
    lateinit var list: List<Visitable<*>>
    lateinit var presenter: CheckoutVariantPresenter
    lateinit var adapter: CheckoutVariantAdapter
    var isDataLoaded = false

    companion object {
        fun createInstance(): CheckoutVariantFragment {
            return CheckoutVariantFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        presenter = CheckoutVariantPresenter()
        presenter.attachView(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_detail_product_page, container, false)
        getRecyclerView(view).addItemDecoration(CheckoutVariantItemDecorator())

        return view
    }

    override fun getAdapterTypeFactory(): CheckoutVariantAdapterTypefactory {
        return CheckoutVariantAdapterTypefactory(this)
    }

    override fun onItemClicked(t: Visitable<*>?) {

    }

    override fun onNeedToNotifySingleItem(position: Int) {
        adapter.notifyItemChanged(position)
    }

    override fun onNeedToNotifyAllItem() {
        adapter.notifyDataSetChanged()
    }

    override fun onClickEditProfile() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onClickEditDuration() {

    }

    override fun onClickEditCourier() {

    }

    override fun onClickInsuranceInfo(insuranceInfo: String) {
        if (activity != null) {
            val tooltip = Tooltip(contextView)
            tooltip.setTitle(contextView.getString(R.string.title_bottomsheet_insurance))
            tooltip.setDesc(insuranceInfo)
            tooltip.setTextButton(contextView.getString(R.string.label_button_bottomsheet_close))
            tooltip.setIcon(R.drawable.ic_insurance)
            tooltip.btnAction.setOnClickListener {
                tooltip.dismiss()
            }
            tooltip.show()
        }
    }

    override fun getScreenName(): String? {
        return null
    }

    override fun initInjector() {

    }

    override fun loadData(page: Int) {
        if (!isDataLoaded) {
            presenter.loadData()
        }
    }

    override fun showToasterError(message: String?) {
        ToasterError.make(view, message
                ?: contextView.getString(R.string.default_request_error_unknown), Snackbar.LENGTH_LONG).show()
    }

    override fun showData(arrayList: ArrayList<Visitable<*>>) {
        hideLoading()
        adapter.clearAllElements()
        adapter.addDataViewModel(arrayList)
        adapter.notifyDataSetChanged()
    }

    override fun createAdapterInstance(): BaseListAdapter<Visitable<*>, CheckoutVariantAdapterTypefactory> {
        adapter = CheckoutVariantAdapter(adapterTypeFactory)
        return adapter
    }

    override fun showGetListError(t: Throwable?) {
        super.showGetListError(t)
    }

    override fun getActivityContext(): Context? {
        return activity
    }
}