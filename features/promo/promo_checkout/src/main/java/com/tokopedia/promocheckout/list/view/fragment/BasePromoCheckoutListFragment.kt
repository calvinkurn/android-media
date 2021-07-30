package com.tokopedia.promocheckout.list.view.fragment

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.promocheckout.R
import com.tokopedia.promocheckout.common.analytics.FROM_CART
import com.tokopedia.promocheckout.common.analytics.TrackingPromoCheckoutUtil
import com.tokopedia.promocheckout.common.data.REQUEST_CODE_PROMO_DEALS
import com.tokopedia.promocheckout.common.data.REQUEST_CODE_PROMO_DETAIL
import com.tokopedia.promocheckout.common.domain.CheckPromoCodeException
import com.tokopedia.promocheckout.common.view.uimodel.DataUiModel
import com.tokopedia.promocheckout.list.di.DaggerPromoCheckoutListComponent
import com.tokopedia.promocheckout.list.di.PromoCheckoutListModule
import com.tokopedia.promocheckout.list.model.listcoupon.PromoCheckoutListModel
import com.tokopedia.promocheckout.list.model.listlastseen.PromoCheckoutLastSeenModel
import com.tokopedia.promocheckout.list.view.adapter.PromoCheckoutListAdapterFactory
import com.tokopedia.promocheckout.list.view.adapter.PromoCheckoutListViewHolder
import com.tokopedia.promocheckout.list.view.adapter.PromoLastSeenAdapter
import com.tokopedia.promocheckout.list.view.adapter.PromoLastSeenViewHolder
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.promocheckout.list.view.presenter.PromoCheckoutListContract
import com.tokopedia.promocheckout.list.view.presenter.PromoCheckoutListPresenter
import com.tokopedia.promocheckout.list.view.viewmodel.PromoCheckoutListViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_promo_checkout_list.*
import kotlinx.android.synthetic.main.fragment_promo_checkout_list.view.*
import javax.inject.Inject

abstract class BasePromoCheckoutListFragment : BaseListFragment<PromoCheckoutListModel, PromoCheckoutListAdapterFactory>(),
        PromoLastSeenViewHolder.ListenerLastSeen,
        PromoCheckoutListViewHolder.ListenerTrackingCoupon {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var promoCheckoutListViewModel: PromoCheckoutListViewModel
    private val promoLastSeenAdapter: PromoLastSeenAdapter by lazy { PromoLastSeenAdapter(arrayListOf(),this) }

    @Inject
    lateinit var trackingPromoCheckoutUtil: TrackingPromoCheckoutUtil
    lateinit var progressDialog: ProgressDialog

    abstract var serviceId: String
    open var categoryId: Int = 0
    open var isCouponActive: Boolean = true
    open var promoCode: String = ""
    var pageTracking: Int = 1

    override fun getAdapterTypeFactory(): PromoCheckoutListAdapterFactory {
        return PromoCheckoutListAdapterFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.run {
            val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
            promoCheckoutListViewModel = viewModelProvider.get(PromoCheckoutListViewModel::class.java)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_promo_checkout_list, container, false)
    }

    override fun getRecyclerViewResourceId(): Int {
        return R.id.recycler_view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        promoCheckoutListViewModel.showLoading.observe(viewLifecycleOwner,{
            if (it){
                showProgressLoading()
            }else{
                hideProgressLoading()
            }
        })

        promoCheckoutListViewModel.dataPromoCheckoutList.observe(viewLifecycleOwner, {
            when(it){
                is Success ->{
                    //should change data to default value rather than null
                    renderList(it.data.tokopointsCouponList?.tokopointsCouponData ?: ArrayList(),
                        it.data.tokopointsCouponList?.tokopointsPaging?.isHasNext ?: false)
                }
                is Fail ->{

                }
            }
        })

        promoCheckoutListViewModel.dataPromoLastSeen.observe(viewLifecycleOwner,{
            when(it){
                is Success ->{
                    renderListLastSeen(it.data, false)
                }
                is Fail ->{

                }
            }
        })
    }

    fun showProgressLoading() {
        activity?.let {
            if (!it.isFinishing) progressDialog.show()
        }
    }

    fun hideProgressLoading() {
        activity?.let {
            if (!it.isFinishing) progressDialog.hide()
        }
    }

    override fun onItemClicked(promoCheckoutListModel: PromoCheckoutListModel?) {
        if (pageTracking == FROM_CART) {
            trackingPromoCheckoutUtil.cartClickCoupon(promoCheckoutListModel?.code ?: "")
        } else {
            trackingPromoCheckoutUtil.checkoutClickCoupon(promoCheckoutListModel?.code ?: "")
        }
    }

     fun changeTitle(title: String) {
        if (!title.isNullOrBlank()) {
            promo_checkout_list_last_seen_label.text = title
        }
    }

    override fun getEmptyDataViewModel(): Visitable<*> {
        val emptyModel = EmptyModel()
        emptyModel.iconRes = R.drawable.ic_empty_promo_list_checkout
        emptyModel.content = getString(R.string.promo_label_empty_list)
        emptyModel.title = getString(R.string.promo_title_empty_list)
        return emptyModel
    }

    fun initView(view: View) {
        val dividerItemDecoration = DividerItemDecoration(requireContext(), DividerItemDecoration.HORIZONTAL)
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(requireContext(), com.tokopedia.design.R.drawable.divider_horizontal_custom_quick_filter)!!)
        with(view.recyclerViewLastSeenPromo) {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            adapter = promoLastSeenAdapter

            while (itemDecorationCount > 0) removeItemDecorationAt(0)
            addItemDecoration(dividerItemDecoration)
        }

        val linearDividerItemDecoration = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        linearDividerItemDecoration.setDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.divider_vertical_list_promo)!!)
        getRecyclerView(view)?.run {
            while (itemDecorationCount > 0) removeItemDecorationAt(0)
            addItemDecoration(linearDividerItemDecoration)
        }

        progressDialog = ProgressDialog(activity)
        progressDialog.setMessage(getString(com.tokopedia.abstraction.R.string.title_loading))
        textInputCoupon.textFieldInput.setText(promoCode)

        populateLastSeen()
        buttonUse.setOnClickListener {
            onPromoCodeUse(textInputCoupon.textFieldInput.text.toString())
        }
        if (isCouponActive) {
            getRecyclerView(view)?.visibility = View.VISIBLE
        } else {
            getRecyclerView(view)?.visibility = View.GONE
        }
    }

    abstract fun onPromoCodeUse(promoCode: String)

    override fun onClickItemLastSeen(promoCheckoutLastSeenModel: PromoCheckoutLastSeenModel) {

    }

    fun showGetListLastSeenError(e: Throwable) {
        populateLastSeen()
        NetworkErrorHelper.showRedCloseSnackbar(activity, ErrorHandler.getErrorMessage(activity, e))
    }

    fun onErrorCheckPromo(e: Throwable) {
        if (pageTracking == FROM_CART) {
            trackingPromoCheckoutUtil.cartClickUsePromoCodeFailed(e)
        } else {
            trackingPromoCheckoutUtil.checkoutClickUsePromoCodeFailed()
        }

        if (e is CheckPromoCodeException || e is MessageErrorException) {
            textInputCoupon.setError(true)
            e.message?.let { textInputCoupon.setMessage(it) }
        } else {
            NetworkErrorHelper.showRedCloseSnackbar(activity, ErrorHandler.getErrorMessage(activity, e))
        }
    }

    fun onErrorEmptyPromo() {
        textInputCoupon.setError(true)
        textInputCoupon.setMessage(getString(R.string.promostacking_checkout_label_error_empty_voucher_code))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE_PROMO_DETAIL && resultCode == Activity.RESULT_OK) {
            activity?.setResult(Activity.RESULT_OK, data)
            activity?.finish()
        } else if (requestCode == REQUEST_CODE_PROMO_DETAIL && resultCode == REQUEST_CODE_PROMO_DEALS) {
            activity?.setResult(REQUEST_CODE_PROMO_DEALS, data)
            activity?.finish()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    fun renderListLastSeen(data: List<PromoCheckoutLastSeenModel>, isDeals: Boolean) {
        if (!data.isNullOrEmpty()) {
            promoLastSeenAdapter.listData.clear()
            promoLastSeenAdapter.listData.addAll(data)
            promoLastSeenAdapter.isDeals = isDeals
            promoLastSeenAdapter.notifyDataSetChanged()
            populateLastSeen()
        }
    }

    protected fun populateLastSeen() {
        if (promoLastSeenAdapter.listData.isNotEmpty()) {
            containerLastSeen.visibility = View.VISIBLE
        } else {
            containerLastSeen.visibility = View.GONE
        }
    }

    override fun getScreenName(): String {
        return ""
    }

    override fun onImpressionCoupon(promoCheckoutListModel: PromoCheckoutListModel?) {
        if (pageTracking == FROM_CART) {
            trackingPromoCheckoutUtil.cartImpressionCoupon(promoCheckoutListModel?.code ?: "")
        } else {
            trackingPromoCheckoutUtil.checkoutImpressionCoupon(promoCheckoutListModel?.code ?: "")
        }
    }

    protected fun trackSuccessCheckPromoCode(data: DataUiModel) {
        if (pageTracking == FROM_CART) {
            trackingPromoCheckoutUtil.cartClickUsePromoCodeSuccess(data.codes.firstOrNull().orEmpty())
        } else {
            trackingPromoCheckoutUtil.checkoutClickUsePromoCodeSuccess(data.codes.firstOrNull().orEmpty())
        }
    }

    override fun initInjector() {
        DaggerPromoCheckoutListComponent.builder()
                .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
                .promoCheckoutListModule(PromoCheckoutListModule())
                .build()
                .inject(this)
    }


    companion object {
        val EXTRA_COUPON_ACTIVE = "EXTRA_COUPON_ACTIVE"
        val EXTRA_PROMO_CODE = "EXTRA_PROMO_CODE"
        val PAGE_TRACKING = "PAGE_TRACKING"
    }

}