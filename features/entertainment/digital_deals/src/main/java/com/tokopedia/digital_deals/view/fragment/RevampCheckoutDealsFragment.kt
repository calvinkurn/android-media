package com.tokopedia.digital_deals.view.fragment

import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.digital_deals.data.EventVerifyResponse
import com.tokopedia.digital_deals.data.ItemMapResponse
import com.tokopedia.digital_deals.di.DealsComponent
import com.tokopedia.digital_deals.view.activity.CheckoutActivity.EXTRA_DEALDETAIL
import com.tokopedia.digital_deals.view.activity.CheckoutActivity.EXTRA_VERIFY
import com.tokopedia.digital_deals.view.model.response.DealsDetailsResponse
import com.tokopedia.digital_deals.view.utils.Utils
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.fragment_checkout_deal.*
import kotlinx.android.synthetic.main.fragment_checkout_deal.tv_available_locations
import kotlinx.android.synthetic.main.header_layout_brand_page.*
import javax.inject.Inject

class RevampCheckoutDealsFragment: BaseDaggerFragment() {

    private var dealsDetail : DealsDetailsResponse = DealsDetailsResponse()
    private var verifyData: EventVerifyResponse = EventVerifyResponse()
    private var itemMap: ItemMapResponse = ItemMapResponse()
    private var quantity = 1

    @Inject
    lateinit var userSession: UserSessionInterface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            dealsDetail = it.getParcelable(EXTRA_DEALDETAIL) ?: DealsDetailsResponse()
            verifyData = it.getParcelable(EXTRA_VERIFY) ?: EventVerifyResponse()
            itemMap = verifyData.metadata.itemMap.firstOrNull() ?: ItemMapResponse()
        }
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(DealsComponent::class.java).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(com.tokopedia.digital_deals.R.layout.fragment_checkout_deal, container, false)
        setHasOptionsMenu(true)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showLayout()
    }

    private fun showLayout(){
        quantity = dealsDetail.quantity
        image_view_brand?.loadImage(dealsDetail.imageWeb)
        tv_brand_name?.text = dealsDetail.brand.title
        tv_deal_details?.text = dealsDetail.displayName
        tv_expiry_date?.text = resources.getString(com.tokopedia.digital_deals.R.string.valid_through,
                Utils.convertEpochToString(dealsDetail.saleEndDate)
        )

        if(dealsDetail.outlets == null || dealsDetail.outlets.isEmpty()){
            tv_available_locations?.text = resources.getString(com.tokopedia.digital_deals.R.string.deals_all_indonesia)
        }

        if(dealsDetail.mrp != 0 && dealsDetail.mrp != dealsDetail.salesPrice){
            tv_mrp_per_quantity?.apply {
                show()
                text = Utils.convertToCurrencyString(dealsDetail.mrp.toLong())
                paintFlags = this.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            }
        } else {
            tv_mrp_per_quantity?.hide()
        }

        tv_sales_price_per_quantity?.text = Utils.convertToCurrencyString(itemMap.price.toLong())
        tv_sales_price_all_quantity?.text = Utils.convertToCurrencyString((itemMap.price * itemMap.quantity).toLong())

        if(itemMap.commission == 0){
            tv_service_fee?.gone()
            tv_service_fee_amount?.gone()
        } else {
            tv_service_fee?.text = Utils.convertToCurrencyString(itemMap.commission.toLong())
        }

        tv_total_amount?.text = Utils.convertToCurrencyString((itemMap.price * itemMap.quantity + itemMap.commission).toLong())
        tv_number_vouchers?.text = resources.getString(com.tokopedia.digital_deals.R.string.number_of_vouchers, itemMap.quantity)

        if(dealsDetail.outlets != null && dealsDetail.outlets.size > 0){
            tv_number_locations?.text = resources.getString(com.tokopedia.digital_deals.R.string.number_of_locations, dealsDetail.outlets.size)
        }

        tv_email?.setText(userSession.email)
        tv_phone?.setText(userSession.phoneNumber)
        ticker_promocode?.enableView()
        base_main_content?.show()
        cl_btn_payment?.show()

    }

    companion object{

        fun createInstance(bundle: Bundle): RevampCheckoutDealsFragment {
            return RevampCheckoutDealsFragment().also {
                it.arguments = bundle
            }
        }
    }
}