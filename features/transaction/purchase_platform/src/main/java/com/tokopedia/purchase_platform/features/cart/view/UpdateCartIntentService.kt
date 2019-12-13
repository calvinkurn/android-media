package com.tokopedia.purchase_platform.features.cart.view

import android.content.Intent
import androidx.core.app.JobIntentService
import com.google.gson.Gson
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.utils.TKPDMapParam
import com.tokopedia.authentication.AuthHelper
import com.tokopedia.purchase_platform.features.cart.data.model.request.UpdateCartRequest
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.CartItemData
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.UpdateCartData
import com.tokopedia.purchase_platform.features.cart.domain.usecase.UpdateCartUseCase
import com.tokopedia.purchase_platform.features.cart.view.di.DaggerNewCartComponent
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface
import rx.Subscriber
import java.util.*
import javax.inject.Inject

/**
 * @author Irfan Khoirul on 31/07/18.
 */

class UpdateCartIntentService : JobIntentService() {

    @Inject
    lateinit var updateCartUseCase: UpdateCartUseCase
    @Inject
    lateinit var userSession: UserSessionInterface

    override fun onCreate() {
        super.onCreate()
        initInjector()
    }

    override fun onHandleWork(intent: Intent) {
        if (intent.hasExtra(EXTRA_CART_ITEM_DATA_LIST)) {
            val cartItemDataList = intent.getParcelableArrayListExtra<CartItemData>(EXTRA_CART_ITEM_DATA_LIST)
            val updateCartRequestList = ArrayList<UpdateCartRequest>()
            for ((originData, updatedData) in cartItemDataList) {
                val updateCartRequest = UpdateCartRequest()
                updateCartRequest.cartId = originData?.cartId ?: 0
                updateCartRequest.notes = updatedData?.remark
                updateCartRequest.quantity = updatedData?.quantity ?: 0
                updateCartRequestList.add(updateCartRequest)
            }
            val paramUpdate = TKPDMapParam<String, String>()
            paramUpdate[UpdateCartUseCase.PARAM_CARTS] = Gson().toJson(updateCartRequestList)

            val requestParams = RequestParams.create()
            requestParams.putObject(UpdateCartUseCase.PARAM_REQUEST_AUTH_MAP_STRING_UPDATE_CART,
                    AuthHelper.generateParamsNetwork(userSession.userId, userSession.deviceId, paramUpdate))

            updateCartUseCase.createObservable(requestParams)
                    .subscribe(object : Subscriber<UpdateCartData>() {
                        override fun onCompleted() {

                        }

                        override fun onError(e: Throwable) {
                            e.printStackTrace()
                        }

                        override fun onNext(updateCartData: UpdateCartData) {
                            // Expected to do nothing
                        }
                    })
        }
    }

    private fun initInjector() {
        val baseMainApplication = application as BaseMainApplication
        DaggerNewCartComponent.builder()
                .baseAppComponent(baseMainApplication.baseAppComponent)
                .build()
                .inject(this)
    }

    companion object {
        val EXTRA_CART_ITEM_DATA_LIST = "EXTRA_CART_ITEM_DATA_LIST"
    }

}
