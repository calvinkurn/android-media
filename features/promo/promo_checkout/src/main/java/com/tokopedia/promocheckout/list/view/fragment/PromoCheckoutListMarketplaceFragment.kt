package com.tokopedia.promocheckout.list.view.fragment

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.constant.IRouterConstant
import com.tokopedia.promocheckout.R
import com.tokopedia.promocheckout.analytics.PromoCheckoutAnalytics.Companion.promoCheckoutAnalytics
import com.tokopedia.promocheckout.common.data.entity.request.Promo
import com.tokopedia.promocheckout.common.util.*
import com.tokopedia.promocheckout.common.view.model.PromoStackingData
import com.tokopedia.promocheckout.common.view.uimodel.ClashingInfoDetailUiModel
import com.tokopedia.promocheckout.common.view.uimodel.DataUiModel
import com.tokopedia.promocheckout.customview.PromoTicketItemDecoration
import com.tokopedia.promocheckout.detail.view.activity.PromoCheckoutDetailMarketplaceActivity
import com.tokopedia.promocheckout.detail.view.fragment.CheckoutCatalogDetailFragment
import com.tokopedia.promocheckout.list.di.PromoCheckoutListComponent
import com.tokopedia.promocheckout.list.model.listcoupon.PromoCheckoutListModel
import com.tokopedia.promocheckout.list.model.listpromocatalog.CatalogListItem
import com.tokopedia.promocheckout.list.model.listpromocatalog.TokopointsCatalogHighlight
import com.tokopedia.promocheckout.list.model.listpromolastseen.GetPromoSuggestion
import com.tokopedia.promocheckout.list.model.listpromolastseen.PromoHistoryItem
import com.tokopedia.promocheckout.list.view.adapter.MarketplacePromoLastSeenAdapter
import com.tokopedia.promocheckout.list.view.adapter.MarketplacePromoLastSeenViewHolder
import com.tokopedia.promocheckout.list.view.adapter.PromoCheckOutExchangeCouponAdapter
import com.tokopedia.promocheckout.list.view.presenter.PromoCheckoutListMarketplaceContract
import com.tokopedia.promocheckout.list.view.presenter.PromoCheckoutListMarketplacePresenter
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.fragment_list_exchange_coupon.view.*
import kotlinx.android.synthetic.main.fragment_promo_checkout_list.*
import kotlinx.android.synthetic.main.fragment_promo_checkout_list.view.*
import javax.inject.Inject


class PromoCheckoutListMarketplaceFragment : BasePromoCheckoutListFragment(),
        PromoCheckoutListMarketplaceContract.View,
        MarketplacePromoLastSeenViewHolder.ListenerLastSeen,
        PromoCheckOutExchangeCouponAdapter.ListenerCouponExchange {

    private var containerParent: ViewGroup? = null

    @Inject
    lateinit var promoCheckoutListMarketplacePresenter: PromoCheckoutListMarketplacePresenter
    val marketplacePromoLastSeenAdapter: MarketplacePromoLastSeenAdapter by lazy { MarketplacePromoLastSeenAdapter(arrayListOf(), this) }
    val promoCheckoutExchangeCouponAdapter: PromoCheckOutExchangeCouponAdapter by lazy { PromoCheckOutExchangeCouponAdapter(ArrayList(), this) }

    private var isOneClickShipment: Boolean = false
    private var promo: Promo? = null
    private var pageNo = 0
    private var mIsRestoredfromBackStack: Boolean = false

    override var serviceId: String = IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.MARKETPLACE_STRING

    override fun onClickRedeemCoupon(catalogId: Int?, slug: String?, title: String, creativeName: String, position: Int) {
        childFragmentManager.beginTransaction().add(R.id.list_parent_container, CheckoutCatalogDetailFragment.newInstance(slug = slug, catalogId = catalogId, promoCode = promoCode, oneClickShipment = isOneClickShipment, pageTracking = pageTracking, promo = promo)).addToBackStack(CHECKOUT_CATALOG_DETAIL_FRAGMENT).commit()
        promoCheckoutAnalytics.clickCatalog(slug, title, catalogId, creativeName, position)
    }

    override fun onClickItemLastSeen(promoHistoryItem: PromoHistoryItem) {
        textInputCoupon.textFieldInput.setText(promoHistoryItem.promoCode)
        promoHistoryItem.promoCode?.let { promoCheckoutListMarketplacePresenter.checkPromoStackingCode(it, isOneClickShipment, promo) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mIsRestoredfromBackStack = false
        isCouponActive = arguments?.getBoolean(IS_COUPON_ACTIVE) ?: true
        promoCode = arguments?.getString(PROMO_CODE) ?: ""
        isOneClickShipment = arguments?.getBoolean(ONE_CLICK_SHIPMENT) ?: false
        pageTracking = arguments?.getInt(PAGE_TRACKING) ?: 1
        promo = arguments?.getParcelable(CHECK_PROMO_FIRST_STEP_PARAM)
        promoCheckoutListMarketplacePresenter.attachView(this)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.containerParent = container
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressDialog = ProgressDialog(activity)
        progressDialog.setMessage(getString(com.tokopedia.abstraction.R.string.title_loading))
        textInputCoupon.textFieldInput.setText(promoCode)
        view.recyclerViewLastSeenPromo.addItemDecoration(PromoTicketItemDecoration(resources.getDimension(com.tokopedia.abstraction.R.dimen.dp_16).toInt()))
        // Change last seen promo text style
        promo_checkout_list_last_seen_label.setType(Typography.HEADING_4)
        initViewExchangeCoupon(view)
    }

    private fun initViewExchangeCoupon(view: View) {
        context?.let { context ->
            val dividerDrawable = ContextCompat.getDrawable(context, com.tokopedia.design.R.drawable.divider_horizontal_custom_quick_filter)
            dividerDrawable?.let { drawable ->
                val dividerItemDecoration = DividerItemDecoration(context, DividerItemDecoration.HORIZONTAL)
                dividerItemDecoration.setDrawable(drawable)
                view.rv_carousel.addItemDecoration(dividerItemDecoration)
            }
        }
        view.rv_carousel.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        view.rv_carousel.adapter = promoCheckoutExchangeCouponAdapter

        populateExchangeCouponList()

        if (isCouponActive) {
            getRecyclerView(view)?.visibility = View.VISIBLE
        } else {
            getRecyclerView(view)?.visibility = View.GONE
        }
    }

    override fun renderListExchangeCoupon(data: TokopointsCatalogHighlight) {
        view?.text_title?.text = data.title
        view?.text_sub_title?.text = data.subTitle
        promoCheckoutExchangeCouponAdapter.items?.clear()
        promoCheckoutExchangeCouponAdapter.items?.addAll(data.catalogList as ArrayList<CatalogListItem>)
        promoCheckoutExchangeCouponAdapter.notifyDataSetChanged()
        populateExchangeCouponList()
    }

    override fun renderListLastSeen(data: GetPromoSuggestion?) {
        marketplacePromoLastSeenAdapter.listData.clear()
        marketplacePromoLastSeenAdapter.listData.addAll(data?.promoHistory as Collection<PromoHistoryItem>)
        marketplacePromoLastSeenAdapter.notifyDataSetChanged()
        populateLastSeen()
    }

    override fun showListCatalogHighlight(e: Throwable) {
        populateExchangeCouponList()
    }

    private fun populateExchangeCouponList() {
        if (promoCheckoutExchangeCouponAdapter.items?.isEmpty() == true) {
            container_exchange_coupon.visibility = View.GONE
        } else {
            container_exchange_coupon.visibility = View.VISIBLE
        }
    }

    override fun showProgressBar() {
        view?.progressBarCatalog?.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        view?.progressBarCatalog?.visibility = View.GONE
    }

    override fun onItemClicked(promoCheckoutListModel: PromoCheckoutListModel?) {
        super.onItemClicked(promoCheckoutListModel)
        activity?.startActivityForResult(PromoCheckoutDetailMarketplaceActivity.createIntent(
                activity, promoCheckoutListModel?.code, oneClickShipment = isOneClickShipment, pageTracking = pageTracking, promo = promo), REQUEST_CODE_DETAIL_PROMO)
    }

    override fun onPromoCodeUse(promoCode: String) {
        promoCheckoutListMarketplacePresenter.checkPromoStackingCode(promoCode, isOneClickShipment, promo)
    }

    override fun onSuccessCheckPromo(data: DataUiModel) {
        trackSuccessCheckPromoCode(data)
        val intent = Intent()
        val typePromo = if (data.isCoupon == PromoStackingData.VALUE_COUPON) PromoStackingData.TYPE_COUPON else PromoStackingData.TYPE_VOUCHER
        val promoStackingData = PromoStackingData(
                typePromo = typePromo,
                promoCode = data.codes[0],
                description = data.message.text,
                title = data.titleDescription,
                counterLabel = "",
                amount = data.cashbackWalletAmount,
                state = data.message.state.mapToStatePromoStackingCheckout(),
                trackingDetailUiModels = data.trackingDetailUiModel)
        intent.putExtra(EXTRA_PROMO_DATA, promoStackingData)
        intent.putExtra(EXTRA_INPUT_TYPE, INPUT_TYPE_PROMO_CODE)
        activity?.setResult(Activity.RESULT_OK, intent)
        activity?.finish()
    }

    override fun onClashCheckPromo(clasingInfoDetailUiModel: ClashingInfoDetailUiModel) {
        val intent = Intent()
        intent.putExtra(EXTRA_CLASHING_DATA, clasingInfoDetailUiModel)
        activity?.setResult(RESULT_CLASHING, intent)
        activity?.finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE_DETAIL_PROMO) {
            if (resultCode == Activity.RESULT_OK) {
                activity?.setResult(Activity.RESULT_OK, data)
                activity?.finish()
            } else {
                val intent = Intent()
                val bundle = data?.getExtras()
                val clashingInfoDetailUiModel: ClashingInfoDetailUiModel? = bundle?.getParcelable(EXTRA_CLASHING_DATA);
                intent.putExtra(EXTRA_CLASHING_DATA, clashingInfoDetailUiModel)
                activity?.setResult(RESULT_CLASHING, intent)

                if (clashingInfoDetailUiModel != null) {
                    activity?.finish()
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun initInjector() {
        super.initInjector()
        getComponent(PromoCheckoutListComponent::class.java).inject(this)
    }

    companion object {
        val REQUEST_CODE_DETAIL_PROMO = 231
        val IS_COUPON_ACTIVE = "IS_COUPON_ACTIVE"
        val PROMO_CODE = "PROMO_CODE"
        val ONE_CLICK_SHIPMENT = "ONE_CLICK_SHIPMENT"
        val PAGE_TRACKING = "PAGE_TRACKING"
        val CHECK_PROMO_FIRST_STEP_PARAM = "CHECK_PROMO_FIRST_STEP_PARAM"
        val CHECKOUT_CATALOG_DETAIL_FRAGMENT = "CHECKOUT_CATALOG_DETAIL_FRAGMENT"

        fun createInstance(isCouponActive: Boolean?, promoCode: String?, oneClickShipment: Boolean?, pageTracking: Int,
                           promo: Promo): PromoCheckoutListMarketplaceFragment {
            val promoCheckoutListMarketplaceFragment = PromoCheckoutListMarketplaceFragment()
            val bundle = Bundle()
            bundle.putBoolean(IS_COUPON_ACTIVE, isCouponActive ?: true)
            bundle.putString(PROMO_CODE, promoCode ?: "")
            bundle.putBoolean(ONE_CLICK_SHIPMENT, oneClickShipment ?: false)
            bundle.putInt(PAGE_TRACKING, pageTracking)
            bundle.putParcelable(CHECK_PROMO_FIRST_STEP_PARAM, promo)
            promoCheckoutListMarketplaceFragment.arguments = bundle
            return promoCheckoutListMarketplaceFragment
        }
    }

    override fun onResume() {

        if (mIsRestoredfromBackStack) {
            isLoadingInitialData = true
            promoCheckoutListPresenter.getListPromo(serviceId, categoryId, pageNo, resources)
            promoCheckoutListMarketplacePresenter.getListExchangeCoupon(resources)
        }
        super.onResume()
    }

    override fun loadData(page: Int) {
        if (isCouponActive) {
            pageNo = page
            promoCheckoutListPresenter.getListPromo(serviceId, categoryId, pageNo, resources)
        }
        promoCheckoutListMarketplacePresenter.getListLastSeen(serviceId, resources)
        promoCheckoutListMarketplacePresenter.getListExchangeCoupon(resources)
    }

    override fun onStop() {
        mIsRestoredfromBackStack = true
        super.onStop()
    }

    override fun onDestroyView() {
        promoCheckoutListMarketplacePresenter.detachView()
        super.onDestroyView()
    }
}
