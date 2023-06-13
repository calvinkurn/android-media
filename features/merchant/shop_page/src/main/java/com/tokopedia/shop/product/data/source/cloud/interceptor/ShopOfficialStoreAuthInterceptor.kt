package com.tokopedia.shop.product.data.source.cloud.interceptor

import android.content.Context
import com.tokopedia.network.NetworkRouter
import com.tokopedia.shop.common.data.interceptor.MojitoInterceptor
import com.tokopedia.user.session.UserSessionInterface

class ShopOfficialStoreAuthInterceptor(
    context: Context?,
    networkRouter: NetworkRouter?,
    userSessionInterface: UserSessionInterface?
) : MojitoInterceptor(context, networkRouter, userSessionInterface)
