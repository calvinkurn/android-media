package com.tokopedia.digital_deals.view.fragment

import android.app.Activity
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.digital_deals.di.DealsComponent
import com.tokopedia.digital_deals.view.activity.DealDetailsActivity
import com.tokopedia.digital_deals.view.model.response.DealsDetailsResponse
import com.tokopedia.digital_deals.view.utils.DealFragmentCallbacks
import com.tokopedia.digital_deals.view.utils.Utils
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.show
import kotlinx.android.synthetic.main.deal_item_card.iv_brand
import kotlinx.android.synthetic.main.deal_item_card.tv_brand_name
import kotlinx.android.synthetic.main.fragment_brand_detail.toolbar
import kotlinx.android.synthetic.main.fragment_checkout_deal.*
import kotlinx.android.synthetic.main.fragment_checkout_deal.tv_deal_details
import kotlinx.android.synthetic.main.fragment_checkout_deal.tv_total_amount
import kotlinx.android.synthetic.main.fragment_deal_quantity.*

class RevampSelecDealsQuantityFragment: BaseDaggerFragment() {

    private lateinit var dealsDetail: DealsDetailsResponse

    private lateinit var dealFragmentCallback : DealFragmentCallbacks

    private var currentQuantity = 1
    private var minQuantity = 1
    private var maxQuantity = 1

    override fun initInjector() {
        getComponent(DealsComponent::class.java).inject(this)
    }

    override fun getScreenName(): String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(com.tokopedia.digital_deals.R.layout.fragment_deal_quantity, container, false)
        setHasOptionsMenu(true)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showLayout()
    }

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        dealFragmentCallback = activity as DealDetailsActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dealsDetail = dealFragmentCallback.dealDetails
    }

    private fun showLayout(){
        toolbar.apply {
            (activity as BaseSimpleActivity).setSupportActionBar(this)
            setNavigationIcon(ContextCompat.getDrawable(context, com.tokopedia.digital_deals.R.drawable.ic_close_deals))
            setTitle(resources.getString(com.tokopedia.digital_deals.R.string.select_number_of_voucher))
        }

        iv_brand.loadImage(dealsDetail.imageWeb)
        tv_brand_name.text = dealsDetail.brand.title
        tv_deal_details.text = dealsDetail.displayName

        minQuantity = if(dealsDetail.minQty > 0) dealsDetail.minQty else 1
        maxQuantity = if(dealsDetail.maxQty > 0) dealsDetail.maxQty else 1
        currentQuantity = minQuantity

        if (dealsDetail.mrp != 0 && dealsDetail.mrp != dealsDetail.salesPrice) {
            tv_mrp.show()
            tv_mrp.text = Utils.convertToCurrencyString(dealsDetail.mrp.toLong())
            tv_mrp.paintFlags = tv_mrp.getPaintFlags() or Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            tv_mrp.gone()
        }

        tv_no_quantity.text = String.format(resources.getString(com.tokopedia.digital_deals.R.string.quantity_of_deals), currentQuantity)
        tv_sales_price.text = Utils.convertToCurrencyString(dealsDetail.salesPrice.toLong())
        tv_total_amount.text = Utils.convertToCurrencyString(dealsDetail.salesPrice.toLong())
    }

    companion object{
        fun createInstance(): RevampSelecDealsQuantityFragment {
            return RevampSelecDealsQuantityFragment()
        }
    }


}