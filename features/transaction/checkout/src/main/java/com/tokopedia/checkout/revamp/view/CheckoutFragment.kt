package com.tokopedia.checkout.revamp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.checkout.databinding.FragmentCheckoutBinding
import com.tokopedia.checkout.databinding.HeaderCheckoutBinding
import com.tokopedia.checkout.revamp.di.CheckoutModule
import com.tokopedia.checkout.revamp.di.DaggerCheckoutComponent
import com.tokopedia.checkout.view.ShipmentFragment
import com.tokopedia.purchase_platform.common.analytics.ConstantTransactionAnalytics
import com.tokopedia.utils.lifecycle.autoCleared
import javax.inject.Inject

class CheckoutFragment : BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: CheckoutViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[CheckoutViewModel::class.java]
    }

    private var binding by autoCleared<FragmentCheckoutBinding>()

    private var header by autoCleared<HeaderCheckoutBinding>()

    private val isOneClickShipment: Boolean
        get() = arguments != null && arguments!!.getBoolean(ShipmentFragment.ARG_IS_ONE_CLICK_SHIPMENT)

    override fun getScreenName(): String {
        return if (isOneClickShipment) {
            ConstantTransactionAnalytics.ScreenName.ONE_CLICK_SHIPMENT
        } else {
            ConstantTransactionAnalytics.ScreenName.CHECKOUT
        }
    }

    override fun initInjector() {
        if (activity != null) {
            val baseMainApplication = activity!!.application as BaseMainApplication
            DaggerCheckoutComponent.builder()
                .baseAppComponent(baseMainApplication.baseAppComponent)
                .checkoutModule(CheckoutModule(this))
                .build()
                .inject(this)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        activity?.findViewById<View>(com.tokopedia.abstraction.R.id.toolbar)?.isVisible = false
        binding = FragmentCheckoutBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.headerCheckout.setNavigationOnClickListener {
            onBackPressed()
        }
        header = HeaderCheckoutBinding.inflate(LayoutInflater.from(context))
        header.tvCheckoutHeaderAddressHeader.isVisible = false
        header.tvCheckoutHeaderAddressName.isVisible = false
        binding.headerCheckout.customView(header.root)

        binding.tvCheckoutTesting.setOnClickListener {
            if (header.tvCheckoutHeaderText.isVisible) {
//                header.groupCheckoutHeaderDefault
//                    .animate()
//                    .alpha(0f)
//                    .withEndAction {
                        header.tvCheckoutHeaderText.animateGone()
//                    }
//                    .start()
                header.tvCheckoutHeaderAddressHeader.animateShow()
                header.tvCheckoutHeaderAddressName.animateShow()
//                header.groupCheckoutHeaderAddress.alpha = 0f
//                header.groupCheckoutHeaderAddress
//                    .animate()
//                    .alpha(1f)
//                    .setDuration(3000)
//                    .withStartAction {
//                        header.groupCheckoutHeaderAddress.isVisible = true
//                    }
//                    .start()
            } else {
                header.tvCheckoutHeaderText.animateShow()
//                header.groupCheckoutHeaderAddress
//                    .animate()
//                    .alpha(0f)
//                    .setDuration(400)
//                    .withEndAction {
                        header.tvCheckoutHeaderAddressHeader.animateGone()
                header.tvCheckoutHeaderAddressName.animateGone()
//                    }
//                    .start()
//                header.groupCheckoutHeaderDefault.alpha = 0f
//                header.groupCheckoutHeaderDefault
//                    .animate()
//                    .alpha(1f)
//                    .setDuration(3000)
//                    .withStartAction {
//                        header.groupCheckoutHeaderDefault.isVisible = true
//                    }
//                    .start()
            }
        }
    }

    fun onBackPressed(): Boolean {
        activity?.finish()
        return true
    }

    companion object {

        fun newInstance(
            isOneClickShipment: Boolean,
            leasingId: String,
            pageSource: String,
            isPlusSelected: Boolean,
            bundle: Bundle?
        ): CheckoutFragment {
            val b = bundle ?: Bundle()
            b.putString(ShipmentFragment.ARG_CHECKOUT_LEASING_ID, leasingId)
            if (leasingId.isNotEmpty()) {
                b.putBoolean(ShipmentFragment.ARG_IS_ONE_CLICK_SHIPMENT, true)
            } else {
                b.putBoolean(ShipmentFragment.ARG_IS_ONE_CLICK_SHIPMENT, isOneClickShipment)
            }
            b.putString(ShipmentFragment.ARG_CHECKOUT_PAGE_SOURCE, pageSource)
            b.putBoolean(ShipmentFragment.ARG_IS_PLUS_SELECTED, isPlusSelected)
            val checkoutFragment = CheckoutFragment()
            checkoutFragment.arguments = b
            return checkoutFragment
        }
    }
}

private fun View.fadeTo(visible: Boolean, duration: Long) {
    // Make this idempotent.
    val tagKey = "fadeTo".hashCode()
    if (visible == isVisible && animation == null && getTag(tagKey) == null) return
    if (getTag(tagKey) == visible) return

    setTag(tagKey, visible)

    if (visible && alpha == 1f) alpha = 0f
    animate()
        .alpha(if (visible) 1f else 0f)
        .withStartAction {
            if (visible) isVisible = true
        }
        .withEndAction {
            setTag(tagKey, null)
            if (isAttachedToWindow && !visible) isVisible = false
        }
        .setInterpolator(FastOutSlowInInterpolator())
        .setDuration(duration)
        .start()
}

internal fun View.animateShow(duration: Long = 400) = fadeTo(true, duration)
internal fun View.animateGone(duration: Long = 400) = fadeTo(false, duration)
