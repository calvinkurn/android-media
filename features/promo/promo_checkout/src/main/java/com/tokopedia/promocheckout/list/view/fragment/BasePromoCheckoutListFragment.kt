package com.tokopedia.promocheckout.list.view.fragment

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.tokopedia.promocheckout.common.data.REQUEST_CODE_PROMO_DETAIL
import com.tokopedia.promocheckout.common.domain.CheckPromoCodeException
import com.tokopedia.promocheckout.common.view.uimodel.DataUiModel
import com.tokopedia.promocheckout.customview.PromoTicketItemDecoration
import com.tokopedia.promocheckout.list.di.DaggerPromoCheckoutListComponent
import com.tokopedia.promocheckout.list.di.PromoCheckoutListModule
import com.tokopedia.promocheckout.list.model.listcoupon.PromoCheckoutListModel
import com.tokopedia.promocheckout.list.model.listpromocatalog.CatalogListItem
import com.tokopedia.promocheckout.list.model.listpromocatalog.TokopointsCatalogHighlight
import com.tokopedia.promocheckout.list.model.listpromolastseen.GetPromoSuggestion
import com.tokopedia.promocheckout.list.model.listpromolastseen.PromoHistoryItem
import com.tokopedia.promocheckout.list.view.adapter.PromoCheckoutListAdapterFactory
import com.tokopedia.promocheckout.list.view.adapter.PromoCheckoutListViewHolder
import com.tokopedia.promocheckout.list.view.adapter.PromoLastSeenAdapter
import com.tokopedia.promocheckout.list.view.adapter.PromoLastSeenViewHolder
import com.tokopedia.promocheckout.list.view.presenter.PromoCheckoutListContract
import com.tokopedia.promocheckout.list.view.presenter.PromoCheckoutListPresenter
import com.tokopedia.promocheckout.list.view.adapter.PromoCheckOutExchangeCouponAdapter
import kotlinx.android.synthetic.main.fragment_list_exchange_coupon.view.*
import kotlinx.android.synthetic.main.fragment_promo_checkout_list.*
import kotlinx.android.synthetic.main.fragment_promo_checkout_list.view.*
import javax.inject.Inject


abstract class BasePromoCheckoutListFragment : BaseListFragment<PromoCheckoutListModel, PromoCheckoutListAdapterFactory>(),
        PromoCheckoutListContract.View, PromoLastSeenViewHolder.ListenerLastSeen, PromoCheckoutListViewHolder.ListenerTrackingCoupon, PromoCheckOutExchangeCouponAdapter.ListenerCouponExchange {

    @Inject
    lateinit var promoCheckoutListPresenter: PromoCheckoutListPresenter
    val promoLastSeenAdapter: PromoLastSeenAdapter by lazy { PromoLastSeenAdapter(ArrayList(), this) }
    val promoCheckoutExchangeCouponAdapter: PromoCheckOutExchangeCouponAdapter by lazy { PromoCheckOutExchangeCouponAdapter(ArrayList(), this) }

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
        promoCheckoutListPresenter.attachView(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_promo_checkout_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        initViewExchangeCoupon(view)
    }

    override fun showProgressLoading() {
        progressDialog.show()
    }

    override fun hideProgressLoading() {
        progressDialog.hide()
    }

    override fun onItemClicked(promoCheckoutListModel: PromoCheckoutListModel?) {
        if (pageTracking == FROM_CART) {
            trackingPromoCheckoutUtil.cartClickCoupon(promoCheckoutListModel?.code ?: "")
        } else {
            trackingPromoCheckoutUtil.checkoutClickCoupon(promoCheckoutListModel?.code ?: "")
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
        val dividerItemDecoration = DividerItemDecoration(context, DividerItemDecoration.HORIZONTAL)
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(context!!, R.drawable.divider_horizontal_custom_quick_filter)!!)
        view.recyclerViewLastSeenPromo.addItemDecoration(PromoTicketItemDecoration(resources.getDimension(R.dimen.dp_16).toInt()))
        view.recyclerViewLastSeenPromo.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        view.recyclerViewLastSeenPromo.adapter = promoLastSeenAdapter

        val linearDividerItemDecoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        linearDividerItemDecoration.setDrawable(ContextCompat.getDrawable(context!!, R.drawable.divider_vertical_list_promo)!!)
        getRecyclerView(view).addItemDecoration(linearDividerItemDecoration)


        progressDialog = ProgressDialog(activity)
        progressDialog.setMessage(getString(R.string.title_loading))
        textInputCoupon.setText(promoCode)

        populateLastSeen()
        buttonUse.setOnClickListener {
            onPromoCodeUse(textInputCoupon.text.toString())
        }
        if (isCouponActive) {
            getRecyclerView(view).visibility = View.VISIBLE
        } else {
            getRecyclerView(view).visibility = View.GONE
        }
    }

    fun initViewExchangeCoupon(view: View) {
        val dividerItemDecoration = DividerItemDecoration(context, DividerItemDecoration.HORIZONTAL)
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(context!!, R.drawable.divider_horizontal_custom_quick_filter)!!)
        view.rv_carousel.addItemDecoration(dividerItemDecoration)
        view.rv_carousel.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        view.rv_carousel.adapter = promoCheckoutExchangeCouponAdapter

        populateExchnageCouponList()

        if (isCouponActive) {
            getRecyclerView(view).visibility = View.VISIBLE
        } else {
            getRecyclerView(view).visibility = View.GONE
        }
    }

    abstract fun onPromoCodeUse(promoCode: String)

    override fun onClickItemLastSeen(promoHistoryItem: PromoHistoryItem) {
    }

    override fun renderListExchangeCoupon(data: TokopointsCatalogHighlight) {
        view?.text_title?.text = data.title
        view?.text_sub_title?.text = data.subTitle
        promoCheckoutExchangeCouponAdapter.items?.clear()
        promoCheckoutExchangeCouponAdapter.items?.addAll(data.catalogList as ArrayList<CatalogListItem>)//data.catalogList)
        promoCheckoutExchangeCouponAdapter.notifyDataSetChanged()
        populateExchnageCouponList()
    }

    override fun showProgressBar() {
        view?.progressBarCatalog?.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        view?.progressBarCatalog?.visibility = View.GONE
    }

    override fun showGetListLastSeenError(e: Throwable) {
        populateLastSeen()
        NetworkErrorHelper.showRedCloseSnackbar(activity, ErrorHandler.getErrorMessage(activity, e))
    }

    override fun showListCatalogHighlight(e: Throwable) {
        populateExchnageCouponList()
    }

    override fun onErrorCheckPromo(e: Throwable) {
        if (pageTracking == FROM_CART) {
            trackingPromoCheckoutUtil.cartClickUsePromoCodeFailed(e)
        } else {
            trackingPromoCheckoutUtil.checkoutClickUsePromoCodeFailed()
        }

        if (e is CheckPromoCodeException || e is MessageErrorException) {
            textInputLayoutCoupon.error = e.message
        } else {
            NetworkErrorHelper.showRedCloseSnackbar(activity, ErrorHandler.getErrorMessage(activity, e))
        }
    }

    override fun onErrorEmptyPromo() {
        textInputLayoutCoupon.error = getString(R.string.promostacking_checkout_label_error_empty_voucher_code)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE_PROMO_DETAIL && resultCode == Activity.RESULT_OK) {
            activity?.setResult(Activity.RESULT_OK, data)
            activity?.finish()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun renderListLastSeen(data: GetPromoSuggestion?) {
        promoLastSeenAdapter.listData.clear()
        promoLastSeenAdapter.listData.addAll(data?.promoHistory as Collection<PromoHistoryItem>)
        promoLastSeenAdapter.notifyDataSetChanged()
        populateLastSeen()
    }

    private fun populateLastSeen() {
        if (promoLastSeenAdapter.listData.isEmpty()) {
            containerLastSeen.visibility = View.GONE
        } else {
            containerLastSeen.visibility = View.VISIBLE
        }
    }

    private fun populateExchnageCouponList() {

        if (promoCheckoutExchangeCouponAdapter.items?.isEmpty()!!) {
            container_exchnage_coupon.visibility = View.GONE
        } else {
            container_exchnage_coupon.visibility = View.VISIBLE
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

    override fun onDestroyView() {
        promoCheckoutListPresenter.detachView()
        super.onDestroyView()
    }

    companion object {
        val EXTRA_COUPON_ACTIVE = "EXTRA_COUPON_ACTIVE"
        val EXTRA_PROMO_CODE = "EXTRA_PROMO_CODE"
        val PAGE_TRACKING = "PAGE_TRACKING"
    }

}