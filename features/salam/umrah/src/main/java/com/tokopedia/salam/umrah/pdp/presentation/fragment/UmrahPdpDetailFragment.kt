package com.tokopedia.salam.umrah.pdp.presentation.fragment
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.salam.umrah.R
import com.tokopedia.salam.umrah.checkout.presentation.activity.UmrahCheckoutActivity
import com.tokopedia.salam.umrah.common.analytics.UmrahTrackingAnalytics
import com.tokopedia.salam.umrah.common.data.UmrahVariant
import com.tokopedia.salam.umrah.common.util.CurrencyFormatter.getRupiahFormat
import com.tokopedia.salam.umrah.pdp.data.ParamPurchase
import com.tokopedia.salam.umrah.pdp.di.UmrahPdpComponent
import com.tokopedia.salam.umrah.pdp.presentation.activity.UmrahPdpActivity.Companion.EXTRA_TOTAL_PASSENGER
import com.tokopedia.salam.umrah.pdp.presentation.activity.UmrahPdpActivity.Companion.EXTRA_TOTAL_PRICE
import com.tokopedia.salam.umrah.pdp.presentation.activity.UmrahPdpActivity.Companion.EXTRA_VARIANT_ROOM
import com.tokopedia.salam.umrah.pdp.presentation.adapter.UmrahPdpDetailAdapter
import kotlinx.android.synthetic.main.fragment_umrah_pdp_detail.*
import javax.inject.Inject
/**
 * @author by M on 31/10/2019
 */
class UmrahPdpDetailFragment : BaseDaggerFragment(){
    @Inject
    lateinit var umrahTrackingUtil: UmrahTrackingAnalytics
    private val umrahPdpDetailAdapter by lazy { UmrahPdpDetailAdapter() }
    override fun getScreenName(): String = ""
    override fun initInjector() = getComponent(UmrahPdpComponent::class.java).inject(this)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_umrah_pdp_detail, container, false)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAll()
    }
    private fun setupAll() {
        setupRVRoom()
        setupTotalPassenger()
        setupPurchaseSummary()
        setupNextButton()
    }
    private fun setupNextButton() {
        bt_umrah_pdp_detail_next.setOnClickListener {
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
                                paramPurchase.departureDate

                        ))
            }
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
//                categoryId = it.categoryId
            }
        }
    }
    private fun setupTotalPassenger(){
        spv_umrah_pdp_detail_jamaah.value = paramPurchase.totalPassenger
        spv_umrah_pdp_detail_jamaah.setOnPassengerCountChangeListener {
            paramPurchase.totalPassenger = it
            setupPurchaseSummary()
            true
        }
    }
    private fun setupRVRoom() {
        umrahPdpDetailAdapter.variants = UmrahPdpFragment.umrahProduct.variants
        if(paramPurchase.variant=="") paramPurchase.variant = umrahPdpDetailAdapter.variants[0].name
        rv_umrah_pdp_detail_rooms.apply {
            adapter = umrahPdpDetailAdapter
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL,false)
        }
        paramPurchase.variantId = UmrahPdpFragment.umrahProduct.variants[0].id
        umrahPdpDetailAdapter.updateSelectedItem(paramPurchase.variant)
        umrahPdpDetailAdapter.pdpDetailListener = object : UmrahPdpDetailAdapter.UmrahPdpDetailListener{
            override fun onRoomClicked(umrahVariant: UmrahVariant) {
                paramPurchase.variant = umrahVariant.name
                paramPurchase.variantId = umrahVariant.id
                paramPurchase.price = umrahVariant.price
                setupPurchaseSummary()
            }
        }
    }
    private fun setupPurchaseSummary(){
        if(paramPurchase.price==0) paramPurchase.price = umrahPdpDetailAdapter.variants[0].price
        val totalPrice = paramPurchase.price * paramPurchase.totalPassenger
        paramPurchase.totalPrice = totalPrice
        tg_umrah_pdp_detail_purchase_summary_passenger_total.text = getString(R.string.umrah_pdp_detail_purchase_summary_total_passenger,paramPurchase.totalPassenger)
        tg_umrah_pdp_detail_purchase_summary_passenger_price.text = getRupiahFormat(paramPurchase.price)
        tg_umrah_pdp_detail_purchase_summary_passenger_price_total.text = getRupiahFormat(totalPrice)
        activity?.run {
            setResult(Activity.RESULT_OK, Intent().apply {
                putExtra(EXTRA_VARIANT_ROOM, paramPurchase.variant)
                putExtra(EXTRA_TOTAL_PASSENGER, paramPurchase.totalPassenger)
                putExtra(EXTRA_TOTAL_PRICE, paramPurchase.totalPrice)
            })
        }
    }
    private fun resetObject(){
        instance = UmrahPdpDetailFragment()
        paramPurchase = ParamPurchase()
    }
    companion object{
        var instance: UmrahPdpDetailFragment = UmrahPdpDetailFragment()
        var paramPurchase = ParamPurchase()
    }
    fun getInstance(slugName: String): UmrahPdpDetailFragment {
        if(slugName != paramPurchase.slugName) {
            resetObject()
            paramPurchase.slugName = slugName
        }
        return instance
    }
}