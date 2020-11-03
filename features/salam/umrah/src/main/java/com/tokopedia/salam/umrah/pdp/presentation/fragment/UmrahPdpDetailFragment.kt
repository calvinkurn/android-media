package com.tokopedia.salam.umrah.pdp.presentation.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.salam.umrah.R
import com.tokopedia.salam.umrah.checkout.presentation.activity.UmrahCheckoutActivity
import com.tokopedia.salam.umrah.common.analytics.UmrahTrackingAnalytics
import com.tokopedia.salam.umrah.common.data.UmrahVariant
import com.tokopedia.salam.umrah.common.util.CurrencyFormatter.getRupiahFormat
import com.tokopedia.salam.umrah.common.util.UmrahQuery
import com.tokopedia.salam.umrah.pdp.data.ParamPurchase
import com.tokopedia.salam.umrah.pdp.di.UmrahPdpComponent
import com.tokopedia.salam.umrah.pdp.presentation.activity.UmrahPdpActivity.Companion.EXTRA_IS_EMPTY
import com.tokopedia.salam.umrah.pdp.presentation.activity.UmrahPdpActivity.Companion.EXTRA_MAX_PASSENGER
import com.tokopedia.salam.umrah.pdp.presentation.activity.UmrahPdpActivity.Companion.EXTRA_TOTAL_PASSENGER
import com.tokopedia.salam.umrah.pdp.presentation.activity.UmrahPdpActivity.Companion.EXTRA_TOTAL_PRICE
import com.tokopedia.salam.umrah.pdp.presentation.activity.UmrahPdpActivity.Companion.EXTRA_VARIANT_ROOM
import com.tokopedia.salam.umrah.pdp.presentation.adapter.UmrahPdpDetailAdapter
import com.tokopedia.salam.umrah.pdp.presentation.viewmodel.UmrahPdpDetailViewModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.bottom_sheets_umrah_pdp_sold_out.view.*
import kotlinx.android.synthetic.main.fragment_umrah_pdp_detail.*
import javax.inject.Inject

/**
 * @author by M on 31/10/2019
 */
class UmrahPdpDetailFragment : BaseDaggerFragment() {
    @Inject
    lateinit var umrahTrackingUtil: UmrahTrackingAnalytics

    @Inject
    lateinit var userSessionInterface: UserSessionInterface

    @Inject
    lateinit var umrahPdpDetailViewModel: UmrahPdpDetailViewModel

    private lateinit var progressDialog: ProgressDialog

    private val umrahPdpDetailAdapter by lazy { UmrahPdpDetailAdapter() }
    override fun getScreenName(): String = ""
    override fun initInjector() = getComponent(UmrahPdpComponent::class.java).inject(this)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_umrah_pdp_detail, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAll()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        umrahPdpDetailViewModel.pdpAvailability.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> onSuccessGetResult(it.data)
                is Fail -> showGetListError()
            }
        })
    }

    private fun showGetListError() {
        NetworkErrorHelper.showEmptyState(context, view?.rootView,null,null,null,R.drawable.umrah_img_empty_search_png){
            requestAvailabilityData()
        }
    }

    private fun onSuccessGetResult(data: Int) {
        if (data >= paramPurchase.totalPassenger) {
            nextToCheckOut()
        }
        else{
            showSoldOutBottomSheet()
        }
    }

    private fun requestAvailabilityData() {
        umrahPdpDetailViewModel.getPdpAvailability(
                UmrahQuery.UMRAH_PDP_QUERY, paramPurchase.slugName)
    }

    private fun setupAll() {
        setupRVRoom()
        setupTotalPassenger()
        setupPurchaseSummary()
        setupNextButton()
        setupFAB()
    }

    private fun setupFAB(){
        fab_umrah_pdp_detail_message.bringToFront()
        fab_umrah_pdp_detail_message.setOnClickListener {
            checkChatSession()
        }
    }

    private fun checkChatSession(){
        if (userSessionInterface.isLoggedIn) {
            context?.let {
                startChatUmroh(it)
            }
        } else {
            goToLoginPage(REQUEST_CODE_LOGIN_FAB)
        }
    }

    private fun startChatUmroh(context: Context){
        val intent = RouteManager.getIntent(context,
                ApplinkConst.TOPCHAT_ASKSELLER,
                resources.getString(R.string.umrah_shop_id), resources.getString(R.string.umrah_shop_link, paramPurchase.slugName),
                resources.getString(R.string.umrah_shop_source), resources.getString(R.string.umrah_shop_name), "")
        startActivity(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_LOGIN_FAB -> context?.let{checkChatSession()}
            }
        }
    }

    private fun setupNextButton() {
        if (maxPassenger == 0) bt_umrah_pdp_detail_next.isEnabled = false
        progressDialog = ProgressDialog(activity)
        progressDialog.setMessage(getString(R.string.umrah_progress_dialog))
        bt_umrah_pdp_detail_next.setOnClickListener {
            requestAvailabilityData()
        }
    }

    private fun nextToCheckOut(){
        progressDialog.show()
        if (userSessionInterface.isLoggedIn) {
            progressDialog.dismiss()
            getParamPurchase()
            umrahTrackingUtil.umrahPdpPurchaseProductsNext(paramPurchase)

            context?.let {
                startActivity(
                        UmrahCheckoutActivity.createIntent(it,
                                paramPurchase.slugName,
                                paramPurchase.variantId,
                                paramPurchase.price,
                                paramPurchase.totalPrice,
                                paramPurchase.totalPassenger,
                                paramPurchase.departureDate,
                                paramPurchase.downPaymentPrice

                        ))
            }
        } else {
            goToLoginPage(REQUEST_CODE_LOGIN)
        }
    }
    private fun getParamPurchase() {
        UmrahPdpFragment.umrahProduct.let {
            paramPurchase.apply {
                name = it.title
                id = it.id
                slugName = it.slugName
                shopId = it.travelAgent.id
                shopName = it.travelAgent.name
                departureDate = it.departureDate
                downPaymentPrice = it.downPaymentPrice
            }
        }
    }

    private fun setupTotalPassenger() {
        spv_umrah_pdp_detail_jamaah.value = paramPurchase.totalPassenger
        spv_umrah_pdp_detail_jamaah.setMinimalPassenger(1)
        spv_umrah_pdp_detail_jamaah.setMaximalPassenger(maxPassenger)
        spv_umrah_pdp_detail_jamaah.setOnPassengerCountChangeListener {
            paramPurchase.totalPassenger = it
            setupPurchaseSummary()
            true
        }
    }

    private fun setupRVRoom() {
        umrahPdpDetailAdapter.variants = UmrahPdpFragment.umrahProduct.variants
        if (paramPurchase.variant == "") paramPurchase.variant = umrahPdpDetailAdapter.variants[0].name
        rv_umrah_pdp_detail_rooms.apply {
            adapter = umrahPdpDetailAdapter
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        }
        paramPurchase.variantId = UmrahPdpFragment.umrahProduct.variants[0].id
        umrahPdpDetailAdapter.updateSelectedItem(paramPurchase.variant)
        umrahPdpDetailAdapter.pdpDetailListener = object : UmrahPdpDetailAdapter.UmrahPdpDetailListener {
            override fun onRoomClicked(umrahVariant: UmrahVariant) {
                paramPurchase.variant = umrahVariant.name
                paramPurchase.variantId = umrahVariant.id
                paramPurchase.price = umrahVariant.price
                setupPurchaseSummary()
            }
        }
    }

    private fun setupPurchaseSummary() {
        if (paramPurchase.price == 0) paramPurchase.price = umrahPdpDetailAdapter.variants[0].price
        val totalPrice = paramPurchase.price * paramPurchase.totalPassenger
        paramPurchase.totalPrice = totalPrice
        tg_umrah_pdp_detail_purchase_summary_passenger_total.text = getString(R.string.umrah_pdp_detail_purchase_summary_total_passenger, paramPurchase.totalPassenger)
        tg_umrah_pdp_detail_purchase_summary_passenger_price.text = getRupiahFormat(paramPurchase.price)
        tg_umrah_pdp_detail_purchase_summary_passenger_price_total.text = getRupiahFormat(totalPrice)
        activity?.run {
            setResult(Activity.RESULT_OK, Intent().apply {
                putExtra(EXTRA_VARIANT_ROOM, paramPurchase.variant)
                putExtra(EXTRA_MAX_PASSENGER, maxPassenger)
                putExtra(EXTRA_TOTAL_PASSENGER, paramPurchase.totalPassenger)
                putExtra(EXTRA_TOTAL_PRICE, paramPurchase.totalPrice)
            })
        }
    }

    @SuppressLint("InflateParams")
    private fun showSoldOutBottomSheet() {
        val soldOutBottomSheet = BottomSheetUnify()
        soldOutBottomSheet.setCloseClickListener {
            soldOutBottomSheet.dismiss()
            showSoldOutPackage()
        }
        val view = LayoutInflater.from(context).inflate(R.layout.bottom_sheets_umrah_pdp_sold_out, null)
        view.es_pdp.apply {
            ContextCompat.getDrawable(context, R.drawable.img_umrah_pdp_empty_state)?.let { setImageDrawable(it) }
            setTitle(getString(R.string.umrah_pdp_empty_state_title))
            setDescription(getString(R.string.umrah_empty_state_desc))
            setPrimaryCTAText(getString(R.string.umrah_empty_button))
            setPrimaryCTAClickListener {
                activity?.run {
                    setResult(Activity.RESULT_OK, Intent().apply {
                        putExtra(EXTRA_IS_EMPTY, true)
                    })
                    finish()
                }
            }
        }
        soldOutBottomSheet.setChild(view)
        soldOutBottomSheet.show(fragmentManager!!, "")
    }

    private fun showSoldOutPackage() {
        bt_umrah_pdp_detail_next.isEnabled = false
    }

    private fun resetObject() {
        instance = UmrahPdpDetailFragment()
        paramPurchase = ParamPurchase()
    }

    companion object {
        private var instance: UmrahPdpDetailFragment = UmrahPdpDetailFragment()
        var paramPurchase = ParamPurchase()
        private var maxPassenger = 0
        const val REQUEST_CODE_LOGIN_FAB = 400
        const val REQUEST_CODE_LOGIN = 401
    }

    fun getInstance(slugName: String, totalAvailability: Int): UmrahPdpDetailFragment {
        if (slugName != paramPurchase.slugName) {
            resetObject()
            paramPurchase.slugName = slugName
            maxPassenger = if (totalAvailability < 4) totalAvailability else 4
            if (maxPassenger == 0) activity?.setResult(Activity.RESULT_OK, Intent().putExtra(EXTRA_MAX_PASSENGER, maxPassenger))
        }
        return instance
    }

    private fun goToLoginPage(requestCode: Int) {
        if (activity != null) {
            progressDialog.dismiss()
            startActivityForResult(RouteManager.getIntent(context, ApplinkConst.LOGIN),
                    requestCode)
        }
    }
}