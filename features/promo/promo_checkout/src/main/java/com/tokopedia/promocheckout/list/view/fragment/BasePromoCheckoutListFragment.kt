package com.tokopedia.promocheckout.list.view.fragment

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.promocheckout.R
import com.tokopedia.promocheckout.common.di.PromoCheckoutModule
import com.tokopedia.promocheckout.list.di.DaggerPromoCheckoutListComponent
import com.tokopedia.promocheckout.list.di.PromoCheckoutListModule
import com.tokopedia.promocheckout.list.model.listcoupon.PromoCheckoutListModel
import com.tokopedia.promocheckout.list.model.listlastseen.PromoCheckoutLastSeenModel
import com.tokopedia.promocheckout.list.view.adapter.PromoCheckoutListAdapterFactory
import com.tokopedia.promocheckout.list.view.adapter.PromoCheckoutListViewHolder
import com.tokopedia.promocheckout.list.view.adapter.PromoLastSeenAdapter
import com.tokopedia.promocheckout.list.view.adapter.PromoLastSeenViewHolder
import com.tokopedia.promocheckout.list.view.presenter.PromoCheckoutListContract
import com.tokopedia.promocheckout.list.view.presenter.PromoCheckoutListPresenter
import kotlinx.android.synthetic.main.fragment_promo_checkout_list.*
import kotlinx.android.synthetic.main.fragment_promo_checkout_list.view.*
import javax.inject.Inject

abstract class BasePromoCheckoutListFragment : BaseListFragment<PromoCheckoutListModel, PromoCheckoutListAdapterFactory>(),
        PromoCheckoutListContract.View, PromoLastSeenViewHolder.ListenerLastSeen, PromoCheckoutListViewHolder.ListenerTrackingCoupon {

    @Inject
    lateinit var promoCheckoutListPresenter: PromoCheckoutListPresenter
    val promoLastSeenAdapter: PromoLastSeenAdapter by lazy { PromoLastSeenAdapter(ArrayList(), this) }

    abstract var serviceId : String
    open var categoryId : Int = 0
    open var isCouponActive : Boolean = true
    open var promoCode : String = ""

    override fun getAdapterTypeFactory(): PromoCheckoutListAdapterFactory {
        return PromoCheckoutListAdapterFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        promoCheckoutListPresenter.attachView(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_promo_checkout_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
    }

    override fun getEmptyDataViewModel(): Visitable<*> {
        val emptyModel = EmptyModel()
        emptyModel.iconRes = R.drawable.ic_empty_promo_list_checkout
        emptyModel.content = getString(R.string.promo_label_empty_list)
        emptyModel.title = getString(R.string.promo_title_empty_list)
        return emptyModel
    }

    fun initView(view: View) {
        val dividerItemDecoration = DividerItemDecoration(context, DividerItemDecoration.HORIZONTAL)
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(context!!, R.drawable.divider_horizontal_custom_quick_filter)!!)
        view.recyclerViewLastSeenPromo.addItemDecoration(dividerItemDecoration)
        view.recyclerViewLastSeenPromo.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        view.recyclerViewLastSeenPromo.adapter = promoLastSeenAdapter

        val linearDividerItemDecoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        linearDividerItemDecoration.setDrawable(ContextCompat.getDrawable(context!!, R.drawable.divider_vertical_list_promo)!!)
        getRecyclerView(view).addItemDecoration(linearDividerItemDecoration)

        populateLastSeen()
        buttonUse.setOnClickListener {
            onPromoCodeUse(textInputCoupon.text.toString())
        }
        if(isCouponActive){
            getRecyclerView(view).visibility = View.VISIBLE
        }else{
            getRecyclerView(view).visibility = View.GONE
        }
    }

    abstract fun onPromoCodeUse(promoCode: String)

    /* hold cos api not ready yet
       promoCheckoutListPresenter.getListLastSeen(resources)
    */
    override fun onClickItemLastSeen(promoCheckoutLastSeenModel: PromoCheckoutLastSeenModel) {

    }

    /* hold cos api not ready yet
       promoCheckoutListPresenter.getListLastSeen(resources)
    */
    override fun showGetListLastSeenError(e: Throwable) {
        populateLastSeen()
        NetworkErrorHelper.showRedCloseSnackbar(activity, ErrorHandler.getErrorMessage(activity, e))
    }

    override fun renderListLastSeen(data: MutableList<PromoCheckoutLastSeenModel>) {
        promoLastSeenAdapter.listData.clear()
        promoLastSeenAdapter.listData.addAll(data)
        promoLastSeenAdapter.notifyDataSetChanged()
        populateLastSeen()
    }

    private fun populateLastSeen() {
        if (promoLastSeenAdapter.listData.size <= 0) {
            containerLastSeen.visibility = View.GONE
        } else {
            containerLastSeen.visibility = View.VISIBLE
        }
    }

    override fun getScreenName(): String {
        return ""
    }

    override fun onImpressionCoupon(promoCheckoutListModel: PromoCheckoutListModel?) {

    }

    override fun initInjector() {
        DaggerPromoCheckoutListComponent.builder()
                .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
                .promoCheckoutListModule(PromoCheckoutListModule())
                .build()
                .inject(this)
    }

    override fun onDestroyView() {
        promoCheckoutListPresenter.detachView()
        super.onDestroyView()
    }

    override fun loadData(page: Int) {
        if(isCouponActive) {
            promoCheckoutListPresenter.getListPromo(serviceId, categoryId, page, resources)
        }
        /* hold cos api not ready yet
        promoCheckoutListPresenter.getListLastSeen(resources)
        */
    }

}
