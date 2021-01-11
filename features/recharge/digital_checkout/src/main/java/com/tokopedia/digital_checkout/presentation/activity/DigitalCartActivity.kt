package com.tokopedia.digital_checkout.presentation.activity

import android.content.Context
import android.net.Uri
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.constant.DeeplinkConstant
import com.tokopedia.authentication.AuthHelper
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData.Companion.PARAM_CATEGORY_ID
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData.Companion.PARAM_CLIENT_NUMBER
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData.Companion.PARAM_INSTANT_CHECKOUT
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData.Companion.PARAM_IS_PROMO
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData.Companion.PARAM_OPERATOR_ID
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData.Companion.PARAM_ORDER_ID
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData.Companion.PARAM_PRODUCT_ID
import com.tokopedia.common_digital.common.constant.DigitalExtraParam
import com.tokopedia.digital_checkout.R
import com.tokopedia.digital_checkout.data.response.atc.DigitalSubscriptionParams
import com.tokopedia.digital_checkout.di.DigitalCheckoutComponent
import com.tokopedia.digital_checkout.di.DigitalCheckoutComponentInstance
import com.tokopedia.digital_checkout.presentation.fragment.DigitalCartFragment
import com.tokopedia.user.session.UserSession
import java.lang.Boolean

/**
 * @author by jessica on 07/01/21
 */

class DigitalCartActivity : BaseSimpleActivity(), HasComponent<DigitalCheckoutComponent> {
    override fun getNewFragment(): Fragment {
        val uriData = intent.data
        var cartPassData: DigitalCheckoutPassData? = null
        var subParams: DigitalSubscriptionParams? = null

        uriData?.let { uri ->
            if (uri.scheme == DeeplinkConstant.SCHEME_INTERNAL) {
                cartPassData = intent.getParcelableExtra(DigitalExtraParam.EXTRA_PASS_DIGITAL_CART_DATA)
                subParams = intent.getParcelableExtra(DigitalExtraParam.EXTRA_PASS_DIGITAL_SUBSCRIPTION_DATA)
            } else if (uri.queryParameterNames.size > 0) {
                cartPassData = processIntentDataCheckoutFromApplink(applicationContext, uriData)
                subParams = processIntentDataSubscription(uriData)
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
        return passData
    }

    private fun processIntentDataSubscription(uriData: Uri): DigitalSubscriptionParams? {
        val subParams = DigitalSubscriptionParams()
        val showSubscribePopUpArg = uriData.getQueryParameter(DigitalSubscriptionParams.ARG_SHOW_SUBSCRIBE_POP_UP)
        val autoSubscribeArg = uriData.getQueryParameter(DigitalSubscriptionParams.ARG_AUTO_SUBSCRIBE)
        if (showSubscribePopUpArg != null) {
            subParams.showSubscribePopUp = Boolean.parseBoolean(showSubscribePopUpArg)
        }
        if (autoSubscribeArg != null) {
            subParams.autoSubscribe = Boolean.parseBoolean(autoSubscribeArg)
        }
        return subParams
    }

    private fun generateATokenRechargeCheckout(context: Context): String? {
        val timeMillis = System.currentTimeMillis().toString()
        val token = AuthHelper.getMD5Hash(timeMillis)
        val userSession = UserSession(context)
        return String.format(getString(R.string.digital_generate_token_checkout),
                userSession.userId, if (token.isEmpty()) timeMillis else token)
    }

    override fun getComponent(): DigitalCheckoutComponent {
        return DigitalCheckoutComponentInstance.getDigitalCheckoutComponent(application)
    }
}