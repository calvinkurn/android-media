package com.tokopedia.digital_deals.view.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.digital_deals.R
import com.tokopedia.digital_deals.di.DealsComponent
import com.tokopedia.digital_deals.view.activity.CheckoutActivity
import com.tokopedia.digital_deals.view.activity.DealDetailsActivity
import com.tokopedia.digital_deals.view.model.response.DealsDetailsResponse
import com.tokopedia.digital_deals.view.utils.DealFragmentCallbacks
import com.tokopedia.digital_deals.view.utils.DealsAnalytics
import com.tokopedia.digital_deals.view.utils.Utils
import com.tokopedia.digital_deals.view.viewmodel.DealsVerifyViewModel
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.deal_item_card.iv_brand
import kotlinx.android.synthetic.main.deal_item_card.tv_brand_name
import kotlinx.android.synthetic.main.fragment_brand_detail.toolbar
import kotlinx.android.synthetic.main.fragment_checkout_deal.tv_deal_details
import kotlinx.android.synthetic.main.fragment_checkout_deal.tv_total_amount
import kotlinx.android.synthetic.main.fragment_deal_quantity.*
import javax.inject.Inject

class RevampSelecDealsQuantityFragment: BaseDaggerFragment() {

    private lateinit var dealsDetail: DealsDetailsResponse

    private lateinit var dealFragmentCallback : DealFragmentCallbacks

    @Inject
    lateinit var viewModel: DealsVerifyViewModel

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var dealsAnalytics: DealsAnalytics

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
        observeVerify()
    }

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        dealFragmentCallback = activity as DealDetailsActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dealsDetail = dealFragmentCallback.dealDetails
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_LOGIN -> context?.let {
                    goToCheckoutPage()
                }
            }
        }
    }

    private fun showLayout(){
        toolbar?.apply {
            (activity as BaseSimpleActivity).setSupportActionBar(this)
            setNavigationIcon(ContextCompat.getDrawable(context, com.tokopedia.digital_deals.R.drawable.ic_close_deals))
            setTitle(resources.getString(com.tokopedia.digital_deals.R.string.select_number_of_voucher))
        }

        iv_brand?.loadImage(dealsDetail.imageWeb)
        tv_brand_name?.text = dealsDetail.brand.title
        tv_deal_details?.text = dealsDetail.displayName

        minQuantity = if(dealsDetail.minQty > 0) dealsDetail.minQty else 1
        maxQuantity = if(dealsDetail.maxQty > 0) dealsDetail.maxQty else 1
        currentQuantity = minQuantity

        if (dealsDetail.mrp != 0 && dealsDetail.mrp != dealsDetail.salesPrice) {
            tv_mrp?.show()
            tv_mrp?.text = Utils.convertToCurrencyString(dealsDetail.mrp.toLong())
            tv_mrp?.paintFlags = tv_mrp.getPaintFlags() or Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            tv_mrp?.gone()
        }

        tv_no_quantity?.text = String.format(resources.getString(com.tokopedia.digital_deals.R.string.quantity_of_deals), currentQuantity)
        tv_sales_price?.text = Utils.convertToCurrencyString(dealsDetail.salesPrice.toLong())
        tv_total_amount?.text = Utils.convertToCurrencyString(dealsDetail.salesPrice.toLong())

        iv_subtract?.setOnClickListener {
            if (currentQuantity > minQuantity) {
                currentQuantity--
                context?.let {
                    tv_no_quantity?.setText(String.format(it.resources.getString(R.string.quantity_of_deals), currentQuantity))
                }
            }
            setTotalAmount()
            setButtons()
        }

        iv_add?.setOnClickListener {
            if (currentQuantity < maxQuantity) {
                currentQuantity++
                context?.let {
                    tv_no_quantity.setText(String.format(it.resources.getString(R.string.quantity_of_deals), currentQuantity))
                }
            }
            setTotalAmount()
            setButtons()
        }
        tv_continue?.setOnClickListener {
            if (userSession.isLoggedIn) {
                goToCheckoutPage()
            } else {
                startActivityForResult(RouteManager.getIntent(context, ApplinkConst.LOGIN),
                    REQUEST_CODE_LOGIN)
            }
        }

    }

    private fun goToCheckoutPage(){
        dealsAnalytics.sendEcommerceQuantity(
            dealsDetail.id,
            currentQuantity,
            dealsDetail.salesPrice,
            dealsDetail.displayName,
            dealsDetail.brand.title,
            dealsDetail.categoryId,
            userSession.userId
        )
        showProgress()
        verify()
    }

    private fun observeVerify(){
        observe(viewModel.dealsVerify){
            hideProgress()
            when (it){
                is Success -> {
                    val intent = Intent(activity, CheckoutActivity::class.java)
                    intent.putExtra(CheckoutActivity.EXTRA_DEALDETAIL, dealsDetail)
                    intent.putExtra(CheckoutActivity.EXTRA_VERIFY, it.data.eventVerify)
                    startActivity(intent)
                }

                is Fail -> {
                    val error = ErrorHandler.getErrorMessage(context, it.throwable)
                    view?.let {
                        Toaster.build(it, error, Toaster.LENGTH_LONG, Toaster.TYPE_ERROR).show()
                    }
                }
            }
        }
    }

    private fun setButtons(){
        context?.let {
            if (currentQuantity > 1) {
                iv_subtract?.setColorFilter(getGreenColor(it), PorterDuff.Mode.SRC_IN)
                iv_subtract?.setClickable(true)
            } else {
                iv_subtract?.setColorFilter(getGrayColor(it), PorterDuff.Mode.SRC_IN)
                iv_subtract?.setClickable(false)
            }
            if (currentQuantity < maxQuantity) {
                iv_add?.setColorFilter(getGreenColor(it), PorterDuff.Mode.SRC_IN)
                iv_add?.setClickable(true)
            } else {
                iv_add?.setColorFilter(getGrayColor(it), PorterDuff.Mode.SRC_IN)
                iv_add?.setClickable(false)
            }
        }
    }

    private fun setTotalAmount(){
        tv_total_amount?.text = Utils.convertToCurrencyString(dealsDetail.salesPrice.toLong() * currentQuantity.toLong())
    }

    private fun verify(){
        viewModel.verify(viewModel.mapVerifyRequest(currentQuantity, dealsDetail))
    }

    private fun showProgress(){
        progress_bar_layout?.show()
    }

    private fun hideProgress(){
        progress_bar_layout?.hide()
    }

    private fun getGreenColor(context: Context): Int {
        return ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_G400)
    }

    private fun getGrayColor(context: Context): Int {
        return ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N150)
    }

    companion object{
        const val REQUEST_CODE_LOGIN = 101

        fun createInstance(): RevampSelecDealsQuantityFragment {
            return RevampSelecDealsQuantityFragment()
        }
    }


}