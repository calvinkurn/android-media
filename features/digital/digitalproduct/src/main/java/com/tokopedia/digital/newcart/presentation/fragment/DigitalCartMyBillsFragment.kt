package com.tokopedia.digital.newcart.presentation.fragment


import android.content.Context
import android.os.Bundle
import android.support.v7.widget.AppCompatTextView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.RelativeLayout
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData
import com.tokopedia.common_digital.cart.view.model.cart.CartDigitalInfoData

import com.tokopedia.digital.R
import com.tokopedia.digital.cart.di.DigitalCartComponent
import com.tokopedia.digital.cart.presentation.compoundview.InputPriceHolderView
import com.tokopedia.digital.newcart.presentation.compoundview.DigitalCartCheckoutHolderView
import com.tokopedia.digital.newcart.presentation.compoundview.DigitalCartDetailHolderView
import com.tokopedia.digital.newcart.presentation.compoundview.DigitalCartMyBillsView
import com.tokopedia.digital.newcart.presentation.contract.DigitalCartMyBillsContract
import com.tokopedia.digital.newcart.presentation.presenter.DigitalCartMyBillsPresenter
import javax.inject.Inject

class DigitalCartMyBillsFragment : DigitalBaseCartFragment<DigitalCartMyBillsContract.Presenter>(), DigitalCartMyBillsContract.View {


    private lateinit var progressBar: ProgressBar
    private lateinit var containerLayout: RelativeLayout
    private lateinit var categoryTextView: AppCompatTextView
    private lateinit var mybillSubscription : DigitalCartMyBillsView

    interface InteractionListener{
        fun updateToolbarTitle(title: String)
    }

    @Inject
    lateinit var digitalCartMyBillsPresenter: DigitalCartMyBillsPresenter

    private var interactionListener: InteractionListener? = null

    override fun setupView(view: View?) {
        progressBar = view!!.findViewById<ProgressBar>(R.id.progress_bar)
        containerLayout = view.findViewById<RelativeLayout>(R.id.container)
        categoryTextView = view.findViewById<AppCompatTextView>(R.id.tv_category_name)
        detailHolderView = view.findViewById<DigitalCartDetailHolderView>(R.id.view_cart_detail)
        checkoutHolderView = view.findViewById<DigitalCartCheckoutHolderView>(R.id.view_checkout_holder)
        checkoutHolderView = view.findViewById<DigitalCartCheckoutHolderView>(R.id.view_checkout_holder)
        inputPriceContainer = view.findViewById<LinearLayout>(R.id.input_price_container)
        inputPriceHolderView = view.findViewById<InputPriceHolderView>(R.id.input_price_holder_view)
        mybillSubscription = view.findViewById(R.id.subscription_mybill)
    }

    override fun onAttachActivity(context: Context?) {
        super.onAttachActivity(context)
        interactionListener = context as InteractionListener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        cartDigitalInfoData = arguments!!.getParcelable(ARG_CART_INFO);
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_digital_cart_my_bill, container, false)
    }

    companion object {
        fun newInstance(cartDigitalInfoData : CartDigitalInfoData, passData : DigitalCheckoutPassData) : DigitalCartMyBillsFragment{
            val fragment = DigitalCartMyBillsFragment()
            val bundle = Bundle()
            bundle.putParcelable(ARG_CART_INFO, cartDigitalInfoData)
            bundle.putParcelable(ARG_PASS_DATA, passData)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.onMyBillsViewCreated()
    }

    override fun renderCategoryInfo(categoryName: String?) {
        categoryTextView.text = categoryName
    }

    override fun hideCartView() {
        containerLayout.visibility = View.GONE
    }

    override fun showFullPageLoading() {
        progressBar.visibility = View.VISIBLE
    }

    override fun showCartView() {
        containerLayout.visibility = View.VISIBLE
    }

    override fun hideFullPageLoading() {
        progressBar.visibility = View.GONE
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(DigitalCartComponent::class.java).inject(this)
        super.presenter = this.digitalCartMyBillsPresenter
    }

    override fun isSubscriptionChecked(): Boolean = mybillSubscription.isChecked()

    override fun renderMyBillsView(headerTitle: String, description: String, checked: Boolean) {
        mybillSubscription.setChecked(checked)
        mybillSubscription.setDescription(description)
        mybillSubscription.setHeaderTitle(headerTitle)
    }

    override fun updateCheckoutButtonText(buttonTitle: String) {
        checkoutHolderView.setTextButton(buttonTitle)
    }
}
