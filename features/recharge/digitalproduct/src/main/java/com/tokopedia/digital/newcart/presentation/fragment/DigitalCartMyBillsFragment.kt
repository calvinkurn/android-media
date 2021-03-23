package com.tokopedia.digital.newcart.presentation.fragment


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.appcompat.widget.AppCompatTextView
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData
import com.tokopedia.digital.R
import com.tokopedia.digital.newcart.di.DigitalCartComponent
import com.tokopedia.digital.newcart.presentation.compoundview.DigitalCartCheckoutHolderView
import com.tokopedia.digital.newcart.presentation.compoundview.DigitalCartDetailHolderView
import com.tokopedia.digital.newcart.presentation.compoundview.DigitalCartMyBillsView
import com.tokopedia.digital.newcart.presentation.compoundview.InputPriceHolderView
import com.tokopedia.digital.newcart.presentation.contract.DigitalCartMyBillsContract
import com.tokopedia.digital.newcart.presentation.model.DigitalSubscriptionParams
import com.tokopedia.digital.newcart.presentation.model.cart.CartDigitalInfoData
import com.tokopedia.digital.newcart.presentation.presenter.DigitalCartMyBillsPresenter
import javax.inject.Inject

class DigitalCartMyBillsFragment : DigitalBaseCartFragment<DigitalCartMyBillsContract.Presenter>(),
        DigitalCartMyBillsContract.View {

    private lateinit var progressBar: ProgressBar
    private lateinit var containerLayout: RelativeLayout
    private lateinit var categoryTextView: AppCompatTextView
    private lateinit var mybillSubscription: DigitalCartMyBillsView

    interface InteractionListener {
        fun updateToolbarTitle(title: String?)
    }

    @Inject
    lateinit var digitalCartMyBillsPresenter: DigitalCartMyBillsPresenter

    private var interactionListener: InteractionListener? = null

    override fun setupView(view: View?) {
        progressBar = requireView().findViewById<ProgressBar>(R.id.progress_bar)
        containerLayout = requireView().findViewById<RelativeLayout>(R.id.container)
        categoryTextView = requireView().findViewById<AppCompatTextView>(R.id.tv_category_name)
        detailHolderView = requireView().findViewById<DigitalCartDetailHolderView>(R.id.view_cart_detail)
        checkoutHolderView = requireView().findViewById<DigitalCartCheckoutHolderView>(R.id.view_checkout_holder)
        checkoutHolderView = requireView().findViewById<DigitalCartCheckoutHolderView>(R.id.view_checkout_holder)
        inputPriceContainer = requireView().findViewById<LinearLayout>(R.id.input_price_container)
        inputPriceHolderView = requireView().findViewById<InputPriceHolderView>(R.id.input_price_holder_view)
        mybillSubscription = requireView().findViewById(R.id.subscription_mybill)
        mybillEgold = requireView().findViewById(R.id.egold_mybill)
        emptyState = requireView().findViewById(R.id.empty_state)

        mybillEgold.setOnMoreInfoClickedListener(this)
    }

    override fun onAttachActivity(context: Context?) {
        super.onAttachActivity(context)
        interactionListener = context as InteractionListener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        cartDigitalInfoData = requireArguments().getParcelable(ARG_CART_INFO)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_digital_cart_mybills, container, false)
    }

    companion object {
        fun newInstance(cartDigitalInfoData: CartDigitalInfoData,
                        passData: DigitalCheckoutPassData,
                        subParams: DigitalSubscriptionParams?): DigitalCartMyBillsFragment {
            val fragment = DigitalCartMyBillsFragment()
            val bundle = Bundle()
            bundle.putParcelable(ARG_CART_INFO, cartDigitalInfoData)
            bundle.putParcelable(ARG_PASS_DATA, passData)
            subParams?.let { bundle.putParcelable(ARG_SUBSCRIPTION_PARAMS, it) }
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

    override fun renderMyBillsSusbcriptionView(headerTitle: String?, description: String?, checked: Boolean, isSubscribed: Boolean) {
        if (headerTitle.isNullOrEmpty() && description.isNullOrEmpty()) {
            mybillSubscription.visibility = View.GONE
            return
        }

        mybillSubscription.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { compoundButton, isChecked ->
            run {
                presenter.onSubcriptionCheckedListener(isChecked)
            }
        })

        // If user is already subsrcibed, hide checkbox for subscribing
        if (isSubscribed) {
            mybillSubscription.getSubscriptionCheckbox().visibility = View.GONE
        } else {
            mybillSubscription.getSubscriptionCheckbox().visibility = View.VISIBLE
            mybillSubscription.setChecked(checked)
        }
        mybillSubscription.hasMoreInfo(false)

        if (description != null) {
            mybillSubscription.setDescription(description)
        }
        if (headerTitle != null) {
            mybillSubscription.setHeaderTitle(headerTitle)
        }

        mybillSubscription.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { compoundButton, isChecked ->
            run {
                presenter.onSubcriptionCheckedListener(isChecked)
            }
        })
    }

    override fun updateCheckoutButtonText(buttonTitle: String?) {
        checkoutHolderView.setTextButton(buttonTitle)
    }

    override fun showMyBillsSubscriptionView() {
        mybillSubscription.visibility = View.VISIBLE
    }

    override fun hideMyBillsSubscriptionView() {
        mybillSubscription.visibility = View.GONE
    }

    override fun renderMyBillsDescriptionView(title: String?) {
        if (title != null)
            mybillSubscription.setDescription(title)
    }

    override fun updateToolbarTitle(headerTitle: String?) {
        interactionListener?.updateToolbarTitle(headerTitle)
    }
}
