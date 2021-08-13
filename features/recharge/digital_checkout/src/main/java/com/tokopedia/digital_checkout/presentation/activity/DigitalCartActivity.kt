package com.tokopedia.digital_checkout.presentation.activity

import android.content.Context
import android.net.Uri
import android.view.MotionEvent
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.applink.constant.DeeplinkConstant
import com.tokopedia.applink.digital.DeeplinkMapperDigital
import com.tokopedia.authentication.AuthHelper
import com.tokopedia.common_digital.atc.data.response.DigitalSubscriptionParams
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData.Companion.PARAM_CATEGORY_ID
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData.Companion.PARAM_CLIENT_NUMBER
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData.Companion.PARAM_FIELD_LABEL_PREFIX
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData.Companion.PARAM_INSTANT_CHECKOUT
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData.Companion.PARAM_IS_PROMO
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData.Companion.PARAM_OPERATOR_ID
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData.Companion.PARAM_ORDER_ID
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData.Companion.PARAM_PRODUCT_ID
import com.tokopedia.common_digital.common.constant.DigitalExtraParam
import com.tokopedia.digital_checkout.R
import com.tokopedia.digital_checkout.di.DigitalCheckoutComponent
import com.tokopedia.digital_checkout.di.DigitalCheckoutComponentInstance
import com.tokopedia.digital_checkout.presentation.fragment.DigitalCartFragment
import com.tokopedia.user.session.UserSession

/**
 * @author by jessica on 07/01/21
 */

class DigitalCartActivity : BaseSimpleActivity(), HasComponent<DigitalCheckoutComponent> {
    override fun getNewFragment(): Fragment {
        val uriData = intent.data
        var cartPassData: DigitalCheckoutPassData? = null
        var subParams: DigitalSubscriptionParams? = null

        uriData?.let { uri ->
            if (uri.queryParameterNames.size > 0) {
                cartPassData = processIntentDataCheckoutFromApplink(applicationContext, uriData)
                subParams = processIntentDataSubscription(uriData)
            } else if (uri.scheme == DeeplinkConstant.SCHEME_INTERNAL && uri.getQueryParameter(DeeplinkMapperDigital.IS_FROM_WIDGET_PARAM).isNullOrEmpty()) {
                cartPassData = intent.getParcelableExtra(DigitalExtraParam.EXTRA_PASS_DIGITAL_CART_DATA)
                subParams = intent.getParcelableExtra(DigitalExtraParam.EXTRA_PASS_DIGITAL_SUBSCRIPTION_DATA)
            }
            return DigitalCartFragment.newInstance(cartPassData, subParams)
        }
        return Fragment()
    }

    private fun processIntentDataCheckoutFromApplink(context: Context, uriData: Uri): DigitalCheckoutPassData? {
        val passData = DigitalCheckoutPassData()
        passData.categoryId = uriData.getQueryParameter(PARAM_CATEGORY_ID)
        passData.orderId = uriData.getQueryParameter(PARAM_ORDER_ID)
        passData.clientNumber = uriData.getQueryParameter(PARAM_CLIENT_NUMBER)
        passData.operatorId = uriData.getQueryParameter(PARAM_OPERATOR_ID)
        passData.productId = uriData.getQueryParameter(PARAM_PRODUCT_ID)
        passData.isPromo = uriData.getQueryParameter(PARAM_IS_PROMO)
        val instantCheckoutParam = uriData.getQueryParameter(PARAM_INSTANT_CHECKOUT)
        passData.instantCheckout = instantCheckoutParam ?: "0"
        passData.idemPotencyKey = generateATokenRechargeCheckout(context)

        val fields: HashMap<String, String> = HashMap()
        val parameters = uriData.queryParameterNames
        if (parameters.isNotEmpty()) {
            for (param in parameters) {
                if (param.startsWith(PARAM_FIELD_LABEL_PREFIX)) {
                    val value = uriData.getQueryParameter(param) ?: ""
                    val key = param.replaceFirst(PARAM_FIELD_LABEL_PREFIX.toRegex(), "")
                    fields[key] = value
                }
            }
        }
        passData.fields = fields

        return passData
    }

    private fun processIntentDataSubscription(uriData: Uri): DigitalSubscriptionParams? {
        val subParams = DigitalSubscriptionParams()
        val showSubscribePopUpArg = uriData.getQueryParameter(DigitalSubscriptionParams.ARG_SHOW_SUBSCRIBE_POP_UP)
        val autoSubscribeArg = uriData.getQueryParameter(DigitalSubscriptionParams.ARG_AUTO_SUBSCRIBE)

        showSubscribePopUpArg?.let {
            subParams.showSubscribePopUp = it.toBoolean()
        }
        autoSubscribeArg?.let {
            subParams.autoSubscribe = autoSubscribeArg.toBoolean()
        }

        return subParams
    }

    private fun generateATokenRechargeCheckout(context: Context): String? {
        val timeMillis = System.currentTimeMillis().toString()
        val token = AuthHelper.getMD5Hash(timeMillis)
        val userSession = UserSession(context)
        return String.format(getString(R.string.digital_cart_generate_token_checkout),
                userSession.userId, if (token.isEmpty()) timeMillis else token)
    }

    override fun getComponent(): DigitalCheckoutComponent {
        return DigitalCheckoutComponentInstance.getDigitalCheckoutComponent(application)
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (currentFocus != null) {
            KeyboardHandler.hideSoftKeyboard(this)
            currentFocus?.clearFocus()
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_digital_checkout
    }

    override fun getParentViewResourceID(): Int {
        return R.id.parent_view
    }

    override fun getToolbarResourceID(): Int {
        return R.id.digital_checkout_toolbar
    }
}